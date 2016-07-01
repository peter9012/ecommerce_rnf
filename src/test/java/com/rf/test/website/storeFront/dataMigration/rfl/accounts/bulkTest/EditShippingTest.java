package com.rf.test.website.storeFront.dataMigration.rfl.accounts.bulkTest;

import java.sql.SQLException;
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
import com.rf.pages.website.storeFront.StoreFrontShippingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class EditShippingTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(EditShippingTest.class.getName());


	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontShippingInfoPage storeFrontShippingInfoPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;

	private String RFL_DB = null;
	private String RFO_DB = null;

	//Hybris Phase 2-4326: View shipping address on 'Shipping Profile' page
	@Test(dataProvider="rfTestData")
	public void testShippingAddressOnShippingProfile_HP2_4326(String accountID,String fName,String mName,String lName,String password, String customerType ,String accountTypeId,String active, String asignedUsers) throws InterruptedException, SQLException{
		if(accountTypeId!="3"){
			RFL_DB = driver.getDBNameRFL();
			RFO_DB = driver.getDBNameRFO();	

			int totalShippingAddressesFromDB = 0;
			List<Map<String, Object>> shippingAddressCountList =  null;
			List<Map<String, Object>> defaultShippingAddressList =  null;
			String shippingAddressName=null;
			logger.info("Account Id of the user is "+accountID);
			List<Map<String, Object>> emailIdList =  null;
			String emailID = null;
			emailIdList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_EMAILID_FROM_ACCOUNTID,accountID),RFL_DB);
			emailID = (String) getValueFromQueryResult(emailIdList, "EmailAddress");
			logger.info("EmailID= "+emailID);

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(emailID, password);

			s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
			logger.info("login is successful");

			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
			s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");

			//------------------The same number of shipping addresses is shown in RFL and Front end----------------------------------------------------------------------------------------------------------------------------
			shippingAddressCountList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_SHIPPING_ADDRESS_COUNT_QUERY_TST4,emailID),RFL_DB);
			totalShippingAddressesFromDB = (Integer) getValueFromQueryResult(shippingAddressCountList, "count");		
			if(assertEqualsDB("Shipping Addresses count on UI is different from DB", totalShippingAddressesFromDB,storeFrontShippingInfoPage.getTotalShippingAddressesDisplayed(),RFL_DB)==false){

				//------------------The same number of billing addresses is shown in RFO and Front end----------------------------------------------------------------------------------------------------------------------------
				shippingAddressCountList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_COUNT_QUERY,emailID),RFO_DB);
				totalShippingAddressesFromDB = (Integer) getValueFromQueryResult(shippingAddressCountList, "count");			
				assertEquals("Shipping Addresses count on UI is different from DB", totalShippingAddressesFromDB,storeFrontShippingInfoPage.getTotalShippingAddressesDisplayed());			
			}

			//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

			if(totalShippingAddressesFromDB > 1){
				defaultShippingAddressList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_DEFAULT_SHIPPING_ADDRESS_QUERY_TST4,emailID),RFL_DB);
				shippingAddressName = (String) getValueFromQueryResult(defaultShippingAddressList, "Name");
				shippingAddressName = shippingAddressName.split(" ")[0];

				//-------------------------------------Radio button is checked for the default shipping address on Front end as per RFL--------------------------------------------------------------------------------------------
				if(assertTrueDB("Default radio button in Shipping page is not selected", storeFrontShippingInfoPage.isDefaultShippingAddressSelected(shippingAddressName),RFL_DB)==false){

					//---------------------------------Radio button is checked for the default shipping address on Front end as per RFO--------------------------------------------------------------------------------------------
					defaultShippingAddressList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_DEFAULT_BILLING_ADDRESS_QUERY,emailID),RFO_DB);
					shippingAddressName = (String) getValueFromQueryResult(defaultShippingAddressList, "AddressLine1");
					assertTrue("Default radio button in Shipping page is not selected", storeFrontShippingInfoPage.isDefaultShippingAddressSelected(shippingAddressName));
				}
			}

			//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

			s_assert.assertAll();
		}
	}


	// Hybris Phase 2-2035 :: Version : 1 :: Edit shipping address on 'Shipping Profile' page
	@Test(dataProvider="rfTestData")
	public void testEditShippingAddressOnShippingProfilePage_2035(String accountID,String fName,String mName,String lName,String password, String customerType ,String accountTypeId,String active, String asignedUsers) throws InterruptedException, SQLException{
		if(accountTypeId!="3"){
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			RFL_DB = driver.getDBNameRFL();
			RFO_DB = driver.getDBNameRFO();
			logger.info("Account Id of the user is "+accountID);
			List<Map<String, Object>> emailIdList =  null;
			String emailID = null;
			emailIdList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_EMAILID_FROM_ACCOUNTID,accountID),RFL_DB);
			emailID = (String) getValueFromQueryResult(emailIdList, "EmailAddress");
			logger.info("EmailID= "+emailID);


			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(emailID, password);

			s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
			logger.info("login is successful");

			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
			s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
			storeFrontShippingInfoPage.clickOnEditForFirstAddress();
			String newShippingAdrressName = TestConstants.ADDRESS_NAME_US+randomNum;
			String lastName = "test";
			storeFrontShippingInfoPage.enterNewShippingAddressName(newShippingAdrressName+" "+lastName);
			storeFrontShippingInfoPage.enterNewShippingAddressLine1(TestConstants.ADDRESS_LINE_1_US);
			storeFrontShippingInfoPage.enterNewShippingAddressCity(TestConstants.CITY_US);
			storeFrontShippingInfoPage.selectNewShippingAddressState(TestConstants.STATE_US);
			storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(TestConstants.POSTAL_CODE_US);
			storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER_US);
			storeFrontShippingInfoPage.selectFirstCardNumber();
			storeFrontShippingInfoPage.enterNewShippingAddressSecurityCode(TestConstants.SECURITY_NUMBER_US);
			storeFrontShippingInfoPage.selectUseThisShippingProfileFutureAutoshipChkbox();
			storeFrontShippingInfoPage.clickOnSaveShippingProfile();

			//--------------- Verify that Newly added Shipping is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------

			s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingAdrressName), "New Shipping address is not selected listed on Shipping profile page");

			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

			//--------------- Verify That 'Autoship Order Address' Text is displayed under default shipping Address-------------------------------------------------------------------------------------------

			//s_assert.assertTrue(storeFrontShippingInfoPage.isAutoshipOrderAddressTextPresent(newShippingAdrressName), "Autoship order text not present under the new Shipping Address");

			//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


			storeFrontCartAutoShipPage = storeFrontConsultantPage.clickNextCRP();
			storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
			storeFrontUpdateCartPage.clickOnEditShipping();

			//---------------Verify that the new added shipping address is displayed in 'Shipment' section on update autoship cart page------------------------------------------------------------------------

			s_assert.assertTrue(storeFrontUpdateCartPage.isShippingAddressPresent(newShippingAdrressName), "New Shipping address NOT added to update cart");

			//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

			storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();

			s_assert.assertAll();
		}
	}

	//Hybris Project-4465 Edit a shipping profile from MY ACCOUNT, having "Use this billing profile for your future auto-ship" check box NOT CHECKED:
	@Test
	public void testEditShippingProfileMyAccountFutureAutoshipNotSelected_4465() throws InterruptedException{
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
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontShippingInfoPage.clickOnEditForFirstAddress();
		String newShippingAddressName = TestConstants.ADDRESS_NAME_US+randomNum;
		String lastName = "test";
		storeFrontShippingInfoPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(TestConstants.ADDRESS_LINE_1_US);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(TestConstants.CITY_US);
		storeFrontShippingInfoPage.selectNewShippingAddressState(TestConstants.STATE_US);
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(TestConstants.POSTAL_CODE_US);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER_US);
		storeFrontShippingInfoPage.selectFirstCardNumber();
		storeFrontShippingInfoPage.enterNewShippingAddressSecurityCode(TestConstants.SECURITY_NUMBER_US);
		storeFrontShippingInfoPage.clickOnSaveShippingProfile();

		//--------------- Verify that Newly edited Shipping is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingAddressName), "New Shipping address is not listed on Shipping profile page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		//------------------ Verify that autoship template doesn't contains the newly edited shipping profile address by verifying by name------------------------------------------------------------		

		s_assert.assertFalse(storeFrontOrdersPage.isShippingAddressContainsName(newShippingAddressName),"Autoship Template Shipping Address contains the new shipping address even when future autoship checkbox not selected");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();

		//------------------ Verify that adhoc orders template doesn't contains the newly created shipping profile address by verifying by name------------------------------------------------------------

		s_assert.assertFalse(storeFrontOrdersPage.isShippingAddressContainsName(newShippingAddressName),"AdHoc Orders Template Shipping Address contains new shipping address even when future autoship checkbox not selected");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


		s_assert.assertAll();
	}

}
