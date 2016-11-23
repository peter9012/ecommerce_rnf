package com.rf.pages.website.rehabitat.storeFront.basePage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Reporter;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.RFBasePage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontConsultantEnrollNowPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontHomePage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontShopSkinCarePage;

public class StoreFrontWebsiteBasePage extends RFBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontWebsiteBasePage.class.getName());

	protected RFWebsiteDriver driver;

	public StoreFrontWebsiteBasePage(RFWebsiteDriver driver){		
		super(driver);
		this.driver = driver;
	}
	/**
	 * Top Navigation Links
	 */
	private final By BECOME_A_CONSULTANT_LOC = By.xpath("//div[@class='navbar-inverse']//*[@title='BECOME A CONSULTANT']");
	private final By ENROLL_NOW_LOC = By.xpath("//div[@class='navbar-inverse']//a[@title='ENROLL NOW']");
	private final By WHY_RF_LOC = By.xpath("//div[@class='navbar-inverse']//a[@title='WHY R+F']");
	private final By SHOP_SKINCARE_LOC = By.xpath("//div[@class='navbar-inverse']//a[@title='SHOP SKINCARE']");
	private final By ALL_PRODUCTS = By.xpath("//div[@class='navbar-inverse']//a[@title='ALL PRODUCTS']");
	private final By RODAN_AND_FIELDS_LOGO = By.id("header-logo");
	private final By FIND_A_CONSULTANT_LINK_LOC = By.xpath("//a[@title='FIND A CONSULTANT']");
	protected final By SPONSOR_SEARCH_FIELD_LOC = By.id("sponserparam");
	private final By SEARCH_SPONSOR_LOC = By.id("search-sponsor-button");
	private final By SELECT_AND_CONTINUE_LOC= By.xpath("//div[@id='findConsultantResultArea']/descendant::button[text()='Select and Continue'][1]");
	private final By SPONSOR_SEARCH_RESULTS_LOC = By.xpath("//div[@class='row']/div[contains(@class,'consultant-box')]");
	private final By NO_RESULT_FOUND_MSG_LOC = By.xpath("//p[contains(text(),'No results found')]");
	private String activePageLoc  = "//span[contains(text(),'%s')]/parent::li";
	private String navigationPageNumberLoc = "//ul[@class='pagination']//a[contains(text(),'%s')]";
	
	private String RFO_DB = null;

	/***
	 * This method do the mouseHover on desired webElement
	 * 
	 * @param sponsor
	 * @return
	 * 
	 */
	public StoreFrontWebsiteBasePage mouseHoverOn(String element){
		if(element.equalsIgnoreCase(TestConstants.BECOME_A_CONSULTANT)){
			driver.moveToElement(BECOME_A_CONSULTANT_LOC);
			logger.info("mouseHovered on 'Become A Consultant'");	
		}
		else if(element.equalsIgnoreCase(TestConstants.SHOP_SKINCARE)){
			driver.moveToElement(SHOP_SKINCARE_LOC);
			logger.info("mouseHovered on 'Shop Skincare'");	
		}
		return this;
	}

	/***
	 * This method clicks on the Enroll Now link in Top Navigation
	 * 
	 * @param
	 * @return
	 * 
	 */
	public StoreFrontConsultantEnrollNowPage clickOnEnrollNow(){
		mouseHoverOn(TestConstants.BECOME_A_CONSULTANT);
		driver.click(ENROLL_NOW_LOC);
		logger.info("clicked on 'Enroll Now'");
		return new StoreFrontConsultantEnrollNowPage(driver);
	}

	/***
	 * This method clicks on the 'Why R+F' link in Top Navigation
	 * 
	 * @param
	 * @return
	 * 
	 */
	public StoreFrontWebsiteBasePage clickOnWhyRF(){
		mouseHoverOn(TestConstants.BECOME_A_CONSULTANT);
		driver.click(WHY_RF_LOC);
		logger.info("clicked on 'Why R+F'");
		return this;
	}

	public StoreFrontShopSkinCarePage clickOnAllProducts(){
		mouseHoverOn(TestConstants.SHOP_SKINCARE);
		driver.click(ALL_PRODUCTS);
		logger.info("clicked on 'All Products'");
		return new StoreFrontShopSkinCarePage(driver);
	}

	/***
	 * This method enter the sponsor name and click on search button
	 * 
	 * @param 
	 * @return current URL
	 * 
	 */
	public String getCurrentURL(){
		String currentURL = driver.getCurrentUrl();
		logger.info("Current URL is "+currentURL);
		return currentURL;
	}

	/***
	 * This method clicks on R+F logo
	 * 
	 * @param 
	 * @return BasePage Object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickOnRodanAndFieldsLogo(){
		driver.click(RODAN_AND_FIELDS_LOGO);
		logger.info("Rodan and Fields logo clicked");
		return this;
	}

	/***
	 * This method clicks on the Find A Consultant Link on Home Page
	 * 
	 * @param 
	 * @return BasePage Object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickOnFindAConsultantLinkOnHomePage(){
		driver.quickWaitForElementPresent(FIND_A_CONSULTANT_LINK_LOC);
		driver.click(FIND_A_CONSULTANT_LINK_LOC);
		logger.info("'Find a consutant' link clicked");
		return this;
	}
	
	/***
	 * This method enter the sponsor name and click on search button
	 * 
	 * @param sponsor
	 * @return
	 * 
	 */
	public StoreFrontWebsiteBasePage searchSponsor(String sponsor){
		driver.type(SPONSOR_SEARCH_FIELD_LOC, sponsor);
		logger.info("Entered sponsor as "+sponsor);
		driver.click(SEARCH_SPONSOR_LOC);
		logger.info("Clicked on 'Search' button");
		return this;
	}
	
	/***
	 * This method selects the first sponsor name in the search result.
	 * 
	 * @param sponsor
	 * @return
	 * 
	 */
	public void selectFirstSponsorFronList(){
		driver.click(SELECT_AND_CONTINUE_LOC);
		logger.info("Clicked on 'Select And Continue' button for first result");
	}
	
	/***
	 * This method verifies whether any result has been present after searching
	 * the sponsor or not.
	 * 
	 * @return boolean
	 * 
	 */
	public boolean isSponsorPresentInResult(){
		return driver.isElementPresent(SPONSOR_SEARCH_RESULTS_LOC);
	}
	
	/***
	 * This method verifies if the 'No results found' message is displayed or not 
	 * @return boolean
	 */
	public boolean isNoResultMessagePresent(){
		return driver.isElementPresent(NO_RESULT_FOUND_MSG_LOC);
	}
	
	/***
	 * This method verifies if the user is on expected navigation page in the sponsor 
	 * search result. 
	 * @return boolean
	 */
	public Boolean isTheUserOnNavigationPage(String pageNo){
		return driver.getAttribute(By.xpath(String.format(activePageLoc, pageNo)),"class").equals("active");
	}
	
	/***
	 * This method naviagtes the user to the desired navigation page in the sponsor
	 * search result 
	 * @return boolean
	 */
	public StoreFrontWebsiteBasePage navigateToPaginationInSponsorSearchResult(String pageNo){
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath(String.format(navigationPageNumberLoc, pageNo))));
		return this;
	}


}