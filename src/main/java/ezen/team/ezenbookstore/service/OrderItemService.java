package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.OrderItem;
import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.repository.OrderItemRepository;
import ezen.team.ezenbookstore.util.ParseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderItemService implements OrderItemServiceInterface {

    private final OrderItemRepository orderItemRepository;
    private final UserService userService;
    private final FileUploadService fileUploadService;

    @Override
    public OrderItem findById(Long id) {
        return orderItemRepository.findById(id).orElse(null);
    }

    @Override
    public List<OrderItem> findAll() {
        return orderItemRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderItem create(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        orderItemRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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

    @Override
    public Map<String, String> buildOrderItemResponse(String orderItemId, @ModelAttribute("user") User user) {
        Map<String, String> response = new HashMap<>();
        Long id = ParseUtils.parseLong(orderItemId);
        OrderItem orderItem = findById(id);
        String title = orderItem.getBook().getTitle();
        String author = orderItem.getBook().getAuthor();
        String publisher = orderItem.getBook().getPublisher();
        String price = orderItem.getBook().getPrice().toString();
        String stock = orderItem.getQuantity().toString();
        String status = orderItem.getStatus().toString();
        String bookId = orderItem.getBook().getId().toString();
        String userGrade = user.getGrade().toString();
        String bookImagePath = fileUploadService.findImageFilePath(orderItem.getBook().getId(), "book");
        response.put("imagePath", bookImagePath);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> updateOrderItemAndUserPoint(Long orderItemId, Long successPoint, @ModelAttribute("user") User user) {
        Map<String, String> response = new HashMap<>();
        OrderItem orderItem = findById(orderItemId);
        byte status = 3;
        OrderItem newOrderItem = orderItem.toBuilder()
                .status(status)
                .build();
        update(newOrderItem);
        int userGrade;
        if (user.getGrade() != 99) {
            if (countByUserIdAndStatus(user.getId(), status) >= 20) {
                userGrade = 2;
            } else if (countByUserIdAndStatus(user.getId(), status) >= 50) {
                userGrade = 3;
            } else if (countByUserIdAndStatus(user.getId(), status) >= 100) {
                userGrade = 4;
            } else {
                userGrade = 1;
            }
        } else {
            userGrade = 99;
        }
        User newUser = user.toBuilder()
                .grade(userGrade)
                .point(user.getPoint() + successPoint)
                .build();
        userService.update(newUser);
        response.put("success", "true");
        return response;
    }

    @Override
    public Integer countByUserIdAndStatus(Long userId, Byte status) {
        return orderItemRepository.countByOrders_UserIdAndStatus(userId, status);
    }

    @Transactional
    @Override
    public boolean updateQuantity(Long ordersItemId, int quantity) {
        OrderItem ordersItem = orderItemRepository.findById(ordersItemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 항목 ID입니다."));
        ordersItem.setQuantity(quantity);
        orderItemRepository.save(ordersItem);
        return true;
    }

    @Transactional
    @Override
    public void deleteOrdersItem(Long ordersItemId) {
        orderItemRepository.deleteById(ordersItemId);
    }

    @Override
    public List<OrderItem> findAllByStatus(Byte status) {
        return orderItemRepository.findAllByStatus(status);
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findAllByOrders_Id(orderId);
    }

    @Override
    public List<OrderItem> findAllByDeliveryCompletedStatus() {
        // orderitem status 2 = 배송완료
        return findAllByStatus((byte) 2);
    }

    @Override
    public List<OrderItem> findAllByOrders_OrderDateBeforeAndStatus(Timestamp timestamp) {
        // orderitem status 2 = 배송완료
        Byte status = 2;
        return orderItemRepository.findAllByOrders_OrderDateBeforeAndStatus(timestamp, status);
    }

}