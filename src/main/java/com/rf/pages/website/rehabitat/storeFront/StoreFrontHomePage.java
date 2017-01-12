package com.rf.pages.website.rehabitat.storeFront;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class StoreFrontHomePage extends StoreFrontWebsiteBasePage{

	public StoreFrontHomePage(RFWebsiteDriver driver) {
		super(driver);		
	}
	private static final Logger logger = LogManager
			.getLogger(StoreFrontHomePage.class.getName());

	private final By ENROLL_NOW_BUTTON_LOC = By.xpath("//a[text()='Enroll Now']");
	private final By FIRST_EVENT_CALENDAR_LOC = By.xpath("//h3[contains(text(),'Presentations')]/following::a[text()='EVENT CALENDER'][1]");
	private final By VISIT_THE_BLOG_LOC = By.xpath("//a[text()='VISIT THE BLOG']");
	private final By DIRECT_LINK_ASSOCIATION = By.xpath("//div[@class='content']//a[contains(text(),'Direct Selling Association')]");
	private final By DSA_CODE_OF_ETHICS_LINK = By.xpath("//a[contains(text(),'DSA Code of Ethics')]");
	private final By DONATE_NOW_BUTTON_LOC = By.xpath("//a[contains(text(),'Donate Now')]");
	private final By ERROR_MSG_TEXT_LOC = By.xpath("//div[@class='content']//h2");
	private final By LOGIN_OR_REGISTER_TXT_LOC = By.xpath("//h1[contains(text(),'LOG IN OR REGISTER')]|| contains(text(),'Log in')]");
	private final By INCORRECT_USERNAME_PASSOWRD_TXT_LOC = By.xpath("//div[contains(@class,'alert-danger') and contains(text(),'') or contains(text(),'Your username or password was incorrect.')]");
	private final By MEET_THE_DOCTORS_TXT_LOC = By.xpath("//h1[text()=' Meet the Doctors']");
	private final By MEET_RF_EXECUTIVE_TEAM_LINK_LOC = By.xpath("//a[@href='/executive-team']");
	private final By TOTAL_TEAM_MEMBERS_IN_EXECUTIVE_TEAM_LOC = By.xpath("//div[@id='modal_front']//div[@class='title']");
	private final By TEAM_MEMBER_NAME_FROM_POPUP_LOC = By.xpath("//div[@class='item active']/descendant::h4[1]");
	private final By CLOSE_ICON_MEMBER_DETAIL_POPUP_LOC = By.xpath("//button[@class='close']");
	private final By BILLING_ADDRESS_DD_LOC= By.id("default-address");
	private final By BILLING_ADDRESS_OPTION_VALUE_LOC= By.xpath("//select[@id='default-address']//option[2]");
	private final By SAVE_BUTTON_LOC = By.id("deliveryAccountSubmit");
	private final By BIG_BUSNINESS_KIT_PAGE_LOC = By.xpath("//input[@id='ENROLL_KIT_0002']");
	private final By PERSONAL_RESULTS_KIT_PAGE_LOC = By.xpath("//input[@id='ENROLL_KIT_0003']");
	private final By FIRST_NAME_FOR_REGISTRATION_LOC = By.id("register.firstName");
	private final By LAST_NAME_FOR_REGISTRATION_LOC = By.id("register.lastName");
	private final By EMAIL_ID_FOR_REGISTRATION_LOC = By.id("register.email");
	private final By PASSWORD_FOR_REGISTRATION_LOC = By.id("register.pwd");
	private final By CONFIRM_PASSWORD_FOR_REGISTRATION_LOC = By.id("register.checkPwd");
	private final By SSN_FOR_REGISTRATION_LOC = By.id("register.socialSecurity");
	private final By NEXT_BUTTON_LOC = By.id("consultant-next-button");
	private final By ADD_TO_CART_FIRST_PRODUCT_LOC = By.xpath("//div[@id='product_listing']/descendant::button[text()='Add to cart'][1]");
	private final By ADD_TO_BAG_OF_FIRST_PRODUCT = By.xpath("//div[@id='product_listing']/descendant::span[text()='Add to Bag'][1]");
	private final By SHOP_BY_PRICE_FILTER_LOC = By.xpath("//input[@id='$0-$49.99ID']/preceding::span[contains(@class,'glyphicon')][1]/..");
	private final By SHOP_BY_PRICE_FILTER_OPTION_0_TO_49$_LOC = By.xpath("//input[@id='$0-$49.99ID']/..");
	private final By SHOP_BY_PRICE_FILTER_OPTION_0_TO_49$_AFTER_CHECKED_LOC = By.xpath("//input[@id='$0-$49.99ID'][@checked = 'checked']");
	private final By SHOP_BY_PRICE_FILTER_OPTION_200_TO_499$_LOC = By.xpath("//input[@id='$200-$499.99ID']/..");
	private final By SHOP_BY_PRICE_FILTER_OPTION_200_TO_499$_AFTER_CHECKED_LOC = By.xpath("//input[@id='$200-$499.99ID'][@checked = 'checked']");
	private final By WELCOME_USER_LOC = By.xpath("//div[@class='loginBlock']/div");
	private final By CONFIRMATION_MSG_OF_CONSULTANT_ENROLLMENT_LOC = By.xpath("//div[@class='global-alerts']/div");
	private final By POLICIES_AND_PROCEDURES_LINK_LOC = By.xpath("//a[contains(text(),'Rodan+Fields Policies and Procedure')]");
	private final By PULSE_PRO_T_C_LINK_LOC = By.xpath("//a[contains(text(),'Pulse Pro Terms and Conditions')]");
	private final By CRP_T_C_LINK_LOC = By.xpath("//a[contains(text(),'Consultant Replenishment Program (CRP) Terms and Conditions')]");
	private final By NORTH_DAKOTA_CHKBOX_LOC = By.xpath("//label[@for='noEnrollmentKit']/..");
	private final By ALL_KIT_SECTION_LOC = By.xpath("//div[@class='enrollmentKit-wrapper row']/div");
	private final By CONNECT_BTN_LOC = By.xpath("//a[text()='CONNECT']");
	private final By APPLYING_AS_BUSINESS_ENTITY_LINK_LOC = By.xpath("//a[text()='Applying as a business entity?']");
	private final By APPLYING_AS_BUSINESS_ENTITY_POPUP_LOC = By.xpath("//div[contains(@class,'enroll-step-deatils')]");
	private final By CLOSE_BTN_ON_APPLYING_AS_BUSINESS_ENTITY_POPUP_LOC = By.id("enroll-sdClose");
	private final By SUBSCRIBE_TO_PULSE_CHKBOX_LOC = By.xpath("//label[@for='pulse-check']");
	private final By PREFIX_FIELD_LOC = By.id("prefixId"); 
	private final By PREFIX_AVAILABLE_LOC = By.xpath("//div[@class='available-dispaly available' or text()='Available']");
	private final By CLOSE_ICON_SEARCH_TEXT_BOX = By.xpath("//div[@class='yCmsComponent']//span[contains(@class,'icon-close')]");
	private final By SEARCH_RESULT_PRODUCTS_LOC = By.xpath("//div[@class='product__listing product__grid']//div[@class='product-item']//a[@class='name']");
	private final By TOTAL_KITS_LOC = By.xpath("//div[contains(@class,'enrollmentKit-wrapper')]/div");
	private final By SHOP_BY_PRICE_FILTER_OPTION_50_TO_199$_LOC = By.xpath("//input[@id='$50-$199.99ID']/..");
	private final By SHOP_BY_PRICE_FILTER_OPTION_50_TO_199$_AFTER_CHECKED_LOC = By.xpath("//input[@id='$50-$199.99ID'][@checked = 'checked']");
	
	private String viewDetailsLinkLoc = "//div[contains(@class,'enrollmentKit-wrapper')]/descendant::a[contains(text(),'View Details')][%s]";
	private String expandedKitDescriptionLoc = "//div[contains(@class,'enrollmentKit-wrapper')]/div[%s]//div[@class='detailed-description']";
	private String closeBtnForKitDetailsLoc = "//div[contains(@class,'enrollmentKit-wrapper')]/div[%s]//a[@class='enrollKit-close']";
	private String kitNameLoc = "//label[text()='%s']/preceding::input[1]";
	private String priceOfProductLoc = "//div[contains(@class,'product__listing')]//div[@class='product-item'][%s]//span[@id='cust_price']";
	private String socialMediaIconLoc = "//div[@class='container']//a[contains(@href,'%s')]";
	private String teamMemberNameLoc = "//div[@id='modal_front']/div[%s]//div[@class='title']/h4";
	private String categoryUnderShopSkinCareLoc = topNavigationLoc+"//a[@title='%s']";
	private String socialMediaLoc = "//div[contains(@class,'social-icons')]//a[contains(@href,'%s')]";

	private int randomLink =0;

	public boolean isFindAConsultantPagePresent(){
		String findAConsultantURL = "/find-consultant";
		return driver.isElementPresent(SPONSOR_SEARCH_FIELD_LOC)&& driver.getCurrentUrl().contains(findAConsultantURL);
	}

	/***
	 * This method click on enroll now button
	 * 
	 * @param 
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickEnrollNowButton(){
		driver.click(ENROLL_NOW_BUTTON_LOC);
		logger.info("clicked on 'Enroll now button'");
		return this;
	}

	/***
	 * This method click on first event calendar button
	 * 
	 * @param 
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontHomePage clickFirstEventCalendar(){
		driver.click(FIRST_EVENT_CALENDAR_LOC);
		logger.info("clicked on 'Event calendar button'");
		return this;
	}

	/***
	 * This method click on visit the block button
	 * 
	 * @param
	 * @return store front home page object
	 * 
	 */
	public StoreFrontHomePage clickVisitTheBlock(){
		driver.click(VISIT_THE_BLOG_LOC);
		logger.info("clicked on 'VISIT THE BLOG' button");
		return this;
	}

	/***
	 * This method click on social media icon
	 * 
	 * @param social media type
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage clickSocialMediaIcon(String mediaType){
		if(driver.isElementPresent(By.xpath(String.format(socialMediaIconLoc, mediaType)))){
			driver.click(By.xpath(String.format(socialMediaIconLoc, mediaType)));
		}
		else{
			driver.moveToElement(By.xpath(String.format(socialMediaLoc, mediaType)));
			driver.click(By.xpath(String.format(socialMediaLoc, mediaType)));
		}
		logger.info("clicked on"+mediaType+" icon");
		return this;
	}

	/***
	 * This method click on direct link association link
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage clickDirectLinkAssociationLink(){
		driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(DIRECT_LINK_ASSOCIATION));
		logger.info("clicked on 'direct link association' link");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method click on DSA code of ethics link
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage clickDsaCodeOfEthicsLink(){
		driver.click(DSA_CODE_OF_ETHICS_LINK);
		logger.info("clicked on 'DSA code of ethics' link");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method click on Donate now button link
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage clickDonateNow(){
		driver.click(DONATE_NOW_BUTTON_LOC);
		logger.info("clicked on 'Donate now' button");
		return this;
	}

	/***
	 * This method get error message for unable to found product
	 * 
	 * @param
	 * @return error message
	 * 
	 */
	public String getErrorMessageForSearchedProduct(){
		String errorMessage = driver.findElement(ERROR_MSG_TEXT_LOC).getText(); 
		logger.info("error is: "+errorMessage);
		return errorMessage;
	}

	/***
	 * This method verifies whether 'login or Register' page has displayed or not
	 * by checking the visibility of the text 'login or Register'
	 * 
	 * @return boolean
	 */
	public boolean isLoginOrRegisterPageDisplayed(){
		boolean isLoginOrRegisterPageDisplayed = driver.isElementVisible(LOGIN_OR_REGISTER_TXT_LOC);
		logger.info("is Login Or Register Page Displayed = "+isLoginOrRegisterPageDisplayed);
		return isLoginOrRegisterPageDisplayed;
	}

	/***
	 * This method verifies whether the Error Message For Incorrect Username Or Password
	 * 'Your username or password was incorrect.' has Displayed or not
	 * 
	 * @return boolean
	 */
	public boolean isErrorMessageForIncorrectUsernamePasswordDisplayed(){
		boolean isErrorMessageForIncorrectUsernamePasswordDisplayed = driver.isElementVisible(INCORRECT_USERNAME_PASSOWRD_TXT_LOC);
		logger.info("is Error Message For Incorrect Username Password Displayed = "+isErrorMessageForIncorrectUsernamePasswordDisplayed);
		return isErrorMessageForIncorrectUsernamePasswordDisplayed;
	}

	/***
	 * This method verify meet the doctors Page.
	 * 
	 * @param
	 * @return boolean value.
	 * 
	 */
	public boolean isMeetTheDoctorsPagePresent(){
		String meetTheDoctorsURL = "meet-the-doctors";
		return driver.isElementPresent(MEET_THE_DOCTORS_TXT_LOC)&& driver.getCurrentUrl().contains(meetTheDoctorsURL);
	}

	/***
	 * This method will click meet the "RF Executive team" link on "Meet the doctors" page.
	 * 
	 * @param 
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage clickMeetRFExecutiveTeamLinkOnMeetTheDoctorPage(){
		driver.click(MEET_RF_EXECUTIVE_TEAM_LINK_LOC);
		logger.info("clicked on 'Meet the R+F executive team' link on 'Meet the doctors' page");
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method click on random team member name link on executive team page.
	 * 
	 * @param
	 * @return random selected team member name.
	 * 
	 */
	public String clickAndReturnTeamMemberName(){
		int totalTeamMember = driver.findElements(TOTAL_TEAM_MEMBERS_IN_EXECUTIVE_TEAM_LOC).size();
		int randomNum = CommonUtils.getRandomNum(1, totalTeamMember);
		logger.info("Random team member selected is "+randomNum);
		String memberName = driver.findElement(By.xpath(String.format(teamMemberNameLoc, randomNum))).getText();
		driver.click(By.xpath(String.format(teamMemberNameLoc, randomNum)));
		logger.info("clicked on 'TEAM MEMBER NAME' link = "+memberName);
		driver.pauseExecutionFor(2000);
		return memberName;
	}

	/***
	 * This method get team member name from popup on executive team page.
	 * 
	 * @param
	 * @return team member name.
	 * 
	 */
	public String getTeamMemberNameFromPopup(){
		String memberName = driver.findElement(TEAM_MEMBER_NAME_FROM_POPUP_LOC).getText();
		logger.info("Fetched 'TEAM MEMBER NAME' from popup == "+memberName);
		return memberName;
	}

	/***
	 * This method will close team member detail popup on executive team page.
	 * 
	 * @param 
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage closeMemberDetailsPopup(){
		driver.click(CLOSE_ICON_MEMBER_DETAIL_POPUP_LOC);
		logger.info("clicked on close icon of member detail popup");
		return this;
	}

	/***
	 * This method enter the consultant enrollment details
	 * 
	 * @param First name,Last name, emailID, password, SSN number
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage enterConsultantEnrollmentDetails(String firstName, String lastName, String emailID, String password, String SSN){
		driver.type(FIRST_NAME_FOR_REGISTRATION_LOC, firstName);
		logger.info("Entered first name as "+firstName);
		driver.type(LAST_NAME_FOR_REGISTRATION_LOC, lastName);
		logger.info("Entered last name as "+lastName);
		driver.type(EMAIL_ID_FOR_REGISTRATION_LOC, emailID);
		logger.info("Entered email id as "+emailID);
		driver.type(PASSWORD_FOR_REGISTRATION_LOC, password);
		logger.info("Entered password as "+password);
		driver.type(CONFIRM_PASSWORD_FOR_REGISTRATION_LOC, password);
		logger.info("Entered confirm password as "+password);
		driver.type(SSN_FOR_REGISTRATION_LOC, SSN);
		logger.info("Entered SSN  as "+SSN);
		return this;
	}

	/***
	 * This method click the next button
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage clickNextButton(){
		driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(NEXT_BUTTON_LOC));
		logger.info("Next button clicked");
		driver.waitForPageLoad();
		driver.waitForLoadingImageToDisappear();
		return this;
	}
	
	/***
	 * This method will return the total number of
	 * kits displayed during consultant enrollment
	 * @return
	 */
	public int getTotalKitsDisplayedDuringConsultantEnrollment(){
		return driver.findElements(TOTAL_KITS_LOC).size();
	}

	/***
	 * This method randomly clicks on any of the kit's view details link
	 * @return
	 */
	public StoreFrontHomePage clickAnyViewDetailsLink(String kitNumber){
		driver.click(By.xpath(String.format(viewDetailsLinkLoc, kitNumber)));
		logger.info("clicked on link "+kitNumber);
		return this;
	}
	
	/***
	 * This method verifies if the kit description is
	 * visible or NOT after expanding it
	 * @param link
	 * @return
	 */
	public boolean isKitDetailsDisplayed(String kitNumber){
		return driver.isElementVisible(By.xpath(String.format(expandedKitDescriptionLoc, kitNumber)));
	}
	
	/***
	 * This method clicks on the close btn of the
	 * expanded kit details
	 * @param kitNumber
	 * @return
	 */
	public StoreFrontHomePage closeTheExpandedKitDetails(String kitNumber){
		driver.pauseExecutionFor(1000);
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement((By.xpath(String.format(closeBtnForKitDetailsLoc, kitNumber)))));
		logger.info("clicked on the close btn of the expanded kit details for kitNumber "+kitNumber);
		return this;
	}
	
	/***
	 * This method verifies if Next button on kit page is enabled or not
	 * @return boolean
	 */
	public boolean isNextButtonEnabledBeforeSelectingKit(){
		return driver.findElement(NEXT_BUTTON_LOC).isEnabled();
	}

	/***
	 * This method selects the North Dakota checkbox on kit page
	 * @return
	 */
	public StoreFrontHomePage selectNorthDakotaCheckBoxOnKitPage(){
		driver.click(NORTH_DAKOTA_CHKBOX_LOC);
		logger.info("North Dakota Checkbox selected");
		return this;
	}

	/***
	 * This method chooses personal result kit
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage chooseProductFromKitPage(){
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(PERSONAL_RESULTS_KIT_PAGE_LOC));
		logger.info("selected the personal result kit");
		return this;
	}

	public boolean areAllKitsDisabled(){
		boolean isKitDisabled = false;
		List<WebElement> allKits = driver.findElements(ALL_KIT_SECTION_LOC);
		for(WebElement e : allKits){
			if(e.getAttribute("style").contains("opacity")==true){
				isKitDisabled=true;
			}
		}
		return isKitDisabled;
	}

	/***
	 * This method choose specific product at kit page
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage chooseProductFromKitPage(String kitName){
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath(String.format(kitNameLoc,kitName.toUpperCase()))));
		logger.info("Choose "+kitName.toUpperCase()+" product at kit page");
		return this;
	}

	/***
	 * This method click the next button
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage clickSaveButton(){
		driver.pauseExecutionFor(3000);
		driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(SAVE_BUTTON_LOC));
		logger.info("Save button clicked");
		return this;
	}


	/***
	 * This method get the confirmation message of consultant enrollment
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public String getConfirmationMsgOfConsultantEnrollment(){
		String msg = driver.findElement(CONFIRMATION_MSG_OF_CONSULTANT_ENROLLMENT_LOC).getText();
		logger.info("Confirmation message is "+msg);
		return msg;
	}

	/***
	 * This method checks whether Welcome drop has displayed or not
	 * @return boolean
	 */
	public boolean isWelcomeUserElementDisplayed(){
		return driver.isElementVisible(WELCOME_DROPDOWN_LOC);
	}


	/***
	 * This method select the first billing address from DD
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage selectBillingAddressFromDD(){
		driver.click(BILLING_ADDRESS_DD_LOC);
		logger.info("Billing address dropdown clicked");
		driver.pauseExecutionFor(500);
		driver.click(BILLING_ADDRESS_OPTION_VALUE_LOC);
		logger.info("Billing address selected");
		return this;
	}

	/***
	 * This method verify add to bag button is present for first product
	 * 
	 * @param
	 * @return boolean value.
	 * 
	 */

	public boolean isAddToBagPresentOfFirstProduct(){
		driver.moveToElementByJS(ADD_TO_CART_FIRST_PRODUCT_LOC);
		return driver.findElement(ADD_TO_BAG_OF_FIRST_PRODUCT).isDisplayed();
	}

	/***
	 * This method verify add to cart button is present for first product
	 * 
	 * @param
	 * @return boolean value.
	 * 
	 */

	public boolean isAddToCartPresentForFirstProduct(){
		return driver.isElementVisible(ADD_TO_CART_FIRST_PRODUCT_LOC);
	}

	/***
	 * This method select the first filter option under shop by price filter
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage selectFirstOptionInShopByPriceFilter(){
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(SHOP_BY_PRICE_FILTER_LOC));
		logger.info("Shop by price dropdown clicked");
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(SHOP_BY_PRICE_FILTER_OPTION_0_TO_49$_LOC));
		logger.info("First option under shop by price filter selected");
		return this;
	}

	/***
	 * This method verify the first filter option under shop by price filter
	 * is checked or not
	 * 
	 * @param
	 * @return boolean value.
	 * 
	 */
	public boolean isShopByPriceFirstFilterChecked(){
		return driver.isElementPresent(SHOP_BY_PRICE_FILTER_OPTION_0_TO_49$_AFTER_CHECKED_LOC);
	}

	/***
	 * This method verify the filter option under shop by price filter
	 * is applied successfully or not
	 * 
	 * @param product number, price range
	 * @return boolean value.
	 * 
	 */
	public boolean isShopByPriceFilterAppliedSuccessfully(int productNumber, String priceRange){
		String price = driver.findElement(By.xpath(String.format(priceOfProductLoc, productNumber))).getText().split("\\$")[1].trim();
		double priceFromUI = Double.parseDouble(price);
		if(priceRange.equalsIgnoreCase("0To49")){
			if(priceFromUI>=0.00 & priceFromUI<=49.99){
				return true;
			}else{
				return false;
			}
		}else if(priceRange.equalsIgnoreCase("50To199")){
			if(priceFromUI>=50.00 & priceFromUI<=199.99){
				return true;
			}else{
				return false;
			}
		}else if(priceRange.equalsIgnoreCase("200To499")){
			if(priceFromUI>=200.00 & priceFromUI<=499.99){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	/***
	 * This method verify the first filter option under shop by price filter
	 * is removed successfully or not
	 * 
	 * @param total no of product, price range
	 * @return boolean value.
	 * 
	 */
	public boolean isShopByPriceFilterRemovedSuccessfully(int totalNoOfProduct, String priceRange){
		Double[] price = new Double[totalNoOfProduct]; 
		for(int i=1; i<=totalNoOfProduct; i++){
			price[i-1] = Double.parseDouble(driver.findElement(By.xpath(String.format(priceOfProductLoc, i))).getText().split("\\$")[1].trim());
		}
		List<Double> lList = Arrays.asList(price);
		Iterator<Double> iterator =  lList.iterator();
		while(iterator.hasNext()){
			if(priceRange.equalsIgnoreCase("0To49")){
				if(iterator.next()>50.0){
					return true;
				}
			}else if(priceRange.equalsIgnoreCase("50To199")){
				if(iterator.next()>200.0){
					return true;
				}
			}else if(priceRange.equalsIgnoreCase("200To499")){
				if(iterator.next()<200.0){
					return true;
				}
			}
		}
		return false;
	}

	/***
	 * This method select the second filter option under shop by price filter
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage selectSecondOptionInShopByPriceFilter(){
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(SHOP_BY_PRICE_FILTER_LOC));
		logger.info("Shop by price dropdown clicked");
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(SHOP_BY_PRICE_FILTER_OPTION_50_TO_199$_LOC));
		logger.info("Second option under shop by price filter selected");
		return this;
	}

	/***
	 * This method verify the second filter option under shop by price filter
	 * is checked or not
	 * 
	 * @param
	 * @return boolean value.
	 * 
	 */
	public boolean isShopByPriceSecondFilterChecked(){
		return driver.isElementPresent(SHOP_BY_PRICE_FILTER_OPTION_50_TO_199$_AFTER_CHECKED_LOC);
	}

	/***
	 * This method select the third filter option under shop by price filter
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage selectThirdOptionInShopByPriceFilter(){
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(SHOP_BY_PRICE_FILTER_LOC));
		logger.info("Shop by price dropdown clicked");
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(SHOP_BY_PRICE_FILTER_OPTION_200_TO_499$_LOC));
		logger.info("Third option under shop by price filter selected");
		return this;
	}

	/***
	 * This method verify the third filter option under shop by price filter
	 * is checked or not
	 * 
	 * @param
	 * @return boolean value.
	 * 
	 */

	public boolean isShopByPriceThirdFilterChecked(){
		return driver.isElementPresent(SHOP_BY_PRICE_FILTER_OPTION_200_TO_499$_AFTER_CHECKED_LOC);
	}

	/***
	 * This method click on place order button
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage clickPlaceOrderButton(){
		clickBecomeAConsultant();
		return this;
	}

	/***
	 * This method clicks on the Policies and Procedures Link
	 * @return store front Home page object
	 */
	public StoreFrontHomePage clickPoliciesAndProceduresLink(){
		driver.click(POLICIES_AND_PROCEDURES_LINK_LOC);
		logger.info("Policies and Procedures link clicked");
		return this;
	}

	/***
	 * This method clicks on the Pro Pulse Terms and Conditions Link
	 * @return store front Home page object
	 */
	public StoreFrontHomePage clickPulseProTermsAndConditionsLink(){
		driver.click(PULSE_PRO_T_C_LINK_LOC);
		logger.info("Pulse Pro Terms and conditions link clicked");
		return this;
	}

	/***
	 * This method clicks on the CRP Terms and Conditions Link
	 * @return store front Home page object
	 */
	public StoreFrontHomePage clickCRPTermsAndConditionsLink(){
		driver.click(CRP_T_C_LINK_LOC);
		logger.info("CRP Terms and conditions link clicked");
		return this;
	}

	/***
	 * This method clicks on the Connect btn on the home page
	 * @return storeFront home page object
	 */
	public StoreFrontHomePage clickConnectBtn(){
		driver.click(CONNECT_BTN_LOC);
		logger.info("Connect btn clicked on home page");
		return this;
	}

//	/***
//	 * This method verifies whether the Connect btn present on the home page or not
//	 * @return boolean
//	 */
//	public boolean isConnectBtnPresentOnHomePage(){
//		return driver.isElementVisible(CONNECT_BTN_LOC);		
//	}
	
	/***
	 * This method verifies whether or NOT
	 * Home page banner displayed or not
	 * 
	 * This method can  be used to verify that user is on Home Page
	 * @return
	 */
	public boolean isHomePageBannerDisplayed(){
		return driver.isElementVisible(By.xpath("//div[@class='hpage-banner-box']"));
	}

	/***
	 * This method clicks on the 'Applying as Business Entity' Link on sponsor page
	 * @return storeFront home page object
	 */
	public StoreFrontHomePage clickApplyingAsBusinessEntityLink(){
		driver.waitForElementPresent(APPLYING_AS_BUSINESS_ENTITY_LINK_LOC);
		driver.click(APPLYING_AS_BUSINESS_ENTITY_LINK_LOC);
		logger.info("clicked on 'Applying as Business Entity' Link");
		driver.pauseExecutionFor(500);
		return this;
	}

	/***
	 * This method clicks on the Cross(X) button of 'Applying as Business Entity' popup on sponsor page
	 * @return storeFront home page object
	 */
	public StoreFrontHomePage closeApplyingAsBusinessEntityPopUp(){
		driver.click(CLOSE_BTN_ON_APPLYING_AS_BUSINESS_ENTITY_POPUP_LOC);
		logger.info("closed the 'Applying as Business Entity' popup");
		return this;
	}

	/***
	 * This method checks whether 'Applying as Business Entity' popup on sponsor page
	 * has displayed or NOT
	 * @return boolean
	 */
	public boolean isApplyingAsBusinessEntityPopupDisplayed(){
		return driver.isElementVisible(APPLYING_AS_BUSINESS_ENTITY_POPUP_LOC);		
	}

	/***
	 * This method selects the Subscribe to Pulse checkbox
	 * @return SF home page object
	 */
	public StoreFrontHomePage selectSubscribeToPulseCheckBox(){
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(SUBSCRIBE_TO_PULSE_CHKBOX_LOC));
		logger.info("Subscribe to Pulse checkbox has been selected");
		return this;
	}

	/***
	 * This method enters the prefix in the orefix field during consultant enrollment
	 * @param prefix
	 * @return
	 */
	public StoreFrontHomePage enterPrefix(String prefix){
		driver.type(PREFIX_FIELD_LOC, prefix);
		return this;
	}

	/***
	 * This method checks if prefix is available or not
	 * @return
	 */
	public boolean isPrefixAvailable(){
		return driver.isElementVisible(PREFIX_AVAILABLE_LOC);
	}

	/***
	 * This method checks whether a pc has been enrolled successfully or not by verifying 
	 * i) order confirmation String in URL
	 * ii) Welcome Drop Down element
	 * iii) autoship element on the page
	 */
	public boolean hasPCEnrolledSuccessfully(){
		return driver.isElementVisible(WELCOME_DROPDOWN_LOC)
				&& driver.isElementVisible(AUTOSHIP_TEXT_LOC);		
	}

	/***
	 * This method checks whether a rc has been enrolled successfully or not by verifying 
	 * i) User is on Corp
	 * ii)Welcome Drop Down element
	 */
	public boolean hasRCEnrolledSuccessfully(){
		return driver.isElementVisible(WELCOME_DROPDOWN_LOC)
				&& driver.getCurrentUrl().contains("/orderConfirmation");
	}

	/***
	 * This method click on specific category under Shop skin care 
	 * @param
	 * @return StoreFrontShopSkinCarePage object
	 */

	public StoreFrontShopSkinCarePage clickOnCategoryFromShopSkinCare(String catTitle){
		mouseHoverOn(TestConstants.SHOP_SKINCARE);
		driver.click(By.xpath(String.format(categoryUnderShopSkinCareLoc, catTitle)));
		logger.info("clicked on " + catTitle);
		return new StoreFrontShopSkinCarePage(driver);
	}

	/***
	 * This method verifies search text box is displayed or not
	 * 
	 * 
	 * @return boolean
	 */
	public boolean isSearchTextBoxDisplayed(){
		driver.pauseExecutionFor(2000);
		return driver.isElementVisible(SEARCH_BOX);
	}
	/***
	 * This method clicks on the Cross(X) button of 'Applying as Business Entity' popup on sponsor page
	 * @return storeFront home page object
	 */
	public StoreFrontHomePage closeSearchTextBox(){
		driver.click(CLOSE_ICON_SEARCH_TEXT_BOX);
		logger.info("closed the 'search text box' overlay");
		return this;
	}

	/***
	 * This method enter search text in search textfield and click enter
	 * 
	 * @param String textToSearch
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontShopSkinCarePage searchEntityAndHitEnter(String textToSearch){
		driver.type(SEARCH_BOX, textToSearch);
		Actions action = new Actions(RFWebsiteDriver.driver);
		action.sendKeys(Keys.ENTER);
		action.perform();
		logger.info("Hit enter for searching entity");
		driver.pauseExecutionFor(5000);
		return new StoreFrontShopSkinCarePage(driver);
	}

	/***
	 * This method validates the search result products with that of search entity
	 * 
	 * @param String productName
	 * @return boolean
	 * 
	 */
	public boolean isExpecetedProductPresentInSearchResults(String expectedProductName){
		List<WebElement> searchResultsProducts = driver.findElements(SEARCH_RESULT_PRODUCTS_LOC);
		for(WebElement product : searchResultsProducts){
			String productName = null;
			productName = product.getText().trim();
			if(productName.contains(expectedProductName)){
				logger.info("Expected product name found in search list");
				return true;
			}
		}
		logger.info("Expected product name not found in search list");
		return false;
	}

}