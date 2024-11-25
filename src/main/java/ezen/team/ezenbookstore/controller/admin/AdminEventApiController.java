package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.Event;
import ezen.team.ezenbookstore.service.EventService;
import ezen.team.ezenbookstore.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.sql.Timestamp;
import java.time.LocalDateTime;
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
            @RequestParam(required = false, defaultValue = "all") String filter,
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {
        try {
            Page<Event> eventPage;

            if ("title".equalsIgnoreCase(searchType)) {
                eventPage = eventService.searchByTitle(keyword, pageable);
            } else if ("content".equalsIgnoreCase(searchType)) {
                eventPage = eventService.searchByContent(keyword, pageable);
            } else {
                eventPage = eventService.findAll(pageable);
            }

            if ("upcoming".equalsIgnoreCase(filter)) {
                eventPage = eventService.findUpcomingEvents(pageable);
            } else if ("ongoing".equalsIgnoreCase(filter)) {
                eventPage = eventService.findOngoingEvents(pageable);
            } else if ("ended".equalsIgnoreCase(filter)) {
                eventPage = eventService.findEndedEvents(pageable);
            }

            int totalPages = eventPage.getTotalPages();
            int currentPage = eventPage.getNumber();
            int pageGroupSize = 10;

            int startPage = Math.max(0, (currentPage / pageGroupSize) * pageGroupSize);
            int endPage = Math.min(startPage + pageGroupSize, totalPages);

            List<Integer> imageCounts = eventPage.getContent().stream()
                    .map(event -> fileUploadService.getImageCount(event.getId(), "event"))
                    .collect(Collectors.toList());

            List<String> imagePaths = eventPage.getContent().stream()
                    .map(event -> fileUploadService.findImageFilePath(event.getId(), "event"))
                    .map(path -> path != null ? path : "/images/default.png")
                    .collect(Collectors.toList());

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
            model.addAttribute("filter", filter);

            return "/admin/eventControl";
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

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateEvent(
            @RequestParam("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            Event event = eventService.findById(id);
            if (event == null) {
                return ResponseEntity.status(404).body("이벤트를 찾을 수 없습니다.");
            }

            event.setTitle(title);
            event.setContent(content);
            event.setStartDate(Timestamp.valueOf(startDate));
            event.setEndDate(Timestamp.valueOf(endDate));

            if (file != null && !file.isEmpty()) {
                boolean uploadSuccess = fileUploadService.uploadFile(file, id.toString(), "event");
                fileUploadService.findImageFilePath(id, "event");
                if (!uploadSuccess) {
                    return ResponseEntity.status(500).body("파일 업로드 실패");
                }
            }


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

            Map<String, Object> eventData = new HashMap<>();
            eventData.put("id", event.getId());
            eventData.put("title", event.getTitle());
            eventData.put("content", event.getContent());
            eventData.put("startDate", event.getStartDate().toString());
            eventData.put("endDate", event.getEndDate().toString());

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
}
