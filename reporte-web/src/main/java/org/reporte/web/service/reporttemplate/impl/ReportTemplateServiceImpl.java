package org.reporte.web.service.reporttemplate.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.reporte.web.dto.reporttemplate.LiteReportTemplate;
import org.reporte.web.dto.reporttemplate.LiteReportTemplates;
import org.reporte.web.dto.reporttemplate.ReportTemplate;
import org.reporte.web.service.RestServiceBaseImpl;
import org.reporte.web.service.reporttemplate.ReportTemplateService;
import org.reporte.web.service.reporttemplate.exception.ReportTemplateServiceException;

@Named
public class ReportTemplateServiceImpl extends RestServiceBaseImpl implements ReportTemplateService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//TODO: replace with 
	private String restServerUrl= "http://localhost:8080/report-rest/api/reportconnectors";
	
	//TODO: thread safe?
	private WebTarget webTarget;
	
	public ReportTemplateServiceImpl(){
		webTarget = ClientBuilder.newClient().target(getRestServerUrl());
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
		ReportTemplate resultTemplate = null;
		try{
			Response response = webTarget.path("/{templateId}")
									 	 //bind templateId to {templateId}
							 			.resolveTemplate("templateId", id)
							 			//accept application/json
							 			.request(MediaType.APPLICATION_JSON_TYPE)
							 			//GET method 
							 			.get();
			
			resultTemplate = readGoodResponseEntity(response, ReportTemplate.class);
		}
		catch(Exception e){
			throw new ReportTemplateServiceException("exception in finding Report Template",e);
		}
		return resultTemplate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReportTemplate save(ReportTemplate reportTemplate) throws ReportTemplateServiceException {
		ReportTemplate resultTemplate = null;
		try{
			//create a request body from model object as entity of type application/json
			final Entity<ReportTemplate> entity = Entity.entity(reportTemplate, MediaType.APPLICATION_JSON_TYPE);
			
			//accept application/json
			Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
		 								 //POST method with request body
	 									 .post(entity);

			
			resultTemplate = readGoodResponseEntity(response, ReportTemplate.class);
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
			//create a request body from model object as entity of type application/json
			final Entity<ReportTemplate> entity = Entity.entity(reportTemplate, MediaType.APPLICATION_JSON_TYPE);
			
			//accept application/json
			Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
		 								 //PUT method with request body
	 									 .put(entity);

			
			resultTemplate = readGoodResponseEntity(response, ReportTemplate.class);
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
	 * @return the restServerUrl
	 */
	public String getRestServerUrl() {
		return restServerUrl;
	}

	/**
	 * @param restServerUrl the restServerUrl to set
	 */
	public void setRestServerUrl(String restServerUrl) {
		this.restServerUrl = restServerUrl;
	}
}