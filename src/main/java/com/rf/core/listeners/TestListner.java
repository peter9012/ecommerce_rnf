package com.rf.core.listeners;


import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import com.rf.core.driver.website.RFWebsiteDriver;

/**
 * @author ShubhamMathur TestListner is the listener class that listens to test
 *         case execution and allows Engineer to complete post test operations.
 *
 */

public class TestListner implements ITestListener {

	private static final Logger logger = LogManager.getLogger(TestListner.class
			.getName());


	@Override
	public void onTestStart(ITestResult tr) {
		logger.info("\n******************************************************************************************************************************"+
				"\n\t\t\t\t\tTEST CASE NAME:                  "+ tr.getMethod().getMethodName()+
				"\n******************************************************************************************************************************");
	}

	@Override
	public void onTestSuccess(ITestResult tr) {
		logger.info("[TEST IS SUCCESSFUL -------- Test case " + tr.getMethod().getMethodName()+ " has passed]");
	}

	@Override
	public void onTestFailure(ITestResult tr) {
		if (tr.getStatus() == ITestResult.FAILURE){
			logger.info("[TEST HAS FAILED-------- Test case " + tr.getMethod().getMethodName()+" has failed]");
			try {
				RFWebsiteDriver.takeSnapShotAndRetPath(RFWebsiteDriver.driver, tr.getMethod().getMethodName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}

	@Override
	public void onTestSkipped(ITestResult tr) {
		logger.info("[TEST IS SKIPPED -------- Test case " + tr.getMethod().getMethodName()	+ " skipped]");
	}

	@Override
	public void onFinish(ITestContext context) { 
		Iterator<ITestResult> listOfFailedTests = context.getFailedTests().getAllResults().iterator();
		while (listOfFailedTests.hasNext()) {
			ITestResult failedTest = listOfFailedTests.next();
			ITestNGMethod method = failedTest.getMethod();
			if (context.getFailedTests().getResults(method).size() > 1) {
				listOfFailedTests.remove();
			} else {
				if (context.getPassedTests().getResults(method).size() > 0) {
					listOfFailedTests.remove();
				}
			}
		}
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub

	}

}
