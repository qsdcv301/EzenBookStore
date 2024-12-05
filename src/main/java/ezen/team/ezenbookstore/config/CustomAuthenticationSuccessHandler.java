package ezen.team.ezenbookstore.config;

import ezen.team.ezenbookstore.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Authentication 객체에서 User 정보를 가져오기
        User user = (User) authentication.getPrincipal();

        // 사용자 등급 확인 후 권한 설정 (기본값: ROLE_USER)
        String role = (user.getGrade() == 99) ? "ROLE_ADMIN" : "ROLE_USER";

        // 기존 권한을 새로운 리스트에 복사 후 새로운 권한 추가
        List<SimpleGrantedAuthority> updatedAuthorities = authentication.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toCollection(ArrayList::new));
        updatedAuthorities.add(new SimpleGrantedAuthority(role));

        // 새로운 Authentication 객체 생성
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                user,
                authentication.getCredentials(),
                updatedAuthorities
        );

        // SecurityContextHolder에 새로운 Authentication 설정
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        // 로그인 성공 후 권한에 따른 리다이렉트 처리
        if ("ROLE_ADMIN".equals(role)) {
            response.sendRedirect("/admin");
        } else {
            response.sendRedirect("/");
        }
    }
}