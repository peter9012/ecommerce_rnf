package com.rf.pages.website.crm;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

import com.rf.core.driver.website.RFWebsiteDriver;

public class CRMAccountDetailsPage extends CRMRFWebsiteBasePage {
	private static final Logger logger = LogManager
			.getLogger(CRMAccountDetailsPage.class.getName());

	public CRMAccountDetailsPage(RFWebsiteDriver driver) {
		super(driver);
	}

	public boolean isAccountDetailsPagePresent(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[2]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		return driver.findElement(By.xpath("//h2[contains(@class,'mainTitle')]")).getText().contains("Account Detail");		
	}

	public boolean isAccountTypeFieldDisplayedAndNonEmpty(){
		driver.switchTo().defaultContent();
		return !(driver.findElement(By.xpath("//div[text()='Account Type']/following::div[2]")).getText().isEmpty());
	}

	public boolean isAccountNumberFieldDisplayedAndNonEmpty(){
		driver.switchTo().defaultContent();
		return !(driver.findElement(By.xpath("//div[text()='Account Number']/following::div[2]")).getText().isEmpty());
	}

	public boolean isAccountStatusFieldDisplayedAndNonEmpty(){
		driver.switchTo().defaultContent();
		return !(driver.findElement(By.xpath("//div[text()='Account Status']/following::div[2]")).getText().isEmpty());
	}

	public boolean isCountryFieldDisplayedAndNonEmpty(){
		driver.switchTo().defaultContent();
		return !(driver.findElement(By.xpath("//div[text()='Country']/following::div[2]")).getText().isEmpty());
	}

	public boolean isPlacementSponsorFieldDisplayed(){
		driver.switchTo().defaultContent();
		return !(driver.findElement(By.xpath("//div[text()='Placement Sponsor']")).getText().isEmpty());
	}

	public boolean isEnrollmentDateFieldDisplayedAndNonEmpty(){
		driver.switchTo().defaultContent();
		return !(driver.findElement(By.xpath("//div[text()='Enrollment Date']/following::div[2]")).getText().isEmpty());
	}

	public boolean isMainPhoneFieldDisplayedAndNonEmpty(){
		driver.switchTo().defaultContent();
		return !(driver.findElement(By.xpath("//div[text()='Main Phone']")).getText().isEmpty());
	}

	public boolean isEmailAddressFieldDisplayedAndNonEmpty(){
		driver.switchTo().defaultContent();
		return !(driver.findElement(By.xpath("//div[text()='Email Address']/following::div[2]")).getText().isEmpty());
	}

	public boolean isAccountDetailsSectionPresent(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		return driver.isElementPresent(By.xpath("//h2[@class='mainTitle']/following::div[@class='pbBody'][1]"));
	}

	public boolean isMainAddressSectionPresent(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		return driver.isElementPresent(By.xpath("//h3[contains(text(),'Main Address')]/following::table[@class='detailList'][1]"));
	}

	public boolean isTaxInformationSectionPresent(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.click(By.xpath("//h3[contains(text(),'Tax Information')]/preceding::img[1]"));
		return driver.isElementPresent(By.xpath("//h3[contains(text(),'Tax Information')]/following::table[@class='detailList']/ancestor::div[1][@style='display: block;']"));
	}

	public boolean isAccountDetailsButtonEnabled(String buttonName){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@title='AccountButtons']")));		
		return driver.findElement(By.xpath("//input[@value='"+buttonName+"']")).isEnabled();
	}

	public void clickAccountDetailsButton(String buttonName){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@title='AccountButtons']")));		
		driver.click(By.xpath("//input[@value='"+buttonName+"']"));
	}

	public boolean isSVSectionPresentOnPulsePage(){
		try{
			driver.waitForElementPresent(By.xpath("//span[contains(text(),'Dismiss')]"));
			driver.click(By.xpath("//span[contains(text(),'Dismiss')]"));
			System.out.println("Dismiss button clicked");
		}catch(Exception e){
			System.out.println("no dismiss button");
		}
		try{
			driver.waitForElementPresent(By.xpath("//span[@class='Number']"));
			return driver.isElementPresent(By.xpath("//span[@class='Number']"));
		}
		finally{
			System.out.println("finally block");
			switchToPreviousTab();
		}
	}

	public boolean isLabelOnAccountDetailsSectionPresent(String label){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		return driver.isElementPresent(By.xpath("//td[text()='"+label+"']"));
	}

	public boolean isLabelOnMainAddressSectionPresent(String label){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		return driver.isElementPresent(By.xpath("//h3[contains(text(),'Main Address')]/following::table[@class='detailList'][1]//td[text()='"+label+"']"));
	}

	public boolean isLabelOnShippingAddressSectionPresent(String label){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		return driver.isElementPresent(By.xpath("//h3[contains(text(),'Shipping Profiles')]/following::table[@class='list'][1]//th[text()='"+label+"']"));
	}

	public String getShippingProfilesCount(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		return String.valueOf(driver.findElements(By.xpath("//h3[contains(text(),'Shipping Profiles')]/following::table[@class='list'][1]//tr[contains(@class,'dataRow')]")).size());		
	}

	public String getCountDisplayedWithLink(String link){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));	
		String completeCountString = driver.findElement(By.xpath("//span[@class='listTitle'][contains(text(),'"+link+"')]/span[@class='count']")).getText();
		return completeCountString.substring(1, completeCountString.length()-1);		
	}

	public boolean isOnlyOneShippingProfileIsDefault(){
		int defaultShippingProfilesCount = driver.findElements(By.xpath("//h3[contains(text(),'Shipping Profiles')]/following::table[@class='list'][1]//tr[contains(@class,'dataRow')]//img[@title='Checked']")).size();
		if(defaultShippingProfilesCount==1){
			return true;
		}
		return false;
	}

	public boolean isLabelUnderTaxInformationSectionPresent(String label){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		return driver.isElementPresent(By.xpath("//h3[contains(text(),'Tax Information')]/following::table[@class='detailList']//td[text()='"+label+"']"));
	}

	public boolean isLinkOnAccountSectionPresent(String link){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		return driver.isElementPresent(By.xpath("//span[@class='listTitle'][contains(text(),'"+link+"')]"));
	}


	public boolean isMouseHoverSectionPresentOfLink(String link){
		Actions actions = new Actions(RFWebsiteDriver.driver);
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		actions.moveToElement(driver.findElement(By.xpath("//span[@class='listTitle'][contains(text(),'"+link+"')]"))).build().perform();
		driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@id='RLPanelFrame']")));		
		return driver.isElementPresent(By.xpath("//h3[contains(text(),'"+link+"')]"));
	}

	public boolean isListItemsWithBlueLinePresent(String item){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		return driver.isElementPresent(By.xpath("//div[contains(@class,'bRelatedList')]//h3[contains(text(),'"+item+"')]"));
	}

	public boolean isLogAccountActivitySectionIsPresent(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]")));
		return driver.findElement(By.xpath("//div[@id='accountNoteId:noteFrm:pnlPage']/div[@class='header']")).getText().contains("Log Account Activity");  
	}

	public boolean isAccountDropdownOnAccountDetailPagePresent(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]")));
		return driver.isElementPresent(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Account')]/following::span[1]/input"));  
	}

	public boolean isAccountDropdownSearchOnAccountDetailPagePresent(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]")));
		return driver.isElementPresent(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Account')]/following::span[1]/a"));  
	}

	public boolean isChannelDropdownOnAccountDetailPagePresent(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]")));
		return driver.isElementPresent(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Channel')]/following::select[1]"));  
	}

	public boolean isChannelDropdownOptionsPresent(String option){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]")));
		return driver.isElementPresent(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Channel')]/following::select[1]/option[@value='"+option+"']"));
	}

	public boolean isReasonDropdownOnAccountDetailPagePresent(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]")));
		return driver.isElementPresent(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Reason')]/following::select[1]"));  
	}

	public boolean isReasonDropdownOptionsPresent(String option){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]")));
		return driver.isElementPresent(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Reason')]/following::select[1]/option[@value='"+option+"']"));
	}

	public boolean isDetailsDropdownOnAccountDetailPagePresent(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]")));
		return driver.isElementPresent(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Detail')]/following::select[1]"));  
	}

	public boolean isNoteSectionOnAccountDetailPagePresent(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]")));
		return driver.isElementPresent(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Notes')]/following::textarea"));  
	}

	public void selectChannelDropdown(String dropdownValue){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]")));
		driver.click(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Channel')]/following::select[1]"));
		driver.waitForElementPresent(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Channel')]/following::select[1]/option[@value='"+dropdownValue+"']"));
		driver.click(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Channel')]/following::select[1]/option[@value='"+dropdownValue+"']"));
	}

	public void selectReasonDropdown(String dropdownValue){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]")));
		driver.click(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Reason')]/following::select[1]"));
		driver.waitForElementPresent(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Reason')]/following::select[1]/option[@value='"+dropdownValue+"']"));
		driver.click(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Reason')]/following::select[1]/option[@value='"+dropdownValue+"']"));
	}

	public void selectDetailDropdown(String dropdownValue){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]")));
		driver.click(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Detail')]/following::select[1]"));
		driver.waitForElementPresent(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Detail')]/following::select[1]/option[@value='"+dropdownValue+"']"));
		driver.click(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Detail')]/following::select[1]/option[@value='"+dropdownValue+"']"));
	}

	public void enterNote(String orderNote){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]")));
		driver.type(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Notes')]/following::textarea"), orderNote);
		//driver.findElement(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Notes')]/following::textarea")).sendKeys(orderNote);
		logger.info("Entered order note is "+orderNote);
	}

	public void clickOnSaveAfterEnteringNote(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]")));
		driver.waitForElementPresent(By.xpath("//a[@id='accountNoteId:noteFrm:save']"));
		driver.click(By.xpath("//a[@id='accountNoteId:noteFrm:save']"));
		driver.waitForCRMLoadingImageToDisappear();
		driver.waitForPageLoad();
	}

	public boolean isAccountDropdownOnAccountDetailPageIsEnabled(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]")));
		return driver.findElement(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Account')]/following::span[1]/input")).isEnabled();
	}

	public boolean isChannelDropdownOnAccountDetailPageIsEnabled(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]")));
		return driver.findElement(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Channel')]/following::select[1]")).isEnabled();
	}

	public boolean isDetailDropdownOnAccountDetailPageIsEnabled(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[3]/descendant::iframe[1]")));
		return driver.findElement(By.xpath("//div[@id='mainDiv']//div[contains(text(),'Detail')]/following::select[1]")).isEnabled();
	}

	public String getNoteFromUIOnAccountDetailPage(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[2]/div[2]/div/div/div/iframe"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/div[2]/div[2]/div/div/div/iframe")));
		String nodeText= driver.findElement(By.xpath("//h3[text()='Account Activities']/following::tr/th[text()='Notes']/following::td[3]")).getText();
		return nodeText;
	}

	public void updateRecognitionNameField(String RecognitionName){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.findElement(By.xpath("//label[contains(text(),'Recognition Name')]/following::input[1]")).clear();
		driver.type(By.xpath("//label[contains(text(),'Recognition Name')]/following::input[1]"), RecognitionName);
	}

	public void clickSaveBtnUnderAccountDetail(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.click(By.xpath("//td[@id='topButtonRow']/input[@title='Save']"));
		driver.waitForPageLoad();
	}

	public String getRecognitionName(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		return driver.findElement(By.xpath("//td[contains(text(),'Recognition Name')]/following-sibling::td[1]")).getText();
	}

	public void updateAddressLine3Field(String AddressLine3){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.findElement(By.xpath("//label[contains(text(),'ddress Line 3')]/following::input[1]")).clear();
		driver.type(By.xpath("//label[contains(text(),'ddress Line 3')]/following::input[1]"), AddressLine3);
	}

	public String getMainAddressLine3(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		return driver.findElement(By.xpath("//td[contains(text(),'Address Line 3')]/following-sibling::td[1]")).getText();
	}

	public boolean verifyShippingProfileCountIsEqualOrGreaterThanOne(String noOfShippingProfile){
		int noOfProfile = Integer.parseInt(noOfShippingProfile);
		return noOfProfile>=1;
	}

	public void clickEditFirstShippingProfile(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.click(By.xpath("//h3[contains(text(),'Shipping Profiles')]/following::table[@class='list'][1]//tr[2]//a[text()='Edit']"));
		driver.waitForCRMLoadingImageToDisappear();
	}

	public void updateShippingProfileName(String profileName){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[2]")));
		driver.type(By.xpath("//label[contains(text(),'ProfileName')]/following::input[1]"), profileName);
	}

	public void clickSaveBtnAfterEditShippingAddress(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[2]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[2]")));
		driver.click(By.xpath("//td[@id='topButtonRow']/input[1]"));
		driver.waitForCRMLoadingImageToDisappear();
	}

	public void closeSubTabOfEditShippingProfile(){
		driver.switchTo().defaultContent();
		Actions actions = new Actions(RFWebsiteDriver.driver);
		actions.moveToElement(driver.findElement(By.xpath("//div[@id='servicedesk']//div[3]//div[3]//ul[@class='x-tab-strip x-tab-strip-top']/li[2]//a[@class='x-tab-strip-close']"))).click().build().perform();
	}

	public String getFirstShippingProfileName(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		return driver.findElement(By.xpath("//h3[contains(text(),'Shipping Profiles')]/following::table[@class='list'][1]//tr[2]/td[2]")).getText();
	}

	public void clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[2]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[2]")));
		if(driver.isElementPresent(By.xpath("//label[text()='Default']/following::input[1][@checked='checked']"))==true){
			logger.info("CheckBox is already selected");
		}else{
			driver.click(By.xpath("//label[text()='Default']/following::input[1]"));
		}
	}

	public void clickAddNewShippingProfileBtn(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		driver.click(By.xpath("//input[@value='New Shipping Profile']"));
		driver.waitForCRMLoadingImageToDisappear();
	}

	public void enterShippingAddress(String addressLine1, String city, String state, String postalCode, String phoneNumber){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[2]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[2]")));
		driver.waitForElementPresent(By.xpath("//label[contains(text(),'Address Line 1')]/following::input[1]"));
		driver.type(By.xpath("//label[contains(text(),'Address Line 1')]/following::input[1]"), addressLine1);
		driver.type(By.xpath("//label[contains(text(),'Locale')]/following::input[1]"), city);
		driver.type(By.xpath("//label[text()='Region']/following::input[1]"), state);
		driver.type(By.xpath("//label[contains(text(),'Postal code')]/following::input[1]"), postalCode);
		driver.type(By.xpath("//label[contains(text(),'Phone')]/following::input[1]"), phoneNumber);
	}

	public String getDefaultSelectedShippingAddressName(){
		driver.switchTo().defaultContent();
		driver.waitForElementPresent(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]"));
		driver.switchTo().frame(driver.findElement(By.xpath("//div[@id='navigatortab']/div[3]/div/div[3]/descendant::iframe[1]")));
		return driver.findElement(By.xpath("//h3[contains(text(),'Shipping Profiles')]/following::table[@class='list'][1]//img[@title='Checked']/../preceding::td[1]")).getText();
	}

}
