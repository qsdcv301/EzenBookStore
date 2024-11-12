package ezen.team.ezenbookstore.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/category")
public class AdminCategoryApiController {

    // 국내도서 카테고리 목록 조회
    @PostMapping("/list/domestic")
    public String getDomesticCategoryList() {
        // 국내도서 카테고리 목록 조회 로직
        return "redirect:/admin/category"; // 국내도서 목록 페이지로 리다이렉트
    }

    // 국외도서 카테고리 목록 조회
    @PostMapping("/list/international")
    public String getInternationalCategoryList() {
        // 국외도서 카테고리 목록 조회 로직
        return "redirect:/admin/category"; // 국외도서 목록 페이지로 리다이렉트
    }

    // 카테고리 추가 (국내 또는 국외 구분)
    @PostMapping("/add")
    public String addCategory(@RequestParam String categoryName, @RequestParam String categoryType) {
        // categoryType이 "domestic"이면 국내, "international"이면 국외로 구분하여 추가 로직
        return "redirect:/admin/category";
    }

    // 카테고리 수정
    @PostMapping("/update")
    public String updateCategory(@RequestParam Long categoryId, @RequestParam String newCategoryName) {
        // 카테고리 수정 로직
        return "redirect:/admin/category";
    }

    // 카테고리 삭제
    @PostMapping("/delete")
    public String deleteCategory(@RequestParam Long categoryId) {
        // 카테고리 삭제 로직
        return "redirect:/admin/category";
    }
}
