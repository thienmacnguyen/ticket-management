package com.macthien.ticket_management.exception;

import com.macthien.ticket_management.dto.response.ErrorResponseDTO;
import com.macthien.ticket_management.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponseDTO> handleAppException(AppException ex, HttpServletRequest request) {
        ErrorCode errorCode = ex.getErrorCode();

        log.warn("Business rule violated: [{}] {} - URI: {} {}",
                errorCode.name(), errorCode.getMessage(), request.getMethod(), request.getRequestURI());

        ErrorResponseDTO response = new ErrorResponseDTO(
                LocalDateTime.now(), errorCode.getHttpStatus().value(),
                errorCode.name(), errorCode.getMessage(), request.getRequestURI()
        );
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        ErrorResponseDTO response = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                errorMessage,
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponseDTO> handleOptimisticLocking(HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.CONCURRENT_UPDATE;
        ErrorResponseDTO response = new ErrorResponseDTO(
                LocalDateTime.now(),
                errorCode.getHttpStatus().value(),
                errorCode.name(),
                errorCode.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled Exception occurred! URI: {} {} - Message: {}",
                request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);

        ErrorResponseDTO response = new ErrorResponseDTO(
                LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "Đã xảy ra lỗi hệ thống, vui lòng thử lại sau.",
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
