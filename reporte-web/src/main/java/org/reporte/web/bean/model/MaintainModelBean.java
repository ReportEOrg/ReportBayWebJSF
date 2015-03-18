package org.reporte.web.bean.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import org.reporte.web.dto.model.Model;
import org.reporte.web.service.model.ModelService;
import org.reporte.web.service.model.exception.ModelServiceException;
import org.reporte.web.util.WebUtils;


/**
 * Maintain Model JSF Backing bean 
 *
 */
@Named("maintainModel")
@ViewScoped
public class MaintainModelBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(MaintainModelBean.class);

	private TreeNode modelTreeRoot;
	private TreeNode selectedNode;
	
	List<Model> refAllModels = new ArrayList<Model>();

	private Model model;

	@Inject
	private ModelService modelService;

	@PostConstruct
	public void init() {

		Model refModel = initTreeNode(null,null);
		
		if(refModel !=null){
			model = refModel;
		}
	}

	public Model getModel() {
		return model;
	}

	/**
	 * initialize the tree node(s) for Left hand side panel representing model(s) under respective datasource(s)
	 */
	private Model initTreeNode(String refDatasourceName, String refModelName){
		
		Model refModel = null;
//		//1. create the root node
//		modelTreeRoot = new DefaultTreeNode("Root",null);
		
		try {
//			//TreeMap is implementing sortedMap 
//			Map<String, TreeNode> dataSourceNodeLookupMap = new TreeMap<String, TreeNode>();
//			String datasourceName;
//			TreeNode datasourceNode;
//			TreeNode modelNode;

			//TODO: source datasource w/o model displayed too?
			//1. obtain all model(s) order by datasource name, model name
			List<Model> allModels = modelService.findAllOrderByDatasourceName();

			//2. sort by datasource name
			Collections.sort(allModels, new ModelComparator());
			
			refAllModels.clear();
			refAllModels.addAll(allModels);
			
			refModel = rebuildTreeNode(refDatasourceName, refModelName, null);
			
//			//3. for each model
//			for (Model model : allModels) {
//				
//				datasourceName = model.getDatasource().getName();
//
//				//3.a obtain datasource node based on datasource name
//				datasourceNode = dataSourceNodeLookupMap.get(datasourceName);
//				
//				//3.b not yet exist create one
//				if(datasourceNode==null){
//					datasourceNode = new DefaultTreeNode(datasourceName,modelTreeRoot);
//					dataSourceNodeLookupMap.put(datasourceName,datasourceNode);
//				}
//				
//				//3.c create and append model node under datasource node
//				modelNode = new DefaultTreeNode("modelNode", model, datasourceNode);
//				datasourceNode.getChildren().add(modelNode);
//				
//				//not yet identify reference
//				if(refModel == null
//				   &&
//				  //i. if not specified used first model found as reference
//				  ((refDatasourceName == null && refModelName ==null) 
//				   || 
//				   //ii. if specified and match, made model as reference
//				   (refDatasourceName!=null && refDatasourceName.equals(datasourceName) &&
//					refModelName!=null && refModelName.equals(model.getName())))
//				  )
//				{
//					refModel = model;
//					
//					datasourceNode.setExpanded(true);
//					modelNode.setSelected(true);
//				}
//			}
		}
		catch (ModelServiceException e) {
			LOG.error("Failed to load all existing Models.", e);
		}
		
		return refModel;
	}
	
	private Model rebuildTreeNode(String refDatasourceName, 
								  String refModelName, 
								  String filterKeywords){
		
		Model refModel = null;
		
		//TreeMap is implementing sortedMap 
		Map<String, TreeNode> dataSourceNodeLookupMap = new TreeMap<String, TreeNode>();
		String datasourceName;
		TreeNode datasourceNode;
		TreeNode modelNode;

		//1. create a new root node
		modelTreeRoot = new DefaultTreeNode("Root",null);
		
		//2. for each model
		for (Model model : refAllModels) {
			
			//if filter keyword present AND no match skip (if filter keyword not present add)
			if(StringUtils.isNotBlank(filterKeywords) && StringUtils.isNotBlank(model.getName()) &&
				!(model.getName().toUpperCase().contains(filterKeywords.toUpperCase()))){
				continue;
			}
			
			datasourceName = model.getDatasource().getName();

			//3.a obtain datasource node based on datasource name
			datasourceNode = dataSourceNodeLookupMap.get(datasourceName);
			
			//3.b not yet exist create one
			if(datasourceNode==null){
				datasourceNode = new DefaultTreeNode(datasourceName,modelTreeRoot);
				dataSourceNodeLookupMap.put(datasourceName,datasourceNode);
			}
			
			//3.c create and append model node under datasource node
			modelNode = new DefaultTreeNode("modelNode", model, datasourceNode);
			datasourceNode.getChildren().add(modelNode);
			
			//not yet identify reference
			if(refModel == null
			   &&
			  //i. if not specified used first model found as reference
			  ((refDatasourceName == null && refModelName ==null) 
			   || 
			   //ii. if specified and match, made model as reference
			   (refDatasourceName!=null && refDatasourceName.equals(datasourceName) &&
				refModelName!=null && refModelName.equals(model.getName())))
			  )
			{
				refModel = model;
				
				datasourceNode.setExpanded(true);
				
				//default first node as selected when filter not applied
				if(StringUtils.isBlank(filterKeywords)){
					modelNode.setSelected(true);
				}
			}
		}
		
		return refModel;
	}
	
	private void openModelWizardDialog(Map<String, String> params) {
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", true);
		options.put("contentWidth", 900);
		options.put("contentHeight", 500);

		Map<String, List<String>> requestParams = new HashMap<String, List<String>>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			List<String> values = new ArrayList<String>();
			values.add(entry.getValue());
			requestParams.put(entry.getKey(), values);
		}
		RequestContext.getCurrentInstance().openDialog("model_wizard", options, requestParams);
	}

	public void addModel() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("title", "New Model");

		openModelWizardDialog(params);
	}

	public void updateModel(String id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("title", "Update Model");
		params.put("id", id);

		openModelWizardDialog(params);
	}

	public void deleteModel() {
		String tableName= model.getName();
		try {
			
			modelService.delete(model);

			//rebuild the tree and try to use 1st model as reference 
			Model refModel = initTreeNode(null, null);
			
			refreshViewModelTableName(refModel);
			
			WebUtils.addInfoMessage("Model '%s' has been successfully deleted.", tableName);
		}
		catch (ModelServiceException e) {
			LOG.error("Failed to delete Model with given name '" + tableName + "'.", e);
			WebUtils.addErrorMessage("An error was encountered while deleting Model with given name '%s'.",
					tableName);
		}
	}

	public void onDialogReturn(SelectEvent event) {
		@SuppressWarnings("unchecked")
		Map<String, String> data = (Map<String, String>) event.getObject();
		if (data != null) {
			String status = (String) data.get("status");
			String action = (String) data.get("action");
			String completedAction = action.equals("create") ? "created" : "updated";
			String completingAction = action.equals("create") ? "creating" : "updating";

			if (status.equals("success")) {
				//obtain the datasource name and model name from dialog
				String modelName = (String) data.get("modelName");
				String datasourceName = (String)data.get("datasourceName");
				WebUtils.addInfoMessage("Model '%s' has been successfully %s.", modelName, completedAction);
				
				//rebuild the tree and try to obtain the model reference of the model worked in dialog 
				Model refModel = initTreeNode(datasourceName, modelName);
				
				refreshViewModelTableName(refModel);
			} else {
				WebUtils.addErrorMessage("An error was encountered while %s Model.", completingAction);
			}
		}
	}
	
	public void filterModels(AjaxBehaviorEvent event) {
		if (event.getSource() instanceof HtmlInputText) {
			
			//1. obtain filter keywords
			String filterKeywords = ((HtmlInputText) event.getSource()).getValue().toString();
			
			rebuildTreeNode(null, null, filterKeywords);
		}
	}
	
	
	/**
	 * handle select node event on tree's node
	 * @param event
	 */
	public void onNodeSelect(NodeSelectEvent event){
		String nodeType = event.getTreeNode().getType();
		
		//handle only if selected is model node
		if("modelNode".equals(nodeType)){
			refreshViewModelTableName((Model)event.getTreeNode().getData());
		}
	}
	
	/**
	 * 
	 * @param refModel
	 */
	private void refreshViewModelTableName(Model refModel){
		
		//if present, make it as active model in view
		if(refModel!=null){
			model = refModel;
		}
	}
	
	public TreeNode getModelTreeRoot() {
		return modelTreeRoot;
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
	 * 
	 * Model comparator
	 *
	 */
	class ModelComparator implements Comparator<Model>{

		@Override
		public int compare(Model o1, Model o2) {
			if(o1 !=null && o2!=null &&
			   o1.getDatasource()!=null && o2.getDatasource()!=null){
				
				String ref1 = o1.getDatasource().getName()==null?"": o1.getDatasource().getName();
				String ref2 = o2.getDatasource().getName()==null?"": o2.getDatasource().getName();
				
				return ref1.compareTo(ref2);
			}
			
			return 0;
		}
	}
}
