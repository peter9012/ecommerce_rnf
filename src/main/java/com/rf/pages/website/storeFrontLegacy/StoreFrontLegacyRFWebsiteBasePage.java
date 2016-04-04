package com.rf.pages.website.storeFrontLegacy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstantsRFL;

import com.rf.pages.RFBasePage;



public class StoreFrontLegacyRFWebsiteBasePage extends RFBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontLegacyRFWebsiteBasePage.class.getName());

	private static final By LOGOUT_BTN_LOC = By.xpath("//a[text()='Log Out']");
	private static final By SHOP_SKINCARE_HEADER_LOC = By.xpath("//span[text()='SHOP SKINCARE']");
	private static String regimenProductLoc = "//div[@id='ProductCategories']//span[text()='%s']/preceding::p[1]//img";
	private static final By ADD_TO_CART_BTN_LOC = By.xpath("//a[@id='addToCartButton']/span");
	private static final By MY_SHOPPING_BAG_LINK = By.xpath("//a[@class='BagLink']");
	private static final By CHECKOUT_BTN_OF_MY_SHOPPING_BAG_LINK = By.xpath("//span[text()='Checkout Now']");
	private static final By OK_BTN_OF_CONFIRMATION_POPUP_FOR_ADHOC_ORDER = By.xpath("//span[text()='OK']");
	private static final By CHECKOUT_BTN = By.xpath("//span[text()='Checkout']");
	private static final By CONTINUE_BTN_PREFERRED_AUTOSHIP_CART_PAGE_LOC = By.xpath("//a[contains(@id,'uxContinue')]");
	private static final By COMPLETE_ORDER_BTN = By.xpath("//input[contains(@id,'uxSubmitOrder')]");
	private static final By ORDER_CONFIRMATION_THANK_YOU_TXT = By.xpath("//cufontext[contains(text(),'Thank')]");
	private static final By CONTINUE_BTN_BILLING_PAGE = By.xpath("//span[contains(text(),'Change Billing Information')]/following::a[contains(@id,'uxContinue')]");
	private final By CONTINUE_WITHOUT_CONSULTANT_LINK = By.xpath("//a[contains(@id,'uxSkipStep')]");

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

	public void openBizPWS(){
		driver.get(TestConstantsRFL.BIZ_PWS);
	}
	public void openComPWS(){
		driver.get(TestConstantsRFL.COM_PWS);
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
		driver.get(pws);
		logger.info("Open Pws Site Is: "+pws);
		driver.waitForPageLoad();
	}

	public void clickContinueWithoutConsultantLink(){
		driver.quickWaitForElementPresent(CONTINUE_WITHOUT_CONSULTANT_LINK);
		driver.click(CONTINUE_WITHOUT_CONSULTANT_LINK);
		logger.info("Continue without sponser link clicked");
	}

}