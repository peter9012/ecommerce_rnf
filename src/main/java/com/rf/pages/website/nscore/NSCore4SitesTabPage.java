package com.rf.pages.website.nscore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;

public class NSCore4SitesTabPage extends NSCore4RFWebsiteBasePage{

	private static final Logger logger = LogManager
			.getLogger(NSCore4SitesTabPage.class.getName());

	public NSCore4SitesTabPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	//private static String pwsContentReviewLink = "//div[contains(@class,'nsCorporate')]/div/ul/li[1]//li/a/span[text()='PWS Content Review']";
	private static String waitingForApprovedLink = "//div[@class='ContentBox']/div/div/p[text()='%s']/../../following-sibling::p/a[text()='Approve']";
	private static String approvedDisappearLink = "//div[@class='ContentBox' and @style='display: none;']/div/div/p[text()='%s']/../../following-sibling::p/a[text()='Approve']";
	private static String denyLinkForStory = "//div[@class='ContentBox']/div/div/p[text()='%s']/../../following-sibling::p/a[text()='Deny']";
	private static String eventNameAtCalendar = "//div[@id='calendar']//span[text()='%s']";
	private static String corporateSubLink = "//h3[contains(text(),'nsCorporate')]/following::li[1]//span[text()='%s']";
	private static String pageSizeDDoption = "//select[@id='pageSize']/option[%s]";
	private static String titleNameLinkLoc = "//a[text()='%s']";
	private static String titleNameChkBox = "//a[text()='%s']/preceding::input[1]";
	private static String titleStatus = "//a[text()='%s']/following::td[3]";

	private static final By CORPORATE_LINK_LOC  = By.xpath("//b[contains(text(),'Corporate')]");
	private static final By ADD_EVENT_LINK_LOC  = By.xpath("//a[text()='Add Event']");
	private static final By SUBJECT_TXT_BOX_LOC  = By.id("subject");
	private static final By EVENT_SAVE_BUTTON  = By.xpath("//a[@class='Button BigBlue']");
	private static final By EVENT_SAVED_SUCCESSFULLY_TXT_LOC = By.xpath("//div[@class='SectionHeader']/following::div[1]/div");
	private static final By EVENT_DELETE_BUTTON  = By.id("cmdDelete");
	private static final By ADD_NEWS_LINK_LOC  = By.xpath("//a[text()='Add News']");
	private static final By PAGE_SIZE_DD  = By.id("pageSize");
	private static final By PAGE_SIZE_DD_OPTIONS  = By.xpath("//select[@id='pageSize']/option");
	private static final By DEACTIVED_IS_ACTIVE_CHK_BOX_FOR_NEWS_TITLE  = By.xpath("//input[@id='isActive'][@value='False']");
	private static final By IS_ACTIVE_CHK_BOX_FOR_NEWS_TITLE  = By.xpath("//input[@id='isActive']/preceding::input[1]");
	private static final By DEACTIVATE_SELECTED_LINK  = By.id("btnDeactivate");
	private static final By ACTIVATE_SELECTED_LINK  = By.id("btnActivate");
	private static final By DELETE_SELECTED_LINK  = By.id("btnDelete");
	private static final By TITLE_TXT_BOX_LOC  = By.id("title");
	private static final By SITE_NAME_TXT_BOX  = By.id("siteName");
	private static final By DEACTIVED_CHK_BOX_FOR_SITE_DETAILS  = By.xpath("//input[@id='active'][@value='False']");
	private static final By ACTIVE_CHK_BOX_FOR_SITE_DETAILS  = By.xpath("//input[@id='active']/preceding::input[1]");
	private static final By SITE_SAVE_BUTTON  = By.id("btnSaveSite");
	private static final By SITE_DETAILS_SAVED_SUCCESSFULLY_TXT_LOC = By.xpath("//td[@class='CoreContent']/div[1]/div");
	private static final By REPLICATED_SITES_HEADER = By.xpath("//div[@class='SectionHeader']//h2[contains(text(),'Replicated Sites')]");
	private static final By PWS_CONTENT_REVIEW_LINK = By.xpath("//div[contains(@class,'nsCorporate')]/div/ul/li[1]//li/a/span[text()='PWS Content Review']");
	private static final By DENY_REASON_TXT_BOX = By.id("denyReason");
	private static final By SUBMIT_BTN_OF_DENY_REASON = By.id("submitDeny");

	public void clickPWSContentReviewLinkUnderNSCorporate(){
		driver.quickWaitForElementPresent(PWS_CONTENT_REVIEW_LINK);
		driver.click(PWS_CONTENT_REVIEW_LINK);
		logger.info("pws content review link clicked under nscorporate");
		driver.waitForPageLoad();
	}

	public boolean verifyNewStoryWaitingForApprovedLink(String story){
		driver.waitForElementPresent(By.xpath(String.format(waitingForApprovedLink, story)));
		return driver.isElementPresent(By.xpath(String.format(waitingForApprovedLink, story)));
	}

	public void clickApproveLinkForNewStory(String story){
		driver.quickWaitForElementPresent(By.xpath(String.format(waitingForApprovedLink, story)));
		driver.click(By.xpath(String.format(waitingForApprovedLink, story)));
		logger.info("Tab clicked is: "+story);
		driver.waitForPageLoad();
	}

	public boolean verifyApproveRequestDisappearFromUIOnceStoryApproved(String story){
		return driver.isElementPresent(By.xpath(String.format(approvedDisappearLink, story)));  
	}

	public void clickDenyLinkForNewStory(String story){
		driver.quickWaitForElementPresent(By.xpath(String.format(denyLinkForStory, story)));
		driver.click(By.xpath(String.format(denyLinkForStory, story)));
		logger.info("Tab clicked is: "+story);
		driver.waitForPageLoad();
	}

	public void enterDenyReasonAndClickSubmit(String denyReason) {
		driver.quickWaitForElementPresent(DENY_REASON_TXT_BOX);
		driver.type(DENY_REASON_TXT_BOX,denyReason);
		driver.click(SUBMIT_BTN_OF_DENY_REASON);
		logger.info("After entering deny story reason submit button clicked");
		driver.waitForPageLoad();
	} 

	
	public void clickCorporateLink(){
		driver.waitForElementPresent(CORPORATE_LINK_LOC);
		driver.click(CORPORATE_LINK_LOC);
		logger.info("Corporate link clicked on site page");
		driver.waitForPageLoad();
	}

	public void clickAddEventLink(){
		driver.waitForElementPresent(ADD_EVENT_LINK_LOC);
		driver.click(ADD_EVENT_LINK_LOC);
		logger.info("Add event link clicked on site page");
		driver.waitForPageLoad();
	}

	public void enterSubjectForEvent(String subject){
		driver.waitForElementPresent(SUBJECT_TXT_BOX_LOC);
		driver.type(SUBJECT_TXT_BOX_LOC, subject);
		logger.info("subject entered as: "+subject);
	}

	public void clickSaveBtn(){
		driver.waitForElementPresent(EVENT_SAVE_BUTTON);
		driver.click(EVENT_SAVE_BUTTON);
		logger.info("Save button clicked for an event");
		driver.waitForPageLoad();
	}

	public String getSavedSuccessfullyTxt(){
		driver.waitForElementPresent(EVENT_SAVED_SUCCESSFULLY_TXT_LOC);
		String savedMsg = driver.findElement(EVENT_SAVED_SUCCESSFULLY_TXT_LOC).getText();
		logger.info("Successfully saved message is: "+savedMsg);
		return savedMsg;
	}

	public boolean isEventPresentAtCalendar(String eventName){
		driver.quickWaitForElementPresent(By.xpath(String.format(eventNameAtCalendar, eventName)));
		return driver.isElementPresent(By.xpath(String.format(eventNameAtCalendar, eventName)));
	}

	public void clickEventNamePresentAtCalendar(String eventName){
		driver.quickWaitForElementPresent(By.xpath(String.format(eventNameAtCalendar, eventName)));
		driver.click(By.xpath(String.format(eventNameAtCalendar, eventName)));
		logger.info("Event name clicked i.e.: "+eventName);
		driver.waitForPageLoad();
	}

	public void clickDeleteBtnForEvent(){
		driver.waitForElementPresent(EVENT_DELETE_BUTTON);
		driver.click(EVENT_DELETE_BUTTON);
		logger.info("Delete button clicked for an event");
		driver.waitForPageLoad();
	}

	public void clickAddNewsLink(){
		driver.waitForElementPresent(ADD_NEWS_LINK_LOC);
		driver.click(ADD_NEWS_LINK_LOC);
		logger.info("Add news link clicked on site page");
		driver.waitForPageLoad();
	}

	public int getSizeOfOptinsFromPageSizeDD(){
		driver.waitForElementPresent(PAGE_SIZE_DD_OPTIONS);
		int noOfOptins = driver.findElements(PAGE_SIZE_DD_OPTIONS).size();
		logger.info("No of options are available in page size DD: "+noOfOptins);
		return noOfOptins;
	}

	public void clickAndSelectOptionInPageSizeDD(int optionNumber){
		driver.click(PAGE_SIZE_DD);
		driver.click(By.xpath(String.format(pageSizeDDoption, optionNumber)));
		logger.info("Page size DD option selected");
	}

	public boolean isTitleNamePresentInAnnouncementsList(String titleName){
		driver.waitForElementPresent(By.xpath(String.format(titleNameLinkLoc, titleName)));
		return driver.isElementPresent(By.xpath(String.format(titleNameLinkLoc, titleName)));
	}

	public void checkIsActiveChkBoxForNewsTitle(){
		driver.waitForElementPresent(DEACTIVED_IS_ACTIVE_CHK_BOX_FOR_NEWS_TITLE);
		if(driver.isElementPresent(DEACTIVED_IS_ACTIVE_CHK_BOX_FOR_NEWS_TITLE)==true){
			driver.click(IS_ACTIVE_CHK_BOX_FOR_NEWS_TITLE);
			logger.info("Is Active checkbox checked for site details");
		}else{
			logger.info("Is Active checkbox already checked for site details");
		}
	}

	public void clickTitleNamePresentInAnnouncementsList(String titleName){
		driver.waitForElementPresent(By.xpath(String.format(titleNameLinkLoc, titleName)));
		driver.click(By.xpath(String.format(titleNameLinkLoc, titleName)));
		logger.info("Title name i.e.: "+titleName+" clicked in announcements list");
	}


	public void checkTitleNameChkBoxInAnnouncementsList(String titleName){
		driver.waitForElementPresent(By.xpath(String.format(titleNameChkBox, titleName)));
		driver.click(By.xpath(String.format(titleNameChkBox, titleName)));
		logger.info("Check box checked for title name is: "+titleName);
	}

	public void clickDeactivateSelectedLink(){
		driver.click(DEACTIVATE_SELECTED_LINK);
		logger.info("Deactivate selected link clicked");
	}

	public String getTitleStatus(String titleName){
		driver.pauseExecutionFor(2000);
		driver.isElementPresent(By.xpath(String.format(titleStatus, titleName)));
		String status = driver.findElement(By.xpath(String.format(titleStatus, titleName))).getText();
		logger.info("title status is: "+status);
		return status;
	}

	public void clickActivateSelectedLink(){
		driver.click(ACTIVATE_SELECTED_LINK);
		logger.info("Activate selected link clicked");
	}

	public void clickDeleteSelectedLink(){
		driver.click(DELETE_SELECTED_LINK);
		logger.info("Delete selected link clicked");
	}

	public void enterTitleForAddNews(String title){
		driver.waitForElementPresent(TITLE_TXT_BOX_LOC);
		driver.type(TITLE_TXT_BOX_LOC, title);
		logger.info("title entered as: "+title);
	}

	public void clickSubLinkOfCorporate(String subLinkName){
		driver.quickWaitForElementPresent(By.xpath(String.format(corporateSubLink, subLinkName)));
		driver.click(By.xpath(String.format(corporateSubLink, subLinkName)));
		logger.info("Sublink clicked is: "+subLinkName);
		driver.waitForPageLoad();
	}


	public void enterSiteNameForSiteDetails(String siteName){
		driver.waitForElementPresent(SITE_NAME_TXT_BOX);
		driver.type(SITE_NAME_TXT_BOX, siteName);
		logger.info("Site name entered as: "+siteName);
	}

	public void checkActiveChkBoxForSiteDetails(){
		driver.waitForElementPresent(DEACTIVED_CHK_BOX_FOR_SITE_DETAILS);
		if(driver.isElementPresent(ACTIVE_CHK_BOX_FOR_SITE_DETAILS)==true){
			driver.click(ACTIVE_CHK_BOX_FOR_SITE_DETAILS);
			logger.info("Active checkbox checked for site details");
		}else{
			logger.info("Active checkbox already checked for site details");
		}
	}

	public void clickSaveBtnOnSiteDetails(){
		driver.waitForElementPresent(SITE_SAVE_BUTTON);
		driver.click(SITE_SAVE_BUTTON);
		logger.info("Save button clicked for site details");
		driver.waitForPageLoad();
	}

	public String getSavedSuccessfullyTxtForSite(){
		driver.waitForElementPresent(SITE_DETAILS_SAVED_SUCCESSFULLY_TXT_LOC);
		String savedMsg = driver.findElement(SITE_DETAILS_SAVED_SUCCESSFULLY_TXT_LOC).getText();
		logger.info("Successfully saved message is: "+savedMsg);
		return savedMsg;
	}

	public void uncheckActiveChkBoxForSiteDetails(){
		driver.waitForElementPresent(ACTIVE_CHK_BOX_FOR_SITE_DETAILS);
		driver.click(ACTIVE_CHK_BOX_FOR_SITE_DETAILS);
		logger.info("Active checkbox unchecked for site details");
	}

	public boolean isReplicatedSitesHeaderPresent(){
		driver.quickWaitForElementPresent(REPLICATED_SITES_HEADER);
		return driver.isElementPresent(REPLICATED_SITES_HEADER);
	}

}