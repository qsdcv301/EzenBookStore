package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.OrderItem;
import ezen.team.ezenbookstore.entity.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Map;

public interface OrderItemServiceInterface {

    OrderItem findById(Long id);

    List<OrderItem> findAll();

    OrderItem create(OrderItem orderItem);

    void delete(Long id);

    OrderItem update(OrderItem orderItem);

    Map<String, String> buildOrderItemResponse(String orderItemId, @ModelAttribute("user") User user);

    Map<String, String> updateOrderItemAndUserPoint(Long orderItemId, Long point, @ModelAttribute("user") User user);

    Integer countByUserIdAndStatus(Long userId, Byte status);

    @Transactional
        // 주문 항목 수량 업데이트
    boolean updateQuantity(Long ordersItemId, int quantity);

    @Transactional
        // 주문 항목 삭제
    void deleteOrdersItem(Long ordersItemId);
}
