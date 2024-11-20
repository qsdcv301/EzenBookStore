package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.Category;
import ezen.team.ezenbookstore.entity.SubCategory;
import ezen.team.ezenbookstore.service.CategoryService;
import ezen.team.ezenbookstore.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/categories") // 기본 경로를 좀 더 명확하게 수정
public class AdminCategoryApiController {

    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;

    // 모든 카테고리 리스트 반환
    @GetMapping("")
    public String categoryControl(Model model) {
        List<Category> categoryList = categoryService.findAll();
        System.out.println("카테고리 리스트: " + categoryList);
        model.addAttribute("categoryList", categoryList);
        return "/admin/categoryControl";
    }


    @GetMapping("/{categoryId}/subcategories")
    public ResponseEntity<List<SubCategory>> getSubcategories(@PathVariable Long categoryId) {
        List<SubCategory> subcategories = subCategoryService.getSubCategoriesByCategoryId(categoryId);
        if (subcategories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(subcategories);
    }

}
