package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.DeliveryService;
import ezen.team.ezenbookstore.service.OrderItemService;
import ezen.team.ezenbookstore.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/order")
public class AdminOrderApiController {

    private final OrdersService ordersService;
    private final OrderItemService orderItemService;
    private final DeliveryService deliveryService;

    @GetMapping("")
    public String getDeliveryCounts(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(name = "delivery", defaultValue = "", required = false) String delivery,
            @RequestParam(name = "payment", defaultValue = "", required = false) String payment,
            @RequestParam(name = "status", defaultValue = "", required = false) String status,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        //전체 주문 가져오기
        List<Orders> ordersList;

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

        List<Orders> filteredOrders = ordersList;


//        // 배송상태 필터링
//        if (!delivery.isEmpty()) {
//            long deliveryValue = Integer.parseInt(delivery);
//            filteredOrders.retainAll(ordersService.findAllByDeliveryId(deliveryValue));
//        }
//
//        // 카테고리 필터링
//        if (!payment.isEmpty()) {
//            Category selectedCategory = categoryService.findById(Long.parseLong(payment));
//            if (selectedCategory != null) {
//                filteredOrders.retainAll(bookService.findAllByCategoryId(selectedCategory.getId()));
//            }
//        }
//
//        // 서브카테고리 필터링
//        if (!status.isEmpty()) {
//            SubCategory selectedSubCategory = subCategoryService.findById(Long.parseLong(status));
//            if (selectedSubCategory != null) {
//                filteredOrders.retainAll(bookService.findAllBySubcategoryId(selectedSubCategory.getId()));
//            }
//        }

        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("delivery", delivery);
        model.addAllAttributes(ordersService.getDeliveryCountsByStatus());
        model.addAttribute("ordersList", ordersList);
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
