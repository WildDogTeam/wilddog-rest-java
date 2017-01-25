package com.wilddog.client.rest.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.wilddog.client.rest.error.JacksonUtilityException;
import com.wilddog.client.rest.error.WilddogException;
import com.wilddog.client.rest.model.WilddogResponse;
import com.wilddog.client.rest.util.JacksonUtility;

import okhttp3.*;
import org.apache.log4j.Logger;

/**
 * 基于 [Wilddog REST API](https://z.wilddog.com/rest/quickstart)的封装
 */
public class Wilddog {
	
	protected static final Logger 			LOGGER 					= Logger.getRootLogger();
	
	public static final String WILDDOG_API_JSON_EXTENSION
																	= ".json";
	

	
///////////////////////////////////////////////////////////////////////////////
//
// PROPERTIES & CONSTRUCTORS
//
///////////////////////////////////////////////////////////////////////////////
	
	
	private final String baseUrl;
	private String secureToken = null;
	private List<BasicNameValuePair> query;

	public Wilddog(String baseUrl ) throws WilddogException {

		if( baseUrl == null || baseUrl.trim().isEmpty() ) {
			String msg = "baseUrl cannot be null or empty; was: '" + baseUrl + "'";
			LOGGER.error( msg );
			throw new WilddogException( msg );
		}
		this.baseUrl = baseUrl.trim();
		query = new ArrayList<BasicNameValuePair>();
		LOGGER.info( "intialized with base-url: " + this.baseUrl );
	}
	
	public Wilddog(String baseUrl, String secureToken) throws WilddogException {
		if( baseUrl == null || baseUrl.trim().isEmpty() ) {
			String msg = "baseUrl cannot be null or empty; was: '" + baseUrl + "'";
			LOGGER.error( msg );
			throw new WilddogException( msg );
		}
		this.secureToken = secureToken;
		this.baseUrl = baseUrl.trim();
		query = new ArrayList<BasicNameValuePair>();
		LOGGER.info( "intialized with base-url: " + this.baseUrl );
	}

	
	
///////////////////////////////////////////////////////////////////////////////
//
// PUBLIC API
//
///////////////////////////////////////////////////////////////////////////////
	


	/**
	 * GETs data from the base-url.
	 * 
	 * @return {@link WilddogResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link WilddogException}
	 */
	public WilddogResponse get() throws WilddogException, UnsupportedEncodingException {
		return this.get( null );
	}
	
	/**
	 * GETs data from the provided-path relative to the base-url.
	 * 
	 * @param path -- if null/empty, refers to the base-url
	 * @return {@link WilddogResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link WilddogException}
	 */
	public WilddogResponse get(String path ) throws WilddogException, UnsupportedEncodingException {
		
		// make the request
		String url = this.buildFullUrlFromRelativePath( path );
		Request request = new Request.Builder()
				.url(url)
				.build();
		Response httpResponse = this.makeRequest( request );
		
		// process the response
		WilddogResponse response = this.processResponse( WilddogRestMethod.GET, httpResponse );
		
		return response;
	}
	
	/**
	 * PATCHs data to the base-url
	 * 
	 * @param data -- can be null/empty
	 * @return
	 * @throws {@link WilddogException}
	 * @throws {@link JacksonUtilityException}
	 * @throws UnsupportedEncodingException
	 */
	
	public WilddogResponse patch(Map<String, Object> data) throws WilddogException, JacksonUtilityException, UnsupportedEncodingException {
		return this.patch(null, data);
	}
	
	/**
	 * PATCHs data on the provided-path relative to the base-url.
	 * 
	 * @param path -- if null/empty, refers to the base-url
	 * @param data -- can be null/empty
	 * @return {@link WilddogResponse}
	 * @throws {@link WilddogException}
	 * @throws {@link JacksonUtilityException}
	 * @throws UnsupportedEncodingException
	 */
	
	public WilddogResponse patch(String path, Map<String, Object> data) throws WilddogException, JacksonUtilityException, UnsupportedEncodingException {
		// make the request
		String url = this.buildFullUrlFromRelativePath( path );

		Request request = new Request.Builder()
				.url(url)
				.patch(this.buildRequestBodyFromDataMap( data ))
				.build();

		Response httpResponse = this.makeRequest( request );
				
		// process the response
		WilddogResponse response = this.processResponse( WilddogRestMethod.PATCH, httpResponse );
				
		return response;
	}
	
	/**
	 * 
	 * @param jsonData
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws WilddogException
	 */
	
	public WilddogResponse patch(String jsonData) throws UnsupportedEncodingException, WilddogException {
		return this.patch(null, jsonData);
	}
	
	/**
	 * 
	 * @param path
	 * @param jsonData
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws WilddogException
	 */
	
	public WilddogResponse patch(String path, String jsonData) throws UnsupportedEncodingException, WilddogException {
		// make the request
		String url = this.buildFullUrlFromRelativePath( path );

		Request request = new Request.Builder()
				.url(url)
				.patch(this.buildRequestBodyFromJsonData( jsonData ))
				.build();

		Response httpResponse = this.makeRequest( request );


		// process the response
		WilddogResponse response = this.processResponse( WilddogRestMethod.PATCH, httpResponse );
				
		return response;		
	}
	
	/**
	 * PUTs data to the base-url (ie: creates or overwrites).
	 * If there is already data at the base-url, this data overwrites it.
	 * If data is null/empty, any data existing at the base-url is deleted.
	 * 
	 * @param data -- can be null/empty
	 * @return {@link WilddogResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link JacksonUtilityException}
	 * @throws {@link WilddogException}
	 */
	public WilddogResponse put(Map<String, Object> data ) throws JacksonUtilityException, WilddogException, UnsupportedEncodingException {
		return this.put( null, data );
	}
	
	/**
	 * PUTs data to the provided-path relative to the base-url (ie: creates or overwrites).
	 * If there is already data at the path, this data overwrites it.
	 * If data is null/empty, any data existing at the path is deleted.
	 * 
	 * @param path -- if null/empty, refers to base-url
	 * @param data -- can be null/empty
	 * @return {@link WilddogResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link JacksonUtilityException}
	 * @throws {@link WilddogException}
	 */
	public WilddogResponse put(String path, Map<String, Object> data ) throws JacksonUtilityException, WilddogException, UnsupportedEncodingException {
		
		// make the request
		String url = this.buildFullUrlFromRelativePath( path );

		Request request = new Request.Builder()
				.url(url)
				.put(this.buildRequestBodyFromDataMap( data ))
				.build();

		Response httpResponse = this.makeRequest( request );

		// process the response
		WilddogResponse response = this.processResponse( WilddogRestMethod.PUT, httpResponse );
		
		return response;
	}
	
	/**
	 * PUTs data to the provided-path relative to the base-url (ie: creates or overwrites).
	 * If there is already data at the path, this data overwrites it.
	 * If data is null/empty, any data existing at the path is deleted.
	 * 
	 * @param jsonData -- can be null/empty
	 * @return {@link WilddogResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link WilddogException}
	 */
	public WilddogResponse put(String jsonData ) throws WilddogException, UnsupportedEncodingException {
		return this.put( null, jsonData );
	}

	/**
	 * PUTs data to the provided-path relative to the base-url (ie: creates or overwrites).
	 * If there is already data at the path, this data overwrites it.
	 * If data is null/empty, any data existing at the path is deleted.
	 * 
	 * @param path -- if null/empty, refers to base-url
	 * @param jsonData -- can be null/empty
	 * @return {@link WilddogResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link WilddogException}
	 */
	public WilddogResponse put(String path, String jsonData ) throws WilddogException, UnsupportedEncodingException {

		// make the request
		String url = this.buildFullUrlFromRelativePath( path );

		Request request = new Request.Builder()
				.url(url)
				.put(this.buildRequestBodyFromJsonData( jsonData ))
				.build();

		Response httpResponse = this.makeRequest( request );

		WilddogResponse response = this.processResponse( WilddogRestMethod.PUT, httpResponse );
		
		return response;		
	}
	
	/**
	 * POSTs data to the base-url (ie: creates).
	 * 
	 * NOTE: the Wilddog API does not treat this method in the conventional way, but instead defines it
	 * as 'PUSH'; the API will insert this data under the base-url but associated with a Wilddog-
	 * generated key; thus, every use of this method will result in a new insert even if the data already 
	 * exists.
	 * 
	 * @param data -- can be null/empty but will result in no data being POSTed
	 * @return {@link WilddogResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link JacksonUtilityException}
	 * @throws {@link WilddogException}
	 */
	public WilddogResponse post(Map<String, Object> data ) throws JacksonUtilityException, WilddogException, UnsupportedEncodingException {
		return this.post( null, data );
	}
	
	/**
	 * POSTs data to the provided-path relative to the base-url (ie: creates).
	 * 
	 * NOTE: the Wilddog API does not treat this method in the conventional way, but instead defines it
	 * as 'PUSH'; the API will insert this data under the provided path but associated with a Wilddog-
	 * generated key; thus, every use of this method will result in a new insert even if the provided path
	 * and data already exist.
	 * 
	 * @param path -- if null/empty, refers to base-url
	 * @param data -- can be null/empty but will result in no data being POSTed
	 * @return {@link WilddogResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link JacksonUtilityException}
	 * @throws {@link WilddogException}
	 */
	public WilddogResponse post(String path, Map<String, Object> data ) throws JacksonUtilityException, WilddogException, UnsupportedEncodingException {
		
		// make the request
		String url = this.buildFullUrlFromRelativePath( path );

		Request request = new Request.Builder()
				.url(url)
				.post(this.buildRequestBodyFromDataMap( data ))
				.build();

		Response httpResponse = this.makeRequest( request );

		// process the response
		WilddogResponse response = this.processResponse( WilddogRestMethod.POST, httpResponse );
		
		return response;
	}
	
	/**
	 * POSTs data to the base-url (ie: creates).
	 * 
	 * NOTE: the Wilddog API does not treat this method in the conventional way, but instead defines it
	 * as 'PUSH'; the API will insert this data under the base-url but associated with a Wilddog-
	 * generated key; thus, every use of this method will result in a new insert even if the provided data 
	 * already exists.
	 * 
	 * @param jsonData -- can be null/empty but will result in no data being POSTed
	 * @return {@link WilddogResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link WilddogException}
	 */
	public WilddogResponse post(String jsonData ) throws WilddogException, UnsupportedEncodingException {
		return this.post( null, jsonData );
	}
	
	/**
	 * POSTs data to the provided-path relative to the base-url (ie: creates).
	 * 
	 * NOTE: the Wilddog API does not treat this method in the conventional way, but instead defines it
	 * as 'PUSH'; the API will insert this data under the provided path but associated with a Wilddog-
	 * generated key; thus, every use of this method will result in a new insert even if the provided path
	 * and data already exist.
	 * 
	 * @param path -- if null/empty, refers to base-url
	 * @param jsonData -- can be null/empty but will result in no data being POSTed
	 * @return {@link WilddogResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link WilddogException}
	 */
	public WilddogResponse post(String path, String jsonData ) throws WilddogException, UnsupportedEncodingException {
		
		// make the request
		String url = this.buildFullUrlFromRelativePath( path );

		Request request = new Request.Builder()
				.url(url)
				.post(this.buildRequestBodyFromJsonData( jsonData ))
				.build();

		Response httpResponse = this.makeRequest( request );

		// process the response
		WilddogResponse response = this.processResponse( WilddogRestMethod.POST, httpResponse );
		
		return response;
	}
	
	/**
	 * Append a query to the request.
	 * 
	 * @param query -- Query string based on Wilddog REST API
	 * @param parameter -- Query parameter
	 * @return Wilddog -- return this Wilddog object
	 */
	
	public Wilddog addQuery(String query, String parameter) {
		this.query.add(new BasicNameValuePair(query, parameter));
		return this;
	}
	
	/**
	 * DELETEs data from the base-url.
	 * 
	 * @return {@link WilddogResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link WilddogException}
	 */
	public WilddogResponse delete() throws WilddogException, UnsupportedEncodingException {
		return this.delete( null );
	}

	/**
	 * DELETEs data from the provided-path relative to the base-url.
	 * 
	 * @param path -- if null/empty, refers to the base-url
	 * @return {@link WilddogResponse}
	 * @throws UnsupportedEncodingException 
	 * @throws {@link WilddogException}
	 */
	public WilddogResponse delete(String path ) throws WilddogException, UnsupportedEncodingException {
		
		// make the request
		String url = this.buildFullUrlFromRelativePath( path );

		Request request = new Request.Builder()
				.url(url)
				.delete()
				.build();

		Response httpResponse = this.makeRequest( request );

		
		// process the response
		WilddogResponse response = this.processResponse( WilddogRestMethod.DELETE, httpResponse );
		
		return response;
	}
	
	
	
///////////////////////////////////////////////////////////////////////////////
//
// PRIVATE API
//
///////////////////////////////////////////////////////////////////////////////
	
	
	private RequestBody buildRequestBodyFromDataMap(Map<String, Object> dataMap ) throws WilddogException, JacksonUtilityException {
		
		String jsonData = JacksonUtility.GET_JSON_STRING_FROM_MAP( dataMap );
		
		return this.buildRequestBodyFromJsonData( jsonData );
	}

	public static final MediaType JSON
			= MediaType.parse("application/json; charset=utf-8");

	private RequestBody buildRequestBodyFromJsonData(String jsonData ) throws WilddogException {

		RequestBody result =  null;
		try {

			result = RequestBody.create(JSON, jsonData);

		} catch( Throwable t ) {
			
			String msg = "unable to create entity from data; data was: " + jsonData;
			LOGGER.error( msg );
			throw new WilddogException( msg, t );
			
		}
		
		return result;
	}
	
	private String buildFullUrlFromRelativePath( String path ) throws UnsupportedEncodingException {
		
		// massage the path (whether it's null, empty, or not) into a full URL
		if( path == null ) {
			path = "";
		}
		path = path.trim();
		if( !path.isEmpty() && !path.startsWith( "/" ) ) {
			path = "/" + path;
		}
		String url = this.baseUrl + path + Wilddog.WILDDOG_API_JSON_EXTENSION;
		
		if(query != null) {
			url += "?";

			Iterator<BasicNameValuePair> it = query.iterator();
			BasicNameValuePair e;
			while(it.hasNext()) {
				e = it.next();
				url += e.getName() + "=" + URLEncoder.encode(e.getValue(), "UTF-8") + "&";
			}
		}
		
		if(secureToken != null) {
			if(query != null) {
				url += "auth=" + secureToken;
			} else {
				url += "?auth=" + secureToken;
			}
		}
		
		if(url.lastIndexOf("&") == url.length()) {
			StringBuilder str = new StringBuilder(url);
			str.deleteCharAt(str.length());
			url = str.toString();
		}
		
		LOGGER.info( "built full url to '" + url + "' using relative-path of '" + path + "'" );
		
		return url;
	}

   static class LoggingInterceptor implements Interceptor {
        @Override public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            LOGGER.info(String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);



            long t2 = System.nanoTime();
            LOGGER.info(String.format("Received response for %s in %.1fms %dbyte %n%s",
                    response.request().url(), (t2 - t1) / 1e6d,  response.body().contentLength(), response.headers()));

            return response;
        }
    }
	
	
	private Response makeRequest( Request request ) throws WilddogException {
		// sanity-check
		if( request == null ) {

			String msg = "request cannot be null";
			LOGGER.error( msg );
			throw new WilddogException( msg );
		}
		Response response = null;
		try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new LoggingInterceptor())
                    .build();

            response = client.newCall(request).execute();

		} catch( Throwable t ) {
			String msg = "unable to receive response from request(" + request.method() +  ") @ " + request.url();
			LOGGER.error( msg );
			throw new WilddogException( msg, t );
		}
		return response;
	}
	
	private WilddogResponse processResponse(WilddogRestMethod method, Response httpResponse ) throws WilddogException {
	
		WilddogResponse response = null;

		// sanity-checks
		if( method == null ) {
			
			String msg = "method cannot be null";
			LOGGER.error( msg );
			throw new WilddogException( msg );
		}
		if( httpResponse == null ) {
			
			String msg = "httpResponse cannot be null";
			LOGGER.error( msg );
			throw new WilddogException( msg );
		}
		
		// get the response-entity
		ResponseBody entity = httpResponse.body();
		
		// get the response-code
		int code = httpResponse.code();

		// set the response-success
		boolean success =  httpResponse.isSuccessful();

		// get the response-body
		String  responseBody = null;

//        if (entity.contentLength() > 0) {
        try {
            responseBody =  new String(entity.bytes(), "UTF-8");
        } catch (IOException e) {
            String msg = "unable to convert response-body into string; response-body was: '" + responseBody + "'";
            LOGGER.error(msg);
            throw new WilddogException(msg, e);
        }
//        }
		
		// convert response-body to map
		Map<String, Object> body = null;
		try {
			
			body = JacksonUtility.GET_JSON_STRING_AS_MAP(responseBody);
			
		} catch( JacksonUtilityException jue ) {
			
			String msg = "unable to convert response-body into map; response-body was: '" + responseBody + "'";
			LOGGER.error( msg );
			throw new WilddogException( msg, jue );
		}
		
		// build the response
		response = new WilddogResponse( success, code, body, responseBody);
		
		//clear the query
		query = null;
		
		return response;
	}
	
	
	
///////////////////////////////////////////////////////////////////////////////
//
// INTERNAL CLASSES
//
///////////////////////////////////////////////////////////////////////////////

	
	public enum WilddogRestMethod {
		
		GET,
		PATCH,
		PUT,
		POST,
		DELETE;
	}



	static class BasicNameValuePair {
		private final String name;
		private final String value;

		public BasicNameValuePair(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return this.name;
		}

		public String getValue() {
			return this.value;
		}

		public String toString() {
			if (this.value == null) {
				return this.name;
			} else {
				int len = this.name.length() + 1 + this.value.length();
				StringBuilder buffer = new StringBuilder(len);
				buffer.append(this.name);
				buffer.append("=");
				buffer.append(this.value);
				return buffer.toString();
			}
		}
	}

	}





