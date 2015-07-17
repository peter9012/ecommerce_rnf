/**
 * 
 */
package com.rf.core.utils;

import java.util.Map;

//import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.asserts.IAssert;
import org.testng.collections.Maps;

public class SoftAssert extends org.testng.asserts.SoftAssert {

	private final Map<AssertionError, IAssert>	m_errors	= Maps.newHashMap();
//	private static WebDriver					driver;

//	public SoftAssert(WebDriver driver) {
//		this.driver = driver;
//	}

	@Override
	public void executeAssert(IAssert a)
	{
		try {
			a.doAssert();
		} catch (AssertionError ex) {
			m_errors.put(ex, a);
			onAssertFailure(a, ex);
		}
	}

	@Override
	public void assertAll()
	{
		if (!m_errors.isEmpty()) {
			StringBuilder sb = new StringBuilder("[FUNCTIONAL FAILURE - ASSERTION ERROR ----------- ");
			boolean first = true;
			for (Map.Entry<AssertionError, IAssert> ae : m_errors.entrySet()) {
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}
				sb.append(ae.getValue().getMessage());
			}

			throw new AssertionError(sb.toString());
		}
	}

	@Override
	public void onAfterAssert(IAssert a)
	{
//		Reporter.log("Expected: " + a.getExpected());
//		Reporter.log("Actual: " + a.getActual());
//		Reporter.log("");
		super.onAfterAssert(a);
	}

	@Override
	public void onBeforeAssert(IAssert a)
	{
		//Reporter.log("Test Case Desciption: " + a.getMessage());
	}

	@Override
	public void onAssertFailure(IAssert a, AssertionError ex)
	{
		try {
			Reporter.log("Test Case Desciption: " + a.getMessage());
			Reporter.log("TEST Case Failed ->" + ex.getMessage());
			Reporter.log("Expected: " + a.getExpected());
			Reporter.log("Actual: " + a.getActual());
			Reporter.log("");
			//String sScreenshotPath= driver.takeSnapShotAndRetPath();
			//Reporter.log("Snapshot Path :<a href='" + sScreenshotPath + "'>"+ sScreenshotPath+"</a>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public boolean isEqual(int iExpected, int iActual)
	{
		boolean flag = false;

		if (iExpected == iActual) {
			flag = true;
		}
		return flag;
	}
}