package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.*;
import ezen.team.ezenbookstore.service.facade.UserFacadeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserApiController {

    private final UserFacadeService userFacadeService;
    private final UserService userService;

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user) {
        userFacadeService.signupUser(user);
        return "redirect:/login";
    }

    @GetMapping("/loginSuccess")
    public String getLoginInfo(@AuthenticationPrincipal CustomOAuth2User user) {
        User findUser = userFacadeService.processOAuthLogin(user);
        return (findUser.getGrade() == 99) ? "redirect:/admin/book" : "redirect:/";
    }

    @PostMapping("/findId")
    public ResponseEntity<Map<String, String>> findIdUser(@ModelAttribute User user) {
        Map<String, String> response = userFacadeService.findUserId(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/findPw")
    public ResponseEntity<Map<String, String>> findPwUser(@ModelAttribute User user,
                                                          @RequestParam String verificationCode,
                                                          HttpSession session) {
        Map<String, String> response = userFacadeService.findUserPassword(user, verificationCode, session);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/newPw")
    public ResponseEntity<Map<String, Boolean>> newPwUser(@ModelAttribute User user) {
        boolean success = userFacadeService.updatePassword(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", success);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@RequestParam List<String> userIdList) {
        Map<String, Boolean> response = new HashMap<>();
        try {
            for (String userIdStr : userIdList) {
                Long userId = Long.parseLong(userIdStr);
                boolean isDeleted = userFacadeService.deleteUser(userId);
                if (!isDeleted) {
                    throw new RuntimeException("Failed to delete user with ID: " + userId);
                }
            }
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Boolean>> updateUser(@ModelAttribute User updatedUser) {
        boolean success = userFacadeService.updateUser(userService.findById(updatedUser.getId()), updatedUser);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", success);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/emailAuthentication")
    public ResponseEntity<Map<String, Boolean>> emailAuthentication(@RequestParam("email") String email, HttpSession session) {
        boolean isEmailSent = userFacadeService.sendEmailVerification(email, session);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isEmail", isEmailSent);
        return ResponseEntity.ok(response);
    }

}
