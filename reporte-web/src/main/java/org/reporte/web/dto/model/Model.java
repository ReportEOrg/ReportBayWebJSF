package org.reporte.web.dto.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.reporte.web.dto.datasource.DataSource;

public class Model implements Serializable{
	public enum Approach {
		SINGLE_TABLE("Single Table"), 
		JOIN_QUERY("Join Query");
		
		private String description;
		
		private Approach(String description){
			this.description = description;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @param description the description to set
		 */
		public void setDescription(String description) {
			this.description = description;
		}
		
		/**
		 * 
		 * @param name
		 * @return
		 */
		public static Approach fromDescription(String description){
			Approach enumValue = null;
					
			if(description!=null){
				for(Approach ref: Approach.values()){
					if(description.equals(ref.getDescription())){
						enumValue = ref;
						break;
					}
				}
			}
					
			return enumValue;
		}
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private String description;
	private DataSource datasource;
	private List<AttributeMapping> attributeBindings = new ArrayList<AttributeMapping>();
	private ModelQuery query;
	private String table;
	private Approach approach;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AttributeMapping> getAttributeBindings() {
		return attributeBindings;
	}

	public void setAttributeBindings(List<AttributeMapping> attributeBindings) {
		this.attributeBindings = attributeBindings;
	}

	public ModelQuery getQuery() {
		return query;
	}

	public void setQuery(ModelQuery query) {
		this.query = query;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public Approach getApproach() {
		return approach;
	}

	public void setApproach(Approach approach) {
		this.approach = approach;
	}
}