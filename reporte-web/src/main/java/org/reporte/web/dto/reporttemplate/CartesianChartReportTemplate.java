package org.reporte.web.dto.reporttemplate;


public class CartesianChartReportTemplate extends ChartReportTemplate{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String xAxisTitle;
	private String yAxisTitle;
	private boolean showXAxis;
	private boolean showYAxis;
	private boolean showDataLabel;
	private String modelDataLabelField;
	private String modelDataValueField;
	private String modelSeriesGroupField;
	
//	private List<TemplateSeries> dataSeries = new ArrayList<TemplateSeries>();
	/**
	 * @return the xAxisTitle
	 */
	public String getXaxisTitle() {
		return xAxisTitle;
	}
	/**
	 * @param xAxisTitle the xAxisTitle to set
	 */
	public void setXaxisTitle(String xAxisTitle) {
		this.xAxisTitle = xAxisTitle;
	}
	/**
	 * @return the yAxisTitle
	 */
	public String getYaxisTitle() {
		return yAxisTitle;
	}
	/**
	 * @param yAxisTitle the yAxisTitle to set
	 */
	public void setYaxisTitle(String yAxisTitle) {
		this.yAxisTitle = yAxisTitle;
	}
	/**
	 * @return the showXAxis
	 */
	public boolean isShowXAxis() {
		return showXAxis;
	}
	/**
	 * @param showXAxis the showXAxis to set
	 */
	public void setShowXAxis(boolean showXAxis) {
		this.showXAxis = showXAxis;
	}
	/**
	 * @return the showYAxis
	 */
	public boolean isShowYAxis() {
		return showYAxis;
	}
	/**
	 * @param showYAxis the showYAxis to set
	 */
	public void setShowYAxis(boolean showYAxis) {
		this.showYAxis = showYAxis;
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
	 * @return the modelDataLabelField
	 */
	public String getModelDataLabelField() {
		return modelDataLabelField;
	}
	/**
	 * @param modelDataLabelField the modelDataLabelField to set
	 */
	public void setModelDataLabelField(String modelDataLabelField) {
		this.modelDataLabelField = modelDataLabelField;
	}
	/**
	 * @return the modelDataValueField
	 */
	public String getModelDataValueField() {
		return modelDataValueField;
	}
	/**
	 * @param modelDataValueField the modelDataValueField to set
	 */
	public void setModelDataValueField(String modelDataValueField) {
		this.modelDataValueField = modelDataValueField;
	}
	/**
	 * @return the modelSeriesGroupField
	 */
	public String getModelSeriesGroupField() {
		return modelSeriesGroupField;
	}
	/**
	 * @param modelSeriesGroupField the modelSeriesGroupField to set
	 */
	public void setModelSeriesGroupField(String modelSeriesGroupField) {
		this.modelSeriesGroupField = modelSeriesGroupField;
	}
//	/**
//	 * @return the dataSeries
//	 */
//	public List<TemplateSeries> getDataSeries() {
//		return dataSeries;
//	}
//	/**
//	 * @param dataSeries the dataSeries to set
//	 */
//	public void setDataSeries(List<TemplateSeries> dataSeries) {
//		this.dataSeries = dataSeries;
//	}
	
}