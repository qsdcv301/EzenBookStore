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

    // 오늘 자정부터 현재까지의 총 amount 합계와 개수
    @Query("SELECT SUM(p.amount), COUNT(p) FROM Payment p WHERE p.paymentDate >= :startOfToday")
    Object[] findTotalAmountAndCountSinceMidnight(LocalDateTime startOfToday);

    // 이번 달의 시작부터 현재까지의 총 amount 합계와 개수
    @Query("SELECT SUM(p.amount), COUNT(p) FROM Payment p WHERE p.paymentDate >= :startOfMonth")
    Object[] findTotalAmountAndCountSinceStartOfMonth(LocalDateTime startOfMonth);

    @Query("SELECT MONTH(p.paymentDate), SUM(p.amount) FROM Payment p " +
            "WHERE YEAR(p.paymentDate) = :year AND MONTH(p.paymentDate) <= :month " +
            "GROUP BY MONTH(p.paymentDate) " +
            "ORDER BY MONTH(p.paymentDate)")
    List<Object[]> findMonthlyAmountsUpToCurrentMonth(int year, int month);

    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.status = 2")
    Long countByStatus2();

}
