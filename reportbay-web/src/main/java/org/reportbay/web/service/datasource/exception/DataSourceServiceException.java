package org.reportbay.web.service.datasource.exception;

public class DataSourceServiceException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 * @param message
	 */
	public DataSourceServiceException(String message) {
		super(message);
	}
	/**
	 * 
	 * @param cause
	 */
	public DataSourceServiceException(Throwable cause) {
		super(cause);
	}
	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public DataSourceServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}