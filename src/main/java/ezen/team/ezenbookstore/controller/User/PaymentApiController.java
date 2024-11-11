package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
//@RequestMapping("/order/payment??")
public class PaymentApiController {

    @GetMapping("/payment")
    public String index() {
        return "payment";
    }


    //결제 완료
    @PostMapping("/order/payment")
    public String completePayment(@ModelAttribute Payment payment) {
        //결제 완료 와 오류 처리
        return "redirect:/payment";
    }

    //결제 취소
    @PostMapping("order/payment")
    public String deletePayment(@RequestParam long paymentId) {
        return "redirect:/payment";
    }

}
