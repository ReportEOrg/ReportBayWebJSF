package org.reportbay.web.service.model.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.reportbay.web.dto.model.Model;
import org.reportbay.web.dto.model.ModelPreviewResult;
import org.reportbay.web.dto.model.Models;
import org.reportbay.web.service.RestServiceBaseImpl;
import org.reportbay.web.service.model.ModelService;
import org.reportbay.web.service.model.exception.ModelServiceException;

@Named
public class ModelServiceImpl extends RestServiceBaseImpl implements ModelService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String API_PREFIX= "/api/models";
	
	//TODO: thread safe?
	private WebTarget webTarget;
	
	/**
	 * 
	 */
	public ModelServiceImpl(){
		
		webTarget = ClientBuilder.newClient().target(getTargetRestServerBaseUri()+API_PREFIX);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Model> findAll() throws ModelServiceException {
		return findAllOrderByDatasourceName();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Model> findAllOrderByDatasourceName() throws ModelServiceException {
		
		List<Model> models = new ArrayList<Model>();

		try{
			//due to the underly REST client implementation of J2EE could be simple client, obtain the Response for close explicitly 
			Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
										 .get();

			//only if good response
			Models result = readGoodResponseEntity(response, Models.class);
				
			if(result!=null){
				models.addAll(result.getModels());
			}
			
		}
		catch(Exception e){
			throw new ModelServiceException("Failed find all Model(s) ",e);
		}
		
		return models;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Model save(Model model) throws ModelServiceException {
		Model responseModel = null;
		//create a request body from model object as entity of type application/json
		final Entity<Model> entity = Entity.entity(model, MediaType.APPLICATION_JSON_TYPE);

		try{
			//accept application/json
			Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
									 	 //POST method with request body
									 	 .post(entity);
			
			responseModel = readGoodResponseEntity(response,Model.class);
		}
		catch(Exception e){
			throw new ModelServiceException("exception in saving model",e);
		}
		return responseModel;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Model update(Model model) throws ModelServiceException {
		Model responseModel = null;

		//create a request body from model object as entity of type application/json
		final Entity<Model> entity = Entity.entity(model, MediaType.APPLICATION_JSON_TYPE);

		try{
			//accept application/json
			Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
					                 	 //PUT method with request body
					                 	 .put(entity);
			
			responseModel =readGoodResponseEntity(response,  Model.class);
		}
		catch(Exception e){
			throw new ModelServiceException("exception in updating model",e);
		}
		
		return responseModel;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean delete(Model model) throws ModelServiceException {
		
		boolean deleted = false;

		try{
			Response response = webTarget.path("/{modelId}")
										 //bind model.getId() to {modelId}
										 .resolveTemplate("modelId", model.getId())
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
			throw new ModelServiceException("exception in deleting model",e);
		}
		
		return deleted;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Model find(int id) throws ModelServiceException {
		Model responseModel = null;
		try{
			Response response = webTarget.path("/{modelId}")
									 	 //bind model.getId() to {modelId}
							 			.resolveTemplate("modelId", id)
							 			//accept application/json
							 			.request(MediaType.APPLICATION_JSON_TYPE)
							 			//GET method 
							 			.get();
			
			responseModel = readGoodResponseEntity(response, Model.class);
		}
		catch(Exception e){
			throw new ModelServiceException("exception in finding model",e);
		}
		return responseModel;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Model deriveModelQueryAttributes(Model model) throws ModelServiceException {
		
		Model responseModel = null;
		
		//create a request body from model object as entity of type application/json
		final Entity<Model> entity = Entity.entity(model, MediaType.APPLICATION_JSON_TYPE);
				
		try{
			Response response = webTarget.path("/derivemodelattributes")
							       	     //accept application/json
					 		       	 	 .request(MediaType.APPLICATION_JSON_TYPE)
					 		       	 	 //PUT method with request body
					 		       	 	 .put(entity);

			responseModel = readGoodResponseEntity(response, Model.class);
		}
		catch(Exception e){
			throw new ModelServiceException("Exception in derive Model Query Attributes", e);
		}
		
		return responseModel;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ModelPreviewResult generateModelPreview(Model model, int maxRow) throws ModelServiceException {

		ModelPreviewResult responseResult = null;

		//create a request body from model object as entity of type application/json
		final Entity<Model> entity = Entity.entity(model, MediaType.APPLICATION_JSON_TYPE);
		
		try{
			
			WebTarget previewWebTarget = webTarget.path("/preview");
			
			if(maxRow>0){
				previewWebTarget = previewWebTarget.queryParam("maxRow", maxRow);
			}
							 				 //accept application/json
			Response response = previewWebTarget.request(MediaType.APPLICATION_JSON_TYPE)
							 				 	//PUT method with request body
	 		       	 						 	.post(entity);
			
			responseResult = readGoodResponseEntity(response, ModelPreviewResult.class);
		}
		catch(Exception e){
			throw new ModelServiceException("Exception in generate Model Preview", e);
		}
		return responseResult;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getModelFieldUniqueValue(int modelId, String fieldName)	throws ModelServiceException {
		List<String> uniqueValueResults = new ArrayList<String>();
		
		try{
			
			WebTarget previewWebTarget = webTarget.path("/{modelId}/uniquedatafield") 
										 		  //bind modelId to {modelId}
		 										  .resolveTemplate("modelId", modelId)
		 										  //query string
		 										  .queryParam("dataField", fieldName);
			
			 //accept application/json
			Response response = previewWebTarget.request(MediaType.APPLICATION_JSON_TYPE)
												.get();
			
			ModelPreviewResult responseResult = readGoodResponseEntity(response, ModelPreviewResult.class);
			
			if(responseResult!=null && responseResult.getColumnValueList()!=null){
				uniqueValueResults.addAll(responseResult.getColumnValueList());
			}
			
		}
		catch(Exception e){
			throw new ModelServiceException("Exception in get Model Field Unique Value", e);
		}
		
		return uniqueValueResults;
	}
	
}