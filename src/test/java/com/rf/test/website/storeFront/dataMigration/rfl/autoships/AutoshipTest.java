package com.rf.test.website.storeFront.dataMigration.rfl.autoships;

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
import com.rf.core.website.constants.dbQueries.DBQueries_RFL;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.StoreFrontCartAutoShipPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontOrdersPage;
import com.rf.pages.website.StoreFrontPCUserPage;
import com.rf.pages.website.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;


public class AutoshipTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(AutoshipTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;	
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private String RFL_DB = null;
	private String RFO_DB = null;

	// Hybris Phase 2-130:change shipping method on autoship - PC	
	@Test
	public void testChangeShippingMethodOnPCAutoShip_130() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCList =  null;
		String pcUserEmailID = null;
		String accountID = null;
		
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
		pcUserEmailID = (String) getValueFromQueryResult(randomPCList, "UserName");
		accountID = String.valueOf(getValueFromQueryResult(randomPCList, "AccountID"));
		logger.info("The Account ID of the user is "+accountID);
		
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, TestConstants.PC_USER_PASSWORD_RFL);
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnShopLink();
		storeFrontPCUserPage.clickOnAllProductsLink();
		storeFrontCartAutoShipPage = storeFrontPCUserPage.addProductToPCPerk();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditShipping();
		String selectedShippingMethod = storeFrontUpdateCartPage.selectAndGetShippingMethodName();
		if(selectedShippingMethod.contains("FedEx Grnd")){
			selectedShippingMethod = "FedEx Ground (HD)";
		}
		logger.info("Shipping Method selected is: "+selectedShippingMethod);
		storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtn();
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();

		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		s_assert.assertTrue(storeFrontOrdersPage.verifyShippingMethod(selectedShippingMethod),"shipping method is NOT "+selectedShippingMethod);
		logout();
		s_assert.assertAll();
	}

	// Hybris Phase 2-131:change shipping method on autoship - Consultant
	@Test
	public void testChangeShippingMethodOnConsultantAutoShip_131() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
		accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		logger.info("Account Id of the user is "+accountID);

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);		
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickNextCRP();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditShipping();
		String selectedShippingMethod = storeFrontUpdateCartPage.selectAndGetShippingMethodName();
		logger.info("Shipping Method selected is: "+selectedShippingMethod);
		storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtn();
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();

		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		assertTrue("shipping method is not as expected", storeFrontOrdersPage.verifyShippingMethod(selectedShippingMethod));
		logout();
		s_assert.assertAll();
	}

	//Hybris Phase 2-4289 : Verify CRP autoship template details
	@Test(enabled=true)
	public void testCRPAutoShipTemplateDetails_HP2_4289() throws InterruptedException, SQLException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();

		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String locale = null;
		String region = null;
		String country = null;
		String shippingAddressFromDB =null;
		String city = null;
		String state = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null; 
		String grandTotalDB = null;
		String shippingMethod = null;
		//String consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_TST4; // A hard code user
		String consultantEmailID = null;
		String lastName = null;
		String shippingMethodId = null;
		String shippingMethodDB = null;

		List<Map<String, Object>> autoShipItemDetailsList = null;
		List<Map<String, Object>> shippingAddressList = null;
		List<Map<String, Object>> shippingMethodList = null;
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> orderIdAccountIdDetailsList =  null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");


		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		String autoshipNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		s_assert.assertTrue(storeFrontOrdersPage.verifyCRPAutoShipHeader(),"CRP Autoship template header is not as expected");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfScheduleDateText(),"Schedule Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");

		//verify shipping address in RFL
		shippingAddressList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_SHIPPING_ADDRESS_DETAILS_FOR_CONSULTANT_RFL_4289, autoshipNumber),RFL_DB);
		firstName = (String) getValueFromQueryResult(shippingAddressList, "FirstName");
		lastName = (String) getValueFromQueryResult(shippingAddressList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "Address1");
		city = (String) getValueFromQueryResult(shippingAddressList, "City");
		state = (String) getValueFromQueryResult(shippingAddressList, "State");
		postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostCode");
		country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingMethodId = String.valueOf(getValueFromQueryResult(shippingAddressList, "ShippingMethodID"));
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		
		// get orderId for RFO
		orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_FOR_ALL_RFO, autoshipNumber),RFO_DB);
		String orderId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));

		/*//verify shipping address in RFO
			if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB),RFL_DB)==false){
				shippingAddressList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
		   firstName = (String) getValueFromQueryResult(shippingAddressList, "FirstName");
		   lastName = (String) getValueFromQueryResult(shippingAddressList, "LastName");
		   addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "AddressLine1");
		   postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
		   locale = (String) getValueFromQueryResult(shippingAddressList, "Locale");
		   region = (String) getValueFromQueryResult(shippingAddressList, "Region");
		   country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		   if(country.equals("236")){
		    country = "United States"; 
		   }
		   shippingAddressFromDB = firstName+" "+lastName+"\n"+ addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n";
		   assertTrue("Shipping Address is not as expected",  storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB));

				System.out.println("Shipping address in RFL");
			}*/
		//verify autoship item details in RFL
		DecimalFormat df = new DecimalFormat("#.00");
		autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_DETAILS_FOR_CONSULTANT_RFL_4289, autoshipNumber),RFL_DB);
		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(autoShipItemDetailsList, "Subtotal")));
		shippingDB = String.valueOf(df.format(getValueFromQueryResult(autoShipItemDetailsList, "ShippingAmount")));
		handlingDB = String.valueOf(df.format(getValueFromQueryResult(autoShipItemDetailsList, "HandlingAmount")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(autoShipItemDetailsList, "TaxAmountTotal")));
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(autoShipItemDetailsList, "Total")));

		// Assert shipping Amount in RFL
		if(assertTrueDB("CRP Autoship Shipping Amount is NOT as in RFL DB",storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB),RFL_DB)==false){
			//verify shipping amount in RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
			assertTrue("Shipping Amount is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));
		}

		// Assert handling Amount with RFL
		if(assertTrueDB("CRP Autoship Handling value is NOT as in RFL DB",storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB),RFL_DB)==false){
			//verify handling amount in RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			handlingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
			assertTrue("Handling Value is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));
		}

		// assert Shipping Method with RFL
		shippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_SHIPPING_METHOD_FOR_CONSULTANT_RFL_4289, shippingMethodId),RFL_DB);
		String methodName = String.valueOf(getValueFromQueryResult(shippingMethodList, "Name"));
		String methodShortName = String.valueOf(getValueFromQueryResult(shippingMethodList, "ShortName"));
		if(methodName.equalsIgnoreCase("FedEx Grnd")){
			methodName = "FedEx Ground";
		}
		shippingMethod = methodName+" "+"("+methodShortName+")";
		if(assertTrueDB("Shipping Method is NOT as in RFL DB", storeFrontOrdersPage.verifyShippingMethod(shippingMethod),RFL_DB)==false){
			// assert shipping method with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
			DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
			methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
			assertTrue("Shipping Method is NOT as in RFO DB", storeFrontOrdersPage.verifyShippingMethod(methodName));
		}

		// assert Subtotal with RFL
		if(assertTrueDB("Subtotal is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB), RFL_DB) == false){
			// assert subtotal with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
			assertTrue("SubTotal is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));
		}

		// assert Tax with RFL
		if(assertTrueDB("Tax is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateTax(taxDB), RFL_DB) == false){
			// assert Tax with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			taxDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
			assertTrue("Tax is not as expected on RFO", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));
		}

		// assert Grand Total with RFL
		if(assertTrueDB("Grand Total is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB), RFL_DB) == false){
			// assert Grand Total with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "Total")));
			assertTrue("Grand Total is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB));
		}

		logout();
		s_assert.assertAll();
	}



	// Hybris Phase 2-4300 :: Version : 1 :: Verify PC autoship template details. 
	@Test(enabled=true)
	public void testPCAutoShipTemplateDetails_HP2_4300() throws InterruptedException, SQLException , ClassNotFoundException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO(); 

		String autoShipItemTotalPriceFromDB = null;
		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String locale = null;
		String region = null;
		String country = null;
		String shippingAddressFromDB =null;
		String city = null;
		String state = null;
		String phoneNumber = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null; 
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String lastName = null;

		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		List<Map<String,Object>> verifyAllDetailsList = null;


		List<Map<String, Object>> randomPCList =DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
		String pcEmailID = (String) getValueFromQueryResult(randomPCList, "Username");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, TestConstants.PC_PASSWORD_TST4);
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		String autoshipNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfScheduleDateText(),"Schedule Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_DETAILS_ACTIVE_PC_USER_HAVING_AUTOSHIP_ORDER_RFL, autoshipNumber), RFL_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "BillingFirstName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		DecimalFormat df = new DecimalFormat("#.00");

		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "Subtotal")));
		shippingDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "ShippingAmount")));
		handlingDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "HandlingAmount")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "TaxAmount")));
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "GrandTotal")));
		String shippingMethodId = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "ShippingMethodId"));
		String paymentTypeId =  String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "PayMentTypeID"));


		// get orderId for RFO
		orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_FOR_ALL_RFO, autoshipNumber),RFO_DB);
		String orderId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));

		// assert shipping Address with RFL
		/*	if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB), RFL_DB)== false){
						// Assert Shipping Address with RFo
				verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
			   firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
			   lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
			   addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
			   city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
			   state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
			   postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
			   shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n";
			   assertTrue(storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB), "Shipping Address is not as expected");
				}*/


		// Assert for PC Perks SubTotal with RFL
		if(assertTrueDB("PC Perks SubTotal value is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB), RFL_DB) == false){
			// assert for PC Perks SubTotal with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
			assertTrue("SubTotal is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));
		}


		// assert PC Perks Shipping value with RFL
		if(assertTrueDB("PC Perks Shipping value is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB), RFL_DB)== false){
			// assert PC Perks Shipping value with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
			assertTrue("Shipping Amount is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));
		}

		// assert PC Handling value with RFL
		if(assertTrueDB("PC Perks Handling value is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB), RFL_DB) == false){
			// assert PC Perks Handling value with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			handlingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
			assertTrue("Handling Value is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));
		}

		// assert PC Tax Amount with RFL
		if(assertTrueDB("PC Perks Tax value is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoShipTemplateTax(taxDB), RFL_DB)==false){
			// assert PC Tax Amount with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			taxDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
			assertTrue("Tax is NOT as in RFO DB", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));
		}

		//assert PC GrandTotal Amount with RFL
		if(assertTrueDB("PC Perks Tax value is NOT as in RFL DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB), RFL_DB)==false){
			// assert PC Tax Amount with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "Total")));
			assertTrue("Grand Total is NOT as in RFO DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB));
		}

		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_SHIPPING_METHOD_QUERY_RFL, shippingMethodId), RFL_DB);
		String methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
		String methodShortName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "ShortName"));
		if(methodName.equalsIgnoreCase("FedEx Grnd")){
			methodName = "FedEx Ground";
		}
		shippingMethodDB = methodName+" "+"("+methodShortName+")";

		if(assertTrueDB("Shipping Method is NOT as in RFL DB", storeFrontOrdersPage.verifyShippingMethod(shippingMethodDB),RFL_DB)==false){
			// assert Shipping Method with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
			DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
			methodName = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));
			assertTrue("Shipping Method is NOT as in RFO DB", storeFrontOrdersPage.verifyShippingMethod(methodName));
		}

		logout();
		s_assert.assertAll();
	}
}