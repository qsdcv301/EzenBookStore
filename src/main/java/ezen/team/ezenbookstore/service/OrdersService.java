package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Orders;
import ezen.team.ezenbookstore.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository orderRepository;

    public Orders findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Orders> findAll() {
        return orderRepository.findAll();
    }

    public Orders create(Orders order) {
        return orderRepository.save(order);
    }

    public Orders update(Orders order) {
        Orders newOrder = Orders.builder()
                .id(order.getId())
                .user(order.getUser())
                .delivery(order.getDelivery())
                .payment(order.getPayment())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .build();
        return orderRepository.save(newOrder);
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    public List<Orders> findAllByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId);
    }

    public List<Orders> findByUserEmail(String email) {
        return orderRepository.findByUserEmail(email);
    }

    public List<Orders> findByOrderId(Long id) {
        return orderRepository.findById(id).map(List::of).orElse(Collections.emptyList());
    }

    public List<Orders> findAllByOrderDateBetweenAndUserId(LocalDate startDate, LocalDate endDate, Long userId) {
        Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
        Timestamp endTimestamp = Timestamp.valueOf(endDate.atStartOfDay().plusDays(1).minusNanos(1));
        return orderRepository.findAllByOrderDateBetweenAndUserId(startTimestamp, endTimestamp, userId);
    }

    public List<Orders> findAllByDelivery_StatusAndUserId(Byte deliveryStatus, Long userId) {
        return orderRepository.findAllByDelivery_StatusAndUserId(deliveryStatus, userId);
    }

    public List<Orders> findAllByStatusAndUserId(Byte orderStatus, Long userId) {
        return orderRepository.findAllByStatusAndUserId(orderStatus, userId);
    }

    public List<Orders> findAllByOrderItemsBookTitleContainingAndUserId(String keyword, Long userId) {
        return orderRepository.findAllByOrderItems_Book_TitleContainingAndUserId(keyword, userId);
    }

    public List<Orders> filterOrders(LocalDate startDate, LocalDate endDate, Byte deliveryStatus, Byte orderStatus, String keyword, Long userId) {
        List<Orders> filteredOrders = findAllByUserId(userId);
        boolean isFirstFilter = true;

        if (startDate != null && endDate != null) {
            List<Orders> ordersByDateRange = findAllByOrderDateBetweenAndUserId(startDate, endDate, userId);
            if (isFirstFilter) {
                filteredOrders = ordersByDateRange;
                isFirstFilter = false;
            } else {
                filteredOrders.retainAll(ordersByDateRange);
            }
        }

        if (deliveryStatus != null) {
            List<Orders> ordersByDeliveryStatus = findAllByDelivery_StatusAndUserId(deliveryStatus, userId);
            if (isFirstFilter) {
                filteredOrders = ordersByDeliveryStatus;
                isFirstFilter = false;
            } else {
                filteredOrders.retainAll(ordersByDeliveryStatus);
            }
        }

        if (orderStatus != null) {
            List<Orders> ordersByOrderStatus = findAllByStatusAndUserId(orderStatus, userId);
            if (isFirstFilter) {
                filteredOrders = ordersByOrderStatus;
                isFirstFilter = false;
            } else {
                filteredOrders.retainAll(ordersByOrderStatus);
            }
        }

        if (keyword != null && !keyword.isEmpty()) {
            List<Orders> ordersByKeyword = findAllByOrderItemsBookTitleContainingAndUserId(keyword, userId);
            if (isFirstFilter) {
                filteredOrders = ordersByKeyword;
                isFirstFilter = false;
            } else {
                filteredOrders.retainAll(ordersByKeyword);
            }
        }

        return filteredOrders;
    }

}
