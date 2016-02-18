package com.rf.test.website.crm.miniRegression;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.crm.CRMAccountDetailsPage;
import com.rf.pages.website.crm.CRMHomePage;
import com.rf.pages.website.crm.CRMLoginPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class CRMMiniRegressionTest extends RFWebsiteBaseTest{

	private static final Logger logger = LogManager
			.getLogger(CRMMiniRegressionTest.class.getName());

	private CRMLoginPage crmLoginpage;
	private CRMHomePage crmHomePage;
	private CRMAccountDetailsPage crmAccountDetailsPage; 
	private String RFO_DB = null;

	//Hybris Project-4499:Verify the Proxy to Pulse for a Consultant
	@Test 
	public void testVerifyProxyToPulseForConsultant_4499() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
		logger.info("The email address is "+consultantEmailID);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(consultantEmailID.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+consultantEmailID.toLowerCase().trim());		
		while(true){
			if(crmHomePage.isSearchResultHasActiveUser()==false){
				logger.info("No active user in the search results..searching new user");
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
				logger.info("The email address is "+consultantEmailID);
				s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
				crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
				emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
			}else{
				break;
			}
		}
		crmHomePage.clickNameWithActiveStatusInSearchResults();
		crmAccountDetailsPage.clickAccountDetailsButton("Pulse");
		crmAccountDetailsPage.switchToChildWindow();
		s_assert.assertTrue(crmAccountDetailsPage.isSVSectionPresentOnPulsePage(), "SV Section is not present on Pulse Page");		
		s_assert.assertAll();
	}

	//Hybris Project-4498:Verify the Proxy to my account for a Consultant
	@Test(enabled=false)//issue  
	public void testVerifyProxyToMyAccountForConsultant_4498() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
		logger.info("The email address is "+consultantEmailID);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(consultantEmailID.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+consultantEmailID.toLowerCase().trim());		
		while(true){
			if(crmHomePage.isSearchResultHasActiveUser()==false){
				logger.info("No active user in the search results..searching new user");
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
				logger.info("The email address is "+consultantEmailID);
				s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
				crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
				emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
			}else{
				break;
			}
		}
		crmHomePage.clickNameWithActiveStatusInSearchResults();
		crmAccountDetailsPage.clickAccountDetailsButton("My Account");
		crmAccountDetailsPage.switchToChildWindow();
		s_assert.assertTrue(crmAccountDetailsPage.isSVSectionPresentOnPulsePage(), "SV Section is not present on Pulse Page");		
		s_assert.assertAll();
	}

	//Hybris Project-4542:View Shipping Profile for Consultant
	@Test
	public void testViewShippingProfileForConsultant_4542() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
		logger.info("The email address is "+consultantEmailID);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(consultantEmailID.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+consultantEmailID.toLowerCase().trim());		
		while(true){
			if(crmHomePage.isSearchResultHasActiveUser()==false){
				logger.info("No active user in the search results..searching new user");
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
				logger.info("The email address is "+consultantEmailID);
				s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
				crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
				emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
			}else{
				break;
			}
		}
		crmHomePage.clickNameWithActiveStatusInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Action"),"Action label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Name"),"Name label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("ProfileName"),"ProfileName label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Default"),"Default label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Address Line 1"),"Address Line 1 label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Address Line 2"),"Address Line 2 label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Address Line 3"),"Address Line 3 label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Locale"),"Locale label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Region"),"Region label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Postal code"),"Postal code label is not present in Shipping address section");

		String shippingProfilesCount = crmAccountDetailsPage.getShippingProfilesCount();
		String countDisplayedWithShippingLink = crmAccountDetailsPage.getCountDisplayedWithLink("Shipping Profiles");
		s_assert.assertTrue(shippingProfilesCount.equals(countDisplayedWithShippingLink), "shipping profiles count = "+shippingProfilesCount+"while count Displayed With Shipping Link = "+countDisplayedWithShippingLink);
		s_assert.assertTrue(crmAccountDetailsPage.isOnlyOneShippingProfileIsDefault(),"default shipping profiles is not one");

		s_assert.assertAll();
	}

	// Hybris Project-4479:Add the Account Notes for Consultant
	@Test
	public void testAddAccountNoteForConsultant_4479() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		String orderNote="This is automation note"+randomNum;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The username is "+consultantEmailID); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(consultantEmailID.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+consultantEmailID.toLowerCase().trim());

		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertTrue(crmAccountDetailsPage.isLogAccountActivitySectionIsPresent(),"Log Account Activity Section is not present on Account Details page");
		//Verify account dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDropdownOnAccountDetailPagePresent(),"Account dropdown is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDropdownSearchOnAccountDetailPagePresent(),"Account dropdown search button is not present on Account Details page");
		//verify channel dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOnAccountDetailPagePresent(),"Channel dropdown is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOptionsPresent("Email"),"Channel dropdown Email option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOptionsPresent("Call"),"Channel dropdown call option is not present on Account Details page");
		//Verify reason dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOnAccountDetailPagePresent(),"Reason dropdown is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Consultants"),"Reason dropdown Consultants option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("CRP"),"Reason dropdown CRP option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("PCs"),"Reason dropdown PCs option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Order"),"Reason dropdown Order option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Returns"),"Reason dropdown Returns option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("RF Mall"),"Reason dropdown RF Mall option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Pulse"),"Reason dropdown Pulse option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Sales Support RF Connection Transfer"),"Reason dropdown Sales Support RF option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Technology"),"Reason dropdown Technology option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Marketing Promotion"),"Reason dropdown Marketing Promotion option is not present on Account Details page");
		//verify details dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isDetailsDropdownOnAccountDetailPagePresent(),"Detail dropdown is not present on Account Details page");
		//verify note section
		s_assert.assertTrue(crmAccountDetailsPage.isNoteSectionOnAccountDetailPagePresent(),"Note section is not present on Account Details page");
		//add note and click save.
		crmAccountDetailsPage.selectChannelDropdown("Email");
		crmAccountDetailsPage.selectReasonDropdown("Consultants");
		crmAccountDetailsPage.selectDetailDropdown("Consultant event approval");
		crmAccountDetailsPage.enterNote(orderNote);
		crmAccountDetailsPage.clickOnSaveAfterEnteringNote();
		//Verify channel account and reason dropdown are disabled.
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDropdownOnAccountDetailPageIsEnabled(),"Account dropdown is not disable on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOnAccountDetailPageIsEnabled(),"Channel dropdown is not disable on Account Details page");
		s_assert.assertFalse(crmAccountDetailsPage.isDetailDropdownOnAccountDetailPageIsEnabled(),"Detail dropdown is not disable on Account Details page");
		//verify the note in account activity section present
		s_assert.assertTrue(crmAccountDetailsPage.getNoteFromUIOnAccountDetailPage().contains(orderNote),"Note text from UI is "+crmAccountDetailsPage.getNoteFromUIOnAccountDetailPage()+" While expected text is "+orderNote);
		s_assert.assertAll();
	}

	// Hybris Project-4507:Edit Preferred Customer Account details
	@Test
	public void testEditPreferredCustomerActDetails_4507() throws InterruptedException	{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomPCUserList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String pcUserName = null;
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcUserName = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
		logger.info("The username is "+pcUserName); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		//Click edit account button under Account detail section
		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");
		//Edit Account Information by updating Recognition Name field
		crmAccountDetailsPage.updateRecognitionNameField(TestConstants.CRM_ACT_INFORMATION_RECOGNITION_NAME);
		//save Account information
		crmAccountDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify updated Recognition Name in sales force
		s_assert.assertTrue(crmAccountDetailsPage.getRecognitionName().equalsIgnoreCase(TestConstants.CRM_ACT_INFORMATION_RECOGNITION_NAME),"Recognition name is not updated");
		//Click edit account button under Account detail section
		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");
		//Edit Account Information by updating Main address
		crmAccountDetailsPage.updateAddressLine3Field(TestConstants.CRM_MAIN_ADDRESS_LINE_3);
		//save Account information
		crmAccountDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify updated Main Address in sales force
		s_assert.assertTrue(crmAccountDetailsPage.getMainAddressLine3().trim().equalsIgnoreCase(TestConstants.CRM_MAIN_ADDRESS_LINE_3.trim()),"Main account address is not updated");
		s_assert.assertAll();
	}

	//Hybris Project-4510:Edit Retail Customer Account details
	@Test
	public void testEditRetailCustomerActDetails_4510() throws InterruptedException	{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomRCUserList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String rcUserName = null;
		randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
		rcUserName = (String) getValueFromQueryResult(randomRCUserList, "UserName");  
		logger.info("The username is "+rcUserName); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		//Click edit account button under Account detail section
		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");
		//Edit Account Information by updating Recognition Name field
		crmAccountDetailsPage.updateRecognitionNameField(TestConstants.CRM_ACT_INFORMATION_RECOGNITION_NAME);
		//save Account information
		crmAccountDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify updated Recognition Name in sales force
		s_assert.assertTrue(crmAccountDetailsPage.getRecognitionName().equalsIgnoreCase(TestConstants.CRM_ACT_INFORMATION_RECOGNITION_NAME),"Recognition name is not updated");
		//Click edit account button under Account detail section
		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");
		//Edit Account Information by updating Main address
		crmAccountDetailsPage.updateAddressLine3Field(TestConstants.CRM_MAIN_ADDRESS_LINE_3);
		//save Account information
		crmAccountDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify updated Main Address in sales force
		s_assert.assertTrue(crmAccountDetailsPage.getMainAddressLine3().trim().equalsIgnoreCase(TestConstants.CRM_MAIN_ADDRESS_LINE_3.trim()),"Main account address is not updated");
		s_assert.assertAll();
	}

	//Hybris Project-4513:Edit Shipping Profile for Consultant
	@Test
	public void testEditShippingProfileForConsultant_4513() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstNameWithSpecialChar = TestConstants.FIRST_NAME+randomNum+"%%";
		String lastName = TestConstants.LAST_NAME;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The username is "+consultantEmailID); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		String noOfShippingProfile = crmAccountDetailsPage.getShippingProfilesCount();
		s_assert.assertTrue(crmAccountDetailsPage.verifyShippingProfileCountIsEqualOrGreaterThanOne(noOfShippingProfile),"expected minium number of shipping profile is 1 actual on UI "+noOfShippingProfile);
		crmAccountDetailsPage.clickEditFirstShippingProfile();
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstNameWithSpecialChar+" "+lastName);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		String updatedProfileName = crmAccountDetailsPage.getFirstShippingProfileName();
		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstNameWithSpecialChar), "Expected shipping profile name is "+shippingProfileFirstNameWithSpecialChar+"Actual on UI "+updatedProfileName);
		s_assert.assertAll();
	}

	//Hybris Project-4544:View Shipping Profile for RC
	@Test
	public void testViewShippingProfileForRC_4544() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomRCList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String rcEmailID = null;
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");		
		logger.info("The email address is "+rcEmailID);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcEmailID);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(rcEmailID.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+rcEmailID.toLowerCase().trim());		
		while(true){
			if(crmHomePage.isSearchResultHasActiveUser()==false){
				logger.info("No active user in the search results..searching new user");
				randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				rcEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");		
				logger.info("The email address is "+rcEmailID);
				s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
				crmHomePage.enterTextInSearchFieldAndHitEnter(rcEmailID);
				emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
			}else{
				break;
			}
		}
		crmHomePage.clickNameWithActiveStatusInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Action"),"Action label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Name"),"Name label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("ProfileName"),"ProfileName label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Default"),"Default label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Address Line 1"),"Address Line 1 label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Address Line 2"),"Address Line 2 label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Address Line 3"),"Address Line 3 label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Locale"),"Locale label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Region"),"Region label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Postal code"),"Postal code label is not present in Shipping address section");

		String shippingProfilesCount = crmAccountDetailsPage.getShippingProfilesCount();
		String countDisplayedWithShippingLink = crmAccountDetailsPage.getCountDisplayedWithLink("Shipping Profiles");
		s_assert.assertTrue(shippingProfilesCount.equals(countDisplayedWithShippingLink), "shipping profiles count = "+shippingProfilesCount+"while count Displayed With Shipping Link = "+countDisplayedWithShippingLink);
		s_assert.assertTrue(crmAccountDetailsPage.isOnlyOneShippingProfileIsDefault(),"default shipping profiles is not one");

		s_assert.assertAll();
	}

	//Hybris Project-4537:View Billing profile for a consultant
	@Test
	public void testViewBillingProfileForConsultant_4537() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
		logger.info("The email address is "+consultantEmailID);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(consultantEmailID.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+consultantEmailID.toLowerCase().trim());		
		while(true){
			if(crmHomePage.isSearchResultHasActiveUser()==false){
				logger.info("No active user in the search results..searching new user");
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
				logger.info("The email address is "+consultantEmailID);
				s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
				crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
				emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
			}else{
				break;
			}
		}
		crmHomePage.clickNameWithActiveStatusInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionPresent("Action"),"Action label is not present in Billing profiles section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionPresent("Name"),"Name label is not present in Billing profiles section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionPresent("Profile Name"),"Profile Name label is not present in Billing profiles section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionPresent("Default"),"Default label is not present in Billing profiles section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionPresent("Name On Card"),"Name On Card label is not present in Billing profiles section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionPresent("Valid Thru"),"Valid Thru label is not present in Billing profiles section");

		String billingProfilesCount = crmAccountDetailsPage.getBillingProfilesCount();
		String countDisplayedWithBillingLink = crmAccountDetailsPage.getCountDisplayedWithLink("Billing");
		s_assert.assertTrue(billingProfilesCount.equals(countDisplayedWithBillingLink), "billing profiles count = "+billingProfilesCount+"while count Displayed With Shipping Link = "+countDisplayedWithBillingLink);
		s_assert.assertTrue(crmAccountDetailsPage.isOnlyOneBillingProfileIsDefault(),"default billing profiles is not one");

		crmAccountDetailsPage.clickRandomBillingProfile();
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingProfileDetailSectionOnNewTabPresent("Profile Name"),"Profile Name label is not present in Billing profile detail section on");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingProfileDetailSectionOnNewTabPresent("Account"),"Account label is not present in Billing profile detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingProfileDetailSectionOnNewTabPresent("Name On Card"),"Name On Card label is not present in Billing profile detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingProfileDetailSectionOnNewTabPresent("Payment Type"),"Payment Type label is not present in Billing profile detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingProfileDetailSectionOnNewTabPresent("Payment Vendor"),"Payment Vendor label is not present in Billing profile detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingProfileDetailSectionOnNewTabPresent("Default"),"Default label is not present in Billing profile detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingProfileDetailSectionOnNewTabPresent("Valid Thru"),"Valid Thru label is not present in Billing profile detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingProfileDetailSectionOnNewTabPresent("End Date"),"End Date label is not present in Billing profile detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingProfileDetailSectionOnNewTabPresent("Start Date"),"Start Date label is not present in Billing profile detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingProfileDetailSectionOnNewTabPresent("Last Modified By"),"Last Modified By label is not present in Billing profile detail section");

		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Address Line 1"),"Address Line 1 label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Address Line 2"),"Address Line 2 label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Address Line 3"),"Address Line 3 label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Locale"),"Locale label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Sub Region"),"Sub Region label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Region"),"Region label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Postal code"),"Postal code label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Latitude"),"Latitude label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Longitude"),"Longitude label is not present in Billing Address section");

		s_assert.assertTrue(crmAccountDetailsPage.isCreditCardOnBillingProfileDetailSectionOnNewTabIsSixteenEncryptedDigit(),"credit card number on billing profile detail section is either not of 16 digit or not encrypted");
		s_assert.assertTrue(crmAccountDetailsPage.isExpiryYearOnBillingProfileDetailSectionOnNewTabIsInYYYYFormat(),"expiry year format on billing profile detail section is not yyyy");
		s_assert.assertAll();
	}


	//Hybris Project-4503:Edit Consultant Account details
	@Test
	public void testEditConsultantAccountDetails_4503() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The email address is "+consultantEmailID); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		//Click edit account button under Account detail section
		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");
		//Edit Account Information by updating account name with gap between the words
		crmAccountDetailsPage.updateAccountNameField(TestConstants.CRM_VALID_ACCOUNT_NAME);
		//save Account information
		crmAccountDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify respective account name should be updated in sales force
		s_assert.assertTrue(crmAccountDetailsPage.getAccountName().contains(TestConstants.CRM_VALID_ACCOUNT_NAME),"Äccount name is not updated!!");
		//Click edit account button under Account detail section
		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");
		//Edit Account Information by updating account name with single name
		crmAccountDetailsPage.updateAccountNameField(TestConstants.CRM_INVALID_ACCOUNT_NAME);
		//save Account information
		crmAccountDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify Error should be displayed stating first and last name should be present
		s_assert.assertTrue(crmAccountDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for Account name field");
		//update account name field with valid name
		crmAccountDetailsPage.updateAccountNameField(TestConstants.CRM_VALID_ACCOUNT_NAME);
		//save Account information
		crmAccountDetailsPage.clickSaveBtnUnderAccountDetail();
		//Click edit account button under Account detail section
		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");
		//Edit Account Information by updating Recognition Name field
		crmAccountDetailsPage.updateRecognitionNameField(TestConstants.CRM_ACT_INFORMATION_RECOGNITION_NAME);
		//save Account information
		crmAccountDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify updated Recognition Name in sales force
		s_assert.assertTrue(crmAccountDetailsPage.getRecognitionName().equalsIgnoreCase(TestConstants.CRM_ACT_INFORMATION_RECOGNITION_NAME),"Recognition name is not updated");
		s_assert.assertAll();
	}

	//Hybris Project-4535:Verify Display of Autoship details for a Consultant
	@Test
	public void testVerifyDisplayAutoshipDetailsForConsultant_4535() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The first name is "+consultantEmailID); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();

		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Name"),"Name mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Autoship Type"),"Autoship Type mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Autoship Status"),"Autoship Status mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Status"),"Status mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Last OrderDate"),"Last OrderDate mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Next Order Date"),"Next Order Date mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Total"),"Total mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("QV"),"QV mouse hover section is not displayed in Autoships section in account details page");

		crmAccountDetailsPage.getCountAutoships();
		crmAccountDetailsPage.clickAutoships();
		s_assert.assertTrue(crmAccountDetailsPage.getCountAutoships().equals(crmAccountDetailsPage.getCountAutoshipNumber()), "Autoships Numbers are not equal");
		crmAccountDetailsPage.clickFirstAutoshipID();

		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Autoship Number"),"Autoship Number is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Autoship Type"),"Autoship Type is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Autoship Status"),"Autoship Status is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Do Not Ship"),"Autoship Do not ship is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Start Date"),"Autoship Start Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("End Date"),"Autoship Status is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Completion Date"),"Autoship Completion Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Account"),"Autoship Account is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Source"),"Autoship Source is not Present"); 
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Last OrderDate"),"Autoship Last Order Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Next Order Date"),"Autoship Next Order Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Last Modified By"),"Autoship Last Modified By is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Created By"),"Autoship Created By is not Present");

		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Is Tax Exempt"),"In Pending Autoship Breakdown Tax Exempt is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("QV"),"In Pending Autoship Breakdown QV is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("CV"),"In Pending Autoship Breakdown CV is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Product Discount"),"In Pending Autoship Breakdown Product Discount is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Total Discount"),"In Pending Autoship Breakdown Total Discount is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Sub Total"),"In Pending Autoship Breakdown Sub Total is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Taxable Total"),"In Pending Autoship Breakdown Taxable Total is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Total"),"In Pending Autoship Breakdown Total is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Delay Count"),"In Pending Autoship Breakdown Delay Count is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Fuel Surcharge"),"In Pending Autoship Breakdown  Fuel Surcharge is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Residential Surcharge"),"In Pending Autoship Breakdown Residential Surcharge is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Product Tax"),"In Pending Autoship Breakdown Product Tax is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Total Tax"),"In Pending Autoship Breakdown Total Tax is not Present");  
		s_assert.assertAll();
	}

	//Hybris Project-4536:Verify Display of Autoship details for a PC
	@Test
	public void testVerifyDisplayAutoshipDetailsForPC_4536() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The first name is "+consultantEmailID); 
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();

		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Name"),"Name mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Autoship Type"),"Autoship Type mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Autoship Status"),"Autoship Status mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Status"),"Status mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Last OrderDate"),"Last OrderDate mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Next Order Date"),"Next Order Date mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Total"),"Total mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("QV"),"QV mouse hover section is not displayed in Autoships section in account details page");

		crmAccountDetailsPage.getCountAutoships();
		crmAccountDetailsPage.clickAutoships();
		s_assert.assertTrue(crmAccountDetailsPage.getCountAutoships().equals(crmAccountDetailsPage.getCountAutoshipNumber()), "Autoships Numbers are not equal");
		crmAccountDetailsPage.clickFirstAutoshipID();

		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Autoship Number"),"Autoship Number is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Autoship Type"),"Autoship Type is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Autoship Status"),"Autoship Status is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Do Not Ship"),"Autoship Do not ship is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Start Date"),"Autoship Start Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("End Date"),"Autoship Status is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Completion Date"),"Autoship Completion Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Account"),"Autoship Account is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Source"),"Autoship Source is not Present"); 
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Last OrderDate"),"Autoship Last Order Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Next Order Date"),"Autoship Next Order Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Last Modified By"),"Autoship Last Modified By is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Created By"),"Autoship Created By is not Present");

		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Is Tax Exempt"),"In Pending Autoship Breakdown Tax Exempt is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("QV"),"In Pending Autoship Breakdown QV is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("CV"),"In Pending Autoship Breakdown CV is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Product Discount"),"In Pending Autoship Breakdown Product Discount is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Total Discount"),"In Pending Autoship Breakdown Total Discount is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Sub Total"),"In Pending Autoship Breakdown Sub Total is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Taxable Total"),"In Pending Autoship Breakdown Taxable Total is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Total"),"In Pending Autoship Breakdown Total is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Delay Count"),"In Pending Autoship Breakdown Delay Count is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Fuel Surcharge"),"In Pending Autoship Breakdown  Fuel Surcharge is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Residential Surcharge"),"In Pending Autoship Breakdown Residential Surcharge is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Product Tax"),"In Pending Autoship Breakdown Product Tax is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Total Tax"),"In Pending Autoship Breakdown Total Tax is not Present");  
		s_assert.assertAll();

	}
}


