package ezen.team.ezenbookstore.dto;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Builder(toBuilder = true)
public class AdminDashBoardDto {
    private String ordersCount;
    private String userCount;
    private String paymentTotalAmount;
    private String qnACount;
    private String totalAmountSinceMidnight;
    private String totalAmountSinceStartOfMonth;
    private List<Long> findMonthlyAmountsUpToCurrentMonth;

}
