package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Review;
import ezen.team.ezenbookstore.entity.OrderItem;
import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final FileUploadService fileUploadService;
    private final OrderItemService orderItemService;

    public Review findById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public Review create(Review review) {
        return reviewRepository.save(review);
    }

    @Transactional(rollbackFor = Exception.class)
    public Review update(Long id, Review review) {
        Review newReview = Review.builder()
                .id(id)
                .book(review.getBook())
                .user(review.getUser())
                .rating(review.getRating())
                .title(review.getTitle())
                .comment(review.getComment())
                .createAt(review.getCreateAt())
                .build();
        return reviewRepository.save(newReview);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }

    public List<Review> findAllByUserId(Long userId) {
        return reviewRepository.findAllByUserId(userId);
    }

    public List<Review> findAllByBookId(Long bookId) {
        return reviewRepository.findAllByBookId(bookId);
    }

    // 모든 리뷰 조회 (페이지네이션)
    public Page<Review> findAll(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    public List<Review> findAllByBookIdAndUserId(Long bookId, Long userId) {
        return reviewRepository.findAllByBookIdAndUserId(bookId, userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> addReview(String title, String comment, byte rating, Long orderItemId, MultipartFile file, Model model) {
        Map<String, String> response = new HashMap<>();
        try {
            User user = (User) model.getAttribute("user");
            OrderItem orderItem = orderItemService.findById(orderItemId);
            Review newReview = Review.builder()
                    .title(title)
                    .comment(comment)
                    .rating(rating)
                    .book(orderItem.getBook())
                    .user(user)
                    .build();
            Review createReview = create(newReview);
            if (file != null && !file.isEmpty()) {
                fileUploadService.uploadFile(file, createReview.getId().toString(), "review");
            }
            byte status = 3;
            OrderItem newOrderItem = OrderItem.builder()
                    .id(orderItemId)
                    .book(orderItem.getBook())
                    .orders(orderItem.getOrders())
                    .quantity(orderItem.getQuantity())
                    .status(status)
                    .build();
            orderItemService.update(newOrderItem);
            response.put("success", "true");
        } catch (Exception e) {
            response.put("success", "false");
        }
        return response;
    }
    public Page<Review> searchByKeyword(String keyword, Pageable pageable) {
        return reviewRepository.findAllByTitleContaining(keyword, pageable);
    }

    public Page<Review> findAllByUserName(String name, Pageable pageable) {
        return reviewRepository.findAllByUser_NameContaining(name, pageable);
    }

    public Page<Review> findAllByBookTitle(String title, Pageable pageable) {
        return reviewRepository.findAllByBook_TitleContaining(title, pageable);
    }
}
