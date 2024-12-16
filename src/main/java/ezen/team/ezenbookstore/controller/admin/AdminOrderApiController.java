package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.DeliveryService;
import ezen.team.ezenbookstore.service.OrderItemService;
import ezen.team.ezenbookstore.service.OrdersService;
import ezen.team.ezenbookstore.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/order")
public class AdminOrderApiController {

    private final OrdersService ordersService;
    private final OrderItemService ordersItemService;
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;
    private final OrderItemService orderItemService;

    @GetMapping("")
    public String getDeliveryCounts(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(name = "delivery", defaultValue = "0", required = false) Byte delivery,
            @RequestParam(name = "payment", defaultValue = "0", required = false) Byte payment,
            @RequestParam(name = "status", defaultValue = "0", required = false) Byte status,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            Model model) {

        Page<Orders> ordersPage = ordersService.getFilteredOrders(type, keyword, delivery, payment, status, page);
        Map<String, Object> deliveryCount = deliveryService.getDeliveryCountsByStatus();

        // 모델에 값 추가
        model.addAttribute("ordersList", ordersPage.getContent());
        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("delivery", delivery);
        model.addAttribute("payment", payment);
        model.addAttribute("deliveryCount", deliveryCount);

        return "admin/orderControl"; // HTML 파일 이름
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Boolean>> updateOrder(
            @RequestParam String orderId,
            @RequestParam String deliveryStatus,
            @RequestParam String paymentStatus,
            @RequestParam String orderStatus
    ) {
        Map<String, Boolean> response = new HashMap<>();
        // orderId가 null 또는 빈 값일 경우 처리
        if (orderId == null || orderId.isEmpty()) {
            response.put("success", false);
            return ResponseEntity.badRequest().body(response);
        }

        // String 값을 Byte로 변환 (빈 값인 경우 기본값 설정)
        Byte deliveryStatusByte = (deliveryStatus != null && !deliveryStatus.isEmpty())
                ? Byte.valueOf(deliveryStatus)
                : null;
        Byte paymentStatusByte = (paymentStatus != null && !paymentStatus.isEmpty())
                ? Byte.valueOf(paymentStatus)
                : null;
        Byte orderStatusByte = (orderStatus != null && !orderStatus.isEmpty())
                ? Byte.valueOf(orderStatus)
                : null;

        // 여러 Order ID 처리
        String[] orderIds = orderId.split(",");
        for (String id : orderIds) {
            // Orders 객체 조회
            Orders orders = ordersService.findById(Long.parseLong(id));
            if (orders != null) {
                // Orders 엔티티에서 Delivery와 Payment 가져오기
                Delivery delivery = orders.getDelivery();
                Payment payment = orders.getPayment();

                // Delivery 상태 업데이트
                if (delivery != null && deliveryStatusByte != null) {
                    delivery.setStatus(deliveryStatusByte);
                    deliveryService.updateStatus(delivery);
                    if (deliveryStatusByte == 2) {
                        List<OrderItem> orderItems = orders.getOrderItems();
                        for (OrderItem orderItem : orderItems) {
                            OrderItem orderItemStatus = orderItemService.findById(orderItem.getId());
                            orderItemStatus = orderItemStatus.toBuilder()
                                    .status((byte) 2)
                                    .build();
                            orderItemService.update(orderItemStatus);
                        }
                    }
                }

                // Payment 상태 업데이트
                if (payment != null && paymentStatusByte != null) {
                    payment.setStatus(paymentStatusByte);
                    paymentService.updateStatus(payment);
                }

                // Orders 상태 업데이트
                if (orderStatusByte != null) {
                    orders.setStatus(orderStatusByte);
                    ordersService.updateStatus(orders);
                }
            }
        }

        // 성공 응답
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}/details")
    @ResponseBody
    public ResponseEntity<List<OrderItem>> getOrderDetails(@PathVariable Long orderId) {
        List<OrderItem> orderItems = ordersItemService.getOrderItemsByOrderId(orderId);
        return ResponseEntity.ok(orderItems);
    }

    // 주문 항목 수량 수정
    @PostMapping("/{ordersItemId}/edit")
    public ResponseEntity<Void> updateOrdersItem(
            @PathVariable Long ordersItemId,
            @RequestBody OrderItem orderItem) {
        orderItemService.updateQuantity(ordersItemId, orderItem.getQuantity());
        return ResponseEntity.ok().build();
    }

    // 주문 항목 삭제
    @PostMapping("/{ordersItemId}/delete")
    public ResponseEntity<Void> deleteOrdersItem(@PathVariable Long ordersItemId) {
        ordersItemService.deleteOrdersItem(ordersItemId);
        return ResponseEntity.ok().build();
    }


}
