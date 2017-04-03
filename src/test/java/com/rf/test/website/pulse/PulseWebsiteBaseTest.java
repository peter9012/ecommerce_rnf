package com.rf.test.website.pulse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.HtmlLogger;
import com.rf.core.utils.SoftAssert;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.pulse.PulseHomePage;
import com.rf.pages.website.pulse.PulseLoginPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontAboutMePage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontAutoshipCartPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontAutoshipStatusPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontCartPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontCheckoutPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontConsultantEnrollNowPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontHomePage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontProductDetailPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontShippingInfoPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontShopSkinCarePage;
import com.rf.test.base.RFBaseTest;

/**
 * @author ShubhamMathur StoreFrontWebsiteBaseTest is the super class for all
 *         desktop Test classes initializes the driver is used for execution.
 *
 */
public class PulseWebsiteBaseTest extends RFBaseTest {
	protected static RFWebsiteDriver driver = new RFWebsiteDriver(propertyFile);
	
	/***
	 * @author Shubham Mathur
	 * @description PulseWebsiteBaseTest constructor having StoreFront HomePage initialization
	 */
	public PulseWebsiteBaseTest() {
		pulseLoginPage = new PulseLoginPage(driver);		
	}

	/***
	 * All page class reference declarations
	 */
	protected PulseLoginPage pulseLoginPage;
	protected PulseHomePage pulseHomePage;

	//StringBuilder verificationErrors = new StringBuilder();
	protected String password=null;
	protected String countryId=null;
	protected String country=null;
	protected boolean runBaseURLOrLogoutExecutionCode = true;

	private static final Logger logger = LogManager
			.getLogger(PulseWebsiteBaseTest.class.getName());

	/**
	 * @throws Exception
	 *             setup function loads the driver and environment and baseUrl
	 *             for the tests
	 */
	@BeforeSuite(alwaysRun=true)
	public void setUp() throws Exception {
		userPropertyFile.loadProps(userProps);
		driver.loadApplication();
	}

	public void navigateToPulseBaseURL(){
		driver.get(driver.getURL()+"/Account/LogOn");
		logger.info("Navigated to pulse base URL");
		driver.waitForPageLoad();
		//	driver.pauseExecutionFor(2000);
	}

	/**
	 * @throws Exception
	 */
	@AfterSuite(alwaysRun = true)
	public void tearDown() throws Exception {
		new HtmlLogger().createHtmlLogFile();                 
		driver.quit();
	}

	public void setStoreFrontPassword(String pass){
		password=pass;
	}

	public Object getValueFromQueryResult(List<Map<String, Object>> userDataList,String column){
		Object value = null;
		for (Map<String, Object> map : userDataList) {
			value = map.get(column);                                            
		}
		logger.info("Data returned by query: "+ value);
		return value;
	}

	public List<String> getValuesFromQueryResult(List<Map<String, Object>> userDataList,String column){
		List<String> allReturnedValuesFromQuery = new ArrayList<String>();
		Object value = null;
		for (Map<String, Object> map : userDataList) {
			logger.info("query result:" + map.get(column));
			value = map.get(column);
			allReturnedValuesFromQuery.add(String.valueOf(value));
		}
		return allReturnedValuesFromQuery;
	}

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws Exception{
		s_assert = new SoftAssert();
		setStoreFrontPassword(driver.getStoreFrontUserPassword());
		navigateToPulseBaseURL();
	//	pulseLoginPage.logout();
	}
	
	@AfterMethod
	public void logout(){
		
	}
}
