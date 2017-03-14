package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontCartPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontShopSkinCarePage;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class RCEnrollmentTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-551 Retail user enrollment - From PWS site
	 * Description : This test is for successfully enrolling a RC user from pws site
	 * and redirection to pws site after RC enrollment.
	 *     
	 */
	@Test(enabled=true)
	public void testRCEnrollmentFromPWSSite_551(){
		String prefix = pwsPrefix();
		String currentURL = null;
		String pwsURL=sfHomePage.getBaseUrl()+"/" +sfHomePage.getCountry() +"/pws/" + prefix;
		timeStamp = CommonUtils.getCurrentTimeStamp();
		email = firstName+"rc"+timeStamp+TestConstants.EMAIL_SUFFIX;
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		sfHomePage.navigateToUrl(pwsURL);
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_RC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_RC);
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(), "RC has not been enrolled successfully");
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.toLowerCase().contains(pwsURL.toLowerCase()), "Expected URL should contain"+pwsURL+"but actual on UI is"+currentURL);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-550 RC Retail user enrollment - From Corp site
	 * Description : This test is for successfully enrolling a RC user
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testRCEnrollmentWithOrderWithoutSponsor_550(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		email = firstName+"rc"+timeStamp+TestConstants.EMAIL_SUFFIX;
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_RC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_RC);
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(), "RC has not been enrolled successfully");
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(), "Adhoc order is not placed successfully by RC");
		rcWithOrderWithoutSponsor=email;
		userPropertyFile.loadProps(userProps);
		setUsers("rcWithOrderWithoutSponsor", rcWithOrderWithoutSponsor);
		s_assert.assertAll();
	}

	@Test
	public void testRCEnrollmentWithSponsor(){
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
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCartPage.searchSponsor(TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(), "Adhoc order is not placed successfully by PC");
		s_assert.assertAll();
		System.out.println(email);
	}
	
	@Test(enabled=true)
	public void testRCEnrollmentWithoutOrder(){
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
		rcWithoutOrder=email;
		s_assert.assertAll();
	}
}
