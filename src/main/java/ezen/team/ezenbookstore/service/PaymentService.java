package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Payment;
import ezen.team.ezenbookstore.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment findById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Payment create(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment update(Long id, Payment payment) {
        Payment newPayment = findById(id);
        newPayment.Builder(newPayment.getId(), payment.getOrderId(), payment.getAmount(),
                payment.getMethod(), payment.getStatus());
        return paymentRepository.save(newPayment);
    }

    public void delete(Long id) {
        paymentRepository.deleteById(id);
    }
}
