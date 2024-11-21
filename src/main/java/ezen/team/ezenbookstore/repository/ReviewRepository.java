package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByUserId(long userId);
    List<Review> findAllByBookId(long bookId);
    Page<Review> findAll(Pageable pageable);
    List<Review> findAllByBookIdAndUserId(long bookId, long userId);
}
