package cc.rockbot.dds.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        log.error("Unexpected error occurred", e);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 10000);
        response.put("message", "系统错误");
        return ResponseEntity.status(500).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException e) {
        log.warn("Business error occurred: {}", e.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("code", e.getErrorCode().getCode());
        response.put("message", e.getDetailMessage());
        return ResponseEntity.status(400).body(response);
    }
} 