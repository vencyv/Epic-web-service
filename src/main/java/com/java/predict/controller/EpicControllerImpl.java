package com.java.predict.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.java.predict.service.EpicDataService;
import com.java.predict.utility.ErrorUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author RuchitM
 * Controller class to Get and Update epic data 
 * from Epic App Orchard
 */
@RestController
@PropertySource(value={"classpath:constant.properties"})
@Api(value="EpicController", description="REST APIs related to Epic App Orchard")
public class EpicControllerImpl implements EpicController  {

	@Autowired
	EpicDataService epicDataService;
	@Autowired
	private ErrorUtil errorUtil;

	@Value("${error.emptyRequestParam}")
	private String emptyRequestParam;
	@Value("${error.malformedInput}")
	private String malformedInput;
	@Value("${error.epicServiceError}")
	private String epicServiceError;

	private static final Logger logger = LogManager.getLogger(EpicControllerImpl.class);

	/* 
	 * This method will accept the post request to get epic data
	 * and call the service to fetch the data from Epic App Orachard 
	 * on the basis of given input parameters 
	 * @Param inputParam
	 */
	//TODO value will come from config.properties file
	@ApiOperation(value="Get patient data from Epic App Orchard", httpMethod="POST", tags="getEpicData",consumes="application/json",produces="application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "inputParam", required = true, dataType = "application/json", paramType = "body")
	})
	@PostMapping(path="/epic/getEpicData", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String getEpicData(@RequestBody String inputParam) {

		logger.info("Enter getEpicData controller");
		logger.info("GetEpicData input parameter: "+inputParam);
		String epicData = "";
		try
		{
			if(!inputParam.trim().isEmpty() && inputParam != null && new JSONObject(inputParam).length() != 0)
			{
				epicData = epicDataService.getEpicData(inputParam);
			}
			else
			{
				//TODO value will come from config.properties file
				String errorResponse = errorUtil.getEpicError("Invalid or empty request parameter", emptyRequestParam);
				errorUtil.appendLog("getEpicData", "Error", errorResponse, "EpicControllerImpl");
				return errorResponse;
			}
			errorUtil.pushLogIntoLoggingservice();
		}
		catch (JSONException jsonException)
		{
			logger.error("Malformed json input parameter: "+jsonException);
			String errorResponse = errorUtil.getEpicError(jsonException.getMessage(), malformedInput);
			errorUtil.appendLog("getEpicData", "Error", errorResponse, "EpicControllerImpl");
			errorUtil.pushLogIntoLoggingservice();
			return errorResponse;
		}
		catch (Exception exception) 
		{
			String errorResponse = errorUtil.getEpicError(exception.getMessage(), epicServiceError);
			errorUtil.appendLog("getEpicData", "Error", errorResponse, "EpicControllerImpl");
			errorUtil.pushLogIntoLoggingservice();
			return errorResponse;
		}

		logger.info("Exit getEpicData controller");
		return epicData;
	}

	/* 
	 * This method will accept the post request to update epic data
	 * and call the service to update the data into Epic App Orachard 
	 * on the basis of given input parameters 
	 * @Param inputParam
	 */
	@ApiOperation(value="Update patient data to Epic App Orchard", httpMethod="POST", tags="updateEpicData",consumes="application/json",produces="application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "inputParam", required = true, dataType = "application/json", paramType = "body")
	})
	@PostMapping(path="/epic/updateEpicData", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String updateEpicData(@RequestBody String inputParam) {

		logger.info("Enter updateEpicData controller");
		logger.info("UpdateEpicData input parameter: "+inputParam);
		String response = "";
		try
		{
			if(!inputParam.trim().isEmpty() && inputParam != null && new JSONObject(inputParam).length() != 0)
			{
				response = epicDataService.updateEpicData(inputParam);
			}
			else
			{
				String errorResponse = errorUtil.getEpicError("Invalid or empty request parameter", emptyRequestParam);
				errorUtil.appendLog("updateEpicData", "Error", errorResponse, "EpicControllerImpl");
				return errorResponse;
			}
			errorUtil.pushLogIntoLoggingservice();
		}
		catch (JSONException jsonException)
		{
			logger.error("Malformed json input parameter: "+jsonException);
			String errorResponse = errorUtil.getEpicError(jsonException.getMessage(), malformedInput);
			errorUtil.appendLog("updateEpicData", "Error", errorResponse, "EpicControllerImpl");
			errorUtil.pushLogIntoLoggingservice();
			return errorResponse;
		}
		catch (Exception exception)
		{
			String errorResponse = errorUtil.getEpicError(exception.getMessage(), epicServiceError);
			errorUtil.appendLog("updateEpicData", "Error", errorResponse, "EpicControllerImpl");
			errorUtil.pushLogIntoLoggingservice();
			return errorResponse;
		}

		logger.info("Exit updateEpicData controller");
		return response;
	}
}
