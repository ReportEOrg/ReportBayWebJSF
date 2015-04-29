package org.reportbay.web.service.publish;

import java.io.Serializable;

import org.reportbay.web.dto.publish.Publish;
import org.reportbay.web.service.publish.exception.PublishServiceException;

public interface PublishService extends Serializable{
	
	/**
	 * 
	 * @param publish
	 * @throws PublishServiceException
	 */
	void publish(Publish publish) throws PublishServiceException;
}