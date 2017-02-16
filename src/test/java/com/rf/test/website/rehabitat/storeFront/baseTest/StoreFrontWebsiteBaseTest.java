package com.rf.test.website.rehabitat.storeFront.baseTest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.HtmlLogger;
import com.rf.core.utils.PropertyFile;
import com.rf.core.utils.SoftAssert;
import com.rf.core.website.constants.TestConstants;
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
public class StoreFrontWebsiteBaseTest extends RFBaseTest {
	protected static RFWebsiteDriver driver = new RFWebsiteDriver(propertyFile);
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
	protected StoreFrontAboutMePage sfAboutMePage;

	StringBuilder verificationErrors = new StringBuilder();
	protected String password=null;
	protected String countryId=null;
	protected String country=null;
	protected boolean runBaseURLOrLogoutExecutionCode = true;

	protected static String conultantWithPulseAndWithCRP = null;
	protected static String conultantWithoutPulseAndWithoutCRP = null;
	protected static String conultantWithPulseAndWithCRPForCancellation = null;
	protected static String conultantHavingSponsorWithoutPWS = null;
	protected static String rcWithoutOrder = null;
	protected static String rcWithOrder = null;
	protected static String pcUserWithoutSponsor = null;
	protected static String pcUserWithPWSSponsor = null;
	protected static String pcUserWithoutPWSSponsor = null;
	protected static String pcUserHavingSingleBillingProfile = null;
	protected static String pwsPrefix = null;

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
	}

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){
		s_assert = new SoftAssert();
		setStoreFrontPassword(driver.getStoreFrontUserPassword());
		setCountry();
		setCountryId();
		checkAndCloseMoreThanOneWindows();
		driver.pauseExecutionFor(2000);
		if(sfHomePage.isWelcomeUserElementDisplayed()==true){
			sfHomePage.clickWelcomeDropdown();
			sfHomePage.logout();
		}
		navigateToStoreFrontBaseURL();
		if(sfHomePage.isWelcomeUserElementDisplayed()==true){
			sfHomePage.clickWelcomeDropdown();
			sfHomePage.logout();
		}
	}

	@AfterGroups(alwaysRun=true,groups="users")
	public void afterGroup() throws IOException{
		logger.info("After Group");
		userPropertyFile.loadProps(userProps);
		userPropertyFile.clearProperty();
		setUsers("conultantWithPulseAndWithCRP", conultantWithPulseAndWithCRP);
		setUsers("pwsPrefix", pwsPrefix);
		setUsers("conultantWithoutPulseAndWithoutCRP", conultantWithoutPulseAndWithoutCRP);
		setUsers("conultantWithPulseAndWithCRPForCancellation", conultantWithPulseAndWithCRPForCancellation);
		setUsers("conultantHavingSponsorWithoutPWS", conultantHavingSponsorWithoutPWS);
		setUsers("pcUserWithPWSSponsor", pcUserWithPWSSponsor);
		setUsers("pcUserWithoutSponsor", pcUserWithoutSponsor);
		setUsers("pcUserWithoutPWSSponsor", pcUserWithoutPWSSponsor);
		setUsers("pcUserHavingSingleBillingProfile", pcUserHavingSingleBillingProfile);
		setUsers("rcWithOrder", rcWithOrder);
		setUsers("rcWithoutOrder", rcWithoutOrder);
	}

	public void setUsers(String key,String value){
		if(value!=null)
			userPropertyFile.setAndWriteProperty(key, value,userProps);
	}

	public void checkAndCloseMoreThanOneWindows(){
		Set<String> allWindows = driver.getWindowHandles();
		String currentWin = driver.getWindowHandle();
		for(String win:allWindows){
			if(win.equals(currentWin)==false && allWindows.size()>1){
				driver.switchTo().window(win);
				driver.close();
				driver.switchTo().window(currentWin);
				allWindows = driver.getWindowHandles();							
			}	
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
		logger.info("Navigated to base URL");
	}

	public void navigateToStoreFrontBaseURLWithoutCountry(){
		driver.get(driver.getURL());
		logger.info("Navigated to base URL without country");
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

	public String getConultantWithPulseAndWithCRP(){
		return userPropertyFile.getProperty("conultantWithPulseAndWithCRP");
	}

	public String getConultantWithoutPulseAndWithoutCRP(){
		return userPropertyFile.getProperty("conultantWithPulseAndWithCRP");
	}
	
}
