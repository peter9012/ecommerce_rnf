package com.rf.test.website.storeFront;

import java.sql.ResultSet;
import java.sql.SQLException;

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
		ResultSet shippingAddressCount_resultSet = null;
		ResultSet defaultShippingAddressFirstName_resultSet = null;
		shippingAddressCount_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_SHIPPING_ADDRESS_COUNT_QUERY,TestConstants.CONSULTANT_EMAIL_ID));
		defaultShippingAddressFirstName_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_DEFAULT_SHIPPING_ADDRESS_FIRST_NAME_QUERY,TestConstants.CONSULTANT_EMAIL_ID));
		while (shippingAddressCount_resultSet.next()) {
			totalShippingAddressesFromDB = Integer.parseInt(shippingAddressCount_resultSet.getString("count"));				
		}
		while (defaultShippingAddressFirstName_resultSet.next()) {
			defaultShippingAddressFirstNameDB = defaultShippingAddressFirstName_resultSet.getString("FirstName");				
		}
		DBUtil.closeConnection();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		consulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID, TestConstants.CONSULTANT_PASSWORD);
		assertTrue("Consultant Page doesn't contain Welcome User Message",consulatantPage.verifyConsultantPage());
		consulatantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = consulatantPage.clickShippingLinkPresentOnWelcomeDropDown();
		assertTrue("shipping info page has not been displayed", storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed());
		assertEquals("Shipping Addresses count on UI is different from DB", totalShippingAddressesFromDB,storeFrontShippingInfoPage.getTotalShippingAddressesDisplayed());
		if(totalShippingAddressesFromDB > 1){
			assertTrue("Default radio button in Shipping page is not selected", storeFrontShippingInfoPage.isDefaultAddressRadioBtnSelected(defaultShippingAddressFirstNameDB));	
		}
		assertTrue("Auto Ship Address Text is not present next to selected default address", storeFrontShippingInfoPage.verifyAutoShipAddressTextNextToDefaultRadioBtn(defaultShippingAddressFirstNameDB));
	}	
}

