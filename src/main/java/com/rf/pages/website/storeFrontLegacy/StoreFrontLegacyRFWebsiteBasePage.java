package com.rf.pages.website.storeFrontLegacy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstantsRFL;

import com.rf.pages.RFBasePage;

public class StoreFrontLegacyRFWebsiteBasePage extends RFBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontLegacyRFWebsiteBasePage.class.getName());

	private static String regimenProductLoc = "//div[@id='ProductCategories']//span[text()='%s']/preceding::p[1]//img";
	private static String businessSystemSubLink= "//div[@id='ContentWrapper']//span[contains(text(),'%s')]";

	private static final By LOGOUT_BTN_LOC = By.xpath("//a[text()='Log Out']");
	private static final By SHOP_SKINCARE_HEADER_LOC = By.xpath("//span[text()='Shop Skincare']");
	private static final By ADD_TO_CART_BTN_LOC = By.xpath("//a[@id='addToCartButton']/span");
	private static final By MY_SHOPPING_BAG_LINK = By.xpath("//a[@class='BagLink']");
	private static final By CHECKOUT_BTN_OF_MY_SHOPPING_BAG_LINK = By.xpath("//span[text()='Checkout Now']");
	private static final By OK_BTN_OF_CONFIRMATION_POPUP_FOR_ADHOC_ORDER = By.xpath("//span[text()='OK']");
	private static final By CHECKOUT_BTN = By.xpath("//span[text()='Checkout']");
	private static final By CONTINUE_BTN_PREFERRED_AUTOSHIP_CART_PAGE_LOC = By.xpath("//a[contains(@id,'uxContinue')]");
	private static final By COMPLETE_ORDER_BTN = By.xpath("//input[contains(@id,'uxSubmitOrder')]");
	private static final By ORDER_CONFIRMATION_THANK_YOU_TXT = By.xpath("//cufontext[contains(text(),'Thank')]");
	private static final By CONTINUE_BTN_BILLING_PAGE = By.xpath("//span[contains(text(),'Change Billing Information')]/following::a[contains(@id,'uxContinue')]");
	private static final By CONTINUE_WITHOUT_CONSULTANT_LINK = By.xpath("//a[contains(@id,'uxSkipStep')]");
	private static final By ADD_TO_CART_BTN_AS_PER_REGIMEN = By.xpath("//div[@id='FullPageItemList']/div[1]//a[@id='addToCartButton']");
	private static final By HELLO_OR_WELCOME_TXT_ON_CORP = By.xpath("//*[contains(text(),'Hello') or contains(text(),'Welcome')]");
	private static final By ORDER_PLACED_CONFIRMATION_TEXT = By.xpath("//div[@id='RFContent']//b");
	private static final By CONSULTANTS_ONLY_PRODUCTS_REGIMEN = By.xpath("//cufontext[contains(text(),'Consultant-Only ')]/following::a[1]/img");
	private static final By FORGOT_PASSWORD_LINK = By.xpath("//a[@id='uxForgotPasswordLink']");
	//private static final By PRODUCT_LOC = By.xpath("//span[text()='Products']");
	private static final By SHOP_SKINCARE_LOC = By.xpath("//span[text()='Shop Skincare']");
	private static final By SELECTED_HIGHLIGHT_LINK = By.xpath("//div[@id='ContentWrapper']//a[@class='selected']/span");

	protected RFWebsiteDriver driver;
	private String RFL_DB = null;
	private Actions actions;
	public StoreFrontLegacyRFWebsiteBasePage(RFWebsiteDriver driver){		
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
		driver.quickWaitForElementPresent(ADD_TO_CART_BTN_LOC);
		driver.click(ADD_TO_CART_BTN_LOC);
		logger.info("Add to cart button is clicked");

	}
	public void mouseHoverOnMyShoppingBagLinkAndClickOnCheckoutBtn(){
		actions =  new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(MY_SHOPPING_BAG_LINK)).click(driver.findElement(CHECKOUT_BTN_OF_MY_SHOPPING_BAG_LINK)).build().perform();
		logger.info("Mouse hover on My shopping bag link and clicked on checkout button");
	}


	public void clickOKBtnOnPopup(){
		driver.quickWaitForElementPresent(OK_BTN_OF_CONFIRMATION_POPUP_FOR_ADHOC_ORDER);
		driver.click(OK_BTN_OF_CONFIRMATION_POPUP_FOR_ADHOC_ORDER);
		logger.info("Ok button clicked on popup");
	}

	public boolean isThankYouTextPresentAfterOrderPlaced(){
		driver.waitForElementPresent(ORDER_CONFIRMATION_THANK_YOU_TXT);
		return driver.isElementPresent(ORDER_CONFIRMATION_THANK_YOU_TXT);
	}
	public void clickCheckoutBtn(){
		driver.quickWaitForElementPresent(CHECKOUT_BTN);
		driver.click(CHECKOUT_BTN);
		logger.info("Checkout button clicked");
		driver.waitForPageLoad();
	}

	public void clickContinueBtn(){
		driver.quickWaitForElementPresent(CONTINUE_BTN_PREFERRED_AUTOSHIP_CART_PAGE_LOC);
		driver.click(CONTINUE_BTN_PREFERRED_AUTOSHIP_CART_PAGE_LOC);
		logger.info("Continue button clicked on Autoship cart page");
		driver.waitForPageLoad();
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
		driver.click(By.xpath("//a[text()='Log-Out' or text()='Log Out']"));
		logger.info("Logout");  
		driver.pauseExecutionFor(3000);
		driver.waitForPageLoad();
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
		driver.click(ADD_TO_CART_BTN_AS_PER_REGIMEN);
		logger.info("Add to cart button is clicked");
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
		driver.pauseExecutionFor(2000);
		driver.quickWaitForElementPresent(SHOP_SKINCARE_LOC);
		driver.click(SHOP_SKINCARE_LOC);
		logger.info("Products button clicked");
		driver.waitForPageLoad();
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

}
