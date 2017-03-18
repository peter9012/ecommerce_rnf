package com.rf.pages.website.rehabitat.storeFront;

import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.pages.website.rehabitat.storeFront.basePage.StoreFrontWebsiteBasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StoreFrontShippingInfoPage extends StoreFrontWebsiteBasePage{

	public StoreFrontShippingInfoPage(RFWebsiteDriver driver) {
		super(driver);  
	}

	private static final Logger logger = LogManager
			.getLogger(StoreFrontShippingInfoPage.class.getName());

	private final By ADD_NEW_SHIPPING_ADDRESS_LOC = By.xpath("//a[contains(text(),'Add new shipping address')]");
	private final By CANCEL_BUTTON_OF_SHIPPING_ADDRESS_LOC = By.xpath("//div[@class='accountActions']//a");
	private final By ACTION_SUCCESS_MSG_LOC = By.xpath("//div[contains(@class,'alert-info') and contains(@class,'alert-dismissable')]");
	private final By CANCEL_BUTTON_ON_DELETE_SHIPPING_POPUP_LOC = By.xpath("//div[@id='colorbox']//a[contains(text(),'Cancel')]");
	private final By DELETE_BUTTON_ON_DELETE_SHIPPING_POPUP_LOC = By.xpath("//div[@id='colorbox']//a[contains(text(),'Delete')]");
	private final By LEAVE_AS_IS_BUTTON_ON_UPDATE_AUTOSHIP_MODAL_LOC = By.xpath("//div[@class='myModal' and contains(@style,'display')]//a[contains(text(),'LEAVE AS IS')]");
	private final By UPDATE_MY_AUTOSHIP_BUTTON_ON_UPDATE_AUTOSHIP_MODAL_LOC = By.xpath("//div[@class='myModal' and contains(@style,'display')]//a[contains(text(),'UPDATE MY AUTO-SHIP')]");
	private final By NEXT_CRP_DEFAULT_ADDRESS_LOC = By.xpath("//strong[contains(text(),'Next CRP')]//following-sibling::div[@class='address']/strong");
	private final By TOTAL_NO_OF_SHIPPING_PROFILE_LOC = By.xpath("//div[contains(@class,'account-addressbook')]/descendant::div[@class='set-default-addform']");
	private final By AUTOSHIP_SHIPPING_PROFILE_NAME_LOC = By.xpath("//strong[contains(text(),'Next CRP')]/following::strong[1]");
	private final By CANCEL_BUTTON_OF_DELETE_ADDRESS_AND_UPDATE_SHIPPING_ADDRESS_FOR_AUTOSHIP_POPUP_LOC = By.xpath("//div[@id='cboxLoadedContent']//a[contains(text(),'Cancel')]");
	private final By UPDATE_MY_AUTOSHIP_BUTTON_OF_DELETE_ADDRESS_AND_UPDATE_SHIPPING_ADDRESS_FOR_AUTOSHIP_POPUP_LOC = By.xpath("//div[@id='cboxLoadedContent']//a[contains(text(),'Update My Autoship')]");
	private final By EDIT_LINK_OF_DEFAULT_SHIPPING_ADDRESS_LOC = By.xpath("//strong[contains(text(),'(Default)')]/ancestor::ul[1]/following-sibling::div[@class='account-cards-actions']//a[contains(text(),'Edit')]");
	private final By DEFAULT_SHIPPING_ADDRESS_NAME_LOC = By.xpath("//strong[contains(text(),'(Default)')]");
	
	private String defaultLinkForProfileLoc = "//strong[contains(text(),'%s')]/ancestor::ul[1]/following-sibling::div//a[contains(text(),'Default')]";
	private String deleteLinkForProfileLoc = "//strong[contains(text(),'%s')]/ancestor::ul[1]/following-sibling::div[@class='account-cards-actions']//a[contains(text(),'Delete')]";
	private String shippingProfileNameLoc  = "//div[contains(@class,'account-addressbook')]/descendant::strong[contains(text(),'%s')][1]";
	private String shippingEditLoc  = "//*[contains(text(),'%s')]/following::a[contains(text(),'Edit')][1]";


	/***
	 * This method clicked on add a new shipping address link 
	 * 
	 * @param
	 * @return store front shipping info page object
	 * 
	 */
	public StoreFrontShippingInfoPage clickAddANewShippingAddressLink(){
		driver.click(ADD_NEW_SHIPPING_ADDRESS_LOC);
		logger.info("Add new shipping address link clicked");
		return this;
	}

	/***
	 * This method validates that shipping profile is present or not
	 * at shipping info page 
	 * 
	 * @param profile name
	 * @return boolean value
	 * 
	 */
	public boolean isShippingProfilePresent(String profileName){
		return driver.isElementPresent(By.xpath(String.format(shippingProfileNameLoc, profileName)));
	}

	/***
	 * This method validates that shipping address details field is present or not
	 * 
	 * @param
	 * @return boolean value
	 * 
	 */
	public boolean isShippingAddressDetailFieldsPresent(String name){
		try{
			driver.pauseExecutionFor(1000);
			driver.type(FIRST_LAST_NAME_FOR_SHIPPING_DETAILS_LOC, name);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	/***
	 * This method click the cancel button of shipping address
	 * 
	 * @param
	 * @return store front shipping info page object
	 * 
	 */
	public StoreFrontShippingInfoPage clickCancelButtonOfShippingAddress(){
		driver.click(CANCEL_BUTTON_OF_SHIPPING_ADDRESS_LOC);
		logger.info("Cancel button clicked");
		return this;
	}

	/***
	 * This method get the default shipping profile name
	 * 
	 * @param
	 * @return profile name
	 * 
	 */
	public String getDefaultShippingAddressName(){
		driver.pauseExecutionFor(2000);
		String profileName = driver.findElement(DEFAULT_SHIPPING_ADDRESS_NAME_LOC).getText().replace("(DEFAULT)","");
		logger.info("default profile name is "+profileName);
		return profileName;
	}

	/***
	 * This method clicked on edit link of default shipping address 
	 * 
	 * @param
	 * @return store front shipping info page object
	 * 
	 */
	public StoreFrontShippingInfoPage clickEditLinkOfDefaultShippingAddress(){
		driver.click(EDIT_LINK_OF_DEFAULT_SHIPPING_ADDRESS_LOC);
		logger.info("Edit link clicked of default shipping address");
		return this;
	}

	/***
	 * This method clicked on edit link of shipping address based on lastName 
	 * 
	 * @param
	 * @return store front shipping info page object
	 * 
	 */
	public StoreFrontShippingInfoPage clickEditLinkOfShippingAddress(String lastName){
		driver.click(By.xpath(String.format(shippingEditLoc, lastName)));
		logger.info("Edit link clicked for shipping address with lastName "+lastName);
		return this;
	}



	/***
	 * This method fetch the address update success message  
	 * 
	 * @param
	 * @return String
	 * 
	 */
	public String getAddressUpdateSuccessMsg(){
		driver.pauseExecutionFor(2000);
		return driver.getText(ACTION_SUCCESS_MSG_LOC);
	}

	/***
	 * This method clicked on cancel button on Delete shipping popup 
	 * 
	 * @param
	 * @return store front shipping info page object
	 * 
	 */
	public StoreFrontShippingInfoPage clickCancelButtonOnDeleteShippingPopup(){
		driver.click(CANCEL_BUTTON_ON_DELETE_SHIPPING_POPUP_LOC);
		logger.info("Cancel button clicked on delete shipping Address popup");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method clicked on delete button on Delete shipping popup 
	 * 
	 * @param
	 * @return store front shipping info page object
	 * 
	 */
	public StoreFrontShippingInfoPage clickDeleteButtonOnDeleteShippingPopup(){
		driver.click(DELETE_BUTTON_ON_DELETE_SHIPPING_POPUP_LOC);
		logger.info("Delete button clicked on delete shipping Address popup");
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method clicked delete option for given profile
	 * 
	 * @param
	 * @return store front shipping info page object
	 * 
	 */
	public StoreFrontShippingInfoPage clickDeleteLinkForShippingProfile(String profile){
		driver.clickByJS(RFWebsiteDriver.driver,By.xpath(String.format(deleteLinkForProfileLoc, profile)));
		logger.info("Delete Link clicked for profile : " + profile);
		driver.pauseExecutionFor(3000);
		return this;
	}

	/***
	 * This method fetch the total no of shipping profile  
	 * 
	 * @param
	 * @return int
	 * 
	 */
	public int getTotalNoOfShippingProfile(){
		logger.info("Total no of shipping profile "+driver.findElements(TOTAL_NO_OF_SHIPPING_PROFILE_LOC).size());
		return driver.findElements(TOTAL_NO_OF_SHIPPING_PROFILE_LOC).size();
	}

	/***
	 * This method fetch the autoship shipping profile name  
	 * 
	 * @param
	 * @return String
	 * 
	 */
	public String getAutoshipShippingProfileName(){
		logger.info("Autoship shipping profile name "+driver.getText(AUTOSHIP_SHIPPING_PROFILE_NAME_LOC));
		return driver.getText(AUTOSHIP_SHIPPING_PROFILE_NAME_LOC);
	}

	/***
	 * This method click the update my autoship button of delete address and update shipping address popup
	 * 
	 * @param
	 * @return store front shipping info page object
	 * 
	 */
	public StoreFrontShippingInfoPage clickUpdateMyAutoshipOnDeleteAddressAndUpdateShippingAddressForAutoshipPopup(){
		driver.click(UPDATE_MY_AUTOSHIP_BUTTON_OF_DELETE_ADDRESS_AND_UPDATE_SHIPPING_ADDRESS_FOR_AUTOSHIP_POPUP_LOC);
		logger.info("Update my autoship button clicked");
		return this;
	}

	/***
	 * This method fetch validate the autoship shipping profile name  
	 * 
	 * @param
	 * @return boolean
	 * 
	 */
	public boolean isAutoshipShippingProfileNamePresent(){
		logger.info("Autoship shipping profile name "+driver.getText(AUTOSHIP_SHIPPING_PROFILE_NAME_LOC));
		return driver.getText(AUTOSHIP_SHIPPING_PROFILE_NAME_LOC).length()>0;
	}

	/***
	 * This method click the cancel button of delete address and update shipping address popup
	 * 
	 * @param
	 * @return store front shipping info page object
	 * 
	 */
	public StoreFrontShippingInfoPage cancelDeleteAddressAndUpdateShippingAddressForAutoshipPopup(){
		driver.click(CANCEL_BUTTON_OF_DELETE_ADDRESS_AND_UPDATE_SHIPPING_ADDRESS_FOR_AUTOSHIP_POPUP_LOC);
		logger.info("Cancel button clicked");
		driver.pauseExecutionFor(1000);
		return this;
	}

	/***
	 * This method click the default button for specific profile
	 * 
	 * @param
	 * @return store front shipping info page object
	 * 
	 */
	public StoreFrontShippingInfoPage clickDefaultButtonForShippingProfile(String profileName){
		driver.click(By.xpath(String.format(defaultLinkForProfileLoc,profileName)));
		logger.info("Default Button Clicked for Profile : " + profileName);
		return this;
	}

	/***
	 * This method click the 'Leave As Is' Button on update auto ship modal
	 * 
	 * @param
	 * @return store front shipping info page object
	 * 
	 */
	public StoreFrontShippingInfoPage clickLeaveAsIsBtnOnUpdateAutoshipModal(){
		driver.click(LEAVE_AS_IS_BUTTON_ON_UPDATE_AUTOSHIP_MODAL_LOC);
		logger.info("Clicked 'Leave As Is' Button on update Autoship modal");
		driver.waitForPageLoad();
		driver.pauseExecutionFor(2000);
		return this;
	}

	/***
	 * This method click the 'Update my autoship' Button on update auto ship modal
	 * 
	 * @param
	 * @return store front checkout page object
	 * 
	 */
	public StoreFrontCheckoutPage clickUpdateMyAutoshipBtnOnUpdateAutoshipModal(){
		driver.click(UPDATE_MY_AUTOSHIP_BUTTON_ON_UPDATE_AUTOSHIP_MODAL_LOC);
		logger.info("Clicked 'Update my autoship' Button on update AutoShip modal");
		driver.pauseExecutionFor(2000);
		return new StoreFrontCheckoutPage(driver);
	}

	/***
	 * This method get the default address profile Name for Next CRP
	 * 
	 * @param
	 * @return String
	 * 
	 */
	public String getProfileNameForNextCRPDeliveryAddress(){
		String profileName = driver.getText(NEXT_CRP_DEFAULT_ADDRESS_LOC).trim();
		logger.info("Next CRP Delivery Address Profile Name : " + profileName);
		return profileName;
	}

}

