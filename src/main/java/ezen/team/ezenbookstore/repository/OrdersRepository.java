package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByUserId(Long userId);

    List<Orders> findByUserEmail(String email);

    List<Orders> findAllByOrderDateBetweenAndUserId(Timestamp startDate, Timestamp endDate, Long userId);

    List<Orders> findAllByDelivery_StatusAndUserId(Byte deliveryStatus, Long userId);

    List<Orders> findAllByStatusAndUserId(Byte orderStatus, Long userId);

    List<Orders> findAllByOrderItems_Book_TitleContainingAndUserId(String keyword, Long userId);

    @Query("SELECT COUNT(d) FROM Orders d WHERE d.status = 2")
    Long countByStatus2();

    @Query("SELECT COUNT(d) FROM Orders d WHERE d.status = 3")
    Long countByStatus3();



    @Query("SELECT o FROM Orders o WHERE LOWER(o.user.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    List<Orders> findAllByUserEmail(@Param("email") String email);

    @Query("SELECT o FROM Orders o WHERE LOWER(o.user.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Orders> findAllByUserName(@Param("name") String name);

    // 특정 상태를 가진 주문 목록 조회
    List<Orders> findAllByStatus(Byte status);

    // 특정 상태의 단일 주문 조회
    Orders findFirstByStatus(Byte status);

    // 특정 배송 상태에 따른 주문 목록 조회
    List<Orders> findAllByDelivery_Status(Byte status);

    // 특정 결제 카테고리에 따른 주문 목록 조회
    List<Orders> findAllByPayment_Status(Byte status);

}
