package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByUserId(Long userId);

    @Query("SELECT SUM(p.amount) FROM Payment p")
    Double findTotalAmount();

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentDate >= CURRENT_DATE")
    Double findTotalAmountSinceMidnight();

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentDate >= :startOfMonth")
    Double findTotalAmountSinceStartOfMonth(LocalDateTime startOfMonth);

    @Query("SELECT MONTH(p.paymentDate), SUM(p.amount) FROM Payment p " +
            "WHERE YEAR(p.paymentDate) = :year AND MONTH(p.paymentDate) <= :month " +
            "GROUP BY MONTH(p.paymentDate) " +
            "ORDER BY MONTH(p.paymentDate)")
    List<Object[]> findMonthlyAmountsUpToCurrentMonth(int year, int month);

}
