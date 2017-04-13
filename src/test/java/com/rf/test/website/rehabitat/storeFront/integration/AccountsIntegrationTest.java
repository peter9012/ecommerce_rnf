package com.rf.test.website.rehabitat.storeFront.integration;

import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontCartPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontCheckoutPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontShopSkinCarePage;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class AccountsIntegrationTest extends StoreFrontWebsiteBaseTest{

	private String RFO_DB = null;

	/***
	 * 	 
	 * Description : This test validates that account flow of consultant to RFO
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testConsultantAccountFlowToRFO(){
		String currentURL = null;
		String currentWindowID = null;
		String urlToAssert = "pulse";
		String count=null;
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);  
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+"conswpwcrp"+timeStamp+TestConstants.EMAIL_SUFFIX;
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String prefix = firstName+timeStamp;
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, email, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		sfHomePage.chooseProductFromKitPage();
		sfHomePage.enterPrefix(prefix);
		sfHomePage.clickNextButton();
		sfHomePage.clickSaveButton();
		sfHomePage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2 ,city, state, postalCode, phoneNumber);
		sfHomePage.clickShippingDetailsNextbutton();
		sfHomePage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfHomePage.clickBillingDetailsNextbutton();
		if(sfHomePage.hasTokenizationFailed()==true){
			sfHomePage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
			sfHomePage.clickBillingDetailsNextbutton();
		}
		sfHomePage.selectPoliciesAndProceduresChkBox();
		sfHomePage.selectIAcknowledgeChkBox();
		sfHomePage.selectTermsAndConditionsChkBox();
		sfHomePage.selectConsentFormChkBox();
		sfHomePage.clickBecomeAConsultant();
		s_assert.assertTrue(sfHomePage.isEnrollemntSuccessfulMsgDisplayed(), "Expected 'ENROLLMENT SUCCESSFUL' msg has NOT displayed"); 
		sfHomePage.addFirstProductForCRPCheckout(validProductName,validProductId);
		sfCheckoutPage = sfHomePage.checkoutCRPBag();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsCheckBoxForAutoshipOrder();
		//  sfCheckoutPage.selectCheckboxForPoliciesAndProcedures();
		sfCheckoutPage.clickConfirmAutoshipOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isCRPOrderConfirmedSuccessMsgAppeared(),"CRP Order confirmed success messge is not appeared");
		sfCheckoutPage.clickRodanAndFieldsLogo();
		consultantWithPulseAndWithCRP=email;
		sfCheckoutPage.pauseExecutionFor(300000);

		// assert account flow of consultant to RFO
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ACCOUNT_QUERY_RFO,consultantWithPulseAndWithCRP),RFO_DB);
		count = String.valueOf(getValueFromQueryResult(randomConsultantList, "count"));
		s_assert.assertEquals(count, "1","Count is "+count+" Account= "+consultantWithPulseAndWithCRP+" has NOT flown into RFO");

		//assert Accounts flow from RFO to Legacy Pulse
		sfHomePage.clickWelcomeDropdown();
		currentWindowID = CommonUtils.getCurrentWindowHandle();
		sfHomePage.navigateToCheckMyPulsePage();
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is"+currentURL);
		sfHomePage.loginToPulse(consultantWithPulseAndWithCRP, password);
		sfHomePage.closePopup();
		s_assert.assertTrue(sfHomePage.isUserPresentInPulse(), "Expected user"+consultantWithPulseAndWithCRP+" is not present in pulse");
		sfHomePage.switchToParentWindow(currentWindowID);

		//assert account flow of SFDC for consultant
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.logout();
		sfHomePage.navigateToSFDCUrl();
		sfHomePage.loginToSFDC(getSFDCUserName(), getSFDCPassword());
		sfHomePage.clickAnyTypeOfActiveCustomerInSearchResultOfSFDC(consultantWithPulseAndWithCRP);
		s_assert.assertTrue(sfHomePage.isUserPresentInSFDC(TestConstants.USER_TYPE_CONSULTANT,consultantWithPulseAndWithCRP), "Expected user"+TestConstants.USER_TYPE_CONSULTANT+" "+consultantWithPulseAndWithCRP+ "not found in SFDC");
		s_assert.assertAll(); 
	}

	/***
	 * 	 
	 * Description : This test validates that account flow of PC to RFO
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testPCAccountFlowToRFO(){
		String count=null;
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+"pcwpwsspon"+timeStamp+TestConstants.EMAIL_SUFFIX;
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_PC);
		//		s_assert.assertTrue(sfCartPage.isPcOneTimeFeeMsgDisplayed(),"PC one time joining fee msg has not displayed");
		sfCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT,validProductId);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		//sfCartPage.enterQuantityOfProductAtCart("1", "2");
		//sfCartPage.clickOnUpdateLinkThroughItemNumber("1");
		sfCartPage.clickCheckoutBtn();
		sfCartPage.searchSponsor(TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		if(sfCheckoutPage.hasTokenizationFailed()==true){
			sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
			sfCheckoutPage.clickBillingDetailsNextbutton();
		}
		sfCheckoutPage.selectIAcknowledgePCChkBox();
		sfCheckoutPage.selectPCTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfHomePage.hasPCEnrolledSuccessfully(), "PC has not been enrolled successfully");
		sfCheckoutPage.clickRodanAndFieldsLogo();
		pcUserWithPWSSponsor=email;
		sfCheckoutPage.pauseExecutionFor(300000);
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ACCOUNT_QUERY_RFO,pcUserWithPWSSponsor),RFO_DB);
		count = String.valueOf(getValueFromQueryResult(randomConsultantList, "count"));
		s_assert.assertEquals(count, "1","Count is "+count+" Account= "+pcUserWithPWSSponsor+" has NOT flown into RFO");

		//assert account flow of SFDC for PC
		sfCheckoutPage.clickWelcomeDropdown();
		sfCheckoutPage.logout();
		sfCheckoutPage = new StoreFrontCheckoutPage(driver);
		sfCheckoutPage.navigateToSFDCUrl();
		sfHomePage.loginToSFDC(getSFDCUserName(), getSFDCPassword());
		sfCheckoutPage.clickAnyTypeOfActiveCustomerInSearchResultOfSFDC(pcUserWithPWSSponsor);
		s_assert.assertTrue(sfHomePage.isUserPresentInSFDC(TestConstants.USERTYPE_PREFERRED_CUSTOMER,pcUserWithPWSSponsor), "Expected user"+TestConstants.USERTYPE_PREFERRED_CUSTOMER+" "+pcUserWithPWSSponsor+ "not found in SFDC");
		s_assert.assertAll(); 
	}

	/***
	 * 	 
	 * Description : This test validates that account flow of RC to RFO
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testRCAccountFlowToRFO(){
		String count=null;
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		timeStamp = CommonUtils.getCurrentTimeStamp();
		email = firstName+"rc"+timeStamp+TestConstants.EMAIL_SUFFIX;
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		navigateToStoreFrontBaseURL();
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_RC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_RC);
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(), "RC has not been enrolled successfully");
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT, validProductId);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCartPage.searchSponsor(TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		if(sfCheckoutPage.hasTokenizationFailed()==true){
			sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
			sfCheckoutPage.clickBillingDetailsNextbutton();
		}
		sfCheckoutPage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		rcWithOrderWithoutSponsor=email;
		sfCheckoutPage.pauseExecutionFor(300000);
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ACCOUNT_QUERY_RFO,rcWithOrderWithoutSponsor),RFO_DB);
		count = String.valueOf(getValueFromQueryResult(randomConsultantList, "count"));
		s_assert.assertEquals(count, "1","Count is "+count+" Account= "+rcWithOrderWithoutSponsor+" has NOT flown into RFO");

		//assert account flow to SFDC for RC
		sfCheckoutPage.clickWelcomeDropdown();
		sfCheckoutPage.logout();
		sfCheckoutPage = new StoreFrontCheckoutPage(driver);
		sfCheckoutPage.navigateToSFDCUrl();
		sfHomePage.loginToSFDC(getSFDCUserName(), getSFDCPassword());
		sfCheckoutPage.clickAnyTypeOfActiveCustomerInSearchResultOfSFDC(rcWithOrderWithoutSponsor);
		s_assert.assertTrue(sfHomePage.isUserPresentInSFDC(TestConstants.USERTYPE_RETAIL_CUSTOMER,rcWithOrderWithoutSponsor), "Expected user"+TestConstants.USERTYPE_RETAIL_CUSTOMER+" "+rcWithOrderWithoutSponsor+ "not found in SFDC");
		s_assert.assertAll(); 
	}
}