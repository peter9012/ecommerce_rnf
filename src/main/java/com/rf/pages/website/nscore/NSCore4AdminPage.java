package com.rf.pages.website.nscore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;

public class NSCore4AdminPage extends NSCore4RFWebsiteBasePage{

	private static final Logger logger = LogManager
			.getLogger(NSCore4AdminPage.class.getName());

	public NSCore4AdminPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	private static String roleName = "//td[@class='CoreContent']/ul/li[%d]/a";
	private static final By LIST_TYPE_LINK_LOC = By.xpath("//span[text()='List Types']");
	private static final By ADD_NEW_LIST_VALUE_LOC = By.xpath("//a[@id='btnAdd']");
	private static final By TOTAL_LIST_VALUES_LOC = By.xpath("//ul[@id='listValues']/li");
	private static String newlyAddedValue = "//ul[@id='listValues']/li[%s]/input";
	private static String newAddedlistCancelMarkLoc = "//ul[@id='listValues']/li[%s]/a/img";
	private static String listTypeLoc = "//a[contains(text(),'%s')]";
	private static final By ROLES_LINK_LOC = By.xpath("//ul[@id='SubNav']//span[text()='Roles']");
	private static final By ADD_NEW_ROLE_LINK_LOC = By.xpath("//a[text()='Add new role']");
	private static final By ROLE_NAME_TXT_FIELD_LOC = By.xpath("//input[@id='name']");
	private static final By SAVE_BTN_LOC = By.xpath("//a[@id='btnSave']");
	private static final By ROLES_NAME_LIST_LOC = By.xpath("//td[@class='CoreContent']/ul/li");
	private static final By ADD_NEW_USER_LINK_LOC = By.xpath("//a[text()='Add new user']");
	private static final By NEW_USER_FIRST_NAME_FIELD_LOC = By.id("firstName");
	private static final By NEW_USER_LAST_NAME_FIELD_LOC = By.id("lastName");
	private static final By NEW_USER_USER_NAME_FIELD_LOC = By.id("username");
	private static final By NEW_USER_PASSWORD_FIELD_LOC = By.id("password"); 
	private static final By NEW_USER_CONFIRM_PASSWORD_FIELD_LOC = By.id("confirmPassword"); 
	private static final By NEW_USER_EMAIL_FIELD_LOC = By.id("email");
	private static final By NEW_USER_ROLES_ADMINISTRATOR_LOC = By.id("Role_4");
	private static final By USER_SAVED_SUCCESSFULLY_MSG_LOC = By.xpath("//div[@class='SectionHeader']/following-sibling::div[contains(@style,'display: block;')]");
	private static final By SEARCH_FIELD_LOC = By.id("uxSearchUserTypeValue");
	private static final By NEW_USER_ADDED_LOC = By.xpath("//table[@id='users']/tbody/tr[1]/td[1]/a");
	private static final By USER_IN_LIST_LOC = By.xpath("//table[@id='users']//tbody/tr[1]");
	private static final By STATUS_DD_LOC = By.xpath("//select[@name='statusId']");
	private static final By GO_BTN_LOC  = By.xpath("//a[@id='btnGo']");
	private static final By NEW_USER_SITE_ACCESS_CHECK_BOX1_LOC = By.xpath("//table[@id='userProperties']/tbody/tr[9]/td[2]/input[1]");
	private static final By NEW_USER_SITE_ACCESS_CHECK_BOX2_LOC = By.xpath("//table[@id='userProperties']/tbody/tr[9]/td[2]/input[3]");
	private static final By NEW_USER_SITE_ACCESS_CHECK_BOX3_LOC = By.xpath("//table[@id='userProperties']/tbody/tr[9]/td[2]/input[5]");
	private static final By NEW_USER_SITE_ACCESS_CHECK_BOX4_LOC = By.xpath("//table[@id='userProperties']/tbody/tr[9]/td[2]/input[7]");


	public void clickRolesLink(){
		driver.quickWaitForElementPresent(ROLES_LINK_LOC);
		driver.click(ROLES_LINK_LOC);
		logger.info("Roles Link is clicked");
		driver.waitForPageLoad();
	}

	public void clickAddNewRoleLink(){
		driver.quickWaitForElementPresent(ADD_NEW_ROLE_LINK_LOC);
		driver.click(ADD_NEW_ROLE_LINK_LOC);
		logger.info("'Add new Role' Link is clicked");
		driver.waitForPageLoad();
	}

	public void enterRoleName(String name){
		driver.quickWaitForElementPresent(ROLE_NAME_TXT_FIELD_LOC);
		driver.type(ROLE_NAME_TXT_FIELD_LOC, name);
		logger.info("Role Name entered is"+name);
	}

	public void clickSaveBtn(){
		driver.quickWaitForElementPresent(SAVE_BTN_LOC);
		driver.click(SAVE_BTN_LOC);
		logger.info("Save button is clicked");
		driver.waitForPageLoad();
	}

	public boolean validateNewRoleListedInRolesList(String newRole){
		driver.quickWaitForElementPresent(ROLES_NAME_LIST_LOC);
		int size = driver.findElements(ROLES_NAME_LIST_LOC).size();
		boolean flag= false;
		for(int i=1;i<=size;i++){
			String actualRoleName=driver.findElement(By.xpath(String.format(roleName, i))).getText();
			System.out.println("----------- "+actualRoleName);
			if(actualRoleName.equalsIgnoreCase(newRole)){
				flag =true;
				break;
			}

		}
		return flag;
	}

	public void clickAddNewUser() {
		driver.click(ADD_NEW_USER_LINK_LOC);
		logger.info("add new user link is clicked");
	}

	public void enterInfoAtAddUserPage(String firstName, String lastName,
			String userName, String password, String confirmPassword) {
		driver.type(NEW_USER_FIRST_NAME_FIELD_LOC, firstName);
		logger.info("first name entered as: "+firstName);
		driver.type(NEW_USER_LAST_NAME_FIELD_LOC, lastName);
		logger.info("last Name entered as: "+lastName);
		driver.type(NEW_USER_USER_NAME_FIELD_LOC, userName);
		logger.info("username entered as: "+userName);
		driver.type(NEW_USER_PASSWORD_FIELD_LOC, password);
		logger.info("password entered as: "+password);
		driver.type(NEW_USER_CONFIRM_PASSWORD_FIELD_LOC, confirmPassword);
		logger.info("confirm password entered as: "+confirmPassword);
		driver.type(NEW_USER_EMAIL_FIELD_LOC, password+"@xyz.com");
		logger.info("email entered as: "+password+"@xyz.com");
	}

	public void selectAllSitesAndSave() {
		driver.click(NEW_USER_ROLES_ADMINISTRATOR_LOC);
		logger.info("administrator checkbox checked");
		driver.click(NEW_USER_SITE_ACCESS_CHECK_BOX1_LOC);
		driver.click(NEW_USER_SITE_ACCESS_CHECK_BOX2_LOC);
		driver.click(NEW_USER_SITE_ACCESS_CHECK_BOX3_LOC);
		driver.click(NEW_USER_SITE_ACCESS_CHECK_BOX4_LOC);
		logger.info("all site access checkbox clicked and save btn pressed");
		driver.click(SAVE_BTN_LOC);
		logger.info("save btn clicked");
		driver.waitForPageLoad();
	}

	public boolean isUserSavedSuccessfully() {
		driver.quickWaitForElementPresent(USER_SAVED_SUCCESSFULLY_MSG_LOC);
		return driver.isElementPresent(USER_SAVED_SUCCESSFULLY_MSG_LOC);
	}

	public boolean isNewUserPresentInList(String userName) {
		driver.type(SEARCH_FIELD_LOC,userName);
		driver.click(GO_BTN_LOC);
		logger.info("go btn clicked");
		driver.waitForPageLoad();
		if(driver.isElementPresent(USER_IN_LIST_LOC)){
			return true;
		}
		return false;
	}

	public void clickOnNewUser() {
		driver.click(NEW_USER_ADDED_LOC);
		driver.waitForPageLoad();
	}

	public void selectStatus(String string) {
		Select select = new Select(driver.findElement(STATUS_DD_LOC));
		select.selectByVisibleText(string);
		selectAllSitesAndSave();
	}

	public void clickListTypesLink(String listType) {
		driver.click(LIST_TYPE_LINK_LOC);
		logger.info("list type link is clicked");
		driver.click(By.xpath(String.format(listTypeLoc, listType)));
		logger.info("list type clicked from the list");
		driver.waitForPageLoad();
	}

	public void clickAddNewListValue(){
		driver.click(ADD_NEW_LIST_VALUE_LOC);
		logger.info("add new list value link is clicked");
	}

	public void enterValueAndSave(String newAddedListValue, int numberofListPresent) {
		driver.type(By.xpath(String.format(newlyAddedValue, numberofListPresent)),newAddedListValue);
		logger.info("new list value added with value test");
		driver.click(SAVE_BTN_LOC);
		logger.info("save btn clicked after adding list value");
	}

	public boolean isNewListAdded(String newAddedListValue, int numberofListPresent) {
		String textBoxValue = driver.findElement(By.xpath(String.format(newlyAddedValue, numberofListPresent))).getAttribute("value");
		if(textBoxValue.equals(newAddedListValue)){
			return true;
		}
		return false;
	}

	public void deleteSavedListTypeAndSave(int numberofListPresent) {
		driver.click(By.xpath(String.format(newAddedlistCancelMarkLoc,numberofListPresent)));
		logger.info("saved list deleted");
	}

	public boolean verifyListDeleted(int numberofListPresent) {
		int numberOfListValues = driver.findElements(TOTAL_LIST_VALUES_LOC).size();
		if(numberOfListValues < numberofListPresent){
			return true;
		}
		return false;
	}

	public int getTotalNumberOfList() {
		int numberOfListValues = driver.findElements(TOTAL_LIST_VALUES_LOC).size();
		return numberOfListValues;
	}

}