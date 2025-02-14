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
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService implements UserServiceInterface{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
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

    @Override
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

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User create(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            // 비밀번호가 null일 경우 기본 비밀번호를 설정
            user.setPassword("소셜 로그인 유저");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        User newUser = User.builder()
                .id(user.getId())
                .provider(user.getProvider())
                .email(user.getEmail())
                .password(user.getPassword())
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

    @Override
    public User updatePass(User user) {
        User newUser = User.builder()
                .id(user.getId())
                .provider(user.getProvider())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
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

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User findByEmailAndProvider(String email, String provider) {
        return userRepository.findByEmailAndProvider(email, provider).orElse(null);
    }

    @Override
    public User findByNameAndTel(String name, String tel) {
        return userRepository.findByNameAndTel(name, tel).orElse(null);
    }

    @Override
    public User findByEmailAndTel(String email, String tel) {
        return userRepository.findByEmailAndTel(email, tel).orElse(null);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // 이메일로 사용자 목록 검색하기
    @Override
    public List<User> findUsersByEmail(String email) {
        return userRepository.findByEmailContainingIgnoreCase(email);
    }

    // 이름으로 사용자 목록 검색하기
    @Override
    public List<User> findUsersByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Long userCount(){
        return userRepository.count();
    }

    @Transactional
    @Override
    public List<User> fetchUserList(String type, String keyword) {
        if (type != null && !type.isEmpty() && keyword != null && !keyword.isEmpty()) {
            if (type.equalsIgnoreCase("email")) {
                return findUsersByEmail(keyword);
            } else if (type.equalsIgnoreCase("name")) {
                return findUsersByName(keyword);
            }
        }
        return findAllUsers();
    }

    @Transactional
    @Override
    public List<User> filterUsersByGrade(List<User> userList, Integer grade) {
        if (grade != null) {
            return userList.stream()
                    .filter(u -> u.getGrade().equals(grade))
                    .collect(Collectors.toList());
        }
        return userList;
    }

    @Transactional
    @Override
    public List<User> paginateUsers(List<User> users, int page, int size) {
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, users.size());

        if (fromIndex >= users.size()) {
            return Collections.emptyList();
        }
        return users.subList(fromIndex, toIndex);
    }

    @Transactional
    @Override
    public Map<String, Long> calculateGradeCounts(List<User> userList) {
        Map<String, Long> gradeCounts = new HashMap<>();
        gradeCounts.put("general", userList.stream().filter(u -> u.getGrade() == 1).count());
        gradeCounts.put("silver", userList.stream().filter(u -> u.getGrade() == 2).count());
        gradeCounts.put("gold", userList.stream().filter(u -> u.getGrade() == 3).count());
        gradeCounts.put("vip", userList.stream().filter(u -> u.getGrade() == 4).count());
        gradeCounts.put("admin", userList.stream().filter(u -> u.getGrade() == 99).count());
        return gradeCounts;
    }

    @Transactional
    @Override
    public int calculateTotalPages(int totalItems, int size) {
        return (int) Math.ceil((double) totalItems / size);
    }


}
