package com.rf.pages.website.storeFrontBrandRefresh;
import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstantsRFL;
import com.rf.pages.RFBasePage;

public class StoreFrontBrandRefreshWebsiteBasePage extends RFBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontBrandRefreshWebsiteBasePage.class.getName());

	private static String regimenProductLoc = "//div[@id='ProductCategories']//span[text()='%s']/preceding::p[1]//img";
	private static String businessSystemSubLink= "//div[@id='RFContent']//a[contains(@href,'%s')]";
	private static String regimenImageOnPwsLoc = "//div[@id='ProductCategories']//p[@class='productInfo']//span[text()='%s']/../preceding-sibling::p/a";
	private static String myAccountLinkAfterLoginLink = "//nav[@id='Col1']//span[text()='%s']/..";
	private static String orderManagementSublink = "//a[@class='IconLink']//span[contains(text(),'%s')]";
	private static String linkUnderShopSkinCareOrBeAConsultant = "//div[@id='LeftNav']//a/span[text()='%s']";
	private static String consultantOnlyProduct= "//p[contains(text(),'%s')]/preceding::a[1]/img";
	private static String sublinkUnderShopSkinCareOrBeAConsultant = "//div[@id='LeftNav']//a/span[text()='%s']/../..//span[text()='%s']";
	private static String consultantOnlyProductonPWSLoc= "//span[contains(text(),'%s')]/preceding::a[1]/img";

	protected static final By BE_A_CONSULTANT_LOC = By.xpath("//span[text()='Become a Consultant']");
	private static final By ORDER_NUMBER_AFTER_PLACED = By.xpath("//span[contains(@id,'uxOrderNumber')]//cufon");
	private static final By EDIT_ORDER_UNDER_MY_ACCOUNT_LOC = By.xpath("//span[text()=' Edit Order']");
	private static final By CHANGE_SHIPPING_INFO_LINK_ON_PWS = By.xpath("//a[contains(@id,'uxChangeShippingLink')]");
	private static final By EDIT_ORDER_BTN_LOC = By.xpath("//p[@class='FormButtons']//a[text()='Edit Order']");
	private static final By ORDER_NUMBER_AT_ORDER_HISTORY = By.xpath("//*[contains(@class,'CartTable')]/div[2]/div[1]");
	private static final By ADD_TO_CART_BTN_FOR_EDIT_ORDER = By.xpath("//div[@class='FloatCol']/div[1]//a[text()='Add to Bag']");
	private static final By EDIT_ORDER_UPDATE_MESSAGE = By.xpath("//p[@class='success']");
	private static final By LOGOUT_BTN_LOC = By.xpath("//a[text()='Log Out']");
	private static final By SHOP_SKINCARE_HEADER_LOC = By.xpath("//span[text()='Shop Skincare']");
	private static final By ADD_TO_CART_BTN_LOC = By.xpath("//a[@id='addToCartButton']");
	private static final By MY_SHOPPING_BAG_LINK = By.xpath("//a[@class='BagLink']");
	private static final By CHECKOUT_BTN_OF_MY_SHOPPING_BAG_LINK = By.xpath("//span[text()='Checkout Now']");
	private static final By OK_BTN_OF_CONFIRMATION_POPUP_FOR_ADHOC_ORDER = By.xpath("//button[text()='OK']");
	private static final By CHECKOUT_BTN = By.xpath("//span[text()='Checkout']");
	private static final By CONTINUE_BTN_PREFERRED_AUTOSHIP_CART_PAGE_LOC = By.xpath("//a[contains(@id,'uxContinue')]");
	private static final By COMPLETE_ORDER_BTN = By.xpath("//input[contains(@id,'uxSubmitOrder')]");
	private static final By ORDER_CONFIRMATION_THANK_YOU_TXT = By.xpath("//h2[contains(text(),'Thank')]");
	private static final By CONTINUE_BTN_BILLING_PAGE = By.xpath("//span[contains(text(),'Change Billing Information')]/following::a[contains(@id,'uxContinue')]");
	private static final By CONTINUE_WITHOUT_CONSULTANT_LINK = By.xpath("//a[contains(@id,'uxSkipStep')]");
	private static final By ADD_TO_CART_BTN_AS_PER_REGIMEN = By.xpath("//a[@id='addToCartButton']");
	private static final By HELLO_OR_WELCOME_TXT_ON_CORP = By.xpath("//*[contains(text(),'Hello') or contains(text(),'Welcome')]");
	private static final By ORDER_PLACED_CONFIRMATION_TEXT = By.xpath("//div[@id='RFContent']//b");
	private static final By CONSULTANTS_ONLY_PRODUCTS_REGIMEN = By.xpath("//cufontext[contains(text(),'Consultant-Only ')]/following::a[1]/img");
	private static final By FORGOT_PASSWORD_LINK = By.xpath("//a[@id='uxForgotPasswordLink']");
	private static final By MY_ACCOUNT_LINK = By.xpath("//a[text()='My Account']");
	private static final By SHOP_SKINCARE_LOC = By.xpath("//span[text()='Shop Skincare']");
	private static final By SELECTED_HIGHLIGHT_LINK = By.xpath("//div[@id='ContentWrapper']//a[@class='selected']/span");
	private static final By CHANGE_BILLING_INFO = By.xpath("//a[contains(@id,'uxBillingInfo_uxChange')]");
	private static final By USE_THIS_BILLING_INFORMATION = By.xpath("//a[contains(@id,'uxUseNewPayment')]");
	private static final By SHOP_SKINCARE_ON_PWS_LOC = By.xpath("//span[text()='SHOP SKINCARE']");
	private static final By PRODUCT_LINK_UNDER_SHOP_SKIN_CARE = By.xpath("//span[text()='CONSULTANT-ONLY PRODUCTS']");
	private static final By COM_PWS_CONSULTANT_ENROLLMENT = By.xpath("//div[@class='websitePrefix']/ul[@class='domainResults']/li[1]");
	private static final By BIZ_PWS_CONSULTANT_ENROLLMENT = By.xpath("//div[@class='websitePrefix']/ul[@class='domainResults']/li[2]");
	private static final By EMAIL_ADDRESS_CONSULTANT_ENROLLMENT = By.xpath("//div[@class='websitePrefix']/ul[@class='domainResults']/li[3]");
	private static final By ABOUT_RF_LOC = By.xpath("//span[text()='About R+F']");
	private static final By RODAN_AND_FIELDS_IMG_LOC = By.xpath("//div[@id='logo']//img");
	protected static final By CONTINUE_BTN_PREFERRED_PROFILE_PAGE_LOC = By.xpath("//*[contains(@id,'uxContinue')]");

	protected RFWebsiteDriver driver;
	private String RFL_DB = null;
	private Actions actions;
	public StoreFrontBrandRefreshWebsiteBasePage(RFWebsiteDriver driver){		
		super(driver);
		this.driver = driver;
	}

	public boolean verifyUserSuccessfullyLoggedInOnPWSSite() {
		driver.quickWaitForElementPresent(LOGOUT_BTN_LOC);
		return driver.isElementPresent(LOGOUT_BTN_LOC);
	}

	public void clickShopSkinCareHeader() {
		driver.waitForElementPresent(SHOP_SKINCARE_HEADER_LOC);
		driver.click(SHOP_SKINCARE_HEADER_LOC);
		logger.info("shop skincare link on Header clicked");
	}
	public void selectRegimenAfterLogin(String regimen){
		regimen = regimen.toUpperCase();
		driver.quickWaitForElementPresent(By.xpath(String.format(regimenProductLoc, regimen)));
		driver.click(By.xpath(String.format(regimenProductLoc, regimen)));
		logger.info("Regimen selected is: "+regimen);
	}

	public void clickAddToCartButtonAfterLogin() {
		try{
			driver.waitForElementPresent((By.xpath("//span[text()='Add to Bag']")));
			driver.click(By.xpath("//span[text()='Add to Bag']"));
			System.out.println("Add to Bag button on ProdDetailPage is clicked");
		}
		catch(NoSuchElementException e){
			try{
				driver.findElement(By.xpath("//a[text()='Add to Cart']"));
				driver.click(By.xpath("//a[text()='Add to Cart']"));
				System.out.println("Add to cart button on ProdDetailPage is clicked");

			} catch (NoSuchElementException e1) {
				driver.findElement(ADD_TO_CART_BTN_LOC);
				driver.quickWaitForElementPresent(ADD_TO_CART_BTN_LOC);
				driver.click(ADD_TO_CART_BTN_LOC);
				logger.info("Add to cart button is clicked");

			}
		}
	}	

	public void mouseHoverOnMyShoppingBagLinkAndClickOnCheckoutBtn(){
		actions =  new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(MY_SHOPPING_BAG_LINK)).click(driver.findElement(CHECKOUT_BTN_OF_MY_SHOPPING_BAG_LINK)).build().perform();
		logger.info("Mouse hover on My shopping bag link and clicked on checkout button");
	}


	public void clickOKBtnOnPopup(){
		try{
			driver.quickWaitForElementPresent(OK_BTN_OF_CONFIRMATION_POPUP_FOR_ADHOC_ORDER);
			driver.click(OK_BTN_OF_CONFIRMATION_POPUP_FOR_ADHOC_ORDER);
			logger.info("Ok button clicked on popup");
		}catch(Exception e){

		}
	}

	public boolean isThankYouTextPresentAfterOrderPlaced(){
		driver.waitForElementPresent(ORDER_CONFIRMATION_THANK_YOU_TXT);
		return driver.isElementPresent(ORDER_CONFIRMATION_THANK_YOU_TXT);
	}

	public void clickCheckoutBtn(){
		driver.waitForElementPresent(CHECKOUT_BTN);
		driver.click(CHECKOUT_BTN);
		logger.info("Checkout button clicked");
		driver.waitForPageLoad();
	}

	public void clickContinueBtn(){
		driver.quickWaitForElementPresent(CONTINUE_BTN_PREFERRED_AUTOSHIP_CART_PAGE_LOC);
		driver.click(CONTINUE_BTN_PREFERRED_AUTOSHIP_CART_PAGE_LOC);
		logger.info("Continue button clicked on Autoship cart page");
		driver.waitForPageLoad();
		driver.pauseExecutionFor(2000);
	}

	public void clickCompleteOrderBtn(){
		driver.quickWaitForElementPresent(COMPLETE_ORDER_BTN);
		driver.click(COMPLETE_ORDER_BTN);
		logger.info("Complete order button clicked");
		driver.waitForPageLoad();
	}

	public void clickContinueBtnOnBillingPage() {
		driver.quickWaitForElementPresent(CONTINUE_BTN_BILLING_PAGE);
		driver.click(CONTINUE_BTN_BILLING_PAGE);
		logger.info("Continue button clicked on billing page");
		driver.waitForPageLoad();  
	}

	public void openPWSSite(String pws){
		boolean isAlreadylogin = false;
		driver.get(pws);
		try{
			logout();
			isAlreadylogin = true;
		}catch(NoSuchElementException e){

		} 
		if(isAlreadylogin == true){
			driver.get(pws);
		}
		logger.info("Open Pws Site Is: "+pws);
		driver.waitForPageLoad();
	}

	public void logout(){
		driver.quickWaitForElementPresent(By.xpath("//a[text()='Log-Out' or text()='Log Out']"));
		if(driver.findElements(By.xpath("//a[text()='Log-Out' or text()='Log Out']")).size()>0){
			driver.click(By.xpath("//a[text()='Log-Out' or text()='Log Out']"));
			logger.info("Log Out Link clicked"); 
			driver.waitForPageLoad();
		}
		else{
			logger.info("User already logged out");
		}
	}

	public void clickContinueWithoutConsultantLink(){
		driver.quickWaitForElementPresent(CONTINUE_WITHOUT_CONSULTANT_LINK);
		driver.click(CONTINUE_WITHOUT_CONSULTANT_LINK);
		logger.info("Continue without sponser link clicked");
	}

	public String getCurrentURL(){
		driver.waitForPageLoad();
		return driver.getCurrentUrl();
	}

	public void clickAddToCartButtonForEssentialsAndEnhancementsAfterLogin() {
		driver.quickWaitForElementPresent(ADD_TO_CART_BTN_AS_PER_REGIMEN);
		driver.clickByJS(RFWebsiteDriver.driver,driver.findElement(ADD_TO_CART_BTN_AS_PER_REGIMEN));
		logger.info("Add to cart button is clicked");
		driver.waitForLoadingImageToDisappear();
		driver.pauseExecutionFor(3000);
	}

	public boolean verifyUserSuccessfullyLoggedIn() {
		driver.waitForPageLoad();
		driver.waitForElementPresent(HELLO_OR_WELCOME_TXT_ON_CORP);
		return driver.isElementPresent(HELLO_OR_WELCOME_TXT_ON_CORP);
	}

	public void navigateToBackPage(){
		driver.waitForPageLoad();
		driver.navigate().back();
	}

	public String getOrderConfirmationTextMsgAfterOrderPlaced(){
		driver.waitForElementPresent(ORDER_PLACED_CONFIRMATION_TEXT);
		String confirmationMsg = driver.findElement(ORDER_PLACED_CONFIRMATION_TEXT).getText().trim();
		logger.info("Order confirmation msg after order placed is: "+confirmationMsg);
		return confirmationMsg;
	}

	public void selectConsultantOnlyProductsRegimen(){
		driver.quickWaitForElementPresent(CONSULTANTS_ONLY_PRODUCTS_REGIMEN);
		driver.click(CONSULTANTS_ONLY_PRODUCTS_REGIMEN);
		logger.info("Consultants only products Regimen selected");
	}

	public boolean isForgotPasswordLinkPresent() {
		driver.waitForElementPresent(FORGOT_PASSWORD_LINK);
		return driver.isElementPresent(FORGOT_PASSWORD_LINK);
	}

	public void clickShopSkinCareBtn(){
		Actions actions = new Actions(RFWebsiteDriver.driver);
		driver.quickWaitForElementPresent(SHOP_SKINCARE_LOC);
		WebElement shopSkinCare = driver.findElement(SHOP_SKINCARE_LOC);
		actions.moveToElement(shopSkinCare).pause(1000).click().build().perform();
		logger.info("All products link clicked "); 
		/*		driver.waitForPageLoad();
		driver.waitForLoadingImageToDisappear();
		driver.waitForElementPresent(By.id("our-products")); 
		driver.pauseExecutionFor(2000);
		driver.quickWaitForElementPresent(SHOP_SKINCARE_LOC);
		driver.click(SHOP_SKINCARE_LOC);
		logger.info("Products button clicked");
		driver.waitForPageLoad();*/
	}

	public boolean isSublinkOfBusinessSystemPresent(String linkNameOfBusinessSystem){
		driver.quickWaitForElementPresent(By.xpath(String.format(businessSystemSubLink, linkNameOfBusinessSystem)));
		return driver.isElementPresent(By.xpath(String.format(businessSystemSubLink, linkNameOfBusinessSystem)));
	}

	public void clickSublinkOfBusinessSystem(String linkNameOfBusinessSystem){
		driver.quickWaitForElementPresent(By.xpath(String.format(businessSystemSubLink, linkNameOfBusinessSystem)));
		driver.click(By.xpath(String.format(businessSystemSubLink, linkNameOfBusinessSystem)));
		logger.info("Sublink of business system i.e. :"+linkNameOfBusinessSystem+" clicked");
		driver.waitForPageLoad();
	}

	public String getSelectedHighlightLinkName(){
		driver.waitForElementPresent(SELECTED_HIGHLIGHT_LINK);
		String linkName = driver.findElement(SELECTED_HIGHLIGHT_LINK).getText();
		logger.info("Selected And highlight link: "+linkName);
		return linkName;
	}

	public void clickChangeBillingInformationBtn(){
		driver.waitForElementPresent(CHANGE_BILLING_INFO);
		driver.click(CHANGE_BILLING_INFO);
		logger.info("Change billing information button clicked");
		driver.waitForPageLoad();
		driver.pauseExecutionFor(3000);
	}

	public void clickUseThisBillingInformationBtn(){
		driver.quickWaitForElementPresent(USE_THIS_BILLING_INFORMATION);
		driver.click(USE_THIS_BILLING_INFORMATION);
		logger.info("Use this billing information clicked");
		driver.waitForPageLoad();
	}

	public void clickShopSkinCareBtnOnPWS(){
		driver.pauseExecutionFor(2000);
		driver.quickWaitForElementPresent(SHOP_SKINCARE_ON_PWS_LOC);
		driver.click(SHOP_SKINCARE_ON_PWS_LOC);
		logger.info("Products button now as shop skincare clicked");
		driver.waitForPageLoad();
	}

	public void mouseHoverOnShopSkinCareAndClickOnConsultantOnlyProductsLink(){
		driver.waitForElementPresent(SHOP_SKINCARE_ON_PWS_LOC);
		actions =  new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(SHOP_SKINCARE_ON_PWS_LOC)).build().perform();
		driver.waitForElementPresent(PRODUCT_LINK_UNDER_SHOP_SKIN_CARE);
		driver.click(PRODUCT_LINK_UNDER_SHOP_SKIN_CARE);
		logger.info("Mouse hover on shop skincare link and clicked on product link on pws");
	}

	public void clickConsultantOnlyProductOnPWS(String productName){
		driver.quickWaitForElementPresent(By.xpath(String.format(consultantOnlyProductonPWSLoc, productName)));
		driver.click(By.xpath(String.format(consultantOnlyProductonPWSLoc, productName)));
		logger.info("consultant only product selected is: "+productName);
	}

	public void clickRegimenOnPWS(String regimen){
		driver.quickWaitForElementPresent(By.xpath(String.format(regimenImageOnPwsLoc, regimen)));
		driver.click(By.xpath(String.format(regimenImageOnPwsLoc, regimen)));
		logger.info("Regimen selected is: "+regimen);
	}

	public String getOrderNumebrAfterOrderPlaced(){
		driver.waitForPageLoad();
		driver.waitForElementPresent(ORDER_NUMBER_AFTER_PLACED);
		String orderNumber = driver.findElement(ORDER_NUMBER_AFTER_PLACED).getAttribute("alt");
		logger.info("Order number is: "+orderNumber);
		return orderNumber;
	}

	public void clickHeaderLinkAfterLogin(String linkName) {
		try{
			driver.quickWaitForElementPresent(By.xpath(String.format(myAccountLinkAfterLoginLink, linkName)));
			Actions actions = new Actions(RFWebsiteDriver.driver);
			actions.moveToElement(driver.findElement(By.xpath(String.format("//nav[@id='Col1']//span[text()='%s']/..", linkName)))).click().build().perform();
			logger.info("my account link is clicked");
		}
		catch(NoSuchElementException e){
			driver.quickWaitForElementPresent(By.xpath("//*[@id='ConsultantWelcome']/ul/li[1]/a"));
			driver.click(By.xpath("//*[@id='ConsultantWelcome']/ul/li[1]/a"));
			logger.info("my account link is clicked");
		}
	}

	public void clickEditOrderLink(){
		driver.quickWaitForElementPresent(EDIT_ORDER_UNDER_MY_ACCOUNT_LOC);
		driver.click(EDIT_ORDER_UNDER_MY_ACCOUNT_LOC);
		logger.info("edit order link is clicked"); 
	}

	public void clickChangeLinkUnderShippingToOnPWS(){
		driver.quickWaitForElementPresent(CHANGE_SHIPPING_INFO_LINK_ON_PWS);
		driver.click(CHANGE_SHIPPING_INFO_LINK_ON_PWS);
		logger.info("Change Link under shipping to clicked");
		driver.waitForPageLoad();
	}

	public void clickEditOrderBtn(){
		driver.quickWaitForElementPresent(EDIT_ORDER_BTN_LOC);
		driver.click(EDIT_ORDER_BTN_LOC);
		logger.info("edit order button is clicked"); 
	}

	public boolean isOrderManagementSublinkPresent(String sublinkName){
		driver.waitForElementPresent(By.xpath(String.format(orderManagementSublink, sublinkName)));
		return driver.isElementPresent(By.xpath(String.format(orderManagementSublink, sublinkName)));
	}

	public void clickOrderManagementSublink(String sublinkName){
		driver.waitForElementPresent(By.xpath(String.format(orderManagementSublink, sublinkName)));
		driver.click(By.xpath(String.format(orderManagementSublink, sublinkName)));
		logger.info(sublinkName+"clicked");
		driver.pauseExecutionFor(2000);
	}

	public boolean isOrderNumberPresentAtOrderHistoryPage(){
		driver.waitForElementPresent(ORDER_NUMBER_AT_ORDER_HISTORY);
		return driver.isElementPresent(ORDER_NUMBER_AT_ORDER_HISTORY);
	}

	public void clickAddToCartBtnForEditOrder() {
		driver.waitForElementPresent(ADD_TO_CART_BTN_FOR_EDIT_ORDER);
		driver.click(ADD_TO_CART_BTN_FOR_EDIT_ORDER);
		logger.info("Add to cart btn clicked");
		driver.waitForStorfrontLegacyLoadingImageToDisappear();
	}

	public String getOrderUpdateMessage(){
		driver.waitForElementPresent(EDIT_ORDER_UPDATE_MESSAGE);
		String messgae = driver.findElement(EDIT_ORDER_UPDATE_MESSAGE).getText();
		logger.info("Order updation message is: "+messgae);
		return messgae;
	}

	public void clickOKBtnOfJavaScriptPopUp(){
		driver.pauseExecutionFor(2000);
		Alert alert = driver.switchTo().alert();
		alert.accept();
		logger.info("Ok button of java Script popup is clicked.");
		driver.waitForPageLoad();
		driver.pauseExecutionFor(3000);
	}

	public String convertBizSiteToComSite(String pws){
		String com  = "com";
		String biz ="biz";
		if(pws.contains(biz))
			pws = pws.replaceAll(biz,com);
		return pws;  
	}

	public String getDotComPWS(){
		driver.waitForElementPresent(COM_PWS_CONSULTANT_ENROLLMENT);
		String pwsUnderPulse = driver.findElement(COM_PWS_CONSULTANT_ENROLLMENT).getText();
		return pwsUnderPulse;
	}

	public String getDotBizPWS(){
		driver.waitForElementPresent(BIZ_PWS_CONSULTANT_ENROLLMENT);
		String pwsUnderPulse = driver.findElement(BIZ_PWS_CONSULTANT_ENROLLMENT).getText();
		return pwsUnderPulse;
	}

	public String getEmailId(){
		driver.waitForElementPresent(EMAIL_ADDRESS_CONSULTANT_ENROLLMENT);
		String pwsUnderPulse = driver.findElement(EMAIL_ADDRESS_CONSULTANT_ENROLLMENT).getText();
		return pwsUnderPulse;
	}

	public void clickMyAccountLink(){
		driver.waitForElementPresent(MY_ACCOUNT_LINK);
		driver.click(MY_ACCOUNT_LINK);
		logger.info("My Account link clicked");
		driver.waitForPageLoad();
	}

	public void mouseHoverShopSkinCareOnPWS(){
		driver.pauseExecutionFor(2000);
		actions =  new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(SHOP_SKINCARE_ON_PWS_LOC)).build().perform();
		logger.info("hover on Products link now as shop skincare");
	}

	public void mouseHoverShopSkinCareAndClickLinkOnPWS(String link){
		driver.pauseExecutionFor(2000);
		actions =  new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(SHOP_SKINCARE_ON_PWS_LOC)).build().perform();
		logger.info("hover on Products link now as shop skincare");
		driver.waitForElementPresent(By.xpath(String.format(linkUnderShopSkinCareOrBeAConsultant, link)));
		driver.click(By.xpath(String.format(linkUnderShopSkinCareOrBeAConsultant, link)));
		logger.info("Clicked "+link+" link is clicked after hovering shop skincare.");
		driver.waitForPageLoad();
	}

	public void mouseHoverShopSkinCareAndClickLink(String link){
		driver.pauseExecutionFor(2000);
		actions =  new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(SHOP_SKINCARE_LOC)).build().perform();
		logger.info("hover on Products link now as shop skincare");
		driver.click(By.xpath(String.format(linkUnderShopSkinCareOrBeAConsultant, link)));
		logger.info("Clicked "+link+" link is clicked after hovering shop skincare.");
		driver.waitForPageLoad();
	}

	public void mouseHoverBeAConsultantAndClickLink(String link){
		driver.pauseExecutionFor(2000);
		Actions actions =  new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(BE_A_CONSULTANT_LOC)).build().perform();
		logger.info("hover performed on be a consultant link.");
		driver.click(By.xpath(String.format(linkUnderShopSkinCareOrBeAConsultant, link)));
		logger.info("Clicked "+link+" link is clicked after hovering be a consultant.");
		driver.waitForPageLoad();
	}

	public void clickConsultantOnlyProduct(String productName){
		driver.quickWaitForElementPresent(By.xpath(String.format(consultantOnlyProduct, productName)));
		driver.click(By.xpath(String.format(consultantOnlyProduct, productName)));
		logger.info("consultant only product selected is: "+productName);
	}

	public void mouseHoverShopSkinCareAndClickSubLink(String link, String sublink){
		driver.pauseExecutionFor(2000);
		actions =  new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(SHOP_SKINCARE_LOC)).build().perform();
		logger.info("hover on Products link now as shop skincare");
		driver.click(By.xpath(String.format(sublinkUnderShopSkinCareOrBeAConsultant, link,sublink)));
		logger.info("Clicked "+sublink+" link is clicked under "+link+" after hovering shop skincare.");
		driver.waitForPageLoad();
	}

	public void mouseHoverAboutRFAndClickLink(String link){
		driver.pauseExecutionFor(2000);
		Actions actions =  new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(ABOUT_RF_LOC)).build().perform();
		logger.info("hover performed on about R+F link.");
		driver.click(By.xpath(String.format(linkUnderShopSkinCareOrBeAConsultant, link)));
		logger.info("Clicked "+link+" link is clicked after hovering About RF.");
		driver.waitForPageLoad();
	}

	public void mouseHoverShopSkinCare(){
		driver.pauseExecutionFor(2000);
		Actions actions =  new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(SHOP_SKINCARE_LOC)).build().perform();
		logger.info("hover on Products link now as shop skincare");
	}


	public void clickMyShoppingBagLink(){
		driver.waitForElementPresent(MY_SHOPPING_BAG_LINK);
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(MY_SHOPPING_BAG_LINK));
		logger.info("Clicked on My shopping bag link.");
		driver.waitForPageLoad();
	}

	public void clickOnRodanAndFieldsLogo(){
		try{
			driver.quickWaitForElementPresent(RODAN_AND_FIELDS_IMG_LOC);
			driver.click(RODAN_AND_FIELDS_IMG_LOC);
		}catch(Exception e){
			driver.quickWaitForElementPresent(By.xpath("//div[@id='Logo']//img"));
			driver.click(By.xpath("//div[@id='Logo']//img"));
		}
		logger.info("Rodan and Fields logo clicked"); 
		driver.waitForPageLoad();
	}

	public void clickContinueBtnForPCAndRC(){
		driver.waitForElementPresent(CONTINUE_BTN_PREFERRED_PROFILE_PAGE_LOC);
		//driver.click(CONTINUE_BTN_PREFERRED_PROFILE_PAGE_LOC);
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(CONTINUE_BTN_PREFERRED_PROFILE_PAGE_LOC));
		logger.info("Continue button clicked");
		driver.waitForPageLoad();
	}

	//	public void closeTheChildWindow() {
	//		String parentWindowID=driver.getWindowHandle();
	//		Set<String> set=driver.getWindowHandles();
	//		Iterator<String> it=set.iterator();
	//		while(it.hasNext()){
	//			String childWindowID=it.next();
	//			if(!parentWindowID.equalsIgnoreCase(childWindowID)){
	//				driver.switchTo().window(childWindowID);
	//				driver.close();
	//				driver.switchTo().window(parentWindowID);
	//			}
	//		}
	//	}

	public boolean isLoginFailed(){
		driver.quickWaitForElementPresent(By.id("loginError"));
		return driver.isElementPresent(By.id("loginError"));
	}

	public void refreshThePage(){
		driver.navigate().refresh();
		driver.waitForPageLoad();
		driver.pauseExecutionFor(2000);
	}

}
