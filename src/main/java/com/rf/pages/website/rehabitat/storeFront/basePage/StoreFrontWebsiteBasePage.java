package com.rf.pages.website.rehabitat.storeFront.basePage;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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
	private final By SEARCH_BOX_LOC = By.id("search-box");
	private final By SELECT_AND_CONTINUE_LOC= By.xpath("//div[@id='findConsultantResultArea']/descendant::input[@id='consultantUid'][1]/..");
	private final By SPONSOR_SEARCH_RESULTS_LOC = By.xpath("//div[@id='findConsultantResultArea']//div[contains(@class,'consultant-box')][1]");
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
	protected final By CHECKOUT_BUTTON_LOC = By.id("checkoutPopup");
	private final By CHECKOUT_CONFIRMATION_OK_BUTTON_LOC = By.xpath("//div[@id='cartCheckoutModal']/a");
	private final By WELCOME_DD_SHIPPING_INFO_LOC = By.xpath("//a[text()='Shipping Info']");
	private final By WELCOME_DD_BILLING_INFO_LOC = By.xpath("//a[text()='Billing Info']");
	protected final By FIRST_LAST_NAME_FOR_SHIPPING_DETAILS_LOC = By.id("address.firstName");
	protected final By STATE_DD_FOR_REGISTRATION_LOC = By.id("address.region");
	private final By MAKE_THIS_MY_DEFAULT_ADDRESS_CHKBOX_LOC = By.xpath("//label[contains(text(),'Make this my default address')]");
	protected final By USE_AS_ENTERED_BUTTON_LOC = By.xpath("//div[@id='cboxLoadedContent']//button[@id='oldAddress']");
	private final By COUNTRY_LOC = By.xpath("//span[@class='selected-country']/preceding::input[1]");
	private final By COUNTRY_NAME_LOC = By.xpath("//span[@class='selected-country']");
	private final By CHECKOUT_BUTTON_POPUP_LOC = By.xpath("//div[@id='addToCartLayer']/a[contains(text(),'Checkout')]");
	private final By ADD_TO_CART_FIRST_PRODUCT_LOC = By.xpath("//div[@id='product_listing']/descendant::button[text()='Add to cart'][1]");
	private final By ADD_TO_BAG_OF_FIRST_PRODUCT = By.xpath("//div[@id='product_listing']/descendant::span[text()='Add to Bag'][1]");
	protected final By SAVE_BUTTON_LOC = By.id("deliveryAccountSubmit");
	private final By WELCOME_DD_AUTOSHIP_STATUS_LOC = By.xpath("//a[text()='Autoship Status']");
	private final By EDIT_LINK_NEXT_TO_MAIN_ACCOUNT_LOC = By.xpath("//div[@class='checkout-steps']/descendant::a[1]");
	private final By WELCOME_DD_ORDERS_LOC = By.xpath("//a[text()='Orders']");
	private final By SAVE_BUTTON_OF_SHIPPING_ADDRESS_LOC = By.xpath("//button[contains(text(),'Save')]");
	private final By ERROR_MESSAGE_FOR_ADDRESS_LINE_1_LOC = By.id("address.line1-error");
	private final By ERROR_MESSAGE_FOR_CITY_LOC = By.id("address.townCity-error");
	private final By ERROR_MESSAGE_FOR_STATE_LOC = By.id("address.region-error");
	private final By ERROR_MESSAGE_FOR_POSTAL_CODE_LOC = By.id("address.postcode-error");
	private final By ERROR_MESSAGE_FOR_PHONE_NUMBER_LOC = By.id("address.phone-error");
	private final By ERROR_MESSAGE_FOR_FIRST_LAST_NAME_LOC = By.id("address.firstName-error");
	private final By SHIPPING_NEXT_BUTTON_LOC = By.id("deliveryAddressSubmit");
	private final By LOGOUT_LOC = By.xpath("//a[text()='Sign Out']");
	private final By WELCOME_DD_EDIT_CRP_LOC = By.xpath("//a[text()='Edit CRP']");
	private final By WELCOME_DD_CHECK_MY_PULSE_LOC = By.xpath("//a[text()='Check My Pulse']");
	private final By USED_AS_ENTERED_BUTTON_LOC  = By.xpath("//div[@id='cboxContent']//button[@id='oldAddress']");
	private final By I_DONT_HAVE_SPONSOR_CHKBOX_LOC = By.xpath("//label[text()=\"I DON'T HAVE SPONSOR\"]");
	private final By SUBMIT_BTN_ON_REQUIRED_SPONSOR_POPUP_LOC = By.id("consultant-sponsor-submit");
	private final By SPONSOR_FIRST_NAME_LOC = By.xpath("//div[@class='enroll-page']/descendant::input[@id='sponsor.firstName'][1]");
	private final By SPONSOR_LAST_NAME_LOC = By.xpath("//div[@class='enroll-page']/descendant::input[@id='sponsor.lastName'][1]");
	private final By SPONSOR_EMAIL_LOC = By.xpath("//div[@class='enroll-page']/descendant::input[@id='sponsor.email'][1]");
	private final By SPONSOR_ZIPCODE_LOC = By.xpath("//div[@class='enroll-page']/descendant::input[@id='sponsor.zipcode'][1]");
	private final By THANKS_MSG_ON_SPONSOR_REQUEST_LOC = By.xpath("//div[@id='sponsor-entire-form-display'][@style='display: block;']//div[@id='sponsor-success-data']");
	private final By BACK_TO_HOMEPAGE_LOC = By.xpath("//div[@id='sponsor-entire-form-display'][@style='display: block;']//input[@id='consultant-backhome']");
	private final By FIRST_LAST_NAME_FOR_ADDRESS_DETAILS_LOC = By.id("address.firstName");
	private final By ADDRESS_LINE_1_FOR_ADDRESS_DETAILS_LOC = By.id("address.line1");
	private final By ADDRESS_LINE_2_FOR_ADDRESS_DETAILS_LOC = By.id("address.line2");
	private final By CITY_FOR_ADDRESS_DETAILS_LOC = By.id("address.townCity");
	private final By POSTAL_CODE_FOR_ADDRESS_DETAILS_LOC = By.id("address.postcode");
	private final By PHONE_NUMBER_FOR_ADDRESS_DETAILS_LOC = By.id("address.phone");
	private final By CARD_TYPE_DD_LOC = By.xpath("//*[@id='c-ct']");
	private final By CARD_NUMBER_LOC= By.id("c-cn");
	private final By NAME_ON_CARD_LOC= By.id("c-chn");
	private final By EXP_MONTH_DD_LOC= By.id("c-exmth");
	private final By EXP_MONTH_LOC= By.xpath("//select[@id='c-exmth']//option[11]");
	private final By EXP_YEAR_DD_LOC= By.id("c-exyr");
	private final By EXP_YEAR_LOC= By.xpath("//select[@id='c-exyr']//option[11]");
	private final By CVV_LOC= By.id("c-cvv");
	private final By IFRAME_LOC= By.id("IFrame");

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
	private String sponsorEmptyFieldValidationOnPopUpLoc = "//label[@id='sponsor.%s-error'][contains(text(),'%s')]";
	private String sponsorInvalidFieldValidationOnPopUpLoc = "//label[@id='sponsor.%s-error'][contains(text(),'%s')]";
	private String cardTypeLoc= "//select[@id='c-ct']//option[text()='%s']";
	
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
		for(int attemptNumber=1;attemptNumber<=3;attemptNumber++){
			try{
				mouseHoverOn(TestConstants.BECOME_A_CONSULTANT);
				driver.pauseExecutionFor(500);
				driver.click(ENROLL_NOW_LOC);
				break;
			}catch(Exception ex){
				logger.info("Become a Consultant not mouse hovered properly..retry");
				driver.pauseExecutionFor(1000);
				continue;
			}
		}
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
	public boolean isSponsorResultDisplayed(){
		return driver.isElementVisible(driver.findElement(SPONSOR_SEARCH_RESULTS_LOC));		
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
		return driver.findElement(SEARCH_BOX_LOC).isDisplayed();
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
		return driver.isElementVisible(driver.findElement(By.xpath(String.format(footerLinkLoc, linkName))));
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
	public StoreFrontWebsiteBasePage enterConsultantShippingDetails(String firstName, String lastName, String addressLine1, String addressLine2, String city, String state, String postal, String phoneNumber){
		String completeName = firstName+" "+lastName;
		driver.type(FIRST_LAST_NAME_FOR_ADDRESS_DETAILS_LOC, completeName);
		logger.info("Entered complete name as "+completeName);
		driver.type(ADDRESS_LINE_1_FOR_ADDRESS_DETAILS_LOC, addressLine1);
		logger.info("Entered address line 1 as "+addressLine1);
		driver.type(ADDRESS_LINE_2_FOR_ADDRESS_DETAILS_LOC, addressLine2);
		logger.info("Entered address line 2 as "+addressLine2);
		driver.type(CITY_FOR_ADDRESS_DETAILS_LOC, city);
		logger.info("Entered city as "+city);
		driver.click(STATE_DD_FOR_REGISTRATION_LOC);
		logger.info("State dropdown clicked");
		driver.click(By.xpath(String.format(stateForShippingDetails, state)));
		logger.info("State selected as "+state);
		driver.type(POSTAL_CODE_FOR_ADDRESS_DETAILS_LOC, postal);
		logger.info("Entered postal code as "+postal);
		driver.type(PHONE_NUMBER_FOR_ADDRESS_DETAILS_LOC, phoneNumber);
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
		driver.click(WELCOME_DD_BILLING_INFO_LOC);
		logger.info("Billing Info clicked from welcome dropdown");
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

	/***
	 * This method click the save button of shipping address
	 * 
	 * @param
	 * @return store front shipping info page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickSaveButtonOfShippingAddress(){
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(SAVE_BUTTON_OF_SHIPPING_ADDRESS_LOC));
		logger.info("Save button clicked");
		return this;
	}

	/***
	 * This method validates the error message for first & last name field
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isErrorMessagePresentForFirstAndLastName(){
		return driver.findElement(ERROR_MESSAGE_FOR_FIRST_LAST_NAME_LOC).isDisplayed();
	}

	/***
	 * This method validates the error message for address line 1 field
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isErrorMessagePresentForAddressLine1(){
		return driver.findElement(ERROR_MESSAGE_FOR_ADDRESS_LINE_1_LOC).isDisplayed();
	}

	/***
	 * This method validates the error message for city field
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isErrorMessagePresentForCity(){
		return driver.findElement(ERROR_MESSAGE_FOR_CITY_LOC).isDisplayed();
	}

	/***
	 * This method validates the error message for state field
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isErrorMessagePresentForState(){
		return driver.findElement(ERROR_MESSAGE_FOR_STATE_LOC).isDisplayed();
	}

	/***
	 * This method validates the error message for postal code field
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isErrorMessagePresentForPostalCode(){
		return driver.findElement(ERROR_MESSAGE_FOR_POSTAL_CODE_LOC).isDisplayed();
	}

	/***
	 * This method validates the error message for phone number field
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isErrorMessagePresentForPhoneNumber(){
		return driver.findElement(ERROR_MESSAGE_FOR_PHONE_NUMBER_LOC).isDisplayed();
	}

	/***
	 * This method click the next button at shipping details page
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickShippingDetailsNextbutton(){
		driver.click(SHIPPING_NEXT_BUTTON_LOC);
		logger.info("Next button clicked of shipping details");
		clickUseAsEnteredButtonOnPopUp();
		return this;
	}

	public void clickUseAsEnteredButtonOnPopUp(){
		driver.click(USED_AS_ENTERED_BUTTON_LOC);
		logger.info("'Used as entered' button clicked");
	}

	/***
	 * This method click the category links
	 * 
	 * @param Category name
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickCategoryLink(String categoryName){
		mouseHoverOn(TestConstants.SHOP_SKINCARE);
		driver.click(By.xpath(String.format(topNavigationSublinksWithTextLoc, categoryName)));
		logger.info("Category "+categoryName+" clicked");
		return this;
	}

	/***
	 * This method validates logout functionality.
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isLogoutSuccessful(){
		return driver.isElementPresent(LOGIN_ICON_LOC);
	}
	/***
	 * This method hover on shopSkincare  and click link mentioned in argument in new Tab.
	 * 
	 * @param regimen name as linkName
	 * @return store front website base page object
	 * @throws AWTException 
	 * 
	 */
	public StoreFrontWebsiteBasePage navigateToShopSkincareLinkInNewTab(String linkName) throws AWTException{
		mouseHoverOn(TestConstants.SHOP_SKINCARE);
		WebElement element = driver.findElement(By.xpath(String.format(topNavigationSublinksWithTextLoc, linkName)));
		Actions action= new Actions(RFWebsiteDriver.driver);
		action.contextClick(element).build().perform();
		logger.info("Right click performed on webelement");
		driver.pauseExecutionFor(2000);
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_DOWN);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_DOWN);
		robot.keyRelease(KeyEvent.VK_ENTER);
		logger.info("key down performed on webelement options and entered clicked");
		driver.pauseExecutionFor(2000);
		logger.info("clicked on"+ "'"+linkName+"'" +"under shopskincare");
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_TAB);
		return this;
	}

	/***
	 * This method validates login functionality in multiple tabs.
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isUserLoggedInNewTab(){
		return driver.isElementPresent(WELCOME_DROPDOWN_LOC);
	}

	/***
	 * This method refresh or reload current page.
	 * 
	 * @param
	 * @return store front base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage pageRefresh(){
		driver.navigate().refresh();
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method click edit CRP link from welcome dropdown.
	 * 
	 * @param
	 * @return store front base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage navigateToEditCRPPage(){
		driver.click(WELCOME_DD_EDIT_CRP_LOC);
		logger.info("Edit CRP clicked from welcome dropdown");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method click check my pulse link from welcome dropdown.
	 * 
	 * @param
	 * @return store front base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage navigateToCheckMyPulsePage(){
		driver.click(WELCOME_DD_CHECK_MY_PULSE_LOC);
		logger.info("check my pulse clicked from welcome dropdown");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method click orders link from welcome drop down
	 * 
	 * @param
	 * @return store front orders page object
	 * 
	 */
	public StoreFrontHomePage logout(){
		driver.click(LOGOUT_LOC);
		logger.info("logout link clicked from welcome dropdown");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return new StoreFrontHomePage(driver);
	}

	/***
	 *  This method click on I DONT HAVE SPONSOR checkbox
	 *  @param
	 *  @return store front base page object
	 */
	public StoreFrontWebsiteBasePage clickIDontHaveSponsorCheckBox(){
		driver.click(I_DONT_HAVE_SPONSOR_CHKBOX_LOC);
		logger.info("'I dont have sponsor checkbox' clicked");
		return this;
	}

	/***
	 * This method checks if the submit button on required sponsor popup is disbaled or not
	 * @return
	 */
	public boolean isSubmitBtnOnSponsorPopUpDisabled(){
		return !(driver.findElement(SUBMIT_BTN_ON_REQUIRED_SPONSOR_POPUP_LOC).isEnabled());
	}

	/***
	 * This method enters the sponsor's first name, last name, email and zip code
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param zipCode
	 * @return
	 */
	public StoreFrontWebsiteBasePage enterDetailsInRequiredConsultantSponsorPopUp(String firstName,String lastName,String email,String zipCode){
		driver.type(SPONSOR_FIRST_NAME_LOC, firstName);
		logger.info("entered sponsor first name as "+firstName);
		driver.type(SPONSOR_LAST_NAME_LOC, lastName);
		logger.info("entered sponsor last name as "+lastName);
		driver.type(SPONSOR_EMAIL_LOC, email);
		logger.info("entered sponsor email as "+email);
		driver.type(SPONSOR_ZIPCODE_LOC, zipCode);
		logger.info("entered sponsor zipcode as "+zipCode);
		return this;		
	}

	/***
	 * This method returns whether empty field validation displayed for sponsor popup or not
	 * @param fieldName
	 * @return
	 */

	public boolean isEmptyFieldValidationForSponsorOnPopupDisplayed(String fieldName){
		String validationMsg = "This field is required";
		return driver.isElementVisible(driver.findElement(By.xpath(String.format(sponsorEmptyFieldValidationOnPopUpLoc, fieldName,validationMsg))));		
	}

	/***
	 * 
	 * @return
	 */
	public boolean isInvalidFieldValidationForSponsorOnPopupDisplayed(String fieldName){
		String validationMsg = null;
		if(fieldName.equalsIgnoreCase("zipCode")){
			validationMsg = "Please enter valid postal code";
		}
		else if(fieldName.equalsIgnoreCase("email")){
			validationMsg = "Please enter a valid email address";
		}
		return driver.isElementVisible(driver.findElement(By.xpath(String.format(sponsorInvalidFieldValidationOnPopUpLoc, fieldName,validationMsg))));
	}

	/***
	 * This method click on Submit button on required consultant sponsor popup
	 *  @param
	 *  @return store front base page object
	 * 
	 */	
	public StoreFrontWebsiteBasePage clickSubmitBtnOnRequiredConsultantSponsorPopUp(){
		driver.findElement(SUBMIT_BTN_ON_REQUIRED_SPONSOR_POPUP_LOC).click();
		logger.info("Submit button on required consultant sponsor popup clicked");
		return this;
	}

	/***
	 * This method checks if Thanks msg after request of sponsor is 
	 * submitted
	 * @return
	 */
	public boolean isThanksMessageAfterSponsorRequestPresent(){
		return driver.isElementPresent(THANKS_MSG_ON_SPONSOR_REQUEST_LOC);
	}

	/***
	 * This method clicks on the Back to HomePage button after request of sponsor is 
	 * submitted
	 * @return
	 */
	public void clickBackToHomePageBtn(){
		driver.click(BACK_TO_HOMEPAGE_LOC);
		logger.info("back to hompage button clicked");
	}	

	/***
	 * This method launch the baseurl
	 *  
	 * @param 
	 * @return store front base page object
	 */
	public StoreFrontWebsiteBasePage getBaseUrl(){
		String url = driver.getURL()+"/"+driver.getCountry().toUpperCase();
		driver.get(url);
		logger.info("Url launched as "+url);
		return this;
	}	

	/***
	 * This method enter the consultant Address details
	 * 
	 * @param First name,Last name, address line1, city, state, postal code, phone number
	 * @return store front Base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage enterConsultantAddressDetails(String firstName, String lastName, String addressLine1, String addressLine2, String city, String state, String postal, String phoneNumber){
		String completeName = firstName+" "+lastName;
		driver.type(FIRST_LAST_NAME_FOR_ADDRESS_DETAILS_LOC, completeName);
		logger.info("Entered complete name as "+completeName);
		driver.type(ADDRESS_LINE_1_FOR_ADDRESS_DETAILS_LOC, addressLine1);
		logger.info("Entered address line 1 as "+addressLine1);
		driver.type(ADDRESS_LINE_2_FOR_ADDRESS_DETAILS_LOC, addressLine2);
		logger.info("Entered address line 2 as "+addressLine2);
		driver.type(CITY_FOR_ADDRESS_DETAILS_LOC, city);
		logger.info("Entered city as "+city);
		driver.click(STATE_DD_FOR_REGISTRATION_LOC);
		logger.info("State dropdown clicked");
		driver.click(By.xpath(String.format(stateForShippingDetails, state)));
		logger.info("State selected as "+state);
		driver.type(POSTAL_CODE_FOR_ADDRESS_DETAILS_LOC, postal);
		logger.info("Entered postal code as "+postal);
		driver.type(PHONE_NUMBER_FOR_ADDRESS_DETAILS_LOC, phoneNumber);
		logger.info("Entered Phone number  as "+phoneNumber);
		return this;
	}

	/***
	 * This method enter the consultant billing details
	 * 
	 * @param Card type, card number, card name, CVV
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontWebsiteBasePage enterConsultantBillingDetails(String cardType, String cardNumber, String nameOnCard,String CVV){
		driver.switchTo().frame(driver.findElement(IFRAME_LOC));
		logger.info("Switched into iframe");
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(CARD_TYPE_DD_LOC));
		logger.info("Card type dropdown clicked");
		driver.click(By.xpath(String.format(cardTypeLoc, cardType)));
		logger.info("Card type selected as "+cardType);
		driver.type(CARD_NUMBER_LOC, cardNumber);
		logger.info("Entered card number as"+cardNumber);
		driver.type(NAME_ON_CARD_LOC, nameOnCard);
		logger.info("Entered card name as"+nameOnCard);
		driver.click(EXP_MONTH_DD_LOC);
		logger.info("Exp month dropdown clicked");
		driver.click(EXP_MONTH_LOC);
		logger.info("Exp month selected");
		driver.click(EXP_YEAR_DD_LOC);
		logger.info("Exp year dropdown clicked");
		driver.click(EXP_YEAR_LOC);
		logger.info("Exp year selected");
		driver.type(CVV_LOC, CVV);
		logger.info("Entered CVV as"+CVV);
		driver.switchTo().defaultContent();
		logger.info("Switched to default content");			
		return this;

	}
}