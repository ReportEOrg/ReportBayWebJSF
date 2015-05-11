package org.reportbay.web.bean.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.reportbay.web.dto.report.LiteReport;
import org.reportbay.web.service.report.ReportService;
import org.reportbay.web.service.report.exception.ReportServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Report Listing JSF Backing bean 
 *
 */
@Named("reportListBean")
@ViewScoped
public class ReportListBean implements Serializable{

	private static final Logger LOG = LoggerFactory.getLogger(ReportListBean.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ReportService reportService;
	

	private List<LiteReport> reportList = new ArrayList<LiteReport>();
	
	public void init() {
		buildReportList();
	}

	/**
	 * @return the reportList
	 */
	public List<LiteReport> getReportList() {
		
		return reportList;
	}

	/**
	 * @param reportList the reportList to set
	 */
	public void setReportList(List<LiteReport> reportList) {
		this.reportList = reportList;
	}
	
	private void buildReportList(){
		try{
			reportList.clear();
		
			reportList.addAll(reportService.getReports());
		}
		catch(ReportServiceException rtse){
			LOG.error("failed to find all report template");
		}
	}
	
	/**
	 * Ajax action event handler method
	 * @param actionEvent
	 */
	public void displayReportDetails(ActionEvent actionEvent){
		Object obj = actionEvent.getComponent().getAttributes().get("viewReport");
		LiteReport liteReport = (LiteReport)obj;
				
		Map<String, List<String>> requestParams = new HashMap<String, List<String>>();
		
		requestParams.put("templateId", new ArrayList<String>());
		requestParams.put("reportId", new ArrayList<String>());
		
		requestParams.get("templateId").add(String.valueOf(liteReport.getTemplateId()));
		requestParams.get("reportId").add(String.valueOf(liteReport.getId()));
		
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentWidth", 850);
		options.put("contentHeight", 440);
		
		RequestContext.getCurrentInstance().openDialog("display_report", options, requestParams);
	}
}