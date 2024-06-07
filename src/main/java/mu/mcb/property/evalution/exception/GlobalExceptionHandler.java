
package mu.mcb.property.evalution.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

/**
 * The Class GlobalExceptionHandler.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(GlobalExceptionHandler.class);
	
	/** The Constant DEFAULT_ERR_LOG_STR. */
	private static final String DEFAULT_ERR_LOG_STR = "Error occur: {}";

	/**
	 * Handle elms exception.
	 *
	 * @param ex the ex
	 * @return the response entity
	 */
	@ExceptionHandler(GenericException.class)
	public ResponseEntity<ErrorResponse<String>> handleElmsException(GenericException ex) {
		ErrorResponse<String> errorRes = new ErrorResponse<>(ex.getHttpStatus().value() + "", ex.getMessage());
		LOGGER.error(DEFAULT_ERR_LOG_STR, errorRes);
		return new ResponseEntity<>(errorRes, ex.getHttpStatus());
	}

	/**
	 * Handle unknown exception.
	 *
	 * @param ex the ex
	 * @return the response entity
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse<String>> handleUnknownException(Exception ex) {
		ErrorResponse<String> errorRes = new ErrorResponse<>("500", ex.getMessage());
		LOGGER.error(DEFAULT_ERR_LOG_STR, errorRes);
		return new ResponseEntity<>(errorRes, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handle token refresh exception.
	 *
	 * @param ex the ex
	 * @param request the request
	 * @return the response entity
	 */
	@ExceptionHandler(value = TokenRefreshException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<ErrorResponse<String>> handleTokenRefreshException(TokenRefreshException ex,
			WebRequest request) {
		ErrorResponse<String> errorRes = new ErrorResponse<>("401", ex.getMessage());
		LOGGER.error(DEFAULT_ERR_LOG_STR, errorRes);
		return new ResponseEntity<>(errorRes, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Handle access denied exception.
	 *
	 * @param ex the ex
	 * @return the response entity
	 */
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<ErrorResponse<String>> handleAccessDeniedException(AccessDeniedException ex) {
		ErrorResponse<String> errorRes = new ErrorResponse<>("403", "User is not authorized for this action");
		LOGGER.error(DEFAULT_ERR_LOG_STR, errorRes);
		return new ResponseEntity<>(errorRes, HttpStatus.FORBIDDEN);
	}
}

/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED
 *
 * Sensproducts
 */
