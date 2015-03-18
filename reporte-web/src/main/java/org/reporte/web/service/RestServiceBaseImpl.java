package org.reporte.web.service;

import javax.ws.rs.core.Response;

public class RestServiceBaseImpl{
	/**
	 * read the response into entity class and close the response (in case underlying is using simple client)
	 * @param response
	 * @param entityClass
	 * @return
	 */
	protected <T> T readGoodResponseEntity(Response response, Class<T> entityClass){
		T entity = null;
		
		if(Response.Status.OK.getStatusCode() == response.getStatus()){
			entity = response.readEntity(entityClass);
		}
		
		response.close();
		
		return entity;
	}
}