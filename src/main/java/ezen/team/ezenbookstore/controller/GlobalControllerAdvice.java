package ezen.team.ezenbookstore.controller;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.CartService;
import ezen.team.ezenbookstore.service.CategoryService;
import ezen.team.ezenbookstore.service.SubCategoryService;
import ezen.team.ezenbookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserService userService;
    private final CartService cartService;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;

    @ModelAttribute
    public void findUser(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object userData = auth.getPrincipal();
            if (userData instanceof User user) {
                model.addAttribute("user", user);
                model.addAttribute("userData", true);
                Integer cartSize =  cartService.findAllByUserId(user.getId()).size();
                model.addAttribute("cartSize", cartSize);
            } else if (userData instanceof CustomOAuth2User customOAuth2User) {
                User customUser = userService.findByEmail(customOAuth2User.getEmail());
                model.addAttribute("user", customUser);
                model.addAttribute("userData", true);
                Integer cartSize =  cartService.findAllByUserId(customUser.getId()).size();
                model.addAttribute("cartSize", cartSize);
            } else {
                model.addAttribute("userData", false);
            }
        } catch (Exception e) {
            model.addAttribute("userData", false);
        }
    }

    @ModelAttribute
    public void findCategory(Model model) {
        try {
            List<Category> categoryList = categoryService.findAll();
            List<SubCategory> subCategoryList = subCategoryService.findAll();
            model.addAttribute("categoryList", categoryList);
            model.addAttribute("subCategoryList", subCategoryList);
        } catch (Exception e) {
            model.addAttribute("categoryData", false);
        }
    }

}