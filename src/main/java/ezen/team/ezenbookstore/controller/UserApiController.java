package ezen.team.ezenbookstore.controller;

import ezen.team.ezenbookstore.entity.CustomOAuth2User;
import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user) {
        userService.create(user);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    @GetMapping("/loginSuccess")
    public String getLoginInfo(Model model, @AuthenticationPrincipal CustomOAuth2User principal) {
        String provider = principal.getProvider();
        String email = principal.getEmail();
        String name = principal.getName();
        User findUser = userService.findByEmailAndProvider(email, provider);
        if (findUser == null) {
            // 새로운 User 객체 생성
            User newUser = User.builder()
                    .provider(provider)
                    .email(email)
                    .name(name)
                    .build();
            userService.create(newUser);
        }
        return "redirect:/payment/" + email;
    }

}
