package com.rf.test.website.rehabitat.storeFront.ordersECC;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.rf.core.utils.ExcelUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class ECCOrdersTest extends StoreFrontWebsiteBaseTest{

	private static final int var=2;
	private static int consOrdercounter=0;
	private static int pcOrdercounter=0;
	private static int rcOrdercounter=0;
	private static final String FILE_PATH=System.getProperty("user.dir")+"\\src\\test\\resources\\ordersECC\\ordersECC.xlsx";

	// Place an adhoc order from consultant
	@Test(priority=1,invocationCount=var)
	public void testPlaceAnAdhocOrderFromConsultant(){
		String consOrderNumber=null;
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectPCTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(),"Order not placed. Thank you message is not displayed");
		s_assert.assertAll();
		consOrderNumber = sfCheckoutPage.getOrderNumberAfterCheckout();
		consOrdercounter++;
		setValueInTheExcel(TestConstants.ECC_ORDER_TYPE_CONSULTANT_ADHOC, consOrderNumber, consOrdercounter);
	}

	//PC Adhoc Order
	@Test(priority=2,invocationCount=var)
	public void testPlaceAnAdhocOrderFromPC(){
		String pcOrderNumber=null;
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectPCTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(),"Order not placed. Thank you message is not displayed");
		s_assert.assertAll();
		pcOrderNumber = sfCheckoutPage.getOrderNumberAfterCheckout();
		pcOrdercounter++;
		setValueInTheExcel(TestConstants.ECC_ORDER_TYPE_PC_ADHOC, pcOrderNumber, pcOrdercounter);
	}

	//RC Adhoc Order
	@Test(priority=3,invocationCount=var)
	public void testPlaceAnAdhocOrderFromRC(){
		String rcOrderNumber=null;
		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(), password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectPCTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(),"Order not placed. Thank you message is not displayed");
		s_assert.assertAll();
		rcOrderNumber = sfCheckoutPage.getOrderNumberAfterCheckout();
		rcOrdercounter++;
		setValueInTheExcel(TestConstants.ECC_ORDER_TYPE_RC_ADHOC, rcOrderNumber, rcOrdercounter);
	}

	@BeforeClass
	public void createExcelSheets(){
		ExcelUtil.createNewSheetInECCOrdersExcelFile(FILE_PATH);
	}
	
	@AfterClass
	public void closeExcelSheets(){
		ExcelUtil.closeECCOrdersExcelFile();
	}

	public void setValueInTheExcel(String orderType, String orderNumber, int counter){
		if(orderType.equalsIgnoreCase(TestConstants.ECC_ORDER_TYPE_CONSULTANT_ADHOC)&& orderNumber!=null)
			ExcelUtil.setOrderValuesInECCExcelFile(FILE_PATH,TestConstants.ECC_ORDER_TYPE_CONSULTANT_ADHOC, 0, counter, orderNumber);
		if(orderType.equalsIgnoreCase(TestConstants.ECC_ORDER_TYPE_PC_ADHOC)&& orderNumber!=null)
			ExcelUtil.setOrderValuesInECCExcelFile(FILE_PATH,TestConstants.ECC_ORDER_TYPE_PC_ADHOC, 0, counter, orderNumber);
		if(orderType.equalsIgnoreCase(TestConstants.ECC_ORDER_TYPE_RC_ADHOC)&& orderNumber!=null)
			ExcelUtil.setOrderValuesInECCExcelFile(FILE_PATH,TestConstants.ECC_ORDER_TYPE_RC_ADHOC, 0, counter, orderNumber);
	}
}

