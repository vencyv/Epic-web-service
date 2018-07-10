package com.java.predict.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.java.predict.utility.ErrorUtil;

/**
 * @author RuchitM
 * Service class to Get and Update epic data from 
 * Epic App Orchard
 */
@PropertySource(value={"classpath:constant.properties"})
@Service
public class EpicDataServiceImpl implements EpicDataService {

	@Value("${error.malformedInput}")
	private String malformedInput;
	@Value("${error.multipleApi}")
	private String multipleApi;
	@Value("${error.invalidURL}")
	private String invalidURL;
	@Value("${error.invalidType}")
	private String invalidType;
	@Value("${error.invalidMethodType}")
	private String invalidMethodType;
	@Value("${error.invalidParameterList}")
	private String invalidParameterList;
	@Value("${error.invalidParameterValueList}")
	private String invalidParameterValueList;
	@Value("${error.countMismatch}")
	private String countMismatch;
	@Value("${http.methodTypeGet}")
	private String methodTypeGet;
	@Value("${http.methodTypePost}")
	private String methodTypePost;
	@Value("${error.epicServiceError}")
	private String epicServiceError;
	@Value("${error.epicAppOrchardError}")
	private String epicAppOrchardError;
	@Value("${epic.success.response}")
	private String successResponse;
	@Value("${error.invalidApiName}")
	private String invalidApiName;
	//TODO: will come from env variable
	@Value("${error.azureKeyVaultError}")
	private String azureKeyVaultError;

	private String KEYVAULT_SERVICE_ENDPOINT = System.getenv("KEYVAULT_SERVICE_ENDPOINT");

	@Autowired
	private ErrorUtil errorUtil;

	private static final Logger logger = LogManager.getLogger(EpicDataServiceImpl.class);

	/* 
	 * This method will fetch epic data on the basis of 
	 * given Type in inputParam
	 * @param inputParam
	 * return epicData
	 * @throws JSONException,Exception
	 */
	@Override
	public String getEpicData(String inputParam) {

		logger.info("Enter getEpicData service");
		String epicData = "";
		try
		{
			JSONObject jsonObject = new JSONObject(inputParam);
			JSONArray jsonArray = jsonObject.getJSONArray("API");
			int jsonArrayLength = jsonArray.length();
			if(jsonArrayLength == 1)
			{			
				for(int i=0; i<jsonArrayLength; i++)
				{
					jsonObject = jsonArray.getJSONObject(i);
					String apiName = jsonObject.getString("Name").trim();
					if(!apiName.isEmpty() && apiName != null)
					{
						if("RESTful".equals(jsonObject.getString("Type")))
						{
							epicData = epicRestRequest(jsonObject);
						}
						else
						{
							String errorResponse = errorUtil.getEpicError("Invalid Type", invalidType);
							errorUtil.appendLog("getEpicData", "Error", errorResponse, "EpicDataServiceImpl");
							return errorResponse;
						}
					}
					else
					{
						String errorResponse = errorUtil.getEpicError("Invalid Name", invalidApiName);
						errorUtil.appendLog("getEpicData", "Error", errorResponse, "EpicDataServiceImpl");
						return errorResponse;
					}
				}
			}
			else
			{
				String errorResponse = errorUtil.getEpicError("Multiple Api in request body", multipleApi);
				errorUtil.appendLog("getEpicData", "Error", errorResponse, "EpicDataServiceImpl");
				return errorResponse;
			}
		}
		catch (JSONException jsonException)
		{
			logger.error("Malformed json input parameter: "+jsonException);
			String errorResponse = errorUtil.getEpicError(jsonException.getMessage(), malformedInput);
			errorUtil.appendLog("getEpicData", "Error", errorResponse, "EpicDataServiceImpl");
			return errorResponse;
		}
		catch (Exception exception) 
		{
			String errorResponse = errorUtil.getEpicError(exception.getMessage(), epicServiceError);
			errorUtil.appendLog("getEpicData", "Error", errorResponse, "EpicDataServiceImpl");
			return errorResponse;
		}
		logger.info("Fetched epic data: "+epicData);
		logger.info("Exit getEpicData service");

		return epicData;
	}

	/* 
	 * This method will update epic data on the basis of 
	 * given Type in inputParam
	 * @param inputParam
	 * return epicData
	 * @throws JSONException,Exception
	 */
	@Override
	public String updateEpicData(String inputParam) {

		logger.info("Enter updateEpicData");
		String response = "";
		try
		{
			JSONObject jsonObject = new JSONObject(inputParam);
			JSONArray jsonArray = jsonObject.getJSONArray("API");
			int jsonArrayLength = jsonArray.length();
			if(jsonArrayLength == 1)
			{			
				for(int i=0; i<jsonArrayLength; i++)
				{
					jsonObject = jsonArray.getJSONObject(i);
					String apiName = jsonObject.getString("Name").trim();
					if(!apiName.isEmpty() && apiName != null)
					{
						if("RESTful".equals(jsonObject.getString("Type")))
						{
							response = epicRestRequest(jsonObject);

						}
						else
						{
							String errorResponse = errorUtil.getEpicError("Invalid Type", invalidType);
							errorUtil.appendLog("updateEpicData", "Error", errorResponse, "EpicDataServiceImpl");
							return errorResponse;
						}
					}
					else
					{
						String errorResponse = errorUtil.getEpicError("Invalid Name", invalidApiName);
						errorUtil.appendLog("updateEpicData", "Error", errorResponse, "EpicDataServiceImpl");
						return errorResponse;
					}
				}
			}
			else
			{
				String errorResponse = errorUtil.getEpicError("Multiple Api in request body", multipleApi);
				errorUtil.appendLog("updateEpicData", "Error", errorResponse, "EpicDataServiceImpl");
				return errorResponse;
			}
		}
		catch (JSONException jsonException){
			String errorResponse = errorUtil.getEpicError(jsonException.getMessage(), malformedInput);
			errorUtil.appendLog("updateEpicData", "Error", errorResponse, "EpicDataServiceImpl");
			return errorResponse;
		}
		catch (Exception exception) {
			String errorResponse = errorUtil.getEpicError(exception.getMessage(), epicServiceError);
			errorUtil.appendLog("updateEpicData", "Error", errorResponse, "EpicDataServiceImpl");
			return errorResponse;
		}
		logger.info("Exit updateEpicData");
		return response;
	}

	/**
	 * This method will fetch epic data for s
	 * Epic App Orchard rest request 
	 * @param jsonObject
	 * @return epicRequestData
	 * @throws JSONException,MalformedURLException,IOException,Exception
	 */
	public String epicRestRequest(JSONObject jsonObject)
	{
		logger.info("Enter epicRestRequest");
		JSONObject jsonResponse = new JSONObject();

		try
		{
			URL url;		
			StringBuilder requestURL = new StringBuilder();
			if(!jsonObject.getString("URL").trim().isEmpty())
			{
				requestURL.append(jsonObject.getString("URL"));
			}
			else
			{
				String errorResponse = errorUtil.getEpicError("Invalid URL", invalidURL);
				errorUtil.appendLog("epicRestRequest", "Error", errorResponse, "EpicDataServiceImpl");
				return errorResponse;
			}
			String methodType = jsonObject.getString("MethodType");
			JSONArray requestParamArr = jsonObject.getJSONArray("Parameters");
			JSONArray requestParamValueArr = jsonObject.getJSONArray("ParametersValue"); 
			JSONObject requestBody = new JSONObject();
			int requestParamArrLength = requestParamArr.length();
			int requestParamValueArrLength = requestParamValueArr.length();
			if(requestParamArrLength == requestParamValueArrLength)
			{
				if(methodTypeGet.equalsIgnoreCase(methodType))
				{
					requestURL.append("?");
					for(int i=0; i<requestParamArrLength; i++)
					{
						if(!requestParamArr.getString(i).trim().isEmpty())
						{
							requestURL.append(requestParamArr.getString(i).trim()+"=");
							if(!requestParamValueArr.getString(i).trim().isEmpty())
							{
								requestURL.append(requestParamValueArr.getString(i).trim());
							}
							else
							{
								String errorResponse = errorUtil.getEpicError("Invalid ParametersValue", invalidParameterValueList);
								errorUtil.appendLog("epicRestRequest", "Error", errorResponse, "EpicDataServiceImpl");
								return errorResponse;
							}
							//if(i < requestParamArr.length())
							//{
							requestURL.append("&");
							//	}
						}
						else
						{
							String errorResponse = errorUtil.getEpicError("Invalid Parameters", invalidParameterList);
							errorUtil.appendLog("epicRestRequest", "Error", errorResponse, "EpicDataServiceImpl");
							return errorResponse;
						}
					}

				}
				else if(methodTypePost.equalsIgnoreCase(methodType))
				{
					//Temporary only to make call for AddFlowSheet and awaiting response from Chris
					requestURL.append("?");
					for(int i=0; i<requestParamArrLength; i++)
					{
						if(!requestParamArr.getString(i).trim().isEmpty())
						{
							requestURL.append(requestParamArr.getString(i).trim()+"=");
							Object obj = requestParamValueArr.get(i);
							if(obj instanceof JSONObject && ((JSONObject) obj).length()!=0)
							{
								requestURL.append(requestParamValueArr.getJSONObject(i).toString().trim());
							}
							else if(obj instanceof JSONArray && ((JSONArray) obj).length()!=0)
							{
								requestURL.append(requestParamValueArr.getJSONArray(i).toString().trim());
							}
							else if(obj instanceof String && !requestParamValueArr.getString(i).trim().isEmpty())
							{
								requestURL.append(requestParamValueArr.getString(i).trim());
							}
							else
							{
								String errorResponse = errorUtil.getEpicError("Invalid ParametersValue", invalidParameterValueList);
								errorUtil.appendLog("epicRestRequest", "Error", errorResponse, "EpicDataServiceImpl");
								return errorResponse;
							}
							//if(i < requestParamArr.length()-1)
							//{
							requestURL.append("&");
							//	}
						}
						else
						{
							String errorResponse = errorUtil.getEpicError("Invalid Parameters", invalidParameterList);
							errorUtil.appendLog("epicRestRequest", "Error", errorResponse, "EpicDataServiceImpl");
							return errorResponse;
						}
					}
					for(int i=0; i<requestParamArrLength; i++)
					{
						Object obj = requestParamValueArr.get(i);
						if(obj instanceof JSONObject && ((JSONObject) obj).length()!=0)
						{
							requestBody.put(requestParamArr.getString(i), requestParamValueArr.getJSONObject(i));
						}
						else if(obj instanceof JSONArray && ((JSONArray) obj).length()!=0)
						{
							requestBody.put(requestParamArr.getString(i), requestParamValueArr.getJSONArray(i));
						}
						else if(obj instanceof String && !requestParamValueArr.getString(i).trim().isEmpty())
						{
							requestBody.put(requestParamArr.getString(i), requestParamValueArr.getString(i).trim());
						}
					}
				}
				else
				{
					String errorResponse = errorUtil.getEpicError("Invalid MethodType", invalidMethodType);
					errorUtil.appendLog("epicRestRequest", "Error", errorResponse, "EpicDataServiceImpl");
					return errorResponse;
				}
			}
			else
			{
				String errorResponse = errorUtil.getEpicError("Parameters and ParametersValue count mismatch", countMismatch);
				errorUtil.appendLog("epicRestRequest", "Error", errorResponse, "EpicDataServiceImpl");
				return errorResponse;
			}
			String authSecret = "";
			try
			{
				authSecret = getSecretFromKeyVault();
			}
			catch (Exception exception) {
				logger.info(exception.getStackTrace());
				String errorResponse = errorUtil.getEpicError(exception.getMessage(), azureKeyVaultError);
				errorUtil.appendLog("epicRestRequest", "Error", errorResponse, "EpicDataServiceImpl");
				return errorResponse;
			}
			logger.info("azure key vault secret:"+authSecret);
			try
			{
				url = new URL(requestURL.toString());
				HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
				con.setRequestProperty("Authorization", authSecret);
				con.setRequestMethod(methodType);
				con.setRequestProperty("Content-Type", "application/json");
				con.setDoOutput(true);
				con.setDoInput(true);
				if(methodType.equals("POST"))
				{
					OutputStream os = (OutputStream) con.getOutputStream();
					os.write(requestBody.toString().getBytes());
				}
				String responseData = getDataFromStream(con);
				if(!responseData.isEmpty() && responseData != null)
				{
					jsonResponse.put("status", "success");
					jsonResponse.put("code", successResponse);
					jsonResponse.put("response", responseData);		
				}
			}
			catch (MalformedURLException malformedURLException)
			{
				String errorResponse = errorUtil.getEpicError("EpicError: "+malformedURLException.getMessage(), epicAppOrchardError);
				errorUtil.appendLog("epicRestRequest", "Error", errorResponse, "EpicDataServiceImpl");
				return errorResponse;
			}
			catch (IOException ioException) 
			{
				String errorResponse = errorUtil.getEpicError("EpicError: "+ioException.getMessage(), epicAppOrchardError);
				errorUtil.appendLog("epicRestRequest", "Error", errorResponse, "EpicDataServiceImpl");
				return errorResponse;
			}
			catch(Exception exception)
			{
				String errorResponse = errorUtil.getEpicError("EpicError: "+exception.getMessage(), epicAppOrchardError);
				errorUtil.appendLog("epicRestRequest", "Error", errorResponse, "EpicDataServiceImpl");
				return errorResponse;
			}

		}
		catch (Exception exception) 
		{
			String errorResponse = errorUtil.getEpicError(exception.getMessage(), epicServiceError);
			errorUtil.appendLog("epicRestRequest", "Error", errorResponse, "EpicDataServiceImpl");
			return errorResponse;
		}	
		logger.info("Exit epicRestRequest");
		return jsonResponse.toString();
	}

	//TODO error implementation for azure key vault
	/**
	 * This method will fetch the Epic App Orchard authorization secret 
	 * from azure key vault
	 * @return authSecret
	 * @throws MalformedURLException, IOException
	 * @throws JSONException 
	 */
	public String getSecretFromKeyVault() throws MalformedURLException, IOException, JSONException
	{
		logger.info("Enter getSecretFromKeyVault");
		String authSecret = "";
		RestTemplate restTemplate = new RestTemplate();
		authSecret = restTemplate.getForObject(KEYVAULT_SERVICE_ENDPOINT, String.class);
		JSONObject jsonObject = new JSONObject(authSecret);
		authSecret = jsonObject.getString("response");
		return authSecret;
	}

	/**
	 * This method will fetch the request data on the given
	 * connection(HttpsURLConnection) 
	 * @param HttpsURLConnection con
	 * @return responseData
	 * @throws IOException
	 */
	public String getDataFromStream(HttpsURLConnection con) throws IOException{

		logger.info("Enter getDataFromStream");
		StringBuilder responseData = new StringBuilder();
		if(con!=null){
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String  tempData;
			while ((tempData = br.readLine()) != null){
				responseData.append(tempData);
			}
			br.close();
		}
		logger.info("Exit getDataFromStream");
		return responseData.toString();
	}

}
