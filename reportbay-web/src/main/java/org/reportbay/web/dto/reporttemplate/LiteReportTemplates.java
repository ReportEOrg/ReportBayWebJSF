package org.reportbay.web.dto.reporttemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Wrapper POJO for holding Lite version of report templates for minimum listing display
 *
 */
public class LiteReportTemplates implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<LiteReportTemplate> connectors = new ArrayList<LiteReportTemplate> ();

	/**
	 * @return the connectors
	 */
	public List<LiteReportTemplate> getConnectors() {
		return connectors;
	}

	/**
	 * @param connectors the connectors to set
	 */
	public void setConnectors(List<LiteReportTemplate> connectors) {
		this.connectors = connectors;
	}
	
}