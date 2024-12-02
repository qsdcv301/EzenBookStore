package ezen.team.ezenbookstore.service.facade;

import ezen.team.ezenbookstore.dto.AdminDashBoardDto;
import ezen.team.ezenbookstore.service.OrdersService;
import ezen.team.ezenbookstore.service.PaymentService;
import ezen.team.ezenbookstore.service.QnAService;
import ezen.team.ezenbookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashBoardServiceImpl implements AdminDashBoardService {

    private final OrdersService ordersService;
    private final UserService userService;
    private final PaymentService paymentService;
    private final QnAService qnAService;

    public AdminDashBoardDto AdminDashBoard() {
        String ordersCount = ordersService.ordersCount().toString();
        String userCount = userService.userCount().toString();
        double paymentAmount = Math.floor(paymentService.findTotalAmount());
        String paymentTotalAmount = Double.toString(paymentAmount);
        String qnACount = qnAService.qnACount().toString();
        String totalAmountSinceMidnight = paymentService.findTotalAmountSinceMidnight().toString();
        String totalAmountSinceStartOfMonth = paymentService.findTotalAmountSinceStartOfMonth().toString();
        List<Long> findMonthlyAmountsUpToCurrentMonth = paymentService.findMonthlyAmountsUpToCurrentMonth();
        return AdminDashBoardDto.builder()
                .ordersCount(ordersCount)
                .userCount(userCount)
                .paymentTotalAmount(paymentTotalAmount)
                .qnACount(qnACount)
                .totalAmountSinceMidnight(totalAmountSinceMidnight)
                .totalAmountSinceStartOfMonth(totalAmountSinceStartOfMonth)
                .findMonthlyAmountsUpToCurrentMonth(findMonthlyAmountsUpToCurrentMonth)
                .build();
    }

}
