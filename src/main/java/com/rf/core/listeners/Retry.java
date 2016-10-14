package com.rf.core.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * 
 * @author Shubham Mathur
 * 
 */
public class Retry implements IRetryAnalyzer{
	private static final Logger logger = LogManager
			.getLogger(Retry.class.getName());

	private int count =0;
	private int maxRetryCount = 1;

	@Override
	public boolean retry(ITestResult result) {
		if(count<maxRetryCount){
			count++;
			logger.info("RETRYING TEST............"+result.getMethod().getMethodName());
			System.out.println("RETRYING TEST............"+result.getMethod().getMethodName());
			return true;
		}
		return false;
	}

}