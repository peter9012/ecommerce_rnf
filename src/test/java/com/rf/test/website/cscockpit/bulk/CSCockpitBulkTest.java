package com.rf.test.website.cscockpit.bulk;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.rf.core.driver.website.RFWebsiteDriver;
import com.rf.core.utils.SoftAssert;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.CSCockpitHomePage;
import com.rf.pages.website.CSCockpitLoginPage;
import com.rf.pages.website.RFWebsiteBasePage;
import com.rf.test.website.RFWebsiteBaseTest;


public class CSCockpitBulkTest extends RFWebsiteBaseTest{

	private static final Logger logger = LogManager
			.getLogger(CSCockpitBulkTest.class.getName());
	private CSCockpitLoginPage cscockpitLoginPage;	
	private CSCockpitHomePage cscockpitHomePage; 

	@BeforeMethod
	public void loginToCSCockpit(){		
		cscockpitLoginPage = new CSCockpitLoginPage(driver);		
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
	}

	@Test(dataProvider="rfTestData")
	public void testBulkReturnOrders(String returnOrderNumber){	
		logger.info("RETURN ORDER NUMBER IS "+returnOrderNumber);
		cscockpitHomePage.clickOrderSearchTab();
		cscockpitHomePage.enterOrderNumberInOrderSearchTab(returnOrderNumber);
		cscockpitHomePage.clickSearchBtn();
		s_assert.assertTrue(cscockpitHomePage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(returnOrderNumber)>0, "Order was NOT placed successfully,expected count after placing order in order detail items section >0 but actual count on UI = "+cscockpitHomePage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder());
		cscockpitHomePage.clickRefundOrderBtnOnOrderTab();
		cscockpitHomePage.checkReturnCompleteOrderChkBoxOnRefundPopUpAndReturnTrueElseFalse();
		cscockpitHomePage.selectRefundReasonOnRefundPopUp("Test");
		cscockpitHomePage.selectFirstReturnActionOnRefundPopUp();
		cscockpitHomePage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitHomePage.clickCreateBtnOnRefundPopUp();
		cscockpitHomePage.clickConfirmBtnOnConfirmPopUp();
		cscockpitHomePage.clickOKBtnOnRMAPopUp();
		cscockpitHomePage.clickOrderSearchTab();
		s_assert.assertTrue(cscockpitHomePage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(returnOrderNumber)>0, "Order Details Section has not been reached");
		cscockpitHomePage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitHomePage.isNoRefundableItemsTxtPresent(), "Order Number = "+returnOrderNumber+" has NOT refund Successfully");
		s_assert.assertAll();		
	} 

}
