package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.Orders;
import jakarta.persistence.criteria.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/order")
public class OrderApiController {
    //order 확인창
    @GetMapping("/order")
    public String viewOrder(@ModelAttribute("order") Orders order) {
        //
        return "order";
    }

    //order 생성
    @PostMapping("/add")
    public String addOrder(@ModelAttribute Orders order) {
        //로직추가
        return "redirect:/order";
    }
    //오더 업데이트
    @PostMapping("/update")
    public String updateOrder(@ModelAttribute Orders order) {
        //로직추가
        return "redirect:/order";
    }
    //오더삭제
    @PostMapping("/delete")
    public String deleteOrder(@RequestParam long orderId) {
        //로직추가
        return "redirect:/order";
    }
}