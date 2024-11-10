package ezen.team.ezenbookstore.controller.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class PaymentApiController {

    @GetMapping("/payment")
    public String index() {
        return "payment";
    }

}
