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
	String timeStamp; 

	public RCEnrollmentTest() {
		timeStamp = CommonUtils.getCurrentTimeStamp();
		firstName=TestConstants.RC_FIRST_NAME;
		lastName = TestConstants.LAST_NAME;
		email = firstName+timeStamp+TestConstants.EMAIL_SUFFIX;
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
	 * qTest : TC-550 RC Retail user enrollment - From Corp site
	 * Description : This test is for successfully enrolling a RC user
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testRCEnrollment_550(){
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_RC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton();
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(), "RC has not been enrolled successfully");
		s_assert.assertAll();
	}	
	
	/***
	 * qTest : TC-551 Retail user enrollment - From PWS site
	 * Description : This test is for successfully enrolling a RC user from pws site
	 * and redirection to pws site after RC enrollment.
	 *     
	 */
	@Test(enabled=false)//Redirect to corp site after enrollment from pws site
	public void testRCEnrollmentFromPWSSite_551(){
		String PWSSite = TestConstants.CONSULTANT_PWS;
		String currentURL = null;
		
		sfHomePage.navigateToUrl(PWSSite);
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_RC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton();
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(), "RC has not been enrolled successfully");
		/*currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(PWSSite), "Expected URL should contain"+PWSSite+"but actual on UI is"+currentURL);*/
		s_assert.assertAll();
	}	
	
	@Test(enabled=true, groups="users")
	public void testRCEnrollmentWithoutOrder(){
		navigateToStoreFrontBaseURL();
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_RC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton();
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(), "RC has not been enrolled successfully");
		s_assert.assertAll();
		rcWithoutOrder=email;
	}
	
	@Test(enabled=true, groups="users")
	public void testRCEnrollmentWithOrder(){
		navigateToStoreFrontBaseURL();
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_RC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton();
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(), "RC has not been enrolled successfully");
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(), "Adhoc order is not placed successfully by PC");
		s_assert.assertAll();
		rcWithoutOrder=email;		
	}
}
