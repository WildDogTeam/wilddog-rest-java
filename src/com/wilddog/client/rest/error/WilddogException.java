package com.wilddog.client.rest.error;

import org.apache.log4j.Logger;


public class WilddogException extends Throwable {

	protected static final Logger LOGGER = Logger.getRootLogger();
	
	private static final long serialVersionUID = 1L;

	public WilddogException(String message ) {
		super( message );
	}
	
	public WilddogException(String message, Throwable cause ) {
		super( message, cause );
	}
	
}
