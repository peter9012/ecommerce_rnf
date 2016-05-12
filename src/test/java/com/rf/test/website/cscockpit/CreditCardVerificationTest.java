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
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class CreditCardVerificationTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(CreditCardVerificationTest.class.getName());

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

	//-----------------------------------------------------------------------------------------------------------------

	public CreditCardVerificationTest() {
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

	//Hybris Project-1749:To verify Add address functionality in the Customer detail Page for consultant
	@Test
	public void testVerifyAddAddressFunctionalityInCustomerDetailPageForConsultant_1749() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum2 = CommonUtils.getRandomNum(10000, 1000000);
		String addressLine = null;
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum;
		String attendeeLastName = TestConstants.LAST_NAME;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String country = null;
		List<Map<String, Object>> randomConsultantList =  null;

		if(driver.getCountry().equalsIgnoreCase("ca")){
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			country=TestConstants.COUNTRY_DD_VALUE_CA;

		}else{
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
			country=TestConstants.COUNTRY_DD_VALUE_US;
		}
		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
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
				storeFrontConsultantPage.clickOnWelcomeDropDown();
				storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
				storeFrontAccountInfoPage.clickOnYourAccountDropdown();
				storeFrontAccountInfoPage.clickOnAutoShipStatus();
				if(storeFrontAccountInfoPage.verifyCrpStatusAfterReactivation()==true){
					logout();
					logger.info("CRP is not active for the user "+consultantEmailID);
					driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
					continue;
				}else{
					break;	
				}

			}
		}
		logout();
		logger.info("login is successful");		
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
		cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
		cscockpitAutoshipTemplateTabPage.clickCustomerTab();
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.isCreateNewAddressPopupPresentInCustomerTab(), "Create new Address popup is not present in billing section of customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.isSetAsAutoshipShippingProfileTxtPresentInAddNewShippingProfilePopup(), "Set as autoship shipping address present in add new shipping address popup  customer tab");

		//For Inactive users
		cscockpitCustomerTabPage.clickCloseOfCreateNewAddressPopUpInCustomerTab();
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.addressCanNotBeAddedForInactiveUserInCustomerTab(),"Address can not be added for inactive user popup is not present");
		cscockpitCustomerTabPage.clickOkBtnOfAddressCanNotBeAddedForInactiveUserInCustomerTab();

		// For Active consultant save address without check checkBox
		//find a user from store front
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
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
				storeFrontConsultantPage.clickOnWelcomeDropDown();
				storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
				storeFrontAccountInfoPage.clickOnYourAccountDropdown();
				storeFrontAccountInfoPage.clickOnAutoShipStatus();
				if(storeFrontAccountInfoPage.verifyCrpStatusAfterReactivation()==true){
					logout();
					logger.info("CRP is not active for the user "+consultantEmailID);
					driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
					continue;
				}else{
					break;	
				}
			}
		}
		logout();
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
		cscockpitCustomerTabPage.clickCreateNewAddressBtn();
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAndClickShiipngAddressErrorPopupAndClickOkBtn(),"Clicked on create new address without entered any data error popup is not displayed");

		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitCustomerTabPage.clickCreateNewAddressBtn();
		cscockpitCustomerTabPage.clickUseThisAddressBtn();
		s_assert.assertTrue(cscockpitCustomerTabPage.getFirstShippingAddressProfileName().contains(attendentFirstName), "shipping profile name expected in customer tab page "+attendentFirstName+" actual on UI "+cscockpitCustomerTabPage.getFirstShippingAddressProfileName());

		//assert with check autoship checkbox
		attendentFirstName = TestConstants.FIRST_NAME+randomNum2;
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitCustomerTabPage.clickSetAsAutoshipChkBoxInCreateNewAddressPopup();
		cscockpitCustomerTabPage.clickCreateNewAddressBtn();
		cscockpitCustomerTabPage.clickUseThisAddressBtn();
		cscockpitCustomerTabPage.clickOnYesOnUpdateAutoshipAddressPopup();
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getAutoshipTemplateShippingAddressName().contains(attendentFirstName), "shipping profile name expected in autoship template tab page "+attendentFirstName+" actual on UI "+cscockpitAutoshipTemplateTabPage.getAutoshipTemplateShippingAddressName());
		s_assert.assertAll();
	}

	//Hybris Project-1750:To verify Add address functionality in the Customer detail Page for PC
	@Test
	public void testVerifyAddAddressFunctionalityInCustomerDetailPageForPC_1750(){
		String randomCustomerSequenceNumber = null;
		String pcEmailID = null;
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum2 = CommonUtils.getRandomNum(10000, 1000000);
		String addressLine = null;
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum;
		String attendeeLastName = TestConstants.LAST_NAME;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String country = null;
		List<Map<String, Object>> randomPCList =  null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			country = TestConstants.COUNTRY_DD_VALUE_CA;

		}else{
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
			country = TestConstants.COUNTRY_DD_VALUE_US;
		}
		//-------------------FOR US----------------------------------
		//For Inactive users
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.addressCanNotBeAddedForInactiveUserInCustomerTab(),"Address can not be added for inactive user popup is not present");
		cscockpitCustomerTabPage.clickOkBtnOfAddressCanNotBeAddedForInactiveUserInCustomerTab();
		//get user
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcEmailID = (String) getValueFromQueryResult(randomPCList, "UserName");  

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
		logger.info("login is successful");		
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.isCreateNewAddressPopupPresentInCustomerTab(), "Create new Address popup is not present in billing section of customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.isSetAsAutoshipShippingProfileTxtPresentInAddNewShippingProfilePopupForPendingAutoship(), "Set as autoship shipping address is not present in add new shipping address popup  customer tab");
		cscockpitCustomerTabPage.clickCloseOfCreateNewAddressPopUpInCustomerTab();
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		cscockpitCustomerTabPage.clickCreateNewAddressBtn();
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAndClickShiipngAddressErrorPopupAndClickOkBtn(),"Clicked on create new address without entered any data error popup is not displayed");
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitCustomerTabPage.clickCreateNewAddressBtn();
		cscockpitCustomerTabPage.clickUseThisAddressBtn();
		s_assert.assertTrue(cscockpitCustomerTabPage.getFirstShippingAddressProfileName().contains(attendentFirstName), "shipping profile name expected in customer tab page "+attendentFirstName+" actual on UI "+cscockpitCustomerTabPage.getFirstShippingAddressProfileName());
		//assert with check autoship checkbox
		attendentFirstName = TestConstants.FIRST_NAME+randomNum2;
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitCustomerTabPage.clickSetAsAutoshipChkBoxInCreateNewAddressPopup();
		cscockpitCustomerTabPage.clickCreateNewAddressBtn();
		cscockpitCustomerTabPage.clickUseThisAddressBtn();
		cscockpitCustomerTabPage.clickOnYesOnUpdateAutoshipAddressPopup();
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getAutoshipTemplateShippingAddressName().contains(attendentFirstName), "shipping profile name expected in autoship template tab page "+attendentFirstName+" actual on UI "+cscockpitAutoshipTemplateTabPage.getAutoshipTemplateShippingAddressName());
		s_assert.assertAll();
	}

	//Hybris Project-1751:To verify Add address functionality in the Customer detail Page for RC
	@Test
	public void testVerifyAddAddressFunctionalityInCustomerDetailPageForRC_1751(){
		String randomCustomerSequenceNumber = null;
		String rcEmailID = null;
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String addressLine = null;
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum;
		String attendeeLastName = TestConstants.LAST_NAME;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("us")){
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
			country = TestConstants.COUNTRY_DD_VALUE_US;
		}else{
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			country = TestConstants.COUNTRY_DD_VALUE_CA;

		}
		//For Inactive users
		driver.get(driver.getCSCockpitURL());  
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.addressCanNotBeAddedForInactiveUserInCustomerTab(),"Address can not be added for inactive user popup is not present");
		cscockpitCustomerTabPage.clickOkBtnOfAddressCanNotBeAddedForInactiveUserInCustomerTab();
		driver.get(driver.getCSCockpitURL());  
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		String noOfPages = cscockpitCustomerSearchTabPage.getTotalNumberOfPages();
		int randomPageNum = CommonUtils.getRandomNum(1, Integer.parseInt(noOfPages));
		cscockpitCustomerSearchTabPage.enterRandomPageNumber(""+randomPageNum);
		String[] emailIDList = cscockpitCustomerSearchTabPage.getEmailIDInCustomerSearchTab();
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			for(int i=1; i<=emailIDList.length;i++){
				rcEmailID = emailIDList[i];  

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
					continue;
				}
				else{
					break;
				}
			}
			break;
		}
		logout();
		driver.get(driver.getCSCockpitURL());  
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(rcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.isCreateNewAddressPopupPresentInCustomerTab(), "Create new Address popup is not present in billing section of customer tab");
		cscockpitCustomerTabPage.clickCloseOfCreateNewAddressPopUpInCustomerTab();
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		cscockpitCustomerTabPage.clickCreateNewAddressBtn();
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAndClickShiipngAddressErrorPopupAndClickOkBtn(),"Clicked on create new address without entered any data error popup is not displayed");
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitCustomerTabPage.clickCreateNewAddressBtn();
		cscockpitCustomerTabPage.clickUseThisAddressBtn();
		s_assert.assertTrue(cscockpitCustomerTabPage.getFirstShippingAddressProfileName().contains(attendentFirstName), "shipping profile name expected in customer tab page "+attendentFirstName+" actual on UI "+cscockpitCustomerTabPage.getFirstShippingAddressProfileName());
		s_assert.assertAll();
	}
}
