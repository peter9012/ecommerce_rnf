package com.rf.test.website.cscockpit.dsv;

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
import com.rf.pages.website.cscockpit.CSCockpitAutoshipTemplateTabPage;
import com.rf.pages.website.cscockpit.CSCockpitAutoshipTemplateUpdateTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCartTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCheckoutTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCustomerSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCustomerTabPage;
import com.rf.pages.website.cscockpit.CSCockpitLoginPage;
import com.rf.pages.website.cscockpit.CSCockpitOrderSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitOrderTabPage;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;


public class CSCockpitDSVTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(CSCockpitDSVTest.class.getName());

	//-------------------------------------------------Pages---------------------------------------------------------
	private CSCockpitLoginPage cscockpitLoginPage;	
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

	//-----------------------------------------------------------------------------------------------------------------

	public CSCockpitDSVTest() {
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		cscockpitCheckoutTabPage = new CSCockpitCheckoutTabPage(driver);
		cscockpitCustomerSearchTabPage = new CSCockpitCustomerSearchTabPage(driver);
		cscockpitCustomerTabPage = new CSCockpitCustomerTabPage(driver);
		cscockpitOrderSearchTabPage = new CSCockpitOrderSearchTabPage(driver);
		cscockpitOrderTabPage = new CSCockpitOrderTabPage(driver);
		cscockpitCartTabPage = new CSCockpitCartTabPage(driver);
		cscockpitAutoshipCartTabPage= new CSCockpitAutoshipCartTabPage(driver);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
		storeFrontOrdersPage = new StoreFrontOrdersPage(driver);
		storeFrontPCUserPage = new StoreFrontPCUserPage(driver);
		storeFrontRCUserPage = new StoreFrontRCUserPage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		cscockpitAutoshipTemplateTabPage = new CSCockpitAutoshipTemplateTabPage(driver);
		cscockpitAutoshipTemplateUpdateTabPage = new CSCockpitAutoshipTemplateUpdateTabPage(driver);
	}

	private String RFO_DB = null;

	//Verify the Consultant Search
	@Test
	public void testVerifyConsultantSearchCriteria() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		cscockpitLoginPage.enterUsername(TestConstants.DSV_CSCOCKPIT_USERNAME);
		cscockpitLoginPage.enterPassword(TestConstants.DSV_CSCOCKPIT_PASSWORD);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		//get valid cid from database.
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(TestConstants.DSV_CONSULTANT_EMAILID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.getTotalResultsInCustomerSearchOnCustomerSearchTab()==1, "searched consultant is not showing 1 result");
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab("1");
		s_assert.assertTrue(cscockpitCustomerTabPage.getEmailAddressFromTopSectionInCustomerTabPage().equalsIgnoreCase(TestConstants.DSV_CONSULTANT_EMAILID), "Customer details page doesn't contain the email Id of consultant as "+TestConstants.DSV_CONSULTANT_EMAILID);
		s_assert.assertTrue(cscockpitCustomerTabPage.getUserNameAndCIDStringFromTopSectionInCustomerTabPage().contains(TestConstants.DSV_CONSULTANT_CID), "Customer details page doesn't contain the CID of consultant as "+TestConstants.DSV_CONSULTANT_CID);
		s_assert.assertAll();
	}

	//Verify the PC Search
	@Test
	public void testVerifyPreferredCustomerSearchCriteria() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		cscockpitLoginPage.enterUsername(TestConstants.DSV_CSCOCKPIT_USERNAME);
		cscockpitLoginPage.enterPassword(TestConstants.DSV_CSCOCKPIT_PASSWORD);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		//get valid cid from database.
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(TestConstants.DSV_PC_EMAILID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.getTotalResultsInCustomerSearchOnCustomerSearchTab()==1, "searched pc is not showing 1 result");
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab("1");
		s_assert.assertTrue(cscockpitCustomerTabPage.getEmailAddressFromTopSectionInCustomerTabPage().equalsIgnoreCase(TestConstants.DSV_PC_EMAILID), "Customer details page doesn't contain the email Id of PC as "+TestConstants.DSV_PC_EMAILID);
		s_assert.assertTrue(cscockpitCustomerTabPage.getUserNameAndCIDStringFromTopSectionInCustomerTabPage().contains(TestConstants.DSV_PC_CID), "Customer details page doesn't contain the CID of PC as "+TestConstants.DSV_PC_CID);
		s_assert.assertAll();
	}

	//Verify the RC Search
	@Test
	public void testVerifyRetailCustomerSearchCriteria() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		cscockpitLoginPage.enterUsername(TestConstants.DSV_CSCOCKPIT_USERNAME);
		cscockpitLoginPage.enterPassword(TestConstants.DSV_CSCOCKPIT_PASSWORD);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		//get valid cid from database.
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(TestConstants.DSV_RC_EMAILID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.getTotalResultsInCustomerSearchOnCustomerSearchTab()==1, "searched RC is not showing 1 result");
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab("1");
		s_assert.assertTrue(cscockpitCustomerTabPage.getEmailAddressFromTopSectionInCustomerTabPage().equalsIgnoreCase(TestConstants.DSV_RC_EMAILID), "Customer details page doesn't contain the email Id of RC as "+TestConstants.DSV_RC_EMAILID);
		s_assert.assertTrue(cscockpitCustomerTabPage.getUserNameAndCIDStringFromTopSectionInCustomerTabPage().contains(TestConstants.DSV_RC_CID), "Customer details page doesn't contain the CID of RC as "+TestConstants.DSV_RC_CID);
		s_assert.assertAll();
	}

	//Verify the add/edit billing for Consultant
	@Test
	public void testVerifyAddAndEditBillingForConsultant() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String editBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum+" edit";
		cscockpitLoginPage.enterUsername(TestConstants.DSV_CSCOCKPIT_USERNAME);
		cscockpitLoginPage.enterPassword(TestConstants.DSV_CSCOCKPIT_PASSWORD);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		//get valid cid from database.
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(TestConstants.DSV_CONSULTANT_EMAILID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.getTotalResultsInCustomerSearchOnCustomerSearchTab()==1, "searched consultant is not showing 1 result");
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab("1");
		s_assert.assertTrue(cscockpitCustomerTabPage.getEmailAddressFromTopSectionInCustomerTabPage().equalsIgnoreCase(TestConstants.DSV_CONSULTANT_EMAILID), "Customer details page doesn't contain the email Id of consultant as "+TestConstants.DSV_CONSULTANT_EMAILID);
		s_assert.assertTrue(cscockpitCustomerTabPage.getUserNameAndCIDStringFromTopSectionInCustomerTabPage().contains(TestConstants.DSV_CONSULTANT_CID), "Customer details page doesn't contain the CID of consultant as "+TestConstants.DSV_CONSULTANT_CID);
		cscockpitCustomerTabPage.clickAddCardButtonInCustomerTab();
		cscockpitCustomerTabPage.enterBillingInfo(newBillingProfileName,TestConstants.DSV_CARD_NUMBER,"AMEX",TestConstants.DSV_SECURITY_CODE,TestConstants.DSV_EXPIRY_MONTH_NUMBER,TestConstants.DSV_EXPIRY_YEAR);
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isBillingListContainsBillingName(newBillingProfileName), "New Billing profile with the name "+newBillingProfileName+" is not added");
		cscockpitCustomerTabPage.clickEditBtnForTheBillingProfile(newBillingProfileName);
		cscockpitCustomerTabPage.enterBillingInfoWithoutSelectingAddress(editBillingProfileName,TestConstants.DSV_CARD_NUMBER,"AMEX",TestConstants.DSV_SECURITY_CODE,TestConstants.DSV_EXPIRY_MONTH_NUMBER,TestConstants.DSV_EXPIRY_YEAR);
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isBillingListContainsBillingName(editBillingProfileName), "Billing profile with the name "+newBillingProfileName+" is not edited");	
		s_assert.assertAll();
	}

	//Verify the add/edit billing for PC
	@Test
	public void testVerifyAddAndEditBillingForPC() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String editBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum+" edit";
		cscockpitLoginPage.enterUsername(TestConstants.DSV_CSCOCKPIT_USERNAME);
		cscockpitLoginPage.enterPassword(TestConstants.DSV_CSCOCKPIT_PASSWORD);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		//get valid cid from database.
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(TestConstants.DSV_PC_EMAILID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.getTotalResultsInCustomerSearchOnCustomerSearchTab()==1, "searched PC is not showing 1 result");
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab("1");
		s_assert.assertTrue(cscockpitCustomerTabPage.getEmailAddressFromTopSectionInCustomerTabPage().equalsIgnoreCase(TestConstants.DSV_PC_EMAILID), "Customer details page doesn't contain the email Id of PC as "+TestConstants.DSV_PC_EMAILID);
		s_assert.assertTrue(cscockpitCustomerTabPage.getUserNameAndCIDStringFromTopSectionInCustomerTabPage().contains(TestConstants.DSV_PC_CID), "Customer details page doesn't contain the CID of PC as "+TestConstants.DSV_PC_CID);
		cscockpitCustomerTabPage.clickAddCardButtonInCustomerTab();
		cscockpitCustomerTabPage.enterBillingInfo(newBillingProfileName,TestConstants.DSV_CARD_NUMBER,"AMEX",TestConstants.DSV_SECURITY_CODE,TestConstants.DSV_EXPIRY_MONTH_NUMBER,TestConstants.DSV_EXPIRY_YEAR);
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isBillingListContainsBillingName(newBillingProfileName), "New Billing profile with the name "+newBillingProfileName+" is not added");
		cscockpitCustomerTabPage.clickEditBtnForTheBillingProfile(newBillingProfileName);
		cscockpitCustomerTabPage.enterBillingInfoWithoutSelectingAddress(editBillingProfileName,TestConstants.DSV_CARD_NUMBER,"AMEX",TestConstants.DSV_SECURITY_CODE,TestConstants.DSV_EXPIRY_MONTH_NUMBER,TestConstants.DSV_EXPIRY_YEAR);
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isBillingListContainsBillingName(editBillingProfileName), "Billing profile with the name "+newBillingProfileName+" is not edited");	
		s_assert.assertAll();
	}


	//Verify the add/edit billing for RC
	@Test
	public void testVerifyAddAndEditBillingForRC() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String editBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum+" edit";
		cscockpitLoginPage.enterUsername(TestConstants.DSV_CSCOCKPIT_USERNAME);
		cscockpitLoginPage.enterPassword(TestConstants.DSV_CSCOCKPIT_PASSWORD);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		//get valid cid from database.
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(TestConstants.DSV_RC_EMAILID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.getTotalResultsInCustomerSearchOnCustomerSearchTab()==1, "searched RC is not showing 1 result");
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab("1");
		s_assert.assertTrue(cscockpitCustomerTabPage.getEmailAddressFromTopSectionInCustomerTabPage().equalsIgnoreCase(TestConstants.DSV_RC_EMAILID), "Customer details page doesn't contain the email Id of consultant as "+TestConstants.DSV_RC_EMAILID);
		s_assert.assertTrue(cscockpitCustomerTabPage.getUserNameAndCIDStringFromTopSectionInCustomerTabPage().contains(TestConstants.DSV_RC_CID), "Customer details page doesn't contain the CID of consultant as "+TestConstants.DSV_RC_CID);
		cscockpitCustomerTabPage.clickAddCardButtonInCustomerTab();
		cscockpitCustomerTabPage.enterBillingInfo(newBillingProfileName,TestConstants.DSV_CARD_NUMBER,"AMEX",TestConstants.DSV_SECURITY_CODE,TestConstants.DSV_EXPIRY_MONTH_NUMBER,TestConstants.DSV_EXPIRY_YEAR);
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isBillingListContainsBillingName(newBillingProfileName), "New Billing profile with the name "+newBillingProfileName+" is not added");
		cscockpitCustomerTabPage.clickEditBtnForTheBillingProfile(newBillingProfileName);
		cscockpitCustomerTabPage.enterBillingInfoWithoutSelectingAddress(editBillingProfileName,TestConstants.DSV_CARD_NUMBER,"AMEX",TestConstants.DSV_SECURITY_CODE,TestConstants.DSV_EXPIRY_MONTH_NUMBER,TestConstants.DSV_EXPIRY_YEAR);
		cscockpitCustomerTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCustomerTabPage.isBillingListContainsBillingName(editBillingProfileName), "Billing profile with the name "+newBillingProfileName+" is not edited");	
		s_assert.assertAll();
	}

	//Verify the add/edit shipping for Consultant
	@Test
	public void testVerifyAddAndEditShippingForConsultant() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);		
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String addressLine = null;
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum;
		String attendeeLastName = TestConstants.LAST_NAME;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String contry = null;		
		addressLine = TestConstants.ADDRESS_LINE_1_CA;
		city = TestConstants.CITY_CA;
		postal = TestConstants.POSTAL_CODE_CA;
		province = TestConstants.PROVINCE_ALBERTA;
		phoneNumber = TestConstants.PHONE_NUMBER_CA;
		contry = "Canada";
		cscockpitLoginPage.enterUsername(TestConstants.DSV_CSCOCKPIT_USERNAME);
		cscockpitLoginPage.enterPassword(TestConstants.DSV_CSCOCKPIT_PASSWORD);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		//get valid cid from database.
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(TestConstants.DSV_CONSULTANT_EMAILID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.getTotalResultsInCustomerSearchOnCustomerSearchTab()==1, "searched consultant is not showing 1 result");
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab("1");
		s_assert.assertTrue(cscockpitCustomerTabPage.getEmailAddressFromTopSectionInCustomerTabPage().equalsIgnoreCase(TestConstants.DSV_CONSULTANT_EMAILID), "Customer details page doesn't contain the email Id of consultant as "+TestConstants.DSV_CONSULTANT_EMAILID);
		s_assert.assertTrue(cscockpitCustomerTabPage.getUserNameAndCIDStringFromTopSectionInCustomerTabPage().contains(TestConstants.DSV_CONSULTANT_CID), "Customer details page doesn't contain the CID of consultant as "+TestConstants.DSV_CONSULTANT_CID);
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, contry, province, phoneNumber);
		cscockpitCustomerTabPage.selectCreditCardDropDownImgOnNewShippingAddressPopUp();
		cscockpitCustomerTabPage.enterCVVOnNewShippingAddressPopUp(TestConstants.DSV_SECURITY_CODE);
		cscockpitCustomerTabPage.clickCreateNewAddressBtn();
		cscockpitCustomerTabPage.clickUseThisAddressBtn();
		s_assert.assertTrue(cscockpitCustomerTabPage.getFirstShippingAddressProfileName().contains(attendentFirstName), "shipping profile name expected in customer tab page "+attendentFirstName+" actual on UI "+cscockpitCustomerTabPage.getFirstShippingAddressProfileName());
		cscockpitCustomerTabPage.clickEditButtonOfShippingAddressInCustomerTab();
		attendentFirstName= attendentFirstName+"edit";
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, contry, province, phoneNumber);
		cscockpitCustomerTabPage.selectCreditCardDropDownImgOnNewShippingAddressPopUp();
		cscockpitCustomerTabPage.enterCVVOnNewShippingAddressPopUp(TestConstants.DSV_SECURITY_CODE);
		cscockpitCustomerTabPage.clickUpdateAddressBtn();
		cscockpitCustomerTabPage.clickUseThisAddressBtn();
		s_assert.assertTrue(cscockpitCustomerTabPage.getFirstShippingAddressProfileName().contains(attendentFirstName), "shipping profile name expected in customer tab page "+attendentFirstName+" actual on UI "+cscockpitCustomerTabPage.getFirstShippingAddressProfileName());
		s_assert.assertAll();
	}

	//Verify the add/edit shipping for PC
	@Test
	public void testVerifyAddAndEditShippingForPC() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);		
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String addressLine = null;
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum;
		String attendeeLastName = TestConstants.LAST_NAME;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String contry = null;		
		addressLine = TestConstants.ADDRESS_LINE_1_CA;
		city = TestConstants.CITY_CA;
		postal = TestConstants.POSTAL_CODE_CA;
		province = TestConstants.PROVINCE_ALBERTA;
		phoneNumber = TestConstants.PHONE_NUMBER_CA;
		contry = "Canada";
		cscockpitLoginPage.enterUsername(TestConstants.DSV_CSCOCKPIT_USERNAME);
		cscockpitLoginPage.enterPassword(TestConstants.DSV_CSCOCKPIT_PASSWORD);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		//get valid cid from database.
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(TestConstants.DSV_PC_EMAILID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.getTotalResultsInCustomerSearchOnCustomerSearchTab()==1, "searched PC is not showing 1 result");
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab("1");
		s_assert.assertTrue(cscockpitCustomerTabPage.getEmailAddressFromTopSectionInCustomerTabPage().equalsIgnoreCase(TestConstants.DSV_PC_EMAILID), "Customer details page doesn't contain the email Id of pc as "+TestConstants.DSV_PC_EMAILID);
		s_assert.assertTrue(cscockpitCustomerTabPage.getUserNameAndCIDStringFromTopSectionInCustomerTabPage().contains(TestConstants.DSV_PC_CID), "Customer details page doesn't contain the CID of pc as "+TestConstants.DSV_PC_CID);
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, contry, province, phoneNumber);
		cscockpitCustomerTabPage.selectCreditCardDropDownImgOnNewShippingAddressPopUp();
		cscockpitCustomerTabPage.enterCVVOnNewShippingAddressPopUp(TestConstants.DSV_SECURITY_CODE);
		cscockpitCustomerTabPage.clickCreateNewAddressBtn();
		cscockpitCustomerTabPage.clickUseThisAddressBtn();
		s_assert.assertTrue(cscockpitCustomerTabPage.getFirstShippingAddressProfileName().contains(attendentFirstName), "shipping profile name expected in customer tab page "+attendentFirstName+" actual on UI "+cscockpitCustomerTabPage.getFirstShippingAddressProfileName());
		cscockpitCustomerTabPage.clickEditButtonOfShippingAddressInCustomerTab();
		attendentFirstName= attendentFirstName+"edit";
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, contry, province, phoneNumber);
		cscockpitCustomerTabPage.selectCreditCardDropDownImgOnNewShippingAddressPopUp();
		cscockpitCustomerTabPage.enterCVVOnNewShippingAddressPopUp(TestConstants.DSV_SECURITY_CODE);
		cscockpitCustomerTabPage.clickUpdateAddressBtn();
		cscockpitCustomerTabPage.clickUseThisAddressBtn();
		s_assert.assertTrue(cscockpitCustomerTabPage.getFirstShippingAddressProfileName().contains(attendentFirstName), "shipping profile name expected in customer tab page "+attendentFirstName+" actual on UI "+cscockpitCustomerTabPage.getFirstShippingAddressProfileName());
		s_assert.assertAll();
	}

	//Verify the add/edit shipping for RC
	@Test
	public void testVerifyAddAndEditShippingForRC() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);		
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String addressLine = null;
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum;
		String attendeeLastName = TestConstants.LAST_NAME;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String contry = null;		
		addressLine = TestConstants.ADDRESS_LINE_1_CA;
		city = TestConstants.CITY_CA;
		postal = TestConstants.POSTAL_CODE_CA;
		province = TestConstants.PROVINCE_ALBERTA;
		phoneNumber = TestConstants.PHONE_NUMBER_CA;
		contry = "Canada";
		cscockpitLoginPage.enterUsername(TestConstants.DSV_CSCOCKPIT_USERNAME);
		cscockpitLoginPage.enterPassword(TestConstants.DSV_CSCOCKPIT_PASSWORD);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		//get valid cid from database.
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(TestConstants.DSV_RC_EMAILID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.getTotalResultsInCustomerSearchOnCustomerSearchTab()==1, "searched RC is not showing 1 result");
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab("1");
		s_assert.assertTrue(cscockpitCustomerTabPage.getEmailAddressFromTopSectionInCustomerTabPage().equalsIgnoreCase(TestConstants.DSV_RC_EMAILID), "Customer details page doesn't contain the email Id of RC as "+TestConstants.DSV_RC_EMAILID);
		s_assert.assertTrue(cscockpitCustomerTabPage.getUserNameAndCIDStringFromTopSectionInCustomerTabPage().contains(TestConstants.DSV_RC_CID), "Customer details page doesn't contain the CID of RC as "+TestConstants.DSV_RC_CID);
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, contry, province, phoneNumber);
		cscockpitCustomerTabPage.selectCreditCardDropDownImgOnNewShippingAddressPopUp();
		cscockpitCustomerTabPage.enterCVVOnNewShippingAddressPopUp(TestConstants.DSV_SECURITY_CODE);
		cscockpitCustomerTabPage.clickCreateNewAddressBtn();
		cscockpitCustomerTabPage.clickUseThisAddressBtn();
		s_assert.assertTrue(cscockpitCustomerTabPage.getFirstShippingAddressProfileName().contains(attendentFirstName), "shipping profile name expected in customer tab page "+attendentFirstName+" actual on UI "+cscockpitCustomerTabPage.getFirstShippingAddressProfileName());
		cscockpitCustomerTabPage.clickEditButtonOfShippingAddressInCustomerTab();
		attendentFirstName= attendentFirstName+"edit";
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, contry, province, phoneNumber);
		cscockpitCustomerTabPage.selectCreditCardDropDownImgOnNewShippingAddressPopUp();
		cscockpitCustomerTabPage.enterCVVOnNewShippingAddressPopUp(TestConstants.DSV_SECURITY_CODE);
		cscockpitCustomerTabPage.clickUpdateAddressBtn();
		cscockpitCustomerTabPage.clickUseThisAddressBtn();
		s_assert.assertTrue(cscockpitCustomerTabPage.getFirstShippingAddressProfileName().contains(attendentFirstName), "shipping profile name expected in customer tab page "+attendentFirstName+" actual on UI "+cscockpitCustomerTabPage.getFirstShippingAddressProfileName());
		s_assert.assertAll();
	}

	//Order Search for Consultant
	@Test
	public void testOrderSearchForConsultant(){		
		cscockpitLoginPage.enterUsername(TestConstants.DSV_CSCOCKPIT_USERNAME);
		cscockpitLoginPage.enterPassword(TestConstants.DSV_CSCOCKPIT_PASSWORD);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.selectOrderStatusOnOrderSearchTab(TestConstants.DSV_CONSULTANT_ORDER_STATUS);
		cscockpitOrderSearchTabPage.enterCIDOnOrderSearchTab(TestConstants.DSV_CONSULTANT_CID);
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(TestConstants.DSV_CONSULTANT_ORDER_NUMBER);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab("1");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(TestConstants.DSV_CONSULTANT_ORDER_NUMBER),"Order number "+TestConstants.DSV_CONSULTANT_ORDER_NUMBER+" is not present on Order details page");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(TestConstants.DSV_CONSULTANT_CID),"CID number "+TestConstants.DSV_CONSULTANT_CID+" is not present on Order details page");		
		s_assert.assertAll();
	}

	//Order Search for PC
	@Test
	public void testOrderSearchForPC(){		
		cscockpitLoginPage.enterUsername(TestConstants.DSV_CSCOCKPIT_USERNAME);
		cscockpitLoginPage.enterPassword(TestConstants.DSV_CSCOCKPIT_PASSWORD);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.selectOrderStatusOnOrderSearchTab(TestConstants.DSV_PC_ORDER_STATUS);
		cscockpitOrderSearchTabPage.enterCIDOnOrderSearchTab(TestConstants.DSV_PC_CID);
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(TestConstants.DSV_PC_ORDER_NUMBER);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab("1");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(TestConstants.DSV_PC_ORDER_NUMBER),"Order number "+TestConstants.DSV_PC_ORDER_NUMBER+" is not present on Order details page");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(TestConstants.DSV_PC_CID),"CID number "+TestConstants.DSV_PC_CID+" is not present on Order details page");		
		s_assert.assertAll();
	}

	//Order Search for RC
	@Test
	public void testOrderSearchForRC(){		
		cscockpitLoginPage.enterUsername(TestConstants.DSV_CSCOCKPIT_USERNAME);
		cscockpitLoginPage.enterPassword(TestConstants.DSV_CSCOCKPIT_PASSWORD);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		//cscockpitOrderSearchTabPage.selectOrderStatusOnOrderSearchTab(TestConstants.DSV_RC_ORDER_STATUS);
		cscockpitOrderSearchTabPage.enterCIDOnOrderSearchTab(TestConstants.DSV_RC_CID);
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(TestConstants.DSV_RC_ORDER_NUMBER);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab("1");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(TestConstants.DSV_RC_ORDER_NUMBER),"Order number "+TestConstants.DSV_RC_ORDER_NUMBER+" is not present on Order details page");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(TestConstants.DSV_RC_CID),"CID number "+TestConstants.DSV_RC_CID+" is not present on Order details page");		
		s_assert.assertAll();
	}

	//Add more products in the autoship
	@Test
	public void testAddMoreProductsInTheAutoship(){		
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);		
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		cscockpitLoginPage.enterUsername(TestConstants.DSV_CSCOCKPIT_USERNAME);
		cscockpitLoginPage.enterPassword(TestConstants.DSV_CSCOCKPIT_PASSWORD);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		//get valid cid from database.
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("Consultant");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(TestConstants.DSV_CONSULTANT_EMAILID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.getTotalResultsInCustomerSearchOnCustomerSearchTab()==1, "searched Consultant is not showing 1 result");
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab("1");
		cscockpitCustomerTabPage.clickAutoshipIdOnCustomerTab();
		cscockpitAutoshipTemplateTabPage.clickEditAutoshiptemplate();
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		String randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		String SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		SKUValue = cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
//		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
//		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo(TestConstants.CARD_NUMBER,TestConstants.NEW_BILLING_PROFILE_NAME+randomNum,TestConstants.SECURITY_CODE);
//		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
//		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
//		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
//		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
//		int totalQtyAfter=cscockpitAutoshipTemplateTabPage.getQuantityOfAllProductInAutoshipTemplateTabPage();
		s_assert.assertAll();
	}
}