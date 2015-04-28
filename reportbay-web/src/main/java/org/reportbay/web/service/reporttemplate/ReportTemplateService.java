package org.reportbay.web.service.reporttemplate;

import java.io.Serializable;
import java.util.List;

import org.reportbay.web.dto.reporttemplate.LiteReportTemplate;
import org.reportbay.web.dto.reporttemplate.ReportTemplate;
import org.reportbay.web.dto.reporttemplate.RestReportTemplate;
import org.reportbay.web.service.reporttemplate.exception.ReportTemplateServiceException;

public interface ReportTemplateService extends Serializable{
	
	/**
	 * 
	 * @return
	 * @throws ReportTemplateServiceException
	 */
	List<LiteReportTemplate> findAll() throws ReportTemplateServiceException;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws ReportTemplateServiceException
	 */
	ReportTemplate find(int id) throws ReportTemplateServiceException;
	
	/**
	 * 
	 * @param reportTemplate
	 * @return
	 * @throws ReportTemplateServiceException
	 */
	ReportTemplate save(ReportTemplate reportTemplate) throws ReportTemplateServiceException;
	
	/**
	 * 
	 * @param reportTemplate
	 * @return
	 * @throws ReportTemplateServiceException
	 */
	ReportTemplate update(ReportTemplate reportTemplate) throws ReportTemplateServiceException;
	
	/**
	 * 
	 * @param reportTemplate
	 * @return
	 * @throws ReportTemplateServiceException
	 */
	boolean delete(ReportTemplate reportTemplate) throws ReportTemplateServiceException;
	
	/**
	 * 
	 * @param restTemplate
	 * @return
	 * @throws ReportTemplateServiceException
	 */
	ReportTemplate mapRestToUIReportTemplate(RestReportTemplate restTemplate) throws ReportTemplateServiceException;
	
	/**
	 * 
	 * @param template
	 * @return
	 * @throws ReportTemplateServiceException
	 */
	RestReportTemplate mapUIToRestReportTemplate(ReportTemplate template) throws ReportTemplateServiceException;
}