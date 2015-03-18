package org.reporte.web.dto.model;

import java.io.Serializable;

public class AttributeMapping implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String referencedColumn;
	private String alias;
	private String typeName;
	private int order;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getReferencedColumn() {
		return referencedColumn;
	}
	public void setReferencedColumn(String referencedColumn) {
		this.referencedColumn = referencedColumn;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}

}