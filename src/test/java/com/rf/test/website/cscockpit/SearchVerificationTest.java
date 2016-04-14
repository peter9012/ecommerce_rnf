package com.rf.test.website.cscockpit;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.cscockpit.CSCockpitAutoshipSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitAutoshipTemplateTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCartTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCheckoutTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCustomerSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCustomerTabPage;
import com.rf.pages.website.cscockpit.CSCockpitLoginPage;
import com.rf.pages.website.cscockpit.CSCockpitOrderSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitOrderTabPage;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;


public class SearchVerificationTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(SearchVerificationTest.class.getName());

	//-------------------------------------------------Pages---------------------------------------------------------
	private CSCockpitLoginPage cscockpitLoginPage;	
	private CSCockpitAutoshipSearchTabPage cscockpitAutoshipSearchTabPage;
	private CSCockpitCheckoutTabPage cscockpitCheckoutTabPage;
	private CSCockpitCustomerSearchTabPage cscockpitCustomerSearchTabPage;
	private CSCockpitCustomerTabPage cscockpitCustomerTabPage;
	private CSCockpitOrderSearchTabPage cscockpitOrderSearchTabPage;
	private CSCockpitOrderTabPage cscockpitOrderTabPage;
	private CSCockpitCartTabPage cscockpitCartTabPage;
	private CSCockpitAutoshipTemplateTabPage cscockpitAutoshipTemplateTabPage;
	private StoreFrontHomePage storeFrontHomePage; 
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;	
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;

	//-----------------------------------------------------------------------------------------------------------------

	public SearchVerificationTest() {
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		cscockpitAutoshipSearchTabPage = new CSCockpitAutoshipSearchTabPage(driver);
		cscockpitCheckoutTabPage = new CSCockpitCheckoutTabPage(driver);
		cscockpitCustomerSearchTabPage = new CSCockpitCustomerSearchTabPage(driver);
		cscockpitCustomerTabPage = new CSCockpitCustomerTabPage(driver);
		cscockpitOrderSearchTabPage = new CSCockpitOrderSearchTabPage(driver);
		cscockpitOrderTabPage = new CSCockpitOrderTabPage(driver);
		cscockpitCartTabPage = new CSCockpitCartTabPage(driver);
		cscockpitAutoshipTemplateTabPage = new CSCockpitAutoshipTemplateTabPage(driver);	
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
		storeFrontOrdersPage = new StoreFrontOrdersPage(driver);
		storeFrontPCUserPage = new StoreFrontPCUserPage(driver);
		storeFrontRCUserPage = new StoreFrontRCUserPage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
	}

	private String RFO_DB = null;

	//Hybris Project-1927:Verify the Find Customer Page UI
	@Test
	public void testToVerifyTheFindCustomerPageUI_1927(){
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCustomerTypePresenceOnPage(),"customer type select DD not present on customerSearchPage");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCustomerCountryPresenceOnPage(),"customer country select DD not present on customerSearchPage");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyAccountStatusPresenceOnPage(),"customer account status DD not present on customerSearchPage");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCustomerNameFieldPresenceOnPage(),"customer name field not presenct on customerSearchPage");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyPostcodeFieldPresenceOnPage(),"postcode field not present on customerSearchPage");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyEmailAddressFieldPresenceOnPage(),"EmailAddress field not present on customerSearchPage");
		s_assert.assertAll();
	}

	//Hybris Project-1928:Verify the Find Customer Search Criteria
	@Test
	public void testVerifyFindCustomerSearchCriteria_1928() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		String accountID=null;
		String firstNameFromDatabase=null;
		String lastNameFromDatabase=null;
		String fullNameFromDatabase=null;
		String firstName = "First Name";
		String lastName = "Last Name";
		String emailAddress = "Email Address";
		RFO_DB = driver.getDBNameRFO();
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		//get valid cid from database.
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();

		String randomValidCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		String validCid=cscockpitCustomerSearchTabPage.getCIDNumberInCustomerSearchTab(randomValidCustomerSequenceNumber);
		consultantEmailID=cscockpitCustomerSearchTabPage.getEmailIdOfTheCustomerInCustomerSearchTab(randomValidCustomerSequenceNumber);

		//Get account Id from account number.
		List<Map<String, Object>>accountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_ID_FOR_PWS,validCid),RFO_DB);
		accountID = String.valueOf(getValueFromQueryResult(accountIdList, "AccountID"));

		//Get first name and last name from database.
		List<Map<String, Object>>userDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_USER_DETAILS_FROM_ACCOUNTID_RFO,accountID),RFO_DB);
		firstNameFromDatabase = (String) getValueFromQueryResult(userDetailsList, "FirstName");
		lastNameFromDatabase = (String) getValueFromQueryResult(userDetailsList, "LastName");
		fullNameFromDatabase=firstNameFromDatabase+" "+lastNameFromDatabase;

		//driver.get(driver.getCSCockpitURL());		

		//get invalid cid from database.
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		String invalidCid=cscockpitCustomerSearchTabPage.getCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownAsSelectInCustomerSearchTab("Select");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Select");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		//enter invalid cid and click search.
		cscockpitCustomerSearchTabPage.enterCIDInOrderSearchTab(TestConstants.INVALID_CUSTOMER_NAME);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.isNoResultDisplayed(),"CSCockpit Sponser CID search expected = No Results and on UI = Results are displayed");
		//Enter first name and click search
		cscockpitCustomerSearchTabPage.enterCIDInOrderSearchTab(firstNameFromDatabase);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid first name Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid first name Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid first name Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid first name Entered");

		//Enter last name and click search
		cscockpitCustomerSearchTabPage.enterCIDInOrderSearchTab(lastNameFromDatabase);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid last name Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid last name Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid last name Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid last name Entered");

		//Enter Full name and click search
		cscockpitCustomerSearchTabPage.enterCIDInOrderSearchTab(fullNameFromDatabase);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid full name Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid full name Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid full name Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid full name Entered");

		//Search cid as customer Name which in database Having account status pending or terminated click search.
		cscockpitCustomerSearchTabPage.enterCIDInOrderSearchTab(invalidCid);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for invalid CID Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for invalid CID Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for invalid CID Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for invalid CID Entered");

		//Search cid as customer CID which in not database and click search.
		cscockpitCustomerSearchTabPage.enterCIDInOrderSearchTab("000000000");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.isNoResultDisplayed(),"CSCockpit Sponser CID search expected = No Results and on UI = Results are displayed");

		//Search cid as customer CID which in database and click search.
		cscockpitCustomerSearchTabPage.enterCIDInOrderSearchTab(validCid);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid CID Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid CID Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid CID Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid CID Entered");

		//Clear CID field
		cscockpitCustomerSearchTabPage.clearCidFieldInOrderSearchTab();
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		//Enter invalid email address and click search.
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(TestConstants.INVALID_EMAIL_ADDRESS);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.isNoResultDisplayed(),"CSCockpit Sponser CID search expected = No Results and on UI = Results are displayed");

		//Enter valid email address and click search.
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult(), "count of result per page is greater than 20");

		//Clear Email address field
		cscockpitCustomerSearchTabPage.clearEmailAddressFieldInOrderSearchTab();
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		//Select one by one status from customer type dropdown and click search.
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult(), "count of result per page is greater than 20");

		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult(), "count of result per page is greater than 20");

		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult(), "count of result per page is greater than 20");

		//select customer type as Select.
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownAsSelectInCustomerSearchTab("Select");

		//Select one by one status from account status dropdown and click search.
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult(), "count of result per page is greater than 20");

		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult(), "count of result per page is greater than 20");

		//select account status as Select.
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Select");

		//select country one by one and assert values.
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(TestConstants.COUNTRY_DD_VALUE_US);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult(), "count of result per page is greater than 20");

		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(TestConstants.COUNTRY_DD_VALUE_CA);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult(), "count of result per page is greater than 20");
		s_assert.assertAll();
	}

}