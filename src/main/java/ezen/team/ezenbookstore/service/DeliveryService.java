package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Delivery;
import ezen.team.ezenbookstore.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeliveryService implements DeliveryServiceInterface {

    private final DeliveryRepository deliveryRepository;

    @Override
    public Delivery findById(Long id) {
        return deliveryRepository.findById(id).orElse(null);
    }

    @Override
    public List<Delivery> findAll() {
        return deliveryRepository.findAll();
    }

    @Override
    public Delivery create(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    @Override
    public Delivery update(Long id, Delivery delivery) {
        Delivery newDelivery = Delivery.builder()
                .id(id)
                .trackingNum(delivery.getTrackingNum())
                .startDate(delivery.getStartDate())
                .endDate(delivery.getEndDate())
                .status(delivery.getStatus())
                .name(delivery.getName())
                .tel(delivery.getTel())
                .addr(delivery.getAddr())
                .addrextra(delivery.getAddrextra())
                .build();
        return deliveryRepository.save(newDelivery);
    }

    @Override
    public void delete(Long id) {
        deliveryRepository.deleteById(id);
    }

    @Override
    public Long countByStatus1() {
        return deliveryRepository.countByStatus1();
    }

    @Override
    public Long countByStatus2() {
        return deliveryRepository.countByStatus2();
    }

    @Override
    public Long countByStatus3() {
        return deliveryRepository.countByStatus3();
    }

    @Override
    public Long countByStatusIn456() {
        return deliveryRepository.countByStatusIn456();
    }


    public Map<String, Object> getDeliveryCountsByStatus() {
        Map<String, Object> deliveryCounts = new HashMap<>();

        // 전체 건수 및 상태별 건수 계산
        deliveryCounts.put("totalCount", deliveryRepository.count());
        deliveryCounts.put("preparingCount", deliveryRepository.countByDeliveryStatus((byte) 1)); // 배송 준비중
        deliveryCounts.put("shippingCount", deliveryRepository.countByDeliveryStatus((byte) 2)); // 배송중
        deliveryCounts.put("completedCount", deliveryRepository.countByDeliveryStatus((byte) 3)); // 배송 완료
        deliveryCounts.put("returnPreparingCount", deliveryRepository.countByDeliveryStatus((byte) 4)); // 반송 준비중
        deliveryCounts.put("returnShippingCount", deliveryRepository.countByDeliveryStatus((byte) 5)); // 반송중
        deliveryCounts.put("returnCompletedCount", deliveryRepository.countByDeliveryStatus((byte) 6)); // 반송 완료

        return deliveryCounts;
    }

    public void updateStatus(Delivery delivery) {
        deliveryRepository.save(delivery);
    }
}
