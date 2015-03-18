package org.reporte.web.dto.datasource;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * Wrapper (Envelop) for holding list of data source
 *
 */
public class DataSources implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<DataSource> datasources;

	/**
	 * @return the datasources
	 */
	public List<DataSource> getDatasources() {
		return datasources;
	}

	/**
	 * @param datasources the datasources to set
	 */
	public void setDatasources(List<DataSource> datasources) {
		this.datasources = datasources;
	}
}