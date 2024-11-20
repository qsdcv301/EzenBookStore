package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("review")
public class ReviewApiController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final BookService bookService;
    private final FileUploadService fileUploadService;
    private final OrderItemService orderItemService;

    @ModelAttribute
    public void findUser(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object userData = auth.getPrincipal();
            if (userData instanceof User user) {
                model.addAttribute("user", user);
                model.addAttribute("userData", true);
                model.addAttribute("mypage", true);
            } else if (userData instanceof CustomOAuth2User customOAuth2User) {
                User customUser = userService.findByEmail(customOAuth2User.getEmail());
                model.addAttribute("user", customUser);
                model.addAttribute("userData", true);
                model.addAttribute("mypage", true);
            } else {
                model.addAttribute("userData", false);
            }
        } catch (Exception e) {
            model.addAttribute("userData", false);
        }
    }

    @GetMapping
    public String viewReview() {
        return "/review";
    }

    //리뷰 추가
    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addReview(@RequestParam(name = "title") String title,
                                                         @RequestParam(name = "comment") String comment,
                                                         @RequestParam(name = "rating") byte rating,
                                                         @RequestParam(name = "orderItemId") Long orderItemId,
                                                         @RequestParam(name = "file") MultipartFile file,
                                                         Model model) {
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
            Review createReview = reviewService.create(newReview);
            fileUploadService.uploadFile(file, createReview.getId().toString(), "review");
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
            return ResponseEntity.ok(response); // 성공 시 200 OK와 함께 반환
        } catch (Exception e) {
            response.put("success", "false");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
        }
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
