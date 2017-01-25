package com.wilddog.client.rest.demo;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.wilddog.client.rest.error.JacksonUtilityException;
import com.wilddog.client.rest.error.WilddogException;
import com.wilddog.client.rest.model.WilddogResponse;
import com.wilddog.client.rest.service.Wilddog;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

public class Demo {

	public static void main(String[] args) throws WilddogException, JsonParseException, JsonMappingException, IOException, JacksonUtilityException {
		String wilddog_baseUrl = "https://<appId>.wilddogio.com/rest";
		for( String s : args ) {

			if( s == null || s.trim().isEmpty() ) continue;
			if( s.trim().split( "=" )[0].equals( "baseUrl" ) ) {
				wilddog_baseUrl = s.trim().split( "=" )[1];
			}
		}
		if( wilddog_baseUrl == null || wilddog_baseUrl.trim().isEmpty() ) {
			throw new IllegalArgumentException( "Program-argument 'baseUrl' not found but required" );
		}

		
		// create the wilddog
		Wilddog wilddog = new Wilddog( wilddog_baseUrl );
		
		
		// "DELETE" (the wd4jDemo-root)
		WilddogResponse response = wilddog.delete();
	

		// "PUT" (test-map into the wd4jDemo-root)
		Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
		dataMap.put( "PUT-root", "This was PUT into the wd4jDemo-root" );
		response = wilddog.put( dataMap );
		System.out.println( "\n\nResult of PUT (for the test-PUT to wd4jDemo-root):\n" + response );
		System.out.println("\n");
		
		
		// "GET" (the wd4jDemo-root)
		response = wilddog.get();
		System.out.println( "\n\nResult of GET:\n" + response );
		System.out.println("\n");
		
		
		// "PUT" (test-map into a sub-node off of the wd4jDemo-root)
		dataMap = new LinkedHashMap<String, Object>();
		dataMap.put( "Key_1", "This is the first value" );
		dataMap.put( "Key_2", "This is value #2" );
		Map<String, Object> dataMap2 = new LinkedHashMap<String, Object>();
		dataMap2.put( "Sub-Key1", "This is the first sub-value" );
		dataMap.put( "Key_3", dataMap2 );
		response = wilddog.put( "test-PUT", dataMap );
		System.out.println( "\n\nResult of PUT (for the test-PUT):\n" + response );
		System.out.println("\n");
		
		
		// "GET" (the test-PUT)
		response = wilddog.get( "test-PUT" );
		System.out.println( "\n\nResult of GET (for the test-PUT):\n" + response );
		System.out.println("\n");
		
		
		// "POST" (test-map into a sub-node off of the wd4jDemo-root)
		response = wilddog.post( "test-POST", dataMap );
		System.out.println( "\n\nResult of POST (for the test-POST):\n" + response );
		System.out.println("\n");
		
		
		// "DELETE" (it's own test-node)
		dataMap = new LinkedHashMap<String, Object>();
		dataMap.put( "DELETE", "This should not appear; should have been DELETED" );
		response = wilddog.put( "test-DELETE", dataMap );
		System.out.println( "\n\nResult of PUT (for the test-DELETE):\n" + response );
		response = wilddog.delete( "test-DELETE");
		System.out.println( "\n\nResult of DELETE (for the test-DELETE):\n" + response );
		response = wilddog.get( "test-DELETE" );
		System.out.println( "\n\nResult of GET (for the test-DELETE):\n" + response );
		
	}
	
}




