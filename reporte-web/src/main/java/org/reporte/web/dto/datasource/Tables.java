package org.reporte.web.dto.datasource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tables implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Table> tables = new ArrayList<Table>();

	/**
	 * @return the tables
	 */
	public List<Table> getTables() {
		return tables;
	}

	/**
	 * @param tables the tables to set
	 */
	public void setTables(List<Table> tables) {
		this.tables = tables;
	}
}