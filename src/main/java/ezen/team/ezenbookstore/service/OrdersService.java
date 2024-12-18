package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.repository.OrdersRepository;
import ezen.team.ezenbookstore.util.FormatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrdersService implements OrdersServiceInterface{

    private final OrdersRepository orderRepository;
    private final FileUploadService fileUploadService;

    @Override
    public Orders findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<Orders> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<Orders> findByUserEmail(String email) {
        return orderRepository.findByUserEmail(email);
    }

    @Override
    public List<Orders> findByOrderId(Long orderId) {
        return orderRepository.findAllByUserId(orderId);
    }

    @Override
    public List<Orders> findAllByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Orders create(Orders order) {
        return orderRepository.save(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId) {
        Orders orders = findById(orderId);
        Orders newOrders = Orders.builder()
                .id(orders.getId())
                .user(orders.getUser())
                .delivery(orders.getDelivery())
                .payment(orders.getPayment())
                .orderDate(orders.getOrderDate())
                .status((byte) 3)
                .orderItems(orders.getOrderItems())
                .build();
        update(newOrders);
    }

    @Override
    public Map<String, Object> buildOrderResponse(Orders order) {
        Map<String, Object> response = new HashMap<>();

        List<String> titleList = new ArrayList<>();
        List<String> authorList = new ArrayList<>();
        List<String> publisherList = new ArrayList<>();
        List<String> priceList = new ArrayList<>();
        List<String> stockList = new ArrayList<>();
        List<String> imageList = new ArrayList<>();
        List<String> orderItemList = new ArrayList<>();
        List<String> orderItemListStatus = new ArrayList<>();

        for (OrderItem orderItem : order.getOrderItems()) {
            titleList.add(orderItem.getBook().getTitle());
            authorList.add(orderItem.getBook().getAuthor());
            publisherList.add(orderItem.getBook().getPublisher());
            priceList.add(orderItem.getBook().getPrice().toString());
            stockList.add(orderItem.getQuantity().toString());
            orderItemList.add(orderItem.getId().toString());
            orderItemListStatus.add(orderItem.getStatus().toString());
            // 이미지 파일 경로 찾기
            String imagePath = fileUploadService.findImageFilePath(orderItem.getBook().getId(), "book");
            if (imagePath != null) {
                imageList.add(imagePath);
            }
        }

        response.put("titleList", titleList);
        response.put("authorList", authorList);
        response.put("publisherList", publisherList);
        response.put("priceList", priceList);
        response.put("stockList", stockList);
        response.put("imageList", imageList);
        response.put("orderItemList", orderItemList);
        response.put("orderItemListStatus", orderItemListStatus);

        String orderStatus = order.getStatus().toString();
        Timestamp orderDate = order.getOrderDate();
        String formattedDate = FormatUtils.formatDate(orderDate);
        String deliveryTracking = order.getDelivery().getTrackingNum();
        String deliveryAddr = order.getDelivery().getAddr();
        String deliveryAddrextra = order.getDelivery().getAddrextra();
        String deliveryName = order.getDelivery().getName();
        String deliveryTel = order.getDelivery().getTel();
        String formattedPaymentAmount = FormatUtils.formatCurrency(order.getPayment().getAmount());

        String status = switch (order.getStatus()) {
            case 1 -> "주문 완료";
            case 2 -> "주문 취소";
            case 3 -> "교환/반품 신청";
            case 4 -> "교환/반품 승인";
            default -> "기타";
        };
        String deliveryStatus = switch (order.getDelivery().getStatus()) {
            case 1 -> "배송 준비중";
            case 2 -> "배송중";
            case 3 -> "배송 완료";
            case 4 -> "반송 준비중";
            case 5 -> "반송중";
            case 6 -> "반송 완료";
            default -> "기타";
        };
        String paymentStatus = switch (order.getPayment().getStatus()) {
            case 1 -> "결제 완료";
            case 2 -> "결제 취소";
            case 3 -> "재결제 완료";
            default -> "기타";
        };

        response.put("orderStatus", orderStatus);
        response.put("orderDate", formattedDate);
        response.put("deliveryTracking", deliveryTracking);
        response.put("deliveryAddr", deliveryAddr);
        response.put("deliveryAddrextra", deliveryAddrextra);
        response.put("deliveryName", deliveryName);
        response.put("deliveryTel", deliveryTel);
        response.put("paymentAmount", formattedPaymentAmount);
        response.put("paymentStatus", paymentStatus);
        response.put("status", status);
        response.put("deliveryStatus", deliveryStatus);
        response.put("success", "true");

        return response;
    }

    @Override
    public List<Orders> findAllByOrderDateBetweenAndUserId(LocalDate startDate, LocalDate endDate, Long userId) {
        Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
        Timestamp endTimestamp = Timestamp.valueOf(endDate.atStartOfDay().plusDays(1).minusNanos(1));
        return orderRepository.findAllByOrderDateBetweenAndUserId(startTimestamp, endTimestamp, userId);
    }

    @Override
    public List<Orders> findAllByDelivery_StatusAndUserId(Byte deliveryStatus, Long userId) {
        return orderRepository.findAllByDelivery_StatusAndUserId(deliveryStatus, userId);
    }

    @Override
    public List<Orders> findAllByStatusAndUserId(Byte orderStatus, Long userId) {
        return orderRepository.findAllByStatusAndUserId(orderStatus, userId);
    }

    @Override
    public List<Orders> findAllByOrderItemsBookTitleContainingAndUserId(String keyword, Long userId) {
        return orderRepository.findAllByOrderItems_Book_TitleContainingAndUserId(keyword, userId);
    }

    @Override
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

    @Override
    public Long ordersCount(){
        return orderRepository.count();
    }

    @Override
    public Long countByStatus2() {
        return orderRepository.countByStatus2();
    }

    @Override
    public Long countByStatus3() {
        return orderRepository.countByStatus3();
    }

    @Transactional
    @Override
    public List<Orders> findAllByEmail(String keyword) {
        return orderRepository.findAllByUserEmail(keyword);
    }

    @Transactional
    @Override
    public List<Orders> findAllByUserName(String keyword) {
        return orderRepository.findAllByUserName(keyword);
    }

    // 특정 상태의 첫 번째 주문 조회
    @Transactional
    @Override
    public Orders findByStatus(Long statusId) {
        Byte status = statusId.byteValue(); // Long -> Byte 변환
        return orderRepository.findFirstByStatus(status);
    }

    // 특정 상태를 가진 모든 주문 조회
    @Transactional
    @Override
    public List<Orders> findAllByStatus(Byte status) {
        return orderRepository.findAllByStatus(status);
    }

    @Transactional
    @Override
    public List<Orders> findAllByDelivery_Status(Byte status) {
        return orderRepository.findAllByDelivery_Status(status);
    }

    // 특정 결제 카테고리의 주문 목록 조회
    @Transactional
    @Override
    public List<Orders> findAllByPayment_Status(Byte status) {
        return orderRepository.findAllByPayment_Status(status);
    }

    @Transactional
    @Override
    public void updateStatus(Orders orders){
        orderRepository.save(orders);
    }

    @Transactional
    @Override
    public Page<Orders> getFilteredOrders(String type, String keyword, Byte delivery, Byte payment, Byte status, int page) {
        List<Orders> ordersList;
        Pageable pageable = PageRequest.of(page, 10);

        // 검색 기능
        if (type != null && !type.isEmpty() && keyword != null && !keyword.isEmpty()) {
            if (type.equalsIgnoreCase("email")) {
                ordersList = findAllByEmail(keyword);
            } else if (type.equalsIgnoreCase("name")) {
                ordersList = findAllByUserName(keyword);
            } else {
                ordersList = findAll();
            }
        } else {
            ordersList = findAll();
        }

        List<Orders> filteredOrders = ordersList;

        // 배송상태 필터링
        if (delivery != 0) {
            filteredOrders.retainAll(findAllByDelivery_Status(delivery));
        }

        // 카테고리 필터링
        if (payment != 0) {
            filteredOrders.retainAll(findAllByPayment_Status(payment));
        }

        // 서브카테고리 필터링
        if (status != 0) {
            filteredOrders.retainAll(findAllByStatus(status));
        }

        // 페이지네이션 적용
        int start = Math.min((int) pageable.getOffset(), filteredOrders.size());
        int end = Math.min((start + pageable.getPageSize()), filteredOrders.size());
        List<Orders> pagedOrders = filteredOrders.subList(start, end);

        return new PageImpl<>(pagedOrders, pageable, filteredOrders.size());
    }
}