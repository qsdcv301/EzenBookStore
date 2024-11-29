package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.OrderItem;
import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final UserService userService;

    public OrderItem findById(Long id) {
        return orderItemRepository.findById(id).orElse(null);
    }

    public List<OrderItem> findAll() {
        return orderItemRepository.findAll();
    }

    public OrderItem create(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    public void delete(Long id) {
        orderItemRepository.deleteById(id);
    }


    public OrderItem update(OrderItem orderItem) {
        OrderItem newOrderItem = OrderItem.builder()
                .id(orderItem.getId())
                .book(orderItem.getBook())
                .orders(orderItem.getOrders())
                .quantity(orderItem.getQuantity())
                .status(orderItem.getStatus())
                .build();
        return orderItemRepository.save(newOrderItem);
    }

    public Map<String, String> buildOrderItemResponse(String orderItemId, @ModelAttribute("user") User user) {
        Map<String, String> response = new HashMap<>();
        Long id = Long.parseLong(orderItemId);
        OrderItem orderItem = findById(id);
        String title = orderItem.getBook().getTitle();
        String author = orderItem.getBook().getAuthor();
        String publisher = orderItem.getBook().getPublisher();
        String price = orderItem.getBook().getPrice().toString();
        String stock = orderItem.getQuantity().toString();
        String status = orderItem.getStatus().toString();
        String bookId = orderItem.getBook().getId().toString();
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
        response.put("orderItemStatus", status);
        return response;
    }

    public Map<String, String> updateOrderItemAndUserPoint(Long orderItemId, Long point, @ModelAttribute("user") User user) {
        Map<String, String> response = new HashMap<>();
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
        OrderItem orderItem = findById(orderItemId);
        byte status = 3;
        OrderItem newOrderItem = OrderItem.builder()
                .id(orderItem.getId())
                .book(orderItem.getBook())
                .orders(orderItem.getOrders())
                .quantity(orderItem.getQuantity())
                .status(status)
                .build();
        update(newOrderItem);
        response.put("success", "true");
        return response;
    }

}