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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService implements ReviewServiceInterface{

    private final ReviewRepository reviewRepository;
    private final FileUploadService fileUploadService;
    private final OrderItemService orderItemService;

    @Override
    public Review findById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Review create(Review review) {
        return reviewRepository.save(review);
    }

    @Override
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public List<Review> findAllByUserId(Long userId) {
        return reviewRepository.findAllByUserId(userId);
    }

    @Override
    public List<Review> findAllByBookId(Long bookId) {
        return reviewRepository.findAllByBookId(bookId);
    }

    @Override
    public Page<Review> findAll(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    @Override
    public List<Review> findAllByBookIdAndUserId(Long bookId, Long userId) {
        return reviewRepository.findAllByBookIdAndUserId(bookId, userId);
    }

    @Override
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

    @Override
    public Page<Review> searchByKeyword(String keyword, Pageable pageable) {
        return reviewRepository.findAllByTitleContaining(keyword, pageable);
    }

    @Override
    public Page<Review> findAllByUserName(String name, Pageable pageable) {
        return reviewRepository.findAllByUser_NameContaining(name, pageable);
    }

    @Override
    public Page<Review> findAllByBookTitle(String title, Pageable pageable) {
        return reviewRepository.findAllByBook_TitleContaining(title, pageable);
    }
    @Override
    public Page<Review> getAllReviewsWithImages(Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findAll(pageable);
        // Review 엔티티 각각에 imagePath 세팅
        reviewPage.getContent().forEach(review -> {
            String imagePath = fileUploadService.findImageFilePath(review.getId(), "review");
            if (imagePath == null) {
                imagePath = "/image/noImage.png";
            }
            review.setImagePath(imagePath);
        });
        return reviewPage;
    }

    @Override
    public Page<Review> searchReviews(String type, String keyword, Pageable pageable) {
        Page<Review> result;
        if ("book".equalsIgnoreCase(type)) {
            result = reviewRepository.findAllByBookTitle(keyword, pageable);
        } else if ("user".equalsIgnoreCase(type)) {
            result = reviewRepository.findAllByUserName(keyword, pageable);
        } else {
            // 잘못된 검색 타입인 경우 빈 페이지 반환
            return Page.empty();
        }

        // 검색된 Review에 imagePath 세팅
        result.getContent().forEach(review -> {
            String imagePath = fileUploadService.findImageFilePath(review.getId(), "review");
            if (imagePath == null) {
                imagePath = "/images/default.png";
            }
            review.setImagePath(imagePath);
        });

        return result;
    }

    @Override
    public Review getReviewDetail(Long id) {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isEmpty()) return null;

        // 단건 리뷰에도 imagePath 세팅
        String imagePath = fileUploadService.findImageFilePath(review.get().getId(), "review");
        if (imagePath == null) {
            imagePath = "/images/default.png";
        }
        review.get().setImagePath(imagePath);

        return review.orElse(null);
    }

    @Override
    public void deleteReviews(List<Long> ids) {
        for (Long id : ids) {
            reviewRepository.deleteById(id);
        }
    }

    @Override
    public int getStartPage(int currentPage, int pageGroupSize) {
        return Math.max(0, (currentPage / pageGroupSize) * pageGroupSize);
    }

    @Override
    public int getEndPage(int startPage, int totalPages, int pageGroupSize) {
        return Math.min(startPage + pageGroupSize, totalPages);
    }
}
