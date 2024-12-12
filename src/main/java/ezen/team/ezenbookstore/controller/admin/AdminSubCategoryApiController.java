package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.SubCategory;
import ezen.team.ezenbookstore.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/subcategories")
public class AdminSubCategoryApiController {

    private final SubCategoryService subCategoryService;

    // 전체 서브카테고리 리스트 반환
    @GetMapping("")
    public List<SubCategory> getAllSubCategories() {
        return subCategoryService.findAll();
    }

    // 선택된 카테고리에 해당하는 서브카테고리 목록을 반환하는 엔드포인트
    @GetMapping("/{categoryId}")
    public List<SubCategory> getSubCategoriesByCategoryId(@PathVariable Long categoryId) {
        return subCategoryService.getSubCategoriesByCategoryId(categoryId);
    }

    @PostMapping("/{subCategoryId}/edit")
    public ResponseEntity<Void> editSubCategory(@PathVariable Long subCategoryId, @RequestBody SubCategory subCategory) {
        subCategoryService.update(subCategoryId, subCategory);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{subCategoryId}/delete")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable Long subCategoryId) {
        subCategoryService.delete(subCategoryId);
        return ResponseEntity.ok().build();
    }
}
