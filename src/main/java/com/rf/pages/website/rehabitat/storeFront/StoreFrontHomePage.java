package com.rf.pages.website.rehabitat.storeFront;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

public class StoreFrontHomePage extends StoreFrontWebsiteBasePage{

	public StoreFrontHomePage(RFWebsiteDriver driver) {
		super(driver);		
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontHomePage.class.getName());

	private final By ENROLL_NOW_BUTTON_LOC = By.xpath("//a[text()='Enroll Now']");
	private final By FIRST_EVENT_CALENDAR_LOC = By.xpath("//h3[contains(text(),'Presentations')]/following::a[text()='EVENT CALENDER'][1]");
	private final By VISIT_THE_BLOG_LOC = By.xpath("//a[text()='VISIT THE BLOG']");
	private final By DIRECT_LINK_ASSOCIATION = By.xpath("//div[@class='content']//a[contains(text(),'Direct Selling Association')]");
	private final By DSA_CODE_OF_ETHICS_LINK = By.xpath("//a[contains(text(),'DSA Code of Ethics')]");
	private final By DONATE_NOW_BUTTON_LOC = By.xpath("//a[contains(text(),'Donate Now')]");
	private final By SEARCH_BOX = By.id("search-box");
	private final By SEARCH_ICON_NEAR_SEARCH_BOX = By.xpath("//input[@id='search-box']/preceding::span[1]");
	private final By ERROR_MSG_TEXT_LOC = By.xpath("//div[@class='content']//h2");
	private final By LOGIN_OR_REGISTER_TXT_LOC = By.xpath("//h1[contains(text(),'LOG IN OR REGISTER')]");
	private final By INCORRECT_USERNAME_PASSOWRD_TXT_LOC = By.xpath("//div[contains(@class,'alert-danger') and contains(text(),'') or contains(text(),'Your username or password was incorrect.')]");
	private final By MEET_THE_DOCTORS_TXT_LOC = By.xpath("//h1[text()=' Meet the Doctors']");
	private final By MEET_RF_EXECUTIVE_TEAM_LINK_LOC = By.xpath("//a[@href='/executive-team']");
	private final By TOTAL_TEAM_MEMBERS_IN_EXECUTIVE_TEAM_LOC = By.xpath("//div[@id='modal_front']//div[@class='title']");
	private final By TEAM_MEMBER_NAME_FROM_POPUP_LOC = By.xpath("//div[@id='executive-team-carousel']/descendant::h4[1]");
	private final By CLOSE_ICON_MEMBER_DETAIL_POPUP_LOC = By.xpath("//button[@class='close']");
		
	private String socialMediaIconLoc = "//div[@class='container']//a[contains(@href,'%s')]";
	private String teamMemberName = "//div[@id='modal_front']/div[%s]//div[@class='title']/h4";

	public boolean isFindAConsultantPagePresent(){
		String findAConsultantURL = "/find-consultant";
		return driver.isElementPresent(SPONSOR_SEARCH_FIELD_LOC)&& driver.getCurrentUrl().contains(findAConsultantURL);
	}

	/***
	 * This method click on enroll now button
	 * 
	 * @param 
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontWebsiteBasePage clickEnrollNowButton(){
		driver.click(ENROLL_NOW_BUTTON_LOC);
		logger.info("clicked on 'Enroll now button'");
		return this;
	}

	/***
	 * This method click on first event calendar button
	 * 
	 * @param 
	 * @return store front website base page object
	 * 
	 */
	public StoreFrontHomePage clickFirstEventCalendar(){
		driver.click(FIRST_EVENT_CALENDAR_LOC);
		logger.info("clicked on 'Event calendar button'");
		return this;
	}

	/***
	 * This method click on visit the block button
	 * 
	 * @param
	 * @return store front home page object
	 * 
	 */
	public StoreFrontHomePage clickVisitTheBlock(){
		driver.click(VISIT_THE_BLOG_LOC);
		logger.info("clicked on 'VISIT THE BLOG' button");
		return this;
	}

	/***
	 * This method click on social media icon
	 * 
	 * @param social media type
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage clickSocialMediaIcon(String mediaType){
		driver.click(By.xpath(String.format(socialMediaIconLoc, mediaType)));
		logger.info("clicked on"+mediaType+" icon");
		return this;
	}

	/***
	 * This method click on direct link association link
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage clickDirectLinkAssociationLink(){
		driver.click(DIRECT_LINK_ASSOCIATION);
		logger.info("clicked on 'direct link association' link");
		return this;
	}

	/***
	 * This method click on DSA code of ethics link
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage clickDsaCodeOfEthicsLink(){
		driver.click(DSA_CODE_OF_ETHICS_LINK);
		logger.info("clicked on 'DSA code of ethics' link");
		return this;
	}

	/***
	 * This method click on Donate now button link
	 * 
	 * @param
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage clickDonateNow(){
		driver.click(DONATE_NOW_BUTTON_LOC);
		logger.info("clicked on 'Donate now' button");
		return this;
	}

	/***
	 * This method search a product through search Icon
	 * 
	 * @param product name
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage searchProduct(String productName){
		driver.type(SEARCH_BOX, productName);
		driver.click(SEARCH_ICON_NEAR_SEARCH_BOX);
		logger.info("clicked on 'Search icon' for search a product");
		return this;
	}

	/***
	 * This method get error message for unable to found product
	 * 
	 * @param
	 * @return error message
	 * 
	 */
	public String getErrorMessageForSearchedProduct(){
		String errorMessage = driver.findElement(ERROR_MSG_TEXT_LOC).getText(); 
		logger.info("error is: "+errorMessage);
		return errorMessage;
	}

	/***
	 * This method verifies whether 'login or Register' page has displayed or not
	 * by checking the visibility of the text 'login or Register'
	 * 
	 * @return boolean
	 */
	public boolean isLoginOrRegisterPageDisplayed(){
		boolean isLoginOrRegisterPageDisplayed = driver.IsElementVisible(driver.findElement(LOGIN_OR_REGISTER_TXT_LOC));
		logger.info("is Login Or Register Page Displayed = "+isLoginOrRegisterPageDisplayed);
		return isLoginOrRegisterPageDisplayed;
	}

	/***
	 * This method verifies whether the Error Message For Incorrect Username Or Password
	 * 'Your username or password was incorrect.' has Displayed or not
	 * 
	 * @return boolean
	 */
	public boolean isErrorMessageForIncorrectUsernamePasswordDisplayed(){
		boolean isErrorMessageForIncorrectUsernamePasswordDisplayed = driver.IsElementVisible(driver.findElement(INCORRECT_USERNAME_PASSOWRD_TXT_LOC));
		logger.info("is Error Message For Incorrect Username Password Displayed = "+isErrorMessageForIncorrectUsernamePasswordDisplayed);
		return isErrorMessageForIncorrectUsernamePasswordDisplayed;
	}

	/***
	 * This method verify meet the doctors Page.
	 * 
	 * @param
	 * @return boolean value.
	 * 
	 */
	public boolean isMeetTheDoctorsPagePresent(){
		String meetTheDoctorsURL = "meet-the-doctors";
		return driver.isElementPresent(MEET_THE_DOCTORS_TXT_LOC)&& driver.getCurrentUrl().contains(meetTheDoctorsURL);
	}

	/***
	 * This method will click meet the "RF Executive team" link on "Meet the doctors" page.
	 * 
	 * @param 
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage clickMeetRFExecutiveTeamLinkOnMeetTheDoctorPage(){
		driver.click(MEET_RF_EXECUTIVE_TEAM_LINK_LOC);
		logger.info("clicked on 'Meet the R+F executive team' link on 'Meet the doctors' page");
		driver.waitForPageLoad();
		return this;
	}

	/***
	 * This method click on random team member name link on executive team page.
	 * 
	 * @param
	 * @return random selected team member name.
	 * 
	 */
	public String clickAndReturnTeamMemberName(){
		int totalTeamMember = driver.findElements(TOTAL_TEAM_MEMBERS_IN_EXECUTIVE_TEAM_LOC).size();
		int randomNum = CommonUtils.getRandomNum(1, totalTeamMember);
		logger.info("Random team member selected is "+randomNum);
		String memberName = driver.findElement(By.xpath(String.format(teamMemberName, randomNum))).getText();
		driver.click(By.xpath(String.format(teamMemberName, randomNum)));
		logger.info("clicked on 'TEAM MEMBER NAME' link = "+memberName);
		return memberName;
	}

	/***
	 * This method get team member name from popup on executive team page.
	 * 
	 * @param
	 * @return team member name.
	 * 
	 */
	public String getTeamMemberNameFromPopup(){
		String memberName = driver.findElement(TEAM_MEMBER_NAME_FROM_POPUP_LOC).getText();
		logger.info("Fetched 'TEAM MEMBER NAME' from popup == "+memberName);
		return memberName;
	}

	/***
	 * This method will close team member detail popup on executive team page.
	 * 
	 * @param 
	 * @return store front Home page object
	 * 
	 */
	public StoreFrontHomePage closeMemberDetailsPopup(){
		driver.click(CLOSE_ICON_MEMBER_DETAIL_POPUP_LOC);
		logger.info("clicked on close icon of member detail popup");
		return this;
	}
	
}