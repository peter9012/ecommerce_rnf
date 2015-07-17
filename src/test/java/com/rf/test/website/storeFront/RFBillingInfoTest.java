package com.rf.test.website.storeFront;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.testng.annotations.Test;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.DBQueries;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.StoreFrontBillingInfoPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.test.website.RFWebsiteBaseTest;

public class RFBillingInfoTest extends RFWebsiteBaseTest{

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsulatantPage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;
	
	//Hybris Phase 2-4327:View new billing profile on 'Billing Profile' page	
	@Test
	public void testViewNewBillingProfile_HP2_4327() throws InterruptedException, SQLException{
		int totalBillingAddressesFromDB = 0;
		ResultSet billingAddressCount_resultSet = null;
		ResultSet defaultBillingAddress_resultSet = null;
		ResultSet defaultBillingAddressProfileName_resultSet = null;
		String firstName = null;
		String addressLine1 = null;
		String postalCode = null;
		String locale = null;
		String region = null;
		String country = null;
		String defaultBillingAddressFromDB =null;
		String defaultBillingAddressProfileNameDB = null;

		billingAddressCount_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_BILLING_ADDRESS_COUNT_QUERY,TestConstants.CONSULTANT_EMAIL_ID));
		defaultBillingAddress_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_DEFAULT_BILLING_ADDRESS_QUERY,TestConstants.CONSULTANT_EMAIL_ID));
		defaultBillingAddressProfileName_resultSet = DBUtil.performSelectQuery(DBQueries.callQueryWithArguement(DBQueries.GET_DEFAULT_BIILING_ADDRESS_PROFILE_NAME_QUERY,TestConstants.CONSULTANT_EMAIL_ID));
		while (billingAddressCount_resultSet.next()) {
			totalBillingAddressesFromDB = Integer.parseInt(billingAddressCount_resultSet.getString("count"));				
		}

		while(defaultBillingAddress_resultSet.next()){
			firstName = defaultBillingAddress_resultSet.getString("AddressProfileName");
			addressLine1 = defaultBillingAddress_resultSet.getString("AddressLine1");
			postalCode = defaultBillingAddress_resultSet.getString("PostalCode");
			locale = defaultBillingAddress_resultSet.getString("Locale");
			region = defaultBillingAddress_resultSet.getString("Region");
			country = defaultBillingAddress_resultSet.getString("CountryID");
			if(country.equals("40")){
				country = "CANADA"; 
			}			
		}
		while (defaultBillingAddressProfileName_resultSet.next()) {
			defaultBillingAddressProfileNameDB = defaultBillingAddressProfileName_resultSet.getString("ProfileName");				
		}
		DBUtil.closeConnection();
		defaultBillingAddressFromDB = firstName+"\n"+ addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID, TestConstants.CONSULTANT_PASSWORD);
		assertTrue("Consultant Page doesn't contain Welcome User Message",storeFrontConsulatantPage.verifyConsultantPage());
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsulatantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		assertTrue("Billing Info page has not been displayed", storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed());
		assertEquals("Billing Addresses count on UI is different from DB", totalBillingAddressesFromDB,storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed());
		if(totalBillingAddressesFromDB>1){
			assertTrue("Default radio button in Billing paged is not selected",storeFrontBillingInfoPage.isDefaultAddressRadioBtnSelected(defaultBillingAddressProfileNameDB));	
		}		
		assertTrue("Default address on UI is different from DB",storeFrontBillingInfoPage.getDefaultBillingAddress().trim().equalsIgnoreCase(defaultBillingAddressFromDB.trim()));
	}
	
	
	
	
}

