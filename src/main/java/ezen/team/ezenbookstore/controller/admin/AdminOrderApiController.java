package ezen.team.ezenbookstore.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/order")
public class AdminOrderApiController {

    // 주문 목록 조회
    @PostMapping("/list")
    public String getOrderList() {
        // 주문 목록 조회 로직
        return "redirect:/admin/order"; // 주문 목록 페이지로 리다이렉트
    }

    // 주문 상태 조회
    @PostMapping("/detail")
    public String getOrderDetail(@RequestParam Long orderId) {
        // 특정 주문 상세 조회 로직
        return "redirect:/admin/order"; // 상세 페이지로 리다이렉트 (예시로 목록 페이지로 설정)
    }

    // 주문 상태 업데이트
    @PostMapping("/updateStatus")
    public String updateOrderStatus(@RequestParam Long orderId, @RequestParam String status) {
        // 주문 상태 업데이트 로직
        return "redirect:/admin/order"; // 상태 업데이트 후 목록 페이지로 리다이렉트
    }

    // 취소/반품/교환 요청 목록 조회
    @PostMapping("/request/list")
    public String getRequestList() {
        // 취소/반품/교환 요청 목록 조회 로직
        return "redirect:/admin/order"; // 요청 목록 페이지로 리다이렉트
    }

    // 취소/반품/교환 요청 상세 조회
    @PostMapping("/request/detail")
    public String getRequestDetail(@RequestParam Long requestId) {
        // 특정 요청 상세 조회 로직
        return "redirect:/admin/order"; // 상세 페이지로 리다이렉트
    }

    // 취소/반품/교환 요청 상태 업데이트
    @PostMapping("/request/updateStatus")
    public String updateRequestStatus(@RequestParam Long requestId, @RequestParam String status) {
        // 요청 상태 업데이트 로직
        return "redirect:/admin/order"; // 상태 업데이트 후 목록 페이지로 리다이렉트
    }
}