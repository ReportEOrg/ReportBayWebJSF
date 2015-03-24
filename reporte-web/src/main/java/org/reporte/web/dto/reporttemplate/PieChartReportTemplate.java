package org.reporte.web.dto.reporttemplate;

import org.reporte.web.common.PieChartDataFormatEnum;

public class PieChartReportTemplate extends ChartReportTemplate{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String categoryName;
	private String modelCategoryField;
	private String modelDataField;
	private boolean showDataLabel;
	private PieChartDataFormatEnum dataTypeFormat;
	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}
	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	/**
	 * @return the modelCategoryField
	 */
	public String getModelCategoryField() {
		return modelCategoryField;
	}
	/**
	 * @param modelCategoryField the modelCategoryField to set
	 */
	public void setModelCategoryField(String modelCategoryField) {
		this.modelCategoryField = modelCategoryField;
	}
	/**
	 * @return the modelDataField
	 */
	public String getModelDataField() {
		return modelDataField;
	}
	/**
	 * @param modelDataField the modelDataField to set
	 */
	public void setModelDataField(String modelDataField) {
		this.modelDataField = modelDataField;
	}
	/**
	 * @return the showDataLabel
	 */
	public boolean isShowDataLabel() {
		return showDataLabel;
	}
	/**
	 * @param showDataLabel the showDataLabel to set
	 */
	public void setShowDataLabel(boolean showDataLabel) {
		this.showDataLabel = showDataLabel;
	}
	/**
	 * @return the dataTypeFormat
	 */
	public PieChartDataFormatEnum getDataTypeFormat() {
		return dataTypeFormat;
	}
	/**
	 * @param dataTypeFormat the dataTypeFormat to set
	 */
	public void setDataTypeFormat(PieChartDataFormatEnum dataTypeFormat) {
		this.dataTypeFormat = dataTypeFormat;
	}
	
}