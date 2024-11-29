package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cart")
public class CartApiController {

    private final CartService cartService;

    @GetMapping
    public String cart(Model model, @ModelAttribute("user") User user) {
        cartService.populateCartModel(user, model);
        return "cart";
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Boolean>> addCart(@RequestParam List<String> bookId,
                                                        @RequestParam List<String> quantity,
                                                        @ModelAttribute("user") User user) {
        cartService.addItemsToCart(user, bookId, quantity);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Boolean>> updateCart(@RequestParam List<String> cartId,
                                                           @RequestParam List<String> quantity,
                                                           @ModelAttribute("user") User user) {
        cartService.updateCartItems(user, cartId, quantity);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, Boolean>> deleteCart(@RequestParam List<String> cartId,
                                                           @ModelAttribute("user") User user) {
        cartService.deleteCartItems(user, cartId);
        return ResponseEntity.ok(Map.of("success", true));
    }

}