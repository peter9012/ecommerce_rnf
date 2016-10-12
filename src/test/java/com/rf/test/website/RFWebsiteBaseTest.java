package com.rf.test.website;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.HtmlLogger;
import com.rf.core.utils.SoftAssert;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.test.base.RFBaseTest;

/**
 * @author ShubhamMathur RFWebsiteBaseTest is the super class for all
 *         desktop Test classes initializes the driver is used for execution.
 *
 */
public class RFWebsiteBaseTest extends RFBaseTest {
	StringBuilder verificationErrors = new StringBuilder();
	protected String password = null;
	protected String countryId = null;
	protected boolean runBaseURLOrLogoutExecutionCode = true;
	private Actions actions;

	protected RFWebsiteDriver driver = new RFWebsiteDriver(propertyFile);
	private static final Logger logger = LogManager
			.getLogger(RFWebsiteBaseTest.class.getName());

	/**
	 * @throws Exception
	 *             setup function loads the driver and environment and baseUrl
	 *             for the tests
	 */
	@BeforeSuite(alwaysRun=true)
	public void setUp() throws Exception {
		driver.loadApplication();
		logger.info("Application loaded");                                                            
		driver.setDBConnectionString();
	}

	public void navigateToStoreFrontBaseURL(){
		String country = driver.getCountry();
		driver.get(driver.getURL()+"/"+country);
	}

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){
		s_assert = new SoftAssert();
		String country = driver.getCountry();
		System.out.println("Country="+country);
		System.out.println("driver="+driver.getURL());
		if(driver.getURL().contains("cscockpit")||driver.getURL().contains("salesforce")==true){                 
			driver.get(driver.getURL());
		}
		else{
			driver.get(driver.getURL()+"/"+country);			
		}
		if(driver.getURL().contains("cscockpit")==true||driver.getURL().contains("salesforce")==true){  

		}else{
//			try{
//				logout();		
//			}catch(NoSuchElementException e){
//
//			} 
			if(isLogoutBtnPresent())
				logout();
		}	
		if(country.equalsIgnoreCase("ca"))
			countryId = "40";
		else if(country.equalsIgnoreCase("us"))
			countryId = "236";            
		if(driver.getURL().contains("cscockpit")==false && (driver.getURL().contains("salesforce")==false && driver.getCurrentUrl().contains(country)==false)){
			driver.selectCountry(country);
		}
		setStoreFrontPassword(driver.getStoreFrontPassword());
		//		if(driver.getURL().contains("salesforce")==true){
		//			try{
		//				crmLogoutFromHome();
		//				driver.get(driver.getURL());
		//			}catch(Exception e){
		//
		//			}
		//		}
	}

	@AfterMethod
	public void tearDownAfterMethod(){
		driver.manage().deleteAllCookies();
		if(driver.getURL().contains("salesforce")==true){
			try{
				crmLogout();
				Alert alert = driver.switchTo().alert();
				System.out.println(alert.getText());
				alert.dismiss();

			}catch(NoAlertPresentException Ex){

			}
		}
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

	public void crmLogout(){
		driver.switchTo().defaultContent();
		driver.quickWaitForElementPresent(By.id("userNavLabel"));
		driver.click(By.id("userNavLabel"));
		driver.waitForElementPresent(By.id("app_logout"));
		driver.click(By.id("app_logout"));
		logger.info("Logout");
		driver.pauseExecutionFor(3000);
	}

	public void crmLogoutFromHome(){
		driver.switchTo().defaultContent();
		driver.quickWaitForElementPresent(By.id("userNavLabel"));
		driver.click(By.id("userNavLabel"));
		driver.waitForElementPresent(By.xpath("//a[@title='Logout']"));
		driver.click(By.xpath("//a[@title='Logout']"));
		logger.info("Logout");
	}

	public void logout(){
		StoreFrontHomePage storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		try
		{
			driver.waitForElementPresent(By.id("account-info-button"));
			driver.click(By.id("account-info-button"));
			logger.info("Your account info has been clicked");
			driver.waitForElementPresent(By.linkText("Log out"));
			driver.click(By.linkText("Log out"));
			logger.info("Logout");
			driver.waitForPageLoad();
			driver.pauseExecutionFor(3000);
		}
		catch(Exception e)
		{

		}

	}

	public boolean isLogoutBtnPresent(){
		int listSize = driver.findElements(By.id("account-info-button")).size();
		if(listSize>0)
			return true;
		else
			return false;
	}

	// This assertion for the UI Texts
	public void assertTrue(String message, boolean condition) {
		if (!condition) {
			logger.info("[FUNCTIONAL FAILURE - ASSERTION ERROR ----------- "
					+ message + "]");                                              
			Assert.fail(message);                                                      
		}

	}


	//This assertion for the Database validations
	public boolean assertTrueDB(String message, boolean condition,String dbName) {
		if (!condition) {
			logger.info("[DATABASE ASSERTION FAILURE -  "+dbName+" ----------- " +message + "]");
			if(!dbName.equals(driver.getDBNameRFL())){
				Assert.fail(message);
			}
			else{
				return false;
			}
		}
		return true;
	}

	public void assertTrue(boolean condition, String message) {

		if (!condition) {
			logger.info("[FUNCTIONAL FAILURE - ASSERTION ERROR ----------- "
					+ message + "]");
			Assert.fail(message);
		}
	}

	public void assertEquals(Object obj1, Object obj2, String message) {
		if (!obj1.equals(obj2)) {
			logger.info("[FUNCTIONAL FAILURE - ASSERTION ERROR ----------- "
					+ message + "]");
			Assert.fail(message);
		}
	}

	public boolean assertEqualsDB(Object obj1, Object obj2, String message,String dbName) {
		if (!obj1.equals(obj2)) {
			logger.info("[DATABASE ASSERTION FAILURE -  "+dbName+" ----------- " +message + "]");
			if(!dbName.equals(driver.getDBNameRFL())){
				Assert.fail(message);
			}              
			else{
				return false;
			}
		}
		return true;                        
	}

	public boolean assertEqualsDB(String message, int num1,int num2,String dbName) {
		if (!(num1==num2)) {
			logger.info("[RFL DATABASE ASSERTION FAILURE -  "+message + "]");
			if(!dbName.equals(driver.getDBNameRFL())){
				Assert.fail(message);
			}
			else{
				return false;
			}
		}
		return true;
	}

	public void assertEquals(String message, int num1,int num2) {
		if (!(num1==num2)) {
			logger.info("[FUNCTIONAL FAILURE - ASSERTION ERROR ----------- "
					+ message + "]");
			Assert.fail(message);
		}
	}

	public void assertFalse(boolean condition, String message) {

		if (condition) {
			logger.info("[FUNCTIONAL FAILURE - ASSERTION ERROR ----------- "
					+ message + "]");
			Assert.fail(message);
		}
	}

	public void assertFalse(String message, boolean condition) {

		if (condition) {
			logger.info("[FUNCTIONAL FAILURE - ASSERTION ERROR ----------- "
					+ message + "]");
			Assert.fail(message);
		}
	}

	//            public void assertTrue(boolean condition) {
	//
	//                            try {
	//                                            assertTrue(condition);
	//
	//                            } catch (Exception e) {
	//                                            logger.trace(e.getMessage());
	//                            }
	//            }

	public void assertEquals(String message, float num1,float num2) {

		if (!(num1==num2)) {
			logger.info("[FUNCTIONAL FAILURE - ASSERTION ERROR ----------- "
					+ message + "]");
			Assert.fail(message);
		}
	}


	public Object getValueFromQueryResult(List<Map<String, Object>> userDataList,String column){
		Object value = null;
		for (Map<String, Object> map : userDataList) {

			//logger.info("query result:" + map.get(column));

			//            logger.info("query result:" + map.get(column));

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
