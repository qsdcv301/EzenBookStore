package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.QnA;
import ezen.team.ezenbookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
//@RequestMapping("/qna")
public class QnAApiController {
    @GetMapping("/qna")
    public String viewQnA() {
        return "qna";
    }
    //qna 중 q하기
    @PostMapping("/add")
    public String addQnA(@ModelAttribute QnA qna) {
        return "redirect:/qna";
    }

    //qna 중 q수정
    @PostMapping("/update")
    public String updateQnA(@ModelAttribute QnA qna) {
        return "redirect:/qna";
    }

    //qna 삭제
    @PostMapping("/delete")
    public String deleteQnA(@RequestParam long qnaId) {
        return "redirect:/qna";
    }

}
