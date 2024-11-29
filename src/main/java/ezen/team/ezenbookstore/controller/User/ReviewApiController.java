package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/review")
public class ReviewApiController {

    private final ReviewService reviewService;

    @GetMapping
    public String viewReview() {
        return "/review";
    }

    // 리뷰 추가
    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addReview(@RequestParam(name = "title") String title,
                                                         @RequestParam(name = "comment") String comment,
                                                         @RequestParam(name = "rating") byte rating,
                                                         @RequestParam(name = "orderItemId") Long orderItemId,
                                                         @RequestParam(name = "file", required = false) MultipartFile file,
                                                         Model model) {
        Map<String, String> response = reviewService.addReview(title, comment, rating, orderItemId, file, model);
        if (response.get("success").equals("true")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}