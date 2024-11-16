package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.QnA;
import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.service.QnAService;
import ezen.team.ezenbookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/qna")
public class QnAApiController {

    private final QnAService qnAService;
    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Boolean>> addQnA(@ModelAttribute QnA qna, @RequestParam String email) {
        Map<String, Boolean> response = new HashMap<>();
        try {
            User user = userService.findByEmail(email);
            QnA newQnA = QnA.builder()
                    .user(user)
                    .category(qna.getCategory())
                    .title(qna.getTitle())
                    .question(qna.getQuestion())
                    .build();
            qnAService.create(newQnA);
            response.put("success", true);
            return ResponseEntity.ok(response); // 성공 시 200 OK와 함께 반환
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
        }
    }

}
