package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Orders;
import ezen.team.ezenbookstore.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Orders update(Long id, Orders order) {
        Orders newOrder = Orders.builder()
                .id(id)
                .user(order.getUser())
                .delivery(order.getDelivery())
                .payment(order.getPayment())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
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

}
