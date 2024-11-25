package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.Event;
import ezen.team.ezenbookstore.service.EventService;
import ezen.team.ezenbookstore.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
    public String eventControl(
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

        try {
            // 기본 검색 조건 설정
            if (searchType == null || searchType.trim().isEmpty()) {
                searchType = "all"; // 기본 검색 타입 설정
            }

            Page<Event> eventPage;

            // 검색 조건에 따른 데이터 조회
            if ("title".equalsIgnoreCase(searchType)) {
                eventPage = eventService.searchByTitle(keyword, pageable);
            } else if ("content".equalsIgnoreCase(searchType)) {
                eventPage = eventService.searchByContent(keyword, pageable);
            } else {
                eventPage = eventService.findAll(pageable); // 전체 조회
            }

            // 페이징 관련 데이터 계산
            int totalPages = eventPage.getTotalPages();
            int currentPage = eventPage.getNumber();
            int pageGroupSize = 10; // 페이지 그룹 크기

            int startPage = Math.max(0, (currentPage / pageGroupSize) * pageGroupSize); // 시작 페이지 번호
            int endPage = Math.min(startPage + pageGroupSize, totalPages); // 끝 페이지 번호

            // 이미지 관련 데이터 수집
            List<Integer> imageCounts = eventPage.getContent().stream()
                    .map(event -> fileUploadService.getImageCount(event.getId(), "event"))
                    .collect(Collectors.toList());

            List<String> imagePaths = eventPage.getContent().stream()
                    .map(event -> fileUploadService.findImageFilePath(event.getId(), "event"))
                    .map(path -> path != null ? path : "/images/default.png") // 기본 이미지 경로
                    .collect(Collectors.toList());

            // 모델에 데이터 추가
            model.addAttribute("eventPage", eventPage);
            model.addAttribute("imageCounts", imageCounts);
            model.addAttribute("imagePaths", imagePaths);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("hasNext", currentPage < totalPages - 1);
            model.addAttribute("hasPrevious", currentPage > 0);
            model.addAttribute("keyword", keyword);
            model.addAttribute("searchType", searchType);

            // View 경로 반환
            return "/admin/eventControl"; // Thymeleaf 또는 JSP 경로
        } catch (Exception e) {
            model.addAttribute("error", "데이터를 불러오는 중 오류가 발생했습니다.");
            return "/admin/eventControl";
        }
    }



    @PostMapping("/add")
    public ResponseEntity<String> addEvent(@ModelAttribute Event event,
                                           @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Event savedEvent = eventService.create(event);

            if (file != null && !file.isEmpty()) {
                boolean isUploaded = fileUploadService.uploadFile(file, String.valueOf(savedEvent.getId()), "event");
                if (!isUploaded) {
                    return ResponseEntity.status(500).body("이미지 업로드 실패");
                }
            }
            return ResponseEntity.ok("이벤트가 추가되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("이벤트 추가 실패: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateEvent(@RequestBody Event event) {
        try {
            eventService.update(event);
            return ResponseEntity.ok("이벤트가 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("이벤트 수정 실패: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getEventDetail(@PathVariable Long id) {
        try {
            Event event = eventService.findById(id);

            if (event == null) {
                return ResponseEntity.status(404).body(null);
            }

            // 이벤트 데이터를 Map 으로 변환
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("id", event.getId());
            eventData.put("title", event.getTitle());
            eventData.put("content", event.getContent());
            eventData.put("startDate", event.getStartDate().toString());
            eventData.put("endDate", event.getEndDate().toString());

            // 이미지 경로 추가
            String imagePath = fileUploadService.findImageFilePath(event.getId(), "event");
            eventData.put("imagePath", imagePath != null ? imagePath : "/images/default.png");

            return ResponseEntity.ok(eventData);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
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
            return ResponseEntity.status(500).body("이벤트 삭제 실패: " + e.getMessage());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<Event>> filterEvents(
            @RequestParam String filter,
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            Page<Event> filteredEvents = switch (filter.toLowerCase()) {
                case "upcoming" -> eventService.findUpcomingEvents(pageable);
                case "ongoing" -> eventService.findOngoingEvents(pageable);
                case "ended" -> eventService.findEndedEvents(pageable);
                default -> eventService.findAll(pageable);
            };
            return ResponseEntity.ok(filteredEvents);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
