package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserApiController {

    private static final String VERIFICATION_CODE_SESSION_KEY = "verificationCode";

    private final UserService userService;
    private final ReviewService reviewService;
    private final CartService cartService;
    private final PaymentService paymentService;
    private final QnAService qnAService;
    private final OrdersService ordersService;
    private final EmailService emailService;

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user) {
        userService.create(user);
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

        return "redirect:/book";
    }

    @PostMapping("/findId")
    public ResponseEntity<Map<String, String>> findIdUser(@ModelAttribute User user) {
        Map<String, String> response = new HashMap<>();
        if (user == null) {
            response.put("success", "false");
            return ResponseEntity.ok(response); // 검색 실패 반환
        }
        try {
            User findUser = userService.findByNameAndTel(user.getName(), user.getTel());
            String email = findUser.getEmail();
            String provider = findUser.getProvider();
            response.put("success", "true");
            response.put("email", email);
            response.put("provider", provider);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", "false");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
        }
    }

    @PostMapping("/findPw")
    public ResponseEntity<Map<String, String>> findPwUser(@ModelAttribute User user,
                                                          @RequestParam String verificationCode,
                                                          HttpSession session) {
        Map<String, String> response = new HashMap<>();
        String storedCode = (String) session.getAttribute(VERIFICATION_CODE_SESSION_KEY);

        if (storedCode != null && storedCode.equals(verificationCode) && user != null) {
            try {
                User findUser = userService.findByEmailAndTel(user.getEmail(), user.getTel());
                if (!findUser.getProvider().equals("ezen")) {
                    response.put("success", "false");
                    response.put("error", "간편 로그인 회원입니다.");
                    return ResponseEntity.ok(response);
                } else {
                    response.put("success", "true");
                    return ResponseEntity.ok(response);
                }
            } catch (Exception e) {
                response.put("success", "false");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
            }
        } else {
            response.put("error", "이메일, 전화번호 혹은 인증번호가 정확하지 않습니다.");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/newPw")
    public ResponseEntity<Map<String, Boolean>> newPwUser(@ModelAttribute User user) {
        Map<String, Boolean> response = new HashMap<>();

        try {
            User findUser = userService.findByEmail(user.getEmail());
            User newUser = User.builder()
                    .id(findUser.getId())
                    .provider(findUser.getProvider())
                    .email(findUser.getEmail())
                    .name(user.getName())
                    .password(user.getPassword())
                    .tel(findUser.getTel())
                    .addr(findUser.getAddr())
                    .addrextra(findUser.getAddrextra())
                    .createdAt(findUser.getCreatedAt())
                    .birthday(findUser.getBirthday())
                    .grade(findUser.getGrade())
                    .point(findUser.getPoint())
                    .build();
            userService.updatePass(newUser);
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
        }
    }

    @PostMapping("/delete")
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

    @PostMapping("/update")
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
                            .tel(tel.get(i).isEmpty() ? user.getTel() : tel.get(i))
                            .addr(addr.get(i).isEmpty() ? user.getAddr() : addr.get(i))
                            .addrextra(addrextra.get(i).isEmpty() ? user.getAddrextra() : addrextra.get(i))
                            .createdAt(user.getCreatedAt())
                            .birthday(birthday.get(i).isEmpty() ? user.getBirthday() : birthdayValue)
                            .grade(grade.get(i).isEmpty() ? user.getGrade() : gradeValue)
                            .point(point.get(i).isEmpty() ? user.getPoint() : pointValue)
                            .build();
                } else {
                    newUser = User.builder()
                            .id(userIdValue)
                            .provider(user.getProvider())
                            .email(user.getEmail())
                            .name(user.getName())
                            .password(password.get(i))
                            .tel(tel.get(i).isEmpty() ? user.getTel() : tel.get(i))
                            .addr(addr.get(i).isEmpty() ? user.getAddr() : addr.get(i))
                            .addrextra(addrextra.get(i).isEmpty() ? user.getAddrextra() : addrextra.get(i))
                            .createdAt(user.getCreatedAt())
                            .birthday(birthday.get(i).isEmpty() ? user.getBirthday() : birthdayValue)
                            .grade(grade.get(i).isEmpty() ? user.getGrade() : gradeValue)
                            .point(point.get(i).isEmpty() ? user.getPoint() : pointValue)
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

    @PostMapping("/emailAuthentication")
    public ResponseEntity<Map<String, Boolean>> emailAuthentication(@RequestParam("email") String email, HttpSession session) {
        // 무작위 6자리 번호 생성
        String verificationCode = generateVerificationCode();
        session.setAttribute(VERIFICATION_CODE_SESSION_KEY, verificationCode);
        // 이메일 발송
        emailService.sendEmail(email, "EzBookStore 이메일 인증", "귀하의 인증 번호는: " + verificationCode);

        Map<String, Boolean> response = new HashMap<>();
        boolean isEmail = true;
        response.put("isEmail", isEmail);
        return ResponseEntity.ok(response);
    }

    // 무작위 6자리 인증 번호 생성
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(999999); // 0부터 999999까지 무작위 숫자 생성
        return String.format("%06d", code); // 6자리로 포맷
    }

}
