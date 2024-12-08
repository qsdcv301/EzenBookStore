package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.SubCategory;
import ezen.team.ezenbookstore.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminSubCategoryApiController {

    private final SubCategoryService subCategoryService;

    // 전체 서브카테고리 리스트 반환
    @GetMapping("/subcategories")
    public List<SubCategory> getAllSubCategories() {
        return subCategoryService.findAll();
    }

    // 선택된 카테고리에 해당하는 서브카테고리 목록을 반환하는 엔드포인트
    @GetMapping("/subcategories/{categoryId}")
    public List<SubCategory> getSubCategoriesByCategoryId(@PathVariable Long categoryId) {
        return subCategoryService.getSubCategoriesByCategoryId(categoryId);
    }
}
