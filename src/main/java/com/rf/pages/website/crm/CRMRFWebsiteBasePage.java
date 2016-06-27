package com.rf.pages.website.crm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.model.Site;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.RFBasePage;
import com.rf.pages.website.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontCartAutoShipPage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontShippingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;


public class CRMRFWebsiteBasePage extends RFBasePage{
	private static final Logger logger = LogManager
			.getLogger(CRMRFWebsiteBasePage.class.getName());

	//private final By RODAN_AND_FIELDS_IMG_LOC = By.xpath("//div[@id='header-middle-top']//a");//fixed
	private final By RODAN_AND_FIELDS_IMG_LOC = By.xpath("//div[@id='header-logo']//a");
	private final By WELCOME_DD_EDIT_CRP_LINK_LOC = By.xpath("//a[contains(text(),'Edit')]");
	private final By WELCOME_USER_DD_LOC = By.id("account-info-button");
	private final By WELCOME_DD_ORDERS_LINK_LOC = By.xpath("//a[text()='Orders']");
	private final By YOUR_ACCOUNT_DROPDOWN_LOC = By.xpath("//button[@class='btn btn-default dropdown-toggle']");
	private final By WELCOME_DD_BILLING_INFO_LINK_LOC = By.linkText("Billing Info");
	private final By WELCOME_DD_SHIPPING_INFO_LINK_LOC = By.linkText("Shipping Info");
	private final By ADD_NEW_SHIPPING_LINK_LOC = By.xpath("//a[@class='add-new-shipping-address']");
	private final By WELCOME_DD_ACCOUNT_INFO_LOC = By.xpath("//a[text()='Account Info']");
	private final By ADD_NEW_BILLING_CARD_NUMBER_LOC = By.id("card-nr");
	private final By UPDATE_CART_BTN_LOC = By.xpath("//input[@value='UPDATE CART']");

	protected RFWebsiteDriver driver;
	private String RFO_DB = null;
	public CRMRFWebsiteBasePage(RFWebsiteDriver driver){		
		super(driver);
		this.driver = driver;
	}

	//contains the common methods useful for all the pages inherited

	public static String convertDBDateFormatToUIFormat(String DBDate){
		String UIMonth=null;
		String[] splittedDate = DBDate.split(" ");
		String date = (splittedDate[0].split("-")[2].charAt(0))=='0'?splittedDate[0].split("-")[2].split("0")[1]:splittedDate[0].split("-")[2];
		String month = (splittedDate[0].split("-")[1].charAt(0))=='0'?splittedDate[0].split("-")[1].split("0")[1]:splittedDate[0].split("-")[1];		
		String year = splittedDate[0].split("-")[0];		
		switch (Integer.parseInt(month)) {		
		case 1:
			UIMonth="January";
			break;
		case 2:
			UIMonth="February";
			break;
		case 3:
			UIMonth="March";
			break;
		case 4:
			UIMonth="April";
			break;
		case 5:
			UIMonth="May";
			break;
		case 6:
			UIMonth="June";
			break;
		case 7:
			UIMonth="July";
			break;
		case 8:
			UIMonth="August";
			break;
		case 9:
			UIMonth="September";
			break;
		case 10:
			UIMonth="October";
			break;
		case 11:
			UIMonth="November";
			break;
		case 12:
			UIMonth="December";
			break;		
		}

		return UIMonth+" "+date+", "+year;
	}


	public String CanadaProvinceCode(String province){
		Map<String, String> CA_PROVINCE_CODE = new HashMap<String, String>();
		CA_PROVINCE_CODE.put("Alberta","AB");
		CA_PROVINCE_CODE.put("British Columbia","BC");
		CA_PROVINCE_CODE.put("Manitoba","MB");
		CA_PROVINCE_CODE.put("New Brunswick","NB");
		CA_PROVINCE_CODE.put("Northwest Territories","NT");
		CA_PROVINCE_CODE.put("Nova Scotia","NS");
		CA_PROVINCE_CODE.put("Nunavut","NU");
		CA_PROVINCE_CODE.put("Ontario","ON");
		CA_PROVINCE_CODE.put("Prince Edward Island","PE");
		CA_PROVINCE_CODE.put("Quebec","QC");
		CA_PROVINCE_CODE.put("Saskatchewan","SK");

		return CA_PROVINCE_CODE.get(province);
	}

	public void openBaseURL(){
		driver.get(driver.getURL());
		driver.waitForPageLoad();
	}

	public void switchToPreviousTab(){
		try{
			ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
			driver.switchTo().window(tabs.get(1));
			driver.close();
			driver.switchTo().window(tabs.get(0));
			driver.pauseExecutionFor(1000);
		}catch(Exception e){
			logger.info("No previous tab");
		}
	}


	public void switchToChildWindow(){
		driver.pauseExecutionFor(10000);
		driver.switchToSecondWindow();
		driver.waitForPageLoad();
	}
	public boolean validateErrorMsgIsDisplayed(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.waitForElementPresent(By.xpath("//div[@class='errorMsg']/strong"));
		return driver.findElement(By.xpath("//div[@class='errorMsg']/strong")).getText().contains("Error");
	}

	public void clickDeleteForTheDefaultShippingProfileSelected(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'Shipping Profiles Help')]/ancestor::div[@class='listRelatedObject customnotabBlock']//img[@title='Checked']/../preceding-sibling::td[2]/a[2]"));
		driver.click(By.xpath("//span[contains(text(),'Shipping Profiles Help')]/ancestor::div[@class='listRelatedObject customnotabBlock']//img[@title='Checked']/../preceding-sibling::td[2]/a[2]"));
	}

	public void clickOKOnDeleteDefaultShippingProfilePopUp(){
		/* driver.switchTo().defaultContent();
		  driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));*/
		// driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.pauseExecutionFor(3000);
		Alert alt=driver.switchTo().alert();
		alt.accept();
		driver.waitForPageLoad();
	}

	public boolean validateDefaultShippingAddressCanNotBeDeleted(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.waitForElementPresent(By.xpath("//table//td[contains(text(),'You cannot delete the default address')]"));
		boolean state= driver.isElementPresent(By.xpath("//table//td[contains(text(),'You cannot delete the default address')]"));
		driver.switchTo().defaultContent();
		return state;
	}

	public void refreshPage(){
		driver.pauseExecutionFor(1500);
		driver.navigate().refresh();
	}


	public void addANewShippingProfileAndMakeItDefault(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'Shipping Profiles Help')]/ancestor::div[@class='listRelatedObject customnotabBlock']//input"));
		driver.click(By.xpath("//span[contains(text(),'Shipping Profiles Help')]/ancestor::div[@class='listRelatedObject customnotabBlock']//input"));
		driver.waitForCRMLoadingImageToDisappear();
		driver.waitForPageLoad();
		if(driver.getCountry().trim().equalsIgnoreCase("ca")){
			driver.switchTo().defaultContent();
			driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[2]"));
			driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[2]")));
			driver.findElement(By.xpath("//table[@class='detailList']//label[contains(text(),'Address Line 1')]/parent::td/following-sibling::td/input")).sendKeys(TestConstants.CRM_NEW_ADDRESS_LINE_1_CA);
			driver.findElement(By.xpath("//table[@class='detailList']//label[contains(text(),'Locale')]/parent::td/following-sibling::td/input")).sendKeys(TestConstants.CRM_NEW_LOCALE_CA);
			driver.findElement(By.xpath("//table[@class='detailList']//label[contains(text(),'Region')]/parent::td/following-sibling::td/input")).sendKeys(TestConstants.CRM_NEW_REGION_CA);
			driver.findElement(By.xpath("//table[@class='detailList']//label[contains(text(),'Postal Code')]/parent::td/following-sibling::td/input")).sendKeys(TestConstants.CRM_NEW_POSTALCODE_CA);
			driver.findElement(By.xpath("//table[@class='detailList']//label[contains(text(),'Address Phone')]/parent::td/following-sibling::td/input")).sendKeys(TestConstants.CRM_NEW_PHONENUM_CA);
			driver.findElement(By.xpath("//table[@class='detailList']//label[contains(text(),'Profile Name')]/parent::td/following-sibling::td/input")).sendKeys(TestConstants.CRM_NEW_PROFILENAME_CA);
			//check default check box
			driver.findElement(By.xpath("//table[@class='detailList']//label[contains(text(),'IsDefault')]/parent::td/following-sibling::td/input")).click();
			//click save
			driver.click(By.xpath("//a[contains(text(),'Save Address')]"));
			driver.pauseExecutionFor(4000);
			driver.click(By.xpath("//input[@title='Choose Address']"));
			driver.click(By.xpath("//a[contains(text(),'Save Address')]"));
		}else{

		}
	}

	public String getProfileNameOfTheDefaultShippingProfile(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'Shipping Profiles Help')]/ancestor::div[@class='listRelatedObject customnotabBlock']//img[@title='Checked']/../preceding-sibling::td[1]"));
		return driver.findElement(By.xpath("//span[contains(text(),'Shipping Profiles Help')]/ancestor::div[@class='listRelatedObject customnotabBlock']//img[@title='Checked']/../preceding-sibling::td[1]")).getText();
	}

	public void clickDeleteForNonDefaultShippingProflle(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'Shipping Profiles Help')]/ancestor::div[@class='listRelatedObject customnotabBlock']//img[@title='Checked']/ancestor::tr[1]/preceding-sibling::tr[1]//a[2]"));
		driver.click(By.xpath("//span[contains(text(),'Shipping Profiles Help')]/ancestor::div[@class='listRelatedObject customnotabBlock']//img[@title='Checked']/ancestor::tr[1]/preceding-sibling::tr[1]//a[2]"));
	}

	public boolean validateNonDefaultShippingProfileDeleted(){
		//refreshPage();
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.waitForElementPresent(By.xpath("//span[contains(text(),'Shipping Profiles')]/span[contains(text(),'[1]')]"));
		boolean state= driver.isElementPresent(By.xpath("//span[contains(text(),'Shipping Profiles')]/span[contains(text(),'[1]')]"));
		driver.switchTo().defaultContent();
		return state;
	}

	public void closeAllOpenedTabs(){
		int totalOpenedTabs = 0;
		totalOpenedTabs = driver.findElements(By.xpath("//li[contains(@id,'navigatortab__scc-pt')]")).size();
		for(int count=1;count<=3;count++){
			logger.info("total opened tabs = "+totalOpenedTabs);
			Actions actions = new Actions(RFWebsiteDriver.driver);
			for(int i=totalOpenedTabs;i>=1;i--){
				//driver.waitForElementPresent(By.xpath("//li[contains(@id,'navigatortab__scc-pt')]["+i+"]/descendant::a[@class='x-tab-strip-close']"));
				actions.moveToElement(driver.findElement(By.xpath("//li[contains(@id,'navigatortab__scc-pt')]["+i+"]/descendant::a[@class='x-tab-strip-close']"))).click().build().perform();
				//driver.click(By.xpath("//li[contains(@id,'navigatortab__scc-pt')]["+i+"]/descendant::a[@class='x-tab-strip-close']"));
				driver.pauseExecutionFor(1000);
			}
			totalOpenedTabs = driver.findElements(By.xpath("//li[contains(@id,'navigatortab__scc-pt')]")).size();
			if(totalOpenedTabs==0){
				break;
			}else{
				continue;
			}
		}
	}

	public void closeTabViaNumberWise(int number){
		driver.switchTo().defaultContent();
		Actions actions = new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(By.xpath("//li[contains(@id,'navigatortab__scc-pt')]["+number+"]/descendant::a[@class='x-tab-strip-close']"))).click().build().perform();
		driver.pauseExecutionFor(1000);
	}

	public void acceptAlert(){
		Alert alt=driver.switchTo().alert();
		alt.accept();
		driver.pauseExecutionFor(2000);
	}
}