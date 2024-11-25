package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.QnA;
import ezen.team.ezenbookstore.repository.QnARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QnAService {

    private final QnARepository qnARepository;

    public QnA findById(Long id) {
        return qnARepository.findById(id).orElse(null);
    }

    public List<QnA> findAll() {
        return qnARepository.findAll();
    }

    public QnA create(QnA qnA) {
        return qnARepository.save(qnA);
    }

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

    public void deleteById(Long id) {
        qnARepository.deleteById(id);
    }

    public List<QnA> findAllByUserId(Long userId) {
        return qnARepository.findAllByUserId(userId);
    }

    public Page<QnA> findAll(Pageable pageable) {
        return qnARepository.findAll(pageable);
    }

    public Page<QnA> findAllByUserId(Long userId, Pageable pageable) {
        return qnARepository.findAllByUserId(userId, pageable);
    }

    public Page<QnA> findAllByUserIdAndCategory(Long userId, Byte category, Pageable pageable) {
        return qnARepository.findAllByUserIdAndCategory(userId, category, pageable);
    }

    public Page<QnA> findByCategory(Byte category, Pageable pageable) {
        // 카테고리로 QnA를 검색하는 로직
        return qnARepository.findByCategory(category, pageable);
    }

    public boolean saveAnswer(Long id, String answer) {
        // 특정 QnA에 답변 저장
        QnA qna = findById(id); // QnA를 조회
        if (qna != null) {
            qna.setAnswer(answer); // 답변 설정
            qnARepository.save(qna); // 저장
            return true; // 성공적으로 저장된 경우
        }
        return false; // QnA를 찾지 못한 경우
    }

    public void bulkAnswer(List<Long> ids, String answer) {
        // 여러 QnA에 동일한 답변 저장
        List<QnA> qnaList = qnARepository.findAllById(ids); // ID 리스트로 QnA 조회
        for (QnA qna : qnaList) {
            qna.setAnswer(answer); // 답변 설정
        }
        qnARepository.saveAll(qnaList); // 모든 QnA 저장
    }
    public Page<QnA> findByAnswerStatus(String answer, Pageable pageable){
        return qnARepository.findByAnswer(answer, pageable);
    }
    public Page<QnA> findByCategoryAndAnswerStatus(byte category, String answer, Pageable pageable){
        return qnARepository.findByCategoryAndAnswer(category, answer, pageable);
    }
}
