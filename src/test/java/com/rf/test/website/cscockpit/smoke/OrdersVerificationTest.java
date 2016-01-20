package com.rf.test.website.cscockpit.smoke;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.CSCockpitHomePage;
import com.rf.pages.website.CSCockpitLoginPage;
import com.rf.test.website.RFWebsiteBaseTest;


public class OrdersVerificationTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(OrdersVerificationTest.class.getName());

	//-------------------------------------------------Pages---------------------------------------------------------
	private CSCockpitLoginPage cscockpitLoginPage;	
	private CSCockpitHomePage cscockpitHomePage; 
	//-----------------------------------------------------------------------------------------------------------------

	//Hybris Project-1942:To verify Adhoc retail order
	@Test
	public void testVerifyAdhocRetailOrder_1942(){
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("Retail");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.clickSearchBtn();
		s_assert.assertAll();		
	}
	
}
