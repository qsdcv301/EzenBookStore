package ezen.team.ezenbookstore.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/payment")
public class AdminPaymentApiController {

    // 결제 내역 조회
    @PostMapping("/list")
    public String getPaymentList() {
        // 결제 내역 조회 로직
        return "redirect:/admin/payment"; // 결제 내역 페이지로 리다이렉트
    }

    // 결제 상세 조회
    @PostMapping("/detail")
    public String getPaymentDetail(@RequestParam Long paymentId) {
        // 특정 결제 내역 상세 조회 로직
        return "redirect:/admin/payment"; // 상세 페이지로 리다이렉트
    }

    // 결제 취소 처리
    @PostMapping("/cancel")
    public String cancelPayment(@RequestParam Long paymentId) {
        // 결제 취소 로직
        return "redirect:/admin/payment"; // 취소 처리 후 결제 내역 페이지로 리다이렉트
    }

    // 환불 처리
    @PostMapping("/refund")
    public String refundPayment(@RequestParam Long paymentId) {
        // 환불 처리 로직
        return "redirect:/admin/payment"; // 환불 처리 후 결제 내역 페이지로 리다이렉트
    }
}
