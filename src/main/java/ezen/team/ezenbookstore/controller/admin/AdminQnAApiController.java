package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.QnA;
import ezen.team.ezenbookstore.service.QnAService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/qna")
public class AdminQnAApiController {

    private final QnAService qnAService;

    @GetMapping("")
    public String qnAControl(@PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            Model model) {
        Page<QnA> qnAPage = qnAService.findAll(pageable);
        int totalPages = qnAPage.getTotalPages(); // 총 페이지 수
        int currentPage = qnAPage.getNumber();
        int pageGroupSize = 10; // 페이지 그룹 크기
        int startPage = Math.max(0, (currentPage / pageGroupSize) * pageGroupSize); // 시작 페이지 번호
        int endPage = Math.min(startPage + pageGroupSize, totalPages); // 끝 페이지 번호

        model.addAttribute("qnAPage", qnAPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/admin/qnaControl";
    }
    //
    // QnA 상세 조회 (답변 작성 시 조회)
    @PostMapping("/detail")
    public String getQnADetail(@RequestParam Long qnaId) {
        // 특정 QnA 상세 조회 로직
        return "redirect:/admin/qna"; // 상세 페이지로 리다이렉트
    }

    // QnA 삭제
//    @PostMapping("/delete")
//    public String deleteQnA(@RequestParam Long qnaId) {
//        // QnA 삭제 로직
//        return "redirect:/admin/qna"; // 답변 삭제 후 QnA 목록 페이지로 리다이렉트
//    }

    // QnA 답변 작성
    @PostMapping("/answer")
    public String addAnswer(@RequestParam Long qnaId, @RequestParam String answerContent) {
        // QnA 답변 작성 로직
        return "redirect:/admin/qna"; // 답변 작성 후 QnA 목록 페이지로 리다이렉트
    }

    // QnA 답변 삭제 << 필요하면
//    @PostMapping("/answer/delete")
//    public String deleteAnswer(@RequestParam Long qnaId) {
//        // QnA 답변 삭제 로직
//        return "redirect:/admin/qna"; // 답변 삭제 후 QnA 목록 페이지로 리다이렉트
//    }

}
