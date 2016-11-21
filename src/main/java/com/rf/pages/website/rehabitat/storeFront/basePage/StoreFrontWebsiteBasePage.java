package com.rf.pages.website.rehabitat.storeFront.basePage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Reporter;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.RFBasePage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontConsultantEnrollNowPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontShopSkinCarePage;

public class StoreFrontWebsiteBasePage extends RFBasePage{
	private static final Logger logger = LogManager
			.getLogger(StoreFrontWebsiteBasePage.class.getName());
	
	protected RFWebsiteDriver driver;
	
	public StoreFrontWebsiteBasePage(RFWebsiteDriver driver){		
		super(driver);
		this.driver = driver;
	}
	/**
	 * Top Navigation Links
	 */
	private final By BECOME_A_CONSULTANT_LOC = By.xpath("//div[@class='navbar-inverse']//*[@title='BECOME A CONSULTANT']");
	private final By ENROLL_NOW_LOC = By.xpath("//div[@class='navbar-inverse']//a[@title='ENROLL NOW']");
	private final By WHY_RF_LOC = By.xpath("//div[@class='navbar-inverse']//a[@title='WHY R+F']");
	private final By SHOP_SKINCARE_LOC = By.xpath("//div[@class='navbar-inverse']//a[@title='SHOP SKINCARE']");
	private final By ALL_PRODUCTS = By.xpath("//div[@class='navbar-inverse']//a[@title='ALL PRODUCTS']");
	
	private String RFO_DB = null;
	
	public StoreFrontWebsiteBasePage mouseHoverOn(String element){
		if(element.equalsIgnoreCase(TestConstants.BECOME_A_CONSULTANT)){
			driver.moveToElement(BECOME_A_CONSULTANT_LOC);
			logger.info("mouseHovered on 'Become A Consultant'");	
		}
		else if(element.equalsIgnoreCase(TestConstants.SHOP_SKINCARE)){
			driver.moveToElement(SHOP_SKINCARE_LOC);
			logger.info("mouseHovered on 'Shop Skincare'");	
		}
		return this;
	}
	
	public StoreFrontConsultantEnrollNowPage clickOnEnrollNow(){
		mouseHoverOn(TestConstants.BECOME_A_CONSULTANT);
		driver.click(ENROLL_NOW_LOC);
		logger.info("clicked on 'Enroll Now'");
		return new StoreFrontConsultantEnrollNowPage(driver);
	}
	
	public StoreFrontWebsiteBasePage clickOnWhyRF(){
		mouseHoverOn(TestConstants.BECOME_A_CONSULTANT);
		driver.click(WHY_RF_LOC);
		logger.info("clicked on 'Why R+F'");
		return this;
	}
	
	public StoreFrontShopSkinCarePage clickOnAllProducts(){
		mouseHoverOn(TestConstants.SHOP_SKINCARE);
		driver.click(ALL_PRODUCTS);
		logger.info("clicked on 'All Products'");
		return new StoreFrontShopSkinCarePage(driver);
	}
	
	

	


}