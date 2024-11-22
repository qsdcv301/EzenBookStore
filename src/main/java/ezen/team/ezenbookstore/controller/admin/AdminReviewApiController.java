package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.Review;
import ezen.team.ezenbookstore.service.BookService;
import ezen.team.ezenbookstore.service.ReviewService;
import ezen.team.ezenbookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/review")
public class AdminReviewApiController {

    private final ReviewService reviewService;
    @GetMapping("")
    public String getAllReviews(Model model) {
        List<Review> reviewList = reviewService.findAll();

        model.addAttribute("reviewList", reviewList);
        return "/admin/reviewControl";
    }

    // 특정 리뷰 삭제
    @PostMapping("/delete")
    public  ResponseEntity<String>deleteReview(@RequestBody List<Long> ids) {
        try{
            for(Long id : ids){
                reviewService.deleteById(id);
            }
            return ResponseEntity.ok("선택된 리뷰가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("responseentity:리뷰 삭제 실패");
        }
    }

    // 검색 기능
    @GetMapping("/search")
    public String searchReviews(
            @RequestParam String type,
            @RequestParam String keyword,
            Model model) {
        List<Review> result;
        if ("book".equalsIgnoreCase(type)) {
            // 책 제목 검색 로직 (예시로 bookId 사용)
            result = reviewService.findAllByBookId(Long.parseLong(keyword));
        } else if ("user".equalsIgnoreCase(type)) {
            // 사용자 검색 로직 (예시로 userId 사용)
            result = reviewService.findAllByUserId(Long.parseLong(keyword));
        } else {
            result = List.of(); // 잘못된 요청 시 빈 리스트 반환
        }

        model.addAttribute("reviewList", result);
        return "/admin/reviewControl"; // 검색 결과를 포함해 Thymeleaf로 렌더링
    }
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewDetail(@PathVariable Long id) {
        Review review = reviewService.findById(id);
        if (review != null) {
            return ResponseEntity.ok(review);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
