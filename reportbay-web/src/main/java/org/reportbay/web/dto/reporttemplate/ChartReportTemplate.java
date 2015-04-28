package org.reportbay.web.dto.reporttemplate;

import java.util.ArrayList;
import java.util.List;

public class ChartReportTemplate extends BaseReportTemplate{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String title;
	private boolean showLegend;
	
	//TODO: to removed once entity is refactor
	private List<TemplateSeries> dataSeries = new ArrayList<TemplateSeries>();

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the showLegend
	 */
	public boolean isShowLegend() {
		return showLegend;
	}
	/**
	 * @param showLegend the showLegend to set
	 */
	public void setShowLegend(boolean showLegend) {
		this.showLegend = showLegend;
	}
	/**
	 * @return the dataSeries
	 */
	public List<TemplateSeries> getDataSeries() {
		return dataSeries;
	}
	/**
	 * @param dataSeries the dataSeries to set
	 */
	public void setDataSeries(List<TemplateSeries> dataSeries) {
		this.dataSeries = dataSeries;
	}
}