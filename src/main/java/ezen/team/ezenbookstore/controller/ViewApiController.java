package ezen.team.ezenbookstore.controller;

import ezen.team.ezenbookstore.entity.CustomOAuth2User;
import ezen.team.ezenbookstore.entity.Notice;
import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.service.NoticeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class ViewApiController {

    private final NoticeService noticeService;

    @ModelAttribute
    public void findUser(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object userData = auth.getPrincipal();
            if (userData instanceof User user) {
                model.addAttribute("user", user);
                model.addAttribute("userData", true);
            } else if (userData instanceof CustomOAuth2User customOAuth2User) {
                model.addAttribute("user", customOAuth2User);
                model.addAttribute("userData", true);
            }else{
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
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/findIdPassword")
    public String findIdPassword() {
        return "findIdPassword";
    }

    @GetMapping("/customerService")
    public String customerService(@RequestParam(name = "noticePage", defaultValue = "0", required = false) int noticePage,
                                  @RequestParam(name = "qPage", defaultValue = "0", required = false) int qPage,
                                  Model model) {
        int size = 10;
        Sort.Direction sortDirection = Sort.Direction.DESC;
        Pageable noticePageable = PageRequest.of(noticePage, size, Sort.by(sortDirection, "id"));
        Page<Notice> noticePaging = noticeService.findAll(noticePageable);

        model.addAttribute("notices", noticePaging.getContent());
        model.addAttribute("noticePage", noticePaging);
        return "customerService";
    }

}
