//package com.jodo.ems.exceptions;
//
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.ConstraintViolationException;
//
//import java.nio.file.AccessDeniedException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
//@ControllerAdvice
//public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//
//    private static final SimpleDateFormat SF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    private Map<String, Object> createErrorResponse(HttpStatus status, String message, String path) {
//        Map<String, Object> errorDetails = new HashMap<>();
//        errorDetails.put("timestamp", SF.format(new Date()));
//        errorDetails.put("status", status.value());
//        errorDetails.put("statusDescription", status.getReasonPhrase());
//        errorDetails.put("message", message);
//        errorDetails.put("path", path);
//        return errorDetails;
//    }
//
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
//            HttpServletRequest request) {
//        Map<String, Object> errorDetails = createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
//                "Could not execute statement: Duplicate entry [" + ex.getMessage() + "]",
//                request.getRequestURI());
//        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex,
//            HttpServletRequest request) {
//        StringBuilder violations = new StringBuilder();
//        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
//        for (ConstraintViolation<?> violation : constraintViolations) {
//            violations.append(violation.getMessage()).append("; ");
//        }
//
//        Map<String, Object> errorDetails = createErrorResponse(HttpStatus.BAD_REQUEST, violations.toString(),
//                request.getRequestURI());
//        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
//    }
//
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//            org.springframework.http.HttpHeaders headers, HttpStatus status, org.springframework.web.context.request.WebRequest request) {
//        Map<String, Object> errorDetails = createErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed",
//                request.getContextPath());
//
//        Map<String, String> fieldErrors = new HashMap<>();
//        ex.getBindingResult().getFieldErrors().forEach(error -> {
//            fieldErrors.put(error.getField(), error.getDefaultMessage());
//        });
//        errorDetails.put("fieldErrors", fieldErrors);
//        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(CustomException.class)
//    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException ex, HttpServletRequest request) {
//        Map<String, Object> errorDetails = createErrorResponse(HttpStatus.valueOf(ex.getStatusCode()), ex.getMessage(),
//                request.getRequestURI());
//        return new ResponseEntity<>(errorDetails, HttpStatus.valueOf(ex.getStatusCode()));
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex, HttpServletRequest request) {
//        Map<String, Object> errorDetails = createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(),
//                request.getRequestURI());
//        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(NullPointerException.class)
//    public ResponseEntity<Map<String, Object>> handleNullPointerException(NullPointerException ex,
//            HttpServletRequest request) {
//        Map<String, Object> errorDetails = createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(),
//                request.getRequestURI());
//        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex,
//            HttpServletRequest request) {
//        Map<String, Object> errorDetails = createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(),
//                request.getRequestURI());
//        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex,
//            HttpServletRequest request) {
//        Map<String, Object> errorDetails = createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(),
//                request.getRequestURI());
//        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(NoSuchMethodException.class)
//    public ResponseEntity<Map<String, Object>> handleNoSuchMethodException(NoSuchMethodException ex,
//            HttpServletRequest request) {
//        Map<String, Object> errorDetails = createErrorResponse(HttpStatus.METHOD_NOT_ALLOWED,
//                "Method not found: " + ex.getMessage(), request.getRequestURI());
//        return new ResponseEntity<>(errorDetails, HttpStatus.METHOD_NOT_ALLOWED);
//    }
//
//    @ExceptionHandler(UnauthorizedException.class)
//    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(UnauthorizedException ex,
//            HttpServletRequest request) {
//        Map<String, Object> errorDetails = createErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(),
//                request.getRequestURI());
//        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex,
//            HttpServletRequest request) {
//        Map<String, Object> errorDetails = createErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(),
//                request.getRequestURI());
//        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
//    }
//}
//package com;


