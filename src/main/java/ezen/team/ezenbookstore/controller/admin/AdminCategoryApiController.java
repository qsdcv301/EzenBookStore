package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.Category;
import ezen.team.ezenbookstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController // 데이터 반환용 REST 컨트롤러로 변경
@RequestMapping("/admin/categories") // 기본 경로를 좀 더 명확하게 수정
public class AdminCategoryApiController {

    private final CategoryService categoryService;

    // 모든 카테고리 리스트 반환
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    // 기존 View를 반환하는 메서드
    @GetMapping("/view")
    public String categoryControl(Model model) {
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList", categoryList);
        return "/admin/categoryControl";
    }

//    // 국내도서 카테고리 목록 조회
//    @GetMapping("/list/domestic")
//    public List<Category> getDomesticCategoryList() {
//        return categoryService.findDomesticCategories();
//    }
//
//    // 국외도서 카테고리 목록 조회
//    @GetMapping("/list/international")
//    public List<Category> getInternationalCategoryList() {
//        return categoryService.findInternationalCategories();
//    }

    // 카테고리 추가
    @PostMapping("/add")
    public String addCategory(@RequestParam String categoryName, @RequestParam String categoryType) {
        // 추가 로직
        return "redirect:/admin/categories/view";
    }

    // 카테고리 수정
    @PostMapping("/update")
    public String updateCategory(@RequestParam Long categoryId, @RequestParam String newCategoryName) {
        // 수정 로직
        return "redirect:/admin/categories/view";
    }

    // 카테고리 삭제
    @PostMapping("/delete")
    public String deleteCategory(@RequestParam Long categoryId) {
        // 삭제 로직
        return "redirect:/admin/categories/view";
    }
}
