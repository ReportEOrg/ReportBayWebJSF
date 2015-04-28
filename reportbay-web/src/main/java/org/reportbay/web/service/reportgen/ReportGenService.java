package org.reportbay.web.service.reportgen;

import java.io.Serializable;

import org.reportbay.web.dto.reportgen.Report;
import org.reportbay.web.dto.reporttemplate.RestReportTemplate;
import org.reportbay.web.service.reportgen.exception.ReportGenServiceException;

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