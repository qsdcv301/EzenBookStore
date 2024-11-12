package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.QnA;
import ezen.team.ezenbookstore.repository.QnARepository;
import lombok.RequiredArgsConstructor;
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

    public void delete(Long id) {
        qnARepository.deleteById(id);
    }
    
}
