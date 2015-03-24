package org.reporte.web.service.reportgen;

import java.io.Serializable;

import org.reporte.web.dto.reportgen.Report;
import org.reporte.web.dto.reporttemplate.RestReportTemplate;
import org.reporte.web.service.reportgen.exception.ReportGenServiceException;

public interface ReportGenService extends Serializable{
	
	/**
	 * 
	 * @param reportTemplateId
	 * @return
	 * @throws ReportGenServiceException
	 */
	Report generateReportByTemplateId(int reportTemplateId) throws ReportGenServiceException;
	/**
	 * 
	 * @param reportTemplate
	 * @return
	 * @throws ReportGenServiceException
	 */
	Report generateReportPreview(RestReportTemplate restReportTemplate) throws ReportGenServiceException;
}