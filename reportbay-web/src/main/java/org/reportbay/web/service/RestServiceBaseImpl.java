package org.reportbay.web.service;

import javax.ws.rs.core.Response;

public class RestServiceBaseImpl{
	
//	private String targetRestServerBaseUri = "http://localhost:8080/reportbay-rest";
	
	private String targetRestServerBaseUri = "http://reporte-staginguat.rhcloud.com/reportbay-rest";
	
	/**
	 * @return the targetRestServerBaseUri
	 */
	public String getTargetRestServerBaseUri() {
		return targetRestServerBaseUri;
	}

	/**
	 * @param targetRestServerBaseUri the targetRestServerBaseUri to set
	 */
	public void setTargetRestServerBaseUri(String targetRestServerBaseUri) {
		this.targetRestServerBaseUri = targetRestServerBaseUri;
	}



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