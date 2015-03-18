package org.reporte.web.bean.reporttemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.chart.ChartModel;
import org.reporte.web.common.ChartTypeEnum;
import org.reporte.web.common.PieChartDataFormatEnum;
import org.reporte.web.common.SqlTypeEnum;
import org.reporte.web.dto.model.AttributeMapping;
import org.reporte.web.dto.model.Model;
import org.reporte.web.dto.reportgen.Report;
import org.reporte.web.dto.reporttemplate.ReportTemplate;
import org.reporte.web.dto.reporttemplate.TemplateSeries;
import org.reporte.web.service.model.ModelService;
import org.reporte.web.service.model.exception.ModelServiceException;
import org.reporte.web.service.reportgen.ReportGenService;
import org.reporte.web.service.reportgen.exception.ReportGenServiceException;
import org.reporte.web.service.reporttemplate.ReportTemplateService;
import org.reporte.web.service.reporttemplate.exception.ReportTemplateServiceException;
import org.reporte.web.util.ReportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Report Template Wizard JSF backing bean 
 *
 */
@Named("reportTemplateWizard")
@ViewScoped
public class ReportTemplateWizardBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = LoggerFactory.getLogger(ReportTemplateWizardBean.class);

	private ReportTemplate reportTemplate;
	
	private String selectedChartType;
	
	private Map<String, String> chartTypeOptions;
	
	private String selectedModel;
	
	private Map<String, String> availableModelOptions = new LinkedHashMap<String, String>();
	private Map<String, Model> availableModelMap = new HashMap<String, Model>();
	
	private Map<String, String> dataValueFieldOptions = new LinkedHashMap<String, String>();
	private Map<String, String> modelFieldOptions = new LinkedHashMap<String, String>();
	private Map<String, String> pieDataFormatOptions = new LinkedHashMap<String, String>();
	
	private Map<String, String> templateSeriesValueOptions = new LinkedHashMap<String, String>();
	
	private ChartModel pfChartModel;
	
	@Inject
	private ModelService modelService;

	@Inject
	private ReportTemplateService reportTemplateService;

	@Inject 
	private ReportGenService reportGenService;
	
	@PostConstruct
	public void init() {
		chartTypeOptions = new LinkedHashMap<String, String>();
		
		for(ChartTypeEnum ref: ChartTypeEnum.values()){
			chartTypeOptions.put(ref.name(), ref.getDescription());
		}
		
		for(PieChartDataFormatEnum ref: PieChartDataFormatEnum.values()){
			pieDataFormatOptions.put(ref.name(), ref.getCode());
		}
		reportTemplate = new ReportTemplate();
		//default as percent
		reportTemplate.setDataTypeFormat(PieChartDataFormatEnum.PERCENT); 
	}
	
	/**
	 * re initialized pre request option
	 */
	public void initOptions(){
		reloadAvailableModel();
	}
	
	private void reloadAvailableModel(){
		
		availableModelOptions.clear();
		availableModelMap.clear();

		try {
			List<Model> availableModelList = modelService.findAll();
			
			for (Model model : availableModelList) {
				String key = String.valueOf(model.getId());
				availableModelOptions.put(key, model.getName());
				availableModelMap.put(key, model);
			}
		} 
		catch (ModelServiceException e) {
			LOG.warn("Failed loading available models", e);
		}
	}
	/**
	 * handle model change event
	 * @param event
	 */
	public void handleModelChange(AjaxBehaviorEvent event) {
		LOG.info("model changed "+selectedModel);
		
		Model model = availableModelMap.get(selectedModel);
		
		prepareFieldOptions(model);
			
		reportTemplate.setModel(model);
	}
	
	/**
	 * 
	 * @param model
	 */
	private void prepareFieldOptions(Model model){
		dataValueFieldOptions.clear();
		modelFieldOptions.clear();
		
		if(model!=null){
			for (AttributeMapping attMapping : model.getAttributeBindings()){
				
				SqlTypeEnum sqlType = SqlTypeEnum.fromString(attMapping.getTypeName());
				
				if(sqlType!=null){
					modelFieldOptions.put(attMapping.getAlias(), attMapping.getAlias());
					switch(sqlType){
						case BIGINT:
						case DECIMAL:
						case DOUBLE:
						case FLOAT:
						case INTEGER:
						case NUMERIC:
						case REAL:
						case SMALLINT:
						case TINYINT:
							dataValueFieldOptions.put(attMapping.getAlias(), attMapping.getAlias());
							break;
						default:
							break;
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param event
	 */
	public void handleChartTypeChange(AjaxBehaviorEvent event) {
		Object eventSource = event.getSource();
		
		if(eventSource instanceof HtmlSelectOneMenu){
			HtmlSelectOneMenu htmlSelectOneMenu = (HtmlSelectOneMenu) eventSource;
			ChartTypeEnum chartType = ChartTypeEnum.fromName(htmlSelectOneMenu.getValue().toString());
			
			reportTemplate.setChartType(chartType);
			
			selectedChartType = (chartType==null? "" : chartType.name());
		}
	}
	
	/**
	 * 
	 * @param actionEvent
	 */
	public void handleAddSeries(ActionEvent actionEvent) {
		TemplateSeries templateSeries = new TemplateSeries();
		templateSeries.setName("");
		templateSeries.setModelSeriesValue("");
		
		List<TemplateSeries> templateSeriesSet = reportTemplate.getTemplateSeries();
		
		if(templateSeriesSet == null){
			templateSeriesSet = new ArrayList<TemplateSeries>();
			reportTemplate.setTemplateSeries(templateSeriesSet);
		}
		
		templateSeriesSet.add(templateSeries);
	}
	
	/**
	 * 
	 * @param event
	 */
	public void handleSeriesGroupChange(AjaxBehaviorEvent event){
		
		String modelSeriesGroupField = ((SelectOneMenu) event.getSource()).getValue().toString();
		
		try {
			if(StringUtils.isNotBlank(modelSeriesGroupField)){
				List<String> resultList = modelService.getModelFieldUniqueValue(reportTemplate.getModel().getId(), modelSeriesGroupField);
				
				templateSeriesValueOptions.clear();

				for (String result : resultList) {
					templateSeriesValueOptions.put(result, result);
				}
			}
		} 
		catch (ModelServiceException e) {
			LOG.warn("Failed to retrieve value on model series group ", e);
		} 
		
	}
	/**
	 * 
	 * @param actionEvent
	 */
	public void handleRemoveSeries(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof HtmlCommandLink) {
			HtmlCommandLink removeSeriesLink = (HtmlCommandLink) actionEvent.getSource();
			DataTable dataTable = (DataTable) removeSeriesLink.getParent().getParent();
			int currentIndex = dataTable.getRowIndex();
			
			List<TemplateSeries> templateSeriesSet = reportTemplate.getTemplateSeries();
			
			//Note: due to the use of business key in hashcode(), business key updated before performing remove, so can't use Set.remove()
			if(templateSeriesSet!=null){
				
				Set<TemplateSeries> tempSeriesSet = new LinkedHashSet<TemplateSeries>();

				int idx = 0;

				for (TemplateSeries templateSeries : templateSeriesSet) {
					
					if(idx != currentIndex){
						tempSeriesSet.add(templateSeries);
					}
					idx++;
				}
				
				//clear existing
				reportTemplate.getTemplateSeries().clear();
				//add back the temp with removed entry
				reportTemplate.getTemplateSeries().addAll(tempSeriesSet);
			}
		}
	}
	
	/**
	 * 
	 */
	public void handleSaveOrUpdate(){
		
		try{
			if(reportTemplate.getId()>0){
				reportTemplateService.update(reportTemplate);
			}
			else{
				reportTemplateService.save(reportTemplate);
			}
		}
		catch(ReportTemplateServiceException e){
			LOG.error("Failed save or update",e);
			//TODO: UI handling
		}
		//in even of error must close the dialog too
		finally{
			// Prepare data to pass it back to whatever that opened this dialog.
			Map<String, Object> data = new HashMap<String, Object>();
			//TODO: status
			
			//passed status back and close dialog
			RequestContext.getCurrentInstance().closeDialog(data);
		}
	}

	
	
	public void cancel() {
		RequestContext.getCurrentInstance().closeDialog(null);
	}
		
	/**
	 * due to ui design, if axis title not present, need to set the show Axis indicator accordingly
	 */
	private void updateShowAxisIndicator(){
		reportTemplate.setShowXAxis(StringUtils.isNotBlank(reportTemplate.getxAxisTitle()));
		reportTemplate.setShowYAxis(StringUtils.isNotBlank(reportTemplate.getyAxisTitle()));
	}
	
	/**
	 * 
	 * @return
	 */
	public ReportTemplate getReportTemplate() {
		return reportTemplate;
	}

	/**
	 * 
	 * @param reportTemplate
	 */
	public void setReportTemplate(ReportTemplate reportTemplate) {
		this.reportTemplate = reportTemplate;
	}

	/**
	 * 
	 * @param event
	 * @return
	 * @throws ReportGenServiceException 
	 */
	public String onFlowProcess(FlowEvent event) {
		//if transistion from tab "configuration" to "preview"
		if ("configuration".equals(event.getOldStep()) &&
			"preview".equals(event.getNewStep())) {
			try{
				generatePreview();
			}
			catch(ReportGenServiceException rgse){
				LOG.error("failed report preview generation", rgse);
				//TODO: handling
			}
		}
//		else if ("model".equals(event.getOldStep()) &&
//				 "configuration".equals(event.getNewStep())){
//					 
//		}
		return event.getNewStep();
	}
	
	/**
	 * @throws ReportGenServiceException 
	 * 
	 */
	private void generatePreview() throws ReportGenServiceException{
		ChartTypeEnum chartType = ChartTypeEnum.fromName(selectedChartType);
		
		if(chartType!=null){
			
			switch(chartType){
				case AREA:
				case BAR:
				case COLUMN:
				case LINE:
					updateShowAxisIndicator();
					break;
				case PIE:
					break;
				default:
					//TODO: handle unrecognized type
					break;
			}
		}
		
		Report report = reportGenService.generateReportPreview(reportTemplate);
		
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
	}
	
	
	/**
	 * @return the selectedChartType
	 */
	public String getSelectedChartType() {
		return selectedChartType;
	}

	/**
	 * @param selectedChartType the selectedChartType to set
	 */
	public void setSelectedChartType(String selectedChartType) {
		this.selectedChartType = selectedChartType;
	}

	/**
	 * @return the chartTypesOptions
	 */
	public Map<String, String> getChartTypeOptions() {
		return chartTypeOptions;
	}

	/**
	 * @param chartTypesOptions the chartTypesOptions to set
	 */
	public void setChartTypeOptions(Map<String, String> chartTypeOptions) {
		this.chartTypeOptions = chartTypeOptions;
	}
	/**
	 * @return the selectedModel
	 */
	public String getSelectedModel() {
		return selectedModel;
	}
	/**
	 * @param selectedModel the selectedModel to set
	 */
	public void setSelectedModel(String selectedModel) {
		this.selectedModel = selectedModel;
	}
	/**
	 * @return the availableModelOptions
	 */
	public Map<String, String> getAvailableModelOptions() {
		return availableModelOptions;
	}
	/**
	 * @param availableModelOptions the availableModelOptions to set
	 */
	public void setAvailableModelOptions(Map<String, String> availableModelOptions) {
		this.availableModelOptions = availableModelOptions;
	}

	/**
	 * @return the isPieChart
	 */
	public boolean isPieChart() {
		
		if(reportTemplate!=null && 
		   ChartTypeEnum.PIE.equals(reportTemplate.getChartType())){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isCartesianChart() {
		
		if(reportTemplate!=null && 
		   (ChartTypeEnum.AREA.equals(reportTemplate.getChartType()) ||
			ChartTypeEnum.BAR.equals(reportTemplate.getChartType()) ||
			ChartTypeEnum.COLUMN.equals(reportTemplate.getChartType()) ||
			ChartTypeEnum.LINE.equals(reportTemplate.getChartType()))){
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * @return the dataValueFieldOptions
	 */
	public Map<String, String> getDataValueFieldOptions() {
		return dataValueFieldOptions;
	}

	/**
	 * @param dataValueFieldOptions the dataValueFieldOptions to set
	 */
	public void setDataValueFieldOptions(Map<String, String> dataValueFieldOptions) {
		this.dataValueFieldOptions = dataValueFieldOptions;
	}

	/**
	 * @return the modelFieldOptions
	 */
	public Map<String, String> getModelFieldOptions() {
		return modelFieldOptions;
	}

	/**
	 * @param modelFieldOptions the modelFieldOptions to set
	 */
	public void setModelFieldOptions(Map<String, String> modelFieldOptions) {
		this.modelFieldOptions = modelFieldOptions;
	}
	
	public Map<String, String> getCategoryFieldOptions() {
		return getModelFieldOptions();
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
	 * @return the templateSeriesValueOptions
	 */
	public Map<String, String> getTemplateSeriesValueOptions() {
		return templateSeriesValueOptions;
	}

	/**
	 * @param templateSeriesValueOptions the templateSeriesValueOptions to set
	 */
	public void setTemplateSeriesValueOptions(Map<String, String> templateSeriesValueOptions) {
		this.templateSeriesValueOptions = templateSeriesValueOptions;
	}

	/**
	 * @return the pieDataFormatOptions
	 */
	public Map<String, String> getPieDataFormatOptions() {
		return pieDataFormatOptions;
	}

	/**
	 * @param pieDataFormatOptions the pieDataFormatOptions to set
	 */
	public void setPieDataFormatOptions(Map<String, String> pieDataFormatOptions) {
		this.pieDataFormatOptions = pieDataFormatOptions;
	}
}