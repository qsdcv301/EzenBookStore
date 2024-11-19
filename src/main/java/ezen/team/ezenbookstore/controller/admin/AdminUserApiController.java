package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/user")
public class AdminUserApiController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final CartService cartService;
    private final PaymentService paymentService;
    private final QnAService qnAService;
    private final OrdersService ordersService;


    @GetMapping("")
    public String getUserList(
            @RequestParam(value = "page", defaultValue = "1") int page, // 기본값 1
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        // 1-based 페이지를 0-based로 변환
        Page<User> userPage = userService.findAll(PageRequest.of(page - 1, size));

        List<User> allUsers = userService.findAllUsers();
        long totalCount = allUsers.size();
        long generalCount = allUsers.stream().filter(u -> u.getGrade() == 0).count();
        long silverCount = allUsers.stream().filter(u -> u.getGrade() == 1).count();
        long goldCount = allUsers.stream().filter(u -> u.getGrade() == 2).count();
        long vipCount = allUsers.stream().filter(u -> u.getGrade() == 3).count();
        long adminCount = allUsers.stream().filter(u -> u.getGrade() == 4).count();

        model.addAttribute("userList", userPage.getContent());
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("currentPage", page); // 1-based 페이지 번호 유지
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("generalCount", generalCount);
        model.addAttribute("silverCount", silverCount);
        model.addAttribute("goldCount", goldCount);
        model.addAttribute("vipCount", vipCount);
        model.addAttribute("adminCount", adminCount);

        return "admin/userControl";
    }


    // 회원 목록 조회
    @PostMapping("/list")
    public String getUserList(@RequestParam(required = false) String memberLevel) {
        // 선택된 회원 등급에 따른 회원 목록 조회 로직
        return "redirect:/admin/user"; // 회원 목록 페이지로 리다이렉트
    }

    // 회원 검색
    @PostMapping("/search")
    public String searchUser(@RequestParam String keyword) {
        // 검색어를 기준으로 회원을 검색하는 로직
        return "redirect:/admin/user"; // 검색 결과 페이지로 리다이렉트
    }

    // 회원 상세 조회
    @PostMapping("/detail")
    public String getUserDetail(@RequestParam Long userId) {
        // 특정 회원 상세 조회 로직
        return "redirect:/admin/user"; // 상세 페이지로 리다이렉트
    }

    @GetMapping("/filter")
    public String filterByGrade(
            @RequestParam(value = "grade", required = false) Integer grade,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        Page<User> userPage;

        if (grade == null) { // 전체 보기
            userPage = userService.findAll(PageRequest.of(page - 1, size));
        } else { // 등급별 필터링
            userPage = userService.findByGrade(grade, PageRequest.of(page - 1, size));
        }

        model.addAttribute("userList", userPage.getContent());
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("grade", grade);

        return "admin/userControl";
    }


    // 회원 정보 삭제
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

    //회원정보 수정
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
}
