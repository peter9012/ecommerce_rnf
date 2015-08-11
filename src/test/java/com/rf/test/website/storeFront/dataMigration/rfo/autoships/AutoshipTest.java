package com.rf.test.website.storeFront.dataMigration.rfo.autoships;

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

		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		//------------------------------- Hard coded User part is commented for now-----------------------------------------------	
		//pcUserEmailID = TestConstants.PC_USER_EMAIL_ID_RFO;	
		//---------------------------------------------------------------------------------------------------------------------		

		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_PC_EMAIL_ID_HAVING_ACTIVE_ORDER_RFO,RFO_DB);
		pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "Username");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, TestConstants.PC_PASSWORD_TST4);
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
	/*This Script is getting failed on "selectAndGetShippingMethodName Method, 
	 *due to the Radio Button Not present on UI*/

	// Hybris Phase 2-131:change shipping method on autoship - Consultant
	@Test
	public void testChangeShippingMethodOnConsultantAutoShip_131() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;

		//------------------------------- Hard coded User part is commented for now-----------------------------------------------	
		//consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_RFO;	
		//---------------------------------------------------------------------------------------------------------------------		


		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_HAVING_ACTIVE_ORDERS_RFO,RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");

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
	@Test
	public void testCRPAutoShipTemplateDetails_HP2_4289_r() throws InterruptedException, SQLException{
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
		String consultantEmailID = null;
		String accountId = null;
		String autoshipId = null;
		String shippingMethodId =null;
		String shippingMethodDB = null;

		List<Map<String, Object>> expirationDateOfCardList = null;
		List<Map<String, Object>> autoShipItemDetailsList = null;
		List<Map<String, Object>> autoShipItemTotalPriceList = null;
		List<Map<String, Object>> shippingAddressList = null;
		List<Map<String, Object>> shippingMethodList = null;
		List<Map<String, Object>> autoShipPaymentDetailsList = null;
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String,Object>> autoshipIdAccountIdDetailsList = null;
		List<Map<String,Object>> subTotalAndGrandTotalList = null;
		DecimalFormat df = new DecimalFormat("#.00");

		autoshipIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries.GET_ACCOUNT_ID_4289_RFO,RFO_DB);
		autoshipId = String.valueOf( getValueFromQueryResult(autoshipIdAccountIdDetailsList,"AutoshipID"));
		accountId = String.valueOf( getValueFromQueryResult(autoshipIdAccountIdDetailsList, "AccountID"));
		System.out.println("account id from database is"+accountId);
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_CONSULTANT_HAVING_SUBMITTED_ORDER_4289_RFO,accountId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");
		System.out.println("Pc user email id from database is"+randomConsultantList);

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
		s_assert.assertTrue(storeFrontOrdersPage.verifySVValue("consultant"),"CRP Autoship template SV value is not as expcted");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfScheduleDateText(),"Schedule Date Text is not present on the Page");
		s_assert.assertTrue(storeFrontOrdersPage.verifyPresenceOfOrderStatusText(),"Order Status Text is not present on the Page");


		//shippingAddressFromDB = firstName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		//if(assertTrueDB("Shipping Address is not as expected", storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB),RFL_DB)==false){
		//verify shipping address in RFO

		//QUERY FOR SHIPPING ADDRESS LIST GIVES 5 RESULT SO UNABLE TO PROCEED FURTHER.
		shippingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_ADDRESS_QUERY_4289_RFO, autoshipId), RFO_DB);
		firstName = String.valueOf(getValueFromQueryResult(shippingAddressList, "FirstName"));
		addressLine1 = String.valueOf(getValueFromQueryResult(shippingAddressList, "Address1"));
		postalCode = String.valueOf(getValueFromQueryResult(shippingAddressList, "PostalCode"));
		locale = String.valueOf(getValueFromQueryResult(shippingAddressList, "Locale"));
		region = String.valueOf(getValueFromQueryResult(shippingAddressList, "Region"));

		country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		if(country.equals("236")){
			country = "United States"; 
		}
		//shippingAddressFromDB = firstName+"\n"+ addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		//shippingAddressFromDB = addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		//assert shipping method in RFO
		//assertTrue("Shipping Address is not as expected",  storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB));
		subTotalAndGrandTotalList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_ORDER_DETAILS_SUBTOTAL_4289_RFO, autoshipId), RFO_DB);
		subTotalDB = df.format((Number) getValueFromQueryResult(subTotalAndGrandTotalList, "Subtotal"));
		taxDB = df.format((Number) getValueFromQueryResult(subTotalAndGrandTotalList, "TotalTax"));
		grandTotalDB = df.format((Number) getValueFromQueryResult(subTotalAndGrandTotalList, "Total"));

		autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_ORDER_DETAILS_QUERY_4289_RFO, autoshipId), RFO_DB);
		shippingDB = df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "ShippingCost"));
		handlingDB = df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "HandlingCost"));
		shippingMethodId = String.valueOf(getValueFromQueryResult(autoShipItemDetailsList, "ShippingMethodID"));

		//assert the shipping method in RFO
		shippingMethodList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
		shippingMethodDB = (String) getValueFromQueryResult(shippingMethodList, "Name");
		assertTrue("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(shippingMethodDB));

		// assert for SubTotal with RFO
		assertTrue("PC Perks SubTotal value is not as in DB",storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));

		//verify shipping amount in RFO
		assertTrue("CRP Autoship Shipping value is not as in DB", storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));

		//verify handling amount in RFO
		assertTrue("CRP Autoship Handling value is not as in DB", storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));
		// assert  Tax Amount with RFO
		assertTrue("PC Perks Tax value is not as in DB", storeFrontOrdersPage.verifyAutoShipTemplateTax(taxDB));

		//assert  GrandTotal Amount with RFO
		assertTrue("PC Perks Tax value is not as in DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB));

		logout();
		s_assert.assertAll();




		//- - - - - -- - - - -- -******************************REASON FOR COMMENTED CODE*****************- - - - - - - - - - - - - - - - - -
		//Assertion for	autoShipItemDetailsList for subTotalDB value query result is not as in UI for both RFO and RFL.
		//Assertion for	autoShipItemDetailsList for taxDB value query result is not as in UI for both RFO and RFL.
		//Assertion for	autoShipItemDetailsList for grantTotalDB value query result is not as in UI for both RFO and RFL.
		//Unable to find query for shipping method for RFO database.
	}

	// Hybris Phase 2-4300 :: Version : 1 :: Verify PC autoship template details. 
	@Test
	public void testPCAutoShipTemplateDetails_HP2_4300() throws InterruptedException, SQLException , ClassNotFoundException{
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
		String pcEmailID = null;
		String accountId = null;
		String autoshipId = null;
		String shippingMethodId =null;

		List<Map<String, Object>> autoShipItemDetailsList = null;
		List<Map<String,Object>> subTotalAndGrandTotalList = null;
		List<Map<String, Object>> autoShipItemTotalPriceList = null;
		List<Map<String, Object>> shippingAddressList = null;
		List<Map<String, Object>> shippingMethodList = null;
		List<Map<String, Object>> autoShipPaymentDetialsList = null;
		List<Map<String,Object>> autoshipIdAccountIdDetailsList = null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		List<Map<String, Object>> randomPCList = null;
		DecimalFormat df = new DecimalFormat("#.00");

		autoshipIdAccountIdDetailsList = DBUtil.performDatabaseQuery(DBQueries.GET_ACCOUNT_ID_4300_RFO,RFO_DB);
		autoshipId = String.valueOf( getValueFromQueryResult(autoshipIdAccountIdDetailsList,"AutoshipID"));
		accountId = String.valueOf( getValueFromQueryResult(autoshipIdAccountIdDetailsList, "AccountID"));
		System.out.println("account id from database is"+accountId);
		randomPCList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_PC_USER_HAVING_AUTOSHIP_ORDER_4300_RFO,accountId),RFO_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCList, "Username");
		System.out.println("Pc user email id from database is"+pcEmailID);
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

		//
		//			shippingAddressFromDB = firstName+"\n"+addressLine1+"\n"+city+", "+state+" "+postalCode+"\n"+country.toUpperCase()+"\n";

		shippingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_ADDRESS_QUERY_4300_RFO, autoshipId), RFO_DB);
		firstName = String.valueOf(getValueFromQueryResult(shippingAddressList, "FirstName"));
		addressLine1 = String.valueOf(getValueFromQueryResult(shippingAddressList, "Address1"));
		postalCode = String.valueOf(getValueFromQueryResult(shippingAddressList, "PostalCode"));
		locale = String.valueOf(getValueFromQueryResult(shippingAddressList, "Locale"));
		region = String.valueOf(getValueFromQueryResult(shippingAddressList, "Region"));

		country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		if(country.equals("236")){
			country = "United States"; 
		}
		//				shippingAddressFromDB = addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase()+"\n";
		//				//Assert shipping address in RFO
		//				assertTrue("Shipping Address is not as expected",storeFrontOrdersPage.verifyShippingAddressDetails(shippingAddressFromDB));


		//			if(shippingMethodDB.equalsIgnoreCase("FedEx Grnd")){
		//				shippingMethodDB = "FedEx Ground (HD)";
		//			}
		subTotalAndGrandTotalList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_ORDER_DETAILS_SUBTOTAL_4300_RFO, autoshipId), RFO_DB);
		subTotalDB = df.format((Number) getValueFromQueryResult(subTotalAndGrandTotalList, "Subtotal"));
		taxDB = df.format((Number) getValueFromQueryResult(subTotalAndGrandTotalList, "TotalTax"));
		grandTotalDB = df.format((Number) getValueFromQueryResult(subTotalAndGrandTotalList, "Total"));

		autoShipItemDetailsList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_AUTOSHIP_ORDER_DETAILS_QUERY_4300_RFO, autoshipId), RFO_DB);
		shippingDB = df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "ShippingCost"));
		handlingDB = df.format((Number) getValueFromQueryResult(autoShipItemDetailsList, "HandlingCost"));
		shippingMethodId = String.valueOf(getValueFromQueryResult(autoShipItemDetailsList, "ShippingMethodID"));

		//assert the shipping method in RFO
		shippingMethodList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_METHOD_QUERY_RFO, shippingMethodId), RFO_DB);
		shippingMethodDB = (String) getValueFromQueryResult(shippingMethodList, "Name");
		assertTrue("Shipping Method is not as expected", storeFrontOrdersPage.verifyShippingMethod(shippingMethodDB));

		// assert for PC Perks SubTotal with RFO
		assertTrue("PC Perks SubTotal value is not as in DB",storeFrontOrdersPage.verifyAutoShipTemplateSubtotal(subTotalDB));

		// assert PC Perks Shipping value with RFO
		assertTrue("PC Perks Shipping value is not as in DB",storeFrontOrdersPage.verifyAutoShipTemplateShipping(shippingDB));

		// assert PC Perks Handling value with RFO
		assertTrue("PC Perks Handling value is not as in DB",storeFrontOrdersPage.verifyAutoShipTemplateHandling(handlingDB));

		// assert PC Tax Amount with RFO
		assertTrue("PC Perks Tax value is not as in DB", storeFrontOrdersPage.verifyOrderHistoryTax(taxDB));

		//assert PC GrandTotal Amount with RFO
		assertTrue("PC Perks Tax value is not as in DB", storeFrontOrdersPage.verifyAutoshipGrandTotalPrice(grandTotalDB));

		logout();
		s_assert.assertAll();
	}
}