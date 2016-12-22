package com.rf.pages.website.dsv;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.rf.core.driver.website.RFWebsiteDriver;

public class DSVStoreFrontHomePage extends DSVRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(DSVStoreFrontHomePage.class.getName());

	private static final By LOGIN_LINK = By.xpath("//li[@id='log-in-button']/a");
	private static final By USERNAME_TXTFIELD = By.id("username");
	private static final By PASSWORD_TXTFIELD = By.id("password");
	private static final By LOGIN_BTN = By.xpath("//input[@value='SIGN IN']");
	private static final By WELCOME_TXT = By.xpath("//a[@class='dropdown-toggle']/span[2]");
	private static final By USERNAME_DROPDOWN = By.xpath("//a[@class='dropdown-toggle']/span[2]");
	private static final By CART_IMG = By.id("bag-special");
	private static final By NXT_CRP_TXT = By.xpath("//div[@id='bag-special']/following-sibling::div[1]");
	private static final By WELCOME_DROP_DOWN = By.xpath("//div[contains(@id,'account-info')]/a/span[3]"); 
	private static final By SHIPPING_INFO_LINK_WELCOME_DROP_DOWN = By.xpath("//div[contains(@id,'account-info')]//a[text()='Shipping Info']");
	private static final By BILLING_INFO_LINK_WELCOME_DROP_DOWN = By.xpath("//div[contains(@id,'account-info')]//a[text()='Billing Info']");
	private static final By ACCOUNT_INFO_LINK_WELCOME_DROP_DOWN = By.xpath("//div[contains(@id,'account-info')]//a[text()='Account Info']");
	private static final By OUR_BUSINESS_LINK_LOC = By.xpath("//a[@id='corp-opp']");
	private static final By ENROLL_NOW_LINK_LOC = By.xpath("//ul[@id='dropdown-menu']//a[@title='Enroll Now']"); 
	private static final By SPONSOR_SEARCH_FIELD_LOC = By.id("sponserparam");
	private static final By SEARCH_BUTTON_LOC = By.id("search-sponsor-button");


	public DSVStoreFrontHomePage(RFWebsiteDriver driver) {
		super(driver);		
	}

	public void clickLoginLink(){
		driver.quickWaitForElementPresent(LOGIN_LINK);
		driver.click(LOGIN_LINK);
	}

	public boolean isLoginLinkPresent(){
		boolean isLoginPresent = false;
		driver.quickWaitForElementPresent(LOGIN_LINK);
		List<WebElement> loginBtnList = driver.findElements(LOGIN_LINK);
		if(loginBtnList.size()>0)
			isLoginPresent = true;
		return isLoginPresent;		
	}

	public boolean isWelcomeTxtPresent(){
		boolean isWelcomePresent = false;
		driver.quickWaitForElementPresent(WELCOME_TXT);
		List<WebElement> welcomeTxtList = driver.findElements(WELCOME_TXT);
		if(welcomeTxtList.size()>0)
			isWelcomePresent = true;
		return isWelcomePresent;		
	}

	public boolean isLoginOrWelcomePresent(){
		boolean isLoginOrWelcomePresent = false;

		driver.quickWaitForElementPresent(LOGIN_LINK);
		List<WebElement> loginBtnList = driver.findElements(LOGIN_LINK);
		if(loginBtnList.size()>0)
			isLoginOrWelcomePresent = true;
		else{
			List<WebElement> welcomeTxtList = driver.findElements(WELCOME_TXT);
			if(welcomeTxtList.size()>0)
				isLoginOrWelcomePresent = true;
		}		
		return isLoginOrWelcomePresent;
	}

	public void enterUsername(String username){
		logger.info("Username is "+username);
		driver.quickWaitForElementPresent(USERNAME_TXTFIELD);
		driver.type(USERNAME_TXTFIELD, username);
	}

	public void enterPassword(String password){
		driver.type(PASSWORD_TXTFIELD, password);
	}

	public void clickLoginBtn(){
		driver.click(LOGIN_BTN);
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public String getWelcomeText(){
		return driver.findElement(WELCOME_TXT).getText();
	}

	public Boolean isUserNameDropDownPresent(){
		return driver.IsElementVisible(driver.findElement(USERNAME_DROPDOWN));
	}

	public boolean isCRPCartImagePresent(){
		return driver.isElementPresent(CART_IMG);
	}

	public String getNextCRPText(){
		return driver.findElement(NXT_CRP_TXT).getText();
	}

	public void clickLogo(){
		driver.click(By.xpath("//img[@title='Rodan+Fields']"));
		System.out.println("R+F logo clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public DSVStoreFrontAutoshipCartPage clickOnCRPCartImg(){
		driver.quickWaitForElementPresent(CART_IMG);
		driver.click(CART_IMG);
		driver.pauseExecutionFor(2000);
		driver.waitForPageLoad();
		return new DSVStoreFrontAutoshipCartPage(driver);
	}

	public void clickWelcomeDropDown(){
		driver.waitForLoadingImageToDisappear();
		driver.quickWaitForElementPresent(WELCOME_DROP_DOWN);
		driver.pauseExecutionFor(2000);
		driver.click(WELCOME_DROP_DOWN);
		driver.pauseExecutionFor(2000);
	}

	public DSVStoreFrontShippingInfoPage clickShippingInfoLinkFromWelcomeDropDown(){
		driver.quickWaitForElementPresent(SHIPPING_INFO_LINK_WELCOME_DROP_DOWN);
		driver.click(SHIPPING_INFO_LINK_WELCOME_DROP_DOWN);
		return new DSVStoreFrontShippingInfoPage(driver);
	}

	public DSVStoreFrontBillingInfoPage clickBillingInfoLinkFromWelcomeDropDown(){
		driver.quickWaitForElementPresent(BILLING_INFO_LINK_WELCOME_DROP_DOWN);
		driver.click(BILLING_INFO_LINK_WELCOME_DROP_DOWN);
		return new DSVStoreFrontBillingInfoPage(driver);
	}

	public String convertComToBizOrBizToComURL(String pws){
		if(pws.contains("com"))
			pws = pws.replaceAll("com", "biz");
		else
			pws = pws.replaceAll("biz", "com");
		logger.info("after biz/com conversion,the pws is "+pws);
		return pws;
	}

	public String convertNonSecureURLToSecureURL(String URL){
		if(URL.contains("https")==false)
			URL = URL.replaceAll("http", "https");
		logger.info("after converting non secure to secure,the URL is "+URL);
		return URL;
	}

	public DSVStoreFrontAccountInfoPage clickAccountInfoLinkFromWelcomeDropDown(){
		driver.quickWaitForElementPresent(ACCOUNT_INFO_LINK_WELCOME_DROP_DOWN);
		driver.click(ACCOUNT_INFO_LINK_WELCOME_DROP_DOWN);		
		return new DSVStoreFrontAccountInfoPage(driver);
	}

	public void hoverOnOurBusinessAndClickEnrollNow() {
		Actions actions = new Actions(RFWebsiteDriver.driver);
		driver.waitForElementPresent(OUR_BUSINESS_LINK_LOC);
		WebElement ourBusiness = driver.findElement(OUR_BUSINESS_LINK_LOC);
		actions.moveToElement(ourBusiness).pause(1000).click().build().perform();
		WebElement enrollNow = driver.findElement(ENROLL_NOW_LINK_LOC);
		actions.moveToElement(enrollNow).pause(1000).build().perform();
		while(true){
			try{
				driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(ENROLL_NOW_LINK_LOC));

				break;
			}catch(Exception e){
				System.out.println("element not clicked..trying again");
				actions.moveToElement(ourBusiness).pause(1000).click().build().perform();

			}
		}
		logger.info("Enroll Now link clicked "); 
		driver.waitForPageLoad();
	}

	public void enterSponsorAndSearch(String dsvCanadianSponsorWithPwssponsor) {
		driver.quickWaitForElementPresent(SPONSOR_SEARCH_FIELD_LOC);
		driver.type(SPONSOR_SEARCH_FIELD_LOC, dsvCanadianSponsorWithPwssponsor);
		logger.info("sponsor name entered");
		driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(SEARCH_BUTTON_LOC));
		logger.info("Search Button Clicked");
		driver.waitForPageLoad();
	}

	public void mouseHoverOnSponsorAndClickSelectAndContinue() {
		JavascriptExecutor js = (JavascriptExecutor)(RFWebsiteDriver.driver);
		js.executeScript("arguments[0].click();", driver.findElement(By.xpath("//div[@id='search-results']//input[contains(@value,'Select')]")));
		logger.info("sponsor's Select & Continue has been clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public void mouseHoverOnSponsorAndClickSelectAndContinue(int resultNumber) {
		JavascriptExecutor js = (JavascriptExecutor)(RFWebsiteDriver.driver);
		if(resultNumber==1)
			js.executeScript("arguments[0].click();", driver.findElement(By.xpath("//div[@id='search-results']//input[contains(@value,'Select')]")));
		if(resultNumber==2){
			List<WebElement> allSearchResults = driver.findElements(By.xpath("//div[@id='search-results']//input[contains(@value,'Select')]"));
			js.executeScript("arguments[0].click();", allSearchResults.get(1));
		}

			logger.info("sponsor's Select & Continue has been clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();

	}

	public boolean validateCurrentURLContainsBiz() {
		return driver.getCurrentUrl().contains("myrandf.biz");
	}

	public boolean validateCurrentURLContainsCom() {
		return driver.getCurrentUrl().contains("rodanandfields.com");

	}

	public DSVStoreFrontAutoshipCartPage clickOnPCPerksCartImg(){
		driver.quickWaitForElementPresent(CART_IMG);
		driver.click(CART_IMG);
		driver.pauseExecutionFor(2000);
		driver.waitForPageLoad();
		return new DSVStoreFrontAutoshipCartPage(driver);
	}	

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
}