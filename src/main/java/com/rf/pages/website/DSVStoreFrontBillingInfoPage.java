package com.rf.pages.website;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;

public class DSVStoreFrontBillingInfoPage extends DSVRFWebsiteBasePage {
	private static final Logger logger = LogManager
			.getLogger(DSVStoreFrontBillingInfoPage.class.getName());

	private static final By ADD_A_NEW_BILLING_PROFILE_LINK = By.xpath("//a[contains(text(),'Add a new billing profile')]");
	private static String BillingProfiles = "//ul[@id='multiple-billing-profiles']//span[contains(@class,'font-bold')][contains(text(),'%s')]";
	private static final By CARD_NUMBER_TXT_FIELD =  By.id("card-nr");
	private static final By NAME_ON_CARD_TXT_FIELD =  By.id("card-name");
	private static final By EXPIRY_MONTH_DROP_DOWN =  By.id("expiryMonth");
	private static final By EXPIRY_YEAR_DROP_DOWN =  By.id("expiryYear");
	private static final By SECURITY_CODE_TXT_FIELD =  By.id("security-code");
	private static final By CHOOSE_BILLING_ADDRESS_DROP_DOWN = By.id("addressBookdropdown");
	private static final By SAVE_BILLING_PROFILE_BTN = By.id("submitButton");
	private static final By CONFIRM_DELETE_POPUP = By.xpath("//input[@value='Yes, delete this profile']");
	private static final By USED_IN_AUTOSHIP_POPUP = By.xpath("//input[contains(@value,'NOT USED')]");
	private static final By BILLING_PROFILE_REMOVED_MSG = By.xpath("//h3[contains(text(),'Billing profile')]/following::p[text()='Your Billing profile has been removed'][1]");

	public DSVStoreFrontBillingInfoPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public void clickAddANewBillingProfileLink(){
		driver.quickWaitForElementPresent(ADD_A_NEW_BILLING_PROFILE_LINK);
		driver.click(ADD_A_NEW_BILLING_PROFILE_LINK);
	}

	public void enterNewBillingProfileDetails(String cardNumber,String nameOnCard,String expiryMonth,String expiryYear,String securityCode) throws Exception{
		driver.type(CARD_NUMBER_TXT_FIELD, cardNumber);
		driver.type(NAME_ON_CARD_TXT_FIELD, nameOnCard);
		Select expirationMonthDD= new Select(driver.findElement(EXPIRY_MONTH_DROP_DOWN));
		expirationMonthDD.selectByVisibleText(expiryMonth);
		Select expirationYearDD= new Select(driver.findElement(EXPIRY_YEAR_DROP_DOWN));
		expirationYearDD.selectByVisibleText(expiryYear);
		driver.type(SECURITY_CODE_TXT_FIELD, securityCode);
		Select chooseBillingAddressDD= new Select(driver.findElement(CHOOSE_BILLING_ADDRESS_DROP_DOWN));
		chooseBillingAddressDD.selectByIndex(0);		
	}

	public void clickSaveBillingProfileBtn(){
		driver.click(SAVE_BILLING_PROFILE_BTN);
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}	

	public boolean isBillingProfilePresentonPage(String billingName){
		driver.quickWaitForElementPresent(By.xpath(String.format(BillingProfiles, billingName)));
		return driver.isElementPresent(By.xpath(String.format(BillingProfiles, billingName)));		
	}

	public boolean isBillingProfileRemovedMsgAppeared(){
		driver.quickWaitForElementPresent(BILLING_PROFILE_REMOVED_MSG);
		return driver.isElementPresent(BILLING_PROFILE_REMOVED_MSG);		
	}

	public void clickEditBillingProfileLink(String billingName){
		driver.click(By.xpath(String.format(BillingProfiles, billingName)+"/following::a[text()='Edit'][1]"));
	}

	public void clickDeleteBillingProfileLink(String billingName){
		driver.click(By.xpath(String.format(BillingProfiles, billingName)+"/following::a[text()='Delete'][1]"));
		driver.quickWaitForElementPresent(CONFIRM_DELETE_POPUP);
		driver.click(CONFIRM_DELETE_POPUP);
		driver.waitForLoadingImageToDisappear();
		try{
			driver.quickWaitForElementPresent(USED_IN_AUTOSHIP_POPUP);
			driver.click(USED_IN_AUTOSHIP_POPUP);
			driver.waitForLoadingImageToDisappear();
		}catch(Exception e){
			logger.info("Used in Autoship? popup NOT appeared");
		}
	}

	public void cleanAllBillingProfiles(){
		List<WebElement> allBillingProfiles =  driver.findElements(By.xpath("//ul[@id='multiple-billing-profiles']/li"));
		for(int i=1;i<=allBillingProfiles.size();i++){
			try{
				driver.click(By.xpath("//ul[@id='multiple-billing-profiles']/descendant::a[contains(text(),'Delete')][1]"));
			}catch(NoSuchElementException e){
				logger.info("All Profiles deleted except the last one");
				break;
			}
			driver.quickWaitForElementPresent(CONFIRM_DELETE_POPUP);
			driver.click(CONFIRM_DELETE_POPUP);
			driver.waitForLoadingImageToDisappear();
			try{
				driver.quickWaitForElementPresent(USED_IN_AUTOSHIP_POPUP);
				driver.click(USED_IN_AUTOSHIP_POPUP);
				driver.waitForLoadingImageToDisappear();
			}catch(Exception e){
				logger.info("Used in Autoship? popup NOT appeared");
			}
		}
	}

	public void enterNameAndSecurityCode(String billingName,String securityCode){
		driver.type(NAME_ON_CARD_TXT_FIELD, billingName);
		driver.type(SECURITY_CODE_TXT_FIELD, securityCode);
	}

}
