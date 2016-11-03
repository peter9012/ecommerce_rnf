package com.rf.pages.website.storeFront;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.model.Site;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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
import com.thoughtworks.selenium.webdriven.commands.IsElementPresent;


public class StoreFrontRFWebsiteBasePage extends RFBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontRFWebsiteBasePage.class.getName());

	private final By RODAN_AND_FIELDS_IMG_LOC = By.xpath("//div[@id='header-logo']//a");
	private final By RODAN_AND_FIELDS_LOGO_IMG_LOC = By.xpath("//img[@title='Rodan+Fields']");
	private final By WELCOME_DD_EDIT_CRP_LINK_LOC = By.xpath("//div[@id='account-info-dropdown']/ul[1]//*[contains(text(),'Edit CRP')]");
	//private final By WELCOME_USER_DD_LOC = By.id("account-info-button");
	private final By WELCOME_USER_DD_LOC = By.xpath("//div[@id='account-info-button']/a");
	private final By WELCOME_DD_ORDERS_LINK_LOC = By.xpath("//a[text()='Orders']");
	private final By YOUR_ACCOUNT_DROPDOWN_LOC = By.xpath("//button[@class='btn btn-default dropdown-toggle']");
	private final By WELCOME_DD_BILLING_INFO_LINK_LOC = By.linkText("Billing Info");
	private final By WELCOME_DD_SHIPPING_INFO_LINK_LOC = By.xpath("//a[text()='Shipping Info']");
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

	public StoreFrontConsultantPage clickOnRodanAndFieldsLogo(){
		driver.waitForElementPresent(RODAN_AND_FIELDS_LOGO_IMG_LOC);
		try{
			driver.click(RODAN_AND_FIELDS_LOGO_IMG_LOC);
		}catch(Exception e){
			try{
				driver.findElement(By.xpath("//div[@id='header']//span")).click();
			}catch(Exception e1){
				try{
					driver.findElement(By.xpath("//div[@id='header-middle-top']//a")).click();
				}catch(Exception e2){
					driver.findElement(By.xpath("//*[@id='header']/div/div[2]/a/span")).click();
				}
			}
		}
		logger.info("Rodan and Fields logo clicked");
		driver.waitForLoadingImageToDisappear();
		return new StoreFrontConsultantPage(driver);
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

	public String selectProductAndProceedToBuy() throws InterruptedException{
		driver.pauseExecutionFor(2000);
		applyPriceFilterHighToLow();
		String productName = null;
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][1]"));
		if(driver.findElement(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][1]")).isEnabled()==true){
			productName = driver.findElement(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][1]/preceding::a[1]")).getText();
			driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][1]")));
			//driver.click(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][1]"));
		}
		else{
			productName = driver.findElement(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][2]/preceding::a[1]")).getText();
			driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][2]")));
		}
		logger.info("Add To Bag button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return productName;
	}

	public String getNameOfTheOnlyAddedProductOnCart(){
		String productNameFromCart = null;
		productNameFromCart=driver.findElement(By.xpath("//div[@id='left-shopping']/div[@class='cartItems']//h3")).getText();
		logger.info("product name from cart is "+productNameFromCart);
		return productNameFromCart;
	}

	public void selectProductAndProceedToBuyWithoutFilter() throws InterruptedException{
		driver.waitForPageLoad();
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][1]"));
		String price = driver.findElement(By.xpath("//div[@id='main-content']/descendant::span[@class='your-price'][1]")).getText().split("\\$")[1].trim();
		if(driver.findElement(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][1]")).isEnabled() && (!price.contains("0.00"))==true){
			System.out.println("In if condition");
			driver.click(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][1]"));
		}else
			try{
				driver.click(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][2]"));
			}catch (Exception e) {
				driver.click(By.xpath("//section[contains(@class,'productCatPage')]/div[2]/descendant::button[contains(text(),'ADD TO BAG')][1]"));
				logger.info("2nd Product selected");
			}
		logger.info("Add To Bag button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		if(driver.isElementPresent(By.xpath("//*[@id='productDetailForm']/input[@value='ADD TO BAG']"))){
			driver.click(By.xpath("//*[@id='productDetailForm']/input[@value='ADD TO BAG']"));
		}
	}


	public void selectProductAndProceedToAddToCRP() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/descendant::*[contains(text(),'ADD TO CRP') or @value='Add to crp'][1]"));
		driver.click(By.xpath("//div[@id='main-content']/descendant::*[contains(text(),'ADD TO CRP') or @value='Add to crp'][1]"));
		logger.info("Add to CRP button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnAddToPcPerksButton(){
		driver.waitForPageLoad();
		driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/descendant::input[contains(@value,'ADD to PC Perks')][1]"));
		//driver.click(By.xpath("//div[@id='main-content']/descendant::input[contains(@value,'ADD to PC Perks')][1]"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//div[@id='main-content']/descendant::input[contains(@value,'ADD to PC Perks')][1]")));
		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='OK']"));
			driver.click(By.xpath("//input[@value='OK']"));
			driver.waitForLoadingImageToDisappear();
		}catch(Exception e){

		}
	}

	public void addQuantityOfProduct(String qty) throws InterruptedException{
		//driver.pauseExecutionFor(2000);
		try{
			driver.quickWaitForElementPresent(By.id("quantity0"));
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
		//driver.pauseExecutionFor(2000);
		driver.waitForPageLoad();
	}

	public void clickOnNextBtnAfterAddingProductAndQty() throws InterruptedException{
		//  driver.waitForElementPresent(By.id("submitForm"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.id("submitForm")));
		//driver.click(By.id("submitForm"));
		logger.info("Next button after adding quantity clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public boolean isCartPageDisplayed(){
		driver.waitForElementPresent(By.xpath("//div[@id='left-shopping']/h1"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='left-shopping']/h1")));
	}

	public void addAnotherProduct() throws InterruptedException{
		try{
			driver.waitForElementPresent(By.xpath("//*[contains(text(),'Continue shopping')]"));
			driver.click(By.xpath("//*[contains(text(),'Continue shopping')]"));
			logger.info("Continue shopping link clicked");
			//driver.pauseExecutionFor(2000);
			driver.waitForPageLoad();
		}catch(Exception e){
			Actions action = new Actions(RFWebsiteDriver.driver);
			driver.quickWaitForElementPresent(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"));
			action.moveToElement(driver.findElement(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"))).doubleClick(driver.findElement(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"))).build().perform();
			//driver.click(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"));
			logger.info("Continue shopping link clicked");
			//driver.pauseExecutionFor(2000);
			driver.waitForPageLoad();
		}
		driver.waitForLoadingImageToDisappear();
		applyPriceFilterHighToLow();
		driver.waitForPageLoad();
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][2]"));
		//driver.click(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][2]"));  
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][2]")));
		logger.info("Add To Bag button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public boolean verifyNumberOfProductsInCart(String numberOfProductsInCart){
		driver.waitForElementPresent(By.xpath("//div[@id='left-shopping']/h1/span"));
		return driver.findElement(By.xpath("//div[@id='left-shopping']/h1/span")).getText().contains(numberOfProductsInCart);
	}

	public boolean isCartHasZeroItems(){
		driver.waitForElementPresent(By.xpath("//div[@id='left-shopping']//span[contains(text(),'0 item')]"));
		return driver.isElementPresent(By.xpath("//div[@id='left-shopping']//span[contains(text(),'0 item')]"));
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
		driver.pauseExecutionFor(2000);
		driver.waitForSpinImageToDisappear();
		driver.type(By.id("password"),password);
		logger.info("password entered as "+password);
		driver.type(By.id("the-password-again"),password);
		logger.info("confirm password entered as "+password);
		driver.pauseExecutionFor(2000);
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//*[@id='next-button']")));
		//driver.click(By.xpath("//*[@id='next-button']"));  
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
		//driver.pauseExecutionFor(2000);
		driver.waitForSpinImageToDisappear();
		driver.type(By.id("password"),password);
		logger.info("password entered as "+password);
		driver.type(By.id("the-password-again"),password);
		logger.info("confirm password entered as "+password);
		driver.pauseExecutionFor(2000);
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.id("next-button")));
		// driver.click(By.id("next-button"));  
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
		//driver.pauseExecutionFor(2000);
		driver.waitForSpinImageToDisappear();
		driver.type(By.id("password"),password);
		logger.info("password entered as "+password);
		driver.type(By.id("the-password-again"),password);
		logger.info("confirm password entered as "+password);
		//driver.click(By.xpath("//input[@id='become-pc']/.."));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//input[@id='become-pc']/..")));
		logger.info("check box for PC user checked");
		//driver.click(By.xpath("//input[@id='next-button']"));  
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//input[@id='next-button']")));
		logger.info("Create New Account button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void enterNewPCDetails(String firstName,String lastName,String password, String emailID) throws InterruptedException{
		driver.type(By.id("first-Name"),firstName);
		logger.info("first name entered as "+firstName);
		driver.type(By.id("last-name"),lastName);
		logger.info("last name entered as "+lastName);
		driver.clear(By.id("email-account"));
		driver.type(By.id("email-account"),emailID+"\t");
		logger.info("email entered as "+emailID);
		//driver.pauseExecutionFor(2000);
		driver.waitForSpinImageToDisappear();
		driver.type(By.id("password"),password);
		logger.info("password entered as "+password);
		driver.type(By.id("the-password-again"),password);
		logger.info("confirm password entered as "+password);
		//driver.click(By.xpath("//input[@id='become-pc']/.."));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//input[@id='become-pc']/..")));  
		logger.info("check box for PC user checked");
		//driver.click(By.xpath("//input[@id='next-button']"));  
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//input[@id='next-button']")));
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
		//driver.pauseExecutionFor(5000);
		if(driver.getCountry().equalsIgnoreCase("CA")){
			driver.waitForElementPresent(By.id("address.line1"));
			((JavascriptExecutor) RFWebsiteDriver.driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.id("address.line1")));
			driver.type(By.id("address.line1"),TestConstants.ADDRESS_LINE_1_CA);
			logger.info("Address Line 1 entered is "+TestConstants.ADDRESS_LINE_1_CA);
			driver.type(By.id("address.townCity"),TestConstants.CITY_CA+"\t");
			logger.info("City entered is "+TestConstants.CITY_CA);
			driver.waitForLoadingImageToDisappear();
			try{
				driver.click(By.xpath("//form[@id='addressForm']/div[@class='row'][1]//select[@id='state']"));
				driver.waitForElementPresent(By.xpath("//form[@id='addressForm']/div[@class='row'][1]//select[@id='state']/option[contains(text(),'"+TestConstants.PROVINCE_CA+"')]"));
				driver.click(By.xpath("//form[@id='addressForm']/div[@class='row'][1]//select[@id='state']/option[contains(text(),'"+TestConstants.PROVINCE_CA+"')]"));
			}catch(Exception e){
				driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.id("state")));
				// driver.click(By.id("state"));
				driver.waitForElementPresent(By.xpath("//select[@id='state']/option[contains(text(),'"+TestConstants.PROVINCE_CA+"')]"));
				driver.click(By.xpath("//select[@id='state']/option[contains(text(),'"+TestConstants.PROVINCE_CA+"')]")); 
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
		driver.click(By.xpath("//div[@id='checkout_delivery_address']//select[@id='state']/.."));
		driver.waitForElementPresent(By.xpath("//div[@id='checkout_delivery_address']//select[@id='state']/option[contains(text(),'"+province+"')]"));
		driver.click(By.xpath("//div[@id='checkout_delivery_address']//select[@id='state']/option[contains(text(),'"+province+"')]"));
		logger.info("state selected");
		driver.type(By.id("address.postcode"),postalCode);
		logger.info("postal code entered is "+postalCode);
		driver.type(By.id("address.phonenumber"),phoneNumber);
		logger.info("phone number entered is "+phoneNumber);
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnContinueWithoutSponsorLink() throws InterruptedException{
		driver.waitForElementPresent(By.id("continue-no-sponsor"));
		driver.click(By.id("continue-no-sponsor"));	
		logger.info("continue without sponsor link clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnNextButtonAfterSelectingSponsor() throws InterruptedException{
		driver.waitForElementPresent(By.id("saveAccountAddress"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.id("saveAccountAddress")));
		//driver.click(By.id("saveAccountAddress"));
		logger.info("Next button after selecting sponsor is clicked");
		driver.waitForLoadingImageToDisappear();
		try{
			driver.quickWaitForElementPresent(By.xpath("//*[@id='QAS_RefineBtn']"));
			driver.click(By.xpath("//*[@id='QAS_RefineBtn']"));
			logger.info("Accept the suggested address button clicked");
		}catch(Exception e){

		}
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public void clickOnShippingAddressNextStepBtn() throws InterruptedException{
		try{
			driver.waitForElementPresent(By.xpath("//input[contains(@class,'use_address')]"));
			driver.click(By.xpath("//input[contains(@class,'use_address')]"));
			logger.info("save shipping info clicked");
		}catch(Exception e){
			driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.id("saveShippingInfo")));
			//action.moveToElement(driver.findElement(By.id("saveShippingInfo"))).click(driver.findElement(By.id("saveShippingInfo"))).build().perform();
		}  
		logger.info("Next button on shipping address clicked");  
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
		try{
			driver.click(By.xpath("//input[contains(@class,'use_address')]"));
			logger.info("save shipping info clicked");
		}catch(Exception e){

		}
		driver.waitForLoadingImageToDisappear();
	}

	public void enterNewBillingCardNumber(String cardNumber){
		driver.waitForElementPresent(By.id("card-nr"));
		driver.type(By.id("card-nr"), cardNumber);
		logger.info("Billing card number entered is "+cardNumber);
	}

	public void enterNewBillingNameOnCard(String nameOnCard){
		//driver.waitForElementPresent(By.id("card-name"));
		//driver.clear(By.id("card-name"));
		driver.type(By.id("card-name"),nameOnCard);
		logger.info("card name entered is "+nameOnCard);
	}

	public void selectNewBillingCardExpirationDate(String month,String year){
		//driver.click(By.id("expiryMonth"));
		driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(By.id("expiryMonth")));
		driver.waitForElementPresent(By.xpath("//select[@id='expiryMonth']/option[text()='"+month.toUpperCase()+"']"));
		driver.click(By.xpath("//select[@id='expiryMonth']/option[text()='"+month.toUpperCase()+"']"));
		//driver.click(By.id("expiryYear"));
		driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(By.id("expiryYear")));
		driver.waitForElementPresent(By.xpath("//select[@id='expiryYear']/option[text()='"+year+"']"));
		driver.click(By.xpath("//select[@id='expiryYear']/option[text()='"+year+"']"));
	}

	public void enterNewBillingSecurityCode(String securityCode){
		driver.type(By.id("security-code"), securityCode);
		logger.info("security code entered is "+securityCode);
	}

	public void selectNewBillingCardAddress() throws InterruptedException{
		//driver.waitForElementPresent(By.id("addressBookdropdown"));
		driver.click(By.id("addressBookdropdown"));
		driver.click(By.xpath("//*[@id='addressBookdropdown']/option[1]"));
		logger.info("New Billing card address selected");
	}

	public void clickOnSaveBillingProfile() throws InterruptedException{
		//driver.waitForElementPresent(By.id("submitButton"));
		driver.click(By.id("submitButton"));
		driver.waitForLoadingImageToDisappear();
		logger.info("Save billing profile button clicked");
	}

	public void clickOnBillingNextStepBtn() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='payment-next-button']/input"));
		//((JavascriptExecutor) RFWebsiteDriver.driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//div[@id='payment-next-button']/input")));
		driver.pauseExecutionFor(5000);
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//div[@id='payment-next-button']/input")));
		//driver.click(By.xpath("//div[@id='payment-next-button']/input"));
		logger.info("Next button on billing profile clicked"); 
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnSetupCRPAccountBtn() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@value='Setup CRP Account']"));
		//driver.click(By.xpath("//ul[@style='cursor: pointer;']/li[1]/div"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//ul[@style='cursor: pointer;']/li[1]/div")));
		//driver.click(By.xpath("//form[@id='placeOrderForm1']//a[contains(text(),'I agree that this agreement shall be accepted electronically.')]/ancestor::strong[1]/preceding::div[1]"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//form[@id='placeOrderForm1']//a[contains(text(),'I agree that this agreement shall be accepted electronically.')]/ancestor::strong[1]/preceding::div[1]")));
		//driver.click(By.xpath("//input[@value='Setup CRP Account']"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//input[@value='Setup CRP Account']")));
		logger.info("Next button on billing profile clicked");
		driver.waitForLoadingImageToDisappear();
	}	


	public void clickPlaceOrderBtn()throws InterruptedException{
		//driver.waitForElementPresent(By.id("placeOrderButton"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.id("placeOrderButton")));
		//driver.click(By.id("placeOrderButton"));
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
		//driver.pauseExecutionFor(2000);
	}

	//	public void clickOnRodanAndFieldsLogo(){
	//		//driver.pauseExecutionFor(2000);
	//		try{
	//			driver.turnOffImplicitWaits();
	//			if(driver.IsElementVisible(driver.findElement(By.xpath("//*[@id='header']/div/div[2]/a/span"))))
	//			{
	//				driver.quickWaitForElementPresent(By.xpath("//*[@id='header']/div/div[2]/a/span"));
	//				driver.click(By.xpath("//*[@id='header']/div/div[2]/a/span"));
	//			}
	//			else{
	//
	//				driver.quickWaitForElementPresent(RODAN_AND_FIELDS_IMG_LOC);
	//				driver.click(RODAN_AND_FIELDS_IMG_LOC);
	//
	//			}
	//
	//		}catch(NoSuchElementException e){
	//			try{
	//				driver.click(By.xpath("//img[@title='Rodan+Fields']"));
	//			}catch(NoSuchElementException e1)
	//			{
	//				driver.click(By.xpath("//div[@id='header-middle-top']//a"));
	//			}
	//
	//		}
	//		finally{
	//			driver.turnOnImplicitWaits();
	//		}
	//		logger.info("Rodan and Fields logo clicked");
	//		driver.waitForLoadingImageToDisappear();
	//		driver.waitForPageLoad();
	//		driver.waitForLoadingImageToDisappear();
	//	}

	public StoreFrontConsultantPage dismissPolicyPopup(){
		try {	
			driver.quickWaitForElementPresent(By.id("agree"));
			WebElement we = driver.findElement(By.xpath("//div[@class='shipping-popup-gray']/span[1]"));
			if (we.isDisplayed()){
				we.click();
				driver.click(By.xpath("//input[@value='Continue']"));
				driver.waitForLoadingImageToDisappear();
			}
			//do nothing
		}
		catch (Exception e) {
			System.out.println("Policy Popup Dialog not seen.");
		}
		return null;
	} 

	public void clickRenewLater()  {
		try{
			driver.quickWaitForElementPresent(By.xpath("//div[contains(@class,'fancybox-overlay')]//input[@id='renewLater']"));
			//driver.pauseExecutionFor(2000);
			driver.click(By.xpath("//div[contains(@class,'fancybox-overlay')]//input[@id='renewLater']"));
			logger.info("Renew later button clicked");
			driver.waitForLoadingImageToDisappear();
		}catch(Exception e){

		}
	}


	public void handlePolicies(){
		dismissPolicyPopup();
		clickRenewLater();
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

	public boolean isPCEnrolledCongratsMessagePresent(){
		driver.waitForElementPresent(By.xpath("//div[@id='Congrats']//span[contains(text(),'Welcome to Rodan + Fields')]/parent::h1[contains(text(),'PC Perks')]"));
		return driver.isElementPresent(By.xpath("//div[@id='Congrats']//span[contains(text(),'Welcome to Rodan + Fields')]/parent::h1[contains(text(),'PC Perks')]"));
	}

	public void applyPriceFilterLowToHigh() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//select[@id='sortOptions']"));
		driver.click(By.xpath("//select[@id='sortOptions']"));
		driver.click(By.xpath("//select[@id='sortOptions']/option[3]"));
		logger.info("filter done for low to high price");
		driver.waitForPageLoad();
		driver.waitForLoadingImageToDisappear();
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
		driver.waitForElementPresent(By.xpath("//input[@id='Terms3']/.."));
		driver.pauseExecutionFor(5000);
		try{
			driver.waitForElementPresent(By.xpath("//div[@class='content']/li[2]//input/.."));
			driver.click(By.xpath("//div[@class='content']/li[1]//input/.."));
			driver.click(By.xpath("//div[@class='content']/li[2]//input/.."));
		}catch(NoSuchElementException e){
			driver.waitForElementPresent(By.xpath("//input[@id='Terms2']/.."));
			//driver.click(By.xpath("//input[@id='Terms2']/.."));
			driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//input[@id='Terms2']/..")));
			//driver.click(By.xpath("//input[@id='Terms3']/.."));
			driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//input[@id='Terms3']/..")));
		}
	}

	public void selectNewBillingCardExpirationDate(){
		driver.waitForElementPresent(By.xpath("//select[@id='expiryMonth']"));
		//driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//select[@id='expiryMonth']")));
		driver.click(By.xpath("//select[@id='expiryMonth']"));
		driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(By.xpath("//select[@id='expiryMonth']/option[10]"));
		driver.click(By.xpath("//select[@id='expiryMonth']/option[10]"));
		//driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//select[@id='expiryMonth']/option[10]")));
		driver.pauseExecutionFor(2000);
		driver.click(By.xpath("//select[@id='expiryYear']"));
		//driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(By.xpath("//select[@id='expiryYear']")));
		driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(By.xpath("//select[@id='expiryYear']/option[10]"));
		driver.click(By.xpath("//select[@id='expiryYear']/option[10]"));
		//driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(By.xpath("//select[@id='expiryYear']/option[10]")));  
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
		//driver.pauseExecutionFor(2000);
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
			driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//input[@id='Terms2']/..")));
			//driver.click(By.xpath("//input[@id='Terms2']/.."));  
			logger.info("I Acknowledge PC Account Checkbox selected");
		}catch(Exception e){
			driver.waitForElementPresent(By.xpath("//form[@id='placeOrderForm1']/ul/div[1]//li[1]/div"));
			driver.click(By.xpath("//form[@id='placeOrderForm1']/ul/div[1]//li[1]/div"));  
			logger.info("I Acknowledge PC Account Checkbox selected");
		}
	}

	public void checkPCPerksTermsAndConditionsCheckBox(){
		try{
			driver.waitForElementPresent(By.xpath("//input[@id='Terms3']/.."));
			driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//input[@id='Terms3']/..")));
			//driver.click(By.xpath("//input[@id='Terms3']/.."));  
			logger.info("PC Perks terms and condition checkbox selected");
		}catch(Exception e){
			driver.waitForElementPresent(By.xpath("//form[@id='placeOrderForm1']/ul/div[1]//li[2]/div"));
			driver.click(By.xpath("//form[@id='placeOrderForm1']/ul/div[1]//li[2]/div"));  
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
		//driver.pauseExecutionFor(2000);
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
		//driver.pauseExecutionFor(2000);
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
		//driver.pauseExecutionFor(2000);
	}

	public StoreFrontCartAutoShipPage clickEditCrpLinkPresentOnWelcomeDropDown() throws InterruptedException{
		//driver.waitForElementPresent(WELCOME_DD_EDIT_CRP_LINK_LOC);
		driver.click(WELCOME_DD_EDIT_CRP_LINK_LOC);
		logger.info("User has clicked on edit Crp link from welcome drop down");
		driver.waitForPageLoad();
		driver.waitForLoadingImageToDisappear();
		return new StoreFrontCartAutoShipPage(driver);
	}

	public boolean isEditCRPLinkPresent(){
		try{
			driver.waitForElementPresent(WELCOME_DD_EDIT_CRP_LINK_LOC);
			driver.findElement(WELCOME_DD_EDIT_CRP_LINK_LOC);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public boolean validateExistingUserPopUp(String userid,String firstName,String lastName,String password,String countryId){
		RFO_DB = driver.getDBNameRFO();
		String pcmailid=null;
		String rcmailid=null;
		String consultantmailid=null;

		List<Map<String, Object>> randomPCUserEmailIdList =  null;
		List<Map<String, Object>> randomRCUserEmailIdList =  null;
		List<Map<String, Object>> randomConsultantEmailIdList =  null;
		//  driver.type(By.id("first-Name"),firstName);
		//  logger.info("first name entered as "+firstName);
		//  driver.type(By.id("last-name"),lastName);
		//  logger.info("last name entered as "+lastName);
		if(userid.equalsIgnoreCase("pc")){
			randomPCUserEmailIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcmailid = String.valueOf(getValueFromQueryResult(randomPCUserEmailIdList, "Username"));

			driver.findElement(By.id("email-account")).sendKeys(pcmailid);
			logger.info("email entered as "+pcmailid);
			try{
				driver.click(By.id("new-password-account"));
			}catch(NoSuchElementException e){
				driver.click(By.id("password"));
			}
		}else if(userid.equalsIgnoreCase("rc")){
			randomRCUserEmailIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcmailid = String.valueOf(getValueFromQueryResult(randomRCUserEmailIdList, "Username"));

			driver.findElement(By.id("email-account")).sendKeys(rcmailid);
			logger.info("email entered as "+rcmailid);
			try{
				driver.click(By.id("new-password-account"));
			}catch(NoSuchElementException e){
				driver.click(By.id("password"));
			}
		}else{
			randomConsultantEmailIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantmailid = String.valueOf(getValueFromQueryResult(randomConsultantEmailIdList, "Username"));
			driver.findElement(By.id("email-account")).sendKeys(consultantmailid);
			logger.info("email entered as "+consultantmailid);
			try{
				driver.click(By.id("new-password-account"));
			}catch(NoSuchElementException e){
				driver.click(By.id("password"));
			}
		}
		//driver.pauseExecutionFor(2000);
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
		return driver.isElementPresent(By.xpath("//div[@class='fancybox-inner']"));
	}


	public boolean validateSendMailToResetMyPasswordFunctionalityPC(){
		driver.waitForElementPresent(By.xpath("//div[@id='activePCPopup']//input[contains(@class,'resetPasswordEmail')]"));
		JavascriptExecutor js = ((JavascriptExecutor)RFWebsiteDriver.driver);
		js.executeScript("arguments[0].click();", driver.findElement(By.xpath("//div[@id='activePCPopup']//input[contains(@class,'resetPasswordEmail')]")));
		//driver.pauseExecutionFor(2000);
		driver.waitForLoadingImageToDisappear();
		//driver.pauseExecutionFor(2000);
		return driver.isElementPresent(By.xpath("//div[@class='fancybox-inner']"));
	}

	public boolean validateCancelEnrollmentFunctionalityPC(){
		driver.waitForElementPresent(By.xpath("//div[@id='activePCPopup']//input[contains(@class,'cancelEnrollment')]"));
		driver.click(By.xpath("//div[@id='activePCPopup']//input[contains(@class,'cancelEnrollment')]"));
		//driver.pauseExecutionFor(2000);
		driver.waitForLoadingImageToDisappear();
		//driver.pauseExecutionFor(2000);
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
		//driver.pauseExecutionFor(2000);
		driver.waitForLoadingImageToDisappear();
		return driver.isElementPresent(By.xpath("//div[@id='header']//a[@title='BECOME A CONSULTANT']")); 
	}


	public boolean validateSendMailToResetMyPasswordFunctionalityConsultant(){
		driver.waitForElementPresent(By.xpath("//div[@id='notavailablePopup']//input[contains(@class,'resetPasswordEmail')]"));
		JavascriptExecutor js = ((JavascriptExecutor)RFWebsiteDriver.driver);
		js.executeScript("arguments[0].click();", driver.findElement(By.xpath("//div[@id='notavailablePopup']//input[contains(@class,'resetPasswordEmail')]")));
		//driver.findElement(By.xpath("//div[@id='notavailablePopup']//input[@class='resetPasswordEmail']")).click();
		//driver.pauseExecutionFor(2000);
		driver.waitForLoadingImageToDisappear();
		//driver.pauseExecutionFor(2000);
		return driver.isElementPresent(By.xpath("//div[@id='header']//a[@title='BECOME A CONSULTANT']")); 

	}

	public boolean validateCancelEnrollmentFunctionalityConsultant(){
		driver.waitForElementPresent(By.xpath("//div[@id='notavailablePopup']//input[contains(@class,'cancelEnrollment')]"));
		driver.click(By.xpath("//div[@id='notavailablePopup']//input[contains(@class,'cancelEnrollment')]"));
		//driver.pauseExecutionFor(2000);
		driver.waitForLoadingImageToDisappear();
		return driver.isElementPresent(By.xpath("//div[@id='header']//a[@title='BECOME A CONSULTANT']"));
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
		//The below code is yielding false output,so commenting it
		/*
		//		randomActiveSitePrefixList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_SITE_PREFIX_RFO,countryID),RFO_DB);
		//		activeSitePrefix = (String) getValueFromQueryResult(randomActiveSitePrefixList, "SitePrefix");			
				PWS = "http://bhopkins"+driver.getBizPWSURL()+"/"+country.toLowerCase();
				System.out.println(PWS);
				logger.info("PWS is "+PWS);
				if(driver.getURL().contains("qa"))
				{
					randomActiveSitePrefixList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_SITE_PREFIX_RFO,countryID),RFO_DB);
					activeSitePrefix = (String) getValueFromQueryResult(randomActiveSitePrefixList, "SitePrefix");   
					PWS = "http://"+activeSitePrefix+".qamyrandf"+".biz/"+country.toLowerCase();
					logger.info("PWS is "+PWS);	
				}
				else if(driver.getURL().contains("tst")){
					randomActiveSitePrefixList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_SITE_PREFIX_RFO,countryID),RFO_DB);
					activeSitePrefix = (String) getValueFromQueryResult(randomActiveSitePrefixList, "SitePrefix");   
					PWS = "http://"+activeSitePrefix+".tstrflmyrandf"+".biz/"+country.toLowerCase();
					logger.info("PWS is "+PWS);			
				}
				else{
		randomActiveSitePrefixList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_SITE_PREFIX_RFO,countryID),RFO_DB);
		activeSitePrefix = (String) getValueFromQueryResult(randomActiveSitePrefixList, "SitePrefix");   
		PWS = "http://"+activeSitePrefix+".myrfo"+env+".biz/"+country.toLowerCase();
		logger.info("PWS is "+PWS);				
				}
		 */		
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

		// The below code is yielding false output,so commenting it
		/*		if(driver.getURL().contains("qa"))
		{
			randomActiveSitePrefixList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_SITE_PREFIX_RFO,countryID),RFO_DB);
			activeSitePrefix = (String) getValueFromQueryResult(randomActiveSitePrefixList, "SitePrefix");   
			PWS = "http://"+activeSitePrefix+".qamyrandf"+".com/"+country.toLowerCase();
			logger.info("PWS is "+PWS);	
		}
		else if(driver.getURL().contains("tst")){
			randomActiveSitePrefixList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_SITE_PREFIX_RFO,countryID),RFO_DB);
			activeSitePrefix = (String) getValueFromQueryResult(randomActiveSitePrefixList, "SitePrefix");   
			PWS = "http://"+activeSitePrefix+".tstrflmyrandf"+".com/"+country.toLowerCase();
			logger.info("PWS is "+PWS);			
		}
		else{
			randomActiveSitePrefixList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_SITE_PREFIX_RFO,countryID),RFO_DB);
			activeSitePrefix = (String) getValueFromQueryResult(randomActiveSitePrefixList, "SitePrefix");			
			PWS = "http://"+activeSitePrefix+".myrfo"+env+".com/"+country.toLowerCase();
			logger.info("PWS is "+PWS);			
		}
		 */

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
			driver.pauseExecutionFor(2000);
			if(driver.getCurrentUrl().contains("sitenotfound")){
				logger.info("SITE NOT FOUND error,try new PWS");
				continue;
			}
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
		//driver.pauseExecutionFor(2000);
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
		driver.pauseExecutionFor(2000);
		WebElement shopSkinCare = driver.findElement(By.id("our-products"));
		actions.moveToElement(shopSkinCare).pause(1000).click().build().perform();
		if(driver.isElementPresent(By.xpath("//ul[@id='dropdown-menu' and @style='display: block;']//a[text()='All Products']"))==false){
			logger.warn("HEADER LINKS ARE NOT PRESENT..loading the shop URL");
			driver.get(driver.getCurrentUrl()+"/quick-shop/quickShop");			
		}else{
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

		}
		driver.waitForPageLoad();
		driver.waitForLoadingImageToDisappear();
	}

	@SuppressWarnings("deprecation")
	public void hoverOnShopLinkAndClickAllProductsLinks(){
		Actions actions = new Actions(RFWebsiteDriver.driver);
		driver.waitForElementPresent(By.id("our-products")); 
		WebElement shopSkinCare = driver.findElement(By.id("our-products"));
		actions.moveToElement(shopSkinCare).pause(1000).click().build().perform();
		driver.pauseExecutionFor(2000);
		if(driver.isElementPresent(By.xpath("//ul[@id='dropdown-menu' and @style='display: block;']//a[text()='All Products']"))==false){
			logger.warn("HEADER LINKS ARE NOT PRESENT..loading the shop URL");
			driver.get(driver.getCurrentUrl()+"/quick-shop/quickShop");
		}else{
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

		}
		driver.waitForPageLoad();
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnWelcomeDropDown() throws InterruptedException{
		driver.waitForElementPresent(WELCOME_USER_DD_LOC);
		((JavascriptExecutor)RFWebsiteDriver.driver).executeScript("arguments[0].click();", driver.findElement(WELCOME_USER_DD_LOC));
		logger.info("clicked on welcome drop down");		
	}

	public StoreFrontOrdersPage clickOrdersLinkPresentOnWelcomeDropDown() throws InterruptedException{
		Actions actions = new Actions(RFWebsiteDriver.driver);
		driver.waitForElementPresent(WELCOME_USER_DD_LOC);
		actions.moveToElement(driver.findElement(WELCOME_USER_DD_LOC)).perform();
		driver.waitForElementPresent(WELCOME_DD_ORDERS_LINK_LOC);
		actions.moveToElement(driver.findElement(WELCOME_DD_ORDERS_LINK_LOC)).click().perform();
		logger.info("User has clicked on orders link from welcome drop down");
		driver.waitForPageLoad();
		driver.waitForLoadingImageToDisappear();
		return new StoreFrontOrdersPage(driver);
	}

	public void clickOnYourAccountDropdown(){
		driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(YOUR_ACCOUNT_DROPDOWN_LOC);
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
		////driver.pauseExecutionFor(2000);
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
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][1]"));
		if(driver.findElement(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][1]")).isEnabled()==true)
			driver.click(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][1]"));
		else
			driver.click(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][2]"));
		logger.info("Add To Bag button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public void clickAddToBagButtonWithoutFilter() throws InterruptedException{  
		driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][1]"));
		String firstProductPrice = driver.findElement(By.xpath("//div[@id='main-content']/descendant::span[@class='your-price'][1]")).getText().split("\\$")[1].trim();
		if(firstProductPrice.contains("0.00")){
			driver.click(By.xpath("//section[contains(@class,'productCatPage')]/div[2]/descendant::button[contains(text(),'ADD TO BAG')][1]"));
			logger.info("2nd Product selected first product having ");
		}else{
			driver.click(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][1]"));
			logger.info("Add To Bag button clicked of first product");
		}driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public void clickAddToBagButton(String country) throws InterruptedException{
		applyPriceFilterHighToLow();
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][1]"));
		if(driver.findElement(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][1]")).isEnabled()==true)
			driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][1]")));
		else
			driver.click(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')][2]"));
		logger.info("Add To Bag button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
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

	//	public String getQuantityOfProductFromAutoshipTemplate(){
	//		String quantity = driver.findElement(By.xpath("//div[@class='order-summary-left spacer'][2]/div[1]/div[2]/div[2]/div[3]")).getText();
	//		return quantity;
	//	}

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
		//driver.pauseExecutionFor(2000);
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
		driver.waitForLoadingImageToDisappear();
		return new StoreFrontBillingInfoPage(driver);
	}

	public StoreFrontShippingInfoPage clickShippingLinkPresentOnWelcomeDropDown() throws InterruptedException{
		//driver.waitForElementPresent(WELCOME_DD_SHIPPING_INFO_LINK_LOC);
		driver.click(WELCOME_DD_SHIPPING_INFO_LINK_LOC);	
		driver.waitForLoadingImageToDisappear();
		logger.info("User has clicked on shipping link from welcome drop down");
		return new StoreFrontShippingInfoPage(driver);
	}

	public void clickAddNewShippingProfileLink() throws InterruptedException{
		try{
			driver.quickWaitForElementPresent(By.xpath("//a[text()='Add new shipping address »']"));
			//driver.click(By.xpath("//a[text()='Add new shipping address »']"));
			driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//a[text()='Add new shipping address »']")));
			logger.info("Ads new shipping profile link clicked");
		}
		catch(NoSuchElementException e){
			driver.waitForElementPresent(ADD_NEW_SHIPPING_LINK_LOC);
			driver.click(ADD_NEW_SHIPPING_LINK_LOC);
			logger.info("Ads new shipping profile link clicked");
		}
	}

	public void enterNewShippingAddressPostalCode(String postalCode){
		driver.type(By.id("postcode"),postalCode);
	}

	public void enterNewShippingAddressPhoneNumber(String phoneNumber){
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
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/descendant::*[contains(text(),'ADD TO CRP') or @value='Add to crp'][1]"));
		driver.click(By.xpath("//div[@id='main-content']/descendant::*[contains(text(),'ADD TO CRP') or @value='Add to crp'][1]"));
		logger.info("Add to CRP button clicked");
		driver.waitForLoadingImageToDisappear();

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
		try{
			driver.waitForElementPresent(By.xpath("//div[@id='payment-next-button']/input"));
			driver.click(By.xpath("//div[@id='payment-next-button']/input"));
		}catch(Exception e){
			driver.waitForElementPresent(By.xpath("//div[@id='start-shipping-method']/div[2]/div/input"));
			driver.click(By.xpath("//*[@id='start-shipping-method']/div[2]/div/input"));	
		}		
		logger.info("Next button on clicked"); 
		driver.waitForLoadingImageToDisappear();
	}

	public StoreFrontAccountInfoPage clickAccountInfoLinkPresentOnWelcomeDropDown() throws InterruptedException{
		driver.waitForElementPresent(WELCOME_DD_ACCOUNT_INFO_LOC);
		driver.click(WELCOME_DD_ACCOUNT_INFO_LOC);		
		logger.info("User has clicked on account link from welcome drop down");
		driver.waitForLoadingImageToDisappear();
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
		//driver.waitForElementPresent(By.xpath("//select[@id='sortOptions']"));
		//highlightElement(driver.findElement(By.xpath("//select[@id='sortOptions']")));
		driver.click(By.xpath("//select[@id='sortOptions']"));
		driver.click(By.xpath("//select[@id='sortOptions']/option[2]"));
		logger.info("filter done for high to low price");
		driver.waitForPageLoad();
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
	}

	public void deselectPriceFilter() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//select[@id='sortOptions']"));
		driver.click(By.xpath("//select[@id='sortOptions']"));
		driver.click(By.xpath("//select[@id='sortOptions']/option[1]"));
		logger.info("deselect the price filter");
	}

	public String getProductPriceBeforeApplyFilter(){
		return driver.findElement(By.xpath("//div[@id='main-content']/descendant::span[@class='your-price'][1]")).getText().split("\\$")[1].trim();
	}

	public void clickOnContinueShoppingLink(){
		try{
			driver.waitForElementPresent(By.xpath("//a[contains(text(),'Continue shopping')]"));
			driver.click(By.xpath("//a[contains(text(),'Continue shopping')]"));
		}
		catch(Exception e){
			try{
				driver.quickWaitForElementPresent(By.xpath("//div[@id='left-shopping']/div[2]//a[contains(text(),'Continue')]"));
				driver.click(By.xpath("//div[@id='left-shopping']/div[2]//a[contains(text(),'Continue')]")); 
			}
			catch(NoSuchElementException e2){
				driver.quickWaitForElementPresent(By.xpath("//*[@value='ADD MORE ITEMS']"));
				driver.click(By.xpath("//*[@value='ADD MORE ITEMS']")); 
			}

		}
		driver.waitForPageLoad();
	}

	public void selectAProductAndAddItToPCPerks(){
		driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/descendant::input[contains(@value,'ADD to PC Perks')][1]"));
		driver.click(By.xpath("//div[@id='main-content']/descendant::input[contains(@value,'ADD to PC Perks')][1]"));
		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='OK']"));
			driver.click(By.xpath("//input[@value='OK']"));
		}catch(Exception e){
		}
		logger.info("Add To PC perks button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public boolean isBillingProfileIsSelectedByDefault(String profileName){
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-billing-profiles']//span[contains(text(),'"+profileName+"')]/following::input[@checked='checked']"));
		return driver.isElementPresent(By.xpath("//div[@id='multiple-billing-profiles']//span[contains(text(),'"+profileName+"')]/following::input[@checked='checked']"));
	}

	public boolean isTheBillingAddressPresentOnPage(String firstName){
		boolean isFirstNamePresent = false;
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-billing-profiles']/div"));

		//List<WebElement> allBillingProfiles = driver.findElements(By.xpath("//div[@id='multiple-billing-profiles']/div[contains(@class,'sel-profile')]/p[1]/span[@class='font-bold']"));
		List<WebElement> allBillingProfiles = driver.findElements(By.xpath("//div[@id='multiple-billing-profiles']//p[1]/span[@class='font-bold']"));
		for(WebElement e:allBillingProfiles){
			if(e.getText().contains(firstName)==true){
				isFirstNamePresent=true;
				break;
			}
		}
		return isFirstNamePresent;
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
		driver.type(By.id("webSitePrefix"), name);
		driver.findElement(By.id("webSitePrefix")).sendKeys(Keys.TAB);
		driver.pauseExecutionFor(2000);
		try{
			if(driver.isElementPresent(By.xpath("//span[@class='icon-search']"))==true){
				driver.click(By.xpath("//span[@class='icon-search']"));
				driver.findElement(By.id("webSitePrefix")).sendKeys(Keys.TAB);
				driver.pauseExecutionFor(1500);
			}}catch(Exception e){
				try{
					driver.waitForElementNotPresent(By.xpath("//h3[text()='DECIDE TODAY HOW TOMORROW LOOKS']"));
					driver.click(By.xpath("//h3[text()='DECIDE TODAY HOW TOMORROW LOOKS']"));
				}catch(Exception e1){
					driver.click(By.xpath("//form[@id='crpEnrollment']//img[contains(@src,'pulse-blue')]")); 
				} } 
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(2000);
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

	public void selectDifferentProductAndAddItToCRP() throws InterruptedException{
		applyPriceFilterHighToLow();		
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/descendant::*[contains(text(),'ADD TO CRP') or @value='Add to crp'][1]"));
		driver.click(By.xpath("//div[@id='main-content']/descendant::*[contains(text(),'ADD TO CRP') or @value='Add to crp'][1]"));
		logger.info("Add to CRP button clicked");
		driver.waitForLoadingImageToDisappear();

		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='OK']"));
			driver.click(By.xpath("//input[@value='OK']"));
			driver.waitForLoadingImageToDisappear();
		}catch(Exception e){

		}
	}

	public void selectProductAndProceedToBuyForPC() throws InterruptedException{
		driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/descendant::input[contains(@value,'ADD to PC Perks')][1]"));
		driver.click(By.xpath("//div[@id='main-content']/descendant::input[contains(@value,'ADD to PC Perks')][1]"));
		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='OK']"));
			driver.click(By.xpath("//input[@value='OK']"));
		}catch(Exception e){
		}
		logger.info("Add To PC perks button clicked");
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
		if(driver.findElement(By.xpath("//div[@id='popup-review']/div/p")).getText().contains("This is not a PC Perks order.")){
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
		return driver.isElementPresent(By.xpath("//div[@id='main-content']/descendant::span[@class='old-price'][1]"));
	}

	public boolean verifyYourPriceIsAvailableOnProductsPage(){		
		return driver.isElementPresent(By.xpath("//div[@id='main-content']/descendant::span[@class='your-price'][1]"));		
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
		driver.quickWaitForElementPresent(By.xpath("//a[text()='Personalize my Profile']"));
		return driver.isElementPresent(By.xpath("//a[text()='Personalize my Profile']"));
	}

	public boolean validateAccessSolutionTool(){
		boolean status = false;
		//click learn more..	
		driver.get(driver.getCurrentUrl()+"/dynamic/url/solutionTool");
		//		
		//		driver.waitForElementPresent(By.xpath("//div[@id='corp_content']/div/div[1]/div[3]/descendant::a"));
		//		driver.click(By.xpath("//div[@id='corp_content']/div/div[1]/div[3]/descendant::a"));
		driver.waitForPageLoad();
		driver.waitForElementPresent(By.id("mirror"));
		if(driver.getCountry().equalsIgnoreCase("us")){
			String parentWindowID=driver.getWindowHandle();
			Set<String> set=driver.getWindowHandles();
			Iterator<String> it=set.iterator();
			while(it.hasNext()){
				String childWindowID=it.next();
				if(!parentWindowID.equalsIgnoreCase(childWindowID)){
					driver.switchTo().window(childWindowID);
					if(driver.getCurrentUrl().contains("solutiontool")&& driver.isElementPresent(By.id("mirror"))){
						status=true;					
					}
				}
			}

		}else{			
			if(driver.getCurrentUrl().contains("solutiontool")&& driver.isElementPresent(By.id("mirror"))){
				status=true;					
			}
		}

		return status;
	}

	public void enterPasswordForUpgradeRCToConsultant(){
		driver.waitForElementPresent(By.xpath("//p[contains(text(),'LOG IN TO TERMINATE MY RETAIL ACCOUNT')]/following::div[@id='terminate-log-in']/div[3]/input"));
		driver.type(By.xpath("//p[contains(text(),'LOG IN TO TERMINATE MY RETAIL ACCOUNT')]/following::div[@id='terminate-log-in']/div[3]/input"), driver.getStoreFrontPassword());
	}

	public void clickOnLoginToTerminateToMyRCAccount(){
		////driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(By.xpath("//p[contains(text(),'LOG IN TO TERMINATE MY RETAIL ACCOUNT')]/following::a[@class='confirm']/input"));
		driver.click(By.xpath("//p[contains(text(),'LOG IN TO TERMINATE MY RETAIL ACCOUNT')]/following::a[@class='confirm']/input"));
		driver.waitForPageLoad();
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(3000);
	}

	public boolean verifyAccountTerminationMessage(){
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'Your old account has been terminated successfully')]"));
		return driver.isElementPresent(By.xpath("//span[contains(text(),'Your old account has been terminated successfully')]"));
	}

	public void clickOnEditMyPWS(){
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'Edit My PWS')]"));
		driver.click(By.xpath("//a[contains(text(),'Edit My PWS')]"));
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
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/descendant::*[contains(text(),'ADD TO CRP') or @value='Add to crp'][1]"));
		driver.click(By.xpath("//div[@id='main-content']/descendant::*[contains(text(),'ADD TO CRP') or @value='Add to crp'][1]"));
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
		//driver.pauseExecutionFor(2000);
		driver.clear(ADD_NEW_BILLING_CARD_NUMBER_LOC);
		driver.type(ADD_NEW_BILLING_CARD_NUMBER_LOC,cardNumber);
		logger.info("New Billing card number enterd as "+cardNumber);  
	}

	public void selectNewShippingAddressState(String state){
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//div[@id='start-new-shipping-address']//select[@id='state']")));
		//driver.pauseExecutionFor(2000);
		driver.waitForElementPresent(By.xpath("//div[@id='start-new-shipping-address']//select[@id='state']/option[contains(text(),'"+state+"')]"));
		driver.click(By.xpath("//div[@id='start-new-shipping-address']//select[@id='state']/option[contains(text(),'"+state+"')]"));
		logger.info("State/Province selected");
	}

	public String getSponsorResultAccordingToCID(String CID){
		driver.waitForPageLoad();
		return driver.findElement(By.xpath("//li[contains(text(),'"+CID+"')]")).getText();
	}

	public boolean isSearchedSponsorIdPresentInSearchList(String CID){
		driver.waitForPageLoad();
		return driver.isElementPresent(By.xpath("//li[contains(text(),'"+CID+"')]"));
	}

	public boolean validateCorpCurrentUrlPresent() {
		return driver.getCurrentUrl().contains("corp");
	}

	public void updateQuantityOfProductToTheSecondProduct(String qty) throws InterruptedException{
		driver.waitForElementPresent(By.id("quantity1"));
		driver.clear(By.id("quantity1"));
		driver.type(By.id("quantity1"),qty);
		logger.info("quantity added is "+qty);
		driver.click(By.xpath("//form[@id='updateCartForm1']/a"));
		//driver.pauseExecutionFor(2000);
		logger.info("Update button clicked after adding quantity");
		driver.waitForPageLoad();
	}

	public void clickUpdateCartBtn() throws InterruptedException{
		driver.waitForElementPresent(UPDATE_CART_BTN_LOC);
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(UPDATE_CART_BTN_LOC));		
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
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')]["+productNumber+"]"));
		productName = driver.findElement(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')]["+productNumber+"]/preceding::a[1]")).getText();
		// driver.click(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')]["+productNumber+"]"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//div[@id='main-content']/descendant::button[contains(text(),'ADD TO BAG')]["+productNumber+"]")));
		return productName.trim();
	}

	public void clickAddNewBillingProfileLink() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'Add new billing profile')]"));
		driver.pauseExecutionFor(2000);
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//a[contains(text(),'Add new billing profile')]")));
		//driver.click(By.xpath("//a[contains(text(),'Add new billing profile')]"));
		logger.info("Add New Billing Profile link clicked");
	}

	public boolean verifyAddNewBillingProfileLinkIsPresent(){
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'Add new billing profile')]"));
		//driver.pauseExecutionFor(2000);
		return driver.isElementPresent(By.xpath("//a[contains(text(),'Add new billing profile')]"));
	}

	public String getPSTDate(){
		Date startTime = new Date();
		TimeZone pstTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
		DateFormat formatter = DateFormat.getDateInstance();
		formatter.setTimeZone(pstTimeZone);
		String formattedDate = formatter.format(startTime);
		logger.info("PST Date is "+formattedDate);
		return formattedDate;
	}

	public void selectShippingAddress(String shippingProfileName){
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']//span[contains(text(),'"+shippingProfileName+"')]/ancestor::div[1]//span[@class='radio-button shiptothis']/input"));
		driver.click(By.xpath("//div[@id='multiple-addresses-summary']//span[contains(text(),'"+shippingProfileName+"')]/ancestor::div[1]//span[@class='radio-button shiptothis']/input"));
		logger.info("Shipping profile selected as: "+shippingProfileName);
	}

	public void selectBillingAddress(String billingProfileName){
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-billing-profiles']//span[contains(text(),'"+billingProfileName+"')]/ancestor::div[1]//span[@class='radio-button billtothis']/input"));
		driver.click(By.xpath("//div[@id='multiple-billing-profiles']//span[contains(text(),'"+billingProfileName+"')]/ancestor::div[1]//span[@class='radio-button billtothis']/input"));
		logger.info("Billing profile selected as: "+billingProfileName);
	}

	public void enterNewShippingAddressName(String name){
		//		driver.waitForElementPresent(By.id("new-attention"));
		//		driver.clear(By.id("new-attention"));
		driver.type(By.id("new-attention"),name);
		logger.info("New Shipping Address name is "+name);
	}

	public void enterNewShippingAddressCity(String city){
		driver.type(By.id("townCity"),city);
	}

	public void clickOnSaveShippingProfile() throws InterruptedException{
		driver.turnOffImplicitWaits();
		try{

			driver.click(By.id("saveShippingAddreessId"));
		}catch(Exception e){
			driver.click(By.id("saveCrpShippingAddress"));
		}
		driver.waitForLoadingImageToDisappear();
		logger.info("Save shipping profile button clicked");
		try{
			driver.quickWaitForElementPresent(By.id("QAS_RefineBtn"));
			driver.click(By.id("QAS_RefineBtn"));
			logger.info("Accept New shipping address button clicked");
		}catch(NoSuchElementException e1){

		}		
		driver.turnOnImplicitWaits();
		driver.waitForLoadingImageToDisappear();
	}

	public boolean isNewlyCreatedShippingProfileIsSelectedByDefault(String profileName){
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-addresses-summary']//span[contains(text(),'"+profileName+"')]/../following-sibling::p//input[@checked='checked']"));
		return driver.isElementPresent(By.xpath("//div[@id='multiple-addresses-summary']//span[contains(text(),'"+profileName+"')]/../following-sibling::p//input[@checked='checked']"));
	}

	public void enterNewShippingAddressLine1DuringEnrollment(String addressLine1){
		driver.clear(By.id("address-1"));
		driver.type(By.id("address-1"),addressLine1);
		logger.info("New Shipping Address is "+addressLine1);
	}

	public String getDefaultSelectedBillingAddressName(){
		driver.waitForElementPresent(By.xpath("//input[@checked='checked' and @name='bill-card']/../../preceding::p[3]/span[1]"));
		return driver.findElement(By.xpath("//input[@checked='checked' and @name='bill-card']/../../preceding::p[3]/span[1]")).getText();
	}

	public String getFirstBillingProfileName(){
		return driver.findElement(By.xpath("//div[@id='multiple-billing-profiles']/div[1]//p[1]/span[1]")).getText();
	}

	public void enterNewShippingAddressLine1(String addressLine1){
		//		driver.waitForElementPresent(By.id("new-address-1"));
		//		driver.clear(By.id("new-address-1"));
		driver.type(By.id("new-address-1"),addressLine1);
		logger.info("New Shipping Address is "+addressLine1);
	}

	public void clickOnEditForDefaultShippingAddress() throws InterruptedException{
		driver.navigate().refresh();
		driver.waitForPageLoad();
		driver.pauseExecutionFor(5000);
		clickOnEditShipping();
		driver.waitForElementPresent(By.xpath("//input[contains(@name,'shipping')][@checked='checked']/ancestor::div[contains(@class,'address-section')]//a[text()='Edit']"));
		driver.click(By.xpath("//input[contains(@name,'shipping')][@checked='checked']/ancestor::div[contains(@class,'address-section')]//a[text()='Edit']"));
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnEditShipping() throws InterruptedException{
		//driver.waitForElementPresent(By.xpath("//div[@id='checkout_summary_deliverymode_div']//a/ancestor::div[@style='display: block;']"));		
		JavascriptExecutor js = (JavascriptExecutor)(RFWebsiteDriver.driver);
		js.executeScript("arguments[0].click();", driver.findElement(By.xpath("//div[@id='checkout_summary_deliverymode_div']//a")));
		driver.waitForLoadingImageToDisappear();
		logger.info("Edit Shipping link clicked");			
	}

	public void clickOnDefaultBillingProfileEdit() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@checked='checked' and @name='bill-card']/preceding::p[1]/a"));
		((JavascriptExecutor) RFWebsiteDriver.driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//input[@checked='checked' and @name='bill-card']/preceding::p[1]/a")));
		//driver.click(By.xpath("//input[@checked='checked' and @name='bill-card']/preceding::p[1]/a"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//input[@checked='checked' and @name='bill-card']/preceding::p[1]/a")));
		driver.waitForLoadingImageToDisappear();
	}

	public String getDefaultSelectedShippingAddress(){
		//driver.waitForElementPresent(By.xpath("//input[@checked='checked']/preceding::span[@class='font-bold'][1]"));
		return driver.findElement(By.xpath("//input[@checked='checked']/preceding::span[@class='font-bold'][1]")).getText();
	}

	public void clickOnEditOfBillingProfile(String profileName){
		driver.waitForLoadingImageToDisappear();
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'"+profileName+"')]/ancestor::div[1]//a[text()='Edit']"));
		driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(By.xpath("//span[contains(text(),'"+profileName+"')]/ancestor::div[1]//a[text()='Edit']")));
		driver.waitForPageLoad();
		logger.info("billing profile"+profileName+" 's edit link clicked");
	}

	public void clickOnEditOfShippingProfile(String profileName){
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'"+profileName+"')]/ancestor::div[1]//a[text()='Edit']"));
		//driver.click(By.xpath("//span[contains(text(),'"+profileName+"')]/ancestor::div[1]//a[text()='Edit']"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//span[contains(text(),'"+profileName+"')]/ancestor::div[1]//a[text()='Edit']")));
		driver.waitForPageLoad();
		logger.info("Shipping profile"+profileName+" 's edit link clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public void clickOnAcceptOfQASPopup(){
		driver.waitForLoadingImageToDisappear();
		driver.quickWaitForElementPresent(By.id("QAS_RefineBtn"));
		driver.click(By.id("QAS_RefineBtn"));
		logger.info("Accept New shipping address button clicked");
		driver.waitForLoadingImageToDisappear();
	}

	public boolean isShippingAddressPresentAtOrderAndReviewPage(String profilename){
		driver.waitForElementPresent(By.xpath("//h3[contains(text(),'Shipping info')]/../..//span[contains(text(),'"+profilename+"')]"));
		return driver.isElementPresent(By.xpath("//h3[contains(text(),'Shipping info')]/../..//span[contains(text(),'"+profilename+"')]"));

	}

	public void clickOnCheckoutButtonAfterAddProduct(){
		driver.waitForElementPresent(By.xpath("//input[@value='CHECKOUT']"));
		driver.click(By.xpath("//input[@value='CHECKOUT']"));
		logger.info("Checkout button clicked");
	}

	public boolean isProfileHasUpdatedMessagePresent(){
		return driver.isElementPresent(By.xpath("//div[@id='globalMessages']//p[text()='Your profile has been updated']"));
	}

	public void mouseHoverOnMiniCart(){
		Actions actions = new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(By.xpath("//a[@id='shopping-cart']"))).build().perform();
		driver.waitForLoadingImageToDisappear();
	}

	public String getNameOfOnlyProductAddedOnMiniCart(){
		String productNameOnMiniCart = null;
		driver.waitForElementPresent(By.xpath("//div[@id='mini-shopping']//a"));
		productNameOnMiniCart = driver.findElement(By.xpath("//div[@id='mini-shopping']//a")).getText().trim();
		logger.info("Product Name On Mini Cart is "+productNameOnMiniCart);
		return productNameOnMiniCart;
	}

	public void clickOnViewShippingCartBtnOnMiniCart(){
		driver.waitForElementToBeVisible(By.xpath("//input[@value='VIEW SHOPPING CART']"), 30);
		driver.click(By.xpath("//input[@value='VIEW SHOPPING CART']"));
		logger.info("'VIEW SHOPPING CART' button clicked on mini cart");
		driver.waitForPageLoad();
		driver.waitForLoadingImageToDisappear();
	}

	public boolean isBillingAddressPresentOnPage(String firstName){
		driver.waitForElementPresent(By.xpath("//div[@id='multiple-billing-profiles']/div"));
		return driver.isElementPresent(By.xpath("//div[@id='multiple-billing-profiles']//span[contains(text(),'"+firstName+"')]"));
	}

	public void clickEditPCPerksLinkPresentOnWelcomeDropDown() throws InterruptedException{
		//driver.waitForElementPresent(WELCOME_DD_EDIT_CRP_LINK_LOC);
		driver.click(By.xpath("//a[text()='Edit PC Perks']"));
		logger.info("User has clicked on edit Crp link from welcome drop down");
		driver.waitForPageLoad();
		driver.waitForLoadingImageToDisappear();
	}

	public void cancelPulseSubscription(){
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'Cancel my Pulse subscription')]"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//a[contains(text(),'Cancel my Pulse subscription')]")));
		//driver.click(By.xpath("//a[text()='Cancel my Pulse subscription »']"));
		driver.pauseExecutionFor(2000);
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

}