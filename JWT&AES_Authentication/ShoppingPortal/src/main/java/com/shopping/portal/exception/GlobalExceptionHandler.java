package com.shopping.portal.exception;

import java.util.Date;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.nio.file.AccessDeniedException;
import java.text.SimpleDateFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {

	@ExceptionHandler(CustomApplicationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleCustomApplicationException(CustomApplicationException ex,
			HttpServletRequest request) {
		HttpStatus status = HttpStatus.valueOf(ex.getStatus());
		ErrorResponse response = createErrorResponse(status, ex.getMessage(), request);
		return new ResponseEntity<>(response, status);
	}

	@ExceptionHandler(ResponseStatusException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ErrorResponse> handleUnauthorizedException(ResponseStatusException ex,
			HttpServletRequest request) {
		ErrorResponse response = createErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<ErrorResponse> handleForbiddenException(AccessDeniedException ex,
			HttpServletRequest request) {
		ErrorResponse response = createErrorResponse(HttpStatus.FORBIDDEN, "FORBIDDEN", request);
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorResponse> handleNotFoundException(NoHandlerFoundException ex,
			HttpServletRequest request) {
		ErrorResponse response = createErrorResponse(HttpStatus.NOT_FOUND, "Resource not found", request);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public ResponseEntity<ErrorResponse> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException ex,
			HttpServletRequest request) {
		ErrorResponse response = createErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed", request);
		return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorResponse> handleInternalServerError(Exception ex, HttpServletRequest request) {
		ErrorResponse response = createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
				"An internal server error occurred", request);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ErrorResponse createErrorResponse(HttpStatus status, String message, HttpServletRequest request) {
		SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setTimestamp(dateTimeFormatter.format(new Date()));
		errorResponse.setStatus(status.value());
		errorResponse.setError(status.getReasonPhrase());
		errorResponse.setMessage(message);
		errorResponse.setPath(getRequestPath(request)); // Include request path
		return errorResponse;
	}

	private String getRequestPath(HttpServletRequest request) {
		return request.getRequestURI();
	}

	class ErrorResponse {
		private String timestamp;
		private int status;
		private String error;
		private String message;
		private String path;

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}
	}
}
