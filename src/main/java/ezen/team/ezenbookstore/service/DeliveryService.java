package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Delivery;
import ezen.team.ezenbookstore.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public Delivery findById(Long id) {
        return deliveryRepository.findById(id).orElse(null);
    }

    public List<Delivery> findByAll() {
        return deliveryRepository.findAll();
    }

    public Delivery create(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

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

    public void delete(Long id) {
        deliveryRepository.deleteById(id);
    }

}