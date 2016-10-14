package com.rf.pages.website.crm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import com.rf.core.driver.website.RFWebsiteDriver;

public class CRMHomePage extends CRMRFWebsiteBasePage {
	private static final Logger logger = LogManager
			.getLogger(CRMHomePage.class.getName());

	private final By USER_NAVIGATION_LABEL_LOC = By.id("userNavLabel");
	private final By SEARCH_TEXT_BOX_LOC = By.id("phSearchInput");
	static String  firstName = null;
	public CRMHomePage(RFWebsiteDriver driver) {
		super(driver);
	}

	public boolean verifyHomePage() throws InterruptedException{
		driver.switchTo().defaultContent();
		driver.waitForCRMLoadingImageToDisappear();
		driver.quickWaitForElementPresent(USER_NAVIGATION_LABEL_LOC);		
		closeAllOpenedTabs();
		return driver.isElementPresent(USER_NAVIGATION_LABEL_LOC);	
	}

	public void clickOnAccountNameForAccountDetailPageInAccountSection(){
		driver.switchTo().defaultContent();
		driver.quickWaitForElementPresent(By.xpath("//div[@id='navigatortab']//iframe[contains(@class,'x-border-panel')]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']//iframe[contains(@class,'x-border-panel')]")));
		driver.quickWaitForElementPresent(By.xpath("//div[@id='Account_body']//tr[@class='headerRow']//a[contains(text(),'Account Name')]/following::tr[1]/th/a"));
		driver.click(By.xpath("//div[@id='Account_body']//tr[@class='headerRow']//a[contains(text(),'Account Name')]/following::tr[1]/th/a"));
		driver.switchTo().defaultContent();	
	}	

	public void enterTextInSearchFieldAndHitEnter(String text){
		driver.type(SEARCH_TEXT_BOX_LOC,text);
		driver.findElement(SEARCH_TEXT_BOX_LOC).sendKeys(Keys.ENTER);
		driver.waitForPageLoad();
		driver.waitForCRMLoadingImageToDisappear();
	}

	public String getNameOnFirstRowInSearchResults(){
		String nameOnfirstRow = null;
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]")));
		nameOnfirstRow = driver.findElement(By.xpath("//div[@id='Account_body']//tr[@class='headerRow']//a[contains(text(),'Account Name')]/following::tr[1]/th/a")).getText();		
		return nameOnfirstRow;
	}

	public String getEmailOnFirstRowInSearchResults(){
		String emailOnfirstRow = null;
		driver.switchTo().defaultContent();
		driver.quickWaitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]")));
		emailOnfirstRow = driver.findElement(By.xpath("//div[@id='Account_body']//tr[@class='headerRow']//a[contains(text(),'Email Address')]/following::tr[1]/td[10]/a")).getText();		
		return emailOnfirstRow;
	}

	public void clickNameOnFirstRowInSearchResults(){
		driver.switchTo().defaultContent();
		driver.quickWaitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]")));
		driver.click(By.xpath("//div[@id='Account_body']//tr[@class='headerRow']//a[contains(text(),'Account Name')]/following::tr[1]/th/a"));
		driver.switchTo().defaultContent();
		driver.waitForCRMLoadingImageToDisappear();
	}

	public void clickNameWithActiveStatusInSearchResults(){
		driver.switchTo().defaultContent();
		driver.quickWaitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]")));
		driver.click(By.xpath("//img[@class='checkImg'][@title='Checked']/ancestor::tr[1]/th/a"));
		driver.switchTo().defaultContent();
		driver.waitForCRMLoadingImageToDisappear();
	}

	public boolean isSearchResultHasActiveUser(){
		driver.switchTo().defaultContent();
		driver.quickWaitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]")));
		return driver.isElementPresent(By.xpath("//img[@class='checkImg'][@title='Checked']/ancestor::tr[1]/th/a"));
	}

	public boolean isSearchResultHasActiveUser(String customer){
		driver.switchTo().defaultContent();
		driver.quickWaitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]")));
		driver.quickWaitForElementPresent(By.xpath("//div[@id='Account_body']//td[text()='"+customer+"']/following::td[text()='Active']/..//th//a"));
		return driver.isElementPresent(By.xpath("//div[@id='Account_body']//td[text()='"+customer+"']/following::td[text()='Active']/..//th//a"));		
	}

	public boolean isAccountSectionPresent(){
		driver.switchTo().defaultContent();
		driver.quickWaitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]")));
		return driver.isElementPresent(By.xpath("//div[@id='Account_body']"));
	}

	public boolean isAccountLinkPresentInLeftNaviagation(){
		driver.switchTo().defaultContent();
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]")));
		return driver.isElementPresent(By.xpath("//a[starts-with(@title,'Accounts')]"));
	}

	public boolean isContactsLinkPresentInLeftNaviagation(){
		driver.switchTo().defaultContent();
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]")));
		return driver.isElementPresent(By.xpath("//a[starts-with(@title,'Contacts')]"));
	}

	public boolean isAccountActivitiesLinkPresentInLeftNaviagation(){
		driver.switchTo().defaultContent();
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]")));
		return driver.isElementPresent(By.xpath("//a[starts-with(@title,'Account Activities')]"));
	}

	public void clickContactOnFirstRowInSearchResults(){
		driver.switchTo().defaultContent();
		driver.quickWaitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]")));
		driver.click(By.xpath("//div[@id='Contact_body']//tr[@class='headerRow']//a[contains(text(),'Account Name')]/following::tr[1]/th/a"));
		driver.switchTo().defaultContent();
		driver.waitForCRMLoadingImageToDisappear();
	}

	public boolean isOrderOfDetailsPresentInListView(){
		driver.switchTo().defaultContent();
		driver.quickWaitForElementPresent(By.xpath("//div[@id='navigatortab']//iframe[contains(@class,'x-border-panel')]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']//iframe[contains(@class,'x-border-panel')]")));
		return driver.isElementPresent(By.xpath(".//*[@id='Account']/div[2]/div"));
	}

	public void clickConsultantCustomerNameInSearchResult(){
		driver.switchTo().defaultContent();
		driver.quickWaitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]")));
		driver.click(By.xpath("//div[@id='Account_body']//tr[@class='headerRow']/following::tr/td[contains(text(),'Active')]/preceding-sibling::th/a"));
		driver.switchTo().defaultContent();
		driver.waitForCRMLoadingImageToDisappear();
	}

	public void clickPreferredCustomerNameInSearchResult(){
		driver.switchTo().defaultContent();
		driver.quickWaitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]")));
		driver.click(By.xpath("//div[@id='Account_body']//tr[@class='headerRow']/following::tr//td[Text()='Preferred Customer']/preceding-sibling::th"));
		driver.switchTo().defaultContent();
		driver.waitForCRMLoadingImageToDisappear();
	}

	public void clickAnyTypeOfActiveCustomerInSearchResult(String customer){
		driver.switchTo().defaultContent();
		driver.quickWaitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]")));
		driver.quickWaitForElementPresent(By.xpath("//div[@id='Account_body']//td[text()='"+customer+"']/following::td[text()='Active']/..//th//a"));
		driver.click(By.xpath("//div[@id='Account_body']//td[text()='"+customer+"']/following::td[text()='Active']/..//th//a"));
		driver.switchTo().defaultContent();
		driver.waitForCRMLoadingImageToDisappear();
	}

	public int getCountOfShippingProfile(){
		//refreshPage();
		driver.switchTo().defaultContent();
		driver.quickWaitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.quickWaitForElementPresent(By.xpath("//span[contains(text(),'Shipping Profiles')]/span[contains(text(),'[1]')]"));
		String count = driver.findElement(By.xpath("//span[contains(text(),'Shipping Profiles')]/span")).getText().split("\\[")[1].split("\\]")[0];
		logger.info("Count of Shipping Profile "+count);
		driver.switchTo().defaultContent();
		return Integer.parseInt(count);
	}

	public String getEmailOnFirstRowInSearchResultsOfMainPhoneNumber(){
		String emailOnfirstRow = null;
		driver.switchTo().defaultContent();
		driver.quickWaitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]")));
		emailOnfirstRow = driver.findElement(By.xpath("//div[@id='Contact_body']//tr[@class='headerRow']/following::tr[1]/td[4]/a")).getText();  
		return emailOnfirstRow;
	}

	public String getAccountNameOnFirstRowInSearchResultsOfMainPhoneNumber(){
		String nameOnfirstRow = null;
		driver.switchTo().defaultContent();
		driver.quickWaitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]")));
		nameOnfirstRow = driver.findElement(By.xpath("//div[@id='Contact_body']//tr[@class='headerRow']/following::tr[1]/td[2]/a")).getText();  
		return nameOnfirstRow;
	}

	public String getNameOnFirstRowInSearchResultsOfMainPhoneNumber(){
		String nameOnfirstRow = null;
		driver.switchTo().defaultContent();
		driver.quickWaitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]")));
		nameOnfirstRow = driver.findElement(By.xpath("//div[@id='Contact_body']//tr[@class='headerRow']/following::tr[1]/th/a")).getText();  
		return nameOnfirstRow;
	}

}
