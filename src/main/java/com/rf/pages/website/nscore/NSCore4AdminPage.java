package com.rf.pages.website.nscore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class NSCore4AdminPage extends NSCore4RFWebsiteBasePage{

	private static final Logger logger = LogManager
			.getLogger(NSCore4AdminPage.class.getName());

	public NSCore4AdminPage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	private static String roleName = "//td[@class='CoreContent']/ul/li[%d]/a";

	private static final By ROLES_LINK_LOC = By.xpath("//ul[@id='SubNav']//span[text()='Roles']");
	private static final By ADD_NEW_ROLE_LINK_LOC = By.xpath("//a[text()='Add new role']");
	private static final By ROLE_NAME_TXT_FIELD_LOC = By.xpath("//input[@id='name']");
	private static final By SAVE_BTN_LOC = By.xpath("//a[@id='btnSave']");
	private static final By ROLES_NAME_LIST_LOC = By.xpath("//td[@class='CoreContent']/ul/li");

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

}