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

	//NSC4_AccountsTab_OverviewStatusChange
	@Test
	public void testAccountsTab_OverviewStatusChange(){
		String accountNumber = null;
		List<Map<String, Object>> randomAccountList =  null;
		RFL_DB = driver.getDBNameRFL();
		logger.info("DB is "+RFL_DB);
		randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
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
	@Test
	public void testNSC4AccountTabOverviewAutoshipsViewOrders(){
		String accountNumber = null;
		String accounts = "Accounts";
		List<Map<String, Object>> randomAccountList =  null;
		RFL_DB = driver.getDBNameRFL();
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
	@Test
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
	@Test
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
	@Test
	public void testNSC4AccountTabFullAccountRecordUpdate(){
		String accountNumber = null;
		List<Map<String, Object>> randomAccountList =  null;
		RFL_DB = driver.getDBNameRFL();
		logger.info("DB is "+RFL_DB);
		randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);
		nscore4HomePage.clickFullAccountRecordLink();
		//get all values before update
		String startDate = nscore4HomePage.getCRPStartDate();
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
		String startDay = nscore4HomePage.getDayFromDate(startDate);
		String startMonth = nscore4HomePage.getMonthFromDate(startDate);
		String startYear = nscore4HomePage.getYearFromDate(startDate);
		String dobDay = nscore4HomePage.getDayFromDate(dob);
		//update the values
		String startDayForUpdate = nscore4HomePage.getUpdatedDayFromDate(startDay);
		String monthForUpdate = nscore4HomePage.getMonthInWords(startMonth);
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
		//For Account Details Section
		nscore4HomePage.clickCRPStartDate();
		nscore4HomePage.selectMonthOnCalenderForNewEvent(monthForUpdate);
		nscore4HomePage.selectYearOnCalenderForNewEvent(startYear);
		nscore4HomePage.clickSpecficDateOfCalendar(startDayForUpdate);
		nscore4HomePage.clickSaveBtnForAccountRecord();
		nscore4HomePage.clickUseAsEnteredbtn();
		s_assert.assertTrue(nscore4HomePage.getUpdationMessage().contains("Account saved successfully"), "Expected message is: Account saved successfully but actual on UI is: "+nscore4HomePage.getUpdationMessage());
		s_assert.assertTrue(nscore4HomePage.getCRPStartDate().contains(startDayForUpdate), "Expected day is: "+startDayForUpdate+" But actual on UI is "+nscore4HomePage.getDayFromDate(nscore4HomePage.getCRPStartDate()));
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
		nscore4HomePage.clickCRPStartDate();
		nscore4HomePage.clickSpecficDateOfCalendar(startDay);
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
	@Test
	public void testAccountsTab_ShippingProfilesAddEditDefaultDelete(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newShippingProfileName = "newSP"+randomNum;
		String attentionCO ="SP";
		String addressLine1 ="123 J street";
		String zipCode= "28214-5037";
		String accountNumber = null;
		List<Map<String, Object>> randomAccountList =  null;
		nscore4OrdersTabPage =new NSCore4OrdersTabPage(driver);
		RFL_DB = driver.getDBNameRFL();
		logger.info("DB is "+RFL_DB);
		randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);
		//NAavigate to Billing & Shipping Profile section
		nscore4HomePage.clickBillingAndShippingProfileLink();
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
		s_assert.assertAll();	   
	}

	//NSC4_AccountsTab_ BillingProfilesAddEditDefaultDelete
	@Test
	public void testAccountsTab_BillingProfilesAddEditDefaultDelete(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = "RFAutoNSCore4"+randomNum;
		String lastName = "lN";
		String nameOnCard = "rfTestUser";
		String cardNumber =  "4747474747474747";
		String accountNumber = null;
		List<Map<String, Object>> randomAccountList =  null;
		nscore4OrdersTabPage =new NSCore4OrdersTabPage(driver);
		RFL_DB = driver.getDBNameRFL();
		logger.info("DB is "+RFL_DB);
		randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFL,RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber"); 
		logger.info("Account number from DB is "+accountNumber);
		nscore4HomePage.enterAccountNumberInAccountSearchField(accountNumber);
		nscore4HomePage.clickGoBtnOfSearch(accountNumber);
		//NAavigate to Billing & Shipping Profile section
		nscore4HomePage.clickBillingAndShippingProfileLink();
		//click 'Add' for the billing profile section
		nscore4HomePage.clickBillingProfileAddLink();
		//Enter all the Information regarding New Billing Profile
		nscore4HomePage.addANewBillingProfile(newBillingProfileName, lastName, nameOnCard, cardNumber);
		//click 'SAVE PAYMENT METHOD'
		nscore4HomePage.clickSavePaymentMethodBtn();
		//Verify that the new profile got created?
		s_assert.assertTrue(nscore4HomePage.isNewlyCreatedBilingProfilePresent(),"Newly created Billing Profile is not Present");
		//click on 'Set As Default Payment Method' on the newly created profile
		nscore4HomePage.clickSetAsDefaultPaymentMethodForNewlyCreatedProfile();
		//Verify profile is now default?
		s_assert.assertTrue(nscore4HomePage.validateNewlyCreatedBillingProfileIsDefault(),"Newly created Billing Profile is Not Marked-DEFAULT");
		//Delete the newly created profile-
		nscore4HomePage.deletePaymentMethodNewlyCreatedProfile();
		s_assert.assertAll();
	}

}