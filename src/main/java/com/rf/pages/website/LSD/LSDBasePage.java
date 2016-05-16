/**
 * 
 */
/**
 * @author smccabe
 *
 */
package com.rf.pages.website.LSD;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.RFBasePage;
//import com.rf.pages.website.crm.LSDBasePage;
import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontCartAutoShipPage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontShippingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.pages.website.storeFront.StoreFrontRFWebsiteBasePage;

public class LSDBasePage extends RFBasePage{
	public LSDBasePage(RFWebsiteDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	private static final Logger logger = LogManager.getLogger(LSDLoginPage.class.getName());
	protected RFWebsiteDriver driver;
	
	private final By GREETING_MESSAGE = By.xpath("//a[@class='greeting white']");
	private final By VIEW_CUSTOMERS_LINK= By.linkText("View my customers");
	private final By NAV_HOME= By.id("nav-home");
	private final By NAV_CUSTOMERS= By.id("nav-customers");
	private final By NAV_ORDERS= By.id("nav-orders");
	private final By NAV_FEEDBACK= By.id("nav-feedback");
	private final By NAV_LOGOUTE= By.id("nav-logout");
	
	
	
	public Login(String sUser, STRING sPassword){
		LSDLoginPage lp = new LSDLoginPage();
		
		
	}
	
	
	
	
	
}
	


	