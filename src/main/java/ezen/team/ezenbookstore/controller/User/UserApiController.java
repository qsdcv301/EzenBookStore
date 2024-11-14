package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final CartService cartService;
    private final PaymentService paymentService;
    private final QnAService qnAService;
    private final OrdersService ordersService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user) {
        userService.create(user);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    @GetMapping("/loginSuccess")
    public String getLoginInfo(@AuthenticationPrincipal CustomOAuth2User user, Model model) {
        String provider = user.getProvider();
        String email = user.getEmail();
        String name = user.getName();

        // 기존 사용자 검색
        User findUser = userService.findByEmail(email);

        // 사용자가 존재하지 않을 경우 새로운 사용자 생성
        if (findUser == null) {
            User newUser = User.builder()
                    .provider(provider)
                    .email(email)
                    .name(name)
                    .build();
            userService.create(newUser);
            findUser = newUser;
        }

        return "redirect:/book/search";
    }

    @PostMapping("/user/delete")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@RequestParam List<String> userId) {
        Map<String, Boolean> response = new HashMap<>();

        if (userId == null || userId.isEmpty()) {
            response.put("success", false);
            return ResponseEntity.ok(response); // 삭제 실패 반환
        }

        try {
            for (String userIdStr : userId) {
                Long userIdValue = Long.parseLong(userIdStr); // String을 Long으로 변환
                List<Review> reviewList = reviewService.findAllByUserId(userIdValue);
                for (Review review : reviewList) {
                    reviewService.deleteById(review.getId());
                }
                List<QnA> qnAList = qnAService.findAllByUserId(userIdValue);
                for (QnA qnA : qnAList) {
                    qnAService.deleteById(qnA.getId());
                }
                List<Cart> cartList = cartService.findAllByUserId(userIdValue);
                for (Cart cart : cartList) {
                    cartService.deleteById(cart.getId());
                }
                List<Payment> paymentList = paymentService.findAllByUserId(userIdValue);
                for (Payment payment : paymentList) {
                    paymentService.deleteById(payment.getId());
                }
                List<Orders> ordersList = ordersService.findAllByUserId(userIdValue);
                for (Orders orders : ordersList) {
                    ordersService.deleteById(orders.getId());
                }
                userService.deleteById(userIdValue); // 개별 User 삭제 처리
            }
            response.put("success", true);
            return ResponseEntity.ok(response); // 성공 시 200 OK와 함께 반환
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
        }
    }

    @PostMapping("/user/update")
    public ResponseEntity<Map<String, Boolean>> updateUser(@RequestParam List<String> userId,
                                                           @RequestParam List<String> password,
                                                           @RequestParam List<String> tel,
                                                           @RequestParam List<String> addr,
                                                           @RequestParam List<String> addrextra,
                                                           @RequestParam List<String> birthday,
                                                           @RequestParam List<String> point,
                                                           @RequestParam List<String> grade) {
        Map<String, Boolean> response = new HashMap<>();

        if (userId == null || userId.isEmpty()) {
            response.put("success", false);
            return ResponseEntity.ok(response); // 업데이트 실패 반환
        }

        try {
            for (int i = 0; i < userId.size(); i++) {
                Long userIdValue = Long.parseLong(userId.get(i)); // String을 Long으로 변환
                int birthdayValue = Integer.parseInt(birthday.get(i));
                int pointValue = Integer.parseInt(point.get(i));
                int gradeValue = Integer.parseInt(grade.get(i));
                User user = userService.findById(userIdValue);
                User newUser = null;
                if (password.get(i).isEmpty()) {
                    newUser = User.builder()
                            .id(userIdValue)
                            .provider(user.getProvider())
                            .email(user.getEmail())
                            .name(user.getName())
                            .tel(tel.get(i).isEmpty() ? null : tel.get(i))
                            .addr(addr.get(i).isEmpty() ? null : addr.get(i))
                            .addrextra(addrextra.get(i).isEmpty() ? null : addrextra.get(i))
                            .createdAt(user.getCreatedAt())
                            .birthday(birthday.get(i).isEmpty() ? null : birthdayValue)
                            .grade(grade.get(i).isEmpty() ? null : gradeValue)
                            .point(point.get(i).isEmpty() ? null : pointValue)
                            .build();
                } else {
                    newUser = User.builder()
                            .id(userIdValue)
                            .provider(user.getProvider())
                            .email(user.getEmail())
                            .name(user.getName())
                            .password(password.get(i))
                            .tel(tel.get(i).isEmpty() ? null : tel.get(i))
                            .addr(addr.get(i).isEmpty() ? null : addr.get(i))
                            .addrextra(addrextra.get(i).isEmpty() ? null : addrextra.get(i))
                            .createdAt(user.getCreatedAt())
                            .birthday(birthday.get(i).isEmpty() ? null : birthdayValue)
                            .grade(grade.get(i).isEmpty() ? null : gradeValue)
                            .point(point.get(i).isEmpty() ? null : pointValue)
                            .build();
                }
                userService.update(newUser);
            }

            response.put("success", true);
            return ResponseEntity.ok(response); // 성공 시 200 OK와 함께 반환
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
        }
    }

}
