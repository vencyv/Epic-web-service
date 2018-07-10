package com.java.predict.controller;

/**
 * @author RuchitM
 * Get and update Epic data into Epic App Orchard  
 */
public interface EpicController {

	/**
	 * Get the epic data from Epic App Orchard 
	 * @param inputParam
	 * @return epicData
	 */
	public String getEpicData(String inputParam);
	
	
	/**
	 *  Get the epic data into Epic App Orchard 
	 * @param inputParam
	 * @return 
	 */
	public String updateEpicData(String inputParam);
}
