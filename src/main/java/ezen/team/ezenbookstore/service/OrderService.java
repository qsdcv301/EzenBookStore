package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Order;
import ezen.team.ezenbookstore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order create(Order order) {
        return orderRepository.save(order);
    }

    public Order update(Long id, Order order) {
        Order newOrder = findById(id);
        newOrder.Builder(newOrder.getId(), order.getUserId(),
                order.getTotalPrice(), order.getStatus());
        return orderRepository.save(newOrder);
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}
