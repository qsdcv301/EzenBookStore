package ezen.team.ezenbookstore.service.facade;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
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
    private final OrderItemService orderItemService;

    // 사용자 등록
    @Transactional
    @Override
    public void signupUser(User user) {
        userService.create(user);
    }

    // OAuth 로그인 정보 처리
    @Transactional
    @Override
    public Map<String, Object> processOAuthLogin(CustomOAuth2User oAuthUser) {
        String provider = oAuthUser.getProvider();
        String email = oAuthUser.getEmail();
        String name = oAuthUser.getName();

        User findUser = userService.findByEmail(email);
        Map<String, Object> response = new HashMap<>();

        if (findUser == null) {
            User newUser = userService.create(User.builder()
                    .provider(provider)
                    .email(email)
                    .name(name)
                    .grade(1)
                    .build());
            response.put("first", true); // 새로운 유저 생성
            response.put("grade", newUser.getGrade());
        } else {
            response.put("first", false); // 기존 유저
            response.put("grade", findUser.getGrade());
        }

        return response;
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
    @Transactional
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
    @Transactional
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
    @Transactional
    @Override
    public boolean updateUser(User user, User updatedInfo) {
        try {
            User findUser = userService.findById(user.getId());
            User updatedUser = findUser.toBuilder()
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

    // 주문 및 QnA 정보 조회 및 필터링
    @Override
    public Map<String, Object> getUserInfo(Long userId, String keyword, String dateRange, String deliveryStatusParam, String orderStatusParam,
                                           int oPage, byte sort, String direction, LocalDate startDate, LocalDate endDate, int qPage) {
        Map<String, Object> modelAttributes = new HashMap<>();

        int size = 10;
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        // QnA 페이징 처리
        Pageable qPageable = PageRequest.of(qPage, size, Sort.by(sortDirection, "id"));
        Page<QnA> qPaging;
        if (sort == 0) {
            qPaging = qnAService.findAllByUserId(userId, qPageable);
        } else {
            qPaging = qnAService.findAllByUserIdAndCategory(userId, sort, qPageable);
        }

        // 배송 상태 및 주문 상태 값 변환
        Byte deliveryStatus = null;
        if (deliveryStatusParam != null && !deliveryStatusParam.isEmpty()) {
            switch (deliveryStatusParam) {
                case "preparing":
                    deliveryStatus = 1;
                    break;
                case "shipping":
                    deliveryStatus = 2;
                    break;
                case "delivered":
                    deliveryStatus = 3;
                    break;
                case "preparingReturn":
                    deliveryStatus = 4;
                    break;
                case "Returning":
                    deliveryStatus = 5;
                    break;
                case "Returned":
                    deliveryStatus = 6;
                    break;
                default:
                    deliveryStatus = 0;
                    break;
            }
        }

        Byte orderStatus = null;
        if (orderStatusParam != null && !orderStatusParam.isEmpty()) {
            switch (orderStatusParam) {
                case "completed":
                    orderStatus = 1;
                    break;
                case "cancelled":
                    orderStatus = 2;
                    break;
                case "exchange":
                    orderStatus = 3;
                    break;
            }
        }

        // 날짜 범위 설정
        if (dateRange != null && !dateRange.isEmpty()) {
            int monthsAgo = Integer.parseInt(dateRange);
            endDate = LocalDate.now();
            startDate = endDate.minusMonths(monthsAgo);
        }
        Integer userOrderItemsSuccessCount = orderItemService.countByUserIdAndStatus(userId, (byte) 3); // 3은 주문확정 도서들
        int nextGrade;
        if (userOrderItemsSuccessCount >= 100) {
            nextGrade = 0;
        } else if (userOrderItemsSuccessCount >= 50) {
            nextGrade = 100 - userOrderItemsSuccessCount;
        } else if (userOrderItemsSuccessCount >= 20) {
            nextGrade = 50 - userOrderItemsSuccessCount;
        } else {
            nextGrade = 20 - userOrderItemsSuccessCount;
        }

        // 주문 필터링 처리
        List<Orders> filteredOrders = ordersService.filterOrders(startDate, endDate, deliveryStatus, orderStatus, keyword, userId);

        // 페이징 적용
        Pageable oPageable = PageRequest.of(oPage, size, Sort.by(sortDirection, "orderDate"));
        int start = Math.min((int) oPageable.getOffset(), filteredOrders.size());
        int end = Math.min((start + oPageable.getPageSize()), filteredOrders.size());
        List<Orders> pagedOrders = filteredOrders.subList(start, end);
        Page<Orders> orderPage = new PageImpl<>(pagedOrders, oPageable, filteredOrders.size());

        // 모델에 필요한 속성 추가
        modelAttributes.put("orderList", orderPage.getContent());
        modelAttributes.put("orderPage", orderPage);
        modelAttributes.put("keyword", keyword);
        modelAttributes.put("dateRange", dateRange);
        modelAttributes.put("deliveryStatus", deliveryStatusParam);
        modelAttributes.put("orderStatus", orderStatusParam);
        modelAttributes.put("startDate", startDate);
        modelAttributes.put("endDate", endDate);
        modelAttributes.put("sort", sort);
        modelAttributes.put("direction", direction);
        modelAttributes.put("questionList", qPaging.getContent());
        modelAttributes.put("qnaPage", qPaging);
        modelAttributes.put("nextGrade", nextGrade);

        return modelAttributes;
    }

}