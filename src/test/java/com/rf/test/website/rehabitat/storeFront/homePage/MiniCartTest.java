package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class MiniCartTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-419 Mini Cart- Cleared after Checkout
	 * Description : This test validates that mini cart is empty
	 * after placed an order
	 *     
	 */
	@Test//Incomplete(need to com & biz url)
	public void testMiniCartClearedAfterCheckout_419(){
		String cardType = TestConstants.CARD_TYPE;
		String cardNumber = TestConstants.CARD_NUMBER;
		String cardName = TestConstants.CARD_NAME;
		String CVV = TestConstants.CVV;
		String noOfItem = "0";
		String noOfItemFromUI = null;   
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfHomePage.checkoutTheCart();
		sfHomePage.clickSaveButton();
		sfHomePage.clickShippingDetailsNextbutton();
		sfHomePage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfHomePage.selectBillingAddressFromDD();
		sfHomePage.checkUseMyDeliveryAddressChkBox();
		sfHomePage.clickBillingDetailsNextbutton();
		sfHomePage.selectTermsAndConditionsChkBox();
		sfHomePage.clickPlaceOrderButton();
		noOfItemFromUI = sfHomePage.getNumberOfItemFromMiniCart();
		s_assert.assertTrue(noOfItemFromUI.equalsIgnoreCase(noOfItem), "Expected no of item is "+noOfItem+" Actual on UI is "+noOfItemFromUI);
		s_assert.assertAll();
	}

}
