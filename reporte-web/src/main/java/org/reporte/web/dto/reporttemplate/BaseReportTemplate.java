package org.reporte.web.dto.reporttemplate;

import java.io.Serializable;
import java.util.Map;


public class BaseReportTemplate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String templateName;
	private int modelId;
	private ReportTemplateTypeEnum reportTemplateType;
	private String reportDisplayName;
	@SuppressWarnings("rawtypes")
	private Map reportQuery;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * @param templateName the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * @return the modelId
	 */
	public int getModelId() {
		return modelId;
	}

	/**
	 * @param modelId the modelId to set
	 */
	public void setModelId(int modelId) {
		this.modelId = modelId;
	}

	/**
	 * @return the type
	 */
	public ReportTemplateTypeEnum getReportTemplateType() {
		return reportTemplateType;
	}

	/**
	 * @param type the type to set
	 */
	public void setReportTemplateType(ReportTemplateTypeEnum reportTemplateType) {
		this.reportTemplateType = reportTemplateType;
	}

	/**
	 * @return the reportDisplayName
	 */
	public String getReportDisplayName() {
		return reportDisplayName;
	}

	/**
	 * @param reportDisplayName the reportDisplayName to set
	 */
	public void setReportDisplayName(String reportDisplayName) {
		this.reportDisplayName = reportDisplayName;
	}

	/**
	 * @return the reportQuery
	 */
	@SuppressWarnings("rawtypes")
	public Map getReportQuery() {
		return reportQuery;
	}

	/**
	 * @param reportQuery the reportQuery to set
	 */
	@SuppressWarnings("rawtypes")
	public void setReportQuery(Map reportQuery) {
		this.reportQuery = reportQuery;
	}

	public enum ReportTemplateTypeEnum { 
		SIMPLE("S"),
		CUSTOM("C");

		private String code;
		
		private ReportTemplateTypeEnum(String shortCode){
			code = shortCode;
		}
		
		public String getCode(){
			return code;
		}
	 }
}