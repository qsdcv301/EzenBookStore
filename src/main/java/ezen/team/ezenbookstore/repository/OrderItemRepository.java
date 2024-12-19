package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.OrderItem;
import ezen.team.ezenbookstore.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByOrders_Id(Long orderId);

    Integer countByOrders_UserIdAndStatus(Long userId, Byte status);

    List<OrderItem> findAllByStatus(Byte status);

    List<OrderItem> findAllByOrders_OrderDateBeforeAndStatus(Timestamp timestamp, Byte status);
}
