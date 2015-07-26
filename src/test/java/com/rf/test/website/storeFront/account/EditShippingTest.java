package com.rf.test.website.storeFront.account;

import java.sql.SQLException;
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
import com.rf.pages.website.StoreFrontShippingInfoPage;
import com.rf.pages.website.StoreFrontUpdateCartPage;
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
	@Test
	public void testShippingAddressOnShippingProfile_HP2_4326() throws InterruptedException, SQLException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();		
		int totalShippingAddressesFromDB = 0;
		List<Map<String, Object>> shippingAddressCountList =  null;
		List<Map<String, Object>> defaultShippingAddressList =  null;
		String address1=null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_TST4, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");

		// assert with RFL
		shippingAddressCountList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_ADDRESS_COUNT_QUERY_TST4,TestConstants.CONSULTANT_EMAIL_ID_TST4),RFL_DB);
		totalShippingAddressesFromDB = (Integer) getValueFromQueryResult(shippingAddressCountList, "count");		
		if(assertEqualsDB("Shipping Addresses count on UI is different from DB", totalShippingAddressesFromDB,storeFrontShippingInfoPage.getTotalShippingAddressesDisplayed(),RFL_DB)==false){
			//assert with RFO
			shippingAddressCountList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_ADDRESS_COUNT_QUERY,TestConstants.CONSULTANT_EMAIL_ID_TST4),RFO_DB);
			totalShippingAddressesFromDB = (Integer) getValueFromQueryResult(shippingAddressCountList, "count");			
			assertEquals("Shipping Addresses count on UI is different from DB", totalShippingAddressesFromDB,storeFrontShippingInfoPage.getTotalShippingAddressesDisplayed());			
		}

		if(totalShippingAddressesFromDB > 1){
			defaultShippingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_DEFAULT_SHIPPING_ADDRESS_QUERY_TST4,TestConstants.CONSULTANT_EMAIL_ID_TST4),RFL_DB);
			address1 = (String) getValueFromQueryResult(defaultShippingAddressList, "Address1");
			//assert with RFL
			if(assertTrueDB("Default radio button in Shipping page is not selected", storeFrontShippingInfoPage.isDefaultShippingAddressSelected(address1),RFL_DB)==false){
				//assert with RFO
				defaultShippingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_DEFAULT_BILLING_ADDRESS_QUERY,TestConstants.CONSULTANT_EMAIL_ID_TST4),RFO_DB);
				address1 = (String) getValueFromQueryResult(defaultShippingAddressList, "AddressLine1");
				assertTrue("Default radio button in Shipping page is not selected", storeFrontShippingInfoPage.isDefaultShippingAddressSelected(address1));
			}
		}
		logout();
		s_assert.assertAll();
	}


	// Hybris Phase 2-2035 :: Version : 1 :: Edit shipping address on 'Shipping Profile' page
	public void testEditShippingAddressOnShippingProfilePage() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);		
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontShippingInfoPage.clickOnEditForFirstAddress();
		String newShippingAdrressName = TestConstants.NEW_ADDRESS_NAME_US+randomNum;
		String lastName = "test";
		storeFrontShippingInfoPage.enterNewShippingAddressName(newShippingAdrressName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(TestConstants.NEW_ADDRESS_LINE1_US);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(TestConstants.NEW_ADDRESS_CITY_US);
		storeFrontShippingInfoPage.selectNewShippingAddressState(TestConstants.NEW_ADDRESS_STATE_US);
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(TestConstants.NEW_ADDRESS_POSTAL_CODE_US);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.NEW_ADDRESS_PHONE_NUMBER_US);
		storeFrontShippingInfoPage.selectFirstCardNumber();
		storeFrontShippingInfoPage.enterNewShippingAddressSecurityCode(TestConstants.NEW_ADDRESS_SECURITY_NUMBER_US);
		storeFrontShippingInfoPage.selectUseThisShippingProfileFutureAutoshipChkbox();
		storeFrontShippingInfoPage.clickOnSaveShippingProfile();
		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingAdrressName), "New Shipping address is not selected listed on Shipping profile page");
		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickNextCRP();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditShipping();
		s_assert.assertTrue(storeFrontUpdateCartPage.isShippingAddressPresent(newShippingAdrressName), "New Shipping address NOT added to update cart");
		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		s_assert.assertAll();
		logout();

	}

	//  Hybris Phase 2-2040 :: Version : 1 :: Edit shipping address during CRP enrollment through my account 
	public void testEditShippingAddressDuringCRPEnrollmentMyAccount(){
		// TODO
	}

}
