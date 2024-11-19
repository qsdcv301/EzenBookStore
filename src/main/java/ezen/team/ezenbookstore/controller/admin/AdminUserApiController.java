package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
            @RequestParam(value = "grade", required = false) Integer grade,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page, // 기본값 1
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        // 1-based 페이지를 0-based로 변환
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> userPage;
        long totalCount, generalCount, silverCount, goldCount, vipCount, adminCount;

        if (grade != null && (type != null && !type.isEmpty()) && (keyword != null && !keyword.isEmpty())) {
            // grade, type, keyword 모두 존재하는 경우
            userPage = userService.findByGradeAndSearch(type, keyword, grade, pageable);
            totalCount = userPage.getTotalElements();
        } else if (type != null && !type.isEmpty() && keyword != null && !keyword.isEmpty()) {
            // type과 keyword만 존재하는 경우
            userPage = userService.searchUsers(type, keyword, pageable);
            totalCount = userPage.getTotalElements();
        } else if (grade != null) {
            // grade만 존재하는 경우
            userPage = userService.findByGrade(grade, pageable);
            totalCount = userPage.getTotalElements();
        } else {
            // 기본 리스트 반환 (전체 항목)
            userPage = userService.findAll(pageable);
            totalCount = userPage.getTotalElements();
        }

        // 각 등급별 카운트 계산
        List<User> filteredUsers = userPage.getContent(); // 현재 페이지 데이터
        generalCount = filteredUsers.stream().filter(u -> u.getGrade() == 0).count();
        silverCount = filteredUsers.stream().filter(u -> u.getGrade() == 1).count();
        goldCount = filteredUsers.stream().filter(u -> u.getGrade() == 2).count();
        vipCount = filteredUsers.stream().filter(u -> u.getGrade() == 3).count();
        adminCount = filteredUsers.stream().filter(u -> u.getGrade() == 4).count();

        // 모델에 데이터 추가
        model.addAttribute("userList", userPage.getContent());
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("currentPage", page); // 1-based 페이지 번호 유지
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("generalCount", generalCount);
        model.addAttribute("silverCount", silverCount);
        model.addAttribute("goldCount", goldCount);
        model.addAttribute("vipCount", vipCount);
        model.addAttribute("adminCount", adminCount);
        model.addAttribute("grade", grade);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);

        return "admin/userControl";
    }





    // 회원 정보 삭제
    @PostMapping("/delete")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@RequestBody List<String> userId) {
        Map<String, Boolean> response = new HashMap<>();

        if (userId == null || userId.isEmpty()) {
            response.put("success", false);
            return ResponseEntity.ok(response); // 삭제 실패 반환
        }

        try {
            for (String userIdStr : userId) {
                Long userIdValue = Long.parseLong(userIdStr); // String을 Long으로 변환

                // 관련 데이터 삭제
                deleteUserRelatedData(userIdValue);

                // 사용자 삭제
                userService.deleteById(userIdValue);
            }

            response.put("success", true);
            return ResponseEntity.ok(response); // 성공 시 200 OK와 함께 반환
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
        }
    }


    // 사용자 관련 데이터를 삭제하는 메서드
    private void deleteUserRelatedData(Long userIdValue) {
        // 리뷰 삭제
        reviewService.findAllByUserId(userIdValue).forEach(review -> reviewService.deleteById(review.getId()));

        // QnA 삭제
        qnAService.findAllByUserId(userIdValue).forEach(qna -> qnAService.deleteById(qna.getId()));

        // 장바구니 삭제
        cartService.findAllByUserId(userIdValue).forEach(cart -> cartService.deleteById(cart.getId()));

        // 결제 내역 삭제
        paymentService.findAllByUserId(userIdValue).forEach(payment -> paymentService.deleteById(payment.getId()));

        // 주문 삭제
        ordersService.findAllByUserId(userIdValue).forEach(order -> ordersService.deleteById(order.getId()));
    }


    //회원정보 수정
    @PostMapping("/update")
    public ResponseEntity<Map<String, Boolean>> updateUserGrade(
            @RequestParam String userId, // 콤마로 구분된 ID 문자열
            @RequestParam Integer grade
    ) {
        Map<String, Boolean> response = new HashMap<>();
        try {
            String[] userIds = userId.split(","); // 콤마로 구분된 ID 분리
            for (String id : userIds) {
                User user = userService.findById(Long.parseLong(id));
                if (user != null) {
                    user.setGrade(grade);
                    userService.update(user);
                }
            }
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
