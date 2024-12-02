package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Orders;

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

}
