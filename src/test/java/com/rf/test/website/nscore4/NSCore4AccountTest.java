package com.rf.test.website.nscore4;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.dbQueries.DBQueries_RFL;
import com.rf.pages.website.nscore.NSCore3HomePage;
import com.rf.pages.website.nscore.NSCore3LoginPage;
import com.rf.pages.website.nscore.NSCore4HomePage;
import com.rf.pages.website.nscore.NSCore4LoginPage;
import com.rf.test.website.RFNSCoreWebsiteBaseTest;

public class NSCore4AccountTest extends RFNSCoreWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(NSCore4AccountTest.class.getName());

	private NSCore4HomePage nscore4HomePage;
	private NSCore4LoginPage nscore4LoginPage;
	String RFL_DB = null;

	public NSCore4AccountTest() {
		nscore4HomePage = new NSCore4HomePage(driver);		
	}

	//NSC4_AdministratorLogin_LogingLogout
	@Test
	public void testAdministrationLoginLogout(){
		s_assert.assertTrue(nscore4HomePage.isLogoutLinkPresent(),"Home Page has not appeared after login");
		nscore4LoginPage = nscore4HomePage.clickLogoutLink();		
		s_assert.assertTrue(nscore4LoginPage.isLoginButtonPresent(),"Login page has not appeared after logout");
		s_assert.assertAll();
	}

	//NSC4_AccountsTab_AccountLookup
	@Test
	public void testAccountsTabAccountLookup(){
		String accountNumber = null;
		String firstName = null;
		String lastName = null;
		List<Map<String, Object>> randomAccountList =  null;
		RFL_DB = driver.getDBNameRFL();
		logger.info("DB is "+RFL_DB);
		randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACCOUNT_DETAILS,RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber");	
		firstName = (String) getValueFromQueryResult(randomAccountList, "FirstName");	
		lastName = (String) getValueFromQueryResult(randomAccountList, "LastName");	
		logger.info("Account number from DB is "+accountNumber);
		logger.info("First name from DB is "+firstName);
		logger.info("Last name from DB is "+lastName);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		s_assert.assertTrue(nscore4HomePage.isFirstAndLastNamePresentinSearchResults(firstName, lastName), "First and last name is not present in search result");
		s_assert.assertAll();
	}

	//NSC4_AccountsTab_OverviewAutoshipsEdit
	@Test
	public void testAccountsTabOverviewAutoshipsEdit(){
		String accountNumber = null;
		List<Map<String, Object>> randomAccountList =  null;
		List<Map<String, Object>> randomSKUList =  null;
		String SKU = null;
		RFL_DB = driver.getDBNameRFL();
		logger.info("DB is "+RFL_DB);
		randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber");	
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);		
		nscore4HomePage.clickConsultantReplenishmentEdit();
		randomSKUList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_SKU,RFL_DB);
		SKU = (String) getValueFromQueryResult(randomSKUList, "SKU");
		logger.info("SKUfrom DB is "+SKU);
		nscore4HomePage.enterSKUValue(SKU);
		nscore4HomePage.clickFirstSKUSearchResultOfAutoSuggestion();
		nscore4HomePage.enterProductQuantityAndAddToOrder("10");
		s_assert.assertTrue(nscore4HomePage.isProductAddedToOrder(SKU), "SKU = "+SKU+" is not added to the Autoship Order");
		nscore4HomePage.clickSaveAutoshipTemplate();
		s_assert.assertTrue(nscore4HomePage.isAddedProductPresentInOrderDetailPage(SKU), "SKU = "+SKU+" is not present in the Order detail page");
		nscore4HomePage.clickCustomerlabelOnOrderDetailPage();
		nscore4HomePage.clickPulseMonthlySubscriptionEdit();
		String updatedQuantity = nscore4HomePage.updatePulseProductQuantityAndReturnValue();
		logger.info("updated pulse product quantity = "+updatedQuantity);
		nscore4HomePage.clickSaveAutoshipTemplate();
		s_assert.assertTrue(nscore4HomePage.getQuantityOfPulseProductFromOrderDetailPage().contains(updatedQuantity), "updated pulse product qunatity is not present in the Order detail page");
		s_assert.assertAll();
	}

}
