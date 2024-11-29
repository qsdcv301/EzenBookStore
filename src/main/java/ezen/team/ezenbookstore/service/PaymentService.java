package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final DeliveryService deliveryService;
    private final OrdersService ordersService;
    private final OrderItemService orderItemService;
    private final BookService bookService;
    private final CartService cartService;

    public Payment findById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public Payment create(Payment payment) {
        return paymentRepository.save(payment);
    }

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

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }

    public List<Payment> findAllByUserId(Long userId) {
        return paymentRepository.findAllByUserId(userId);
    }

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

}