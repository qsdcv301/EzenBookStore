package ezen.team.ezenbookstore.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
        logger.error("잘못된 요청 예외 발생: ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("요청 데이터가 잘못되었습니다: 관리자에게 문의 해주세요.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("잘못된 요청 데이터 또는 리소스 없음: ", ex);
        if (ex.getMessage().toLowerCase().contains("찾을 수 없습니다")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("요청 데이터가 유효하지 않습니다: " + ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        logger.error("데이터 무결성 위반 발생: ", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("데이터 무결성 위반이 발생했습니다. 요청 데이터를 확인해주세요.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        logger.error("Validation 실패: ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        logger.error("지원되지 않는 HTTP 메서드 요청: ", ex);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body("지원되지 않는 HTTP 메서드입니다. 올바른 요청 방식을 확인해주세요.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        logger.error("읽을 수 없는 요청 데이터: ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("잘못된 요청 형식입니다. 요청 데이터를 확인해주세요.");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        logger.error("지원되지 않는 미디어 타입 요청: ", ex);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body("지원되지 않는 미디어 타입입니다. 요청 내용을 확인해주세요.");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        logger.error("필수 요청 파라미터 누락: ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("필수 요청 파라미터가 누락되었습니다: " + ex.getParameterName());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        logger.error("존재하지 않는 URL 요청: ", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("요청한 URL을 찾을 수 없습니다. 경로를 확인해주세요.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        logger.error("접근 거부: ", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("접근 권한이 없습니다.");
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        logger.error("ResponseStatusException 발생: ", ex);
        return ResponseEntity.status(ex.getStatusCode())
                .body(ex.getReason());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        logger.error("처리되지 않은 예외가 발생: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버에 문제가 발생했습니다: 관리자에게 문의 해주세요.");
    }
}
