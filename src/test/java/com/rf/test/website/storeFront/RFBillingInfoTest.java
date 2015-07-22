package com.rf.test.website.storeFront;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
		List<Map<String, Object>> billingAddressCountList =  null;
		List<Map<String, Object>> defaultBillingAddressList = null;
		List<Map<String, Object>> defaultBillingAddressProfileNameList = null;
		int totalBillingAddressesFromDB = 0;
		String firstName = null;
		String postalCode = null;
		String locale = null;
		String region = null;
		String country = null;
		String defaultBillingAddressFromDB =null;
		String defaultBillingAddressProfileNameDB = null;
		String address1 = null;
		
		storeFrontHomePage = new StoreFrontHomePage(driver);
		if(driver.getEnvName().equalsIgnoreCase("stg2")){
			billingAddressCountList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_BILLING_ADDRESS_COUNT_QUERY,TestConstants.CONSULTANT_EMAIL_ID_STG2));
			defaultBillingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_DEFAULT_BILLING_ADDRESS_QUERY,TestConstants.CONSULTANT_EMAIL_ID_STG2));
			defaultBillingAddressProfileNameList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_DEFAULT_BIILING_ADDRESS_PROFILE_NAME_QUERY,TestConstants.CONSULTANT_EMAIL_ID_STG2));
			totalBillingAddressesFromDB = (Integer) getValueFromQueryResult(billingAddressCountList, "count");

			firstName = (String) getValueFromQueryResult(defaultBillingAddressList, "AddressProfileName");
			address1 = (String) getValueFromQueryResult(defaultBillingAddressList, "AddressLine1");
			postalCode = (String) getValueFromQueryResult(defaultBillingAddressList, "PostalCode");
			locale = (String) getValueFromQueryResult(defaultBillingAddressList, "Locale");
			region = (String) getValueFromQueryResult(defaultBillingAddressList, "Region");
			country = (String) getValueFromQueryResult(defaultBillingAddressList, "CountryID");				
			if(country.equals("40")){
				country = "CANADA"; 
			}	
			defaultBillingAddressProfileNameDB = (String) getValueFromQueryResult(defaultBillingAddressProfileNameList, "ProfileName");
			defaultBillingAddressFromDB = firstName+"\n"+ address1+"\n"+locale+", "+region+" "+postalCode+"\n"+country;
			storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_STG2, TestConstants.CONSULTANT_PASSWORD_STG2);

		}
		else if (driver.getEnvName().equalsIgnoreCase("tst4")){			
			billingAddressCountList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_BILLING_ADDRESS_COUNT_QUERY_TST4,TestConstants.CONSULTANT_EMAIL_ID_TST4));
			defaultBillingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_DEFAULT_BILLING_ADDRESS_QUERY_TST4,TestConstants.CONSULTANT_EMAIL_ID_TST4));
			totalBillingAddressesFromDB = (Integer) getValueFromQueryResult(billingAddressCountList, "count");
			address1 = (String) getValueFromQueryResult(defaultBillingAddressList, "Address1");
			storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_TST4, TestConstants.CONSULTANT_PASSWORD_TST4);
		}				
		assertTrue("Consultant Page doesn't contain Welcome User Message",storeFrontConsulatantPage.verifyConsultantPage());
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsulatantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		assertTrue("Billing Info page has not been displayed", storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed());
		assertEquals("Billing Addresses count on UI is different from DB", totalBillingAddressesFromDB,storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed());
//		if(totalBillingAddressesFromDB>1){
//			assertTrue("Default radio button in Billing paged is not selected",storeFrontBillingInfoPage.isDefaultBillingAddressSelected(address1));	
//		}		
//		assertTrue("Default address on UI is different from DB",storeFrontBillingInfoPage.getDefaultBillingAddress().trim().equalsIgnoreCase(defaultBillingAddressFromDB.trim()));
	}

	//Hybris Phase 2-4223 :: Version : 1 :: Account with multiple payment profiles
	// ISSUE on TST4
	@Test(enabled=false)
	public void testBillingInfoPageDetails() throws SQLException, InterruptedException{
		int totalBillingAddressesFromDB = 0;
		List<Map<String, Object>> billingAddressCountList =  null;

		storeFrontHomePage = new StoreFrontHomePage(driver);
		
		if(driver.getEnvName().equalsIgnoreCase("stg2")){
			billingAddressCountList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_BILLING_ADDRESS_COUNT_QUERY,TestConstants.CONSULTANT_EMAIL_ID_STG2));
			totalBillingAddressesFromDB = (Integer) getValueFromQueryResult(billingAddressCountList, "count");
			
			storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_STG2, TestConstants.CONSULTANT_PASSWORD_STG2);
		}
		else if (driver.getEnvName().equalsIgnoreCase("tst4")){
			billingAddressCountList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_BILLING_ADDRESS_COUNT_QUERY_TST4,TestConstants.CONSULTANT_EMAIL_ID_TST4));
			totalBillingAddressesFromDB = (Integer) getValueFromQueryResult(billingAddressCountList, "count");
			
			storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_TST4, TestConstants.CONSULTANT_PASSWORD_TST4);
		}				
		assertTrue("Consultant Page doesn't contain Welcome User Message",storeFrontConsulatantPage.verifyConsultantPage());
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsulatantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		assertTrue("Billing Info page has not been displayed", storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed());
		assertEquals("Billing Addresses count on UI is different from DB", totalBillingAddressesFromDB,storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed());
	}

}

