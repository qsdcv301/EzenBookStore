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
        Orders newOrder = findById(id);
        newOrder.Builder(newOrder.getId(), order.getUserId(),
                order.getTotalPrice(), order.getStatus());
        return orderRepository.save(newOrder);
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}
