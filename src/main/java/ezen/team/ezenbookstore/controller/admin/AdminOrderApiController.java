package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.Book;
import ezen.team.ezenbookstore.entity.OrderItem;
import ezen.team.ezenbookstore.entity.Orders;
import ezen.team.ezenbookstore.service.OrderItemService;
import ezen.team.ezenbookstore.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/order")
public class AdminOrderApiController {

    private final OrdersService ordersService;
    private final OrderItemService orderItemService;

    @GetMapping("")
    public String ordersControl(Model model) {
        List<Orders> ordersList = ordersService.findAll();
        model.addAttribute("ordersList", ordersList);
        return "admin/orderControl";
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
