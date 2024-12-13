package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.Notice;
import ezen.team.ezenbookstore.service.NoticeService;
import ezen.team.ezenbookstore.service.NoticeServiceInterface;
import ezen.team.ezenbookstore.service.TextFormatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/notice")
public class AdminNoticeApiController {

    private final NoticeService noticeService;
    private final TextFormatService textFormatService;

    // 공지사항 목록 조회 및 검색
    @GetMapping
    public String noticeControl(
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {
        Page<Notice> noticePage = noticeService.getFilteredNotices(searchType, keyword, pageable);

        model.addAttribute("noticePage", noticePage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType);
        model.addAttribute("totalPages", noticePage.getTotalPages());
        model.addAttribute("currentPage", noticePage.getNumber());
        model.addAttribute("hasNext", noticePage.hasNext());
        model.addAttribute("hasPrevious", noticePage.hasPrevious());

        return "admin/noticeControl";
    }

    // 공지사항 추가
    @PostMapping("/add")
    public String addNotice(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestPart(value = "imageAdd", required = false) MultipartFile image) {
        try {
            noticeService.createNoticeWithFile(title, content, image);
            return "redirect:/admin/notice";
        } catch (Exception e) {
            return "redirect:/admin/notice?error=true";
        }
    }

    // 공지사항 수정
    @PostMapping("/update")
    public String updateNotice(
            @RequestParam("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            noticeService.updateNoticeWithFile(id, title, content, image);
            return "redirect:/admin/notice";
        } catch (Exception e) {
            return "redirect:/admin/notice?error=true";
        }
    }

    // 공지사항 삭제
    @PostMapping("/delete")
    public String deleteNotices(@RequestParam("ids") List<Long> ids) {
        try {
            noticeService.deleteNotices(ids);
            return "redirect:/admin/notice";
        } catch (Exception e) {
            return "redirect:/admin/notice?error=true";
        }
    }
}
