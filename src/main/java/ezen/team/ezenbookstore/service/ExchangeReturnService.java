package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.ExchangeReturn;
import ezen.team.ezenbookstore.entity.OrderItem;
import ezen.team.ezenbookstore.entity.Orders;
import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.repository.ExchangeReturnRepository;
import ezen.team.ezenbookstore.util.ParseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExchangeReturnService implements ExchangeReturnServiceInterface{

    private final ExchangeReturnRepository exchangeReturnRepository;
    private final UserService userService;
    private final FileUploadService fileUploadService;
    private final OrderItemService orderItemService;
    private final OrdersService ordersService;

    @Override
    public ExchangeReturn findById(Long id) {
        return exchangeReturnRepository.findById(id).orElse(null);
    }

    @Override
    public List<ExchangeReturn> findAll() {
        return exchangeReturnRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExchangeReturn create(ExchangeReturn exchangeReturn) {
        return exchangeReturnRepository.save(exchangeReturn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExchangeReturn update(ExchangeReturn exchangeReturn) {
        ExchangeReturn newExchangeReturn = ExchangeReturn.builder()
                .id(exchangeReturn.getId())
                .user(exchangeReturn.getUser())
                .orderItem(exchangeReturn.getOrderItem())
                .category(exchangeReturn.getCategory())
                .question(exchangeReturn.getQuestion())
                .answer(exchangeReturn.getAnswer())
                .createAt(exchangeReturn.getCreateAt())
                .build();
        return exchangeReturnRepository.save(newExchangeReturn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        exchangeReturnRepository.deleteById(id);
    }

    @Override
    public List<ExchangeReturn> findAllByUserId(Long userId) {
        return exchangeReturnRepository.findAllByUserId(userId);
    }

    @Override
    public Page<ExchangeReturn> findAll(Pageable pageable) {
        return exchangeReturnRepository.findAll(pageable);
    }

    @Override
    public Page<ExchangeReturn> findAllByUserId(Long userId, Pageable pageable) {
        return exchangeReturnRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Page<ExchangeReturn> findAllByUserIdAndCategory(Long userId, Byte category, Pageable pageable) {
        return exchangeReturnRepository.findAllByUserIdAndCategory(userId, category, pageable);
    }

    @Override
    public Map<String, String> findExchangeReturnDetails(String ERId) {
        Map<String, String> response = new HashMap<>();
        Long ErId = ParseUtils.parseLong(ERId);
        ExchangeReturn ER = findById(ErId);
        User user = userService.findById(ER.getUser().getId());
        OrderItem orderItem = orderItemService.findById(ER.getOrderItem().getId());
        String bookTitle = orderItem.getBook().getTitle();
        String question = ER.getQuestion();
        String answer = ER.getAnswer();
        String email = user.getEmail();
        String name = user.getName();
        String tel = user.getTel();
        // 카테고리 숫자를 문자열로 변환
        String category = switch (ER.getCategory()) {
            case 1 -> "교환";
            case 2 -> "환불";
            default -> "기타";
        };
        response.put("success", "true");
        response.put("bookTitle", bookTitle);
        response.put("question", question);
        response.put("answer", answer);
        response.put("email", email);
        response.put("name", name);
        response.put("category", category);
        response.put("tel", tel);
        // 이미지 파일 경로 찾기
        String imagePath = fileUploadService.findImageFilePath(ErId, "er");
        if (imagePath != null) {
            response.put("imagePath", imagePath);
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Boolean> addExchangeReturn(ExchangeReturn er, long orderItemId, User user, MultipartFile file) {
        Map<String, Boolean> response = new HashMap<>();
        OrderItem orderItem = orderItemService.findById(orderItemId);
        OrderItem newOrderItem = OrderItem.builder()
                .id(orderItem.getId())
                .book(orderItem.getBook())
                .orders(orderItem.getOrders())
                .quantity(orderItem.getQuantity())
                .status((byte) 4)
                .build();
        orderItemService.update(newOrderItem);
        ExchangeReturn newExchangeReturn = ExchangeReturn.builder()
                .user(user)
                .orderItem(orderItem)
                .category(er.getCategory())
                .question(er.getQuestion())
                .build();
        ExchangeReturn addExchangeReturn = create(newExchangeReturn);
        if (file != null && !file.isEmpty()) {
            fileUploadService.uploadFile(file, addExchangeReturn.getId().toString(), "er");
        }
        Orders orders = ordersService.findById(orderItem.getOrders().getId());
        Orders newOrders = Orders.builder()
                .id(orders.getId())
                .user(orders.getUser())
                .delivery(orders.getDelivery())
                .payment(orders.getPayment())
                .status((byte) 3)
                .orderItems(orders.getOrderItems())
                .build();
        ordersService.update(newOrders);
        response.put("success", true);
        return response;
    }

}