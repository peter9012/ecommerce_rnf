package com.rf.test.website.nscore4;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.utils.ExcelReader;
import com.rf.core.website.constants.TestConstantsRFL;
import com.rf.core.website.constants.dbQueries.DBQueries_RFL;
import com.rf.pages.website.nscore.NSCore4AdminPage;
import com.rf.pages.website.nscore.NSCore4HomePage;
import com.rf.pages.website.nscore.NSCore4LoginPage;
import com.rf.pages.website.nscore.NSCore4MobilePage;
import com.rf.pages.website.nscore.NSCore4OrdersTabPage;
import com.rf.pages.website.nscore.NSCore4ProductsTabPage;
import com.rf.pages.website.nscore.NSCore4SitesTabPage;
import com.rf.test.website.RFNSCoreWebsiteBaseTest;

public class NSCore4AccountTest extends RFNSCoreWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(NSCore4AccountTest.class.getName());

	private NSCore4HomePage nscore4HomePage;
	private NSCore4LoginPage nscore4LoginPage;
	private NSCore4MobilePage nscore4MobilePage;
	private NSCore4OrdersTabPage nscore4OrdersTabPage;
	private NSCore4SitesTabPage nscore4SitesTabPage;
	private NSCore4AdminPage nscore4AdminPage;
	private NSCore4ProductsTabPage nscore4ProductsTabPage;
	String RFL_DB = null;
	List<Map<String, Object>> randomAccountList =  null;

	public NSCore4AccountTest() {
		nscore4HomePage = new NSCore4HomePage(driver);
		nscore4SitesTabPage = new NSCore4SitesTabPage(driver);
		nscore4ProductsTabPage = new NSCore4ProductsTabPage(driver);
		nscore4OrdersTabPage = new NSCore4OrdersTabPage(driver);
		nscore4AdminPage = new NSCore4AdminPage(driver);
	}

	@BeforeClass
	public void executeCommonQuery(){
		RFL_DB = driver.getDBNameRFL();
		randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS,RFL_DB);
	}

	//NSC4_AdministratorLogin_LogingLogout
	@Test(priority=1)
	public void testAdministrationLoginLogout(){
		s_assert.assertTrue(nscore4HomePage.isLogoutLinkPresent(),"Home Page has not appeared after login");
		nscore4LoginPage = nscore4HomePage.clickLogoutLink();		
		s_assert.assertTrue(nscore4LoginPage.isLoginButtonPresent(),"Login page has not appeared after logout");
		login("admin", "skin123!");
		s_assert.assertAll();
	}

	//NSC4_AdministratorLogin_InvalidLoging
	@Test(priority=2)
	public void testAdministratorLoginInvalidLogin(){
		nscore4LoginPage = nscore4HomePage.clickLogoutLink();	
		login("abcd", "test1234!");
		s_assert.assertTrue(nscore4LoginPage.isLoginCredentailsErrorMsgPresent(), "Login credentials error msg is not displayed");
		login("admin", "skin123!");
		s_assert.assertAll();
	}

	//NSC4_AccountsTab_AccountLookup
	@Test(priority=3)
	public void testAccountsTabAccountLookup(){
		String accountNumber = null;
		String firstName = null;
		String lastName = null;
		logger.info("DB is "+RFL_DB);
		randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACCOUNT_DETAILS,RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber");	
		firstName = (String) getValueFromQueryResult(randomAccountList, "FirstName");	
		lastName = (String) getValueFromQueryResult(randomAccountList, "LastName");	
		logger.info("Account number from DB is "+accountNumber);
		logger.info("First name from DB is "+firstName);
		logger.info("Last name from DB is "+lastName);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		s_assert.assertTrue(nscore4HomePage.isFirstAndLastNamePresentinSearchResults(firstName, lastName), "First and last name is not present in search result,when searched with account number");
		nscore4HomePage.enterAccountNumberInAccountSearchField(firstName+" "+lastName);
		s_assert.assertTrue(nscore4HomePage.isFirstAndLastNamePresentinSearchResults(firstName, lastName), "First and last name is not present in search result,when searched with first & last name");
		s_assert.assertAll();
	}

	//NSC4_AccountsTab_OverviewAutoshipsEdit
	@Test(priority=4)
	public void testAccountsTabOverviewAutoshipsEdit(){
		String accountNumber = null;  
		String SKU = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = "RFAutoNSCore4"+randomNum;
		String lastName = "lN";
		String nameOnCard = "rfTestUser";
		String cardNumber =  "4747474747474747";
		logger.info("DB is "+RFL_DB); 
		do{
			accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
			logger.info("Account number from DB is "+accountNumber);
			nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
			nscore4HomePage.clickGoBtnOfSearch(accountNumber);  
			if(nscore4HomePage.isEditPresentForConsultantReplenishmentPresent() && nscore4HomePage.isEditPresentForPulseMonthlySubscriptionPresent()==true){
				break;
			}else{
				randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
				nscore4HomePage.clickTab("Accounts");
			}
		}while(true);
		nscore4HomePage.clickConsultantReplenishmentEdit();
		SKU= nscore4HomePage.addAndGetProductSKU("10");
		s_assert.assertTrue(nscore4HomePage.isProductAddedToOrder(SKU), "SKU = "+SKU+" is not added to the Autoship Order");
		//nscore4HomePage.addPaymentMethod(newBillingProfileName, lastName, nameOnCard, cardNumber);
		//nscore4HomePage.clickSavePaymentMethodBtn();
		nscore4HomePage.clickSaveAutoshipTemplate();
		s_assert.assertTrue(nscore4HomePage.isAddedProductPresentInOrderDetailPage(SKU), "SKU = "+SKU+" is not present in the Order detail page");
		nscore4HomePage.clickCustomerlabelOnOrderDetailPage();
		nscore4HomePage.clickPulseMonthlySubscriptionEdit();
		String updatedQuantity = nscore4HomePage.updatePulseProductQuantityAndReturnValue();
		logger.info("updated pulse product quantity = "+updatedQuantity);
		//nscore4HomePage.addPaymentMethod(newBillingProfileName, lastName, nameOnCard, cardNumber);
		//nscore4HomePage.clickSavePaymentMethodBtn();
		nscore4HomePage.clickSaveAutoshipTemplate();
		s_assert.assertTrue(nscore4HomePage.getQuantityOfPulseProductFromOrderDetailPage().contains(updatedQuantity), "updated pulse product qunatity is not present in the Order detail page");
		s_assert.assertAll();
	}

	//NSC4_MobileTab_ HeadlineNews
	@Test(priority=5)
	public void testMobileTabHeadLineNews(){
		nscore4MobilePage=nscore4HomePage.clickMobileTab();
		//click headlines news link
		nscore4MobilePage.clickHeadLineNewsLink();
		//verify 'Browse HeadLine News' Page comes up?
		s_assert.assertTrue(nscore4MobilePage.validateBrowseHeadLineNewsPage(), "Browse 'HeadLineNews' Page is not displayed");
		s_assert.assertAll();
	}

	//NSC4_MobileTab_ R+FInTheNews
	@Test(priority=6)
	public void testMobileTabRFInNews(){
		nscore4MobilePage=nscore4HomePage.clickMobileTab();
		//click R+F In the news link
		nscore4MobilePage.clickRFInNewsLink();
		//verify 'Browse RF In News' Page comes up?
		s_assert.assertTrue(nscore4MobilePage.validateBrowseRFInNewsPage(), "Browse 'RF In News' Page is not displayed");
		s_assert.assertAll();
	}

	//NSC4_AccountsTab_OrdersEditCancelOrder
	@Test(priority=7)
	public void testAccountsTab_OrdersEditCancelOrder(){
		String accountNumber = null;
		List<Map<String, Object>> randomAccountList =  null;
		logger.info("DB is "+RFL_DB);
		while(true){
			randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
			accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
			logger.info("Account number from DB is "+accountNumber);
			nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
			nscore4HomePage.clickGoBtnOfSearch(accountNumber);
			nscore4HomePage.selectOrderStatusByDropDown("Pending");
			if(nscore4HomePage.isNoOrderFoundMessagePresent())
				continue;
			else
				break;
		}
		nscore4OrdersTabPage = nscore4HomePage.clickPendingOrderID();
		nscore4OrdersTabPage.clickCancelOrderBtn();
		s_assert.assertTrue(nscore4OrdersTabPage.validateOrderStatus(),"order is not cancelled");
		s_assert.assertAll();
	}

	//NSC4_OrdersTab_OrderIdSearch
	@Test(priority=8)
	public void testOrdersTab_OrderIdSearch(){
		String accountNumber = null;
		logger.info("DB is "+RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);
		String orderID = nscore4HomePage.getOrderIDFromOverviewPage();
		nscore4OrdersTabPage = nscore4HomePage.clickOrdersTab();
		nscore4OrdersTabPage.enterOrderIDInInputField(orderID);
		s_assert.assertTrue(nscore4OrdersTabPage.isOrderDetailPagePresent(),"This is not order details page");
		s_assert.assertTrue(nscore4OrdersTabPage.isOrderInformationPresent("Products"),"Product information not present as expected");
		s_assert.assertTrue(nscore4OrdersTabPage.isOrderInformationPresent("Shipments"),"Shipments information not present as expected");
		s_assert.assertTrue(nscore4OrdersTabPage.isOrderInformationPresent("Payment"),"Payment information not present as expected");
		s_assert.assertAll();
	}

	//NSC4_OrdersTab_OrderAdvancedSearch
	@Test(priority=9)
	public void testOrdersTab_OrderAdvancedSearch(){
		String accountNumber = null;
		List<Map<String, Object>> completeNameList =  null;
		logger.info("DB is "+RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
		logger.info("Account number from DB is "+accountNumber);
		completeNameList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_SPONSER_DETAILS_FROM_SPONSER_ACCOUNT_NUMBER, accountNumber),RFL_DB);
		String firstNameDB = String.valueOf(getValueFromQueryResult(completeNameList, "FirstName"));
		String lastNameDB = String.valueOf(getValueFromQueryResult(completeNameList, "LastName"));
		String completeNameDB = firstNameDB+" "+lastNameDB;
		logger.info("Complete name from DB is: "+completeNameDB);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);
		nscore4OrdersTabPage = nscore4HomePage.clickOrdersTab();
		nscore4OrdersTabPage.selectDropDownAdvancedSearch("Customer Account");
		nscore4OrdersTabPage.enterValueInAdvancedSearchInputField(accountNumber);
		String completeNameFromUI = nscore4OrdersTabPage.getCompleteNameFromFirstRow();
		s_assert.assertTrue(completeNameDB.equalsIgnoreCase(completeNameFromUI), "Expected complete name is: "+"completeNameDB+ actual on UI is: "+completeNameFromUI);
		s_assert.assertAll();
	}

	//NSC4_SitesTab_nsCorporate_CorporatePWSContentReviewApprove
	@Test(priority=10)
	public void testCorporatePWSContentReviewApprove() {
		int randomNumb = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String name= "RFTest QA";
		String linkName = "Product Focus";
		String storyTitle = "This is story title: "+randomNumb;
		String story = "This is new Automation story: "+randomNumber;

		logger.info("DB is "+RFL_DB);
		nscore4HomePage.enterAccountNumberInAccountSearchField(name);
		nscore4HomePage.clickGoBtnOfSearch(); 
		int rows = nscore4HomePage.getCountOfSearchResultRows();
		int randomNum = CommonUtils.getRandomNum(1,rows);
		nscore4HomePage.clickAndReturnAccountnumber(randomNum);
		nscore4HomePage.clickProxyLink(linkName);
		nscore4HomePage.switchToSecondWindow();
		s_assert.assertTrue(driver.getCurrentUrl().contains("myrfo"+driver.getEnvironment()+".com"), "User is not on dot com pws after clicking product focus proxy link.");
		nscore4HomePage.clickHeaderLinkAfterLogin("edit my pws");
		nscore4HomePage.clickEditMyStoryLink();
		nscore4HomePage.clickIWantToWriteMyOwnStory();
		nscore4HomePage.typeSomeStoryAndclickSaveStory(storyTitle,story);
		s_assert.assertTrue(nscore4HomePage.verifyWaitingForApprovalLinkForNewStory(storyTitle),"Waiting for approval link is not present for newly created story");
		nscore4HomePage.switchToPreviousTab();
		nscore4HomePage.clickTab("Sites");
		nscore4SitesTabPage.clickPWSContentReviewLinkUnderNSCorporate();
		s_assert.assertTrue(nscore4SitesTabPage.verifyNewStoryWaitingForApprovedLink(story),"Waiting for approved link is not present for newly created story");
		nscore4SitesTabPage.clickApproveLinkForNewStory(story);
		s_assert.assertTrue(nscore4SitesTabPage.verifyApproveRequestDisappearFromUIOnceStoryApproved(story),"Approve request still appears in UI once Approved");
		nscore4SitesTabPage.clickTab("Accounts");
		nscore4HomePage.enterAccountNumberInAccountSearchField(name);
		nscore4HomePage.clickGoBtnOfSearch(); 
		nscore4HomePage.clickAndReturnAccountnumber(randomNum);
		nscore4HomePage.clickProxyLink(linkName);
		nscore4HomePage.switchToSecondWindow();
		nscore4HomePage.clickHeaderLinkAfterLogin("edit my pws");
		s_assert.assertTrue(nscore4HomePage.verifyNewlyCreatedStoryIsUpdated(story),"New Story displayed on legacy is not the newly created");
		nscore4HomePage.switchToPreviousTab();
		s_assert.assertAll();
	}

	//NSC4_SitesTab_nsCorporate_CorporatePWSContentReviewDenied
	@Test(priority=11)
	public void testCorporatePWSContentReviewDenied(){
		int randomNumb = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumbers = CommonUtils.getRandomNum(10000, 1000000);
		String name= "RFTest QA";
		String linkName = "Product Focus";
		String storyTitle = "This is story title: "+randomNumb;
		String story = "This is new Automation story: "+randomNumber;
		String denyReason = "This is automation deny: "+randomNumbers;

		logger.info("DB is "+RFL_DB);
		nscore4HomePage.enterAccountNumberInAccountSearchField(name);
		nscore4HomePage.clickGoBtnOfSearch(); 
		int rows = nscore4HomePage.getCountOfSearchResultRows();
		int randomNum = CommonUtils.getRandomNum(1,rows);
		nscore4HomePage.clickAndReturnAccountnumber(randomNum);
		nscore4HomePage.clickProxyLink(linkName);
		nscore4HomePage.switchToSecondWindow();
		s_assert.assertTrue(driver.getCurrentUrl().contains("myrfo"+driver.getEnvironment()+".com"), "User is not on dot com pws after clicking product focus proxy link.");
		nscore4HomePage.clickHeaderLinkAfterLogin("edit my pws");
		nscore4HomePage.clickEditMyStoryLink();
		nscore4HomePage.clickIWantToWriteMyOwnStory();
		nscore4HomePage.typeSomeStoryAndclickSaveStory(storyTitle,story);
		s_assert.assertTrue(nscore4HomePage.verifyWaitingForApprovalLinkForNewStory(storyTitle),"Waiting for approval link is not present for newly created story");
		nscore4HomePage.switchToPreviousTab();
		nscore4HomePage.clickTab("Sites");
		nscore4SitesTabPage.clickPWSContentReviewLinkUnderNSCorporate();
		s_assert.assertTrue(nscore4SitesTabPage.verifyNewStoryWaitingForApprovedLink(story),"Waiting for approved link is not present for newly created story");
		nscore4SitesTabPage.clickDenyLinkForNewStory(story);
		nscore4SitesTabPage.enterDenyReasonAndClickSubmit(denyReason);
		s_assert.assertTrue(nscore4SitesTabPage.verifyApproveRequestDisappearFromUIOnceStoryApproved(story),"Approve request still appears in UI once Approved");
		nscore4SitesTabPage.clickTab("Accounts");
		nscore4HomePage.enterAccountNumberInAccountSearchField(name);
		nscore4HomePage.clickGoBtnOfSearch(); 
		nscore4HomePage.clickAndReturnAccountnumber(randomNum);
		nscore4HomePage.clickProxyLink(linkName);
		nscore4HomePage.switchToSecondWindow();
		nscore4HomePage.clickHeaderLinkAfterLogin("edit my pws");
		nscore4HomePage.clickEditMyStoryLink();
		s_assert.assertTrue(nscore4HomePage.getStoryDeniedText(storyTitle).contains("not approved"),"Story denied txt expected =(not approved)"+"while on UI"+nscore4HomePage.getStoryDeniedText(storyTitle));
		nscore4HomePage.switchToPreviousTab();
		s_assert.assertAll();
	}

	//NSC4_OrdersTab_NewOrder
	@Test(priority=12)
	public void testOrdersTab_NewOrder(){
		String accountNumber = null;
		List<Map<String, Object>> completeNameList =  null;
		String SKU = null;
		logger.info("DB is "+RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);
		nscore4OrdersTabPage = nscore4HomePage.clickOrdersTab();
		nscore4OrdersTabPage.clickStartANewOrderLink();
		completeNameList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_SPONSER_DETAILS_FROM_SPONSER_ACCOUNT_NUMBER, accountNumber),RFL_DB);
		String firstNameDB = String.valueOf(getValueFromQueryResult(completeNameList, "FirstName"));
		String lastNameDB = String.valueOf(getValueFromQueryResult(completeNameList, "LastName"));
		String completeNameDB = firstNameDB+" "+lastNameDB;
		logger.info("Complete name from DB is: "+completeNameDB);
		nscore4OrdersTabPage.enterAccountNameAndClickStartOrder(completeNameDB, accountNumber);
		SKU = nscore4HomePage.addAndGetProductSKU("1");
		s_assert.assertTrue(nscore4HomePage.isProductAddedToOrder(SKU), "SKU = "+SKU+" is not added to the Autoship Order");
		nscore4OrdersTabPage.clickPaymentApplyLink();
		nscore4OrdersTabPage.clickSubmitOrderBtn();
		s_assert.assertTrue(nscore4OrdersTabPage.isOrderInformationPresent("Products"),"Product information not present as expected");
		s_assert.assertTrue(nscore4OrdersTabPage.isOrderInformationPresent("Shipments"),"Shipments information not present as expected");
		s_assert.assertTrue(nscore4OrdersTabPage.isOrderInformationPresent("Payment"),"Payment information not present as expected");
		s_assert.assertTrue(nscore4OrdersTabPage.validateOrderStatusAfterSubmitOrder(),"Order is not submitted after submit order");
		s_assert.assertTrue(nscore4OrdersTabPage.isOrderDetailPagePresent(),"This is not order details page");
		s_assert.assertAll();
	}

	//NSC4_OrdersTab_BrowseOrders
	@Test(priority=13)
	public void testOrdersTabBrowseOrders(){
		String accountNumber = null;
		logger.info("DB is "+RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);
		nscore4OrdersTabPage = nscore4HomePage.clickOrdersTab();
		//click Browse Orders link
		nscore4OrdersTabPage.clickBrowseOrdersLlink();
		//Select Status as pending in filter
		nscore4OrdersTabPage.selectStatusDD("Pending");
		//Select Type as PC in filter
		nscore4OrdersTabPage.selectTypeDD("PC");
		//click 'GO' Button
		nscore4OrdersTabPage.clickGoSearchBtn();
		//nscore4OrdersTabPage.clickGoSearchBtn();
		//Verify all the results from the table satisfy the previous filter(s)-1.Order Status
		s_assert.assertTrue(nscore4OrdersTabPage.validateOrderStatusRow(),"Atleast 1 of the status is not 'Pending'");
		s_assert.assertTrue(nscore4OrdersTabPage.validateOrderTypeRow(),"Atleast 1 of the type is not 'PC'");
		s_assert.assertAll();
	}

	//NSC4_AccountsTab_OverviewPostNewNote
	@Test(priority=14)
	public void testNSC4AccountsTabOverviewPostNewNote(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String accountNumber = null;
		String categoryOfNotePopup = "1a";
		String categoryOfChildNote = "1k";
		String typeOfNotePopup="A";
		String typeOfChildNote="E";
		String noteTxt = "AutomationNote"+randomNum;
		String noteTxtOfChildNote = "AutomationChildNote"+randomNumber;

		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber); 
		nscore4HomePage.clickPostNewNodeLinkInOverviewTab();
		nscore4HomePage.selectAndEnterAddANoteDetailsInPopup(categoryOfNotePopup,typeOfNotePopup,noteTxt);
		nscore4HomePage.clickSaveBtnOnAddANotePopup();
		s_assert.assertTrue(nscore4HomePage.isNewlyCreatedNotePresent(noteTxt), "Newly created note"+noteTxt+" is not present ON UI");
		nscore4HomePage.clickPostFollowUpLinkForParentNote(noteTxt);
		nscore4HomePage.selectAndEnterAddANoteDetailsInPopup(categoryOfChildNote,typeOfChildNote,noteTxtOfChildNote);
		nscore4HomePage.clickSaveBtnOnAddANotePopup();
		s_assert.assertTrue(nscore4HomePage.isNewlyCreatedChildNotePresent(noteTxt,noteTxtOfChildNote), "Newly created Child note"+noteTxtOfChildNote+" is not present ON UI under parent note"+noteTxt);
		s_assert.assertTrue(nscore4HomePage.isPostFollowUpLinkPresentForChildNote(noteTxt,noteTxtOfChildNote), "Newly created Child note"+noteTxtOfChildNote+" post follow up link is not present ON UI under parent note"+noteTxt);
		nscore4HomePage.clickCollapseLinkNearToParentNote(noteTxt);
		s_assert.assertFalse(nscore4HomePage.isChildNoteDetailsAppearsOnUI(noteTxt), "Newly created Child note"+noteTxtOfChildNote+" details are present ON UI under parent note"+noteTxt);
		nscore4HomePage.clickExpandLinkNearToParentNote(noteTxt);
		s_assert.assertTrue(nscore4HomePage.isChildNoteDetailsAppearsOnUI(noteTxt), "Newly created Child note"+noteTxtOfChildNote+" details are not present ON UI under parent note"+noteTxt);
		s_assert.assertAll();
	}

	//NSC4_AccountsTab_OverviewPlaceNew Order
	@Test(priority=15)
	public void testAccountsTab_OverviewPlaceNewOrder(){
		String accountNumber = null;
		List<Map<String, Object>> randomSKUList =  null;
		String SKU = null;
		nscore4OrdersTabPage =new NSCore4OrdersTabPage(driver);
		logger.info("DB is "+RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);
		//click 'Place-New-Order' link
		nscore4HomePage.clickPlaceNewOrderLink();
		SKU= nscore4HomePage.addAndGetProductSKU("10");
		s_assert.assertTrue(nscore4HomePage.isProductAddedToOrder(SKU), "SKU = "+SKU+" is not added to the Autoship Order");
		nscore4OrdersTabPage.clickPaymentApplyLink();
		nscore4OrdersTabPage.clickSubmitOrderBtn();
		s_assert.assertTrue(nscore4OrdersTabPage.isOrderInformationPresent("Products"),"Product information not present as expected");
		s_assert.assertTrue(nscore4OrdersTabPage.isOrderInformationPresent("Shipments"),"Shipments information not present as expected");
		s_assert.assertTrue(nscore4OrdersTabPage.isOrderInformationPresent("Payment"),"Payment information not present as expected");
		s_assert.assertTrue(nscore4OrdersTabPage.validateOrderStatusAfterSubmitOrder(),"Order is not submitted after submit order");
		s_assert.assertTrue(nscore4OrdersTabPage.isOrderDetailPagePresent(),"This is not order details page");
		s_assert.assertAll();
	}

	//NSC4_AccountsTab_OverviewStatusChange
	@Test(priority=16)
	public void testAccountsTab_OverviewStatusChange(){
		String accountNumber = null;
		logger.info("DB is "+RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);
		//get the status before change
		String beforeStatus=nscore4HomePage.getStatus();
		//click the status link and change the status-
		nscore4HomePage.clickStatusLink();
		//change the status and save 
		nscore4HomePage.changeStatusDD(1);
		nscore4HomePage.clickSaveStatusBtn();
		nscore4HomePage.refreshPage();
		//get the status after change
		String afterStatus=nscore4HomePage.getStatus();
		//verify the status got changed successfully?
		s_assert.assertNotEquals(beforeStatus, afterStatus);
		//Revert the Changes-
		nscore4HomePage.clickStatusLink();
		nscore4HomePage.changeStatusDD(0);
		nscore4HomePage.clickSaveStatusBtn();
		nscore4HomePage.refreshPage();
		String finalStatus=nscore4HomePage.getStatus();
		s_assert.assertEquals(beforeStatus, finalStatus);
		s_assert.assertAll();
	}

	//NSC4_AccountsTab_OverviewAutoshipsViewOrders
	@Test(priority=17)
	public void testNSC4AccountTabOverviewAutoshipsViewOrders(){
		String accountNumber = null;
		String accounts = "Accounts";
		logger.info("DB is "+RFL_DB);
		String year = nscore4HomePage.getCurrentDateAndMonthAndYearAndMonthShortNameFromPstDate(nscore4HomePage.getPSTDate())[2];
		String currentYear = "%"+year+"%";
		randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_CONSULTANT_ACCOUNT_NUMBER_HAVING_AUTOSHIP_ORDER_WITH_CURRENT_YEAR_RFL, currentYear),RFL_DB);
		accountNumber =(String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);
		nscore4HomePage.clickViewOrderLinkUnderConsultantReplenishment();
		nscore4HomePage.clickCalenderStartDateForFilter();
		nscore4HomePage.selectMonthOnCalenderForNewEvent("Jan");
		nscore4HomePage.selectYearOnCalenderForNewEvent("2016");
		nscore4HomePage.clickSpecficDateOfCalendar("1");
		int totalSearchResults = nscore4HomePage.getCountOfSearchResults();
		String[] allCompleteDate = nscore4HomePage.getAllCompleteDate(totalSearchResults);
		s_assert.assertTrue(nscore4HomePage.isAllCompleteDateContainCurrentYear(allCompleteDate), "All complete date on UI are not in the range of filter for consultant autoship");;
		nscore4HomePage.navigateToBackPage();
		nscore4HomePage.clickTab(accounts);
		randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_CONSULTANT_ACCOUNT_NUMBER_HAVING_PULSE_AUTOSHIP_ORDER_WITH_CURRENT_YEAR_RFL, currentYear, currentYear),RFL_DB);
		accountNumber =(String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);
		nscore4HomePage.clickViewOrderLinkUnderPulseMonthlySubscription();
		nscore4HomePage.clickCalenderStartDateForFilter();
		nscore4HomePage.selectMonthOnCalenderForNewEvent("Jan");
		nscore4HomePage.selectYearOnCalenderForNewEvent("2016");
		nscore4HomePage.clickSpecficDateOfCalendar("1");
		totalSearchResults = nscore4HomePage.getCountOfSearchResults();
		int randomSearchResult = CommonUtils.getRandomNum(1, totalSearchResults);
		allCompleteDate = nscore4HomePage.getAllCompleteDate(totalSearchResults);
		s_assert.assertTrue(nscore4HomePage.isAllCompleteDateContainCurrentYear(allCompleteDate), "All complete date on UI are not in the range of filter for consultant pulse autoship");;
		nscore4HomePage.clickAndReturnRandomOrderNumber(randomSearchResult);
		s_assert.assertTrue(nscore4HomePage.getOrderNumberFromOrderDetails().contains(accountNumber), "Expected OrderNumber on order details page is: "+accountNumber+" Actual on UI is: "+nscore4HomePage.getOrderNumberFromOrderDetails());
		s_assert.assertAll();
	}


	//NSC4_SitesTab_nsCorporate_CorporateAddEditDeleteNews
	@Test(priority=18)
	public void testNSC4SitesTabNSCorporateAddEditDeleteNews(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum2 = CommonUtils.getRandomNum(10000, 1000000);
		String sites = "Sites";
		String title = "For Automation"+randomNum;
		String newTitle = "For Automation"+randomNum2;
		nscore4HomePage.clickTab(sites);
		nscore4SitesTabPage.clickCorporateLink();
		nscore4SitesTabPage.clickAddNewsLink();
		nscore4SitesTabPage.enterTitleForAddNews(title);
		nscore4SitesTabPage.checkIsActiveChkBoxForNewsTitle();
		nscore4SitesTabPage.clickSaveBtn();
		s_assert.assertTrue(nscore4SitesTabPage.getSavedSuccessfullyTxt().contains("News saved successfully"), "Expected saved message is: News saved successfully but actual on UI is: "+nscore4SitesTabPage.getSavedSuccessfullyTxt());
		int noOfOptionsInSizePageDD = nscore4SitesTabPage.getSizeOfOptinsFromPageSizeDD();
		nscore4SitesTabPage.clickAndSelectOptionInPageSizeDD(noOfOptionsInSizePageDD);
		s_assert.assertTrue(nscore4SitesTabPage.isTitleNamePresentInAnnouncementsList(title), "Title name is not present in announcement list");
		nscore4SitesTabPage.clickTitleNamePresentInAnnouncementsList(title);
		nscore4SitesTabPage.enterTitleForAddNews(newTitle);
		nscore4SitesTabPage.clickSaveBtn();
		s_assert.assertTrue(nscore4SitesTabPage.getSavedSuccessfullyTxt().contains("News saved successfully"), "Expected saved message is: News saved successfully but actual on UI is: "+nscore4SitesTabPage.getSavedSuccessfullyTxt());
		noOfOptionsInSizePageDD = nscore4SitesTabPage.getSizeOfOptinsFromPageSizeDD();
		nscore4SitesTabPage.clickAndSelectOptionInPageSizeDD(noOfOptionsInSizePageDD);
		s_assert.assertTrue(nscore4SitesTabPage.isTitleNamePresentInAnnouncementsList(newTitle), "Title name is not present in announcement list");
		nscore4SitesTabPage.checkTitleNameChkBoxInAnnouncementsList(newTitle);
		nscore4SitesTabPage.clickDeactivateSelectedLink();
		s_assert.assertTrue(nscore4SitesTabPage.getTitleStatus(newTitle).contains("Inactive"), "Expected title status is: Inactive but actual on UI: "+nscore4SitesTabPage.getTitleStatus(newTitle));
		nscore4SitesTabPage.checkTitleNameChkBoxInAnnouncementsList(newTitle);
		nscore4SitesTabPage.clickActivateSelectedLink();
		s_assert.assertTrue(nscore4SitesTabPage.getTitleStatus(newTitle).contains("Active"), "Expected title status is: Active but actual on UI is: "+nscore4SitesTabPage.getTitleStatus(newTitle));
		nscore4SitesTabPage.checkTitleNameChkBoxInAnnouncementsList(newTitle);
		nscore4SitesTabPage.clickDeleteSelectedLink();
		s_assert.assertFalse(nscore4SitesTabPage.isTitleNamePresentInAnnouncementsList(title), "Title name is present in announcement list after delete");
		s_assert.assertAll();
	}

	//NSC4_SitesTab_nsCorporate_CorporateSiteDetailsEditSite
	@Test(priority=19)
	public void testNSC4SitesTabNSCorporateSiteDetailEditSite(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String sites = "Sites";
		String siteDetails = "Site Details";
		String newSiteName = "Auto"+randomNum;
		nscore4HomePage.clickTab(sites);
		nscore4SitesTabPage.clickSubLinkOfCorporate(siteDetails);
		nscore4SitesTabPage.enterSiteNameForSiteDetails(newSiteName);
		nscore4SitesTabPage.checkActiveChkBoxForSiteDetails();
		nscore4SitesTabPage.clickSaveBtnOnSiteDetails();
		s_assert.assertTrue(nscore4SitesTabPage.getSavedSuccessfullyTxtForSite().contains("Site saved successfully"), "Expected saved message is: Site saved successfully but actual on UI is: "+nscore4SitesTabPage.getSavedSuccessfullyTxtForSite());
		nscore4SitesTabPage.clickTab(sites);
		nscore4SitesTabPage.clickSubLinkOfCorporate(siteDetails);
		nscore4SitesTabPage.enterSiteNameForSiteDetails("Corporate");
		nscore4SitesTabPage.uncheckActiveChkBoxForSiteDetails();
		nscore4SitesTabPage.clickSaveBtnOnSiteDetails();
		s_assert.assertTrue(nscore4SitesTabPage.getSavedSuccessfullyTxtForSite().contains("Site saved successfully"), "Expected saved message is: Site saved successfully but actual on UI is: "+nscore4SitesTabPage.getSavedSuccessfullyTxtForSite());
		s_assert.assertAll();
	}

	//NSC4_AccountsTab_FullAccountRecordUpdate
	@Test(priority=20)
	public void testNSC4AccountTabFullAccountRecordUpdate(){
		String accountNumber = null;
		logger.info("DB is "+RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);
		nscore4HomePage.clickFullAccountRecordLink();
		//get all values before update
		String userName = nscore4HomePage.getUserName();
		String firstName = nscore4HomePage.getFirstName();
		String homePhone = nscore4HomePage.getLastFourDgitOfHomePhoneNumber();
		String emailID = nscore4HomePage.getEmailAddress();
		String taxExemptValue = nscore4HomePage.getTaxExemptValue();
		String nameOnSSNCard = nscore4HomePage.getNameOnSSNCard();
		String dob = nscore4HomePage.getDOBValue();
		String gender = nscore4HomePage.getSelectedGender();
		String attentionName = nscore4HomePage.getAttentionName();
		String zipCode = nscore4HomePage.getZIPCode();
		String phoneNumber = nscore4HomePage.getLastFourDgitOfPhoneNumber();
		String dobDay = nscore4HomePage.getDayFromDate(dob);
		//update the values
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum2 = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum3 = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum4 = CommonUtils.getRandomNum(10000, 1000000);
		String userNameForUpdate = "Automation"+randomNum+"@gmail.com";
		String nameForUpdate = "Autoname"+randomNum1;
		String homePhoneForUpdate =  String.valueOf(CommonUtils.getRandomNum(1000, 9999));
		String emailIDForUpdate = "auto"+randomNum2+"@gmail.com";
		String taxExemptValueForUpdate =nscore4HomePage.getTaxExemptValueForUpdate(taxExemptValue);
		String nameOnSSNCardForUpdate = "Auto"+randomNum3;
		String dobDayForUpdate = nscore4HomePage.getUpdatedDayFromDate(dobDay);
		String genderForUpdate = nscore4HomePage.getGenderValueForUpdate(gender);
		String attentionNameForUpdate = "Auto"+randomNum4;
		String ZIPCodeForUpdate = "78130-3397";
		String phoneNumberForUpdate =  String.valueOf(CommonUtils.getRandomNum(1000, 9999));
		//For Account Access Section
		nscore4HomePage.enterUserName(userNameForUpdate);
		nscore4HomePage.clickSaveBtnForAccountRecord();
		nscore4HomePage.clickUseAsEnteredbtn();
		s_assert.assertTrue(nscore4HomePage.getUpdationMessage().contains("Account saved successfully"), "Expected message is: Account saved successfully but actual on UI is: "+nscore4HomePage.getUpdationMessage());
		s_assert.assertTrue(nscore4HomePage.getUserName().contains(userNameForUpdate), "Expected username is: "+userNameForUpdate+" But actual on UI is "+nscore4HomePage.getUserName());
		// For Personal Info Section
		nscore4HomePage.enterFirstName(nameForUpdate);
		nscore4HomePage.enterLastFourDigitOfHomePhoneNumber(homePhoneForUpdate);
		nscore4HomePage.enterEmailAddress(emailIDForUpdate);
		nscore4HomePage.selectTaxExemptValue(taxExemptValueForUpdate);
		nscore4HomePage.enterNameOnSSNCard(nameOnSSNCardForUpdate);
		nscore4HomePage.clickDOBDate();
		nscore4HomePage.clickSpecficDateOfCalendar(dobDayForUpdate);
		//driver.pauseExecutionFor(3000);
		nscore4HomePage.selectGender(genderForUpdate);
		nscore4HomePage.clickSaveBtnForAccountRecord();
		nscore4HomePage.clickUseAsEnteredbtn();
		s_assert.assertTrue(nscore4HomePage.getUpdationMessage().contains("Account saved successfully"), "Expected message is: Account saved successfully but actual on UI is: "+nscore4HomePage.getUpdationMessage());
		s_assert.assertTrue(nscore4HomePage.getFirstName().contains(nameForUpdate), "Expected first name is: "+nameForUpdate+" But actual on UI is "+nscore4HomePage.getFirstName());
		s_assert.assertTrue(nscore4HomePage.getLastFourDgitOfHomePhoneNumber().contains(homePhoneForUpdate), "Expected home phone number is: "+homePhoneForUpdate+" But actual on UI is "+nscore4HomePage.getLastFourDgitOfHomePhoneNumber());
		s_assert.assertTrue(nscore4HomePage.getEmailAddress().contains(emailIDForUpdate), "Expected email ID is: "+emailIDForUpdate+" But actual on UI is "+nscore4HomePage.getEmailAddress());
		s_assert.assertTrue(nscore4HomePage.getTaxExemptValue().contains(taxExemptValueForUpdate), "Expected tax exempt value is: "+taxExemptValueForUpdate+" But actual on UI is "+nscore4HomePage.getTaxExemptValue());
		s_assert.assertTrue(nscore4HomePage.getNameOnSSNCard().contains(nameOnSSNCardForUpdate), "Expected name on SSN Card is: "+nameOnSSNCardForUpdate+" But actual on UI is "+nscore4HomePage.getNameOnSSNCard());
		s_assert.assertTrue(nscore4HomePage.getDOBValue().contains(dobDayForUpdate), "Expected day for DOB is: "+dobDayForUpdate+" But actual on UI is "+nscore4HomePage.getDayFromDate(nscore4HomePage.getDOBValue()));
		s_assert.assertTrue(nscore4HomePage.getSelectedGender().contains(genderForUpdate), "Expected gender is: "+genderForUpdate+" But actual on UI is "+nscore4HomePage.getSelectedGender());
		// Assert for Address of Record
		nscore4HomePage.enterAttentionName(attentionNameForUpdate);
		nscore4HomePage.enterZIPCode(ZIPCodeForUpdate);
		nscore4HomePage.enterLastFourDigitOfPhoneNumber(phoneNumberForUpdate);
		nscore4HomePage.clickSaveBtnForAccountRecord();
		nscore4HomePage.clickUseAsEnteredbtn();
		s_assert.assertTrue(nscore4HomePage.getUpdationMessage().contains("Account saved successfully"), "Expected message is: Account saved successfully but actual on UI is: "+nscore4HomePage.getUpdationMessage());
		s_assert.assertTrue(nscore4HomePage.getAttentionName().contains(attentionNameForUpdate), "Expected attention name is: "+attentionNameForUpdate+" But actual on UI is "+nscore4HomePage.getAttentionName());
		s_assert.assertTrue(nscore4HomePage.getZIPCode().contains(ZIPCodeForUpdate), "Expected ZIP code is: "+ZIPCodeForUpdate+" But actual on UI is "+nscore4HomePage.getZIPCode());
		s_assert.assertTrue(nscore4HomePage.getLastFourDgitOfPhoneNumber().contains(phoneNumberForUpdate), "Expected phone number is: "+phoneNumberForUpdate+" But actual on UI is "+nscore4HomePage.getLastFourDgitOfPhoneNumber());
		nscore4HomePage.enterUserName(userName);
		nscore4HomePage.enterFirstName(firstName);
		nscore4HomePage.enterLastFourDigitOfHomePhoneNumber(homePhone);
		nscore4HomePage.enterEmailAddress(emailID);
		nscore4HomePage.selectTaxExemptValue(taxExemptValue);
		nscore4HomePage.enterNameOnSSNCard(nameOnSSNCard);
		nscore4HomePage.clickDOBDate();
		nscore4HomePage.clickSpecficDateOfCalendar(dobDay);
		nscore4HomePage.selectGender(gender);
		nscore4HomePage.enterAttentionName(attentionName);
		nscore4HomePage.enterZIPCode(zipCode);
		nscore4HomePage.enterLastFourDigitOfPhoneNumber(phoneNumber);
		nscore4HomePage.clickSaveBtnForAccountRecord();
		nscore4HomePage.clickUseAsEnteredbtn();
		s_assert.assertTrue(nscore4HomePage.getUpdationMessage().contains("Account saved successfully"), "Expected message is: Account saved successfully but actual on UI is: "+nscore4HomePage.getUpdationMessage());
		s_assert.assertAll();
	}

	//NSC4_AccountsTab_ ShippingProfilesAddEditDefaultDelete
	@Test(priority=21)
	public void testAccountsTab_ShippingProfilesAddEditDefaultDelete(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newShippingProfileName = "newSP"+randomNum;
		String attentionCO ="SP";
		String addressLine1 ="123 J street";
		String zipCode= "28214-5037";
		String accountNumber = null;
		nscore4OrdersTabPage =new NSCore4OrdersTabPage(driver);
		logger.info("DB is "+RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);
		//NAavigate to Billing & Shipping Profile section
		nscore4HomePage.clickBillingAndShippingProfileLink();
		int totalNoOfAddressBeforeAdd = nscore4HomePage.getTotalNoOfShippingProfiles();
		//click 'Add' for the Shipping profile section
		nscore4HomePage.clickShippingProfileAddLink();
		//Enter all Information regarding new Shipping Profile-
		nscore4HomePage.addANewShippingProfile(newShippingProfileName, attentionCO, addressLine1, zipCode);
		//click 'SAVE ADDRESS BTN'
		nscore4HomePage.clickSaveAddressBtn();
		nscore4HomePage.refreshPage();
		//verify newly created shipping profile created?
		s_assert.assertTrue(nscore4HomePage.isNewlyCreatedShippingProfilePresent(newShippingProfileName),"Newly created Shipping Profile is not Present");
		//click on 'Set As Default Address' on the newly created profile
		nscore4HomePage.clickSetAsDefaultAddressForNewlyCreatedProfile(newShippingProfileName);
		//Verify profile is now default?
		s_assert.assertTrue(nscore4HomePage.validateNewlyCreatedShippingProfileIsDefault(newShippingProfileName),"Newly created Shipping Profile is Not Marked-DEFAULT");
		//Delete the newly created profile-
		nscore4HomePage.deleteAddressNewlyCreatedProfile(newShippingProfileName);
		int totalNoOfAddressAfterDelete = nscore4HomePage.getTotalNoOfShippingProfiles();
		s_assert.assertTrue(totalNoOfAddressAfterDelete ==  totalNoOfAddressBeforeAdd, "Expected count of shipping profile after delete is: "+totalNoOfAddressBeforeAdd+" Actual on UI is: "+totalNoOfAddressAfterDelete);
		s_assert.assertAll();    
	}

	//NSC4_AccountsTab_ BillingProfilesAddEditDefaultDelete
	@Test(priority=22)
	public void testAccountsTab_BillingProfilesAddEditDefaultDelete(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = "RFAutoNSCore4"+randomNum;
		String lastName = "lN";
		String nameOnCard = "rfTestUser";
		String cardNumber =  "4747474747474747";
		String addressLine1 = TestConstantsRFL.ADDRESS_LINE1;
		String zipCode = TestConstantsRFL.POSTAL_CODE;
		String accountNumber = null;
		logger.info("DB is "+RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);
		//NAavigate to Billing & Shipping Profile section
		nscore4HomePage.clickBillingAndShippingProfileLink();
		int totalBillingProfilesBeforeAddingNew = nscore4HomePage.getTotalBillingProfiles();
		//click 'Add' for the billing profile section
		nscore4HomePage.clickBillingProfileAddLink();
		//Enter all the Information regarding New Billing Profile
		nscore4HomePage.addANewBillingProfile(newBillingProfileName, lastName, nameOnCard, cardNumber,addressLine1,zipCode);
		//click 'SAVE PAYMENT METHOD'
		nscore4HomePage.clickSavePaymentMethodBtn();
		//Verify that the new profile got created?
		s_assert.assertTrue(nscore4HomePage.getTotalBillingProfiles()==totalBillingProfilesBeforeAddingNew+1,"Newly created Billing Profile is not Present");
		//click on 'Set As Default Payment Method' on the newly created profile
		nscore4HomePage.clickSetAsDefaultPaymentMethodForNewlyCreatedProfile();
		//Verify profile is now default?
		s_assert.assertTrue(nscore4HomePage.validateNewlyCreatedBillingProfileIsDefault(),"Newly created Billing Profile is Not Marked-DEFAULT");
		//Delete the newly created profile-
		nscore4HomePage.deletePaymentMethodNewlyCreatedProfile();
		s_assert.assertTrue(nscore4HomePage.isBillingProfileDeleted(),"Billing profile is not deleted successfully");
		s_assert.assertAll();
	}

	//NSC4_AdminTab_ Users_AddEditRoles
	@Test(priority=23)
	public void testAdminTabUsersAddEditRoles(){
		int randomNum = CommonUtils.getRandomNum(10, 100);
		String roleName ="SampleRole";
		nscore4AdminPage=nscore4HomePage.clickAdminTab();
		//click Role->Add new role
		nscore4AdminPage.clickRolesLink();
		nscore4AdminPage.clickAddNewRoleLink();
		//Enter New Role Name and Save
		nscore4AdminPage.enterRoleName(roleName+randomNum);
		nscore4AdminPage.clickSaveBtn();
		//Verify that the new role got created and displayed as a link in Roles list?
		s_assert.assertTrue(nscore4AdminPage.validateNewRoleListedInRolesList(roleName+randomNum),"NEW Role Name is not listed in the roles list");
		s_assert.assertAll();
	}

	//NSC4_SitesTab_nsDistributor_BasePWSSitePagesAddEditNewSite
	@Test(priority=24)
	public void testBasePWSPagesAddEditNewSite(){
		int randomNumb = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumbers = CommonUtils.getRandomNum(10000, 1000000);
		String sublinkName = "Site Pages";
		String pageName = "AutomationPage"+randomNumb;
		String pageTitle = "AutoTitle"+randomNumber;
		String pageDescription = "AutoDesc"+randomNumbers;
		String pageURL = "/Pages/About/"+pageName;
		String pageKeyword = "Unique"+randomNumb;
		String templateView = "Two Piece View";

		logger.info("DB is "+RFL_DB);
		nscore4HomePage.clickTab("Sites");
		nscore4SitesTabPage.clickSubLinkOfDistributorOnSitePage(sublinkName);
		nscore4SitesTabPage.clickAddNewPageLink();
		nscore4SitesTabPage.enterNewPageDetails(pageName,pageTitle,pageDescription,pageURL,pageKeyword,templateView);
		nscore4SitesTabPage.clickSaveButtonForNewCreatedPage();
		s_assert.assertTrue(nscore4SitesTabPage.getPageSavedSuccessfullyTxtForSite().contains("Page saved successfully!"), "Expected saved message is: Site saved successfully but actual on UI is: "+nscore4SitesTabPage.getPageSavedSuccessfullyTxtForSite());
		int noOfOptionsInSizePageDD = nscore4SitesTabPage.getSizeOfOptinsFromPageSizeDD();
		nscore4SitesTabPage.clickAndSelectOptionInPageSizeDD(noOfOptionsInSizePageDD);
		s_assert.assertTrue(nscore4SitesTabPage.verifyPageNameOnSitePageList(pageName),"Newly created page name is not present on site page list");
		nscore4SitesTabPage.clickPageNameOnSitePageList(pageName);
		nscore4SitesTabPage.checkActiveCheckboxOnSitePage();
		nscore4SitesTabPage.clickSaveButtonForNewCreatedPage();
		s_assert.assertTrue(nscore4SitesTabPage.getPageSavedSuccessfullyTxtForSite().contains("Page saved successfully!"), "Expected Active checkbox unchecked message is: Site saved successfully but actual on UI is: "+nscore4SitesTabPage.getPageSavedSuccessfullyTxtForSite());
		s_assert.assertAll();
	}

	// NSC4_ProductsTab_ NewUpdateCatalog
	@Test(priority=25)
	public void testProductsTab_NewUpdateCatalog(){
		int randomNumber =  CommonUtils.getRandomNum(10000, 1000000);
		int randomNum =  CommonUtils.getRandomNum(1000, 100000);
		String catalogName = "Test"+randomNumber;
		String updatedCatalogInfo = "Test"+randomNum;
		nscore4HomePage.clickTab("Products");
		nscore4ProductsTabPage.clickCreateANewCatalogLink();
		nscore4ProductsTabPage.enterCatalogInfo(catalogName);
		nscore4ProductsTabPage.clickSaveCatalogBtn();
		nscore4HomePage.addAndGetProductSKUForCatalog();//(String) getValueFromQueryResult(randomSKUList, "SKU");;
		nscore4ProductsTabPage.clickSaveCatalogBtn();
		s_assert.assertTrue(nscore4ProductsTabPage.isSuccessMessagePresent(),"Success message is not displayed");
		nscore4ProductsTabPage.clickCatalogManagementLink();
		s_assert.assertTrue(nscore4ProductsTabPage.isNewCatalogPresentInList(catalogName),"new catalog is not present in the catalog list");
		nscore4ProductsTabPage.clicknewlyCreatedCatalogName(catalogName);
		nscore4ProductsTabPage.enterCatalogInfo(updatedCatalogInfo);
		nscore4ProductsTabPage.clickSaveCatalogBtn();
		s_assert.assertTrue(nscore4ProductsTabPage.isSuccessMessagePresent(),"Success message is not displayed");
		nscore4ProductsTabPage.clickCatalogManagementLink();
		s_assert.assertTrue(nscore4ProductsTabPage.isNewCatalogPresentInList(updatedCatalogInfo),"catalog info is not updated in the catalog list");
		s_assert.assertAll();
	}

	//NSC4_SitesTab_nsCorporate_CorporateReplicateSites
	@Test(priority=26)
	public void testNSC4SitesTabNSCorporateReplicateSites(){
		String sites = "Sites";
		String replicatedSites = "Replicated Sites";
		nscore4HomePage.clickTab(sites);
		nscore4SitesTabPage.clickSubLinkOfCorporate(replicatedSites);
		s_assert.assertTrue(nscore4SitesTabPage.isReplicatedSitesHeaderPresent(), "Replicated Sites is not present");
		s_assert.assertAll();
	}


	//NSC4 Full Return 
	@Test(priority=27)
	public void testNSC4FullReturn(){
		String accountNumber = null;
		String accounts = "Accounts";
		List<Map<String, Object>> randomSKUList =  null;
		String SKU = null;
		String placeNewOrder = "Place New Order";
		logger.info("DB is "+RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber");	
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);		
		nscore4HomePage.clickSublinkOfOverview(placeNewOrder);
		SKU= nscore4HomePage.addAndGetProductSKU("10");
		s_assert.assertTrue(nscore4HomePage.isProductAddedToOrder(SKU), "SKU = "+SKU+" is not added to the Autoship Order");
		nscore4HomePage.clickApplyPaymentButton();
		nscore4HomePage.clickSubmitOrderBtn();
		String orderID = nscore4HomePage.getOrderID().split("\\#")[1];
		nscore4HomePage.clickTab(accounts);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);		
		nscore4HomePage.clickOrderId(orderID);
		nscore4OrdersTabPage.clickReturnOrderLink();
		nscore4OrdersTabPage.enableReturnedChkBox();
		nscore4OrdersTabPage.clickUpdateLink();
		nscore4OrdersTabPage.clickSubmitReturnBtn();
		s_assert.assertTrue(nscore4OrdersTabPage.getOrderType().contains("Return"), "Order type does not contain return");
		s_assert.assertAll();
	}

	//NSC4 Partial  Return 
	@Test(priority=28)
	public void testNSC4PartialReturn(){
		String accountNumber = null;
		String accounts = "Accounts";
		String quantity = "5";
		String partialQuantity = "2";
		List<Map<String, Object>> randomSKUList =  null;
		String SKU = null;
		String placeNewOrder = "Place New Order";
		logger.info("DB is "+RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber");	
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);		
		nscore4HomePage.clickSublinkOfOverview(placeNewOrder);
		randomSKUList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_SKU,RFL_DB);
		SKU= nscore4HomePage.addAndGetProductSKU("10");
		s_assert.assertTrue(nscore4HomePage.isProductAddedToOrder(SKU), "SKU = "+SKU+" is not added to the Autoship Order");
		nscore4HomePage.clickApplyPaymentButton();
		nscore4HomePage.clickSubmitOrderBtn();
		String orderID = nscore4HomePage.getOrderID().split("\\#")[1];
		nscore4HomePage.clickTab(accounts);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);		
		nscore4HomePage.clickOrderId(orderID);
		nscore4OrdersTabPage.clickReturnOrderLink();
		nscore4OrdersTabPage.enableReturnedChkBox();
		nscore4OrdersTabPage.enterReturnQuantity(partialQuantity);
		nscore4OrdersTabPage.clickUpdateLink();
		nscore4OrdersTabPage.clickSubmitReturnBtn();
		s_assert.assertTrue(nscore4OrdersTabPage.getOrderType().contains("Return"), "Order type does not contain return");
		s_assert.assertTrue(nscore4OrdersTabPage.getReturnQuantityOfProduct().contains(partialQuantity), "Expected partial quantity is: "+partialQuantity+"Actual on UI is: "+nscore4OrdersTabPage.getReturnQuantityOfProduct());
		s_assert.assertAll();
	}

	//Return with Restocking fee
	@Test(priority=29)
	public void testNSC4ReturnWithRestockingFee(){
		String accountNumber = null;
		String accounts = "Accounts";
		String SKU = null;
		String restockingFee = "10.00";
		String placeNewOrder = "Place New Order";
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = "RFAutoNSCore4"+randomNum;
		String lastName = "lN";
		String nameOnCard = "rfTestUser";
		String cardNumber =  "4747474747474747";
		logger.info("DB is "+RFL_DB);
		accountNumber =(String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);  
		nscore4HomePage.clickSublinkOfOverview(placeNewOrder);
		SKU= nscore4HomePage.addAndGetProductSKU("10");
		s_assert.assertTrue(nscore4HomePage.isProductAddedToOrder(SKU), "SKU = "+SKU+" is not added to the Autoship Order");
		nscore4HomePage.addPaymentMethod(newBillingProfileName, lastName, nameOnCard, cardNumber);
		nscore4HomePage.clickSavePaymentMethodBtn();
		nscore4HomePage.clickApplyPaymentButton();
		nscore4HomePage.clickSubmitOrderBtn();
		String orderID = nscore4HomePage.getOrderID().split("\\#")[1];
		nscore4HomePage.clickTab(accounts);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);  
		nscore4HomePage.clickOrderId(orderID);
		nscore4OrdersTabPage.clickReturnOrderLink();
		nscore4OrdersTabPage.enableReturnedChkBox();
		nscore4OrdersTabPage.enterRestockingFeeInPercent(restockingFee);
		nscore4OrdersTabPage.clickUpdateLink();
		nscore4OrdersTabPage.clickSubmitReturnBtn();
		s_assert.assertTrue(nscore4OrdersTabPage.getOrderType().contains("Return"), "Order type does not contain return");
		s_assert.assertTrue(nscore4OrdersTabPage.isRestockingFeeTxtPresent(), "Restocking fee is not present after return order with restocking fee");
		s_assert.assertAll();
	}

	// NSC4_AdminTab_ Users_AddEditUsers
	@Test(priority=30)
	public void testAdminTab_Users_AddEditUsers(){
		int randomNum = CommonUtils.getRandomNum(100, 10000);
		String firstName = "firstName"+randomNum;
		String lastName = "Ln"+randomNum;
		String userName = "userName"+randomNum;
		System.out.println(userName+"   username");
		String password = "abc"+randomNum;
		String confirmPassword = password;

		nscore4HomePage.clickTab("Admin");
		nscore4AdminPage.clickAddNewUser();
		nscore4AdminPage.enterInfoAtAddUserPage(firstName,lastName,userName,password,confirmPassword);
		nscore4AdminPage.selectAllSitesAndSave();
		s_assert.assertTrue(nscore4AdminPage.isUserSavedSuccessfully(),"user is saved successfully msg not present");
		s_assert.assertTrue(nscore4AdminPage.isNewUserPresentInList(userName),"new user is not present in the list");
		nscore4AdminPage.clickOnNewUser();
		nscore4AdminPage.selectStatus("InActive");
		s_assert.assertTrue(nscore4AdminPage.isUserSavedSuccessfully(),"user is saved successfully msg not present");
		s_assert.assertAll();

	}

	// NSC4_AdminTab_ LystTypes
	@Test(priority=31)
	public void testAdminTab_LystTypes(){
		int randomNum = CommonUtils.getRandomNum(100, 1000);
		String newAddedListValue = "test"+randomNum;
		nscore4HomePage.clickTab("Admin");
		nscore4AdminPage.clickListTypesLink("Account Note Category");
		nscore4AdminPage.clickAddNewListValue();
		int numberofListPresent = nscore4AdminPage.getTotalNumberOfList();
		nscore4AdminPage.enterValueAndSave(newAddedListValue,numberofListPresent);
		nscore4AdminPage.clickListTypesLink("Account Note Category");
		s_assert.assertTrue(nscore4AdminPage.isNewListAdded(newAddedListValue,numberofListPresent),"new list is not being added");
		nscore4AdminPage.deleteSavedListTypeAndSave(numberofListPresent);
		nscore4AdminPage.handleAlertPop();
		s_assert.assertTrue(nscore4AdminPage.verifyListDeleted(numberofListPresent),"list is not being deleted");
		s_assert.assertAll();
	}

	//NSC4_AccountsTab_OverviewChangeAndStatusHistory
	@Test(priority=32)
	public void testAccountsTab_OverviewChangeAndStatusHistory(){
		String accountNumber = null;
		List<Map<String, Object>> randomAccountList =  null;
		RFL_DB = driver.getDBNameRFL();
		logger.info("DB is "+RFL_DB);
		randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);
		//Overview tab-> click 'Policies Change History' link
		nscore4HomePage.clickPoliciesChangeHistoryLink();
		//verify 'Policies Change History' Page is displayed with respective columns?
		s_assert.assertTrue(nscore4HomePage.validatePoliciesChangeHistoryPageDisplayedWithRespectiveColumns(),"'Policis Change History' Page Is Not displayed with the respective columns");
		//click status history
		nscore4HomePage.clickStatusHistoryLink();
		//verify 'Status History' Page is displayed with respective columns?
		s_assert.assertTrue(nscore4HomePage.validateStatusHistoryPageDisplayedWithRespectiveColumns(),"'Status History' Page Is Not displayed with the respective columns");
		s_assert.assertAll();
	}

	//NSC4_SitesTab_nsCorporate_CorporateNewEditDeleteEvent
	@Test(priority=33)
	public void testNSC4SitesTabNSCorporateNewEditDeleteEvent(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum2 = CommonUtils.getRandomNum(10000, 1000000);
		String sites = "Sites";
		String subject = "For Automation"+randomNum;
		nscore4HomePage.clickTab(sites);
		nscore4SitesTabPage.clickCorporateLink();
		nscore4SitesTabPage.clickAddEventLink();
		nscore4SitesTabPage.enterSubjectForEvent(subject);
		nscore4SitesTabPage.clickSaveBtn();
		s_assert.assertTrue(nscore4SitesTabPage.getSavedSuccessfullyTxt().contains("Event saved successfully"), "Expected saved message is: Event saved successfully but actual on UI is: "+nscore4SitesTabPage.getSavedSuccessfullyTxt());
		s_assert.assertTrue(nscore4SitesTabPage.isEventPresentAtCalendar(subject), "Event is not present at calendar ");
		nscore4SitesTabPage.clickEventNamePresentAtCalendar(subject);
		subject = "For Automation"+randomNum2;
		nscore4SitesTabPage.enterSubjectForEvent(subject);
		nscore4SitesTabPage.clickSaveBtn();
		s_assert.assertTrue(nscore4SitesTabPage.getSavedSuccessfullyTxt().contains("Event saved successfully"), "Expected saved message is: Event saved successfully but actual on UI is: "+nscore4SitesTabPage.getSavedSuccessfullyTxt());
		s_assert.assertTrue(nscore4SitesTabPage.isEventPresentAtCalendar(subject), "Event is not present at calendar ");
		nscore4SitesTabPage.clickEventNamePresentAtCalendar(subject);
		nscore4SitesTabPage.clickDeleteBtnForEvent();
		nscore4SitesTabPage.clickOKBtnOfJavaScriptPopUp();
		s_assert.assertFalse(nscore4SitesTabPage.isEventPresentAtCalendar(subject), "Event is present at calendar ");
		s_assert.assertAll();
	}


	//NSC4_SitesTab_nsDistributor_BasePWSSiteMap
	@Test(priority=34)
	public void testNSC4SitesTabBasePWSSiteMap(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String sites = "Sites";
		String siteMap = "Site Map";
		String linkText = "AutoRF"+randomNum;
		String pageOption = "MeetDrFields";
		String clearNavigationCache = "Clear Navigation Cache";
		String reloadProductCache  = "Reload Product Cache";

		nscore4HomePage.clickTab(sites);
		nscore4SitesTabPage.clickSubLinkOfDistributorOnSitePage(siteMap);
		nscore4SitesTabPage.clickAddLinkForSiteMap();
		nscore4SitesTabPage.enterLinkTextForSiteMap(linkText);
		nscore4SitesTabPage.selectPagesForSiteMap(pageOption);
		nscore4SitesTabPage.clickSaveBtnOnSiteMap();
		s_assert.assertTrue(nscore4SitesTabPage.isLinkTextNamePresentInTreeMap(linkText), "Link text name is not prsent in site map tree");
		nscore4SitesTabPage.expandTheTreeOfSiteMapOfBasePWS();
		nscore4SitesTabPage.moveToSiteMapLinkUnderDrFieldsForAddLinkOfBasePWS(linkText);
		nscore4SitesTabPage.clickActivateLinkOnSiteMap();
		nscore4HomePage.clickTab(sites);
		nscore4SitesTabPage.clickSubLinkOfNSCorporate(clearNavigationCache);
		s_assert.assertTrue(nscore4SitesTabPage.getProductClearAndReloadConfirmationMessage().contains("The navigation cache has been cleared"), "Expected confirmation message is: The navigation cache has been cleared but actual on UI is: "+nscore4SitesTabPage.getProductClearAndReloadConfirmationMessage());
		nscore4SitesTabPage.clickSubLinkOfNSCorporate(reloadProductCache);
		s_assert.assertTrue(nscore4SitesTabPage.getProductClearAndReloadConfirmationMessage().contains("The product cache has been reloaded"), "Expected confirmation message is: The product cache has been reloaded but actual on UI is: "+nscore4SitesTabPage.getProductClearAndReloadConfirmationMessage());
		nscore4SitesTabPage.clickSubLinkOfDistributorOnSitePage(siteMap);
		nscore4SitesTabPage.expandTheTreeOfSiteMapOfBasePWS();
		nscore4SitesTabPage.clickLinkTextNamePresentInTreeMap(linkText);
		nscore4SitesTabPage.clickDeactivateLinkOnSiteMap();
		s_assert.assertTrue(nscore4SitesTabPage.isActivateLinkPresentOnSiteMap(), "Activate link is not present after clicked on Deactivate link");
		s_assert.assertAll();
	}

	
	
	@Test(enabled=false)
	public void testOrdersTab_NewOrder_RC() {		
		/*for(int i=0; i<200; i++){*/
            /*System.out.println("Count is: " + i);*/
    		String accountNumber = null;
    		List<Map<String, Object>> completeNameList =  null;
    		String SKU = null;
    		logger.info("DB is "+RFL_DB);
    		randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS,RFL_DB);
    		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
    		logger.info("Account number from DB is "+accountNumber);
    		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
    		nscore4HomePage.clickGoBtnOfSearch(accountNumber);
    		nscore4OrdersTabPage = nscore4HomePage.clickOrdersTab();
    		nscore4OrdersTabPage.clickStartANewOrderLink();
    		completeNameList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_SPONSER_DETAILS_FROM_SPONSER_ACCOUNT_NUMBER, accountNumber),RFL_DB);
    		String firstNameDB = String.valueOf(getValueFromQueryResult(completeNameList, "FirstName"));
    		String lastNameDB = String.valueOf(getValueFromQueryResult(completeNameList, "LastName"));
    		String completeNameDB = firstNameDB+" "+lastNameDB;
    		logger.info("Complete name from DB is: "+completeNameDB);
    		nscore4OrdersTabPage.enterAccountNameAndClickStartOrder(completeNameDB, accountNumber);
    		//SKU = nscore4HomePage.addAndGetProductSKU("1");
    		nscore4HomePage.addAndGetSpecficProductSKU("1");
    		//s_assert.assertTrue(nscore4HomePage.isProductAddedToOrder(SKU), "SKU = "+SKU+" is not added to the Autoship Order");
    		driver.pauseExecutionFor(3000);
    		nscore4OrdersTabPage.clickPaymentApplyLink();
    		nscore4OrdersTabPage.clickSubmitOrderBtn();
    		s_assert.assertTrue(nscore4OrdersTabPage.isOrderInformationPresent("Products"),"Product information not present as expected");
    /*		s_assert.assertTrue(nscore4OrdersTabPage.isOrderInformationPresent("Shipments"),"Shipments information not present as expected");
    		s_assert.assertTrue(nscore4OrdersTabPage.isOrderInformationPresent("Payment"),"Payment information not present as expected");
    		s_assert.assertTrue(nscore4OrdersTabPage.validateOrderStatusAfterSubmitOrder(),"Order is not submitted after submit order");
    		s_assert.assertTrue(nscore4OrdersTabPage.isOrderDetailPagePresent(),"This is not order details page");*/
    		s_assert.assertAll();
    		try {
    			ExcelReader.ExcelWriter(firstNameDB, lastNameDB, accountNumber, "C:\\Users\\plu\\heirloom\\rf-automation\\JavaBooks.xlsx");
    		} catch (IOException e) {
    			
    		/*}*/
    		
       }

	}
	

	//	//Override Shipping and handling with a higher value
	//	@Test
	//	public void testOverrideShippingAndHandlingWithAHigherValue(){
	//		String refundedShipping ="20";
	//		String refundedHandling ="20";
	//		String accounts = "Accounts";
	//		String accountNumber = null;
	//		List<Map<String, Object>> randomAccountList =  null;
	//		List<Map<String, Object>> randomSKUList =  null;
	//		String SKU = null;
	//		nscore4OrdersTabPage =new NSCore4OrdersTabPage(driver);
	//		RFL_DB = driver.getDBNameRFL();
	//		logger.info("DB is "+RFL_DB);
	//		randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
	//		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
	//		logger.info("Account number from DB is "+accountNumber);
	//		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
	//		nscore4HomePage.clickGoBtnOfSearch(accountNumber);
	//		//click 'Place-New-Order' link
	//		nscore4HomePage.clickPlaceNewOrderLink();
	//		SKU= nscore4HomePage.addAndGetProductSKU("10");
	//		s_assert.assertTrue(nscore4HomePage.isProductAddedToOrder(SKU), "SKU = "+SKU+" is not added to the Autoship Order");
	//		nscore4OrdersTabPage.clickPaymentApplyLink();
	//		nscore4OrdersTabPage.clickSubmitOrderBtn();
	//		s_assert.assertTrue(nscore4OrdersTabPage.isOrderInformationPresent("Products"),"Product information not present as expected");
	//		s_assert.assertTrue(nscore4OrdersTabPage.isOrderInformationPresent("Shipments"),"Shipments information not present as expected");
	//		s_assert.assertTrue(nscore4OrdersTabPage.isOrderInformationPresent("Payment"),"Payment information not present as expected");
	//		s_assert.assertTrue(nscore4OrdersTabPage.validateOrderStatusAfterSubmitOrder(),"Order is not submitted after submit order");
	//		s_assert.assertTrue(nscore4OrdersTabPage.isOrderDetailPagePresent(),"This is not order details page");
	//		String orderID = nscore4HomePage.getOrderID().split("\\#")[1];
	//		nscore4HomePage.clickTab(accounts);
	//		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
	//		nscore4HomePage.clickGoBtnOfSearch(accountNumber);  
	//		nscore4HomePage.clickOrderId(orderID);
	//		nscore4OrdersTabPage.clickReturnOrderLink();
	//		//Enable returned checkBox
	//		nscore4OrdersTabPage.enableReturnedChkBox();
	//		nscore4OrdersTabPage.clickUpdateLink();
	//		//Verify total amount to refund displayed?
	//		s_assert.assertTrue(nscore4OrdersTabPage.isTotalAmountToBeRefundedPresent(),"Total Amount to refund is not displayed");
	//		//Override the refunded shipping & handling charges with 'higher' value (replace with 20)
	//		double refundedShippingBefore =nscore4OrdersTabPage.getRefundedShippingChargesValue();
	//		double refundedHandlingBefore =nscore4OrdersTabPage.getRefundedHandlingChargesValue();
	//
	//		nscore4OrdersTabPage.updateRefundedShippingChargesValue(refundedShipping);
	//		nscore4OrdersTabPage.updateRefundedHandlingChargesValue(refundedHandling);
	//		nscore4OrdersTabPage.clickUpdateLink();
	//		//verify user should not be able to update the refund shipping/handling value higher than the actual value-
	//		double refundedShippingAfter =nscore4OrdersTabPage.getRefundedShippingChargesValue();
	//		double refundedHandlingAfter =nscore4OrdersTabPage.getRefundedHandlingChargesValue();
	//
	//		//verify shipping?
	//		s_assert.assertEquals(refundedShippingBefore, refundedShippingAfter);
	//		//verify handling?
	//		s_assert.assertEquals(refundedHandlingBefore, refundedHandlingAfter);
	//		s_assert.assertAll();
	//	}


	//	//Override Shipping and handling with a less or equal value
	//	@Test(enabled=true)
	//	public void testOverrideShippingAndHandlingWithALessOrEqualValue(){
	//		String refundedShipping ="0.00";
	//		String refundedHandling ="0.00";
	//		String accounts = "Accounts";
	//		String accountNumber = null;
	//		List<Map<String, Object>> randomAccountList =  null;
	//		String SKU = null;
	//		nscore4OrdersTabPage =new NSCore4OrdersTabPage(driver);
	//		RFL_DB = driver.getDBNameRFL();
	//		logger.info("DB is "+RFL_DB);
	//		randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
	//		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
	//		logger.info("Account number from DB is "+accountNumber);
	//		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
	//		nscore4HomePage.clickGoBtnOfSearch(accountNumber);
	//		//click 'Place-New-Order' link
	//		nscore4HomePage.clickPlaceNewOrderLink();
	//		SKU= nscore4HomePage.addAndGetProductSKU("10");
	//		s_assert.assertTrue(nscore4HomePage.isProductAddedToOrder(SKU), "SKU = "+SKU+" is not added to the Autoship Order");
	//		nscore4OrdersTabPage.clickPaymentApplyLink();
	//		nscore4OrdersTabPage.clickSubmitOrderBtn();
	//		s_assert.assertTrue(nscore4OrdersTabPage.isOrderInformationPresent("Shipments"),"Shipments information not present as expected");
	//		s_assert.assertTrue(nscore4OrdersTabPage.isOrderInformationPresent("Payment"),"Payment information not present as expected");
	//		s_assert.assertTrue(nscore4OrdersTabPage.validateOrderStatusAfterSubmitOrder(),"Order is not submitted after submit order");
	//		s_assert.assertTrue(nscore4OrdersTabPage.isOrderDetailPagePresent(),"This is not order details page");
	//		String orderID = nscore4HomePage.getOrderID().split("\\#")[1];
	//		nscore4HomePage.clickTab(accounts);
	//		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
	//		nscore4HomePage.clickGoBtnOfSearch(accountNumber);  
	//		nscore4HomePage.clickOrderId(orderID);
	//		nscore4OrdersTabPage.clickReturnOrderLink();
	//		//Enable returned checkBox
	//		nscore4OrdersTabPage.enableReturnedChkBox();
	//		nscore4OrdersTabPage.clickUpdateLink();
	//		//Verify total amount to refund displayed?
	//		s_assert.assertTrue(nscore4OrdersTabPage.isTotalAmountToBeRefundedPresent(),"Total Amount to refund is not displayed");
	//		//Override the refunded shipping & handling charges with 'Lower' value (replace with 00)
	//		double refundedShippingBefore =nscore4OrdersTabPage.getRefundedShippingChargesValue();
	//		double refundedHandlingBefore =nscore4OrdersTabPage.getRefundedHandlingChargesValue();
	//		System.out.println("refundedShippingBefore "+refundedShippingBefore);
	//		System.out.println("refundedHandlingBefore "+refundedHandlingBefore);
	//		nscore4OrdersTabPage.updateRefundedShippingChargesLowerValue(refundedShipping);
	//		nscore4OrdersTabPage.updateRefundedHandlingChargesLowerValue(refundedHandling);
	//		nscore4OrdersTabPage.clickUpdateLink();
	//		//verify user should not be able to update the refund shipping/handling value higher than the actual value-
	//		double refundedShippingAfter =nscore4OrdersTabPage.getRefundedShippingChargesValue();
	//		double refundedHandlingAfter =nscore4OrdersTabPage.getRefundedHandlingChargesValue();
	//		System.out.println("refundedShippingAfter "+refundedShippingAfter);
	//		System.out.println("refundedHandlingAfter "+refundedHandlingAfter);
	//		//verify shipping?
	//		s_assert.assertTrue(refundedShippingBefore!=refundedShippingAfter,"shipping value has not changed");
	//		//verify handling?
	//		s_assert.assertTrue(refundedHandlingBefore!=refundedHandlingAfter,"handling shipping has not changed");
	////		nscore4OrdersTabPage.clickSubmitReturnBtn();
	////		s_assert.assertTrue(nscore4OrdersTabPage.getOrderType().contains("Return"), "Order type does not contain return");
	//		s_assert.assertAll();
	//	}


}