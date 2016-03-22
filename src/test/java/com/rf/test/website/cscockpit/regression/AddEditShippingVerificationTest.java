package com.rf.test.website.cscockpit.regression;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.cscockpit.CSCockpitAutoshipCartTabPage;
import com.rf.pages.website.cscockpit.CSCockpitAutoshipSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitAutoshipTemplateTabPage;
import com.rf.pages.website.cscockpit.CSCockpitAutoshipTemplateUpdateTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCartTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCheckoutTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCustomerSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCustomerTabPage;
import com.rf.pages.website.cscockpit.CSCockpitLoginPage;
import com.rf.pages.website.cscockpit.CSCockpitOrderSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitOrderTabPage;
import com.rf.pages.website.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;


public class AddEditShippingVerificationTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(AddEditShippingVerificationTest.class.getName());

	//-------------------------------------------------Pages---------------------------------------------------------
	private CSCockpitLoginPage cscockpitLoginPage;	
	private CSCockpitAutoshipSearchTabPage cscockpitAutoshipSearchTabPage;
	private CSCockpitCheckoutTabPage cscockpitCheckoutTabPage;
	private CSCockpitCustomerSearchTabPage cscockpitCustomerSearchTabPage;
	private CSCockpitCustomerTabPage cscockpitCustomerTabPage;
	private CSCockpitOrderSearchTabPage cscockpitOrderSearchTabPage;
	private CSCockpitOrderTabPage cscockpitOrderTabPage;
	private CSCockpitCartTabPage cscockpitCartTabPage;
	private CSCockpitAutoshipTemplateTabPage cscockpitAutoshipTemplateTabPage;
	private CSCockpitAutoshipCartTabPage cscockpitAutoshipCartTabPage;
	private CSCockpitAutoshipTemplateUpdateTabPage cscockpitAutoshipTemplateUpdateTabPage;
	private StoreFrontHomePage storeFrontHomePage; 
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;	
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;

	//-----------------------------------------------------------------------------------------------------------------

	public AddEditShippingVerificationTest() {
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		cscockpitAutoshipSearchTabPage = new CSCockpitAutoshipSearchTabPage(driver);
		cscockpitCheckoutTabPage = new CSCockpitCheckoutTabPage(driver);
		cscockpitCustomerSearchTabPage = new CSCockpitCustomerSearchTabPage(driver);
		cscockpitCustomerTabPage = new CSCockpitCustomerTabPage(driver);
		cscockpitOrderSearchTabPage = new CSCockpitOrderSearchTabPage(driver);
		cscockpitOrderTabPage = new CSCockpitOrderTabPage(driver);
		cscockpitCartTabPage = new CSCockpitCartTabPage(driver);
		cscockpitAutoshipTemplateTabPage = new CSCockpitAutoshipTemplateTabPage(driver);
		cscockpitAutoshipCartTabPage = new CSCockpitAutoshipCartTabPage(driver);
		cscockpitAutoshipTemplateUpdateTabPage = new CSCockpitAutoshipTemplateUpdateTabPage(driver);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
		storeFrontOrdersPage = new StoreFrontOrdersPage(driver);
		storeFrontPCUserPage = new StoreFrontPCUserPage(driver);
		storeFrontRCUserPage = new StoreFrontRCUserPage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontAccountInfoPage = new StoreFrontAccountInfoPage(driver);
	}

	private String RFO_DB = null;

	//Hybris Project-1754:To verify Edit address functionality in the Customer detail Page for consultant
	@Test
	public void testVerifyEditAddressFunctionalityInCustomerDetailPageForConsultant_1754() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String consultantEmailID;
		String accountID;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum;
		String attendeeLastName = TestConstants.LAST_NAME;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String country = null;
		String addressLine = null;
		addressLine = TestConstants.ADDRESS_LINE_1_US;
		city = TestConstants.CITY_US;
		postal = TestConstants.POSTAL_CODE_US;
		province = TestConstants.PROVINCE_ALABAMA_US;
		phoneNumber = TestConstants.PHONE_NUMBER_US;
		country = "United States";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		driver.get(driver.getStoreFrontURL()+"/us");
		List<Map<String, Object>> randomConsultantList = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else{
				break;
			}
		}
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		if(storeFrontAccountInfoPage.verifyCRPCancelled()==false){
			storeFrontAccountInfoPage.clickOnCancelMyCRP();
		}
		storeFrontAccountInfoPage.clickOnCancelMyPulseSubscription();
		logout();
		//get emailId of username
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));  
		logger.info("emaild of consultant username "+consultantEmailID);
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.isSetAsAutoshipShippingProfileTxtPresentInAddNewShippingProfilePopup(), "Set as autoship shipping address present in add new shipping address popup  customer tab");
		cscockpitCustomerTabPage.clickCloseOfCreateNewAddressPopUpInCustomerTab();
		cscockpitCustomerTabPage.clickMenuButton();
		cscockpitCustomerTabPage.clickLogoutButton();

		//For Inactive Users
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.addressCanNotBeAddedForInactiveUserInCustomerTab(), "Address can be added for Inactive user");
		cscockpitCustomerTabPage.clickOkBtnOfAddressCanNotBeAddedForInactiveUserInCustomerTab();
		cscockpitCustomerTabPage.clickMenuButton();
		cscockpitCustomerTabPage.clickLogoutButton();

		//For Active user
		driver.get(driver.getStoreFrontURL()+"/us");
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else{
				break;
			}
		}
		logout();
		//get emailId of username
		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));  
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickEditButtonOfShippingAddressInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.isEditAddressPopupPresentInCustomerTab(), "Edit Address popup is not present in billing section of customer tab");
		cscockpitCustomerTabPage.clickCloseOfEditAddressPopUpInCustomerTab();
		cscockpitCustomerTabPage.clickEditButtonOfShippingAddressInCustomerTab();
		cscockpitCustomerTabPage.clickUpdateAddressbtnInEditAddressPopup();
		s_assert.assertTrue(cscockpitCustomerTabPage.isQASpopupPresent(), "QAS popup is not present after clicked on update address without editing data");
		cscockpitCustomerTabPage.clickUseEnteredAddressbtnInEditAddressPopup();
		//without select set as autoship shipping profile check box
		cscockpitCustomerTabPage.clickEditButtonOfShippingAddressInCustomerTab();
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitCustomerTabPage.clickUpdateAddressbtnInEditAddressPopup();
		cscockpitCustomerTabPage.clickUseEnteredAddressbtnInEditAddressPopup();
		s_assert.assertTrue(cscockpitCustomerTabPage.getFirstShippingAddressProfileName().toLowerCase().trim().contains(attendentFirstName.toLowerCase()), "shipping profile name expected in customer tab page "+attendentFirstName+" actual on UI "+cscockpitCustomerTabPage.getFirstShippingAddressProfileName());
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.getShippingAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(attendentFirstName.toLowerCase()),"Shipping Address Name Expected is "+attendentFirstName.toLowerCase()+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressNameInAutoshipTemplateTab());
		cscockpitAutoshipTemplateTabPage.clickCustomerTab();
		//with select set as autoship shipping profile check box
		cscockpitCustomerTabPage.clickEditButtonOfShippingAddressInCustomerTab();
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitCustomerTabPage.clickSetAsAutoshipChkBoxInCreateNewAddressPopup();
		cscockpitCustomerTabPage.clickUpdateAddressbtnInEditAddressPopup();
		cscockpitCustomerTabPage.clickUseEnteredAddressbtnInEditAddressPopup();
		cscockpitCustomerTabPage.clickOnYesOnUpdateAutoshipAddressPopup();
		s_assert.assertTrue(cscockpitCustomerTabPage.getFirstShippingAddressProfileName().toLowerCase().trim().contains(attendentFirstName.toLowerCase()), "shipping profile name expected in customer tab page "+attendentFirstName+" actual on UI "+cscockpitCustomerTabPage.getFirstShippingAddressProfileName());
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getShippingAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(attendentFirstName.toLowerCase()),"Shipping Address Name Expected is "+attendentFirstName.toLowerCase()+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressNameInAutoshipTemplateTab());
		s_assert.assertAll();
	}


	//Hybris Project-1753:To verify Edit address functionality in the Customer detail Page for PC
	@Test
	public void testVerifyEditAddressFunctionalityInCustomerDetailPageForPC_1753() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String pcEmailID;
		String accountID;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum;
		String attendeeLastName = TestConstants.LAST_NAME;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String country = null;
		String addressLine = null;
		addressLine = TestConstants.ADDRESS_LINE_1_US;
		city = TestConstants.CITY_US;
		postal = TestConstants.POSTAL_CODE_US;
		province = TestConstants.PROVINCE_ALABAMA_US;
		phoneNumber = TestConstants.PHONE_NUMBER_US;
		country = "United States";
		List<Map<String, Object>> randomPCList = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);

		//For Inactive Users
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.addressCanNotBeAddedForInactiveUserInCustomerTab(), "Address can be added for Inactive user");
		cscockpitCustomerTabPage.clickOkBtnOfAddressCanNotBeAddedForInactiveUserInCustomerTab();
		cscockpitCustomerTabPage.clickMenuButton();
		cscockpitCustomerTabPage.clickLogoutButton();

		//For Active user
		driver.get(driver.getStoreFrontURL()+"/us");
		while(true){
			randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			pcEmailID = (String) getValueFromQueryResult(randomPCList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomPCList, "AccountID"));
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else{
				break;
			}
		}
		logout();
		//get emailId of username
		List<Map<String, Object>> randomPCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		pcEmailID = String.valueOf(getValueFromQueryResult(randomPCUsernameList, "EmailAddress"));  
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickEditButtonOfShippingAddressInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.isEditAddressPopupPresentInCustomerTab(), "Edit Address popup is not present in billing section of customer tab");
		cscockpitCustomerTabPage.clickCloseOfEditAddressPopUpInCustomerTab();
		cscockpitCustomerTabPage.clickEditButtonOfShippingAddressInCustomerTab();
		cscockpitCustomerTabPage.clickUpdateAddressbtnInEditAddressPopup();
		s_assert.assertTrue(cscockpitCustomerTabPage.isQASpopupPresent(), "QAS popup is not present after clicked on update address without editing data");
		cscockpitCustomerTabPage.clickUseEnteredAddressbtnInEditAddressPopup();
		//without select set as autoship shipping profile check box
		cscockpitCustomerTabPage.clickEditButtonOfShippingAddressInCustomerTab();
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitCustomerTabPage.clickUpdateAddressbtnInEditAddressPopup();
		cscockpitCustomerTabPage.clickUseEnteredAddressbtnInEditAddressPopup();
		s_assert.assertTrue(cscockpitCustomerTabPage.getFirstShippingAddressProfileName().toLowerCase().trim().contains(attendentFirstName.toLowerCase()), "shipping profile name expected in customer tab page "+attendentFirstName+" actual on UI "+cscockpitCustomerTabPage.getFirstShippingAddressProfileName());
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.getShippingAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(attendentFirstName.toLowerCase()),"Shipping Address Name Expected is "+attendentFirstName.toLowerCase()+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressNameInAutoshipTemplateTab());
		cscockpitAutoshipTemplateTabPage.clickCustomerTab();
		//with select set as autoship shipping profile check box
		cscockpitCustomerTabPage.clickEditButtonOfShippingAddressInCustomerTab();
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitCustomerTabPage.clickSetAsAutoshipChkBoxInCreateNewAddressPopup();
		cscockpitCustomerTabPage.clickUpdateAddressbtnInEditAddressPopup();
		cscockpitCustomerTabPage.clickUseEnteredAddressbtnInEditAddressPopup();
		cscockpitCustomerTabPage.clickOnYesOnUpdateAutoshipAddressPopup();
		s_assert.assertTrue(cscockpitCustomerTabPage.getFirstShippingAddressProfileName().toLowerCase().trim().contains(attendentFirstName.toLowerCase()), "shipping profile name expected in customer tab page "+attendentFirstName+" actual on UI "+cscockpitCustomerTabPage.getFirstShippingAddressProfileName());
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getShippingAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(attendentFirstName.toLowerCase()),"Shipping Address Name Expected is "+attendentFirstName.toLowerCase()+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressNameInAutoshipTemplateTab());
		s_assert.assertAll();
	}

	//Hybris Project-1752:To verify Edit address functionality in the Customer detail Page for RC
	@Test
	public void testVerifyEditAddressFunctionalityInCustomerDetailPageForRC_1752() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String rcEmailID;
		String accountID;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum;
		String attendeeLastName = TestConstants.LAST_NAME;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String country = null;
		String addressLine = null;
		addressLine = TestConstants.ADDRESS_LINE_1_US;
		city = TestConstants.CITY_US;
		postal = TestConstants.POSTAL_CODE_US;
		province = TestConstants.PROVINCE_ALABAMA_US;
		phoneNumber = TestConstants.PHONE_NUMBER_US;
		country = "United States";
		List<Map<String, Object>> randomRCList = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);

		//For Inactive Users
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("Retail");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.addressCanNotBeAddedForInactiveUserInCustomerTab(), "Address can be added for Inactive user");
		cscockpitCustomerTabPage.clickOkBtnOfAddressCanNotBeAddedForInactiveUserInCustomerTab();
		cscockpitCustomerTabPage.clickMenuButton();
		cscockpitCustomerTabPage.clickLogoutButton();

		//For Active user
		driver.get(driver.getStoreFrontURL()+"/us");
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_EMAIL_ID_RFO,"236"),RFO_DB);
			rcEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+rcEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else{
				break;
			}
		}
		logout();
		//get emailId of username
		List<Map<String, Object>> randomPCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		rcEmailID = String.valueOf(getValueFromQueryResult(randomPCUsernameList, "EmailAddress"));  
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(rcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickEditButtonOfShippingAddressInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.isEditAddressPopupPresentInCustomerTab(), "Edit Address popup is not present in billing section of customer tab");
		cscockpitCustomerTabPage.clickCloseOfEditAddressPopUpInCustomerTab();
		cscockpitCustomerTabPage.clickEditButtonOfShippingAddressInCustomerTab();
		cscockpitCustomerTabPage.clickUpdateAddressbtnInEditAddressPopup();
		s_assert.assertTrue(cscockpitCustomerTabPage.isQASpopupPresent(), "QAS popup is not present after clicked on update address without editing data");
		cscockpitCustomerTabPage.clickUseEnteredAddressbtnInEditAddressPopup();
		cscockpitCustomerTabPage.clickEditButtonOfShippingAddressInCustomerTab();
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitCustomerTabPage.clickUpdateAddressbtnInEditAddressPopup();
		cscockpitCustomerTabPage.clickUseEnteredAddressbtnInEditAddressPopup();
		s_assert.assertTrue(cscockpitCustomerTabPage.getFirstShippingAddressProfileName().toLowerCase().trim().contains(attendentFirstName.toLowerCase()), "shipping profile name expected in customer tab page "+attendentFirstName+" actual on UI "+cscockpitCustomerTabPage.getFirstShippingAddressProfileName());
		s_assert.assertAll();
	}

}