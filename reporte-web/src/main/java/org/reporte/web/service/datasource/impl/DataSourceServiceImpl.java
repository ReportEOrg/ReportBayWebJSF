package org.reporte.web.service.datasource.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.reporte.web.dto.datasource.DataSource;
import org.reporte.web.dto.datasource.DataSources;
import org.reporte.web.dto.datasource.Table;
import org.reporte.web.dto.datasource.Tables;
import org.reporte.web.service.RestServiceBaseImpl;
import org.reporte.web.service.datasource.DataSourceService;
import org.reporte.web.service.datasource.exception.DataSourceServiceException;

@Named
public class DataSourceServiceImpl extends RestServiceBaseImpl implements DataSourceService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//TODO: replace with 
	private String restServerUrl= "http://localhost:8080/report-rest/api/datasources";
	
	//TODO: thread safe?
	private WebTarget webTarget;
	
	public DataSourceServiceImpl(){
		webTarget = ClientBuilder.newClient().target(getRestServerUrl());
	}

	@Override
	public DataSource save(DataSource datasource) throws DataSourceServiceException {
		DataSource responseDataSource = null;
		//create a request body from DataSource object as entity of type application/json
		final Entity<DataSource> entity = Entity.entity(datasource, MediaType.APPLICATION_JSON_TYPE);

		try{
			//accept application/json
			Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
									 	 //POST method with request body
									 	 .post(entity);
			
			responseDataSource = readGoodResponseEntity(response, DataSource.class);
		}
		catch(Exception e){
			throw new DataSourceServiceException("exception in saving datasource",e);
		}
		return responseDataSource;
	}

	@Override
	public DataSource update(DataSource datasource) throws DataSourceServiceException {
		DataSource responseDataSource = null;

		//create a request body from DataSource object as entity of type application/json
		final Entity<DataSource> entity = Entity.entity(datasource, MediaType.APPLICATION_JSON_TYPE);

		try{
			//accept application/json
			Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
						                 //PUT method with request body
						                 .put(entity);
			
			responseDataSource = readGoodResponseEntity(response, DataSource.class);
		}
		catch(Exception e){
			throw new DataSourceServiceException("exception in updating datasource",e);
		}
		
		return responseDataSource;
	}

	@Override
	public boolean delete(DataSource datasource) throws DataSourceServiceException {
		boolean deleted = false;

		try{
			Response response = webTarget.path("/{datasourceId}")
										 //bind datasourceId to {datasourceId}
										 .resolveTemplate("datasourceId", datasource.getId())
										 //accept application/json
										 .request(MediaType.APPLICATION_JSON_TYPE)
										 //DELETE method
										 .delete();
			
			if(Response.Status.OK.equals(response.getStatus())){
				deleted = true;
			}
		}
		catch(Exception e){
			throw new DataSourceServiceException("exception in updating datasource",e);
		}
		
		return deleted;
		
	}

	@Override
	public DataSource find(int id) throws DataSourceServiceException {
		DataSource dataSource = null;
		try{
			Response response = webTarget.path("/{datasourceId}")
								  		 //bind datasourceId to {datasourceId}
								  		 .resolveTemplate("datasourceId", id)
								  		 //accept application/json
								  		 .request(MediaType.APPLICATION_JSON_TYPE)
								  		 //GET method 
								  		 .get();
			
			dataSource = readGoodResponseEntity(response, DataSource.class);
		}
		catch(Exception e){
			throw new DataSourceServiceException("exception in finding datasource",e);
		}
		return dataSource;
	}

	@Override
	public List<DataSource> findAll() throws DataSourceServiceException {

		Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).get();
		
		DataSources result = readGoodResponseEntity(response, DataSources.class);
		
		List<DataSource> datasources;
		
		if(result!=null){
			datasources = result.getDatasources();
		}
		else{
			datasources = new ArrayList<DataSource>();
		}
		return datasources;
	}

	@Override
	public List<String> findDataSourceTableName(DataSource datasource) throws DataSourceServiceException {
		
		List<String> tableNameList = new ArrayList<String>();
		try{
			Response response = webTarget.path("/{datasourceId}/tables")
								  		 //bind datasourceId to {datasourceId}
								  		 .resolveTemplate("datasourceId", datasource.getId())
								  		 //accept application/json
								  		 .request(MediaType.APPLICATION_JSON_TYPE)
								  		 //GET method
								  		 .get();
			
			Tables tables = readGoodResponseEntity(response, Tables.class);
			
			if(tables!=null && tables.getTables()!=null){

				for(Table table: tables.getTables()){
					
					if(table!=null && StringUtils.isNotBlank(table.getName())){
						tableNameList.add(table.getName());
					}
				}
			}
		}
		catch(Exception e){
			throw new DataSourceServiceException("exception in finding datasource",e);
		}
		return tableNameList;
	}

	/**
	 * @return the restServerUrl
	 */
	public String getRestServerUrl() {
		return restServerUrl;
	}

	/**
	 * @param restServerUrl the restServerUrl to set
	 */
	public void setRestServerUrl(String restServerUrl) {
		this.restServerUrl = restServerUrl;
	}
}