package mu.mcb.property.evalution.exception;

import org.springframework.http.HttpStatus;

/**
 * The Class GenericException.
 */
public class GenericException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The message. */
	private String message;
	
	/** The http status. */
	private HttpStatus httpStatus;

	/**
	 * Instantiates a new generic exception.
	 *
	 * @param message the message
	 * @param httpStatus the http status
	 */
	public GenericException(String message, HttpStatus httpStatus) {
		super(message);
		this.message = message;
		this.httpStatus = httpStatus;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the http status.
	 *
	 * @return the http status
	 */
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
