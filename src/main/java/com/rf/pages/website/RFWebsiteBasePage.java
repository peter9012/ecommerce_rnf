package com.rf.pages.website;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.RFBasePage;


public class RFWebsiteBasePage extends RFBasePage{
	private static final Logger logger = LogManager
			.getLogger(RFWebsiteBasePage.class.getName());

	private final By RODAN_AND_FIELDS_IMG_LOC = By.xpath("//img[@title='Rodan+Fields']");
	protected RFWebsiteDriver driver;
	public RFWebsiteBasePage(RFWebsiteDriver driver){		
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

	public void clickOnShopLink(){
		driver.waitForElementPresent(By.xpath("//a[@id='our-products']"));
		driver.findElement(By.xpath("//a[@id='our-products']")).click();
		logger.info("Shop link clicked "+"//a[@id='our-products']");
	}

	public void clickOnAllProductsLink(){
		try{
			//driver.waitForElementPresent(By.xpath("//a[@title='All Products']"));
			driver.findElement(By.xpath("//a[@title='All Products']")).click();
		}catch(NoSuchElementException e){
			logger.info("All products link was not present");
			driver.findElement(By.xpath("//div[@id='dropdown-menu']//a[@href='/us/quick-shop/quickShop']")).click();
		}
		logger.info("All products link clicked "+"//a[@title='All Products']");	
		driver.waitForPageLoad();
	}

	public StoreFrontUpdateCartPage clickOnQuickShopImage(){
		driver.waitForElementToBeVisible(By.xpath("//a[@href='/us/quick-shop/quickShop' and @title='']"), 30);
		driver.waitForElementPresent(By.xpath("//a[@href='/us/quick-shop/quickShop' and @title='']"));
		driver.findElement(By.xpath("//a[@href='/us/quick-shop/quickShop' and @title='']")).click();
		logger.info("Quick shop Img link clicked "+"//a[@href='/us/quick-shop/quickShop' and @title='']");
		return new StoreFrontUpdateCartPage(driver);
	}

	public boolean areProductsDisplayed(){
		driver.waitForElementPresent(By.xpath("//div[contains(@class,'quickshop-section')]"));
		return driver.isElementPresent(By.xpath("//div[contains(@class,'quickshop-section')]"));
	}

	public void selectProductAndProceedToBuy() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Buy now']"));
		driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Buy now']")).click();
		logger.info("Buy Now button clicked");
		driver.waitForPageLoad();
	}

	public void selectProductAndProceedToAddToCRP() throws InterruptedException{
		try{
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//select"));
			driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//select")).click();
			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//select/option[2]"));
			driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//select/option[2]")).click();
		}catch(NoSuchElementException e){

		}
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Add to crp']"));
		driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[1]//input[@value='Add to crp']")).click();
		logger.info("Add To CRP button clicked");
	}

	public void addQuantityOfProduct(String qty) throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@id='quantity0']"));
		driver.findElement(By.xpath("//input[@id='quantity0']")).clear();
		driver.findElement(By.xpath("//input[@id='quantity0']")).sendKeys(qty);
		driver.findElement(By.xpath("//a[@class='updateLink']")).click();		
	}

	public void clickOnNextBtnAfterAddingProductAndQty() throws InterruptedException{
		driver.waitForElementPresent(By.id("submitForm)"));
		driver.findElement(By.id("submitForm")).click();
		Thread.sleep(5000);
	}

	public boolean isCartPageDisplayed(){
		driver.waitForElementPresent(By.xpath("//div[@id='left-shopping']/h1"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='left-shopping']/h1")));
	}

	public void addAnotherProduct() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//p[@class='floated-right']//a[contains(text(),'Continue shopping')]"));
		driver.findElement(By.xpath("//p[@class='floated-right']//a[contains(text(),'Continue shopping')]")).click();
		logger.info("Continue shopping link clicked");
		driver.waitForPageLoad();
		//		try{
		//			driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[2]//select"));
		//			driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[2]//select")).click();
		//			Thread.sleep(2000);
		//			driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[2]//select/option[2]")).click();
		//		}catch(NoSuchElementException e){
		//
		//		}		
		driver.waitForElementPresent(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[2]//input[@value='Buy now']"));
		driver.findElement(By.xpath("//div[@id='main-content']/div[@class='quick-product-wrapper'][1]/div[2]//input[@value='Buy now']")).click();
		logger.info("Buy Now button clicked and another product selected");
		driver.waitForPageLoad();
	}

	public boolean verifyNumberOfProductsInCart(String numberOfProductsInCart){
		driver.waitForElementPresent(By.xpath("//div[@id='left-shopping']/h1/span"));
		return driver.findElement(By.xpath("//div[@id='left-shopping']/h1/span")).getText().contains(numberOfProductsInCart);
	}

	public void clickOnCheckoutButton(){
		driver.waitForElementPresent(By.xpath("//input[@value='checkout']"));
		driver.findElement(By.xpath("//input[@value='checkout']")).click();
		logger.info("checkout button clicked");
		driver.waitForPageLoad();
	}

	public boolean isLoginOrCreateAccountPageDisplayed(){
		driver.waitForElementPresent(By.xpath("//div[@id='content-full-page']/h1[text()='Log in or create an account']"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='content-full-page']/h1[text()='Log in or create an account']")));
	}

	public void enterNewRCDetails() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String firstName="RCUser";
		String lastName = "Test";
		String emailAddress = firstName+randomNum+"@xyz.com";
		driver.findElement(By.xpath("//input[@id='first-Name']")).sendKeys(firstName);
		driver.findElement(By.xpath("//input[@id='last-name']")).sendKeys(lastName);
		driver.findElement(By.xpath("//input[@id='email-account']")).sendKeys(emailAddress+"\t");
		driver.waitForLoadingImageToDisappear();
		driver.findElement(By.xpath("//input[@id='password']")).sendKeys(TestConstants.CONSULTANT_PASSWORD_STG2);
		driver.findElement(By.xpath("//input[@id='the-password-again']")).sendKeys(TestConstants.CONSULTANT_PASSWORD_STG2);		
		driver.findElement(By.xpath("//input[@id='next-button']")).click();		
		logger.info("Create New Account button clicked");
		driver.waitForLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public void enterNewPCDetails() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String firstName="PCUser";
		String lastName = "Test";
		String emailAddress = firstName+randomNum+"@xyz.com";
		driver.findElement(By.xpath("//input[@id='first-Name']")).sendKeys(firstName);
		driver.findElement(By.xpath("//input[@id='last-name']")).sendKeys(lastName);
		driver.findElement(By.xpath("//input[@id='email-account']")).sendKeys(emailAddress+"\t");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@id='password']")).sendKeys(TestConstants.CONSULTANT_PASSWORD_STG2);
		driver.findElement(By.xpath("//input[@id='the-password-again']")).sendKeys(TestConstants.CONSULTANT_PASSWORD_STG2);
		driver.findElement(By.xpath("//input[@id='become-pc']/..")).click();
		driver.findElement(By.xpath("//input[@id='next-button']")).click();		
		logger.info("Create New Account button clicked");		
	}

	public boolean isPopUpForPCThresholdPresent() throws InterruptedException{
		boolean isPopUpForPCThresholdPresent=false;
		driver.waitForElementPresent(By.xpath("//div[@id='popup-content']//p[contains(text(),'Please add products in your PC cart greater than the threshold CAD $90')]"));
		isPopUpForPCThresholdPresent = driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='popup-content']//p[contains(text(),'Please add products in your PC cart greater than the threshold CAD $90')]")));
		if(isPopUpForPCThresholdPresent==true){
			driver.findElement(By.xpath("//div[@id='popup-content']//input")).click();
			Thread.sleep(5000);
			return true;
		}
		return false;
	}

	public boolean isCheckoutPageDisplayed(){
		driver.waitForElementPresent(By.xpath("//h1[contains(text(),'Checkout')]"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//h1[contains(text(),'Checkout')]")));
	}

	public void enterMainAccountInfo(){
		driver.findElement(By.xpath("//input[@id='address.line1']")).sendKeys(TestConstants.ADDRESS_LINE_1);
		driver.findElement(By.xpath("//input[@id='address.townCity']")).sendKeys(TestConstants.CITY);
		Select select = new Select(driver.findElement(By.xpath("//form[@id='addressForm']//select[@id='state']")));
		select.selectByVisibleText(TestConstants.PROVINCE);
		driver.findElement(By.xpath("//input[@id='address.postcode']")).sendKeys(TestConstants.POSTAL_CODE);
		driver.findElement(By.xpath("//input[@id='address.phonenumber']")).sendKeys(TestConstants.PHONE_NUMBER);		
	}

	public void clickOnContinueWithoutSponsorLink() throws InterruptedException{
		Thread.sleep(3000);
		driver.waitForElementPresent(By.xpath("//a[@id='continue-no-sponsor']"));
		driver.findElement(By.xpath("//a[@id='continue-no-sponsor']")).click();
		Thread.sleep(5000); // taking time,will remove		
	}

	public void clickOnNextButtonAfterSelectingSponsor() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@id='saveAccountAddress']"));
		driver.findElement(By.xpath("//input[@id='saveAccountAddress']")).click();
		Thread.sleep(5000);
		try{
			driver.waitForElementPresent(By.xpath("//input[@id='QAS_AcceptOriginal']"));
			driver.findElement(By.xpath("//input[@id='QAS_AcceptOriginal']")).click();
			Thread.sleep(5000); 
		}catch(NoSuchElementException e){

		}
	}

	public void clickOnShippingAddressNextStepBtn() throws InterruptedException{
		Actions action = new Actions(RFWebsiteDriver.driver);
		Thread.sleep(5000);
		driver.waitForElementPresent(By.xpath("//input[@id='saveShippingInfo']"));
		action.moveToElement(driver.findElement(By.xpath("//input[@id='saveShippingInfo']"))).click(driver.findElement(By.xpath("//input[@id='saveShippingInfo']"))).build().perform();
		logger.info("Next button on shipping address clicked");
		Thread.sleep(15000);
	}

	public void enterNewBillingCardNumber(String cardNumber){
		driver.waitForElementPresent(By.id("card-nr"));
		driver.type(By.id("card-nr"), cardNumber);
	}

	public void enterNewBillingNameOnCard(String nameOnCard){
		driver.waitForElementPresent(By.xpath("//input[@id='card-name']"));
		driver.findElement(By.xpath("//input[@id='card-name']")).clear();
		driver.findElement(By.xpath("//input[@id='card-name']")).sendKeys(nameOnCard);
	}

	public void selectNewBillingCardExpirationDate(){
		driver.findElement(By.xpath("//select[@id='expiryMonth']")).click();
		driver.waitForElementPresent(By.xpath("//select[@id='expiryMonth']/option[10]"));
		driver.findElement(By.xpath("//select[@id='expiryMonth']/option[10]")).click();
		driver.findElement(By.xpath("//select[@id='expiryYear']")).click();
		driver.waitForElementPresent(By.xpath("//select[@id='expiryYear']/option[10]"));
		driver.findElement(By.xpath("//select[@id='expiryYear']/option[10]")).click();
	}

	public void enterNewBillingSecurityCode(String securityCode){
		driver.type(By.id("security-code"), securityCode);
	}

	public void selectNewBillingCardAddress() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//*[@id='addressBookdropdown']"));
		driver.findElement(By.xpath("//*[@id='addressBookdropdown']")).click();
		Thread.sleep(3000);		
		driver.findElement(By.xpath("//*[@id='addressBookdropdown']/option[1]")).click();
		logger.info("New Billing card address selected");
	}

	public void clickOnSaveBillingProfile() throws InterruptedException{
		driver.waitForElementPresent(By.id("submitButton"));
		Thread.sleep(2000);
		driver.click(By.id("submitButton"));
		Thread.sleep(60000); // Save Billing taking too long,will remove later
		logger.info("Save billing profile button clicked");
	}

	public void clickOnBillingNextStepBtn() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//div[@id='payment-next-button']/input"));
		Thread.sleep(10000);
		driver.waitForElementToBeClickable(driver.findElement(By.xpath("//div[@id='payment-next-button']/input")), 30);
		driver.findElement(By.xpath("//div[@id='payment-next-button']/input")).click();
		logger.info("Next button on billing profile clicked");
		Thread.sleep(3000);
	}

	public void clickOnSetupCRPAccountBtn() throws InterruptedException{
		Thread.sleep(5000);
		driver.waitForElementPresent(By.xpath("//input[@class='positive right pad_right ']"));
		driver.findElement(By.xpath("//ul[@style='cursor: pointer;']/li[1]/div")).click();
		driver.findElement(By.xpath("//ul[@style='cursor: pointer;']/li[3]/div")).click();
		driver.findElement(By.xpath("//ul[@style='cursor: pointer;']/li[4]/div")).click();
		driver.findElement(By.xpath("//input[@class='positive right pad_right ']")).click();
		logger.info("Next button on billing profile clicked");
		Thread.sleep(3000);
	}


	public void clickPlaceOrderBtn()throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@id='placeOrderButton']"));
		driver.findElement(By.xpath("//input[@id='placeOrderButton']")).click();
		Thread.sleep(3000);
		logger.info("Place order button clicked");
	}

	public void clickOnRodanAndFieldsLogo(){
		driver.waitForElementPresent(RODAN_AND_FIELDS_IMG_LOC);
		driver.findElement(RODAN_AND_FIELDS_IMG_LOC).click();
		logger.info("Rodan and Fields logo clicked");		
	}

	public boolean isOrderPlacedSuccessfully(){
		driver.waitForElementPresent(By.xpath("//div[@id='confirm-left-shopping']/h1[text()='Thank you for your order']"));
		return driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='confirm-left-shopping']/h1[text()='Thank you for your order']")));
	}

	public boolean verifyWelcomeDropdownToCheckUserRegistered(){		
		driver.waitForElementPresent(By.xpath("//li[@id='account-info-button']/a"));
		return driver.findElement(By.xpath("//li[@id='account-info-button']/a")).getText().contains("Welcome");
	}

	public void applyPriceFilterLowToHigh() throws InterruptedException{
		driver.waitForElementPresent(By.xpath("//input[@class='refine-products-button'][contains(@value,'Price')]"));
		driver.findElement(By.xpath("//input[@class='refine-products-button'][contains(@value,'Price')]")).click();
		driver.findElement(By.xpath("//input[@class='refine-products-button'][contains(@value,'Price')]/following::li[1]/form/div")).click();
		Thread.sleep(5000);
	}

	public boolean verifyPCPerksTermsAndConditionsPopup() throws InterruptedException{
		boolean isPCPerksTermsAndConditionsPopup = false;
		driver.waitForElementPresent(By.xpath("//div[@id='pcperktermsconditions']//input[@value='Ok']"));
		isPCPerksTermsAndConditionsPopup = driver.IsElementVisible(driver.findElement(By.xpath("//div[@id='pcperktermsconditions']//input[@value='Ok']")));
		if(isPCPerksTermsAndConditionsPopup==true){
			driver.findElement(By.xpath("//div[@id='pcperktermsconditions']//input[@value='Ok']")).click();
			Thread.sleep(3000);
			return true;
		}
		return false;
	}

	public void clickOnPCPerksTermsAndConditionsCheckBoxes(){
		driver.waitForElementToBeClickable(By.xpath("//input[@id='Terms2']/.."), 15);
		driver.findElement(By.xpath("//input[@id='Terms2']/..")).click();
		driver.findElement(By.xpath("//input[@id='Terms3']/..")).click();
	}

	public boolean selectNewBillingCardExpirationDateAsExpiredDate()	 {
		driver.findElement(By.xpath("//select[@id='expiryMonth']")).click();
		driver.waitForElementPresent(By.xpath("//select[@id='expiryMonth']/option[@value='01']"));
		driver.findElement(By.xpath("//select[@id='expiryMonth']/option[@value='01']")).click();
		driver.findElement(By.xpath("//select[@id='expiryYear']")).click();
		driver.waitForElementPresent(By.xpath("//select[@id='expiryYear']/option[1]"));
		driver.findElement(By.xpath("//select[@id='expiryYear']/option[1]")).click();
		driver.findElement(By.xpath("//select[@id='expiryMonth']")).click();
		driver.waitForElementPresent(By.xpath("//select[@id='expiryMonth']/option[@value='01']"));
		//driver.findElement(By.xpath("//select[@id='expiryMonth']/option[@value='01']")).click();
		return driver.findElement(By.xpath("//select[@id='expiryMonth']/option[@value='01']")).isEnabled();
	}

	
	public boolean validatePasswordFieldMessage(){
		if(driver.findElement(By.xpath("//div[contains(text(),'Please enter 6')]")).isDisplayed()){
			return true;
		}
		else{
			return false;
		}
	}
	public void clearPasswordField(){
		driver.findElement(By.xpath("//input[@id='new-password-account']")).clear();
	}

	public boolean recurringMonthlyChargesSection() {
		driver.waitForElementPresent(By.xpath("//h3[contains(text(),'Recurring Monthly Charges')]"));
		return driver.findElement(By.xpath("//h3[contains(text(),'Recurring Monthly Charges')]")).isDisplayed();
	}

	public boolean pulseSubscriptionTextbox() {
		driver.waitForElementPresent(By.xpath("//input[@id='webSitePrefix']"));
		return driver.findElement(By.xpath("//input[@id='webSitePrefix']")).isEnabled();
	}

	public void clickOnAllowMySpouseOrDomesticPartnerCheckbox() {
		//driver.waitForElementToBeVisible(By.xpath("//input[@id='spouse-check']"), 15);
		boolean status=driver.findElement(By.xpath("//input[@id='spouse-check']/..")).isSelected();
		if(status==false){
			driver.findElement(By.xpath("//input[@id='spouse-check']/..")).click();
		}
	}

	public void enterSpouseFirstName(String firstName){
		driver.waitForElementPresent(By.id("spouse-first"));
		driver.findElement(By.id("spouse-first")).sendKeys(firstName);
		logger.info("Spouse first name entered as "+firstName);
	}

	public void enterSpouseLastName(String firstName){
		driver.waitForElementPresent(By.id("spouse-last"));
		driver.findElement(By.id("spouse-last")).sendKeys(firstName);
		logger.info("Spouse last name entered as "+firstName);
	}

}