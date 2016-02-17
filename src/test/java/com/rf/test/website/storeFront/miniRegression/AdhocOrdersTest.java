package com.rf.test.website.storeFront.miniRegression;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class AdhocOrdersTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(AdhocOrdersTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;	
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private String RFO_DB = null;

	// Hybris Phase 2-1878 :: Version : 1 :: Create Adhoc Order For The Consultant Customer
	@Test //mini-regression test
	public void testCreateAdhocOrderConsultant_1878() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);


		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		/*storeFrontConsultantPage.clickOnShopLink();
		storeFrontConsultantPage.clickOnAllProductsLink();*/
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		//s_assert.assertTrue(storeFrontUpdateCartPage.verifyCheckoutConfirmation(),"Confirmation of order popup is not present");
		//storeFrontUpdateCartPage.clickOnConfirmationOK();

		String subtotal = String.valueOf(storeFrontUpdateCartPage.getSubtotal());
		logger.info("subtotal ="+subtotal);
		String deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		String handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
		logger.info("handlingCharges ="+handlingCharges);
		String tax = String.valueOf(storeFrontUpdateCartPage.getTax());
		logger.info("tax ="+tax);
		String total = String.valueOf(storeFrontUpdateCartPage.getTotal());
		logger.info("total ="+total);
		String totalSV = String.valueOf(storeFrontUpdateCartPage.getTotalSV());
		logger.info("totalSV ="+totalSV);
		String shippingMethod = storeFrontUpdateCartPage.getShippingMethod();
		logger.info("shippingMethod ="+shippingMethod);
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		String BillingAddress = storeFrontUpdateCartPage.getSelectedBillingAddress();
		logger.info("BillingAddress ="+BillingAddress);

		storeFrontUpdateCartPage.clickOnDefaultBillingProfileEdit();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();

		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		//String orderNumber = storeFrontUpdateCartPage.getOrderNumberAfterPlaceOrder();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");

		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();

		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);

		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subtotal),"Adhoc Order template subtotal "+subtotal+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());

		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(tax),"Adhoc Order template tax "+tax+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());

		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(total),"Adhoc Order template grand total "+total+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());

		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingCharges),"Adhoc Order template handling amount "+handlingCharges+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		s_assert.assertTrue(shippingMethod.contains(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate()),"Adhoc Order template shipping method "+shippingMethod+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());

		s_assert.assertTrue(totalSV.contains(storeFrontOrdersPage.getTotalSVValue()), "Adhoc order template total sv value "+totalSV+"and on UI is "+storeFrontOrdersPage.getTotalSVValue());

		s_assert.assertAll();
	}

	// Hybris Phase 2-1877 :: Version : 1 :: Create Adhoc Order For The Preferred Customer 
	@Test //mini-regression test
	public void testCreateAdhocOrderPC_1877() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();

		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	

		//s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"PC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		/*storeFrontPCUserPage.clickOnShopLink();
		storeFrontPCUserPage.clickOnAllProductsLink();*/
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		//s_assert.assertTrue(storeFrontUpdateCartPage.verifyCheckoutConfirmation(),"Confirmation of order popup is not present");
		//storeFrontUpdateCartPage.clickOnConfirmationOK();

		String subtotal = storeFrontUpdateCartPage.getSubtotal();
		logger.info("subtotal ="+subtotal);
		String deliveryCharges = storeFrontUpdateCartPage.getDeliveryCharges();
		logger.info("deliveryCharges ="+deliveryCharges);
		String handlingCharges = storeFrontUpdateCartPage.getHandlingCharges();
		logger.info("handlingCharges ="+handlingCharges);
		String tax = storeFrontUpdateCartPage.getTax();
		logger.info("tax ="+tax);
		String total = storeFrontUpdateCartPage.getTotal();
		logger.info("total ="+total);
		//String totalSV = storeFrontUpdateCartPage.getTotalSV();
		//logger.info("totalSV ="+totalSV);
		String shippingMethod = storeFrontUpdateCartPage.getShippingMethod();
		logger.info("shippingMethod ="+shippingMethod);
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		String BillingAddress = storeFrontUpdateCartPage.getSelectedBillingAddress();
		logger.info("BillingAddress ="+BillingAddress);

		storeFrontUpdateCartPage.clickOnDefaultBillingProfileEdit();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();

		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		//String orderNumber = storeFrontUpdateCartPage.getOrderNumberAfterPlaceOrder();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");

		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		//storeFrontOrdersPage.clickOrderNumber(orderNumber);
		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);


		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subtotal),"Adhoc Order template subtotal "+subtotal+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());

		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(tax),"Adhoc Order template tax "+tax+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());

		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(total),"Adhoc Order template grand total "+total+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());

		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingCharges),"Adhoc Order template handling amount "+handlingCharges+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		s_assert.assertTrue(shippingMethod.contains(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate()),"Adhoc Order template shipping method "+shippingMethod+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());

		//s_assert.assertTrue(storeFrontOrdersPage.getTotalSVValue().contains(totalSV), "Adhoc order template total sv value "+totalSV+"and on UI is "+storeFrontOrdersPage.getTotalSVValue());

		s_assert.assertAll();
	}

	// Hybris Phase 2-1879 :: Version : 1 :: Create Adhoc Order For The Retail Customer
	@Test //mini-regression test
	public void testCreateAdhocOrderRC_1879() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;

		String rcUserEmailID =null;
		String accountId = null;

		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	

		//s_assert.assertTrue(storeFrontRCUserPage.verifyRCUserPage(rcUserEmailID),"RC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		/*storeFrontRCUserPage.clickOnShopLink();
		storeFrontRCUserPage.clickOnAllProductsLink();  */ 
		storeFrontRCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickOnContinueWithoutSponsorLink();
		storeFrontUpdateCartPage.clickOnNextButtonAfterSelectingSponsor();

		String subtotal = storeFrontUpdateCartPage.getSubtotal();
		logger.info("Subtotal while creating order is "+subtotal);
		String deliveryCharges = storeFrontUpdateCartPage.getDeliveryCharges();
		logger.info("Delivery charges while creating order is "+deliveryCharges);
		String handlingCharges = storeFrontUpdateCartPage.getHandlingCharges();
		logger.info("Handling charges while creating order is "+handlingCharges);
		String tax = storeFrontUpdateCartPage.getTax();
		logger.info("Tax while creating order is "+tax);
		String total = storeFrontUpdateCartPage.getTotal();
		logger.info("Total while creating order is "+total);
		String shippingMethod = storeFrontUpdateCartPage.getShippingMethod();
		logger.info("shippingMethod ="+shippingMethod);
		//  String shippingMethod = storeFrontUpdateCartPage.getShippingMethod();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		//  String BillingAddress = storeFrontUpdateCartPage.getSelectedBillingAddress();

		storeFrontUpdateCartPage.clickOnDefaultBillingProfileEdit();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		//String orderNumber = storeFrontUpdateCartPage.getOrderNumberAfterPlaceOrder();
		//logger.info("Order Number after placing the order is "+orderNumber);
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");

		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		//storeFrontOrdersPage.clickOrderNumber(orderNumber);
		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);

		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subtotal),"Adhoc Order template subtotal "+subtotal+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());

		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(tax),"Adhoc Order template tax "+tax+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());

		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(total),"Adhoc Order template grand total "+total+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());

		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingCharges),"Adhoc Order template handling amount "+handlingCharges+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		s_assert.assertTrue(shippingMethod.contains(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate()),"Adhoc Order template shipping method "+shippingMethod+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());

		s_assert.assertAll();
	}

	//Hybris Project-1880:Create Adhoc Order with Multiple line items
	@Test
	public void testCreateAdhocOrderWithMultipleLineItems_1880() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		logger.info("login is successful");
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		String firstProductName = storeFrontUpdateCartPage.clickAddToBagAndGetProductName("1");
		storeFrontUpdateCartPage.clickOnContinueShoppingLink();
		String secondProductName = storeFrontUpdateCartPage.clickAddToBagAndGetProductName("2");
		storeFrontUpdateCartPage.clickOnContinueShoppingLink();
		String thirdProductName = storeFrontPCUserPage.clickAddToBagAndGetProductName("3");
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		// get details of adhoc order
		String subtotal = storeFrontUpdateCartPage.getSubtotal();
		logger.info("subtotal ="+subtotal);
		String deliveryCharges = storeFrontUpdateCartPage.getDeliveryCharges();
		logger.info("deliveryCharges ="+deliveryCharges);
		String handlingCharges = storeFrontUpdateCartPage.getHandlingCharges();
		logger.info("handlingCharges ="+handlingCharges);
		String tax = storeFrontUpdateCartPage.getTax();
		logger.info("tax ="+tax);
		String total = storeFrontUpdateCartPage.getTotal();
		logger.info("total ="+total);
		String shippingMethod = storeFrontUpdateCartPage.getShippingMethod();
		logger.info("shippingMethod ="+shippingMethod);
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		String BillingAddress = storeFrontUpdateCartPage.getSelectedBillingAddress();
		logger.info("BillingAddress ="+BillingAddress);
		storeFrontUpdateCartPage.clickOnDefaultBillingProfileEdit();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");
		//assert product names
		s_assert.assertTrue(storeFrontUpdateCartPage.getProductNameAtOrderConfirmationPage("2").contains(firstProductName),"When select first product, Product name "+firstProductName+" and at order confirmation page "+storeFrontUpdateCartPage.getProductNameAtOrderConfirmationPage("2"));
		s_assert.assertTrue(storeFrontUpdateCartPage.getProductNameAtOrderConfirmationPage("3").contains(secondProductName),"When select second product, Product name "+secondProductName+" and at order confirmation page "+storeFrontUpdateCartPage.getProductNameAtOrderConfirmationPage("3"));
		s_assert.assertTrue(storeFrontUpdateCartPage.getProductNameAtOrderConfirmationPage("4").contains(thirdProductName),"When select third product, Product name "+thirdProductName+" and at order confirmation page "+storeFrontUpdateCartPage.getProductNameAtOrderConfirmationPage("4"));
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subtotal),"Adhoc Order template subtotal "+subtotal+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(tax),"Adhoc Order template tax "+tax+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(total),"Adhoc Order template grand total "+total+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingCharges),"Adhoc Order template handling amount "+handlingCharges+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
		s_assert.assertTrue(shippingMethod.contains(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate()),"Adhoc Order template shipping method "+shippingMethod+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());
		//assert product names at adhoc template
		s_assert.assertTrue(storeFrontOrdersPage.getProductNameFromAdhocTemplate("2").contains(firstProductName),"When select first product, Product name "+firstProductName+" and at Adhoc template "+storeFrontOrdersPage.getProductNameFromAdhocTemplate("2"));
		s_assert.assertTrue(storeFrontOrdersPage.getProductNameFromAdhocTemplate("3").contains(secondProductName),"When select second product, Product name "+secondProductName+" and at Adhoc template "+storeFrontOrdersPage.getProductNameFromAdhocTemplate("3"));
		s_assert.assertTrue(storeFrontOrdersPage.getProductNameFromAdhocTemplate("4").contains(thirdProductName),"When select third product, Product name "+thirdProductName+" and at Adhoc template "+storeFrontOrdersPage.getProductNameFromAdhocTemplate("4"));
		s_assert.assertAll();
	}

	//Hybris Project-1890:Create Order With Shipping Method UPS 2Day-CAD$20.00
	@Test
	public void testCreateOrderWithShippingMethodUPS2Day_1890() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID =null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontOrdersPage=new StoreFrontOrdersPage(driver);
		storeFrontRCUserPage=new StoreFrontRCUserPage(driver);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		logger.info("login is successful");
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		storeFrontHomePage.selectProductAndProceedToBuy();
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//click edit next to shipment Section 
		storeFrontHomePage.clickEditShippingInShipmentOnCheckoutPage();
		//select UPS 2 Day shipping method
		String selectedShippingMethod=storeFrontHomePage.selectShippingMethodUPS2DayUnderShippingSectionAndGetName();
		//click next on shipping section
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		//click place order
		storeFrontHomePage.clickPlaceOrderBtn();
		//Navigate to Orders Page
		storeFrontHomePage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		//click on first adhoc order placed
		storeFrontOrdersPage.clickOnFirstAdHocOrder();
		//validate the shipping Method as selected on Orders detail page
		s_assert.assertTrue(storeFrontOrdersPage.verifyShippingMethodOnTemplateAfterAdhocOrderPlaced(selectedShippingMethod), "Selected Shipping Method didn't match with the method on Orders page!!");
		s_assert.assertAll(); 
	}

	//Hybris Phase 2-4286 :: Version : 1 :: Verify order details of CRP autoship order
	@Test
	public void testOrderDetailsOfCRPAutoShipOrder_HP2_4286() throws SQLException, InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String locale = null;
		String region = null;
		String country = null;
		String shippingAddressFromDB =null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null;	
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String consultantEmailID = null;
		String lastName = null;
		String accountId = null;
		String shippingMethodId =null;
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		DecimalFormat df = new DecimalFormat("#.00");
		storeFrontHomePage = new StoreFrontHomePage(driver);

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}		

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number for assert
		String autoshipNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		//get Autoship Id Fro RFO
		List<Map<String, Object>> autoshipIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_AUTOSHIP_ID_FOR_RFO, autoshipNumber),RFO_DB);
		String autoshipID = String.valueOf(getValueFromQueryResult(autoshipIdDetailsList, "AutoshipID"));
		System.out.println("Autoship id "+autoshipID);
		List<Map<String,Object>> shippingAddressList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_FOR_AUTOSHIP_TEMPLATE_RFO, autoshipID), RFO_DB);
		firstName = (String) getValueFromQueryResult(shippingAddressList, "FirstName");
		lastName = (String) getValueFromQueryResult(shippingAddressList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "Address1");
		postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
		locale = (String) getValueFromQueryResult(shippingAddressList, "Locale");
		region = (String) getValueFromQueryResult(shippingAddressList, "Region");
		country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		if(country.equals("40")){
			country = "canada"; 
		}else if(country.equals("236")){
			country = "United States";
		}
		shippingAddressFromDB = firstName.trim()+" "+lastName.trim()+"\n"+ addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase();
		shippingAddressFromDB = shippingAddressFromDB.trim().toLowerCase();
		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_AND_HANDLING_COST_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
		shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
		shippingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
		handlingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));

		getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_TOTAL_SUBTOTAL_TAX_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "Total")));

		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);

		//assert shipping Address with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate().contains(shippingAddressFromDB), "Adhoc Order template shipping address on RFO is"+shippingAddressFromDB+" and on UI is "+storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate());

		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"CRP autoship template subTotal on RFO is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());

		// Assert Tax with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"CRP autoship template tax amount on RFO is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate());

		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"CRP autoship template grand total on RFO is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());

		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"CRP autoship template shipping amount on RFO is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());

		// assert Handling Value with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"CRP autoship template handling amount on RFO is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		// assert for shipping Method with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(shippingMethodDB),"CRP autoship template shipping method on RFO is "+shippingMethodDB+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());
		s_assert.assertAll();
	}

	// Hybris Phase 2-4300 :: Version : 1 :: Verify PC autoship template details. 
	@Test
	public void testPCAutoShipTemplateDetails_HP2_4300() throws InterruptedException, SQLException , ClassNotFoundException{
		RFO_DB = driver.getDBNameRFO();
		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String locale = null;
		String region = null;
		String country = null;
		String shippingAddressFromDB =null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null;	
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String pcUserEmailID = null;
		String lastName = null;
		String accountId = null;
		String shippingMethodId = null;
		List<Map<String, Object>> randomPCUserList =  null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		DecimalFormat df = new DecimalFormat("#.00");
		storeFrontHomePage = new StoreFrontHomePage(driver);

		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	

		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String autoshipNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		s_assert.assertTrue(storeFrontOrdersPage.verifyPCPerksAutoShipHeader(),"Order Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfScheduleDateText(),"Schedule Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");

		//get Autoship Id Fro RFO
		List<Map<String, Object>> autoshipIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_AUTOSHIP_ID_FOR_RFO, autoshipNumber),RFO_DB);
		String autoshipID = String.valueOf(getValueFromQueryResult(autoshipIdDetailsList, "AutoshipID"));

		List<Map<String,Object>> shippingAddressList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_FOR_AUTOSHIP_TEMPLATE_RFO, autoshipID), RFO_DB);
		firstName = (String) getValueFromQueryResult(shippingAddressList, "FirstName");
		lastName = (String) getValueFromQueryResult(shippingAddressList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "Address1");
		postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
		locale = (String) getValueFromQueryResult(shippingAddressList, "Locale");
		region = (String) getValueFromQueryResult(shippingAddressList, "Region");
		country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		if(country.equals("40")){
			country = "canada"; 
		}else if(country.equals("236")){
			country = "United States";
		}
		shippingAddressFromDB = firstName.trim()+" "+lastName.trim()+"\n"+ addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase();
		shippingAddressFromDB = shippingAddressFromDB.trim().toLowerCase();
		shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_AND_HANDLING_COST_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
		shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
		shippingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
		handlingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));

		getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_TOTAL_SUBTOTAL_TAX_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "Total")));

		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);

		//assert shipping Address with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate().contains(shippingAddressFromDB), "Adhoc Order template shipping address on RFO is"+shippingAddressFromDB+" and on UI is "+storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate());

		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"PC autoship template subTotal is "+subTotalDB+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());

		// Assert Tax with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"PC autoship template tax amount is "+taxDB+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate());

		// Assert Grand Total with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"PC autoship template grand total is "+grandTotalDB+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());

		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"PC autoship template shipping amount is "+shippingDB+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());

		// assert Handling Value with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"PC autoship template handling amount is "+handlingDB+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());

		// assert for shipping Method with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(shippingMethodDB),"PC autoship template shipping method is "+shippingMethodDB+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());

		s_assert.assertAll();
	}


}
