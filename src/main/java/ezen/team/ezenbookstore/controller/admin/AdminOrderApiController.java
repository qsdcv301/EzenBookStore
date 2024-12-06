package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.DeliveryService;
import ezen.team.ezenbookstore.service.OrderItemService;
import ezen.team.ezenbookstore.service.OrdersService;
import ezen.team.ezenbookstore.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/order")
public class AdminOrderApiController {

    private final OrdersService ordersService;
    private final OrderItemService orderItemService;
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;

    @GetMapping("")
    public String getDeliveryCounts(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(name = "delivery", defaultValue = "0", required = false) Byte delivery,
            @RequestParam(name = "payment", defaultValue = "0", required = false) Byte payment,
            @RequestParam(name = "status", defaultValue = "0", required = false) Byte status,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            Model model) {

        //전체 주문 가져오기
        List<Orders> ordersList;
        Pageable pageable = PageRequest.of(page, 10);

        // 검색 기능
        if (type != null && !type.isEmpty() && keyword != null && !keyword.isEmpty()) {
            // type에 따라 검색 필터링을 다르게 적용
            if (type.equalsIgnoreCase("email")) {
                ordersList = ordersService.findAllByEmail(keyword);
            } else if (type.equalsIgnoreCase("name")) {
                ordersList = ordersService.findAllByUserName(keyword);
            } else {
                ordersList = ordersService.findAll();
            }
        } else {
            ordersList = ordersService.findAll();
        }

        //작업중
        List<Orders> filteredOrders = ordersList;

        // 배송상태 필터링
        if (delivery != 0) {
            filteredOrders.retainAll(ordersService.findAllByDelivery_Status(delivery));
        }

        // 카테고리 필터링
        if (payment != 0) {
            filteredOrders.retainAll(ordersService.findAllByPayment_Status(payment));
        }

        // 서브카테고리 필터링
        if (status != 0) {
            filteredOrders.retainAll(ordersService.findAllByStatus(status));
        }

        // 페이지네이션 적용
        int start = Math.min((int) pageable.getOffset(), filteredOrders.size());
        int end = Math.min((start + pageable.getPageSize()), filteredOrders.size());
        List<Orders> pagedOrders = filteredOrders.subList(start, end);
        Page<Orders> ordersPage = new PageImpl<>(pagedOrders, pageable, filteredOrders.size());

        Map<String, Object> deliveryCount = new HashMap<>();
        deliveryCount = deliveryService.getDeliveryCountsByStatus();
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
    ){
        Map<String, Boolean> response = new HashMap<>();
        // orderId가 null 또는 빈 값일 경우 처리
        if (orderId == null || orderId.isEmpty()) {
            response.put("success", false);
            return ResponseEntity.badRequest().body(response);
        }

        // String을 Byte로 변환
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

}
