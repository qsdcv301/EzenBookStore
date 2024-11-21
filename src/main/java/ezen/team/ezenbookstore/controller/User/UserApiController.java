package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

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
            return "redirect:/user/info?first=true";
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
    public String infoUser(@RequestParam(name = "keyword", defaultValue = "", required = false) String keyword,
                           @RequestParam(name = "dateRange", defaultValue = "", required = false) String dateRange,
                           @RequestParam(name = "deliveryStatus", defaultValue = "", required = false) String deliveryStatusParam,
                           @RequestParam(name = "orderStatus", defaultValue = "", required = false) String orderStatusParam,
                           @RequestParam(name = "oPage", defaultValue = "0", required = false) int oPage,
                           @RequestParam(name = "sort", defaultValue = "0", required = false) byte sort,
                           @RequestParam(name = "direction", defaultValue = "desc", required = false) String direction,
                           @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                           @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                           @RequestParam(name = "qPage", defaultValue = "0", required = false) int qPage,
                           Model model) {

        User user = (User) model.getAttribute("user");
        int size = 10;
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable qPageable = PageRequest.of(qPage, size, Sort.by(sortDirection, "id"));
        Page<QnA> qPaging;
        if (sort == 0) {
            qPaging = qnAService.findAllByUserId(user.getId(), qPageable);
        } else {
            qPaging = qnAService.findAllByUserIdAndCategory(user.getId(), sort, qPageable);
        }

        // 배송 상태 및 주문 상태 값 변환
        Byte deliveryStatus = null;
        if (deliveryStatusParam != null && !deliveryStatusParam.isEmpty()) {
            switch (deliveryStatusParam) {
                case "preparing":
                    deliveryStatus = 1;
                    break;
                case "shipping":
                    deliveryStatus = 2;
                    break;
                case "delivered":
                    deliveryStatus = 3;
                    break;
            }
        }

        Byte orderStatus = null;
        if (orderStatusParam != null && !orderStatusParam.isEmpty()) {
            switch (orderStatusParam) {
                case "cancelled":
                    orderStatus = 1;
                    break;
                case "exchange":
                    orderStatus = 2;
                    break;
                case "completed":
                    orderStatus = 3;
                    break;
            }
        }

        // 날짜 범위 설정
        if (dateRange != null && !dateRange.isEmpty()) {
            int monthsAgo = Integer.parseInt(dateRange);
            endDate = LocalDate.now();
            startDate = endDate.minusMonths(monthsAgo);
        }

        // 주문 필터링 처리
        List<Orders> filteredOrders = ordersService.filterOrders(startDate, endDate, deliveryStatus, orderStatus, keyword, user.getId());

        // 페이징 적용
        Pageable oPageable = PageRequest.of(oPage, size, Sort.by(sortDirection, "orderDate"));
        int start = Math.min((int) oPageable.getOffset(), filteredOrders.size());
        int end = Math.min((start + oPageable.getPageSize()), filteredOrders.size());
        List<Orders> pagedOrders = filteredOrders.subList(start, end);
        Page<Orders> orderPage = new PageImpl<>(pagedOrders, oPageable, filteredOrders.size());

        model.addAttribute("orderList", orderPage.getContent());
        model.addAttribute("orderPage", orderPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("dateRange", dateRange);
        model.addAttribute("deliveryStatus", deliveryStatusParam);
        model.addAttribute("orderStatus", orderStatusParam);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);

        model.addAttribute("questionList", qPaging.getContent());
        model.addAttribute("qnaPage", qPaging);
        return "info";
    }

}
