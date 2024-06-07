package mu.mcb.property.evalution.exception;


/**
 * The Class ErrorResponse.
 *
 * @param <T> the generic type
 */
public class ErrorResponse<T> {

	/** The status. */
	private final String status;

	/** The cause. */
	private final T cause;

	/**
	 * Instantiates a new error response.
	 *
	 * @param status the status
	 * @param cause the cause
	 */
	public ErrorResponse(final String status, final T cause) {
		super();
		this.status = status;
		this.cause = cause;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Gets the cause.
	 *
	 * @return the cause
	 */
	public T getCause() {
		return cause;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "ErrorResponse [status=" + status + ", cause=" + cause + "]";
	}
}
