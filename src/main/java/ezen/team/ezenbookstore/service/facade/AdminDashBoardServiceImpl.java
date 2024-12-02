package ezen.team.ezenbookstore.service.facade;

import ezen.team.ezenbookstore.dto.AdminDashBoardDto;
import ezen.team.ezenbookstore.service.*;
import ezen.team.ezenbookstore.util.FormatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashBoardServiceImpl implements AdminDashBoardService {

    private final OrdersService ordersService;
    private final UserService userService;
    private final PaymentService paymentService;
    private final QnAService qnAService;
    private final DeliveryService deliveryService;

    public AdminDashBoardDto AdminDashBoard() {
        // 서비스 호출을 각각 변수에 저장하여 중복 호출 제거
        Long ordersCountValue = ordersService.ordersCount();
        Long userCountValue = userService.userCount();
        Double paymentAmountValue = paymentService.findTotalAmount();
        Long qnACountValue = qnAService.countByAnswerIsNullOrEmpty();
        Long delivery1CountValue = deliveryService.countByStatus1();
        Long delivery2CountValue = deliveryService.countByStatus2();
        Long delivery3CountValue = deliveryService.countByStatus3();
        Long delivery456CountValue = deliveryService.countByStatusIn456();
        Long orders2CountValue = ordersService.countByStatus2();
        Long orders3CountValue = ordersService.countByStatus3();
        Long payment2CountValue = paymentService.countByStatus2();

        // 오늘 자정부터 현재까지의 총 amount와 개수 가져오기
        Object[] totalAmountAndCountSinceMidnight = paymentService.findTotalAmountAndCountSinceMidnight(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));
        double totalAmountSinceMidnightValue = 0.0;
        long countSinceMidnightValue = 0L;

        if (totalAmountAndCountSinceMidnight.length > 0 && totalAmountAndCountSinceMidnight[0] instanceof Number) {
            totalAmountSinceMidnightValue = ((Number) totalAmountAndCountSinceMidnight[0]).doubleValue();
        }
        if (totalAmountAndCountSinceMidnight.length > 1 && totalAmountAndCountSinceMidnight[1] instanceof Number) {
            countSinceMidnightValue = ((Number) totalAmountAndCountSinceMidnight[1]).longValue();
        }

        // 이번 달의 시작부터 현재까지의 총 amount와 개수 가져오기
        Object[] totalAmountAndCountSinceStartOfMonth = paymentService.findTotalAmountAndCountSinceStartOfMonth();
        double totalAmountSinceStartOfMonthValue = 0.0;
        long countSinceStartOfMonthValue = 0L;

        if (totalAmountAndCountSinceStartOfMonth.length > 0 && totalAmountAndCountSinceStartOfMonth[0] instanceof Number) {
            totalAmountSinceStartOfMonthValue = ((Number) totalAmountAndCountSinceStartOfMonth[0]).doubleValue();
        }
        if (totalAmountAndCountSinceStartOfMonth.length > 1 && totalAmountAndCountSinceStartOfMonth[1] instanceof Number) {
            countSinceStartOfMonthValue = ((Number) totalAmountAndCountSinceStartOfMonth[1]).longValue();
        }

        List<Long> monthlyAmounts = paymentService.findMonthlyAmountsUpToCurrentMonth();

        // 값 할당 및 처리
        String ordersCount = ordersCountValue != null ? FormatUtils.formatKorea(ordersCountValue) : "0";
        String userCount = userCountValue != null ? FormatUtils.formatKorea(userCountValue) : "0";

        Long paymentAmount = paymentAmountValue != null ? (long) Math.floor(paymentAmountValue) : 0L;
        String paymentTotalAmount = FormatUtils.formatCurrency(paymentAmount);

        String qnACount = qnACountValue != null ? FormatUtils.formatKorea(qnACountValue) : "0";

        Long longTotalAmountSinceMidnight = totalAmountAndCountSinceMidnight[0] != null ? (long) Math.floor(totalAmountSinceMidnightValue) : 0L;
        String totalAmountSinceMidnight = FormatUtils.formatCurrency(longTotalAmountSinceMidnight);
        String totalCountSinceMidnight = FormatUtils.formatKorea(countSinceMidnightValue);

        Long longTotalAmountSinceStartOfMonth = totalAmountAndCountSinceStartOfMonth[0] != null ? (long) Math.floor(totalAmountSinceStartOfMonthValue) : 0L;
        String totalAmountSinceStartOfMonth = FormatUtils.formatCurrency(longTotalAmountSinceStartOfMonth);
        String totalCountSinceStartOfMonth = FormatUtils.formatKorea(countSinceStartOfMonthValue);

        String delivery1Count = FormatUtils.formatKorea(delivery1CountValue);
        String delivery2Count = FormatUtils.formatKorea(delivery2CountValue);
        String delivery3Count = FormatUtils.formatKorea(delivery3CountValue);
        String delivery456Count = FormatUtils.formatKorea(delivery456CountValue);
        String orders2Count = FormatUtils.formatKorea(orders2CountValue);
        String orders3Count = FormatUtils.formatKorea(orders3CountValue);
        String payment2Count = FormatUtils.formatKorea(payment2CountValue);

        List<Long> findMonthlyAmountsUpToCurrentMonth = monthlyAmounts != null ? monthlyAmounts : new ArrayList<>();

        // DTO 생성 및 반환
        return AdminDashBoardDto.builder()
                .ordersCount(ordersCount)
                .userCount(userCount)
                .paymentTotalAmount(paymentTotalAmount)
                .qnACount(qnACount)
                .totalAmountSinceMidnight(totalAmountSinceMidnight)
                .totalCountSinceMidnight(totalCountSinceMidnight)
                .totalAmountSinceStartOfMonth(totalAmountSinceStartOfMonth)
                .totalCountSinceStartOfMonth(totalCountSinceStartOfMonth)
                .delivery1Count(delivery1Count)
                .delivery2Count(delivery2Count)
                .delivery3Count(delivery3Count)
                .delivery456Count(delivery456Count)
                .orders2Count(orders2Count)
                .orders3Count(orders3Count)
                .payment2Count(payment2Count)
                .findMonthlyAmountsUpToCurrentMonth(findMonthlyAmountsUpToCurrentMonth)
                .build();
    }

}
