package com.rf.pages.website.storeFrontLegacy;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;

public class StoreFrontLegacyHomePage extends StoreFrontLegacyRFWebsiteBasePage{

	private static final By BUSINESS_SYSTEM_LOC = By.xpath("//span[text()='Business System']");
	private static final By ENROLL_NOW_ON_BUSINESS_PAGE_LOC = By.xpath("//a[text()='ENROLL NOW']");
	private static final By CID_LOC = By.id("NameOrId");
	private static final By CID_SEARCH_LOC = By.id("BtnSearch");
	private static final By SEARCH_RESULTS_LOC = By.xpath("//div[@id='searchResults']//a");


	public StoreFrontLegacyHomePage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public void clickBusinessSystemBtn(){
		driver.quickWaitForElementPresent(BUSINESS_SYSTEM_LOC);
		driver.click(BUSINESS_SYSTEM_LOC);
		driver.waitForPageLoad();
	}

	public void clickEnrollNowBtnOnBusinessPage(){
		driver.quickWaitForElementPresent(ENROLL_NOW_ON_BUSINESS_PAGE_LOC);
		driver.click(ENROLL_NOW_ON_BUSINESS_PAGE_LOC);
		driver.waitForPageLoad();
	}

	public void enterCID(String cid){
		driver.quickWaitForElementPresent(CID_LOC);
		driver.type(CID_LOC, cid);
		driver.click(CID_SEARCH_LOC);
	}

	public void clickSearchResults(){
		driver.quickWaitForElementPresent(SEARCH_RESULTS_LOC);
		driver.click(SEARCH_RESULTS_LOC);
		driver.waitForPageLoad();
	}

	public void selectEnrollmentKit(String kit){
		if(kit.equalsIgnoreCase("Big Business Launch Kit")){
			driver.quickWaitForElementPresent(By.xpath("//cufontext[contains(text(),'Big')]/ancestor::div[@class='KitContent'][1]"));
			driver.click(By.xpath("//cufontext[contains(text(),'Big')]/ancestor::div[@class='KitContent'][1]"));
		}
	}

	public void selectRegimenAndClickNext(String regimen){
		if(regimen.equalsIgnoreCase("Redefine")){
			driver.quickWaitForElementPresent(By.xpath("//cufontext[text()='REDEFINE ']/ancestor::a[1]"));
			driver.click(By.xpath("//cufontext[text()='REDEFINE ']/ancestor::a[1]"));
		}
		driver.click(By.xpath("//a[@id='BtnDone']"));
	}

	public void selectEnrollmentType(String enrollmentType){
		if(enrollmentType.equalsIgnoreCase("Express")){
			driver.quickWaitForElementPresent(By.xpath("btnGoToExpressEnroll"));
			driver.click(By.id("btnGoToExpressEnroll"));
		}

		driver.click(By.xpath("//a[@id='btnNext']"));
	}

	public void enterSetUpAccountInformation(String firstName,String lastName,String emailAddress,String password,String addressLine1,String postalCode){
		driver.type(By.id("Account_FirstName"), firstName);
		driver.type(By.id("Account_LastName"), lastName);
		driver.type(By.id("Account_EmailAddress"), emailAddress);
		driver.type(By.id("Account_Password"), password);
		driver.type(By.id("txtConfirmPassword"), password);
		driver.type(By.id("MainAddress_Address1"), addressLine1);
		driver.type(By.id("MainAddress_PostalCode"), postalCode+"\t");
		driver.type(By.id("txtPhoneNumber1"),"415");
		driver.type(By.id("txtPhoneNumber2"),"780");
		driver.type(By.id("txtPhoneNumber3"),"9099");		
	}

	public void clickSetUpAccountNextBtn(){
		driver.click(By.id("btnNext"));
		driver.quickWaitForElementPresent(By.xpath(".//*[@id='QAS_AcceptOriginal']"));
		driver.findElement(By.xpath(".//*[@id='QAS_AcceptOriginal']")).click();
		driver.waitForPageLoad();
	}

	public void enterBillingInformation(String cardNumber,String nameOnCard,String expMonth,String expYear){
		driver.findElement(By.xpath(".//*[@id='Payment_AccountNumber']")).sendKeys(cardNumber);
		driver.findElement(By.xpath(".//*[@id='Payment_NameOnCard']")).sendKeys("RFtest");
		Select dropdown1 = new Select(driver.findElement(By.id("ExpMonth")));
		dropdown1.selectByVisibleText(expMonth);
		Select dropdown2 = new Select(driver.findElement(By.id("ExpYear")));
		dropdown2.selectByVisibleText(expYear);
	}

	public void enterAccountInformation(){
		int randomNum = CommonUtils.getRandomNum(1000, 9999);
		driver.findElement(By.xpath(".//*[@id='txtTaxNumber1']")).sendKeys("121");
		driver.findElement(By.xpath(".//*[@id='txtTaxNumber2']")).sendKeys("23");
		driver.findElement(By.xpath(".//*[@id='txtTaxNumber3']")).sendKeys(String.valueOf(randomNum));
		driver.findElement(By.xpath(".//*[@id='Account_EnrollNameOnCard']")).sendKeys("RFtest ");
	}
	
	public void enterPWS(String pws){
		driver.quickWaitForElementPresent(By.id("Account_EnrollSubdomain"));
		driver.type(By.id("Account_EnrollSubdomain"), pws);
	}

	public void clickCompleteAccountNextBtn(){
		driver.waitForElementToBeVisible(By.xpath("//li[@id='Abailable0' and contains(text(),'is available')]"), 30);
		driver.findElement(By.xpath(".//*[@id='btnNext']/span")).click();
	}

	public void clickTermsAndConditions(){
		driver.findElement(By.xpath(".//*[@id='PoliceProcedureAcceptance']/div[2]/ul/li[2]/div/div[1]/div/div")).click();
		driver.findElement(By.xpath(".//*[@id='FrmReviewAndConfirm']/fieldset[3]/div[2]/ul/li[2]/div/div[1]/div/div")).click();
		driver.findElement(By.xpath(".//*[@id='FrmReviewAndConfirm']/fieldset[3]/div[3]/ul/li[2]/div/div[1]/div/div")).click();
		driver.findElement(By.xpath(".//*[@id='FrmReviewAndConfirm']/fieldset[3]/div[4]/ul/li[2]/div/div[1]/div/div")).click();
	}

	public void chargeMyCardAndEnrollMe(){
		driver.quickWaitForElementPresent(By.id("ChargeAndEnrollMe"));
		driver.click(By.id("ChargeAndEnrollMe"));
		driver.pauseExecutionFor(2000);
		driver.quickWaitForElementPresent(By.id("confirmAuthoship"));
		driver.click(By.id("confirmAuthoship"));
		driver.waitForPageLoad();
	}

	public boolean isCongratulationsMessageAppeared(){
		driver.quickWaitForElementPresent(By.id("Congrats"));
		return driver.isElementPresent(By.id("Congrats"));
	}

}
