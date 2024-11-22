package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.QnA;
import ezen.team.ezenbookstore.service.QnAService;
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
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/qna")
public class AdminQnAApiController {

    private final QnAService qnaService;

    // QnA 목록 조회 및 카테고리 필터링
    @GetMapping
    public String qnAControl(@PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                             Model model) {
        Page<QnA> qnAPage = qnaService.findAll(pageable);

        int totalPages = qnAPage.getTotalPages(); // 총 페이지 수
        int currentPage = qnAPage.getNumber();
        int pageGroupSize = 10; // 페이지 그룹 크기
        int startPage = Math.max(0, (currentPage / pageGroupSize) * pageGroupSize); // 시작 페이지 번호
        int endPage = Math.min(startPage + pageGroupSize, totalPages); // 끝 페이지 번호

        model.addAttribute("qnAPage", qnAPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/admin/qnaControl";
    }
// 구현중
//    @PostMapping("/filter")
//    public  ResponseEntity<Page<QnA>> filterQnAList(
//                @RequestBody Map<String, String> filterData,
//                @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
//        Byte category = Byte.valueOf(filterData.getOrDefault("category", "all"));
//        String status = filterData.getOrDefault("status", "all");
//
//        Page<QnA> qnaPage;
//        // 필터 조건 처리
//        if ("answered".equals(status)) {
//            // 답변 완료 필터
//            qnaPage = qnaService.findAnsweredQnA(pageable,category);
//        } else if ("pending".equals(status)) {
//            // 답변 대기중 필터
//            qnaPage = qnaService.findPendingQnA(parseCategory(category), pageable);
//        } else if (!"all".equals(category)) {
//            // 카테고리 필터
//            qnaPage = qnaService.findByCategory(parseCategory(category), pageable);
//        } else {
//            // 전체 조회
//            qnaPage = qnaService.findAll(pageable);
//        }
//        return ResponseEntity.ok(qnaPage);
//    }

    // QnA 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<QnA> getQnADetail(@PathVariable Long id) {
        QnA qna = qnaService.findById(id);
        if (qna != null) {
            return ResponseEntity.ok(qna);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // QnA 답변 저장
    @PostMapping("/{id}/answer")
    public ResponseEntity<String> saveAnswer(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String answer = request.get("answer");
        boolean updated = qnaService.saveAnswer(id, answer);

        if (updated) {
            return ResponseEntity.ok("답변이 저장되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("QnA를 찾을 수 없습니다.");
        }
    }

    // QnA 일괄 답변 저장
    @PostMapping("/bulk-answer")
    public ResponseEntity<String> bulkAnswer(@RequestBody Map<String, Object> request) {
        List<Long> ids = (List<Long>) request.get("ids");
        String answer = (String) request.get("answer");

        if (ids == null || ids.isEmpty() || answer == null || answer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다.");
        }

        qnaService.bulkAnswer(ids, answer);
        return ResponseEntity.ok("일괄 답변이 완료되었습니다.");
    }
}
