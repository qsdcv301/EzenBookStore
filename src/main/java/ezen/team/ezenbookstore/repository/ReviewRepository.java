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
    Page<Review> findAllByTitleContaining(String keyword, Pageable pageable);
    Page<Review> findAllByBook_TitleContaining(String title, Pageable pageable); // 책 제목으로 검색
    Page<Review> findAllByUser_NameContaining(String name, Pageable pageable); // 사용자 이름으로 검색
}
