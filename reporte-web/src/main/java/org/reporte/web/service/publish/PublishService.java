package org.reporte.web.service.publish;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import org.reporte.web.service.publish.exception.PublishServiceException;

public interface PublishService extends Serializable{
	
	/**
	 * 
	 * @return
	 */
	String getServiceName();
	/**
	 * @param publishName
	 * @param file
	 * @param paramMap
	 */
	void publish(String publishName, File file, Map<String, Object> paramMap) throws PublishServiceException;
}