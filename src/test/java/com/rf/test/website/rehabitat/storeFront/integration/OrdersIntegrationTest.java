package com.rf.test.website.rehabitat.storeFront.integration;

import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontCartPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontShopSkinCarePage;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class OrdersIntegrationTest extends StoreFrontWebsiteBaseTest{

	private String RFO_DB = null;
	private String productName = null;
	private String orderNumber = null;

	/***
	 * 	 
	 * Description : This test validates that account flow of consultant to RFO
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testConsultantOrderFlowToRFO(){
		String count=null;
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> ordersList =  null;
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		validProductId="ENPS125";
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC,validProductId);
		sfShopSkinCarePage.clickOnAddMoreItemsOnCheckoutPopUp();
		validProductId="AARGAC1";
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC,validProductId);
		sfShopSkinCarePage.clickOnAddMoreItemsOnCheckoutPopUp();
		validProductId="PFC0001";
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC,validProductId);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		if(sfCheckoutPage.hasTokenizationFailed()==true){
			sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
			sfCheckoutPage.clickBillingDetailsNextbutton();
		}
		sfCheckoutPage.selectPCTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(),"Order not placed. Thank you message is not displayed");
		orderNumber = sfCheckoutPage.getOrderNumberAfterCheckout();
		sfCheckoutPage.pauseExecutionFor(360000);
		ordersList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_QUERY_RFO,orderNumber),RFO_DB);
		count = String.valueOf(getValueFromQueryResult(ordersList, "count"));
		s_assert.assertEquals(count, "1","Count is "+count+" OrderNumber = "+orderNumber+" has NOT flown into RFO");
		s_assert.assertAll(); 
	}
}