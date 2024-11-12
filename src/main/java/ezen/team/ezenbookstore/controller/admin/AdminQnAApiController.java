package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.QnA;
import ezen.team.ezenbookstore.service.QnAService;
import lombok.RequiredArgsConstructor;
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
    public String qnAControl(Model model) {
        List<QnA> qnAList = qnAService.findAll();

        model.addAttribute("qnAList", qnAList);

        return "/admin/qnaControl";
    }

    // QnA 목록 조회
    @PostMapping("/list")
    public String getQnAList(@RequestParam(required = false) String category) {
        // 선택된 카테고리에 따른 QnA 목록 조회 로직
        return "redirect:/admin/qna"; // QnA 목록 페이지로 리다이렉트
    }

    // QnA 상세 조회 (답변 작성 시 조회)
    @PostMapping("/detail")
    public String getQnADetail(@RequestParam Long qnaId) {
        // 특정 QnA 상세 조회 로직
        return "redirect:/admin/qna"; // 상세 페이지로 리다이렉트
    }

    // QnA 삭제
    @PostMapping("/delete")
    public String deleteQnA(@RequestParam Long qnaId) {
        // QnA 삭제 로직
        return "redirect:/admin/qna"; // 답변 삭제 후 QnA 목록 페이지로 리다이렉트
    }

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
