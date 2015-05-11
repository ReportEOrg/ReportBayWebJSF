package org.reportbay.web.service.report;

import java.io.Serializable;
import java.util.List;

import org.reportbay.web.dto.report.LiteReport;
import org.reportbay.web.dto.reportgen.Report;
import org.reportbay.web.dto.reporttemplate.RestReportTemplate;
import org.reportbay.web.service.report.exception.ReportServiceException;

public interface ReportService extends Serializable{
	
	/**
	 * 
	 * @param reportTemplateId
	 * @return
	 * @throws ReportGenServiceException
	 */
	Report generateReportByTemplateId(int reportTemplateId) throws ReportServiceException;
	/**
	 * 
	 * @param reportTemplate
	 * @return
	 * @throws ReportGenServiceException
	 */
	Report generateReportPreview(RestReportTemplate restReportTemplate) throws ReportServiceException;
	
	/**
	 * 
	 * @return
	 * @throws ReportServiceException
	 */
	List<LiteReport> getReports() throws ReportServiceException;
	
	/**
	 * 
	 * @param reportId
	 * @return
	 * @throws ReportServiceException
	 */
	Report retrieveSnapShotReport(int reportId) throws ReportServiceException;
}