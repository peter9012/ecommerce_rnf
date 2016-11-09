package com.rf.test.website.storeFront.hybris.expressEnrollment.corp;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.test.website.RFWebsiteBaseTest;

public class RFExpressBusinessKitTest extends RFWebsiteBaseTest{

	private StoreFrontHomePage storeFrontHomePage;
	private String kitName = null;
	private String regimenName = null;
	private String enrollmentType = null;
	private String addressLine1 = null;
	private String city = null;
	private String postalCode = null;
	private String phoneNumber = null;
	private String country = null;
	private String state = null;

	//Hybris Project-94:Express Enrollment Billing Profile Billing Info - Edit
	//Hybris Project-93 :: Version : 1 :: Express Enrollment Billing Profile Main Account Info - Edit
	//Hybris Project-2202 :: Version : 1 :: CORP:Express Enrollment USD995 RF Express Business Kit, Personal Regimen REVERSE REGIMEN(CRP:N,P:Y) 
	@Test
	public void testExpressEnrollmentRFExpressKitReverseRegimen_2202_93_94() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REVERSE;
		kitName = TestConstants.KIT_NAME_EXPRESS;
		String sRandName = RandomStringUtils.randomAlphabetic(12);

		if(country.equalsIgnoreCase("CA")){      
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{   
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();  
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, sRandName, TestConstants.PASSWORD, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
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
		//storeFrontHomePage.clickOnEnrollMeBtn();
		//s_assert.assertTrue(storeFrontHomePage.verifyPopUpForTermsAndConditions(), "PopUp for terms and conditions is not visible");
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		//the testcase should end here ^^^ We created the account :)
		s_assert.assertAll(); // Please don't comment this line
	}
	//Hybris Project-2204 :: Version : 1 :: CORP:Express Enrollment Business Portfolio Kit(CRP:N,P:N)
	@Test
	public void testExpressEnrollmentBusinessPorfolioKitRedefineRegimen_2204() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
		String sRandName = RandomStringUtils.randomAlphabetic(12);

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_PORTFOLIO;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{
			kitName = TestConstants.KIT_NAME_PORTFOLIO;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();  
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, TestConstants.FIRST_NAME+randomNum, sRandName, TestConstants.PASSWORD, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		//		s_assert.assertTrue(storeFrontHomePage.verifySubsribeToPulseCheckBoxIsSelected(), "Subscribe to pulse checkbox not selected");
		//		s_assert.assertTrue(storeFrontHomePage.verifyEnrollToCRPCheckBoxIsSelected(), "Enroll to CRP checkbox not selected");
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		s_assert.assertTrue(storeFrontHomePage.verifyShipImmediatelyRadioButtinIsSelected(), "Ship Immediately radio button is not selected");
		storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		s_assert.assertAll(); // Please don't comment this line
	}
}
