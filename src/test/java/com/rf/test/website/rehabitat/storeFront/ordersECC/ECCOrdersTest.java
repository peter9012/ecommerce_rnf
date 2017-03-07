package com.rf.test.website.rehabitat.storeFront.ordersECC;

import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
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
	private List<String> cardsListForFixedSelection = null;
	private List<String> cardsListForRandomSelection = null;
	private int fixedSelectionCountForAllCards;
	private int fixedSelectionCountToReset;
	private int cardSelectionCounter = 0; 

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
		cardType = getCardType();
		cardNumber = getCardNumberUsingType(cardType);		
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
		cardType = getCardType();
		cardNumber = getCardNumberUsingType(cardType);	
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
		cardType = getCardType();
		cardNumber = getCardNumberUsingType(cardType);	
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
		ExcelUtil.setHeadingsInTheExcel(setColumnHeadings());
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

	public List<String> setColumnHeadings(){
		List<String> columnHeadings = new ArrayList<String>();
		columnHeadings.add(TestConstants.ORDER_NUMBER_HEADING);
		columnHeadings.add(TestConstants.PRODUCT_NAME_HEADING);
		columnHeadings.add(TestConstants.ITEM_QTY_HEADING);
		columnHeadings.add(TestConstants.CARD_TYPE_HEADING);
		columnHeadings.add(TestConstants.ORDER_DATE_HEADING);
		columnHeadings.add(TestConstants.TOTAL_PRICE_HEADING);
		columnHeadings.add(TestConstants.ORDER_STATUS_HEADING);
		return columnHeadings;
	}

	public String getCardType(){
		// Resetting the Counts for each different TC
		if(cardSelectionCounter == 0){
			setAllCardsAndSelectionCount();
		}
		cardSelectionCounter++;
		if(cardSelectionCounter == var){
			cardSelectionCounter = 0;
		}

		// Selecting Fixed and random Card Type
		if(!cardsListForFixedSelection.isEmpty()){
			if(fixedSelectionCountForAllCards>0){
				fixedSelectionCountForAllCards--;
				return cardsListForFixedSelection.get(0);
			}
			else if(fixedSelectionCountForAllCards==0 && !(cardsListForFixedSelection.size() == 1)){
				cardsListForFixedSelection.remove(0);
				fixedSelectionCountForAllCards = fixedSelectionCountToReset;
				fixedSelectionCountForAllCards--;
				return cardsListForFixedSelection.get(0);
			}
			else if(fixedSelectionCountForAllCards==0 && cardsListForFixedSelection.size() == 1){
				cardsListForFixedSelection.remove(0);
			}
		}
		return cardsListForRandomSelection.get(CommonUtils.getRandomNum(0,3));
	}

	public void setAllCardsAndSelectionCount(){
		String visaCard = TestConstants.CARD_TYPE_VISA;
		String amexCard = TestConstants.CARD_TYPE_AMEX;
		String masterCard = TestConstants.CARD_TYPE_MASTER_CARD;
		String discoverCard = TestConstants.CARD_TYPE_DISCOVER;
		cardsListForFixedSelection = new ArrayList<String>();
		cardsListForRandomSelection = new ArrayList<String>();
		cardsListForFixedSelection.add(visaCard);
		cardsListForFixedSelection.add(amexCard);
		cardsListForFixedSelection.add(masterCard);
		cardsListForFixedSelection.add(discoverCard);
		cardsListForRandomSelection.add(visaCard);
		cardsListForRandomSelection.add(amexCard);
		cardsListForRandomSelection.add(masterCard);
		cardsListForRandomSelection.add(discoverCard);
		fixedSelectionCountForAllCards = var/4;
		fixedSelectionCountToReset = fixedSelectionCountForAllCards;
	}

	public String getCardNumberUsingType(String cardType){
		String cardNum = null;
		if(cardType.equals(TestConstants.CARD_TYPE_VISA)){
			cardNum = TestConstants.CARD_NUMBER_VISA;
		}
		if(cardType.equals(TestConstants.CARD_TYPE_AMEX)){
			cardNum = TestConstants.CARD_NUMBER_AMEX;
		}
		if(cardType.equals(TestConstants.CARD_TYPE_MASTER_CARD)){
			cardNum = TestConstants.CARD_NUMBER_MASTERCARD;
		}
		if(cardType.equals(TestConstants.CARD_TYPE_DISCOVER)){
			cardNum = TestConstants.CARD_NUMBER_VISA;
		}
		return cardNum;
	}

}