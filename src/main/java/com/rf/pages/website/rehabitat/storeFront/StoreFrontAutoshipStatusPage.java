package com.rf.pages.website.rehabitat.storeFront;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;

public class StoreFrontAutoshipStatusPage extends StoreFrontWebsiteBasePage{

	public StoreFrontAutoshipStatusPage(RFWebsiteDriver driver) {
		super(driver);  
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontAutoshipStatusPage.class.getName());

	private final By CLOSE_ICON_PULSE_OVERLAY_LOC = By.id("cboxClose");
	private final By LEARN_MORE_ABOUT_PULSE_LOC = By.xpath("//a[contains(text(),'Learn more about Pulse')]");
	private final By PULSE_POPUP_LOC = By.xpath("//h1[contains(text(),'Pulse Business Management')]");
	private final By PC_PERKS_AUTOSHIP_STATUS_LOC = By.xpath("//div[contains(@class,'pcPerks-statusPage')]/div[contains(text(),'PC Perks Status')]");
	private final By DELAY_OR_CANCEL_PC_PERKS_LOC = By.xpath("//a[text()='Delay or Cancel PC Perks']");
	private final By DELAY_OR_CANCEL_PC_PERKS_POPUP_LOC = By.xpath("//h2[text()='Delay or Cancel PC Perks']");
	private final By CANCEL_OPTION_ON_CANCEL_PC_PERKS_POPUP_LOC = By.xpath("//h2[text()='Delay or Cancel PC Perks']");
	private final By DELAY_OPTION_ON_CANCEL_PC_PERKS_POPUP_LOC = By.xpath("//a[text()='Delay']");
	private final By REASON_DD_LOC = By.id("//select[@id='code']");
	private final By REASON_DD_VALUE_OTHER_LOC = By.id("//select[@id='code']/option[text()='Other']");
	private final By MESSAGE_BOX_LOC = By.id("reasonMessage");
	private final By SEND_EMAIL_TO_CANCEL_ACCOUNT_BUTTON_LOC = By.xpath("//form[@id='pcperkscancellationform']/descendant::button[text()='SEND E-MAIL TO CANCEL ACCOUNT']");
	private final By ACCOUNT_TERMINATION_CONFIRMATION_POPUP_LOC = By.xpath("//div[@class='modal-content']/descendant::h2[text()='CONFIRM ACCOUNT TERMINATION']");
	private final By POPUP_CONFIRM_TERMINATION_BUTTON = By.id("cancel-pc-perks-confirm");
	private final By NEXT_BILL_SHIP_DATE_LOC = By.id("strtDate");
	private final By NEXT_BILL_SHIP_DATE_TEXTBOX_LOC = By.xpath("//*[@id='command']//input[@type='text']");
	private final By SUBMIT_QUERY_BUTTON = By.xpath("//*[@id='command']/input[@type='submit']");
	private final By VIEW_DETAILS_LINK_AUTOSHIP_STATUS_LOC = By.xpath("//a[text()='view details']");
	private final By PC_PERKS_STATUS_ON_AUTOSHIP_STATUS_PAGE = By.xpath("//div[contains(text(),'Current PC Perks Status')]/following::div[1]");
	private final By SUBSCRIBE_TO_PULSE_BTN_LOC = By.id("asmrunnowconfirmsubmit");
	private final By CANCEL_PULSE_SUBSCRIPTION_BTN_LOC = By.xpath("//a[contains(text(),'Cancel my Pulse subscription')]");
	private static final By ENROLL_IN_CRP_BTN_LOC = By.xpath("//input[@value='Enroll In CRP']");
	private static final By CANCEL_MY_CRP_LINK_LOC = By.xpath("//a[@id='cancelCRPStatus']");
	private static final By CANCEL_CRP_BUTTON_LOC = By.xpath("//input[@value='CANCEL CRP']");
	private static final By ACTION_SUCCESS_MSG_ON_AUTOSHIP_STATUS_PAGE_LOC = By.xpath("//div[@class='alert alert-info alert-dismissable']"); 
	private static final By CRP_CURRENT_STATUS_LOC = By.xpath("//div[contains(text(),'Current CRP Status')]/following-sibling::div[1]");
	private final By CANCEL_MY_CRP_LOC = By.id("cancelCRPStatus");

	private String socialMediaIconLoc = "//div[@class='container']//a[contains(@href,'%s')]";

	/***
	 * This method click on learn more about pulse link.
	 * 
	 * @param 
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipStatusPage clickLearnMoreAboutPulse(){
		driver.click(LEARN_MORE_ABOUT_PULSE_LOC);
		logger.info("clicked on 'learn more about pulse' link");
		return this;
	}

	/***
	 * This method validates overlay and its content displayed when clicked on learn more about pulse link
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isPulsePopupPresentWithContent(){
		return driver.isElementPresent(PULSE_POPUP_LOC);

	}

	/***
	 * This method close pulse overlay
	 * 
	 * @param
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipStatusPage closePulsePopup(){
		driver.click(CLOSE_ICON_PULSE_OVERLAY_LOC);
		logger.info("clicked X of 'pulse overlay'");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method validates PC Autoship status page
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isPCPerksAutoshipStatusPagePresent(){
		return driver.isElementVisible(PC_PERKS_AUTOSHIP_STATUS_LOC);
	}
	/***
	 * This method close pulse overlay
	 * 
	 * @param
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipStatusPage delayOrCancelPCPerks(){
		driver.click(DELAY_OR_CANCEL_PC_PERKS_LOC);
		logger.info("clicked 'Delay or cancel PC Perks'");
		driver.pauseExecutionFor(2000);
		return this;
	}
	/***
	 * This method validates Delay or cancel pc perks popup
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isDelayOrCancelPCPerksPopupPresent(){
		return driver.isElementVisible(DELAY_OR_CANCEL_PC_PERKS_POPUP_LOC);
	}
	/***
	 * This method clicks on cancel on delay or cancel popup to cancel pc perks.
	 * 
	 * @param
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipStatusPage selectCancelPCPerksOnPopup(){
		driver.click(CANCEL_OPTION_ON_CANCEL_PC_PERKS_POPUP_LOC);
		logger.info("clicked 'Cancel PC perks' on delay or cancel popup.");
		driver.pauseExecutionFor(2000);
		return this;
	}
	/***
	 * This method fill all required details and click send an email to cancel account
	 * 
	 * @param
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipStatusPage fillTheEntriesAndClickOnSubmitDuringTermination(){
		driver.click(REASON_DD_LOC);
		driver.click(REASON_DD_VALUE_OTHER_LOC);
		driver.type(MESSAGE_BOX_LOC, "I want to terminate my account");
		driver.clickByJS(RFWebsiteDriver.driver, driver.findElement(SEND_EMAIL_TO_CANCEL_ACCOUNT_BUTTON_LOC));
		driver.waitForLoadingImageToDisappear(); 
		return this;
	}
	/***
	 * This method validates account termination confirmation popup
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean verifyAccountTerminationIsConfirmedPopup(){
		if(driver.isElementVisible(ACCOUNT_TERMINATION_CONFIRMATION_POPUP_LOC)){
			return true;
		}else{
			return false;
		}
	}

	/***
	 * TODO
	 */
	public void clickOnConfirmTerminationPopup(){
		driver.quickWaitForElementPresent(POPUP_CONFIRM_TERMINATION_BUTTON);
		driver.click(POPUP_CONFIRM_TERMINATION_BUTTON);
		logger.info("Confirm popup clicked");
		driver.waitForPageLoad();
	}

	/***
	 * This method get next bill and ship date fron autship status page.
	 * 
	 * @param
	 * @return String nextShipDate.
	 * 
	 */
	public String getNextBillAndShipDateFromAutoship(){
		String nextBillShipDate = null;
		if(driver.isElementVisible(NEXT_BILL_SHIP_DATE_LOC)){
			nextBillShipDate=driver.findElement(NEXT_BILL_SHIP_DATE_LOC).getText();
			logger.info("Next bill and ship date "+nextBillShipDate);
			return nextBillShipDate;
		}else{
			logger.info("No next bill and ship date present for user.");
			return nextBillShipDate;
		}
	}
	/***
	 * This method convert and delay UI next bill ship date to enter in textbox 
	 * and delay by month mention in argument.
	 * @param
	 * @return String nextShipDate.
	 * 
	 */
	public String delayedNextBillShipDateInUIFormat(String updatedNextBillShipdate){
		String completeDate[] = updatedNextBillShipdate.split("\\-");
		String day =completeDate[2];
		String year =completeDate[0];
		String month=completeDate[1];
		int a = 0;
		int b = 0;
		String UIMonth = null;
		switch (Integer.parseInt(month)) {  
		case 1:
			UIMonth="Jan";
			break;
		case 2:
			UIMonth="Feb";
			break;
		case 3:
			UIMonth="Mar";
			break;
		case 4:
			UIMonth="Apr";
			break;
		case 5:
			UIMonth="May";
			break;
		case 6:
			UIMonth="Jun";
			break;
		case 7:
			UIMonth="Jul";
			break;
		case 8:
			UIMonth="Aug";
			break;
		case 9:
			UIMonth="Sep";
			break;
		case 10:
			UIMonth="Oct";
			break;
		case 11:
			UIMonth="Nov";
			break;
		case 12:
			UIMonth="Dec";
			break;  
		}
		String UIFormatDate=UIMonth+" "+day+","+" "+year;
		logger.info("UI format created date is "+UIFormatDate);
		return UIFormatDate;
	}
	/***
	 * This method convert and delay UI next bill ship date to enter in textbox 
	 * and delay by month mention in argument.
	 * @param
	 * @return String nextShipDate.
	 * 
	 */
	public String delayedNextBillShipDate(String currentBillShipDate, String delayedByMonth){
		String completeDate[] = currentBillShipDate.split(" ");
		String []splittedDay =completeDate[1].split("\\,");
		String day =splittedDay[0];
		String year =completeDate[2];
		String month=completeDate[0];
		int a = 0;
		int b = 0;
		String UIMonth = null;
		String selectedMonth = null;
		if(month.equalsIgnoreCase("Jan")){
			a=1;
		}else if(month.equalsIgnoreCase("Feb")){
			a=2;
		}else if(month.equalsIgnoreCase("Mar")){
			a=3;
		}
		else if(month.equalsIgnoreCase("Apr")){
			a=4;
		}
		else if(month.equalsIgnoreCase("May")){
			a=5;
		}
		else if(month.equalsIgnoreCase("Jun")){
			a=6;
		}
		else if(month.equalsIgnoreCase("Jul")){
			a=7;
		}
		else if(month.equalsIgnoreCase("Aug")){
			a=8;
		}
		else if(month.equalsIgnoreCase("Sep")){
			a=9;
		}
		else if(month.equalsIgnoreCase("Oct")){
			a=10;
		}
		else if(month.equalsIgnoreCase("Nov")){
			a=11;
		}else if(month.equalsIgnoreCase("Dec")){
			a=12;
		}else{
			a=13;
		}
		if(a==13){
			a=1;
			b=1;
		}
		switch (a) {  
		case 1:
			UIMonth="01";
			break;
		case 2:
			UIMonth="02";
			break;
		case 3:
			UIMonth="03";
			break;
		case 4:
			UIMonth="04";
			break;
		case 5:
			UIMonth="05";
			break;
		case 6:
			UIMonth="06";
			break;
		case 7:
			UIMonth="07";
			break;
		case 8:
			UIMonth="08";
			break;
		case 9:
			UIMonth="09";
			break;
		case 10:
			UIMonth="10";
			break;
		case 11:
			UIMonth="11";
			break;
		case 12:
			UIMonth="12";
			break;  
		}
		if(b==1){
			int yearly=Integer.parseInt(year)+1;
			year=Integer.toString(yearly);
		}
		selectedMonth = Integer.toString(Integer.parseInt(UIMonth)+Integer.parseInt(delayedByMonth));
		if(selectedMonth.length()==1){
			selectedMonth = "0"+selectedMonth;	
		}
		if(Integer.parseInt(selectedMonth)>12){
			selectedMonth=Integer.toString(Integer.parseInt(selectedMonth)-Integer.parseInt(UIMonth));
			if(selectedMonth.length()==1){
				selectedMonth = "0"+selectedMonth;	
			}	
			int yearly=Integer.parseInt(year)+1;
			year=Integer.toString(yearly);
		}
		String dateAfterOneMonth=year+"-"+selectedMonth+"-"+day;
		logger.info("created date is "+dateAfterOneMonth);
		return dateAfterOneMonth;
	}

	/***
	 * This method clicks on delay on delay or cancel popup to delay pc perks.
	 * 
	 * @param
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipStatusPage selectDelayPCPerksOnPopup(){
		driver.click(DELAY_OPTION_ON_CANCEL_PC_PERKS_POPUP_LOC);
		logger.info("clicked 'Cancel PC perks' on delay or cancel popup.");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method fill next bill and ship date for autoship.
	 * 
	 * @param
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipStatusPage fillNextBillAndShipdate(String nextBillShipDate){
		driver.type(NEXT_BILL_SHIP_DATE_TEXTBOX_LOC,nextBillShipDate);
		logger.info("Next bill and ship date entered as "+nextBillShipDate);
		driver.waitForLoadingImageToDisappear(); 
		return this;
	}

	/***
	 * This method click submit button after entering next bill ship date on
	 * autoship status page
	 * @param
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipStatusPage selectSubmitQueryButton(){
		driver.click(SUBMIT_QUERY_BUTTON);
		logger.info("Submit query button clicked after entering next bill ship date.");
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method click on view details link on
	 * autoship status page
	 * @param
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipCartPage viewDetailsOfAutoship(){
		driver.click(VIEW_DETAILS_LINK_AUTOSHIP_STATUS_LOC);
		logger.info("view details link clicked on autoship status page");
		driver.waitForPageLoad();
		return new StoreFrontAutoshipCartPage(driver);
	}

	/***
	 * This method validates next Bill ship date on autoship status page
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isNextAutoshipBillShipDatePresent(){
		return driver.isElementVisible(NEXT_BILL_SHIP_DATE_LOC);
	}

	/***
	 * This method get current PC perks status from autoship status page.
	 * 
	 * @param
	 * @return String PC perks status.
	 * 
	 */
	public String getCurrentPCPerksStatusFromAutoshipStatusPage(){
		String pcPerksStatus = null;
		if(driver.isElementVisible(PC_PERKS_STATUS_ON_AUTOSHIP_STATUS_PAGE)){
			pcPerksStatus=driver.findElement(PC_PERKS_STATUS_ON_AUTOSHIP_STATUS_PAGE).getText();
			logger.info("PC Perks status on autoship Status page "+pcPerksStatus);
			return pcPerksStatus;
		}else{
			logger.info("No PC Perks Status present for user on autoship Status page.");
			return pcPerksStatus;
		}
	}

	/***
	 * This method clicks on the subscribe to pulse button
	 * @return
	 */
	public StoreFrontAutoshipStatusPage clickSubscribeToPulseBtn(){
		driver.click(SUBSCRIBE_TO_PULSE_BTN_LOC);
		logger.info("Subscribe to pulse btn clicked");
		return this;
	}

	/***
	 * This method verifies whether subscribe to pulse button
	 * is dislpayed or NOT
	 * @return
	 */
	public boolean isSubscribeToPulseBtnDisplayed(){
		return driver.isElementVisible(SUBSCRIBE_TO_PULSE_BTN_LOC);
	}

	/***
	 * This method clicks on the cancel pulse subscription btn
	 */
	public StoreFrontAutoshipStatusPage clickCancelPulseSubscription(){
		driver.click(CANCEL_PULSE_SUBSCRIPTION_BTN_LOC);
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method click on Enroll in CRP  Button on 
	 * autoship status page
	 * @param
	 * @return
	 * 
	 */
	public void clickEnrollInCRPButton(){
		driver.click(ENROLL_IN_CRP_BTN_LOC);
		logger.info("Enroll in CRP clicked on autoship status page");
	}

	/***
	 * This method click on cancel CRP Link on 
	 * autoship status page
	 * @param
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipCartPage clickCancelCRPLink(){
		driver.click(CANCEL_MY_CRP_LINK_LOC);
		logger.info("Cancel my CRP link clicked on autoship status page");
		return new StoreFrontAutoshipCartPage(driver);
	}

	/***
	 * This method click on confirm cancel CRP button on 
	 * autoship status page
	 * @param
	 * @return store front autoship status page object
	 * 
	 */
	public StoreFrontAutoshipCartPage clickCancelCRPButton(){
		driver.click(CANCEL_CRP_BUTTON_LOC);
		logger.info("Cancel CRP button clicked on autoship status page");
		return new StoreFrontAutoshipCartPage(driver);
	}

	/***
	 * This method get the success message for actions performed
	 *
	 * @param
	 * @return String
	 * 
	 */
	public String getActionSucccessMsgOnAutoshipStatusPage(){
		return driver.getText(ACTION_SUCCESS_MSG_ON_AUTOSHIP_STATUS_PAGE_LOC);
	}

	/***
	 * This method get the current crp status
	 *
	 * @param
	 * @return String
	 * 
	 */
	public String getCurrentCRPStatus(){
		return driver.getText(CRP_CURRENT_STATUS_LOC).trim();
	}

	/***
	 * This method validates the presence of Enroll into CRP button
	 *
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isEnrollIntoCRPButtonPresent(){
		return driver.isElementVisible(ENROLL_IN_CRP_BTN_LOC);
	}

	/***
	 * This method validates next Bill ship date on autoship status page
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isCancelMyCrpLinkVisible(){
		return driver.isElementVisible(CANCEL_MY_CRP_LOC);
	}
}