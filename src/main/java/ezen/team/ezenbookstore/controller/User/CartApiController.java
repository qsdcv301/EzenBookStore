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
        model.addAttribute("user", user);
        model.addAttribute("cartList", cartList);
        return "cart";
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Boolean>> addCart(@RequestParam List<String> bookId,
                                                        @RequestParam List<String> quantity) {
        Map<String, Boolean> response = new HashMap<>();
        // bookId와 quantity 리스트가 일치하지 않거나 비어 있는 경우에 대한 검증
        if (bookId == null || bookId.isEmpty() || quantity == null || quantity.isEmpty() || bookId.size() != quantity.size()) {
            response.put("success", false);
            return ResponseEntity.ok(response); // 추가 실패 반환
        }
        try {
            // 현재 사용자 이메일을 가져와 User 객체 조회
            String userEmail = userService.getUserEmail();
            User user = userService.findByEmail(userEmail);
            // bookId와 quantity를 인덱스를 기준으로 순환
            for (int i = 0; i < bookId.size(); i++) {
                Long bookIdValue = Long.parseLong(bookId.get(i)); // String을 Long으로 변환
                Integer quantityValue = Integer.parseInt(quantity.get(i)); // String을 Integer로 변환

                Cart newCart = Cart.builder()
                        .user(user)
                        .book(bookService.findById(bookIdValue))
                        .quantity(quantityValue)
                        .build();

                // Cart 저장 처리
                cartService.create(newCart);
            }
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
    public ResponseEntity<Map<String, Boolean>> deleteCart(@RequestParam List<String> cartId) {
        Map<String, Boolean> response = new HashMap<>();

        if (cartId == null || cartId.isEmpty()) {
            response.put("success", false);
            return ResponseEntity.ok(response); // 삭제 실패 반환
        }

        try {
            for (String cartIdStr : cartId) {
                Long cartIdValue = Long.parseLong(cartIdStr); // String을 Long으로 변환
                cartService.deleteById(cartIdValue); // 개별 Cart 삭제 처리
            }
            response.put("success", true);
            return ResponseEntity.ok(response); // 성공 시 200 OK와 함께 반환
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
        }
    }

}
