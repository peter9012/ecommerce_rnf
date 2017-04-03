package com.rf.pages.website.pulse;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.testng.Reporter;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.RFBasePage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontAboutMePage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontAutoshipCartPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontAutoshipStatusPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontCartPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontCheckoutPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontConsultantEnrollNowPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontHomePage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontProductDetailPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontShippingInfoPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontShopSkinCarePage;

public class PulseWebsiteBasePage extends RFBasePage {
	private static final Logger logger = LogManager.getLogger(PulseWebsiteBasePage.class.getName());

	protected RFWebsiteDriver driver;
	
	@FindBy(xpath="//p[@class='Name']")
	protected WebElement nameDropDown;

	@FindBy(xpath="//a[contains(text(),'Logout')]")
	private WebElement logoutLink;

	public PulseWebsiteBasePage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}
	
	public void logout(){
		Actions actions = new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(nameDropDown).click(nameDropDown)
		.moveToElement(logoutLink).doubleClick(logoutLink)
		.build().perform();
//		driver.clickByJS(RFWebsiteDriver.driver, By.xpath("//a[contains(text(),'Logout')]"));
//		driver.pauseExecutionFor(2000);
		driver.waitForPageLoad();
	}
}
