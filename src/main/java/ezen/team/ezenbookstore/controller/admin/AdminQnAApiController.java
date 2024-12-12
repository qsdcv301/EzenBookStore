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

    // QnA 목록 조회
    @GetMapping
    public String qnAControl(
            @RequestParam(required = false, defaultValue = "all") String category,
            @RequestParam(required = false, defaultValue = "all") String status,
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {
        Page<QnA> qnAPage = qnaService.filterQnAList(category, status, pageable);
        model.addAttribute("qnAPage", qnAPage);
        model.addAttribute("currentCategory", category);
        model.addAttribute("currentStatus", status);
        return "admin/qnaControl";
    }

    // QnA 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getQnADetail(@PathVariable Long id) {
        Map<String, String> qnaDetail = qnaService.findQnAById(id.toString());
        if ("true".equals(qnaDetail.get("success"))) {
            return ResponseEntity.ok(qnaDetail);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(qnaDetail);
        }
    }

    // QnA 답변 저장
    @PostMapping("/{id}/answer")
    public ResponseEntity<String> saveAnswer(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String answer = request.get("answer");
        boolean updated = qnaService.saveAnswer(id, answer);
        return updated ? ResponseEntity.ok("답변이 저장되었습니다.") : ResponseEntity.status(HttpStatus.NOT_FOUND).body("QnA를 찾을 수 없습니다.");
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

    // QnA 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQnA(@PathVariable Long id) {
        qnaService.deleteById(id);
        return ResponseEntity.ok("QnA가 삭제되었습니다.");
    }
}
