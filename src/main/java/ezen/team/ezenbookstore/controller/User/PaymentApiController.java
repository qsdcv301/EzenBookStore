package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/order/payment")
public class PaymentApiController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Map<String, Boolean>> requestPayment(@RequestParam(name = "paymentCode") String paymentCode,
                                                               @RequestParam(name = "userName") String userName,
                                                               @RequestParam(name = "addr") String addr,
                                                               @RequestParam(name = "addrextra") String addrextra,
                                                               @RequestParam(name = "tel") String tel,
                                                               @RequestParam(name = "amount") Long amount,
                                                               @RequestParam(name = "titleList") List<String> titleList,
                                                               @RequestParam(name = "quantityList") List<Integer> quantityList,
                                                               @RequestParam(name = "cartIdList", required = false) List<Long> cartIdList,
                                                               @ModelAttribute("user") User user) {
        Map<String, Boolean> response = new HashMap<>();

        try {
            if (titleList.size() != quantityList.size()) {
                response.put("success", false);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            boolean isPaymentSuccessful = paymentService.processPayment(user, paymentCode, userName, addr, addrextra, tel, amount, titleList, quantityList, cartIdList);

            if (isPaymentSuccessful) {
                response.put("success", true);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}