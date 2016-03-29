package com.rf.pages.website.cscockpit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class CSCockpitAutoshipCartTabPage extends CSCockpitRFWebsiteBasePage{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitAutoshipCartTabPage.class.getName());

	private static String sortByDropDownLoc= "//div[@class='csResultsSortList']/select/option[text()='%s']";
	private static String qtyOfAddedProduct = "//div[@class='z-listbox-body']//span[contains(text(),'%s')]/following::td[3]//input";

	private static final By PRODUCT_NOT_AVAILABLE_POPUP_OK_BTN = By.xpath("//td[@class='z-button-cm'][text()='OK']");
	private static final By ADD_TO_CART_BTN = By.xpath("//td[contains(text(),'Add To Cart')]");

	protected RFWebsiteDriver driver;

	public CSCockpitAutoshipCartTabPage(RFWebsiteDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public String getQtyOfProductAddedInToCart(String SKUvalue){
		driver.waitForElementPresent(By.xpath(String.format(qtyOfAddedProduct, SKUvalue)));
		return driver.findElement(By.xpath(String.format(qtyOfAddedProduct, SKUvalue))).getAttribute("value");
	}

	public boolean verifyAutoshipCartTabPage(){
		driver.waitForElementPresent(ADD_TO_CART_BTN);
		return driver.isElementPresent(ADD_TO_CART_BTN);
	}

}