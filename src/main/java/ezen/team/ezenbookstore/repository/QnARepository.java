package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.QnA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnARepository extends JpaRepository<QnA, Long> {
    List<QnA> findAllByUserId(Long userId);

    Page<QnA> findAllByUserId(Long userId, Pageable pageable);

    Page<QnA> findAllByUserIdAndCategory(Long userId, Byte category, Pageable pageable);

    Page<QnA> findByCategory(Byte category, Pageable pageable);

    // 카테고리별 QnA 조회
    Page<QnA> findByCategory(byte category, Pageable pageable);

    // 답변이 있는 QnA 조회
    Page<QnA> findByCategoryAndAnswerIsNotNullAndAnswerNotEmpty(byte category, Pageable pageable);

    // 답변이 없는 QnA 조회
    Page<QnA> findByCategoryAndAnswerIsNullOrAnswerIsEmpty(byte category, Pageable pageable);

    // 모든 카테고리의 답변이 있는 QnA 조회
    Page<QnA> findByAnswerIsNotNullAndAnswerNotEmpty(Pageable pageable);

    // 모든 카테고리의 답변이 없는 QnA 조회
    Page<QnA> findByAnswerIsNullOrAnswerIsEmpty(Pageable pageable);

    @Query("SELECT COUNT(q) FROM QnA q WHERE q.answer IS NULL OR q.answer = ''")
    Long countByAnswerIsNullOrEmpty();

}
