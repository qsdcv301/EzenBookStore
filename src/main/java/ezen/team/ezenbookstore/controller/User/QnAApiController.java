package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.QnA;
import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.service.CategoryService;
import ezen.team.ezenbookstore.service.FileUploadService;
import ezen.team.ezenbookstore.service.QnAService;
import ezen.team.ezenbookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/qna")
public class QnAApiController {

    private final QnAService qnAService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final FileUploadService fileUploadService;

    @PostMapping("/{questionId}")
    public ResponseEntity<Map<String, String>> findQnA(@PathVariable(required = false) String questionId) {
        Map<String, String> response = new HashMap<>();
        try {
            Long QnAId = Long.parseLong(questionId);
            QnA QnA = qnAService.findById(QnAId);
            User user = userService.findById(QnA.getUser().getId());
            String title = QnA.getTitle();
            String question = QnA.getQuestion();
            String answer = QnA.getAnswer();
            String email = user.getEmail();
            String name = user.getName();
            String tel = user.getTel();
            // 카테고리 숫자를 문자열로 변환
            String category = switch (QnA.getCategory()) {
                case 1 -> "주문/결제";
                case 2 -> "배송";
                case 3 -> "반품/교환";
                case 4 -> "상품문의";
                case 5 -> "기타";
                default -> "기타";
            };
            response.put("success", "true");
            response.put("question", question);
            response.put("answer", answer);
            response.put("email", email);
            response.put("name", name);
            response.put("category", category);
            response.put("title", title);
            response.put("tel", tel);
            // 이미지 파일 경로 찾기
            String imagePath = fileUploadService.findImageFilePath(QnAId,"qna");
            if (imagePath != null) {
                response.put("imagePath", imagePath);
            }
            return ResponseEntity.ok(response); // 성공 시 200 OK와 함께 반환
        } catch (Exception e) {
            response.put("success", "false");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Boolean>> addQnA(@ModelAttribute QnA qna,
                                                       @RequestParam String email,
                                                       @RequestParam(name = "files", required = false) List<MultipartFile> files) {
        Map<String, Boolean> response = new HashMap<>();
        try {
            User user = userService.findByEmail(email);
            QnA newQnA = QnA.builder()
                    .user(user)
                    .category(qna.getCategory())
                    .title(qna.getTitle())
                    .question(qna.getQuestion())
                    .build();
            QnA addQnA = qnAService.create(newQnA);
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    fileUploadService.uploadFile(file, addQnA.getId().toString(), "qna");
                }
            }
            response.put("success", true);
            return ResponseEntity.ok(response); // 성공 시 200 OK와 함께 반환
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
        }
    }

}
