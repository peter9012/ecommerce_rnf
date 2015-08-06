package com.rf.test.website.storeFront.account;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
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
	public void testAddNewShippingAddressOnShippingProfilePage_2029() throws InterruptedException, SQLException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;

		//------------------------------- Random Users part is commented for now-----------------------------------------------	
		/*		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
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
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;

		//------------------------------- Random Users part is commented for now-----------------------------------------------	
		/*		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
				consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");*/
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

	// Hybris Project-4461 ADD a shipping profile from MY ACCOUNT, having "Use this billing profile for your future auto-ship" check box NOT CHECKED:
	@Test
	public void testAddShippingProfileMyAccountFutureAutoshipNotSelected_4461() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;

		//------------------------------- Random Users part is commented for now-----------------------------------------------	
		/*		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
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
	@Test
	public void testChangeDefaultShippingProfile_2094() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;

		//------------------------------- Random Users part is commented for now-----------------------------------------------	
		/*		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
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

	//Hybris Project-4462 ADD/EDIT a shipping profile from AD-HOC CHECKOUT page. (There is NO checkbox.)
	@Test
	public void testEditShippingProfileAdhocCheckoutNoCheckbox_4462() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String consultantEmailID = null;
		consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_TST4; // A hard code user

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontShippingInfoPage = new StoreFrontShippingInfoPage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		storeFrontConsultantPage.clickOnShopLink();
		storeFrontConsultantPage.clickOnAllProductsLink();
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyCheckoutConfirmation(),"Confirmation of order popup is not present");
		storeFrontUpdateCartPage.clickOnConfirmationOK();
		storeFrontUpdateCartPage.editShippingAddress();
		s_assert.assertFalse(storeFrontUpdateCartPage.isUseThisShippingProfileFutureAutoshipChkboxVisible(),"Autoship Checkbox Present");

		String newShippingAddressName = TestConstants.NEW_ADDRESS_NAME_US+randomNum;
		String lastName = "test";
		storeFrontUpdateCartPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontUpdateCartPage.clickOnSaveShippingProfileAfterEdit();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		String orderNumber = storeFrontUpdateCartPage.getOrderNumberAfterPlaceOrder();
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOrderNumber(orderNumber);
		s_assert.assertTrue(storeFrontOrdersPage.isShippingAddressContainsName(newShippingAddressName),"AdHoc Orders Template Shipping Address doesnot contain new shipping address");

		logout();
		s_assert.assertAll();
	}

	//Hybris Project-4464 EDIT the billing profile and add a new billing address (not card)	
	@Test
	public void testEditCheckoutBillingProfileAndAddNewBillingAddressNotCard_4464() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;

		//------------------------------- Random Users part is commented for now----------------------------------------------- 
		/*  randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
	    consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");*/
		//---------------------------------------------------------------------------------------------------------------------  

		consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_TST4; // A hard code user
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);  
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnShopLink();
		storeFrontConsultantPage.clickOnAllProductsLink();
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyCheckoutConfirmation(),"Confirmation of order popup is not present");
		storeFrontUpdateCartPage.clickOnConfirmationOK();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnEditDefaultBillingProfile();
		storeFrontUpdateCartPage.clickAddANewAddressLink();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		String newBillingAddressLine1 = TestConstants.NEW_ADDRESS_LINE1_US+randomNum;
		String lastName = "ln";
		storeFrontUpdateCartPage.enterNewBillingAddressName(TestConstants.NEW_ADDRESS_NAME_US+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingAddressLine1(newBillingAddressLine1);
		storeFrontUpdateCartPage.enterNewBillingAddressCity(TestConstants.NEW_ADDRESS_CITY_US);
		storeFrontUpdateCartPage.selectNewBillingAddressState();
		storeFrontUpdateCartPage.enterNewBillingAddressPostalCode(TestConstants.NEW_ADDRESS_POSTAL_CODE_US);
		storeFrontUpdateCartPage.enterNewBillingAddressPhoneNumber(TestConstants.NEW_ADDRESS_PHONE_NUMBER_US);
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyNewAddressGetsAssociatedWithTheDefaultBillingProfile(newBillingAddressLine1), "New Address has not got associated with the billing profile");

		logout();
		s_assert.assertAll();
	}

	//Hybris Project-4463 select any other shipping info by selecting radio button 'ship to this address'
	@Test
	public void testSelectOtherShippingInfoBySelectingRadioButton_4463() throws InterruptedException	{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		consultantEmailID = TestConstants.CONSULTANT_EMAIL_ID_TST4; // A hard code user

		//------------------------------- Random Users part is commented for now----------------------------------------------- 
		/*  randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
	    consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");*/
		//---------------------------------------------------------------------------------------------------------------------

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);  
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnShopLink();
		storeFrontConsultantPage.clickOnAllProductsLink();
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyCheckoutConfirmation(),"Confirmation of order popup is not present");
		storeFrontUpdateCartPage.clickOnConfirmationOK();
		String newShippingAddressName = storeFrontUpdateCartPage.clickOnNewShipToThisAddressRadioButtonAndReturnProfileName();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnPaymentNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		String orderNumber = storeFrontUpdateCartPage.getOrderNumberAfterPlaceOrder();
		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		//------------------ Verify that autoship template doesn't contains the default shipping profile address by verifying by name------------------------------------------------------------		

		s_assert.assertFalse(storeFrontOrdersPage.isShippingAddressContainsName(newShippingAddressName),"Autoship Template Shipping Address contains the default shipping address");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOrderNumber(orderNumber);

		//------------------ Verify that adhoc orders template contains the newly created shipping profile address by verifying by name------------------------------------------------------------

		s_assert.assertTrue(storeFrontOrdersPage.isShippingAddressContainsName(newShippingAddressName),"AdHoc Orders Template Shipping Address doesn't contains new shipping address when future autoship checkbox not selected");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		logout();
		s_assert.assertAll();	
	}

	//select any other shipping info by selecting radio button 'ship to this address' via Next CRP
	@Test
	public void testSelectOtherShippingInfoBySelectingRadioButtonViaNextCrp() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = "anneginsberg@hotmail.com";
		//------------------------------- Random Users part is commented for now----------------------------------------------- 
		/*  randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
	    consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");*/
		//---------------------------------------------------------------------------------------------------------------------  
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);  
		logger.info("login is successful"); 
		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickNextCRP();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		String newShippingAddressName = storeFrontUpdateCartPage.clickOnNewShipToThisAddressRadioButtonAndReturnProfileName();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnPaymentNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		//------------------ Verify that autoship template contains the default shipping profile address by verifying by name------------------------------------------------------------		

		s_assert.assertTrue(storeFrontOrdersPage.isShippingAddressContainsName(newShippingAddressName),"Autoship Template Shipping Address doesn't contains the default shipping address");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();

		//------------------ Verify that adhoc orders template doesn't  contains the newly created shipping profile address by verifying by name------------------------------------------------------------

		s_assert.assertFalse(storeFrontOrdersPage.isShippingAddressContainsName(newShippingAddressName),"AdHoc Orders Template Shipping Address contains new shipping address when future autoship checkbox not selected");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		logout();
		s_assert.assertAll();	


	}

}