package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cart")
public class CartApiController {
    // 추가
    @PostMapping("/addCart")
    public String addCart(@ModelAttribute Cart cart) {
        // cart추가
        return "redirect:/cart";
    }
    // 업데이트
    @PostMapping("/updateCart")
    public String updateCart(@ModelAttribute Cart cart) {
        return "redirect:/cart";
    }
    // 삭제
    @PostMapping("/deleteCart")
    public String deleteCart(@RequestParam long cartId) {
        return "redirect:/cart";
    }
}
