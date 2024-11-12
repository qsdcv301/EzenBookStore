package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.OrderItem;
import ezen.team.ezenbookstore.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItem findById(Long id) {
        return orderItemRepository.findById(id).orElse(null);
    }

    public List<OrderItem> findAll() {
        return orderItemRepository.findAll();
    }

    public OrderItem create(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    public OrderItem update(Long id, OrderItem orderItem) {
        OrderItem newOrderItem = OrderItem.builder()
                .id(id)
                .book(orderItem.getBook())
                .quantity(orderItem.getQuantity())
                .build();
        return orderItemRepository.save(newOrderItem);
    }

    public void delete(Long id) {
        orderItemRepository.deleteById(id);
    }
}
