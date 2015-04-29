package org.reportbay.web.bean.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.ChartModel;
import org.reportbay.web.common.ChartTypeEnum;
import org.reportbay.web.dto.publish.Publish;
import org.reportbay.web.dto.reportgen.Report;
import org.reportbay.web.dto.reporttemplate.ReportTemplate;
import org.reportbay.web.dto.reporttemplate.RestReportTemplate;
import org.reportbay.web.service.publish.PublishService;
import org.reportbay.web.service.publish.exception.PublishServiceException;
import org.reportbay.web.service.reportgen.ReportGenService;
import org.reportbay.web.service.reportgen.exception.ReportGenServiceException;
import org.reportbay.web.service.reporttemplate.ReportTemplateService;
import org.reportbay.web.service.reporttemplate.exception.ReportTemplateServiceException;
import org.reportbay.web.util.ReportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Chart Report JSF Backing bean 
 *
 */
@Named("displayReportBean")
@ViewScoped
public class DisplayReportBean implements Serializable{
	
	private static final Logger LOG = LoggerFactory.getLogger(DisplayReportBean.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ChartModel pfChartModel;
	private String chartType;
	
	private String chartImageBase64;
	private String reportDisplayName;
	
	@Inject
	private ReportGenService reportGenService;
	
	@Inject
	private ReportTemplateService reportTemplateService;
	
	@Inject
	private PublishService publishService;

	public void init() {
		// Retrieve the params passed via Dialog Framework.
		Map<String, String> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String templateIdStr = requestParams.get("templateId");
		
		if(templateIdStr!=null){
			try {
				int templateId = Integer.valueOf(templateIdStr);
				
				ReportTemplate reportTemplate = reportTemplateService.find(templateId);
				
				RestReportTemplate restReportTemplate = reportTemplateService.mapUIToRestReportTemplate(reportTemplate);
				
				chartType = reportTemplate.getChartType().getCode();
				reportDisplayName = reportTemplate.getReportDisplayName();
				
				Report report = reportGenService.generateReportPreview(restReportTemplate);
	
				if(report!=null){
					String type = report.getType();
					
					ChartTypeEnum refChartType = ChartTypeEnum.fromName(type);
					
					if(refChartType!=null){
						switch(refChartType){
							case AREA:
								pfChartModel = ReportUtil.mapAreaReportToPFModel(report.getCartesianChartReport());
								break;
							case BAR:
								pfChartModel = ReportUtil.mapBarReportToPFModel(report.getCartesianChartReport());
								break;
							case COLUMN:
								pfChartModel = ReportUtil.mapColumnReportToPFModel(report.getCartesianChartReport());
								break;
							case LINE:
								pfChartModel = ReportUtil.mapLineReportToPFModel(report.getCartesianChartReport());
								break;
							case PIE:
								pfChartModel = ReportUtil.mapPieReportToPFModel(report.getPieChartReport());
								break;
							default:
								//TODO: handling unrecognized type
								break;
						}
					}
					else{
						//TODO: error handling
					}
				}
				
			} catch (NumberFormatException e) {
				LOG.error("invalid template id {}",templateIdStr, e);
			} catch (ReportGenServiceException | ReportTemplateServiceException e) {
				LOG.error("error generating report ",e);
			} 
		}
		else{
			LOG.warn("template id is null, skipped");
		}
	}

	//call back action handler
	public void publishToExternal(ActionEvent event){
		
		List<String> errorList = new ArrayList<String>();
		
		if(chartImageBase64!=null && chartImageBase64.split(",").length > 1){
			
			String encoded = chartImageBase64.split(",")[1];
			
			//TODO: based on the type of report append proper file extension
			
			//1. build publish list
			List<Publish> publishList = buildPublishList(reportDisplayName+".png",encoded);
			
			for(Publish publish: publishList){
				try{
					//2. invoke publish API
					publishService.publish(publish);
				}
				catch(PublishServiceException pse){
					errorList.add(publish.getOption());
				}	
			}
		}
		
		if(!errorList.isEmpty()){
		//TODO: ui handle failed services
		}

	}
	
	/**
	 * 
	 * @param publishName
	 * @param base64Content
	 * @return
	 */
	private List<Publish> buildPublishList(String publishName, String base64Content){
		List<Publish> publishList = new ArrayList<Publish>();
		
		//TODO: based on selected publish option build the publish list
		
		//TODO: replace this
		Publish publish = new Publish();
		publishList.add(publish);
		publish.setOption("Drop Box");
		publish.setPublishName(publishName);
		
		Properties publishParams = new Properties();
		publish.setParams(publishParams);
		publishParams.put("base64Content", base64Content);
		
		return publishList;
	}
	
	/**
	 * @return the pfChartModel
	 */
	public ChartModel getPfChartModel() {
		return pfChartModel;
	}

	/**
	 * @param pfChartModel the pfChartModel to set
	 */
	public void setPfChartModel(ChartModel pfChartModel) {
		this.pfChartModel = pfChartModel;
	}

	/**
	 * @return the chartType
	 */
	public String getChartType() {
		return chartType;
	}

	/**
	 * @param chartType the chartType to set
	 */
	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	/**
	 * 
	 * @return
	 */
	public String getChartImageBase64() {
		return chartImageBase64;
	}

	/**
	 * 
	 * @param chartImageBase64
	 */
	public void setChartImageBase64(String chartImageBase64) {
		this.chartImageBase64 = chartImageBase64;
	}
	
}