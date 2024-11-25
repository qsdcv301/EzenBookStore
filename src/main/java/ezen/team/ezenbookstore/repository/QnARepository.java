package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.QnA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnARepository extends JpaRepository<QnA, Long> {
    List<QnA> findAllByUserId(Long userId);

    Page<QnA> findAllByUserId(Long userId, Pageable pageable);

    Page<QnA> findAllByUserIdAndCategory(Long userId, Byte category, Pageable pageable);

    Page<QnA> findByCategory(Byte category, Pageable pageable);

    Page<QnA> findByAnswer(String answer, Pageable pageable);

    Page<QnA> findByCategoryAndAnswer(Byte category, String answer, Pageable pageable);

}
