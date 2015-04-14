package org.reporte.web.bean.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.ReorderEvent;
import org.reporte.web.dto.datasource.DataSource;
import org.reporte.web.dto.model.AttributeMapping;
import org.reporte.web.dto.model.Model;
import org.reporte.web.dto.model.Model.Approach;
import org.reporte.web.dto.model.ModelPreviewResult;
import org.reporte.web.dto.model.ModelQuery;
import org.reporte.web.service.datasource.DataSourceService;
import org.reporte.web.service.datasource.exception.DataSourceServiceException;
import org.reporte.web.service.model.ModelService;
import org.reporte.web.service.model.exception.ModelServiceException;
import org.reporte.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model Wizard JSF backing bean 
 *
 */
@Named("modelWizard")
@ViewScoped
public class ModelWizardBean implements Serializable {
	private static final long serialVersionUID = 4607556957265028946L;
	private static final Logger LOG = LoggerFactory.getLogger(ModelWizardBean.class);

	private static final int DEFAULT_QUERY_ROW_LIMIT = 5;
	private static final int MAX_SAMPLE_ROW = 20;

	private List<DataSource> datasources;
	private List<String> tableNames;
	private List<AttributeMapping> columnNames;
	private Model model;
	private boolean showSingleTablePanel = true;
	private boolean showJoinQueryPanel;
	private boolean disabledResultTab = true;
	private boolean disabledModelTab = true;
	private Set<String> columns;
	private List<Map<String, String>> resultSet;
	private List<Map<String, String>> originalResultSet;
	private int activeIndex;
	private int limit = DEFAULT_QUERY_ROW_LIMIT;
	private int noOfRecordMatched;
	private String approach;
	private boolean requiredDatasource = true;
	private boolean requiredTargetTbl = true;
	private boolean disabledNextNav = false;

	// Parameters passed via Dialog Framework
	private int modelId;
	private String title;

	@Inject
	private ModelService modelService;
	
	@Inject 
	private DataSourceService dataSourceService;

	@PostConstruct
	public void init() {
		// Retrieve the params passed via Dialog Framework.
		Map<String, String> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		title = requestParams.get("title");
		modelId = requestParams.containsKey("id") ? Integer.valueOf(requestParams.get("id")) : 0;

		if (modelId != 0) {
			try {
				model = modelService.find(modelId);
				
				tableNames = dataSourceService.findDataSourceTableName(model.getDatasource());
			} 
			catch (ModelServiceException e) {
				LOG.error("Error loading model with given Id[{}].",modelId,e);
			}
			catch(DataSourceServiceException e){
				LOG.error("Error loading datasource table name with given model id [{}].",modelId,e);
			}
		} else {
			initNewModel();
		}

		try {
			datasources = dataSourceService.findAll();
		} catch (DataSourceServiceException e) {
			LOG.error("Failed to load existing datasources.", e);
		}
	}

	// ////////////////////////////////////////////////
	// GETTER & SETTER METHODS //
	// ////////////////////////////////////////////////

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public List<DataSource> getDatasources() {
		return datasources;
	}

	public void setDatasources(List<DataSource> datasources) {
		this.datasources = datasources;
	}

	public List<String> getTableNames() {
		return tableNames;
	}

	public void setTableNames(List<String> tableNames) {
		this.tableNames = tableNames;
	}

	public boolean isShowSingleTablePanel() {
		return showSingleTablePanel;
	}

	public void setShowSingleTablePanel(boolean showSingleTablePanel) {
		this.showSingleTablePanel = showSingleTablePanel;
	}

	public boolean isShowJoinQueryPanel() {
		return showJoinQueryPanel;
	}

	public void setShowJoinQueryPanel(boolean showJoinQueryPanel) {
		this.showJoinQueryPanel = showJoinQueryPanel;
	}

	public String getApproach() {
		return approach;
	}

	public void setApproach(String approach) {
		this.approach = approach;
	}

	public List<AttributeMapping> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<AttributeMapping> columnNames) {
		this.columnNames = columnNames;
	}

	public boolean isDisabledResultTab() {
		return disabledResultTab;
	}

	public void setDisabledResultTab(boolean disabledResultTab) {
		this.disabledResultTab = disabledResultTab;
	}

	public boolean isDisabledModelTab() {
		return disabledModelTab;
	}

	public void setDisabledModelTab(boolean disabledModelTab) {
		this.disabledModelTab = disabledModelTab;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public List<Map<String, String>> getResultSet() {
		return resultSet;
	}

	public void setResultSet(List<Map<String, String>> resultSet) {
		this.resultSet = resultSet;
	}

	public Set<String> getColumns() {
		return columns;
	}

	public void setColumns(Set<String> columns) {
		this.columns = columns;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public boolean isRequiredDatasource() {
		return requiredDatasource;
	}

	public void setRequiredDatasource(boolean requiredDatasource) {
		this.requiredDatasource = requiredDatasource;
	}

	public boolean isRequiredTargetTbl() {
		return requiredTargetTbl;
	}

	public void setRequiredTargetTbl(boolean requiredTargetTbl) {
		this.requiredTargetTbl = requiredTargetTbl;
	}

	public int getNoOfRecordMatched() {
		return noOfRecordMatched;
	}

	public void setRecordMatched(int noOfRecordMatched) {
		this.noOfRecordMatched = noOfRecordMatched;
	}

	public boolean isDisabledNextNav() {
		return disabledNextNav;
	}

	public void setDisabledNextNav(boolean disabledNextNav) {
		this.disabledNextNav = disabledNextNav;
	}

	public int getModelId() {
		return modelId;
	}

	public void setModelId(int modelId) {
		this.modelId = modelId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	// ////////////////////////////////////////////////
	// PRIVATE METHODS //
	// ////////////////////////////////////////////////

	/**
	 * 
	 * @param resultSet
	 * @param limit
	 * @return
	 */
	private List<Map<String, String>> applyLimit(List<Map<String, String>> resultSet, int limit) {
		List<Map<String, String>> rs = new ArrayList<Map<String, String>>();
		for (int i = 0; i < resultSet.size(); i++) {
			if (i == limit) {
				break;
			}
			rs.add(resultSet.get(i));
		}
		return rs;
	}

	// ////////////////////////////////////////////////
	// ACTION LISTENER METHODS //
	// ////////////////////////////////////////////////

	public void onApproachChange() {
		if (Model.Approach.SINGLE_TABLE.getDescription().equals(approach)) {
			showSingleTablePanel = true;
			showJoinQueryPanel = false;
			disabledNextNav = false;
			model.setTable(null);
			model.setApproach(Approach.SINGLE_TABLE);
		} else if (Model.Approach.JOIN_QUERY.getDescription().equals(approach)) {
			showJoinQueryPanel = true;
			showSingleTablePanel = false;
			disabledNextNav = true;
			activeIndex = 0;
			disabledResultTab = true;
			disabledModelTab = true;
			if (CollectionUtils.isNotEmpty(resultSet)) {
				resultSet.clear();
			}
			model.setApproach(Approach.JOIN_QUERY);
		}
		model.getQuery().setValue(null);
		if (CollectionUtils.isNotEmpty(columnNames)) {
			columnNames.clear();
		}

		model.getAttributeBindings().clear();
	}

	public void onDatasourceSltOneMenuChange() {
		DataSource selectedDatasource = model.getDatasource();
		if (selectedDatasource != null) {
			try {
				tableNames = dataSourceService.findDataSourceTableName(selectedDatasource);
			} catch (DataSourceServiceException e) {
				LOG.error("Failed to load available table names for selected Datasource with name[{}].",selectedDatasource.getName(), e);
				tableNames = new ArrayList<String>();
			}
			setRequiredDatasource(false);
		} else {
			tableNames = new ArrayList<String>();
			model.setDatasource(null);
			setRequiredDatasource(true);
		}
	}

	public void onLimitSltOneMenuChange() {
		resultSet = applyLimit(originalResultSet, limit);
	}

	public void onTableSltOneMenuChange() {
		
		String tableName = model.getTable();

		if (StringUtils.isNotEmpty(tableName)) {
			
			try {
				model = modelService.deriveModelQueryAttributes(model);
				columnNames = model.getAttributeBindings();
			} catch (ModelServiceException e) {
				LOG.error("Failed to load metadata columns for the table - {}.",tableName);
				columnNames = new ArrayList<AttributeMapping>();
			}

			setRequiredTargetTbl(false);
		} else {
			columnNames = new ArrayList<AttributeMapping>();
			model.setAttributeBindings(columnNames);
			model.setTable(null);
			setRequiredTargetTbl(true);
		}
	}

	public void onChangeTxtAreaQuery() {
		disabledResultTab = true;
		disabledModelTab = true;
		activeIndex = 0;
		disabledNextNav = true;
	}

	public void onReorder(ReorderEvent event) {
		// During this event, the order on the UI reflect here. 
		// So, we better reset the order here.
		int i = 1;
		for (AttributeMapping mapping : model.getAttributeBindings()) {
			mapping.setOrder(i++);
		}
	}

	public String onFlowProcess(FlowEvent event) {
		model.getAttributeBindings();
		if (event.getNewStep().equals("confirmation")) {
			// Rearrange the mappings for display during summary according to
			// the order as they were defined by user.
			Collections.sort(model.getAttributeBindings(), new Comparator<AttributeMapping>() {

				@Override
				public int compare(AttributeMapping o1, AttributeMapping o2) {
					return o1.getOrder() - o2.getOrder();
				}
			});
		}
		return event.getNewStep();
	}

	// ////////////////////////////////////////////////
	// 				ACTION METHODS 					 //
	// ////////////////////////////////////////////////

	public void verify() {
		model.getAttributeBindings().clear();
		columnNames = new ArrayList<AttributeMapping>();
		columns = new HashSet<String>();

		try {
			
			Model resultModel = modelService.deriveModelQueryAttributes(model);

			if(resultModel==null){
				WebUtils.addErrorMessage("Error verifying query");
			}
			model = resultModel;
			columnNames = model.getAttributeBindings();
			
			generatePreview(model);
			
			// Enable and move to 'Result' tab.
			disabledResultTab = false;
			activeIndex = 1;

			WebUtils.addInfoMessage("Query verification was successful.");
		} 
		catch(ModelServiceException mse){
			LOG.error("Query verification failed. Parsed query = [{}].",model.getQuery().getJoinQuery(), mse);
			WebUtils.addErrorMessage(mse.getCause().getMessage());
		}
	}

	/**
	 * 
	 * @param model
	 */
	private void generatePreview(Model model){
		try{
			//1. generate preview for model
			ModelPreviewResult previewResult = modelService.generateModelPreview(model, MAX_SAMPLE_ROW);
		
			//2. obtain the column name list based on model query result
			List<String> columnNameList = previewResult.getColumnNameList();
			
			columns.addAll(columnNameList);
			
			//3. no. of match record(s)
			noOfRecordMatched = previewResult.getMatchRecordCount();
			
			//4. preview result
			originalResultSet = previewResult.getResultRowList();
			
			//5. apply display limit filter selected 
			resultSet = applyLimit(originalResultSet, limit);
		}
		catch(ModelServiceException mse){
			LOG.error("Query verification failed. Parsed query = [{}].",model.getQuery().getJoinQuery(), mse);
			WebUtils.addErrorMessage(mse.getCause().getMessage());
		}
	}
	
	public void proceedToModel() {
		disabledModelTab = false;
		activeIndex = 2;
		disabledNextNav = false;
	}

	/**
	 * 
	 */
	public void finish() {
		String action = null;
		String status = null;
		String datasourceName = null;
		String modelName = null;
		try {
			datasourceName = model.getDatasource().getName();
			modelName = model.getName();
			if (modelId == 0) {
				action = "create";
				
				modelService.save(model);
			} else {
				action = "update";
				modelService.update(model);
			}
			status = "success";
			//successfully save, clear the backing bean's model info for next entry
			initNewModel();
		} catch (ModelServiceException e) {
			LOG.error("Failed to {} Model.",action, e);
			status = "failed";
		}
		//in even of error must close the dialog too
		finally{
			// Prepare data to pass it back to whatever that opened this dialog.
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("action", action);
			data.put("modelName", modelName);
			data.put("datasourceName", datasourceName);
			data.put("status", status);
			
			RequestContext.getCurrentInstance().closeDialog(data);
		}
	}

	public void cancel() {
		RequestContext.getCurrentInstance().closeDialog(null);
	}

	/**
	 * 
	 */
	private void initNewModel(){
		model = new Model();
		model.setQuery(new ModelQuery());
		model.setAttributeBindings(new ArrayList<AttributeMapping>());
		model.setApproach(Approach.SINGLE_TABLE);
		approach = model.getApproach().getDescription();
		onApproachChange();
	}	
}
