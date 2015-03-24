package org.reporte.web.service.reporttemplate.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.reporte.web.common.ChartTypeEnum;
import org.reporte.web.dto.model.Model;
import org.reporte.web.dto.reporttemplate.BaseReportTemplate;
import org.reporte.web.dto.reporttemplate.BaseReportTemplate.ReportTemplateTypeEnum;
import org.reporte.web.dto.reporttemplate.CartesianChartReportTemplate;
import org.reporte.web.dto.reporttemplate.ChartReportTemplate;
import org.reporte.web.dto.reporttemplate.LiteReportTemplate;
import org.reporte.web.dto.reporttemplate.LiteReportTemplates;
import org.reporte.web.dto.reporttemplate.PieChartReportTemplate;
import org.reporte.web.dto.reporttemplate.ReportTemplate;
import org.reporte.web.dto.reporttemplate.RestReportTemplate;
import org.reporte.web.service.RestServiceBaseImpl;
import org.reporte.web.service.model.ModelService;
import org.reporte.web.service.model.exception.ModelServiceException;
import org.reporte.web.service.reporttemplate.ReportTemplateService;
import org.reporte.web.service.reporttemplate.exception.ReportTemplateServiceException;

@Named
public class ReportTemplateServiceImpl extends RestServiceBaseImpl implements ReportTemplateService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String API_PREFIX= "/api/reportconnectors";
	
	@Inject
	private ModelService modelService;

	//TODO: thread safe?
	private WebTarget webTarget;
	
	public ReportTemplateServiceImpl(){
		webTarget = ClientBuilder.newClient().target(getTargetRestServerBaseUri()+API_PREFIX);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<LiteReportTemplate> findAll() throws ReportTemplateServiceException {
		
		List<LiteReportTemplate> reportTemplates = new ArrayList<LiteReportTemplate>();

		try{
			//due to the underly REST client implementation of J2EE could be simple client, obtain the Response for close explicitly 
			Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
										 .get();
			
			LiteReportTemplates templates = readGoodResponseEntity(response, LiteReportTemplates.class);
			
			if(templates !=null && templates.getConnectors() !=null){
				reportTemplates.addAll(templates.getConnectors());
			}
		}
		catch(Exception e){
			throw new ReportTemplateServiceException("Failed find all report template(s) ",e);
		}
		
		return reportTemplates;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReportTemplate find(int id) throws ReportTemplateServiceException {
		ReportTemplate template = null;
		try{
			Response response = webTarget.path("/{templateId}")
									 	 //bind templateId to {templateId}
							 			.resolveTemplate("templateId", id)
							 			//accept application/json
							 			.request(MediaType.APPLICATION_JSON_TYPE)
							 			//GET method 
							 			.get();
			
			RestReportTemplate resultTemplate = readGoodResponseEntity(response, RestReportTemplate.class);
			
			//convert from REST to UI POJO
			template = mapRestToUIReportTemplate(resultTemplate);
		}
		catch(Exception e){
			throw new ReportTemplateServiceException("exception in finding Report Template",e);
		}
		return template;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReportTemplate save(ReportTemplate reportTemplate) throws ReportTemplateServiceException {
		ReportTemplate resultTemplate = null;
		try{
			//convert from UI to REST POJO
			RestReportTemplate template = mapUIToRestReportTemplate(reportTemplate);
			//create a request body from model object as entity of type application/json
			final Entity<RestReportTemplate> entity = Entity.entity(template, MediaType.APPLICATION_JSON_TYPE);
			
			//accept application/json
			Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
		 								 //POST method with request body
	 									 .post(entity);

			
			template = readGoodResponseEntity(response, RestReportTemplate.class);
			
			//convert from REST to UI POJO
			resultTemplate = mapRestToUIReportTemplate(template);
		}
		catch(Exception e){
			throw new ReportTemplateServiceException("exception in creating Report Template",e);
		}
		return resultTemplate;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReportTemplate update(ReportTemplate reportTemplate) throws ReportTemplateServiceException {
		ReportTemplate resultTemplate = null;
		try{
			//convert from UI to REST POJO
			RestReportTemplate template = mapUIToRestReportTemplate(reportTemplate);
			
			//create a request body from model object as entity of type application/json
			final Entity<RestReportTemplate> entity = Entity.entity(template, MediaType.APPLICATION_JSON_TYPE);
			
			//accept application/json
			Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
		 								 //PUT method with request body
	 									 .put(entity);

			
			template = readGoodResponseEntity(response, RestReportTemplate.class);
			
			//convert from REST to UI POJO
			resultTemplate = mapRestToUIReportTemplate(template);
		}
		catch(Exception e){
			throw new ReportTemplateServiceException("exception in updating Report Template",e);
		}
		return resultTemplate;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean delete(ReportTemplate reportTemplate) throws ReportTemplateServiceException {
		boolean deleted = false;

		try{
			Response response = webTarget.path("/{reportConnectorId}")
										 //bind reportTemplate.getId() to {reportConnectorId}
										 .resolveTemplate("reportConnectorId", reportTemplate.getId())
										 //accept application/json
										 .request(MediaType.APPLICATION_JSON_TYPE)
										 //DELETE method
										 .delete();
			
			if(Response.Status.OK.equals(response.getStatus())){
				deleted = true;
			}
			
			response.close();
		}
		catch(Exception e){
			throw new ReportTemplateServiceException("exception in deleting report template",e);
		}
		
		return deleted;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ReportTemplate mapRestToUIReportTemplate(RestReportTemplate restTemplate) 
			throws ReportTemplateServiceException{
		ReportTemplate template = null;

		if(restTemplate!=null){
			ChartTypeEnum type = restTemplate.getType();
			
			if(type!=null){
				
				template = new ReportTemplate();
				template.setChartType(type);

				switch(type){
					case AREA:
					case BAR:
					case COLUMN:
					case LINE:
						mapRestToUICartesianTemplate(restTemplate.getCartesianChartTemplate(), template);
						updateUITemplateModel(template, restTemplate.getCartesianChartTemplate().getModelId());
						break;
					case PIE:
						mapRestToUIPieTemplate(restTemplate.getPieChartTemplate(), template);
						updateUITemplateModel(template, restTemplate.getPieChartTemplate().getModelId());
						break;
					default:
						//TODO: error, unrecognized type
						break;
				}
			}
			else{
				throw new ReportTemplateServiceException("unable to map as template type is null");
			}
		}
		
		return template;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public RestReportTemplate mapUIToRestReportTemplate(ReportTemplate template) 
			throws ReportTemplateServiceException{
		RestReportTemplate restTemplate = null;
		
		if(template!=null){
			ChartTypeEnum type = template.getChartType();
			
			if(type!=null && template.getModel()!=null){
				restTemplate = new RestReportTemplate();
				restTemplate.setType(type);
				
				switch(type){
					case AREA:
					case BAR:
					case COLUMN:
					case LINE:
						CartesianChartReportTemplate cartesianTemplate = new CartesianChartReportTemplate();
						restTemplate.setCartesianChartTemplate(cartesianTemplate);
						mapUIToRestCartesianTemplate(template, cartesianTemplate);
						break;
					case PIE:
						PieChartReportTemplate pieTemplate = new PieChartReportTemplate();
						restTemplate.setPieChartTemplate(pieTemplate);
						mapUIToRestPieTemplate(template, pieTemplate);
						break;
					default:
						//TODO: error, unrecognized type
						break;
				}
			}
			else{
				throw new ReportTemplateServiceException("unable to map as invalid template type ["+type+"] or "+ 
														 "model ["+template.getModelCategoryField()+"]");
			}
		}
		
		return restTemplate;
	}
	
	/***************** private method **************/ 
	 
	/*******************************************
	 *      REST To UI
	 *******************************************/
	/**
	 * 
	 * @param source
	 * @param target
	 */
	private void mapRestToUICartesianTemplate(CartesianChartReportTemplate source, ReportTemplate target){
		//chart type attributes mapping
		mapRestToUIChartTemplate(source, target);
		
		//cartesian chart type attributes mapping
		target.setxAxisTitle(source.getXaxisTitle());
		target.setyAxisTitle(source.getYaxisTitle());
		target.setShowXAxis(source.isShowXAxis());;
		target.setShowYAxis(source.isShowYAxis());
		target.setModelDataLabelField(source.getModelDataLabelField());
		target.setModelDataValueField(source.getModelDataValueField());
		target.setModelSeriesGroupField(source.getModelSeriesGroupField());
		
		target.getTemplateSeries().addAll(source.getDataSeries());
	}
	
	/**
	 * 
	 * @param source
	 * @param target
	 */
	private void mapRestToUIPieTemplate(PieChartReportTemplate source, ReportTemplate target){
		//chart type attributes mapping
		mapRestToUIChartTemplate(source, target);
		
		//pie chart type attributes mapping
		target.setCategoryName(source.getCategoryName());
		target.setModelCategoryField(source.getModelCategoryField());
		target.setModelDataField(source.getModelDataField());
		target.setShowDataLabel(source.isShowDataLabel());
		target.setDataTypeFormat(source.getDataTypeFormat());
	}
	
	/**
	 * 
	 * @param source
	 * @param target
	 */
	private void mapRestToUIChartTemplate(ChartReportTemplate source, ReportTemplate target){
		//base attributes mapping
		mapRestToUIBaseTemplate(source, target);
		
		//chart attribute mapping
		target.setChartTitle(source.getTitle());
		target.setShowLegend(source.isShowLegend());
		
		//chartType is handled externally
	}
	
	/**
	 * 
	 * @param source
	 * @param target
	 */
	private void mapRestToUIBaseTemplate(BaseReportTemplate source, ReportTemplate target){
		target.setId(source.getId());
		target.setName(source.getTemplateName());
		target.setReportDisplayName(source.getReportDisplayName());
		
		//Model retrieval will be handled separately
		
		//ReportQuery is not exposed to ui, hence not mapped.
	}
	
	/**
	 * 
	 * @param target
	 * @param modelId
	 * @throws ReportTemplateServiceException
	 */
	private void updateUITemplateModel(ReportTemplate target, int modelId) throws ReportTemplateServiceException {
		
		Model model = null;
		 
		try {
			model = modelService.find(modelId);
			
			if(model==null){
				throw new ReportTemplateServiceException("Failed to find model for id "+modelId);
			}
			
			target.setModel(model);

		} catch (ModelServiceException e) {
			throw new ReportTemplateServiceException(e);
		}
	}
	
	/*******************************************
	 *      UI To REST
	 *******************************************/
	
	/**
	 * 
	 * @param source
	 * @param target
	 */
	private void mapUIToRestCartesianTemplate(ReportTemplate source, CartesianChartReportTemplate target){
		//chart type attributes mapping
		mapUIToRestChartTemplate(source, target);
		target.setReportTemplateType(ReportTemplateTypeEnum.SIMPLE);
		
		//cartesian chart type attributes mapping
		target.setXaxisTitle(source.getxAxisTitle());
		target.setYaxisTitle(source.getyAxisTitle());
		target.setShowXAxis(source.isShowXAxis());;
		target.setShowYAxis(source.isShowYAxis());
		target.setModelDataLabelField(source.getModelDataLabelField());
		target.setModelDataValueField(source.getModelDataValueField());
		target.setModelSeriesGroupField(source.getModelSeriesGroupField());
		
		target.getDataSeries().addAll(source.getTemplateSeries());
	}
	
	/**
	 * 
	 * @param source
	 * @param target
	 */
	private void mapUIToRestPieTemplate(ReportTemplate source, PieChartReportTemplate target){
		
		//chart type attributes mapping
		mapUIToRestChartTemplate(source, target);
		target.setReportTemplateType(ReportTemplateTypeEnum.SIMPLE);
		
		//pie chart type attributes mapping
		target.setCategoryName(source.getCategoryName());
		target.setModelCategoryField(source.getModelCategoryField());
		target.setModelDataField(source.getModelDataField());
		target.setShowDataLabel(source.isShowDataLabel());
		target.setDataTypeFormat(source.getDataTypeFormat());
	}
	
	/**
	 * 
	 * @param source
	 * @param target
	 */
	private void mapUIToRestChartTemplate(ReportTemplate source, ChartReportTemplate target){
		//base attributes mapping
		mapUIToRestBaseTemplate(source, target);
		
		//chart attribute mapping
		target.setTitle(source.getChartTitle());
		target.setShowLegend(source.isShowLegend());
		
		//chartType is handled externally
	}
	
	/**
	 * 
	 * @param source
	 * @param target
	 */
	private void mapUIToRestBaseTemplate(ReportTemplate source, BaseReportTemplate target){
		target.setId(source.getId());
		target.setTemplateName(source.getName());
		target.setReportDisplayName(source.getReportDisplayName());

		target.setModelId(source.getModel().getId());
		
		//ReportQuery is not exposed to ui, hence not mapped.
	}
}