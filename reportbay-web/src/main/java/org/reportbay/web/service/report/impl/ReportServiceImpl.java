package org.reportbay.web.service.report.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.reportbay.web.dto.report.LiteReport;
import org.reportbay.web.dto.report.LiteReports;
import org.reportbay.web.dto.reportgen.Report;
import org.reportbay.web.dto.reporttemplate.RestReportTemplate;
import org.reportbay.web.service.RestServiceBaseImpl;
import org.reportbay.web.service.report.ReportService;
import org.reportbay.web.service.report.exception.ReportServiceException;

@Named
public class ReportServiceImpl extends RestServiceBaseImpl implements ReportService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String API_PREFIX= "/api/reports";
	
	//TODO: thread safe?
	private WebTarget webTarget;
	
	public ReportServiceImpl(){
		
		webTarget = ClientBuilder.newClient().target(getTargetRestServerBaseUri()+API_PREFIX);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Report generateReportByTemplateId(int reportTemplateId) throws ReportServiceException{
		Report responseReport = null;
		try{
			Response response = webTarget.path("/previewconnector/{reportConnectorId}")
									 	 //bind reportTemplateId to {reportConnectorId}
							 			.resolveTemplate("reportConnectorId", reportTemplateId)
							 			//accept application/json
							 			.request(MediaType.APPLICATION_JSON_TYPE)
							 			//GET method 
							 			.get();
			
			responseReport = readGoodResponseEntity(response, Report.class);
		}
		catch(Exception e){
			throw new ReportServiceException("exception in generating report for template",e);
		}
		return responseReport;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Report generateReportPreview(RestReportTemplate restReportTemplate) throws ReportServiceException{
		Report responseReport = null;
		
		//create a request body from model object as entity of type application/json
		final Entity<RestReportTemplate> entity = Entity.entity(restReportTemplate, MediaType.APPLICATION_JSON_TYPE);

		try{
			Response response = webTarget.path("/preview")
							 			//accept application/json
							 			.request(MediaType.APPLICATION_JSON_TYPE)
							 			//POST method with request body
	 		       	 					.post(entity);
			
			responseReport = readGoodResponseEntity(response, Report.class);
		}
		catch(Exception e){
			throw new ReportServiceException("exception in generating report for template",e);
		}
		return responseReport;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<LiteReport> getReports() throws ReportServiceException{
		List<LiteReport> reportList = new ArrayList<LiteReport>();
		try{
                              			//accept application/json
			Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
							 			//GET method 
							 			.get();
			
			LiteReports responseReport = readGoodResponseEntity(response, LiteReports.class);
			
			if(responseReport!=null && responseReport.getLiteReports()!=null){
				reportList.addAll(responseReport.getLiteReports());
			}
		}
		catch(Exception e){
			throw new ReportServiceException("exception in retriving report list",e);
		}
		return reportList;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Report retrieveSnapShotReport(int reportId) throws ReportServiceException{
		Report responseReport = null;
		try{
			Response response = webTarget.path("/{reportId}")
									 	 //bind reportId to {reportId}
							 			.resolveTemplate("reportId", reportId)
							 			//accept application/json
							 			.request(MediaType.APPLICATION_JSON_TYPE)
							 			//GET method 
							 			.get();
			
			responseReport = readGoodResponseEntity(response, Report.class);
		}
		catch(Exception e){
			throw new ReportServiceException("exception in generating report for template",e);
		}
		return responseReport;
	}
}