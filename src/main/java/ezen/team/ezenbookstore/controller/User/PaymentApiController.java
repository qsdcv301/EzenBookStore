package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/payment")
public class PaymentApiController {

    @GetMapping("/payment")
    public String viewPayment() {
        return "payment";
    }


    //결제 완료
    @PostMapping("/add")
    public String addPayment(@ModelAttribute Payment payment) {
        //결제 완료 와 오류 처리
        return "redirect:/payment";
    }

    //결제 취소
    @PostMapping("/delete")
    public String deletePayment(@RequestParam long paymentId) {
        return "redirect:/payment";
    }

}
