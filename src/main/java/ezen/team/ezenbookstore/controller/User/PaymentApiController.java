package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/order/payment")
public class PaymentApiController {

    private final PaymentService paymentService;
    private final UserService userService;
    private final BookService bookService;
    private final OrderItemService orderItemService;
    private final DeliveryService deliveryService;
    private final OrdersService ordersService;
    private final CartService cartService;

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
                                                               Model model) {
        Map<String, Boolean> response = new HashMap<>();
        User user = (User) model.getAttribute("user");
        try {
            if (titleList.size() != quantityList.size()) {
                response.put("success", false);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 크기가 다르면 400 Bad Request 반환
            }
            Delivery delivery = Delivery.builder()
                    .status((byte) 1)
                    .name(userName)
                    .tel(tel)
                    .addr(addr)
                    .addrextra(addrextra)
                    .build();
            Delivery newDelivery = deliveryService.create(delivery);
            Payment payment = Payment.builder()
                    .user(user)
                    .amount(amount)
                    .status((byte) 1)
                    .paymentCode(paymentCode)
                    .build();
            Payment newPayment = paymentService.create(payment);
            Orders orders = Orders.builder()
                    .user(user)
                    .delivery(newDelivery)
                    .payment(newPayment)
                    .status((byte) 1)
                    .build();
            Orders newOrders = ordersService.create(orders);
            // 인덱스를 통해 titleList와 quantityList의 값들을 동시에 처리
            for (int i = 0; i < titleList.size(); i++) {
                String title = titleList.get(i);
                Integer quantity = quantityList.get(i);
                if (cartIdList != null) {
                    Long cartId = cartIdList.get(i);
                    cartService.deleteById(cartId);
                }
                Book book = bookService.findByTitle(title);
                OrderItem orderItem = OrderItem.builder()
                        .book(book)
                        .orders(newOrders)
                        .quantity(quantity)
                        .status((byte) 1)
                        .build();
                orderItemService.create(orderItem);
                Book newBook = Book.builder()
                        .id(book.getId())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .publisher(book.getPublisher())
                        .publishDate(book.getPublishDate())
                        .isbn(book.getIsbn())
                        .stock(book.getStock() - quantity)
                        .ifkr(book.getIfkr())
                        .price(book.getPrice())
                        .category(book.getCategory())
                        .subcategory(book.getSubcategory())
                        .count(book.getCount())
                        .discount(book.getDiscount())
                        .bookdescription(book.getBookdescription())
                        .build();
                bookService.update(newBook);
            }
            response.put("success", true);
            return ResponseEntity.ok(response); // 성공 시 200 OK와 함께 반환
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
        }
    }

}
