package ezen.team.ezenbookstore.service.facade;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserFacadeService implements UserFacadeServiceInterface {

    private final UserService userService;
    private final ReviewService reviewService;
    private final CartService cartService;
    private final PaymentService paymentService;
    private final QnAService qnAService;
    private final OrdersService ordersService;
    private final EmailService emailService;

    private static final String VERIFICATION_CODE_SESSION_KEY = "verificationCode";

    // 사용자 등록
    @Override
    public void signupUser(User user) {
        userService.create(user);
    }

    // OAuth 로그인 정보 처리
    @Override
    public User processOAuthLogin(CustomOAuth2User oAuthUser) {
        String provider = oAuthUser.getProvider();
        String email = oAuthUser.getEmail();
        String name = oAuthUser.getName();

        User findUser = userService.findByEmail(email);
        if (findUser == null) {
            return userService.create(User.builder()
                    .provider(provider)
                    .email(email)
                    .name(name)
                    .grade(1)
                    .build());
        } else {
            return findUser;
        }
    }

    // ID 찾기
    @Override
    public Map<String, String> findUserId(User user) {
        Map<String, String> response = new HashMap<>();
        try {
            User findUser = userService.findByNameAndTel(user.getName(), user.getTel());
            response.put("success", "true");
            response.put("email", findUser.getEmail());
            response.put("provider", findUser.getProvider());
        } catch (Exception e) {
            response.put("success", "false");
        }
        return response;
    }

    // 비밀번호 찾기
    @Override
    public Map<String, String> findUserPassword(User user, String verificationCode, HttpSession session) {
        Map<String, String> response = new HashMap<>();
        String storedCode = (String) session.getAttribute(VERIFICATION_CODE_SESSION_KEY);

        if (storedCode != null && storedCode.equals(verificationCode)) {
            try {
                User findUser = userService.findByEmailAndTel(user.getEmail(), user.getTel());
                if (!findUser.getProvider().equals("이젠")) {
                    response.put("success", "false");
                    response.put("error", "간편 로그인 회원입니다.");
                } else {
                    response.put("success", "true");
                }
            } catch (Exception e) {
                response.put("success", "false");
            }
        } else {
            response.put("error", "이메일, 전화번호 혹은 인증번호가 정확하지 않습니다.");
        }
        return response;
    }

    // 비밀번호 업데이트
    @Override
    public boolean updatePassword(User user) {
        try {
            User findUser = userService.findByEmail(user.getEmail());
            User updatedUser = findUser.toBuilder()
                    .password(user.getPassword())
                    .build();
            userService.updatePass(updatedUser);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 사용자 삭제 및 관련 엔티티 삭제
    @Override
    public boolean deleteUser(Long userId) {
        try {
            reviewService.findAllByUserId(userId).forEach(review -> reviewService.deleteById(review.getId()));
            qnAService.findAllByUserId(userId).forEach(qnA -> qnAService.deleteById(qnA.getId()));
            cartService.findAllByUserId(userId).forEach(cart -> cartService.deleteById(cart.getId()));
            paymentService.findAllByUserId(userId).forEach(payment -> paymentService.deleteById(payment.getId()));
            ordersService.findAllByUserId(userId).forEach(order -> ordersService.deleteById(order.getId()));
            userService.deleteById(userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 사용자 업데이트
    @Override
    public boolean updateUser(User user, User updatedInfo) {
        try {
            User updatedUser = User.builder()
                    .tel(updatedInfo.getTel() != null ? updatedInfo.getTel() : user.getTel())
                    .addr(updatedInfo.getAddr() != null ? updatedInfo.getAddr() : user.getAddr())
                    .addrextra(updatedInfo.getAddrextra() != null ? updatedInfo.getAddrextra() : user.getAddrextra())
                    .birthday(updatedInfo.getBirthday() != null ? updatedInfo.getBirthday() : user.getBirthday())
                    .grade(updatedInfo.getGrade() != null ? updatedInfo.getGrade() : user.getGrade())
                    .point(updatedInfo.getPoint() != null ? updatedInfo.getPoint() : user.getPoint())
                    .password(updatedInfo.getPassword() != null ? updatedInfo.getPassword() : user.getPassword())
                    .build();
            userService.update(updatedUser);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 이메일 인증번호 발송 및 세션 저장
    @Override
    public boolean sendEmailVerification(String email, HttpSession session) {
        String verificationCode = generateVerificationCode();
        session.setAttribute(VERIFICATION_CODE_SESSION_KEY, verificationCode);
        emailService.sendEmail(email, "EzBookStore 이메일 인증", "귀하의 인증 번호는: " + verificationCode);
        return true;
    }

    // 인증번호 생성
    @Override
    public String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(999999);
        return String.format("%06d", code);
    }

}