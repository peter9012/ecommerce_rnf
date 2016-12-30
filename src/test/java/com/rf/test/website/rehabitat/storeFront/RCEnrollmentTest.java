package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontCartPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontShopSkinCarePage;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class RCEnrollmentTest extends StoreFrontWebsiteBaseTest{
	public String email=null;
	private String firstName = null;
	private String lastName = null;
	String addressLine1 = null;
	String addressLine2 = null;
	String city = null;
	String state = null;
	String postalCode = null;
	String phoneNumber = null;
	String cardType = null;
	String cardNumber = null;
	String cardName = null;
	String CVV = null;
	int randomNum; 

	public RCEnrollmentTest() {
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		firstName=TestConstants.RC_FIRST_NAME;
		lastName = TestConstants.LAST_NAME;
		email = firstName+randomNum+TestConstants.EMAIL_SUFFIX;
		addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		city = TestConstants.CITY_US;
		state = TestConstants.STATE_US;
		postalCode = TestConstants.POSTAL_CODE_US;
		phoneNumber = TestConstants.PHONE_NUMBER;
		cardType = TestConstants.CARD_TYPE;
		cardNumber = TestConstants.CARD_NUMBER;
		cardName = TestConstants.CARD_NAME;
		CVV = TestConstants.CVV;
	}

	/***
	 * qTest : TC-452 RC Retail user enrollment - From Corp site
	 * Description : This test is for successfully enrolling a RC user
	 * 
	 *     
	 */
	@Test//TODO
	public void testRCEnrollment_452(){
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_RC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton();
		sfCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.selectFirstProduct();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.enterQuantityOfProductAtCart("1", "10");
		sfCartPage.clickOnUpdateLinkThroughItemNumber("1");
		sfCartPage.clickCheckoutBtn();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.selectPoliciesAndProceduresChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfHomePage.hasPCEnrolledSuccessfully(), "PC has not been enrolled successfully");
		s_assert.assertAll();
	}	
}