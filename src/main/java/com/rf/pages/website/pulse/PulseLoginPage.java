package com.rf.pages.website.pulse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;

public class PulseLoginPage extends PulseWebsiteBasePage{

	@FindBy(xpath="//input[@name='username']")
	private WebElement usernameField;
	
	@FindBy(xpath="//input[@name='password']")
	private WebElement passwordField;
	
	@FindBy(xpath="//input[@value='Sign In']")
	private WebElement loginBtn;
	
	public PulseLoginPage(RFWebsiteDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	private static final Logger logger = LogManager
			.getLogger(PulseLoginPage.class.getName());


	public PulseHomePage enterCredentailsAndLogin(String username, String password){
		usernameField.sendKeys(username);
		passwordField.sendKeys(password);
		loginBtn.click();
		return new PulseHomePage(driver);
	}
	}