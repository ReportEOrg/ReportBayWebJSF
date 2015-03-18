package org.reporte.web.service.reporttemplate.exception;

public class ReportTemplateServiceException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 * @param message
	 */
	public ReportTemplateServiceException(String message) {
		super(message);
	}
	/**
	 * 
	 * @param cause
	 */
	public ReportTemplateServiceException(Throwable cause) {
		super(cause);
	}
	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public ReportTemplateServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}