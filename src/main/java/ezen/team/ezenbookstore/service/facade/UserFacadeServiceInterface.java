package ezen.team.ezenbookstore.service.facade;

import ezen.team.ezenbookstore.entity.CustomOAuth2User;
import ezen.team.ezenbookstore.entity.User;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

public interface UserFacadeServiceInterface {

    void signupUser(User user);

    Map<String, String> findUserId(User user);

    Map<String, String> findUserPassword(User user, String verificationCode, HttpSession session);

    boolean updatePassword(User user);

    User processOAuthLogin(CustomOAuth2User oAuthUser);

    boolean updateUser(User user, User updatedInfo);

    boolean deleteUser(Long userId);

    boolean sendEmailVerification(String email, HttpSession session);

    String generateVerificationCode();

}
