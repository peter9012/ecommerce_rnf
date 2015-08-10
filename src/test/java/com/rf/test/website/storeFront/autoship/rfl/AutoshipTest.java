package com.rf.test.website.storeFront.autoship.rfl;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.DBQueries;
import com.rf.core.website.constants.TestConstants;
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
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		//		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_PC_USER_EMAIL_ID_RFL,RFL_DB);
		//		pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "EmailAddress");
		pcUserEmailID = "wigginsk@comcast.net";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, TestConstants.PC_USER_PASSWORD_RFL);
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnShopLink();
		storeFrontPCUserPage.clickOnAllProductsLink();
		storeFrontCartAutoShipPage = storeFrontPCUserPage.addProductToPCPerk();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditShipping();
		String selectedShippingMethod = storeFrontUpdateCartPage.selectAndGetShippingMethodName();
		logger.info("Shipping Method selected is: "+selectedShippingMethod);
		storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtn();
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();

		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		assertTrue("shipping method is not as expected", storeFrontOrdersPage.verifyShippingMethod(selectedShippingMethod));
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

		//------------------------------- Random Users part is commented for now-----------------------------------------------	
		/*		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
				consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");*/
		//---------------------------------------------------------------------------------------------------------------------		

		consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_TST4; // A hard code user

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
		String phoneNumber = null;
		String subTotalDB = null;
		String shippingDB = null;
		String handlingDB = null;
		String taxDB = null; 
		String grandTotalDB = null;
		String shippingMethod = null;
		String payeeNameDB = null;
		String cardTypeDB = null;
		String consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_TST4; // A hard code user
		String lastName = null;
		String shippingMethodId = null;
		String shippingMethodDB = null;

		List<Map<String, Object>> expirationDateOfCardList = null;
		List<Map<String, Object>> autoShipItemDetailsList = null;
		List<Map<String, Object>> autoShipItemTotalPriceList = null;
		List<Map<String, Object>> shippingAddressList = null;
		List<Map<String, Object>> shippingMethodList = null;
		List<Map<String, Object>> autoShipPaymentDetailsList = null;
		List<Map<String, Object>> randomConsultantList =  null;
		/*randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_HAVING_SUBMITTED_ORDERS_RFL,RFL_DB);
	  consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");*/


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
		shippingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_ADDRESS_DETAILS_FOR_CONSULTANT_RFL_4289, autoshipNumber),RFL_DB);
		firstName = (String) getValueFromQueryResult(shippingAddressList, "FirstName");
		lastName = (String) getValueFromQueryResult(shippingAddressList, "LastName");
		addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "Address1");
		city = (String) getValueFromQueryResult(shippingAddressList, "City");
		state = (String) getValueFromQueryResult(shippingAddressList, "State");
		postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostCode");
		phoneNumber = (String) getValueFromQueryResult(shippingAddressList, "PhoneNumber");
		country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}
		shippingMethodId = String.valueOf(getValueFromQueryResult(shippingAddressList, "ShippingMethodID"));
		shippingAddressFromDB = firstName+" "+lastName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		System.out.println("Created ***"+shippingAddressFromDB);
		if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB),RFL_DB)==false){
			//verify shipping address in RFO
			/*shippingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_ADDRESS_QUERY_RFO,consultantEmailID),RFO_DB);
	   firstName = (String) getValueFromQueryResult(shippingAddressList, "AddressProfileName");
	   addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "AddressLine1");
	   postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
	   locale = (String) getValueFromQueryResult(shippingAddressList, "Locale");
	   region = (String) getValueFromQueryResult(shippingAddressList, "Region");
	   country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
	   if(country.equals("236")){
	    country = "United States"; 
	   }
	   shippingAddressFromDB = firstName+" "+lastName+"\n"+ addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase()+"\n";
	   //shippingAddressFromDB = addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase()+"\n";
	   assertTrue("Shipping Address is not as expected",  storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB));
			 */
			System.out.println("Shipping address in RFL");
		}
		//verify autoship item details in RFL
		DecimalFormat df = new DecimalFormat("#.00");
		autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_DETAILS_FOR_CONSULTANT_RFL_4289, autoshipNumber),RFL_DB);
		subTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "Subtotal")));
		shippingDB = String.valueOf(df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "ShippingAmount")));
		handlingDB = String.valueOf(df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "HandlingAmount")));
		taxDB = String.valueOf(df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "TaxAmountTotal")));
		grandTotalDB = String.valueOf(df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "PaymentTotal")));
		DecimalFormat dff = new DecimalFormat("#.00");

		// Assert shipping Amount in RFL
		if(assertTrueDB("CRP Autoship Shipping value is not as in DB",storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB),RFL_DB)==false){
			//verify shipping amount in RFO
			autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_COST_QUERY_RFO,consultantEmailID),RFO_DB);
			shippingDB = String.valueOf(dff.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "ShippingCost")));
			assertTrue("CRP Autoship Shipping value is not as in DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));
		}

		// Assert handling Amount with RFL
		if(assertTrueDB("CRP Autoship Handling value is not as in DB",storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB),RFL_DB)==false){
			//verify handling amount in RFO
			autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_HANDLING_COST_QUERY_RFO,consultantEmailID),RFO_DB);
			handlingDB = String.valueOf(dff.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "HandlingCost")));
			assertTrue("CRP Autoship Handling value is not as in DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));
		}

		// assert Shipping Method with RFL
		shippingMethodList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_METHOD_FOR_CONSULTANT_RFL_4289, shippingMethodId),RFL_DB);
		String methodName = String.valueOf(getValueFromQueryResult(shippingMethodList, "Name"));
		String methodShortName = String.valueOf(getValueFromQueryResult(shippingMethodList, "ShortName"));
		if(methodName.equalsIgnoreCase("FedEx Grnd")){
			methodName = "FedEx Ground";
		}
		shippingMethod = methodName+" "+"("+methodShortName+")";
		System.out.println("Method from DB "+shippingMethod);
		if(assertTrueDB("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(shippingMethod),RFL_DB)==false){
			// assert shipping method with RFO
			// waiting for RFo Queries
			//     assertTrue("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(shippingMethod));
		}


		// assert Subtotal with RFL
		if(assertTrueDB("Shipping Method is not as expected", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB), RFL_DB) == false){
			// assert subtotal with RFO
			// waiting for RFO Queries.
		}

		/*// assert Tax with RFL
	  if(assertTrueDB("", storeFrontOrdersPage.verifyAutoShipTemplateTax(taxDB), RFL_DB) == false){
	   // assert subtotal with RFO
	   // waiting for RFO Queries.
	  }

	  // assert Grand Total with RFL
	  if(assertTrueDB("", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB), RFL_DB) == false){
	   // assert subtotal with RFO
	   // waiting for RFO Queries.
	  }*/

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
		String cardTypeDB = null;

		List<Map<String, Object>> autoShipItemDetailsList = null;
		List<Map<String, Object>> autoShipItemTotalPriceList = null;
		List<Map<String, Object>> shippingAddressList = null;
		List<Map<String, Object>> shippingMethodList = null;
		List<Map<String, Object>> autoShipPaymentDetialsList = null;

		List<Map<String, Object>> randomPCList = DBUtil.performDatabaseQuery(DBQueries.GET_PC_USER_HAVING_AUTOSHIP_ORDER_RFL_4300,RFL_DB);
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

		// Assert for Schedule Date with RFL
		shippingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_ADDRESS_QUERY_TST4, pcEmailID),RFL_DB);

		firstName = (String) getValueFromQueryResult(shippingAddressList, "Attention");
		addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "Address1");
		city = (String) getValueFromQueryResult(shippingAddressList, "City");
		state = (String) getValueFromQueryResult(shippingAddressList, "State");
		postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
		phoneNumber = (String) getValueFromQueryResult(shippingAddressList, "PhoneNumber");
		country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		if(country.equals("1")){
			country = "United States"; 
		}   
		DecimalFormat df = new DecimalFormat("#.00");

		shippingAddressFromDB = firstName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
//		if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB), RFL_DB)== false){
//			// Assert for Schedule Date with RFO
//			shippingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_ADDRESS_QUERY_RFO, pcEmailID), RFO_DB);
//
//			firstName = String.valueOf(getValueFromQueryResult(shippingAddressList, "AddressProfileName"));
//			addressLine1 = String.valueOf(getValueFromQueryResult(shippingAddressList, "AddressLine1"));
//			postalCode = String.valueOf(getValueFromQueryResult(shippingAddressList, "PostalCode"));
//			locale = String.valueOf(getValueFromQueryResult(shippingAddressList, "Locale"));
//			region = String.valueOf(getValueFromQueryResult(shippingAddressList, "Region"));
//			country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
//			if(country.equals("236")){
//				country = "United States"; 
//			}
//			shippingAddressFromDB = addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase()+"\n";
//			assertTrue("Shipping Address is not as expected",storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB));
//		}

		// assert for shipping Method with RFL
		shippingMethodList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_SHIPPING_METHOD_QUERY_TST4, autoshipNumber), RFL_DB);
		shippingMethodDB = (String) getValueFromQueryResult(shippingMethodList, "Name");
		if(shippingMethodDB.equalsIgnoreCase("FedEx Grnd")){
			shippingMethodDB = "FedEx Ground (HD)";
		}
		if(assertTrueDB("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(shippingMethodDB), RFL_DB)==false){
			// assert for shipping Method with RFO
			// Waiting for queries from RFO.
		}

		// Assert for Card type with RFL
		autoShipPaymentDetialsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_PAYMENT_DETAILS_QUERY_TST4, pcEmailID), RFL_DB);
		autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_ORDER_DETAILS_QUERY_TST4, autoshipNumber), RFL_DB);
		subTotalDB = df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "Subtotal"));
		shippingDB = df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "ShippingAmount"));
		handlingDB = df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "HandlingAmount"));
		taxDB = df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "TaxAmount"));
		grandTotalDB = df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "Total"));

		// Assert for PC Perks SubTotal with RFL
		if(assertTrueDB("PC Perks SubTotal value is not as in DB", storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB), RFL_DB) == false){
			// assert for PC Perks SubTotal with RFO
			autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SUBTOTAL_VALUE_QUERY_RFO, pcEmailID), RFO_DB);
			subTotalDB = df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "SubTotal"));
			assertTrue("PC Perks SubTotal value is not as in DB",storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));
		}
		// assert PC Perks Shipping value with RFL
		if(assertTrueDB("PC Perks Shipping value is not as in DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB), RFL_DB)== false){
			// assert PC Perks Shipping value with RFO
			autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_COST_QUERY_RFO, pcEmailID), RFO_DB);
			shippingDB = df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "ShippingCost"));
			assertTrue("PC Perks Shipping value is not as in DB",storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));
		}

		// assert PC Handling value with RFL
		if(assertTrueDB("PC Perks Handling value is not as in DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB), RFL_DB) == false){
			// assert PC Perks Handling value with RFO
			autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_HANDLING_COST_QUERY_RFO, pcEmailID), RFO_DB);
			handlingDB = df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "HandlingCost"));
			assertTrue("PC Perks Handling value is not as in DB",storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));
		}

		// assert PC Tax Amount with RFL
		if(assertTrueDB("PC Perks Tax value is not as in DB", storeFrontOrdersPage.verifyAutoShipTemplateTax(taxDB), RFL_DB)==false){
			// assert PC Tax Amount with RFO
			// Waiting for RFO Queries
		}

		//assert PC GrandTotal Amount with RFL
		if(assertTrueDB("PC Perks Tax value is not as in DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB), RFL_DB)==false){
			// assert PC Tax Amount with RFO
			// Waiting for RFO Queries
		}

		logout();
		s_assert.assertAll();
	}
}
