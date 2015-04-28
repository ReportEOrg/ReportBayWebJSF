package org.reportbay.web.dto.model;

import java.io.Serializable;

public class ModelQuery implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String value;
	private String joinQuery;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getJoinQuery() {
		return joinQuery;
	}
	public void setJoinQuery(String joinQuery) {
		this.joinQuery = joinQuery;
	}
}