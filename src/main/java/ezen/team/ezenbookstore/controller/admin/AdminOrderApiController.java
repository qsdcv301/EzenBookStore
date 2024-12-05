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
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        //전체 주문 가져오기
        List<Orders> ordersList;
        Pageable pageable = PageRequest.of(page, size);

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

    // 주문 목록 조회
    @PostMapping("/list")
    public String getOrderList() {
        // 주문 목록 조회 로직
        return "redirect:/admin/order"; // 주문 목록 페이지로 리다이렉트
    }

    // 주문 상태 조회
    @PostMapping("/detail")
    public String getOrderDetail(@RequestParam Long orderId) {
        // 특정 주문 상세 조회 로직
        return "redirect:/admin/order"; // 상세 페이지로 리다이렉트 (예시로 목록 페이지로 설정)
    }

    // 주문 상태 업데이트
    @PostMapping("/updateStatus")
    public String updateOrderStatus(@RequestParam Long orderId, @RequestParam String status) {
        // 주문 상태 업데이트 로직
        return "redirect:/admin/order"; // 상태 업데이트 후 목록 페이지로 리다이렉트
    }

    // 취소/반품/교환 요청 목록 조회
    @PostMapping("/request/list")
    public String getRequestList() {
        // 취소/반품/교환 요청 목록 조회 로직
        return "redirect:/admin/order"; // 요청 목록 페이지로 리다이렉트
    }

    // 취소/반품/교환 요청 상세 조회
    @PostMapping("/request/detail")
    public String getRequestDetail(@RequestParam Long requestId) {
        // 특정 요청 상세 조회 로직
        return "redirect:/admin/order"; // 상세 페이지로 리다이렉트
    }

    // 취소/반품/교환 요청 상태 업데이트
    @PostMapping("/request/updateStatus")
    public String updateRequestStatus(@RequestParam Long requestId, @RequestParam String status) {
        // 요청 상태 업데이트 로직
        return "redirect:/admin/order"; // 상태 업데이트 후 목록 페이지로 리다이렉트
    }


    @GetMapping("/search")
    public String searchOrders(
            @RequestParam("searchType") String searchType,
            @RequestParam("keyword") String keyword,
            Model model) {

        List<Orders> orders;

        if ("userId".equals(searchType)) {
            orders = ordersService.findByUserEmail(keyword);
        } else if ("id".equals(searchType)) {
            try {
                Long id = Long.parseLong(keyword);
                orders = ordersService.findByOrderId(id);
            } catch (NumberFormatException e) {
                orders = new ArrayList<>();
            }
        } else {
            orders = new ArrayList<>();
        }

        model.addAttribute("ordersList", orders);
        return "admin/orderControl";
    }

}
