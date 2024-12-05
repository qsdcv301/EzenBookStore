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
    private String totalCountSinceMidnight;
    private String totalAmountSinceStartOfMonth;
    private String totalCountSinceStartOfMonth;
    private String delivery1Count;
    private String delivery2Count;
    private String delivery3Count;
    private String delivery456Count;
    private String orders2Count;
    private String orders3Count;
    private String payment2Count;
    private List<Long> findMonthlyAmountsUpToCurrentMonth;
}