package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Cart;
import ezen.team.ezenbookstore.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public Cart findById(Long id) {
        return cartRepository.findById(id).orElse(null);
    }

    public List<Cart> findAll(Long id) {
        return cartRepository.findAll();
    }

    public Cart create(Cart cart) {
        return cartRepository.save(cart);
    }

    public Cart update(Long id, Cart cart) {
        Cart newCart = Cart.builder()
                .id(id)
                .userId(cart.getUserId())
                .bookId(cart.getBookId())
                .quantity(cart.getQuantity())
                .build();
        return cartRepository.save(newCart);
    }

    public void delete(Long id) {
        cartRepository.deleteById(id);
    }
}
