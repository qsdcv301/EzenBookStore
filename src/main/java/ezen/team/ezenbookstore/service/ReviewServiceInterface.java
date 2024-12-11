package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ReviewServiceInterface {

    Review findById(Long id);

    List<Review> findAll();

    Review create(Review review);

    Review update(Long id, Review review);

    void deleteById(Long id);

    List<Review> findAllByUserId(Long userId);

    List<Review> findAllByBookId(Long bookId);

    Page<Review> findAll(Pageable pageable);

    List<Review> findAllByBookIdAndUserId(Long bookId, Long userId);

    Map<String, String> addReview(String title, String comment, byte rating, Long orderItemId, MultipartFile file, Model model);

    Page<Review> searchByKeyword(String keyword, Pageable pageable);

    Page<Review> findAllByUserName(String name, Pageable pageable);

    Page<Review> findAllByBookTitle(String title, Pageable pageable);
    Page<Review> getAllReviewsWithImages(Pageable pageable);
    Page<Review> searchReviews(String type, String keyword, Pageable pageable);
    Review getReviewDetail(Long id);
    void deleteReviews(List<Long> ids);
    int getStartPage(int currentPage, int pageGroupSize);
    int getEndPage(int startPage, int totalPages, int pageGroupSize);

}
