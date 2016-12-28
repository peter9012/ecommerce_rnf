package com.rf.pages.website.rehabitat.storeFront;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

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
	private final By SEARCH_BOX = By.id("search-box");
	private final By SEARCH_ICON_NEAR_SEARCH_BOX = By.xpath("//input[@id='search-box']/preceding::span[1]");
	private final By ERROR_MSG_TEXT_LOC = By.xpath("//div[@class='content']//h2");
	private final By LOGIN_OR_REGISTER_TXT_LOC = By.xpath("//h1[contains(text(),'LOG IN OR REGISTER')]");
	private final By INCORRECT_USERNAME_PASSOWRD_TXT_LOC = By.xpath("//div[contains(@class,'alert-danger') and contains(text(),'') or contains(text(),'Your username or password was incorrect.')]");
	private final By MEET_THE_DOCTORS_TXT_LOC = By.xpath("//h1[text()=' Meet the Doctors']");
	private final By MEET_RF_EXECUTIVE_TEAM_LINK_LOC = By.xpath("//a[@href='/executive-team']");
	private final By TOTAL_TEAM_MEMBERS_IN_EXECUTIVE_TEAM_LOC = By.xpath("//div[@id='modal_front']//div[@class='title']");
	private final By TEAM_MEMBER_NAME_FROM_POPUP_LOC = By.xpath("//div[@class='item active']/descendant::h4[1]");
	private final By CLOSE_ICON_MEMBER_DETAIL_POPUP_LOC = By.xpath("//button[@class='close']");
	private final By BILLING_ADDRESS_DD_LOC= By.id("default-address");
	private final By BILLING_ADDRESS_OPTION_VALUE_LOC= By.xpath("//select[@id='default-address']//option[2]");
	private final By SAVE_BUTTON_LOC = By.id("deliveryAccountSubmit");
	private final By FIRST_PRODUCT_AT_KIT_PAGE_LOC = By.xpath("//input[@id='ENROLL_KIT_0002']");
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
	private final By TOTAL_NO_OF_PRODUCTS_LOC = By.xpath("//div[@class='product-item']");
	private final By SHOP_BY_PRICE_FILTER_OPTION_200_TO_499$_LOC = By.xpath("//input[@id='$200-$499.99ID']/..");
	private final By SHOP_BY_PRICE_FILTER_OPTION_200_TO_499$_AFTER_CHECKED_LOC = By.xpath("//input[@id='$200-$499.99ID'][@checked = 'checked']");
	private final By MINI_CART_NUMBER_OF_ITEMS_LOC = By.xpath("//span[@class='nav-items-total']");
	private final By WELCOME_USER_LOC = By.xpath("//div[@class='loginBlock']/div");
	private final By CONFIRMATION_MSG_OF_CONSULTANT_ENROLLMENT = By.xpath("//div[@class='global-alerts']/div");

	private String kitNameLoc = "//label[text()='%s']/preceding::input[1]";
	private String priceOfProductLoc = "//div[contains(@class,'product__listing')]//div[@class='product-item'][%s]//span[@id='cust_price']";
	private String socialMediaIconLoc = "//div[@class='container']//a[contains(@href,'%s')]";
	private String teamMemberName = "//div[@id='modal_front']/div[%s]//div[@class='title']/h4";

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
		driver.click(By.xpath(String.format(socialMediaIconLoc, mediaType)));
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
		driver.click(DIRECT_LINK_ASSOCIATION);
		logger.info("clicked on 'direct link association' link");
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
	 * This method search a product through search Icon
	 * 
	 * @param product name
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage searchProduct(String productName){
		driver.type(SEARCH_BOX, productName);
		driver.click(SEARCH_ICON_NEAR_SEARCH_BOX);
		logger.info("clicked on 'Search icon' for search a product");
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
		boolean isLoginOrRegisterPageDisplayed = driver.isElementVisible(driver.findElement(LOGIN_OR_REGISTER_TXT_LOC));
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
		boolean isErrorMessageForIncorrectUsernamePasswordDisplayed = driver.isElementVisible(driver.findElement(INCORRECT_USERNAME_PASSOWRD_TXT_LOC));
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
		String memberName = driver.findElement(By.xpath(String.format(teamMemberName, randomNum))).getText();
		driver.click(By.xpath(String.format(teamMemberName, randomNum)));
		logger.info("clicked on 'TEAM MEMBER NAME' link = "+memberName);
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
	 * This method choose first product at kit page
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage chooseProductFromKitPage(){
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(FIRST_PRODUCT_AT_KIT_PAGE_LOC));
		logger.info("Choose first product at kit page");
		return this;
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
		String msg = driver.findElement(CONFIRMATION_MSG_OF_CONSULTANT_ENROLLMENT).getText();
		logger.info("Confirmation message is "+msg);
		return msg;
	}

	public boolean isWelcomeUserElementDisplayed(){
		System.out.println("*** "+driver.isElementVisible(WELCOME_USER_LOC));
		return driver.isElementVisible(WELCOME_USER_LOC);
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
		driver.pauseExecutionFor(5000);
		driver.moveToElementByJS(ADD_TO_CART_FIRST_PRODUCT_LOC);
		driver.pauseExecutionFor(5000);
		return driver.findElement(ADD_TO_BAG_OF_FIRST_PRODUCT).isDisplayed();
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
		//driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(SHOP_BY_PRICE_FILTER_LOC));
		return driver.isElementPresent(SHOP_BY_PRICE_FILTER_OPTION_0_TO_49$_AFTER_CHECKED_LOC);
	}

	/***
	 * This method get total no of product
	 * 
	 * @param
	 * @return total no of product
	 * 
	 */
	public int getTotalNoOfProduct(){
		int totalNoOfProducts = driver.findElements(TOTAL_NO_OF_PRODUCTS_LOC).size(); 
		logger.info("Total no of product is: "+totalNoOfProducts);
		return totalNoOfProducts;
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
			if(priceFromUI>0.00 & priceFromUI<49.99){
				return true;
			}else{
				return false;
			}
		}else if(priceRange.equalsIgnoreCase("50To199")){
			if(priceFromUI>50.00 & priceFromUI<199.99){
				return true;
			}else{
				return false;
			}
		}else if(priceRange.equalsIgnoreCase("200To499")){
			if(priceFromUI>200.00 & priceFromUI<499.99){
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

	private final By SHOP_BY_PRICE_FILTER_OPTION_50_TO_199$_LOC = By.xpath("//input[@id='$50-$199.99ID']/..");
	private final By SHOP_BY_PRICE_FILTER_OPTION_50_TO_199$_AFTER_CHECKED_LOC = By.xpath("//input[@id='$50-$199.99ID'][@checked = 'checked']");

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
	 * This method get total no of itme in mini cart
	 * 
	 * @param
	 * @return no of item
	 * 
	 */
	public String getNumberOfItemFromMiniCart(){
		String noOfItem = driver.findElement(MINI_CART_NUMBER_OF_ITEMS_LOC).getText(); 
		logger.info("error is: "+noOfItem);
		return noOfItem;
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
}