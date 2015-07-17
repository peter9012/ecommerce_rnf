package com.rf.core.driver;

import java.net.MalformedURLException;

public interface RFDriver {

	/**
	 * @author Shubham Mathur RFDriver enforces subclasses to implement
	 * loadApplicaiton method
	 * @throws MalformedURLException 
	 */
	
	public void loadApplication() throws MalformedURLException;
	
}
 