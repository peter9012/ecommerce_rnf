package com.rf.test.website.storeFront.hybris.expressEnrollment.biz;

import org.testng.annotations.Test;
import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.test.website.RFStoreFrontWebsiteBaseTest;
import com.rf.test.website.RFWebsiteBaseTest;

public class BigBusinessLaunchKitTest extends RFWebsiteBaseTest{

	private StoreFrontHomePage storeFrontHomePage;
	private String kitName = null;
	private String regimenName = null;
	private String enrollmentType = null;
	private String addressLine1 = null;
	private String city = null;
	private String postalCode = null;
	private String phoneNumber = null;
	private String country = null;
	private String env = null;
	private String PWS = null;
	private String state = null;

	//Hybris Project-96:Express Enrollment Billing Info Shipping Info - New
	//Hybris Project-95:Express Enrollment Billing Profile Main Account Info - New
	//Hybris Project-62 :: Version : 1 :: Express EnrollmentTest USD 695 Big Business Launch Kit, Personal Regimen REVERSE REGIMEN
	@Test
	public void testExpressEnrollmentBusinessKitReverseRegimen_62_95_96() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		storeFrontHomePage = new StoreFrontHomePage(driver);
		country = driver.getCountry();
		env = driver.getEnvironment();
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REVERSE;
		storeFrontHomePage.openPWSSite(country, env);

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}

		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		// verify Edit shipping info
		int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		storeFrontHomePage.clickOnReviewAndConfirmShippingEditBtn();
		String newFirstName = TestConstants.FIRST_NAME+randomNum1;
		String newLastName = TestConstants.LAST_NAME+randomNum1;
		storeFrontHomePage.enterUserInformationForEnrollment(newFirstName, newLastName, password, addressLine1, city,state,postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		s_assert.assertTrue(storeFrontHomePage.isReviewAndConfirmPageContainsShippingAddress(addressLine1), "Shiiping address is not updated on Review and Confirm page after EDIT");
		s_assert.assertTrue(storeFrontHomePage.isReviewAndConfirmPageContainsFirstAndLastName(newFirstName+" "+newLastName), "First and last Name is not updated on Review and Confirm page after EDIT");

		//Verify Edit billing info
		int randomNum2 = CommonUtils.getRandomNum(10000, 1000000);
		storeFrontHomePage.clickOnReviewAndConfirmBillingEditBtn();
		storeFrontHomePage.enterEditedCardNumber(TestConstants.CARD_NUMBER);
		String editedBillingProfileName = TestConstants.FIRST_NAME+randomNum2;
		storeFrontHomePage.enterNameOnCard(editedBillingProfileName+" "+TestConstants.LAST_NAME);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);;
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
//		storeFrontHomePage.clickOnRodanAndFieldsLogo();
//		StoreFrontConsultantPage storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
//		StoreFrontBillingInfoPage storeFrontBillingInfoPage = new StoreFrontBillingInfoPage(driver);
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
//		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
//
//		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------
//
//		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(editedBillingProfileName),"Newly added/Edited Billing profile is NOT listed on the page");
		s_assert.assertAll(); 
	}
}
