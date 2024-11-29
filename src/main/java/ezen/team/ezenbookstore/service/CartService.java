package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Cart;
import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.repository.CartRepository;
import ezen.team.ezenbookstore.util.ParseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final BookService bookService;
    private final FileUploadService fileUploadService;

    public Cart findById(Long id) {
        return cartRepository.findById(id).orElse(null);
    }

    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public Cart create(Cart cart) {
        return cartRepository.save(cart);
    }

    @Transactional(rollbackFor = Exception.class)
    public Cart update(Cart cart) {
        Cart newCart = Cart.builder()
                .id(cart.getId())
                .user(cart.getUser())
                .book(cart.getBook())
                .quantity(cart.getQuantity())
                .build();
        return cartRepository.save(newCart);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        cartRepository.deleteById(id);
    }

    public List<Cart> findAllByUserId(Long userId) {
        return cartRepository.findAllByUserId(userId);
    }

    public void populateCartModel(User user, Model model) {
        List<Cart> cartList = findAllByUserId(user.getId());
        List<String> imageList = new ArrayList<>();
        for (Cart cart : cartList) {
            String imagePath = fileUploadService.findImageFilePath(cart.getBook().getId(), "book");
            imageList.add(imagePath != null ? imagePath : "");
        }
        model.addAttribute("imageList", imageList);
        model.addAttribute("cartList", cartList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addItemsToCart(User user, List<String> bookIdList, List<String> quantityList) {
        if (bookIdList == null || bookIdList.isEmpty() || quantityList == null || quantityList.isEmpty() || bookIdList.size() != quantityList.size()) {
            throw new IllegalArgumentException("요청 데이터가 유효하지 않습니다.");
        }

        for (int i = 0; i < bookIdList.size(); i++) {
            Long bookIdValue = ParseUtils.parseLong(bookIdList.get(i));
            Integer quantityValue = ParseUtils.parseInt(quantityList.get(i));

            Cart newCart = Cart.builder()
                    .user(user)
                    .book(bookService.findById(bookIdValue))
                    .quantity(quantityValue)
                    .build();

            create(newCart);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCartItems(User user, List<String> cartIdList, List<String> quantityList) {
        if (cartIdList == null || cartIdList.isEmpty() || quantityList == null || quantityList.isEmpty() || cartIdList.size() != quantityList.size()) {
            throw new IllegalArgumentException("요청 데이터가 유효하지 않습니다.");
        }

        for (int i = 0; i < cartIdList.size(); i++) {
            Long cartIdValue = ParseUtils.parseLong(cartIdList.get(i));
            Integer quantityValue = ParseUtils.parseInt(quantityList.get(i));
            Cart cart = findById(cartIdValue);
            if (cart != null && cart.getUser().getId().equals(user.getId())) {
                cart.setQuantity(quantityValue);
                update(cart);
            } else {
                throw new IllegalArgumentException("권한이 없거나 잘못된 요청입니다.");
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteCartItems(User user, List<String> cartIdList) {
        if (cartIdList == null || cartIdList.isEmpty()) {
            throw new IllegalArgumentException("요청 데이터가 유효하지 않습니다.");
        }

        for (String cartIdStr : cartIdList) {
            Long cartIdValue = ParseUtils.parseLong(cartIdStr);
            Cart cart = findById(cartIdValue);
            if (cart != null && cart.getUser().getId().equals(user.getId())) {
                deleteById(cartIdValue);
            } else {
                throw new IllegalArgumentException("권한이 없거나 잘못된 요청입니다.");
            }
        }
    }
}