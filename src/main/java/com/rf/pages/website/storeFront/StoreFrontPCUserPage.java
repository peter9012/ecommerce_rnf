package com.rf.pages.website.storeFront;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import com.rf.core.driver.website.RFWebsiteDriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontPCUserPage extends StoreFrontRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontPCUserPage.class.getName());


	public StoreFrontPCUserPage(RFWebsiteDriver driver) {
		super(driver);
	}

	private final By WELCOME_USER_LOC = By.xpath("//a[contains(text(),'Welcome')]");
	//private final By WELCOME_USER_DD_LOC = By.cssSelector("li[id='account-info-button']"); 
	private final By WELCOME_DD_ORDERS_LINK_LOC = By.xpath("//div[@id='account-info']//a[text()='Orders']");
	private final By WELCOME_DD_ACCOUNT_INFO_LOC = By.xpath("//a[text()='Account Info']");
	private final By YOUR_ACCOUNT_DROPDOWN_LOC = By.xpath("//div[@id='left-menu']//div/button[contains(text(),'Your Account')]");

	public boolean verifyPCUserPage(){
		driver.waitForElementPresent(WELCOME_USER_LOC);
		return driver.isElementPresent(WELCOME_USER_LOC);		
	}

	public StoreFrontAccountInfoPage clickAccountInfoLinkPresentOnWelcomeDropDown() throws InterruptedException{
		logger.info(WELCOME_DD_ACCOUNT_INFO_LOC);
		driver.click(WELCOME_DD_ACCOUNT_INFO_LOC);
		logger.info("Account info linked from welcome drop down clicked");
		return new StoreFrontAccountInfoPage(driver);
	}

	public StoreFrontCartAutoShipPage addProductToPCPerk(){
		try{
			driver.quickWaitForElementPresent(By.xpath("//div[contains(@class,'blue')]/div[2]/div[1]//input[contains(@value,'PC Perks')]"));
			driver.click(By.xpath("//div[contains(@class,'blue')]/div[2]/div[1]//input[contains(@value,'PC Perks')]"));
		}catch(Exception e){
			try{
				driver.click(By.xpath("//div[contains(@class,'blue')]/div[2]/div[2]//input[contains(@value,'PC Perks')]"));
			}catch(Exception e1){
				driver.click(By.xpath("//div[contains(@class,'blue')]/div[2]/div[3]//input[contains(@value,'PC Perks')]"));
			}
		}

		logger.info("Add Product to PC Perk button clicked");

		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='OK']"));
			driver.click(By.xpath("//input[@value='OK']"));
			driver.waitForLoadingImageToDisappear();
		}catch(Exception e){

		}

		return new StoreFrontCartAutoShipPage(driver);
	}

	public void clickOnPCPerksStatus(){
		driver.waitForElementPresent(By.xpath("//a[contains(text(),'PC Perks Status')]"));
		driver.click(By.xpath("//a[contains(text(),'PC Perks Status')]"));
		driver.waitForPageLoad();
		driver.pauseExecutionFor(3000);
	}

	public void clickDelayOrCancelPCPerks(){
		driver.waitForElementPresent(By.xpath("//a[text()='Delay or Cancel PC Perks']"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//a[text()='Delay or Cancel PC Perks']")));
		//driver.click(By.xpath("//a[text()='Delay or Cancel PC Perks']"));
	}

	public void clickPleaseCancelMyPcPerksActBtn(){
		driver.waitForElementPresent(By.id("cancel-pc-perks-button"));
		driver.click(By.id("cancel-pc-perks-button"));
	}

	public StoreFrontHomePage cancelMyPCPerksAct(){
		driver.waitForElementPresent(By.id("problemType"));
		//driver.click(By.id("problemType"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.id("problemType")));
		driver.waitForElementPresent(By.xpath("//select[@id='problemType']/option[6]"));
		driver.click(By.xpath("//select[@id='problemType']/option[6]"));
		driver.waitForElementPresent(By.xpath("//textarea[@id='terminationComments']"));
		driver.findElement(By.xpath("//textarea[@id='terminationComments']")).sendKeys("test");
		driver.click(By.xpath("//input[@id='pcperkTerminationConfirm']"));
		driver.waitForElementPresent(By.xpath("//input[@id='confirmpcTemrminate']"));
		driver.click(By.xpath("//input[@id='confirmpcTemrminate']"));
		driver.waitForLoadingImageToDisappear();
		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='Close window']"));
			driver.click(By.xpath("//input[@value='Close window']"));
		}catch(Exception e){   
			driver.click(By.xpath("//div[@id='popup-content']/div/div/following::input[@value='Close window']"));
		}
		driver.waitForPageLoad();
		return new StoreFrontHomePage(driver);
	}

	public boolean validateNextPCPerksMiniCart() {  
		return driver.findElement(By.xpath("//li[@id='mini-shopping-special-button']")).isDisplayed();
	}

	public void clickChangeMyAutoshipDateButton(){
		driver.waitForElementPresent(By.id("change-autoship-button"));
		driver.click(By.id("change-autoship-button"));
	}

	public void selectSecondAutoshipDateAndClickSave(){
		driver.waitForElementPresent(By.xpath("//ul[@id='autoship-date']//li[2]/span[1]"));
		driver.click(By.xpath("//ul[@id='autoship-date']//li[2]/span[1]"));
		logger.info("pc perks delayed date selected");
		driver.waitForElementPresent(By.xpath("//ul[@id='autoship-date']//input[@value='save']"));
		driver.click(By.xpath("//ul[@id='autoship-date']//input[@value='save']"));
		logger.info("save button clicked after different date selected");
		driver.waitForLoadingImageToDisappear();

	}

	public String getAccountterminationReasonTooMuchProduct(){
		driver.waitForElementPresent(By.id("problemType"));
		driver.click(By.id("problemType"));
		driver.waitForElementPresent(By.xpath("//select[@id='problemType']/option[2]"));
		return driver.findElement(By.xpath("//select[@id='problemType']/option[2]")).getText();
	}

	public String getAccountterminationReasonTooExpensive(){
		driver.waitForElementPresent(By.id("problemType"));
		driver.click(By.id("problemType"));
		driver.waitForElementPresent(By.xpath("//select[@id='problemType']/option[3]"));
		return driver.findElement(By.xpath("//select[@id='problemType']/option[3]")).getText();
	}
	public String getAccountterminationReasonEnrolledInAutoShipProgram(){
		driver.waitForElementPresent(By.id("problemType"));
		driver.click(By.id("problemType"));
		driver.waitForElementPresent(By.xpath("//select[@id='problemType']/option[4]"));
		return driver.findElement(By.xpath("//select[@id='problemType']/option[4]")).getText();
	}
	public String getAccountterminationReasonProductNotRight(){
		driver.waitForElementPresent(By.id("problemType"));
		driver.click(By.id("problemType"));
		driver.waitForElementPresent(By.xpath("//select[@id='problemType']/option[5]"));
		return driver.findElement(By.xpath("//select[@id='problemType']/option[5]")).getText();
	}
	public String getAccountterminationReasonUpgradingToConsultant(){
		driver.waitForElementPresent(By.id("problemType"));
		driver.click(By.id("problemType"));
		driver.waitForElementPresent(By.xpath("//select[@id='problemType']/option[6]"));
		return driver.findElement(By.xpath("//select[@id='problemType']/option[6]")).getText();
	}
	public String getAccountterminationReasonReceiveProductTooOften(){
		driver.waitForElementPresent(By.id("problemType"));
		driver.click(By.id("problemType"));
		driver.waitForElementPresent(By.xpath("//select[@id='problemType']/option[7]"));
		return driver.findElement(By.xpath("//select[@id='problemType']/option[7]")).getText();
	}
	public String getAccountterminationReasonDoNotWantToObligated(){
		driver.waitForElementPresent(By.id("problemType"));
		driver.click(By.id("problemType"));
		driver.waitForElementPresent(By.xpath("//select[@id='problemType']/option[8]"));
		return driver.findElement(By.xpath("//select[@id='problemType']/option[8]")).getText();
	}
	public String getAccountterminationReasonOther(){
		driver.waitForElementPresent(By.id("problemType"));
		driver.click(By.id("problemType"));
		driver.waitForElementPresent(By.xpath("//select[@id='problemType']/option[9]"));
		return driver.findElement(By.xpath("//select[@id='problemType']/option[9]")).getText();
	}

	public boolean verifyToSectionInSendcancellationMessageSection(){
		driver.quickWaitForElementPresent(By.xpath("//form[@id='CancelAutoshipAccountForm']/div[2]/div[1]/div[2]"));
		return driver.isElementPresent(By.xpath("//form[@id='CancelAutoshipAccountForm']/div[2]/div[1]/div[2]"));
	}
	public boolean verifySubjectSectionInSendcancellationMessageSection(){
		driver.quickWaitForElementPresent(By.xpath("//form[@id='CancelAutoshipAccountForm']/div[2]/div[2]/div[2]"));
		return driver.isElementPresent(By.xpath("//form[@id='CancelAutoshipAccountForm']/div[2]/div[2]/div[2]"));
	}

	public boolean verifyMessageSectionInSendcancellationMessageSection(){
		driver.quickWaitForElementPresent(By.xpath("//textarea[@id='terminationComments']"));
		return driver.isElementPresent(By.xpath("//textarea[@id='terminationComments']"));
	}

	public boolean verifyIHaveChangedMyMindButton(){
		driver.quickWaitForElementPresent(By.xpath("//form[@id='CancelAutoshipAccountForm']/div[2]/div[4]/div[2]/input[1]"));
		return driver.isElementPresent(By.xpath("//form[@id='CancelAutoshipAccountForm']/div[2]/div[4]/div[2]/input[1]"));
	}

	public boolean verifySendAnEmailToCancelAccountButton(){
		driver.quickWaitForElementPresent(By.xpath("//form[@id='CancelAutoshipAccountForm']/div[2]/div[4]/div[2]/input[2]"));
		return driver.isElementPresent(By.xpath("//form[@id='CancelAutoshipAccountForm']/div[2]/div[4]/div[2]/input[2]"));
	}

	public boolean validatePCPerkCartDisplayed(){
		driver.waitForElementPresent(By.xpath("//div[@id='bag-special']/span"));
		return driver.isElementPresent(By.xpath("//div[@id='bag-special']/span"));
	}

	public String getNextBillAndShipDate(){
		driver.quickWaitForElementPresent(By.xpath("//div[@id='pc-perks-status']/div[3]/span[2]"));
		return driver.findElement(By.xpath("//div[@id='pc-perks-status']/div[3]/span[2]")).getText();
	}		

	public String getOneMonthOutDate(String date){
		String completeDate[] = date.split(" ");
		String []splittedDay =completeDate[1].split("\\,");
		String day =splittedDay[0];
		String year =completeDate[2];
		String month=completeDate[0];
		int a = 0;
		int b = 0;
		String UIMonth = null;
		if(month.equalsIgnoreCase("January")){
			a=1;
		}else if(month.equalsIgnoreCase("February")){
			a=2;
		}else if(month.equalsIgnoreCase("March")){
			a=3;
		}
		else if(month.equalsIgnoreCase("April")){
			a=4;
		}
		else if(month.equalsIgnoreCase("May")){
			a=5;
		}
		else if(month.equalsIgnoreCase("June")){
			a=6;
		}
		else if(month.equalsIgnoreCase("July")){
			a=7;
		}
		else if(month.equalsIgnoreCase("August")){
			a=8;
		}
		else if(month.equalsIgnoreCase("September")){
			a=9;
		}
		else if(month.equalsIgnoreCase("October")){
			a=10;
		}
		else if(month.equalsIgnoreCase("November")){
			a=11;
		}else if(month.equalsIgnoreCase("December")){
			a=12;
		}else{
			a=0;
		}
		a=a+1;
		if(a==13){
			a=1;
			b=1;
		}
		switch (a) {  
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
		if(b==1){
			int yearly=Integer.parseInt(year)+1;
			year=Integer.toString(yearly);
		}
		String dateAfterOneMonth=UIMonth+" "+day+","+" "+year;
		logger.info("created date is "+dateAfterOneMonth);
		return dateAfterOneMonth;
	}

	public boolean verifyPCPerksStatus(){
		driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']//div[contains(text(),'PC perks status')]"));
		if(driver.isElementPresent(By.xpath("//div[@id='main-content']//div[contains(text(),'PC perks status')]"))){
			return true;
		}else{
			return false;
		}
	}

	public String getShipAndBillDateAfterOneMonthFromUI(){
		driver.waitForElementPresent(By.xpath("//ul[@id='autoship-date']/li[2]/ul/li[1]/span[2]"));
		String dateList=driver.findElement(By.xpath("//ul[@id='autoship-date']/li[2]/ul/li[1]/span[2]")).getText();
		logger.info("bill and ship date after one month is "+dateList);
		return dateList;
	}

	public String getShipAndBillDateAfterTwoMonthFromUI(){
		driver.waitForElementPresent(By.xpath("//ul[@id='autoship-date']/li[2]/ul/li[2]/span[2]"));
		String dateList=driver.findElement(By.xpath("//ul[@id='autoship-date']/li[2]/ul/li[2]/span[2]")).getText();
		logger.info("bill and ship date after two month is "+dateList);
		return dateList;
	}

	public void selectFirstAutoshipDateAndClickSave(){
		driver.waitForElementPresent(By.xpath("//ul[@id='autoship-date']//input[@value='save']"));
		driver.click(By.xpath("//ul[@id='autoship-date']//input[@value='save']"));
		logger.info("save button clicked after different date selected");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public boolean verifyNextAutoshipDateRadioButtons(){
		driver.quickWaitForElementPresent(By.xpath("//span[@class='radio-button selectautoshipDate']"));
		if(driver.isElementPresent(By.xpath("//span[@class='radio-button selectautoshipDate']"))){
			return true;
		}else{
			return false;
		}
	}

	public void clickOnAddtoPCPerksButton(){
		driver.quickWaitForElementPresent(By.xpath("//div[@id='main-content']/descendant::input[contains(@value,'ADD to PC Perks')][1]"));
		//driver.click(By.xpath("//div[@id='main-content']/descendant::input[contains(@value,'ADD to PC Perks')][1]"));
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(By.xpath("//div[@id='main-content']/descendant::input[contains(@value,'ADD to PC Perks')][1]")));
		try{
			driver.quickWaitForElementPresent(By.xpath("//input[@value='OK']"));
			driver.click(By.xpath("//input[@value='OK']"));
		}catch(Exception e){
		}
		logger.info("Add To PC perks button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public boolean verifyUpdateCartMessage(String message){
		driver.quickWaitForElementPresent(By.xpath(".//div[@id='globalMessages']//p"));
		if(driver.findElement(By.xpath(".//div[@id='globalMessages']//p")).getText().equalsIgnoreCase(message)){
			return true;
		}else{
			return false;
		}

	}

}

