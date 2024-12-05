package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.*;
import ezen.team.ezenbookstore.util.ParseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/order")
public class OrderApiController {

    private final OrdersService ordersService;
    private final OrderItemService orderItemService;

    @PostMapping("/{ordersId}")
    public ResponseEntity<Map<String, Object>> findOrders(@PathVariable(required = false) String ordersId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long orderId = ParseUtils.parseLong(ordersId);
            Orders order = ordersService.findById(orderId);
            response = ordersService.buildOrderResponse(order);
            return ResponseEntity.ok(response); // 성공 시 200 OK와 함께 반환
        } catch (Exception e) {
            response.put("success", "false");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
        }
    }

    @PostMapping("/success/{orderItemId}")
    public ResponseEntity<Map<String, String>> findOrderItem(@PathVariable(required = false) String orderItemId, @ModelAttribute("user") User user) {
        Map<String, String> response = new HashMap<>();
        try {
            response = orderItemService.buildOrderItemResponse(orderItemId, user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", "false");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/success")
    public ResponseEntity<Map<String, String>> updateOrderItem(@RequestParam(name = "orderItemId") Long orderItemId,
                                                               @RequestParam(name = "point") Long point,
                                                               @ModelAttribute("user") User user) {
        Map<String, String> response = new HashMap<>();
        try {
            response = orderItemService.updateOrderItemAndUserPoint(orderItemId, point, user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", "false");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/orderCancel")
    public ResponseEntity<Map<String, Boolean>> orderCancel(@RequestParam(name = "orderId") Long orderId) {
        Map<String, Boolean> response = new HashMap<>();
        try {
            ordersService.cancelOrder(orderId);
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}