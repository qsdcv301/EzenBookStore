package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.Notice;
import ezen.team.ezenbookstore.service.FileUploadService;
import ezen.team.ezenbookstore.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/notice")
public class AdminNoticeApiController {

    private final NoticeService noticeService;
    private final FileUploadService fileUploadService;

    @GetMapping
    public String noticeControl(
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {
        // 검색 조건에 따라 데이터를 조회
        Page<Notice> noticePage;
        if ("title".equals(searchType)) {
            noticePage = noticeService.searchByTitle(keyword, pageable);
        } else if ("content".equals(searchType)) {
            noticePage = noticeService.searchByContent(keyword, pageable);
        } else {
            noticePage = noticeService.findAll(pageable);
        }

        // 각 공지사항의 이미지 경로를 추가
        noticePage.getContent().forEach(notice -> {
            String imagePath = fileUploadService.findImageFilePath(notice.getId(), "notice");
            notice.setImagePath(imagePath != null ? imagePath : "/images/default.png");
        });

        // 페이징 계산
        int totalPages = noticePage.getTotalPages();
        int currentPage = noticePage.getNumber();
        int pageGroupSize = 10;
        int startPage = Math.max(0, (currentPage / pageGroupSize) * pageGroupSize);
        int endPage = Math.min(startPage + pageGroupSize, totalPages);

        // 데이터를 모델에 추가
        model.addAttribute("noticePage", noticePage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/noticeControl"; 
    }

    @PostMapping("/add")
    public String addNotice(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestPart(value = "imageAdd", required = false) MultipartFile image) {
        try {
            Notice newNotice = Notice.builder()
                    .title(title)
                    .content(content)
                    .build();
            Notice savedNotice = noticeService.create(newNotice);

            if (image != null && !image.isEmpty()) {
                fileUploadService.uploadFile(image, savedNotice.getId().toString(), "notice");
            }

            return "redirect:/admin/notice";
        } catch (Exception e) {
            return "redirect:/admin/notice?error=true";
        }
    }

    @PostMapping("/update")
    public String updateNotice(
            @RequestParam("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            Notice notice = noticeService.findById(id);
            if (notice == null) {
                return "redirect:/admin/notice?error=not_found";
            }

            notice.setTitle(title);
            notice.setContent(content);

            if (image != null && !image.isEmpty()) {
                fileUploadService.uploadFile(image, id.toString(), "notice");
            }

            noticeService.update(notice);

            return "redirect:/admin/notice";
        } catch (Exception e) {
            return "redirect:/admin/notice?error=true";
        }
    }

    @PostMapping("/delete")
    public String deleteNotices(@RequestParam("ids") List<Long> ids) {
        try {
            ids.forEach(noticeService::delete);
            return "redirect:/admin/notice";
        } catch (Exception e) {
            return "redirect:/admin/notice?error=true";
        }
    }
}
