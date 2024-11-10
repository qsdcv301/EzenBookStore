package ezen.team.ezenbookstore.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/review")
public class AdminReviewApiController {

    // 리뷰 목록 조회
    @PostMapping("/list")
    public String getReviewList() {
        // 리뷰 목록 조회 로직
        return "redirect:/admin/review"; // 리뷰 목록 페이지로 리다이렉트
    }

    // 리뷰 상세 조회 (리뷰 내용 모달창으로 출력)
    @PostMapping("/detail")
    public String getReviewDetail(@RequestParam Long reviewId) {
        // 특정 리뷰 상세 조회 로직
        return "redirect:/admin/review"; // 모달창으로 상세 페이지 출력 (예시로 목록 페이지로 리다이렉트)
    }

    // 리뷰 삭제
    @PostMapping("/delete")
    public String deleteReview(@RequestParam Long reviewId) {
        // 리뷰 삭제 로직
        return "redirect:/admin/review"; // 삭제 처리 후 리뷰 목록 페이지로 리다이렉트
    }
}
