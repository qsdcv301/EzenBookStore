package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.Event;
import ezen.team.ezenbookstore.service.EventService;
import ezen.team.ezenbookstore.service.FileUploadService;
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
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/event")
public class AdminEventApiController {

    private final EventService eventService;
    private final FileUploadService fileUploadService;

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
        // 이미지 경로를 모델에 추가

        List<Integer> imageCounts = eventPage.getContent().stream()
                .map(event -> fileUploadService.getImageCount(event.getId(), "event"))
                .collect(Collectors.toList());
        List<String> imagePaths = eventPage.getContent().stream()
                .map(event -> fileUploadService.findImageFilePath(event.getId(), "event"))
                .map(path -> path != null ? path : "/images/default.png")
                .collect(Collectors.toList());

        // 페이징 결과 및 검색 조건 추가
        model.addAttribute("imageCounts", imageCounts);
        model.addAttribute("imagePaths", imagePaths);
        model.addAttribute("eventPage", eventPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        return "/admin/eventControl";
    }

    @PostMapping("/add")
    public ResponseEntity<String> addEvent(@ModelAttribute Event event, @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            // 이벤트 저장
            Event savedEvent = eventService.create(event);

            // 파일이 비어 있지 않으면 업로드
            if (!file.isEmpty()) {
                boolean isUploaded = fileUploadService.uploadFile(file, String.valueOf(savedEvent.getId()), "event");
                if (!isUploaded) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 실패");
                }
            }
            return ResponseEntity.ok("이벤트가 추가되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이벤트 추가 실패: " + e.getMessage());
        }
    }


    @PostMapping("/update")
    public ResponseEntity<String> updateEvent(@RequestBody Map<String, Object> payload) {
        try {
            // JSON 데이터에서 필드 추출
            Long eventId = Long.valueOf(payload.get("id").toString());
            String title = payload.get("title").toString();
            String content = payload.get("content").toString();
            String startDateStr = payload.get("startDate").toString();
            String endDateStr = payload.get("endDate").toString();

            // Timestamp 변환
            Timestamp startDate = Timestamp.valueOf(startDateStr);
            Timestamp endDate = Timestamp.valueOf(endDateStr);

            // 기존 이벤트 수정
            Event event = eventService.findById(eventId);
            event.setTitle(title);
            event.setContent(content);
            event.setStartDate(startDate);
            event.setEndDate(endDate);

            eventService.update(event);
            return ResponseEntity.ok("이벤트가 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이벤트 수정 실패: " + e.getMessage());
        }
    }




    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getEventById(@PathVariable Long id) {
        try {
            Event event = eventService.findById(id);

            // 데이터 매핑
            Map<String, Object> response = new HashMap<>();
            response.put("id", event.getId());
            response.put("title", event.getTitle());
            response.put("content", event.getContent());
            response.put("startDate", event.getStartDate().toString()); // Timestamp -> String
            response.put("endDate", event.getEndDate().toString()); // Timestamp -> String
            response.put("imagePath", fileUploadService.findImageFilePath(event.getId(), "event"));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }





    @PostMapping("/delete")
    public ResponseEntity<String> deleteEvents(@RequestBody List<Long> ids) {
        try {
            for (Long id : ids) {
                eventService.delete(id);
            }
            return ResponseEntity.ok("선택된 이벤트가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이벤트 삭제 실패");
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<Event>> filterEvents(@RequestParam String filter,
                                                    @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Event> filteredEvents;

        if ("upcoming".equals(filter)) {
            filteredEvents = eventService.findUpcomingEvents(pageable);
        } else if ("ongoing".equals(filter)) {
            filteredEvents = eventService.findOngoingEvents(pageable);
        } else if ("ended".equals(filter)) {
            filteredEvents = eventService.findEndedEvents(pageable);
        } else {
            filteredEvents = eventService.findAll(pageable); // 전체
        }

        return ResponseEntity.ok(filteredEvents);
    }
}
