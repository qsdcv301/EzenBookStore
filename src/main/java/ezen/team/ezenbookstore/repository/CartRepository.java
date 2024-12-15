package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByUserId(Long userId);
    Cart findByBookIdAndUserId(Long bookId, Long userId);
}
