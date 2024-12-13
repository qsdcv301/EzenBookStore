package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.QnA;
import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.repository.QnARepository;
import ezen.team.ezenbookstore.util.ParseUtils;
import jakarta.persistence.EntityNotFoundException;
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
    private TextFormatService textFormatService;

    @Override
    public QnA findById(Long id) {
        return qnARepository.findById(id).orElse(null);
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
            String formattedAnswer = textFormatService.formatText(answer);
            qna.setAnswer(formattedAnswer); // 답변 설정
            qnARepository.save(qna); // 저장
            return true; // 성공적으로 저장된 경우
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bulkAnswer(List<Long> ids, String answer) {
        // 여러 QnA에 동일한 답변 저장
        String formattedAnswer = textFormatService.formatText(answer);
        List<QnA> qnaList = qnARepository.findAllById(ids); // ID 리스트로 QnA 조회
        qnaList.forEach(qna -> qna.setAnswer(formattedAnswer));
        qnARepository.saveAll(qnaList); // 일괄 저장
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
        QnA existingQnA = qnARepository.findById(qnA.getId())
                .orElseThrow(() -> new EntityNotFoundException("QnA not found"));
        existingQnA.setUser(qnA.getUser());
        existingQnA.setCategory(qnA.getCategory());
        existingQnA.setTitle(qnA.getTitle());
        existingQnA.setQuestion(qnA.getQuestion());
        existingQnA.setAnswer(qnA.getAnswer());
        existingQnA.setCreateAt(qnA.getCreateAt());
        return qnARepository.save(existingQnA);
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
            if (qna == null) {
                response.put("success", "false");
                return response;
            }
            User user = userService.findById(qna.getUser().getId());
            String title = qna.getTitle();
            String question = qna.getQuestion();
            String answer = qna.getAnswer();
            String email = user.getEmail();
            String name = user.getName();
            String tel = user.getTel();
            String categoryName = switch (qna.getCategory()) {
                case 1 -> "회원 문의";
                case 2 -> "상품 문의";
                case 3 -> "배송 문의";
                case 4 -> "주문 문의";
                case 5 -> "취소/반품 문의";
                case 6 -> "기타 문의";
                default -> "기타 문의";
            };
            response.put("success", "true");
            response.put("question", question);
            response.put("answer", answer);
            response.put("email", email);
            response.put("name", name);
            response.put("category", categoryName);
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
            String formattedQuestion = textFormatService.formatText(qna.getQuestion());
            QnA newQnA = QnA.builder()
                    .user(user)
                    .category(qna.getCategory())
                    .title(qna.getTitle())
                    .question(formattedQuestion)
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
    public Long countByAnswerIsNullOrEmpty(){
        return qnARepository.countByAnswerIsNullOrEmpty();
    }

    @Override
    public Page<QnA> findByCategory(byte category, Pageable pageable) {
        return qnARepository.findByCategory(category, pageable);
    }

    @Override
    public Page<QnA> filterQnAList(String category, String status, Pageable pageable) {
        boolean isAllCategory = "all".equalsIgnoreCase(category);
        boolean isAllStatus = "all".equalsIgnoreCase(status);

        byte categoryByte = 1;
        if (!isAllCategory) {
            try {
                categoryByte = Byte.parseByte(category);
            } catch (NumberFormatException e) {
                categoryByte = 0;
            }
        }

        if (isAllCategory && isAllStatus) {
            return qnARepository.findAll(pageable);
        } else if (isAllCategory) {
            if ("answered".equalsIgnoreCase(status)) {
                return qnARepository.findByAnswerIsNotNullAndAnswerNotEmpty(pageable);
            } else if ("pending".equalsIgnoreCase(status)) {
                return qnARepository.findByAnswerIsNullOrAnswerIsEmpty(pageable);
            }
        } else {
            if ("all".equalsIgnoreCase(status)) {
                return qnARepository.findByCategory(categoryByte, pageable);
            } else if ("answered".equalsIgnoreCase(status)) {
                return qnARepository.findByCategoryAndAnswerIsNotNullAndAnswerNotEmpty(categoryByte, pageable);
            } else if ("pending".equalsIgnoreCase(status)) {
                return qnARepository.findByCategoryAndAnswerIsNullOrAnswerIsEmpty(categoryByte, pageable);
            }
        }

        return qnARepository.findAll(pageable);
    }
}
