package org.imogene.ws.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

public class ResponseEncodingFilter extends OncePerRequestFilter {
	
	private static Logger logger = Logger.getLogger(ResponseEncodingFilter.class.getName());

	private static final String DEFAULT_ENCODING="utf-8";
	
	private boolean activated=false;
	
	private String encoding = DEFAULT_ENCODING;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {	
		logger.debug("Encoding filter called");
		if(activated){
			response.setCharacterEncoding(encoding);
			logger.debug("encoding forced to "+encoding);
		}
		  chain.doFilter(request, response);
	}
	
	public void setActivated(boolean activated){
		this.activated = activated;
	}
	
	public void setEncoding(String encoding){
		this.encoding = encoding;
	}
	
}
