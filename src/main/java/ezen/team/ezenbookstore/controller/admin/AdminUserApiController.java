package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.*;
import lombok.RequiredArgsConstructor;
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
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        // 전체 사용자 리스트 가져오기 (검색 조건이 있는 경우와 없는 경우 모두 포함)
        List<User> userList = userService.fetchUserList(type, keyword);

        // grade로 필터링 (옵션)
        List<User> filteredUsers = userService.filterUsersByGrade(userList, grade);

        // 페이지네이션 처리
        List<User> paginatedUsers = userService.paginateUsers(filteredUsers, page, size);

        // 검색 결과 내 각 등급별 카운트 계산
        Map<String, Long> gradeCounts = userService.calculateGradeCounts(userList);

        // 총 페이지 수 계산
        int totalPages = userService.calculateTotalPages(filteredUsers.size(), size);

        // 모델에 데이터 추가
        model.addAttribute("userList", paginatedUsers); // 필터링된 유저 리스트 (페이지네이션 적용됨)
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalCount", userList.size()); // 필터링된 총 유저 수
        model.addAttribute("generalCount", gradeCounts.get("general")); // 검색 결과 내 일반회원 수
        model.addAttribute("silverCount", gradeCounts.get("silver")); // 검색 결과 내 실버회원 수
        model.addAttribute("goldCount", gradeCounts.get("gold")); // 검색 결과 내 골드회원 수
        model.addAttribute("vipCount", gradeCounts.get("vip")); // 검색 결과 내 VIP 회원 수
        model.addAttribute("adminCount", gradeCounts.get("admin")); // 검색 결과 내 관리자 수
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
        for (String userIdStr : userId) {
            Long userIdValue = Long.parseLong(userIdStr); // String을 Long으로 변환
            // 관련 데이터 삭제
            deleteUserRelatedData(userIdValue);
            // 사용자 삭제
            userService.deleteById(userIdValue);
        }
        response.put("success", true);
        return ResponseEntity.ok(response); // 성공 시 200 OK와 함께 반환

    }

    //회원정보 수정
    @PostMapping("/update")
    public ResponseEntity<Map<String, Boolean>> updateUserGrade(
            @RequestParam String userId, // 콤마로 구분된 ID 문자열
            @RequestParam Integer grade
    ) {
        Map<String, Boolean> response = new HashMap<>();
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
    }

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
}
