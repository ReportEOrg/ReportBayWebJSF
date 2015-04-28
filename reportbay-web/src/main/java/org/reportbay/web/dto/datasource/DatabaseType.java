package org.reportbay.web.dto.datasource;

import java.io.Serializable;


public class DatabaseType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum DatabaseFamily {
		MsSQL, MySQL, Oracle
	}

	private int id;
	private String name;
	private DatabaseFamily family;
	private String urlPattern;
	private String driverName;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DatabaseFamily getFamily() {
		return family;
	}
	public void setFamily(DatabaseFamily family) {
		this.family = family;
	}
	public String getUrlPattern() {
		return urlPattern;
	}
	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	
}