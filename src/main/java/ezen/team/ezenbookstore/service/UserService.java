package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.CustomOAuth2User;
import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Object userInConnection() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
                Object principal = authentication.getPrincipal();
                Object userObject = null;

                userObject = principal instanceof User user ? (User) principal
                        : principal instanceof CustomOAuth2User customOauth2User ? customOauth2User
                        : null;

                return userObject;
            } else {
                // 인증되지 않은 경우 null 반환
                return null;
            }
        } catch (Exception e) {
            // 예외 로그를 출력하거나 로그 시스템에 기록
            System.err.println("Error in userInConnection: " + e.getMessage());
            e.printStackTrace();

            // 예외 발생 시 null 반환 또는 다른 처리를 위해 사용자 정의 예외를 던질 수 있음
            return null;
        }
    }

    public String getUserEmail() {
        Object userObject = userInConnection();

        if (userObject instanceof User user) {
            return user.getEmail();
        } else if (userObject instanceof CustomOAuth2User customOAuth2User) {
            return customOAuth2User.getEmail();
        } else {
            return null; // 인증되지 않은 경우
        }
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User create(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            // 비밀번호가 null일 경우 기본 비밀번호를 설정
            user.setPassword("소셜 로그인 유저");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User update(User user) {
        User newUser = User.builder()
                .id(user.getId())
                .provider(user.getProvider())
                .email(user.getEmail())
                .name(user.getName())
                .tel(user.getTel())
                .addr(user.getAddr())
                .addrextra(user.getAddrextra())
                .createdAt(user.getCreatedAt())
                .birthday(user.getBirthday())
                .grade(user.getGrade())
                .point(user.getPoint())
                .build();
        return userRepository.save(newUser);
    }

    public User updatePass(User user) {
        User newUser = User.builder()
                .id(user.getId())
                .provider(user.getProvider())
                .password(user.getPassword())
                .email(user.getEmail())
                .name(user.getName())
                .tel(user.getTel())
                .addr(user.getAddr())
                .addrextra(user.getAddrextra())
                .createdAt(user.getCreatedAt())
                .birthday(user.getBirthday())
                .grade(user.getGrade())
                .point(user.getPoint())
                .build();
        return userRepository.save(newUser);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User findByEmailAndProvider(String email, String provider) {
        return userRepository.findByEmailAndProvider(email, provider).orElse(null);
    }

    public User findByNameAndTel(String name, String tel) {
        return userRepository.findByNameAndTel(name, tel).orElse(null);
    }

    public User findByEmailAndTel(String email, String tel) {
        return userRepository.findByEmailAndTel(email, tel).orElse(null);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Page<User> findByGrade(Integer grade, Pageable pageable) {
        return userRepository.findByGrade(grade, pageable);
    }
}
