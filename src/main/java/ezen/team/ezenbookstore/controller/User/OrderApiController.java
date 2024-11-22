package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/order")
public class OrderApiController {

    private final OrdersService ordersService;
    private final UserService userService;
    private final FileUploadService fileUploadService;
    private final OrderItemService orderItemService;

    @ModelAttribute
    public void findUser(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object userData = auth.getPrincipal();
            if (userData instanceof User user) {
                model.addAttribute("user", user);
                model.addAttribute("userData", true);
                model.addAttribute("mypage", true);
            } else if (userData instanceof CustomOAuth2User customOAuth2User) {
                User customUser = userService.findByEmail(customOAuth2User.getEmail());
                model.addAttribute("user", customUser);
                model.addAttribute("userData", true);
                model.addAttribute("mypage", true);
            } else {
                model.addAttribute("userData", false);
            }
        } catch (Exception e) {
            model.addAttribute("userData", false);
        }
    }

    @PostMapping("/{ordersId}")
    public ResponseEntity<Map<String, Object>> findOrders(@PathVariable(required = false) String ordersId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long orderId = Long.parseLong(ordersId);
            Orders order = ordersService.findById(orderId);

            // 제목, 저자, 출판사, 가격, 재고를 리스트로 만들어 Map에 추가
            List<String> titleList = new ArrayList<>();
            List<String> authorList = new ArrayList<>();
            List<String> publisherList = new ArrayList<>();
            List<String> priceList = new ArrayList<>();
            List<String> stockList = new ArrayList<>();
            List<String> imageList = new ArrayList<>();
            List<String> orderItemList = new ArrayList<>();
            List<String> orderItemListStatus = new ArrayList<>();

            for (OrderItem orderItem : order.getOrderItems()) {
                titleList.add(orderItem.getBook().getTitle());
                authorList.add(orderItem.getBook().getAuthor());
                publisherList.add(orderItem.getBook().getPublisher());
                priceList.add(orderItem.getBook().getPrice().toString());
                stockList.add(orderItem.getBook().getStock().toString());
                orderItemList.add(orderItem.getId().toString());
                orderItemListStatus.add(orderItem.getStatus().toString());
                // 이미지 파일 경로 찾기
                String imagePath = fileUploadService.findImageFilePath(orderItem.getBook().getId(), "book");
                if (imagePath != null) {
                    imageList.add(imagePath);
                }
            }

            response.put("titleList", titleList);
            response.put("authorList", authorList);
            response.put("publisherList", publisherList);
            response.put("priceList", priceList);
            response.put("stockList", stockList);
            response.put("imageList", imageList);
            response.put("orderItemList", orderItemList);
            response.put("orderItemListStatus", orderItemListStatus);

            // 주문 관련 정보 추가
            String orderStatus = order.getStatus().toString();
            Timestamp orderDate = order.getOrderDate();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String formattedDate = formatter.format(orderDate);
            String deliveryTracking = order.getDelivery().getTrackingNum();
            String deliveryAddr = order.getDelivery().getAddr();
            String deliveryAddrextra = order.getDelivery().getAddrextra();
            String deliveryName = order.getDelivery().getName();
            String deliveryTel = order.getDelivery().getTel();
            // Locale을 한국으로 설정하여 통화 형식 변환
            NumberFormat numberFormatter = NumberFormat.getNumberInstance(Locale.KOREA);
            // 금액을 포맷하여 "원"을 추가
            String formattedPaymentAmount = numberFormatter.format(order.getPayment().getAmount()) + "원";

            //orderItem Status
            // 1 배송 전
            // 2 배송 완료
            // 3 주문 확정
            // 4 교환
            // 5 반품

            // 주문 상태를 변환
            String status = switch (order.getStatus()) {
                case 1 -> "주문 완료";
                case 2 -> "주문 취소";
                case 3 -> "교환/반품 신청";
                default -> "기타";
            };
            String deliveryStatus = switch (order.getDelivery().getStatus()) {
                case 1 -> "배송 준비중";
                case 2 -> "배송중";
                case 3 -> "배송 완료";
                case 4 -> "반송 준비중";
                case 5 -> "반송중";
                case 6 -> "반송 완료";
                default -> "기타";
            };
            String paymentStatus = switch (order.getPayment().getStatus()) {
                case 1 -> "결제 완료";
                case 2 -> "결제 취소";
                case 3 -> "재결제 완료";
                default -> "기타";
            };

            response.put("orderStatus", orderStatus);
            response.put("orderDate", formattedDate);
            response.put("deliveryTracking", deliveryTracking);
            response.put("deliveryAddr", deliveryAddr);
            response.put("deliveryAddrextra", deliveryAddrextra);
            response.put("deliveryName", deliveryName);
            response.put("deliveryTel", deliveryTel);
            response.put("paymentAmount", formattedPaymentAmount);
            response.put("paymentStatus", paymentStatus);
            response.put("status", status);
            response.put("deliveryStatus", deliveryStatus);

            response.put("success", "true");
            return ResponseEntity.ok(response); // 성공 시 200 OK와 함께 반환
        } catch (Exception e) {
            response.put("success", "false");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
        }
    }

    @PostMapping("/success/{orderItemId}")
    public ResponseEntity<Map<String, String>> findOrderItem(@PathVariable(required = false) String orderItemId, Model model) {
        Map<String, String> response = new HashMap<>();
        try {
            Long id = Long.parseLong(orderItemId);
            OrderItem orderItem = orderItemService.findById(id);
            String title = orderItem.getBook().getTitle();
            String author = orderItem.getBook().getAuthor();
            String publisher = orderItem.getBook().getPublisher();
            String price = orderItem.getBook().getPrice().toString();
            String stock = orderItem.getQuantity().toString();
            String imagePath = fileUploadService.findImageFilePath(orderItem.getBook().getId(), "book");
            String status = orderItem.getStatus().toString();
            String bookId = orderItem.getBook().getId().toString();
            User user = (User) model.getAttribute("user");
            String userGrade = user.getGrade().toString();
            response.put("success", "true");
            response.put("bookId", bookId);
            response.put("orderItemId", id.toString());
            response.put("orderItemTitle", title);
            response.put("orderItemAuthor", author);
            response.put("orderItemPublisher", publisher);
            response.put("orderItemPrice", price);
            response.put("orderItemStock", stock);
            response.put("userGrade", userGrade);
            response.put("imagePath", imagePath);
            response.put("orderItemStatus", status);
            return ResponseEntity.ok(response); // 성공 시 200 OK와 함께 반환
        } catch (Exception e) {
            response.put("success", "false");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
        }
    }

    @PostMapping("/success")
    public ResponseEntity<Map<String, String>> findOrderItem(@RequestParam(name = "orderItemId") Long orderItemId,
                                                             @RequestParam(name = "point") Long point,
                                                             Model model) {
        Map<String, String> response = new HashMap<>();
        try {
            User user = (User) model.getAttribute("user");
            User newUser = User.builder()
                    .id(user.getId())
                    .provider(user.getProvider())
                    .email(user.getEmail())
                    .name(user.getName())
                    .password(user.getPassword())
                    .tel(user.getTel())
                    .addr(user.getAddr())
                    .addrextra(user.getAddrextra())
                    .createdAt(user.getCreatedAt())
                    .birthday(user.getBirthday())
                    .grade(user.getGrade())
                    .point(user.getPoint() + point)
                    .build();
            userService.update(newUser);
            OrderItem orderItem = orderItemService.findById(orderItemId);
            byte status = 2; // 1일반 2구매 확정 3리뷰 작성 4 교환 5 반품
            OrderItem newOrderItem = OrderItem.builder()
                    .id(orderItem.getId())
                    .book(orderItem.getBook())
                    .orders(orderItem.getOrders())
                    .quantity(orderItem.getQuantity())
                    .status(status)
                    .build();
            orderItemService.update(newOrderItem);
            response.put("success", "true");
            return ResponseEntity.ok(response); // 성공 시 200 OK와 함께 반환
        } catch (Exception e) {
            response.put("success", "false");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
        }
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
