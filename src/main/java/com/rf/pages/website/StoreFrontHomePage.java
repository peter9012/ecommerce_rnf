package com.rf.pages.website;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class StoreFrontHomePage extends RFWebsiteBasePage {

	private final By BUSINESS_LINK_LOC = By.cssSelector("a[id='corp-opp']"); 
	private final By ENROLL_NOW_LINK_LOC = By.cssSelector("a[title='Enroll Now']");	
	private final By LOGIN_LINK_LOC = By.cssSelector("li[id='log-in-button']>a");
	private final By LOGIN_BTN_LOC = By.cssSelector("input[value='Log in']");
	private final By USERNAME_TXTFLD_LOC = By.id("username");
	private final By PASSWORD_TXTFLD_LOC = By.id("password");

	
	public StoreFrontHomePage(RFWebsiteDriver driver) {
		super(driver);		
	}

	public void clickOnOurBusinessLink(){
		driver.findElement(BUSINESS_LINK_LOC).click();
	}

	public StoreFrontEnrollNowPage clickOnOurEnrollNowLink(){
		driver.findElement(ENROLL_NOW_LINK_LOC).click();
		return new StoreFrontEnrollNowPage(driver);
	}

	public StoreFrontConsultantPage loginAsConsultant(String username,String password){
		driver.click(LOGIN_LINK_LOC);		
		driver.type(USERNAME_TXTFLD_LOC, username);
		driver.type(PASSWORD_TXTFLD_LOC, password);
		driver.click(LOGIN_BTN_LOC);	
		return new StoreFrontConsultantPage(driver);
	}
	
	public StoreFrontRCUserPage loginAsRCUser(String username,String password){
		driver.click(LOGIN_LINK_LOC);		
		driver.type(USERNAME_TXTFLD_LOC, username);
		driver.type(PASSWORD_TXTFLD_LOC, password);
		driver.click(LOGIN_BTN_LOC);	
		return new StoreFrontRCUserPage(driver);
	}
	public StoreFrontPCUserPage loginAsPCUser(String username,String password){
		driver.click(LOGIN_LINK_LOC);		
		driver.type(USERNAME_TXTFLD_LOC, username);
		driver.type(PASSWORD_TXTFLD_LOC, password);
		driver.click(LOGIN_BTN_LOC);	
		return new StoreFrontPCUserPage(driver);
		}
	
	public void openConsultantPWS(String pwsURL){
		driver.get(pwsURL);		
	}

}
