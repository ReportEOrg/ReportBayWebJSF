package org.reportbay.web.dto.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LiteReports implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<LiteReport>liteReports = new ArrayList<LiteReport>();

	/**
	 * @return the liteReports
	 */
	public List<LiteReport> getLiteReports() {
		return liteReports;
	}

	/**
	 * @param liteReports the liteReports to set
	 */
	public void setLiteReports(List<LiteReport> liteReports) {
		this.liteReports = liteReports;
	}
}