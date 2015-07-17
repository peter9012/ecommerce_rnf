package com.rf.core.listeners;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import com.rf.core.driver.website.RFWebsiteDriver;

/**
 * @author ShubhamMathur TestListner is the listener class that listens to test
 *         case execution and allows Engineer to complete post test operations.
 *
 */

public class TestListner extends TestListenerAdapter {

	private static final Logger logger = LogManager.getLogger(TestListner.class
			.getName());


	@Override
	public void onTestStart(ITestResult tr) {
		System.out.println("test starts "+tr.getName());


		//		testIds = qmetryID(tr);
		//		testIds = qmetryID(tr);
		//		for (String test : testIds)
		//			logger.info("[TC-" + test + " ------- Executing "
		//					+ tr.getMethod().getMethodName() + "]"
		//					+ "<br>DESCRIPTION :" + tr.getMethod().getDescription());
	}

	@Override
	public void onTestSuccess(ITestResult tr) {

		//		testIds = qmetryID(tr);
		//		testIds = qmetryID(tr);
		//		for (String test : testIds)
		//			logger.info("[TEST IS SUCCESSFUL -------- Test case " + test
		//					+ " passed]");
	}

	@Override
	public void onTestFailure(ITestResult testResult) {
		if (testResult.getStatus() == ITestResult.FAILURE){
			
			try {
				RFWebsiteDriver.takeSnapShotAndRetPath(RFWebsiteDriver.driver, testResult.getMethod());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}

	@Override
	public void onTestSkipped(ITestResult tr) {
		//		testIds = qmetryID(tr);
		//		for (String test : testIds)
		//			logger.info("[TEST IS SKIPPED -------- Test case " + test
		//					+ " skipped]");
	}

}
