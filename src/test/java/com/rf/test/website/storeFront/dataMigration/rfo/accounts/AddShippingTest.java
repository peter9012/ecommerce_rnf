package com.rf.test.website.storeFront.dataMigration.rfo.accounts;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.StoreFrontCartAutoShipPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontOrdersPage;
import com.rf.pages.website.StoreFrontShippingInfoPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class AddShippingTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(AddShippingTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontShippingInfoPage storeFrontShippingInfoPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;

	private String RFL_DB = null;
	private String RFO_DB = null;		

	// Hybris Phase 2-2029 :: Version : 1 :: Add shipping address on 'Shipping Profile' page 	
	@Test
	public void testAddNewShippingAddressOnShippingProfilePage() throws InterruptedException, SQLException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;

		//------------------------------- Random Users part is commented for now-----------------------------------------------	
			/*	randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_TST4,RFO_DB);
//			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");*/
		//---------------------------------------------------------------------------------------------------------------------

		consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_TST4;  // A Hard Coded User		
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);		
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontShippingInfoPage.clickAddNewShippingProfileLink();
		String newShippingAddressName = TestConstants.NEW_ADDRESS_NAME_US+randomNum;
		String lastName = "ln";
		storeFrontShippingInfoPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(TestConstants.NEW_ADDRESS_LINE1_US);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(TestConstants.NEW_ADDRESS_CITY_US);
		storeFrontShippingInfoPage.selectNewShippingAddressState();
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(TestConstants.NEW_ADDRESS_POSTAL_CODE_US);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.NEW_ADDRESS_PHONE_NUMBER_US);
		storeFrontShippingInfoPage.selectFirstCardNumber();
		storeFrontShippingInfoPage.enterNewShippingAddressSecurityCode(TestConstants.NEW_ADDRESS_SECURITY_NUMBER_US);
		storeFrontShippingInfoPage.selectUseThisShippingProfileFutureAutoshipChkbox();
		storeFrontShippingInfoPage.clickOnSaveShippingProfile();

		//--------------- Verify that Newly added Shipping is listed in the Shipping profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingAddressName), "New Shipping address is not listed on Shipping profile page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		//--------------- Verify That 'Autoship Order Address' Text is displayed under default shipping Address-------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontShippingInfoPage.isAutoshipOrderAddressTextPresent(newShippingAddressName), "Autoship order text is not present under the new Shipping Address when future autoship checkbox is selected");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickNextCRP();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditShipping();

		//---------------Verify that the new added shipping address is displayed in 'Shipment' section on update autoship cart page------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontUpdateCartPage.isShippingAddressPresent(newShippingAddressName), "New Shipping address NOT selected in update cart under shipping section");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		logout();
		s_assert.assertAll();		
	}

	//Hybris Phase 2-2031:Add shipping address in autoship template
	@Test
	public void testAddShippingAddressInAutoshipTemplate_2031() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;

		//------------------------------- Random Users part is commented for now-----------------------------------------------	
		/*		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_TST4,RFO_DB);
//			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");*/
		//---------------------------------------------------------------------------------------------------------------------		

		consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_TST4; 		
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);		
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		
		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickNextCRP();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditShipping();
		storeFrontUpdateCartPage.clickAddNewShippingProfileLink();
		String newShippingAddressName = TestConstants.NEW_ADDRESS_NAME_US+randomNum;
		String lastName = "ln";
		storeFrontUpdateCartPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontUpdateCartPage.enterNewShippingAddressLine1(TestConstants.NEW_ADDRESS_LINE1_US);
		storeFrontUpdateCartPage.enterNewShippingAddressCity(TestConstants.NEW_ADDRESS_CITY_US);
		storeFrontUpdateCartPage.selectNewShippingAddressState();
		storeFrontUpdateCartPage.enterNewShippingAddressPostalCode(TestConstants.NEW_ADDRESS_POSTAL_CODE_US);
		storeFrontUpdateCartPage.enterNewShippingAddressPhoneNumber(TestConstants.NEW_ADDRESS_PHONE_NUMBER_US);
		storeFrontUpdateCartPage.clickOnSaveShippingProfile();
		storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtn();
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		storeFrontUpdateCartPage.clickOnEditShipping();

		//---------------Verify that the new added shipping address is displayed in 'Shipment' section on update autoship cart page------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontUpdateCartPage.isShippingAddressPresent(newShippingAddressName), "New Shipping address NOT selected in update cart under shipping section");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingAddressName), "New Shipping address is not listed on Shipping profile page");
		logout();
		s_assert.assertAll();
	}

	// ADD a shipping profile from MY ACCOUNT, having "Use this billing profile for your future auto-ship" check box NOT CHECKED:
	//Hybris Project-4461:ADD a shipping profile from MY ACCOUNT, having "Use this billing profile for your future auto-ship"
	@Test
	public void testAddShippingProfileMyAccountFutureAutoshipNotSelected() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;

		//------------------------------- Random Users part is commented for now-----------------------------------------------	
	/* randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_HAVING_ACTIVE_ORDERS_RFO,RFO_DB);
				consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");*/
		//---------------------------------------------------------------------------------------------------------------------

		consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_TST4;  // A Hard Coded User		
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);		
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontShippingInfoPage.clickAddNewShippingProfileLink();
		String newShippingAddressName = TestConstants.NEW_ADDRESS_NAME_US+randomNum;
		String lastName = "ln";
		storeFrontShippingInfoPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(TestConstants.NEW_ADDRESS_LINE1_US);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(TestConstants.NEW_ADDRESS_CITY_US);
		storeFrontShippingInfoPage.selectNewShippingAddressState();
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(TestConstants.NEW_ADDRESS_POSTAL_CODE_US);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.NEW_ADDRESS_PHONE_NUMBER_US);
		storeFrontShippingInfoPage.selectFirstCardNumber();
		storeFrontShippingInfoPage.enterNewShippingAddressSecurityCode(TestConstants.NEW_ADDRESS_SECURITY_NUMBER_US);
		storeFrontShippingInfoPage.clickOnSaveShippingProfile();
		

		//--------------- Verify that Newly added Shipping is listed in the Shipping profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingAddressName), "New Shipping address is not listed on Shipping profile page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		//--------------- Verify That 'Autoship Order Address' Text is not displayed under default shipping Address-------------------------------------------------------------------------------------------

		s_assert.assertFalse(storeFrontShippingInfoPage.isAutoshipOrderAddressTextPresent(newShippingAddressName), "Autoship order text is present under the new Shipping Address when future autoship checkbox is not selected");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		//------------------ Verify that autoship template doesn't contains the newly created shipping profile address by verifying by name------------------------------------------------------------		

		s_assert.assertFalse(storeFrontOrdersPage.isShippingAddressContainsName(newShippingAddressName),"Autoship Template Shipping Address contains the new shipping address even when future autoship checkbox not selected");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();

		//------------------ Verify that adhoc orders template doesn't contains the newly created shipping profile address by verifying by name------------------------------------------------------------

		s_assert.assertFalse(storeFrontOrdersPage.isShippingAddressContainsName(newShippingAddressName),"AdHoc Orders Template Shipping Address contains new shipping address when future autoship checkbox not selected");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		logout();
		s_assert.assertAll();		
	}
	//Hybris Phase 2:2094 Pop up to update autoship shipping profile on changing default selection
	//At Shipping info page, on changing default shipping profile (by selecting radio button),  and confirming the pop up message
	@Test
	public void testChangeDefaultShippingProfile() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;

		//------------------------------- Random Users part is commented for now-----------------------------------------------	
			/*	randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_HAVING_ACTIVE_ORDERS_RFO,RFO_DB);
				consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");*/
		//---------------------------------------------------------------------------------------------------------------------

		consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_TST4;  // A Hard Coded User
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);		
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontShippingInfoPage.clickAddNewShippingProfileLink();
		String newShippingAddressName = TestConstants.NEW_ADDRESS_NAME_US+randomNum;
		String lastName = "ln";
		storeFrontShippingInfoPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(TestConstants.NEW_ADDRESS_LINE1_US);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(TestConstants.NEW_ADDRESS_CITY_US);
		storeFrontShippingInfoPage.selectNewShippingAddressState();
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(TestConstants.NEW_ADDRESS_POSTAL_CODE_US);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.NEW_ADDRESS_PHONE_NUMBER_US);
		storeFrontShippingInfoPage.selectFirstCardNumber();
		storeFrontShippingInfoPage.enterNewShippingAddressSecurityCode(TestConstants.NEW_ADDRESS_SECURITY_NUMBER_US);
		storeFrontShippingInfoPage.clickOnSaveShippingProfile();

		//--------------- Verify that Newly added Shipping is listed in the Shipping profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingAddressName), "New Shipping address is not listed on Shipping profile page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		
		storeFrontShippingInfoPage.makeShippingProfileAsDefault(newShippingAddressName);
		s_assert.assertTrue(storeFrontShippingInfoPage.isDefaultAddressRadioBtnSelected(newShippingAddressName),"Default shipping address is not selected");
		s_assert.assertTrue(storeFrontShippingInfoPage.isAutoshipOrderAddressTextPresent(newShippingAddressName),"Autoship order address is not present under default shipping profile");
		
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		//------------------ Verify that autoship template contains the default shipping profile address by verifying by name------------------------------------------------------------		

		s_assert.assertTrue(storeFrontOrdersPage.isShippingAddressContainsName(newShippingAddressName),"Autoship Template Shipping Address doesn't contains the default shipping address");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		
		logout();
		s_assert.assertAll();		
	}

}


