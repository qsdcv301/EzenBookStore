package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.Cart;
import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.service.BookService;
import ezen.team.ezenbookstore.service.CartService;
import ezen.team.ezenbookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cart")
public class CartApiController {

    private final CartService cartService;
    private final UserService userService;
    private final BookService bookService;

    @GetMapping
    public String cart(Model model) {
        String userEmail = userService.getUserEmail();
        User user = userService.findByEmail(userEmail);
        List<Cart> cartList = cartService.findAllByUserId(user.getId());
        model.addAttribute("cartList", cartList);
        return "cart";
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Boolean>> addCart(@ModelAttribute Cart cart,
                                                        @RequestParam(name = "bookId") Long bookId) {
        Map<String, Boolean> response = new HashMap<>();
        try {
            // 현재 사용자 이메일을 가져와 User 객체 조회
            String userEmail = userService.getUserEmail();
            User user = userService.findByEmail(userEmail);

            // 새로운 Cart 객체 생성
            Cart newCart = Cart.builder()
                    .user(user)
                    .book(bookService.findById(bookId))
                    .quantity(cart.getQuantity())
                    .build();

            // Cart 저장 처리
            cartService.create(newCart);

            response.put("success", true);
            return ResponseEntity.ok(response); // 성공 시 200 OK와 함께 반환
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
        }
    }

    // 업데이트
    @PostMapping("/update")
    public ResponseEntity<Map<String, Boolean>> updateCart(@ModelAttribute Cart cart,
                                                           @RequestParam(name = "bookId") Long bookId) {
        Map<String, Boolean> response = new HashMap<>();
        try {
            // 현재 사용자 이메일을 가져와 User 객체 조회
            String userEmail = userService.getUserEmail();
            User user = userService.findByEmail(userEmail);

            // 새로운 Cart 객체 생성
            Cart newCart = Cart.builder()
                    .id(cart.getId())
                    .user(user)
                    .book(bookService.findById(bookId))
                    .quantity(cart.getQuantity())
                    .build();

            // Cart 저장 처리
            cartService.update(cart.getId(), newCart);

            response.put("success", true);
            return ResponseEntity.ok(response); // 성공 시 200 OK와 함께 반환
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
        }
    }

    // 삭제
    @PostMapping("/delete")
    public ResponseEntity<Map<String, Boolean>> deleteCart(@RequestParam Map<String, List<String>> request) {
        Map<String, Boolean> response = new HashMap<>();
        List<String> cartIds = request.get("cartIds");

        if (cartIds == null || cartIds.isEmpty()) {
            response.put("success", false);
            return ResponseEntity.ok(response); // 삭제 실패 반환
        }

        try {
            for (String cartIdStr : cartIds) {
                Long cartId = Long.parseLong(cartIdStr); // String을 Long으로 변환
                cartService.deleteById(cartId); // 개별 Cart 삭제 처리
            }

            response.put("success", true);
            return ResponseEntity.ok(response); // 성공 시 200 OK와 함께 반환
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
        }
    }

}
