package org.reporte.web.service.reportgen.impl;

import javax.inject.Named;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.reporte.web.dto.reportgen.Report;
import org.reporte.web.dto.reporttemplate.RestReportTemplate;
import org.reporte.web.service.RestServiceBaseImpl;
import org.reporte.web.service.reportgen.ReportGenService;
import org.reporte.web.service.reportgen.exception.ReportGenServiceException;

@Named
public class ReportGenServiceImpl extends RestServiceBaseImpl implements ReportGenService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String API_PREFIX= "/api/reportgen";
	
	//TODO: thread safe?
	private WebTarget webTarget;
	
	public ReportGenServiceImpl(){
		
		webTarget = ClientBuilder.newClient().target(getTargetRestServerBaseUri()+API_PREFIX);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Report generateReportByTemplateId(int reportTemplateId) throws ReportGenServiceException{
		Report responseReport = null;
		try{
			Response response = webTarget.path("/{reportConnectorId}")
									 	 //bind reportTemplateId to {reportConnectorId}
							 			.resolveTemplate("reportConnectorId", reportTemplateId)
							 			//accept application/json
							 			.request(MediaType.APPLICATION_JSON_TYPE)
							 			//GET method 
							 			.get();
			
			responseReport = readGoodResponseEntity(response, Report.class);
		}
		catch(Exception e){
			throw new ReportGenServiceException("exception in generating report for template",e);
		}
		return responseReport;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Report generateReportPreview(RestReportTemplate restReportTemplate) throws ReportGenServiceException{
		Report responseReport = null;
		
		//create a request body from model object as entity of type application/json
		final Entity<RestReportTemplate> entity = Entity.entity(restReportTemplate, MediaType.APPLICATION_JSON_TYPE);

		try{
			Response response = webTarget.path("/generateReportPreview")
							 			//accept application/json
							 			.request(MediaType.APPLICATION_JSON_TYPE)
							 			//POST method with request body
	 		       	 					.post(entity);
			
			responseReport = readGoodResponseEntity(response, Report.class);
		}
		catch(Exception e){
			throw new ReportGenServiceException("exception in generating report for template",e);
		}
		return responseReport;
	}
}