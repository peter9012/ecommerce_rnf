package com.rf.test.website.storeFront.account;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.DBQueries;
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
		storeFrontShippingInfoPage.clickAddNewShippingProfileLink();
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
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);		
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickNextCRP();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditShipping();
		storeFrontUpdateCartPage.clickAddNewShippingProfileLink();
		String newShippingAdrressName = TestConstants.NEW_ADDRESS_NAME_US+randomNum;
		String lastName = "test";
		storeFrontUpdateCartPage.enterNewShippingAddressName(newShippingAdrressName+" "+lastName);
		storeFrontUpdateCartPage.enterNewShippingAddressLine1(TestConstants.NEW_ADDRESS_LINE1_US);
		storeFrontUpdateCartPage.enterNewShippingAddressCity(TestConstants.NEW_ADDRESS_CITY_US);
		storeFrontUpdateCartPage.selectNewShippingAddressState(TestConstants.NEW_ADDRESS_STATE_US);
		storeFrontUpdateCartPage.enterNewShippingAddressPostalCode(TestConstants.NEW_ADDRESS_POSTAL_CODE_US);
		storeFrontUpdateCartPage.enterNewShippingAddressPhoneNumber(TestConstants.NEW_ADDRESS_PHONE_NUMBER_US);
		storeFrontUpdateCartPage.clickOnSaveShippingProfile();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyNewShippingAddressSelectedOnUpdateCart(newShippingAdrressName), "New Shipping address is not selected by default on cart page");
		storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtn();
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		storeFrontUpdateCartPage.acceptNewShippingAddress();		
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingAdrressName), "New Shipping address is not selected listed on Shipping profile page");
		logout();
		s_assert.assertAll();
	}

}

