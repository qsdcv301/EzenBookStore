package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.Event;
import ezen.team.ezenbookstore.entity.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminNoticeEventApiController {


    // 공지사항 생성
    @PostMapping("/notice/add")
    public String addNotice(@RequestBody Notice notice) {
        // 공지사항 생성 로직
        return "redirect:/admin/notice-event";
    }

    // 공지사항 수정
    @PostMapping("/notice/update")
    public String updateNotice(@RequestBody Notice notice) {
        // 공지사항 수정 로직
        return "redirect:/admin/notice-event";
    }

    // 공지사항 삭제
    @PostMapping("/notice/delete")
    public String deleteNotice(@RequestParam Long noticeId) {
        // 공지사항 삭제 로직
        return "redirect:/admin/notice-event";
    }

    // 이벤트 생성
    @PostMapping("/event/add")
    public String addEvent(@RequestBody Event event) {
        // 이벤트 생성 로직
        return "redirect:/admin/notice-event";
    }

    // 이벤트 수정
    @PostMapping("/event/update")
    public String updateEvent(@RequestBody Event event) {
        // 이벤트 수정 로직
        return "redirect:/admin/notice-event";
    }

    // 이벤트 삭제
    @PostMapping("/event/delete")
    public String deleteEvent(@RequestParam Long eventId) {
        // 이벤트 삭제 로직
        return "redirect:/admin/notice-event";
    }
}
