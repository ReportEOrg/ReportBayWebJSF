package org.reporte.web.service.datasource;

import java.io.Serializable;
import java.util.List;

import org.reporte.web.dto.datasource.DataSource;
import org.reporte.web.service.datasource.exception.DataSourceServiceException;

public interface DataSourceService extends Serializable{
	
	/**
	 * 
	 * @param datasource
	 * @return
	 * @throws DataSourceServiceException
	 */
	DataSource save(DataSource datasource) throws DataSourceServiceException;
	/**
	 * 
	 * @param datasource
	 * @return
	 * @throws DataSourceServiceException
	 */
	DataSource update(DataSource datasource) throws DataSourceServiceException;
	/**
	 * 
	 * @param datasource
	 * @return
	 * @throws DataSourceServiceException
	 */
	boolean delete(DataSource datasource) throws DataSourceServiceException;
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataSourceServiceException
	 */
	DataSource find(int id) throws DataSourceServiceException;
	/**
	 * 
	 * @return
	 * @throws DataSourceServiceException
	 */
	List<DataSource> findAll() throws DataSourceServiceException;
	
	/**
	 * 
	 * @param datasource
	 * @return
	 * @throws DataSourceServiceException
	 */
	List<String> findDataSourceTableName(DataSource datasource) throws DataSourceServiceException;
}