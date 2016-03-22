package com.rf.pages.website.cscockpit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;

public class CSCockpitCartTabPage extends CSCockpitRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitCartTabPage.class.getName());

	protected RFWebsiteDriver driver;
	public static String individualProductCountOnCart = "//div[text()='Product']/ancestor::div[@class='z-listbox-header']/following::div[1]//tbody[2]/tr[%s]/td[6]//input";

	private static final By PRODUCT_COUNT_ON_CART =By.xpath("//div[@class='csToolbarLeftButtons']//td[contains(@class,'csMasterContentCell')]");
	private static final By PRODUCT_NOT_AVAILABLE_FOR_CRP_AUTOSHIP = By.xpath("//td[@class='z-button-cm'][text()='OK']");

	public CSCockpitCartTabPage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}	

	public boolean verifyProductNotAvailablePopUp(){
		driver.quickWaitForElementPresent(PRODUCT_NOT_AVAILABLE_FOR_CRP_AUTOSHIP);
		return driver.isElementPresent(PRODUCT_NOT_AVAILABLE_FOR_CRP_AUTOSHIP);
	}

	public void addProductToCartPageTillAtleastFourDistinctProducts(){
		while(true){
			if(driver.findElements(PRODUCT_COUNT_ON_CART).size()>=4){
				logger.info("Required products are there in cart");
				break;
			}else{
				clearCatalogSearchFieldAndClickSearchBtn();
				String SKUValue=getCustomerSKUValueInCartTab(String.valueOf(getRandomProductWithSKUFromSearchResult()));
				searchSKUValueInCartTab(SKUValue);
				clickAddToCartBtnInCartTab();
				if(driver.findElements(PRODUCT_COUNT_ON_CART).size()>=4){
					break;
				}else{
					continue;
				}
			}
		}
	}

	public String getProductCountFromcartPage(String row){
		driver.waitForElementPresent(By.xpath(String.format(individualProductCountOnCart, row)));
		String countOfProduct=driver.findElement(By.xpath(String.format(individualProductCountOnCart, row))).getAttribute("value");
		logger.info("product count for row"+row+" is"+countOfProduct);
		return countOfProduct;
	}

}