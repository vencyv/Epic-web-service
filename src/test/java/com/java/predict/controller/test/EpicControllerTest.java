package com.java.predict.controller.test;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.java.predict.AppInitializor;
import com.java.predict.config.AppConfig;
import com.java.predict.controller.EpicControllerImpl;
import com.java.predict.service.EpicDataService;

@ContextConfiguration(classes={AppInitializor.class,AppConfig.class})
@WebMvcTest
public class EpicControllerTest extends AbstractTestNGSpringContextTests  {

	@Autowired
	MockMvc mockMvc;

	@SpyBean
	@Autowired
	EpicDataService epicDataService;

	@DataProvider(name = "epicDataProvider")
	public static Object[][] epicDataProvide()
	{
		return new Object[][]{
			{"{}", "ERROR-2001" },
			{"{ } ", "ERROR-2001" }
		};
	}

	@Test(dataProvider="epicDataProvider")
	public void testGetEpicData_InvalidInputParam(String inputParam, String expectedResult)
	{
		try {
			MvcResult mvcResult = mockMvc.perform(post("/epic/getEpicData")
					.contentType(MediaType.APPLICATION_JSON)
					.content(inputParam))
					.andReturn();
			String response = mvcResult.getResponse().getContentAsString();
			JSONObject jsonResponse = new JSONObject(response);
			Assert.assertEquals(jsonResponse.get("code").toString(), expectedResult);		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test()
	public void testGetEpicData_ValidInputParam()
	{
		String inputParam = "{\"API\": [{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2015/Clinical/Utility/GetPatientsOnSystemList/SystemList\",\"Name\": \"GetPatientsOnSystemList\","
				+ "\"Type\": \"RESTful\",\"MethodType\": \"GET\",\"Parameters\": [\"SystemListID\", \"SystemListIDType\", \"UserID\", \"UserIDType\"],\"ParametersValue\": [\"5922\", \"INTERNAL\", \"1\", \"EXTERNAL\"]}]}";
		String expectedResult = "{\"Patients\": [{\"Name\": \"Joan Grand Central\",\"Age\": \"67 y.o.\",\"Sex\": \"F\",\"PatientID\": [{\"PatientID\": \"E2615\",\"PatientIDType\": \"EPI\"},{\"PatientID\": \"Z4495\",\"PatientIDType\": \"EXTERNAL\"}]}]}";
		try {
			doReturn(expectedResult).when(epicDataService).getEpicData(inputParam);
			MvcResult mvcResult = mockMvc.perform(post("/epic/getEpicData")
					.contentType(MediaType.APPLICATION_JSON)
					.content(inputParam))
					.andReturn();
			Assert.assertEquals( mvcResult.getResponse().getContentAsString(), expectedResult);		
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	@Test(dataProvider="epicDataProvider")
	public void testupdateEpicData_InvalidInputParam(String inputParam, String expectedResult)
	{
		try {
			MvcResult mvcResult = mockMvc.perform(post("/epic/updateEpicData")
					.contentType(MediaType.APPLICATION_JSON)
					.content(inputParam))
					.andReturn();
			String response = mvcResult.getResponse().getContentAsString();
			JSONObject jsonResponse = new JSONObject(response);
			Assert.assertEquals(jsonResponse.get("code").toString(), expectedResult);	
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	@Test()
	public void testupdateEpicData_ValidInputParam()
	{
		String inputParam = "{\"API\": [{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2011/Clinical/Patient/ADDFLOWSHEETVALUE/FlowsheetValue\",\"Name\": \"ADDFLOWSHEETVALUE\","
				+ "\"Type\": \"RESTful\",\"MethodType\": \"POST\",\"Parameters\": [\"SystemListID\", \"SystemListIDType\", \"UserID\", \"UserIDType\"],\"ParametersValue\": [\"5922\", \"INTERNAL\", \"1\", \"EXTERNAL\"]}]}";
		String expectedResult = "{\"Patients\": [{\"Name\": \"Joan Grand Central\",\"Age\": \"67 y.o.\",\"Sex\": \"F\",\"PatientID\": [{\"PatientID\": \"E2615\",\"PatientIDType\": \"EPI\"},{\"PatientID\": \"Z4495\",\"PatientIDType\": \"EXTERNAL\"}]}]}";
		try {
			doReturn(expectedResult).when(epicDataService).updateEpicData(inputParam);
			MvcResult mvcResult = mockMvc.perform(post("/epic/updateEpicData")
					.contentType(MediaType.APPLICATION_JSON)
					.content(inputParam))
					.andReturn();
			String response = mvcResult.getResponse().getContentAsString();
			JSONObject jsonResponse = new JSONObject(response);
			Assert.assertEquals(jsonResponse.get("response").toString(), expectedResult);		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
