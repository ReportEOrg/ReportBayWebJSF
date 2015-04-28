package org.reportbay.web.service.reportgen.exception;


public class ReportGenServiceException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 * @param message
	 */
	public ReportGenServiceException(String message) {
		super(message);
	}
	/**
	 * 
	 * @param cause
	 */
	public ReportGenServiceException(Throwable cause) {
		super(cause);
	}
	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public ReportGenServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}