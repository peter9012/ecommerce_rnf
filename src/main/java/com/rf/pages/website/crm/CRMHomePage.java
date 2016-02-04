package com.rf.pages.website.crm;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.storeFront.StoreFrontRFWebsiteBasePage;

public class CRMHomePage extends CRMRFWebsiteBasePage {
	private static final Logger logger = LogManager
			.getLogger(CRMHomePage.class.getName());

	private final By USER_NAVIGATION_LABEL_LOC = By.id("userNavLabel");
	private final By SEARCH_TEXT_BOX_LOC = By.id("phSearchInput");
	private final By SEARCH_BUTTON_LOC = By.id("phSearchButton");
	private final By GENERAL_SEARCH_ACCOUNT_NAME_SELECTION = By.xpath("//div[@id='Account_body']/table/tbody/tr/th/a[contains(text(),'%s')]");
	//private String ACCOUNT_NAME_SELECTION ="//div[@id='Contact_body']/table/tbody/tr[2]/td[2]/a[contains(text(),'%s')]";
	private String ACCOUNT_NAME_SELECTION="//div[@id='Contact_body']//td[2]//a[contains(text(),'%s')]";
	//private final By ACCOUNT_NAME_SELECTION =By.xpath("//div[@id='Contact_body']/table/tbody/tr[2]/td[2]/a[contains(text(),'RCUser')]");
	private final By ACCOUNT_TYPE = By.xpath("//div[starts-with(@id,'ext-gen')]/div/div[contains(text(),'Account Type')]/following::div[2]");
	private final By COUNTRY_LOC = By.xpath("//div[starts-with(@id,'ext-gen')]/div/div[contains(text(),'Country')]/following::div[2]");
	private final By ACCOUNT_STATUS_LOC = By.xpath("//div[@id='ext-gen152']/div/div[contains(text(),'Active')]");
	private final By VERIFY_CONSULTANT_SV_LOC = By.xpath("//div[contains(text(),'SV')]/following::div[@class='wc_value'][1]");
	private final By VERIFY_CONSULTANT_PSQV_LOC = By.xpath("//div[contains(text(),'PSQV')]/following::div[@class='wc_value'][1]");
	private final By VERIFY_ACCOUNT_NUMBER_LOC = By.xpath("//div[contains(text(),'Account Number')]/following::div[@class='wc_value'][1]");
	private final By VERIFY_COUNTRY_NAME_LOC = By.xpath("//div[contains(text(),'Country')]/following::div[@class='wc_value'][1]");
	private final By VERIFY_EMAIL_ADDRESS_LOC = By.xpath("//div[contains(text(),'Email Address')]/following::div[@class='wc_value'][1]");
	private final By VERIFY_ENROLLMENT_DATE_LOC = By.xpath("//div[contains(text(),'Enrollment Date')]/following::div[@class='wc_value'][1]");
	private final By VERIFY_RECOGNITION_TITLE_LOC = By.xpath("//div[contains(text(),'Recognition Title')]/following::div[@class='wc_value'][1]");
	private final By LIST_HOVER_PERFORMANCE_KPI_LOC = By.xpath("//div[@class='listHoverLinks']/a/span[contains(text(),'Performance KPIs')]");
	private final By EDIT_PWS_DOMAIN_BUTTON_LOC = By.xpath("//td[@id='topButtonRow']/input[@value='Edit PWS Domain']");
	//private final By PULSE_BUTTON_LOC = By.xpath("//td[@id='topButtonRow']/input[contains(@value,'Pulse')]");
	private final By PULSE_BUTTON_LOC = By.xpath("//td[@id='topButtonRow']//input[@title='Pulse' and @name='pulse' ]");
	private final By VERIFY_PHONE_NUMBER_LOC = By.xpath("//div[starts-with(@id,'ext-gen')]/div/div[contains(text(),'Main Phone')]/following::div[2]");
	private final By I_FRAME_LOC = By.xpath("//iframe[starts-with(@id,'ext-comp-1014')]");
    private final By AUTOSHIP_TITLE_LOC =By.xpath("//span[@class='listTitle' and contains(text(),'Autoships')]");
    private final By PERFORMANCE_KPI_LOC = By.xpath("//span[@class='listTitle' and contains(text(),'Performance KPIs')]");
    private final By ACCOUNT_ACTIVITIES_LOC = By.xpath("//span[@class='listTitle' and contains(text(),'Account Policies')]");
    //private final By EDIT_ACCOUNT_BUTTON_LOC = By.xpath("//td[@id='topButtonRow']/input[@value='Edit Account']");
    private final By EDIT_ACCOUNT_BUTTON_LOC = By.className("actionLink"); //New
    //private final By SAVE_BUTTON_LOC = By.xpath("//td[@id='bottomButtonRow']/input[@title='Save']");
    private final By SAVE_BUTTON_LOC = By.xpath("//*[@id='bottomButtonRow']/input[1]");
    private final By BACK_TO_SERVICE_CLOUD_CONSOLE_LOC = By.linkText("Back to Service Cloud Console");
	static String  firstName = null;
	public CRMHomePage(RFWebsiteDriver driver) {
		super(driver);
	}
	public boolean verifyHomePage() throws InterruptedException{		
		driver.waitForCRMLoadingImageToDisappear();
		driver.waitForElementPresent(USER_NAVIGATION_LABEL_LOC);		
		closeAllOpenedTabs();
		return driver.isElementPresent(USER_NAVIGATION_LABEL_LOC);	
	}
	
	public void closeAllOpenedTabs(){
		int totalOpenedTabs = driver.findElements(By.xpath("//li[contains(@id,'navigatortab__scc-pt')]")).size();
		logger.info("total opened tabs = "+totalOpenedTabs);
		Actions actions = new Actions(RFWebsiteDriver.driver);
		for(int i=totalOpenedTabs;i>=1;i--){
			//driver.waitForElementPresent(By.xpath("//li[contains(@id,'navigatortab__scc-pt')]["+i+"]/descendant::a[@class='x-tab-strip-close']"));
			actions.moveToElement(driver.findElement(By.xpath("//li[contains(@id,'navigatortab__scc-pt')]["+i+"]/descendant::a[@class='x-tab-strip-close']"))).click().build().perform();
			//driver.click(By.xpath("//li[contains(@id,'navigatortab__scc-pt')]["+i+"]/descendant::a[@class='x-tab-strip-close']"));
			driver.pauseExecutionFor(1000);
		}
	}
	
	public void searchUserById(String emailId){
		//driver.waitForElementPresent(SEARCH_TEXT_BOX_LOC);
		driver.findElement(SEARCH_TEXT_BOX_LOC).sendKeys(emailId);
		driver.findElement(SEARCH_TEXT_BOX_LOC).sendKeys(Keys.ENTER);
		//driver.click(SEARCH_BUTTON_LOC);
		driver.waitForPageLoad();
	}
	public boolean verifySearchPage(){
		driver.waitForElementPresent(SEARCH_TEXT_BOX_LOC);
		return driver.isElementPresent(SEARCH_TEXT_BOX_LOC);
		
	}
	public void clickaccountNameForAccountDetailsPage(String name){
		//driver.switchTo().frame(driver.findElement(I_FRAME_LOC));
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']//iframe[contains(@class,'x-border-panel')]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']//iframe[contains(@class,'x-border-panel')]")));
		driver.click(By.xpath(String.format(ACCOUNT_NAME_SELECTION, name)));
		driver.switchTo().defaultContent();
		}
	public void clickOnAccountNameForAccountDetailPageInAccountSection(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']//iframe[contains(@class,'x-border-panel')]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']//iframe[contains(@class,'x-border-panel')]")));
		driver.waitForElementPresent(By.xpath("//div[@id='Account_body']//tr[@class='headerRow']//a[contains(text(),'Account Name')]/following::tr[1]/th/a"));
		driver.click(By.xpath("//div[@id='Account_body']//tr[@class='headerRow']//a[contains(text(),'Account Name')]/following::tr[1]/th/a"));
		driver.switchTo().defaultContent();	
	}
	
	public boolean verifyAccountTypeForRCUser(){
		driver.waitForElementPresent(ACCOUNT_TYPE);
		logger.info("Account Type from UI "+driver.findElement(ACCOUNT_TYPE).getText());
		return driver.findElement(ACCOUNT_TYPE).getText().contains("Retail Customer");
	}
	public boolean verifyAccountTypeForPCUser(){
		driver.waitForElementPresent(ACCOUNT_TYPE);
		logger.info("Account Type from UI "+driver.findElement(ACCOUNT_TYPE).getText());
		return driver.findElement(ACCOUNT_TYPE).getText().contains("Preferred Customer");
	}
	public boolean verifyAccountTypeForConsultant(){
		driver.waitForElementPresent(ACCOUNT_TYPE);
		logger.info("Account Type from UI "+driver.findElement(ACCOUNT_TYPE).getText());
		String accType= driver.findElement(ACCOUNT_TYPE).getText();
		System.out.println("account type for user is"+accType);
		return driver.findElement(ACCOUNT_TYPE).getText().contains("Consultant");
	}
	public boolean verifyPhoneNumber(String phone){
		driver.waitForElementPresent(VERIFY_PHONE_NUMBER_LOC);
		logger.info("Phone number from UI "+driver.findElement(VERIFY_PHONE_NUMBER_LOC).getText());
		return driver.findElement(VERIFY_PHONE_NUMBER_LOC).getText().toLowerCase().contains(phone.toLowerCase());
	}
	public boolean verifyEmailAddress(String emailAdd){
		driver.waitForElementPresent(VERIFY_EMAIL_ADDRESS_LOC);
		logger.info("Email address from UI "+driver.findElement(VERIFY_EMAIL_ADDRESS_LOC).getText());
		return driver.findElement(VERIFY_EMAIL_ADDRESS_LOC).getText().toLowerCase().contains(emailAdd.toLowerCase());
	}
	public boolean verifyEditPWSDomainButton(){
		return driver.isElementPresent(EDIT_PWS_DOMAIN_BUTTON_LOC);
	}
	public boolean verifyPulseButton(){
		return driver.isElementPresent(PULSE_BUTTON_LOC);
		
	}
	public boolean verifyListHoverAutoShipTitle(){
		return driver.isElementPresent(AUTOSHIP_TITLE_LOC);
	}
	public boolean verifyListHoverPerformanceKPI(){
		return driver.isElementPresent(PERFORMANCE_KPI_LOC);
	}
	public boolean verifyListHoverAccountActivities(){
		return driver.isElementPresent(ACCOUNT_ACTIVITIES_LOC);
	}
	public boolean verifyCountryName(){
		driver.switchTo().defaultContent();
		logger.info("country name from UI"+ driver.findElement(COUNTRY_LOC).getText());
		return driver.findElement(COUNTRY_LOC).getText().contains("United States");
	}
	public boolean verifySVValue(String value){
		return driver.findElement(VERIFY_CONSULTANT_SV_LOC).getText().contains(value);
	}
	public boolean verifyPSQVValue(String psqvValue){
		return driver.findElement(VERIFY_CONSULTANT_PSQV_LOC).getText().contains("psqvValue");
	}
	public void editAccountPage(){
		driver.switchTo().frame("ext-comp-1019");
		driver.click(EDIT_ACCOUNT_BUTTON_LOC);
		driver.waitForPageLoad();
		driver.click(SAVE_BUTTON_LOC);
		driver.switchTo().defaultContent();
		logger.info("Save button clicked after editing profile");
		driver.waitForPageLoad();
	}
	public void clickOnBackToServiceCloudConsole(){
		driver.quickWaitForElementPresent(BACK_TO_SERVICE_CLOUD_CONSOLE_LOC);
		if(driver.isElementPresent(BACK_TO_SERVICE_CLOUD_CONSOLE_LOC)){
		driver.click(BACK_TO_SERVICE_CLOUD_CONSOLE_LOC);		
		logger.info("User has clicked on back to service cloud console link");
		}else{
			logger.info("back to service cloud console link not present");
		}
		driver.pauseExecutionFor(30000);
		}
	public int clickOnBillingProfileAndGetNumberBillingProfile(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[2]//iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[2]//iframe[1]")));
		driver.waitForElementPresent(By.xpath("//div[@class='listHoverLinks']/a/span[contains(text(),'Billing  Profiles')]/span"));
		String count=driver.findElement(By.xpath("//div[@class='listHoverLinks']/a/span[contains(text(),'Billing  Profiles')]/span")).getText();
		String [] countOfBillingProfile = count.split("\\[");
		driver.waitForElementPresent(By.xpath("//div[@class='listHoverLinks']/a/span[contains(text(),'Billing  Profiles')]"));
		driver.findElement(By.xpath("//div[@class='listHoverLinks']/a/span[contains(text(),'Billing  Profiles')]")).click();
		logger.info("Billing profile link is clicked");
		driver.switchTo().defaultContent();
		return Integer.parseInt(countOfBillingProfile[1].substring(0, countOfBillingProfile[1].length()-1));
	}
	
	public int getCountOfBillingProfileUnderBillingProfileSection(){
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[2]//iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[2]//iframe[1]")));
		driver.waitForElementPresent(By.xpath(" //div[@id='ep']/following-sibling::div[5]/div[1]/div[1]/div[2]/table/tbody/tr"));
		int count=driver.findElements(By.xpath(" //div[@id='ep']/following-sibling::div[5]/div[1]/div[1]/div[2]/table/tbody/tr")).size();
		int rowCount = count-1;
		driver.switchTo().defaultContent();
		logger.info("billing count from UI "+rowCount);
		return rowCount;
	}
	
	public boolean verifyBillingInfoActionField(){
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[2]//iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[2]//iframe[1]")));
		driver.waitForElementPresent(By.xpath("//div[@id='ep']/following-sibling::div[5]/div[1]/div/div[2]/table//th[contains(text(),'Action')]"));
		boolean actionButton = driver.isElementPresent(By.xpath("//div[@id='ep']/following-sibling::div[5]/div[1]/div/div[2]/table//th[contains(text(),'Action')]"));
		driver.switchTo().defaultContent();
		return actionButton;
	}
	
	public void clickOnShippingProfileName(){ 
		 driver.switchTo().frame(1);
		 driver.waitForElementPresent(By.xpath("//*[@id='ShippingProfile__c_body']/table/tbody/tr[2]/th/a"));
		 driver.findElement(By.xpath("//*[@id='ShippingProfile__c_body']/table/tbody/tr[2]/th/a")).click();
		 driver.switchTo().defaultContent();
}
	public void clickEditShipping(){
		driver.switchTo().frame(1);
		//click on edit 
		driver.waitForElementPresent(By.xpath("//*[@id='0015500000BaVVi_00N1a000005tJN3_body']/table/tbody/tr[2]/td[1]/a[1]"));
		driver.findElement(By.xpath("//*[@id='0015500000BaVVi_00N1a000005tJN3_body']/table/tbody/tr[2]/td[1]/a[1]"));
		driver.switchTo().defaultContent();
		//clear and enter new locale
		driver.waitForElementPresent(By.xpath("//*[@id='00N1a000005tJNH']"));
		driver.findElement(By.xpath("//*[@id='00N1a000005tJNH']")).clear();
		driver.findElement(By.xpath("//*[@id='00N1a000005tJNH']")).sendKeys("San Francisco");
		driver.switchTo().defaultContent();
	}
	
	public void searchUserandSelect(String Name){
		//driver.waitForElementPresent(SEARCH_TEXT_BOX_LOC);
		driver.findElement(SEARCH_TEXT_BOX_LOC).sendKeys(Name);
		driver.findElement(SEARCH_TEXT_BOX_LOC).sendKeys(Keys.ENTER);
		driver.waitForPageLoad();
		driver.switchTo().frame(1);
		driver.findElement(By.xpath("//div[@id='Account_body']//tr[@class='headerRow']//a[contains(text(),'Account Name')]/following::tr[1]/th/a")).click();
		//driver.findElement(By.xpath("//*[@id='Account_body']/table/tbody/tr[2]/th/a")).click();
		driver.switchTo().defaultContent();
		driver.waitForPageLoad();
}
	public void AccountDetailsPage(){
    System.out.println(driver.findElement(By.xpath("//div[starts-with(@id,'ext-gen')]/div/div[contains(text(),'Account Number')]/following::div[2]")).getText());
	}
	
	
	public void enterTextInSearchFieldAndHitEnter(String text){
		driver.findElement(SEARCH_TEXT_BOX_LOC).sendKeys(text);
		driver.findElement(SEARCH_TEXT_BOX_LOC).sendKeys(Keys.ENTER);
		driver.waitForPageLoad();
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
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]")));
		emailOnfirstRow = driver.findElement(By.xpath("//div[@id='Account_body']//tr[@class='headerRow']//a[contains(text(),'Email Address')]/following::tr[1]/td[9]/a")).getText();		
		return emailOnfirstRow;
	}
	
	public void clickNameOnFirstRowInSearchResults(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]")));
		driver.click(By.xpath("//div[@id='Account_body']//tr[@class='headerRow']//a[contains(text(),'Account Name')]/following::tr[1]/th/a"));
		driver.switchTo().defaultContent();
		driver.waitForCRMLoadingImageToDisappear();
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

	public void EditAccountDetails(){
		driver.switchTo().frame(1);
		//click Edit button
		driver.click(EDIT_ACCOUNT_BUTTON_LOC);
		//driver.waitForPageLoad();
		//driver.findElement(By.xpath("//*[@id='Account_body']//tr[@class='headerRow']//a[contains(text(),'Account Name')]/following::tr[2]/td[1]/a")).click();
		driver.switchTo().defaultContent();
	}
	
	public void EnterNewLocale(){
		//clear and enter new locale
		driver.switchTo().defaultContent();
		List<WebElement> frames = driver.findElements(By.tagName("iframe"));
		 System.out.println("Number of frames" + frames.size());
		 for(int i=0;i<frames.size();i++){
			 driver.switchTo().frame(i);
			 int s = driver.findElements(By.xpath("//*[@id='00N1a000005uQGL']")).size();
			 driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		driver.findElement(By.xpath("//*[@id='00N1a000005uQGL']")).clear();
		driver.findElement(By.xpath("//*[@id='00N1a000005uQGL']")).sendKeys("San Francisco");
		driver.switchTo().defaultContent();}
	}
	
	public void ClickSave(){
			 driver.switchTo().defaultContent();
		 driver.switchTo().frame(2);
		 driver.click(SAVE_BUTTON_LOC);
		 driver.switchTo().defaultContent();	
	}
	
	public void GetUpdatedAddress(){
		//Get the text of the Updated address
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		 System.out.println(driver.findElement(By.xpath("//*[@id='ep']/div[2]/div[5]/table/tbody/tr[4]/td[2]")).getText());
	}
}
