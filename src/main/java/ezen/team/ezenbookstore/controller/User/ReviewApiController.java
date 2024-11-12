package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class ReviewApiController {
    @GetMapping("/review")public String viewReview(){return "review";}
    //리뷰 추가
    @PostMapping("/add")
    public String addReview(@ModelAttribute Review review) {
        //
        return "redirect:/review";
    }
    //업데이트 리뷰(리뷰 수정)
    @PostMapping("update")
    public String updateReview(@ModelAttribute Review review) {
        return "redirect:/review";
    }
    //리뷰삭제
    @PostMapping("/delete")
    public String deleteReview(@RequestParam long reviewId) {
        return "redirect:/review";
    }
}
