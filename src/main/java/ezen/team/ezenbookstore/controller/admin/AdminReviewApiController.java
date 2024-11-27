package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.Review;
import ezen.team.ezenbookstore.service.FileUploadService;
import ezen.team.ezenbookstore.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    private final FileUploadService fileUploadService;

    @GetMapping("")
    public String getAllReviews(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {
        try {
            // 리뷰 데이터 페이징
            Page<Review> reviewPage;

            // 키워드가 있는 경우 검색 수행
            if (!keyword.isEmpty()) {
                reviewPage = reviewService.searchByKeyword(keyword, pageable);
            } else {
                // 키워드가 없는 경우 전체 리뷰 조회
                reviewPage = reviewService.findAll(pageable);
            }
            //이미지 카운트
            //        List<Integer> imageCounts= reviewPage.getContent().stream()
//                        .map(review -> fileUploadService.getImageCount(review.getId(),"review"))
//                        .collect(Collectors.toList());
            // 이미지 정보 추가
            List<String> imagePaths = reviewPage.getContent().stream()
                    .map(review -> fileUploadService.findImageFilePath(review.getId(), "review"))
                    .map(path -> path != null ? path : "/images/default.png")
                    .collect(Collectors.toList());

            // 페이징 관련 데이터 계산
            int totalPages = reviewPage.getTotalPages();
            int currentPage = reviewPage.getNumber();
            int pageGroupSize = 5;
            int startPage = Math.max(0, (currentPage / pageGroupSize) * pageGroupSize);
            int endPage = Math.min(startPage + pageGroupSize, totalPages);

            // 모델에 데이터 추가
            model.addAttribute("reviewPage", reviewPage);
            model.addAttribute("imagePaths", imagePaths);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("keyword", keyword);

            return "/admin/reviewControl";
        } catch (Exception e) {
            model.addAttribute("error", "리뷰 데이터를 불러오는 중 오류가 발생했습니다.");
            return "/admin/reviewControl";
        }
    }

    // 리뷰 삭제
    @PostMapping("/delete")
    public ResponseEntity<String> deleteReview(@RequestBody List<Long> ids) {
        try {
            for (Long id : ids) {
                reviewService.deleteById(id);
            }
            return ResponseEntity.ok("선택된 리뷰가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 삭제 실패: " + e.getMessage());
        }
    }

    // 리뷰 검색
    @GetMapping("/search")
    public String searchReviews(
            @RequestParam String type,
            @RequestParam String keyword,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {
        Page<Review> result;
        if ("book".equalsIgnoreCase(type)) {
            // 책 제목 검색 로직
            result = reviewService.findAllByBookTitle(keyword, pageable);
        } else if ("user".equalsIgnoreCase(type)) {
            // 사용자 검색 로직
            result = reviewService.findAllByUserName(keyword, pageable);
        } else {
            result = Page.empty(); // 잘못된 요청 시 빈 결과 반환
        }
        model.addAttribute("reviewPage", result);
        return "/admin/reviewControl"; // 검색 결과를 포함해 Thymeleaf로 렌더링
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getReviewDetail(@PathVariable Long id) {
        try {
            Review review = reviewService.findById(id);
            if (review == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // 리뷰 데이터를 Map으로 변환
            Map<String, Object> reviewData = Map.of(
                    "id", review.getId(),
                    "title", review.getTitle(),
                    "comment", review.getComment(),
                    "rating", review.getRating(),
                    "userId", review.getUser().getId(),
                    "bookId", review.getBook().getId(),
                    "imagePath", fileUploadService.findImageFilePath(review.getId(), "review")
            );

            return ResponseEntity.ok(reviewData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
