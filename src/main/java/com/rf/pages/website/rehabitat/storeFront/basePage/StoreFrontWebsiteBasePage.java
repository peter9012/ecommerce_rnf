package com.rf.pages.website.rehabitat.storeFront.basePage;

import java.util.Iterator;
import java.util.Set;

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
	String topNavigationLoc = "//div[contains(@class,'navbar-inverse')]";
	/**
	 * Top Navigation Links
	 */
	private final By BECOME_A_CONSULTANT_LOC = By.xpath(topNavigationLoc+"//*[@title='BECOME A CONSULTANT']");
	private final By ENROLL_NOW_LOC = By.xpath(topNavigationLoc+"//a[@title='ENROLL NOW']");
	private final By WHY_RF_LOC = By.xpath(topNavigationLoc+"//a[@title='WHY R+F']");
	private final By SHOP_SKINCARE_LOC = By.xpath(topNavigationLoc+"//a[@title='SHOP SKINCARE']");
	private final By ALL_PRODUCTS_LOC = By.xpath(topNavigationLoc+"//a[@title='ALL PRODUCTS']");
	private final By RODAN_AND_FIELDS_LOGO_LOC = By.id("header-logo");
	private final By FIND_A_CONSULTANT_LINK_LOC = By.xpath("//a[@title='FIND A CONSULTANT']");
	protected final By SPONSOR_SEARCH_FIELD_LOC = By.id("sponserparam");
	protected final By PRODUCTS_NAME_LINK_LOC = By.xpath("//div[@id='product_listing']/descendant::div[@class='details'][1]//a");
	private final By SEARCH_SPONSOR_LOC = By.id("search-sponsor-button");
	private final By SELECT_AND_CONTINUE_LOC= By.xpath("//div[@id='findConsultantResultArea']/descendant::button[text()='Select and Continue'][1]");
	private final By SPONSOR_SEARCH_RESULTS_LOC = By.xpath("//div[@class='row']/div[contains(@class,'consultant-box')]");
	private final By NO_RESULT_FOUND_MSG_LOC = By.xpath("//p[contains(text(),'No results found')]");
	private final By EVENTS_LOC = By.xpath(topNavigationLoc+"//a[@title='EVENTS']");
	private final By PROGRAMS_AND_INCENTIVES_LOC = By.xpath(topNavigationLoc+"//a[@title='PROGRAMS & INCENTIVES']");
	private final By MEET_OUR_COMMUNITY_LOC = By.xpath(topNavigationLoc+"//a[@title='MEET OUR COMMUNITY']");
	private final By WHO_WE_ARE_LINK_LOC = By.xpath(topNavigationLoc+"//a[@title='WHO WE ARE']");
	private final By GIVING_BACK_LINK_LOC = By.xpath(topNavigationLoc+"//a[@title='GIVING BACK']");
	private final By MEET_THE_DOCTORS_LINK_LOC = By.xpath(topNavigationLoc+"//a[@title='MEET THE DOCTORS']");
	private final By ABOUT_RF_LOC = By.xpath(topNavigationLoc+"//a[@title='ABOUT R+F']");
	private final By EXECUTIVE_TEAM_LOC = By.xpath(topNavigationLoc+"//a[@title='EXECUTIVE TEAM']");
	private final By SEARCH_ICON_LOC = By.xpath("//span[@class='icon-search']");
	private final By LOGIN_ICON_LOC = By.xpath("//div[@class='loginBlock']//a");
	private final By USERNAME_TXTFLD_LOC = By.id("username");
	private final By PASSWORD_TXTFLD_LOC = By.id("password");
	private final By LOGIN_BTN_LOC = By.xpath("//input[@value='LOG IN']");
	private final By CLOSE_ICON_OF_SEARCH_TEXT_BOX_IN_HEADER_NAVIGATION_LOC = By.xpath("//div[@class='yCmsComponent']//span[contains(@class,'icon-close')]");
	private final By DEFAULT_COUNTRY_NAME_IN_TOGGLE_LOC = By.xpath("//div[contains(@class,'wSelect-selected')]");
	private final By TOGGLE_BUTTON_OF_COUNTRY_LOC = By.xpath("//div[@class='form-group']/div");
	private final By MINI_CART_ICON_LOC = By.xpath("//a[contains(@class,'mini-cart-link')]");
	private final By SIGN_UP_NOW_LINK_LOC = By.xpath("//a[contains(text(),'Sign up now')]");

	private String topNavigationSublinksWithTextLoc  = topNavigationLoc+"//a[text()='%s']";
	private String topNavigationSublinksWithTitleLoc   = topNavigationLoc+"//*[@title='%s']";
	private String activePageLoc  = "//span[contains(text(),'%s')]/parent::li";
	private String navigationPageNumberLoc = "//ul[@class='pagination']//a[contains(text(),'%s')]";
	private String subLinkUnderAboutRFLoc = topNavigationLoc+"//a[@title='%s']/following::a[text()='%s'][1]";	
	private String footerLinkLoc = "//div[@class='footer-sections']//a[text()='%s']";
	private String countryOptionsInToggleButtonLoc  = "//div[@class='wSelect-options-holder']//div[contains(text(),'%s')]";
	private String socialMediaLinkAtFooterLoc  = "//a[contains(@class,'%s')]";

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
		else if(element.equalsIgnoreCase(TestConstants.ABOUT_RF)){
			driver.moveToElement(ABOUT_RF_LOC);
			logger.info("mouseHovered on 'About R+F'"); 
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
	public StoreFrontConsultantEnrollNowPage clickEnrollNow(){
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
	public StoreFrontWebsiteBasePage clickWhyRF(){
		mouseHoverOn(TestConstants.BECOME_A_CONSULTANT);
		driver.click(WHY_RF_LOC);
		logger.info("clicked on 'Why R+F'");
		return this;
	}

	public StoreFrontShopSkinCarePage clickAllProducts(){
		mouseHoverOn(TestConstants.SHOP_SKINCARE);
		driver.click(ALL_PRODUCTS_LOC);
		logger.info("clicked on 'All Products'");
		return new StoreFrontShopSkinCarePage(driver);
	}

	/***
	 * This method clicks on the 'Executive Team' link in Top Navigation
	 * 
	 * @param
	 * @return
	 * 
	 */
	public StoreFrontWebsiteBasePage clickExecutiveTeam(){
		mouseHoverOn(TestConstants.ABOUT_RF);
		driver.click(EXECUTIVE_TEAM_LOC);
		logger.info("clicked on 'Executuve Team'");
		return this;
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
	public StoreFrontWebsiteBasePage clickRodanAndFieldsLogo(){
		driver.click(RODAN_AND_FIELDS_LOGO_LOC);
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
	public StoreFrontWebsiteBasePage clickFindAConsultantLinkOnHomePage(){
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

	/***
	 * This method hover on become a consultant  and click on events link
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickEvents(){
		mouseHoverOn(TestConstants.BECOME_A_CONSULTANT);
		driver.click(EVENTS_LOC);
		logger.info("clicked on 'Events link'");
		return this;
	}

	/***
	 * This method switch the window
	 * 
	 * @param parent window handle
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage switchToParentWindow(String parentWindowID){
		driver.close();
		driver.switchTo().window(parentWindowID);
		logger.info("Switched to parent window");
		return this;
	}

	/***
	 * This method switch the window from parent to child
	 * 
	 * @param parent window handle
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage switchToChildWindow(String parentWindowID){
		driver.pauseExecutionFor(1000);
		Set<String> set=driver.getWindowHandles();
		Iterator<String> it=set.iterator();
		while(it.hasNext()){
			String childWindowID=it.next();
			if(!parentWindowID.equalsIgnoreCase(childWindowID)){
				driver.switchTo().window(childWindowID);
				logger.info("Switched to child window");
			}
		}
		return this;
	}

	/***
	 * This method hover on become a consultant  and click on programms and incentives link
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickProgramsAndIncentives(){
		mouseHoverOn(TestConstants.BECOME_A_CONSULTANT);
		driver.click(PROGRAMS_AND_INCENTIVES_LOC);
		logger.info("clicked on 'PROGRAMS & INCENTIVES' link");
		return this;
	}

	/***
	 * This method hover on become a consultant  and click on meet our community link
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickMeetOurCommunityLink(){
		mouseHoverOn(TestConstants.BECOME_A_CONSULTANT);
		driver.click(MEET_OUR_COMMUNITY_LOC);
		logger.info("clicked on 'MEET OUR COMMUNITY' link");
		return this;
	}

	/***
	 * This method hover on About R+F  and click on Who We Are link
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickWhoWeAreLink(){
		mouseHoverOn(TestConstants.ABOUT_RF);
		driver.click(WHO_WE_ARE_LINK_LOC);
		logger.info("clicked on 'WHO WE ARE' link");
		return this;
	}

	/***
	 * This method hover on About R+F  and click on Meet The Doctors link
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickMeetTheDoctorsLink(){
		mouseHoverOn(TestConstants.ABOUT_RF);
		driver.click(MEET_THE_DOCTORS_LINK_LOC);
		logger.info("clicked on 'MEET THE DOCTORS' link");
		return this;
	}

	/***
	 * This method hover on About R+F  and click on Giving back link
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickGivingBackLink(){
		mouseHoverOn(TestConstants.ABOUT_RF);
		driver.click(GIVING_BACK_LINK_LOC);
		logger.info("clicked on 'GIVING BACK'link");
		return this;
	}

	/***
	 * This method click on search icon
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickSearchIcon(){
		driver.click(SEARCH_ICON_LOC);
		logger.info("clicked on Search Icon");
		return this;
	}

	/***
	 * This method will close search text box in header navigation.
	 * 
	 * @param
	 * @return store front base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickCloseIconOfSearchTextBoxPopup(){
		driver.click(CLOSE_ICON_OF_SEARCH_TEXT_BOX_IN_HEADER_NAVIGATION_LOC);
		driver.pauseExecutionFor(3000);
		return this;
	}

	/***
	 * This method validates search text box in header navigation.
	 * 
	 * @param
	 * @return boolean value.
	 * 
	 */
	public boolean isSearchBoxPresent(){
		return driver.findElement(SEARCH_ICON_LOC).isDisplayed();
	}

	/***
	 * This method get product name
	 * 
	 * @param
	 * @return product name
	 * 
	 */
	public String getProductName(){
		String productName = driver.findElement(PRODUCTS_NAME_LINK_LOC).getText(); 
		logger.info("product name is: "+productName);
		return productName;
	}

	/***
	 * This method clicks on the login icon,enter the username and password
	 * and click on 'LOG IN' button
	 * 
	 * @param username ,password
	 * @return StoreFrontWebsiteBasePage
	 * 
	 */

	public StoreFrontWebsiteBasePage loginToStoreFront(String username,String password){
		clickLoginIcon();
		driver.type(USERNAME_TXTFLD_LOC, username);
		logger.info("username entered as "+username);
		driver.type(PASSWORD_TXTFLD_LOC, password);
		logger.info("password entered as  "+password);
		driver.click(LOGIN_BTN_LOC);
		logger.info("login button clicked");
		return this;
	}

	/***
	 * This method verifies the display of footer links on homepage. 
	 * search result. 
	 * @return boolean
	 */
	public Boolean isTheFooterLinkDisplayed(String linkName){
		return driver.IsElementVisible(driver.findElement(By.xpath(String.format(footerLinkLoc, linkName))));
	}

	/***
	 * This method will click the footer links on homepage. 
	 * search result. 
	 * @return store front website base page object
	 */
	public StoreFrontWebsiteBasePage clickFooterLink(String linkName){
		driver.click(By.xpath(String.format(footerLinkLoc, linkName)));
		logger.info("clicked on link "+ "'"+linkName+"'");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method hover on shopSkincare  and click sublink mentioned in argument.
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage navigateToShopSkinCareSubLinks(String LinkName,String sublinkName){
		mouseHoverOn(TestConstants.SHOP_SKINCARE);
		driver.click(By.xpath(String.format(subLinkUnderAboutRFLoc, LinkName, sublinkName)));
		logger.info("clicked on sublink" +"'"+sublinkName+"'");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method get default selected country name from toggle 
	 *
	 * @param
	 * @return country name
	 * 
	 */
	public String getDefaultSelectedCountryNameFromToggle(){
		String countryName = driver.findElement(DEFAULT_COUNTRY_NAME_IN_TOGGLE_LOC).getText(); 
		logger.info("Country name is: "+countryName);
		return countryName;
	}

	/***
	 * This method click on toggle button of country
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickToggleButtonOfCountry(){
		driver.click(TOGGLE_BUTTON_OF_COUNTRY_LOC);
		logger.info("clicked on toggle button of country");
		return this;
	}

	/***
	 * This method select the country from toggle button
	 * 
	 * @param country name
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage selectCountryFromToggleButton(String countryName){
		driver.click(By.xpath(String.format(countryOptionsInToggleButtonLoc, countryName)));
		logger.info("Country "+countryName+" is Selected");
		return this;
	}

	/***
	 * This method click the login icon
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickLoginIcon(){
		driver.click(LOGIN_ICON_LOC);
		logger.info("Login icon clicked");
		return this;
	}

	/***
	 * This method verify the username filed is present or not
	 * 
	 * @param 
	 * @return boolean value
	 * 
	 */
	public boolean isUsernameFieldPresent(){
		return driver.findElement(USERNAME_TXTFLD_LOC).isDisplayed();
	}

	/***
	 * This method verify the password filed is present or not
	 * 
	 * @param 
	 * @return boolean value
	 * 
	 */
	public boolean isPasswordFieldPresent(){
		return driver.findElement(PASSWORD_TXTFLD_LOC).isDisplayed();
	}

	//	/***
	//	 * This method verify the category links is present or not
	//	 * 
	//	 * @param Category name
	//	 * @return boolean value
	//	 * 
	//	 */
	//	public boolean isCategoryLinkPresent(String categoryName){
	//		mouseHoverOn(TestConstants.SHOP_SKINCARE);
	//		return driver.findElement(By.xpath(String.format(topNavigationSublinksWithTextLoc, categoryName))).isDisplayed();
	//	}

	/***
	 * This method click the category links
	 * 
	 * @param Category name
	 * @returnstore front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickCategoryLink(String categoryName){
		mouseHoverOn(TestConstants.SHOP_SKINCARE);
		driver.click(By.xpath(String.format(topNavigationSublinksWithTextLoc, categoryName)));
		logger.info("Category "+categoryName+" clicked");
		return this;
	}

	/***
	 * This method verify mini cart is present or not 
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isMiniCartPresent(){
		return driver.findElement(MINI_CART_ICON_LOC).isDisplayed();
	}

	/***
	 * This method verify toggle button is present or not 
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isToggleButtonPresent(){
		return driver.findElement(TOGGLE_BUTTON_OF_COUNTRY_LOC).isDisplayed();
	}

	/***
	 * This method validates social media icon is present or not  at footer
	 * 
	 * @param social media type
	 * @return boolean value
	 * 
	 */
	public boolean isSocialMediaIconPresentAtFooter(String mediaType){
		return driver.findElement(By.xpath(String.format(socialMediaLinkAtFooterLoc, mediaType))).isDisplayed();
	}

	/***
	 * This method validates the display of top Navigation sublink 
	 * 
	 * @param link name
	 * @return boolean value
	 * 
	 */
	public boolean isTopNavigationSublinkDisplayed(String topNavigationLink,String sublink){
		mouseHoverOn(topNavigationLink);
		return driver.findElement(By.xpath(String.format(topNavigationSublinksWithTitleLoc, sublink))).isDisplayed();
	}

	/***
	 * This method click the sign up now links
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickSignUpNowLink(){
		driver.click(SIGN_UP_NOW_LINK_LOC);
		logger.info("Sign up now link clicked");
		return this;
	}
}