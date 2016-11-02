package com.rf.test.website.cscockpit.dsv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
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
import com.rf.test.website.RFDSVCscockpitWebsiteBaseTest;


public class CSCockpitDSVTest extends RFDSVCscockpitWebsiteBaseTest{
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
		cscockpitAutoshipTemplateTabPage = new CSCockpitAutoshipTemplateTabPage(driver);
		cscockpitAutoshipTemplateUpdateTabPage = new CSCockpitAutoshipTemplateUpdateTabPage(driver);
	}


	//Hybris Project-5493:Verify Consultant Search Criteria
	@Test(priority=1)
	public void testVerifyConsultantSearchCriteria_5493() throws InterruptedException{
		cscockpitCustomerSearchTabPage.clickCustomerSearchTab();
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

	//Hybris Project-5494:Verify Preferred Customer Search Criteria
	@Test(priority=2)
	public void testVerifyPreferredCustomerSearchCriteria_5494() throws InterruptedException{
		cscockpitCustomerSearchTabPage.clickCustomerSearchTab();
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

	// Hybris Project-5495:Verify Retail Customer Search Criteria
	@Test(priority=3)
	public void testVerifyRetailCustomerSearchCriteria_5495() throws InterruptedException{
		cscockpitCustomerSearchTabPage.clickCustomerSearchTab();
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

	// Hybris Project-5496:Verify Add and Edit Billing for Consultant
	@Test(priority=4)
	public void testVerifyAddAndEditBillingForConsultant_5496() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(1000, 9999);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;		
		String editBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum+" edit";
		cscockpitCustomerSearchTabPage.clickCustomerSearchTab();
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

	//Hybris Project-5497:Verify Add and Edit Billing for PC
	@Test(priority=5)
	public void testVerifyAddAndEditBillingForPC_5497() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(1000, 9999);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+" "+randomNum;
		String editBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+" "+randomNum+" edit";
		cscockpitCustomerSearchTabPage.clickCustomerSearchTab();
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

	// Hybris Project-5498:Verify Add and Edit Billing for RC
	@Test(priority=6)
	public void testVerifyAddAndEditBillingForRC_5498() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(1000, 9999);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String editBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum+" edit";
		cscockpitCustomerSearchTabPage.clickCustomerSearchTab();
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

	// Hybris Project-5499:Verify Add and Edit Shipping for Consultant
	@Test(priority=7)
	public void testVerifyAddAndEditShippingForConsultant_5499() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(1000, 9999);		
		String addressLine = null;
		String attendentFirstName = TestConstants.DSV_FIRST_NAME+" "+randomNum;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String contry = null;		
		addressLine = TestConstants.DSV_ADDRESS_LINE_1_CA;
		city = TestConstants.DSV_CITY_CA;
		postal = TestConstants.DSV_POSTAL_CODE_CA;
		province = TestConstants.PROVINCE_ALBERTA;
		phoneNumber = TestConstants.PHONE_NUMBER_CA;
		contry = "Canada";
		cscockpitCustomerSearchTabPage.clickCustomerSearchTab();
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, addressLine, city, postal, contry, province, phoneNumber);
//		cscockpitCustomerTabPage.selectCreditCardDropDownImgOnNewShippingAddressPopUp();
//		cscockpitCustomerTabPage.enterCVVOnNewShippingAddressPopUp(TestConstants.DSV_SECURITY_CODE);
		cscockpitCustomerTabPage.clickCreateNewAddressBtn();
		cscockpitCustomerTabPage.clickUseEnteredAddressBtn();
		s_assert.assertTrue(cscockpitCustomerTabPage.getFirstShippingAddressProfileName().contains(attendentFirstName), "shipping profile name expected in customer tab page "+attendentFirstName+" actual on UI "+cscockpitCustomerTabPage.getFirstShippingAddressProfileName());
		cscockpitCustomerTabPage.clickEditButtonOfShippingAddressInCustomerTab();
		attendentFirstName= attendentFirstName+" edit";
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, addressLine, city, postal, contry, province, phoneNumber);
//		cscockpitCustomerTabPage.selectCreditCardDropDownImgOnNewShippingAddressPopUp();
//		cscockpitCustomerTabPage.enterCVVOnNewShippingAddressPopUp(TestConstants.DSV_SECURITY_CODE);
		cscockpitCustomerTabPage.clickUpdateAddressBtn();
		cscockpitCustomerTabPage.clickUseEnteredAddressBtn();
		s_assert.assertTrue(cscockpitCustomerTabPage.getFirstShippingAddressProfileName().contains(attendentFirstName), "shipping profile name expected in customer tab page "+attendentFirstName+" actual on UI "+cscockpitCustomerTabPage.getFirstShippingAddressProfileName());
		s_assert.assertAll();
	}

	//Hybris Project-5500:Verify Add and Edit Shipping for PC
	@Test(priority=8)
	public void testVerifyAddAndEditShippingForPC_5500() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(1000, 9999);		
		String addressLine = null;
		String attendentFirstName = TestConstants.DSV_FIRST_NAME+" "+randomNum;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String contry = null;		
		addressLine = TestConstants.DSV_ADDRESS_LINE_1_CA;
		city = TestConstants.DSV_CITY_CA;
		postal = TestConstants.DSV_POSTAL_CODE_CA;
		province = TestConstants.PROVINCE_ALBERTA;
		phoneNumber = TestConstants.PHONE_NUMBER_CA;
		contry = "Canada";
		cscockpitCustomerSearchTabPage.clickCustomerSearchTab();
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, addressLine, city, postal, contry, province, phoneNumber);
		//cscockpitCustomerTabPage.selectCreditCardDropDownImgOnNewShippingAddressPopUp();
		//cscockpitCustomerTabPage.enterCVVOnNewShippingAddressPopUp(TestConstants.DSV_SECURITY_CODE);
		cscockpitCustomerTabPage.clickCreateNewAddressBtn();
		cscockpitCustomerTabPage.clickUseEnteredAddressBtn();
		s_assert.assertTrue(cscockpitCustomerTabPage.getFirstShippingAddressProfileName().contains(attendentFirstName), "shipping profile name expected in customer tab page "+attendentFirstName+" actual on UI "+cscockpitCustomerTabPage.getFirstShippingAddressProfileName());
		cscockpitCustomerTabPage.clickEditButtonOfShippingAddressInCustomerTab();
		attendentFirstName= attendentFirstName+" edit";
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, addressLine, city, postal, contry, province, phoneNumber);
		//cscockpitCustomerTabPage.selectCreditCardDropDownImgOnNewShippingAddressPopUp();
		//cscockpitCustomerTabPage.enterCVVOnNewShippingAddressPopUp(TestConstants.DSV_SECURITY_CODE);
		cscockpitCustomerTabPage.clickUpdateAddressBtn();
		cscockpitCustomerTabPage.clickUseEnteredAddressBtn();
		s_assert.assertTrue(cscockpitCustomerTabPage.getFirstShippingAddressProfileName().contains(attendentFirstName), "shipping profile name expected in customer tab page "+attendentFirstName+" actual on UI "+cscockpitCustomerTabPage.getFirstShippingAddressProfileName());
		s_assert.assertAll();
	}

	//Hybris Project-5501:Verify Add and Edit Shipping for RC
	@Test(priority=9)
	public void testVerifyAddAndEditShippingForRC_5501() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(1000, 9999);		
		String addressLine = null;
		String attendentFirstName = TestConstants.DSV_FIRST_NAME+" "+randomNum;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String contry = null;		
		addressLine = TestConstants.DSV_ADDRESS_LINE_1_CA;
		city = TestConstants.DSV_CITY_CA;
		postal = TestConstants.DSV_POSTAL_CODE_CA;
		province = TestConstants.PROVINCE_ALBERTA;
		phoneNumber = TestConstants.PHONE_NUMBER_CA;
		contry = "Canada";
		cscockpitCustomerSearchTabPage.clickCustomerSearchTab();
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, addressLine, city, postal, contry, province, phoneNumber);
//		cscockpitCustomerTabPage.selectCreditCardDropDownImgOnNewShippingAddressPopUp();
//		cscockpitCustomerTabPage.enterCVVOnNewShippingAddressPopUp(TestConstants.DSV_SECURITY_CODE);
		cscockpitCustomerTabPage.clickCreateNewAddressBtn();
		cscockpitCustomerTabPage.clickUseEnteredAddressBtn();
		s_assert.assertTrue(cscockpitCustomerTabPage.getFirstShippingAddressProfileName().contains(attendentFirstName), "shipping profile name expected in customer tab page "+attendentFirstName+" actual on UI "+cscockpitCustomerTabPage.getFirstShippingAddressProfileName());
		cscockpitCustomerTabPage.clickEditButtonOfShippingAddressInCustomerTab();
		attendentFirstName= attendentFirstName+" edit";
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, addressLine, city, postal, contry, province, phoneNumber);
//		cscockpitCustomerTabPage.selectCreditCardDropDownImgOnNewShippingAddressPopUp();
//		cscockpitCustomerTabPage.enterCVVOnNewShippingAddressPopUp(TestConstants.DSV_SECURITY_CODE);
		cscockpitCustomerTabPage.clickUpdateAddressBtn();
		cscockpitCustomerTabPage.clickUseEnteredAddressBtn();
		s_assert.assertTrue(cscockpitCustomerTabPage.getFirstShippingAddressProfileName().contains(attendentFirstName), "shipping profile name expected in customer tab page "+attendentFirstName+" actual on UI "+cscockpitCustomerTabPage.getFirstShippingAddressProfileName());
		s_assert.assertAll();
	}

	//Hybris Project-5502:Verify Order Search for Consultant
	@Test(priority=10)
	public void testVerifyOrderSearchForConsultant_5502(){  
		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.selectOrderStatusOnOrderSearchTab(TestConstants.DSV_CONSULTANT_ORDER_STATUS);
		cscockpitOrderSearchTabPage.enterCIDOnOrderSearchTab(TestConstants.DSV_CONSULTANT_CID_FOR_ORDER_SEARCH);
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(TestConstants.DSV_CONSULTANT_ORDER_NUMBER);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab("1");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(TestConstants.DSV_CONSULTANT_ORDER_NUMBER),"Order number "+TestConstants.DSV_CONSULTANT_ORDER_NUMBER+" is not present on Order details page");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(TestConstants.DSV_CONSULTANT_CID_FOR_ORDER_SEARCH),"CID number "+TestConstants.DSV_CONSULTANT_CID+" is not present on Order details page");  
		s_assert.assertAll();
	}

	// Hybris Project-5503:Verify Order Search for PC
	@Test(priority=11)
	public void testVerifyOrderSearchForPC_5503(){  
		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.selectOrderStatusOnOrderSearchTab(TestConstants.DSV_PC_ORDER_STATUS);
		cscockpitOrderSearchTabPage.enterCIDOnOrderSearchTab(TestConstants.DSV_PC_CID_FOR_ORDER_SEARCH);
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(TestConstants.DSV_PC_ORDER_NUMBER);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab("1");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(TestConstants.DSV_PC_ORDER_NUMBER),"Order number "+TestConstants.DSV_PC_ORDER_NUMBER+" is not present on Order details page");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(TestConstants.DSV_PC_CID_FOR_ORDER_SEARCH),"CID number "+TestConstants.DSV_PC_CID+" is not present on Order details page");  
		s_assert.assertAll();
	}

	//Hybris Project-5504:Verify Order Search for RC
	@Test(priority=12)
	public void testVerifyOrderSearchForRC_5504(){  
		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.selectOrderStatusOnOrderSearchTab(TestConstants.DSV_RC_ORDER_STATUS);
		cscockpitOrderSearchTabPage.enterCIDOnOrderSearchTab(TestConstants.DSV_RC_CID_FOR_ORDER_SEARCH);
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(TestConstants.DSV_RC_ORDER_NUMBER);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab("1");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(TestConstants.DSV_RC_ORDER_NUMBER),"Order number "+TestConstants.DSV_RC_ORDER_NUMBER+" is not present on Order details page");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(TestConstants.DSV_RC_CID_FOR_ORDER_SEARCH),"CID number "+TestConstants.DSV_RC_CID+" is not present on Order details page");  
		s_assert.assertAll();
	}

	//Hybris Project-5505:Verify Update CRP Autoship for Consultant
	@Test(enabled=false)//WIP
	public void testVerifyUpdateCRPAutoshipConsultant_5505(){		
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);		
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		cscockpitCustomerSearchTabPage.clickCustomerSearchTab();
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
		s_assert.assertAll();
	}

}