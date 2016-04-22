package com.rf.test.website.nscore4;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
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

	public NSCore4AccountTest() {
		nscore4HomePage = new NSCore4HomePage(driver);
		nscore4SitesTabPage = new NSCore4SitesTabPage(driver);
		nscore4ProductsTabPage = new NSCore4ProductsTabPage(driver);
	}

	//NSC4_AdministratorLogin_LogingLogout
	@Test
	public void testAdministrationLoginLogout(){
		s_assert.assertTrue(nscore4HomePage.isLogoutLinkPresent(),"Home Page has not appeared after login");
		nscore4LoginPage = nscore4HomePage.clickLogoutLink();		
		s_assert.assertTrue(nscore4LoginPage.isLoginButtonPresent(),"Login page has not appeared after logout");
		login("admin", "skin123!");
		s_assert.assertAll();
	}

	//NSC4_AdministratorLogin_InvalidLoging
	@Test
	public void testAdministratorLoginInvalidLogin(){
		nscore4LoginPage = nscore4HomePage.clickLogoutLink();	
		login("abcd", "test1234!");
		s_assert.assertTrue(nscore4LoginPage.isLoginCredentailsErrorMsgPresent(), "Login credentials error msg is not displayed");
		login("admin", "skin123!");
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
		s_assert.assertTrue(nscore4HomePage.isFirstAndLastNamePresentinSearchResults(firstName, lastName), "First and last name is not present in search result,when searched with account number");
		nscore4HomePage.enterAccountNumberInAccountSearchField(firstName+" "+lastName);
		s_assert.assertTrue(nscore4HomePage.isFirstAndLastNamePresentinSearchResults(firstName, lastName), "First and last name is not present in search result,when searched with first & last name");
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

	//NSC4_MobileTab_ HeadlineNews
	@Test
	public void testMobileTabHeadLineNews(){
		nscore4MobilePage=nscore4HomePage.clickMobileTab();
		//click headlines news link
		nscore4MobilePage.clickHeadLineNewsLink();
		//verify 'Browse HeadLine News' Page comes up?
		s_assert.assertTrue(nscore4MobilePage.validateBrowseHeadLineNewsPage(), "Browse 'HeadLineNews' Page is not displayed");
		s_assert.assertAll();
	}

	//NSC4_MobileTab_ R+FInTheNews
	@Test
	public void testMobileTabRFInNews(){
		nscore4MobilePage=nscore4HomePage.clickMobileTab();
		//click R+F In the news link
		nscore4MobilePage.clickRFInNewsLink();
		//verify 'Browse RF In News' Page comes up?
		s_assert.assertTrue(nscore4MobilePage.validateBrowseRFInNewsPage(), "Browse 'RF In News' Page is not displayed");
		s_assert.assertAll();
	}

	//NSC4_AccountsTab_OrdersEditCancelOrder
	@Test
	public void testAccountsTab_OrdersEditCancelOrder(){
		String accountNumber = null;
		List<Map<String, Object>> randomAccountList =  null;
		RFL_DB = driver.getDBNameRFL();
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
	@Test
	public void testOrdersTab_OrderIdSearch(){
		String accountNumber = null;
		List<Map<String, Object>> randomAccountList =  null;
		RFL_DB = driver.getDBNameRFL();
		logger.info("DB is "+RFL_DB);
		randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
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
	@Test
	public void testOrdersTab_OrderAdvancedSearch(){
		String accountNumber = null;
		List<Map<String, Object>> randomAccountList =  null;
		RFL_DB = driver.getDBNameRFL();
		logger.info("DB is "+RFL_DB);
		randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);
		nscore4OrdersTabPage = nscore4HomePage.clickOrdersTab();
		nscore4OrdersTabPage.selectDropDownAdvancedSearch("Customer Account");
		nscore4OrdersTabPage.enterValueInAdvancedSearchInputField("010080");
		s_assert.assertTrue(nscore4OrdersTabPage.isSearchResultFirstName("Tracy")," Results first name is not for Tracy ");
		s_assert.assertTrue(nscore4OrdersTabPage.isSearchResultLastName("Sharpe")," Results last name is not for Sharpe");
		s_assert.assertAll();
	}

	//NSC4_SitesTab_nsCorporate_CorporatePWSContentReviewApprove
	@Test
	public void testCorporatePWSContentReviewApprove() {
		RFL_DB = driver.getDBNameRFL();
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
	@Test
	public void testCorporatePWSContentReviewDenied(){
		RFL_DB = driver.getDBNameRFL();
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

	//NSC4_SitesTab_nsCorporate_CorporateNewEditDeleteEvent
	@Test
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

	//&--
	//NSC4_OrdersTab_NewOrder
	@Test
	public void testOrdersTab_NewOrder(){
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
		nscore4OrdersTabPage = nscore4HomePage.clickOrdersTab();
		nscore4OrdersTabPage.clickStartANewOrderLink();
		nscore4OrdersTabPage.enterAccountNameAndClickStartOrder("Tracy Sharpe");
		randomSKUList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_SKU,RFL_DB);
		SKU = (String) getValueFromQueryResult(randomSKUList, "SKU");
		logger.info("SKUfrom DB is "+SKU);
		nscore4HomePage.enterSKUValue(SKU);
		nscore4HomePage.clickFirstSKUSearchResultOfAutoSuggestion();
		nscore4HomePage.enterProductQuantityAndAddToOrder("1");
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
	@Test
	public void testOrdersTabBrowseOrders(){
		String accountNumber = null;
		List<Map<String, Object>> randomAccountList =  null;
		RFL_DB = driver.getDBNameRFL();
		logger.info("DB is "+RFL_DB);
		randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
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
	@Test
	public void testNSC4AccountsTabOverviewPostNewNote(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomAccountList =  null;
		String accountNumber = null;
		String categoryOfNotePopup = "1a";
		String categoryOfChildNote = "1k";
		String typeOfNotePopup="A";
		String typeOfChildNote="E";
		String noteTxt = "AutomationNote"+randomNum;
		String noteTxtOfChildNote = "AutomationChildNote"+randomNumber;

		randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(); 
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
	@Test
	public void testAccountsTab_OverviewPlaceNewOrder(){
		String accountNumber = null;
		List<Map<String, Object>> randomAccountList =  null;
		List<Map<String, Object>> randomSKUList =  null;
		String SKU = null;
		nscore4OrdersTabPage =new NSCore4OrdersTabPage(driver);
		RFL_DB = driver.getDBNameRFL();
		logger.info("DB is "+RFL_DB);
		randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);
		//click 'Place-New-Order' link
		nscore4HomePage.clickPlaceNewOrderLink();
		randomSKUList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_SKU,RFL_DB);
		SKU = (String) getValueFromQueryResult(randomSKUList, "SKU");
		logger.info("SKUfrom DB is "+SKU);
		nscore4HomePage.enterSKUValue(SKU);
		nscore4HomePage.clickFirstSKUSearchResultOfAutoSuggestion();
		nscore4HomePage.enterProductQuantityAndAddToOrder("1");
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
	
}