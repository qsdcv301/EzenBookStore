package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.*;
import ezen.team.ezenbookstore.service.facade.UserFacadeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
        // processOAuthLogin 메서드 호출
        Map<String, Object> findUser = userFacadeService.processOAuthLogin(user);

        // 첫 로그인인 경우, /user/info로 리다이렉트
        if (Boolean.TRUE.equals(findUser.get("first"))) {
            return "redirect:/user/info?first=true";
        } else {
            // 관리자인 경우 /admin으로 리다이렉트
            Integer grade = (Integer) findUser.get("grade");
            if (grade == 99) {
                return "redirect:/admin";
            }
            // 일반 사용자는 홈으로 리다이렉트
            return "redirect:/";
        }
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
    public ResponseEntity<Map<String, Boolean>> deleteUser(@RequestParam(name = "userIdList") List<String> userIdList) {
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

    @GetMapping("/info")
    public String infoUser(@RequestParam(name = "keyword", defaultValue = "", required = false) String keyword,
                           @RequestParam(name = "dateRange", defaultValue = "", required = false) String dateRange,
                           @RequestParam(name = "deliveryStatus", defaultValue = "", required = false) String deliveryStatusParam,
                           @RequestParam(name = "orderStatus", defaultValue = "", required = false) String orderStatusParam,
                           @RequestParam(name = "oPage", defaultValue = "0", required = false) int oPage,
                           @RequestParam(name = "sort", defaultValue = "0", required = false) byte sort,
                           @RequestParam(name = "direction", defaultValue = "desc", required = false) String direction,
                           @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                           @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                           @RequestParam(name = "qPage", defaultValue = "0", required = false) int qPage,
                           @ModelAttribute("user") User user,
                           Model model) {

        // Facade를 통해 사용자 정보 관련 데이터를 조회
        Map<String, Object> userInfo = userFacadeService.getUserInfo(user.getId(), keyword, dateRange, deliveryStatusParam, orderStatusParam,
                oPage, sort, direction, startDate, endDate, qPage);

        // 모델에 조회된 데이터 추가
        model.addAllAttributes(userInfo);

        return "info";
    }

}
