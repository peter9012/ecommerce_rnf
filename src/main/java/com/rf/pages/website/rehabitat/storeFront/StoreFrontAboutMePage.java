package com.rf.pages.website.rehabitat.storeFront;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;

public class StoreFrontAboutMePage extends StoreFrontWebsiteBasePage{

	public StoreFrontAboutMePage(RFWebsiteDriver driver) {
		super(driver);  
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontAboutMePage.class.getName());

	private final By DISABLED_SEND_BUTTON_LOC = By.xpath("//button[@id='emailToConsultantSubmitButton' and @disabled='']");
	private final By PERSONALIZE_MY_PROFILE_LOC = By.xpath("//a[contains(text(),'PERSONALIZE MY PROFILE')]");
	private final By CONTACT_ME_HEADER_LOC = By.xpath("//h3[contains(text(),'Contact Me')]");
	private final By SUBMISSION_GUIDELINES_LOC=By.xpath("//a[contains(text(),'Submission Guidelines')]");
	private final By INSTAGRAM_LINK_ON_PAGE_HEADER_LOC=By.xpath("//div[@class='socialMediaUrls']/a[contains(@class,'instagram')]"); 
	private final By CITY_LOC=By.xpath("//input[contains(@class,'cityField')]");
	private final By STATE_LOC=By.id("contactDetailsData.state");
	private final By Checkbox_SHOW_SINCE_LOC=By.xpath("//input[@id='showSince']");
	private final By LABEL_SHOW_SINCE_LOC=By.xpath("//label[contains(text(),'Consultant Since')]");
	private final By SHOW_SINCE_HEADER_LOC=By.xpath("//div[@class='contactInfo']/p[3]");
	private final By CITY_STATE_HEADER_LOC=By.xpath("//div[@class='contactInfo']/p[2]");
	private final By PHONE_NUMBER_LOC=By.xpath("//input[contains(@class,'phoneField')]");
	private final By EMAIL_LOC=By.xpath("//input[contains(@class,'emailField')]");
	private final By PHONE_NUMBER_ON_PAGE_HEADER_LOC=By.xpath("//a[contains(@href,'tel')]");
	private final By EMAIL_ON_PAGE_HEADER_LOC=By.xpath("//a[contains(@href,'mailto')]");
	private final By PINTEREST_ON_ABOUT_ME_PAGE_HEADER=By.xpath("//div[@class='socialMediaUrls']/a[contains(@class,'pinterest')]");
	private final By TWITTER_ON_ABOUT_ME_PAGE_HEADER=By.xpath("//div[@class='socialMediaUrls']/a[contains(@class,'twitter')]");
	private final By FACEBOOK_ON_ABOUT_ME_PAGE_HEADER=By.xpath("//div[@class='socialMediaUrls']/a[contains(@class,'facebook')]");
	private final By FACEBOOK_LOC=By.id("faceboolUrl");
	private final By TWITTER_LOC=By.id("twiiterUrl");
	private final By PINTEREST_LOC=By.id("pinterestUrl");
	private final By INSTAGRAM_LOC=By.id("instagramUrl");

	private String quesOnAboutMePageLoc = "//form[@id='aboutmeFormData']//h2[contains(text(),'%s')]";
	private String answerOnAboutMePageLoc = "//form[@id='aboutmeFormData']//h2[contains(text(),'%s')]/following-sibling::p";

	/***
	 * This method click Personalize my profile button
	 * 
	 * @param
	 * @return Store Front About Me Page
	 * 
	 */
	public StoreFrontAboutMePage clickPersonalizeMyProfileButton(){
		driver.click(PERSONALIZE_MY_PROFILE_LOC);
		logger.info("Personalize my profile btn clicked");
		return this;
	}

	/***
	 * This method validates the presence of contact me header on about me page
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isContactMeHeaderPresentOnAboutMePage(){
		return driver.isElementVisible(CONTACT_ME_HEADER_LOC);

	}

	/***
	 * This method validates the presence of 'What I love most about Rodan and Fields' ques on about me page
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isExpectedQuesPresentOnAboutMePage(String ques){
		return driver.isElementVisible(By.xpath(String.format(quesOnAboutMePageLoc,ques)));

	}

	/***
	 * This method validates the presence of answer of 'What I love most about Rodan and Fields' ques on about me page
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isAnswerOfExpectedQuesPresentOnAboutMePage(String ques){
		return driver.isElementVisible(By.xpath(String.format(answerOnAboutMePageLoc,ques)));

	}

	/***
	 * This method validates about me page present.
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isAboutMePagePresent(){
		String aboutMePage ="about-me";
		return (driver.isElementVisible(PERSONALIZE_MY_PROFILE_LOC)&& driver.getCurrentUrl().toLowerCase().contains(aboutMePage.toLowerCase()));

	}

	/***
	 * This method verify user clicked on personalize my profile button or not
	 * 
	 * @param 
	 * @return boolean value
	 * 
	 */ 
	public boolean isSubmissionGuidelinesDisplayed(){
		return driver.isElementVisible(SUBMISSION_GUIDELINES_LOC);
	}

	/***
	 * This method verify user clicked on personalize my profile button or not
	 * 
	 * @param 
	 * @return boolean value
	 * 
	 */ 
	public StoreFrontAboutMePage clickSubmissionGuidelinesLink(){
		driver.click(SUBMISSION_GUIDELINES_LOC);
		return this;
	}

	/***
	 * This method type city on about me page
	 * 
	 * @param 
	 * @return StoreFrontAboutMePage obj
	 * 
	 */ 
	public StoreFrontAboutMePage typeCityOnAboutMePage(String city){
		driver.type(CITY_LOC, city);
		logger.info("city is typed: "+city);
		return this;
	}

	/***
	 * This method select province on about me page
	 * 
	 * @param 
	 * @return StoreFrontAboutMePage obj
	 * 
	 */ 
	public StoreFrontAboutMePage selectProvinceOnAboutMePage(String visibleStateName){
		Select state=driver.getSelect(STATE_LOC);
		state.selectByVisibleText(visibleStateName);
		logger.info("state is selected with visible text:"+visibleStateName);
		return this;
	}

	/***
	 * This method verify city and state displayed on about me page header
	 * 
	 * @param 
	 * @return boolean
	 * 
	 */ 
	public String getCityStateTextOnAboutMePageHeader(){
		return driver.getText(CITY_STATE_HEADER_LOC);
	}

	/***
	 * This method select checkbox 'Show since' on about me page header
	 * 
	 * @param 
	 * @return label show since text
	 * 
	 */ 
	public String selectCheckboxShowSinceAndReturnLabelName(){
		if(driver.findElement(Checkbox_SHOW_SINCE_LOC).getAttribute("value")=="true"){
			logger.info("checkbox already selected");
			return driver.getText(LABEL_SHOW_SINCE_LOC);
		}
		else{
			driver.click(LABEL_SHOW_SINCE_LOC);
			logger.info("checkbox show since is selected");
		}
		return driver.getText(LABEL_SHOW_SINCE_LOC);
	}

	/***
	 * This method verify label for consultant show since is editable or not 
	 * 
	 * @param 
	 * @return boolean value
	 * 
	 */ 
	public boolean isConsultantSinceEditable(){
		return driver.isElementPresent(LABEL_SHOW_SINCE_LOC);
	}

	/***
	 * This method get show since text from about me page header
	 * 
	 * @param 
	 * @return show since text
	 * 
	 */ 
	public String getShowSinceTextOnAboutMePageHeader(){
		driver.pauseExecutionFor(3000);
		return driver.getText(SHOW_SINCE_HEADER_LOC);
	}

	/***
	 * This method unselect show since checkbox
	 * 
	 * @param 
	 * @return StoreFrontAboutMePage obj
	 * 
	 */ 
	public StoreFrontAboutMePage deSelectCheckboxShowSince(){
		driver.click(LABEL_SHOW_SINCE_LOC);
		logger.info("checkbox show since is deselected");
		return this;
	}

	/***
	 * This method type phone number and email on about me page
	 * 
	 * @param phone number, email
	 * @return StoreFrontAboutMePage obj
	 * 
	 */ 
	public StoreFrontAboutMePage typePhoneAndEmailOnAboutMePage(String phoneNo, String email){
		driver.type(PHONE_NUMBER_LOC, phoneNo);
		logger.info("phone number is entered: "+phoneNo);
		driver.type(EMAIL_LOC, email);
		logger.info("email is entered: "+email);
		return this;
	}

	/***
	 * This method get phone number from about me page header
	 * 
	 * @param 
	 * @return phone number
	 * 
	 */ 
	public String getPhoneNumberFromAboutMePageHeader(){
		return driver.getText(PHONE_NUMBER_ON_PAGE_HEADER_LOC);
	}

	/***
	 * This method get email from about me page header
	 * 
	 * @param 
	 * @return email
	 * 
	 */
	public String getEmailFromAboutMePageHeader(){
		return driver.getText(EMAIL_ON_PAGE_HEADER_LOC);
	}


	/***
	 * This method verify instagram link displayed on about me page
	 * 
	 * @param 
	 * @return boolean
	 * 
	 */ 
	public StoreFrontAboutMePage clearContactAndSocialMediaFields(){
		driver.clear(PHONE_NUMBER_LOC);
		logger.info("Phone number field cleared");
		driver.clear(EMAIL_LOC);
		logger.info("Email field cleared");
		driver.clear(FACEBOOK_LOC);
		logger.info("Facebook field cleared");
		driver.clear(TWITTER_LOC);
		logger.info("Twitter field cleared");
		driver.clear(PINTEREST_LOC);
		logger.info("Pinterest field cleared");
		driver.clear(INSTAGRAM_LOC);
		logger.info("Instagram field cleared");
		return this;
	}

	/***
	 * This method verify contact and social media link displayed on about me page or not
	 * 
	 * @param 
	 * @return boolean
	 * 
	 */ 
	public boolean isContactOrSocialMediaDetailsPresentOnAboutMePage(){
		return driver.isElementVisible(PHONE_NUMBER_ON_PAGE_HEADER_LOC) ||  driver.isElementVisible(EMAIL_ON_PAGE_HEADER_LOC) || driver.isElementVisible(INSTAGRAM_LINK_ON_PAGE_HEADER_LOC) || driver.isElementVisible(PINTEREST_ON_ABOUT_ME_PAGE_HEADER) || driver.isElementVisible(TWITTER_ON_ABOUT_ME_PAGE_HEADER) || driver.isElementVisible(FACEBOOK_ON_ABOUT_ME_PAGE_HEADER);
	}


	/***
	 * This method enter details of social media fields on about me page
	 * 
	 * @param 
	 * @return StoreFrontAboutMePage obj
	 * 
	 */ 
	public StoreFrontAboutMePage enterSocialMediaFieldsOnAboutMePage(String fbId, String twitterID, String pinterestId){
		driver.type(FACEBOOK_LOC, fbId);
		logger.info("facebook id entered "+fbId);
		driver.type(TWITTER_LOC, twitterID);
		logger.info("Twitter id entered "+twitterID);
		driver.type(PINTEREST_LOC, pinterestId);
		logger.info("Pinterest id entered "+pinterestId);
		driver.clear(INSTAGRAM_LOC);
		logger.info("Instagram field cleared");
		return this;
	}

	/***
	 * This method verify instagram link displayed on about me page
	 * 
	 * @param 
	 * @return boolean
	 * 
	 */ 
	public boolean isInstagramLinkDisplayedOnAboutMePage(){
		return driver.isElementVisible(INSTAGRAM_LINK_ON_PAGE_HEADER_LOC);
	}

	/***
	 * This method verify show since consultant present on about me page header
	 * 
	 * @param 
	 * @return label show since text
	 * 
	 */ 
	public boolean isShowSincePresentOnAboutMePageHeader(){
		return driver.isElementPresent(SHOW_SINCE_HEADER_LOC);
	}

	/***
	 * This method validates about me page present.
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isSendButtonDisabled(){
		return driver.isElementVisible(DISABLED_SEND_BUTTON_LOC);
	}
}