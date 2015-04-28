package org.reportbay.web.dto.reportgen;

import java.io.Serializable;


public class Report implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//TODO: change to enum
	private String type;
	
	private CartesianChartReport cartesianChartReport;
	private PieChartReport pieChartReport;
	private CrossTabReport crossTabReport;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the cartesianChartReport
	 */
	public CartesianChartReport getCartesianChartReport() {
		return cartesianChartReport;
	}

	/**
	 * @param cartesianChartReport the cartesianChartReport to set
	 */
	public void setCartesianChartReport(CartesianChartReport cartesianChartReport) {
		this.cartesianChartReport = cartesianChartReport;
	}

	/**
	 * @return the pieChartReport
	 */
	public PieChartReport getPieChartReport() {
		return pieChartReport;
	}

	/**
	 * @param pieChartReport the pieChartReport to set
	 */
	public void setPieChartReport(PieChartReport pieChartReport) {
		this.pieChartReport = pieChartReport;
	}

	/**
	 * @return the crossTabReport
	 */
	public CrossTabReport getCrossTabReport() {
		return crossTabReport;
	}

	/**
	 * @param crossTabReport the crossTabReport to set
	 */
	public void setCrossTabReport(CrossTabReport crossTabReport) {
		this.crossTabReport = crossTabReport;
	}
}