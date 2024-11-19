package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
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

    @ModelAttribute
    public void findUser(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object userData = auth.getPrincipal();
            if (userData instanceof User user) {
                model.addAttribute("user", user);
                model.addAttribute("userData", true);
            } else if (userData instanceof CustomOAuth2User customOAuth2User) {
                User customUser = userService.findByEmail(customOAuth2User.getEmail());
                model.addAttribute("user", customUser);
                model.addAttribute("userData", true);
            } else {
                model.addAttribute("userData", false);
            }
        } catch (Exception e) {
            model.addAttribute("userData", false);
        }
    }

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
                    .addr("주소 입력 필요")
                    .addrextra("상세 주소 입력 필요")
                    .tel("전화 번호 입력 필요")
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
    public ResponseEntity<Map<String, Boolean>> updateUser(@RequestParam(name = "userId", required = false) List<String> userId,
                                                           @RequestParam(name = "password", required = false) List<String> password,
                                                           @RequestParam(name = "tel", required = false) List<String> tel,
                                                           @RequestParam(name = "addr", required = false) List<String> addr,
                                                           @RequestParam(name = "addrextra", required = false) List<String> addrextra,
                                                           @RequestParam(name = "birthday", required = false) List<String> birthday,
                                                           @RequestParam(name = "point", required = false) List<String> point,
                                                           @RequestParam(name = "grade", required = false) List<String> grade) {
        Map<String, Boolean> response = new HashMap<>();

        if (userId == null || userId.isEmpty()) {
            response.put("success", false);
            return ResponseEntity.ok(response); // 업데이트 실패 반환
        }

        try {
            for (int i = 0; i < userId.size(); i++) {
                User user = userService.findById(Long.parseLong(userId.get(i)));
                User newUser = null;

                // 생일 문자열이 유효한지 확인
                Timestamp convertedTimestamp = null;
                if (birthday != null && birthday.get(i) != null && !birthday.get(i).isEmpty()) {
                    String birthdayTime = birthday.get(i) + " 00:00:00";
                    convertedTimestamp = Timestamp.valueOf(birthdayTime);
                }
                System.out.println(convertedTimestamp);
                if (password == null || password.get(i).isEmpty()) {
                    newUser = User.builder()
                            .id(Long.parseLong(userId.get(i)))
                            .provider(user.getProvider())
                            .email(user.getEmail())
                            .name(user.getName())
                            .password(user.getPassword())
                            .tel((tel == null || tel.get(i) == null || tel.get(i).isEmpty()) ? user.getTel() : tel.get(i))
                            .addr((addr == null || addr.get(i) == null || addr.get(i).isEmpty()) ? user.getAddr() : addr.get(i))
                            .addrextra((addrextra == null || addrextra.get(i) == null || addrextra.get(i).isEmpty()) ? user.getAddrextra() : addrextra.get(i))
                            .createdAt(user.getCreatedAt())
                            .birthday(convertedTimestamp == null ? user.getBirthday() : convertedTimestamp)
                            .grade((grade == null || grade.get(i) == null || grade.get(i).isEmpty()) ? user.getGrade() : Integer.parseInt(grade.get(i)))
                            .point((point == null || point.get(i) == null || point.get(i).isEmpty()) ? user.getPoint() : Integer.parseInt(point.get(i)))
                            .build();
                } else {
                    newUser = User.builder()
                            .id(Long.parseLong(userId.get(i)))
                            .provider(user.getProvider())
                            .email(user.getEmail())
                            .name(user.getName())
                            .password(password.get(i))
                            .tel((tel == null || tel.get(i) == null || tel.get(i).isEmpty()) ? user.getTel() : tel.get(i))
                            .addr((addr == null || addr.get(i) == null || addr.get(i).isEmpty()) ? user.getAddr() : addr.get(i))
                            .addrextra((addrextra == null || addrextra.get(i) == null || addrextra.get(i).isEmpty()) ? user.getAddrextra() : addrextra.get(i))
                            .createdAt(user.getCreatedAt())
                            .birthday(convertedTimestamp == null ? user.getBirthday() : convertedTimestamp)
                            .grade((grade == null || grade.get(i) == null || grade.get(i).isEmpty()) ? user.getGrade() : Integer.parseInt(grade.get(i)))
                            .point((point == null || point.get(i) == null || point.get(i).isEmpty()) ? user.getPoint() : Integer.parseInt(point.get(i)))
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

    @GetMapping("/info")
    public String infoUser(Model model) {
        User user = (User) model.getAttribute("user");
        return "info";
    }

}
