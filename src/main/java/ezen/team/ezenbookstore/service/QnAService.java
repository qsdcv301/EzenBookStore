package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.QnA;
import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.repository.QnARepository;
import ezen.team.ezenbookstore.util.ParseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QnAService implements QnAServiceInterface{

    private final QnARepository qnARepository;
    private final UserService userService;
    private final FileUploadService fileUploadService;

    @Override
    public QnA findById(Long id) {
        return qnARepository.findById(id).orElse(null);
    }

    @Override
    public Page<QnA> findByCategory(Byte category, Pageable pageable) {
        return qnARepository.findByCategory(category, pageable);
    }

    @Override
    public Page<QnA> findAll(Pageable pageable) {
        return qnARepository.findAll(pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveAnswer(Long id, String answer) {
        // 특정 QnA에 답변 저장
        QnA qna = findById(id); // QnA를 조회
        if (qna != null) {
            qna.setAnswer(answer); // 답변 설정
            qnARepository.save(qna); // 저장
            return true; // 성공적으로 저장된 경우
        }
        return false;
    }

    @Override
    public void bulkAnswer(List<Long> ids, String answer) {
        // 여러 QnA에 동일한 답변 저장
        List<QnA> qnaList = qnARepository.findAllById(ids); // ID 리스트로 QnA 조회
        for (QnA qna : qnaList) {
            qna.setAnswer(answer); // 답변 설정
        }
    }

    @Override
    public List<QnA> findAll() {
        return qnARepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QnA create(QnA qnA) {
        return qnARepository.save(qnA);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QnA update(QnA qnA) {
        QnA newQnA = QnA.builder()
                .id(qnA.getId())
                .user(qnA.getUser())
                .category(qnA.getCategory())
                .title(qnA.getTitle())
                .question(qnA.getQuestion())
                .answer(qnA.getAnswer())
                .createAt(qnA.getCreateAt())
                .build();
        return qnARepository.save(newQnA);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        qnARepository.deleteById(id);
    }

    @Override
    public List<QnA> findAllByUserId(Long userId) {
        return qnARepository.findAllByUserId(userId);
    }

    @Override
    public Page<QnA> findAllByUserId(Long userId, Pageable pageable) {
        return qnARepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Page<QnA> findAllByUserIdAndCategory(Long userId, Byte category, Pageable pageable) {
        return qnARepository.findAllByUserIdAndCategory(userId, category, pageable);
    }

    @Override
    public Map<String, String> findQnAById(String questionId) {
        Map<String, String> response = new HashMap<>();
        try {
            Long QnAId = ParseUtils.parseLong(questionId);
            QnA qna = findById(QnAId);
            User user = userService.findById(qna.getUser().getId());
            String title = qna.getTitle();
            String question = qna.getQuestion();
            String answer = qna.getAnswer();
            String email = user.getEmail();
            String name = user.getName();
            String tel = user.getTel();
            String category = switch (qna.getCategory()) {
                case 1 -> "주문/결제";
                case 2 -> "배송";
                case 3 -> "반품/교환";
                case 4 -> "상품문의";
                case 5 -> "기타";
                default -> "기타";
            };
            response.put("success", "true");
            response.put("question", question);
            response.put("answer", answer);
            response.put("email", email);
            response.put("name", name);
            response.put("category", category);
            response.put("title", title);
            response.put("tel", tel);
            String imagePath = fileUploadService.findImageFilePath(QnAId, "qna");
            if (imagePath != null) {
                response.put("imagePath", imagePath);
            }
        } catch (Exception e) {
            response.put("success", "false");
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Boolean> addQnA(QnA qna, String email, List<MultipartFile> files) {
        Map<String, Boolean> response = new HashMap<>();
        try {
            User user = userService.findByEmail(email);
            QnA newQnA = QnA.builder()
                    .user(user)
                    .category(qna.getCategory())
                    .title(qna.getTitle())
                    .question(qna.getQuestion())
                    .build();
            QnA addQnA = create(newQnA);
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    fileUploadService.uploadFile(file, addQnA.getId().toString(), "qna");
                }
            }
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
        }
        return response;
    }

    @Override
    public Long qnACount(){
        return qnARepository.count();
    }

}