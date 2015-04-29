package org.reportbay.web.service.publish.impl;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.reportbay.web.dto.publish.Publish;
import org.reportbay.web.service.RestServiceBaseImpl;
import org.reportbay.web.service.publish.PublishService;
import org.reportbay.web.service.publish.exception.PublishServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Publish Service implementation
 *
 */
public class PublishServiceImpl extends RestServiceBaseImpl implements PublishService{

	private static final Logger LOG = LoggerFactory.getLogger(PublishServiceImpl.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String API_PREFIX= "/api/publish";
	
	private WebTarget webTarget;
	
	public PublishServiceImpl(){
		webTarget = ClientBuilder.newClient().target(getTargetRestServerBaseUri()+API_PREFIX);
	}
	
	/**
	 * {@inheritDoc} 
	 */
	@Override
	public void publish(Publish publish) throws PublishServiceException {
		LOG.info("publishing {}",publish);
		String errorString = null;
		
		//create a request body from model object as entity of type application/json
		final Entity<Publish> entity = Entity.entity(publish, MediaType.APPLICATION_JSON_TYPE);
				
		try{
			Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
										 //POST method with request body
				 	 					 .post(entity);
			
			if(!Response.Status.OK.equals(response.getStatus())){
				errorString = response.readEntity(String.class);
			}
			
			response.close();
		}
		catch(Exception e){
			throw new PublishServiceException("exception in publishing",e);
		}
		
		if(errorString!=null){
			throw new PublishServiceException("exception in publishing"+errorString);
		}
	}
}