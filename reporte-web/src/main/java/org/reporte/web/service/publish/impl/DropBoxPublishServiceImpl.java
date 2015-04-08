package org.reporte.web.service.publish.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Locale;
import java.util.Map;

import org.reporte.web.service.publish.PublishService;
import org.reporte.web.service.publish.exception.PublishServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;
import com.dropbox.core.http.StandardHttpRequestor;

/**
 * 
 * Drop box publish service implementation
 *
 */
public class DropBoxPublishServiceImpl implements PublishService{
	
	private static final Logger LOG = LoggerFactory.getLogger(DropBoxPublishServiceImpl.class);
	
	public static final String ACCESS_TOKEN = "access_token";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getServiceName() {
		return "Drop Box";
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void publish(String publishName, File file, Map<String, Object> paramMap) throws PublishServiceException {
		LOG.info("publish");

		//1. obtain the config
		DbxRequestConfig config =createDbxRequestConfig();

		//2. obtain the oauth2 access token
		String accessToken = (String)paramMap.get(ACCESS_TOKEN);
		
		//3. setup client
		DbxClient client = new DbxClient(config, accessToken);
		
		try(FileInputStream inputStream = new FileInputStream(file)){
			DbxEntry.File uploadedFile = client.uploadFile("/"+publishName,
	                									   DbxWriteMode.add(), 
	                									   file.length(), 
	                									   inputStream);
			
			LOG.info("uploaded success : {}",uploadedFile.toString());
		} 
		catch (IOException e) {
			throw new PublishServiceException("Error processing file", e);
		} 
		catch (DbxException e) {
			throw new PublishServiceException("Error upload to drop box", e);
		} 
	}

	/**
	 * 
	 * @return
	 */
	private DbxRequestConfig createDbxRequestConfig(){
		DbxRequestConfig config;

        String proxyHost = System.getProperty("http.proxyHost");
        String proxyPort = System.getProperty("http.proxyPort");

        //if proxy server required
        if(proxyHost !=null && proxyPort!=null){
        	Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, Integer.valueOf(proxyPort)));
        	StandardHttpRequestor httpRequester = new StandardHttpRequestor(proxy);
        
        	config = new DbxRequestConfig("ReportE/1.0",
					   Locale.getDefault().toString(),
					   httpRequester);
        }
        else{
        	config = new DbxRequestConfig("ReportE/1.0",
        								  Locale.getDefault().toString());
        }
        
        return config;
	}
	
	
}