package com.rf.test.mobile.storeFront.hybris.orders;

import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.mobile.storeFront.StoreFrontConsultantPage;
import com.rf.pages.mobile.storeFront.StoreFrontHomePage;
import com.rf.pages.mobile.storeFront.StoreFrontOrdersPage;
import com.rf.pages.mobile.storeFront.StoreFrontPCUserPage;
import com.rf.pages.mobile.storeFront.StoreFrontUpdateCartPage;
import com.rf.pages.mobile.storeFront.StoreFrontConsultantPage;
import com.rf.test.mobile.RFStoreFrontMobileBaseTest;


public class OrderValidationTest extends RFStoreFrontMobileBaseTest{
	private static final Logger logger = LogManager
			.getLogger(OrderValidationTest.class.getName());
	public String emailID=null;
	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontConsultantPage storeFrontConsultantPage; 
	private String RFO_DB = null;
	String country=null;
	String env = null;

	
	// Hybris Phase 2-1877 :: Version : 1 :: Create Adhoc Order For The Preferred Customer 
	@Test
	public void testCreateAdhocOrderPC_1877() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();

		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("error");
			if(isSiteNotFoundPresent){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	

		logger.info("login is successful");
		storeFrontHomePage.clickRFMenuBars();
		storeFrontHomePage.clickShopSkinCareOnMenuBar();
		storeFrontHomePage.clickAllProductsLink();
		storeFrontHomePage.clickAddToBagButton(country);
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		storeFrontHomePage.clickOnCheckoutButton();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnDefaultBillingProfileEdit();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");
		s_assert.assertAll();
	}
	
}