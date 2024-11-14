package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Review;
import ezen.team.ezenbookstore.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review findById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Review create(Review review) {
        return reviewRepository.save(review);
    }

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

    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }

    public List<Review> findAllByUserId(Long userId) {
        return reviewRepository.findAllByUserId(userId);
    }

}
