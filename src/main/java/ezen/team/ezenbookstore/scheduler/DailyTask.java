package ezen.team.ezenbookstore.scheduler;

import ezen.team.ezenbookstore.entity.OrderItem;
import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.service.OrderItemService;
import ezen.team.ezenbookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class DailyTask {

    private final OrderItemService orderItemService;
    private final UserService userService;

    @Scheduled(cron = "0 0 6 * * ?") // 매일 6시
    public void executeTask() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        Timestamp timestamp = Timestamp.valueOf(sevenDaysAgo);
        List<OrderItem> orderItemList = orderItemService.findAllByOrders_OrderDateBeforeAndStatus(timestamp);
        System.out.println("업데이트할 항목 개수: " + orderItemList.size());
        Map<Long, Integer> pointUpdateList = new HashMap<>();
        for (OrderItem orderItem : orderItemList) {
            Long userId = orderItem.getOrders().getUser().getId();
            Integer bookPrice = orderItem.getBook().getPrice();
            Integer bookQuantity = orderItem.getQuantity();
            Integer addPoint = (int) Math.floor((bookPrice * bookQuantity) * 0.005);
            pointUpdateList.compute(userId, (k, v) -> (v == null ? addPoint : v + addPoint));
            OrderItem newOrderItem = orderItem.toBuilder()
                    .status((byte) 3)
                    .build();
            orderItemService.update(newOrderItem);
        }
        for (Map.Entry<Long, Integer> entry : pointUpdateList.entrySet()) {
            Long userId = entry.getKey();
            Integer addPoint = entry.getValue();
            User user = userService.findById(userId);
            User newUser = user.toBuilder()
                    .point(user.getPoint() + addPoint)
                    .build();
            userService.update(newUser);
        }
        System.out.println("매일 새벽 6시에 실행: " + System.currentTimeMillis());
    }

}
