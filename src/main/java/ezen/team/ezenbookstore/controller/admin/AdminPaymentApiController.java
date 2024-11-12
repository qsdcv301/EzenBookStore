package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.Payment;
import ezen.team.ezenbookstore.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/payment")
public class AdminPaymentApiController {

    private final PaymentService paymentService;

    @GetMapping("")
    public String paymentControl(Model model) {
        List<Payment> paymentList = paymentService.findAll();

        model.addAttribute("paymentList", paymentList);

        return "/admin/paymentControl";
    }

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
