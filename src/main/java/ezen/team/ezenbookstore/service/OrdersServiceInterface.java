package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrdersServiceInterface {

    Orders findById(Long id);

    List<Orders> findAll();

    List<Orders> findByUserEmail(String email);

    List<Orders> findByOrderId(Long orderId);

    List<Orders> findAllByUserId(Long userId);

    void deleteById(Long id);

    Orders create(Orders order);

    Orders update(Orders order);

    void cancelOrder(Long orderId);

    Map<String, Object> buildOrderResponse(Orders order);

    List<Orders> findAllByOrderDateBetweenAndUserId(LocalDate startDate, LocalDate endDate, Long userId);

    List<Orders> findAllByDelivery_StatusAndUserId(Byte deliveryStatus, Long userId);

    List<Orders> findAllByStatusAndUserId(Byte orderStatus, Long userId);

    List<Orders> findAllByOrderItemsBookTitleContainingAndUserId(String keyword, Long userId);

    List<Orders> filterOrders(LocalDate startDate, LocalDate endDate, Byte deliveryStatus, Byte orderStatus, String keyword, Long userId);

    Long ordersCount();

    Long countByStatus2();

    Long countByStatus3();

    @Transactional
    List<Orders> findAllByEmail(String keyword);

    @Transactional
    List<Orders> findAllByUserName(String keyword);

    // 특정 상태의 첫 번째 주문 조회
    @Transactional
    Orders findByStatus(Long statusId);

    // 특정 상태를 가진 모든 주문 조회
    @Transactional
    List<Orders> findAllByStatus(Byte status);

    @Transactional
    List<Orders> findAllByDelivery_Status(Byte status);

    // 특정 결제 카테고리의 주문 목록 조회
    @Transactional
    List<Orders> findAllByPayment_Status(Byte status);

    @Transactional
    void updateStatus(Orders orders);

    @Transactional
    Page<Orders> getFilteredOrders(String type, String keyword, Byte delivery, Byte payment, Byte status, int page);
}
