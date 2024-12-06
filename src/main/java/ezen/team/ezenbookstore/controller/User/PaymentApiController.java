package ezen.team.ezenbookstore.controller.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import ezen.team.ezenbookstore.entity.*;
import lombok.RequiredArgsConstructor;
import ezen.team.ezenbookstore.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/order/payment")
public class PaymentApiController {

    @Value("${portone.api.url}")
    private String portoneApiUrl;

    @Value("${portone.api.key}")
    private String portoneApiKey;

    @Value("${portone.api.secret}")
    private String portoneApiSecret;

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public ResponseEntity<Map<String, Boolean>> requestPayment(@RequestParam(name = "imp_uid") String impUid,
                                                               @RequestParam(name = "paymentCode") String paymentCode,
                                                               @RequestParam(name = "userName") String userName,
                                                               @RequestParam(name = "addr") String addr,
                                                               @RequestParam(name = "addrextra") String addrextra,
                                                               @RequestParam(name = "tel") String tel,
                                                               @RequestParam(name = "amount") Long amount,
                                                               @RequestParam(name = "titleList") List<String> titleList,
                                                               @RequestParam(name = "quantityList") List<Integer> quantityList,
                                                               @RequestParam(name = "cartIdList", required = false) List<Long> cartIdList,
                                                               @ModelAttribute("user") User user) {
        Map<String, Boolean> response = new HashMap<>();

        try {
            if (titleList.size() != quantityList.size()) {
                response.put("success", false);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // 포트원에서 액세스 토큰을 가져오기
            String accessToken = getAccessToken();
            if (accessToken == null) {
                response.put("success", false);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // 포트원에서 결제 정보 확인
            boolean isPaymentValid = verifyPaymentWithPortone(accessToken, impUid, amount);
            if (!isPaymentValid) {
                // 결제 검증 실패 시 결제 취소 요청
                cancelPaymentWithPortone(accessToken, impUid);
                response.put("success", false);
                return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(response);
            }

            // 결제 검증 성공 시 데이터베이스에 결제 정보 삽입
            boolean isPaymentSuccessful = paymentService.processPayment(user, paymentCode, userName, addr, addrextra, tel, amount, titleList, quantityList, cartIdList);

            if (isPaymentSuccessful) {
                response.put("success", true);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 포트원에서 결제 상태 확인
    private boolean verifyPaymentWithPortone(String accessToken, String impUid, Long expectedAmount) {
        try {
            RestTemplate restTemplate = createRestTemplate();
            String paymentUrl = portoneApiUrl + "/payments/" + impUid;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(paymentUrl, HttpMethod.GET, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null && responseBody.get("response") != null) {
                Map<String, Object> paymentData = (Map<String, Object>) responseBody.get("response");
                Long amount = ((Number) paymentData.get("amount")).longValue();
                String status = (String) paymentData.get("status");

                // 결제 상태가 "paid"이고 금액이 일치하는지 확인
                return "paid".equals(status) && expectedAmount.equals(amount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 포트원 결제 취소 요청
    private void cancelPaymentWithPortone(String accessToken, String impUid) {
        try {
            RestTemplate restTemplate = createRestTemplate();
            String cancelUrl = portoneApiUrl + "/payments/cancel";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", accessToken);

            Map<String, Object> request = new HashMap<>();
            request.put("imp_uid", impUid);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            restTemplate.postForEntity(cancelUrl, entity, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // PortOne에서 액세스 토큰을 가져오는 메서드
    private String getAccessToken() {
        try {
            RestTemplate restTemplate = createRestTemplate();
            String tokenUrl = portoneApiUrl + "/users/getToken";

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON); // Content-Type: application/json

            // 요청 바디 설정
            Map<String, String> request = new HashMap<>();
            request.put("imp_key", portoneApiKey);
            request.put("imp_secret", portoneApiSecret);

            // 요청 바디를 JSON으로 직렬화
            String requestBody = objectMapper.writeValueAsString(request);

            // HttpEntity 생성하여 요청 헤더와 바디 포함
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            // POST 요청 전송
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, entity, Map.class);

            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null && responseBody.get("response") != null) {
                Map<String, Object> responseData = (Map<String, Object>) responseBody.get("response");
                return "Bearer " + responseData.get("access_token");
            }
        } catch (HttpClientErrorException e) {
            // HTTP 오류 응답 바디 출력
            System.out.println("HTTP 오류 응답 바디: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 타임아웃 설정이 있는 RestTemplate 생성
    private RestTemplate createRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);  // 연결 타임아웃 10초
        factory.setReadTimeout(10000);     // 읽기 타임아웃 10초
        return new RestTemplate(factory);
    }

}
