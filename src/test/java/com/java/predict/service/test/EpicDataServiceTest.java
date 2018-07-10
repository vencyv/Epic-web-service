package com.java.predict.service.test;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.java.predict.AppInitializor;
import com.java.predict.service.EpicDataServiceImpl;

@ContextConfiguration(classes = {AppInitializor.class})
@WebAppConfiguration
public class EpicDataServiceTest extends AbstractTestNGSpringContextTests  {

	@Autowired
	private EpicDataServiceImpl epicDataServiceImpl;

	@DataProvider(name = "epicGetDataProvider")
	public static Object[][] epicGetDataProvide()
	{
		return new Object[][]{
			{"{\"API\": [{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2015/Clinical/Utility/GetPatientsOnSystemList/SystemList\",\"Name\": \"GetPatientsOnSystemList\""
					+ "\"Type\": \"RESTful\",\"MethodType\": \"GET\",\"Parameters\": [\"SystemListID\", \"SystemListIDType\", \"UserID\", \"UserIDType\"],\"ParametersValue\": [\"5922\", \"INTERNAL\", \"1\", \"EXTERNAL\"]}]}", "ERROR-2002" },
			{"{\"API\": [{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2015/Clinical/Utility/GetPatientsOnSystemList/SystemList\",\"Name\": \"GetPatientsOnSystemList\","
					+ "\"Type\": \"RESTful\",\"MethodType\": \"GET\",\"Parameters\": [\"SystemListID\", \"SystemListIDType\", \"UserID\", \"UserIDType\"],\"ParametersValue\": [\"5922\", \"INTERNAL\", \"1\", \"EXTERNAL\"]},{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2015/Clinical/Utility/GetPatientsOnSystemList/SystemList\",\"Name\": \"GetPatientsOnSystemList\","
					+ "\"Type\": \"RESTful\",\"MethodType\": \"GET\",\"Parameters\": [\"SystemListID\", \"SystemListIDType\", \"UserID\", \"UserIDType\"],\"ParametersValue\": [\"5922\", \"INTERNAL\", \"1\", \"EXTERNAL\"]}]}", "ERROR-2003"},
			{"{\"API\": [{\"URL\": \"\",\"Name\": \"GetPatientsOnSystemList\","
					+ "\"Type\": \"RESTful\",\"MethodType\": \"GET\",\"Parameters\": [\"SystemListID\", \"SystemListIDType\", \"UserID\", \"UserIDType\"],\"ParametersValue\": [\"5922\", \"INTERNAL\", \"1\", \"EXTERNAL\"]}]}", "ERROR-2004"},
			{"{\"API\": [{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2015/Clinical/Utility/GetPatientsOnSystemList/SystemList\",\"Name\": \"GetPatientsOnSystemList\","
					+ "\"Type\": \" \",\"MethodType\": \"GET\",\"Parameters\": [\"SystemListID\", \"SystemListIDType\", \"UserID\", \"UserIDType\"],\"ParametersValue\": [\"5922\", \"INTERNAL\", \"1\", \"EXTERNAL\"]}]}", "ERROR-2005"},
			{"{\"API\": [{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2015/Clinical/Utility/GetPatientsOnSystemList/SystemList\",\"Name\": \"GetPatientsOnSystemList\","
					+ "\"Type\": \"AAA\",\"MethodType\": \"GET\",\"Parameters\": [\"SystemListID\", \"SystemListIDType\", \"UserID\", \"UserIDType\"],\"ParametersValue\": [\"5922\", \"INTERNAL\", \"1\", \"EXTERNAL\"]}]}", "ERROR-2005"},
			{"{\"API\": [{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2015/Clinical/Utility/GetPatientsOnSystemList/SystemList\",\"Name\": \"GetPatientsOnSystemList\","
					+ "\"Type\": \"RESTful\",\"MethodType\": \"\",\"Parameters\": [\"SystemListID\", \"SystemListIDType\", \"UserID\", \"UserIDType\"],\"ParametersValue\": [\"5922\", \"INTERNAL\", \"1\", \"EXTERNAL\"]}]}", "ERROR-2006"},
			{"{\"API\": [{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2015/Clinical/Utility/GetPatientsOnSystemList/SystemList\",\"Name\": \"GetPatientsOnSystemList\","
					+ "\"Type\": \"RESTful\",\"MethodType\": \"AAA\",\"Parameters\": [\"SystemListID\", \"SystemListIDType\", \"UserID\", \"UserIDType\"],\"ParametersValue\": [\"5922\", \"INTERNAL\", \"1\", \"EXTERNAL\"]}]}", "ERROR-2006"},
			{"{\"API\": [{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2015/Clinical/Utility/GetPatientsOnSystemList/SystemList\",\"Name\": \"GetPatientsOnSystemList\","
					+ "\"Type\": \"RESTful\",\"MethodType\": \"GET\",\"Parameters\": [\"SystemListID\", \"SystemListIDType\", \"UserID\", \"\"],\"ParametersValue\": [\"5922\", \"INTERNAL\", \"1\", \"EXTERNAL\"]}]}", "ERROR-2007"},
			{"{\"API\": [{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2015/Clinical/Utility/GetPatientsOnSystemList/SystemList\",\"Name\": \"GetPatientsOnSystemList\","
					+ "\"Type\": \"RESTful\",\"MethodType\": \"GET\",\"Parameters\": [\"SystemListID\", \"SystemListIDType\", \"UserID\", \"UserIDType\"],\"ParametersValue\": [\"5922\", \"INTERNAL\", \"1\", \"\"]}]}", "ERROR-2008"},
			{"{\"API\": [{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2015/Clinical/Utility/GetPatientsOnSystemList/SystemList\",\"Name\": \"GetPatientsOnSystemList\","
					+ "\"Type\": \"RESTful\",\"MethodType\": \"GET\",\"Parameters\": [\"SystemListID\", \"SystemListIDType\", \"UserID\"],\"ParametersValue\": [\"5922\", \"INTERNAL\", \"1\", \"EXTERNAL\"]}]}", "ERROR-2009"}
		};
	}

	@Test(dataProvider = "epicGetDataProvider")
	public void testGetEpicData(String inputParam, String expectedResult)
	{
		try {
			String response = epicDataServiceImpl.getEpicData(inputParam);
			JSONObject jsonResponse = new JSONObject(response);
			Assert.assertEquals(jsonResponse.get("code").toString(), expectedResult);
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@DataProvider(name = "epicUpdateDataProvider")
	public static Object[][] epicUpdateDataProvide()
	{
		return new Object[][]{
			{"{\"API\": [{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2011/Clinical/Patient/ADDFLOWSHEETVALUE/FlowsheetValue\",\"Name\": \"ADDFLOWSHEETVALUE\""
					+ "\"Type\": \"RESTful\",\"MethodType\": \"POST\",\"Parameters\": [\"PatientID\",\"PatientIDType\",\"ContactID\",\"ContactIDType\",\"UserID\",\"UserIDType\",\"FlowsheetID\",\"FlowsheetIDType\",\"Value\",\"InstantValueTaken\",\"FlowsheetTemplateID\",\"FlowsheetTemplateIDType\"],\"ParametersValue\":  [\"E2734\", \"EPI\", \"1852\", \"CSN\",\"1\", \"EXTERNAL\", \"10\",\"INTERNAL\", \"110\", \"2016-01-21T10:00:00Z\",\"81\", \"INTERNAL\"]}]}", "ERROR-2002" },
			{"{\"API\": [{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2011/Clinical/Patient/ADDFLOWSHEETVALUE/FlowsheetValue\",\"Name\": \"GetPatientsOnSystemList\","
					+ "\"Type\": \"RESTful\",\"MethodType\": \"POST\",\"Parameters\": [\"PatientID\",\"PatientIDType\",\"ContactID\",\"ContactIDType\",\"UserID\",\"UserIDType\",\"FlowsheetID\",\"FlowsheetIDType\",\"Value\",\"InstantValueTaken\",\"FlowsheetTemplateID\",\"FlowsheetTemplateIDType\"],\"ParametersValue\":  [\"E2734\", \"EPI\", \"1852\", \"CSN\",\"1\", \"EXTERNAL\", \"10\",\"INTERNAL\", \"110\", \"2016-01-21T10:00:00Z\",\"81\", \"INTERNAL\"]},{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2011/Clinical/Patient/ADDFLOWSHEETVALUE/FlowsheetValue\",\"Name\": \"ADDFLOWSHEETVALUE\","
					+ "\"Type\": \"RESTful\",\"MethodType\": \"POST\",\"Parameters\": [\"PatientID\",\"PatientIDType\",\"ContactID\",\"ContactIDType\",\"UserID\",\"UserIDType\",\"FlowsheetID\",\"FlowsheetIDType\",\"Value\",\"InstantValueTaken\",\"FlowsheetTemplateID\",\"FlowsheetTemplateIDType\"],\"ParametersValue\":  [\"E2734\", \"EPI\", \"1852\", \"CSN\",\"1\", \"EXTERNAL\", \"10\",\"INTERNAL\", \"110\", \"2016-01-21T10:00:00Z\",\"81\", \"INTERNAL\"]}]}", "ERROR-2003"},
			{"{\"API\": [{\"URL\": \"\",\"Name\": \"ADDFLOWSHEETVALUE\","
					+ "\"Type\": \"RESTful\",\"MethodType\": \"POST\",\"Parameters\": [\"PatientID\",\"PatientIDType\",\"ContactID\",\"ContactIDType\",\"UserID\",\"UserIDType\",\"FlowsheetID\",\"FlowsheetIDType\",\"Value\",\"InstantValueTaken\",\"FlowsheetTemplateID\",\"FlowsheetTemplateIDType\"],\"ParametersValue\":  [\"E2734\", \"EPI\", \"1852\", \"CSN\",\"1\", \"EXTERNAL\", \"10\",\"INTERNAL\", \"110\", \"2016-01-21T10:00:00Z\",\"81\", \"INTERNAL\"]}]}", "ERROR-2004"},
			{"{\"API\": [{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2011/Clinical/Patient/ADDFLOWSHEETVALUE/FlowsheetValue\",\"Name\": \"ADDFLOWSHEETVALUE\","
					+ "\"Type\": \" \",\"MethodType\": \"POST\",\"Parameters\": [\"PatientID\",\"PatientIDType\",\"ContactID\",\"ContactIDType\",\"UserID\",\"UserIDType\",\"FlowsheetID\",\"FlowsheetIDType\",\"Value\",\"InstantValueTaken\",\"FlowsheetTemplateID\",\"FlowsheetTemplateIDType\"],\"ParametersValue\":  [\"E2734\", \"EPI\", \"1852\", \"CSN\",\"1\", \"EXTERNAL\", \"10\",\"INTERNAL\", \"110\", \"2016-01-21T10:00:00Z\",\"81\", \"INTERNAL\"]}]}", "ERROR-2005"},
			{"{\"API\": [{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2011/Clinical/Patient/ADDFLOWSHEETVALUE/FlowsheetValue\",\"Name\": \"ADDFLOWSHEETVALUE\","
					+ "\"Type\": \"AAA\",\"MethodType\": \"POST\",\"Parameters\": [\"PatientID\",\"PatientIDType\",\"ContactID\",\"ContactIDType\",\"UserID\",\"UserIDType\",\"FlowsheetID\",\"FlowsheetIDType\",\"Value\",\"InstantValueTaken\",\"FlowsheetTemplateID\",\"FlowsheetTemplateIDType\"],\"ParametersValue\":  [\"E2734\", \"EPI\", \"1852\", \"CSN\",\"1\", \"EXTERNAL\", \"10\",\"INTERNAL\", \"110\", \"2016-01-21T10:00:00Z\",\"81\", \"INTERNAL\"]}]}", "ERROR-2005"},
			{"{\"API\": [{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2011/Clinical/Patient/ADDFLOWSHEETVALUE/FlowsheetValue\",\"Name\": \"ADDFLOWSHEETVALUE\","
					+ "\"Type\": \"RESTful\",\"MethodType\": \"\",\"Parameters\": [\"PatientID\",\"PatientIDType\",\"ContactID\",\"ContactIDType\",\"UserID\",\"UserIDType\",\"FlowsheetID\",\"FlowsheetIDType\",\"Value\",\"InstantValueTaken\",\"FlowsheetTemplateID\",\"FlowsheetTemplateIDType\"],\"ParametersValue\":  [\"E2734\", \"EPI\", \"1852\", \"CSN\",\"1\", \"EXTERNAL\", \"10\",\"INTERNAL\", \"110\", \"2016-01-21T10:00:00Z\",\"81\", \"INTERNAL\"]}]}", "ERROR-2006"},
			{"{\"API\": [{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2011/Clinical/Patient/ADDFLOWSHEETVALUE/FlowsheetValue\",\"Name\": \"ADDFLOWSHEETVALUE\","
					+ "\"Type\": \"RESTful\",\"MethodType\": \"AAA\",\"Parameters\": [\"PatientID\",\"PatientIDType\",\"ContactID\",\"ContactIDType\",\"UserID\",\"UserIDType\",\"FlowsheetID\",\"FlowsheetIDType\",\"Value\",\"InstantValueTaken\",\"FlowsheetTemplateID\",\"FlowsheetTemplateIDType\"],\"ParametersValue\":  [\"E2734\", \"EPI\", \"1852\", \"CSN\",\"1\", \"EXTERNAL\", \"10\",\"INTERNAL\", \"110\", \"2016-01-21T10:00:00Z\",\"81\", \"INTERNAL\"]}]}", "ERROR-2006"},
			{"{\"API\": [{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2011/Clinical/Patient/ADDFLOWSHEETVALUE/FlowsheetValue\",\"Name\": \"ADDFLOWSHEETVALUE\","
					+ "\"Type\": \"RESTful\",\"MethodType\": \"POST\",\"Parameters\":  [\"PatientID\",\"PatientIDType\",\"ContactID\",\"ContactIDType\",\"UserID\",\"UserIDType\",\"FlowsheetID\",\"FlowsheetIDType\",\"Value\",\"InstantValueTaken\",\"FlowsheetTemplateID\",\"\"],\"ParametersValue\":  [\"E2734\", \"EPI\", \"1852\", \"CSN\",\"1\", \"EXTERNAL\", \"10\",\"INTERNAL\", \"110\", \"2016-01-21T10:00:00Z\",\"81\", \"INTERNAL\"]}]}", "ERROR-2007"},
			{"{\"API\": [{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2011/Clinical/Patient/ADDFLOWSHEETVALUE/FlowsheetValue\",\"Name\": \"ADDFLOWSHEETVALUE\","
					+ "\"Type\": \"RESTful\",\"MethodType\": \"POST\",\"Parameters\": [\"PatientID\",\"PatientIDType\",\"ContactID\",\"ContactIDType\",\"UserID\",\"UserIDType\",\"FlowsheetID\",\"FlowsheetIDType\",\"Value\",\"InstantValueTaken\",\"FlowsheetTemplateID\",\"FlowsheetTemplateIDType\"],\"ParametersValue\":  [\"E2734\", \"EPI\", \"1852\", \"CSN\",\"1\", \"EXTERNAL\", \"10\",\"INTERNAL\", \"110\", \"2016-01-21T10:00:00Z\",\"81\", \"\"]}]}", "ERROR-2008"},
			{"{\"API\": [{\"URL\": \"https://apporchard.epic.com/interconnect-ao85prd-username/api/epic/2011/Clinical/Patient/ADDFLOWSHEETVALUE/FlowsheetValue\",\"Name\": \"ADDFLOWSHEETVALUE\","
					+ "\"Type\": \"RESTful\",\"MethodType\": \"POST\",\"Parameters\": [\"SystemListID\", \"SystemListIDType\", \"UserID\"],\"ParametersValue\":  [\"E2734\", \"EPI\", \"1852\", \"CSN\",\"1\", \"EXTERNAL\", \"10\",\"INTERNAL\", \"110\", \"2016-01-21T10:00:00Z\",\"81\", \"INTERNAL\"]}]}", "ERROR-2009"}
		};
	}

	@Test(dataProvider = "epicUpdateDataProvider")
	public void testUpdateEpicData(String inputParam, String expectedResult)
	{
		try {
		String response = epicDataServiceImpl.updateEpicData(inputParam);
		JSONObject jsonResponse = new JSONObject(response);
		Assert.assertEquals(jsonResponse.get("code").toString(), expectedResult);
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
