package com.rf.test.website.rehabitat.storeFront.ordersECC;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.rf.core.utils.ExcelUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class ECCOrdersTest extends StoreFrontWebsiteBaseTest{

	private static final int var=1;
	private static int consOrdercounter=0;
	private static int pcOrdercounter=0;
	private static int rcOrdercounter=0;
	private String orderNumber = null;
	private String productName = null;
	private String itemQty = null;
	private String orderDate = null;
	private String totalPrice = null;
	private String orderStatus = null;

	private static final String FILE_PATH=System.getProperty("user.dir")+"\\src\\test\\resources\\ordersECC\\ordersECC.xlsx";

	// Place an adhoc order from consultant
	@Test(priority=1,invocationCount=var)
	public void testPlaceAnAdhocOrderFromConsultant(){
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		productName = sfShopSkinCarePage.getProductName(TestConstants.PRODUCT_NUMBER);
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		itemQty = sfCartPage.getQuantityOfProductFromCart("1");
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
		orderNumber = sfCheckoutPage.getOrderNumberAfterCheckout();
		sfCheckoutPage.clickWelcomeDropdown();
		sfOrdersPage = sfCheckoutPage.navigateToOrdersPage();
		orderStatus = sfOrdersPage.getStatusOfOrderFromOrderHistory(orderNumber);
		totalPrice = sfOrdersPage.getValueForOrderFromOrderHistory(orderNumber, "Grand Total");
		orderDate = sfOrdersPage.getValueForOrderFromOrderHistory(orderNumber, "Order Date");
		consOrdercounter++;
		setValueInTheExcel(TestConstants.ECC_ORDER_TYPE_CONSULTANT_ADHOC, consOrdercounter, setOrderDetails());
	}

	//PC Adhoc Order
	@Test(priority=2,invocationCount=var)
	public void testPlaceAnAdhocOrderFromPC(){
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		productName = sfShopSkinCarePage.getProductName(TestConstants.PRODUCT_NUMBER);
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		itemQty = sfCartPage.getQuantityOfProductFromCart("1");
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
		orderNumber = sfCheckoutPage.getOrderNumberAfterCheckout();
		sfCheckoutPage.clickWelcomeDropdown();
		sfOrdersPage = sfCheckoutPage.navigateToOrdersPage();
		orderStatus = sfOrdersPage.getStatusOfOrderFromOrderHistory(orderNumber);
		totalPrice = sfOrdersPage.getValueForOrderFromOrderHistory(orderNumber, "Grand Total");
		orderDate = sfOrdersPage.getValueForOrderFromOrderHistory(orderNumber, "Order Date");
		pcOrdercounter++;
		setValueInTheExcel(TestConstants.ECC_ORDER_TYPE_PC_ADHOC, pcOrdercounter, setOrderDetails());
	}

	//RC Adhoc Order
	@Test(priority=3,invocationCount=var)
	public void testPlaceAnAdhocOrderFromRC(){
		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(), password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		productName = sfShopSkinCarePage.getProductName(TestConstants.PRODUCT_NUMBER);
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		itemQty = sfCartPage.getQuantityOfProductFromCart("1");
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
		orderNumber = sfCheckoutPage.getOrderNumberAfterCheckout();
		sfCheckoutPage.clickWelcomeDropdown();
		sfOrdersPage = sfCheckoutPage.navigateToOrdersPage();
		orderStatus = sfOrdersPage.getStatusOfOrderFromOrderHistory(orderNumber);
		totalPrice = sfOrdersPage.getValueForOrderFromOrderHistory(orderNumber, "Grand Total");
		orderDate = sfOrdersPage.getValueForOrderFromOrderHistory(orderNumber, "Order Date");
		rcOrdercounter++;
		setValueInTheExcel(TestConstants.ECC_ORDER_TYPE_RC_ADHOC, rcOrdercounter,setOrderDetails());
	}

	@BeforeClass
	public void createExcelSheets(){
		ExcelUtil.createNewSheetInECCOrdersExcelFile(FILE_PATH,country);
	}

	@AfterClass(alwaysRun=true)
	public void closeExcelSheets(){
		ExcelUtil.closeECCOrdersExcelFile();
	}

	public List<String> setOrderDetails(){
		List<String> orderDetails = new ArrayList<String>();
		orderDetails.add(orderNumber);
		orderDetails.add(productName);
		orderDetails.add(itemQty);
		orderDetails.add(cardType);
		orderDetails.add(orderDate);
		orderDetails.add(totalPrice);
		orderDetails.add(orderStatus);
		return orderDetails;
	}

	public void setValueInTheExcel(String orderType, int counter, List<String> orderDetails){
		if(orderType.equalsIgnoreCase(TestConstants.ECC_ORDER_TYPE_CONSULTANT_ADHOC)&& orderNumber!=null){
			ExcelUtil.setOrderDetailsInECCExcelFile(FILE_PATH, TestConstants.ECC_ORDER_TYPE_CONSULTANT_ADHOC,counter, orderDetails);
		}
		if(orderType.equalsIgnoreCase(TestConstants.ECC_ORDER_TYPE_PC_ADHOC)&& orderNumber!=null){
			ExcelUtil.setOrderDetailsInECCExcelFile(FILE_PATH,TestConstants.ECC_ORDER_TYPE_PC_ADHOC, counter, orderDetails);
		}
		if(orderType.equalsIgnoreCase(TestConstants.ECC_ORDER_TYPE_RC_ADHOC)&& orderNumber!=null){
			ExcelUtil.setOrderDetailsInECCExcelFile(FILE_PATH,TestConstants.ECC_ORDER_TYPE_RC_ADHOC, counter, orderDetails);
		}
	}


}

