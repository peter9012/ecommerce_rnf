package com.rf.test.website.rehabitat.storeFront.ordersECC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.rf.core.listeners.InvocationCountListener;
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
	private String userEmail = null;
	private String tax = null;
	Map<String,String> cardDetails = null;
	private static int varCount = 0;
	private String shippingAddress;

	private static final String FILE_PATH=System.getProperty("user.dir")+"\\src\\test\\resources\\ordersECC\\";
	private static final String FILE_NAME_US = "ordersECC_US.xlsx";
	private static final String FILE_NAME_CA = "ordersECC_CA.xlsx";

	// Place an adhoc order from consultant
	@Test(priority=1,invocationCount=var)
	public void testPlaceAnAdhocOrderFromConsultant(){
		String productNumber = null;
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		userEmail = consultantWithPulseAndWithCRP();
		sfHomePage.loginToStoreFront(userEmail, password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		cardType = getCardType();
		/*productNumber = sfShopSkinCarePage.getProductNumToSelectForCard(TestConstants.USER_TYPE_CONSULTANT,cardType, TestConstants.PRODUCT_NUMBER);
		productName = sfShopSkinCarePage.getProductName(productNumber);*/
		productName = validProductName;
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC,validProductId);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		itemQty = sfCartPage.getQuantityOfProductFromCart("1");
		sfCheckoutPage=sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickAddNewShippingAddressButton();
		setRandomAddressAsPerCountry();
		shippingAddress = getCompleteShippingAddress();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		cardDetails = getCardDetailsUsingType(cardType);
		cardNumber = cardDetails.get("cardNum");
		CVV = cardDetails.get("cardCVV");
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		if(sfCheckoutPage.hasTokenizationFailed()==true){
			sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
			sfCheckoutPage.clickBillingDetailsNextbutton();
		}
		tax = sfCheckoutPage.getEstimatedTaxForTheOrder();
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
		String productNumber = null;
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		userEmail = pcUserWithPWSSponsor();
		sfHomePage.loginToStoreFront(userEmail, password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		cardType = getCardType();
		/*productNumber = sfShopSkinCarePage.getProductNumToSelectForCard(TestConstants.USER_TYPE_PC,cardType, TestConstants.PRODUCT_NUMBER);
		productName = sfShopSkinCarePage.getProductName(productNumber);
		sfShopSkinCarePage.addProductToCart(productNumber, TestConstants.ORDER_TYPE_ADHOC);*/
		productName = validProductName;
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC,validProductId);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		itemQty = sfCartPage.getQuantityOfProductFromCart("1");
		sfCheckoutPage=sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickAddNewShippingAddressButton();
		setRandomAddressAsPerCountry();
		shippingAddress = getCompleteShippingAddress();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		cardDetails = getCardDetailsUsingType(cardType);
		cardNumber = cardDetails.get("cardNum");
		CVV = cardDetails.get("cardCVV");
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		if(sfCheckoutPage.hasTokenizationFailed()==true){
			sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
			sfCheckoutPage.clickBillingDetailsNextbutton();
		}
		tax = sfCheckoutPage.getEstimatedTaxForTheOrder();
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
		String productNumber = null;
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		userEmail = rcWithOrderWithoutSponsor();
		sfHomePage.loginToStoreFront(userEmail, password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		cardType = getCardType();
		/*productNumber = sfShopSkinCarePage.getProductNumToSelectForCard(TestConstants.USER_TYPE_RC,cardType, TestConstants.PRODUCT_NUMBER);
		productName = sfShopSkinCarePage.getProductName(productNumber);*/
		productName = validProductName;
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT,validProductId);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		itemQty = sfCartPage.getQuantityOfProductFromCart("1");
		sfCheckoutPage=sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickAddNewShippingAddressButton();
		setRandomAddressAsPerCountry();
		shippingAddress = getCompleteShippingAddress();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		cardDetails = getCardDetailsUsingType(cardType);
		cardNumber = cardDetails.get("cardNum");
		CVV = cardDetails.get("cardCVV");
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		if(sfCheckoutPage.hasTokenizationFailed()==true){
			sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
			sfCheckoutPage.clickBillingDetailsNextbutton();
		}
		tax = sfCheckoutPage.getEstimatedTaxForTheOrder();
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
		ExcelUtil.createNewSheetInECCOrdersExcelFile(getFilePathAsPerCountry(country));
		ExcelUtil.setHeadingsInTheExcel(setColumnHeadings());
		varCount = InvocationCountListener.getInvocationCount();
	}

	@AfterClass(alwaysRun=true)
	public void closeExcelSheets(){
		ExcelUtil.closeECCOrdersExcelFile();
	}

	public List<String> setOrderDetails(){
		List<String> orderDetails = new ArrayList<String>();
		orderDetails.add(userEmail);
		orderDetails.add(orderNumber);
		orderDetails.add(productName);
		orderDetails.add(itemQty);
		orderDetails.add(cardType);
		orderDetails.add(orderDate);
		orderDetails.add(totalPrice);
		orderDetails.add(tax);
		orderDetails.add(orderStatus);
		orderDetails.add(shippingAddress);
		return orderDetails;
	}

	public List<String> setColumnHeadings(){
		List<String> columnHeadings = new ArrayList<String>();
		columnHeadings.add(TestConstants.USER_EMAIL_HEADING);
		columnHeadings.add(TestConstants.ORDER_NUMBER_HEADING);
		columnHeadings.add(TestConstants.PRODUCT_NAME_HEADING);
		columnHeadings.add(TestConstants.ITEM_QTY_HEADING);
		columnHeadings.add(TestConstants.CARD_TYPE_HEADING);
		columnHeadings.add(TestConstants.ORDER_DATE_HEADING);
		columnHeadings.add(TestConstants.TOTAL_PRICE_HEADING);
		columnHeadings.add(TestConstants.TAX_HEADING);
		columnHeadings.add(TestConstants.ORDER_STATUS_HEADING);
		columnHeadings.add(TestConstants.SHIPPING_ADDRESS_HEADING);
		return columnHeadings;
	}

	public String getCardType(){
		// Resetting the Counts for each different TC
		if(cardSelectionCounter == 0){
			setAllCardsAndSelectionCount();
		}
		cardSelectionCounter++;
		if(cardSelectionCounter == varCount){
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
		/*return cardsListForRandomSelection.get(CommonUtils.getRandomNum(0,3));*/
		return cardsListForRandomSelection.get(CommonUtils.getRandomNum(0,cardsListForRandomSelection.size()-1));
	}

	public void setAllCardsAndSelectionCount(){
		String visaCard = TestConstants.CARD_TYPE_VISA;
		//	String amexCard = TestConstants.CARD_TYPE_AMEX;
		String masterCard = TestConstants.CARD_TYPE_MASTER_CARD;
		String discoverCard = TestConstants.CARD_TYPE_DISCOVER;
		cardsListForFixedSelection = new ArrayList<String>();
		cardsListForRandomSelection = new ArrayList<String>();
		cardsListForFixedSelection.add(visaCard);
		//	cardsListForFixedSelection.add(amexCard);
		cardsListForFixedSelection.add(masterCard);
		cardsListForFixedSelection.add(discoverCard);
		cardsListForRandomSelection.add(visaCard);
		//	cardsListForRandomSelection.add(amexCard);
		cardsListForRandomSelection.add(masterCard);
		cardsListForRandomSelection.add(discoverCard);
		fixedSelectionCountForAllCards = varCount/3;
		//	fixedSelectionCountForAllCards = varCount/4;
		fixedSelectionCountToReset = fixedSelectionCountForAllCards;
	}

	public Map<String,String> getCardDetailsUsingType(String cardType){
		Map<String,String> cardDetails = new HashMap<String,String>();
		if(cardType.equals(TestConstants.CARD_TYPE_VISA)){
			cardDetails.put("cardNum",TestConstants.CARD_NUMBER_VISA);
			cardDetails.put("cardCVV",TestConstants.CVV);
		}
		if(cardType.equals(TestConstants.CARD_TYPE_AMEX)){
			cardDetails.put("cardNum",TestConstants.CARD_NUMBER_AMEX);
			cardDetails.put("cardCVV",TestConstants.CVV_AMEX);
		}
		if(cardType.equals(TestConstants.CARD_TYPE_MASTER_CARD)){
			cardDetails.put("cardNum",TestConstants.CARD_NUMBER_MASTERCARD);
			cardDetails.put("cardCVV",TestConstants.CVV);
		}
		if(cardType.equals(TestConstants.CARD_TYPE_DISCOVER)){
			cardDetails.put("cardNum",TestConstants.CARD_NUMBER_DISCOVER);
			cardDetails.put("cardCVV",TestConstants.CVV);
		}
		return cardDetails;
	}

	public String getFilePathAsPerCountry(String country){
		String filePath = null;
		if(country.equalsIgnoreCase("us")){
			filePath = FILE_PATH+FILE_NAME_US;
		}
		if(country.equalsIgnoreCase("ca")){
			filePath = FILE_PATH+FILE_NAME_CA;
		}
		return filePath;
	}

	public void setValueInTheExcel(String orderType, int counter, List<String> orderDetails){
		if(orderType.equalsIgnoreCase(TestConstants.ECC_ORDER_TYPE_CONSULTANT_ADHOC)&& orderNumber!=null){
			ExcelUtil.setOrderDetailsInECCExcelFile(TestConstants.ECC_ORDER_TYPE_CONSULTANT_ADHOC,counter, orderDetails);
		}
		if(orderType.equalsIgnoreCase(TestConstants.ECC_ORDER_TYPE_PC_ADHOC)&& orderNumber!=null){
			ExcelUtil.setOrderDetailsInECCExcelFile(TestConstants.ECC_ORDER_TYPE_PC_ADHOC, counter, orderDetails);
		}
		if(orderType.equalsIgnoreCase(TestConstants.ECC_ORDER_TYPE_RC_ADHOC)&& orderNumber!=null){
			ExcelUtil.setOrderDetailsInECCExcelFile(TestConstants.ECC_ORDER_TYPE_RC_ADHOC, counter, orderDetails);
		}
	}

	public void setRandomAddressAsPerCountry(){
		int addressToSelect = CommonUtils.getRandomNum(1,3);
		if(country.equalsIgnoreCase("us")){
			switch(addressToSelect){  
			case 1:
				addressLine1 = TestConstants.ADDRESS_LINE_1_US;
				addressLine2 = TestConstants.ADDRESS_LINE_2_US;
				city = TestConstants.CITY_US;
				state = TestConstants.STATE_US;
				postalCode = TestConstants.POSTAL_CODE_US;
				break;
			case 2:
				addressLine1 = TestConstants.SECOND_ADDRESS_LINE_1_US;
				addressLine2 = TestConstants.SECOND_ADDRESS_LINE_2_US;
				city = TestConstants.SECOND_CITY_US;
				state = TestConstants.STATE_US;
				postalCode = TestConstants.SECOND_POSTAL_CODE_US;
				break;
			case 3:
				addressLine1 = TestConstants.THIRD_ADDRESS_LINE_1_US;
				addressLine2 = TestConstants.THIRD_ADDRESS_LINE_2_US;
				city = TestConstants.THIRD_CITY_US;
				state = TestConstants.STATE_US;
				postalCode = TestConstants.THIRD_POSTAL_CODE_US;
				break; 
			default:;
			}
		}
		else if(country.equalsIgnoreCase("ca")){
			switch(addressToSelect){  
			case 1:
				addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
				addressLine2 = TestConstants.ADDRESS_LINE_2_CA;
				city = TestConstants.CITY_CA;
				state = TestConstants.STATE_CA;
				postalCode = TestConstants.POSTAL_CODE_CA;
				break;  
			case 2:
				addressLine1 = TestConstants.SECOND_ADDRESS_LINE_1_CA;
				addressLine2 = TestConstants.SECOND_ADDRESS_LINE_2_CA;
				city = TestConstants.SECOND_CITY_CA;
				state = TestConstants.STATE_CA;
				postalCode = TestConstants.SECOND_POSTAL_CODE_CA;
				break;
			case 3:
				addressLine1 = TestConstants.THIRD_ADDRESS_LINE_1_CA;
				addressLine2 = TestConstants.THIRD_ADDRESS_LINE_2_CA;
				city = TestConstants.THIRD_CITY_CA;
				state = TestConstants.STATE_CA;
				postalCode = TestConstants.THIRD_POSTAL_CODE_CA;
				break;
			default:;
			}
		}
	}

	public String getCompleteShippingAddress(){
		return addressLine1 + " " + addressLine2 + " " + city + " " + state + " " + postalCode;

	}




}