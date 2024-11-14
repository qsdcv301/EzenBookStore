package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByUserId(Long userId);
}
