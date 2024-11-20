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
import java.util.stream.Collectors;

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

        // 1-based 페이지를 0-based로 변환
        Pageable pageable = PageRequest.of(page - 1, size);

        // 필터링된 결과 가져오기
        Page<User> userPage;
        if (type != null && !type.isEmpty() && keyword != null && !keyword.isEmpty()) {
            userPage = userService.searchUsers(type, keyword, pageable);
        } else {
            userPage = userService.findAll(pageable);
        }

        // 검색 결과의 총 유저 수 (검색 조건에 따른 "전체")
        long totalCount = userPage.getTotalElements();

        // grade로 한번 더 필터링
        List<User> filteredUsers = userPage.getContent();
        List<User> gradefFilterUsers = filteredUsers;
        if (grade != null) {
            gradefFilterUsers = gradefFilterUsers.stream()
                    .filter(u -> u.getGrade().equals(grade))
                    .collect(Collectors.toList());
        }

        // 필터링 후 새로운 총 유저 수
        long filteredTotalCount = gradefFilterUsers.size();

        // 검색 결과 내 각 등급별 카운트
        long generalCount = filteredUsers.stream().filter(u -> u.getGrade() == 0).count();
        long silverCount = filteredUsers.stream().filter(u -> u.getGrade() == 1).count();
        long goldCount = filteredUsers.stream().filter(u -> u.getGrade() == 2).count();
        long vipCount = filteredUsers.stream().filter(u -> u.getGrade() == 3).count();
        long adminCount = filteredUsers.stream().filter(u -> u.getGrade() == 4).count();

        // 모델에 데이터 추가
        model.addAttribute("userList", gradefFilterUsers); // 필터링된 유저 리스트
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalCount", totalCount); // 검색 결과의 총 유저 수
        model.addAttribute("generalCount", generalCount); // 검색 결과 내 일반회원 수
        model.addAttribute("silverCount", silverCount); // 검색 결과 내 실버회원 수
        model.addAttribute("goldCount", goldCount); // 검색 결과 내 골드회원 수
        model.addAttribute("vipCount", vipCount); // 검색 결과 내 VIP 회원 수
        model.addAttribute("adminCount", adminCount); // 검색 결과 내 관리자 수
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
