package cc.rockbot.dds.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("Business exception: {}", e.getMessage(), e);
        ErrorResponse response = new ErrorResponse(e.getErrorCode().getCode(), e.getDetailMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("System error: {}", e.getMessage(), e);
        ErrorResponse response = new ErrorResponse(
            ErrorCode.SYSTEM_ERROR.getCode(),
            ErrorCode.SYSTEM_ERROR.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 