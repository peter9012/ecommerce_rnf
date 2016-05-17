package com.rf.pages.website.crm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.model.Site;
import org.openqa.selenium.Alert;
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
import com.rf.pages.website.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontCartAutoShipPage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontShippingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;


public class CRMRFWebsiteBasePage extends RFBasePage{
	private static final Logger logger = LogManager
			.getLogger(CRMRFWebsiteBasePage.class.getName());

	//private final By RODAN_AND_FIELDS_IMG_LOC = By.xpath("//div[@id='header-middle-top']//a");//fixed
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
	public CRMRFWebsiteBasePage(RFWebsiteDriver driver){		
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


	public String CanadaProvinceCode(String province){
		Map<String, String> CA_PROVINCE_CODE = new HashMap<String, String>();
		CA_PROVINCE_CODE.put("Alberta","AB");
		CA_PROVINCE_CODE.put("British Columbia","BC");
		CA_PROVINCE_CODE.put("Manitoba","MB");
		CA_PROVINCE_CODE.put("New Brunswick","NB");
		CA_PROVINCE_CODE.put("Northwest Territories","NT");
		CA_PROVINCE_CODE.put("Nova Scotia","NS");
		CA_PROVINCE_CODE.put("Nunavut","NU");
		CA_PROVINCE_CODE.put("Ontario","ON");
		CA_PROVINCE_CODE.put("Prince Edward Island","PE");
		CA_PROVINCE_CODE.put("Quebec","QC");
		CA_PROVINCE_CODE.put("Saskatchewan","SK");

		return CA_PROVINCE_CODE.get(province);
	}
	//	public void clickOnShopLink(){
	//		driver.waitForElementPresent(By.id("our-products"));
	//		driver.click(By.id("our-products"));
	//		logger.info("Shop link clicked ");
	//	}
	//
	//	public void clickOnAllProductsLink(){
	//		try{
	//			//driver.waitForElementPresent(By.xpath("//a[@title='All Products']"));
	//			//driver.click(By.xpath("//a[@title='All Products']"));
	//			driver.moveToELement(By.xpath("//*[@id='header']//A[@id='our-products']"));
	//			//driver.waitForElementPresent(By.xpath("//A[contains(text(),'All Products')]"));
	//			driver.moveToELement(By.xpath("//A[contains(text(),'All Products')]"));
	//			driver.click(By.xpath("//A[contains(text(),'All Products')]"));
	//		}catch(NoSuchElementException e){
	//			logger.info("All products link was not present");
	//			driver.click(By.xpath("//div[@id='dropdown-menu']//a[@href='/us/quick-shop/quickShop']"));
	//		}
	//		logger.info("All products link clicked "+"//a[@title='All Products']");	
	//		driver.waitForPageLoad();
	//	}
	//
	//	public StoreFrontUpdateCartPage clickOnQuickShopImage(){
	//		driver.waitForElementToBeVisible(By.xpath("//a[@href='/us/quick-shop/quickShop' and @title='']"), 30);
	//		driver.waitForElementPresent(By.xpath("//a[@href='/us/quick-shop/quickShop' and @title='']"));
	//		driver.click(By.xpath("//a[@href='/us/quick-shop/quickShop' and @title='']"));
	//		logger.info("Quick shop Img link clicked "+"//a[@href='/us/quick-shop/quickShop' and @title='']");
	//		driver.waitForPageLoad();
	//		return new StoreFrontUpdateCartPage(driver);
	//	}
	//
	//	public boolean areProductsDisplayed(){
	//		driver.waitForElementPresent(By.xpath("//div[contains(@class,'quickshop-section')]"));
	//		return driver.isElementPresent(By.xpath("//div[contains(@class,'quickshop-section')]"));
	//	}
	//
	//	public void selectProductAndProceedToBuy() throws InterruptedException{
	//		applyPriceFilterHighToLow();
	//		driver.waitForPageLoad();
	//		if(driver.getCountry().equalsIgnoreCase("CA")){
	//			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
	//			if(driver.findElement(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button")).isEnabled()==true)
	//				driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
	//			else
	//				driver.click(By.xpath("//div[@id='main-content']/div[5]/div[2]//form[@id='productDetailForm']/button"));
	//			logger.info("Add To Bag button clicked");
	//			driver.waitForLoadingImageToDisappear();
	//			driver.waitForPageLoad();
	//
	//		}
	//		else if(driver.getCountry().equalsIgnoreCase("US")){
	//			try{
	//				driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
	//				driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
	//			}catch(Exception e){
	//				driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[1]//form[@id='productDetailForm']/button"));
	//				driver.click(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[1]//form[@id='productDetailForm']/button"));
	//			}
	//			logger.info("Add To Bag button clicked");
	//			driver.waitForLoadingImageToDisappear();
	//			driver.waitForPageLoad();
	//		}
	//
	//	}
	//
	//	public void selectProductAndProceedToBuyWithoutFilter() throws InterruptedException{
	//		driver.waitForPageLoad();
	//		if(driver.getCountry().equalsIgnoreCase("CA")){
	//			driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
	//			if(driver.findElement(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button")).isEnabled()==true)
	//				driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
	//			else
	//				driver.click(By.xpath("//div[@id='main-content']/div[5]/div[2]//form[@id='productDetailForm']/button"));
	//			logger.info("Add To Bag button clicked");
	//			driver.waitForLoadingImageToDisappear();
	//			driver.waitForPageLoad();
	//
	//		}
	//		else if(driver.getCountry().equalsIgnoreCase("US")){
	//			try{
	//				driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
	//				driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
	//			}catch(Exception e){
	//				driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[1]//form[@id='productDetailForm']/button"));
	//				driver.click(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[1]//form[@id='productDetailForm']/button"));
	//			}
	//			logger.info("Add To Bag button clicked");
	//			driver.waitForLoadingImageToDisappear();
	//			driver.waitForPageLoad();
	//		}
	//
	//	}
	//
	//	public void selectProductAndProceedToAddToCRP() throws InterruptedException{
	//		driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Add to crp']"));
	//		if(driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Add to crp']")).isEnabled()==true)
	//			driver.click(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Add to crp']"));
	//		else
	//			driver.click(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[2]//input[@value='Add to crp']"));
	//		logger.info("Add to CRP button clicked");
	//		driver.waitForLoadingImageToDisappear();
	//	}
	//
	//	public void clickOnAddToPcPerksButton(){
	//		driver.waitForPageLoad();
	//		if(driver.getCountry().equalsIgnoreCase("us")){
	//			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div[1]//input[@value='ADD to PC Perks']"));
	//			driver.click(By.xpath("//div[@id='main-content']/div[contains(@class,'quickshop-section')]/div[2]/div[1]//input[@value='ADD to PC Perks']"));
	//		}else{
	//			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//input[@value='ADD to PC Perks']"));
	//			driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]//input[@value='ADD to PC Perks']"));
	//		}
	//
	//		try{
	//			driver.quickWaitForElementPresent(By.xpath("//input[@value='OK']"));
	//			driver.click(By.xpath("//input[@value='OK']"));
	//			driver.waitForLoadingImageToDisappear();
	//		}catch(Exception e){
	//
	//		}
	//	}
	//
	//	public void addQuantityOfProduct(String qty) throws InterruptedException{
	//		driver.pauseExecutionFor(1000);
	//		try{
	//			driver.waitForElementPresent(By.id("quantity0"));
	//			driver.findElement(By.id("quantity0")).clear();
	//			driver.findElement(By.id("quantity0")).sendKeys(qty);
	//			logger.info("quantity added is "+qty);
	//			driver.click(By.xpath("//div[@id='left-shopping']/div//a[@class='updateLink']"));
	//			logger.info("Update button clicked after adding quantity");
	//		}catch(NoSuchElementException e){
	//			driver.waitForElementPresent(By.id("quantity1"));
	//			driver.findElement(By.id("quantity1")).clear();
	//			driver.findElement(By.id("quantity1")).sendKeys(qty);
	//			logger.info("quantity added is "+qty);
	//			driver.click(By.xpath("//div[@id='left-shopping']/div//a[@class='updateLink']"));
	//			logger.info("Update button clicked after adding quantity");
	//		}
	//		driver.pauseExecutionFor(1000);
	//		driver.waitForPageLoad();
	//	}
	//
	//	public void clickOnNextBtnAfterAddingProductAndQty() throws InterruptedException{
	//		driver.waitForElementPresent(By.id("submitForm"));
	//		driver.click(By.id("submitForm"));
	//		logger.info("Next button after adding quantity clicked");
	//		driver.waitForLoadingImageToDisappear();
	//	}
	//
	//	public boolean isCartPageDisplayed(){
	//		driver.waitForElementPresent(By.xpath("//div[@id='left-shopping']/h1"));
	//		return driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='left-shopping']/h1")));
	//	}
	//
	//	//	public void addAnotherProduct() throws InterruptedException{
	//	//		driver.waitForElementPresent(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"));
	//	//		driver.click(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"));
	//	//		logger.info("Continue shopping link clicked");
	//	//		driver.waitForPageLoad();
	//	//		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
	//	//		if(driver.getCountry().equalsIgnoreCase("CA")){
	//	//			driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
	//	//			logger.info("Buy Now button clicked and another product selected");
	//	//			driver.waitForPageLoad();
	//	//		}
	//	//		else if(driver.getCountry().equalsIgnoreCase("US")){
	//	//			driver.click(By.xpath("//div[@id='main-content']/div[4]/div[2]/div[1]//form[@id='productDetailForm']/button"));
	//	//			logger.info("Buy Now button clicked and another product selected");
	//	//			driver.waitForPageLoad();
	//	//		}
	//	//
	//	//	}
	//
	//	//	public void addAnotherProduct() throws InterruptedException{
	//	//		Actions action = new Actions(RFWebsiteDriver.driver);
	//	//		driver.quickWaitForElementPresent(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"));
	//	//		action.moveToElement(driver.findElement(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"))).doubleClick(driver.findElement(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"))).build().perform();
	//	//		//driver.click(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"));
	//	//		logger.info("Continue shopping link clicked");
	//	//		driver.pauseExecutionFor(2000);
	//	//		driver.waitForPageLoad();
	//	//		//  driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
	//	//		if(driver.getCountry().equalsIgnoreCase("CA")){
	//	//			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
	//	//			driver.click(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
	//	//			logger.info("Buy Now button clicked and another product selected");
	//	//			driver.waitForPageLoad();
	//	//		}
	//	//		else if(driver.getCountry().equalsIgnoreCase("US")){
	//	//			driver.waitForElementPresent(By.xpath("//div[@class='quickshop-section blue']/div[contains(@class,'quick-product')]/div[contains(@class,'product-third-module')][2]//form[@action='/us/cart/add']/button"));
	//	//			driver.click(By.xpath("//div[@class='quickshop-section blue']/div[contains(@class,'quick-product')]/div[contains(@class,'product-third-module')][2]//form[@action='/us/cart/add']/button"));
	//	//			logger.info("Buy Now button clicked and another product selected");
	//	//			driver.waitForPageLoad();
	//	//		}
	//	//
	//	//	}
	//
	//	public void addAnotherProduct() throws InterruptedException{
	//		try{
	//			driver.waitForElementPresent(By.xpath("//div[@id='left-shopping']/p/a[contains(text(),'Continue shopping')]"));
	//			driver.click(By.xpath("//div[@id='left-shopping']/p/a[contains(text(),'Continue shopping')]"));
	//			logger.info("Continue shopping link clicked");
	//			driver.pauseExecutionFor(2000);
	//			driver.waitForPageLoad();
	//		}catch(Exception e){
	//			Actions action = new Actions(RFWebsiteDriver.driver);
	//			driver.quickWaitForElementPresent(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"));
	//			action.moveToElement(driver.findElement(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"))).doubleClick(driver.findElement(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"))).build().perform();
	//			//driver.click(By.xpath("//div[@id='left-shopping']/div/a[contains(text(),'Continue shopping')]"));
	//			logger.info("Continue shopping link clicked");
	//			driver.pauseExecutionFor(2000);
	//			driver.waitForPageLoad();
	//		}
	//		//  driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[1]//form[@id='productDetailForm']/button"));
	//		applyPriceFilterHighToLow();
	//		if(driver.getCountry().equalsIgnoreCase("CA")){
	//			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[5]/div[2]/div[2]/div[1]//form[@id='productDetailForm']/button"));
	//			driver.click(By.xpath("//div[@id='main-content']/div[5]/div[2]/div[2]/div[1]//form[@id='productDetailForm']/button"));
	//			logger.info("Buy Now button clicked and another product selected");
	//			driver.waitForPageLoad();
	//		}
	//		else if(driver.getCountry().equalsIgnoreCase("US")){
	//			driver.waitForElementPresent(By.xpath("//*[@id='main-content']/div[5]/div[2]//button"));
	//			driver.click(By.xpath("//*[@id='main-content']/div[5]/div[2]//button"));
	//			logger.info("Buy Now button clicked and another product selected");
	//			driver.waitForPageLoad();
	//		}
	//	}
	//
	//	public boolean verifyNumberOfProductsInCart(String numberOfProductsInCart){
	//		driver.waitForElementPresent(By.xpath("//div[@id='left-shopping']/h1/span"));
	//		return driver.findElement(By.xpath("//div[@id='left-shopping']/h1/span")).getText().contains(numberOfProductsInCart);
	//	}
	//
	//	public void clickOnCheckoutButton(){
	//		driver.waitForElementPresent(By.xpath("//input[@value='PLACE ORDER']"));
	//		driver.click(By.xpath("//input[@value='PLACE ORDER']"));
	//		logger.info("checkout button clicked");
	//		driver.waitForPageLoad();
	//	}
	//
	//	public boolean isLoginOrCreateAccountPageDisplayed(){
	//		driver.waitForElementPresent(By.xpath("//h1[text()='Log in or register']"));
	//		return driver.IsElementVisible(driver.findElement(By.xpath("//h1[text()='Log in or register']")));
	//	}
	//
	//	public void enterNewRCDetails(String firstName,String lastName,String password) throws InterruptedException{
	//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
	//		String emailAddress = firstName+randomNum+"@xyz.com";
	//		driver.findElement(By.id("first-Name")).sendKeys(firstName);
	//		logger.info("first name entered as "+firstName);
	//		driver.findElement(By.id("last-name")).sendKeys(lastName);
	//		logger.info("last name entered as "+lastName);
	//		driver.findElement(By.id("email-account")).clear();
	//		driver.findElement(By.id("email-account")).sendKeys(emailAddress+"\t");
	//		logger.info("email entered as "+emailAddress);
	//		driver.pauseExecutionFor(1000);
	//		driver.waitForSpinImageToDisappear();
	//		driver.findElement(By.id("password")).sendKeys(password);
	//		logger.info("password entered as "+password);
	//		driver.findElement(By.id("the-password-again")).sendKeys(password);
	//		logger.info("confirm password entered as "+password);
	//		driver.click(By.id("next-button"));		
	//		logger.info("Create New Account button clicked");
	//		driver.waitForLoadingImageToDisappear();
	//		driver.waitForPageLoad();
	//	}
	//
	//	//overloaded method for email address
	//	public void enterNewRCDetails(String firstName,String lastName,String emailAddress,String password) throws InterruptedException{
	//		driver.findElement(By.id("first-Name")).sendKeys(firstName);
	//		logger.info("first name entered as "+firstName);
	//		driver.findElement(By.id("last-name")).sendKeys(lastName);
	//		logger.info("last name entered as "+lastName);
	//		driver.findElement(By.id("email-account")).clear();
	//		driver.findElement(By.id("email-account")).sendKeys(emailAddress+"\t");
	//		logger.info("email entered as "+emailAddress);
	//		driver.pauseExecutionFor(1000);
	//		driver.waitForSpinImageToDisappear();
	//		driver.findElement(By.id("password")).sendKeys(password);
	//		logger.info("password entered as "+password);
	//		driver.findElement(By.id("the-password-again")).sendKeys(password);
	//		logger.info("confirm password entered as "+password);
	//		driver.click(By.id("next-button"));  
	//		logger.info("Create New Account button clicked");
	//		driver.waitForLoadingImageToDisappear();
	//		driver.waitForPageLoad();
	//	}
	//
	//	public void enterNewPCDetails(String firstName,String lastName,String password) throws InterruptedException{
	//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
	//		String emailAddress = firstName+randomNum+"@xyz.com";
	//		driver.findElement(By.id("first-Name")).sendKeys(firstName);
	//		logger.info("first name entered as "+firstName);
	//		driver.findElement(By.id("last-name")).sendKeys(lastName);
	//		logger.info("last name entered as "+lastName);
	//		driver.findElement(By.id("email-account")).clear();
	//		driver.findElement(By.id("email-account")).sendKeys(emailAddress+"\t");
	//		logger.info("email entered as "+emailAddress);
	//		driver.pauseExecutionFor(1000);
	//		driver.waitForSpinImageToDisappear();
	//		driver.findElement(By.id("password")).sendKeys(password);
	//		logger.info("password entered as "+password);
	//		driver.findElement(By.id("the-password-again")).sendKeys(password);
	//		logger.info("confirm password entered as "+password);
	//		driver.click(By.xpath("//input[@id='become-pc']/.."));
	//		logger.info("check box for PC user checked");
	//		driver.click(By.xpath("//input[@id='next-button']"));		
	//		logger.info("Create New Account button clicked");		
	//	}
	//
	//	public void enterNewPCDetails(String firstName,String lastName,String password, String emailID) throws InterruptedException{
	//		driver.findElement(By.id("first-Name")).sendKeys(firstName);
	//		logger.info("first name entered as "+firstName);
	//		driver.findElement(By.id("last-name")).sendKeys(lastName);
	//		logger.info("last name entered as "+lastName);
	//		driver.findElement(By.id("email-account")).clear();
	//		driver.findElement(By.id("email-account")).sendKeys(emailID+"\t");
	//		logger.info("email entered as "+emailID);
	//		driver.pauseExecutionFor(1000);
	//		driver.waitForSpinImageToDisappear();
	//		driver.findElement(By.id("password")).sendKeys(password);
	//		logger.info("password entered as "+password);
	//		driver.findElement(By.id("the-password-again")).sendKeys(password);
	//		logger.info("confirm password entered as "+password);
	//		driver.click(By.xpath("//input[@id='become-pc']/.."));
	//		logger.info("check box for PC user checked");
	//		driver.click(By.xpath("//input[@id='next-button']"));  
	//		logger.info("Create New Account button clicked"); 
	//		driver.waitForLoadingImageToDisappear();
	//	}
	//
	//	public boolean isPopUpForPCThresholdPresent() throws InterruptedException{
	//		boolean isPopUpForPCThresholdPresent=false;
	//		driver.waitForElementPresent(By.xpath("//div[@id='popup-content']//p[contains(text(),'Please add products')]"));
	//		isPopUpForPCThresholdPresent = driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='popup-content']//p[contains(text(),'Please add products')]")));
	//		if(isPopUpForPCThresholdPresent==true){
	//			driver.click(By.xpath("//div[@id='popup-content']//input"));
	//			return true;
	//		}
	//		return false;
	//	}
	//
	//	public boolean isCheckoutPageDisplayed(){
	//		driver.waitForElementPresent(By.xpath("//h1[contains(text(),'Checkout')]"));
	//		return driver.IsElementVisible(driver.findElement(By.xpath("//h1[contains(text(),'Checkout')]")));
	//	}
	//
	//	public void enterMainAccountInfo(){
	//		driver.pauseExecutionFor(5000);
	//		if(driver.getCountry().equalsIgnoreCase("CA")){
	//			driver.findElement(By.id("address.line1")).sendKeys(TestConstants.ADDRESS_LINE_1_CA);
	//			logger.info("Address Line 1 entered is "+TestConstants.ADDRESS_LINE_1_CA);
	//			driver.findElement(By.id("address.townCity")).sendKeys(TestConstants.CITY_CA+"\t");
	//			logger.info("City entered is "+TestConstants.CITY_CA);
	//			driver.waitForLoadingImageToDisappear();
	//			try{
	//				driver.click(By.xpath("//form[@id='addressForm']/div[@class='row'][1]//select[@id='state']"));
	//				driver.waitForElementPresent(By.xpath("//form[@id='addressForm']/div[@class='row'][1]//select[@id='state']/option[2]"));
	//				driver.click(By.xpath("//form[@id='addressForm']/div[@class='row'][1]//select[@id='state']/option[2]"));
	//			}catch(Exception e){
	//				driver.click(By.id("state"));
	//				driver.waitForElementPresent(By.xpath("//select[@id='state']/option[2]"));
	//				driver.click(By.xpath("//select[@id='state']/option[2]"));	
	//			}	
	//			logger.info("state selected");
	//			driver.findElement(By.id("address.postcode")).sendKeys(TestConstants.POSTAL_CODE_CA);
	//			logger.info("postal code entered is "+TestConstants.POSTAL_CODE_CA);
	//			driver.findElement(By.id("address.phonenumber")).sendKeys(TestConstants.PHONE_NUMBER);
	//			logger.info("phone number entered is "+TestConstants.PHONE_NUMBER);
	//		}
	//		else if(driver.getCountry().equalsIgnoreCase("US")){
	//			driver.findElement(By.id("address.line1")).sendKeys(TestConstants.ADDRESS_LINE_1_US);
	//			logger.info("Address line 1 entered is "+TestConstants.ADDRESS_LINE_1_US);
	//			driver.findElement(By.id("address.townCity")).sendKeys(TestConstants.CITY_US);
	//			logger.info("City entered is "+TestConstants.CITY_US);
	//			driver.click(By.id("state"));
	//			driver.waitForElementPresent(By.xpath("//select[@id='state']/option[2]"));
	//			driver.click(By.xpath("//select[@id='state']/option[2]"));
	//			logger.info("state selected");
	//			driver.findElement(By.id("address.postcode")).sendKeys(TestConstants.POSTAL_CODE_US);
	//			logger.info("postal code entered is "+TestConstants.POSTAL_CODE_US);
	//			driver.findElement(By.id("address.phonenumber")).sendKeys(TestConstants.PHONE_NUMBER_US);
	//			logger.info("phone number entered is "+TestConstants.PHONE_NUMBER_US);
	//		}
	//
	//	}
	//
	//	public void enterMainAccountInfo(String address1,String city,String province,String postalCode,String phoneNumber){
	//		driver.waitForElementPresent(By.id("address.line1"));
	//		driver.findElement(By.id("address.line1")).sendKeys(address1);
	//		logger.info("Address Line 1 entered is "+address1);
	//		driver.findElement(By.id("address.townCity")).sendKeys(city);
	//		logger.info("City entered is "+city);
	//		driver.waitForElementPresent(By.id("state"));
	//		driver.click(By.id("state"));
	//		driver.waitForElementPresent(By.xpath("//select[@id='state']/option[contains(text(),'"+province+"')]"));
	//		driver.click(By.xpath("//select[@id='state']/option[contains(text(),'"+province+"')]"));
	//		logger.info("state selected");
	//		driver.findElement(By.id("address.postcode")).sendKeys(postalCode);
	//		logger.info("postal code entered is "+postalCode);
	//		driver.findElement(By.id("address.phonenumber")).sendKeys(phoneNumber);
	//		logger.info("phone number entered is "+phoneNumber);
	//	}
	//
	//	public void clickOnContinueWithoutSponsorLink() throws InterruptedException{
	//		driver.waitForElementPresent(By.id("continue-no-sponsor"));
	//		driver.click(By.id("continue-no-sponsor"));	
	//		logger.info("continue without sponsor link clicked");
	//		driver.waitForLoadingImageToDisappear();
	//	}
	//
	//	public void clickOnNextButtonAfterSelectingSponsor() throws InterruptedException{
	//		driver.waitForElementPresent(By.id("saveAccountAddress"));
	//		driver.click(By.id("saveAccountAddress"));
	//		logger.info("Next button after selecting sponsor is clicked");
	//		driver.waitForLoadingImageToDisappear();
	//		try{
	//			driver.quickWaitForElementPresent(By.id("QAS_AcceptOriginal"));
	//			driver.click(By.id("QAS_AcceptOriginal"));
	//			logger.info("Accept as original button clicked");
	//			driver.waitForLoadingImageToDisappear();
	//		}catch(NoSuchElementException e){
	//
	//		}		
	//	}
	//
	//	public void clickOnShippingAddressNextStepBtn() throws InterruptedException{
	//		Actions action = new Actions(RFWebsiteDriver.driver);
	//		driver.waitForElementPresent(By.id("saveShippingInfo"));
	//		action.moveToElement(driver.findElement(By.id("saveShippingInfo"))).click(driver.findElement(By.id("saveShippingInfo"))).build().perform();
	//		logger.info("Next button on shipping address clicked");		
	//		driver.waitForLoadingImageToDisappear();
	//		driver.pauseExecutionFor(2000);
	//	}
	//
	//	public void enterNewBillingCardNumber(String cardNumber){
	//		driver.waitForElementPresent(By.id("card-nr"));
	//		driver.type(By.id("card-nr"), cardNumber);
	//		logger.info("Billing card number entered is "+cardNumber);
	//	}
	//
	//	public void enterNewBillingNameOnCard(String nameOnCard){
	//		driver.waitForElementPresent(By.id("card-name"));
	//		driver.findElement(By.id("card-name")).clear();
	//		driver.findElement(By.id("card-name")).sendKeys(nameOnCard);
	//		logger.info("card name entered is "+nameOnCard);
	//	}
	//
	//	public void selectNewBillingCardExpirationDate(){
	//		driver.click(By.id("expiryMonth"));
	//		driver.waitForElementPresent(By.xpath("//select[@id='expiryMonth']/option[10]"));
	//		driver.click(By.xpath("//select[@id='expiryMonth']/option[10]"));
	//		driver.click(By.id("expiryYear"));
	//		driver.waitForElementPresent(By.xpath("//select[@id='expiryYear']/option[10]"));
	//		driver.click(By.xpath("//select[@id='expiryYear']/option[10]"));
	//		logger.info("expiration date is selected");
	//	}
	//
	//	public void enterNewBillingSecurityCode(String securityCode){
	//		driver.type(By.id("security-code"), securityCode);
	//		logger.info("security code entered is "+securityCode);
	//	}
	//
	//	public void selectNewBillingCardAddress() throws InterruptedException{
	//		driver.waitForElementPresent(By.id("addressBookdropdown"));
	//		driver.click(By.id("addressBookdropdown"));
	//		driver.click(By.xpath("//*[@id='addressBookdropdown']/option[1]"));
	//		logger.info("New Billing card address selected");
	//	}
	//
	//	public void clickOnSaveBillingProfile() throws InterruptedException{
	//		driver.waitForElementPresent(By.id("submitButton"));
	//		driver.click(By.id("submitButton"));
	//		driver.waitForLoadingImageToDisappear();
	//		logger.info("Save billing profile button clicked");
	//	}
	//
	//
	//
	//	public void clickOnBillingNextStepBtn() throws InterruptedException{
	//		driver.waitForElementPresent(By.xpath("//div[@id='payment-next-button']/input"));
	//		driver.click(By.xpath("//div[@id='payment-next-button']/input"));
	//		logger.info("Next button on billing profile clicked");	
	//		driver.waitForLoadingImageToDisappear();
	//		driver.pauseExecutionFor(2000);
	//	}
	//
	//	public void clickOnSetupCRPAccountBtn() throws InterruptedException{
	//		driver.waitForElementPresent(By.xpath("//input[@value='Setup CRP Account']"));
	//		driver.click(By.xpath("//ul[@style='cursor: pointer;']/li[1]/div"));
	//		driver.click(By.xpath("//form[@id='placeOrderForm1']//a[contains(text(),'I agree that this agreement shall be accepted electronically.')]/ancestor::strong[1]/preceding::div[1]"));
	//		driver.click(By.xpath("//input[@value='Setup CRP Account']"));
	//		logger.info("Next button on billing profile clicked");
	//	}
	//
	//	public void clickPlaceOrderBtn()throws InterruptedException{
	//		driver.waitForElementPresent(By.id("placeOrderButton"));
	//		driver.click(By.id("placeOrderButton"));
	//		logger.info("Place order button clicked");
	//		driver.waitForLoadingImageToDisappear();
	//		try{
	//			switchToPreviousTab();
	//		}catch(Exception e){
	//
	//		}
	//		driver.waitForPageLoad();		
	//	}
	//
	public void switchToPreviousTab(){
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
		driver.close();
		driver.switchTo().window(tabs.get(0));
		driver.pauseExecutionFor(1000);
	}


	public void switchToChildWindow(){
		driver.pauseExecutionFor(10000);
		driver.switchToSecondWindow();
		driver.waitForPageLoad();
	}
	public boolean validateErrorMsgIsDisplayed(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.waitForElementPresent(By.xpath("//div[@class='errorMsg']/strong"));
		return driver.findElement(By.xpath("//div[@class='errorMsg']/strong")).getText().contains("Error");
	}

	public void clickDeleteForTheDefaultShippingProfileSelected(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'Shipping Profiles Help')]/ancestor::div[@class='listRelatedObject customnotabBlock']//img[@title='Checked']/../preceding-sibling::td[2]/a[2]"));
		driver.click(By.xpath("//span[contains(text(),'Shipping Profiles Help')]/ancestor::div[@class='listRelatedObject customnotabBlock']//img[@title='Checked']/../preceding-sibling::td[2]/a[2]"));
	}

	public void clickOKOnDeleteDefaultShippingProfilePopUp(){
		/* driver.switchTo().defaultContent();
		  driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));*/
		// driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.pauseExecutionFor(3000);
		Alert alt=driver.switchTo().alert();
		alt.accept();
		driver.waitForPageLoad();
	}

	public boolean validateDefaultShippingAddressCanNotBeDeleted(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.waitForElementPresent(By.xpath("//table//td[contains(text(),'You cannot delete the default address')]"));
		boolean state= driver.isElementPresent(By.xpath("//table//td[contains(text(),'You cannot delete the default address')]"));
		driver.switchTo().defaultContent();
		return state;
	}

	public void refreshPage(){
		driver.pauseExecutionFor(1500);
		driver.navigate().refresh();
	}


	public void addANewShippingProfileAndMakeItDefault(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'Shipping Profiles Help')]/ancestor::div[@class='listRelatedObject customnotabBlock']//input"));
		driver.click(By.xpath("//span[contains(text(),'Shipping Profiles Help')]/ancestor::div[@class='listRelatedObject customnotabBlock']//input"));
		driver.waitForCRMLoadingImageToDisappear();
		driver.waitForPageLoad();
		if(driver.getCountry().trim().equalsIgnoreCase("ca")){
			driver.switchTo().defaultContent();
			driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[2]"));
			driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[2]")));
			driver.findElement(By.xpath("//table[@class='detailList']//label[contains(text(),'Address Line 1')]/parent::td/following-sibling::td/input")).sendKeys(TestConstants.CRM_NEW_ADDRESS_LINE_1_CA);
			driver.findElement(By.xpath("//table[@class='detailList']//label[contains(text(),'Locale')]/parent::td/following-sibling::td/input")).sendKeys(TestConstants.CRM_NEW_LOCALE_CA);
			driver.findElement(By.xpath("//table[@class='detailList']//label[contains(text(),'Region')]/parent::td/following-sibling::td/input")).sendKeys(TestConstants.CRM_NEW_REGION_CA);
			driver.findElement(By.xpath("//table[@class='detailList']//label[contains(text(),'Postal Code')]/parent::td/following-sibling::td/input")).sendKeys(TestConstants.CRM_NEW_POSTALCODE_CA);
			driver.findElement(By.xpath("//table[@class='detailList']//label[contains(text(),'Address Phone')]/parent::td/following-sibling::td/input")).sendKeys(TestConstants.CRM_NEW_PHONENUM_CA);
			driver.findElement(By.xpath("//table[@class='detailList']//label[contains(text(),'Profile Name')]/parent::td/following-sibling::td/input")).sendKeys(TestConstants.CRM_NEW_PROFILENAME_CA);
			//check default check box
			driver.findElement(By.xpath("//table[@class='detailList']//label[contains(text(),'IsDefault')]/parent::td/following-sibling::td/input")).click();
			//click save
			driver.click(By.xpath("//a[contains(text(),'Save Address')]"));
			driver.pauseExecutionFor(4000);
			driver.click(By.xpath("//input[@title='Choose Address']"));
			driver.click(By.xpath("//a[contains(text(),'Save Address')]"));
		}else{

		}
	}

	public String getProfileNameOfTheDefaultShippingProfile(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'Shipping Profiles Help')]/ancestor::div[@class='listRelatedObject customnotabBlock']//img[@title='Checked']/../preceding-sibling::td[1]"));
		return driver.findElement(By.xpath("//span[contains(text(),'Shipping Profiles Help')]/ancestor::div[@class='listRelatedObject customnotabBlock']//img[@title='Checked']/../preceding-sibling::td[1]")).getText();
	}

	public void clickDeleteForNonDefaultShippingProflle(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'Shipping Profiles Help')]/ancestor::div[@class='listRelatedObject customnotabBlock']//img[@title='Checked']/ancestor::tr[1]/preceding-sibling::tr[1]//a[2]"));
		driver.click(By.xpath("//span[contains(text(),'Shipping Profiles Help')]/ancestor::div[@class='listRelatedObject customnotabBlock']//img[@title='Checked']/ancestor::tr[1]/preceding-sibling::tr[1]//a[2]"));
	}

	public boolean validateNonDefaultShippingProfileDeleted(){
		//refreshPage();
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'Shipping Profiles')]/span[contains(text(),'[1]')]"));
		boolean state= driver.isElementPresent(By.xpath("//span[contains(text(),'Shipping Profiles')]/span[contains(text(),'[1]')]"));
		driver.switchTo().defaultContent();
		return state;
	}

	public void closeAllOpenedTabs(){
		int totalOpenedTabs = driver.findElements(By.xpath("//li[contains(@id,'navigatortab__scc-pt')]")).size();
		logger.info("total opened tabs = "+totalOpenedTabs);
		Actions actions = new Actions(RFWebsiteDriver.driver);
		for(int i=totalOpenedTabs;i>=1;i--){
			//driver.waitForElementPresent(By.xpath("//li[contains(@id,'navigatortab__scc-pt')]["+i+"]/descendant::a[@class='x-tab-strip-close']"));
			actions.moveToElement(driver.findElement(By.xpath("//li[contains(@id,'navigatortab__scc-pt')]["+i+"]/descendant::a[@class='x-tab-strip-close']"))).click().build().perform();
			//driver.click(By.xpath("//li[contains(@id,'navigatortab__scc-pt')]["+i+"]/descendant::a[@class='x-tab-strip-close']"));
			driver.pauseExecutionFor(1000);
		}
	}

	public void closeTabViaNumberWise(int number){
		driver.switchTo().defaultContent();
		Actions actions = new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(By.xpath("//li[contains(@id,'navigatortab__scc-pt')]["+number+"]/descendant::a[@class='x-tab-strip-close']"))).click().build().perform();
		driver.pauseExecutionFor(1000);
	}
}