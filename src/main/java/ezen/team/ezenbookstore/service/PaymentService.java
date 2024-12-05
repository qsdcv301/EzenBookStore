package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ezen.team.ezenbookstore.util.FormatUtils.getStartOfMonth;

@Service
@RequiredArgsConstructor
public class PaymentService implements PaymentServiceInterface {

    private final PaymentRepository paymentRepository;
    private final DeliveryService deliveryService;
    private final OrdersService ordersService;
    private final OrderItemService orderItemService;
    private final BookService bookService;
    private final CartService cartService;

    @Override
    public Payment findById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Payment create(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Payment update(Long id, Payment payment) {
        Payment newPayment = Payment.builder()
                .id(id)
                .user(payment.getUser())
                .paymentDate(payment.getPaymentDate())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .paymentCode(payment.getPaymentCode())
                .build();
        return paymentRepository.save(newPayment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }

    @Override
    public List<Payment> findAllByUserId(Long userId) {
        return paymentRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processPayment(User user, String paymentCode, String userName, String addr, String addrextra, String tel,
                                  Long amount, List<String> titleList, List<Integer> quantityList, List<Long> cartIdList) {
        try {
            if (titleList.size() != quantityList.size()) {
                return false;
            }

            Delivery delivery = Delivery.builder()
                    .status((byte) 1)
                    .name(userName)
                    .tel(tel)
                    .addr(addr)
                    .addrextra(addrextra)
                    .build();
            Delivery newDelivery = deliveryService.create(delivery);

            Payment payment = Payment.builder()
                    .user(user)
                    .amount(amount)
                    .status((byte) 1)
                    .paymentCode(paymentCode)
                    .build();
            Payment newPayment = create(payment);

            Orders orders = Orders.builder()
                    .user(user)
                    .delivery(newDelivery)
                    .payment(newPayment)
                    .status((byte) 1)
                    .build();
            Orders newOrders = ordersService.create(orders);

            for (int i = 0; i < titleList.size(); i++) {
                String title = titleList.get(i);
                Integer quantity = quantityList.get(i);
                if (cartIdList != null) {
                    Long cartId = cartIdList.get(i);
                    cartService.deleteById(cartId);
                }
                Book book = bookService.findByTitle(title);
                OrderItem orderItem = OrderItem.builder()
                        .book(book)
                        .orders(newOrders)
                        .quantity(quantity)
                        .status((byte) 1)
                        .build();
                orderItemService.create(orderItem);

                Book updatedBook = Book.builder()
                        .id(book.getId())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .publisher(book.getPublisher())
                        .publishDate(book.getPublishDate())
                        .isbn(book.getIsbn())
                        .stock(book.getStock() - quantity)
                        .ifkr(book.getIfkr())
                        .price(book.getPrice())
                        .category(book.getCategory())
                        .subcategory(book.getSubcategory())
                        .count(book.getCount())
                        .discount(book.getDiscount())
                        .bookdescription(book.getBookdescription())
                        .build();
                bookService.update(updatedBook);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Double findTotalAmount(){
        return paymentRepository.findTotalAmount();
    }

    @Override
    public Object[] findTotalAmountAndCountSinceMidnight(LocalDateTime startOfToday) {
        // 직접 전달받은 startOfToday 값을 사용하도록 수정
        return paymentRepository.findTotalAmountAndCountSinceMidnight(startOfToday);
    }

    @Override
    public Object[] findTotalAmountAndCountSinceStartOfMonth() {
        // 이번 달의 시작 날짜를 구해서 전달하도록 수정
        LocalDateTime startOfMonth = getStartOfMonth();
        return paymentRepository.findTotalAmountAndCountSinceStartOfMonth(startOfMonth);
    }

    @Override
    public List<Long> findMonthlyAmountsUpToCurrentMonth() {
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        int currentMonth = today.getMonthValue();

        List<Object[]> result = paymentRepository.findMonthlyAmountsUpToCurrentMonth(currentYear, currentMonth);

        // 결과에서 amount 값만 추출하여 List<Long> 형태로 변환
        return result.stream()
                .map(row -> (Long) row[1])
                .collect(Collectors.toList());
    }

    @Override
    public Long countByStatus2() {
        return paymentRepository.countByStatus2();
    }

}