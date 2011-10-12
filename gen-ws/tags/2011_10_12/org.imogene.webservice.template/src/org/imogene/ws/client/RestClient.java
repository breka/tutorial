package org.imogene.ws.client;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.log4j.Logger;
import org.imogene.ws.entity.MedooBean;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


/**
 * REST client to push data to an external REST Web Service
 * @author Medes-IMPS
 */
public class RestClient {
	
	private static Logger logger = Logger.getLogger("org.imogene.ws.client.RestClient");
	
    private RestTemplate restTemplate; 
    private Credentials credentials; 
    private String wsUrl;
    			   
    

	public void push(final MedooBean bean, final String entityName) {
    	
    	if(bean!=null && entityName!=null && !entityName.equals("")) {
    		
    		CommonsClientHttpRequestFactory factory = (CommonsClientHttpRequestFactory) restTemplate.getRequestFactory();
    		HttpClient client = factory.getHttpClient();
    		client.getState().setCredentials(AuthScope.ANY, credentials);
    		
    		if(!wsUrl.endsWith("/"))
    			wsUrl = wsUrl + "/";
    		
    		try {
				restTemplate.put(wsUrl + entityName, bean);
			} catch (RestClientException e) {
				//e.printStackTrace();
				logger.error(e.getMessage());
			}
    	}
    }
     


    /**
     * Setter for bean injection
     * @param credentials
     */
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

    /**
     * Setter for bean injection
     * @param restTemplate
     */
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

    /**
     * Setter for bean injection
     * @param wsUrl
     */
	public void setWsUrl(String wsUrl) {
		this.wsUrl = wsUrl;
	}
	 

}
