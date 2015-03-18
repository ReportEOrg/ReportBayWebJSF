package org.reporte.web.bean.reporttemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.component.html.HtmlInputText;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.chart.ChartModel;
import org.reporte.web.common.ChartTypeEnum;
import org.reporte.web.dto.reportgen.Report;
import org.reporte.web.dto.reporttemplate.LiteReportTemplate;
import org.reporte.web.dto.reporttemplate.ReportTemplate;
import org.reporte.web.service.reportgen.ReportGenService;
import org.reporte.web.service.reportgen.exception.ReportGenServiceException;
import org.reporte.web.service.reporttemplate.ReportTemplateService;
import org.reporte.web.service.reporttemplate.exception.ReportTemplateServiceException;
import org.reporte.web.util.ReportUtil;
import org.reporte.web.util.WebUtils;

/**
 * Report Template JSF Backing bean
 *
 */
@Named("maintainReportTemplate")
@ViewScoped
public class MaintainReportTemplateBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(MaintainReportTemplateBean.class);

	private ChartModel chartModel;
	
	private TreeNode templateTreeRoot;
	private TreeNode selectedNode;
	
	private ReportTemplate reportTemplate;

	private List<LiteReportTemplate> refReportTemplateList = new ArrayList<LiteReportTemplate>();
	private List<LiteReportTemplate> filteredReportTemplateList = new ArrayList<LiteReportTemplate>();
	
	@Inject
	private ReportGenService reportGenService;

	@Inject
	private ReportTemplateService reportTemplateService;

	@PostConstruct
	public void init() {
		loadReportTemplates();
	}

	public void createNewReportTemplate() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("title", "New Report Template");
		openNewReportTemplateDialog(params);
	}


	public String loadReportTemplates() {
		initReportTemplateList();
		return "/views/template/manage_report_templates.xhtml?faces-redirect=true";
	}

	private void initReportTemplateList() {
		try {
			//1. clear existing list
			refReportTemplateList.clear();
			filteredReportTemplateList.clear();
			
			//2. populate with retrieved list
			refReportTemplateList.addAll(reportTemplateService.findAll());
			
			//3. apply filter
			filteredReportTemplateList.addAll(applyFilter(refReportTemplateList,null));
			
			//4. create the root node
			templateTreeRoot = new DefaultTreeNode("Root",null);
			
			//5. default 1st node as select if applicable
			boolean nodeSelected = false;
			for(LiteReportTemplate template : filteredReportTemplateList){
				TreeNode node = new DefaultTreeNode(template, templateTreeRoot);
				
				if(!nodeSelected){
					node.setSelected(true);
					nodeSelected = true;
				}
			}
			
			//5. load 1st node details if applicable
			if(!filteredReportTemplateList.isEmpty()){
				
				updateReportTemplateDetails(filteredReportTemplateList.get(0).getId());				
				
			}
		} 
		catch (ReportTemplateServiceException rtse) {
			LOG.error("failed to find all report template",rtse);
		}
		catch (ReportGenServiceException rgse){
			LOG.error("failed to generate report for default template",rgse);
		}
	}

	public void editReportTemplate() {
//		if (currentDisplayTemplate instanceof PieChartTemplate) {
//			pieChart = true;
//			loadPieChartTemplate((PieChartTemplate) currentDisplayTemplate);
//		} else if (currentDisplayTemplate instanceof CartesianChartTemplate) {
//			pieChart = false;
//			loadCartesianChartTemplate((CartesianChartTemplate) currentDisplayTemplate);
//		}
//		edit = true;
//		saveOrUpdate = "Update";
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("title", "Edit Report Template");
//		openNewReportTemplateDialog(params);
	}

	public void refresh() {
		initReportTemplateList();
	}

	private void openNewReportTemplateDialog(Map<String, String> params) {
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", true);
		options.put("contentWidth", 1000);
		options.put("contentHeight", 550);
		Map<String, List<String>> requestParams = new HashMap<String, List<String>>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			List<String> values = new ArrayList<String>();
			values.add(entry.getValue());
			requestParams.put(entry.getKey(), values);
		}
		RequestContext.getCurrentInstance().openDialog("report_template_wizard", options, requestParams);
	}

	/**
	 * 
	 */
	public void deleteReportTemplate() {

		try {
			reportTemplateService.delete(reportTemplate);
			
			refresh();
		} 
		catch (ReportTemplateServiceException e) {
			LOG.error("failed to delete report template",e);
		}
	}

	public void filterExtReportTemplates(AjaxBehaviorEvent event) {
		if (event.getSource() instanceof HtmlInputText) {
			
			//1. obtain filter keywords
			String filterKeywords = ((HtmlInputText) event.getSource()).getValue().toString();
			
			//2. clear filtered list
			filteredReportTemplateList.clear();
			
			filteredReportTemplateList.addAll(applyFilter(refReportTemplateList, filterKeywords));
			
			templateTreeRoot = new DefaultTreeNode("Root",null);
			
			for(LiteReportTemplate template : filteredReportTemplateList){
				 new DefaultTreeNode(template, templateTreeRoot);
			}
		}
	}

	public void onDialogReturn(SelectEvent event) {
		refresh();
	}
	
	/**
	 * 
	 * @return
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

	public ChartModel getChartModel() {
		return chartModel;
	}

	public void setChartModel(ChartModel chartModel) {
		this.chartModel = chartModel;
	}

	/**
	 * @return the refReportTemplateList
	 */
	public List<LiteReportTemplate> getRefReportTemplateList() {
		return refReportTemplateList;
	}

	/**
	 * @param refReportTemplateList the refReportTemplateList to set
	 */
	public void setRefReportTemplateList(List<LiteReportTemplate> refReportTemplateList) {
		this.refReportTemplateList = refReportTemplateList;
	}

	/**
	 * @return the filteredReportTemplateList
	 */
	public List<LiteReportTemplate> getFilteredReportTemplateList() {
		return filteredReportTemplateList;
	}

	/**
	 * @param filteredReportTemplateList the filteredReportTemplateList to set
	 */
	public void setFilteredReportTemplateList(List<LiteReportTemplate> filteredReportTemplateList) {
		this.filteredReportTemplateList = filteredReportTemplateList;
	}

	/**
	 * 
	 * @param refList
	 * @param filterKeywords
	 * @return
	 */
	private List<LiteReportTemplate> applyFilter(List<LiteReportTemplate> refList, String filterKeywords){
		List<LiteReportTemplate> filteredList = new ArrayList<LiteReportTemplate> ();
		
		for(LiteReportTemplate ref: refList){

			if(ref!=null){
				
				if(StringUtils.isNotBlank(filterKeywords) && StringUtils.isNotBlank(ref.getName())){
					if(ref.getName().toUpperCase().contains(filterKeywords.toUpperCase())){
						filteredList.add(ref);
					}
				}
				else{
					filteredList.add(ref);
				}
			}
		}
		
		return filteredList;
	}

	/**
	 * @return the templateTreeRoot
	 */
	public TreeNode getTemplateTreeRoot() {
		return templateTreeRoot;
	}

	/**
	 * @param templateTreeRoot the templateTreeRoot to set
	 */
	public void setTemplateTreeRoot(TreeNode templateTreeRoot) {
		this.templateTreeRoot = templateTreeRoot;
	}

	/**
	 * @return the selectedNode
	 */
	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	/**
	 * @param selectedNode the selectedNode to set
	 */
	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}
	
	/**
	 * handle select node event on tree's node
	 * @param event
	 */
	public void onNodeSelect(NodeSelectEvent event){

		LiteReportTemplate selectedTemplate = (LiteReportTemplate)event.getTreeNode().getData();
		
		try {
			updateReportTemplateDetails(selectedTemplate.getId());
		} 
		catch (ReportTemplateServiceException e) {
			WebUtils.addErrorMessage("An error encountered retrieving details");
		} catch (ReportGenServiceException e) {
			WebUtils.addErrorMessage("An error encountered generating preview details");
		}
	}

	/**
	 * @return the reportTemplate
	 */
	public ReportTemplate getReportTemplate() {
		return reportTemplate;
	}

	/**
	 * @param reportTemplate the reportTemplate to set
	 */
	public void setReportTemplate(ReportTemplate reportTemplate) {
		this.reportTemplate = reportTemplate;
	}
	
	/**
	 * 
	 * @param reportTemplateId
	 * @throws ReportTemplateServiceException
	 * @throws ReportGenServiceException
	 */
	private void updateReportTemplateDetails(int reportTemplateId) 
			throws ReportTemplateServiceException, ReportGenServiceException{
		reportTemplate = reportTemplateService.find(reportTemplateId);

		//generate preview
		generatePreview(reportTemplate);
	}
	
	/**
	 * 
	 * @param reportTemplate
	 * @throws ReportGenServiceException
	 */
	private void generatePreview(ReportTemplate reportTemplate) throws ReportGenServiceException{
		
		chartModel = null;
		
		if(reportTemplate!=null){
			Report report = reportGenService.generateReportByTemplateId(reportTemplate.getId());
			
			if(report!=null){
				String type = report.getType();
				
				ChartTypeEnum refChartType = ChartTypeEnum.fromName(type);
				
				if(refChartType!=null){
					switch(refChartType){
						case AREA:
							chartModel = ReportUtil.mapAreaReportToPFModel(report.getCartesianChartReport());
							break;
						case BAR:
							chartModel = ReportUtil.mapBarReportToPFModel(report.getCartesianChartReport());
							break;
						case COLUMN:
							chartModel = ReportUtil.mapColumnReportToPFModel(report.getCartesianChartReport());
							break;
						case LINE:
							chartModel = ReportUtil.mapLineReportToPFModel(report.getCartesianChartReport());
							break;
						case PIE:
							chartModel = ReportUtil.mapPieReportToPFModel(report.getPieChartReport());
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
	}
}
