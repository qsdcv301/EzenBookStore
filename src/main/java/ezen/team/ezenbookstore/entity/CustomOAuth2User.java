package ezen.team.ezenbookstore.entity;

import ezen.team.ezenbookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oauth2User;
    private final UserService userService;

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 등급에 따라 권한 설정
        String email = getEmail();
        int grade = userService.findByEmail(email).getGrade(); // UserService에서 등급 검색

        // 등급에 따라 권한 설정
        String role = (grade == 99) ? "ROLE_ADMIN" : "ROLE_USER";
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getName() {
        if (oauth2User.getAttributes().containsKey("response")) {
            // 네이버의 경우
            Map<String, Object> response = (Map<String, Object>) oauth2User.getAttributes().get("response");
            return (String) response.get("name");
        } else if (oauth2User.getAttributes().containsKey("kakao_account")) {
            // 카카오의 경우
            Map<String, Object> kakaoAccount = (Map<String, Object>) oauth2User.getAttributes().get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            return (String) profile.get("nickname");
        } else {
            // 구글의 경우
            return oauth2User.getAttribute("name");
        }
    }

    public String getEmail() {
        if (oauth2User.getAttributes().containsKey("response")) {
            // 네이버의 경우
            Map<String, Object> response = (Map<String, Object>) oauth2User.getAttributes().get("response");
            return (String) response.get("email");
        } else if (oauth2User.getAttributes().containsKey("kakao_account")) {
            // 카카오의 경우
            Map<String, Object> kakaoAccount = (Map<String, Object>) oauth2User.getAttributes().get("kakao_account");
            return (String) kakaoAccount.get("email");
        } else {
            // 구글의 경우
            return oauth2User.getAttribute("email");
        }
    }
    public String getProvider() {
        if (oauth2User.getAttributes().containsKey("response")) {
            // 네이버의 경우
            return "네이버";
        } else if (oauth2User.getAttributes().containsKey("kakao_account")) {
            // 카카오의 경우
            return "카카오";
        } else {
            // 구글의 경우
            return "구글";
        }
    }
}
