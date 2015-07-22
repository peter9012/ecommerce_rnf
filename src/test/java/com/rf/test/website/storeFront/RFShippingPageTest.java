package com.rf.test.website.storeFront;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.DBQueries;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontShippingInfoPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.test.website.RFWebsiteBaseTest;

public class RFShippingPageTest extends RFWebsiteBaseTest{

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage consulatantPage;
	private StoreFrontShippingInfoPage storeFrontShippingInfoPage;

	//Hybris Phase 2-4326: View shipping address on 'Shipping Profile' page
	@Test
	public void testShippingAddressOnShippingProfile_HP2_4326() throws InterruptedException, SQLException{
		int totalShippingAddressesFromDB = 0;
		String defaultShippingAddressFirstNameDB=null;

		List<Map<String, Object>> shippingAddressCountList =  null;
		List<Map<String, Object>> defaultShippingAddressList =  null;
		String address1=null;

		storeFrontHomePage = new StoreFrontHomePage(driver);
		
		if(driver.getEnvName().equalsIgnoreCase("stg2")){
			shippingAddressCountList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_ADDRESS_COUNT_QUERY,TestConstants.CONSULTANT_EMAIL_ID_STG2));
			defaultShippingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_DEFAULT_BILLING_ADDRESS_QUERY,TestConstants.CONSULTANT_EMAIL_ID_STG2));
			address1 = (String) getValueFromQueryResult(defaultShippingAddressList, "AddressLine1");
			totalShippingAddressesFromDB = (Integer) getValueFromQueryResult(shippingAddressCountList, "count");			
			consulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_STG2, TestConstants.CONSULTANT_PASSWORD_STG2);	
		}
		else if (driver.getEnvName().equalsIgnoreCase("tst4")) {
			shippingAddressCountList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_ADDRESS_COUNT_QUERY_TST4,TestConstants.CONSULTANT_EMAIL_ID_TST4));
			defaultShippingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_DEFAULT_SHIPPING_ADDRESS_QUERY_TST4,TestConstants.CONSULTANT_EMAIL_ID_TST4));
			totalShippingAddressesFromDB = (Integer) getValueFromQueryResult(shippingAddressCountList, "count");
			address1 = (String) getValueFromQueryResult(defaultShippingAddressList, "Address1");			
			consulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_TST4, TestConstants.CONSULTANT_PASSWORD_TST4);
		}
		
		assertTrue("Consultant Page doesn't contain Welcome User Message",consulatantPage.verifyConsultantPage());
		consulatantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = consulatantPage.clickShippingLinkPresentOnWelcomeDropDown();
		assertTrue("shipping info page has not been displayed", storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed());
		assertEquals("Shipping Addresses count on UI is different from DB", totalShippingAddressesFromDB,storeFrontShippingInfoPage.getTotalShippingAddressesDisplayed());
		if(totalShippingAddressesFromDB > 1){				
			assertTrue("Default radio button in Shipping page is not selected", storeFrontShippingInfoPage.isDefaultShippingAddressSelected(address1));
		}
		assertTrue("Auto Ship Address Text is not present next to selected default address", storeFrontShippingInfoPage.verifyAutoShipAddressTextNextToDefaultRadioBtn(defaultShippingAddressFirstNameDB));
	}	
}

