package com.rf.test.website.storeFront.dataMigration.rfl.order;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFL;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.test.website.RFWebsiteBaseTest;


public class ViewOrderDetailsTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(ViewOrderDetailsTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;	
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontRCUserPage storeFrontRCUserPage; 
	private StoreFrontPCUserPage storeFrontPCUserPage;

	private String RFL_DB = null;
	private String RFO_DB = null;


	//Hybris Phase 2-4286 :: Version : 1 :: Verify order details of CRP autoship order
	@Test
	public void testOrderDetailsOfCRPAutoShipOrder_HP2_4286() throws SQLException, InterruptedException{
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
		String consultantEmailID = null;
		String lastName = null;
		String shippingMethodId = null;

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
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
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

		//get Autoship Id Fro RFO
		List<Map<String, Object>> autoshipIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_AUTOSHIP_ID_FOR_RFO, autoshipNumber),RFO_DB);
		String autoshipID = String.valueOf(getValueFromQueryResult(autoshipIdDetailsList, "AutoshipID"));

		//verify shipping address in RFL
		if(assertTrueDB("CRP autoship shipping address on RFL is "+shippingAddressFromDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate(), storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate().toLowerCase().trim().contains(shippingAddressFromDB.toLowerCase().trim()),RFL_DB)==false){
			shippingAddressList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_FOR_AUTOSHIP_TEMPLATE_RFO, autoshipID), RFO_DB);
			firstName = (String) getValueFromQueryResult(shippingAddressList, "FirstName");
			lastName = (String) getValueFromQueryResult(shippingAddressList, "LastName");
			addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "Address1");
			postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
			locale = (String) getValueFromQueryResult(shippingAddressList, "Locale");
			region = (String) getValueFromQueryResult(shippingAddressList, "Region");
			country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
			if(country.equals("236")){
				country = "United States"; 
			}
			shippingAddressFromDB = firstName+" "+lastName+"\n"+ addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase()+"\n";
			s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate().toLowerCase().contains(shippingAddressFromDB.toLowerCase()),"CRP autoship shipping address on RFO is "+shippingAddressFromDB.toLowerCase().trim()+" \n and on UI is "+storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate().toLowerCase().trim());	
		}

		//verify autoship item details in RFL
		DecimalFormat df = new DecimalFormat("#.00");
		autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_DETAILS_FOR_CONSULTANT_RFL_4289, autoshipNumber),RFL_DB);
		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(autoShipItemDetailsList, "Subtotal")));
		shippingDB = String.valueOf(df.format(getValueFromQueryResult(autoShipItemDetailsList, "ShippingAmount")));
		handlingDB = String.valueOf(df.format(getValueFromQueryResult(autoShipItemDetailsList, "HandlingAmount")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(autoShipItemDetailsList, "TaxAmountTotal")));
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(autoShipItemDetailsList, "Total")));

		// assert Shipping Method with RFL
		shippingMethod = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
		if(assertTrueDB("CRP Autoship shipping method on RFL is "+shippingMethod+" \n and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate(), storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(shippingMethod),RFL_DB)==false){
			// assert shipping method with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_AND_HANDLING_COST_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
			shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
			verifyShippingMethodList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
			shippingMethod = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
			s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(shippingMethod),"CRP Autoship shipping method on RFO is "+shippingMethod+" \n and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());
		}

		// assert Subtotal with RFL
		if(assertTrueDB("CRP Autoship subtotal amount on RFL is "+subTotalDB+" \n and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate(), storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB), RFL_DB) == false){
			// assert subtotal with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_TOTAL_SUBTOTAL_TAX_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
			subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
			s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"CRP Autoship subtotal amount on RFO is "+subTotalDB+" \n and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		}

		// Assert shipping Amount in RFL
		if(assertTrueDB("CRP autoship shipping amount on RFL is "+shippingDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate(),storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),RFL_DB)==false){
			//verify shipping amount in RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_AND_HANDLING_COST_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
			shippingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
			s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"CRP autoship shipping amount on RFO is "+shippingDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
		}

		// Assert handling Amount with RFL
		if(assertTrueDB("CRP Autoship handling amount on RFL is "+handlingDB+" \n and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate(),storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),RFL_DB)==false){
			//verify handling amount in RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_AND_HANDLING_COST_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
			handlingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
			s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"CRP Autoship handling amount on RFO is "+handlingDB+" \n and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
		}

		// assert Tax with RFL
		if(assertTrueDB("CRP Autoship tax amount on RFL is "+taxDB+" \n and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate(), storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB), RFL_DB) == false){
			// assert Tax with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_TOTAL_SUBTOTAL_TAX_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
			taxDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
			s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"CRP Autoship subtotal amount on RFO is "+taxDB+" \n and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate());
		}

		// assert Grand Total with RFL
		if(assertTrueDB("CRP Autoship GrandTotal on RFL is "+grandTotalDB+" \n and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate(), storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB), RFL_DB) == false){
			// assert Grand total with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_TOTAL_SUBTOTAL_TAX_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
			grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "Total")));
			s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"CRP Autoship GrandTotal on RFO is "+grandTotalDB+" \n and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
		}
		
		s_assert.assertAll();
	}

	// Hybris Phase 2 4287 -> Verify order details of consultant order
	@Test
	public void testOrdersDetailsOfConsultant_HP2_4287() throws SQLException, InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();

		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String country = null;
		String shippingAddressFromDB =null;
		String city = null;
		String state = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null; 
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String consultantEmailID = null;     
		String lastName = null;

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);

		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderDateText(),"Order Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");


		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_DETAILS_ACTIVE_CONSULTANT_USER_HAVING_ADHOC_ORDER_RFL, orderHistoryNumber), RFL_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "BillingFirstName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		DecimalFormat df = new DecimalFormat("#.00");

		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "Subtotal")));
		shippingDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "ShippingAmount")));
		handlingDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "HandlingAmount")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "TaxAmount")));
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "GrandTotal")));
		String shippingMethodId = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "ShippingMethodId"));

		verifyShippingMethodList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_SHIPPING_METHOD_QUERY_RFL, shippingMethodId), RFL_DB);
		shippingMethodDB = String.valueOf(getValueFromQueryResult(verifyShippingMethodList, "Name"));

		DecimalFormat dff = new DecimalFormat("#.00");

		// get orderId for RFO
		orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_FOR_ALL_RFO, orderHistoryNumber),RFO_DB);
		String orderId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));

		// assert shipping Address with RFL
		if(assertTrueDB("Adhoc Order template shipping address on RFL is "+shippingAddressFromDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAddressFromAdhocTemplate(), storeFrontOrdersPage.getShippingAddressFromAdhocTemplate().toLowerCase().contains(shippingAddressFromDB.toLowerCase()),RFL_DB)==false){
			//verify shipping address in RFO
			verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
			firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
			lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
			addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
			city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
			state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
			postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
			country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
			if(country.equals("236")){
				country = "United States"; 
			}
			shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
			s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAdhocTemplate().toLowerCase().contains(shippingAddressFromDB.toLowerCase()),"Adhoc Order template shipping address on RFO is "+shippingAddressFromDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAddressFromAdhocTemplate());
		}

		//Assert Subtotal with RFL
		if(assertTrueDB("Adhoc Order template subTotal on RFL is "+subTotalDB+" \n and on UI is "+storeFrontOrdersPage.getSubTotalFromAdhocOrderTemplate(), storeFrontOrdersPage.getSubTotalFromAdhocOrderTemplate().contains(subTotalDB), RFL_DB)==false){
			//Assert Subtotal with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
			s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAdhocOrderTemplate().contains(subTotalDB),"Adhoc Order template subTotal on RFO is "+subTotalDB+" \n and on UI is "+storeFrontOrdersPage.getSubTotalFromAdhocOrderTemplate());
		}

		// assert shipping amount with RFL
		if(assertTrueDB("Adhoc Order template shipping amount on RFL is "+shippingDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAmountFromAdhocOrderTemplate(), storeFrontOrdersPage.getShippingAmountFromAdhocOrderTemplate().contains(shippingDB),RFL_DB)==false){
			// assert shipping amount with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
			s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAdhocOrderTemplate().contains(shippingDB),"Adhoc Order template shipping amount on RFO is "+shippingDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAmountFromAdhocOrderTemplate());
		}

		// assert Handling Value with RFL
		if(assertTrueDB("Adhoc order template handling amount on RFL is "+handlingDB+" \n and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAdhocOrderTemplate(), storeFrontOrdersPage.getHandlingAmountFromAdhocOrderTemplate().contains(handlingDB),RFL_DB)==false){
			// assert Handling Value with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			handlingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
			s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAdhocOrderTemplate().contains(handlingDB),"Adhoc order template handling amount on RFO is "+handlingDB+" \n and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAdhocOrderTemplate());
		}
		// Assert Tax with RFL
		if(assertTrueDB("Adhoc order template tax amount on RFL is "+taxDB+" \n and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate(), storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate().contains(taxDB),RFL_DB)==false){
			// assert Tax with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			taxDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
			s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate().contains(taxDB),"Adhoc order template tax amount on RFO is "+taxDB+" \n and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());
		}

		// Assert Grand Total with RFL
		if(assertTrueDB("Adhoc Order template grand total on RFL is "+grandTotalDB+" \n and on UI is "+storeFrontOrdersPage.getGrandTotalFromAdhocOrderTemplate(), storeFrontOrdersPage.getGrandTotalFromAdhocOrderTemplate().contains(grandTotalDB),RFL_DB)==false){
			// assert Grand Total with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "Total")));
			s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAdhocOrderTemplate().contains(grandTotalDB),"Adhoc Order template grand total on RFO is "+grandTotalDB+" \n and on UI is "+storeFrontOrdersPage.getGrandTotalFromAdhocOrderTemplate());
		}

		// assert for shipping Method with RFL
		String shippingMethod = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
		if(assertTrueDB("Adhoc Order template shipping method on RFL is "+shippingAddressFromDB+" \n and on UI is "+storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate(), storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate().contains(shippingMethodDB),RFL_DB)==false){
			// assert Shipping Method with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
			shippingMethod = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
			s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate().contains(shippingMethod),"Adhoc Order template shipping method on RFO is "+shippingMethod+" \n and on UI is "+storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate());
		}

		
		s_assert.assertAll();
	}

	// Hybris Phase 2-4291 :: Version : 1 :: Verify PC autoship order. 
	@Test
	public void testOrderDetailsForAutoshipOrdersForPC_4291() throws InterruptedException{
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
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		String autoshipNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfScheduleDateText(),"Schedule Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_DETAILS_ACTIVE_PC_USER_HAVING_AUTOSHIP_ORDER_RFL, autoshipNumber), RFL_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");

		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		DecimalFormat df = new DecimalFormat("#.00");

		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "Subtotal")));
		shippingDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "ShippingAmount")));
		handlingDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "HandlingAmount")));
		taxDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "TaxAmount")));
		grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(verifyAllDetailsList, "GrandTotal")));
		String shippingMethodId = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "ShippingMethodId"));

		//get Autoship Id From RFO
		List<Map<String, Object>> autoshipIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_AUTOSHIP_ID_FOR_RFO, autoshipNumber),RFO_DB);
		String autoshipID = String.valueOf(getValueFromQueryResult(autoshipIdDetailsList, "AutoshipID"));

		// assert shipping Address with RFL
		if(assertTrueDB("PC Perks Autoship template shipping address on RFL is "+shippingAddressFromDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate(), storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate().toLowerCase().contains(shippingAddressFromDB.toLowerCase()), RFL_DB)==false){
			// Assert Shipping Address with RFo
			List<Map<String, Object>> shippingAddressList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_FOR_AUTOSHIP_TEMPLATE_RFO, autoshipID), RFO_DB);
			firstName = (String) getValueFromQueryResult(shippingAddressList, "FirstName");
			lastName = (String) getValueFromQueryResult(shippingAddressList, "LastName");
			addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "Address1");
			postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
			locale = (String) getValueFromQueryResult(shippingAddressList, "Locale");
			region = (String) getValueFromQueryResult(shippingAddressList, "Region");
			country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
			if(country.equals("236")){
				country = "United States"; 
			}
			shippingAddressFromDB = firstName+" "+lastName+"\n"+ addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase()+"\n";
			s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate().toLowerCase().contains(shippingAddressFromDB.toLowerCase()), "PC Perks Autoship template shipping address on RFO is "+shippingAddressFromDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate());
		}

		// Assert Shipping Method with RFL
		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
		if(assertTrueDB("PC Autoship template shipping method on RFL is "+shippingMethodDB+" and on UI "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate(), storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(shippingMethodDB),RFL_DB)==false){
			// assert Shipping Method with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_AND_HANDLING_COST_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
			shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
			shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
			s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(shippingMethodDB),"PC Autoship template shipping method on RFO is "+shippingMethodDB+" and on UI "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());
		}

		// Assert for PC Perks SubTotal with RFL
		if(assertTrueDB("PC Perks Autoship template subTotal amount on RFL is "+subTotalDB+" \n and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate(), storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB), RFL_DB) == false){
			// assert for PC Perks SubTotal with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_TOTAL_SUBTOTAL_TAX_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
			subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
			s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"PC Autoship template subTotal amount on RFO is "+subTotalDB+" \n and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		}


		// assert PC Perks Shipping value with RFL
		if(assertTrueDB("PC Autoship template shipping amount on RFL is "+shippingDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate(), storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB), RFL_DB)== false){
			// assert PC Perks Shipping value with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_AND_HANDLING_COST_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
			shippingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
			s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"PC Autoship template shipping amount on RFO is "+shippingDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
		}

		// assert PC Handling value with RFL
		if(assertTrueDB("PC Autoship template handling amount on RFL is "+handlingDB+" \n and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate(), storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB), RFL_DB) == false){
			// assert PC Perks Handling value with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_AND_HANDLING_COST_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
			handlingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
			s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"PC Autoship template handling amount on RFO is "+handlingDB+" \n and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
		}

		// assert PC Tax Amount with RFL
		if(assertTrueDB("PC Autoship template tax amount on RFL is "+taxDB+" \n and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate(), storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB), RFL_DB)==false){
			// assert PC Tax Amount with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_TOTAL_SUBTOTAL_TAX_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
			taxDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
			s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"PC Autoship template tax amount on RFO is "+taxDB+" \n and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate());
		}

		//assert PC GrandTotal Amount with RFL
		if(assertTrueDB("PC Autoship template grand total amount on RFL is "+grandTotalDB, storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB), RFL_DB)==false){
			// assert PC Grand Total Amount with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_TOTAL_SUBTOTAL_TAX_FOR_AUTOSHIP_TEMPLATE_RFO,autoshipID),RFO_DB);
			grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "Total")));
			s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"PC Autoship template grand total amount on RFO is "+grandTotalDB+" \n and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
		}

		
		s_assert.assertAll();
	}

	// Hybris Phase 2-4292 :: Version : 1 :: Verify order details of PC Order (i.e. Adhoc Order)
	@Test
	public void testOrderDetailsForAdhocOrdersForPC_4292() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();

		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String country = null;
		String shippingAddressFromDB =null;
		String city = null;
		String state = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null;	
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String pcEmailID = null;
		String lastName = null;

		List<Map<String, Object>> randomPCList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String, Object>> verifyOrderDetailsList = null;
		List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;

		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCList, "Username");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
		s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"PC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");


		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);

		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderDateText(),"Order Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ADDRESS_DETAILS_FOR_RFL, orderHistoryNumber), RFL_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		DecimalFormat df = new DecimalFormat("#.00");

		verifyOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_DETAILS_FOR_RFL, orderHistoryNumber), RFL_DB);
		subTotalDB = String.valueOf(df.format((getValueFromQueryResult(verifyOrderDetailsList, "Subtotal"))));
		shippingDB =  String.valueOf(df.format((getValueFromQueryResult(verifyOrderDetailsList, "ShippingAmount"))));
		handlingDB =  String.valueOf(df.format(getValueFromQueryResult(verifyOrderDetailsList, "HandlingAmount")));
		taxDB =  String.valueOf(df.format(getValueFromQueryResult(verifyOrderDetailsList, "TaxAmount")));
		grandTotalDB =  String.valueOf(df.format(getValueFromQueryResult(verifyOrderDetailsList, "Total")));
		String shippingMethodId = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "ShippingMethodId"));

		// get orderId for RFO
		orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_FOR_ALL_RFO, orderHistoryNumber),RFO_DB);
		String orderId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));


		// assert shipping Address with RFL
		if(assertTrueDB("PC Adhoc Order template shipping address on RFL is "+shippingAddressFromDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAddressFromAdhocTemplate(), storeFrontOrdersPage.getShippingAddressFromAdhocTemplate().toLowerCase().contains(shippingAddressFromDB.toLowerCase()),RFL_DB)==false){
			//verify shipping address in RFO
			verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
			firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
			lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
			addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
			city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
			state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
			postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
			country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
			if(country.equals("236")){
				country = "United States"; 
			}
			shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
			s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAdhocTemplate().toLowerCase().contains(shippingAddressFromDB), "PC Adhoc Order template shipping address on RFO is "+shippingAddressFromDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAddressFromAdhocTemplate());
		}
		//Assert Subtotal with RFL
		if(assertTrueDB("PC Adhoc Order template subTotal on RFL is "+subTotalDB+" \n and on UI is "+storeFrontOrdersPage.getSubTotalFromAdhocOrderTemplate(), storeFrontOrdersPage.getSubTotalFromAdhocOrderTemplate().contains(subTotalDB), RFL_DB)==false){
			//Assert Subtotal with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			subTotalDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
			s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAdhocOrderTemplate().contains(subTotalDB),"PC Adhoc Order template subTotal on RFO is "+subTotalDB+" \n and on UI is "+storeFrontOrdersPage.getSubTotalFromAdhocOrderTemplate());
		}

		// assert shipping amount with RFL
		if(assertTrueDB("PC Adhoc Order template shipping amount on RFL is "+shippingDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAmountFromAdhocOrderTemplate(), storeFrontOrdersPage.getShippingAmountFromAdhocOrderTemplate().contains(shippingDB),RFL_DB)==false){
			// assert shipping amount with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
			s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAdhocOrderTemplate().contains(shippingDB),"PC Adhoc Order template shipping amount on RFO is "+shippingDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAmountFromAdhocOrderTemplate());
		}

		// assert Handling Value with RFL
		if(assertTrueDB("PC Adhoc Order template handling amount on RFL is "+handlingDB+" \n and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAdhocOrderTemplate(), storeFrontOrdersPage.getHandlingAmountFromAdhocOrderTemplate().contains(handlingDB),RFL_DB)==false){
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			handlingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
			s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAdhocOrderTemplate().contains(handlingDB),"PC Adhoc Order template handling amount on RFO is "+handlingDB+" \n and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAdhocOrderTemplate());
		}

		// Assert Tax with RFL
		if(assertTrueDB("PC Adhoc Order template tax amount on RFL is "+taxDB+" \n and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate(), storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate().contains(taxDB),RFL_DB)==false){
			// assert Tax with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			taxDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
			s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate().contains(taxDB),"PC Adhoc Order template tax amount on RFO is "+taxDB+" \n and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());
		}

		// Assert Grand Total with RFL
		if(assertTrueDB("PC Adhoc Order template grand total amount on RFL is "+grandTotalDB+" \n and on UI is "+storeFrontOrdersPage.getGrandTotalFromAdhocOrderTemplate(), storeFrontOrdersPage.getGrandTotalFromAdhocOrderTemplate().contains(grandTotalDB),RFL_DB)==false){
			// assert Grand Total with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			grandTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total")));
			s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAdhocOrderTemplate().contains(grandTotalDB),"PC Adhoc Order template grand total amount on RFO is "+grandTotalDB+" \n and on UI is "+storeFrontOrdersPage.getGrandTotalFromAdhocOrderTemplate());
		}

		// assert for shipping Method with RFL
		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
		if(assertTrueDB("PC Adhoc Order template shipping method on RFL is "+shippingMethodDB+" \n and on UI is "+storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate(), storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate().contains(shippingMethodDB),RFL_DB)==false){
			// assert shipping method with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
			shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
			s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate().contains(shippingMethodDB),"PC Adhoc Order template shipping method on RFO is "+shippingMethodDB+" \n and on UI is "+storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate());
		}
		
		s_assert.assertAll();
	}

	//Hybris Phase 2-4293 :: Version : 1 :: Verify details of retail order. 
	@Test
	public void testOrderDetailsForAdhocOrdersForRC_4293() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();

		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String country = null;
		String shippingAddressFromDB =null;
		String city = null;
		String state = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null;	
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String lastName = null;
		String rcUser = null;

		List<Map<String, Object>> randomRCList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String, Object>> verifyOrderDetailsList = null;
		List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;

		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS,RFL_DB);
		rcUser = (String) getValueFromQueryResult(randomRCList, "UserName");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUser, password);
		logger.info("login is successful");
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ADDRESS_DETAILS_FOR_RFL, orderHistoryNumber), RFL_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		DecimalFormat df = new DecimalFormat("#.00");

		verifyOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_DETAILS_FOR_RFL, orderHistoryNumber), RFL_DB);
		subTotalDB = String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "Subtotal"))));
		shippingDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "ShippingAmount"))));
		handlingDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "HandlingAmount"))));
		taxDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "TaxAmount"))));
		grandTotalDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "Total"))));
		String shippingMethodId = String.valueOf((Number)getValueFromQueryResult(verifyAllDetailsList, "ShippingMethodId"));

		// get orderId for RFO
		orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_FOR_ALL_RFO, orderHistoryNumber),RFO_DB);
		String orderId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));

		// assert shipping Address with RFL
		if(assertTrueDB("RC Adhoc Order Template shipping address on RFL is "+shippingAddressFromDB+" \nand on UI is "+storeFrontOrdersPage.getShippingAddressFromAdhocTemplate(), storeFrontOrdersPage.getShippingAddressFromAdhocTemplate().toLowerCase().contains(shippingAddressFromDB.toLowerCase()),RFL_DB)==false){
			//verify shipping address in RFO
			verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
			firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
			lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
			addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
			city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
			state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
			postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
			country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
			if(country.equals("236")){
				country = "United States"; 
			}
			shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
			s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAdhocTemplate().toLowerCase().contains(shippingAddressFromDB.toLowerCase()), "RC Adhoc Order Template shipping address on RFO is "+shippingAddressFromDB+" \nand on UI is "+storeFrontOrdersPage.getShippingAddressFromAdhocTemplate());
		}

		//Assert Subtotal with RFL
		if(assertTrueDB("RC Adhoc Order Template subTotal on RFL is "+subTotalDB+" \n and on UI is "+storeFrontOrdersPage.getSubTotalFromAdhocOrderTemplate(), storeFrontOrdersPage.getSubTotalFromAdhocOrderTemplate().contains(subTotalDB), RFL_DB)==false){
			//Assert Subtotal with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			subTotalDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
			s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAdhocOrderTemplate().contains(subTotalDB),"RC Adhoc Order Template subTotal on RFO is "+subTotalDB+" \n and on UI is "+storeFrontOrdersPage.getSubTotalFromAdhocOrderTemplate());
		}

		// assert shipping amount with RFL
		if(assertTrueDB("RC Adhoc Order Template shipping amount on RFL is "+shippingDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAmountFromAdhocOrderTemplate(), storeFrontOrdersPage.getShippingAmountFromAdhocOrderTemplate().contains(shippingDB),RFL_DB)==false){
			// assert shipping amount with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
			s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAdhocOrderTemplate().contains(shippingDB),"RC Adhoc Order Template shipping amount on RFO is "+shippingDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAmountFromAdhocOrderTemplate());
		}

		// assert Handling Value with RFL
		if(assertTrueDB("RC Adhoc Order Template handling amount on RFL is "+handlingDB+" \n and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAdhocOrderTemplate(), storeFrontOrdersPage.getHandlingAmountFromAdhocOrderTemplate().contains(handlingDB),RFL_DB)==false){
			// assert Handling Value with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			handlingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
			s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAdhocOrderTemplate().contains(handlingDB),"RC Adhoc Order Template handling amount on RFO is "+handlingDB+" \n and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAdhocOrderTemplate());
		}

		// Assert Tax with RFL
		if(assertTrueDB("RC Adhoc Order Template tax amount on RFL is "+taxDB+" \n and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate(), storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),RFL_DB)==false){
			// assert Tax with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			taxDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
			s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"RC Adhoc Order Template tax amount on RFO is "+taxDB+" \n and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate());
		}

		// Assert Grand Total with RFL
		if(assertTrueDB("RC Adhoc Order Template grand total on RFL is "+grandTotalDB+" and on UI is"+storeFrontOrdersPage.getGrandTotalFromAdhocOrderTemplate(), storeFrontOrdersPage.getGrandTotalFromAdhocOrderTemplate().contains(grandTotalDB),RFL_DB)==false){
			// assert Grand Total with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			grandTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total")));
			s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAdhocOrderTemplate().contains(grandTotalDB),"RC Adhoc Order Template grand total on RFO is "+grandTotalDB+" and on UI is"+storeFrontOrdersPage.getGrandTotalFromAdhocOrderTemplate());
		}

		// assert for shipping Method with RFL
		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
		if(assertTrueDB("RC Adhoc Order Template grand total on RFL is "+shippingMethodDB+" \n and on UI is "+storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate(), storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate().contains(shippingMethodDB),RFL_DB)==false){
			// assert shipping method with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
			shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
			s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate().contains(shippingMethodDB),"RC Adhoc Order Template grand total on RFO is "+shippingMethodDB+" \n and on UI is "+storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate());
		}

		
		s_assert.assertAll();
	}

	//Hybris Project-4294:Verify details of failed consultant autoship orders.
	@Test
	public void testDetailsOfFailedConsultantAutoshipOrder_4294() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();

		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String country = null;
		String shippingAddressFromDB =null;
		String city = null;
		String state = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null;	
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String lastName = null;
		String consultantEmailID =  null;

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String, Object>> verifyOrderDetailsList = null;
		List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_ACTIVE_CONSULTANT_HAVING_FAILED_CRP_ORDER_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String failedOrderNumber = storeFrontOrdersPage.getOrderNumberFromOrderHistoryForFailedAutoshipOrdersForConsultant();

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ADDRESS_DETAILS_FOR_RFL, failedOrderNumber), RFL_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		DecimalFormat df = new DecimalFormat("#.00");

		verifyOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_DETAILS_FOR_RFL, failedOrderNumber), RFL_DB);
		subTotalDB = String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "Subtotal"))));
		shippingDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "ShippingAmount"))));
		handlingDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "HandlingAmount"))));
		taxDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "TaxAmount"))));
		grandTotalDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "Total"))));
		String shippingMethodId = String.valueOf((Number)getValueFromQueryResult(verifyAllDetailsList, "ShippingMethodId"));

		// get orderId for RFO
		orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_FOR_ALL_RFO, failedOrderNumber),RFO_DB);
		String orderId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));

		// assert shipping Address with RFL
		if(assertTrueDB("CRP failed autoship template shipping address on RFL is ", storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate().toLowerCase().contains(shippingAddressFromDB.toLowerCase()),RFL_DB)==false){
			//verify shipping address in RFO
			verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
			firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
			lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
			addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
			city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
			state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
			postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
			country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
			if(country.equals("236")){
				country = "United States"; 
			}
			shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
			s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate().toLowerCase().contains(shippingAddressFromDB), "CRP failed autoship template shipping address on RFO is "+shippingAddressFromDB+" \nand on UI is "+storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate());
		}

		//Assert Subtotal with RFL
		if(assertTrueDB("CRP failed autoship template subTotal amount on RFL is "+subTotalDB+" \n and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate(), storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB), RFL_DB)==false){
			//Assert Subtotal with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			subTotalDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
			s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"CRP failed autoship template subTotal amount on RFO is "+subTotalDB+" \n and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		}

		// assert shipping amount with RFL
		if(assertTrueDB("CRP failed autoship template shipping amount on RFL is "+shippingDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate(), storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),RFL_DB)==false){
			// assert shipping amount with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
			s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"CRP failed autoship template shipping amount on RFO is "+shippingDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
		}

		// assert Handling Value with RFL
		if(assertTrueDB("CRP failed autoship template handling amount on RFL is "+handlingDB+" \n and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate(), storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),RFL_DB)==false){
			// assert Handling Value with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			handlingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
			s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"CRP failed autoship template handling amount on RFO is "+handlingDB+" \n and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
		}

		// Assert Tax with RFL
		if(assertTrueDB("CRP failed autoship template tax amount on RFL is "+taxDB+" \n and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate(), storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),RFL_DB)==false){
			// assert Tax with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			taxDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
			s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"CRP failed autoship template tax amount on RFO is "+taxDB+" \n and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate());
		}

		// Assert Grand Total with RFL
		if(assertTrueDB("CRP failed autoship template grand total on RFL is "+grandTotalDB+" \n and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate(), storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),RFL_DB)==false){
			// assert Grand Total with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			grandTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total")));
			s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"CRP failed autoship template grand total on RFO is "+grandTotalDB+" \n and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
		}

		// assert for shipping Method with RFL
		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
		if(assertTrueDB("CRP failed autoship template shipping method on RFL is "+shippingMethodDB+" \n and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate(), storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(shippingMethodDB),RFL_DB)==false){
			// assert Shipping Method with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
			shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
			s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(shippingMethodDB),"CRP failed autoship template shipping method on RFO is "+shippingMethodDB+" \n and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());
		}

		
		s_assert.assertAll();

	}

	//Hybris Project-4295:Verify details of failed pc autoship orders.
	@Test
	public void testDetilsOfFailedAutoshipOrdersForPC_4295() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();

		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String country = null;
		String shippingAddressFromDB =null;
		String city = null;
		String state = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null;	
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String lastName = null;
		String accountIdForPCFailedOrder = null;

		List<Map<String, Object>> randomPCList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String, Object>> verifyOrderDetailsList = null;
		List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		String PCEmailID = null;

		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_PC_USER_FOR_FAILED_AUTOSHIP_ORDER, accountIdForPCFailedOrder),RFL_DB);
		PCEmailID = String.valueOf(getValueFromQueryResult(randomPCList, "Username"));

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(PCEmailID, password);
		s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"PC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String failedOrderNumber = storeFrontOrdersPage.getOrderNumberFromOrderHistoryForFailedAutoshipOrdersForPC();				

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ADDRESS_DETAILS_FOR_RFL, failedOrderNumber), RFL_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		DecimalFormat df = new DecimalFormat("#.00");

		verifyOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_DETAILS_FOR_RFL, failedOrderNumber), RFL_DB);
		subTotalDB = String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "Subtotal"))));
		shippingDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "ShippingAmount"))));
		handlingDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "HandlingAmount"))));
		taxDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "TaxAmount"))));
		grandTotalDB =  String.valueOf(df.format((Number)(getValueFromQueryResult(verifyOrderDetailsList, "Total"))));
		String shippingMethodId = String.valueOf((Number)getValueFromQueryResult(verifyAllDetailsList, "ShippingMethodId"));

		// get orderId for RFO
		orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_FOR_ALL_RFO, failedOrderNumber),RFO_DB);
		String orderId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));	

		// assert shipping Address with RFL
		if(assertTrueDB("PC failed autoship shipping address on RFL is "+shippingAddressFromDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate(), storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate().toLowerCase().contains(shippingAddressFromDB.toLowerCase()),RFL_DB)==false){
			//verify shipping address in RFO
			verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
			firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
			lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
			addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
			city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
			state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
			postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
			country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
			if(country.equals("236")){
				country = "United States"; 
			}
			shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
			s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate().toLowerCase().contains(shippingAddressFromDB.toLowerCase()), "PC failed autoship shipping address on RFO is "+shippingAddressFromDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAddressFromAutoshipTemplate());
		}

		//Assert Subtotal with RFL
		if(assertTrueDB("PC failed autoship subtotal amount on RFL is "+subTotalDB+" \n and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate(), storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB), RFL_DB)==false){
			//Assert Subtotal with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			subTotalDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
			s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"PC failed autoship subtotal amount on RFO is "+subTotalDB+" \n and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		}

		// assert shipping amount with RFL
		if(assertTrueDB("PC failed autoship shipping amount on RFL is "+shippingDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate(), storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),RFL_DB)==false){
			// assert shipping amount with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
			s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(shippingDB),"PC failed autoship shipping amount on RFO is "+shippingDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
		}

		// assert Handling Value with RFL
		if(assertTrueDB("PC failed autoship handling amount on RFL is "+handlingDB+" \n and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate(), storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),RFL_DB)==false){
			// assert Handling Value with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			handlingDB = String.valueOf(df.format((Number)getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
			s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingDB),"PC failed autoship handling amount on RFO is "+handlingDB+" \n and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
		}

		// Assert Tax with RFL
		if(assertTrueDB("PC failed autoship tax amount on RFL is "+taxDB+" \n and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate(), storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),RFL_DB)==false){
			// assert Tax with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			taxDB = String.valueOf(df.format((Number)getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
			s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(taxDB),"PC failed autoship tax amount on RFO is "+taxDB+" \n and on UI is "+storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate());
		}

		// Assert Grand Total with RFL
		if(assertTrueDB("PC failed autoship grand total on RFL is "+grandTotalDB+" \n and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate(), storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),RFL_DB)==false){
			// assert Grand Total with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			grandTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(getOtherDetailValuesList, "Total")));
			s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(grandTotalDB),"PC failed autoship grand total on RFO is "+grandTotalDB+" \n and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
		}

		// assert for shipping Method with RFL
		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
		if(assertTrueDB("PC failed autoship shipping method on RFL is "+shippingMethodDB+" \n and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate(), storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(shippingMethodDB),RFL_DB)==false){
			// assert Shipping Method with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
			shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
			s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(shippingMethodDB),"PC failed autoship shipping method on RFO is "+shippingMethodDB+" \n and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());
		}

		
		s_assert.assertAll();

	}

	//Hybris Project-4296::Verify details of failed retail orders.
	@Test(enabled=false) // QUERY giving anonymous result with no username
	public void testDetailsOfFailedAdhocOrdersForRC_4296() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();

		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String country = null;
		String shippingAddressFromDB =null;
		String city = null;
		String state = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null;	
		String grandTotalDB = null;
		String shippingMethodDB = null;
		String RCEmailID = null;
		String lastName = null;
		String accountIdForPCFailedOrder = null;

		List<Map<String, Object>> randomRCList =  null;
		List<Map<String, Object>> verifyAllDetailsList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;
		List<Map<String, Object>> verifyOrderDetailsList = null;
		List<Map<String,Object>> orderIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;

		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ACTIVE_RC_USER_HAVING_FAILED_ORDERS, accountIdForPCFailedOrder),RFL_DB);
		RCEmailID = String.valueOf(getValueFromQueryResult(randomRCList, "Username"));

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(RCEmailID, password);
		logger.info("login is successful");
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String failedOrderNumber = storeFrontOrdersPage.getOrderNumberFromOrderHistoryForFailedAdhocOrdersForRC();				

		verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ADDRESS_DETAILS_FOR_RFL, failedOrderNumber), RFL_DB);
		firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
		city = (String) getValueFromQueryResult(verifyAllDetailsList, "City");
		state = (String) getValueFromQueryResult(verifyAllDetailsList, "State");
		postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostCode");
		country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		DecimalFormat df = new DecimalFormat("#.00");

		verifyOrderDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_DETAILS_FOR_FAILED_ORDER_FOR_RC_RFL_, failedOrderNumber), RFL_DB);
		subTotalDB = String.valueOf(df.format(getValueFromQueryResult(verifyOrderDetailsList, "Subtotal")));
		shippingDB =  String.valueOf(df.format(getValueFromQueryResult(verifyOrderDetailsList, "ShippingTotal")));
		handlingDB =  String.valueOf(df.format(getValueFromQueryResult(verifyOrderDetailsList, "HandlingTotal")));
		taxDB =  String.valueOf(df.format(getValueFromQueryResult(verifyOrderDetailsList, "TaxAmount")));
		grandTotalDB =  String.valueOf(df.format(getValueFromQueryResult(verifyOrderDetailsList, "GrandTotal")));
		String shippingMethodId = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "ShippingMethodId"));

		// get orderId for RFO
		orderIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_FOR_ALL_RFO, failedOrderNumber),RFO_DB);
		String orderId = String.valueOf(getValueFromQueryResult(orderIdAccountIdDetailsList, "OrderID"));

		//		// assert shipping Address with RFL
		if(assertTrueDB("Retail failed adhoc order shipping address on RFL is "+shippingAddressFromDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAddressFromAdhocTemplate(), storeFrontOrdersPage.getShippingAddressFromAdhocTemplate().toLowerCase().contains(shippingAddressFromDB.toLowerCase()),RFL_DB)==false){
			//verify shipping address in RFO
			verifyAllDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_QUERY_FOR_ALL_RFO, orderId), RFO_DB);
			firstName = (String) getValueFromQueryResult(verifyAllDetailsList, "FirstName");
			lastName = (String) getValueFromQueryResult(verifyAllDetailsList, "LastName");
			addressLine1 = (String) getValueFromQueryResult(verifyAllDetailsList, "Address1");
			city = (String) getValueFromQueryResult(verifyAllDetailsList, "Locale");
			state = (String) getValueFromQueryResult(verifyAllDetailsList, "Region");
			postalCode = (String) getValueFromQueryResult(verifyAllDetailsList, "PostalCode");
			country = String.valueOf(getValueFromQueryResult(verifyAllDetailsList, "CountryID"));
			if(country.equals("236")){
				country = "United States"; 
			}
			shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
			s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAdhocTemplate().toLowerCase().contains(shippingAddressFromDB.toLowerCase()), "Retail failed adhoc order shipping address on RFO is "+shippingAddressFromDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAddressFromAdhocTemplate());
		}

		//Assert Subtotal with RFL
		if(assertTrueDB("Retail failed adhoc order subtotal on RFL is "+subTotalDB+" \n and on UI is "+storeFrontOrdersPage.getSubTotalFromAdhocOrderTemplate(), storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB), RFL_DB)==false){
			//Assert Subtotal with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			subTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "SubTotal")));
			s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subTotalDB),"Retail failed adhoc order subtotal on RFO is "+subTotalDB+" \n and on UI is "+storeFrontOrdersPage.getSubTotalFromAdhocOrderTemplate());
		}

		// assert shipping amount with RFL
		if(assertTrueDB("Retail failed adhoc order shipping amount on RFL is "+shippingDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAmountFromAdhocOrderTemplate(), storeFrontOrdersPage.getShippingAmountFromAdhocOrderTemplate().contains(shippingDB),RFL_DB)==false){
			// assert shipping amount with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingCost")));
			s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAdhocOrderTemplate().contains(shippingDB),"Retail failed adhoc order shipping amount on RFO is "+shippingDB+" \n and on UI is "+storeFrontOrdersPage.getShippingAmountFromAdhocOrderTemplate());
		}

		// assert Handling Value with RFL
		if(assertTrueDB("Retail failed adhoc order handling amount on RFL is "+handlingDB+" \n and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAdhocOrderTemplate(), storeFrontOrdersPage.getHandlingAmountFromAdhocOrderTemplate().contains(handlingDB),RFL_DB)==false){
			// assert Handling Value with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			handlingDB = String.valueOf(df.format(getValueFromQueryResult(shippingCostAndHandlingCostList, "HandlingCost")));
			s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAdhocOrderTemplate().contains(handlingDB),"Retail failed adhoc order handling amount on RFO is "+handlingDB+" \n and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAdhocOrderTemplate());
		}

		// Assert Tax with RFL
		if(assertTrueDB("Retail failed adhoc order tax amount on RFL is "+taxDB+" \n and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate(), storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate().contains(taxDB),RFL_DB)==false){
			// assert Tax with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			taxDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "TotalTax")));
			s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate().contains(taxDB),"Retail failed adhoc order tax amount on RFO is "+taxDB+" \n and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());
		}

		// Assert Grand Total with RFL
		if(assertTrueDB("Retail failed adhoc order grand total amount on RFL is "+grandTotalDB+" \n and on UI is "+storeFrontOrdersPage.getGrandTotalFromAdhocOrderTemplate(), storeFrontOrdersPage.getGrandTotalFromAdhocOrderTemplate().contains(grandTotalDB),RFL_DB)==false){
			// assert Grand Total with RFO
			getOtherDetailValuesList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_DETAILS_FOR_RFO,orderId),RFO_DB);
			grandTotalDB = String.valueOf(df.format(getValueFromQueryResult(getOtherDetailValuesList, "Total")));
			s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAdhocOrderTemplate().contains(grandTotalDB),"Retail failed adhoc order grand total amount on RFO is "+grandTotalDB+" \n and on UI is "+storeFrontOrdersPage.getGrandTotalFromAdhocOrderTemplate());
		}

		// assert for shipping Method with RFL
		shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
		if(assertTrueDB("Retail failed adhoc order shipping method on RFL is "+shippingMethodDB+" \n and on UI is "+storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate().contains(shippingMethodDB), storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate().contains(shippingMethodDB),RFL_DB)==false){
			// assert Shipping Method with RFO
			shippingCostAndHandlingCostList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_COST_HANDLING_COST_FOR_RFO,orderId),RFO_DB);
			shippingMethodId =  String.valueOf(getValueFromQueryResult(shippingCostAndHandlingCostList, "ShippingMethodID"));
			shippingMethodDB = storeFrontOrdersPage.convertShippingMethodNameAsOnUI(shippingMethodId);
			s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate().contains(shippingMethodDB),"Retail failed adhoc order shipping method on RFO is "+shippingMethodDB+" \n and on UI is "+storeFrontOrdersPage.getShippingMethodFromAdhocOrderTemplate().contains(shippingMethodDB));
		}
		
		s_assert.assertAll();

	}
}
