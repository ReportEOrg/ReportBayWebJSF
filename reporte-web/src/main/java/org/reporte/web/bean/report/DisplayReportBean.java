package org.reporte.web.bean.report;

import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.codec.binary.Base64;
import org.primefaces.model.chart.ChartModel;
import org.reporte.web.common.ChartTypeEnum;
import org.reporte.web.dto.reportgen.Report;
import org.reporte.web.dto.reporttemplate.ReportTemplate;
import org.reporte.web.dto.reporttemplate.RestReportTemplate;
import org.reporte.web.service.publish.PublishService;
import org.reporte.web.service.publish.exception.PublishServiceException;
import org.reporte.web.service.publish.impl.DropBoxPublishServiceImpl;
import org.reporte.web.service.reportgen.ReportGenService;
import org.reporte.web.service.reportgen.exception.ReportGenServiceException;
import org.reporte.web.service.reporttemplate.ReportTemplateService;
import org.reporte.web.service.reporttemplate.exception.ReportTemplateServiceException;
import org.reporte.web.util.ReportUtil;
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
		
		if(chartImageBase64!=null && chartImageBase64.split(",").length > 1){
			
			List<String> errorList = new ArrayList<String>();
			
			String encoded = chartImageBase64.split(",")[1];
			
			//1. decode the base64 string image
			byte[] decoded = Base64.decodeBase64(encoded);
			
			File tempFile = null;

			try(ByteArrayInputStream bais = new ByteArrayInputStream(decoded)){
				//2. create a temp file
				tempFile = File.createTempFile("chart", ".png");
				
				//3. convert the input stream to image
				RenderedImage renderedImage = ImageIO.read(bais);
				
				//4. write image to temp file
				ImageIO.write(renderedImage, "png", tempFile);
				
				//5. publish to the user configured publish target(s)
				errorList.addAll(publish(reportDisplayName+".png", tempFile));
				
			}
			catch(IOException e){
				LOG.error("Failed convert stream to image ", e);
				errorList.add("Convert image ");
			}
			finally{
				if(tempFile!=null){
					//remove the temp file
					tempFile.delete();
				}
			}
			
			if(!errorList.isEmpty()){
				//TODO: ui handle failed services
			}
			
		}
	}
	
	/**
	 * @param publishName
	 * @param reportImageFile
	 */
	private List<String> publish(String publishName, File reportImageFile) {
		List<String> failedServiceNameList = new ArrayList<String>();
		
		//obtain user's pre-configured publish service(s) 
		List<PublishService> userPublishServices = retrieveUserPublishServices();
		
		//prepare the param map required based on user credential
		Map<String, Object> publishParamMap = preparePublishParamMap();
		
		for (PublishService publishService : userPublishServices) {
			//perform publishing the report
			try {
				publishService.publish(publishName,reportImageFile,publishParamMap);
			} catch (PublishServiceException e) {
				LOG.warn("Service "+publishService.getServiceName()+" failed to publish",e);
				failedServiceNameList.add(String.format("Publish to %s ",publishService.getServiceName()));
			}
		}
		
		return failedServiceNameList;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<PublishService> retrieveUserPublishServices(){
		//TODO: obtain the configure publish service from user credential
		
		List<PublishService> userPublishServices = new ArrayList<PublishService>();
		
		//TODO: replace the following temporary code
		userPublishServices.add(new DropBoxPublishServiceImpl());
		
		return userPublishServices;
	}
	
	
	private Map<String, Object> preparePublishParamMap(){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		String accessToken = System.getProperty("dropbox.access.token", "Y9xnDyZNbhAAAAAAAAAACHCXh3i41MnsYL5XJfyqsrTZ9LzoO0sg9BCacLo-9nlj");
		//TODO: obtain required info from user credential
		paramMap.put(DropBoxPublishServiceImpl.ACCESS_TOKEN, accessToken);
		
		return paramMap;
		
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