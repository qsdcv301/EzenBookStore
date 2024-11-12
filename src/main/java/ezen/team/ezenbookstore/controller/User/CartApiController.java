package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.Cart;
import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.service.CartService;
import ezen.team.ezenbookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cart")
public class CartApiController {

    private final CartService cartService;

    private final UserService userService;

    @GetMapping
    public String cart(Model model) {
        String userEmail = userService.getUserEmail();
        User user = userService.findByEmail(userEmail);
        List<Cart> cartList = cartService.findAllByUserId(user.getId());
        model.addAttribute("cartList", cartList);
        return "cart";
    }

    // 추가
    @GetMapping("/cart")
    String viewCart(@ModelAttribute Cart cart) {
        return "cart";
    }

    @PostMapping("/add")
    public String addCart(@ModelAttribute Cart cart) {
        // cart추가
        return "redirect:/cart";
    }

    // 업데이트
    @PostMapping("/update")
    public String updateCart(@ModelAttribute Cart cart) {
        return "redirect:/cart";
    }

    // 삭제
    @PostMapping("/delete")
    public String deleteCart(@RequestParam long cartId) {

        return "redirect:/cart";
    }
}
