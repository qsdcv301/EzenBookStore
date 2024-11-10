package ezen.team.ezenbookstore.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/user")
public class AdminUserApiController {

    // 회원 목록 조회
    @PostMapping("/list")
    public String getUserList(@RequestParam(required = false) String memberLevel) {
        // 선택된 회원 등급에 따른 회원 목록 조회 로직
        return "redirect:/admin/user"; // 회원 목록 페이지로 리다이렉트
    }

    // 회원 검색
    @PostMapping("/search")
    public String searchUser(@RequestParam String keyword) {
        // 검색어를 기준으로 회원을 검색하는 로직
        return "redirect:/admin/user"; // 검색 결과 페이지로 리다이렉트
    }

    // 회원 상세 조회
    @PostMapping("/detail")
    public String getUserDetail(@RequestParam Long userId) {
        // 특정 회원 상세 조회 로직
        return "redirect:/admin/user"; // 상세 페이지로 리다이렉트
    }

    // 회원 정보 수정
    @PostMapping("/update")
    public String updateUser(@RequestParam Long userId, @RequestParam String memberLevel) {
        // 회원 정보 수정 로직 (예: 회원 등급 수정)
        return "redirect:/admin/user"; // 수정 후 회원 목록 페이지로 리다이렉트
    }
}
