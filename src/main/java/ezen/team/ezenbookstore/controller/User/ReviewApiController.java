package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class ReviewApiController {
    //리뷰 추가
    @PostMapping("/addReview")
    public String addReview(@ModelAttribute Review review) {
        //
        return "redirect:/review";
    }
    //업데이트 리뷰(리뷰 수정)
    @PostMapping("updateReview")
    public String updateReview(@ModelAttribute Review review) {
        return "redirect:/review";
    }
    //리뷰삭제
    @PostMapping("/deleteReview")
    public String deleteReview(@RequestParam long reviewId) {
        return "redirect:/review";
    }
}
