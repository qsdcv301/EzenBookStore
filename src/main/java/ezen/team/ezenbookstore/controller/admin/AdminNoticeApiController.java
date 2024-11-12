package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.Event;
import ezen.team.ezenbookstore.entity.Notice;
import ezen.team.ezenbookstore.service.EventService;
import ezen.team.ezenbookstore.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/notice")
public class AdminNoticeApiController {

    private final NoticeService noticeService;

    @GetMapping
    public String noticeControl(Model model) {

        List<Notice> noticeList = noticeService.findAll();

        model.addAttribute("noticeList", noticeList);

        return "/admin/noticeEventControl";
    }

    // 공지사항 생성
    @PostMapping("/add")
    public String addNotice(@RequestBody Notice notice) {
        // 공지사항 생성 로직
        return "redirect:/admin/notice-event";
    }

    // 공지사항 수정
    @PostMapping("/update")
    public String updateNotice(@RequestBody Notice notice) {
        // 공지사항 수정 로직
        return "redirect:/admin/notice-event";
    }

    // 공지사항 삭제
    @PostMapping("/delete")
    public String deleteNotice(@RequestParam Long noticeId) {
        // 공지사항 삭제 로직
        return "redirect:/admin/notice-event";
    }
    }
