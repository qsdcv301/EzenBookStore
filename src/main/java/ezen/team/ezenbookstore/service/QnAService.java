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

}
