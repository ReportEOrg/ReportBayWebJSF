package org.reportbay.web.dto.reporttemplate;

import java.io.Serializable;

public class TemplateSeries implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private String modelSeriesValue;

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the modelSeriesValue
	 */
	public String getModelSeriesValue() {
		return modelSeriesValue;
	}
	/**
	 * @param modelSeriesValue the modelSeriesValue to set
	 */
	public void setModelSeriesValue(String modelSeriesValue) {
		this.modelSeriesValue = modelSeriesValue;
	}
}