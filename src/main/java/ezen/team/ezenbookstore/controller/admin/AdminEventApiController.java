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
@RequestMapping("/admin/event")
public class AdminEventApiController {

    private final EventService eventService;
    private final NoticeService noticeService;



    @GetMapping
    public String eventControl(Model model) {

        List<Event> eventList = eventService.findAll();

        model.addAttribute("eventList", eventList);

        return "/admin/noticeEventControl";
    }


    // 이벤트 생성
    @PostMapping("/add")
    public String addEvent(@RequestBody Event event) {
        // 이벤트 생성 로직
        return "redirect:/admin/notice-event";
    }

    // 이벤트 수정
    @PostMapping("/update")
    public String updateEvent(@RequestBody Event event) {
        // 이벤트 수정 로직
        return "redirect:/admin/notice-event";
    }

    // 이벤트 삭제
    @PostMapping("/delete")
    public String deleteEvent(@RequestParam Long eventId) {
        // 이벤트 삭제 로직
        return "redirect:/admin/notice-event";
    }
}
