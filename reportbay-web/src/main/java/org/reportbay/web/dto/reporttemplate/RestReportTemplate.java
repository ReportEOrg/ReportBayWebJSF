package org.reportbay.web.dto.reporttemplate;

import java.io.Serializable;

import org.reportbay.web.common.ChartTypeEnum;


public class RestReportTemplate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ChartTypeEnum type;
	
	private CartesianChartReportTemplate cartesianChartTemplate;
	
	private PieChartReportTemplate pieChartTemplate;
	
	private CrossTabReportTemplate crossTabTemplate;

	/**
	 * @return the type
	 */
	public ChartTypeEnum getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ChartTypeEnum type) {
		this.type = type;
	}

	/**
	 * @return the cartesianChartTemplate
	 */
	public CartesianChartReportTemplate getCartesianChartTemplate() {
		return cartesianChartTemplate;
	}

	/**
	 * @param cartesianChartTemplate the cartesianChartTemplate to set
	 */
	public void setCartesianChartTemplate(
			CartesianChartReportTemplate cartesianChartTemplate) {
		this.cartesianChartTemplate = cartesianChartTemplate;
	}

	/**
	 * @return the pieChartTemplate
	 */
	public PieChartReportTemplate getPieChartTemplate() {
		return pieChartTemplate;
	}

	/**
	 * @param pieChartTemplate the pieChartTemplate to set
	 */
	public void setPieChartTemplate(PieChartReportTemplate pieChartTemplate) {
		this.pieChartTemplate = pieChartTemplate;
	}

	/**
	 * @return the crossTabTemplate
	 */
	public CrossTabReportTemplate getCrossTabTemplate() {
		return crossTabTemplate;
	}

	/**
	 * @param crossTabTemplate the crossTabTemplate to set
	 */
	public void setCrossTabTemplate(CrossTabReportTemplate crossTabTemplate) {
		this.crossTabTemplate = crossTabTemplate;
	}
}