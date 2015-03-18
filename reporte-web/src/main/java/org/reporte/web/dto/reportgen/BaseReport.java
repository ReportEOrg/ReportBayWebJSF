package org.reporte.web.dto.reportgen;

import java.io.Serializable;

public abstract class BaseReport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String reportName;
	/**
	 * 
	 */
	private String reportType;
	/**
	 * 
	 * @return 
	 */
	public String getReportName() {
	 	 return reportName; 
	}
	/**
	 * 
	 * @param reportName 
	 */
	public void setReportName(String reportName) { 
		 this.reportName = reportName; 
	}
	/**
	 * 
	 * @return 
	 */
	public String getReportType() {
	 	 return reportType; 
	}
	/**
	 * 
	 * @param reportType 
	 */
	public void setReportType(String reportType) { 
		 this.reportType = reportType; 
	} 

}
