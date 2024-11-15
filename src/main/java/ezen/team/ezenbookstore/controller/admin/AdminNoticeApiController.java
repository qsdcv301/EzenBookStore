package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.Notice;
import ezen.team.ezenbookstore.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/notice")
public class AdminNoticeApiController {

    private final NoticeService noticeService;

    // 공지사항 페이지 이동 및 목록 조회
    @GetMapping
    public String noticeControl(Model model) {
        // 공지사항 목록을 내림차순으로 조회
        List<Notice> noticeLists = noticeService.findAllByOrderByIdDesc();
        model.addAttribute("noticeLists", noticeLists);
        return "/admin/noticeControl";
    }

    // 공지사항 생성 - AJAX 요청 처리
    @PostMapping("/add")
    public ResponseEntity<String> addNotice(@RequestBody Notice notice) {
        try {
            noticeService.create(notice);
            return ResponseEntity.ok("공지사항이 추가되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지사항 추가 실패");
        }
    }

    // 공지사항 수정 - AJAX 요청 처리
    @PostMapping("/update")
    public ResponseEntity<String> updateNotice(@RequestBody Notice notice) {
        try {
            noticeService.update(notice);
            return ResponseEntity.ok("공지사항이 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지사항 수정 실패");
        }
    }

    // 공지사항 일괄 수정 - AJAX 요청 처리
    @PostMapping("/batchUpdate")
    public ResponseEntity<String> updateNotices(@RequestBody List<Notice> notices) {
        try {
            for (Notice notice : notices) {
                noticeService.update(notice);
            }
            return ResponseEntity.ok("공지사항이 일괄 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지사항 일괄 수정 실패");
        }
    }

    // 공지사항 삭제 - AJAX 요청 처리
    @PostMapping("/delete")
    public ResponseEntity<String> deleteNotice(@RequestBody Long noticeId) {
        try {
            noticeService.delete(noticeId);
            return ResponseEntity.ok("공지사항이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지사항 삭제 실패");
        }
    }

    // 공지사항 일괄 삭제 - AJAX 요청 처리
    @PostMapping("/batchDelete")
    public ResponseEntity<String> deleteNotices(@RequestBody List<Long> noticeIds) {
        try {
            for (Long id : noticeIds) {
                noticeService.delete(id);
            }
            return ResponseEntity.ok("선택된 공지사항이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지사항 일괄 삭제 실패");
        }
    }
}
