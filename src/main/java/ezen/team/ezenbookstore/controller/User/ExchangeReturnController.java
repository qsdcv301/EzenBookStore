package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.CustomOAuth2User;
import ezen.team.ezenbookstore.entity.ExchangeReturn;
import ezen.team.ezenbookstore.entity.OrderItem;
import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.service.ExchangeReturnService;
import ezen.team.ezenbookstore.service.FileUploadService;
import ezen.team.ezenbookstore.service.OrderItemService;
import ezen.team.ezenbookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/er")
public class ExchangeReturnController {

    private final ExchangeReturnService exchangeReturnService;
    private final UserService userService;
    private final FileUploadService fileUploadService;
    private final OrderItemService orderItemService;

    @ModelAttribute
    public void findUser(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object userData = auth.getPrincipal();
            if (userData instanceof User user) {
                model.addAttribute("user", user);
                model.addAttribute("userData", true);
                model.addAttribute("mypage", true);
            } else if (userData instanceof CustomOAuth2User customOAuth2User) {
                User customUser = userService.findByEmail(customOAuth2User.getEmail());
                model.addAttribute("user", customUser);
                model.addAttribute("userData", true);
                model.addAttribute("mypage", true);
            } else {
                model.addAttribute("userData", false);
            }
        } catch (Exception e) {
            model.addAttribute("userData", false);
        }
    }

    @PostMapping("/{questionId}")
    public ResponseEntity<Map<String, String>> findEA(@PathVariable(required = false) String questionId) {
        Map<String, String> response = new HashMap<>();
        try {
            Long ERId = Long.parseLong(questionId);
            ExchangeReturn ER = exchangeReturnService.findById(ERId);
            User user = userService.findById(ER.getUser().getId());
            OrderItem orderItem = orderItemService.findById(ER.getOrderItem().getId());
            String bookTitle = orderItem.getBook().getTitle();
            String question = ER.getQuestion();
            String answer = ER.getAnswer();
            String email = user.getEmail();
            String name = user.getName();
            String tel = user.getTel();
            // 카테고리 숫자를 문자열로 변환
            String category = switch (ER.getCategory()) {
                case 1 -> "교환";
                case 2 -> "환불";
                default -> "기타";
            };
            response.put("success", "true");
            response.put("bookTitle", bookTitle);
            response.put("question", question);
            response.put("answer", answer);
            response.put("email", email);
            response.put("name", name);
            response.put("category", category);
            response.put("tel", tel);
            // 이미지 파일 경로 찾기
            String imagePath = fileUploadService.findImageFilePath(ERId, "er");
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
    public ResponseEntity<Map<String, Boolean>> addQnA(@ModelAttribute ExchangeReturn er,
                                                       @RequestParam(name = "file") MultipartFile file,
                                                       Model model) {
        Map<String, Boolean> response = new HashMap<>();
        try {
            User user = (User) model.getAttribute("user");
            ExchangeReturn newExchangeReturn = ExchangeReturn.builder()
                    .user(user)
                    .category(er.getCategory())
                    .question(er.getQuestion())
                    .build();
            ExchangeReturn addExchangeReturn = exchangeReturnService.create(newExchangeReturn);
            if (file != null && !file.isEmpty()) {
                fileUploadService.uploadFile(file, addExchangeReturn.getId().toString(), "er");
            }
            response.put("success", true);
            return ResponseEntity.ok(response); // 성공 시 200 OK와 함께 반환
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
        }
    }

}
