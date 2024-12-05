package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.QnA;
import ezen.team.ezenbookstore.service.QnAService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/qna")
public class QnAApiController {

    private final QnAService qnAService;

    @PostMapping("/{questionId}")
    public ResponseEntity<Map<String, String>> findQnA(@PathVariable(required = false) String questionId) {
        Map<String, String> response = qnAService.findQnAById(questionId);
        if (response.get("success").equals("true")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Boolean>> addQnA(@ModelAttribute QnA qna,
                                                       @RequestParam String email,
                                                       @RequestParam(name = "files", required = false) List<MultipartFile> files) {
        Map<String, Boolean> response = qnAService.addQnA(qna, email, files);
        if (response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}