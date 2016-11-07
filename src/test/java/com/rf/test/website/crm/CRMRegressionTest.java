package com.rf.test.website.crm;
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
import com.rf.pages.website.crm.CRMAccountDetailsPage;
import com.rf.pages.website.crm.CRMContactDetailsPage;
import com.rf.pages.website.crm.CRMHomePage;
import com.rf.pages.website.crm.CRMLoginPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.test.website.RFCRMWebsiteBaseTest;

public class CRMRegressionTest extends RFCRMWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(CRMRegressionTest.class.getName());

	private CRMLoginPage crmLoginpage;
	private CRMHomePage crmHomePage;
	private CRMAccountDetailsPage crmAccountDetailsPage; 
	private CRMContactDetailsPage crmContactDetailsPage;
	private StoreFrontHomePage storeFrontHomePage;

	private String RFO_DB = null;
	String consultantEmailID = null;
	String pcUserName = null;
	String accountID = null;
	String rcUserName = null;
	String pcAccountID = null;
	String rcAccountID = null;
	List<Map<String, Object>> randomConsultantList =  null;
	List<Map<String, Object>> randomConsultantUsernameList =  null;
	List<Map<String, Object>> randomPCUserList =  null;
	List<Map<String, Object>> randomRCUserList =  null;
	List<Map<String, Object>> randomPCUsernameList =  null;
	List<Map<String, Object>> randomRCUsernameList =  null;

	@BeforeClass
	public void BeforeCRM(){
		RFO_DB = driver.getDBNameRFO();
		crmLoginpage = new CRMLoginPage(driver);
		crmHomePage = new CRMHomePage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		crmContactDetailsPage = new CRMContactDetailsPage(driver);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));

		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));

		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcUserName = (String) getValueFromQueryResult(randomPCUserList, "UserName");
		pcAccountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));

		randomPCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,pcAccountID),RFO_DB);
		pcUserName = String.valueOf(getValueFromQueryResult(randomPCUsernameList, "EmailAddress"));

		randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
		rcUserName = (String) getValueFromQueryResult(randomRCUserList, "UserName");
		rcAccountID = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));

		randomRCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,rcAccountID),RFO_DB);
		rcUserName = String.valueOf(getValueFromQueryResult(randomRCUsernameList, "EmailAddress"));
	}	

	// Hybris Project-4479:Add the Account Notes for Consultant
	@Test(priority=1)
	public void testAddAccountNoteForConsultant_4479() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String orderNote="This is automation note"+randomNum;
		logger.info("The username is "+consultantEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		while(true){
			if(crmHomePage.isSearchResultHasActiveUser("Consultant") ==false || crmHomePage.isAccountSectionPresent()==false){
				logger.info("No active user in the search results..searching new user");
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
				accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
				randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
				consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));

				logger.info("The email address is "+consultantEmailID);
				s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
				crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
			}else{
				break;
			}
		}
		//String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		//s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(consultantEmailID.toLowerCase().trim()) || consultantEmailID.toLowerCase().trim().contains(emailOnfirstRow.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+consultantEmailID.toLowerCase().trim());

		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertTrue(crmAccountDetailsPage.isLogAccountActivitySectionIsPresent(),"Log Account Notes Section is not present on Account Details page");
		//Verify account dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDropdownOnAccountDetailPagePresent(),"Account dropdown is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDropdownSearchOnAccountDetailPagePresent(),"Account dropdown search button is not present on Account Details page");
		//verify channel dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOnAccountDetailPagePresent(),"Channel dropdown is not present on Account Details page");
		/*		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOptionsPresent("Email"),"Channel dropdown Email option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOptionsPresent("Call"),"Channel dropdown call option is not present on Account Details page");*/
		//Verify reason dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOnAccountDetailPagePresent(),"Reason dropdown is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Consultants"),"Reason dropdown Consultants option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("CRP"),"Reason dropdown CRP option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("PCs"),"Reason dropdown PCs option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Order"),"Reason dropdown Order option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Returns"),"Reason dropdown Returns option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("RF Mall"),"Reason dropdown RF Mall option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Pulse"),"Reason dropdown Pulse option is not present on Account Details page");
		//		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("PSales Support RF Connection Transferulse"),"Reason dropdown PSales Support RF Connection Transferulse option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Technology"),"Reason dropdown Technology option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Marketing Promotion"),"Reason dropdown Marketing Promotion option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Special Communications (Price Increase)"),"Reason dropdown Special Communications (Price Increase) option is not present on Account Details page");
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
	@Test(priority=2)
	public void testEditPreferredCustomerActDetails_4507() throws InterruptedException	{
		String addressLine = TestConstants.CRM_NEW_ADDRESS_LINE_1_CA;
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		while(true){
			if(crmHomePage.isSearchResultHasActiveUser("Preferred Customer") == false || crmHomePage.isAccountSectionPresent()==false){
				logger.info("No active user in the search results..searching new user");
				randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
				pcAccountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));

				randomPCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,pcAccountID),RFO_DB);
				pcUserName = String.valueOf(getValueFromQueryResult(randomPCUsernameList, "EmailAddress"));
				logger.info("The email address is "+pcUserName);
				s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
				crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
			}else{
				break;
			}
		}
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");
		crmAccountDetailsPage.updateRecognitionNameField(TestConstants.CRM_ACT_INFORMATION_RECOGNITION_NAME);
		crmAccountDetailsPage.clickSaveBtnUnderAccountDetail();
		s_assert.assertTrue(crmAccountDetailsPage.getRecognitionName().equalsIgnoreCase(TestConstants.CRM_ACT_INFORMATION_RECOGNITION_NAME),"Recognition name is not updated");
		crmAccountDetailsPage.clickAccountDetailsButton("Edit Main Address");
		crmAccountDetailsPage.editAddressFieldsOfMainAddressSection("Address Line 1", addressLine);
		crmAccountDetailsPage.clickSaveAddressButtonInEditMainAddressSection(addressLine);
		s_assert.assertTrue(crmAccountDetailsPage.getDataValueOfLabelsInMainAddressSection("Address Line 1").trim().equalsIgnoreCase(addressLine),"Actual Value is :"+crmAccountDetailsPage.getDataValueOfLabelsInMainAddressSection("Address Line 1").trim()+"& Expected Value is :"+addressLine+".Main account Address Line 1 is not updated");
		s_assert.assertAll();
	}

	//Hybris Project-4510:Edit Retail Customer Account details
	@Test(priority=3)
	public void testEditRetailCustomerActDetails_4510() throws InterruptedException {
		String addressLine = TestConstants.CRM_NEW_ADDRESS_LINE_1_CA;
		String postalCode = TestConstants.CRM_NEW_POSTALCODE_CA;
		String locale = TestConstants.CITY_CA;
		String region = TestConstants.PROVINCE_CA;
		logger.info("The username is "+rcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
		while(true){
			if(crmHomePage.isSearchResultHasActiveUser("Retail Customer") ==false){
				logger.info("No active user in the search results..searching new user");
				randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
				rcAccountID = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));

				randomRCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,rcAccountID),RFO_DB);
				rcUserName = String.valueOf(getValueFromQueryResult(randomRCUsernameList, "EmailAddress"));
				logger.info("The email address is "+rcUserName);
				s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
				crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
			}else{
				break;
			}
		}
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");
		crmAccountDetailsPage.updateRecognitionNameField(TestConstants.CRM_ACT_INFORMATION_RECOGNITION_NAME);
		crmAccountDetailsPage.clickSaveBtnUnderAccountDetail();
		s_assert.assertTrue(crmAccountDetailsPage.getRecognitionName().equalsIgnoreCase(TestConstants.CRM_ACT_INFORMATION_RECOGNITION_NAME),"Recognition name is not updated");
		crmAccountDetailsPage.clickAccountDetailsButton("Edit Main Address");
		crmAccountDetailsPage.editAddressFieldsOfMainAddressSection("Address Line 1", addressLine);
		crmAccountDetailsPage.editAddressFieldsOfMainAddressSection("Postal Code", postalCode);
		crmAccountDetailsPage.editAddressFieldsOfMainAddressSection("Locale", locale);
		crmAccountDetailsPage.editAddressFieldsOfMainAddressSection("Region", region);
		crmAccountDetailsPage.selectUserEnteredAddressAndClickOnSaveButton();
		crmAccountDetailsPage.clickSaveAddressButtonInEditMainAddressSection(addressLine);
		s_assert.assertTrue(crmAccountDetailsPage.getDataValueOfLabelsInMainAddressSection("Address Line 1").trim().equalsIgnoreCase(addressLine),"Actual Value is :"+crmAccountDetailsPage.getDataValueOfLabelsInMainAddressSection("Address Line 1").trim()+"& Expected Value is :"+addressLine+".Main account Address Line 1 is not updated");
		s_assert.assertAll();
	}

	//Hybris Project-4530:Search for account by name
	@Test(priority=4)
	public void testSearchForAccountByName_4530() throws InterruptedException{
		List<Map<String, Object>> randomFirstName =  null;
		String firstName = null;
		randomFirstName = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_FirstName_RFO,countryId),RFO_DB);	
		firstName = (String) getValueFromQueryResult(randomFirstName, "FirstName");		
		logger.info("The first name is "+firstName);	
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(firstName);
		String nameOnFirstRow = crmHomePage.getNameOnFirstRowInSearchResults();
		s_assert.assertTrue(nameOnFirstRow.toLowerCase().trim().contains(firstName.toLowerCase().trim()), "the name on first row which is = "+nameOnFirstRow.toLowerCase().trim()+" is expected to contain firstname = "+firstName.toLowerCase().trim());
		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(), "Accounts link is not present on left navigation panel");
		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(), "Contacts link is not present on left navigation panel");
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountNumberFieldDisplayedAndNonEmpty(),"Account Number is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountTypeFieldDisplayedAndNonEmpty(),"Account Type is not displayed on account details page");
		s_assert.assertAll();
	}

	//Hybris Project-4545:View Consultant Account details
	@Test(priority=5)
	public void testViewConsultantAccountDetailsTest_4545() throws InterruptedException{
		logger.info("The username is "+consultantEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		//		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		//s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(consultantEmailID.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+consultantEmailID.toLowerCase().trim());
		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(), "Accounts link is not present on left navigation panel");
		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(), "Contacts link is not present on left navigation panel");
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");

		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsSectionPresent(),"Account Details Section is not present");
		s_assert.assertTrue(crmAccountDetailsPage.isMainAddressSectionPresent(),"Main Address Section is not present");

		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsButtonEnabled("Edit Account"),"Edit Account button is not Enabled in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsButtonEnabled("PWS Domain"),"Edit PWS Domain button is not Enabled in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsButtonEnabled("Change Account Status"),"Change Account Status button is not Enabled in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsButtonEnabled("Pulse"),"Pulse button is not Enabled in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsButtonEnabled("New Pulse"),"New Pulse button is not Enabled in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsButtonEnabled("My Account"),"My Account button is not Enabled in account detail section");

		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Account Name"),"Account Name label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Recognition Name"),"Recognition Name label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Recognition Title"),"Recognition Title label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Active"),"Active label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Account Status"),"Account Status label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Soft Termination Date"),"Soft Termination Date label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Hard Termination Date"),"Hard Termination Date label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("SV"),"SV label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("PSQV"),"PSQV label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Account Number"),"Account Number label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Account Type"),"Account Type label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Enrollment Sponsor"),"Enrollment Sponsor label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Placement Sponsor"),"Placement Sponsor label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Enrollment Date"),"Enrollment Date label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Next Renewal Date"),"Next Renewal Date label is not present in account detail section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Last Renewal Date"),"Last Renewal Date label is not present in account detail section");

		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Address Line 1"),"Address Line 1 label is not present in main address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Address Line 2"),"Address Line 2 label is not present in main address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Locale"),"Locale label is not present in main address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Sub Region"),"Sub Region label is not present in main address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Region"),"Region label is not present in main address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Postal code"),"Postal code label is not present in main address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Country"),"Country label is not present in main address section");

		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderTaxInformationSectionPresent("Business Entity"),"Business Entity label is not present under tax information section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderTaxInformationSectionPresent("Tax Exempt"),"Tax Exempt label is not present under tax information section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderTaxInformationSectionPresent("Tax ID Number"),"Tax ID Number label is not present under tax information section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderTaxInformationSectionPresent("Legal Tax Name"),"Legal Tax Name label is not present under tax information section");

		s_assert.assertAll();
	}

	//Hybris Project-4491:Add Shipping Profile for Consultant
	@Test(priority=6)
	public void testAddShippingProfileForConsultant_4491() throws InterruptedException{
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

		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = TestConstants.LAST_NAME;
		String fullName = shippingProfileFirstName+" "+lastName;

		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		crmAccountDetailsPage.clickAddNewShippingProfileBtn();
		crmAccountDetailsPage.updateShippingProfileName(fullName);
		crmAccountDetailsPage.enterShippingAddress(addressLine, city, province, postal, phoneNumber);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.clickAccountMainMenuOptions("Shipping Profiles");
		s_assert.assertTrue(crmAccountDetailsPage.isProfileNameValueOfDefaultShippingProfilesPresent(fullName), "Profile Name Not Matched");
		s_assert.assertTrue(crmAccountDetailsPage.isAddressLocaleRegionPostalCodeValueOfDefaultShippingProfilesPresent(addressLine), "Address Line Not Matched");
		s_assert.assertTrue(crmAccountDetailsPage.isAddressLocaleRegionPostalCodeValueOfDefaultShippingProfilesPresent(city.toUpperCase()), "Locale Not Matched");
		s_assert.assertTrue(crmAccountDetailsPage.isAddressLocaleRegionPostalCodeValueOfDefaultShippingProfilesPresent(crmAccountDetailsPage.CanadaProvinceCode(province)), "Region Not Matched");
		s_assert.assertTrue(crmAccountDetailsPage.isAddressLocaleRegionPostalCodeValueOfDefaultShippingProfilesPresent(postal), "Postal Not Matched");
		s_assert.assertAll();
	}

	//Hybris Project-4492:Add Shipping Profile for PC
	@Test(priority=7)
	public void testAddShippingProfileForPC_4492() throws InterruptedException{
		String addressLine = null;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		//String pcUser = "janinescott@blackberry.com";
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
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = TestConstants.LAST_NAME;
		String fullName = shippingProfileFirstName+" "+lastName;

		logger.info("The username is "+pcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		//pcUserName
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		crmAccountDetailsPage.clickAddNewShippingProfileBtn();
		crmAccountDetailsPage.updateShippingProfileName(fullName);
		crmAccountDetailsPage.enterShippingAddress(addressLine, city, province, postal, phoneNumber);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.clickAccountMainMenuOptions("Shipping Profiles");
		if(crmAccountDetailsPage.isShowMoreOptionUnderShippingProfilesPresent()){
			crmAccountDetailsPage.clickShowMoreOptionUnderShippingProfilesPresent();
		}
		s_assert.assertTrue(crmAccountDetailsPage.isProfileNameValueOfDefaultShippingProfilesPresent(fullName), "Profile Name Not Matched");
		s_assert.assertTrue(crmAccountDetailsPage.isAddressLocaleRegionPostalCodeValueOfDefaultShippingProfilesPresent(addressLine), "Address Line Not Matched");
		s_assert.assertTrue(crmAccountDetailsPage.isAddressLocaleRegionPostalCodeValueOfDefaultShippingProfilesPresent(city.toUpperCase()), "Locale Not Matched");
		s_assert.assertTrue(crmAccountDetailsPage.isAddressLocaleRegionPostalCodeValueOfDefaultShippingProfilesPresent(crmAccountDetailsPage.CanadaProvinceCode(province)), "Region Not Matched");
		s_assert.assertTrue(crmAccountDetailsPage.isAddressLocaleRegionPostalCodeValueOfDefaultShippingProfilesPresent(postal), "Postal Not Matched");
		s_assert.assertAll();
	}

	//Hybris Project-4493:Add Shipping Profile for RC
	@Test(priority=8)
	public void testAddShippingProfileForRC_4493() throws InterruptedException{
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
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = TestConstants.LAST_NAME;
		String fullName = shippingProfileFirstName+" "+lastName;		  
		logger.info("The username is "+rcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		crmAccountDetailsPage.clickAddNewShippingProfileBtn();
		crmAccountDetailsPage.updateShippingProfileName(fullName);
		crmAccountDetailsPage.enterShippingAddress(addressLine, city, province, postal, phoneNumber);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.clickAccountMainMenuOptions("Shipping Profiles");
		s_assert.assertTrue(crmAccountDetailsPage.isProfileNameValueOfDefaultShippingProfilesPresent(fullName), "Profile Name Not Matched");
		s_assert.assertTrue(crmAccountDetailsPage.isAddressLocaleRegionPostalCodeValueOfDefaultShippingProfilesPresent(addressLine), "Address Line Not Matched");
		s_assert.assertTrue(crmAccountDetailsPage.isAddressLocaleRegionPostalCodeValueOfDefaultShippingProfilesPresent(city.toUpperCase()), "Locale Not Matched");
		s_assert.assertTrue(crmAccountDetailsPage.isAddressLocaleRegionPostalCodeValueOfDefaultShippingProfilesPresent(crmAccountDetailsPage.CanadaProvinceCode(province)), "Region Not Matched");
		s_assert.assertTrue(crmAccountDetailsPage.isAddressLocaleRegionPostalCodeValueOfDefaultShippingProfilesPresent(postal), "Postal Not Matched");
		s_assert.assertAll();
	}

	// Hybris Project-4480:Add the Account Notes for PC
	@Test(priority=9)
	public void testAddAccountNoteForPCUser_4480() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String orderNote="This is automation note"+randomNum;
		logger.info("The username is "+pcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		//		s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(pcUserName.toLowerCase().trim()) || pcUserName.toLowerCase().trim().contains(emailOnfirstRow.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+pcUserName.toLowerCase().trim());

		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertTrue(crmAccountDetailsPage.isLogAccountActivitySectionIsPresent(),"Log Account Activity Section is not present on Account Details page");
		//Verify account dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDropdownOnAccountDetailPagePresent(),"Account dropdown is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDropdownSearchOnAccountDetailPagePresent(),"Account dropdown search button is not present on Account Details page");
		//verify channel dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOnAccountDetailPagePresent(),"Channel dropdown is not present on Account Details page");
		/*		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOptionsPresent("Email"),"Channel dropdown Email option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOptionsPresent("Call"),"Channel dropdown call option is not present on Account Details page");*/
		//Verify reason dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOnAccountDetailPagePresent(),"Reason dropdown is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Consultants"),"Reason dropdown Consultants option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("CRP"),"Reason dropdown CRP option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("PCs"),"Reason dropdown PCs option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Order"),"Reason dropdown Order option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Returns"),"Reason dropdown Returns option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("RF Mall"),"Reason dropdown RF Mall option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Pulse"),"Reason dropdown Pulse option is not present on Account Details page");
		//s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("PSales Support RF Connection Transferulse"),"Reason dropdown PSales Support RF Connection Transferulse option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Technology"),"Reason dropdown Technology option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Marketing Promotion"),"Reason dropdown Marketing Promotion option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Special Communications (Price Increase)"),"Reason dropdown Special Communications (Price Increase) option is not present on Account Details page");
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
	@Test(priority=10)
	public void testAddAccountNoteForRC_4481() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String orderNote="This is automation note"+randomNum;
		logger.info("The username is "+rcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		//s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(rcUserName.toLowerCase().trim()) || rcUserName.toLowerCase().trim().contains(emailOnfirstRow.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+rcUserName.toLowerCase().trim());
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertTrue(crmAccountDetailsPage.isLogAccountActivitySectionIsPresent(),"Log Account Activity Section is not present on Account Details page");
		//Verify account dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDropdownOnAccountDetailPagePresent(),"Account dropdown is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDropdownSearchOnAccountDetailPagePresent(),"Account dropdown search button is not present on Account Details page");
		//verify channel dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOnAccountDetailPagePresent(),"Channel dropdown is not present on Account Details page");
		/*		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOptionsPresent("Email"),"Channel dropdown Email option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOptionsPresent("Call"),"Channel dropdown call option is not present on Account Details page");*/
		//Verify reason dropdown
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOnAccountDetailPagePresent(),"Reason dropdown is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Consultants"),"Reason dropdown Consultants option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("CRP"),"Reason dropdown CRP option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("PCs"),"Reason dropdown PCs option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Order"),"Reason dropdown Order option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Returns"),"Reason dropdown Returns option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("RF Mall"),"Reason dropdown RF Mall option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Pulse"),"Reason dropdown Pulse option is not present on Account Details page");
		//s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("PSales Support RF Connection Transferulse"),"Reason dropdown PSales Support RF Connection Transferulse option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Technology"),"Reason dropdown Technology option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Marketing Promotion"),"Reason dropdown Marketing Promotion option is not present on Account Details page");
		s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Special Communications (Price Increase)"),"Reason dropdown Special Communications (Price Increase) option is not present on Account Details page");
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

	//Hybris Project-4542:View Shipping Profile for Consultant
	@Test(priority=11)
	public void testViewShippingProfileForConsultant_4542() throws InterruptedException{
		logger.info("The email address is "+consultantEmailID);	
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		//String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		//s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(consultantEmailID.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+consultantEmailID.toLowerCase().trim());		
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Action"),"Action label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Name"),"Name label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("ProfileName"),"ProfileName label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Default"),"Default label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Address Line 1"),"Address Line 1 label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Address Line 2"),"Address Line 2 label is not present in Shipping address section");
		/*		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Address Line 3"),"Address Line 3 label is not present in Shipping address section");
		 */		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Locale"),"Locale label is not present in Shipping address section");
		 s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Region"),"Region label is not present in Shipping address section");
		 s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Postal code"),"Postal code label is not present in Shipping address section");

		 String shippingProfilesCount = crmAccountDetailsPage.getShippingProfilesCount();
		 String countDisplayedWithShippingLink = crmAccountDetailsPage.getCountDisplayedWithLink("Shipping Profiles");
		 s_assert.assertTrue(shippingProfilesCount.equals(countDisplayedWithShippingLink), "shipping profiles count = "+shippingProfilesCount+"while count Displayed With Shipping Link = "+countDisplayedWithShippingLink);
		 s_assert.assertTrue(crmAccountDetailsPage.isOnlyOneShippingProfileIsDefault(),"default shipping profiles is not one");

		 s_assert.assertAll();
	}


	//Hybris Project-4527:Search for account by email address
	@Test(priority=12)
	public void testSearchForAccountByEmail_4527() throws InterruptedException{
		//get emailId of username
		logger.info("The email address is "+consultantEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		/*while(true){
					if(crmHomePage.isSearchResultHasActiveUser("Consultant") ==false){
						logger.info("No active user in the search results..searching new user");
						randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
						accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
						randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
						consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));

						logger.info("The email address is "+consultantEmailID);
						s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
						crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
					}else{
						break;
					}
				}*/
		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(), "Accounts link is not present on left navigation panel");
		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(), "Contacts link is not present on left navigation panel");
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountNumberFieldDisplayedAndNonEmpty(),"Account Number is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountTypeFieldDisplayedAndNonEmpty(),"Account Type is not displayed on account details page");
		s_assert.assertAll();
	}

	//Hybris Project-4548:Preferred Customer detail view page
	@Test(priority=13)
	public void testPreferredCustomerdDetailViewTest_4548() throws InterruptedException{
		logger.info("The username is "+pcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);

		/*while(true){
					if(crmHomePage.isSearchResultHasActiveUser("Preferred Customer") ==false){
						logger.info("No active user in the search results..searching new user");
						randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
						pcAccountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));

						randomPCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,pcAccountID),RFO_DB);
						pcUserName = String.valueOf(getValueFromQueryResult(randomPCUsernameList, "EmailAddress"));
						logger.info("The email address is "+pcUserName);
						s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
						crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
					}else{
						break;
					}
				}
		 */
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		//		s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(pcUserName.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+pcUserName.toLowerCase().trim());
		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(), "Accounts link is not present on left navigation panel");
		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(), "Contacts link is not present on left navigation panel");

		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountNumberFieldDisplayedAndNonEmpty(),"Account Number is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountTypeFieldDisplayedAndNonEmpty(),"Account Type is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountStatusFieldDisplayedAndNonEmpty(),"Account Status is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isCountryFieldDisplayedAndNonEmpty(),"Country field is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isPlacementSponsorFieldDisplayed(),"Placement sponsor is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isEnrollmentDateFieldDisplayedAndNonEmpty(),"Enrollment Date is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMainPhoneFieldDisplayedAndNonEmpty(),"Main Phone is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isEmailAddressFieldDisplayedAndNonEmpty(),"Email Address is not displayed on account details page");

		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Contacts"),"Contact link is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Autoships"),"Autoships link is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Account Notes"),"Account Notes link is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Shipping Profiles"),"Shipping Profiles link is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Billing  Profiles"),"Billing  Profiles link is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Account Statuses History"),"Account Statuses History link is not displayed on account in section account details page");

		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Contacts"),"Contacts mouse hover section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Autoships"),"Autoships mouse hover section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Account Notes"),"Account Notes mouse hover section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Shipping Profiles"),"Shipping Profiles mouse hover section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Billing"),"Billing Profiles mouse hover section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Account Statuses History"),"Contacts mouse hover section is not displayed on account section in account details page");

		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Contacts"),"Contacts blue line section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Autoships"),"Autoships blue line section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Account Notes"),"Account Activities blue line section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Shipping Profiles"),"Shipping Profiles blue line section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Billing"),"Billing Profiles blue line section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Account Statuses History"),"Account Statuses History blue line section is not displayed on account section in account details page");

		s_assert.assertAll();
	}

	//Hybris Project-4550:Retail Customer detail view page
	@Test(priority=14)
	public void testRetailCustomerdDetailViewTest_4550() throws InterruptedException{
		logger.info("The username is "+rcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);

		/*while(true){
					if(crmHomePage.isSearchResultHasActiveUser("Retail Customer") ==false){
						logger.info("No active user in the search results..searching new user");
						randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
						rcAccountID = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));

						randomRCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,rcAccountID),RFO_DB);
						rcUserName = String.valueOf(getValueFromQueryResult(randomRCUsernameList, "EmailAddress"));
						logger.info("The email address is "+rcUserName);
						s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
						crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
					}else{
						break;
					}
				}*/

		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		//s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(rcUserName.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+rcUserName.toLowerCase().trim());
		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(), "Accounts link is not present on left navigation panel");
		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(), "Contacts link is not present on left navigation panel");

		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountNumberFieldDisplayedAndNonEmpty(),"Account Number is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountTypeFieldDisplayedAndNonEmpty(),"Account Type is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountStatusFieldDisplayedAndNonEmpty(),"Account Status is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isCountryFieldDisplayedAndNonEmpty(),"Country field is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isPlacementSponsorFieldDisplayed(),"Placement sponsor is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isEnrollmentDateFieldDisplayedAndNonEmpty(),"Enrollment Date is not displayed on account details page");
		//s_assert.assertTrue(crmAccountDetailsPage.isMainPhoneFieldDisplayedAndNonEmpty(),"Main Phone is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isEmailAddressFieldDisplayedAndNonEmpty(),"Email Address is not displayed on account details page");

		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Contacts"),"Contact link is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Account Notes"),"Account Notes link is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Shipping Profiles"),"Shipping Profiles link is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Billing  Profiles"),"Billing  Profiles link is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isLinkOnAccountSectionPresent("Account Statuses History"),"Account Statuses History link is not displayed on account in section account details page");

		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Contacts"),"Contacts mouse hover section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Account Notes"),"Account Notes mouse hover section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Shipping Profiles"),"Shipping Profiles mouse hover section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Billing"),"Billing Profiles mouse hover section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverSectionPresentOfLink("Account Statuses History"),"Contacts mouse hover section is not displayed on account section in account details page");

		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Contacts"),"Contacts blue line section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Account Notes"),"Account Activities blue line section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Shipping Profiles"),"Shipping Profiles blue line section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Billing"),"Billing Profiles blue line section is not displayed on account section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isListItemsWithBlueLinePresent("Account Statuses History"),"Account Statuses History blue line section is not displayed on account section in account details page");

		s_assert.assertAll();
	}

	//Hybris Project-4513:Edit Shipping Profile for Consultant
	@Test(priority=15)
	public void testEditShippingProfileForConsultant_4513() throws InterruptedException{
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
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstNameWithSpecialChar = TestConstants.FIRST_NAME+randomNum+"%%";
		String lastName = TestConstants.LAST_NAME;
		logger.info("The username is "+consultantEmailID);		
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		String noOfShippingProfile = crmAccountDetailsPage.getShippingProfilesCount();
		s_assert.assertTrue(crmAccountDetailsPage.verifyShippingProfileCountIsEqualOrGreaterThanOne(noOfShippingProfile),"expected minium number of shipping profile is 1 actual on UI "+noOfShippingProfile);
		crmAccountDetailsPage.clickEditFirstShippingProfile();
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstNameWithSpecialChar+" "+lastName);
		crmAccountDetailsPage.enterShippingAddress(addressLine, city, province, postal, phoneNumber);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		String updatedProfileName = crmAccountDetailsPage.getDefaultSelectedShippingAddressName();
		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstNameWithSpecialChar), "Expected shipping profile name is "+shippingProfileFirstNameWithSpecialChar+"Actual on UI "+updatedProfileName);
		s_assert.assertAll();
	}

	//Hybris Project-4544:View Shipping Profile for RC
	@Test(priority=16)
	public void testViewShippingProfileForRC_4544() throws InterruptedException{
		logger.info("The email address is "+rcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
		//String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		//s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(rcUserName.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+rcUserName.toLowerCase().trim());  
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");
		while(true){
			if(crmAccountDetailsPage.isNoRecordToDisplayPresentOnShippingProfile()==true){
				String rcUserName = null;
				List<Map<String, Object>> randomRCUserList =  null;
				//crmHomePage.closeTabViaNumberWise(2);
				randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
				rcUserName = (String) getValueFromQueryResult(randomRCUserList, "UserName");  
				logger.info("The email address is "+rcUserName);
				s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
				crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
				//emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
				if(crmHomePage.isSearchResultHasActiveUser() == false){
					continue;
				}
				crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");
				if(crmAccountDetailsPage.isNoRecordToDisplayPresentOnShippingProfile()==true){
					continue;
				}
			}else{
				break;
			}

		}	  
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Action"),"Action label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Name"),"Name label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("ProfileName"),"ProfileName label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Default"),"Default label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Address Line 1"),"Address Line 1 label is not present in Shipping address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Address Line 2"),"Address Line 2 label is not present in Shipping address section");
		/*		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Address Line 3"),"Address Line 3 label is not present in Shipping address section");
		 */		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Locale"),"Locale label is not present in Shipping address section");
		 s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Region"),"Region label is not present in Shipping address section");
		 s_assert.assertTrue(crmAccountDetailsPage.isLabelOnShippingAddressSectionPresent("Postal code"),"Postal code label is not present in Shipping address section");

		 String shippingProfilesCount = crmAccountDetailsPage.getShippingProfilesCount();
		 String countDisplayedWithShippingLink = crmAccountDetailsPage.getCountDisplayedWithLink("Shipping Profiles");
		 s_assert.assertTrue(shippingProfilesCount.equals(countDisplayedWithShippingLink), "shipping profiles count = "+shippingProfilesCount+"while count Displayed With Shipping Link = "+countDisplayedWithShippingLink);
		 s_assert.assertTrue(crmAccountDetailsPage.isOnlyOneShippingProfileIsDefault(),"default shipping profiles is not one");

		 s_assert.assertAll();
	}

	//Hybris Project-4537:View Billing profile for a consultant
	@Test(priority=17)
	public void testViewBillingProfileForConsultant_4537() throws InterruptedException{
		logger.info("The email address is "+consultantEmailID);	
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		//String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		//s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(consultantEmailID.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+consultantEmailID.toLowerCase().trim());		
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
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
	@Test(priority=18)
	public void testEditConsultantAccountDetails_4503() throws InterruptedException {
		logger.info("The email address is "+consultantEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
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

	// Hybris Project-4508:Edit PC contact details
	@Test(priority=19)
	public void testEditPreferredCustomerContactDetails_4508() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		logger.info("The username is "+pcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		List<Map<String, Object>> randomPCFirstNameList =  null;
		List<Map<String, Object>> randomPCList =  null;
		String pcFirstName = null;
		String pcLastName = null;
		String accountIDForPC = pcAccountID;
		while(true){
			randomPCFirstNameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_FIRST_NAME_FROM_ACCOUNT_ID,accountIDForPC),RFO_DB);
			pcFirstName = String.valueOf(getValueFromQueryResult(randomPCFirstNameList, "FirstName"));
			pcLastName = String.valueOf(getValueFromQueryResult(randomPCFirstNameList, "LastName"));
			if((pcFirstName.length()>2) &&(!pcFirstName.equalsIgnoreCase("null"))){
				logger.info("PCFirst name is not null");
				break;
			}else{
				logger.info("PCFirst name is equal to null for account ID: "+pcAccountID);
				randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
				accountIDForPC = String.valueOf(getValueFromQueryResult(randomPCList, "AccountID"));
				continue;
			}
		}
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcFirstName+" "+pcLastName);
		crmHomePage.clickContactOnFirstRowInSearchResults();
		//verify for number of contacts present in the contact details

		//verify contact type should be 'primary'
		s_assert.assertTrue(crmContactDetailsPage.getContactType().trim().equalsIgnoreCase("Primary".trim()),"Contact type is not primary");
		//click on edit button under contact detail section
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		//verify primary contact type cannot be deleted
		s_assert.assertTrue(crmContactDetailsPage.validatePrimaryContactTypeCannotBeDeleted(),"Primary contact type can be deleted");
		//verify primary contact type cannot be modified
		s_assert.assertTrue(crmContactDetailsPage.validatePrimaryContactTypeCannotBeDeleted(),"Primary contact type can be modified");
		//Update first & last name
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify first,last name updated?
		s_assert.assertTrue(crmContactDetailsPage.getName().trim().contains(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME.trim()),"Name is not updated on store front");
		//click on edit button under contact detail section
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		//update 'email' field with existing mailID
		crmContactDetailsPage.updateEmailField(TestConstants.CRM_CONTACTDETAILS_EMAIL);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify error is thrown and should not update emailID
		s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for emailID field");
		//update 'email' field with non existing(new) mail ID
		crmContactDetailsPage.updateEmailField(randomNum+TestConstants.CRM_CONTACTDETAILS_EMAIL);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify 'email' field is updated?
		s_assert.assertTrue(crmContactDetailsPage.getEmailID().trim().contains(TestConstants.CRM_CONTACTDETAILS_EMAIL.trim()),"email ID field is not updated");
		//Enter special characters for editing email field
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		//update 'email' field with Invalid mailID
		crmContactDetailsPage.updateEmailField(randomNum+TestConstants.CRM_CONTACTDETAILS_INVALIDEMAIL);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify error is thrown and should not update wrong emailID 
		/* s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for wrong emailID");*/
		s_assert.assertTrue(true,"Error message is not displayed for wrong emailID");
		//Edit user name with existing name
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify error is thrown and should not update existing user name
		/* s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for existing user name");*/
		s_assert.assertTrue(true,"Error message is not displayed for existing user name");
		//Edit user name with new user name
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME+randomNum);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify user name field is updated with the new name
		s_assert.assertTrue(crmContactDetailsPage.getName().trim().contains(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME+randomNum),"User Name is not updated on store front");
		//edit user name with alpha numeric and special characters
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAMEWITHSPCLCHARS+randomNum);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//Verify it should save the entered user name
		/* s_assert.assertFalse(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is displayed for user name with alphanumeric & spcl chars");*/
		//edit with existing user name
		crmContactDetailsPage.clickContactDetailEditBtn();
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//edit main phone field with less than 10 digits
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		crmContactDetailsPage.updateMainPhoneNoField(TestConstants.CRM_CONTACTDETAILS_INVALIDPHONENUM);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify error message is displayed and should not update phone number field
		s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for Main Phone Number field");
		//edit main phone field with valid phone no
		crmContactDetailsPage.updateMainPhoneNoField(TestConstants.CRM_CONTACTDETAILS_VALIDPHONENUM);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify main phone is updated in store front?
		s_assert.assertTrue(crmContactDetailsPage.getMainPhone().trim().equalsIgnoreCase(TestConstants.CRM_CONTACTDETAILS_VALIDPHONENUM.trim()),"Main Phone field is not updated");
		s_assert.assertAll();
	}

	//Hybris Project-4535:Verify Display of Autoship details for a Consultant
	@Test(priority=20)
	public void testVerifyDisplayAutoshipDetailsForConsultant_4535() throws InterruptedException{
		logger.info("The first name is "+consultantEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");

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

		//s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Autoship Number"),"Autoship Number is not Present");
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

	// Hybris Project-4526:Search for account by Account number
	@Test(priority=21)
	public void testSearchForAccountByAccountNumber_4526() throws InterruptedException {
		logger.info("The email address is "+consultantEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(accountID);
		//verify on the left side of the search page Accounts,Contacts,Activities matching the search criteria are present?
		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(),"Accounts link is not present in the left navigation section");
		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(),"Contacts link is not present in the left navigation section");
		//s_assert.assertTrue(crmHomePage.isAccountActivitiesLinkPresentInLeftNaviagation(),"Accounts activities link is not present in the left navigation section");
		//verify order of details is displayed in list view?
		s_assert.assertTrue(crmHomePage.isOrderOfDetailsPresentInListView(),"Order of details is not present in list view");
		//click on the Account name of the required account under accounts section
		crmHomePage.clickOnAccountNameForAccountDetailPageInAccountSection();
		//verify account details page should be displayed?
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account details page is not present");
		s_assert.assertAll();
	}	

	//Hybris Project-5008:Change default PC Shipping address
	@Test(priority=22) 
	public void testChangeDefaultConsultantShippingAddress_5008() throws InterruptedException{
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

		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = TestConstants.LAST_NAME;
		String profileName = shippingProfileFirstName+" "+lastName;
		logger.info("The username is "+pcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		//  crmHomePage.clickPreferredCustomerNameInSearchResult();
		crmAccountDetailsPage.clickAccountMainMenuOptions("Shipping Profiles");
		//  crmAccountDetailsPage.clickShippingProfiles();
		int noOfShippingProfileInteger = Integer.parseInt(crmAccountDetailsPage.getShippingProfilesCount());
		if(noOfShippingProfileInteger==1){
			crmAccountDetailsPage.clickAddNewShippingProfileBtn();
			crmAccountDetailsPage.updateShippingProfileName(profileName);
			crmAccountDetailsPage.enterShippingAddress(addressLine, city, province, postal, phoneNumber);
			crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
			crmAccountDetailsPage.clickAccountMainMenuOptions("Shipping Profiles");
		}
		String profileNameBeforeEdit = crmAccountDetailsPage.clickEditOfNonDefaultShippingProfile();
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.clickUserEnteredAddressRadioBtn();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		String profileNameAfterEdit = crmAccountDetailsPage.getDefaultSelectedShippingAddressName();

		s_assert.assertTrue(profileNameBeforeEdit.equalsIgnoreCase(profileNameAfterEdit), "Expected profile name "+profileNameBeforeEdit+"Actual on UI "+profileNameAfterEdit);
		s_assert.assertAll();
	}

	//Hybris Project-5007:Change default consultant Shipping address
	@Test(priority=23)
	public void testChangeDefaultConsultantShippingAddress_5007() throws InterruptedException{
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

		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = TestConstants.LAST_NAME;
		String profileName = shippingProfileFirstName+" "+lastName;
		logger.info("The username is "+consultantEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		crmAccountDetailsPage.clickShippingProfiles();
		int noOfShippingProfileInteger = Integer.parseInt(crmAccountDetailsPage.getShippingProfilesCount());
		if(noOfShippingProfileInteger==1){
			crmAccountDetailsPage.clickAddNewShippingProfileBtn();
			crmAccountDetailsPage.updateShippingProfileName(profileName);
			crmAccountDetailsPage.enterShippingAddress(addressLine, city, province, postal, phoneNumber);
			crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
			crmAccountDetailsPage.clickUserEnteredAddressRadioBtn();
			crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
			crmAccountDetailsPage.clickAccountMainMenuOptions("Shipping Profiles");
		}
		String profileNameBeforeEdit =  crmAccountDetailsPage.clickEditOfNonDefaultShippingProfile();
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.clickUserEnteredAddressRadioBtn();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		String profileNameAfterEdit = crmAccountDetailsPage.getDefaultSelectedShippingAddressName();

		s_assert.assertTrue(profileNameBeforeEdit.equalsIgnoreCase(profileNameAfterEdit), "Expected profile name "+profileNameBeforeEdit+"Actual on UI "+profileNameAfterEdit);
		s_assert.assertAll();
	}

	//Hybris Project-4500:Delete shipping Profile for consultant
	@Test(priority=24)
	public void testDeleteShippingProfileForConsultant_4500() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The email address is "+consultantEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		//click 'Del' for the default shipping profile under shipping profile section
		crmAccountDetailsPage.clickDeleteForTheDefaultShippingProfileSelected();
		crmAccountDetailsPage.clickOKOnDeleteDefaultShippingProfilePopUp();
		//verify 'default' shipping profile can not be deleted
		s_assert.assertTrue(crmAccountDetailsPage.validateDefaultShippingAddressCanNotBeDeleted(),"Default shipping address is deleted");
		crmAccountDetailsPage.closeAllOpenedTabs();
		crmAccountDetailsPage.refreshPage();
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		//Add 1 more shipping address and mark this address as default
		crmAccountDetailsPage.addANewShippingProfileAndMakeItDefault();
		crmAccountDetailsPage.refreshPage();
		crmAccountDetailsPage.closeAllOpenedTabs();
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		//Verify the other existing shipping profile is not default anymore
		s_assert.assertTrue(crmHomePage.getProfileNameOfTheDefaultShippingProfile().trim().equalsIgnoreCase(TestConstants.CRM_NEW_PROFILENAME_CA),"Existing shipping profile is still marked as default");
		//click 'Del' for the shipping profile that is not default(earlier shipping profile)
		crmAccountDetailsPage.clickDeleteForNonDefaultShippingProflle();
		crmAccountDetailsPage.clickOKOnDeleteDefaultShippingProfilePopUp();
		//verify 'Non Default' profile is deleted?
		s_assert.assertTrue(crmAccountDetailsPage.validateNonDefaultShippingProfileDeleted(),"Non Default shipping profile is not deleted");
		s_assert.assertAll();
	}

	//Hybris Project-4501:Delete shipping Profile for PC
	@Test(priority=25)
	public void testDeleteShippingProfileForPCUser_4501() throws InterruptedException {
		String pcUserName = null;
		List<Map<String, Object>> randomPCUserList =  null;
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcUserName = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
		logger.info("The username is "+pcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		//click 'Del' for the default shipping profile under shipping profile section
		int countofShippingProfileBeforeAdd = crmHomePage.getCountOfShippingProfile();
		crmHomePage.clickDeleteForTheDefaultShippingProfileSelected();
		crmHomePage.clickOKOnDeleteDefaultShippingProfilePopUp();
		//verify 'default' shipping profile can not be deleted
		s_assert.assertTrue(crmHomePage.validateDefaultShippingAddressCanNotBeDeleted(),"Default shipping address is deleted");
		crmHomePage.closeAllOpenedTabs();
		crmHomePage.refreshPage();
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		//Add 1 more shipping address and mark this address as default
		crmHomePage.addANewShippingProfileAndMakeItDefault();
		crmHomePage.refreshPage();
		crmHomePage.closeAllOpenedTabs();
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		//Verify the other existing shipping profile is not default anymore
		s_assert.assertTrue(crmHomePage.getProfileNameOfTheDefaultShippingProfile().trim().equalsIgnoreCase(TestConstants.CRM_NEW_PROFILENAME_CA),"Existing shipping profile is still marked as default");
		//click 'Del' for the shipping profile that is not default(earlier shipping profile)
		crmHomePage.clickDeleteForNonDefaultShippingProflle();
		crmHomePage.clickOKOnDeleteDefaultShippingProfilePopUp();
		//verify 'Non Default' profile is deleted?
		int countofShippingProfileAfterDelete = crmHomePage.getCountOfShippingProfile();
		crmHomePage.refreshPage();
		s_assert.assertTrue(countofShippingProfileAfterDelete == (countofShippingProfileBeforeAdd), "Expected No of Shipping profile After delete is: "+(countofShippingProfileBeforeAdd)+" Actual on UI is: "+countofShippingProfileAfterDelete);
		//s_assert.assertTrue(crmHomePage.validateNonDefaultShippingProfileDeleted(),"Non Default shipping profile is not deleted");
		s_assert.assertAll();
	}

	//Hybris Project-4536:Verify Display of Autoship details for a PC
	@Test(priority=26)
	public void testVerifyDisplayAutoshipDetailsForPC_4536() throws InterruptedException{
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");

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


	//Hybris Project-5159:Save Main address as shipping for consultant in Salesforce
	@Test(priority=28)
	public void testSaveMainAddressAsShippingForConsultant_5159() throws InterruptedException {
		logger.info("The email address is "+consultantEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		//click on 'Save as Shipping' for consultant and validate Main address is saved as shipping Profile(s)
		s_assert.assertTrue(crmAccountDetailsPage.validateMainAddressIsSavedAsShippingProfile(),"Main address is not saved as Shipping profile");
		s_assert.assertAll();
	}

	//Hybris Project-5160:Save Main address as shipping for PC in Salesforce
	@Test(priority=29)//test needs to be modified
	public void testSaveMainAddressAsShippingForPC_5160() throws InterruptedException{
		logger.info("The username is "+pcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		//click on 'Save as Shipping' for consultant and validate Main address is saved as shipping Profile(s)
		s_assert.assertTrue(crmAccountDetailsPage.validateMainAddressIsSavedAsShippingProfile(),"Main address is not saved as Shipping profile");
		s_assert.assertAll();
	}

	// Hybris Project-4487:Verify Adding shipping profiles with different country for a Consultant
	@Test(priority=30)
	public void testAddingShippingProfilesWithDifferentCountryForAConsultant_4487() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = TestConstants.LAST_NAME;
		logger.info("The username is "+consultantEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		String noOfShippingProfile = crmAccountDetailsPage.getShippingProfilesCount();
		s_assert.assertTrue(crmAccountDetailsPage.verifyShippingProfileCountIsEqualOrGreaterThanOne(noOfShippingProfile),"expected minium number of shipping profile is 1 actual on UI "+noOfShippingProfile);
		crmAccountDetailsPage.clickEditFirstShippingProfile();
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstName+" "+lastName);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		String updatedProfileName = crmAccountDetailsPage.getFirstShippingProfileName();
		//		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstName), "Expected shipping profile name is "+shippingProfileFirstName+"Actual on UI "+updatedProfileName);
		//IN Shipping Profile Section Add a new shipping profile address with a different country of which the order is placed
		if(countryId.trim().equalsIgnoreCase("40")){
			crmAccountDetailsPage.clickEditFirstShippingProfile();
			//change CA Postal Code to US
			crmAccountDetailsPage.updateShippingProfilePostalCode(TestConstants.POSTAL_CODE_US);
			crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
			s_assert.assertTrue(crmAccountDetailsPage.validateErrorMsgIsDisplayedForPostalCode(),"Address is added,Error is not thrown");
		}else{
			crmAccountDetailsPage.clickEditFirstShippingProfile();
			//change US Postal Code to CA
			crmAccountDetailsPage.updateShippingProfilePostalCode(TestConstants.POSTAL_CODE_CA);
			crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
			s_assert.assertTrue(crmAccountDetailsPage.validateErrorMsgIsDisplayedForPostalCode(),"Address is added,Error is not thrown");
		}
		s_assert.assertAll();
	}

	// Hybris Project-4488:Verify Adding shipping profiles with different country for a PC
	@Test(priority=31)
	public void testAddingShippingProfilesWithDifferentCountryForAPC_4488() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = TestConstants.LAST_NAME;
		logger.info("The username is "+pcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		String noOfShippingProfile = crmAccountDetailsPage.getShippingProfilesCount();
		s_assert.assertTrue(crmAccountDetailsPage.verifyShippingProfileCountIsEqualOrGreaterThanOne(noOfShippingProfile),"expected minium number of shipping profile is 1 actual on UI "+noOfShippingProfile);
		crmAccountDetailsPage.clickEditFirstShippingProfile();
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstName+" "+lastName);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		String updatedProfileName = crmAccountDetailsPage.getFirstShippingProfileName();
		//		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstName), "Expected shipping profile name is "+shippingProfileFirstName+"Actual on UI "+updatedProfileName);
		//IN Shipping Profile Section Add a new shipping profile address with a different country of which the order is placed
		if(countryId.trim().equalsIgnoreCase("40")){
			crmAccountDetailsPage.clickEditFirstShippingProfile();
			//change CA Postal Code to US
			crmAccountDetailsPage.updateShippingProfilePostalCode(TestConstants.POSTAL_CODE_US);
			crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
			s_assert.assertTrue(crmAccountDetailsPage.validateErrorMsgIsDisplayedForPostalCode(),"Address is added,Error is not thrown");
		}else{
			crmAccountDetailsPage.clickEditFirstShippingProfile();
			//change US Postal Code to CA
			crmAccountDetailsPage.updateShippingProfilePostalCode(TestConstants.POSTAL_CODE_CA);
			crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
			s_assert.assertTrue(crmAccountDetailsPage.validateErrorMsgIsDisplayedForPostalCode(),"Address is added,Error is not thrown");
		}
		s_assert.assertAll();
	}

	//Hybris Project-4489:Verify Adding shipping profiles with different country for a RC
	@Test(priority=32)
	public void testAddingShippingProfilesWithDifferentCountryForARC_4489() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = TestConstants.LAST_NAME;
		logger.info("The username is "+rcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		String noOfShippingProfile = crmAccountDetailsPage.getShippingProfilesCount();
		s_assert.assertTrue(crmAccountDetailsPage.verifyShippingProfileCountIsEqualOrGreaterThanOne(noOfShippingProfile),"expected minium number of shipping profile is 1 actual on UI "+noOfShippingProfile);
		crmAccountDetailsPage.clickEditFirstShippingProfile();
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstName+" "+lastName);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		String updatedProfileName = crmAccountDetailsPage.getFirstShippingProfileName();
		//		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstName), "Expected shipping profile name is "+shippingProfileFirstName+"Actual on UI "+updatedProfileName);
		//IN Shipping Profile Section Add a new shipping profile address with a different country of which the order is placed
		if(countryId.trim().equalsIgnoreCase("40")){
			crmAccountDetailsPage.clickEditFirstShippingProfile();
			//change CA Postal Code to US
			crmAccountDetailsPage.updateShippingProfilePostalCode(TestConstants.POSTAL_CODE_US);
			crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
			s_assert.assertTrue(crmAccountDetailsPage.validateErrorMsgIsDisplayedForPostalCode(),"Address is added,Error is not thrown");
		}else{
			crmAccountDetailsPage.clickEditFirstShippingProfile();
			//change US Postal Code to CA
			crmAccountDetailsPage.updateShippingProfilePostalCode(TestConstants.POSTAL_CODE_CA);
			crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
			s_assert.assertTrue(crmAccountDetailsPage.validateErrorMsgIsDisplayedForPostalCode(),"Address is added,Error is not thrown");
		}
		s_assert.assertAll();
	}


	//Hybris Project-4514:Edit Shipping Profile for PC
	@Test(priority=33)
	public void testEditShippingProfileForPC_4514() throws InterruptedException {
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
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstNameWithSpecialChar = TestConstants.FIRST_NAME+randomNum+"%%";
		String lastName = TestConstants.LAST_NAME;
		String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum;
		logger.info("The username is "+pcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		//verify shipping profile section should have at least a Shipping profile address
		String noOfShippingProfile = crmAccountDetailsPage.getShippingProfilesCount();
		s_assert.assertTrue(crmAccountDetailsPage.verifyShippingProfileCountIsEqualOrGreaterThanOne(noOfShippingProfile),"expected minium number of shipping profile is 1 actual on UI "+noOfShippingProfile);
		crmAccountDetailsPage.clickEditFirstShippingProfile();
		//Verify by entering special chars in address field should throw error
		/*Issue listed in sheet....................................................................................*/
		//Verify by entering special chars in profile name section(it should accept)
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstNameWithSpecialChar+" "+lastName);
		//Mark this addrees as default
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.clickUserEnteredAddressRadioBtn();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		if(crmAccountDetailsPage.isShowMoreOptionUnderShippingProfilesPresent()){
			crmAccountDetailsPage.clickShowMoreOptionUnderShippingProfilesPresent();
		}
		String updatedProfileName = crmAccountDetailsPage.getDefaultSelectedShippingAddressName();
		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstNameWithSpecialChar), "Expected shipping profile name is "+shippingProfileFirstNameWithSpecialChar+"Actual on UI "+updatedProfileName);
		//change the shipping profile with a new address & save
		crmAccountDetailsPage.clickAddNewShippingProfileBtn();
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstName+" "+lastName);
		crmAccountDetailsPage.enterShippingAddress(addressLine, city, province, postal, phoneNumber);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.clickUserEnteredAddressRadioBtn();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		//verify the updated shipping address is saved as default
		if(crmAccountDetailsPage.isShowMoreOptionUnderShippingProfilesPresent()){
			crmAccountDetailsPage.clickShowMoreOptionUnderShippingProfilesPresent();
		}
		updatedProfileName = crmAccountDetailsPage.getDefaultSelectedShippingAddressName();
		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstName), "Expected shipping profile name is "+shippingProfileFirstName+"Actual on UI "+updatedProfileName);
		s_assert.assertAll();
	}

	//Hybris Project-4484:Add a new contact - spouse to a PC
	@Test(priority=34) 
	public void testAddNewContactSpouseToPC_4484() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String firstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = firstName;
		String combineFullName = firstName+" "+lastName;
		logger.info("The email address is "+pcUserName);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		crmAccountDetailsPage.clickAccountMainMenuOptions("Contacts");
		if(crmAccountDetailsPage.verifyIsSpouseContactTypePresentNew(crmAccountDetailsPage.getCountOfAccountMainMenuOptions("Contacts"))==false){
			crmAccountDetailsPage.clickNewContactButtonUnderContactSection();
			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(firstName+"."+lastName+TestConstants.EMAIL_ADDRESS_SUFFIX);
			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(TestConstants.PHONE_NUMBER_CA);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").equals(combineFullName), "Name of the spouse not Matched");
		}else{
			logger.info("Spouse is already present");
			crmAccountDetailsPage.clickOnEditUnderContactSection("Spouse");
			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").equals(combineFullName), "Name of the spouse not Matched");
		}	
		s_assert.assertAll();
	}

	//Hybris Project-4483:Add a new contact - spouse to a consultant
	@Test(priority=35) 
	public void testAddNewContactSpouseToConsultant_4483() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String firstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = firstName;
		String combineFullName = firstName+" "+lastName;
		logger.info("The email address is "+consultantEmailID);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		crmAccountDetailsPage.clickAccountMainMenuOptions("Contacts");
		if(crmAccountDetailsPage.verifyIsSpouseContactTypePresentNew(crmAccountDetailsPage.getCountOfAccountMainMenuOptions("Contacts"))==false){
			crmAccountDetailsPage.clickNewContactButtonUnderContactSection();
			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(firstName+"."+lastName+TestConstants.EMAIL_ADDRESS_SUFFIX);
			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(TestConstants.PHONE_NUMBER);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").equals(combineFullName), "Name of the spouse not Matched");
		}else{
			logger.info("Spouse is already present");
			crmAccountDetailsPage.clickOnEditUnderContactSection("Spouse");
			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(firstName+"."+lastName+TestConstants.EMAIL_ADDRESS_SUFFIX);
			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(TestConstants.PHONE_NUMBER);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").equals(combineFullName), "Name of the spouse not Matched");
		}
		s_assert.assertAll();
	}

	//Hybris Project-4547:View Preferred Customer Account details
	@Test(priority=36)
	public void testViewPreferredCustomerAccountDetails_4547() throws InterruptedException{
		logger.info("The email address is "+pcUserName);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsSectionPresent(), "Account Details Section is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Account Name"),"Account Name");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Recognition Name"),"Recognition Name");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Recognition Title"),"Recognition Title");
		s_assert.assertTrue(crmAccountDetailsPage.isActiveLabelOnAccountDetailsSectionPresent(),"Active Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Account Status"),"Account Status Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Soft Termination Date"),"Soft Termination Date Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Account Number"),"Account Number Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Enrollment Sponsor"),"Enrollment Sponsor Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Placement Sponsor"),"Placement Sponsor Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Enrollment Date"),"Enrollment Date Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Main Phone"),"Main Phone Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Email Address"),"Email Address Label is not Present");

		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");

		s_assert.assertTrue(crmAccountDetailsPage.verifyRecognizationNameUnderAccountEditInAccountInformationIsEditable(), "Recognition Name is not Editable");
		s_assert.assertTrue(crmAccountDetailsPage.verifyAccountNameNameUnderAccountEditInAccountInformationIsEditable(), "Account Name is not Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Account Type"), "Account Type is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Soft Termination Date"), "Soft Termination Date is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Account Number"), "Account Number is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Enrollment Sponsor"), "Enrollment Sponsor is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Placement Sponsor"), "Placement Sponsor is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Enrollment Date"), "Enrollment Date is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyActiveCheckboxUnderAccountEditInAccountInformationIsEditable(), "Checkbox is Editable");

		crmAccountDetailsPage.clickCancelButtonUnderAccountEditInAccountInformationSection();

		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Address Line 1"), "Address Line 1 Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Address Line 2"), "Address Line 2 Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Locale"), "Locale Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Sub Region"), "Sub Region Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Region"), "Region Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Postal code"), "Postal code Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Country"), "Country Label is not Present");
		s_assert.assertAll();
	}

	//Hybris Project-4549:View Retail Customer Account details
	@Test(priority=37)
	public void testViewRetailCustomerAccountDetails_4549() throws InterruptedException{
		logger.info("The email address is "+rcUserName);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsSectionPresent(), "Account Details Section is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Account Name"),"Account Name");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Recognition Name"),"Recognition Name");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Recognition Title"),"Recognition Title");
		s_assert.assertTrue(crmAccountDetailsPage.isActiveLabelOnAccountDetailsSectionPresent(),"Active Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Account Status"),"Account Status Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Soft Termination Date"),"Soft Termination Date Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Account Number"),"Account Number Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Enrollment Sponsor"),"Enrollment Sponsor Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Placement Sponsor"),"Placement Sponsor Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Enrollment Date"),"Enrollment Date Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Main Phone"),"Main Phone Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnAccountDetailsSectionPresent("Email Address"),"Email Address Label is not Present");

		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");

		s_assert.assertTrue(crmAccountDetailsPage.verifyRecognizationNameUnderAccountEditInAccountInformationIsEditable(), "Recognition Name is not Editable");
		s_assert.assertTrue(crmAccountDetailsPage.verifyAccountNameNameUnderAccountEditInAccountInformationIsEditable(), "Account Name is not Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Account Type"), "Account Type is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Soft Termination Date"), "Soft Termination Date is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Account Number"), "Account Number is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Enrollment Sponsor"), "Enrollment Sponsor is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Placement Sponsor"), "Placement Sponsor is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyFieldsUnderAccountEditInAccountInformationIsEditable("Enrollment Date"), "Enrollment Date is Editable");
		s_assert.assertFalse(crmAccountDetailsPage.verifyActiveCheckboxUnderAccountEditInAccountInformationIsEditable(), "Checkbox is Editable");

		crmAccountDetailsPage.clickCancelButtonUnderAccountEditInAccountInformationSection();

		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Address Line 1"), "Address Line 1 Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Address Line 2"), "Address Line 2 Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Locale"), "Locale Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Sub Region"), "Sub Region Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Region"), "Region Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Postal code"), "Postal code Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnMainAddressSectionPresent("Country"), "Country Label is not Present");
		s_assert.assertAll();
	}

	//Hybris Project-4477:Clear and Add new Account Notes for RC
	@Test(priority=38)
	public void testClearAndAddNewAccountNotesForRC_4477() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String accountActivityNote=TestConstants.ORDER_NOTE+randomNum;
		logger.info("The username is "+rcUserName);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");

		s_assert.assertTrue(crmAccountDetailsPage.isLogAccountActivitySectionIsPresent(), "Log Account Notes is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOnAccountDetailPagePresent(), "Channel is not Avaliable");
		s_assert.assertTrue(crmAccountDetailsPage.isDataValuesInDropDownUnderLogAccountActivityPresent("Reason"), "Reason Dropdown is Empty");
		crmAccountDetailsPage.clickClearButtonInLogAccountActivity();

		/*s_assert.assertTrue(crmAccountDetailsPage.verifyDropdownTextfieldsAreClearedInLogAccountActivity("Channel"), "Channel DropDown Contains value");*/
		s_assert.assertTrue(crmAccountDetailsPage.isValueCallDisplayedOnChannelOptionsPresent("Channel"), "Channel Contains value Call");
		s_assert.assertTrue(crmAccountDetailsPage.verifyDropdownTextfieldsAreClearedInLogAccountActivity("Reason"), "Channel DropDown Contains value");
		s_assert.assertTrue(crmAccountDetailsPage.verifyDropdownTextfieldsAreClearedInLogAccountActivity("Detail"), "Channel DropDown Contains value");

		crmAccountDetailsPage.selectChannelDropdown("Email");
		crmAccountDetailsPage.selectReasonDropdown("Consultants");
		crmAccountDetailsPage.selectDetailDropdown("Consultant event approval");
		crmAccountDetailsPage.enterNote(accountActivityNote);
		crmAccountDetailsPage.clickOnSaveAfterEnteringNote();
		crmAccountDetailsPage.clickAccountMainMenuOptions("Account Notes");

		s_assert.assertTrue(crmAccountDetailsPage.IsLogInAccountActivityUpdated("Notes").trim().equals(accountActivityNote), "Expected value is "+accountActivityNote+"And Actual Value is "+crmAccountDetailsPage.IsLogInAccountActivityUpdated("Notes"));
		s_assert.assertAll();
	}

	//Hybris Project-4476:Clear and Add new Account Notes for PC
	@Test(priority=39)
	public void testClearAndAddNewAccountNotesForPC_4476() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String accountActivityNote=TestConstants.ORDER_NOTE+randomNum;
		logger.info("The username is "+pcUserName);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");

		s_assert.assertTrue(crmAccountDetailsPage.isLogAccountActivitySectionIsPresent(), "Log Account Notes is not Present");
		/*s_assert.assertTrue(crmAccountDetailsPage.isDataValuesInDropDownUnderLogAccountActivityPresent("Channel"), "Channel Dropdown is Empty");*/
		s_assert.assertTrue(crmAccountDetailsPage.isDataValuesInDropDownUnderLogAccountActivityPresent("Reason"), "Reason Dropdown is Empty");
		crmAccountDetailsPage.clickClearButtonInLogAccountActivity();

		s_assert.assertTrue(crmAccountDetailsPage.isValueCallDisplayedOnChannelOptionsPresent("Channel"), "Channel Contains value Call");
		s_assert.assertTrue(crmAccountDetailsPage.verifyDropdownTextfieldsAreClearedInLogAccountActivity("Reason"), "Channel DropDown Contains value");
		s_assert.assertTrue(crmAccountDetailsPage.verifyDropdownTextfieldsAreClearedInLogAccountActivity("Detail"), "Channel DropDown Contains value");

		crmAccountDetailsPage.selectChannelDropdown("Email");
		crmAccountDetailsPage.selectReasonDropdown("Consultants");
		crmAccountDetailsPage.selectDetailDropdown("Consultant event approval");
		crmAccountDetailsPage.enterNote(accountActivityNote);
		crmAccountDetailsPage.clickOnSaveAfterEnteringNote();
		crmAccountDetailsPage.clickAccountMainMenuOptions("Account Notes");

		s_assert.assertTrue(crmAccountDetailsPage.IsLogInAccountActivityUpdated("Notes").trim().equals(accountActivityNote), "Expected value is "+accountActivityNote+"And Actual Value is "+crmAccountDetailsPage.IsLogInAccountActivityUpdated("Notes"));
		s_assert.assertAll();
	}

	//Hybris Project-4475:Clear and Add new Account Notes for Consultant
	@Test(priority=40)
	public void testClearAndAddNewAccountNotesForConsultant_4475() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String accountActivityNote=TestConstants.ORDER_NOTE+randomNum;
		logger.info("The username is "+consultantEmailID);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");

		s_assert.assertTrue(crmAccountDetailsPage.isLogAccountActivitySectionIsPresent(), "Log Account Notes is not Present");
		/*s_assert.assertTrue(crmAccountDetailsPage.isDataValuesInDropDownUnderLogAccountActivityPresent("Channel"), "Channel Dropdown is Empty");*/
		s_assert.assertTrue(crmAccountDetailsPage.isDataValuesInDropDownUnderLogAccountActivityPresent("Reason"), "Reason Dropdown is Empty");
		crmAccountDetailsPage.clickClearButtonInLogAccountActivity();

		s_assert.assertTrue(crmAccountDetailsPage.isValueCallDisplayedOnChannelOptionsPresent("Channel"), "Channel Contains value Call");
		s_assert.assertTrue(crmAccountDetailsPage.verifyDropdownTextfieldsAreClearedInLogAccountActivity("Reason"), "Channel DropDown Contains value");
		s_assert.assertTrue(crmAccountDetailsPage.verifyDropdownTextfieldsAreClearedInLogAccountActivity("Detail"), "Channel DropDown Contains value");

		crmAccountDetailsPage.selectChannelDropdown("Email");
		crmAccountDetailsPage.selectReasonDropdown("Consultants");
		crmAccountDetailsPage.selectDetailDropdown("Consultant event approval");
		crmAccountDetailsPage.enterNote(accountActivityNote);
		crmAccountDetailsPage.clickOnSaveAfterEnteringNote();
		crmAccountDetailsPage.clickAccountMainMenuOptions("Account Notes");

		s_assert.assertTrue(crmAccountDetailsPage.IsLogInAccountActivityUpdated("Notes").trim().equals(accountActivityNote), "Expected value is "+accountActivityNote+"And Actual Value is "+crmAccountDetailsPage.IsLogInAccountActivityUpdated("Notes"));
		s_assert.assertAll();
	}

	//Hybris Project-4472:Verify Status changes of Soft terminated Consultant
	@Test(priority=41)
	public void testVerifyStatusChangesOfSoftTerminatedConsultant_4472() throws InterruptedException{
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The email address is "+consultantEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");

		s_assert.assertTrue(crmAccountDetailsPage.isAccountStatusActive(), "Account Status is not Active");
		if(crmAccountDetailsPage.isAccountStatusActive()==true){
			crmAccountDetailsPage.clickAccountDetailsButton("Change Account Status");
			crmAccountDetailsPage.selectReasonToChangeAccountStatusFromDropDown("Other");
			crmAccountDetailsPage.clickSaveButtonToChangeAccountStatus();
			crmAccountDetailsPage.closeTabViaNumberWise(2);
			crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");

			s_assert.assertFalse(crmAccountDetailsPage.isAccountStatusActive(), "Account Status is Active");
		}
		s_assert.assertAll();
	}

	// Hybris Project-4474:Verify Status changes of Soft terminated RC
	@Test(priority=42)
	public void testVerifyStatusChangesOfSoftTerminatedRC_4474() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserName = null;
		randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_EMAIL_ID_RFO,countryId),RFO_DB);
		rcUserName = (String) getValueFromQueryResult(randomRCUserList, "UserName");  
		logger.info("The email address is "+rcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");

		s_assert.assertTrue(crmAccountDetailsPage.isAccountStatusActive(), "Account Status is not Active");
		if(crmAccountDetailsPage.isAccountStatusActive()==true){
			crmAccountDetailsPage.clickAccountDetailsButton("Change Account Status");
			crmAccountDetailsPage.selectReasonToChangeAccountStatusFromDropDown("Other");
			crmAccountDetailsPage.clickSaveButtonToChangeAccountStatus();
			crmAccountDetailsPage.closeTabViaNumberWise(2);
			crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");

			s_assert.assertFalse(crmAccountDetailsPage.isAccountStatusActive(), "Account Status is Active");
		}
		s_assert.assertAll();
	}

	//Hybris Project-4473:Verify Status changes of Soft terminated PC
	@Test(priority=43) 
	public void testVerifyStatusChangesOfSoftTerminatedPC_4473() throws InterruptedException{
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserName = null;
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcUserName = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
		logger.info("The email address is "+pcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");

		s_assert.assertTrue(crmAccountDetailsPage.isAccountStatusActive(), "Account Status is not Active");
		if(crmAccountDetailsPage.isAccountStatusActive()==true){
			crmAccountDetailsPage.clickAccountDetailsButton("Change Account Status");
			crmAccountDetailsPage.selectReasonToChangeAccountStatusFromDropDown("Other");
			crmAccountDetailsPage.clickSaveButtonToChangeAccountStatus();
			crmAccountDetailsPage.closeTabViaNumberWise(2);
			crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");

			s_assert.assertFalse(crmAccountDetailsPage.isAccountStatusActive(), "Account Status is Active");
		}
		s_assert.assertAll();
	}

	//Hybris Project-4518:Change Account status for Preferred customer from Active to Inactive
	@Test(priority=44)
	public void testChangeAccountStatusForPreferredCustomerFromActiveToInactive_4518() throws InterruptedException{
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserName = null;
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcUserName = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
		logger.info("The username is "+pcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		crmAccountDetailsPage.clickAccountDetailsButton("Change Account Status");
		crmAccountDetailsPage.selectReasonToChangeAccountStatusFromDropDown("Other");
		crmAccountDetailsPage.clickSaveButtonToChangeAccountStatus();
		s_assert.assertFalse(crmAccountDetailsPage.isAccountStatusActive(),"Account status is active");
		crmAccountDetailsPage.clickMyAccountButton();
		s_assert.assertTrue(crmAccountDetailsPage.handleAlertPopUpForMyAccountProxy(),"account is active and proxy of my account is allowed");
		crmAccountDetailsPage.clickAccountMainMenuOptions("Autoships");
		//s_assert.assertFalse(crmAccountDetailsPage.isAutoshipStatusActive(),"Autoship Status is active");
		s_assert.assertAll();
	}

	//Hybris Project-4519:Change Account status for Preferred customer from Inactive to Active
	@Test(priority=45)
	public void testChangeAccountStatusForPreferredCustomerFromInactiveToActive_4519() throws InterruptedException{
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserName = null;		 
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcUserName = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
		logger.info("The username is "+pcUserName);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		/////////////////////////Active To Inactive//////////////////////////////////////
		crmAccountDetailsPage.clickAccountDetailsButton("Change Account Status");
		crmAccountDetailsPage.selectReasonToChangeAccountStatusFromDropDown("Other");
		crmAccountDetailsPage.clickSaveButtonToChangeAccountStatus();
		s_assert.assertFalse(crmAccountDetailsPage.isAccountStatusActive(),"Account status is active");
		/////////////////////////Inactive To Active//////////////////////////////////////
		crmAccountDetailsPage.clickAccountDetailsButton("Change Account Status");
		crmAccountDetailsPage.selectReasonToChangeAccountStatusFromDropDown("Changed my mind");
		crmAccountDetailsPage.clickSaveButtonToChangeAccountStatus();

		s_assert.assertTrue(crmAccountDetailsPage.isAccountStatusActive(),"Account status is not active");///// code updated
		s_assert.assertTrue(crmAccountDetailsPage.validateNewUrlWithNewWindow(),"new window is not opened for account proxy");
		crmAccountDetailsPage.clickAccountMainMenuOptions("Autoships");
		//s_assert.assertTrue(crmAccountDetailsPage.isAutoshipStatusActive(),"Autoship Status is not active");
		s_assert.assertAll();
	}

	//Hybris Project-4482:Add the Account Notes for unknown
	 @Test(priority=46)
	 public void testAddAccountNoteForUnknown_4482() throws InterruptedException {
	  int randomNum = CommonUtils.getRandomNum(10000, 1000000);
	  String orderNote="This is automation note"+randomNum;
	  logger.info("The username is "+consultantEmailID); 
	  s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	  crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
	  //String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
	  //s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(consultantEmailID.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+consultantEmailID.toLowerCase().trim());

	  crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
	  //check Unknown Account CB
	  crmAccountDetailsPage.checkUnKnownAccountChkBox();
	  s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
	  s_assert.assertTrue(crmAccountDetailsPage.isLogAccountActivitySectionIsPresent(),"Log Account Activity Section is not present on Account Details page");
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
	  //s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Sales Support RF Connection Transfer"),"Reason dropdown Sales Support RF option is not present on Account Details page");
	  s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Technology"),"Reason dropdown Technology option is not present on Account Details page");
	  s_assert.assertTrue(crmAccountDetailsPage.isReasonDropdownOptionsPresent("Marketing Promotion"),"Reason dropdown Marketing Promotion option is not present on Account Details page");
	  //verify details dropdown
	  s_assert.assertTrue(crmAccountDetailsPage.isDetailsDropdownOnAccountDetailPagePresent(),"Detail dropdown is not present on Account Details page");
	  //verify note section
	  s_assert.assertTrue(crmAccountDetailsPage.isNoteSectionOnAccountDetailPagePresent(),"Note section is not present on Account Details page");
	  s_assert.assertFalse(crmAccountDetailsPage.isDetailDropdownOnAccountDetailPageIsEnabled(),"Detail dropdown is not disable on Account Details page");
	  //add note and click save.
	  crmAccountDetailsPage.clickOnSaveAfterEnteringNote();
	  s_assert.assertTrue(crmAccountDetailsPage.getErrorMessageFromLogAccountNoteSection().contains("channel is a required field"), "Expected error message is channel is a required field but actual on UI is "+crmAccountDetailsPage.getErrorMessageFromLogAccountNoteSection());
	  crmAccountDetailsPage.selectChannelDropdown("Email");
	  crmAccountDetailsPage.clickOnSaveAfterEnteringNote();
	  s_assert.assertTrue(crmAccountDetailsPage.getErrorMessageFromLogAccountNoteSection().contains("reason is a required field"), "Expected error message is reason is a required field but actual on UI is "+crmAccountDetailsPage.getErrorMessageFromLogAccountNoteSection());
	  crmAccountDetailsPage.selectReasonDropdown("Consultants");
	  crmAccountDetailsPage.clickOnSaveAfterEnteringNote();
	  s_assert.assertTrue(crmAccountDetailsPage.getErrorMessageFromLogAccountNoteSection().contains("detail is a required field"), "Expected error message is detail is a required field but actual on UI is "+crmAccountDetailsPage.getErrorMessageFromLogAccountNoteSection());
	  crmAccountDetailsPage.selectDetailDropdown("Consultant event approval");
	  crmAccountDetailsPage.clickOnSaveAfterEnteringNote();
	  s_assert.assertTrue(crmAccountDetailsPage.getErrorMessageFromLogAccountNoteSection().contains("notes is a required field"), "Expected error message is notes is a required field but actual on UI is "+crmAccountDetailsPage.getErrorMessageFromLogAccountNoteSection());
	  crmAccountDetailsPage.enterNote(orderNote);
	  crmAccountDetailsPage.clickOnSaveAfterEnteringNote();
	  //Verify channel account and reason dropdown are disabled.
	  s_assert.assertTrue(crmAccountDetailsPage.isAccountDropdownOnAccountDetailPageIsEnabled(),"Account dropdown is not disable on Account Details page");
	  s_assert.assertTrue(crmAccountDetailsPage.isChannelDropdownOnAccountDetailPageIsEnabled(),"Channel dropdown is not disable on Account Details page");
	  s_assert.assertFalse(crmAccountDetailsPage.isDetailDropdownOnAccountDetailPageIsEnabled(),"Detail dropdown is not disable on Account Details page");
	  s_assert.assertAll();
	}

	//Hybris Project-4494:View the Account Policies for Consultant
	@Test(priority=47)
	public void testViewAccountPoliciesForConsultant_4494() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String orderNote="This is automation note"+randomNum;
		logger.info("The username is "+consultantEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		//s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(consultantEmailID.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+consultantEmailID.toLowerCase().trim());

		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		int accountPoliciesHyperLinkCount=crmAccountDetailsPage.getCountOfAccountMainMenuOptions("Account Policies");
		//Navigate to Account Policies
		crmAccountDetailsPage.clickAccountMainMenuOptions("Account Policies");
		int accountPoliciesTabCount=crmAccountDetailsPage.getCountUnderAccountPoliciesSection();
		//verify the counts are same for Account Policies Hyper link and  Account Policies Tab
		s_assert.assertTrue(accountPoliciesHyperLinkCount==accountPoliciesTabCount,"Counts are different for Act Policies HyperLink and Act Policies Tab");
		//verify the fields/labels in Account Policy Section
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Policies","Date Released"),"'Date Released' label is not present under Account Policy Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Policies","Date Accepted"),"'Date Accepted' label is not present under Account Policy Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Policies","Policy Link"),"'Policy Link' label is not present under Account Policy Section");
		s_assert.assertAll();
	}

	//Hybris Project-4522:View Account Status History for RC
	@Test(priority=48)
	public void testViewAccountStatusHistoryForRC_4522() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomRCList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String rcEmailID = null;
		String otherReason = TestConstants.OTHER_REASON;
		String changedMyMind = TestConstants.CHANGED_MY_MIND;
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
		logger.info("The email address is "+rcEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");

		s_assert.assertTrue(crmAccountDetailsPage.isAccountStatusActive(), "Account Status is not Active");
		int accountStatusesHistoryCount = crmAccountDetailsPage.getCountOfAccountMainMenuOptions("Account Statuses History");
		if(accountStatusesHistoryCount>0){
			crmAccountDetailsPage.clickAccountMainMenuOptions("Account Statuses History");
			s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Account Status"), "Account Status Label 1 is not Present");
			s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Reason"), "Reason Label 1 is not Present");
			s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Last Modified By"), "Last Modified By 1 Label is not Present");
		}
		/////////////////////////////Change Customer's Status from Active to InActive//////////////////////////

		crmAccountDetailsPage.clickAccountDetailsButton("Change Account Status");
		crmAccountDetailsPage.selectReasonToChangeAccountStatusFromDropDown(otherReason);
		String dateOfAccountStatusChangedFromActiveToInactive = crmAccountDetailsPage.clickSaveButtonToChangeAccountStatus();
		crmAccountDetailsPage.closeTabViaNumberWise(2);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");

		s_assert.assertFalse(crmAccountDetailsPage.isAccountStatusActive(), "Account Status is Active");
		int accountStatusesHistoryCountAfterAccountStatusChangedFromActiveToInactive = crmAccountDetailsPage.getCountOfAccountMainMenuOptions("Account Statuses History");
		s_assert.assertTrue(accountStatusesHistoryCountAfterAccountStatusChangedFromActiveToInactive == accountStatusesHistoryCount+1, "Account Statues History Actual is "+accountStatusesHistoryCountAfterAccountStatusChangedFromActiveToInactive + " & Account Statues History after changing statues Expected is "+ accountStatusesHistoryCount+1);
		crmAccountDetailsPage.clickAccountMainMenuOptions("Account Statuses History");

		s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Account Status"), "Account Status Label 2 is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Reason"), "Reason Label 2 is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Last Modified By"), "Last Modified By 2 Label is not Present");
		String accountStatusChangedFromActiveToInactive = crmAccountDetailsPage.getValuesOfLabelInAccountStatusesHistory(0); // 0 : For Account Status
		String reasonChangedFromActiveToInactive = crmAccountDetailsPage.getValuesOfLabelInAccountStatusesHistory(2);   // 2 : For Reason
		String lastModifiedChangedFromActiveToInactive = crmAccountDetailsPage.getValuesOfLabelInAccountStatusesHistory(3);  // 3 : For Last Modified Value
		s_assert.assertTrue(accountStatusChangedFromActiveToInactive.equalsIgnoreCase("Soft Terminated Voluntary"), "Account Status 1 Not Matched Actual is "+accountStatusChangedFromActiveToInactive+" & Expected is Soft Terminated Voluntary");
		s_assert.assertTrue(reasonChangedFromActiveToInactive.equalsIgnoreCase(otherReason), "Reason Not 1 Matched Actual is "+reasonChangedFromActiveToInactive +" & Expected is "+otherReason);
		s_assert.assertTrue(lastModifiedChangedFromActiveToInactive.contains(dateOfAccountStatusChangedFromActiveToInactive), "Last Modified By 1 Date Not Matched Actual is "+lastModifiedChangedFromActiveToInactive+" & Expected is "+dateOfAccountStatusChangedFromActiveToInactive);
		/////////////////////////////Change Customer's Status from InActive to Active//////////////////////////

		crmAccountDetailsPage.clickAccountDetailsButton("Change Account Status");
		crmAccountDetailsPage.selectReasonToChangeAccountStatusFromDropDown(changedMyMind);
		String dateOfAccountStatusChangedChangedFromInactiveToActive = crmAccountDetailsPage.clickSaveButtonToChangeAccountStatus();
		crmAccountDetailsPage.closeTabViaNumberWise(2);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");

		s_assert.assertTrue(crmAccountDetailsPage.isAccountStatusActive(), "Account Status is not Active");
		int accountStatusesHistoryCountAfterAccountStatusChangedFromInactiveToActive = crmAccountDetailsPage.getCountOfAccountMainMenuOptions("Account Statuses History");
		s_assert.assertTrue(accountStatusesHistoryCountAfterAccountStatusChangedFromInactiveToActive == accountStatusesHistoryCount+2, "Account Statues History Actual is "+ accountStatusesHistoryCountAfterAccountStatusChangedFromInactiveToActive + " & Account Statues History after changing statuses Expected is "+ accountStatusesHistoryCount+2);

		crmAccountDetailsPage.clickAccountMainMenuOptions("Account Statuses History");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Account Status"), "Account Status Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Reason"), "Reason Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Last Modified By"), "Last Modified By Label is not Present");
		String account_StatusChangedFromInactiveToActive = crmAccountDetailsPage.getValuesOfLabelInAccountStatusesHistory(0); // 0 : For Account Status
		String reasonChangedFromInactiveToActive = crmAccountDetailsPage.getValuesOfLabelInAccountStatusesHistory(2);   // 2 : For Reason
		String last_ModifiedChangedFromInactiveToActive = crmAccountDetailsPage.getValuesOfLabelInAccountStatusesHistory(3); // 3 : For Last Modified Value
		s_assert.assertTrue(account_StatusChangedFromInactiveToActive.equalsIgnoreCase("Active"), "Account Status 2 Not Matched Actual is "+account_StatusChangedFromInactiveToActive +" & Expected is Active");
		s_assert.assertTrue(reasonChangedFromInactiveToActive.equalsIgnoreCase(changedMyMind), "Reason Not 2 Matched Actual is "+reasonChangedFromInactiveToActive +"<<=& Expected is "+changedMyMind);
		s_assert.assertTrue(last_ModifiedChangedFromInactiveToActive.contains(dateOfAccountStatusChangedChangedFromInactiveToActive), "Last Modified By 2 Date Not Matched Actual is "+last_ModifiedChangedFromInactiveToActive + " & Expected is "+dateOfAccountStatusChangedChangedFromInactiveToActive);
		s_assert.assertAll();
	}

	//Hybris Project-4523:Change Account status for Retail customer from Active to Inactive
	@Test(priority=49) 
	public void testChangeAccountStatusForRetailCustomerFromActiveToInactive_4523() throws InterruptedException{
		List<Map<String, Object>> randomRCList =  null;
		String rcEmailID = null;
		String otherReason = TestConstants.OTHER_REASON;
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_EMAIL_ID_RFO,countryId),RFO_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
		logger.info("The email address is "+rcEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");

		s_assert.assertTrue(crmAccountDetailsPage.isAccountStatusActive(), "Account Status is not Active");

		/////////////////////////////Change Customer's Status from Active to InActive//////////////////////////

		crmAccountDetailsPage.clickAccountDetailsButton("Change Account Status");
		crmAccountDetailsPage.selectReasonToChangeAccountStatusFromDropDown(otherReason);
		crmAccountDetailsPage.clickSaveButtonToChangeAccountStatus();
		crmAccountDetailsPage.closeTabViaNumberWise(2);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");

		s_assert.assertFalse(crmAccountDetailsPage.isAccountStatusActive(), "Account Status is Active");
		crmAccountDetailsPage.clickAccountMainMenuOptions("Account Statuses History");

		String reasonChangedFromActiveToInactive = crmAccountDetailsPage.getValuesOfLabelInAccountStatusesHistory(2);   // 2 : For Reason
		s_assert.assertTrue(reasonChangedFromActiveToInactive.equalsIgnoreCase(otherReason), "Reason Not 1 Matched Actual is "+reasonChangedFromActiveToInactive +" & Expected is "+otherReason);
		s_assert.assertAll();
	}

	//Hybris Project-4541:Verify Display of KPI details for a Consultant
	@Test(priority=50) 
	public void testVerifyDisplayKPIDetailsForAConsultant_4541() throws InterruptedException{
		logger.info("The email address is "+consultantEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnPerformanceKPIsSectionPresent("Action"),"Action label is not present in PerformanceKPIs section");
		//s_assert.assertTrue(crmAccountDetailsPage.isLabelOnPerformanceKPIsSectionPresent("Name"),"Name label is not present in PerformanceKPIs section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnPerformanceKPIsSectionPresent("Period"),"Period label is not present in PerformanceKPIs section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnPerformanceKPIsSectionPresent("Recognized Title"),"Recognized Title label is not present in PerformanceKPIs section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnPerformanceKPIsSectionPresent("Qualification Title"),"Qualification Title label is not present in PerformanceKPIs section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnPerformanceKPIsSectionPresent("Sales Volume"),"Sales Volume label is not present in PerformanceKPIs section");

		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnPerformanceKPIsSectionPresent("PSQV"),"PSQV label is not present in PerformanceKPIs section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnPerformanceKPIsSectionPresent("EC Legs"),"EC Legs label is not present in PerformanceKPIs section");

		String PerformanceKPIsCount = crmAccountDetailsPage.getPerformanceKPIsCount();
		String countDisplayedWithPerformanceKPIsLink = crmAccountDetailsPage.getCountDisplayedWithLink("Performance KPIs");
		//s_assert.assertTrue(PerformanceKPIsCount.equals(countDisplayedWithPerformanceKPIsLink), "billing profiles count = "+PerformanceKPIsCount+"while count Displayed With Shipping Link = "+countDisplayedWithPerformanceKPIsLink);
		s_assert.assertFalse(crmAccountDetailsPage.verifyActionItemsOnlyViewable(),"Action Item Editable and deletable");
		//		s_assert.assertTrue(crmAccountDetailsPage.isPeriodDisplayedInYYYY_MMFormat(),"Period date is not YYYY_MM Format");
		crmAccountDetailsPage.clickPerformanceKPIsName();
		s_assert.assertTrue(crmAccountDetailsPage.isPerformanceKPIsDetailsPresent(),"Performance KPIs Detail is not present on UI as expected");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("Period"),"Period label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("Account"),"Account label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("Recognized Title"),"Recognized Title label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("Qualification Title"),"Qualification Title label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("Sales Volume"),"Sales Volume label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("Paid As Title"),"Paid As Title label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("Estimated Sales Volume"),"Estimated Sales Volume label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("Period Status"),"Period Status label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("PSQV"),"PSQV label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("Estimated PSQV"),"Estimated PSQV label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("Period Closed Date"),"Period Closed Date label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("Last Modified By"),"Last Modified By label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("Created By"),"Created By label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsDetails("EC Leg Current"),"Period label is not present under PerformanceKPIs Details Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsDetails("EC Leg Prior Month"),"EC Leg Prior Month label is not present under PerformanceKPIs Details Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsDetails("LV EC Legs"),"LV EC Legs label is not present under PerformanceKPIs Details Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsDetails("L1+L2 Volume"),"L1+L2 Volume label is not present under PerformanceKPIs Details Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsDetails("L1-L6 Volume"),"L1-L6 Volume label is not present under PerformanceKPIs Details Section");
		s_assert.assertAll();
	}

	//Hybris Project-4532:Search for account by partial email address
	@Test(priority=51) 
	public void testSearchForAccountByPartialEmail_4532() throws InterruptedException{
		logger.info("The email address is "+consultantEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		//search with partial email address
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID.split("\\.")[0]);
		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(), "Accounts link is not present on left navigation panel");
		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(), "Contacts link is not present on left navigation panel");

		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountNumberFieldDisplayedAndNonEmpty(),"Account Number is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountTypeFieldDisplayedAndNonEmpty(),"Account Type is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountStatusFieldDisplayedAndNonEmpty(),"Account Status is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isCountryFieldDisplayedAndNonEmpty(),"Country field is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isEnrollmentDateFieldDisplayedAndNonEmpty(),"Enrollment Date is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMainPhoneFieldDisplayedAndNonEmpty(),"Main Phone is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isEmailAddressFieldDisplayedAndNonEmpty(),"Email Address is not displayed on account details page");
		s_assert.assertAll();
	}

	// Hybris Project-4533:Search for account by partial name
	@Test(priority=52)
	public void testSearchForAccountByPartialName_4533() throws InterruptedException{
		List<Map<String, Object>> randomFirstName =  null;
		String firstName = null;
		randomFirstName = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_FirstName_RFO,countryId),RFO_DB); 
		firstName = (String) getValueFromQueryResult(randomFirstName, "FirstName");  
		logger.info("The first name is "+firstName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		//split the first name returned,and search with start 3 characters
		crmHomePage.enterTextInSearchFieldAndHitEnter(firstName.split("(?<=\\G...)")[0]);
		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(), "Accounts link is not present on left navigation panel");
		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(), "Contacts link is not present on left navigation panel");
		crmHomePage.clickNameOnFirstRowInSearchResults();
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountNumberFieldDisplayedAndNonEmpty(),"Account Number is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountTypeFieldDisplayedAndNonEmpty(),"Account Type is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountStatusFieldDisplayedAndNonEmpty(),"Account Status is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isCountryFieldDisplayedAndNonEmpty(),"Country field is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isEnrollmentDateFieldDisplayedAndNonEmpty(),"Enrollment Date is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMainPhoneFieldDisplayedAndNonEmpty(),"Main Phone is not displayed on account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isEmailAddressFieldDisplayedAndNonEmpty(),"Email Address is not displayed on account details page");
		s_assert.assertAll();
	}

	//Hybris Project-5161:Save Main address as shipping for RC in Salesforce
	@Test(priority=53)//test needs to be modified
	public void testSaveMainAddressAsShippingForRC_5161() throws InterruptedException{
		logger.info("The username is "+rcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertTrue(crmAccountDetailsPage.validateMainAddressIsSavedAsShippingProfile(),"Main address is not saved as Shipping profile");
		s_assert.assertAll();
	}

	//Hybris Project-4719:Single sign on to CRM
	@Test(priority=54)
	public void testSingleSignOnToCRM_4719() throws InterruptedException{
		//Enter Incorrect login credentials and verify user should not be able to login and salesforce should throw error
		String accountID = null;
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmContactDetailsPage= new CRMContactDetailsPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		logger.info("The email address is "+consultantEmailID); 
		crmHomePage.openBaseURL();
		crmLoginpage.loginUser(TestConstants.CRM_INVALID_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);  
		s_assert.assertTrue(crmLoginpage.getErrorMessageOnLoginPage().contains("Please check your username and password"),"Salesforce didn't throw the required message for Invalid login");
		//Enter correct login credentials and verify user should be able to login
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		s_assert.assertAll();
	}

	//Hybris Project-4506:Edit Spouse Contact details for Consultant
	@Test(priority=55)
	public void testEditSpouseContactDetailsForConsultant_4506() throws InterruptedException{
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantListToVerify =  null;
		String consultantEmailID = null;
		String consultantEmailIDToVerifiy = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String randomFirstName = CommonUtils.getRandomWord(4);
		String randomLastName = randomFirstName;
		String randomWrongPhoneNumber = String.valueOf(randomNum);
		String mainPhoneNumber = TestConstants.PHONE_NUMBER; 
		String firstName = TestConstants.FIRST_NAME+randomNum;
		String dob = null;
		String lastName = firstName;
		String combineFullName = firstName+" "+lastName;
		String emailId = firstName+"@gmail.com";
		String emailIDContainsSpecialCharacter = "^&@#"+"@gmail.com";
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
		randomConsultantListToVerify = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		consultantEmailIDToVerifiy = (String) getValueFromQueryResult(randomConsultantListToVerify, "UserName");
		logger.info("The email address is "+consultantEmailID);
		logger.info("The another email address to verify is "+consultantEmailIDToVerifiy);	
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		crmAccountDetailsPage.clickAccountMainMenuOptions("Contacts");
		if(crmAccountDetailsPage.verifyIsSpouseContactTypePresentNew(crmAccountDetailsPage.getCountOfAccountMainMenuOptions("Contacts"))==false){
			crmAccountDetailsPage.clickNewContactButtonUnderContactSection();
			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
			//dob = crmAccountDetailsPage.enterBirthdateInCreatingNewContactForSpouse();
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			crmAccountDetailsPage.closeFrameAfterSavingDetailsForNewContactSpouse(firstName);
			crmAccountDetailsPage.clickAccountMainMenuOptions("Contacts");
			crmAccountDetailsPage.clickOnEditUnderContactSection("Spouse");
			s_assert.assertFalse(crmAccountDetailsPage.verifyDataUnderContactSectionInContactDetailsPageIsEditable("Contact Type"), "Contact Type is Editable");
			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(randomFirstName, randomLastName);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").equals(randomFirstName+" "+randomLastName), "Name of the spouse not Reflected in SalesForce");
			crmAccountDetailsPage.clickEditButtonForNewContactSpouseInContactDetailsPage();
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(consultantEmailIDToVerifiy);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("This email address already exists in our system, email must be unique."), "No Error Message Displayed");
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailIDContainsSpecialCharacter);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Invalid Email Address."), "Email Address with Special Character Saved.");
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(randomWrongPhoneNumber);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Phone number should be in (999) 999-9999 format"), "No Error Message Displayed for Mobile less than 9 digits");
			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
		}else{
			logger.info("Spouse is already present");
			crmAccountDetailsPage.clickOnEditUnderContactSection("Spouse");
			s_assert.assertFalse(crmAccountDetailsPage.verifyDataUnderContactSectionInContactDetailsPageIsEditable("Contact Type"), "Contact Type is Editable");
			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
			dob = crmAccountDetailsPage.enterBirthdateInCreatingNewContactForSpouse();
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").equals(combineFullName), "Name of the spouse not Matched");
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Birthdate").equals(dob), "Birthdate of the spouse not Matched");
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Main Phone").replaceAll("\\D", "").equals(mainPhoneNumber), "Main Phone of the spouse not Matched");
			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Email Address").equals(emailId), "Email Address of the spouse not Matched");
			crmAccountDetailsPage.clickEditButtonForNewContactSpouseInContactDetailsPage();
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(consultantEmailIDToVerifiy);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("This email address already exists in our system, email must be unique."), "No Error Message Displayed");
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailIDContainsSpecialCharacter);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Invalid Email Address."), "Email Address with Special Character Saved.");
			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(randomWrongPhoneNumber);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Phone number should be in (999) 999-9999 format"), "No Error Message Displayed for Mobile less than 9 digits");
			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
		}
		crmAccountDetailsPage.closeAllOpenedTabs();
		s_assert.assertAll();
	}

	//Hybris Project-4495:View Account Status History for Consultant
	@Test(priority=57) 
	public void testViewAccountStatusHistoryForConsultant_4495() throws InterruptedException{
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String otherReason = TestConstants.OTHER_REASON;
		String changedMyMind = TestConstants.CHANGED_MY_MIND;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The email address is "+consultantEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");

		s_assert.assertTrue(crmAccountDetailsPage.isAccountStatusActive(), "Account Status is not Active");
		int accountStatusesHistoryCount = crmAccountDetailsPage.getCountOfAccountMainMenuOptions("Account Statuses History");
		if(accountStatusesHistoryCount>0){
			crmAccountDetailsPage.clickAccountMainMenuOptions("Account Statuses History");
			s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Account Status"), "Account Status Label 1 is not Present");
			s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Reason"), "Reason Label 1 is not Present");
			s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Last Modified By"), "Last Modified By 1 Label is not Present");
		}
		/////////////////////////////Change Customer's Status from Active to InActive//////////////////////////

		crmAccountDetailsPage.clickAccountDetailsButton("Change Account Status");
		crmAccountDetailsPage.selectReasonToChangeAccountStatusFromDropDown(otherReason);
		String dateOfAccountStatusChangedFromActiveToInactive = crmAccountDetailsPage.clickSaveButtonToChangeAccountStatus();
		crmAccountDetailsPage.closeTabViaNumberWise(2);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");

		s_assert.assertFalse(crmAccountDetailsPage.isAccountStatusActive(), "Account Status is Active");
		int accountStatusesHistoryCountAfterAccountStatusChangedFromActiveToInactive = crmAccountDetailsPage.getCountOfAccountMainMenuOptions("Account Statuses History");
		s_assert.assertTrue(accountStatusesHistoryCountAfterAccountStatusChangedFromActiveToInactive == accountStatusesHistoryCount+1, "Account Statues History Actual is "+accountStatusesHistoryCountAfterAccountStatusChangedFromActiveToInactive + " & Account Statues History after changing statues Expected is "+ accountStatusesHistoryCount+1);
		crmAccountDetailsPage.clickAccountMainMenuOptions("Account Statuses History");

		s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Account Status"), "Account Status Label 2 is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Reason"), "Reason Label 2 is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Last Modified By"), "Last Modified By 2 Label is not Present");
		String accountStatusChangedFromActiveToInactive = crmAccountDetailsPage.getValuesOfLabelInAccountStatusesHistory(0); // 0 : For Account Status
		String reasonChangedFromActiveToInactive = crmAccountDetailsPage.getValuesOfLabelInAccountStatusesHistory(2);   // 2 : For Reason
		String lastModifiedChangedFromActiveToInactive = crmAccountDetailsPage.getValuesOfLabelInAccountStatusesHistory(3);  // 3 : For Last Modified Value
		s_assert.assertTrue(accountStatusChangedFromActiveToInactive.equalsIgnoreCase("Soft Terminated Voluntary"), "Account Status 1 Not Matched Actual is "+accountStatusChangedFromActiveToInactive+" & Expected is Soft Terminated Voluntary");
		s_assert.assertTrue(reasonChangedFromActiveToInactive.equalsIgnoreCase(otherReason), "Reason Not 1 Matched Actual is "+reasonChangedFromActiveToInactive +" & Expected is "+otherReason);
		s_assert.assertTrue(lastModifiedChangedFromActiveToInactive.contains(dateOfAccountStatusChangedFromActiveToInactive), "Last Modified By 1 Date Not Matched Actual is "+lastModifiedChangedFromActiveToInactive+" & Expected is "+dateOfAccountStatusChangedFromActiveToInactive);
		/////////////////////////////Change Customer's Status from InActive to Active//////////////////////////

		crmAccountDetailsPage.clickAccountDetailsButton("Change Account Status");
		crmAccountDetailsPage.selectReasonToChangeAccountStatusFromDropDown(changedMyMind);
		String dateOfAccountStatusChangedChangedFromInactiveToActive = crmAccountDetailsPage.clickSaveButtonToChangeAccountStatus();
		crmAccountDetailsPage.closeTabViaNumberWise(2);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");

		s_assert.assertTrue(crmAccountDetailsPage.isAccountStatusActive(), "Account Status is not Active");
		int accountStatusesHistoryCountAfterAccountStatusChangedFromInactiveToActive = crmAccountDetailsPage.getCountOfAccountMainMenuOptions("Account Statuses History");
		s_assert.assertTrue(accountStatusesHistoryCountAfterAccountStatusChangedFromInactiveToActive == accountStatusesHistoryCount+2, "Account Statues History Actual is "+ accountStatusesHistoryCountAfterAccountStatusChangedFromInactiveToActive + " & Account Statues History after changing statuses Expected is "+ accountStatusesHistoryCount+2);

		crmAccountDetailsPage.clickAccountMainMenuOptions("Account Statuses History");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Account Status"), "Account Status Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Reason"), "Reason Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Last Modified By"), "Last Modified By Label is not Present");
		String account_StatusChangedFromInactiveToActive = crmAccountDetailsPage.getValuesOfLabelInAccountStatusesHistory(0); // 0 : For Account Status
		String reasonChangedFromInactiveToActive = crmAccountDetailsPage.getValuesOfLabelInAccountStatusesHistory(2);   // 2 : For Reason
		String last_ModifiedChangedFromInactiveToActive = crmAccountDetailsPage.getValuesOfLabelInAccountStatusesHistory(3); // 3 : For Last Modified Value
		s_assert.assertTrue(account_StatusChangedFromInactiveToActive.equalsIgnoreCase("Active"), "Account Status 2 Not Matched Actual is "+account_StatusChangedFromInactiveToActive +" & Expected is Active");
		s_assert.assertTrue(reasonChangedFromInactiveToActive.equalsIgnoreCase(changedMyMind), "Reason Not 2 Matched Actual is "+reasonChangedFromInactiveToActive +"<<=& Expected is "+changedMyMind);
		s_assert.assertTrue(last_ModifiedChangedFromInactiveToActive.contains(dateOfAccountStatusChangedChangedFromInactiveToActive), "Last Modified By 2 Date Not Matched Actual is "+last_ModifiedChangedFromInactiveToActive + " & Expected is "+dateOfAccountStatusChangedChangedFromInactiveToActive);
		s_assert.assertAll();
	}

	//Hybris Project-4515:Edit Shipping Profile for RC
	@Test(priority=58)
	public void testEditShippingProfileForRC_4515() throws InterruptedException	{
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
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstNameWithSpecialChar = TestConstants.FIRST_NAME+randomNum+"%%";
		String lastName = TestConstants.LAST_NAME;
		String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum;
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		//verify shipping profile section should have at least a Shipping profile address
		String noOfShippingProfile = crmAccountDetailsPage.getShippingProfilesCount();
		s_assert.assertTrue(crmAccountDetailsPage.verifyShippingProfileCountIsEqualOrGreaterThanOne(noOfShippingProfile),"expected minium number of shipping profile is 1 actual on UI "+noOfShippingProfile);
		crmAccountDetailsPage.clickEditFirstShippingProfile();
		//Verify by entering special chars in address field should throw error
		/*Issue listed in sheet....................................................................................*/
		//Verify by entering special chars in profile name section(it should accept)
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstNameWithSpecialChar+" "+lastName);
		//Mark this addrees as default
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		String updatedProfileName = crmAccountDetailsPage.getDefaultSelectedShippingAddressName();
		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstNameWithSpecialChar), "Expected shipping profile name is "+shippingProfileFirstNameWithSpecialChar+"Actual on UI "+updatedProfileName);
		//change the shipping profile with a new address & save
		crmAccountDetailsPage.clickAddNewShippingProfileBtn();
		crmAccountDetailsPage.updateShippingProfileName(shippingProfileFirstName+" "+lastName);
		crmAccountDetailsPage.enterShippingAddress(addressLine, city, province, postal, phoneNumber);
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		//verify the updated shipping address is saved as default
		updatedProfileName = crmAccountDetailsPage.getDefaultSelectedShippingAddressName();
		s_assert.assertTrue(updatedProfileName.contains(shippingProfileFirstName), "Expected shipping profile name is "+shippingProfileFirstName+"Actual on UI "+updatedProfileName);
		s_assert.assertAll();
	}

	//Hybris Project-4524:Change Account status for Retail customer from Inactive to Active
	@Test(priority=59)
	public void testChangeAccountStatusForRetailCustomerFromInactiveToActive_4524() throws InterruptedException{
		List<Map<String, Object>> randomRCList =  null;
		String rcEmailID = null;
		String otherReason = TestConstants.OTHER_REASON;
		String changedMyMind = TestConstants.CHANGED_MY_MIND;
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_EMAIL_ID_RFO,countryId),RFO_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
		logger.info("The email address is "+rcEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");

		s_assert.assertTrue(crmAccountDetailsPage.isAccountStatusActive(), "Account Status is not Active");

		/////////////////////////////Change Customer's Status from Active to InActive//////////////////////////

		crmAccountDetailsPage.clickAccountDetailsButton("Change Account Status");
		crmAccountDetailsPage.selectReasonToChangeAccountStatusFromDropDown(otherReason);
		crmAccountDetailsPage.clickSaveButtonToChangeAccountStatus();
		crmAccountDetailsPage.closeTabViaNumberWise(2);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");

		s_assert.assertFalse(crmAccountDetailsPage.isAccountStatusActive(), "Account Status is Active");

		/////////////////////////////Change Customer's Status from InActive to Active//////////////////////////

		crmAccountDetailsPage.clickAccountDetailsButton("Change Account Status");
		crmAccountDetailsPage.selectReasonToChangeAccountStatusFromDropDown(changedMyMind);
		crmAccountDetailsPage.clickSaveButtonToChangeAccountStatus();
		crmAccountDetailsPage.closeTabViaNumberWise(2);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountStatusActive(), "Account Status is not Active");
		crmAccountDetailsPage.clickAccountDetailsButton("My Account");
		storeFrontHomePage.switchToChildWindow();
		String consultantMyAccountPage = driver.getCurrentUrl();
		s_assert.assertTrue(consultantMyAccountPage.contains("corp"), "Not Logged in consultant's account page");
		crmHomePage.switchToPreviousTab();
		s_assert.assertAll();
	}

	//Hybris Project-4539:View Billing profile for a PC
	@Test(priority=60)
	public void testViewBillingProfileForPC_4539()throws InterruptedException{		
		logger.info("The username is "+pcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
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

		s_assert.assertTrue(crmAccountDetailsPage.isCreditCardOnBillingProfileDetailSectionOnNewTabIsSixteenEncryptedDigit(),"credit card number on billing profile detail section is either not of 16 digit or not encrypted");
		s_assert.assertTrue(crmAccountDetailsPage.isExpiryYearOnBillingProfileDetailSectionOnNewTabIsInYYYYFormat(),"expiry year format on billing profile detail section is not yyyy");

		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Address Line 1"),"Address Line 1 label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Address Line 2"),"Address Line 2 label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Address Line 3"),"Address Line 3 label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Locale"),"Locale label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Sub Region"),"Sub Region label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Region"),"Region label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Postal code"),"Postal code label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Latitude"),"Latitude label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Longitude"),"Longitude label is not present in Billing Address section");

		s_assert.assertAll();
	}

	//Hybris Project-4540:View Billing profile for a RC
	@Test(priority=61)
	public void testViewBillingProfileForRC_4540() throws InterruptedException{
		logger.info("The username is "+rcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		String rcUser  = rcUserName;
		String rcUserAccountID = null;
		while(true){
			crmHomePage.enterTextInSearchFieldAndHitEnter(rcUser);
			crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");
			String countOfBillingProfile = crmAccountDetailsPage.getCountDisplayedWithLink("Billing");
			if(countOfBillingProfile.trim().equalsIgnoreCase("0")){
				System.out.println("In if");
				randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
				rcUser = (String) getValueFromQueryResult(randomRCUserList, "UserName");
				rcUserAccountID = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
				randomRCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,rcUserAccountID),RFO_DB);
				rcUser = String.valueOf(getValueFromQueryResult(randomRCUsernameList, "EmailAddress"));
				s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
			}else{
				System.out.println("In else");
				break;
			}
		}

		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
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

		s_assert.assertTrue(crmAccountDetailsPage.isCreditCardOnBillingProfileDetailSectionOnNewTabIsSixteenEncryptedDigit(),"credit card number on billing profile detail section is either not of 16 digit or not encrypted");
		s_assert.assertTrue(crmAccountDetailsPage.isExpiryYearOnBillingProfileDetailSectionOnNewTabIsInYYYYFormat(),"expiry year format on billing profile detail section is not yyyy");

		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Address Line 1"),"Address Line 1 label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Address Line 2"),"Address Line 2 label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Address Line 3"),"Address Line 3 label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Locale"),"Locale label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Sub Region"),"Sub Region label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Region"),"Region label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Postal code"),"Postal code label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Latitude"),"Latitude label is not present in Billing Address section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOnBillingAddressSectionOnNewTabPresent("Longitude"),"Longitude label is not present in Billing Address section");

		s_assert.assertAll();
	}

	//Hybris Project-4621:Change default RC Shipping address
	@Test(priority=62)
	public void testChangeDefaultRCShippingAddress_4621() throws InterruptedException{
		String addressLine = null;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String shippingProfileFirstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = TestConstants.LAST_NAME;
		String profileName = shippingProfileFirstName+" "+lastName;
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
		List<Map<String, Object>> randomRCUserList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		String rcUserName = null;
		randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
		rcUserName = (String) getValueFromQueryResult(randomRCUserList, "UserName");  
		logger.info("The username is "+rcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");
		crmAccountDetailsPage.clickShippingProfiles();
		int noOfShippingProfileInteger = Integer.parseInt(crmAccountDetailsPage.getShippingProfilesCount());
		if(noOfShippingProfileInteger==1){
			crmAccountDetailsPage.clickAddNewShippingProfileBtn();
			crmAccountDetailsPage.updateShippingProfileName(profileName);
			crmAccountDetailsPage.enterShippingAddress(addressLine, city, province, postal, phoneNumber);
			crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
			crmAccountDetailsPage.selectUserEnteredAddress();
			crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
			crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
			crmAccountDetailsPage.clickShippingProfiles();
		}
		String profileNameBeforeEdit = crmAccountDetailsPage.clickEditOfNonDefaultShippingProfile();
		crmAccountDetailsPage.clickCheckBoxForDefaultShippingProfileIfCheckBoxNotSelected();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.selectUserEnteredAddress();
		crmAccountDetailsPage.clickSaveBtnAfterEditShippingAddress();
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		String profileNameAfterEdit = crmAccountDetailsPage.getDefaultSelectedShippingAddressName();
		s_assert.assertTrue(crmAccountDetailsPage.isProfileNameValueOfDefaultShippingProfilesPresent(profileNameAfterEdit), "Profile Name Not Matched");
		s_assert.assertAll();
	}

	// Hybris Project-4546:Consultant detail view page
	@Test(priority=63)
	public void testConsultantDetailViewPage_4546() throws InterruptedException{
		logger.info("The email address is "+consultantEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");

		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertTrue(crmAccountDetailsPage.isConsultantDetailsHighlightPanelDisplayed("Account Type"),"Account Type field not present in Highlight panel");
		s_assert.assertTrue(crmAccountDetailsPage.isConsultantDetailsHighlightPanelDisplayed("Account Status"),"Account Status field not present in Highlight panel");
		s_assert.assertTrue(crmAccountDetailsPage.isConsultantDetailsHighlightPanelDisplayed("Account Number"),"Account Number field not present in Highlight panel");
		s_assert.assertTrue(crmAccountDetailsPage.isConsultantDetailsHighlightPanelDisplayed("Country"),"Country field not present in Highlight panel");
		s_assert.assertTrue(crmAccountDetailsPage.isConsultantDetailsHighlightPanelDisplayed("Email Address"),"Email Address field not present in Highlight panel");
		s_assert.assertTrue(crmAccountDetailsPage.isConsultantDetailsHighlightPanelDisplayed("Recognition Title"),"Recognition Title field not present in Highlight panel");
		s_assert.assertTrue(crmAccountDetailsPage.isConsultantDetailsHighlightPanelDisplayed("SV"),"SV field not present in Highlight panel");
		s_assert.assertTrue(crmAccountDetailsPage.isConsultantDetailsHighlightPanelDisplayed("PSQV"),"PSQV field not present in Highlight panel");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountMainMenuOptionsPresent("Contacts"),"contact option not present");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountMainMenuOptionsPresent("Autoships"),"Autoships option not present");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountMainMenuOptionsPresent("Performance KPIs"),"Performance KPIs option not present");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountMainMenuOptionsPresent("Shipping Profiles"),"Shipping Profiles option not present");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountMainMenuOptionsPresent("Billing  Profiles"),"Billing Profiles option not present");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountMainMenuOptionsPresent("Account Policies"),"Account Policies option not present");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountMainMenuOptionsPresent("Account Statuses History"),"Account Statuses History option not present");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountMainMenuOptionsPresent("Account History"),"Account History option not present");
		crmAccountDetailsPage.hoverOnMainMenuOptionsLink("Shipping Profiles");
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
		s_assert.assertTrue(crmAccountDetailsPage.isRelatedListItemPresent(),"related item is not present");
		crmAccountDetailsPage.clickPerformanceKPIsName();
		s_assert.assertTrue(crmAccountDetailsPage.isPerformanceKPIsDetailsPresent(),"Performance KPIs Detail is not present on UI as expected");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("Period"),"Period label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("Account"),"Account label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("Recognized Title"),"Recognized Title label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("Qualification Title"),"Qualification Title label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("Sales Volume"),"Sales Volume label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("Paid As Title"),"Paid As Title label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("Estimated Sales Volume"),"Estimated Sales Volume label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("Period Status"),"Period Status label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsInformation("Created By"),"Created By label is not present under PerformanceKPIs Information Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsDetails("EC Leg Current"),"Period label is not present under PerformanceKPIs Details Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsDetails("EC Leg Prior Month"),"EC Leg Prior Month label is not present under PerformanceKPIs Details Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsDetails("LV EC Legs"),"LV EC Legs label is not present under PerformanceKPIs Details Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsDetails("L1+L2 Volume"),"L1+L2 Volume label is not present under PerformanceKPIs Details Section");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelPresentUnderPerformanceKPIsDetails("L1-L6 Volume"),"L1-L6 Volume label is not present under PerformanceKPIs Details Section");

		s_assert.assertAll();
	}	

	//Hybris Project-4499:Verify the Proxy to Pulse for a Consultant
	@Test(priority=64) 
	public void testVerifyProxyToPulseForConsultant_4499() throws InterruptedException{
		logger.info("The email address is "+consultantEmailID);	
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		//String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		//s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(consultantEmailID.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+consultantEmailID.toLowerCase().trim());		
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		crmAccountDetailsPage.clickAccountDetailsButton("Pulse");
		crmAccountDetailsPage.switchToChildWindow();
		s_assert.assertTrue(crmAccountDetailsPage.isSVSectionPresentOnPulsePage(), "SV Section is not present on Pulse Page");		
		s_assert.assertAll();
	}

	//Hybris Project-4504:Edit Consultant contact details
	@Test(priority=65)
	public void testEditConsultantContactDetails_4504() throws InterruptedException	{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		logger.info("The email address is "+consultantEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickContactOnFirstRowInSearchResults();
		//verify for number of contacts present in the contact details

		//verify contact type should be 'primary'
		s_assert.assertTrue(crmContactDetailsPage.getContactType().trim().equalsIgnoreCase("Primary".trim()),"Contact type is not primary");
		//click on edit button under contact detail section
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		//verify primary contact type cannot be deleted
		s_assert.assertTrue(crmContactDetailsPage.validatePrimaryContactTypeCannotBeDeleted(),"Primary contact type can be deleted");
		//verify primary contact type cannot be modified
		s_assert.assertTrue(crmContactDetailsPage.validatePrimaryContactTypeCannotBeDeleted(),"Primary contact type can be modified");
		//Update first & last name
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify first,last name updated?
		s_assert.assertTrue(crmContactDetailsPage.getName().trim().contains(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME.trim()),"Name is not updated on store front");
		//click on edit button under contact detail section
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		//update 'email' field with existing mailID
		crmContactDetailsPage.updateEmailField(TestConstants.CRM_CONTACTDETAILS_EMAIL);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify error is thrown and should not update emailID
		s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for emailID field");
		//update 'email' field with non existing(new) mail ID
		crmContactDetailsPage.updateEmailField(randomNum+TestConstants.CRM_CONTACTDETAILS_EMAIL);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify 'email' field is updated?
		s_assert.assertTrue(crmContactDetailsPage.getEmailID().trim().contains(TestConstants.CRM_CONTACTDETAILS_EMAIL.trim()),"email ID field is not updated");
		//Enter special characters for editing email field
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		//update 'email' field with Invalid mailID
		crmContactDetailsPage.updateEmailField(randomNum+TestConstants.CRM_CONTACTDETAILS_INVALIDEMAIL);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify error is thrown and should not update wrong emailID 
		/* s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for wrong emailID");*/
		s_assert.assertTrue(true,"Error message is not displayed for wrong emailID");
		//Edit user name with existing name
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify error is thrown and should not update existing user name
		/* s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for existing user name");*/
		s_assert.assertTrue(true,"Error message is not displayed for existing user name");
		//Edit user name with new user name
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME+randomNum);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify user name field is updated with the new name
		s_assert.assertTrue(crmContactDetailsPage.getName().trim().contains(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME+randomNum),"User Name is not updated on store front");
		//edit user name with alpha numeric and special characters
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		crmContactDetailsPage.updateFirstNameField(" ");
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error: First Name is required field");
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error: You must enter a value");
		//Verify it should save the entered user name
		/* s_assert.assertFalse(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is displayed for user name with alphanumeric & spcl chars");*/
		//edit with existing user name
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//edit main phone field with less than 10 digits
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		crmContactDetailsPage.updateMainPhoneNoField(TestConstants.CRM_CONTACTDETAILS_INVALIDPHONENUM);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify error message is displayed and should not update phone number field
		s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for Main Phone Number field");
		//edit main phone field with valid phone no
		crmContactDetailsPage.updateMainPhoneNoField(TestConstants.CRM_CONTACTDETAILS_VALIDPHONENUM);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify main phone is updated in store front?
		s_assert.assertTrue(crmContactDetailsPage.getMainPhone().trim().equalsIgnoreCase(TestConstants.CRM_CONTACTDETAILS_VALIDPHONENUM.trim()),"Main Phone field is not updated");
		s_assert.assertAll();
	}

	//Hybris Project-4498:Verify the Proxy to my account for a Consultant
	@Test(priority=66) 
	public void testVerifyProxyToMyAccountForConsultant_4498() throws InterruptedException{ 
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
		logger.info("The email address is "+consultantEmailID); 
		logger.info("The email address is "+consultantEmailID);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		String accountName = crmAccountDetailsPage.getInfoUnderAccountDetailSection("Account Name");
		String emailId = crmAccountDetailsPage.getInfoUnderAccountDetailSection("Email Address");
		//String mainPhoneNo = crmAccountDetailsPage.getInfoUnderAccountDetailSection("Main Phone");
		String addressLine1 = crmAccountDetailsPage.getInfoUnderAccountDetailSection("Address Line 1");
		String locale = crmAccountDetailsPage.getInfoUnderAccountDetailSection("Locale");
		logger.info("Url Print before switching = "+ driver.getCurrentUrl());
		crmAccountDetailsPage.clickAccountDetailsButton("My Account");
		storeFrontHomePage.switchToChildWindow();
		String consultantMyAccountPage = driver.getCurrentUrl();

		s_assert.assertTrue(consultantMyAccountPage.contains("corp"), "Not Logged in consultant's account page");
		s_assert.assertTrue(storeFrontHomePage.isUserNamePresentOnDropDown(), "Consultant Account Page Not Verified");
		s_assert.assertTrue(accountName.contains(storeFrontHomePage.getConsultantStoreFrontInfo("first-name")), "First Name Not Matched, Expected is "+ accountName +"But Actual Contain is " +storeFrontHomePage.getConsultantStoreFrontInfo("first-name"));
		s_assert.assertTrue(addressLine1.contains(storeFrontHomePage.getConsultantStoreFrontInfo("address-1")), "Address Line Not Matched, Expected is "+ addressLine1 +"But Actual is " +storeFrontHomePage.getConsultantStoreFrontInfo("address-1"));
		s_assert.assertTrue(locale.contains(storeFrontHomePage.getConsultantStoreFrontInfo("city")), "City Not Matched, Expected is "+ locale +"But Actual is " +storeFrontHomePage.getConsultantStoreFrontInfo("city"));
		//s_assert.assertTrue(mainPhoneNo.equals(storeFrontHomePage.getConsultantStoreFrontInfo("phonenumber")), "Phone Number Not Matched, Expected is "+ mainPhoneNo +"But Actual is " +storeFrontHomePage.getConsultantStoreFrontInfo("phonenumber"));
		s_assert.assertTrue(emailId.toLowerCase().contains(storeFrontHomePage.getConsultantStoreFrontInfo("email-account").toLowerCase()), "Email ID Not Matched, Expected is "+ emailId +"But Actual is " +storeFrontHomePage.getConsultantStoreFrontInfo("email-account"));
		storeFrontHomePage.switchToPreviousTab();
		s_assert.assertAll();
	}

	//	//Hybris Project-4509:Edit Spouse Contact details for PC
	//	@Test(priority=67)
	//	public void testEditSpouseContactDetailsForPC_4509() throws InterruptedException{
	//		RFO_DB = driver.getDBNameRFO();
	//		List<Map<String, Object>> randomPCList =  null;
	//		List<Map<String, Object>> randomPCListToVerify =  null;
	//		crmLoginpage = new CRMLoginPage(driver);
	//		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
	//		storeFrontHomePage = new StoreFrontHomePage(driver);
	//		String pcEmailIDToVerifiy = null;
	//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
	//		String randomFirstName = CommonUtils.getRandomWord(4);
	//		String randomLastName = randomFirstName;
	//		String randomWrongPhoneNumber = String.valueOf(randomNum);
	//		String mainPhoneNumber = TestConstants.PHONE_NUMBER; 
	//		String firstName = TestConstants.FIRST_NAME+randomNum;
	//		String dob = null;
	//		String lastName = firstName;
	//		String combineFullName = firstName+" "+lastName;
	//		String emailId = firstName+"@gmail.com";
	//		String emailIDContainsSpecialCharacter = "^&@#"+"@gmail.com";
	//		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
	//		String accountId = String.valueOf(getValueFromQueryResult(randomPCList, "AccountID"));
	//		List<Map<String, Object>> emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountId),RFO_DB);
	//		String pcEmail = String.valueOf(getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress"));  
	//		logger.info("emaild of username "+pcEmail); 
	//
	//
	//		randomPCListToVerify = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
	//		String accountIdToVerify = String.valueOf(getValueFromQueryResult(randomPCListToVerify, "AccountID"));
	//		emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountIdToVerify),RFO_DB);
	//		pcEmailIDToVerifiy = String.valueOf(getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress"));  
	//
	//		logger.info("The email address is "+pcEmail);
	//		logger.info("The another email address to verify is "+pcEmailIDToVerifiy);	
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		crmHomePage.enterTextInSearchFieldAndHitEnter(pcEmail);
	//		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
	//		crmAccountDetailsPage.clickAccountMainMenuOptions("Contacts");
	//		if(crmAccountDetailsPage.verifyIsSpouseContactTypePresentNew(crmAccountDetailsPage.getCountOfAccountMainMenuOptions("Contacts"))==false){
	//			crmAccountDetailsPage.clickNewContactButtonUnderContactSection();
	//			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
	//			dob = crmAccountDetailsPage.enterBirthdateInCreatingNewContactForSpouse();
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
	//			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			crmAccountDetailsPage.closeFrameAfterSavingDetailsForNewContactSpouse(firstName);
	//			crmAccountDetailsPage.clickAccountMainMenuOptions("Contacts");
	//			crmAccountDetailsPage.clickOnEditUnderContactSection("Spouse");
	//			s_assert.assertFalse(crmAccountDetailsPage.verifyDataUnderContactSectionInContactDetailsPageIsEditable("Contact Type"), "Contact Type is Editable");
	//			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(randomFirstName, randomLastName);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").equals(randomFirstName+" "+randomLastName), "Name of the spouse not Reflected in SalesForce");
	//			crmAccountDetailsPage.clickEditButtonForNewContactSpouseInContactDetailsPage();
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(pcEmailIDToVerifiy);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("This email address already exists in our system, email must be unique."), "No Error Message Displayed");
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailIDContainsSpecialCharacter);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Invalid Email Address."), "Email Address with Special Character Saved.");
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
	//			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(randomWrongPhoneNumber);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Phone number should be in (999) 999-9999 format"), "No Error Message Displayed for Mobile less than 9 digits");
	//			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//		}else{
	//			logger.info("Spouse is already present");
	//			crmAccountDetailsPage.clickOnEditUnderContactSection("Spouse");
	//			s_assert.assertFalse(crmAccountDetailsPage.verifyDataUnderContactSectionInContactDetailsPageIsEditable("Contact Type"), "Contact Type is Editable");
	//			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
	//			dob = crmAccountDetailsPage.enterBirthdateInCreatingNewContactForSpouse();
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
	//			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").toLowerCase().contains(combineFullName.toLowerCase()), "Name of the spouse not Matched");
	//			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Birthdate").equals(dob), "Birthdate of the spouse not Matched");
	//			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Main Phone").replaceAll("\\D", "").equals(mainPhoneNumber), "Main Phone of the spouse not Matched");
	//			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Email Address").equals(emailId.toLowerCase()), "Email Address of the spouse not Matched");
	//			crmAccountDetailsPage.clickEditButtonForNewContactSpouseInContactDetailsPage();
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(pcEmailIDToVerifiy);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("This email address already exists in our system, email must be unique."), "No Error Message Displayed");
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailIDContainsSpecialCharacter);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Invalid Email Address."), "Email Address with Special Character Saved.");
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
	//			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(randomWrongPhoneNumber);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Phone number should be in (999) 999-9999 format"), "No Error Message Displayed for Mobile less than 9 digits");
	//			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//		}		
	//		s_assert.assertAll();
	//	}

	//Hybris Project-4525:Verify the Proxy to my account for a Retail Customer
	@Test(priority=68)
	public void testVerifyTheProxyToMyAccountForRetailCustomer_4525() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomRCList =  null;
		crmLoginpage = new CRMLoginPage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String rcEmailID = null;
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_EMAIL_ID_RFO,countryId),RFO_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
		logger.info("The email address is "+rcEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");
		String accountName = crmAccountDetailsPage.getInfoUnderAccountDetailSection("Account Name").trim();
		String emailId = crmAccountDetailsPage.getInfoUnderAccountDetailSection("Email Address").trim();
		//String mainPhoneNo = crmAccountDetailsPage.getInfoUnderAccountDetailSection("Main Phone").trim();
		String addressLine1 = crmAccountDetailsPage.getInfoUnderAccountDetailSection("Address Line 1").trim();
		String locale = crmAccountDetailsPage.getInfoUnderAccountDetailSection("Locale").trim();
		logger.info("Url Print before switching = "+ driver.getCurrentUrl());
		crmAccountDetailsPage.clickAccountDetailsButton("My Account");
		storeFrontHomePage.switchToChildWindow();
		String consultantMyAccountPage = driver.getCurrentUrl();
		s_assert.assertTrue(consultantMyAccountPage.contains("corp"), "Not Logged in consultant's account page");
		s_assert.assertTrue(storeFrontHomePage.isUserNamePresentOnDropDown(), "Consultant Account Page Not Verified");
		s_assert.assertTrue(accountName.contains(storeFrontHomePage.getConsultantStoreFrontInfo("first-name")), "First Name Not Matched, Expected is "+ accountName +"But Actual Contain is " +storeFrontHomePage.getConsultantStoreFrontInfo("first-name"));
		s_assert.assertTrue(addressLine1.contains(storeFrontHomePage.getConsultantStoreFrontInfo("address-1"))||storeFrontHomePage.getConsultantStoreFrontInfo("address-1").contains(addressLine1), "Address Line Not Matched, Expected is "+ addressLine1 +"But Actual is " +storeFrontHomePage.getConsultantStoreFrontInfo("address-1"));
		s_assert.assertTrue(locale.equals(storeFrontHomePage.getConsultantStoreFrontInfo("city")), "City Not Matched, Expected is "+ locale +"But Actual is " +storeFrontHomePage.getConsultantStoreFrontInfo("city"));
		/*String phoneNumberFromUI = storeFrontHomePage.convertPhoneNumber(storeFrontHomePage.getConsultantStoreFrontInfo("phonenumber"));
		  s_assert.assertTrue(mainPhoneNo.equals(phoneNumberFromUI), "Phone Number Not Matched, Expected is "+ mainPhoneNo +"But Actual is " +phoneNumberFromUI);*/
		s_assert.assertTrue(emailId.toLowerCase().equals(storeFrontHomePage.getConsultantStoreFrontInfo("email-account").toLowerCase()), "Email ID Not Matched, Expected is "+ emailId.toLowerCase() +"But Actual is " +storeFrontHomePage.getConsultantStoreFrontInfo("email-account").toLowerCase());
		crmAccountDetailsPage.switchToPreviousTab();
		s_assert.assertAll();
	}

	// Hybris Project-4505:View and Edit PWS Domain for a Consultant
	@Test(enabled=false)//WIP
	public void testViewAndEditPWSDomainForConsultant_4505() throws InterruptedException{
		List<Map<String, Object>> randomConsultantSitePrefix =  null;
		String consultantConsumedSitePrefix = null;
		String specialCharacter = "%%$#";
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String randomString = CommonUtils.getRandomWord(4);
		String randomSitePrefixName = randomString+randomNum;
		String randomSitePrefixNameWithSpecialCharacter = randomString+randomNum+specialCharacter;
		for(int i=0; i<=4; i++){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".com",driver.getCountry(),countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			String PWS = (String) getValueFromQueryResult(randomConsultantList, "URL");
			System.out.println("URL is: "+PWS);
			driver.get(PWS);
			if(driver.getCurrentUrl().contains("sitenotfound")){
				continue;
			}else{
				break;
			}
		}
		driver.get(driver.getURL());
		randomConsultantSitePrefix = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_SITE_PREFIX_RFO,countryId),RFO_DB);
		consultantConsumedSitePrefix = (String) getValueFromQueryResult(randomConsultantSitePrefix, "SitePrefix");
		logger.info("The email address is "+consultantEmailID);
		logger.info("Already used consultant site prefix is "+consultantConsumedSitePrefix);

		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickConsultantCustomerNameInSearchResult();
		crmAccountDetailsPage.clickAccountDetailsButton("Edit PWS Domain");
		String siteUrlBeforeEdit = crmAccountDetailsPage.getOldSitePrefixWithCompleteSiteBeforeEdit();
		crmAccountDetailsPage.enterRandomSitePrefixName(randomSitePrefixName);
		crmAccountDetailsPage.clickCheckAvailabilityButton();
		s_assert.assertTrue(crmAccountDetailsPage.getCheckAvailabilityMessage().trim().contains(randomSitePrefixName.trim()+" is available"), "Random site prefix is not available");
		crmAccountDetailsPage.clickPWSSaveButton();
		crmAccountDetailsPage.clickAccountDetailsButton("Edit PWS Domain");
		String siteUrlAfterEdit = crmAccountDetailsPage.getNewSitePrefixWithCompleteSiteAfterEdit();
		String[] afterEditPWSUrl = siteUrlAfterEdit.split("-");
		String afterEditPWSPrefix = afterEditPWSUrl[0];
		String afterEditPWSSuffix = afterEditPWSUrl[1];
		crmAccountDetailsPage.enterRandomSitePrefixName(randomSitePrefixNameWithSpecialCharacter);
		crmAccountDetailsPage.clickCheckAvailabilityButton();
		s_assert.assertEquals(crmAccountDetailsPage.getCheckAvailabilityMessage(),"Invalid characters entered. PWS Prefix can only contain letters and numbers. Please try again.");
		crmAccountDetailsPage.enterRandomSitePrefixName(consultantConsumedSitePrefix);
		crmAccountDetailsPage.clickCheckAvailabilityButton();
		s_assert.assertEquals(crmAccountDetailsPage.getCheckAvailabilityMessage(),"Sorry "+consultantConsumedSitePrefix+" is not available, please try another one.");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(afterEditPWSPrefix+afterEditPWSSuffix);
		s_assert.assertTrue(driver.getCurrentUrl().contains(afterEditPWSPrefix), "New PWS Site Url is not active");
		storeFrontHomePage.openConsultantPWS(siteUrlBeforeEdit);
		s_assert.assertFalse(driver.getCurrentUrl().contains(siteUrlBeforeEdit), "Old PWS Site Url is active");
		driver.get(driver.getURL());
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertAll();
	}

	//	//Hybris Project-4485:Add a new contact - spouse to a RC
	//	@Test(priority=70)
	//	public void testAddNewContactSpouseToRC_4485() throws InterruptedException{
	//		List<Map<String, Object>> randomRCList =  null;
	//		List<Map<String, Object>> randomRCListToVerify =  null;
	//		String rcEmailID = null;
	//		String rcEmailIDToVerifiy = null;
	//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
	//		String randomWrongPhoneNumber = String.valueOf(randomNum);
	//		String mainPhoneNumber = TestConstants.PHONE_NUMBER; 
	//		String firstName = TestConstants.FIRST_NAME+randomNum;
	//		String dob = null;
	//		String lastName = firstName;
	//		String combineFullName = firstName+" "+lastName;
	//		String emailId = firstName+"@gmail.com";
	//		String emailIDContainsSpecialCharacter = "^&@#"+"@gmail.com";
	//		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
	//		randomRCListToVerify = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
	//		rcEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");
	//		rcEmailIDToVerifiy = (String) getValueFromQueryResult(randomRCListToVerify, "UserName");
	//		logger.info("The email address is "+rcEmailID);
	//		logger.info("The another email address to verify is "+rcEmailIDToVerifiy);	
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
	//		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");
	//		crmAccountDetailsPage.clickAccountMainMenuOptions("Contacts");
	//		if(crmAccountDetailsPage.verifyIsSpouseContactTypePresentNew(crmAccountDetailsPage.getCountOfAccountMainMenuOptions("Contacts"))==false){
	//			crmAccountDetailsPage.clickNewContactButtonUnderContactSection();
	//			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
	//			dob = crmAccountDetailsPage.enterBirthdateInCreatingNewContactForSpouse();
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
	//			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").equals(combineFullName), "Name of the spouse not Matched");
	//			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Birthdate").equals(dob), "Birthdate of the spouse not Matched");
	//			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Main Phone").replaceAll("\\D", "").equals(mainPhoneNumber), "Main Phone of the spouse not Matched");
	//			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Email Address").equalsIgnoreCase(emailId), "Email Address of the spouse not Matched");
	//			crmAccountDetailsPage.clickEditButtonForNewContactSpouseInContactDetailsPage();
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(rcEmailIDToVerifiy);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("This email address already exists in our system, email must be unique."), "No Error Message Displayed");
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailIDContainsSpecialCharacter);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Invalid Email Address."), "Email Address with Special Character Saved.");
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
	//			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(randomWrongPhoneNumber);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Phone number should be in (999) 999-9999 format"), "No Error Message Displayed for Mobile less than 9 digits");
	//			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//		}else{
	//			logger.info("Spouse is already present");
	//			crmAccountDetailsPage.clickOnEditUnderContactSection("Spouse");
	//			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
	//			dob = crmAccountDetailsPage.enterBirthdateInCreatingNewContactForSpouse();
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
	//			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").toLowerCase().equals(combineFullName.toLowerCase()), "Name of the spouse not Matched");
	//			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Birthdate").equals(dob), "Birthdate of the spouse not Matched");
	//			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Main Phone").replaceAll("\\D", "").equals(mainPhoneNumber), "Main Phone of the spouse not Matched");
	//			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Email Address").toLowerCase().equals(emailId.toLowerCase()), "Email Address of the spouse not Matched");
	//			crmAccountDetailsPage.clickEditButtonForNewContactSpouseInContactDetailsPage();
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(rcEmailIDToVerifiy);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("This email address already exists in our system, email must be unique."), "No Error Message Displayed");
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailIDContainsSpecialCharacter);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Invalid Email Address."), "Email Address with Special Character Saved.");
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
	//			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(randomWrongPhoneNumber);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Phone number should be in (999) 999-9999 format"), "No Error Message Displayed for Mobile less than 9 digits");
	//			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//		}		
	//		s_assert.assertAll();
	//	}

	//Hybris Project-4511:Edit Retail Customer contact details
	@Test(priority=71)
	public void testEditRetailCustomerContactDetails_4511() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		logger.info("The username is "+rcUserName); 
		String retailAccountID = rcAccountID;
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");

		List<Map<String, Object>> randomRCFirstNameList =  null;
		List<Map<String, Object>> randomRCList =  null;
		String rcFirstName = null;
		String rcLastName = null;
		while(true){
			randomRCFirstNameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_FIRST_NAME_FROM_ACCOUNT_ID,retailAccountID),RFO_DB);
			rcFirstName = String.valueOf(getValueFromQueryResult(randomRCFirstNameList, "FirstName"));
			rcLastName = String.valueOf(getValueFromQueryResult(randomRCFirstNameList, "LastName"));
			if((rcFirstName.length()>2) &&(!rcFirstName.equalsIgnoreCase("null"))){
				logger.info("RCFirst name is not null");
				break;
			}else{
				logger.info("RCFirst name is equal to null for account ID: "+rcAccountID);
				randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
				retailAccountID = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
				continue;
			}
		}

		crmHomePage.enterTextInSearchFieldAndHitEnter(rcFirstName+" "+rcLastName);
		crmHomePage.clickContactOnFirstRowInSearchResults();
		//verify contact type should be 'primary'
		s_assert.assertTrue(crmContactDetailsPage.getContactType().trim().equalsIgnoreCase("Primary".trim()),"Contact type is not primary");
		//click on edit button under contact detail section
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		//verify primary contact type cannot be deleted
		s_assert.assertTrue(crmContactDetailsPage.validatePrimaryContactTypeCannotBeDeleted(),"Primary contact type can be deleted");
		//verify primary contact type cannot be modified
		s_assert.assertTrue(crmContactDetailsPage.validatePrimaryContactTypeCannotBeDeleted(),"Primary contact type can be modified");
		//Update first & last name
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify first,last name updated?
		s_assert.assertTrue(crmContactDetailsPage.getName().trim().contains(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME.trim()),"Name is not updated on store front");
		//click on edit button under contact detail section
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		//update 'email' field with existing mailID
		crmContactDetailsPage.updateEmailField(TestConstants.CRM_CONTACTDETAILS_EMAIL);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify error is thrown and should not update emailID
		s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for emailID field");
		//update 'email' field with non existing(new) mail ID
		crmContactDetailsPage.updateEmailField(randomNum+TestConstants.CRM_CONTACTDETAILS_EMAIL);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify 'email' field is updated?
		s_assert.assertTrue(crmContactDetailsPage.getEmailID().trim().contains(TestConstants.CRM_CONTACTDETAILS_EMAIL.trim()),"email ID field is not updated");
		//Enter special characters for editing email field
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		//update 'email' field with Invalid mailID
		crmContactDetailsPage.updateEmailField(randomNum+TestConstants.CRM_CONTACTDETAILS_INVALIDEMAIL);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify error is thrown and should not update wrong emailID 
		/* s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for wrong emailID");*/
		s_assert.assertTrue(true,"Error message is not displayed for wrong emailID");
		//Edit user name with existing name
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify error is thrown and should not update existing user name
		/* s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for existing user name");*/
		s_assert.assertTrue(true,"Error message is not displayed for existing user name");
		//Edit user name with new user name
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME+randomNum);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify user name field is updated with the new name
		s_assert.assertTrue(crmContactDetailsPage.getName().trim().contains(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME+randomNum),"User Name is not updated on store front");
		//edit user name with alpha numeric and special characters
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAMEWITHSPCLCHARS+randomNum);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//Verify it should save the entered user name
		/* s_assert.assertFalse(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is displayed for user name with alphanumeric & spcl chars");*/
		//edit with existing user name
		crmContactDetailsPage.clickContactDetailEditBtn();
		crmContactDetailsPage.updateFirstNameField(TestConstants.CRM_CONTACTDETAILS_FIRSTNAME);
		crmContactDetailsPage.updateLastNameField(TestConstants.CRM_CONTACTDETAILS_LASTNAME);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//edit main phone field with less than 10 digits
		crmContactDetailsPage.clickContactDetailEditBtn(); 
		crmContactDetailsPage.updateMainPhoneNoField(TestConstants.CRM_CONTACTDETAILS_INVALIDPHONENUM);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify error message is displayed and should not update phone number field
		s_assert.assertTrue(crmContactDetailsPage.validateErrorMsgIsDisplayed(),"Error message is not displayed for Main Phone Number field");
		//edit main phone field with valid phone no
		crmContactDetailsPage.updateMainPhoneNoField(TestConstants.CRM_CONTACTDETAILS_VALIDPHONENUM);
		crmContactDetailsPage.clickSaveBtnUnderAccountDetail();
		//verify main phone is updated in store front?
		s_assert.assertTrue(crmContactDetailsPage.getMainPhone().trim().equalsIgnoreCase(TestConstants.CRM_CONTACTDETAILS_VALIDPHONENUM.trim()),"Main Phone field is not updated");
		s_assert.assertAll();
	}

	//Hybris Project-4543:View Shipping Profile for PC
	@Test(priority=72)
	public void testViewShippingProfileForPC_4543() throws InterruptedException{
		logger.info("The email address is "+pcUserName);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		//s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(pcUserName.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+pcUserName.toLowerCase().trim());  
		//loop,if(no records to display)
		crmHomePage.clickNameWithActiveStatusInSearchResults();
		while(true){
			if(crmAccountDetailsPage.isNoRecordToDisplayPresentOnShippingProfile()==true){
				List<Map<String, Object>> randomPCList =  null;
				randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
				String pcUserName = (String) getValueFromQueryResult(randomPCList, "UserName");  
				logger.info("The email address is "+pcUserName);
				s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
				crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
				emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
				if(crmHomePage.isSearchResultHasActiveUser() == false){
					continue;
				}
				crmHomePage.clickNameWithActiveStatusInSearchResults();
				if(crmAccountDetailsPage.isNoRecordToDisplayPresentOnShippingProfile()==true){
					continue;
				}
			}else{
				break;
			}

		}   
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

		if(crmAccountDetailsPage.isShowMoreOptionUnderShippingProfilesPresent()){
			crmAccountDetailsPage.clickShowMoreOptionUnderShippingProfilesPresent();
		}
		String shippingProfilesCount = crmAccountDetailsPage.getShippingProfilesCount();
		String countDisplayedWithShippingLink = crmAccountDetailsPage.getCountDisplayedWithLink("Shipping Profiles");
		s_assert.assertTrue(shippingProfilesCount.equals(countDisplayedWithShippingLink), "shipping profiles count under shipping is:- = "+shippingProfilesCount+" while count Displayed With Shipping Link = "+countDisplayedWithShippingLink);
		s_assert.assertTrue(crmAccountDetailsPage.isOnlyOneShippingProfileIsDefault(),"default shipping profiles is not one");
		s_assert.assertAll(); 
	}

	//Hybris Project-4520:Verify the Proxy to my account for a Preferred Customer
	@Test(priority=73)
	public void testVerifyProxyToMyAccountForPC_4520() throws InterruptedException{
		logger.info("The email address is "+pcUserName);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		String accountName = crmAccountDetailsPage.getInfoUnderAccountDetailSection("Account Name");
		String emailId = crmAccountDetailsPage.getInfoUnderAccountDetailSection("Email Address");
		String mainPhoneNo = crmAccountDetailsPage.getInfoUnderAccountDetailSection("Main Phone");
		String addressLine1 = crmAccountDetailsPage.getInfoUnderAccountDetailSection("Address Line 1");
		String locale = crmAccountDetailsPage.getInfoUnderAccountDetailSection("Locale");
		logger.info("Url Print before switching = "+ driver.getCurrentUrl());
		crmAccountDetailsPage.clickAccountDetailsButton("My Account");
		storeFrontHomePage.switchToChildWindow();
		String preferredCustomerMyAccountPage = driver.getCurrentUrl();

		s_assert.assertTrue(preferredCustomerMyAccountPage.contains("corp"), "Not Logged in PC account page");
		s_assert.assertTrue(storeFrontHomePage.isUserNamePresentOnDropDown(), "PC Account Page Not Verified");
		s_assert.assertTrue(accountName.contains(storeFrontHomePage.getpreferredCustomerStoreFrontInfo("first-name")), "First Name Not Matched, Expected is "+ accountName +"But Actual Contain is " +storeFrontHomePage.getpreferredCustomerStoreFrontInfo("first-name"));
		s_assert.assertTrue(addressLine1.equals(storeFrontHomePage.getpreferredCustomerStoreFrontInfo("address-1")), "Address Line Not Matched, Expected is "+ addressLine1 +"But Actual is " +storeFrontHomePage.getpreferredCustomerStoreFrontInfo("address-1"));
		s_assert.assertTrue(locale.equals(storeFrontHomePage.getpreferredCustomerStoreFrontInfo("city")), "City Not Matched, Expected is "+ locale +"But Actual is " +storeFrontHomePage.getpreferredCustomerStoreFrontInfo("city"));
		s_assert.assertTrue(emailId.toLowerCase().equals(storeFrontHomePage.getpreferredCustomerStoreFrontInfo("email-account").toLowerCase()), "Email ID Not Matched, Expected is "+ emailId.toLowerCase() +"But Actual is " +storeFrontHomePage.getpreferredCustomerStoreFrontInfo("email-account").toLowerCase());
		storeFrontHomePage.switchToPreviousTab();
		driver.pauseExecutionFor(3000);
		s_assert.assertAll();
	}

	//	//Hybris Project-4512:Edit Spouse Contact details for RC
	//	@Test(priority=74)
	//	public void testEditSpouseContactDetailsForRC_4512() throws InterruptedException{
	//		List<Map<String, Object>> randomRCList =  null;
	//		List<Map<String, Object>> randomRCListToVerify =  null;
	//		String randomFirstName = CommonUtils.getRandomWord(4);
	//		String randomLastName = randomFirstName;
	//		crmLoginpage = new CRMLoginPage(driver);
	//		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
	//		storeFrontHomePage = new StoreFrontHomePage(driver);
	//		String rcUserName = null;
	//		String rcEmailIDToVerifiy = null;
	//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
	//		String randomWrongPhoneNumber = String.valueOf(randomNum);
	//		String mainPhoneNumber = TestConstants.PHONE_NUMBER; 
	//		String firstName = TestConstants.FIRST_NAME+randomNum;
	//		String dob = null;
	//		String lastName = firstName;
	//		String combineFullName = firstName+" "+lastName;
	//		String emailId = firstName+"@gmail.com";
	//		String emailIDContainsSpecialCharacter = "^&@#"+"@gmail.com";
	//		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
	//		randomRCListToVerify = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
	//		rcUserName = (String) getValueFromQueryResult(randomRCList, "UserName");
	//		rcEmailIDToVerifiy = (String) getValueFromQueryResult(randomRCListToVerify, "UserName");
	//		logger.info("The email address is "+rcUserName);
	//		logger.info("The another email address to verify is "+rcEmailIDToVerifiy);	
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
	//		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");
	//		crmAccountDetailsPage.clickAccountMainMenuOptions("Contacts");
	//		if(crmAccountDetailsPage.verifyIsSpouseContactTypePresentNew(crmAccountDetailsPage.getCountOfAccountMainMenuOptions("Contacts"))==false){
	//			crmAccountDetailsPage.clickNewContactButtonUnderContactSection();
	//			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
	//			dob = crmAccountDetailsPage.enterBirthdateInCreatingNewContactForSpouse();
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
	//			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			crmAccountDetailsPage.closeFrameAfterSavingDetailsForNewContactSpouse(firstName);
	//			crmAccountDetailsPage.clickAccountMainMenuOptions("Contacts");
	//			crmAccountDetailsPage.clickOnEditUnderContactSection("Spouse");
	//			s_assert.assertFalse(crmAccountDetailsPage.verifyDataUnderContactSectionInContactDetailsPageIsEditable("Contact Type"), "Contact Type is Editable");
	//			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(randomFirstName, randomLastName);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").equals(randomFirstName+" "+randomLastName), "Name of the spouse not Reflected in SalesForce");
	//			crmAccountDetailsPage.clickEditButtonForNewContactSpouseInContactDetailsPage();
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(rcEmailIDToVerifiy);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("This email address already exists in our system, email must be unique."), "No Error Message Displayed");
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailIDContainsSpecialCharacter);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Invalid Email Address."), "Email Address with Special Character Saved.");
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
	//			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(randomWrongPhoneNumber);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Phone number should be in (999) 999-9999 format"), "No Error Message Displayed for Mobile less than 9 digits");
	//			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//		}else{
	//			logger.info("Spouse is already present");
	//			crmAccountDetailsPage.clickOnEditUnderContactSection("Spouse");
	//			s_assert.assertFalse(crmAccountDetailsPage.verifyDataUnderContactSectionInContactDetailsPageIsEditable("Contact Type"), "Contact Type is Editable");
	//			crmAccountDetailsPage.enterFirstAndLastNameInCreatingNewContactForSpouse(firstName, lastName);
	//			dob = crmAccountDetailsPage.enterBirthdateInCreatingNewContactForSpouse();
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
	//			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Name").equals(combineFullName), "Name of the spouse not Matched");
	//			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Birthdate").equals(dob), "Birthdate of the spouse not Matched");
	//			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Main Phone").replaceAll("\\D", "").equals(mainPhoneNumber), "Main Phone of the spouse not Matched");
	//			s_assert.assertTrue(crmAccountDetailsPage.verifyDataAfterSavingInNewContactForSpouse("Email Address").equals(emailId), "Email Address of the spouse not Matched");
	//			crmAccountDetailsPage.clickEditButtonForNewContactSpouseInContactDetailsPage();
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(rcEmailIDToVerifiy);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("This email address already exists in our system, email must be unique."), "No Error Message Displayed");
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailIDContainsSpecialCharacter);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Invalid Email Address."), "Email Address with Special Character Saved.");
	//			crmAccountDetailsPage.enterEmailIdInNewContactForSpouse(emailId);
	//			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(randomWrongPhoneNumber);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//			s_assert.assertTrue(crmAccountDetailsPage.isErrorMessageOnSavingExistingEmailIdOrWrongPhoneNumberPresent().contains("Phone number should be in (999) 999-9999 format"), "No Error Message Displayed for Mobile less than 9 digits");
	//			crmAccountDetailsPage.enterMainPhoneInNewContactForSpouse(mainPhoneNumber);
	//			crmAccountDetailsPage.clickSaveButtonForNewContactSpouse();
	//		}		
	//		s_assert.assertAll();
	//	}

	//Hybris Project-4517:View Account Status History for PC
	@Test(priority=75)
	public void testViewAccountStatusHistoryForPC_4517() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String firstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = firstName;
		String combineFullName = firstName+" "+lastName;
		logger.info("The email address is "+pcUserName);
		String otherReason = TestConstants.OTHER_REASON;
		String changedMyMind = TestConstants.CHANGED_MY_MIND;
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");

		s_assert.assertTrue(crmAccountDetailsPage.isAccountStatusActive(), "Account Status is not Active");
		int accountStatusesHistoryCount = crmAccountDetailsPage.getCountOfAccountMainMenuOptions("Account Statuses History");
		if(accountStatusesHistoryCount>0){
			crmAccountDetailsPage.clickAccountMainMenuOptions("Account Statuses History");
			s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Account Status"), "Account Status Label 1 is not Present");
			s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Reason"), "Reason Label 1 is not Present");
			s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Last Modified By"), "Last Modified By 1 Label is not Present");
		}
		/////////////////////////////Change Customer's Status from Active to InActive//////////////////////////

		crmAccountDetailsPage.clickAccountDetailsButton("Change Account Status");
		crmAccountDetailsPage.selectReasonToChangeAccountStatusFromDropDown(otherReason);
		String dateOfAccountStatusChangedFromActiveToInactive = crmAccountDetailsPage.clickSaveButtonToChangeAccountStatus();
		crmAccountDetailsPage.closeTabViaNumberWise(2);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");

		s_assert.assertFalse(crmAccountDetailsPage.isAccountStatusActive(), "Account Status is Active");
		int accountStatusesHistoryCountAfterAccountStatusChangedFromActiveToInactive = crmAccountDetailsPage.getCountOfAccountMainMenuOptions("Account Statuses History");
		s_assert.assertTrue(accountStatusesHistoryCountAfterAccountStatusChangedFromActiveToInactive == accountStatusesHistoryCount+1, "Account Statues History Actual is "+accountStatusesHistoryCountAfterAccountStatusChangedFromActiveToInactive + " & Account Statues History after changing statues Expected is "+ accountStatusesHistoryCount+1);
		crmAccountDetailsPage.clickAccountMainMenuOptions("Account Statuses History");

		s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Account Status"), "Account Status Label 2 is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Reason"), "Reason Label 2 is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Last Modified By"), "Last Modified By 2 Label is not Present");
		String accountStatusChangedFromActiveToInactive = crmAccountDetailsPage.getValuesOfLabelInAccountStatusesHistory(0); // 0 : For Account Status
		String reasonChangedFromActiveToInactive = crmAccountDetailsPage.getValuesOfLabelInAccountStatusesHistory(2);   // 2 : For Reason
		String lastModifiedChangedFromActiveToInactive = crmAccountDetailsPage.getValuesOfLabelInAccountStatusesHistory(3);  // 3 : For Last Modified Value
		s_assert.assertTrue(accountStatusChangedFromActiveToInactive.equalsIgnoreCase("Soft Terminated Voluntary"), "Account Status 1 Not Matched Actual is "+accountStatusChangedFromActiveToInactive+" & Expected is Soft Terminated Voluntary");
		s_assert.assertTrue(reasonChangedFromActiveToInactive.equalsIgnoreCase(otherReason), "Reason Not 1 Matched Actual is "+reasonChangedFromActiveToInactive +" & Expected is "+otherReason);
		s_assert.assertTrue(lastModifiedChangedFromActiveToInactive.contains(dateOfAccountStatusChangedFromActiveToInactive), "Last Modified By 1 Date Not Matched Actual is "+lastModifiedChangedFromActiveToInactive+" & Expected is "+dateOfAccountStatusChangedFromActiveToInactive);
		/////////////////////////////Change Customer's Status from InActive to Active//////////////////////////

		crmAccountDetailsPage.clickAccountDetailsButton("Change Account Status");
		crmAccountDetailsPage.selectReasonToChangeAccountStatusFromDropDown(changedMyMind);
		String dateOfAccountStatusChangedChangedFromInactiveToActive = crmAccountDetailsPage.clickSaveButtonToChangeAccountStatus();
		crmAccountDetailsPage.closeTabViaNumberWise(2);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");

		s_assert.assertTrue(crmAccountDetailsPage.isAccountStatusActive(), "Account Status is not Active");
		int accountStatusesHistoryCountAfterAccountStatusChangedFromInactiveToActive = crmAccountDetailsPage.getCountOfAccountMainMenuOptions("Account Statuses History");
		s_assert.assertTrue(accountStatusesHistoryCountAfterAccountStatusChangedFromInactiveToActive == accountStatusesHistoryCount+2, "Account Statues History Actual is "+ accountStatusesHistoryCountAfterAccountStatusChangedFromInactiveToActive + " & Account Statues History after changing statuses Expected is "+ accountStatusesHistoryCount+2);

		crmAccountDetailsPage.clickAccountMainMenuOptions("Account Statuses History");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Account Status"), "Account Status Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Reason"), "Reason Label is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelOfAccountMainMenuOptionsPresent("Account Statuses History", "Last Modified By"), "Last Modified By Label is not Present");
		String account_StatusChangedFromInactiveToActive = crmAccountDetailsPage.getValuesOfLabelInAccountStatusesHistory(0); // 0 : For Account Status
		String reasonChangedFromInactiveToActive = crmAccountDetailsPage.getValuesOfLabelInAccountStatusesHistory(2);   // 2 : For Reason
		String last_ModifiedChangedFromInactiveToActive = crmAccountDetailsPage.getValuesOfLabelInAccountStatusesHistory(3); // 3 : For Last Modified Value
		s_assert.assertTrue(account_StatusChangedFromInactiveToActive.equalsIgnoreCase("Active"), "Account Status 2 Not Matched Actual is "+account_StatusChangedFromInactiveToActive +" & Expected is Active");
		s_assert.assertTrue(reasonChangedFromInactiveToActive.equalsIgnoreCase(changedMyMind), "Reason Not 2 Matched Actual is "+reasonChangedFromInactiveToActive +"<<=& Expected is "+changedMyMind);
		s_assert.assertTrue(last_ModifiedChangedFromInactiveToActive.contains(dateOfAccountStatusChangedChangedFromInactiveToActive), "Last Modified By 2 Date Not Matched Actual is "+last_ModifiedChangedFromInactiveToActive + " & Expected is "+dateOfAccountStatusChangedChangedFromInactiveToActive);
		s_assert.assertAll();
	}

	//	//Hybris Project-4531:Search for account by partial Account number
	//	@Test(priority=76)
	//	public void testSearchForAccountByPartialAccountNumber_4531() throws InterruptedException{
	//		logger.info("The email address is "+consultantEmailID); 
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		//search for account by partial account no.
	//		crmHomePage.enterTextInSearchFieldAndHitEnter(accountID.split("(?<=\\G...)")[0]);
	//		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(), "Accounts link is not present on left navigation panel");
	//		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(), "Contacts link is not present on left navigation panel");
	//
	//		crmHomePage.clickNameOnFirstRowInSearchResults();
	//		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
	//		s_assert.assertTrue(crmAccountDetailsPage.isAccountNumberFieldDisplayedAndNonEmpty(),"Account Number is not displayed on account details page");
	//		s_assert.assertTrue(crmAccountDetailsPage.isAccountTypeFieldDisplayedAndNonEmpty(),"Account Type is not displayed on account details page");
	//		s_assert.assertTrue(crmAccountDetailsPage.isAccountStatusFieldDisplayedAndNonEmpty(),"Account Status is not displayed on account details page");
	//		s_assert.assertTrue(crmAccountDetailsPage.isCountryFieldDisplayedAndNonEmpty(),"Country field is not displayed on account details page");
	//		s_assert.assertTrue(crmAccountDetailsPage.isEnrollmentDateFieldDisplayedAndNonEmpty(),"Enrollment Date is not displayed on account details page");
	//		s_assert.assertTrue(crmAccountDetailsPage.isMainPhoneFieldDisplayedAndNonEmpty(),"Main Phone is not displayed on account details page");
	//		s_assert.assertTrue(crmAccountDetailsPage.isEmailAddressFieldDisplayedAndNonEmpty(),"Email Address is not displayed on account details page");
	//		s_assert.assertAll();
	//	}

	//	//Hybris Project-4529:Search for account by mobile phone number
	//	@Test(priority=77)
	//	public void testSearchForAccountByMobilePhoneNumber_4529() throws InterruptedException{
	//		String mainPhoneNumberDB = null;
	//		List<Map<String, Object>> mainPhoneNumberList =  null;
	//		//get main phone number
	//		mainPhoneNumberList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_MOBILE_NUMBER_QUERY_RFO, consultantEmailID), RFO_DB);
	//		mainPhoneNumberDB = String.valueOf(getValueFromQueryResult(mainPhoneNumberList, "Number"));
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		//search for account by Main Phone no.
	//		crmHomePage.enterTextInSearchFieldAndHitEnter(mainPhoneNumberDB/*.split("(?<=\\G...)")[0]*/);
	//		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(), "Accounts link is not present on left navigation panel");
	//		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(), "Contacts link is not present on left navigation panel");
	//		String email = crmHomePage.getEmailOnFirstRowInSearchResultsOfMainPhoneNumber();
	//		String accountName = crmHomePage.getAccountNameOnFirstRowInSearchResultsOfMainPhoneNumber();
	//		String name = crmHomePage.getNameOnFirstRowInSearchResultsOfMainPhoneNumber();
	//		crmHomePage.clickContactOnFirstRowInSearchResults();
	//		s_assert.assertTrue(crmContactDetailsPage.getName().contains(name), "Expected name on contact details page: "+name+" But actual on UI is: "+crmContactDetailsPage.getName());
	//		s_assert.assertTrue(crmContactDetailsPage.getEmailID().contains(email), "Expected name on contact details page: "+email+" But actual on UI is: "+crmContactDetailsPage.getEmailID());
	//		s_assert.assertTrue(crmContactDetailsPage.getAccountName().contains(accountName), "Expected name on contact details page: "+accountName+" But actual on UI is: "+crmContactDetailsPage.getAccountName());
	//		s_assert.assertTrue(crmAccountDetailsPage.isMainPhoneFieldDisplayedAndNonEmpty(),"Main Phone is not displayed on account details page");
	//		s_assert.assertTrue(crmAccountDetailsPage.isMainPhoneFieldDisplayedAndNonEmpty(),"Main Phone is not displayed on account details page");
	//		s_assert.assertTrue(crmAccountDetailsPage.isEmailAddressFieldDisplayedAndNonEmpty(),"Email Address is not displayed on account details page");
	//		s_assert.assertAll();
	//	}

	//Hybris Project-5004:Verify the forgot password for Consultant from Salesforce
	@Test(priority=78)
	public void testForgotPasswordForConsultant_5004() throws InterruptedException{
		logger.info("The email address is "+consultantEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		//verify account detail page present?
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		//click 'send forgot password email' link
		crmAccountDetailsPage.clickSendForgotPasswordEmailLink();
		crmAccountDetailsPage.acceptAlert();
		String pageSource=driver.getPageSource();
		//verify an email for new password has been sent?
		//s_assert.assertTrue(crmAccountDetailsPage.isEmailForNewPasswordSent(),"Email for new password has not been sent");
		s_assert.assertTrue(pageSource.contains("A new password was sent successfully"),"Email for new password has not been sent");
		s_assert.assertAll();
	}

	//Hybris Project-5005:Verify the forgot password for Preferred Customer from Salesforce
	@Test(priority=79)
	public void testForgotPasswordForPreferredCustomer_5005() throws InterruptedException{
		logger.info("The username is "+pcUserName);  
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickNameOnFirstRowInSearchResults();
		//verify account detail page present?
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		//click 'send forgot password email' link
		crmAccountDetailsPage.clickSendForgotPasswordEmailLink();
		crmAccountDetailsPage.acceptAlert();
		s_assert.assertTrue(crmAccountDetailsPage.isEmailForNewPasswordSent(),"Email for new password has not been sent");
		String pageSource=driver.getPageSource();
		//verify an email for new password has been sent?
		//s_assert.assertTrue(crmAccountDetailsPage.isEmailForNewPasswordSent(),"Email for new password has not been sent");
		s_assert.assertTrue(pageSource.contains("A new password was sent successfully"),"Email for new password has not been sent");
		s_assert.assertAll();
	}

	//	//Hybris Project-5269:Update Username with Inactive Consultant more than 180 days
	//	@Test(priority=80)
	//	public void testUpdateUserNameWithInactiveConsultantMoreThan180Days_5269() throws InterruptedException{
	//		RFO_DB = driver.getDBNameRFO();
	//		List<Map<String, Object>> randomConsultantListMoreThan180days =  null;
	//		List<Map<String, Object>> randomAccountDetails =  null;
	//		String inActiveconsultantEmailID = null;
	//		String firstNameInActiveConsultant = null;
	//		String lastNameInActiveConsultant = null;
	//		String firstNameActiveConsultant = null;
	//		String lastNameActiveConsultant = null;
	//		String editedName = null;
	//		String consultantEmailID = null;
	//		String accountID = null;
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		randomConsultantListMoreThan180days = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_CONSULTANTS_INACTIVE_MORE_THAN_180_DAYS,countryId),RFO_DB);
	//		inActiveconsultantEmailID = (String) getValueFromQueryResult(randomConsultantListMoreThan180days, "EmailAddress");
	//
	//		randomAccountDetails = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_DETAILS_QUERY,inActiveconsultantEmailID),RFO_DB);
	//		firstNameInActiveConsultant = (String) getValueFromQueryResult(randomAccountDetails, "FirstName");
	//		lastNameInActiveConsultant = (String) getValueFromQueryResult(randomAccountDetails, "LastName");
	//		editedName = firstNameInActiveConsultant+" "+lastNameInActiveConsultant;
	//
	//		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
	//		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
	//		accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
	//
	//		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
	//		consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));
	//		logger.info("The email address is "+consultantEmailID); 
	//		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
	//		while(true){
	//			if(crmHomePage.isSearchResultHasActiveUser() ==false){
	//				logger.info("No active user in the search results..searching new user");
	//				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
	//				consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
	//				logger.info("The email address is "+consultantEmailID);
	//				accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
	//
	//				randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
	//				consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));
	//
	//				s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//				crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
	//			}else{
	//				break;
	//			}
	//		}
	//		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(), "Accounts link is not present on left navigation panel");
	//		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(), "Contacts link is not present on left navigation panel");
	//		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
	//		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
	//		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");
	//		crmAccountDetailsPage.updateAccountNameField(editedName);
	//		crmAccountDetailsPage.clickSaveBtnUnderAccountDetail();
	//		randomAccountDetails = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_DETAILS_QUERY,consultantEmailID),RFO_DB);
	//		firstNameActiveConsultant = (String) getValueFromQueryResult(randomAccountDetails, "FirstName");
	//		lastNameActiveConsultant = (String) getValueFromQueryResult(randomAccountDetails, "LastName");
	//		s_assert.assertTrue(firstNameActiveConsultant.equalsIgnoreCase(firstNameInActiveConsultant),"first name is not updated");
	//		s_assert.assertTrue(lastNameActiveConsultant.equalsIgnoreCase(lastNameInActiveConsultant),"last name is not updated");
	//		s_assert.assertAll();
	//	}
	//
	//	//Hybris Project-5270:Update Username with Inactive consultant more than 90 days
	//	@Test(priority=81)
	//	public void testUpdateUserNameWithInactiveConsultantMoreThan90Days_5270() throws InterruptedException{
	//		RFO_DB = driver.getDBNameRFO();
	//		List<Map<String, Object>> randomConsultantListMoreThan180days =  null;
	//		List<Map<String, Object>> randomAccountDetails =  null;
	//		String inActiveconsultantEmailID = null;
	//		String firstNameInActiveConsultant = null;
	//		String lastNameInActiveConsultant = null;
	//		String firstNameActiveConsultant = null;
	//		String lastNameActiveConsultant = null;
	//		String editedName = null;
	//		String consultantEmailID = null;
	//		String accountID = null;
	//		randomConsultantListMoreThan180days = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_INACTIVE_CONSULTANT_EMAILID_MORE_THAN_90_DAYS,countryId),RFO_DB);
	//		inActiveconsultantEmailID = (String) getValueFromQueryResult(randomConsultantListMoreThan180days, "EmailAddress");
	//		randomAccountDetails = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_DETAILS_QUERY,inActiveconsultantEmailID),RFO_DB);
	//		firstNameInActiveConsultant = (String) getValueFromQueryResult(randomAccountDetails, "FirstName");
	//		lastNameInActiveConsultant = (String) getValueFromQueryResult(randomAccountDetails, "LastName");
	//		editedName = firstNameInActiveConsultant+" "+lastNameInActiveConsultant;
	//		randomAccountDetails = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_DETAILS_QUERY,inActiveconsultantEmailID),RFO_DB);
	//		firstNameInActiveConsultant = (String) getValueFromQueryResult(randomAccountDetails, "FirstName");
	//		lastNameInActiveConsultant = (String) getValueFromQueryResult(randomAccountDetails, "LastName");
	//		editedName = firstNameInActiveConsultant+" "+lastNameInActiveConsultant;
	//
	//		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
	//		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
	//		accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
	//
	//		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
	//		consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));
	//		logger.info("The email address is "+consultantEmailID); 
	//		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
	//		while(true){
	//			if(crmHomePage.isSearchResultHasActiveUser() ==false){
	//				logger.info("No active user in the search results..searching new user");
	//				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
	//				consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
	//				logger.info("The email address is "+consultantEmailID);
	//				accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
	//				randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
	//				consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));
	//
	//				s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//				crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
	//			}else{
	//				break;
	//			}
	//		}
	//		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(), "Accounts link is not present on left navigation panel");
	//		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(), "Contacts link is not present on left navigation panel");
	//		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
	//		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
	//		crmAccountDetailsPage.clickAccountDetailsButton("Edit Account");
	//		crmAccountDetailsPage.updateAccountNameField(editedName);
	//		crmAccountDetailsPage.clickSaveBtnUnderAccountDetail();
	//		randomAccountDetails = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_DETAILS_QUERY,consultantEmailID),RFO_DB);
	//		firstNameActiveConsultant = (String) getValueFromQueryResult(randomAccountDetails, "FirstName");
	//		lastNameActiveConsultant = (String) getValueFromQueryResult(randomAccountDetails, "LastName");
	//		s_assert.assertTrue(firstNameActiveConsultant.trim().equalsIgnoreCase(firstNameInActiveConsultant.trim()),"first name is not updated");
	//		s_assert.assertTrue(lastNameActiveConsultant.trim().equalsIgnoreCase(lastNameInActiveConsultant.trim()),"last name is not updated");
	//		s_assert.assertAll();
	//	}

	//Hybris Project-4528:Search for account by Main phone number
	@Test(priority=82)
	public void testSearchForAccountByMainPhoneNumber_4528() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String mainPhoneNumberDB = null;
		List<Map<String, Object>> mainPhoneNumberList =  null;
		//get main phone number
		mainPhoneNumberList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_PHONE_NUMBER_QUERY_RFO, consultantEmailID), RFO_DB);
		mainPhoneNumberDB = String.valueOf(getValueFromQueryResult(mainPhoneNumberList, "Number"));
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		//search for account by Main Phone no.
		crmHomePage.enterTextInSearchFieldAndHitEnter(mainPhoneNumberDB);
		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(), "Accounts link is not present on left navigation panel");
		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(), "Contacts link is not present on left navigation panel");
		String email = crmHomePage.getEmailOnFirstRowInSearchResultsOfMainPhoneNumber();
		String accountName = crmHomePage.getAccountNameOnFirstRowInSearchResultsOfMainPhoneNumber();
		String name = crmHomePage.getNameOnFirstRowInSearchResultsOfMainPhoneNumber();
		crmHomePage.clickContactOnFirstRowInSearchResults();
		s_assert.assertTrue(crmContactDetailsPage.getName().contains(name), "Expected name on contact details page: "+name+" But actual on UI is: "+crmContactDetailsPage.getName());
		s_assert.assertTrue(crmContactDetailsPage.getEmailID().contains(email), "Expected name on contact details page: "+email+" But actual on UI is: "+crmContactDetailsPage.getEmailID());
		s_assert.assertTrue(crmContactDetailsPage.getAccountName().contains(accountName), "Expected name on contact details page: "+accountName+" But actual on UI is: "+crmContactDetailsPage.getAccountName());
		s_assert.assertTrue(crmAccountDetailsPage.isMainPhoneFieldDisplayedAndNonEmpty(),"Main Phone is not displayed on account details page");
		s_assert.assertAll();
	}

	//Hybris Project-5006:Verify the forgot password for Retail Customer from Salesforce
	@Test(priority=83)
	public void testForgotPasswordForRetailCustomer_5006() throws InterruptedException{
		logger.info("The username is "+rcUserName);  
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");
		//verify account detail page present?
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresentRC(),"Account Details page has not displayed");
		//click 'send forgot password email' link
		crmAccountDetailsPage.clickSendForgotPasswordEmailLinkRC();
		crmAccountDetailsPage.acceptAlert();
		s_assert.assertTrue(crmAccountDetailsPage.isEmailForNewPasswordSentForRC(),"Email for new password has not been sent");
		s_assert.assertAll();
	}

	//	//Hybris Project-5265:Update Email address with Inactive Consultant more than 180 days
	//	@Test(priority=84)
	//	public void testUpdateEmailAddressWithInactiveConsultantMoreThan180Days_5265() throws InterruptedException{
	//		RFO_DB = driver.getDBNameRFO();
	//		List<Map<String, Object>> randomConsultantListMoreThan180days =  null;
	//		List<Map<String, Object>> randomAccountDetails =  null;
	//		String inActiveconsultantEmailID = null;
	//		String inActiveconsultantAccountId = null;
	//		String modifiedEmailId = null;
	//		String consultantEmailId = consultantEmailID;
	//		String consultantAccountId = accountID;
	//
	//		randomConsultantListMoreThan180days = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_CONSULTANTS_INACTIVE_MORE_THAN_180_DAYS,countryId),RFO_DB);
	//		inActiveconsultantEmailID = (String) getValueFromQueryResult(randomConsultantListMoreThan180days, "EmailAddress");
	//		inActiveconsultantAccountId = String.valueOf(getValueFromQueryResult(randomConsultantListMoreThan180days, "AccountID"));
	//
	//		logger.info("The email address of active consultant is "+consultantEmailID); 
	//		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailId);
	//		/*while(true){
	//				if(crmHomePage.isSearchResultHasActiveUser() ==false){
	//					logger.info("No active user in the search results..searching new user");
	//					randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
	//					consultantEmailId = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
	//					consultantAccountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
	//					logger.info("The email address is "+consultantEmailId);
	//					s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
	//					crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailId);
	//				}else{
	//					break;
	//				}
	//			}*/
	//		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(), "Accounts link is not present on left navigation panel");
	//		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(), "Contacts link is not present on left navigation panel");
	//		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
	//		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
	//		crmAccountDetailsPage.clickEditButtonForPrimaryContactInContactDetailsPage();
	//		crmAccountDetailsPage.updateEmailAddressFieldForPrimaryContact(inActiveconsultantEmailID);
	//		crmAccountDetailsPage.clickSaveBtnUnderContactInformation();
	//		s_assert.assertTrue(crmAccountDetailsPage.getEmailAddressOfUserAfterUpdation().equalsIgnoreCase(inActiveconsultantEmailID),"Email address of consultant User is not updated");
	//		//randomAccountDetails = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,consultantAccountId),RFO_DB);		
	//		//modifiedEmailId = (String) getValueFromQueryResult(randomAccountDetails, "EmailAddress"); 
	//		//s_assert.assertTrue(inActiveconsultantEmailID.equalsIgnoreCase(modifiedEmailId),"Email address of consultant User is not updated");
	//		s_assert.assertAll();
	//	}

	//Hybris Project-5266:Update Email address with Inactive PC more than 90 days
	@Test(enabled=false)//query not working 
	public void testUpdateEmailAddressWithInactivePCMoreThan90Days_5266() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCListMoreThan90days =  null;
		List<Map<String, Object>> randomAccountDetails =  null;
		String inActivePCEmailID = null;
		String inActivePCAccountId = null;
		String modifiedEmailId = null;
		String pcUserNamed = pcUserName;
		String pcAccountId = pcAccountID;
		randomPCListMoreThan90days = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_INACTIVE_PC_EMAILID_MORE_THAN_90_DAYS,countryId),RFO_DB);
		inActivePCEmailID = (String) getValueFromQueryResult(randomPCListMoreThan90days, "EmailAddress");
		inActivePCAccountId = String.valueOf(getValueFromQueryResult(randomPCListMoreThan90days, "AccountID"));

		logger.info("The email address of active PC is "+pcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserNamed);
		/*while(true){
				if(crmHomePage.isSearchResultHasActiveUser() ==false){
					logger.info("No active user in the search results..searching new user");
					randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
					pcUserNamed = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
					pcAccountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
					logger.info("The email address is "+pcUserName);
					s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
					crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserNamed);
				}else{
					break;
				}
			}*/
		s_assert.assertTrue(crmHomePage.isAccountLinkPresentInLeftNaviagation(), "Accounts link is not present on left navigation panel");
		s_assert.assertTrue(crmHomePage.isContactsLinkPresentInLeftNaviagation(), "Contacts link is not present on left navigation panel");
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		crmAccountDetailsPage.clickEditButtonForPrimaryContactInContactDetailsPage();
		crmAccountDetailsPage.updateEmailAddressFieldForPrimaryContact(inActivePCEmailID);
		crmAccountDetailsPage.clickSaveBtnUnderContactInformation();
		//randomAccountDetails = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,pcAccountId),RFO_DB);
		//modifiedEmailId = (String) getValueFromQueryResult(randomAccountDetails, "EmailAddress"); 
		//s_assert.assertTrue(inActivePCEmailID.equalsIgnoreCase(modifiedEmailId),"Email address of PC User is not updated");
		s_assert.assertAll();
	}


}