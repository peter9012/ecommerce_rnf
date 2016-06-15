package com.rf.test.mobile.storeFront.hybris.billingAndShipping;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.mobile.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.mobile.storeFront.StoreFrontCartAutoShipPage;
import com.rf.pages.mobile.storeFront.StoreFrontConsultantPage;
import com.rf.pages.mobile.storeFront.StoreFrontHomePage;
import com.rf.pages.mobile.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.mobile.RFStoreFrontMobileBaseTest;

public class AddBillingTest extends RFStoreFrontMobileBaseTest{
	private static final Logger logger = LogManager
			.getLogger(AddBillingTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontConsultantPage storeFrontConsultantPage;

	private String country = null;
	private String RFO_DB = null;
	private int randomNum; 	
	List<Map<String, Object>> randomConsultantList =  null;
	String consultantEmailID = null;
	String accountID = null;

	@BeforeClass
	public void setupDataForAddBilling() throws InterruptedException{	
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontBillingInfoPage = new StoreFrontBillingInfoPage(driver);
		storeFrontCartAutoShipPage = new StoreFrontCartAutoShipPage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO(); 
		country = driver.getCountry();

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			//			storeFrontHomePage.clickRodanAndFieldsLogo();
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else{
				storeFrontConsultantPage.clickOnWelcomeDropDown();
				if(storeFrontHomePage.isEditCRPLinkPresent()==true)
					break;
				else
					driver.get(driver.getURL());				
			}

		}
		logger.info("login is successful");		
	}

	// Hybris Phase 2-2041 :: Version : 1 :: Add new billing profile on 'Billing Profile' page
	@Test(priority=1)
	public void testAddNewBillingProfileOnBillingProfilePage_2041() throws InterruptedException, SQLException{
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		String defaultBillingProfileName = null;
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
		int totalBillingProfiles = storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed();
		if(totalBillingProfiles>1){
			defaultBillingProfileName = storeFrontBillingInfoPage.getDefaultSelectedBillingAddressName();
		}
		else{
			defaultBillingProfileName = storeFrontBillingInfoPage.getFirstBillingProfileName();
		}
		logger.info("Default Billing profile is "+defaultBillingProfileName);
		storeFrontBillingInfoPage.clickAddNewBillingProfileLink();
		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontBillingInfoPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontBillingInfoPage.selectNewBillingCardExpirationDate(TestConstants.CARD_EXP_MONTH, TestConstants.CARD_EXP_YEAR);
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.selectNewBillingCardAddress();
		storeFrontBillingInfoPage.selectUseThisBillingProfileFutureAutoshipChkbox();
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();

		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly added Billing profile is NOT listed on the billing info page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		s_assert.assertFalse(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(newBillingProfileName),"Newly created billing profile is DEFAULT selected on the billing info page");
		s_assert.assertTrue(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(defaultBillingProfileName),"Old Default billing profile is not DEFAULT selected on the billing info page");

		//storeFrontHomePage.clickRFStamp();
		storeFrontHomePage.clickRFMenuBars();
		storeFrontHomePage.clickShopSkinCareOnMenuBar();
		storeFrontHomePage.clickAllProductsLink();
		storeFrontHomePage.clickAddToBagButton(country);
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		storeFrontHomePage.clickOnCheckoutButton();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly created Billing profile is NOT listed on the Adhoc cart");
		s_assert.assertTrue(storeFrontHomePage.isBillingProfileIsSelectedByDefault(defaultBillingProfileName),"Old Default billing profile is not DEFAULT selected on the Adhoc cart");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();

		//------------------ Verify that CRP/PC cart contains the newly created billing profile address as selected ------------------------------------------------------------

		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(newBillingProfileName),"New Billing Profile is not selected by default on autoship cart page");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		s_assert.assertAll();  
	}

}
