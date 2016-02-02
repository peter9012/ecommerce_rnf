package com.rf.pages.website.storeFront;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.model.Site;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.RFBasePage;


public class StoreFrontRFWebsiteBasePage extends RFBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontRFWebsiteBasePage.class.getName());

	private final By RODAN_AND_FIELDS_IMG_LOC = By.xpath("//div[@id='header-logo']//a");
	private final By WELCOME_DD_EDIT_CRP_LINK_LOC = By.xpath("//a[contains(text(),'Edit')]");
	private final By WELCOME_USER_DD_LOC = By.id("account-info-button");
	private final By WELCOME_DD_ORDERS_LINK_LOC = By.xpath("//a[text()='Orders']");
	private final By YOUR_ACCOUNT_DROPDOWN_LOC = By.xpath("//button[@class='btn btn-default dropdown-toggle']");
	private final By WELCOME_DD_BILLING_INFO_LINK_LOC = By.linkText("Billing Info");
	private final By WELCOME_DD_SHIPPING_INFO_LINK_LOC = By.linkText("Shipping Info");
	private final By ADD_NEW_SHIPPING_LINK_LOC = By.xpath("//a[@class='add-new-shipping-address']");
	private final By WELCOME_DD_ACCOUNT_INFO_LOC = By.xpath("//a[text()='Account Info']");
	private final By ADD_NEW_BILLING_CARD_NUMBER_LOC = By.id("card-nr");
	private final By UPDATE_CART_BTN_LOC = By.xpath("//input[@value='UPDATE CART']");

	protected RFWebsiteDriver driver;
	private String RFO_DB = null;
	public StoreFrontRFWebsiteBasePage(RFWebsiteDriver driver){		
		super(driver);
		this.driver = driver;
	}

	//contains the common methods useful for all the pages inherited

	public static String convertDBDateFormatToUIFormat(String DBDate){
		String UIMonth=null;
		String[] splittedDate = DBDate.split(" ");
		String date = (splittedDate[0].split("-")[2].charAt(0))=='0'?splittedDate[0].split("-")[2].split("0")[1]:splittedDate[0].split("-")[2];
		String month = (splittedDate[0].split("-")[1].charAt(0))=='0'?splittedDate[0].split("-")[1].split("0")[1]:splittedDate[0].split("-")[1];		
		String year = splittedDate[0].split("-")[0];		
		switch (Integer.parseInt(month)) {		
		case 1:
			UIMonth="January";
			break;
		case 2:
			UIMonth="February";
			break;
		case 3:
			UIMonth="March";
			break;
		case 4:
			UIMonth="April";
			break;
		case 5:
			UIMonth="May";
			break;
		case 6:
			UIMonth="June";
			break;
		case 7:
			UIMonth="July";
			break;
		case 8:
			UIMonth="August";
			break;
		case 9:
			UIMonth="September";
			break;
		case 10:
			UIMonth="October";
			break;
		case 11:
			UIMonth="November";
			break;
		case 12:
			UIMonth="December";
			break;		
		}

		return UIMonth+" "+date+", "+year;
	}

	public void clickOnShopLink(){
		driver.waitForElementPresent(By.id("our-products"));
		driver.click(By.id("our-products"));
		logger.info("Shop link clicked ");
	}

	public void clickOnAllProductsLink(){
		try{
			//driver.waitForElementPresent(By.xpath("//a[@title='All Products']"));
			//driver.click(By.xpath("//a[@title='All Products']"));
			driver.moveToELement(By.xpath("//*[@id='header']//A[@id='our-products']"));
			//driver.waitForElementPresent(By.xpath("//A[contains(text(),'All Products')]"));
			driver.moveToELement(By.xpath("//A[contains(text(),'All Products')]"));
			driver.click(By.xpath("//A[contains(text(),'All Products')]"));
		}catch(NoSuchElementException e){
			logger.info("All products link was not present");
			driver.click(By.xpath("//div[@id='dropdown-menu']//a[@href='/us/quick-shop/quickShop']"));
		}
		logger.info("All products link clicked "+"//a[@title='All Products']");	
		driver.waitForPageLoad();
	}

	public StoreFrontUpdateCartPage clickOnQuickShopImage(){
		driver.waitForElementToBeVisible(By.xpath("//a[@href='/us/quick-shop/quickShop' and @title='']"), 30);
		driver.waitForElementPresent(By.xpath("//a[@href='/us/quick-shop/quickShop' and @title='']"));
		driver.click(By.xpath("//a[@href='/us/quick-shop/quickShop' and @title='']"));
		logger.info("Quick shop Img link clicked "+"//a[@href='/us/quick-shop/quickShop' and @title='']");
		driver.waitForPageLoad();
		return new StoreFrontUpdateCartPage(driver);
	}

	public boolean areProductsDisplayed(){
		driver.waitForElementPresent(By.xpath("//div[contains(@class,'quickshop-section')]"));
		return driver.isElementPresent(By.xpath("//div[contains(@class,'quickshop-section')]"));
	}

	public void selectProductAndProceedToBuy() throws InterruptedException{
		applyPriceFilterHighToLow();
		driver.waitForPageLoad();
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
			if(driver.findElement(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button")).isEnabled()==true)
				driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
			else
				driver.click(By.xpath("//div[@id='main-content']/div[5]/div[2]//form[@id='productDetailForm']/button"));
			logger.info("Add To Bag button clicked");
			driver.waitForLoadingImageToDisappear();
			driver.waitForPageLoad();

		}
		else if(driver.getCountry().equalsIgnoreCase("US")){
			try{
				driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
				driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
			}catch(Exception e){
				driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[1]//form[@id='productDetailForm']/button"));
				driver.click(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[1]//form[@id='productDetailForm']/button"));
			}
			logger.info("Add To Bag button clicked");
			driver.waitForLoadingImageToDisappear();
			driver.waitForPageLoad();
		}

	}

	public void selectProductAndProceedToBuyWithoutFilter() throws InterruptedException{
		driver.waitForPageLoad();
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
			if(driver.findElement(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button")).isEnabled()==true)
				driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
			else
				driver.click(By.xpath("//div[@id='main-content']/div[5]/div[2]//form[@id='productDetailForm']/button"));
			logger.info("Add To Bag button clicked");
			driver.waitForLoadingImageToDisappear();
			driver.waitForPageLoad();

		}
		else if(driver.getCountry().equalsIgnoreCase("US")){
			try{
				driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
				driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
			}catch(Exception e){
				driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[1]//form[@id='productDetailForm']/button"));
				driver.click(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[1]//form[@id='productDetailForm']/button"));
			}
			logger.info("Add To Bag button clicked");
			driver.waitForLoadingImageToDisappear();
			driver.waitForPageLoad();
		}

	}

	public void selectProductAndProceedToAddToCRP() throws InterruptedException{
		driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Add to crp']"));
		if(driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Add to crp']")).isEnabled()==true)
			driver.click(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Add to crp']"));
		else
			driver.click(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[2]//input[@value='Add to crp']"));
		logger.info("Add to CRP button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnAddToPcPerksButton(){
		driver.waitForPageLoad();
		if(driver.getCountry().equalsIgnoreCase("us")){
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div[1]//input[@value='ADD to PC Perks']"));
			driver.click(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div[1]//input[@value='ADD to PC Perks']"));
		}else{
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//input[@value='ADD to PC Perks']"));
			driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]//input[@value='ADD to PC Perks']"));
		}

		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='OK']"));
			driver.click(By.xpath("//input[@value='OK']"));
			driver.waitForLoadingImageToDisappear();
		}catch(Exception e){

		}
	}

	public void addQuantityOfProduct(String qty) throws InterruptedException{
		driver.pauseExecutionFor(1000);
		try{
			driver.waitForElementPresent(By.id("quantity0"));
			driver.clear(By.id("quantity0"));
			driver.type(By.id("quantity0"),qty);
			logger.info("quantity added is "+qty);
			driver.click(By.xpath("//div[@id='left-shopping']/div//a[@class='updateLink']"));
			logger.info("Update button clicked after adding quantity");
		}catch(NoSuchElementException e){
			driver.waitForElementPresent(By.id("quantity1"));
			driver.clear(By.id("quantity1"));
			driver.type(By.id("quantity1"),qty);
			logger.info("quantity added is "+qty);
			driver.click(By.xpath("//div[@id='left-shopping']/div//a[@class='updateLink']"));
			logger.info("Update button clicked after adding quantity");
		}
		driver.pauseExecutionFor(1000);
		driver.waitForPageLoad();
	}

	public void clickOnNextBtnAfterAddingProductAndQty() throws InterruptedException{
		driver.waitForElementPresent(By.id("submitForm"));
		driver.click(By.id("submitForm"));
		logger.info("Next button after adding quantity clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public boolean isCartPageDisplayed(){
		driver.waitForElementPresent(By.xpath("//div[@id='left-shopping']/h1"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='left-shopping']/h1")));
	}

	public void addAnotherProduct() throws InterruptedException{
		try{
			driver.waitForElementPresent(By.xpath("//div[@id='left-shopping']/p/a[contains(text(),'Continue shopping')]"));
			driver.click(By.xpath("//div[@id='left-shopping']/p/a[contains(text(),'Continue shopping')]"));
			logger.info("Continue shopping link clicked");
			driver.pauseExecutionFor(2000);
			driver.waitForPageLoad();
		}catch(Exception e){
			Actions action = new Actions(RFWebsiteDriver.driver);
			driver.quickWaitForElementPresent(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"));
			action.moveToElement(driver.findElement(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"))).doubleClick(driver.findElement(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"))).build().perform();
			//driver.click(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"));
			logger.info("Continue shopping link clicked");
			driver.pauseExecutionFor(2000);
			driver.waitForPageLoad();
		}
		//  driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
		applyPriceFilterHighToLow();
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[2]/div[2]/div[1]//form[@id='productDetailForm']/button"));
			driver.click(By.xpath("//div[@id='main-content']/div[5]/div[2]/div[2]/div[1]//form[@id='productDetailForm']/button"));
			logger.info("Buy Now button clicked and another product selected");
			driver.waitForPageLoad();
		}
		else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.waitForElementPresent(By.xpath("//*[@id='main-content']/div[5]/div[2]//button"));
			driver.click(By.xpath("//*[@id='main-content']/div[5]/div[2]//button"));
			logger.info("Buy Now button clicked and another product selected");
			driver.waitForPageLoad();
		}
	}

	public boolean verifyNumberOfProductsInCart(String numberOfProductsInCart){
		driver.waitForElementPresent(By.xpath("//div[@id='left-shopping']/h1/span"));
		return driver.findElement(By.xpath("//div[@id='left-shopping']/h1/span")).getText().contains(numberOfProductsInCart);
	}

	public void clickOnCheckoutButton(){
		driver.waitForElementPresent(By.xpath("//input[@value='PLACE ORDER']"));
		driver.click(By.xpath("//input[@value='PLACE ORDER']"));
		logger.info("checkout button clicked");
		driver.waitForPageLoad();
	}

	public boolean isLoginOrCreateAccountPageDisplayed(){
		driver.waitForElementPresent(By.xpath("//h1[text()='Log in or register']"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//h1[text()='Log in or register']")));
	}

	public void enterNewRCDetails(String firstName,String lastName,String password) throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String emailAddress = firstName+randomNum+"@xyz.com";
		driver.type(By.id("first-Name"),firstName);
		logger.info("first name entered as "+firstName);
		driver.type(By.id("last-name"),lastName);
		logger.info("last name entered as "+lastName);
		driver.clear(By.id("email-account"));
		driver.type(By.id("email-account"),emailAddress+"\t");
		logger.info("email entered as "+emailAddress);
		driver.pauseExecutionFor(1000);
		driver.waitForSpinImageToDisappear();
		driver.type(By.id("password"),password);
		logger.info("password entered as "+password);
		driver.type(By.id("the-password-again"),password);
		logger.info("confirm password entered as "+password);
		driver.click(By.id("next-button"));		
		logger.info("Create New Account button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	//overloaded method for email address
	public void enterNewRCDetails(String firstName,String lastName,String emailAddress,String password) throws InterruptedException{
		driver.type(By.id("first-Name"),firstName);
		logger.info("first name entered as "+firstName);
		driver.type(By.id("last-name"),lastName);
		logger.info("last name entered as "+lastName);
		driver.clear(By.id("email-account"));
		driver.type(By.id("email-account"),emailAddress+"\t");
		logger.info("email entered as "+emailAddress);
		driver.pauseExecutionFor(1000);
		driver.waitForSpinImageToDisappear();
		driver.type(By.id("password"),password);
		logger.info("password entered as "+password);
		driver.type(By.id("the-password-again"),password);
		logger.info("confirm password entered as "+password);
		driver.click(By.id("next-button"));  
		logger.info("Create New Account button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public void enterNewPCDetails(String firstName,String lastName,String password) throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String emailAddress = firstName+randomNum+"@xyz.com";
		driver.type(By.id("first-Name"),firstName);
		logger.info("first name entered as "+firstName);
		driver.type(By.id("last-name"),lastName);
		logger.info("last name entered as "+lastName);
		driver.clear(By.id("email-account"));
		driver.type(By.id("email-account"),emailAddress+"\t");
		logger.info("email entered as "+emailAddress);
		driver.pauseExecutionFor(1000);
		driver.waitForSpinImageToDisappear();
		driver.type(By.id("password"),password);
		logger.info("password entered as "+password);
		driver.type(By.id("the-password-again"),password);
		logger.info("confirm password entered as "+password);
		driver.click(By.xpath("//input[@id='become-pc']/.."));
		logger.info("check box for PC user checked");
		driver.click(By.xpath("//input[@id='next-button']"));		
		logger.info("Create New Account button clicked");		
	}

	public void enterNewPCDetails(String firstName,String lastName,String password, String emailID) throws InterruptedException{
		driver.type(By.id("first-Name"),firstName);
		logger.info("first name entered as "+firstName);
		driver.type(By.id("last-name"),lastName);
		logger.info("last name entered as "+lastName);
		driver.clear(By.id("email-account"));
		driver.type(By.id("email-account"),emailID+"\t");
		logger.info("email entered as "+emailID);
		driver.pauseExecutionFor(1000);
		driver.waitForSpinImageToDisappear();
		driver.type(By.id("password"),password);
		logger.info("password entered as "+password);
		driver.type(By.id("the-password-again"),password);
		logger.info("confirm password entered as "+password);
		driver.click(By.xpath("//input[@id='become-pc']/.."));
		logger.info("check box for PC user checked");
		driver.click(By.xpath("//input[@id='next-button']"));  
		logger.info("Create New Account button clicked"); 
		driver.waitForLoadingImageToDisappear();
	}

	public boolean isPopUpForPCThresholdPresent() throws InterruptedException{
		boolean isPopUpForPCThresholdPresent=false;
		driver.waitForElementPresent(By.xpath("//div[@id='popup-content']//p[contains(text(),'Please add products')]"));
		isPopUpForPCThresholdPresent = driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='popup-content']//p[contains(text(),'Please add products')]")));
		if(isPopUpForPCThresholdPresent==true){
			driver.click(By.xpath("//div[@id='popup-content']//input"));
			return true;
		}
		return false;
	}

	public boolean isCheckoutPageDisplayed(){
		driver.waitForElementPresent(By.xpath("//h1[contains(text(),'Checkout')]"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//h1[contains(text(),'Checkout')]")));
	}

	public void enterMainAccountInfo(){
		driver.pauseExecutionFor(5000);
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.type(By.id("address.line1"),TestConstants.ADDRESS_LINE_1_CA);
			logger.info("Address Line 1 entered is "+TestConstants.ADDRESS_LINE_1_CA);
			driver.type(By.id("address.townCity"),TestConstants.CITY_CA+"\t");
			logger.info("City entered is "+TestConstants.CITY_CA);
			driver.waitForLoadingImageToDisappear();
			try{
				driver.click(By.xpath("//form[@id='addressForm']/div[@class='row'][1]//select[@id='state']"));
				driver.waitForElementPresent(By.xpath("//form[@id='addressForm']/div[@class='row'][1]//select[@id='state']/option[2]"));
				driver.click(By.xpath("//form[@id='addressForm']/div[@class='row'][1]//select[@id='state']/option[2]"));
			}catch(Exception e){
				driver.click(By.id("state"));
				driver.waitForElementPresent(By.xpath("//select[@id='state']/option[2]"));
				driver.click(By.xpath("//select[@id='state']/option[2]"));	
			}	
			logger.info("state selected");
			driver.type(By.id("address.postcode"),TestConstants.POSTAL_CODE_CA);
			logger.info("postal code entered is "+TestConstants.POSTAL_CODE_CA);
			driver.type(By.id("address.phonenumber"),TestConstants.PHONE_NUMBER);
			logger.info("phone number entered is "+TestConstants.PHONE_NUMBER);
		}
		else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.type(By.id("address.line1"),TestConstants.ADDRESS_LINE_1_US);
			logger.info("Address line 1 entered is "+TestConstants.ADDRESS_LINE_1_US);
			driver.type(By.id("address.townCity"),TestConstants.CITY_US);
			logger.info("City entered is "+TestConstants.CITY_US);
			driver.click(By.id("state"));
			driver.waitForElementPresent(By.xpath("//select[@id='state']/option[2]"));
			driver.click(By.xpath("//select[@id='state']/option[2]"));
			logger.info("state selected");
			driver.type(By.id("address.postcode"),TestConstants.POSTAL_CODE_US);
			logger.info("postal code entered is "+TestConstants.POSTAL_CODE_US);
			driver.type(By.id("address.phonenumber"),TestConstants.PHONE_NUMBER_US);
			logger.info("phone number entered is "+TestConstants.PHONE_NUMBER_US);
		}

	}

	public void enterMainAccountInfo(String address1,String city,String province,String postalCode,String phoneNumber){
		driver.waitForElementPresent(By.id("address.line1"));
		driver.type(By.id("address.line1"),address1);
		logger.info("Address Line 1 entered is "+address1);
		driver.type(By.id("address.townCity"),city);
		logger.info("City entered is "+city);
		driver.waitForElementPresent(By.id("state"));
		driver.click(By.id("state"));
		driver.waitForElementPresent(By.xpath("//select[@id='state']/option[contains(text(),'"+province+"')]"));
		driver.click(By.xpath("//select[@id='state']/option[contains(text(),'"+province+"')]"));
		logger.info("state selected");
		driver.type(By.id("address.postcode"),postalCode);
		logger.info("postal code entered is "+postalCode);
		driver.type(By.id("address.phonenumber"),phoneNumber);
		logger.info("phone number entered is "+phoneNumber);
	}

	public void clickOnContinueWithoutSponsorLink() throws InterruptedException{
		driver.waitForElementPresent(By.id("continue-no-sponsor"));
		driver.click(By.id("continue-no-sponsor"));	
		logger.info("continue without sponsor link clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnNextButtonAfterSelectingSponsor() throws InterruptedException{
		driver.waitForElementPresent(By.id("saveAccountAddress"));
		driver.click(By.id("saveAccountAddress"));
		logger.info("Next button after selecting sponsor is clicked");
		driver.waitForLoadingImageToDisappear();
		try{
			driver.quickWaitForElementPresent(By.id("QAS_AcceptOriginal"));
			driver.click(By.id("QAS_AcceptOriginal"));
			logger.info("Accept as original button clicked");
			driver.waitForLoadingImageToDisappear();
		}catch(NoSuchElementException e){

		}		
	}

	public void clickOnShippingAddressNextStepBtn() throws InterruptedException{
		Actions action = new Actions(RFWebsiteDriver.driver);
		driver.waitForElementPresent(By.id("saveShippingInfo"));
		action.moveToElement(driver.findElement(By.id("saveShippingInfo"))).click(driver.findElement(By.id("saveShippingInfo"))).build().perform();
		logger.info("Next button on shipping address clicked");		
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
	}

	public void enterNewBillingCardNumber(String cardNumber){
		driver.waitForElementPresent(By.id("card-nr"));
		driver.type(By.id("card-nr"), cardNumber);
		logger.info("Billing card number entered is "+cardNumber);
	}

	public void enterNewBillingNameOnCard(String nameOnCard){
		driver.waitForElementPresent(By.id("card-name"));
		driver.clear(By.id("card-name"));
		driver.type(By.id("card-name"),nameOnCard);
		logger.info("card name entered is "+nameOnCard);
	}

	public void selectNewBillingCardExpirationDate(){
		driver.click(By.id("expiryMonth"));
		driver.waitForElementPresent(By.xpath("//select[@id='expiryMonth']/option[10]"));
		driver.click(By.xpath("//select[@id='expiryMonth']/option[10]"));
		driver.click(By.id("expiryYear"));
		driver.waitForElementPresent(By.xpath("//select[@id='expiryYear']/option[10]"));
		driver.click(By.xpath("//select[@id='expiryYear']/option[10]"));
		logger.info("expiration date is selected");
	}

	public void enterNewBillingSecurityCode(String securityCode){
		driver.type(By.id("security-code"), securityCode);
		logger.info("security code entered is "+securityCode);
	}

	public void selectNewBillingCardAddress() throws InterruptedException{
		driver.waitForElementPresent(By.id("addressBookdropdown"));
		driver.click(By.id("addressBookdropdown"));
		driver.click(By.xpath("//*[@id='addressBookdropdown']/option[1]"));
		logger.info("New Billing card address selected");
	}

	public void clickOnSaveBillingProfile() throws InterruptedException{
		driver.waitForElementPresent(By.id("submitButton"));
		driver.click(By.id("submitButton"));
		driver.waitForLoadingImageToDisappear();
		logger.info("Save billing profile button clicked");
	}



	public void clickOnBillingNextStepBtn() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='payment-next-button']/input"));
		driver.click(By.xpath("//div[@id='payment-next-button']/input"));
		logger.info("Next button on billing profile clicked");	
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
	}

	public void clickOnSetupCRPAccountBtn() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@value='Setup CRP Account']"));
		driver.click(By.xpath("//ul[@style='cursor: pointer;']/li[1]/div"));
		driver.click(By.xpath("//form[@id='placeOrderForm1']//a[contains(text(),'I agree that this agreement shall be accepted electronically.')]/ancestor::strong[1]/preceding::div[1]"));
		driver.click(By.xpath("//input[@value='Setup CRP Account']"));
		logger.info("Next button on billing profile clicked");
	}

	public void clickPlaceOrderBtn()throws InterruptedException{
		driver.waitForElementPresent(By.id("placeOrderButton"));
		driver.click(By.id("placeOrderButton"));
		logger.info("Place order button clicked");
		driver.waitForLoadingImageToDisappear();
		try{
			switchToPreviousTab();
		}catch(Exception e){

		}
		driver.waitForPageLoad();		
	}

	public void switchToPreviousTab(){
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
		driver.close();
		driver.switchTo().window(tabs.get(0));
		driver.pauseExecutionFor(1000);
	}

	public void clickOnRodanAndFieldsLogo(){
		driver.pauseExecutionFor(2000);
		try{
			driver.turnOffImplicitWaits();
			driver.quickWaitForElementPresent(RODAN_AND_FIELDS_IMG_LOC);
			driver.click(RODAN_AND_FIELDS_IMG_LOC);
		}catch(NoSuchElementException e){
			try{
				driver.click(By.xpath("//img[@title='Rodan+Fields']"));
			}catch(NoSuchElementException e1){
				driver.click(By.xpath("//div[@id='header-middle-top']//a"));
			}
		}
		finally{
			driver.turnOnImplicitWaits();
		}
		logger.info("Rodan and Fields logo clicked");	
		driver.waitForPageLoad();
	}

	public boolean isOrderPlacedSuccessfully(){
		driver.waitForElementPresent(By.xpath("//h1[contains(text(),'Thank you')]"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//h1[contains(text(),'Thank you')]")));
	}

	public boolean verifyWelcomeDropdownToCheckUserRegistered(){		
		driver.waitForElementPresent(By.id("account-info-button"));
		return driver.isElementPresent(By.id("account-info-button"));
		//driver.findElement(By.xpath("//div[@id='account-info-button']/a")).getText().contains("Welcome");
	}

	public void applyPriceFilterLowToHigh() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//select[@id='sortOptions']"));
		driver.click(By.xpath("//select[@id='sortOptions']"));
		driver.click(By.xpath("//select[@id='sortOptions']/option[3]"));
		logger.info("filter done for low to high price");
	}

	public boolean verifyPCPerksTermsAndConditionsPopup() throws InterruptedException{
		boolean isPCPerksTermsAndConditionsPopup = false;
		driver.waitForElementPresent(By.xpath("//div[@id='pcperktermsconditions']//input[@value='Ok']"));
		isPCPerksTermsAndConditionsPopup = driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='pcperktermsconditions']//input[@value='Ok']")));
		if(isPCPerksTermsAndConditionsPopup==true){
			driver.click(By.xpath("//div[@id='pcperktermsconditions']//input[@value='Ok']"));
			return true;
		}
		return false;
	}
	public void clickOnPCPerksTermsAndConditionsCheckBoxes(){
		//driver.waitForElementToBeClickable(By.xpath("//form[@id='placeOrderForm1']/ul/div[@class='content'][1]/li[1]//input"), 15);
		driver.pauseExecutionFor(3000);
		try{
			driver.quickWaitForElementPresent(By.xpath("//div[@class='content']/li[2]//input/.."));
			driver.click(By.xpath("//div[@class='content']/li[1]//input/.."));
			driver.click(By.xpath("//div[@class='content']/li[2]//input/.."));
		}catch(NoSuchElementException e){
			driver.waitForElementPresent(By.xpath("//input[@id='Terms2']/.."));
			driver.click(By.xpath("//input[@id='Terms2']/.."));
			driver.click(By.xpath("//input[@id='Terms3']/.."));
		}
	}
	public void selectNewBillingCardExpirationDateAsExpiredDate(){
		driver.click(By.id("expiryMonth"));
		driver.waitForElementPresent(By.xpath("//select[@id='expiryMonth']/option[@value='02']"));
		driver.click(By.xpath("//select[@id='expiryMonth']/option[@value='02']"));
		driver.click(By.id("expiryYear"));
		driver.waitForElementPresent(By.xpath("//select[@id='expiryYear']/option[2]"));
		driver.click(By.xpath("//select[@id='expiryYear']/option[1]"));
	}

	public boolean validatePasswordFieldMessage(){
		if(driver.findElement(By.xpath("//div[contains(text(),'Please enter 6')]")).isDisplayed()){
			return true;
		}
		else{
			return false;
		}
	}
	public void clearPasswordField(){
		driver.clear(By.id("new-password-account"));
		logger.info("password field cleared");
	}

	public boolean recurringMonthlyChargesSection() {
		driver.quickWaitForElementPresent(By.xpath("//h3[contains(text(),'Recurring Monthly Charges')]"));
		return driver.findElement(By.xpath("//h3[contains(text(),'Recurring Monthly Charges')]")).isDisplayed();
	}

	public boolean pulseSubscriptionTextbox() {
		driver.waitForElementPresent(By.id("webSitePrefix"));
		return driver.findElement(By.id("webSitePrefix")).isEnabled();
	}

	public void clickOnAllowMySpouseOrDomesticPartnerCheckbox() {
		boolean status = false;
		//driver.waitForElementToBeVisible(By.xpath("//input[@id='spouse-check']"), 15);
		try{
			WebElement checkbox=driver.findElement(By.xpath("//input[@id='spouse-check']/.."));
			status=checkbox.isSelected();
			if(status==false){
				driver.click(By.xpath("//input[@id='spouse-check']/.."));
			}
		}catch(NoSuchElementException e){
			try{
				WebElement checkboxLoc=driver.findElement(By.xpath("//input[@id='enrollAllowSpouse1']/.."));
				status=checkboxLoc.isSelected();
				if(status==false){
					driver.click(By.xpath("//input[@id='enrollAllowSpouse1']/.."));
				}
			}catch(Exception e1){

			}
		}
	}

	public void enterSpouseFirstName(String firstName){
		driver.waitForElementPresent(By.id("spouse-first"));
		driver.type(By.id("spouse-first"),firstName);
		logger.info("Spouse first name entered as "+firstName);
	}

	public void enterSpouseLastName(String firstName){
		driver.waitForElementPresent(By.id("spouse-last"));
		driver.type(By.id("spouse-last"),firstName);
		logger.info("Spouse last name entered as "+firstName);
	}

	public void clickOnRequestASponsorBtn(){
		try{
			driver.waitForElementPresent(By.xpath("//input[@value='Request a sponsor']"));
			driver.click(By.xpath("//input[@value='Request a sponsor']"));
			driver.waitForLoadingImageToDisappear();
		}catch(Exception e){
			driver.waitForElementPresent(By.xpath("//a[@id='requestsponsor']"));
			driver.click(By.xpath("//a[@id='requestsponsor']"));
			driver.waitForLoadingImageToDisappear();
		}
	}

	public void clickOKOnSponsorInformationPopup(){
		driver.pauseExecutionFor(2000);
		try{
			//   driver.waitForElementToBeVisible(By.xpath("//div[@id='sponsorMessage']//div[@id='popup-sponsorMessage']//input[contains(@value,'OK')]"), 15);
			driver.quickWaitForElementPresent(By.xpath("//div[@id='confirm-left-shopping']//div[@id='popup']//input[@value ='OK ']"));
			driver.click(By.xpath("//div[@id='confirm-left-shopping']//div[@id='popup']//input[@value ='OK ']"));
		}catch(Exception e){
			logger.info("No sponsor informantion popup appeared");
		}
	}

	public void clickYesIWantToJoinPCPerksCB(){
		driver.waitForElementPresent(By.id("pc-customer2-div-order-summary"));
		driver.click(By.id("pc-customer2-div-order-summary"));
		driver.waitForPageLoad();
	}

	public void checkIAcknowledgePCAccountCheckBox(){
		try{
			driver.waitForElementPresent(By.xpath("//input[@id='Terms2']/.."));
			driver.click(By.xpath("//input[@id='Terms2']/.."));  
			logger.info("I Acknowledge PC Account Checkbox selected");
		}catch(Exception e){
			driver.waitForElementPresent(By.xpath("//form[@id='placeOrderForm1']/ul/div[1]/li[1]/div"));
			driver.click(By.xpath("//form[@id='placeOrderForm1']/ul/div[1]/li[1]/div"));  
			logger.info("I Acknowledge PC Account Checkbox selected");
		}
	}

	public void checkPCPerksTermsAndConditionsCheckBox(){
		try{
			driver.waitForElementPresent(By.xpath("//input[@id='Terms3']/.."));
			driver.click(By.xpath("//input[@id='Terms3']/.."));  
			logger.info("PC Perks terms and condition checkbox selected");
		}catch(Exception e){
			driver.waitForElementPresent(By.xpath("//form[@id='placeOrderForm1']/ul/div[1]/li[2]/div"));
			driver.click(By.xpath("//form[@id='placeOrderForm1']/ul/div[1]/li[2]/div"));  
			logger.info("PC Perks terms and condition checkbox selected");
		}
	}

	public boolean isWelcomePCPerksMessageDisplayed(){
		driver.waitForElementPresent(By.xpath("//div[@id='Congrats']/h1[contains(text(),'PC')]"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='Congrats']/h1[contains(text(),'PC')]")));
	}

	public String createNewPC(String firstName,String lastName,String password) throws InterruptedException{
		String emailAddress = firstName+"@xyz.com";
		driver.type(By.id("first-Name"),firstName);
		logger.info("first name entered as "+firstName);
		driver.type(By.id("last-name"),lastName);
		logger.info("last name entered as "+lastName);
		driver.type(By.id("email-account"),emailAddress+"\t");
		logger.info("email entered as "+emailAddress);
		driver.pauseExecutionFor(2000);
		driver.type(By.id("password"),password);
		logger.info("password entered as "+password);
		driver.type(By.id("the-password-again"),password);
		logger.info("confirm password entered as "+password);
		driver.click(By.xpath("//input[@id='become-pc']/.."));
		logger.info("check box for PC user checked");
		driver.click(By.xpath("//input[@id='next-button']"));  
		logger.info("Create New Account button clicked"); 
		return emailAddress;
	}

	public String createNewRC(String firstName,String lastName,String password){	
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		//		String firstName="RCUser";
		//		String lastName = "Test";
		String emailAddress = firstName+randomNum+"@xyz.com";
		driver.type(By.id("first-Name"),firstName);
		logger.info("first name entered as "+firstName);
		driver.type(By.id("last-name"),lastName);
		logger.info("last name entered as "+lastName);
		driver.type(By.id("email-account"),emailAddress+"\t");
		logger.info("email entered as "+emailAddress);
		driver.pauseExecutionFor(2000);
		driver.type(By.id("password"),password);
		logger.info("password entered as "+password);
		driver.type(By.id("the-password-again"),password);
		logger.info("confirm password entered as "+password);
		driver.click(By.id("next-button"));  
		logger.info("Create New Account button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return emailAddress;
	}

	public void enterNewRCDetails(String firstName,String emailAddress) throws InterruptedException{
		String lastName = "ln";
		driver.type(By.id("first-Name"),firstName);
		logger.info("first name entered as "+firstName);
		driver.type(By.id("last-name"),lastName);
		logger.info("last name entered as "+lastName);
		driver.clear(By.id("email-account"));
		driver.type(By.id("email-account"),emailAddress+"\t");
		logger.info("email entered as "+emailAddress);
		driver.pauseExecutionFor(1000);
	}

	public StoreFrontCartAutoShipPage clickEditCrpLinkPresentOnWelcomeDropDown() throws InterruptedException{
		driver.waitForElementPresent(WELCOME_DD_EDIT_CRP_LINK_LOC);
		driver.click(WELCOME_DD_EDIT_CRP_LINK_LOC);
		logger.info("User has clicked on edit Crp link from welcome drop down");
		driver.waitForPageLoad();
		return new StoreFrontCartAutoShipPage(driver);
	}

	public boolean validateExistingUserPopUp(String userid,String firstName,String lastName,String password,String countryId){
		RFO_DB = driver.getDBNameRFO();
		String pcmailid=null;
		String rcmailid=null;
		String consultantmailid=null;

		List<Map<String, Object>> randomPCUserEmailIdList =  null;
		List<Map<String, Object>> randomRCUserEmailIdList =  null;
		List<Map<String, Object>> randomConsultantEmailIdList =  null;
		driver.type(By.id("first-Name"),firstName);
		logger.info("first name entered as "+firstName);
		driver.type(By.id("last-name"),lastName);
		logger.info("last name entered as "+lastName);
		if(userid.equalsIgnoreCase("pc")){
			randomPCUserEmailIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcmailid = String.valueOf(getValueFromQueryResult(randomPCUserEmailIdList, "Username"));

			driver.findElement(By.id("email-account")).sendKeys(pcmailid+"\t");
			logger.info("email entered as "+pcmailid);
		}else if(userid.equalsIgnoreCase("rc")){
			randomRCUserEmailIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcmailid = String.valueOf(getValueFromQueryResult(randomRCUserEmailIdList, "Username"));

			driver.findElement(By.id("email-account")).sendKeys(rcmailid+"\t");
			logger.info("email entered as "+rcmailid);
		}else{
			randomConsultantEmailIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantmailid = String.valueOf(getValueFromQueryResult(randomConsultantEmailIdList, "Username"));
			driver.findElement(By.id("email-account")).sendKeys(consultantmailid+"\t");
			logger.info("email entered as "+consultantmailid);
		}
		driver.pauseExecutionFor(1000);
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
		return driver.isElementPresent(By.xpath("//div[@class='fancybox-inner']"));
	}


	public boolean validateSendMailToResetMyPasswordFunctionalityPC(){
		driver.waitForElementPresent(By.xpath("//div[@id='activePCPopup']//input[contains(@class,'resetPasswordEmail')]"));
		JavascriptExecutor js = ((JavascriptExecutor)RFWebsiteDriver.driver);
		js.executeScript("arguments[0].click();", driver.findElement(By.xpath("//div[@id='activePCPopup']//input[contains(@class,'resetPasswordEmail')]")));
		driver.pauseExecutionFor(1000);
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
		return driver.isElementPresent(By.xpath("//div[@class='fancybox-inner']"));
	}

	public boolean validateCancelEnrollmentFunctionalityPC(){
		driver.waitForElementPresent(By.xpath("//div[@id='activePCPopup']//input[contains(@class,'cancelEnrollment')]"));
		driver.click(By.xpath("//div[@id='activePCPopup']//input[contains(@class,'cancelEnrollment')]"));
		driver.pauseExecutionFor(1000);
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
		driver.waitForPageLoad();
		return driver.isElementPresent(By.xpath("//div[@class='fancybox-inner']"));
	}

	public boolean validateSendMailToResetMyPasswordFunctionalityRC(){
		driver.waitForElementPresent(By.xpath(" //div[@id='activeRetailPopup']//input[contains(@class,'resetPasswordEmail')]"));
		JavascriptExecutor js = ((JavascriptExecutor)RFWebsiteDriver.driver);
		js.executeScript("arguments[0].click();", driver.findElement(By.xpath(" //div[@id='activeRetailPopup']//input[contains(@class,'resetPasswordEmail')]")));
		return driver.isElementPresent(By.xpath("//div[contains(text(),'An e-mail has been sent to reset your password')]"));   
	}


	public boolean validateCancelEnrollmentFunctionality(){
		driver.waitForElementPresent(By.xpath("//div[@id='activeRetailPopup']//input[contains(@class,'cancelEnrollment')]"));
		driver.click(By.xpath("//div[@id='activeRetailPopup']//input[contains(@class,'cancelEnrollment')]"));
		driver.pauseExecutionFor(2000);
		driver.waitForLoadingImageToDisappear();
		return validateHomePage();
	}


	public boolean validateSendMailToResetMyPasswordFunctionalityConsultant(){
		try{
			driver.waitForElementPresent(By.xpath("//div[@id='notavailablePopup']//input[contains(@class,'resetPasswordEmail')]"));
			JavascriptExecutor js = ((JavascriptExecutor)RFWebsiteDriver.driver);
			js.executeScript("arguments[0].click();", driver.findElement(By.xpath("//div[@id='notavailablePopup']//input[contains(@class,'resetPasswordEmail')]")));
			//driver.findElement(By.xpath("//div[@id='notavailablePopup']//input[@class='resetPasswordEmail']")).click();
			driver.pauseExecutionFor(2000);
			driver.waitForLoadingImageToDisappear();
			driver.pauseExecutionFor(2000);
			return validateHomePage();   
		}catch(NoSuchElementException e){
			return driver.isElementPresent(By.xpath("//div[@id='notavailablePopup']//input[contains(@class,'resetPasswordEmail')]"));
		}
	}

	public boolean validateCancelEnrollmentFunctionalityConsultant(){
		driver.waitForElementPresent(By.xpath("//div[@id='notavailablePopup']//input[contains(@class,'cancelEnrollment')]"));
		driver.click(By.xpath("//div[@id='notavailablePopup']//input[contains(@class,'cancelEnrollment')]"));
		driver.pauseExecutionFor(2000);
		driver.waitForLoadingImageToDisappear();
		return validateHomePage();
	}

	public boolean validateHomePage(){
		driver.waitForPageLoad();
		String url = driver.getURL();
		String currentURL = driver.getCurrentUrl();
		System.out.println(currentURL);
		return driver.getCurrentUrl().contains(url);
	}

	public void navigateToBackPage(){
		driver.waitForPageLoad();
		driver.navigate().back();
	}

	public void clickOnAllowMySpouseOrDomesticPartnerCheckboxForUncheck() {
		//driver.waitForElementToBeVisible(By.xpath("//input[@id='spouse-check']"), 15);
		boolean status=driver.findElement(By.xpath("//input[@id='spouse-check']/..")).isSelected();
		if(status==true){
			driver.click(By.xpath("//input[@id='spouse-check']/.."));
		}
	}

	public void cancelTheProvideAccessToSpousePopup(){
		driver.pauseExecutionFor(6000);
		if(driver.findElement(By.id("cancelSpouse")).isDisplayed()){
			driver.click(By.id("cancelSpouse"));
			driver.pauseExecutionFor(3000);
		}
	}

	public boolean verifyAllowMySpouseCheckBoxIsSelectedOrNot(){
		logger.info("Checkbox status "+driver.findElement(By.id("spouse-check")).isSelected());
		driver.waitForElementPresent(By.id("spouse-check"));
		if(driver.findElement(By.id("spouse-check")).getAttribute("class").equalsIgnoreCase("checked")){
			return true;
		}else{
			return false;
		}

	}

	public Object getValueFromQueryResult(List<Map<String, Object>> userDataList,String column){
		Object value = null;
		for (Map<String, Object> map : userDataList) {
			logger.info("query result:" + map.get(column));
			value = map.get(column);			
		}
		return value;
	}

	public String getBizPWS(String country,String env){
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomActiveSitePrefixList =  null;
		String activeSitePrefix = null;
		String PWS = null;
		String countryID =null;

		if(country.equalsIgnoreCase("ca")){
			countryID="40";
		}
		else if(country.equalsIgnoreCase("us")){
			countryID="236";
		} 
		randomActiveSitePrefixList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_SITE_PREFIX_RFO,countryID),RFO_DB);
		activeSitePrefix = (String) getValueFromQueryResult(randomActiveSitePrefixList, "SitePrefix");			
		PWS = "http://"+activeSitePrefix+".myrfo"+env+".biz/"+country.toLowerCase();
		logger.info("PWS is "+PWS);
		return PWS;
	}

	public String getComPWS(String country,String env){
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomActiveSitePrefixList =  null;
		String activeSitePrefix = null;
		String PWS = null;
		String countryID =null;

		if(country.equalsIgnoreCase("ca")){
			countryID="40";
		}
		else if(country.equalsIgnoreCase("us")){
			countryID="236";
		} 
		randomActiveSitePrefixList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_SITE_PREFIX_RFO,countryID),RFO_DB);
		activeSitePrefix = (String) getValueFromQueryResult(randomActiveSitePrefixList, "SitePrefix");			
		PWS = "http://"+activeSitePrefix+".myrfo"+env+".com/"+country.toLowerCase();
		logger.info("PWS is "+PWS);
		return PWS;
	}


	public String openPWSSite(String country,String env){
		while(true){
			driver.get(getBizPWS(country, env));
			driver.waitForPageLoad();
			if(driver.getCurrentUrl().contains("sitenotfound"))
				continue;
			else
				break;
		}	
		return driver.getCurrentUrl();
	}

	public String openComPWSSite(String country,String env){
		while(true){
			driver.get(getComPWS(country, env));
			driver.waitForPageLoad();
			if(driver.getCurrentUrl().contains("sitenotfound"))
				continue;
			else
				break;
		}	
		return driver.getCurrentUrl();
	}

	public void switchToChildWindow(){
		driver.switchToSecondWindow();
		driver.waitForPageLoad();
	}

	public void clickCheckMyPulseLinkPresentOnWelcomeDropDown(){
		driver.waitForElementPresent(By.xpath("//a[text()='Check My Pulse']"));
		driver.click(By.xpath("//a[text()='Check My Pulse']"));
		driver.pauseExecutionFor(5000);
	}

	public boolean validatePulseHomePage(){
		System.out.println("current url is "+driver.getCurrentUrl());
		return driver.getCurrentUrl().contains("pulse");
	}

	@SuppressWarnings("deprecation")
	public void hoverOnShopLinkAndClickAllProductsLinksAfterLogin(){
		Actions actions = new Actions(RFWebsiteDriver.driver);
		driver.waitForElementPresent(By.id("our-products")); 
		WebElement shopSkinCare = driver.findElement(By.id("our-products"));
		actions.moveToElement(shopSkinCare).pause(1000).click().build().perform();
		WebElement allProducts = driver.findElement(By.xpath("//ul[@id='dropdown-menu' and @style='display: block;']//a[text()='All Products']"));
		actions.moveToElement(allProducts).pause(1000).build().perform();
		while(true){
			try{
				driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath(" //ul[@id='dropdown-menu' and @style='display: block;']//a[text()='All Products']")));

				break;
			}catch(Exception e){
				System.out.println("element not clicked..trying again");
				actions.moveToElement(shopSkinCare).pause(1000).click().build().perform();

			}
		}
		logger.info("All products link clicked "); 
		driver.waitForPageLoad();
	}

	@SuppressWarnings("deprecation")
	public void hoverOnShopLinkAndClickAllProductsLinks(){
		Actions actions = new Actions(RFWebsiteDriver.driver);
		driver.waitForElementPresent(By.id("our-products")); 
		WebElement shopSkinCare = driver.findElement(By.id("our-products"));
		actions.moveToElement(shopSkinCare).pause(1000).click().build().perform();
		WebElement allProducts = driver.findElement(By.xpath("//ul[@id='dropdown-menu' and @style='display: block;']//a[text()='All Products']"));
		actions.moveToElement(allProducts).pause(1000).build().perform();
		while(true){
			try{
				driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath(" //ul[@id='dropdown-menu' and @style='display: block;']//a[text()='All Products']")));

				break;
			}catch(Exception e){
				System.out.println("element not clicked..trying again");
				actions.moveToElement(shopSkinCare).pause(1000).click().build().perform();

			}
		}
		logger.info("All products link clicked "); 
		driver.waitForPageLoad();
	}

	public void clickOnWelcomeDropDown() throws InterruptedException{
		driver.waitForElementPresent(WELCOME_USER_DD_LOC);
		driver.pauseExecutionFor(2000);
		driver.click(WELCOME_USER_DD_LOC);
		logger.info("clicked on welcome drop down");		
	}

	public StoreFrontOrdersPage clickOrdersLinkPresentOnWelcomeDropDown() throws InterruptedException{
		driver.waitForElementPresent(WELCOME_DD_ORDERS_LINK_LOC);
		driver.click(WELCOME_DD_ORDERS_LINK_LOC);
		logger.info("User has clicked on orders link from welcome drop down");
		return new StoreFrontOrdersPage(driver);
	}

	public void clickOnYourAccountDropdown(){
		driver.click(YOUR_ACCOUNT_DROPDOWN_LOC);
		logger.info("Your accountdropdown clicked from left panel clicked "+YOUR_ACCOUNT_DROPDOWN_LOC);
	}

	public void clickOnPlaceOrderButton(){
		driver.waitForElementPresent(By.xpath("//input[@value='NEXT']"));
		driver.click(By.xpath("//input[@value='NEXT']"));
		logger.info("Place order button clicked");
		driver.waitForPageLoad();
	}

	public boolean verifyUpradingToConsulTantPopup(){
		driver.waitForPageLoad();
		if(driver.isElementPresent(By.xpath("//div[@id='activePCPopup']//h2[contains(text(),'UPGRADING TO A CONSULTANT')]"))){
			return true;
		}else
			return false;
	}
	public void enterPasswordForUpgradePcToConsultant(){
		try{
			driver.quickWaitForElementPresent(By.xpath("//h3[contains(text(),'Log In to Reactivate My Account')]/following::input[2]"));
			driver.type(By.xpath("//h3[contains(text(),'Log In to Reactivate My Account')]/following::input[2]"), driver.getStoreFrontPassword());
		}catch(Exception e){
			driver.waitForElementPresent(By.xpath("//h3[contains(text(),'Log In to Terminate My PC Account')]/following::input[2]"));
			driver.type(By.xpath("//h3[contains(text(),'Log In to Terminate My PC Account')]/following::input[2]"), driver.getStoreFrontPassword());
		}
	}

	public void clickOnLoginToTerminateToMyPCAccount(){
		//driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(By.xpath("//h3[contains(text(),'Log In to Terminate My PC Account')]/following::a[2]/input"));
		driver.click(By.xpath("//h3[contains(text(),'Log In to Terminate My PC Account')]/following::a[2]/input"));
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();	
		driver.pauseExecutionFor(3000);
		driver.waitForLoadingImageToDisappear();
		
	}

	public void navigateToCountry(){
		driver.waitForElementPresent(By.xpath("//div[@class='footer-tagline-decide']/following::div[4]//button"));
		String defaultSelectedCountry= driver.findElement(By.xpath("//div[@class='footer-tagline-decide']/following::div[4]//button")).getText();
		driver.click(By.xpath("//div[@class='footer-tagline-decide']/following::div[4]//button"));
		if(defaultSelectedCountry.contains("USA")){
			driver.waitForElementPresent(By.xpath("//div[@class='footer-tagline-decide']/following::div[4]//a[contains(text(),'CAN')]"));
			driver.click(By.xpath("//div[@class='footer-tagline-decide']/following::div[4]//a[contains(text(),'CAN')]"));
			logger.info("navigated to canada site successfully");
		}else{
			driver.waitForElementPresent(By.xpath("//div[@class='footer-tagline-decide']/following::div[4]//a[contains(text(),'USA')]"));
			driver.click(By.xpath("//div[@class='footer-tagline-decide']/following::div[4]//a[contains(text(),'USA')]"));
			logger.info("navigated to USA site successfully");
		}
		driver.waitForPageLoad();
	}

	public void clickAddToBagButton() throws InterruptedException{
		applyPriceFilterHighToLow();
		if(driver.getCountry().equalsIgnoreCase("us")){
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
			driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
		}else{
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@action='/ca/cart/add']/button"));
			driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@action='/ca/cart/add']/button"));
			//   driver.waitForElementPresent(By.xpath("//div[@id='main-content']//a[text()='REDEFINE Regimen']/following::form[1]"));
			//   driver.click(By.xpath("//div[@id='main-content']//a[text()='REDEFINE Regimen']/following::form[1]"));
		}
		logger.info("Add To Bag button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public void clickAddToBagButton(String country) throws InterruptedException{
		applyPriceFilterHighToLow();
		if(country.equalsIgnoreCase("CA")){
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[contains(@class,'quick-product-wrapper')]/div[1]/div[2]/div[1]//button"));
			if(driver.findElement(By.xpath("//div[@id='main-content']/div[contains(@class,'quick-product-wrapper')]/div[1]/div[2]/div[1]//button")).isEnabled()==true)
				driver.click(By.xpath("//div[@id='main-content']/div[contains(@class,'quick-product-wrapper')]/div[1]/div[2]/div[1]//button"));
			else
				driver.click(By.xpath("//div[@id='main-content']/div[contains(@class,'quick-product-wrapper')]/div[2]/div[2]/div[1]//button"));
			logger.info("Add To Bag button clicked");
			driver.waitForLoadingImageToDisappear();
		}else if(country.equalsIgnoreCase("US")){
			try{
				driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
				driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
			}catch(Exception e){
				driver.click(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div[2]/div[2]/div[1]//button"));
				logger.info("Add To Bag button clicked");
				driver.waitForLoadingImageToDisappear();
			}
		}
	}

	public void clickOnEditAtAutoshipTemplate(){
		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='Edit']"));
			driver.click(By.xpath("//input[@value='EDIT']"));
		}
		catch(Exception e){
			try{
				driver.click(By.xpath("//input[@value='Edit']"));
			}
			catch(Exception e1){
				driver.click(By.xpath("//input[@value='edit']")); 
			}
		}
	}

	public String getQuantityOfProductFromAutoshipTemplate(){
		String quantity = driver.findElement(By.xpath("//div[@class='order-summary-left spacer'][2]/div[1]/div[2]/div[2]/div[3]")).getText();
		return quantity;
	}

	public void clickOnBillingNextStepButton() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='start-shipping-method']/div[2]/div/input"));
		driver.click(By.xpath("//*[@id='start-shipping-method']/div[2]/div/input"));
		logger.info("Next button on clicked"); 
		driver.waitForLoadingImageToDisappear();
		driver.waitForElementPresent(By.xpath("//div[@id='payment-next-button']/input"));
		driver.click(By.xpath("//div[@id='payment-next-button']/input"));
	}

	public boolean verifyConsultantCantShipToQuebecMsg(){
		String message = driver.findElement(By.xpath("//div[@id='errorQCEnrollDiv']")).getText();
		if(message.equals("Consultants cannot ship to Quebec.")){
			return true;
		}
		else{
			return false;
		}
	}

	public void clickOnAccountInfoNextButton(){
		driver.waitForElementPresent(By.id("clickNext"));
		driver.click(By.id("clickNext"));
	}	

	public boolean verifyEditPcPerksIsPresentInWelcomDropdownForUpgrade(){
		driver.pauseExecutionFor(2000);
		if(driver.isElementPresent(By.xpath("//a[text()='Edit PC Perks']"))){
			return true;
		}else
			return false;
	}

	public String getSponserNameFromUIWhileEnrollingPCUser(){
		driver.waitForElementPresent(By.xpath("//div[@id='sponsorInfo']"));
		String sponserEmailID =driver.findElement(By.xpath("//div[@id='sponsorInfo']")).getText();
		logger.info("Default Sponser email address from UI is "+sponserEmailID);
		return sponserEmailID;
	}

	public String splitPWS(String pws){
		if(pws.contains(".biz")){
			pws = pws.split(".biz")[0];	
		}
		else
			pws = pws.split(".com")[0];
		return pws;
	}

	public StoreFrontBillingInfoPage clickBillingInfoLinkPresentOnWelcomeDropDown(){
		driver.waitForElementPresent(WELCOME_DD_BILLING_INFO_LINK_LOC);
		driver.click(WELCOME_DD_BILLING_INFO_LINK_LOC);
		logger.info("User has clicked on billing link from welcome drop down");
		driver.waitForPageLoad();
		return new StoreFrontBillingInfoPage(driver);
	}

	public StoreFrontShippingInfoPage clickShippingLinkPresentOnWelcomeDropDown() throws InterruptedException{
		driver.waitForElementPresent(WELCOME_DD_SHIPPING_INFO_LINK_LOC);
		driver.click(WELCOME_DD_SHIPPING_INFO_LINK_LOC);		
		logger.info("User has clicked on shipping link from welcome drop down");
		return new StoreFrontShippingInfoPage(driver);
	}

	public void clickAddNewShippingProfileLink() throws InterruptedException{
		try{
			driver.waitForElementPresent(By.xpath("//a[text()='Add new shipping address »']"));
			driver.click(By.xpath("//a[text()='Add new shipping address »']"));
			logger.info("Ads new shipping profile link clicked");
		}
		catch(NoSuchElementException e){
			driver.waitForElementPresent(ADD_NEW_SHIPPING_LINK_LOC);
			driver.click(ADD_NEW_SHIPPING_LINK_LOC);
			logger.info("Ads new shipping profile link clicked");
		}
	}

	public void enterNewShippingAddressPostalCode(String postalCode){
		driver.clear(By.id("postcode"));
		driver.type(By.id("postcode"),postalCode);
	}

	public void enterNewShippingAddressPhoneNumber(String phoneNumber){
		driver.clear(By.id("phonenumber"));
		driver.type(By.id("phonenumber"),phoneNumber);
	}

	public void clickOnAutoshipCart(){
		if(driver.getCountry().equalsIgnoreCase("CA")){  
			driver.waitForElementPresent(By.xpath("//div[@id='bag-special']/span"));
			driver.click(By.xpath("//div[@id='bag-special']/span"));;
			driver.waitForPageLoad();

		}else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.waitForElementPresent(By.xpath("//div[@id='bag-special']/span/span[1]"));
			driver.click(By.xpath("//div[@id='bag-special']/span/span[1]"));;
			driver.waitForPageLoad();
		}
	}

	public void clickOnAddToCRPButtonCreatingCRPUnderBizSite() throws InterruptedException{
		applyPriceFilterHighToLow();
		if(driver.getCountry().equalsIgnoreCase("CA")){  
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[3]/div[1]//button[@class='btn btn-primary']]"));
			driver.click(By.xpath("//div[@id='main-content']/div[3]/div[1]//button[@class='btn btn-primary']"));
			logger.info("Add to CRP button clicked");
			driver.waitForLoadingImageToDisappear();
		}
		else if(driver.getCountry().equalsIgnoreCase("US")){
			driver.waitForElementPresent(By.xpath("//*[@class='quickshop-section yellow productCatPage']/div[2]/div[1]//button[@class='btn btn-primary']"));
			driver.click(By.xpath("//*[@class='quickshop-section yellow productCatPage']/div[2]/div[1]//button[@class='btn btn-primary']"));
			logger.info("Add to CRP button clicked");
			driver.waitForLoadingImageToDisappear();   
		}
		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='OK']"));
			driver.click(By.xpath("//input[@value='OK']"));
			driver.waitForLoadingImageToDisappear();
		}catch(Exception e){

		}
	}

	public void clickOnCRPCheckout(){
		driver.waitForElementPresent(By.id("crpCheckoutButton"));
		driver.click(By.id("crpCheckoutButton"));
		logger.info("checkout button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnBillingNextStepButtonDuringEnrollInCRP() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='start-shipping-method']/div[2]/div/input"));
		driver.click(By.xpath("//*[@id='start-shipping-method']/div[2]/div/input"));
		logger.info("Next button on clicked"); 
	}

	public StoreFrontAccountInfoPage clickAccountInfoLinkPresentOnWelcomeDropDown() throws InterruptedException{
		driver.waitForElementPresent(WELCOME_DD_ACCOUNT_INFO_LOC);
		driver.click(WELCOME_DD_ACCOUNT_INFO_LOC);		
		logger.info("User has clicked on account link from welcome drop down");
		driver.pauseExecutionFor(3000);
		return new StoreFrontAccountInfoPage(driver);
	}

	public boolean verifyOrderConfirmation(){
		logger.info("Asserting Order Confirmation Message");
		driver.waitForElementPresent(By.xpath("//div[@id='order-confirm']/span"));
		if(driver.findElement(By.xpath("//div[@id='order-confirm']/span")).getText().equalsIgnoreCase("Your CRP order has been created")){
			return true;
		}
		return false;
	}

	public boolean validateMyAccountDDPresentInTopNav(){
		driver.waitForElementPresent(WELCOME_USER_DD_LOC);
		return driver.isElementPresent(WELCOME_USER_DD_LOC);
	}

	public boolean validateAdhocCartIsDisplayed(){
		driver.waitForElementPresent(By.xpath("//span[@class='cart-section']"));
		return driver.isElementPresent(By.xpath("//span[@class='cart-section']"));
	}

	public void applyPriceFilterHighToLow() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//select[@id='sortOptions']"));
		driver.click(By.xpath("//select[@id='sortOptions']"));
		driver.click(By.xpath("//select[@id='sortOptions']/option[2]"));
		logger.info("filter done for high to low price");
		driver.waitForPageLoad();
	}

	public void deselectPriceFilter() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//select[@id='sortOptions']"));
		driver.click(By.xpath("//select[@id='sortOptions']"));
		driver.click(By.xpath("//select[@id='sortOptions']/option[1]"));
		logger.info("deselect the price filter");
	}

	public String getProductPriceBeforeApplyFilter(){
		if(driver.getCountry().equalsIgnoreCase("ca")){
			return driver.findElement(By.xpath("//div[@id='main-content']/div[5]/div[1]//span[@class='your-price']")).getText().split("\\$")[1].trim();
		}
		return driver.findElement(By.xpath("//div[@class='quickshop-section blue']/div[2]/div[1]//span[@class='your-price']")).getText().split("\\$")[1].trim();
	}

	public void clickOnContinueShoppingLink(){
		try{
			driver.quickWaitForElementPresent(By.xpath("//div[@id='left-shopping']/div[1]//a[contains(text(),'Continue shopping')]"));
			driver.click(By.xpath("//div[@id='left-shopping']/div[1]//a[contains(text(),'Continue shopping')]"));
		}
		catch(Exception e){
			driver.click(By.xpath("//div[@id='left-shopping']/div[2]//a[contains(text(),'Continue')]"));   
		}
		driver.waitForPageLoad();
	}

	public void selectAProductAndAddItToPCPerks(){
		driver.waitForElementNotPresent(By.xpath("//input[@class='btn btn-primary' and @value='ADD to PC Perks' and @tabindex='5']"));
		driver.click(By.xpath("//input[@class='btn btn-primary' and @value='ADD to PC Perks' and @tabindex='5']"));;
		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='OK']"));
			driver.click(By.xpath("//input[@value='OK']"));
			driver.waitForLoadingImageToDisappear();
		}catch(Exception e){

		}
		driver.waitForPageLoad();
		driver.pauseExecutionFor(1000);
	}

	public boolean isBillingProfileIsSelectedByDefault(String profileName){
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-billing-profiles']//span[contains(text(),'"+profileName+"')]/following::input[@checked='checked']"));
		return driver.isElementPresent(By.xpath("//div[@id='multiple-billing-profiles']//span[contains(text(),'"+profileName+"')]/following::input[@checked='checked']"));
	}

	public boolean isTheBillingAddressPresentOnPage(String firstName){
		boolean isFirstNamePresent = false;
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-billing-profiles']/div/div"));
		List<WebElement> allBillingProfiles = driver.findElements(By.xpath("//div[@id='multiple-billing-profiles']/div"));  
		for(int i=1;i<=allBillingProfiles.size();i++){   
			try{
				isFirstNamePresent = driver.findElement(By.xpath("//div[@id='multiple-billing-profiles']/div[contains(@class,'sel-profile')]["+i+"]/p[1]/span[@class='font-bold']")).getText().toLowerCase().contains(firstName.toLowerCase());
			}catch(Exception e){
				try{					
					isFirstNamePresent = driver.findElement(By.xpath(" //div[@id='multiple-billing-profiles']/div["+i+"]/p[1]/span[1]")).getText().toLowerCase().contains(firstName.toLowerCase());
				}catch(ExceptionInInitializerError e2){
					isFirstNamePresent = driver.findElement(By.xpath(" //div[@id='multiple-billing-profiles']/div/div["+i+"]/p[1]/span[1]")).getText().toLowerCase().contains(firstName.toLowerCase());
				}
			}
			if(isFirstNamePresent == true){ 
				return true;
			}
		}
		return false;
	}

	public String getDotComPWS(String country){
		driver.waitForElementPresent(By.xpath("//p[@id='prefix-validation']/span[1]"));
		String pwsUnderPulse = driver.findElement(By.xpath("//p[@id='prefix-validation']/span[1]")).getText();
		return pwsUnderPulse;
	}

	public String getDotBizPWS(String country){
		driver.waitForElementPresent(By.xpath("//p[@id='prefix-validation']/span[2]"));
		String pwsUnderPulse = driver.findElement(By.xpath("//p[@id='prefix-validation']/span[2]")).getText();
		return pwsUnderPulse;
	}

	public String getEmailId(String country){
		driver.waitForElementPresent(By.xpath("//p[@id='prefix-validation']/span[3]"));
		String pwsUnderPulse = driver.findElement(By.xpath("//p[@id='prefix-validation']/span[3]")).getText();
		return pwsUnderPulse;
	}

	public void enterWebsitePrefixName(String name){
		driver.waitForElementPresent(By.id("webSitePrefix"));
		driver.type(By.id("webSitePrefix"), name+"\t");
		driver.pauseExecutionFor(1000);
	}

	public boolean verifySpecialCharNotAcceptInPrefixName(){
		driver.quickWaitForElementPresent(By.xpath("//span[contains(text(),'This prefix is not available')]"));
		if(driver.isElementPresent(By.xpath("//span[contains(text(),'This prefix is not available')]"))){
			return true;
		}else
			return false;
	}

	public void clickOnAutoshipStatusLink(){
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'Autoship Status')]"));
		driver.click(By.xpath("//a[contains(text(),'Autoship Status')]"));
		logger.info("Autoship status link clicked");
		driver.waitForPageLoad();
	}

	public void subscribeToPulse(){
		if(driver.isElementPresent(By.xpath("//a[text()='Cancel my Pulse subscription »']"))){
			driver.click(By.xpath("//a[text()='Cancel my Pulse subscription »']"));
			driver.pauseExecutionFor(2500);
			driver.click(By.xpath("//a[@id='cancelPulse']"));
			driver.waitForLoadingImageToDisappear();
			try{
				driver.quickWaitForElementPresent(By.id("cancel-pulse-button"));
				driver.click(By.id("cancel-pulse-button"));
				driver.waitForLoadingImageToDisappear();
			}catch(Exception e){

			}
			driver.waitForPageLoad();
		}
		driver.click(By.xpath("//input[@id='subscribe_pulse_button_new']"));
		driver.waitForLoadingImageToDisappear();
	}

	public void selectDifferenetProductAndAddItToCRP(){

		if(driver.getCountry().equalsIgnoreCase("ca")){
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[5]//button[@class='btn btn-primary']"));
			driver.click(By.xpath("//div[@id='main-content']/div[5]/div[5]//button[@class='btn btn-primary']"));
		}else{
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[5]//div[@class='product-shop-buttons']/div[2]//button"));
			driver.click(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[5]//div[@class='product-shop-buttons']/div[2]//button"));
		}
		driver.waitForSpinImageToDisappear();
		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='OK']"));
			driver.click(By.xpath("//input[@value='OK']"));
		}catch(Exception e){

		}
		driver.pauseExecutionFor(1000);
		driver.waitForPageLoad();
	}

	public void selectProductAndProceedToBuyForPC() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[5]//input[@value='ADD to PC Perks']"));
			driver.click(By.xpath("//div[@id='main-content']/div[5]/div[5]//input[@value='ADD to PC Perks']"));
		}
		else {
			driver.quickWaitForElementPresent(By.xpath("//div[@class='quickshop-section blue']/div[2]/div[5]//input[@value='ADD to PC Perks']"));
			driver.click(By.xpath("//div[@class='quickshop-section blue']/div[2]/div[5]//input[@value='ADD to PC Perks']"));
		}
		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='OK']"));
			driver.click(By.xpath("//input[@value='OK']"));
		}catch(Exception e){
		}
		logger.info("Add To Bag button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();

	}

	public boolean verifyCheckoutConfirmationPOPupPresent(){
		driver.quickWaitForElementPresent(By.xpath("//div[@id='popup-review']"));
		return driver.isElementPresent(By.xpath("//div[@id='popup-review']"));		  
	}

	public void clickOnOkButtonOnCheckoutConfirmationPopUp(){
		driver.click(By.xpath("//input[@value='OK']"));
		driver.waitForPageLoad();
	}

	public boolean verifyAccountInfoPageHeaderPresent(){
		driver.waitForElementPresent(By.xpath("//div[@id='checkout_summary_deliveryaddress_div']//span"));
		if(driver.isElementPresent(By.xpath("//div[@id='checkout_summary_deliveryaddress_div']//span"))){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean verifyCheckoutConfirmationPopUpMessagePC(){
		if(driver.findElement(By.xpath("//div[@id='popup-review']/div/p")).getText().contains("This is not a PC Perks order. Shipping charges will be applied to this order. Please click OK to continue.")){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean verifyCheckoutConfirmationPopUpMessageConsultant() {
		if(driver.findElement(By.xpath("//div[@id='popup-review']/div/p")).getText().contains("This is not a CRP order. You will be charged for regular order. Please click OK to continue.")){
			return true;}

		else{
			return false;
		}
	}

	public boolean verifyRetailPriceIsAvailableOnProductsPage(){
		driver.waitForPageLoad();
		if(driver.getCountry().equalsIgnoreCase("us")){
			return driver.isElementPresent(By.xpath("//div[contains(@class,'quickshop-section blue')]/div[contains(@class,'quick-product-wrapper')]/div[1]//span[@class='old-price']"));
		}else{
			return driver.isElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//span[@class='old-price']"));
		}
	}

	public boolean verifyYourPriceIsAvailableOnProductsPage(){
		if(driver.getCountry().equalsIgnoreCase("us")){
			return driver.isElementPresent(By.xpath("//div[contains(@class,'quickshop-section blue')]/div[contains(@class,'quick-product-wrapper')]/div[1]//span[@class='your-price']"));
		}else{
			return driver.isElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//span[@class='your-price']"));
		}
	}

	public boolean verifyRetailPriceIsAvailableOnAdhocCart(){
		driver.waitForPageLoad();
		return driver.isElementPresent(By.id("cart-retail-price"));
	}

	public boolean verifyYourPriceIsAvailableOnAdhocCart(){
		return driver.isElementPresent(By.id("cart-price"));
	}

	public boolean verifyTotalSavingsIsAvailableOnAdhocCart(){
		driver.waitForPageLoad();
		return driver.isElementPresent(By.xpath("//div[@class='checkout-module-content']//div[@id='module-total'][2]"));
	}

	public boolean verifyRetailPriceIsAvailableOnAutoshipCart(){
		driver.waitForPageLoad();
		return driver.isElementPresent(By.xpath("//div[@class='cart-items']/div[1]//p[@id='cart-retail-price']"));
	}

	public boolean verifyYourPriceIsAvailableOnAutoshipCart(){
		return driver.isElementPresent(By.xpath("//div[@class='cart-items']/div[1]//p[@id='cart-price']"));
	}

	public boolean verifyTotalSavingsIsAvailableOnAutoshipCart(){
		driver.waitForPageLoad();
		return driver.isElementPresent(By.xpath("//div[@class='checkout-module-content']//div[@id='module-subtotal'][1]"));
	}

	public boolean validateEditYourInformationLink(){
		driver.quickWaitForElementPresent(WELCOME_USER_DD_LOC);
		return driver.isElementPresent(WELCOME_USER_DD_LOC);
	}

	public boolean validateAccessSolutionTool(){
		//click learn more..
		driver.waitForElementPresent(By.xpath("//div[@id='corp_content']/div/div[1]/div[3]/descendant::a"));
		driver.click(By.xpath("//div[@id='corp_content']/div/div[1]/div[3]/descendant::a"));
		driver.waitForPageLoad();
		driver.waitForElementPresent(By.id("mirror"));
//		String parentWindowID=driver.getWindowHandle();
//		Set<String> set=driver.getWindowHandles();
//		Iterator<String> it=set.iterator();
		boolean status=false;
//		while(it.hasNext()){
//			String childWindowID=it.next();
//			if(!parentWindowID.equalsIgnoreCase(childWindowID)){
//				driver.switchTo().window(childWindowID);
				if(driver.getCurrentUrl().contains("solutiontool")&& driver.isElementPresent(By.id("mirror"))){
					status=true;					
				}
//			}
//		}
		return status;
	}

	public void enterPasswordForUpgradeRCToConsultant(){
		driver.waitForElementPresent(By.xpath("//p[contains(text(),'LOG IN TO TERMINATE MY RETAIL ACCOUNT')]/following::div[@id='terminate-log-in']/div[3]/input"));
		driver.type(By.xpath("//p[contains(text(),'LOG IN TO TERMINATE MY RETAIL ACCOUNT')]/following::div[@id='terminate-log-in']/div[3]/input"), driver.getStoreFrontPassword());
	}

	public void clickOnLoginToTerminateToMyRCAccount(){
		//driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(By.xpath("//p[contains(text(),'LOG IN TO TERMINATE MY RETAIL ACCOUNT')]/following::a[@class='confirm']/input"));
		driver.click(By.xpath("//p[contains(text(),'LOG IN TO TERMINATE MY RETAIL ACCOUNT')]/following::a[@class='confirm']/input"));
		driver.waitForPageLoad();
		driver.pauseExecutionFor(3000);
	}

	public boolean verifyAccountTerminationMessage(){
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'Your old account has been terminated successfully')]"));
		return driver.isElementPresent(By.xpath("//span[contains(text(),'Your old account has been terminated successfully')]"));
	}

	public void clickOnEditMyPWS(){
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'EDIT MY PWS')]"));
		driver.click(By.xpath("//a[contains(text(),'EDIT MY PWS')]"));
	}

	public void enterPhoneNumberOnEditPWS(String number){
		driver.waitForElementPresent(By.id("phone"));
		driver.type(By.id("phone"), number);
	}

	public void clickOnSaveAfterEditPWS(){
		driver.waitForElementPresent(By.xpath("//div[@class='editphotosmode']//input"));
		driver.click(By.xpath("//div[@class='editphotosmode']//input"));
	}
	public void clickOnAddToCRPButtonAfterCancelMyCRP(){
		if(driver.getCountry().equalsIgnoreCase("ca")){
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//button[@id='crp_noButton']"));
			driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]//button[@id='crp_noButton']"));
		}else{
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[1]//button[@id='crp_noButton']"));
			driver.click(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[1]//button[@id='crp_noButton']"));
		}
	}

	public boolean verifyEnrollInCRPPopupAfterClickOnAddToCRP(){
		driver.waitForPageLoad();
		return driver.isElementPresent(By.xpath("//div[@id='popup'][1]"));
	}

	public boolean verifyUpradingToConsulTantPopupForRC(){
		driver.waitForPageLoad();
		if(driver.isElementPresent(By.xpath("//div[@id='activePCPopup' and @style=' display:none;']"))){
			return true;
		}else
			return false;
	}

	public String getAutoshipTemplateUpdatedMsg(){
		driver.quickWaitForElementPresent(By.xpath(".//div[@id='globalMessages']//p"));
		return driver.findElement(By.xpath(".//div[@id='globalMessages']//p")).getText();
	}

	public void clickOnConnectWithAConsultant(){
		driver.waitForElementPresent(By.xpath("//div[@class='hidden-xs']//h3[contains(text(),'CONNECT WITH A CONSULTANT')]/following::a[contains(text(),'CONNECT')][1]"));
		driver.click(By.xpath("//div[@class='hidden-xs']//h3[contains(text(),'CONNECT WITH A CONSULTANT')]/following::a[contains(text(),'CONNECT')][1]"));
	}

	public boolean verifyFindYourSponsorPage(){
		driver.waitForPageLoad();
		System.out.println("current url "+driver.getCurrentUrl());
		return driver.getCurrentUrl().toLowerCase().contains("sponsorpage");
		//return driver.isElementPresent(By.xpath("//h2[contains(text(),'Find Your R+F Sponsor')]"));
	}

	public boolean verifySponsorListIsPresentAfterClickOnSearch(){
		driver.waitForPageLoad();
		return driver.isElementPresent(By.id("search-results"));
	}
	public boolean verifySponsorNameContainRFCorporate(){
		driver.waitForPageLoad();
		return driver.findElement(By.xpath("//div[@id='sponsorInfo']/span")).getText().contains("Corporate");
	}

	public boolean verifyIsSponsorAlreadySelected(){
		return driver.isElementPresent(By.xpath("//div[@id='show-sponsor'][@style='display: none;']"));
	}

	public boolean verifyConfirmationMessagePresentOnUI(){
		return driver.findElement(By.xpath(".//div[@id='globalMessages']//p")).isDisplayed();
	}

	public void enterEditedCardNumber(String cardNumber){
		driver.waitForPageLoad();
		driver.waitForElementPresent(By.id("credit-cards"));  
		JavascriptExecutor js = ((JavascriptExecutor)RFWebsiteDriver.driver);
		js.executeScript("$('#card-nr-masked').hide();$('#card-nr').show(); ", driver.findElement(ADD_NEW_BILLING_CARD_NUMBER_LOC));
		driver.pauseExecutionFor(2000);
		driver.clear(ADD_NEW_BILLING_CARD_NUMBER_LOC);
		driver.type(ADD_NEW_BILLING_CARD_NUMBER_LOC,cardNumber);
		logger.info("New Billing card number enterd as "+cardNumber);  
	}

	public void selectNewShippingAddressState(){
		driver.click(By.xpath("//div[@id='start-new-shipping-address']//select[@id='state']"));
		driver.pauseExecutionFor(1000);
		driver.waitForElementPresent(By.xpath("//div[@id='start-new-shipping-address']//select[@id='state']/option[2]"));
		driver.click(By.xpath("//div[@id='start-new-shipping-address']//select[@id='state']/option[2]"));
		logger.info("State/Province selected");
	}

	public String getSponsorResultAccordingToCID(String CID){
		driver.waitForPageLoad();
		return driver.findElement(By.xpath("//li[contains(text(),'"+CID+"')]")).getText();
	}

	public boolean validateCorpCurrentUrlPresent() {
		return driver.getCurrentUrl().contains("corp");
	}

	public void updateQuantityOfProductToTheSecondProduct(String qty) throws InterruptedException{
		driver.waitForElementPresent(By.id("quantity1"));
		driver.clear(By.id("quantity1"));
		driver.type(By.id("quantity1"),qty);
		logger.info("quantity added is "+qty);
		driver.click(By.xpath("//a[@class='updateLink']"));
		driver.pauseExecutionFor(5500);
		logger.info("Update button clicked after adding quantity");
		driver.waitForPageLoad();
	}

	public void clickUpdateCartBtn() throws InterruptedException{
		driver.waitForElementPresent(UPDATE_CART_BTN_LOC);
		driver.click(UPDATE_CART_BTN_LOC);		
		logger.info("Update cart button clicked "+UPDATE_CART_BTN_LOC);
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public boolean verifyPCPerksInfoOnModalWindow(){
		driver.quickWaitForElementPresent(By.xpath("//div[contains(@class,'pc-perks fancybox')]"));
		return driver.isElementPresent(By.xpath("//div[contains(@class,'pc-perks fancybox')]"));
	}

	public String clickAddToBagAndGetProductName(String productNumber){
		String productName = null;
		if(driver.getCountry().equalsIgnoreCase("us")){
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div["+productNumber+"]/div[2]/div[1]//button"));
			productName = driver.findElement(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div["+productNumber+"]/h3/a")).getText();
			driver.click(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div["+productNumber+"]/div[2]/div[1]//button"));
			return productName.trim();
		}else{
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[contains(@class,'quick-product-wrapper')]/div["+productNumber+"]/div[2]/div[1]//button"));
			productName = driver.findElement(By.xpath("//div[@id='main-content']/div[contains(@class,'quick-product-wrapper')]/div["+productNumber+"]/h3/a")).getText();
			driver.click(By.xpath("//div[@id='main-content']/div[contains(@class,'quick-product-wrapper')]/div["+productNumber+"]/div[2]/div[1]//button"));
			return productName.trim();
		}
	}

}