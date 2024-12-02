package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.status = 1")
    Long countByStatus1();

    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.status = 2")
    Long countByStatus2();

    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.status = 3")
    Long countByStatus3();

    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.status IN (4, 5, 6)")
    Long countByStatusIn456();

}