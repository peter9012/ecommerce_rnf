package com.rf.test.website.crm.regression;

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

public class CRMRegressionTest extends RFWebsiteBaseTest{

	private static final Logger logger = LogManager
			.getLogger(CRMRegressionTest.class.getName());

	private CRMLoginPage crmLoginpage;
	private CRMHomePage crmHomePage;
	private CRMAccountDetailsPage crmAccountDetailsPage; 
	private String RFO_DB = null;

	//Hybris Project-4491:Add Shipping Profile for Consultant
	@Test
	public void testAddShippingProfileForConsultant_4491() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomConsultantList =  null;
		String addressLine = null;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;

		}else{
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = TestConstants.LAST_NAME;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
		logger.info("The username is "+consultantEmailID);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		crmAccountDetailsPage.clickAddNewShippingProfileBtn();
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstName+" "+lastName);
		crmAccountDetailsPage.enterShippingAddress(addressLine, city, province, postal, phoneNumber);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		String updatedProfileName = crmAccountDetailsPage.getDefaultSelectedShippingAddressName();
		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstName), "Expected default selected shipping profile name is "+shippingProfileFirstName+"Actual on UI "+updatedProfileName);
		s_assert.assertAll();
	}

	//Hybris Project-4492:Add Shipping Profile for PC
	@Test
	public void testAddShippingProfileForPC_4492() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomPCList =  null;
		String addressLine = null;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;

		}else{
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String pcEmailID = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum+"%%";
		String lastName = TestConstants.LAST_NAME;
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCList, "UserName");		
		logger.info("The username is "+pcEmailID);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		crmAccountDetailsPage.clickAddNewShippingProfileBtn();
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstName+" "+lastName);
		crmAccountDetailsPage.enterShippingAddress(addressLine, city, province, postal, phoneNumber);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		String updatedProfileName = crmAccountDetailsPage.getDefaultSelectedShippingAddressName();
		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstName), "Expected default selected shipping profile name is "+shippingProfileFirstName+"Actual on UI "+updatedProfileName);
		s_assert.assertAll();
	}

	//Hybris Project-4493:Add Shipping Profile for RC
	@Test(enabled=false)//WIP
	public void testAddShippingProfileForRC_4493() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();	
		List<Map<String, Object>> randomRCList =  null;
		String addressLine = null;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;

		}else{
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String rcEmailID = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = TestConstants.LAST_NAME;
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_EMAIL_ID_RFO,countryId),RFO_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");		
		logger.info("The username is "+rcEmailID);	
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		crmAccountDetailsPage.clickAddNewShippingProfileBtn();
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstName+" "+lastName);
		crmAccountDetailsPage.enterShippingAddress(addressLine, city, province, postal, phoneNumber);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		String updatedProfileName = crmAccountDetailsPage.getDefaultSelectedShippingAddressName();
		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstName), "Expected default selected shipping profile name is "+shippingProfileFirstName+"Actual on UI "+updatedProfileName);
		s_assert.assertAll();
	}

	// Hybris Project-4480:Add the Account Notes for PC
	@Test(enabled=false)//WIP
	public void testAddAccountNoteForPCUser_4480() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomPCUserList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String pcUserEmailID = null;
		String orderNote="This is automation note"+randomNum;
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");   
		logger.info("The username is "+pcUserEmailID); 

		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserEmailID);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(pcUserEmailID.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+pcUserEmailID.toLowerCase().trim());

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

	//Hybris Project-4481:Add the Account Notes for RC
	@Test(enabled=false)//WIP
	public void testAddAccountNoteForRC_4481() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomRCUserList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String rcUserEmailId = null;
		String accountId=null;
		String orderNote="This is automation note"+randomNum;

		randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
		rcUserEmailId = (String) getValueFromQueryResult(randomRCUserList, "UserName");  
		logger.info("The username is "+rcUserEmailId); 

		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserEmailId);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(rcUserEmailId.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+rcUserEmailId.toLowerCase().trim());

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
}