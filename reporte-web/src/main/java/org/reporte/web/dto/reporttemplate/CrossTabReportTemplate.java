package org.reporte.web.dto.reporttemplate;

import java.util.List;

public class CrossTabReportTemplate extends BaseReportTemplate{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<CrossTabTemplateDetail> crossTabDetail;

	/**
	 * @return the crossTabDetail
	 */
	public List<CrossTabTemplateDetail> getCrossTabDetail() {
		return crossTabDetail;
	}

	/**
	 * @param crossTabDetail the crossTabDetail to set
	 */
	public void setCrossTabDetail(List<CrossTabTemplateDetail> crossTabDetail) {
		this.crossTabDetail = crossTabDetail;
	}
}