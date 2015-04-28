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
import org.reportbay.web.dto.reporttemplate.LiteReportTemplate;
import org.reportbay.web.service.reporttemplate.ReportTemplateService;
import org.reportbay.web.service.reporttemplate.exception.ReportTemplateServiceException;
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
	
	
	//TODO: replaced with report service
	@Inject
	private ReportTemplateService reportTemplateService;
	

	private List<LiteReportTemplate> reportList = new ArrayList<LiteReportTemplate>();
	
	public void init() {
		buildReportList();
	}

	/**
	 * @return the reportList
	 */
	public List<LiteReportTemplate> getReportList() {
		
		return reportList;
	}

	/**
	 * @param reportList the reportList to set
	 */
	public void setReportList(List<LiteReportTemplate> reportList) {
		this.reportList = reportList;
	}
	
	private void buildReportList(){
		try{
			reportList.clear();
		
			reportList.addAll(reportTemplateService.findAll());
		}
		catch(ReportTemplateServiceException rtse){
			LOG.error("failed to find all report template");
		}
	}
	
	/**
	 * Ajax action event handler method
	 * @param actionEvent
	 */
	public void displayReportDetails(ActionEvent actionEvent){
		Object obj = actionEvent.getComponent().getAttributes().get("viewReport");
				
		Map<String, List<String>> requestParams = new HashMap<String, List<String>>();
		
		requestParams.put("templateId", new ArrayList<String>());
		requestParams.put("templateType", new ArrayList<String>());
		
		requestParams.get("templateId").add(String.valueOf(((LiteReportTemplate)obj).getId()));
		
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentWidth", 850);
		options.put("contentHeight", 440);
		
		RequestContext.getCurrentInstance().openDialog("display_report", options, requestParams);
	}
}