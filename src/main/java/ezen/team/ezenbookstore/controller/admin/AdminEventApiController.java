package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.Event;
import ezen.team.ezenbookstore.service.EventServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/event")
public class AdminEventApiController {

    private final EventServiceInterface eventService;

    // 이벤트 목록 조회 및 필터링
    @GetMapping
    public String eventControl(
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "all") String filter,
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {
        try {
            // 비즈니스 로직을 서비스로 이동
            Page<Event> eventPage = eventService.getFilteredEvents(searchType, keyword, filter, pageable);

            List<Integer> imageCounts = eventService.getImageCounts(eventPage.getContent());

            model.addAttribute("eventPage", eventPage);
            model.addAttribute("imageCounts", imageCounts);
            model.addAttribute("keyword", keyword);
            model.addAttribute("searchType", searchType);
            model.addAttribute("filter", filter);
            model.addAttribute("totalPages", eventPage.getTotalPages());
            model.addAttribute("currentPage", eventPage.getNumber());
            model.addAttribute("hasNext", eventPage.hasNext());
            model.addAttribute("hasPrevious", eventPage.hasPrevious());

            return "admin/eventControl";
        } catch (Exception e) {
            model.addAttribute("error", "데이터를 불러오는 중 오류가 발생했습니다.");
            return "admin/eventControl";
        }
    }

    // 이벤트 추가
    @PostMapping("/add")
    public ResponseEntity<String> addEvent(@ModelAttribute Event event,
                                           @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            eventService.updateEvent(null, event.getTitle(), event.getContent(),
                    event.getStartDate().toLocalDateTime(), event.getEndDate().toLocalDateTime(), file);
            return ResponseEntity.ok("이벤트가 추가되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("이벤트 추가 실패: " + e.getMessage());
        }
    }

    // 이벤트 수정
    @PostMapping(value = "/update", consumes = "multipart/form-data")
    public ResponseEntity<String> updateEvent(
            @RequestParam("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            eventService.updateEvent(id, title, content, startDate, endDate, file);
            return ResponseEntity.ok("이벤트가 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("이벤트 수정 실패: " + e.getMessage());
        }
    }

    // 이벤트 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getEventDetail(@PathVariable Long id) {
        try {
            Map<String, Object> eventDetail = eventService.getEventDetail(id);
            return ResponseEntity.ok(eventDetail);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    // 이벤트 삭제
    @PostMapping("/delete")
    public ResponseEntity<String> deleteEvents(@RequestBody List<Long> ids) {
        try {
            for (Long id : ids) {
                eventService.delete(id);
            }
            return ResponseEntity.ok("선택된 이벤트가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("이벤트 삭제 실패: " + e.getMessage());
        }
    }
}
