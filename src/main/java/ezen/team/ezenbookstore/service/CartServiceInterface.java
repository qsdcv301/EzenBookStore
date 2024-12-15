package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Cart;
import ezen.team.ezenbookstore.entity.User;
import org.springframework.ui.Model;

import java.util.List;

public interface CartServiceInterface {

    Cart findById(Long id);

    List<Cart> findAll();

    Cart create(Cart cart);

    Cart update(Cart cart);

    void deleteById(Long id);

    List<Cart> findAllByUserId(Long userId);

    void populateCartModel(User user, Model model);

    void addItemsToCart(User user, List<String> bookIdList, List<String> quantityList);

    void updateCartItems(User user, List<String> cartIdList, List<String> quantityList);

    void deleteCartItems(User user, List<String> cartIdList);

    Cart findByBookIdAndUserId(Long bookId, Long userId);

}
