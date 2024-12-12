package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.QnA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface QnAServiceInterface {

    QnA findById(Long id);

    Page<QnA> findAll(Pageable pageable);

    boolean saveAnswer(Long id, String answer);

    void bulkAnswer(List<Long> ids, String answer);

    List<QnA> findAll();

    QnA create(QnA qnA);

    QnA update(QnA qnA);

    void deleteById(Long id);

    List<QnA> findAllByUserId(Long userId);

    Page<QnA> findAllByUserId(Long userId, Pageable pageable);

    Page<QnA> findAllByUserIdAndCategory(Long userId, Byte category, Pageable pageable);

    Map<String, String> findQnAById(String questionId);

    Map<String, Boolean> addQnA(QnA qna, String email, List<MultipartFile> files);

    Long countByAnswerIsNullOrEmpty();


    Page<QnA> findByCategory(byte category, Pageable pageable);

    Page<QnA> filterQnAList(String category, String status, Pageable pageable);

}
