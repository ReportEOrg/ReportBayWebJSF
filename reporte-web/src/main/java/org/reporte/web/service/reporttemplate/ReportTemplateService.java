package org.reporte.web.service.reporttemplate;

import java.io.Serializable;
import java.util.List;

import org.reporte.web.dto.reporttemplate.LiteReportTemplate;
import org.reporte.web.dto.reporttemplate.ReportTemplate;
import org.reporte.web.service.reporttemplate.exception.ReportTemplateServiceException;

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
}