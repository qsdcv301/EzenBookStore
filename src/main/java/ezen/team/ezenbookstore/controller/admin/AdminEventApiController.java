package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.Event;
import ezen.team.ezenbookstore.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/event")
public class AdminEventApiController {

    private final EventService eventService;

    @GetMapping
    public String eventControl(@RequestParam(required = false) String searchType,
                               @RequestParam(required = false) String keyword,
                               @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                               Model model) {

        Page<Event> eventPage;

        if ("title".equals(searchType)) {
            eventPage = eventService.searchByTitle(keyword, pageable);
        } else if ("content".equals(searchType)) {
            eventPage = eventService.searchByContent(keyword, pageable);
        } else {
            eventPage = eventService.findAll(pageable); // 전체 조회
        }

        int totalPages = eventPage.getTotalPages(); // 총 페이지 수
        int currentPage = eventPage.getNumber();
        int pageGroupSize = 10; // 페이지 그룹 크기

        int startPage = Math.max(0, (currentPage / pageGroupSize) * pageGroupSize); // 시작 페이지 번호
        int endPage = Math.min(startPage + pageGroupSize, totalPages); // 끝 페이지 번호

        // 페이징 결과 및 검색 조건 추가
        model.addAttribute("eventPage", eventPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "/admin/eventControl";
    }


    // 이벤트 생성 - AJAX 요청 처리
    @PostMapping("/add")
    public ResponseEntity<String> addEvent(@RequestBody Event event) {
        try {
            eventService.create(event);
            return ResponseEntity.ok("공지사항이 추가되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지사항 추가 실패");
        }
    }

    // 공지사항 수정 - AJAX 요청 처리
    @PostMapping("/update")
    public ResponseEntity<String> updateEvent(@RequestBody Event event) {
        try {
            eventService.update(event);
            return ResponseEntity.ok("공지사항이 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지사항 수정 실패");
        }
    }

    //공지사항 삭제
    @PostMapping("/delete")
    public ResponseEntity<String> deleteEvents(@RequestBody List<Long> ids) {
        try {
            for (Long id : ids) {
                eventService.delete(id);
            }
            return ResponseEntity.ok("선택된 공지사항이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지사항 삭제 실패");
        }
    }

    @GetMapping("/searchTitle")
    public String searchTitle(@RequestParam(required = false) String keyword,
                              @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                              Model model) {
        // 페이징 처리된 공지사항 리스트 조회
        Page<Event> eventPage = eventService.searchByTitle(keyword, pageable);
        model.addAttribute("eventPage", eventPage);
        return "/admin/eventControl";
    }

    @GetMapping("/searchContent")
    public String searchContent(@RequestParam(required = false) String keyword,
                                @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                Model model) {
        // 페이징 처리된 공지사항 리스트 조회
        Page<Event> eventPage = eventService.searchByContent(keyword, pageable);
        model.addAttribute("eventPage", eventPage);
        return "/admin/eventControl";

    }
}
