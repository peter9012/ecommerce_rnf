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
import com.rf.pages.website.storeFront.StoreFrontCartAutoShipPage;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
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
		while(true){

			randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomPCList, "AccountID"));
			logger.info("The Account ID of the user is "+accountID);

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+storeFrontPCUserPage);
				driver.get(driver.getURL());
			}
			else
				break;
		} 

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

		storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(selectedShippingMethod),"shipping method on autoship template is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate()+" NOT "+selectedShippingMethod);

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

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");	
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 

		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditShipping();
		String selectedShippingMethod = storeFrontUpdateCartPage.selectAndGetShippingMethodName();
		logger.info("Shipping Method selected is: "+selectedShippingMethod);
		storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtn();
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();

		storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		s_assert.assertTrue(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().contains(selectedShippingMethod),"shipping method on autoship template is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate()+" NOT "+selectedShippingMethod);

		s_assert.assertAll();
	}

	//Hybris Phase 2-4289 : Verify CRP autoship template details
	@Test
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
		String consultantEmailID = null;
		String lastName = null;
		String shippingMethodId = null;
		String accountID = null;

		List<Map<String, Object>> autoShipItemDetailsList = null;
		List<Map<String, Object>> shippingAddressList = null;
		List<Map<String, Object>> shippingMethodList = null;
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> orderIdAccountIdDetailsList =  null;
		List<Map<String,Object>> shippingCostAndHandlingCostList = null;
		List<Map<String,Object>> getOtherDetailValuesList = null;
		List<Map<String, Object>> verifyShippingMethodList = null;

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");	
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
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



	// Hybris Phase 2-4300 :: Version : 1 :: Verify PC autoship template details. 
	@Test
	public void testPCAutoShipTemplateDetails_HP2_4300() throws InterruptedException, SQLException , ClassNotFoundException{
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
}