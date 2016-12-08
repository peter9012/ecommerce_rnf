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
import com.rf.pages.website.rehabitat.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontAutoshipStatusPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontCheckoutPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontConsultantEnrollNowPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontHomePage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontShippingInfoPage;
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
	private final By SELECT_AND_CONTINUE_LOC= By.xpath("//div[@id='findConsultantResultArea']/descendant::input[@id='consultantUid'][1]/..");
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
	private final By WELCOME_DD_ACCOUNT_INFO_LOC = By.xpath("//a[text()='Account Info']");
	private final By WELCOME_DROPDOWN_LOC = By.xpath("//div[@class='user-wrapper']/span");
	private final By CHECKOUT_BUTTON_LOC = By.id("checkoutPopup");
	private final By CHECKOUT_CONFIRMATION_OK_BUTTON_LOC = By.xpath("//div[@id='cartCheckoutModal']/a");
	private final By WELCOME_DD_SHIPPING_INFO_LOC = By.xpath("//a[text()='Shipping Info']");
	protected final By FIRST_LAST_NAME_FOR_SHIPPING_DETAILS_LOC = By.id("address.firstName");
	private final By ADDRESS_LINE_1_FOR_SHIPPING_DETAILS_LOC = By.id("address.line1");
	private final By CITY_FOR_SHIPPING_DETAILS_LOC = By.id("address.townCity");
	private final By POSTAL_CODE_FOR_SHIPPING_DETAILS_LOC = By.id("address.postcode");
	private final By PHONE_NUMBER_FOR_SHIPPING_DETAILS_LOC = By.id("address.phone");
	private final By STATE_DD_FOR_REGISTRATION_LOC = By.id("address.region");
	private final By MAKE_THIS_MY_DEFAULT_ADDRESS_CHKBOX_LOC = By.xpath("//label[contains(text(),'Make this my default address')]");
	private final By USE_AS_ENTERED_BUTTON_LOC = By.xpath("//div[@id='cboxLoadedContent']//button[@id='oldAddress']");
	private final By COUNTRY_LOC = By.xpath("//span[@class='selected-country']/preceding::input[1]");
	private final By COUNTRY_NAME_LOC = By.xpath("//span[@class='selected-country']");
	private final By CHECKOUT_BUTTON_POPUP_LOC = By.xpath("//div[@id='addToCartLayer']/a[contains(text(),'Checkout')]");
	private final By ADD_TO_CART_FIRST_PRODUCT_LOC = By.xpath("//div[@id='product_listing']/descendant::button[text()='Add to cart'][1]");
	private final By ADD_TO_BAG_OF_FIRST_PRODUCT = By.xpath("//div[@id='product_listing']/descendant::span[text()='Add to Bag'][1]");
	private final By SAVE_BUTTON_LOC = By.id("deliveryAccountSubmit");
	private final By WELCOME_DD_AUTOSHIP_STATUS_LOC = By.xpath("//a[text()='Autoship Status']");
	private final By EDIT_LINK_NEXT_TO_MAIN_ACCOUNT_LOC = By.xpath("//div[@class='checkout-steps']/descendant::a[1]");
	private final By WELCOME_DD_ORDERS_LOC = By.xpath("//a[text()='Orders']");

	private String textLoc = "//*[contains(text(),'%s')]";
	private String stateForShippingDetails = "//select[@id='address.region']//option[text()='%s']";
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
	public void selectFirstSponsorFromList(){
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

	/***
	 * This method hover on shopSkincare  and click link mentioned in argument.
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage navigateToShopSkincareLink(String linkName){
		mouseHoverOn(TestConstants.SHOP_SKINCARE);
		driver.click(By.xpath(String.format(topNavigationSublinksWithTextLoc, linkName)));
		logger.info("clicked on"+ "'"+linkName+"'" +"under shopskincare");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method click Account info link from welcome dropdown.
	 * 
	 * @param
	 * @return store front Account info page object
	 * 
	 */
	public StoreFrontAccountInfoPage navigateToAccountInfoPage(){
		driver.click(WELCOME_DD_ACCOUNT_INFO_LOC);
		logger.info("Account Info clicked from welcome dropdown");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return new StoreFrontAccountInfoPage(driver);
	}

	/***
	 * This method click the welcome dropdown.
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickWelcomeDropdown(){
		driver.click(WELCOME_DROPDOWN_LOC);
		logger.info("Welcome dropdown clicked");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method click shipping info link from welcome dropdown.
	 * 
	 * @param
	 * @return store front Shipping info page object
	 * 
	 */
	public StoreFrontShippingInfoPage navigateToShippingInfoPage(){
		driver.click(WELCOME_DD_SHIPPING_INFO_LOC);
		logger.info("Shipping Info clicked from welcome dropdown");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return new StoreFrontShippingInfoPage(driver);
	}

	/***
	 * This method enter the consultant shipping details
	 * 
	 * @param First name,Last name, address line1, city, state, postal code, phone number
	 * @return store front Base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage enterConsultantShippingDetails(String firstName, String lastName, String addressLine1, String city, String state, String postal, String phoneNumber){
		String completeName = firstName+" "+lastName;
		driver.type(FIRST_LAST_NAME_FOR_SHIPPING_DETAILS_LOC, completeName);
		logger.info("Entered complete name as "+completeName);
		driver.type(ADDRESS_LINE_1_FOR_SHIPPING_DETAILS_LOC, addressLine1);
		logger.info("Entered address line 1 as "+addressLine1);
		driver.type(CITY_FOR_SHIPPING_DETAILS_LOC, city);
		logger.info("Entered city as "+city);
		driver.click(STATE_DD_FOR_REGISTRATION_LOC);
		logger.info("State dropdown clicked");
		driver.click(By.xpath(String.format(stateForShippingDetails, state)));
		logger.info("State selected as "+state);
		driver.type(POSTAL_CODE_FOR_SHIPPING_DETAILS_LOC, postal);
		logger.info("Entered postal code as "+postal);
		driver.type(PHONE_NUMBER_FOR_SHIPPING_DETAILS_LOC, phoneNumber);
		logger.info("Entered Phone number  as "+phoneNumber);
		return this;
	}

	/***
	 * This method click Use as entered button.
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickUseAsEnteredButton(){
		driver.click(USE_AS_ENTERED_BUTTON_LOC);
		logger.info("Use as entered button clicked");
		return this;
	}

	/***
	 * This method click add to bag button for first product
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage selectFirstProduct(){
		driver.pauseExecutionFor(5000);
		driver.moveToElementByJS(ADD_TO_CART_FIRST_PRODUCT_LOC);
		driver.click(ADD_TO_BAG_OF_FIRST_PRODUCT);
		return this;
	}

	/***
	 * This method click the checkout button on popup.
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage checkoutThePopup(){
		driver.click(CHECKOUT_BUTTON_POPUP_LOC);
		logger.info("Clicked on checkout button at popup");
		return this;
	}

	/***
	 * This method click the save button
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickSaveButton(){
		driver.click(SAVE_BUTTON_LOC);
		logger.info("Save button clicked");
		return this;
	}

	/***
	 * This method validates the country field is editable or not
	 * 
	 * @param country name
	 * @return boolean value
	 * 
	 */
	public boolean isCountryNameEditable(String country){
		try{
			driver.type(COUNTRY_LOC, country);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	/***
	 * This method get the country name
	 * 
	 * @param
	 * @return country name
	 * 
	 */
	public String getCountryName(){
		driver.pauseExecutionFor(2000);
		String countryName = driver.findElement(COUNTRY_NAME_LOC).getText();
		logger.info("country name is "+countryName);
		return countryName;
	}



	/***
	 * This method check the check box of make this my default address
	 * 
	 * @param
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage checkMakeThisMyDefaultAddressChkBox(){
		driver.click(MAKE_THIS_MY_DEFAULT_ADDRESS_CHKBOX_LOC);
		logger.info("Make this my default address check box checked");
		return this;
	}

	/***
	 * This method click the checkout button at cart page
	 * 
	 * @param
	 * @return store front checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage checkoutTheCart(){
		driver.click(CHECKOUT_BUTTON_LOC);
		logger.info("Clicked on checkout button");
		try{
			driver.click(CHECKOUT_CONFIRMATION_OK_BUTTON_LOC);
			logger.info("Clicked on OK button at checkout confirmation popup");
		}catch(Exception e){
			logger.info("No checkout confirmation popup");
		}
		return new StoreFrontCheckoutPage(driver);
	}

	/***
	 * This method click Autoship status link from welcome dropdown.
	 * 
	 * @param
	 * @return store front Account info page object
	 * 
	 */
	public StoreFrontAutoshipStatusPage navigateToAutoshipStatusPage(){
		driver.click(WELCOME_DD_AUTOSHIP_STATUS_LOC);
		logger.info("Autoship status clicked from welcome dropdown");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return new StoreFrontAutoshipStatusPage(driver);
	}

	/***
	 * This method Update first name on checkout Page.
	 * 
	 * @param
	 * @return store front base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage updateFirstName(String updatedName){
		driver.type(FIRST_LAST_NAME_FOR_SHIPPING_DETAILS_LOC, updatedName);
		logger.info("First Name Updated for account info at checkout page");
		return this;
	}
	/***
	 * This method Update last name on checkout Page.
	 * 
	 * @param
	 * @return store front base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage updateLastName(String updatedName){
		driver.type(FIRST_LAST_NAME_FOR_SHIPPING_DETAILS_LOC, updatedName);
		logger.info("Last Name Updated for account info at checkout page");
		return this;
	}
	/***
	 * This method edit Main Account info at checkout page
	 * 
	 * @param
	 * @return store front base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage editMainAccountInfo(){
		driver.click(EDIT_LINK_NEXT_TO_MAIN_ACCOUNT_LOC);
		logger.info("Edit link clicked next to main account info");
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method click billing info link from welcome dropdown.
	 * 
	 * @param
	 * @return store front billing info page object
	 * 
	 */
	public StoreFrontBillingInfoPage navigateToBillingInfoPage(){
		driver.click(WELCOME_DD_SHIPPING_INFO_LOC);
		logger.info("Shipping Info clicked from welcome dropdown");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return new StoreFrontBillingInfoPage(driver);
	}

	/***
	 * This method validates text at page 
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isTextPresent(String textName){
		return driver.isElementPresent(By.xpath(String.format(textLoc, textName)));
	}

	/***
	 * This method click orders link from welcome drop down
	 * 
	 * @param
	 * @return store front orders page object
	 * 
	 */
	public StoreFrontOrdersPage navigateToOrdersPage(){
		driver.click(WELCOME_DD_ORDERS_LOC);
		logger.info("orders link clicked from welcome dropdown");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return new StoreFrontOrdersPage(driver);
	}
}