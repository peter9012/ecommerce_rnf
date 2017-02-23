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
import org.testng.ITestContext;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
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

	protected String email=null;
	protected String firstName = null;
	protected String lastName = null;
	protected String addressLine1 = null;
	protected String addressLine2 = null;
	protected String city = null;
	protected String state = null;
	protected String postalCode = null;
	protected String phoneNumber = null;
	protected String cardType = null;
	protected String cardNumber = null;
	protected String cardName = null;
	protected String CVV = null;
	protected String timeStamp=null;
	protected String randomWords = null;

	protected String updatedAddressLine1 = null;
	protected String updatedAddressLine2 = null;
	protected String updatedCity = null;
	protected String updatedPostalCode = null;
	protected String stateAbbreviation = null;

	/***
	 * @author Shubham Mathur
	 * @description StoreFrontWebsiteBaseTest constructor having StoreFront HomePage initialization
	 */
	public StoreFrontWebsiteBaseTest() {
		setCountry();
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

	protected static String consultantWithPulseAndWithCRP = null;
	protected static String consultantWithoutPulseAndWithoutCRP = null;
	protected static String consultantWithPulseAndWithCRPForCancellation = null;
	protected static String consultantHavingSponsorWithoutPWS = null;
	protected static String rcWithoutOrder = null;
	protected static String rcWithOrderWithoutSponsor = null;
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
	public void setUp(ITestContext context) throws Exception {
		userPropertyFile.loadProps(userProps);
		if(getSuiteName(context).equalsIgnoreCase("storeFrontMiniRegressionSuite")){
			userPropertyFile.clearProperty();
		}
		driver.loadApplication();
		driver.setDBConnectionString();
	}

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){
		s_assert = new SoftAssert();
		setStoreFrontPassword(driver.getStoreFrontUserPassword());
		checkAndCloseMoreThanOneWindows();
		navigateToStoreFrontBaseURL();
		if(sfHomePage.isWelcomeUserElementDisplayed()==true){
			sfHomePage.clickWelcomeDropdown();
			sfHomePage.logout();
		}
		navigateToStoreFrontBaseURL();	
	}

	@BeforeClass(alwaysRun=true)
	public void setAddressDetailsAsPerCountry(){
		setCountry();
		setCountryId();
		String countryName = getCountry();
		firstName = TestConstants.FIRST_NAME;
		phoneNumber = TestConstants.PHONE_NUMBER;
		cardType = TestConstants.CARD_TYPE;
		cardNumber = TestConstants.CARD_NUMBER;
		cardName = TestConstants.CARD_NAME;
		CVV = TestConstants.CVV;
		if(countryName.equalsIgnoreCase("us")){
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			addressLine2 = TestConstants.ADDRESS_LINE_2_US;
			city = TestConstants.CITY_US;
			state = TestConstants.STATE_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			updatedAddressLine1 = TestConstants.SECOND_ADDRESS_LINE_1_US;
			updatedAddressLine2 = TestConstants.SECOND_ADDRESS_LINE_2_US;
			updatedCity = TestConstants.SECOND_CITY_US;
			updatedPostalCode = TestConstants.SECOND_POSTAL_CODE_US;
			stateAbbreviation = TestConstants.STATE_US_ABBREVIATION;
		}
		else if(countryName.equalsIgnoreCase("ca")){
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			addressLine2 = TestConstants.ADDRESS_LINE_2_CA;
			city = TestConstants.CITY_CA;
			state = TestConstants.STATE_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			updatedAddressLine1 = TestConstants.SECOND_ADDRESS_LINE_1_CA;
			updatedAddressLine2 = TestConstants.SECOND_ADDRESS_LINE_2_CA;
			updatedCity = TestConstants.SECOND_CITY_CA;
			updatedPostalCode = TestConstants.SECOND_POSTAL_CODE_CA;
			stateAbbreviation = TestConstants.STATE_CA_ABBREVIATION;
		}
	}

	//	@AfterGroups(alwaysRun=true,groups="users")
	//	public void afterGroup() throws IOException{
	//		logger.info("After Group");
	//		userPropertyFile.loadProps(userProps);
	//		setUsers("consultantWithPulseAndWithCRP", consultantWithPulseAndWithCRP);
	//		setUsers("pwsPrefix", pwsPrefix);
	//		setUsers("consultantWithoutPulseAndWithoutCRP", consultantWithoutPulseAndWithoutCRP);
	//		//setUsers("consultantWithPulseAndWithCRPForCancellation", consultantWithPulseAndWithCRPForCancellation);
	//		//		setUsers("consultantHavingSponsorWithoutPWS", consultantHavingSponsorWithoutPWS);
	//		setUsers("pcUserWithPWSSponsor", pcUserWithPWSSponsor);
	//		//		setUsers("pcUserWithoutSponsor", pcUserWithoutSponsor);
	//		//		setUsers("pcUserWithoutPWSSponsor", pcUserWithoutPWSSponsor);
	//		//		setUsers("pcUserHavingSingleBillingProfile", pcUserHavingSingleBillingProfile);
	//		setUsers("rcWithOrder", rcWithOrder);
	//		//		setUsers("rcWithoutOrder", rcWithoutOrder);
	//	}

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
		driver.waitForPageLoad();
		driver.pauseExecutionFor(2000);
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

	public String consultantWithPulseAndWithCRP(){
		return userPropertyFile.getProperty("consultantWithPulseAndWithCRP");
	}

	public String consultantWithoutPulseAndWithoutCRP(){
		return userPropertyFile.getProperty("consultantWithoutPulseAndWithoutCRP");
	}

	public String pcUserWithPWSSponsor(){
		return userPropertyFile.getProperty("pcUserWithPWSSponsor");
	}

	public String pwsPrefix(){
		return userPropertyFile.getProperty("pwsPrefix");
	}

	public String rcWithOrderWithoutSponsor(){
		return userPropertyFile.getProperty("rcWithOrderWithoutSponsor");
	}

	public static String getSuiteName(ITestContext context){
		String suiteName = context.getCurrentXmlTest().getSuite().getName();
		return suiteName;
	}

}
