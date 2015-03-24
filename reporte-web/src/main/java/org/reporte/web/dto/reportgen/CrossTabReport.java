package org.reporte.web.dto.reportgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CrossTabReport extends BaseReport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<CrossTabAttribute> attributes = new ArrayList<CrossTabAttribute>();
	private List<Map<String, String>> resultSet = new ArrayList<Map<String,String>>();

	/**
	 * 
	 * @return
	 */
	public List<CrossTabAttribute> getAttributes() {
		return attributes;
	}
	/**
	 * 
	 * @param attributes
	 */
	public void setAttributes(List<CrossTabAttribute> attributes) {
		this.attributes = attributes;
	}
	/**
	 * 
	 * @return
	 */
	public List<Map<String, String>> getResultSet() {
		return resultSet;
	}
	/**
	 * 
	 * @param resultSet
	 */
	public void setResultSet(List<Map<String, String>> resultSet) {
		this.resultSet = resultSet;
	}
}