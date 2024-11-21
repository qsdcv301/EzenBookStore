package ezen.team.ezenbookstore.controller;

import ezen.team.ezenbookstore.entity.CustomOAuth2User;
import ezen.team.ezenbookstore.entity.Notice;
import ezen.team.ezenbookstore.entity.QnA;
import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.service.NoticeService;
import ezen.team.ezenbookstore.service.QnAService;
import ezen.team.ezenbookstore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class ViewApiController {

    private final NoticeService noticeService;
    private final QnAService qnAService;
    private final UserService userService;

    @ModelAttribute
    public void findUser(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object userData = auth.getPrincipal();
            if (userData instanceof User user) {
                model.addAttribute("user", user);
                model.addAttribute("userData", true);
            } else if (userData instanceof CustomOAuth2User customOAuth2User) {
                User customUser = userService.findByEmail(customOAuth2User.getEmail());
                model.addAttribute("user", customUser);
                model.addAttribute("userData", true);
            } else {
                model.addAttribute("userData", false);
            }
        } catch (Exception e) {
            model.addAttribute("userData", false);
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Boolean>> signup(@ModelAttribute User user, @RequestParam(name = "birthdayString") String birthday) {
        Map<String, Boolean> response = new HashMap<>();
        try {
            // 생일 문자열이 유효한지 확인
            Timestamp convertedTimestamp = null;
            if (birthday != null) {
                String birthdayTime = birthday + " 00:00:00";
                convertedTimestamp = Timestamp.valueOf(birthdayTime);
            }
            User newUser = User.builder()
                    .provider(user.getProvider())
                    .email(user.getEmail())
                    .name(user.getName())
                    .password(user.getPassword())
                    .tel(user.getTel())
                    .birthday(convertedTimestamp)
                    .addr(user.getAddr())
                    .addrextra(user.getAddrextra())
                    .grade(1) //기본 회원 1
                    .build();
            userService.create(newUser);
            response.put("success", true);
            return ResponseEntity.ok(response); // 성공 시 200 OK와 함께 반환
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
        }
    }

    @PostMapping("/duplication/{email}")
    public ResponseEntity<Map<String, Boolean>> duplication(@PathVariable("email") String email) {
        Map<String, Boolean> response = new HashMap<>();
        try {
            boolean findUser = userService.findByEmail(email) != null;
            response.put("success", findUser);
            return ResponseEntity.ok(response); // 성공 시 200 OK와 함께 반환
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 예외 발생 시 500 오류 반환
        }
    }


    @GetMapping("/findIdPassword")
    public String findIdPassword() {
        return "findIdPassword";
    }

    @GetMapping("/customerService")
    public String customerService(@RequestParam(name = "noticePage", defaultValue = "0", required = false) int noticePage,
                                  @RequestParam(name = "qPage", defaultValue = "0", required = false) int qPage,
                                  @RequestParam(name = "sort", defaultValue = "0", required = false) byte sort,
                                  Model model) {
        String userEmail = null;
        int size = 10;
        Sort.Direction sortDirection = Sort.Direction.DESC;
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object userData = auth.getPrincipal();
            if (userData instanceof User user) {
                userEmail = user.getEmail();
            } else if (userData instanceof CustomOAuth2User customOAuth2User) {
                User customUser = userService.findByEmail(customOAuth2User.getEmail());
                userEmail = customUser.getEmail();
            }
            Long userId = userService.findByEmail(userEmail).getId();
            Pageable qPageable = PageRequest.of(qPage, size, Sort.by(sortDirection, "id"));
            Page<QnA> qPaging;
            if (sort == 0) {
                qPaging = qnAService.findAllByUserId(userId, qPageable);
            } else {
                qPaging = qnAService.findAllByUserIdAndCategory(userId, sort, qPageable);
            }
            model.addAttribute("questionList", qPaging.getContent());
            model.addAttribute("qnaPage", qPaging);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Pageable noticePageable = PageRequest.of(noticePage, size, Sort.by(sortDirection, "id"));
        Page<Notice> noticePaging = noticeService.findAll(noticePageable);
        model.addAttribute("notices", noticePaging.getContent());
        model.addAttribute("noticePage", noticePaging);
        model.addAttribute("sort", sort);
        return "customerService";
    }

}
