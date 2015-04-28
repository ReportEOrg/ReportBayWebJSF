package org.reportbay.web.dto.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Models implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Model> models = new ArrayList<Model>();

	public List<Model> getModels() {
		return models;
	}

	public void setModels(List<Model> models) {
		this.models = models;
	}
	
	
}