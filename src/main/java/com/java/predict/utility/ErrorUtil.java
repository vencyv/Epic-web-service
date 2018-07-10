package com.java.predict.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author RuchitM
 * Utility Class to return error response 
 */
@Component
public class ErrorUtil 
{
	private static final Logger logger = LogManager.getLogger(ErrorUtil.class);
	private JSONArray logArray = new JSONArray();
	
	private String LOGGING_URL = System.getenv("LOGGING_URL");
	private String TIMEZONE = System.getenv("TIMEZONE");
	
	/**
	 * This method will return the error response
	 * @param exceptionMsg
	 * @param errorCode
	 * @return
	 */
	public String getEpicError(String exceptionMsg,String errorCode)
	{
		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("status", "error");
			jsonObject.put("code", errorCode);
			jsonObject.put("response", exceptionMsg);
		}
		catch(JSONException e)
		{
		//throw 	
		}
		return jsonObject.toString();
	}
	
	/* 
	 * Method to append all logs
	 * @Param methodName, type, message
	 */
	public void appendLog(String methodName, String type, String message, String className)
	{
		logger.info("Enter appendLog:");
		JSONObject logJsonObject = new JSONObject();
		try {
			logJsonObject.put("Microservice", "Epic-web-service");
			logJsonObject.put("Class", className);
			logJsonObject.put("Method", methodName);
			logJsonObject.put("Status", type);
			logJsonObject.put("Message", new JSONObject(message));
			logJsonObject.put("DateTime", getBatchDateTime());
			logArray.put(logJsonObject);
			logger.info("Exit appendLog :"+ logJsonObject.toString());
		} catch (JSONException e) {
			// TODO:
			logger.info("Exception in appendLog: "+ e.getMessage());
		}
	}

	/* 
	 * Method to push log into Logging Service
	 */
	public void pushLogIntoLoggingservice()
	{
		logger.info("Enter pushLogIntoLoginservice:");
		String response = " ";
		JSONObject logJsonObject = new JSONObject();
		try
		{
			logJsonObject.put("Logs", logArray);
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(logJsonObject.toString(), headers);
			//TODO: logging response
			response = restTemplate.postForObject(LOGGING_URL, entity, String.class);
			logArray = new JSONArray();
			logger.info("Exit pushLogIntoLogingservice:");
		}
		catch(Exception e)
		{
			// TODO:
			logger.info("Exception in pushLogIntoLoggingservice(): "+ e.getMessage());
		}
	}
	
	/* 
	 * Method to get batch time in given time zone format
	 */
	public String getBatchDateTime()
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZZZZ");
		Date datetime = new Date();
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
		String batchDateTime = simpleDateFormat.format(datetime);
		return batchDateTime;
	}


	
}
