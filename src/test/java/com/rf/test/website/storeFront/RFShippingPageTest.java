package com.rf.test.website.storeFront;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.DBQueries;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.StoreFrontCartAutoShipPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontShippingInfoPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class RFShippingPageTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(RFShippingPageTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontShippingInfoPage storeFrontShippingInfoPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;

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
		assertTrue("Consultant Page doesn't contain Welcome User Message",storeFrontConsultantPage.verifyConsultantPage());
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		assertTrue("shipping info page has not been displayed", storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed());

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
	}	

	// Hybris Phase 2-2029 :: Version : 1 :: Add shipping address on 'Shipping Profile' page 
	// WIP
	@Test(enabled=false)
	public void testAddNewShippingAddressOnShippingProfilePage() throws InterruptedException, SQLException{

		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();		
		int totalShippingAddressesFromDB = 0;
		List<Map<String, Object>> shippingAddressCountList =  null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_TST4, TestConstants.CONSULTANT_PASSWORD_TST4);
		assertTrue("Consultant Page doesn't contain Welcome User Message",storeFrontConsultantPage.verifyConsultantPage());
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		assertTrue("shipping info page has not been displayed", storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed());
		storeFrontShippingInfoPage.clickAddNewShippingProfileLink();
		storeFrontShippingInfoPage.enterNewShippingAddressName("");
		storeFrontShippingInfoPage.enterNewShippingAddressLine1("");
		storeFrontShippingInfoPage.enterNewShippingAddressCity("");
		storeFrontShippingInfoPage.selectNewShippingAddressState("");
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode("");
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber("");
		storeFrontShippingInfoPage.selectCardNumber("");
		storeFrontShippingInfoPage.enterNewShippingAddressSecurityCode("");
		storeFrontShippingInfoPage.selectUseThisShippingProfileFutureAutoshipChkbox();
		storeFrontShippingInfoPage.clickOnSaveShippingProfile();
		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickNextCRP();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
	}

}

