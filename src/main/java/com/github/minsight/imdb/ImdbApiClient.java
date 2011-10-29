package com.github.minsight.imdb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.github.minsight.model.ImdbEntry;

public class ImdbApiClient {

	HttpClient httpclient = new DefaultHttpClient();

	public HttpClient getHttpClient() {
		return this.httpclient;
	}

	public ImdbEntry getMovieInfo(String movieName) {

		ImdbEntry movieInfo = null;
		try {
			movieInfo = retrieveMovieInfo(movieName);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("MovieInfo Retrieved:" + movieInfo);
		return movieInfo;
	}

	private ImdbEntry retrieveMovieInfo(String movieName) throws IOException,
			ClientProtocolException, JsonParseException, JsonMappingException {
		HttpGet httpget = new HttpGet("http://www.imdbapi.com/?t=" + movieName);

		System.out.println("executing request " + httpget.getURI());

		// Create a response handler
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = getHttpClient().execute(httpget, responseHandler);
		System.out.println("----------------------------------------");
		System.out.println(responseBody);
		System.out.println("----------------------------------------");

		ObjectMapper mapper = new ObjectMapper();
		ImdbEntry imdbEntry = mapper.readValue(responseBody, ImdbEntry.class);
		return imdbEntry;
	}

	public List<ImdbEntry> getMoviesInfo(List<String> movieNames) {

		List<ImdbEntry> imdbEntries = new ArrayList<ImdbEntry>();

		for (String movieName : movieNames) {
			imdbEntries.add(this.getMovieInfo(movieName));
		}

		return imdbEntries;
	}
}