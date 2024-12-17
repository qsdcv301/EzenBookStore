package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Payment;
import ezen.team.ezenbookstore.entity.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface PaymentServiceInterface {

    Payment findById(Long id);

    List<Payment> findAll();

    Payment create(Payment payment);

    Payment update(Long id, Payment payment);

    void deleteById(Long id);

    List<Payment> findAllByUserId(Long userId);

    boolean processPayment(User user, String paymentCode, String userName, String addr, String addrextra, String tel,
                           Long amount, List<String> titleList, List<Integer> quantityList, List<Long> cartIdList);

    Double findTotalAmount();

    List<Long> findTotalAmountAndCountSinceMidnight();

    List<Long> findTotalAmountAndCountSinceStartOfMonth();

    List<Long> findMonthlyAmountsUpToCurrentMonth();

    Long countByStatus2();

}
