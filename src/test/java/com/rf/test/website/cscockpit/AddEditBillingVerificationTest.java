package com.rf.test.website.cscockpit;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.cscockpit.CSCockpitAutoshipTemplateTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCustomerSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCustomerTabPage;
import com.rf.pages.website.cscockpit.CSCockpitLoginPage;
import com.rf.pages.website.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class AddEditBillingVerificationTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(AddEditBillingVerificationTest.class.getName());

	//-------------------------------------------------Pages---------------------------------------------------------
	private CSCockpitLoginPage cscockpitLoginPage;	
	private CSCockpitCustomerSearchTabPage cscockpitCustomerSearchTabPage;
	private CSCockpitCustomerTabPage cscockpitCustomerTabPage;
	private CSCockpitAutoshipTemplateTabPage cscockpitAutoshipTemplateTabPage;
	private StoreFrontHomePage storeFrontHomePage; 
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;	
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;

	//-----------------------------------------------------------------------------------------------------------------

	public AddEditBillingVerificationTest() {
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		cscockpitCustomerSearchTabPage = new CSCockpitCustomerSearchTabPage(driver);
		cscockpitCustomerTabPage = new CSCockpitCustomerTabPage(driver);
		cscockpitAutoshipTemplateTabPage = new CSCockpitAutoshipTemplateTabPage(driver);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
		storeFrontPCUserPage = new StoreFrontPCUserPage(driver);
		storeFrontRCUserPage = new StoreFrontRCUserPage(driver);
		storeFrontAccountInfoPage = new StoreFrontAccountInfoPage(driver);
	}

	private String RFO_DB = null;

	//Hybris Project-1727:To verify Add card functionality from customer detail page for consultant
	@Test
	public void testVerifyAddCardFunctionalityFromCustomerDetailPageForConsultant_1727() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String consultantEmailID;
		String accountID;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum2 = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum3 = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum4 = CommonUtils.getRandomNum(10000, 1000000);
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum;
		String attendeeLastName = TestConstants.LAST_NAME;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String country = null;
		String addressLine = null;
		String attention = "Attention";
		String line1 = "Line 1";
		if(driver.getCountry().equalsIgnoreCase("us")){
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
			country = "United States";
		}else{
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			country = "Canada";
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomConsultantList = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			try{
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else{
				break;
			}
		}
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
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.addressCanNotBeAddedForInactiveUserInCustomerTab(), "Address can be added for Inactive user");
		cscockpitCustomerTabPage.clickOkBtnOfAddressCanNotBeAddedForInactiveUserInCustomerTab();
		cscockpitCustomerTabPage.clickCustomerSearchTab();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAddCardButtonInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.isAddNewPaymentProfilePopupPresentInCustomerTab(), "Add new payment profile popup is not present in billing section of customer tab");
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isReviewCreditCardDetailsErrorMsgPresent(), "Review credit card error msg is not present");
		String profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		cscockpitCustomerTabPage.enterBillingInfoWithoutEnterCreditCardAndAddress(profileName);
		cscockpitCustomerTabPage.selectBillingAddressInPaymentProfilePopup();
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isReviewCreditCardDetailsErrorMsgPresent(), "Review credit card error msg is not present");
		cscockpitCustomerTabPage.enterCreditCardNumberInPaymentProfilePopup();
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(profileName),"Newly Added credit card is not present in credit card list");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(profileName.toLowerCase().trim()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
		cscockpitAutoshipTemplateTabPage.clickCustomerTab();
		cscockpitCustomerTabPage.clickAddCardButtonInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.isAddNewPaymentProfilePopupPresentInCustomerTab(), "Add new payment profile popup is not present in billing section of customer tab");
		profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum2;
		cscockpitCustomerTabPage.enterBillingInfo(profileName);
		cscockpitCustomerTabPage.clickOnSetAsAutoshipBillingProfileChkBox();
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(profileName.toLowerCase().trim()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
		cscockpitAutoshipTemplateTabPage.clickCustomerTab();
		cscockpitCustomerTabPage.clickAddCardButtonInCustomerTab();
		cscockpitCustomerTabPage.clickAddANewAddressOfAddANewPaymentProfilePopup();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddressValuesPresentInPaymentProfilePopup(attention), "Attention field is not present in payment profile popup");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddressValuesPresentInPaymentProfilePopup(line1), "line 1 field is not present in payment profile popup");
		profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum3;
		cscockpitCustomerTabPage.enterBillingInfoWithoutSelectAddress(profileName);
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(profileName),"Newly Added credit card is not present in credit card list After add a new address");
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(attendentFirstName),"Newly Added credit card with new address is not present in credit card list");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(profileName.toLowerCase().trim()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
		cscockpitAutoshipTemplateTabPage.clickCustomerTab();
		cscockpitCustomerTabPage.clickAddCardButtonInCustomerTab();
		profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum4;
		cscockpitCustomerTabPage.enterBillingInfoWithoutSelectAddress(profileName);
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitCustomerTabPage.clickOnSetAsAutoshipBillingProfileChkBox();
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(profileName),"Newly Added credit card is not present in credit card list After add a new address");
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(attendentFirstName),"Newly Added credit card with new address is not present in credit card list");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(profileName.toLowerCase().trim()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());

		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		try{
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(profileName);
		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(profileName),"Newly added billing profile is not present");
		logout();
		s_assert.assertAll();
	}
	
	//Hybris Project-1728:To verify Add card functionality from customer detail page for preferred customer
	@Test
	public void testVerifyAddCardFunctionalityFromCustomerDetailPageForPC_1728() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String pcEmailID;
		String accountID;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum2 = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum3 = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum4 = CommonUtils.getRandomNum(10000, 1000000);
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum;
		String attendeeLastName = TestConstants.LAST_NAME;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String country = null;
		String addressLine = null;
		String attention = "Attention";
		String line1 = "Line 1";
		if(driver.getCountry().equalsIgnoreCase("us")){
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
			country = "United States";
		}else{
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			country = "Canada";
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomPCList = null;
		while(true){
			randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcEmailID  = (String) getValueFromQueryResult(randomPCList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomPCList, "AccountID")); 
			logger.info("Account Id of user "+accountID);
			try{
				storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
			}
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logout();
		//get emailId of username
		List<Map<String, Object>> randomPCUsernameList =  null;
		randomPCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		pcEmailID = String.valueOf(getValueFromQueryResult(randomPCUsernameList, "EmailAddress"));  
		logger.info("emaild of username "+pcEmailID);
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		//cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.addressCanNotBeAddedForInactiveUserInCustomerTab(), "Address can be added for Inactive user");
		cscockpitCustomerTabPage.clickOkBtnOfAddressCanNotBeAddedForInactiveUserInCustomerTab();
		cscockpitCustomerTabPage.clickCustomerSearchTab();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAddCardButtonInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.isAddNewPaymentProfilePopupPresentInCustomerTab(), "Add new payment profile popup is not present in billing section of customer tab");
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isReviewCreditCardDetailsErrorMsgPresent(), "Review credit card error msg is not present");
		String profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		cscockpitCustomerTabPage.enterBillingInfoWithoutEnterCreditCardAndAddress(profileName);
		cscockpitCustomerTabPage.selectBillingAddressInPaymentProfilePopup();
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isReviewCreditCardDetailsErrorMsgPresent(), "Review credit card error msg is not present");
		cscockpitCustomerTabPage.enterCreditCardNumberInPaymentProfilePopup();
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(profileName),"Newly Added credit card is not present in credit card list");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(profileName.toLowerCase().trim()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
		cscockpitAutoshipTemplateTabPage.clickCustomerTab();
		cscockpitCustomerTabPage.clickAddCardButtonInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.isAddNewPaymentProfilePopupPresentInCustomerTab(), "Add new payment profile popup is not present in billing section of customer tab");
		profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum2;
		cscockpitCustomerTabPage.enterBillingInfo(profileName);
		cscockpitCustomerTabPage.clickOnSetAsAutoshipBillingProfileChkBox();
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(profileName.toLowerCase().trim()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
		cscockpitAutoshipTemplateTabPage.clickCustomerTab();
		cscockpitCustomerTabPage.clickAddCardButtonInCustomerTab();
		cscockpitCustomerTabPage.clickAddANewAddressOfAddANewPaymentProfilePopup();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddressValuesPresentInPaymentProfilePopup(attention), "Attention field is not present in payment profile popup");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddressValuesPresentInPaymentProfilePopup(line1), "line 1 field is not present in payment profile popup");
		profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum3;
		cscockpitCustomerTabPage.enterBillingInfoWithoutSelectAddress(profileName);
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(profileName),"Newly Added credit card is not present in credit card list After add a new address");
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(attendentFirstName),"Newly Added credit card with new address is not present in credit card list");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(profileName.toLowerCase().trim()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
		cscockpitAutoshipTemplateTabPage.clickCustomerTab();
		cscockpitCustomerTabPage.clickAddCardButtonInCustomerTab();
		profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum4;
		cscockpitCustomerTabPage.enterBillingInfoWithoutSelectAddress(profileName);
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitCustomerTabPage.clickOnSetAsAutoshipBillingProfileChkBox();
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(profileName),"Newly Added credit card is not present in credit card list After add a new address");
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(attendentFirstName),"Newly Added credit card with new address is not present in credit card list");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(profileName.toLowerCase().trim()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());

		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		try{
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
		}
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontPCUserPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(profileName);
		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(profileName),"Newly added billing profile is not present");
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-1729:To verify Add card functionality from customer detail page for Retail Order
	@Test(enabled=false)//RC tests needs to be updated
	public void testVerifyAddCardFunctionalityFromCustomerDetailPageForRC_1729() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String rcUserEmailID;
		String accountID;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum2 = CommonUtils.getRandomNum(10000, 1000000);
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum;
		String attendeeLastName = TestConstants.LAST_NAME;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String country = null;
		String addressLine = null;
		String attention = "Attention";
		String line1 = "Line 1";
		if(driver.getCountry().equalsIgnoreCase("us")){
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
			country = "United States";
		}else{
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			country = "Canada";
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomRCList = null;
		List<Map<String, Object>> randomRCUsernameList = null;
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_EMAIL_ID_RFO,countryId),RFO_DB);
			accountID = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			randomRCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			rcUserEmailID = String.valueOf(getValueFromQueryResult(randomRCUsernameList, "EmailAddress"));
			try{
				storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			}
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+rcUserEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else{
				storeFrontRCUserPage.clickOnWelcomeDropDown();
				if(storeFrontRCUserPage.verifyEditPcPerksIsPresentInWelcomDropdownForUpgrade()){
					logout();
					continue;
				}else{
					break;
				}
			}}
		logout();
		//get emailId of username
		logger.info("emaild of username "+rcUserEmailID);
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.addressCanNotBeAddedForInactiveUserInCustomerTab(), "Address can be added for Inactive user");
		cscockpitCustomerTabPage.clickOkBtnOfAddressCanNotBeAddedForInactiveUserInCustomerTab();
		cscockpitCustomerTabPage.clickCustomerSearchTab();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(rcUserEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAddCardButtonInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.isAddNewPaymentProfilePopupPresentInCustomerTab(), "Add new payment profile popup is not present in billing section of customer tab");
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isReviewCreditCardDetailsErrorMsgPresent(), "Review credit card error msg is not present");
		String profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		cscockpitCustomerTabPage.enterBillingInfoWithoutEnterCreditCardAndAddress(profileName);
		cscockpitCustomerTabPage.selectBillingAddressInPaymentProfilePopup();
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isReviewCreditCardDetailsErrorMsgPresent(), "Review credit card error msg is not present");
		cscockpitCustomerTabPage.enterCreditCardNumberInPaymentProfilePopup();
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(profileName),"Newly Added credit card is not present in credit card list");

		cscockpitCustomerTabPage.clickAddCardButtonInCustomerTab();
		cscockpitCustomerTabPage.clickAddANewAddressOfAddANewPaymentProfilePopup();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddressValuesPresentInPaymentProfilePopup(attention), "Attention field is not present in payment profile popup");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddressValuesPresentInPaymentProfilePopup(line1), "line 1 field is not present in payment profile popup");
		profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum2;
		cscockpitCustomerTabPage.enterBillingInfoWithoutSelectAddress(profileName);
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(profileName),"Newly Added credit card is not present in credit card list After add a new address");
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(attendentFirstName),"Newly Added credit card with new address is not present in credit card list");
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		try{
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
		}
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontRCUserPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(profileName);
		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(profileName),"Newly added billing profile is not present");
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-1730:To verify Edit card functionality from customer detail page for consultant
	@Test
	public void testVerifyEditCardFunctionalityFromCustomerDetailPageForConsultant_1730() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String consultantEmailID;
		String accountID;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum2 = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum3 = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum4 = CommonUtils.getRandomNum(10000, 1000000);
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum;
		String attendeeLastName = TestConstants.LAST_NAME;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String country = null;
		String addressLine = null;
		String attention = "Attention";
		String line1 = "Line 1";
		if(driver.getCountry().equalsIgnoreCase("us")){
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
			country = "United States";
		}else{
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			country = "Canada";
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomConsultantList = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			try{
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
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
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.isSetAsAutoshipShippingProfileTxtPresentInAddNewShippingProfilePopup(), "Set as autoship shipping address present in add new shipping address popup  customer tab");
		cscockpitCustomerTabPage.clickCloseOfCreateNewAddressPopUpInCustomerTab();
		cscockpitCustomerTabPage.clickCustomerSearchTab();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab("");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.addressCanNotBeAddedForInactiveUserInCustomerTab(), "Address can be added for Inactive user");
		cscockpitCustomerTabPage.clickOkBtnOfAddressCanNotBeAddedForInactiveUserInCustomerTab();
		cscockpitCustomerTabPage.clickMenuButton();
		cscockpitCustomerTabPage.clickLogoutButton();

		//For Active user
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			try{
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
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
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickEditButtonForCreditCardInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.isEditPaymentProfilePopupPresentInCustomerTab(), "Edit payment profile popup is not present in billing section of customer tab");
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isReviewCreditCardDetailsErrorMsgPresent(), "Review credit card error msg is not present");
		String profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		cscockpitCustomerTabPage.enterBillingInfoWithoutSelectAddress(profileName);
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(profileName),"Newly Added credit card is not present in credit card list");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(profileName.toLowerCase().trim()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
		cscockpitCustomerTabPage.clickCustomerTab();
		//With check autoship checkbox
		cscockpitCustomerTabPage.clickEditButtonForCreditCardInCustomerTab();
		profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum2;
		cscockpitCustomerTabPage.enterBillingInfoWithoutSelectAddress(profileName);
		cscockpitCustomerTabPage.clickOnSetAsAutoshipBillingProfileChkBox();
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(profileName),"Newly Added credit card is not present in credit card list");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(profileName.toLowerCase().trim()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
		//Edit Address
		cscockpitCustomerTabPage.clickCustomerTab();
		cscockpitCustomerTabPage.clickEditButtonForCreditCardInCustomerTab();
		profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum3;
		cscockpitCustomerTabPage.enterBillingInfoWithoutEnterCreditCardAndAddress(profileName);
		cscockpitCustomerTabPage.enterCreditCardNumberInPaymentProfilePopup();
		cscockpitCustomerTabPage.clickEditAddressInEditPaymentProfilePopup();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddressValuesPresentInPaymentProfilePopup(attention), "Attention field is not present in payment profile popup");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddressValuesPresentInPaymentProfilePopup(line1), "line 1 field is not present in payment profile popup");
		cscockpitCustomerTabPage.enterBillingInfoWithoutSelectAddress(profileName);
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(profileName),"Newly Added credit card is not present in credit card list After add a new address");
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(attendentFirstName),"Newly Added credit card with new address is not present in credit card list");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(profileName.toLowerCase().trim()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
		cscockpitAutoshipTemplateTabPage.clickCustomerTab();
		//With check autoship checkbox for billing address
		cscockpitCustomerTabPage.clickEditButtonForCreditCardInCustomerTab();
		profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum4;
		cscockpitCustomerTabPage.enterBillingInfoWithoutEnterCreditCardAndAddress(profileName);
		cscockpitCustomerTabPage.enterCreditCardNumberInPaymentProfilePopup();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddressValuesPresentInPaymentProfilePopup(attention), "Attention field is not present in payment profile popup");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddressValuesPresentInPaymentProfilePopup(line1), "line 1 field is not present in payment profile popup");
		cscockpitCustomerTabPage.enterBillingInfoWithoutSelectAddress(profileName);
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitCustomerTabPage.clickOnSetAsAutoshipBillingProfileChkBox();
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(profileName),"Newly Added credit card is not present in credit card list After add a new address");
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(attendentFirstName),"Newly Added credit card with new address is not present in credit card list");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(profileName.toLowerCase().trim()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());

		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		try{
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(profileName);
		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(profileName),"Newly added billing profile is not present");
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-1731:To verify Edit card functionality from customer detail page for Preferred Customer
	@Test
	public void testVerifyEditCardFunctionalityFromCustomerDetailPageForPC_1731() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String pcEmailID;
		String accountID;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum2 = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum3 = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum4 = CommonUtils.getRandomNum(10000, 1000000);
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum;
		String attendeeLastName = TestConstants.LAST_NAME;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String country = null;
		String addressLine = null;
		String attention = "Attention";
		String line1 = "Line 1";
		if(driver.getCountry().equalsIgnoreCase("us")){
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
			country = "United States";
		}else{
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			country = "Canada";
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		List<Map<String, Object>> randomPCList = null;

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
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcEmailID = (String) getValueFromQueryResult(randomPCList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomPCList, "AccountID"));
			try{
				storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
			}		
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else{
				break;
			}
		}
		logout();
		//get emailId of username
		List<Map<String, Object>> randomPCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		pcEmailID = String.valueOf(getValueFromQueryResult(randomPCUsernameList, "EmailAddress"));  
		logger.info("emaild of username "+pcEmailID);
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickEditButtonForCreditCardInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.isEditPaymentProfilePopupPresentInCustomerTab(), "Edit payment profile popup is not present in billing section of customer tab");
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isReviewCreditCardDetailsErrorMsgPresent(), "Review credit card error msg is not present");
		String profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		cscockpitCustomerTabPage.enterBillingInfoWithoutSelectAddress(profileName);
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(profileName),"Newly Added credit card is not present in credit card list");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(profileName.toLowerCase().trim()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
		cscockpitCustomerTabPage.clickCustomerTab();
		//With check autoship checkbox
		cscockpitCustomerTabPage.clickEditButtonForCreditCardInCustomerTab();
		profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum2;
		cscockpitCustomerTabPage.enterBillingInfoWithoutSelectAddress(profileName);
		cscockpitCustomerTabPage.clickOnSetAsAutoshipBillingProfileChkBox();
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(profileName),"Newly Added credit card is not present in credit card list");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(profileName.toLowerCase().trim()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
		//Edit Address
		cscockpitCustomerTabPage.clickCustomerTab();
		cscockpitCustomerTabPage.clickEditButtonForCreditCardInCustomerTab();
		profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum3;
		cscockpitCustomerTabPage.enterBillingInfoWithoutEnterCreditCardAndAddress(profileName);
		cscockpitCustomerTabPage.enterCreditCardNumberInPaymentProfilePopup();
		cscockpitCustomerTabPage.clickEditAddressInEditPaymentProfilePopup();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddressValuesPresentInPaymentProfilePopup(attention), "Attention field is not present in payment profile popup");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddressValuesPresentInPaymentProfilePopup(line1), "line 1 field is not present in payment profile popup");
		cscockpitCustomerTabPage.enterBillingInfoWithoutSelectAddress(profileName);
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(profileName),"Newly Added credit card is not present in credit card list After add a new address");
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(attendentFirstName),"Newly Added credit card with new address is not present in credit card list");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(profileName.toLowerCase().trim()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
		cscockpitAutoshipTemplateTabPage.clickCustomerTab();
		//With check autoship checkbox for billing address
		cscockpitCustomerTabPage.clickEditButtonForCreditCardInCustomerTab();
		profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum4;
		cscockpitCustomerTabPage.enterBillingInfoWithoutEnterCreditCardAndAddress(profileName);
		cscockpitCustomerTabPage.enterCreditCardNumberInPaymentProfilePopup();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddressValuesPresentInPaymentProfilePopup(attention), "Attention field is not present in payment profile popup");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddressValuesPresentInPaymentProfilePopup(line1), "line 1 field is not present in payment profile popup");
		cscockpitCustomerTabPage.enterBillingInfoWithoutSelectAddress(profileName);
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitCustomerTabPage.clickOnSetAsAutoshipBillingProfileChkBox();
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(profileName),"Newly Added credit card is not present in credit card list After add a new address");
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(attendentFirstName),"Newly Added credit card with new address is not present in credit card list");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(profileName.toLowerCase().trim()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());

		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		try{
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
		}	
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontPCUserPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(profileName);
		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(profileName),"Newly added billing profile is not present");
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-1732:To verify Edit card functionality from customer detail page for Retail Customer
	@Test(enabled=false)//RC tests needs to be updated
	public void testVerifyEditCardFunctionalityFromCustomerDetailPageForRC_1732() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String rcEmailID;
		String accountID;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum3 = CommonUtils.getRandomNum(10000, 1000000);
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum;
		String attendeeLastName = TestConstants.LAST_NAME;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String country = null;
		String addressLine = null;
		String attention = "Attention";
		String line1 = "Line 1";
		if(driver.getCountry().equalsIgnoreCase("us")){
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
			country = "United States";
		}else{
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			country = "Canada";
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		List<Map<String, Object>> randomRCList = null;
		List<Map<String, Object>> randomRCUsernameList = null;
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
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_EMAIL_ID_RFO,countryId),RFO_DB);
			accountID = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			randomRCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			rcEmailID = String.valueOf(getValueFromQueryResult(randomRCUsernameList, "EmailAddress"));
			//rcUserEmailID ="melanie@chrisfix.com";
			try{
				storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcEmailID, password);
			}
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+rcEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else{
				storeFrontRCUserPage.clickOnWelcomeDropDown();
				if(storeFrontRCUserPage.verifyEditPcPerksIsPresentInWelcomDropdownForUpgrade()){
					logout();
					continue;
				}else{
					break;
				}
			}}
		logout();
		//get emailId of username
		logger.info("emaild of username "+rcEmailID);
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("Retail");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(rcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickEditButtonForCreditCardInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.isEditPaymentProfilePopupPresentInCustomerTab(), "Edit payment profile popup is not present in billing section of customer tab");
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isReviewCreditCardDetailsErrorMsgPresent(), "Review credit card error msg is not present");
		String profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		cscockpitCustomerTabPage.enterBillingInfoWithoutSelectAddress(profileName);
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(profileName),"Newly Added credit card is not present in credit card list");
		cscockpitCustomerTabPage.clickEditButtonForCreditCardInCustomerTab();
		profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum3;
		cscockpitCustomerTabPage.enterBillingInfoWithoutEnterCreditCardAndAddress(profileName);
		cscockpitCustomerTabPage.enterCreditCardNumberInPaymentProfilePopup();
		cscockpitCustomerTabPage.clickEditAddressInEditPaymentProfilePopup();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddressValuesPresentInPaymentProfilePopup(attention), "Attention field is not present in payment profile popup");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddressValuesPresentInPaymentProfilePopup(line1), "line 1 field is not present in payment profile popup");
		cscockpitCustomerTabPage.enterBillingInfoWithoutSelectAddress(profileName);
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(profileName),"Newly Added credit card is not present in credit card list After add a new address");
		s_assert.assertTrue(cscockpitCustomerTabPage.isNewlyAddedCreditCardPresent(attendentFirstName),"Newly Added credit card with new address is not present in credit card list");
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		try{
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcEmailID, password);
		}	
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontRCUserPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(profileName);
		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(profileName),"Newly added billing profile is not present");
		logout();
		s_assert.assertAll();
	}
}