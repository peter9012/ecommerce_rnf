package com.rf.test.website.rehabitat.storeFront.baseTest;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.HtmlLogger;
import com.rf.core.utils.SoftAssert;
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
public class StoreFrontWebsiteBaseTest extends RFBaseTest {

	/***
	 * @author Shubham Mathur
	 * @description StoreFrontWebsiteBaseTest constructor having StoreFront HomePage initialization
	 */
	public StoreFrontWebsiteBaseTest() {
		sfHomePage = new StoreFrontHomePage(driver);
	}

	/***
	 * All page class reference declarations
	 */
	protected StoreFrontHomePage sfHomePage;
	//protected StoreFrontConsultantEnrollNowPage sfConsEnrollNowPage;
	protected StoreFrontShopSkinCarePage sfShopSkinCarePage;
	protected StoreFrontCartPage sfCartPage;
	protected StoreFrontCheckoutPage sfCheckoutPage;
	protected StoreFrontAccountInfoPage sfAccountInfoPage;
	protected StoreFrontShippingInfoPage sfShippingInfoPage;
	protected StoreFrontAutoshipStatusPage sfAutoshipStatusPage;
	protected StoreFrontBillingInfoPage sfBillingInfoPage;
	protected StoreFrontOrdersPage sfOrdersPage;
	protected StoreFrontProductDetailPage sfProductDetailPage;
	protected StoreFrontAutoshipCartPage sfAutoshipCartPage;

	StringBuilder verificationErrors = new StringBuilder();
	protected String password=null;
	protected String countryId=null;
	protected String country=null;
	protected boolean runBaseURLOrLogoutExecutionCode = true;

	protected RFWebsiteDriver driver = new RFWebsiteDriver(propertyFile);

	private static final Logger logger = LogManager
			.getLogger(StoreFrontWebsiteBaseTest.class.getName());

	/**
	 * @throws Exception
	 *             setup function loads the driver and environment and baseUrl
	 *             for the tests
	 */
	@BeforeSuite(alwaysRun=true)
	public void setUp() throws Exception {
		driver.loadApplication();
		driver.setDBConnectionString();
		//		setCountry();
		//		setCountryId();
		//		setStoreFrontPassword(driver.getStoreFrontUserPassword());
	}

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){
		s_assert = new SoftAssert();
		setStoreFrontPassword(driver.getStoreFrontUserPassword());
		setCountry();
		setCountryId();
		navigateToStoreFrontBaseURL();
		if(sfHomePage.isWelcomeUserElementDisplayed()==true){
			sfHomePage.clickWelcomeDropdown();
			sfHomePage.logout();
		}
	}

	public void setCountry(){
		country = driver.getCountry();
	}

	public String getCountry(){
		return country;
	}

	public void setCountryId(){
		if(country.equalsIgnoreCase("ca"))
			countryId = "40";
		else if(country.equalsIgnoreCase("us"))
			countryId = "236";		
	}

	public void navigateToStoreFrontBaseURL(){
		driver.get(driver.getURL()+"/"+country.toUpperCase());
	}

	public void closeCurrentWindow(){
		driver.close();
		logger.info("Current widow closed");
	}

	public void openTheBrowserAndApplication(){
		try {
			driver.loadApplication();
			navigateToStoreFrontBaseURL();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Application launched");
	}

	//	public void logout(){
	//		driver.click(WELCOME_DROPDOWN_LOC);
	//		logger.info("Welcome dropdown clicked");
	//		driver.pauseExecutionFor(2000);
	//		driver.click(SIGN_OUT_LOC);
	//		driver.waitForPageLoad();
	//	}

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

}
