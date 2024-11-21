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
import java.util.HashMap;
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
    public String getCategoryList(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        // 전체 카테고리 리스트 가져오기
        List<Category> categoryList;

        if (keyword != null && !keyword.isEmpty()) {
            // type에 따라 검색 필터링 적용
                categoryList = categoryService.findCategoriesByName(keyword);
        } else {
            categoryList = categoryService.findAll();
        }

        // 필터링 후 총 카테고리 수
        long totalCategories = categoryList.size();

        // 페이지네이션 처리 (0-based index)
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, categoryList.size());

        List<Category> paginatedCategories;
        if (fromIndex >= categoryList.size()) {
            paginatedCategories = Collections.emptyList();
        } else {
            paginatedCategories = categoryList.subList(fromIndex, toIndex);
        }

        // 총 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalCategories / size);

        // 모델에 데이터 추가
        model.addAttribute("categoryList", paginatedCategories); // 필터링된 카테고리 리스트 (페이지네이션 적용됨)
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalCount", totalCategories); // 총 카테고리 수
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageSize", size);

        return "admin/categoryControl";
    }



    @GetMapping("/{categoryId}/subcategories")
    public ResponseEntity<List<SubCategory>> getSubcategories(@PathVariable Long categoryId) {
        List<SubCategory> subcategories = subCategoryService.getSubCategoriesByCategoryId(categoryId);
        if (subcategories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(subcategories);
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, Boolean>> deleteCategories(@RequestBody List<Long> categoryIds) {
        Map<String, Boolean> response = new HashMap<>();

        if (categoryIds == null || categoryIds.isEmpty()) {
            response.put("success", false);
            return ResponseEntity.ok(response); // 삭제 실패 반환
        }

        try {
            for (Long categoryId : categoryIds) {
                categoryService.deleteCategoryWithSubcategories(categoryId);
            }

            response.put("success", true);
            return ResponseEntity.ok(response); // 성공 반환
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 반환
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Boolean>> updateCategories(@RequestBody List<Map<String, String>> categoryUpdates) {
        Map<String, Boolean> response = new HashMap<>();

        if (categoryUpdates == null || categoryUpdates.isEmpty()) {
            response.put("success", false);
            return ResponseEntity.ok(response); // 수정 실패 반환
        }

        try {
            for (Map<String, String> categoryData : categoryUpdates) {
                Long id = Long.parseLong(categoryData.get("id"));
                String name = categoryData.get("name");

                // 카테고리 업데이트
                categoryService.update(id, Category.builder().name(name).build());
            }

            response.put("success", true);
            return ResponseEntity.ok(response); // 성공 반환
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 반환
        }
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Boolean>> addCategory(@RequestBody Category newCategory) {
        Map<String, Boolean> response = new HashMap<>();

        if (newCategory.getName() == null || newCategory.getName().isEmpty()) {
            response.put("success", false);
            return ResponseEntity.badRequest().body(response); // 이름이 없으면 실패 반환
        }

        try {
            categoryService.create(newCategory);
            response.put("success", true);
            return ResponseEntity.ok(response); // 성공 반환
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 오류 반환
        }
    }

    // 서브카테고리 추가
    @PostMapping("/{categoryId}/subcategories")
    public ResponseEntity<Void> addSubCategory(@PathVariable Long categoryId, @RequestBody SubCategory subCategory) {
        subCategoryService.addSubCategory(categoryId, subCategory);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/subcategories/{subCategoryId}/edit")
    public ResponseEntity<Void> editSubCategory(@PathVariable Long subCategoryId, @RequestBody SubCategory subCategory) {
        subCategoryService.update(subCategoryId, subCategory);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/subcategories/{subCategoryId}/delete")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable Long subCategoryId) {
        subCategoryService.delete(subCategoryId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Category>> searchCategories(@RequestParam String keyword) {
        List<Category> categories = categoryService.searchByName(keyword);
        return ResponseEntity.ok(categories);
    }

}
