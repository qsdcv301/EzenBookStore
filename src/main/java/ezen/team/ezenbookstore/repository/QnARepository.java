package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.QnA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnARepository extends JpaRepository<QnA, Long> {
    List<QnA> findAllByUserId(Long userId);

    Page<QnA> findAllByUserId(Long userId, Pageable pageable);

    Page<QnA> findAllByUserIdAndCategory(Long userId, Byte category, Pageable pageable);

    @Query("SELECT q FROM QnA q WHERE q.answer IS NOT NULL AND q.answer <> ''")
    Page<QnA> findByAnswerIsNotNullAndAnswerNotEmpty(Pageable pageable);

    @Query("SELECT q FROM QnA q WHERE q.answer IS NULL OR q.answer = ''")
    Page<QnA> findByAnswerIsNullOrAnswerIsEmpty(Pageable pageable);

    Page<QnA> findByCategory(byte category, Pageable pageable);

    @Query("SELECT q FROM QnA q WHERE q.category = :category AND q.answer IS NOT NULL AND q.answer <> ''")
    Page<QnA> findByCategoryAndAnswerIsNotNullAndAnswerNotEmpty(@Param("category") byte category, Pageable pageable);

    @Query("SELECT q FROM QnA q WHERE q.category = :category AND (q.answer IS NULL OR q.answer = '')")
    Page<QnA> findByCategoryAndAnswerIsNullOrAnswerIsEmpty(@Param("category") byte category, Pageable pageable);

    @Query("SELECT COUNT(q) FROM QnA q WHERE q.answer IS NULL OR q.answer = ''")
    Long countByAnswerIsNullOrEmpty();

}
