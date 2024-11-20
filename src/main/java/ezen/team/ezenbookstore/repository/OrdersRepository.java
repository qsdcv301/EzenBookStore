package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByUserId(Long userId);

    List<Orders> findByUserEmail(String email);

    List<Orders> findAllByOrderDateBetweenAndUserId(Timestamp startDate, Timestamp endDate, Long userId);

    List<Orders> findAllByDelivery_StatusAndUserId(Byte deliveryStatus, Long userId);

    List<Orders> findAllByStatusAndUserId(Byte orderStatus, Long userId);

    List<Orders> findAllByOrderItems_Book_TitleContainingAndUserId(String keyword, Long userId);

}
