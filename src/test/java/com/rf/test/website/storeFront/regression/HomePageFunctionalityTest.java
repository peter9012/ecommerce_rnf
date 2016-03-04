package com.rf.test.website.storeFront.regression;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.storeFront.StoreFrontAccountTerminationPage;
import com.rf.pages.website.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.pages.website.storeFront.StoreFrontShippingInfoPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class HomePageFunctionalityTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(HomePageFunctionalityTest.class.getName());
	public String emailID=null;
	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;
	private StoreFrontAccountTerminationPage storeFrontAccountTerminationPage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;
	private StoreFrontShippingInfoPage storeFrontShippingInfoPage;
	private String phoneNumber = null;
	private String country = null;
	private String RFO_DB = null;
	private String env = null;
	private String kitName = null;
	private String regimenName = null;
	private String enrollmentType = null;
	private String addressLine1 = null;
	private String city = null;
	private String postalCode = null;


	//Hybris Project-4350:Verify "Join my team" button on the .com and .biz site
	@Test 
	public void testJoinMyTeamButtonPresentOnTheComAndBizSite_4350(){
		country = driver.getCountry();
		env = driver.getEnvironment();  
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String PWS = storeFrontHomePage.openPWSSite(country, env);
		storeFrontHomePage.clickOnUserName();
		s_assert.assertTrue(storeFrontHomePage.verifyJoinMyTeamLinkPresent(), "Join My Team Link is not present on the Biz page");
		storeFrontHomePage.clickOnJoinMyTeamBtn();
		s_assert.assertTrue(driver.getCurrentUrl().contains(".biz/"+driver.getCountry()+"/consultant/enrollment/kitproduct"),"user is not on biz enrollment kit page after clicking 'Join My Team' btn");
		String comPWS = storeFrontHomePage.convertBizToComPWS(PWS);
		storeFrontHomePage.openConsultantPWS(comPWS);
		storeFrontHomePage.clickOnUserName();
		s_assert.assertFalse(storeFrontHomePage.verifyJoinMyTeamLinkPresent(), "Join My Team Link present on the Com page");
		s_assert.assertAll(); 
	}

	//Hybris Project-4332:Verify Meet Your Consultant Page from .biz site after clicking on Personalize My Profile link
	@Test 
	public void testMeetyourConsultantPageFromBizSiteAfterClickOnPersonalizeMyProfileLink_4332(){
		RFO_DB = driver.getDBNameRFO();  
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantWithPWSEmailID = null; //TestConstants.CONSULTANT_USERNAME;
		String consultantPWSURL = null; //TestConstants.CONSULTANT_BIZ_URL;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
			consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");
			consultantPWSURL = storeFrontHomePage.convertComSiteToBizSite(consultantPWSURL);
			storeFrontHomePage.openPWS(consultantPWSURL);
			if(driver.getCurrentUrl().contains("sitenotfound"))
				continue;
			else
				break;
		}
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		storeFrontHomePage.clickOnUserName();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		s_assert.assertTrue(storeFrontConsultantPage.getHomtownNamePresentOnAfterClickonPersinalizeLink().length()>0, "HomeTown text is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontConsultantPage.getConsultantSinceTextPresentAfterClickonPersinalizeLink().length()>0, "Consultant since text is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontConsultantPage.getFavoriteProductNameIsPresentAfterClickonPersinalizeLink().length()>0, "Favorite product text is not present on meet your consultant page");
		s_assert.assertAll(); 
	}

	//Hybris Project-4333:Verify Meet Your Consultant Page from .com site after clicking on Personalize My Profile link.
	@Test 
	public void testMeetyourConsultantPageFromComSiteAfterClickOnPersonalizeMyProfileLink_4333(){
		RFO_DB = driver.getDBNameRFO();  
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantWithPWSEmailID = null; //TestConstants.CONSULTANT_USERNAME;
		String consultantPWSURL = null; //TestConstants.CONSULTANT_COM_URL;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
			consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");
			consultantPWSURL = storeFrontHomePage.convertBizSiteToComSite(consultantPWSURL);
			storeFrontHomePage.openPWS(consultantPWSURL);
			if(driver.getCurrentUrl().contains("sitenotfound"))
				continue;
			else
				break;
		}
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		storeFrontHomePage.clickOnUserName();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		s_assert.assertTrue(storeFrontConsultantPage.getHomtownNamePresentOnAfterClickonPersinalizeLink().length()>0, "HomeTown text is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontConsultantPage.getConsultantSinceTextPresentAfterClickonPersinalizeLink().length()>0, "Consultant since text is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontConsultantPage.getFavoriteProductNameIsPresentAfterClickonPersinalizeLink().length()>0, "Favorite product text is not present on meet your consultant page");
		s_assert.assertAll();
	}

	//Hybris Project-3832:Verify ABOUT SECTION of Meet Your Consultant Page
	@Test 
	public void testAboutSectionOfMeetYourConsultantPage_3832(){
		RFO_DB = driver.getDBNameRFO();		
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantWithPWSEmailID = null; //TestConstants.CONSULTANT_USERNAME;
		String consultantPWSURL = null; //TestConstants.CONSULTANT_COM_URL;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomConsultantList =DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");

		// For .com site
		consultantPWSURL = storeFrontHomePage.convertBizSiteToComSite(consultantPWSURL);
		storeFrontHomePage.openPWS(consultantPWSURL);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		storeFrontHomePage.clickOnUserName();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		s_assert.assertTrue(storeFrontConsultantPage.getHomtownNamePresentOnAfterClickonPersinalizeLink().length()>0, "HomeTown text is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontConsultantPage.getConsultantSinceTextPresentAfterClickonPersinalizeLink().length()>0, "Consultant since text is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontConsultantPage.getFavoriteProductNameIsPresentAfterClickonPersinalizeLink().length()>0, "Favorite product text is not present on meet your consultant page");

		// For .biz site
		consultantPWSURL = storeFrontHomePage.convertComSiteToBizSite(consultantPWSURL);
		storeFrontHomePage.openPWS(consultantPWSURL);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		storeFrontHomePage.clickOnUserName();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		s_assert.assertTrue(storeFrontConsultantPage.getHomtownNamePresentOnAfterClickonPersinalizeLink().length()>0, "HomeTown text is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontConsultantPage.getConsultantSinceTextPresentAfterClickonPersinalizeLink().length()>0, "Consultant since text is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontConsultantPage.getFavoriteProductNameIsPresentAfterClickonPersinalizeLink().length()>0, "Favorite product text is not present on meet your consultant page");
		s_assert.assertAll();
	}

	//Hybris Project-3836:Verify 3 Content Block SECTION of Meet Your Consultant Page
	@Test 
	public void testContentBlockSectionOfMeetYourConsultantPage_3836(){
		RFO_DB = driver.getDBNameRFO();		
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantWithPWSEmailID = null; //TestConstants.CONSULTANT_USERNAME;
		String consultantPWSURL = null; //TestConstants.CONSULTANT_COM_URL;

		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomConsultantList =DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);

		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");
		//consultantPWSURL = storeFrontHomePage.convertBizToComPWS(consultantPWSURL);

		// for .com site
		consultantPWSURL = storeFrontHomePage.convertBizSiteToComSite(consultantPWSURL);
		storeFrontHomePage.openPWS(consultantPWSURL);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		storeFrontHomePage.clickOnUserName();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		s_assert.assertTrue(storeFrontConsultantPage.verifyBlockOfWhyIJoinedOnMeetYourConsultantPage(), "Why I joined Rodan + Fields block is not Present on meet your consultant page ");
		s_assert.assertTrue(storeFrontConsultantPage.verifyBlockOfProductsOnMeetYourConsultantPage(), "What I love most about R+F products block is not Present on meet your consultant page ");
		s_assert.assertTrue(storeFrontConsultantPage.verifyBlockOfBusinessOnMeetYourConsultantPage(), "What I love most about my R+F Business block is not Present on meet your consultant page ");

		// For .biz site
		consultantPWSURL = storeFrontHomePage.convertComSiteToBizSite(consultantPWSURL);
		storeFrontHomePage.openPWS(consultantPWSURL);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		storeFrontHomePage.clickOnUserName();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		s_assert.assertTrue(storeFrontConsultantPage.verifyBlockOfWhyIJoinedOnMeetYourConsultantPage(), "Why I joined Rodan + Fields block is not Present on meet your consultant page ");
		s_assert.assertTrue(storeFrontConsultantPage.verifyBlockOfProductsOnMeetYourConsultantPage(), "What I love most about R+F products block is not Present on meet your consultant page ");
		s_assert.assertTrue(storeFrontConsultantPage.verifyBlockOfBusinessOnMeetYourConsultantPage(), "What I love most about my R+F Business block is not Present on meet your consultant page ");
		s_assert.assertAll();
	}

	// Hybris Project-3844:Verify links in Meet Your consultant Banner
	@Test 
	public void testVerifyLinksInMeetYourConsultantBanner_3844() {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement
					(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		//click meet your consultant banner link
		storeFrontConsultantPage.clickOnMeetYourConsultantLink();
		//validate we are navigated to "Meet your Consultant" page on .COM
		s_assert.assertTrue(storeFrontConsultantPage.validateMeetYourConsultantPage(),"Meet your consultant page is not displayed");
		s_assert.assertAll();
	}

	//Hybris Project-3847:Verify Footer Links on .COM home Page
	@Test 
	public void testVerifyFooterLinksOnHomePage_3847()	 {
		//Navgate to app home page
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//validate RF Connection link
		s_assert.assertTrue(storeFrontHomePage.validateRFConnectionLink(),"RF Connection link is not redirecting to proper screen");
		//validate Carrers link
		s_assert.assertTrue(storeFrontHomePage.validateCareersLink(),"Careers link is not redirecting to proper screen");
		//validtae Contact-Us Link
		s_assert.assertTrue(storeFrontHomePage.validateContactUsLink(),"Contact Us link is not redirecting to proper screen");
		//validate Disclaimer link
		s_assert.assertTrue(storeFrontHomePage.validateDisclaimerLink(),"Disclaimer link is not redirecting to proper screen");
		//validate Privacy-policy link
		s_assert.assertTrue(storeFrontHomePage.validatePrivacyPolicyLink(),"PrivacyPolicy link is not redirecting to proper screen");
		//validate Satisfaction-Guarantee link
		s_assert.assertTrue(storeFrontHomePage.validateSatisfactionGuaranteeLink(),"SatisfactionGuarantee link is not redirecting to proper screen");
		//validate Terms&Conditions link
		s_assert.assertTrue(storeFrontHomePage.validateTermsConditionsLink(),"TermsConditions link is not redirecting to proper screen");
		//validate pressroom link
		s_assert.assertTrue(storeFrontHomePage.validatePressRoomLink(),"PressRoom link is not redirecting to proper screen");
		//validate country flag dropdown
		s_assert.assertTrue(storeFrontHomePage.validateCountryFlagDropDownBtn(),"Country flag Drop down button is not present in UI on homepage");
		//validate Derm RF Link
		s_assert.assertTrue(storeFrontHomePage.validateDermRFLink()," Derm RF link is not redirecting to proper screen");
		s_assert.assertAll();
	}

	//Hybris Project-3845:Verify the Links in the top Navigation on .COM home Page
	@Test 
	public void testVerifyLinksInTheTopNavigationOnHomePage_3845()	 {
		//Navgate to app home page
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//click on logo and validate the homepage
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.validateHomePage(),"Home page is not displayed ");
		//click login  link and validate username,password,login button & forgot pwd link
		s_assert.assertTrue(storeFrontHomePage.validateLoginFunctionality(),"All the Elements of login functionality are not displayed!");
		//validate 'shop','About' &'Become a Consultant' Menu 
		s_assert.assertTrue(storeFrontHomePage.validateTopNavigationMenuElements(),"Top Menu Navigation Elements are not displayed/present");
		//validate country flag dropdown
		s_assert.assertTrue(storeFrontHomePage.validateCountryFlagDropDownBtn(),"Country flag Drop down button is not present in UI on homepage");
		s_assert.assertAll();
	}

	//Hybris Project-3823:Verify Top Nav as Logged in Consultant(.com)
	@Test 
	public void testVerifyTopNavLoggedInConsultant_3823() throws InterruptedException	 {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		//validate(after logging as consultant) on homapage CRP cart is displayed
		s_assert.assertTrue(storeFrontConsultantPage.validateCRPCartDisplayed(), "CRP Cart is not displayed");
		//add product to cart..
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks(); 
		storeFrontHomePage.selectProductAndProceedToBuy();
		//validate CRP Cart is displayed?
		s_assert.assertTrue(storeFrontConsultantPage.validateCRPCartDisplayed(), "CRP Cart is not displayed");
		//validate adhoc cart is displayed?
		s_assert.assertTrue(storeFrontConsultantPage.validateAdhocCartIsDisplayed(), "Ad-hoc Cart is not displayed");
		//validate 'My Act' dropdown in the top Nav is present
		s_assert.assertTrue(storeFrontConsultantPage.validateMyAccountDDPresentInTopNav(), "My act DD is not displayed in the top Nav");
		s_assert.assertAll(); 
	}

	//Hybris Project-3841:Verify Top Nav as Logged in Consultant(.biz)
	@Test 
	public void testVerifyTopNavLoggedInConsultantbizSite_3841() throws InterruptedException{
		country = driver.getCountry();
		env = driver.getEnvironment(); 
		storeFrontHomePage = new StoreFrontHomePage(driver);
		RFO_DB = driver.getDBNameRFO();
		String PWS = storeFrontHomePage.getBizPWS(country, env);
		PWS = storeFrontHomePage.convertComSiteToBizSite(PWS);
		storeFrontHomePage.openPWS(PWS);
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountId = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement

					(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		//validate(after logging as consultant) on homapage CRP cart is displayed
		s_assert.assertTrue(storeFrontConsultantPage.validateCRPCartDisplayed(), "CRP Cart is not displayed");
		//add product to cart..
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks(); 
		storeFrontHomePage.selectProductAndProceedToBuy();
		//validate CRP Cart is displayed?
		s_assert.assertTrue(storeFrontConsultantPage.validateCRPCartDisplayed(), "CRP Cart is not displayed");
		//validate adhoc cart is displayed?
		s_assert.assertTrue(storeFrontConsultantPage.validateAdhocCartIsDisplayed(), "Ad-hoc Cart is not displayed");
		//validate 'My Act' dropdown in the top Nav is present
		s_assert.assertTrue(storeFrontConsultantPage.validateMyAccountDDPresentInTopNav(), "My act DD is not displayed in the top Nav");
		s_assert.assertAll(); 
	}

	//Hybris Project-3824:Verify Top Nav as Logged in PC User(.com)
	@Test 
	public void testVerifyTopNavLoggedInPCUser_3824() throws InterruptedException{
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement

					(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		//validate(after logging as PC) on homapage PC Perks cart is displayed
		s_assert.assertTrue(storeFrontPCUserPage.validatePCPerkCartDisplayed(), "CRP Cart is not displayed");
		//add product to cart..
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks(); 
		storeFrontHomePage.selectProductAndProceedToBuy();
		//validate PC Perks Cart is displayed?
		s_assert.assertTrue(storeFrontPCUserPage.validatePCPerkCartDisplayed(), "CRP Cart is not displayed");
		//validate adhoc cart is displayed?
		s_assert.assertTrue(storeFrontPCUserPage.validateAdhocCartIsDisplayed(), "Ad-hoc Cart is not displayed");
		//validate 'My Act' dropdown in the top Nav is present
		s_assert.assertTrue(storeFrontPCUserPage.validateMyAccountDDPresentInTopNav(), "My act DD is not displayed in the top Nav");
		s_assert.assertAll(); 
	}

	//Hybris Project-3842:Verify Top Nav as Logged in PC User(.biz)
	@Test 
	public void testVerifyTopNavLoggedInPCUserbizSite_3842() throws InterruptedException	{
		country = driver.getCountry();
		env = driver.getEnvironment(); 
		storeFrontHomePage = new StoreFrontHomePage(driver);
		RFO_DB = driver.getDBNameRFO();
		String PWS = storeFrontHomePage.getBizPWS(country, env);
		PWS = storeFrontHomePage.convertComSiteToBizSite(PWS);
		storeFrontHomePage.openPWS(PWS);
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement

					(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		//validate(after logging as PC) on homapage PC Perks cart is displayed
		s_assert.assertTrue(storeFrontPCUserPage.validatePCPerkCartDisplayed(), "CRP Cart is not displayed");
		//add product to cart..
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks(); 
		storeFrontHomePage.selectProductAndProceedToBuy();
		//validate PC Perks Cart is displayed?
		s_assert.assertTrue(storeFrontPCUserPage.validatePCPerkCartDisplayed(), "CRP Cart is not displayed");
		//validate adhoc cart is displayed?
		s_assert.assertTrue(storeFrontPCUserPage.validateAdhocCartIsDisplayed(), "Ad-hoc Cart is not displayed");
		//validate 'My Act' dropdown in the top Nav is present
		s_assert.assertTrue(storeFrontPCUserPage.validateMyAccountDDPresentInTopNav(), "My act DD is not displayed in the top Nav");
		s_assert.assertAll(); 
	}

	//Hybris Project-3840:Verify Top Nav as Logged in RC User(.biz)
	@Test 
	public void testVerifyTopNavLoggedInRCUserbizSite_3840() throws InterruptedException	{
		country = driver.getCountry();
		env = driver.getEnvironment(); 
		storeFrontHomePage = new StoreFrontHomePage(driver);
		RFO_DB = driver.getDBNameRFO();
		String PWS = storeFrontHomePage.getBizPWS(country, env);
		PWS = storeFrontHomePage.convertComSiteToBizSite(PWS);
		storeFrontHomePage.openPWS(PWS);
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserEmailID = null;
		String accountIdForRCUser = null;
		while(true){
			randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement
					(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");  
			accountIdForRCUser = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForRCUser);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		//add product to cart..
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks(); 
		storeFrontHomePage.selectProductAndProceedToBuy();
		//validate adhoc cart is displayed?
		s_assert.assertTrue(storeFrontRCUserPage.validateAdhocCartIsDisplayed(), "Ad-hoc Cart is not displayed");
		//validate 'My Act' dropdown in the top Nav is present
		s_assert.assertTrue(storeFrontRCUserPage.validateMyAccountDDPresentInTopNav(), "My act DD is not displayed in the top Nav");
		s_assert.assertAll(); 
	}

	//Hybris Project-3819 Verify links in meet your consultant banner
	@Test 
	public void testVerifyLinksInMeetYourConsultantBanner_3819() {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		//click meet your consultant banner link
		storeFrontConsultantPage.clickOnMeetYourConsultantLink();
		//validate we are navigated to "Meet your Consultant" page on .COM
		s_assert.assertTrue(storeFrontConsultantPage.validateMeetYourConsultantPage(),"Meet your consultant page is not displayed");
		s_assert.assertAll();
	}

	//Hybris Project-3825:Verify Top Nav as Logged in RC User(.com)
	@Test 
	public void testVerifyTopNavLoggedInRCUser_3825() throws InterruptedException{
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserEmailID = null;
		String accountIdForRCUser = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");  
			accountIdForRCUser = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForRCUser);
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		//validate home page
		s_assert.assertTrue(storeFrontRCUserPage.validateHomePage(),"home -page is not displayed");
		//add product to cart..
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks(); 
		storeFrontHomePage.selectProductAndProceedToBuy();
		//validate adhoc cart is displayed?
		s_assert.assertTrue(storeFrontRCUserPage.validateAdhocCartIsDisplayed(), "Ad-hoc Cart is not displayed");
		//validate 'My Act' dropdown in the top Nav is present
		s_assert.assertTrue(storeFrontRCUserPage.validateMyAccountDDPresentInTopNav(), "My act DD is not displayed in the top Nav");
		s_assert.assertAll(); 
	}

	//Hybris Project-3820:Verify the Links in the top Navigation on .COM home Page
	@Test 
	public void testVerifyLinksInTheTopNavigationOnHomePage_3820()	{
		//Navgate to app home page
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//click on logo and validate the homepage
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.validateHomePage(),"Home page is not displayed ");
		//click login  link and validate username,password,login button & forgot pwd link
		s_assert.assertTrue(storeFrontHomePage.validateLoginFunctionality(),"All the Elements of login functionality are not displayed!");
		//validate 'shop','About' &'Become a Consultant' Menu 
		s_assert.assertTrue(storeFrontHomePage.validateTopNavigationMenuElements(),"Top Menu Navigation Elements are not displayed/present");
		//validate country flag dropdown
		s_assert.assertTrue(storeFrontHomePage.validateCountryFlagDropDownBtn(),"Country flag Drop down button is not present in UI on homepage");
		s_assert.assertAll();
	}	

	//Hybris Project-3822:Verify Footer Links on .COM home Page
	@Test 
	public void testVerifyFooterLinksOnHomePage_3822(){
		//Navgate to app home page
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//validate RF Connection link
		s_assert.assertTrue(storeFrontHomePage.validateRFConnectionLink(),"RF Connection link is not redirecting to proper screen");
		//validate Carrers link
		s_assert.assertTrue(storeFrontHomePage.validateCareersLink(),"Careers link is not redirecting to proper screen");
		//validtae Contact-Us Link
		s_assert.assertTrue(storeFrontHomePage.validateContactUsLink(),"Contact Us link is not redirecting to proper screen");
		//validate Disclaimer link
		s_assert.assertTrue(storeFrontHomePage.validateDisclaimerLink(),"Disclaimer link is not redirecting to proper screen");
		//validate Privacy-policy link
		s_assert.assertTrue(storeFrontHomePage.validatePrivacyPolicyLink(),"PrivacyPolicy link is not redirecting to proper screen");
		//validate Satisfaction-Guarantee link
		s_assert.assertTrue(storeFrontHomePage.validateSatisfactionGuaranteeLink(),"SatisfactionGuarantee link is not redirecting to proper screen");
		//validate Terms&Conditions link
		s_assert.assertTrue(storeFrontHomePage.validateTermsConditionsLink(),"TermsConditions link is not redirecting to proper screen");
		//validate pressroom link
		s_assert.assertTrue(storeFrontHomePage.validatePressRoomLink(),"PressRoom link is not redirecting to proper screen");
		//validate country flag dropdown
		s_assert.assertTrue(storeFrontHomePage.validateCountryFlagDropDownBtn(),"Country flag Drop down button is not present in UI on homepage");
		//validate Derm RF Link
		s_assert.assertTrue(storeFrontHomePage.validateDermRFLink()," Derm RF link is not redirecting to proper screen");
		s_assert.assertAll();
	}

	//Hybris Project-4677:Verify that Country cannot be modified
	@Test 
	public void testCountryCannotBeModified_4677() throws InterruptedException  {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontAccountInfoPage=new StoreFrontAccountInfoPage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		//goto account info page..
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage=storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		//validate country can't be modified
		s_assert.assertTrue(storeFrontAccountInfoPage.validateCountryCanOrNotBeModified(),"country can be modified on account info page");
		s_assert.assertAll();	
	}

	// Hybris Project-4031:from .com  Login as a existing RC and access Solution Tool
	@Test 
	public void testLoginAsExstingRCAndAccessSolutionTool_4031(){
		RFO_DB = driver.getDBNameRFO();
		country = driver.getCountry();
		env = driver.getEnvironment();  
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontRCUserPage=new StoreFrontRCUserPage(driver);
		String PWS = storeFrontHomePage.getBizPWS(country, env);
		PWS = storeFrontHomePage.convertBizSiteToComSite(PWS);
		storeFrontHomePage.openPWS(PWS);
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserEmailID = null;
		String accountIdForRCUser = null;
		while(true){
			randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");  
			accountIdForRCUser = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForRCUser);
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		//Access Solution Tool..
		s_assert.assertTrue(storeFrontRCUserPage.validateAccessSolutionTool(),"Solution tool is not giving the expected results");
		s_assert.assertAll();
	}

	//Hybris Project-1895:To verify the Meet the consultant banner on PWS sites
	@Test 
	public void testValidateMeetConsultantBannerPWSSite_1895()  {
		country = driver.getCountry();
		env = driver.getEnvironment();  
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage=new StoreFrontConsultantPage(driver);
		storeFrontHomePage.openPWSSite(country, env);

		//click meet your consultant banner link
		storeFrontConsultantPage.clickOnMeetYourConsultantLink();
		//validate we are navigated to "Meet your Consultant" page on .COM
		s_assert.assertTrue(storeFrontConsultantPage.validateMeetYourConsultantPage(),"Meet your consultant page is not displayed");
		//validate 'edit your information' link shouldn't be present as user doesn't logged in..
		s_assert.assertFalse(storeFrontConsultantPage.validateEditYourInformationLink(), "edit your Information link is present in UI");
		s_assert.assertFalse(storeFrontHomePage.verifyConsultantSinceOnMeetYourConsultantPage(), "Consultant since is not present");
		s_assert.assertAll();
	}

	//Hybris Project-1904:To Verify the cancel functionality on edit meet the consultant page from com site
	@Test 
	public void testCancelFunctionalityOnEditMeetYourConsultantPageComSite_1904()  {
		RFO_DB = driver.getDBNameRFO();
		country = driver.getCountry();
		env = driver.getEnvironment();  
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String consultantEmailID = null;
		while(true){
			List<Map<String, Object>> sponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
			consultantEmailID = String.valueOf(getValueFromQueryResult(sponserList, "Username"));
			String PWS = String.valueOf(getValueFromQueryResult(sponserList, "URL"));
			String comPWS = storeFrontHomePage.convertBizSiteToComSite(PWS);
			storeFrontHomePage.openPWS(comPWS);

			//Login with same PWS consultant
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		//click meet your consultant banner link
		storeFrontConsultantPage.clickOnMeetYourConsultantLink();
		//validate we are navigated to "Meet your Consultant" page
		s_assert.assertTrue(storeFrontConsultantPage.validateMeetYourConsultantPage(),"Meet your consultant page is not displayed");
		//click on 'Personalize My  Profile' link..
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		//click on cancel button on 'editConsultantInfo' page
		storeFrontHomePage.clickCancelBtnOnEditConsultantInfoPage();
		//validate we are navigated to "Meet your Consultant" page
		s_assert.assertTrue(storeFrontConsultantPage.validateMeetYourConsultantPage(),"Meet your consultant page is not displayed");
		s_assert.assertAll();
	}

	//Hybris Project-1905:To Verify the Submission Guidelines link on edit meet the consultant page from biz site
	@Test 
	public void testSubmissionGuidelinesLinkOnEditMeetTheConsultantPageBizSite_1905()  {
		RFO_DB = driver.getDBNameRFO();
		country = driver.getCountry();
		env = driver.getEnvironment();  
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String consultantEmailID = null;
		while(true){
			List<Map<String, Object>> sponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
			consultantEmailID = String.valueOf(getValueFromQueryResult(sponserList, "Username"));
			String PWS = String.valueOf(getValueFromQueryResult(sponserList, "URL"));
			String comPWS = storeFrontHomePage.convertComSiteToBizSite(PWS);
			storeFrontHomePage.openPWS(comPWS);

			//Login with same PWS consultant
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		//click meet your consultant banner link
		storeFrontConsultantPage.clickOnMeetYourConsultantLink();
		//validate we are navigated to "Meet your Consultant" page
		s_assert.assertTrue(storeFrontConsultantPage.validateMeetYourConsultantPage(),"Meet your consultant page is not displayed");
		//click on 'Personalize My  Profile' link..
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		// Click on Submission Guidelines link in the MeetYourConsultant edit information page & validate SG Pdf is open..
		s_assert.assertTrue(storeFrontHomePage.validateSubmissionGuideLinesLink(),"Submission Guoidelines link is not giving the expected results");
		s_assert.assertAll();
	}

	// Hybris Project-1920:To verify the contact us functionality in edit meet the consultant page for com PWS site
	@Test 
	public void testContactUsFunctionalityInEditMeetConsultantPagecomPWSSite_1920(){
		RFO_DB = driver.getDBNameRFO();
		country = driver.getCountry();
		env = driver.getEnvironment(); 
		String comPWS = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String consultantEmailID = null;
		while(true){
			List<Map<String, Object>> sponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
			consultantEmailID = String.valueOf(getValueFromQueryResult(sponserList, "Username"));
			String PWS = String.valueOf(getValueFromQueryResult(sponserList, "URL"));
			comPWS = storeFrontHomePage.convertBizSiteToComSite(PWS);
			storeFrontHomePage.openPWS(comPWS);

			//Login with same PWS consultant
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		//click meet your consultant banner link
		storeFrontConsultantPage.clickOnMeetYourConsultantLink();
		//validate we are navigated to "Meet your Consultant" page
		s_assert.assertTrue(storeFrontConsultantPage.validateMeetYourConsultantPage(),"Meet your consultant page is not displayed");
		//click on 'Personalize My  Profile' link..
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		//select the checkbox next to the email field and click save..
		storeFrontHomePage.checkEmailFieldCBOnEditConsultantInfoPage();
		storeFrontHomePage.clickOnSaveAfterEditPWS();
		//validate we are navigated to "Meet your Consultant" page
		s_assert.assertTrue(storeFrontConsultantPage.validateMeetYourConsultantPage(),"Meet your consultant page is not displayed");

		logout();

		storeFrontHomePage.openPWS(comPWS);

		//Login with same PWS consultant
		storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnMeetYourConsultantLink();
		s_assert.assertFalse(storeFrontHomePage.verifyEmailIdIsPresentInContactBox(), "Email Address is not Present in contact box After Edit");
		s_assert.assertTrue(storeFrontHomePage.verifyEnterYourNameFunctionalityIsPresentOnMeetMyConsultantPage(), "Enter your name box is not present on com site");
		s_assert.assertTrue(storeFrontHomePage.verifyEnterYourEmailFunctionalityIsPresentOnMeetMyConsultantPage(), "Enter your email box is not present on com site");
		s_assert.assertTrue(storeFrontHomePage.verifyEnterYourMessageFunctionalityIsPresentOnMeetMyConsultantPage(), "Enter your Message box is not present on com site");
		s_assert.assertTrue(storeFrontHomePage.verifySubmitButtonIsPresentOnMeetMyConsultantPage(), "Send button is not present on com site");

		logout();

		String bizPWS = storeFrontHomePage.convertComSiteToBizSite(comPWS);
		storeFrontHomePage.openPWS(bizPWS);

		//Login with same PWS consultant
		storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnMeetYourConsultantLink();
		s_assert.assertFalse(storeFrontHomePage.verifyEmailIdIsPresentInContactBox(), "Email Address is not Present in contact box After Edit");
		s_assert.assertTrue(storeFrontHomePage.verifyEnterYourNameFunctionalityIsPresentOnMeetMyConsultantPage(), "Enter your name box is not present on com site");
		s_assert.assertTrue(storeFrontHomePage.verifyEnterYourEmailFunctionalityIsPresentOnMeetMyConsultantPage(), "Enter your email box is not present on com site");
		s_assert.assertTrue(storeFrontHomePage.verifyEnterYourMessageFunctionalityIsPresentOnMeetMyConsultantPage(), "Enter your Message box is not present on com site");
		s_assert.assertTrue(storeFrontHomePage.verifySubmitButtonIsPresentOnMeetMyConsultantPage(), "Send button is not present on com site");

		s_assert.assertAll();
	}

	//Hybris Project-1898:To verify the contact your sponsor section in getting started page
	@Test 
	public void testContactYourSponsorSectionInGettingStartedPage_1898() throws InterruptedException{
		storeFrontHomePage = new StoreFrontHomePage(driver);
		RFO_DB = driver.getDBNameRFO();
		phoneNumber = TestConstants.PHONE_NUMBER_CA;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String bizPWS = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String firstName=TestConstants.FIRST_NAME+randomNum;
		while(true){
			List<Map<String, Object>> sponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
			String consultantEmailID = String.valueOf(getValueFromQueryResult(sponserList, "Username"));
			String PWS = String.valueOf(getValueFromQueryResult(sponserList, "URL"));
			bizPWS = storeFrontHomePage.convertComSiteToBizSite(PWS);
			storeFrontHomePage.openPWS(bizPWS);
			storeFrontHomePage.clickOnSponsorName();
			s_assert.assertTrue(storeFrontHomePage.verifyJoinMyTeamLinkPresent(), "Join My Team Link is not present on the Com page");
			s_assert.assertTrue(storeFrontHomePage.verifyContactBoxIsPresent(), "Contact Box is not Present");

			//Login with same PWS consultant
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		storeFrontHomePage.clickOnWelcomeDropDown();
		storeFrontHomePage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontHomePage.clickOnYourAccountDropdown();
		storeFrontHomePage.clickOnEditMyPWS();
		storeFrontHomePage.enterPhoneNumberOnEditPWS(phoneNumber);
		s_assert.assertTrue(storeFrontHomePage.verifyConsultantSinceOnMeetYourConsultantPage(), "Email Address is not Present in contact box After Edit");
		String emailAddress = firstName+randomNum+"@xyz.com"; 
		storeFrontHomePage.updateEmailOnMeetYourConsultantPage(emailAddress);
		storeFrontHomePage.clickOnSaveAfterEditPWS();
		logout();
		storeFrontHomePage.openPWS(bizPWS);
		storeFrontHomePage.clickOnSponsorName();

		s_assert.assertTrue(storeFrontHomePage.verifyEmailIdIsPresentInContactBoxAfterUpdate(emailAddress), "Email Address is not Present in contact box After Edit");
		s_assert.assertTrue(storeFrontHomePage.verifyPhoneNumberIsPresentInContactBox(phoneNumber), "Phone number is not Present in contact box After Edit");
		s_assert.assertAll();

	}

	// Hybris Project-4030:from .com  Login as a existing PC and access Solution Tool
	@Test 
	public void testLoginAsExstingPCAndAccessSolutionTool_4030()  {
		RFO_DB = driver.getDBNameRFO();
		country = driver.getCountry();
		env = driver.getEnvironment();  
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontPCUserPage=new StoreFrontPCUserPage(driver);
		String bizPWS = storeFrontHomePage.getBizPWS(country, env);
		String comPWS = storeFrontHomePage.convertBizSiteToComSite(bizPWS);
		storeFrontHomePage.openPWS(comPWS);

		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);

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
		//Access Solution Tool..
		s_assert.assertTrue(storeFrontPCUserPage.validateAccessSolutionTool(),"Solution tool is not giving the expected results");
		s_assert.assertAll();
	}

	//Hybris Project-1897:To verify the Meet the consultant banner on solution tool page
	@Test 
	public void testMeetConsultantBannerOnSolutionToolPage_1897(){
		RFO_DB = driver.getDBNameRFO();  
		country = driver.getCountry();
		env = driver.getEnvironment();  
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String PWS = storeFrontHomePage.getBizPWS(country, env);
		PWS = storeFrontHomePage.convertBizSiteToComSite(PWS);
		storeFrontHomePage.openPWS(PWS);
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountId = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		//click learn more link
		storeFrontHomePage.clickLearnMoreLinkUnderSolutionToolAndSwitchControl();
		//validate consultant info on top right corner..
		s_assert.assertTrue(storeFrontHomePage.validateConsultantNameOnTopRightCorner(),"Consultant Info is not present on right top Corner");
		s_assert.assertAll();
	}

	// Hybris Project-4028:Access Solution tool from .COM Site Category pages Left Menu
	@Test 
	public void testAccessSolutionToolcomSiteCategoryPagesLeftMenu_4028()	 {
		country = driver.getCountry();
		env = driver.getEnvironment();  
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String PWS = storeFrontHomePage.getBizPWS(country, env);
		PWS = storeFrontHomePage.convertBizSiteToComSite(PWS);
		storeFrontHomePage.openPWS(PWS);
		//Access Solution Tool..
		s_assert.assertTrue(storeFrontHomePage.validateAccessSolutionTool(),"Solution tool is not giving the expected results");
		s_assert.assertAll();
	}

	// Hybris Project-1892:To verify the Meet the consultant page UI for com and biz site
	@Test
	public void testVerifyMeetConsultantPage_1892() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);

		//Get Biz PWS from database to login. 
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
		String bizPWS=(String) getValueFromQueryResult(randomConsultantList, "URL"); 
		//Open Biz PWS.
		storeFrontHomePage.openConsultantPWS(bizPWS);
		while(true){
			if(driver.getCurrentUrl().contains("sitenotfound")){
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
				bizPWS=(String) getValueFromQueryResult(randomConsultantList, "URL");  
				storeFrontHomePage.openConsultantPWS(bizPWS); 
				continue;
			}else
				break;
		} 
		logger.info("biz pws to login is "+bizPWS);

		//Verify details on biz site.
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
			consultantEmailID= (String) getValueFromQueryResult(randomConsultantList, "Username"); 
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				storeFrontHomePage.openConsultantPWS(bizPWS);  
			}
			else
				break;
		}
		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnMeetYourConsultantLink();
		//validate we are navigated to "Meet your Consultant" page
		s_assert.assertTrue(storeFrontConsultantPage.validateMeetYourConsultantPage(),"Meet your consultant page is not displayed");
		//Validate Contact me section on meet your consultant page.
		s_assert.assertTrue(storeFrontHomePage.verifyContactBoxIsPresent(),"Contact me section is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyEnterYourNameFunctionalityIsPresentOnMeetMyConsultantPage(),"Enter your Name functionality is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontHomePage.verifyEnterYourEmailFunctionalityIsPresentOnMeetMyConsultantPage(),"Enter your email functionality is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontHomePage.verifyEnterYourMessageFunctionalityIsPresentOnMeetMyConsultantPage(),"Enter your Message functionality is not present on meet your consultant page");
		//Validate Follow me section on meet your consultant page.
		storeFrontHomePage.addFollowMeSection(TestConstants.FACEBOOK_LINK);
		s_assert.assertTrue(storeFrontHomePage.verifyFollowMeSectionIsPresent(),"Follow me section is not present");
		//Validate Footer section on meet your consultant page.
		s_assert.assertTrue(storeFrontHomePage.verifyPrivacyPolicyLink(),"Privacy Policy Link is not present");
		s_assert.assertTrue(storeFrontHomePage.verifySatisfactionGuaranteeLink(),"Satisfaction guarantee link is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyDisclaimerLink(),"Disclaimer link is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyContactUsLink(),"Contact us link is not present");
		logout();
		//Verify details on com site.
		//Get com PWS from database to login. 
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".com",driver.getCountry(),countryId),RFO_DB);
		String comPWS=(String) getValueFromQueryResult(randomConsultantList, "URL"); 
		//Open Biz PWS.
		storeFrontHomePage.openConsultantPWS(comPWS);
		while(true){
			if(driver.getCurrentUrl().contains("sitenotfound")){
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".com",driver.getCountry(),countryId),RFO_DB);
				comPWS=(String) getValueFromQueryResult(randomConsultantList, "URL");  
				storeFrontHomePage.openConsultantPWS(comPWS); 
				continue;
			}else
				break;
		} 
		logger.info("com pws to login is "+comPWS);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnMeetYourConsultantLink();
		//validate we are navigated to "Meet your Consultant" page
		s_assert.assertTrue(storeFrontConsultantPage.validateMeetYourConsultantPage(),"Meet your consultant page is not displayed");
		//Validate Contact me section on meet your consultant page.
		s_assert.assertTrue(storeFrontHomePage.verifyContactBoxIsPresent(),"Contact me section is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyEnterYourNameFunctionalityIsPresentOnMeetMyConsultantPage(),"Enter your Name functionality is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontHomePage.verifyEnterYourEmailFunctionalityIsPresentOnMeetMyConsultantPage(),"Enter your email functionality is not present on meet your consultant page");
		s_assert.assertTrue(storeFrontHomePage.verifyEnterYourMessageFunctionalityIsPresentOnMeetMyConsultantPage(),"Enter your Message functionality is not present on meet your consultant page");
		//Validate Follow me section on meet your consultant page.
		storeFrontHomePage.addFollowMeSection(TestConstants.FACEBOOK_LINK);
		s_assert.assertTrue(storeFrontHomePage.verifyFollowMeSectionIsPresent(),"Follow me section is not present");
		//Validate Footer section on meet your consultant page.
		s_assert.assertTrue(storeFrontHomePage.verifyPrivacyPolicyLink(),"Privacy Policy Link is not present");
		s_assert.assertTrue(storeFrontHomePage.verifySatisfactionGuaranteeLink(),"Satisfaction guarantee link is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyDisclaimerLink(),"Disclaimer link is not present");
		s_assert.assertTrue(storeFrontHomePage.verifyContactUsLink(),"Contact us link is not present");
		s_assert.assertAll();
	}

	// Hybris Project-1900:To verify the More About Me section for PWS Biz
	@Test
	public void testToVerifyTheMoreAboutMeSectionForPWSBiz_1900() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;

		String consultantEmailId =null;
		String accountId = null;

		country = driver.getCountry();
		env = driver.getEnvironment();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openPWSSite(country, env);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".com",driver.getCountry(),countryId),RFO_DB);
			consultantEmailId = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailId, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailId);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontConsultantPage.clickOnEditMyPWS();
		storeFrontConsultantPage.addNewContentOfYourOwnCopy();
		storeFrontConsultantPage.clickResetToDefaultCopyLink();
		s_assert.assertTrue(storeFrontConsultantPage.verifyDefaultContentReseted(),"Default content is not reseted");
		storeFrontConsultantPage.addNewContentOfYourOwnCopy();
		storeFrontConsultantPage.clickSaveButton();
		s_assert.assertTrue(storeFrontConsultantPage.verifyNewlyAddedContentSaved(),"newly added content not saved");
		s_assert.assertTrue(storeFrontConsultantPage.validateMeetYourConsultantPage(),"This is not meet your consultant page");
		s_assert.assertAll();
	}

	//Hybris Project-1899:To verify the More About Me section for PWS Com
	@Test
	public void testToVerifyTheMoreAboutMeSectionForPWSCom_1899(){
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;

		String consultantEmailId =null;
		String accountId = null;

		country = driver.getCountry();
		env = driver.getEnvironment();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openComPWSSite(country, env);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".com",driver.getCountry(),countryId),RFO_DB);
			consultantEmailId = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailId, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailId);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnMeetYourConsultantLink();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		storeFrontConsultantPage.addNewContentOfYourOwnCopyInComPWS();
		storeFrontConsultantPage.clickResetToDefaultCopyLinkInComPWS();
		s_assert.assertTrue(storeFrontConsultantPage.verifyDefaultContentResetedForComPWS(),"Default content is not reseted");
		storeFrontConsultantPage.addNewContentOfYourOwnCopyInComPWS();
		storeFrontConsultantPage.clickSaveButton();
		s_assert.assertTrue(storeFrontConsultantPage.verifyNewlyAddedContentSaved(),"newly added content not saved");
		s_assert.assertTrue(storeFrontConsultantPage.validateMeetYourConsultantPage(),"This is not meet your consultant page");
		s_assert.assertAll();
	}

	//Hybris Project-4055:View Meet your consultant Page on .BIZ & .COM as Logged in US Consultant (PWS Owner)
	@Test
	public void testViewYourConsultantPageOnBizAndCOMLoggedInUSConsultant_4055(){
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;

		String consultantEmailId =null;
		String accountId = null;

		country = driver.getCountry();
		env = driver.getEnvironment();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openPWSSite(country, env);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".com",driver.getCountry(),countryId),RFO_DB);
			consultantEmailId = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailId, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailId);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		storeFrontConsultantPage.clickOnMeetYourConsultantLink();
		s_assert.assertTrue(storeFrontConsultantPage.verifyPersonalizeMyProfileLinkPresent(),"Personalize my profile link is not present at meet your consultant page");
		storeFrontHomePage.openComPWSSite(country, env);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailId, password);
		storeFrontConsultantPage.clickOnMeetYourConsultantLink();
		s_assert.assertTrue(storeFrontConsultantPage.verifyPersonalizeMyProfileLinkPresent(),"Personalize my profile link is not present at meet your consultant page");
		s_assert.assertAll();
	}

	// Hybris Project-4056:Edit the Meet Your Consultant Page and Personalise and Save the changes
	@Test
	public void testEditTheMeetYourConsultantPageAndPersonaliseAndSaveTheChanges_4056(){
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;

		String consultantEmailId =null;
		String accountId = null;

		country = driver.getCountry();
		env = driver.getEnvironment();
		String phoneNumber = TestConstants.PHONE_NUMBER;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openPWSSite(country, env);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".com",driver.getCountry(),countryId),RFO_DB);
			consultantEmailId = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailId, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailId);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		storeFrontConsultantPage.clickOnMeetYourConsultantLink();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		storeFrontConsultantPage.enterPhoneNumberOnEditPWS(phoneNumber);
		storeFrontConsultantPage.clickSaveButton();
		s_assert.assertTrue(storeFrontHomePage.verifyContactBoxIsPresent(),"contactBox is not present");
		s_assert.assertTrue(storeFrontConsultantPage.validateEditedPhoneNumberSaved(phoneNumber),"Information is not edited in personalize my profile");
		s_assert.assertAll();
	}

	//Hybris Project-4054:Login to Canadian PWS site with US Consultant's Credentials(Owner of the PWS site)
	@Test
	public void testLoginToCanadianPWSsiteWithUSConsultantCredential_4054(){
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;

		String consultantEmailId =null;
		String accountId = null;

		country = "ca";
		env = driver.getEnvironment();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openPWSSite(country, env);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".com",driver.getCountry(),countryId),RFO_DB);
			consultantEmailId = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailId, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailId);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		logger.info("login is successful");
		s_assert.assertTrue(storeFrontConsultantPage.verifyShopSkinCareLinkPresent(),"shop skincare Link is not present in header");
		s_assert.assertTrue(storeFrontConsultantPage.verifyAboutRFLinkPresent(),"ABOUT R+F link is not present in the header");
		s_assert.assertAll();
	}

	//Hybris Project-4280:Login with USer Name and verify Edit Meet your Consultant Page
	@Test
	public void testLoginWithUserNameAndVerifyEditMeetYourConsultantPage_4280() {
		RFO_DB = driver.getDBNameRFO();  
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantWithPWSEmailID = null; //TestConstants.CONSULTANT_USERNAME;
		String consultantPWSURL = null; //TestConstants.CONSULTANT_COM_URL;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomConsultantList =DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");

		// For .com site
		consultantPWSURL = storeFrontHomePage.convertBizSiteToComSite(consultantPWSURL);
		storeFrontHomePage.openPWS(consultantPWSURL);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		storeFrontHomePage.clickOnUserName();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		//Clear Home Town City Name Field
		storeFrontConsultantPage.eraseHomeTownCityName();
		storeFrontHomePage.clickOnSaveAfterEditPWS();
		//click on Personalize My Profile link again and validate the change made
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		//validate HomeTown City field is blank
		s_assert.assertTrue(storeFrontConsultantPage.validateHomeTownCityFieldValueIsNull(),"Home Town City Field Value is not null");

		// For .biz site
		consultantPWSURL = storeFrontHomePage.convertComSiteToBizSite(consultantPWSURL);
		storeFrontHomePage.openPWS(consultantPWSURL);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		storeFrontHomePage.clickOnUserName();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		//Clear Home Town City Name Field
		storeFrontConsultantPage.eraseHomeTownCityName();
		storeFrontHomePage.clickOnSaveAfterEditPWS();
		//click on Personalize My Profile link again and validate the change made
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		//validate HomeTown City field is blank
		s_assert.assertTrue(storeFrontConsultantPage.validateHomeTownCityFieldValueIsNull(),"Home Town City Field Value is not null");
		s_assert.assertAll();
	}

	//Hybris Project-3997:Login in as RC user and go to find a consultant page
	@Test
	public void testLoginAsRCUserAndValidateSearchFieldOnFindAConsPage_3997(){
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID =null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		logger.info("login is successful");
		//Navigate to find A Consultant page..
		storeFrontHomePage.clickFindAConsultantLinkOnHomePage();
		//verify search field should be present on find a consultant page
		s_assert.assertTrue(storeFrontHomePage.validateConsultantSearchFieldPresent(),"Sponsor/Consultant Search field is not present on 'Find A Consultant' Page");
		s_assert.assertAll();
	}

	// Hybris Project-3995:Login in as PC user and go to find a consultant page
	@Test
	public void testLoginAsPCUserAndValidateSearchFieldOnFindAConsPage_3995() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.FIRST_NAME+randomNum;
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product with the price less than $80 and proceed to buy it
		storeFrontHomePage.applyPriceFilterLowToHigh();
		storeFrontHomePage.selectProductAndProceedToBuyWithoutFilter();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);

		//Pop for PC threshold validation
		s_assert.assertTrue(storeFrontHomePage.isPopUpForPCThresholdPresent(),"Threshold poup for PC validation NOT present");

		//In the Cart page add one more product
		storeFrontHomePage.addAnotherProduct();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");
		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksTermsAndConditionsPopup(),"PC Perks terms and conditions popup not visible when checkboxes for t&c not selected and place order button clicked");
		logger.info("PC Perks terms and conditions popup is visible when checkboxes for t&c not selected and place order button clicked");
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		//Navigate to find A Consultant page..
		storeFrontHomePage.clickFindAConsultantLinkOnHomePage();
		//verify search field should be present on find a consultant page
		s_assert.assertTrue(storeFrontHomePage.validateConsultantSearchFieldPresent(),"Sponsor/Consultant Search field is not present on 'Find A Consultant' Page");
		s_assert.assertAll();
	}

	//Hybris Project-3993:Search with US terminated consultnat's Full name
	@Test
	public void testSearchWithUSTerminatedConsultantFullName_3993() throws InterruptedException
	{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
		s_assert.assertTrue(storeFrontAccountTerminationPage.validateConfirmAccountTerminationPopUp(), "confirm account termination pop up is not displayed");
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
		storeFrontAccountTerminationPage.clickConfirmTerminationBtn();  
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
		storeFrontHomePage.clickOnCountryAtWelcomePage();
		//connect with a consultant
		storeFrontHomePage.clickConnectUnderConnectWithAConsultantSection();
		//search with terminated consultant
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(consultantEmailID);
		//verify invalid sponsor
		s_assert.assertTrue(storeFrontHomePage.validateInvalidSponsor(),"Terminated Sponsor is Present!!");
		s_assert.assertAll(); 
	}

	//Hybris Project-3989:Search with CA terminated consultnat's Full name
	@Test
	public void testSearchWithCATerminatedConsultantFullName_3989() throws InterruptedException	{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
		s_assert.assertTrue(storeFrontAccountTerminationPage.validateConfirmAccountTerminationPopUp(), "confirm account termination pop up is not displayed");
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
		storeFrontAccountTerminationPage.clickConfirmTerminationBtn();  
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
		storeFrontHomePage.clickOnCountryAtWelcomePage();
		//connect with a consultant
		storeFrontHomePage.clickConnectUnderConnectWithAConsultantSection();
		//search with terminated consultant
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(consultantEmailID);
		//verify invalid sponsor
		s_assert.assertTrue(storeFrontHomePage.validateInvalidSponsor(),"Terminated Sponsor is Present!!");
		s_assert.assertAll(); 
	}

	//Hybris Project-3991:Go to' Find a consultant' page and search with US consultnat's First and Last name
	@Test
	public void testSearchWithUSConsultantFirstNameOnFindAConsultantPage_3991()	{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantDetailsList =  null;
		String firstName= null;
		String lastName =null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomConsultantDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_USER_DETAIL_FROM_ACCOUNTID_RFO,countryId),RFO_DB);
		firstName = (String) getValueFromQueryResult(randomConsultantDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(randomConsultantDetailsList, "LastName");
		//Navigate to find A Consultant page..
		storeFrontHomePage.clickFindAConsultantLinkOnHomePage();
		//search with Consultant's first Name
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(firstName);
		//verify full name,city,state,Zip code,PWS is present
		s_assert.assertTrue(storeFrontHomePage.verifySponsorDetailsPresent(),"Sponsor details is not present!!");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorFullNamePresent(),"Sponsor Full Name is not present!!");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorZipCodePresent(),"Sponsor zipcode is not present!!");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorCityPresent(),"Sponsor details is not present!!");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorPWSComSitePresent(),"Sponsor details is not present!!");
		//search with Consultant's last Name
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(lastName);
		//verify full name,city,state,Zip code,PWS is present
		s_assert.assertTrue(storeFrontHomePage.verifySponsorDetailsPresent(),"Sponsor details is not present!!");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorFullNamePresent(),"Sponsor Full Name is not present!!");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorZipCodePresent(),"Sponsor zipcode is not present!!");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorCityPresent(),"Sponsor details is not present!!");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorPWSComSitePresent(),"Sponsor details is not present!!");
		s_assert.assertAll(); 
	}

	//Hybris Project-4060:Post Contact Me Request on Meet Your Consultant Page on US Con's PWS as another
	@Test
	public void testPostContactMeRequestOnMeetYourConsultantPageOnUSConsPWSAsAnother_4060() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO();  
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantWithPWSEmailID = null; //TestConstants.CONSULTANT_USERNAME;
		String consultantPWSURL = null; //TestConstants.CONSULTANT_COM_URL;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomConsultantList =DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");

		// For .com site
		consultantPWSURL = storeFrontHomePage.convertBizSiteToComSite(consultantPWSURL);
		storeFrontHomePage.openPWS(consultantPWSURL);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		storeFrontHomePage.clickOnUserName();
		//Post contact me request on meet your consultant page 
		storeFrontHomePage.postContactMeRequestOnMeetYourConsultantPage(consultantWithPWSEmailID);
		// For .biz site
		consultantPWSURL = storeFrontHomePage.convertComSiteToBizSite(consultantPWSURL);
		storeFrontHomePage.openPWS(consultantPWSURL);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		storeFrontHomePage.clickOnUserName();
		storeFrontHomePage.postContactMeRequestOnMeetYourConsultantPage(consultantWithPWSEmailID);
		s_assert.assertAll();
	}

	// Hybris Project-1919:To verify the follow me functionality in edit meet the consultant page for com PWS site
	@Test
	public void testFollowMeFunctionalityInEditMeetTheConsultantPageForCOMPWSIte_1919() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO();  
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantWithPWSEmailID = null; //TestConstants.CONSULTANT_USERNAME;
		String consultantPWSURL = null; //TestConstants.CONSULTANT_COM_URL;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomConsultantList =DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");

		// For .com site
		consultantPWSURL = storeFrontHomePage.convertBizSiteToComSite(consultantPWSURL);
		storeFrontHomePage.openPWS(consultantPWSURL);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		storeFrontHomePage.clickOnUserName();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		//Go to follow me section and add facebook,twitter,pinterest & instagram url
		storeFrontHomePage.addSocialNetworkingURLUnderFollowMeSection();
		storeFrontHomePage.clickSaveBtnOnEditConsultantInfoPage();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		//Go to follow me section and clear facebook,twitter,pinterest & instagram url
		storeFrontHomePage.clearSocialNetworkingURLUnderFollowMeSection();
		storeFrontHomePage.clickSaveBtnOnEditConsultantInfoPage();
		// For .biz site
		consultantPWSURL = storeFrontHomePage.convertComSiteToBizSite(consultantPWSURL);
		storeFrontHomePage.openPWS(consultantPWSURL);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		storeFrontHomePage.clickOnUserName();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		//Go to follow me section and add facebook,twitter,pinterest & instagram url
		storeFrontHomePage.addSocialNetworkingURLUnderFollowMeSection();
		storeFrontHomePage.clickSaveBtnOnEditConsultantInfoPage();
		storeFrontHomePage.clickOnPersonalizeMyProfileLink();
		//Go to follow me section and clear facebook,twitter,pinterest & instagram url
		storeFrontHomePage.clearSocialNetworkingURLUnderFollowMeSection();
		storeFrontHomePage.clickSaveBtnOnEditConsultantInfoPage();
		s_assert.assertAll();
	}	

	//Hybris Project-4027:Access Solution tool from .COM Site
	@Test
	public void testAccessSolutionToolFromComSite_4027() {
		RFO_DB = driver.getDBNameRFO();  
		country = driver.getCountry();
		env = driver.getEnvironment();  
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String PWS = storeFrontHomePage.getBizPWS(country, env);
		PWS = storeFrontHomePage.convertBizSiteToComSite(PWS);
		storeFrontHomePage.openPWS(PWS);
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountId = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		//click learn more link
		storeFrontHomePage.clickLearnMoreLinkUnderSolutionToolAndSwitchControl();
		//validate consultant info on top right corner..
		s_assert.assertTrue(storeFrontHomePage.validateConsultantNameOnTopRightCorner(),"Consultant Info is not present on right top Corner On Solution tool page");
		s_assert.assertAll();
	}


	// Hybris Project-4063:Access Canadian PWS site of USConsultant as US RCUser
	@Test
	public void testAccessCanadianPWSiteOfUSConsultantAsUSRCUser_4063() throws InterruptedException	{
		RFO_DB = driver.getDBNameRFO(); 
		country = driver.getCountry();
		env = driver.getEnvironment(); 
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String bizPWS=storeFrontHomePage.getBizPWS(country, env);
		//Navigate to canadian PWS
		String bizPWSCA=storeFrontHomePage.convertUSBizPWSToCA(bizPWS);
		driver.get(bizPWSCA);
		//Access Canadian PWS site of US Consultant as US RC user
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID =null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		logger.info("login is successful");
		//verify US RC user should login and redirect to US site of same PWS.
		s_assert.assertTrue(driver.getCurrentUrl().trim().equalsIgnoreCase(bizPWS+"/"),"US RC user is not redirected to US Site of same PWS");
		s_assert.assertAll();
	}

	//Hybris Project-4069:Access US Con's Canadian PWS as a Canadian Consultant With Pulse
	@Test
	public void testAccessUSConsCanadianPWSAsCanadianConsWithPulse_4069(){
		RFO_DB = driver.getDBNameRFO(); 
		country = driver.getCountry();
		env = driver.getEnvironment(); 
		if(driver.getCountry().trim().equalsIgnoreCase("us")){
			storeFrontHomePage = new StoreFrontHomePage(driver);
			String bizPWS=storeFrontHomePage.getBizPWS(country, env);
			//Navigate to canadian PWS
			String bizPWSCA=storeFrontHomePage.convertUSBizPWSToCA(bizPWS);
			driver.get(bizPWSCA);
			//Get the Consultant with PWS of CA
			List<Map<String, Object>> randomConsultantList =  null;
			String consultantWithPWSEmailID = null;
			while(true){
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,env,"ca","40"),RFO_DB);
				consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
				String comPWSOfSponser=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));
				logger.info("BIZ PWS of sponsor is "+comPWSOfSponser);
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
				boolean isLoginError = driver.getCurrentUrl().contains("error");
				if(isLoginError){
					logger.info("Login error for the user "+consultantWithPWSEmailID);
					driver.get(driver.getURL());
				}
				else
					break;
				//Verify user is redirected to its ows pws site
				s_assert.assertTrue(driver.getCurrentUrl().replaceAll("biz","com").trim().equalsIgnoreCase(comPWSOfSponser),"user is not redirected to its own PWS site");
			}
			s_assert.assertAll();
		}else{
			logger.info("Not Executed Test is for 'US' Environment");
		}
	}

	//Hybris Project-3978:Go to 'Find a consultant page' and search with CA consultnat Full name
	@Test
	public void testToFindAConsultantPageAndSearchWithCAConsultantFullName_3978(){
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantFirstName = null;
		String consultantlastName = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_CONSULTANT_DETAILS_RFO,countryId),RFO_DB);
		consultantFirstName = (String) getValueFromQueryResult(randomConsultantList, "FirstName");
		consultantlastName = (String) getValueFromQueryResult(randomConsultantList, "LastName");
		storeFrontHomePage.clickOnSponsorName();
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(consultantFirstName+" "+consultantlastName);
		s_assert.assertTrue(storeFrontHomePage.verifySponsorDetailsPresent(),"Sponsor Detail not present on page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorFullNamePresent(),"Sponsor full name not present in Sponsor Details");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorZipCodePresent(), "Sponsor ZipCode not present in Sponsor Detail page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorCityPresent(), "Sponsor city not present in Sponsor Detail page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorPWSComSitePresent(), "Sponsor's PWS com site not present in Sponsor Detail page");
		s_assert.assertAll();

	}

	//Hybris Project-3979:search with CA consultnat Last name
	@Test
	public void testSearchWithCAConsultantLastName_3979(){
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantlastName = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_CONSULTANT_DETAILS_RFO,countryId),RFO_DB);
		consultantlastName = (String) getValueFromQueryResult(randomConsultantList, "LastName");
		storeFrontHomePage.clickOnSponsorName();
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(consultantlastName);
		s_assert.assertTrue(storeFrontHomePage.verifySponsorDetailsPresent(),"Sponsor Detail not present on page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorFullNamePresent(),"Sponsor full name not present in Sponsor Details");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorZipCodePresent(), "Sponsor ZipCode not present in Sponsor Detail page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorCityPresent(), "Sponsor city not present in Sponsor Detail page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorPWSComSitePresent(), "Sponsor's PWS com site not present in Sponsor Detail page");
		s_assert.assertAll();

	}

	//Hybris Project-3980:Go to 'Find a consultant page' and search with CA consultnat Full name
	@Test
	public void testGoToFindAConsultantPageAndSearchWithCAConsultantFullName_3980(){
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantFirstName = null;
		String consultantlastName = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_CONSULTANT_DETAILS_RFO,countryId),RFO_DB);
		consultantFirstName = (String) getValueFromQueryResult(randomConsultantList, "FirstName");
		consultantlastName = (String) getValueFromQueryResult(randomConsultantList, "LastName");
		storeFrontHomePage.clickOnSponsorName();
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(consultantFirstName+" "+consultantlastName);
		s_assert.assertTrue(storeFrontHomePage.verifySponsorDetailsPresent(),"Sponsor Detail not present on page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorFullNamePresent(),"Sponsor full name not present in Sponsor Details");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorZipCodePresent(), "Sponsor ZipCode not present in Sponsor Detail page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorCityPresent(), "Sponsor city not present in Sponsor Detail page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorPWSComSitePresent(), "Sponsor's PWS com site not present in Sponsor Detail page");
		s_assert.assertAll();
	}

	//Hybris Project-3981:search with CA consultant with CID
	@Test
	public void testSearchWithCAConsultantWithCID_3981(){
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		country = driver.getCountry();
		String CCS = null;
		randomConsultantList = 	DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		logger.info("Account Id of the user is "+accountID);

		// Get Account Number
		List<Map<String, Object>>sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		CCS = (String) getValueFromQueryResult(sponsorIdList, "AccountNumber");
		storeFrontHomePage.clickOnSponsorName();
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(CCS);
		s_assert.assertTrue(storeFrontHomePage.verifySponsorDetailsPresent(),"Sponsor Detail not present on page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorFullNamePresent(),"Sponsor full name not present in Sponsor Details");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorZipCodePresent(), "Sponsor ZipCode not present in Sponsor Detail page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorCityPresent(), "Sponsor city not present in Sponsor Detail page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorPWSComSitePresent(), "Sponsor's PWS com site not present in Sponsor Detail page");
		s_assert.assertAll();
	}

	//Hybris Project-3982:Search with CA consultant First name
	@Test
	public void testSearchWithCAConsultantFirstName_3982(){
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantFirstName = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_CONSULTANT_DETAILS_RFO,countryId),RFO_DB);
		consultantFirstName = (String) getValueFromQueryResult(randomConsultantList, "FirstName");
		storeFrontHomePage.clickOnSponsorName();
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(consultantFirstName);
		s_assert.assertTrue(storeFrontHomePage.verifySponsorDetailsPresent(),"Sponsor Detail not present on page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorFullNamePresent(),"Sponsor full name not present in Sponsor Details");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorZipCodePresent(), "Sponsor ZipCode not present in Sponsor Detail page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorCityPresent(), "Sponsor city not present in Sponsor Detail page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorPWSComSitePresent(), "Sponsor's PWS com site not present in Sponsor Detail page");
		s_assert.assertAll();
	}

	//Hybris Project-3983:Search with PWS prefix COM site
	@Test
	public void testSearchWithPWSPrefixCOMSite_3983(){
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomActiveSitePrefixList =  null;
		String activeSitePrefix = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomActiveSitePrefixList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_SITE_PREFIX_RFO,countryId),RFO_DB);
		activeSitePrefix = (String) getValueFromQueryResult(randomActiveSitePrefixList, "SitePrefix");
		storeFrontHomePage.clickOnSponsorName();
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(activeSitePrefix);
		s_assert.assertTrue(storeFrontHomePage.verifySponsorDetailsPresent(),"Sponsor Detail not present on page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorFullNamePresent(),"Sponsor full name not present in Sponsor Details");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorZipCodePresent(), "Sponsor ZipCode not present in Sponsor Detail page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorCityPresent(), "Sponsor city not present in Sponsor Detail page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorPWSComSitePresent(), "Sponsor's PWS com site not present in Sponsor Detail page");
		s_assert.assertAll();
	}

	//Hybris Project-3984:search with CA consultnat's account number
	@Test
	public void testSearchWithCAConsultantAccountNumber_3984(){
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		country = driver.getCountry();
		String CCS = null;
		randomConsultantList =DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		logger.info("Account Id of the user is "+accountID);

		// Get Account Number
		List<Map<String, Object>>sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		CCS = (String) getValueFromQueryResult(sponsorIdList, "AccountNumber");
		storeFrontHomePage.clickOnSponsorName();
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(CCS);
		s_assert.assertTrue(storeFrontHomePage.verifySponsorDetailsPresent(),"Sponsor Detail not present on page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorFullNamePresent(),"Sponsor full name not present in Sponsor Details");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorZipCodePresent(), "Sponsor ZipCode not present in Sponsor Detail page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorCityPresent(), "Sponsor city not present in Sponsor Detail page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorPWSComSitePresent(), "Sponsor's PWS com site not present in Sponsor Detail page");
		s_assert.assertAll();
	}

	//Hybris Project-3985:Search with Active Retail customers full name
	@Test
	public void testSearchWithActiveRetailCustomersFullName_3985(){
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		List<Map<String, Object>> randomRCDetailList = null;
		String rcFirstName = null;
		String rcLastName = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
		accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
		logger.info("Account Id of the user is "+accountId);
		randomRCDetailList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_USER_DETAILS_FROM_ACCOUNTID_RFO,accountId),RFO_DB);
		rcFirstName = String.valueOf(getValueFromQueryResult(randomRCDetailList, "FirstName"));
		rcLastName = String.valueOf(getValueFromQueryResult(randomRCDetailList, "LastName"));
		storeFrontHomePage.clickOnSponsorName();
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(rcFirstName+" "+rcLastName);
		s_assert.assertTrue(storeFrontHomePage.verifyNotFoundMsgPresent(), "Not found msg not present for rc");
		s_assert.assertAll();
	}

	//Hybris Project-3986:Search with ActiveRetail customers Account ID
	@Test
	public void testSearchWithActiveRetailCustomersAccountID_3986(){
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
		accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
		storeFrontHomePage.clickOnSponsorName();
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(accountId);
		s_assert.assertTrue(storeFrontHomePage.verifyNotFoundMsgPresent(), "Not found msg not present for rc");
		s_assert.assertAll();
	}

	//Hybris Project-3987:Search with Active Preferred customer's full name
	@Test
	public void testSearchWithActivePrefferedCustomerFullName_3987(){
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		List<Map<String, Object>> randomPCDetailList =  null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String accountId = null;
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
		randomPCDetailList =  DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_USER_DETAILS_FROM_ACCOUNTID_RFO,accountId),RFO_DB);
		String pcFirstName = String.valueOf(getValueFromQueryResult(randomPCDetailList, "FirstName"));
		String pcLastName = String.valueOf(getValueFromQueryResult(randomPCDetailList, "LastName"));
		storeFrontHomePage.clickOnSponsorName();
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(pcFirstName+" "+pcLastName);
		s_assert.assertTrue(storeFrontHomePage.verifyNotFoundMsgPresent(), "Not found msg not present for rc");
		s_assert.assertAll();
	}

	//Hybris Project-3988:Search with Active Preferred customer's Account id
	@Test
	public void testSearchWithActivePreferredCustomerAccountId_3988(){
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String accountId = null;
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
		storeFrontHomePage.clickOnSponsorName();
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(accountId);
		s_assert.assertTrue(storeFrontHomePage.verifyNotFoundMsgPresent(), "Not found msg not present for rc");
		s_assert.assertAll();
	}

	//Hybris Project-4003:Look up with Active CA consultant's full name
	@Test
	public void testLookUpActiveCAConsultantFullName_4003() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
			List<Map<String, Object>> randomConsultantList =  null;
			List<Map<String, Object>> randomConsultantDetailList = null;
			String accountID = null;
			String consultantFirstName = null;
			String consultantLastName = null;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			randomConsultantList = 	DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".com",driver.getCountry(),countryId),RFO_DB);
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));

			randomConsultantDetailList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_USER_DETAILS_FROM_ACCOUNTID_RFO,accountID),RFO_DB);
			consultantFirstName = (String) getValueFromQueryResult(randomConsultantDetailList, "FirstName");
			consultantLastName = (String) getValueFromQueryResult(randomConsultantDetailList, "LastName");
			storeFrontHomePage.clickOnSponsorName();
			storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(consultantFirstName+" "+consultantLastName);
			s_assert.assertTrue(storeFrontHomePage.verifySponsorDetailsPresent(),"Sponsor Detail not present on page");
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			s_assert.assertTrue(storeFrontHomePage.verifyUserRedirectingToComSite(),"user is not redirecting to com site");
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	// Hybris Project-4004:Look up with Active CA consultant's Account ID
	@Test
	public void testLookUpWithActiveCAConsultantAccountID_4004() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();
			List<Map<String, Object>> randomConsultantList =  null;
			String accountID = null;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			randomConsultantList = 	DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			storeFrontHomePage.clickOnSponsorName();
			storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(accountID);
			s_assert.assertTrue(storeFrontHomePage.verifySponsorDetailsPresent(),"Sponsor Detail not present on page");
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			s_assert.assertTrue(storeFrontHomePage.verifyUserRedirectingToComSite(),"user is not redirecting to com site");
			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	// Hybris Project-4007:Look up with Active CA consultnat who has no pulse/ PWS
	@Test
	public void testLookUpWithActiveCAConsultantWithNoPulseOrPWS_4007() throws InterruptedException	{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO();  
			List<Map<String, Object>> randomConsultantList =  null;
			//List<Map<String, Object>> randomConsultantPWSList =  null;
			String consultantWithPWSEmailID = null;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			// Get Consultant with PWS from database
			while(true){
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
				consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
				boolean isLoginError = driver.getCurrentUrl().contains("error");
				if(isLoginError){
					logger.info("Login error for the user "+consultantWithPWSEmailID);
					driver.get(driver.getURL());
				}
				else
					break;
			}

			logger.info("login is successful");
			//cancel pulse ofr the consultant user
			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage=storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			storeFrontAccountInfoPage.clickOnYourAccountDropdown();
			storeFrontAccountInfoPage.clickOnAutoShipStatus();
			//Verify  user can cancel Pulse subscription through my account.
			storeFrontAccountInfoPage.cancelPulseSubscription();
			s_assert.assertTrue(storeFrontAccountInfoPage.validatePulseCancelled(),"pulse subscription is not cancelled for the user");
			//Navigate to the base url
			driver.get(driver.getURL());
			//connect with a consultant
			storeFrontHomePage.clickConnectUnderConnectWithAConsultantSection();
			//search with Active PC Act ID
			storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(consultantWithPWSEmailID);
			//verify 'No Result found' is displayed
			s_assert.assertTrue(storeFrontHomePage.validateInvalidSponsor(),"'No Result Found' is not displayed!!");
			s_assert.assertAll(); 
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	//Hybris Project-4005:Look up with Teminated CA consultnat's full name / Account ID
	@Test
	public void testLookUpWithTerminatedCAConsultantFullNameOrActID_4005() throws InterruptedException	{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			RFO_DB = driver.getDBNameRFO(); 
			List<Map<String, Object>> randomConsultantList =  null;
			String consultantEmailID = null;
			String accountID = null;
			storeFrontHomePage = new StoreFrontHomePage(driver);
			while(true){
				randomConsultantList =DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
				consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
				accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
				logger.info("Account Id of the user is "+accountID);
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
				boolean isLoginError = driver.getCurrentUrl().contains("error");
				if(isLoginError){
					logger.info("Login error for the user "+consultantEmailID);
					driver.get(driver.getURL());
				}
				else
					break;
			}
			logger.info("login is successful");
			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			storeFrontAccountInfoPage.clickOnYourAccountDropdown();
			storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
			storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
			s_assert.assertTrue(storeFrontAccountTerminationPage.validateConfirmAccountTerminationPopUp(), "confirm account termination pop up is not displayed");
			s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
			storeFrontAccountTerminationPage.clickConfirmTerminationBtn();  
			storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
			storeFrontHomePage.clickOnCountryAtWelcomePage();
			//connect with a consultant
			storeFrontHomePage.clickConnectUnderConnectWithAConsultantSection();
			//search with terminated consultant
			storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(consultantEmailID);
			//verify 'No Result found' is displayed
			s_assert.assertTrue(storeFrontHomePage.validateInvalidSponsor(),"Terminated CA Consultant is Present!!!");
			s_assert.assertAll(); 
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}

	}

	//Hybris Project-4011:Look up with Active Retail customer's full name / Account ID
	@Test
	public void testLookUpWithActiveRCFullNameOrAccountID_4011(){
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID =null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		logger.info("login is successful");
		storeFrontHomePage.clickOnUserName();
		//search with account ID
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(accountId);
		//verify 'No Result found' is displayed
		s_assert.assertTrue(storeFrontHomePage.validateInvalidSponsor(),"Respective RC User is Present!!!");
		s_assert.assertAll(); 
	}

	//Hybris Project-4009:Look up with Active preferred customer's full name / Account ID
	@Test
	public void testLookUpWithActivePCFullNameOrAccountID_4009(){
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
		accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
		logger.info("Account Id of the user is "+accountId);
		storeFrontHomePage.clickFindAConsultantLinkOnHomePage();
		//search with account ID
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(accountId);
		//verify 'No Result found' is displayed
		s_assert.assertTrue(storeFrontHomePage.validateInvalidSponsor(),"Respective PC User is Present!!!");
		s_assert.assertAll(); 
	}

	//Hybris Project-4002:Look up with Terminated Retail consultant's Full name/ Account ID
	@Test
	public void testLookUpWithTerminatedRCFullNameOrccountID_4002() throws InterruptedException	{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID =null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		logger.info("login is successful");
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontRCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage=storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.selectTerminationReason();
		storeFrontAccountTerminationPage.enterTerminationComments();
		storeFrontAccountTerminationPage.selectCheckBoxForVoluntarilyTerminate();
		storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyPopupHeader(),"Account termination Page Pop Up Header is not Present");
		storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup(); 
		//Navigate to the base url
		driver.get(driver.getURL());
		//connect with a consultant
		storeFrontHomePage.clickConnectUnderConnectWithAConsultantSection();
		//search with terminated RC
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(accountId);
		//verify 'No Result found' is displayed
		s_assert.assertTrue(storeFrontHomePage.validateInvalidSponsor(),"Terminated RC User is Present!!!");
		s_assert.assertAll(); 
	}

	// Hybris Project-4001:Look up with Terminated Preferred consultant's Full name/ Account ID
	@Test
	public void testLookUpWithTerminatedPCFullNameOrccountID_4001() throws InterruptedException	{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontPCUserPage.clickOnYourAccountDropdown();
		storeFrontPCUserPage.clickOnPCPerksStatus();
		storeFrontPCUserPage.clickDelayOrCancelPCPerks();
		storeFrontPCUserPage.clickPleaseCancelMyPcPerksActBtn();
		storeFrontPCUserPage.cancelMyPCPerksAct();
		//Navigate to the base url
		driver.get(driver.getURL());
		//connect with a consultant
		storeFrontHomePage.clickConnectUnderConnectWithAConsultantSection();
		//search with terminated PC
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(accountId);
		//verify 'No Result found' is displayed
		s_assert.assertTrue(storeFrontHomePage.validateInvalidSponsor(),"Terminated PC User is Present!!!");
		s_assert.assertAll(); 
	}

	//Hybris Project-4000:Go to Find a consultant page and search with CA consultnat user who upgraded from RC to Consultant
	@Test
	public void testGoToFindAConsultantPageAndSearchWithCAConsultantUserWhoUpgradedFromRCtoConsultant_4000() throws InterruptedException{
		List<Map<String, Object>> randomRCList =  null;
		RFO_DB = driver.getDBNameRFO(); 
		String rcUserEmailID = null;
		String accountId = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String lastName = "lN";
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName =  TestConstants.REGIMEN_NAME_REVERSE;
		if(driver.getCountry().equalsIgnoreCase("CA")){

			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{

			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);  
		storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
		storeFrontHomePage.enterEmailAddress(rcUserEmailID);
		s_assert.assertTrue(storeFrontHomePage.verifyUpradingToConsulTantPopup(), "Upgrading to a consultant popup is not present");
		storeFrontHomePage.enterPasswordForUpgradeRCToConsultant();
		storeFrontHomePage.clickOnLoginToTerminateToMyRCAccount();
		s_assert.assertTrue(storeFrontHomePage.verifyAccountTerminationMessage(), "Rc user is not terminated successfully");
		storeFrontHomePage.enterFirstName(firstName);
		storeFrontHomePage.enterLastName(lastName);
		storeFrontHomePage.enterEmailAddress(rcUserEmailID);
		storeFrontHomePage.enterPassword(password);
		storeFrontHomePage.enterConfirmPassword(password);
		storeFrontHomePage.enterAddressLine1(addressLine1);
		storeFrontHomePage.enterCity(city);
		storeFrontHomePage.selectProvince();
		storeFrontHomePage.enterPostalCode(postalCode);
		storeFrontHomePage.enterPhoneNumber(phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		storeFrontHomePage.clickOnSponsorName();
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(firstName);
		s_assert.assertTrue(storeFrontHomePage.verifySponsorDetailsPresent(),"Sponsor Detail not present on page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorFullNamePresent(),"Sponsor full name not present in Sponsor Details");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorZipCodePresent(), "Sponsor ZipCode not present in Sponsor Detail page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorCityPresent(), "Sponsor city not present in Sponsor Detail page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorPWSComSitePresent(), "Sponsor's PWS com site not present in Sponsor Detail page");
		s_assert.assertAll();
	}

	//Hybris Project-3999:Go to Find a consultant page and search with CA consultnat user who upgraded from PC to Consultnat
	@Test
	public void testGoToFindAConsultantPageAndSearchWithCAConsultantUserWhoUpgradedFromPcToConsultant_3999() throws InterruptedException{
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String socialInsuranceNumber2 = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String lastName = "lN";
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName =  TestConstants.REGIMEN_NAME_REVERSE;
		if(driver.getCountry().equalsIgnoreCase("CA")){

			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{

			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);  
		storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
		storeFrontHomePage.enterEmailAddress(pcUserEmailID);
		s_assert.assertTrue(storeFrontHomePage.verifyUpradingToConsulTantPopup(), "Upgrading to a consultant popup is not present");
		storeFrontHomePage.enterPasswordForUpgradePcToConsultant();
		storeFrontHomePage.clickOnLoginToTerminateToMyPCAccount();
		s_assert.assertTrue(storeFrontHomePage.verifyAccountTerminationMessage(), "Pc user is not terminated successfully");
		storeFrontHomePage.enterEmailAddress(pcUserEmailID);
		storeFrontHomePage.clickOnEnrollUnderLastUpline();
		logger.info("After click enroll under last upline we are on "+driver.getCurrentUrl());
		storeFrontHomePage.selectEnrollmentKitPage(TestConstants.KIT_NAME_BIG_BUSINESS, TestConstants.REGIMEN_NAME_REVERSE);  
		storeFrontHomePage.chooseEnrollmentOption(TestConstants.EXPRESS_ENROLLMENT);
		storeFrontHomePage.enterFirstName(firstName);
		storeFrontHomePage.enterLastName(lastName);
		storeFrontHomePage.enterEmailAddress(pcUserEmailID);
		storeFrontHomePage.enterPassword(password);
		storeFrontHomePage.enterConfirmPassword(password);
		storeFrontHomePage.enterAddressLine1(addressLine1);
		storeFrontHomePage.enterCity(city);
		storeFrontHomePage.selectProvince();
		storeFrontHomePage.enterPostalCode(postalCode);
		storeFrontHomePage.enterPhoneNumber(phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber2);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		storeFrontHomePage.clickOnSponsorName();
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(firstName);
		s_assert.assertTrue(storeFrontHomePage.verifySponsorDetailsPresent(),"Sponsor Detail not present on page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorFullNamePresent(),"Sponsor full name not present in Sponsor Details");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorZipCodePresent(), "Sponsor ZipCode not present in Sponsor Detail page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorCityPresent(), "Sponsor city not present in Sponsor Detail page");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorPWSComSitePresent(), "Sponsor's PWS com site not present in Sponsor Detail page");
		s_assert.assertAll();
	}

	//Hybris Project-4006:Look up with Pending CA consultnat's full name/ Account ID
	@Test
	public void testLookUpWithPendingCAConsultantAccountID_4006() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> pendingUserList =  null;
		String accountIDPendingUser=null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		pendingUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_ACCOUNT_ID_FOR_PENDING_USER,RFO_DB);
		accountIDPendingUser=(String.valueOf(getValueFromQueryResult(pendingUserList, "AccountID")));
		System.out.println("accountIDPendingUser"+accountIDPendingUser);
		//Navigate to find A Consultant page..
		storeFrontHomePage.clickFindAConsultantLinkOnHomePage();
		//search with Consultant's first Name
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(accountIDPendingUser);
		//verify 'No Result found' is displayed
		s_assert.assertTrue(storeFrontHomePage.validateInvalidSponsor(),"Result shown for pending user!!!");
		s_assert.assertAll();
	}

	// Hybris Project-3996:Login in as consultant user and go to find a consultant page
	@Test
	public void testSearchFunctionalityAsConsultantOnFindAConsultantPage_3996() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		String sampleSponsorName="test";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountTerminationPage = storeFrontAccountInfoPage.clickTerminateMyAccount();
		storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
		s_assert.assertTrue(storeFrontAccountTerminationPage.validateConfirmAccountTerminationPopUp(), "confirm account termination pop up is not displayed");
		s_assert.assertTrue(storeFrontAccountTerminationPage.verifyAccountTerminationIsConfirmedPopup(), "Account still exist");
		storeFrontAccountTerminationPage.clickConfirmTerminationBtn();  
		storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
		storeFrontHomePage.clickOnCountryAtWelcomePage();
		//connect with a consultant
		storeFrontHomePage.clickConnectUnderConnectWithAConsultantSection();
		//verify search functionality is working?
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(sampleSponsorName);
		s_assert.assertTrue(storeFrontHomePage.validateSearchFunctionalityWorking(),"Search functionality not working as no results are Shown");
		s_assert.assertAll();
	}

	//Hybris Project-3992:Go to 'Find a consultant' page and search with First and Last name of consultant
	@Test
	public void testSearchWithFirstAndLastNameOfConsultantOnFindAConsultantPage_3992()	{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantDetailsList =  null;
		String firstName= null;
		String lastName =null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		randomConsultantDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_USER_DETAIL_FROM_ACCOUNTID_RFO,accountID),RFO_DB);
		firstName = (String) getValueFromQueryResult(randomConsultantDetailsList, "FirstName");
		lastName = (String) getValueFromQueryResult(randomConsultantDetailsList, "LastName");
		//Navigate to find A Consultant page..
		storeFrontHomePage.clickFindAConsultantLinkOnHomePage();
		//search with Consultant's first Name
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(firstName);
		//verify full name,city,state,Zip code,PWS is present
		s_assert.assertTrue(storeFrontHomePage.verifySponsorDetailsPresent(),"Sponsor details is not present!!");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorFullNamePresent(),"Sponsor Full Name is not present!!");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorZipCodePresent(),"Sponsor zipcode is not present!!");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorCityPresent(),"Sponsor details is not present!!");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorPWSComSitePresent(),"Sponsor details is not present!!");
		//search with Consultant's last Name
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(lastName);
		//verify full name,city,state,Zip code,PWS is present
		s_assert.assertTrue(storeFrontHomePage.verifySponsorDetailsPresent(),"Sponsor details is not present!!");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorFullNamePresent(),"Sponsor Full Name is not present!!");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorZipCodePresent(),"Sponsor zipcode is not present!!");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorCityPresent(),"Sponsor details is not present!!");
		s_assert.assertTrue(storeFrontHomePage.verifySponsorPWSComSitePresent(),"Sponsor details is not present!!");
		s_assert.assertAll(); 
	}

//--
	
	//Hybris Project-4658:Place an adhoc order for RC user enrolled without creating an order
	 @Test
	 public void testPlaceAnAdhocOrderForRCUserEnrolledWithoutCreatingAnOrder_4658() throws InterruptedException{
	  int randomNum = CommonUtils.getRandomNum(10000, 1000000);
	  String firstName=TestConstants.FIRST_NAME+randomNum;
	  String country = driver.getCountry();
	    
	  if(country.equalsIgnoreCase("CA")){
	   kitName = TestConstants.KIT_NAME_PERSONAL;    
	   addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
	   city = TestConstants.CITY_CA;
	   postalCode = TestConstants.POSTAL_CODE_CA;
	   phoneNumber = TestConstants.PHONE_NUMBER_CA;
	  }else{
	   kitName = TestConstants.KIT_NAME_PERSONAL;
	   addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
	   city = TestConstants.NEW_ADDRESS_CITY_US;
	   postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
	   phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
	  }
	  storeFrontHomePage = new StoreFrontHomePage(driver);
	  storeFrontAccountInfoPage = new StoreFrontAccountInfoPage(driver);
	  storeFrontShippingInfoPage = new StoreFrontShippingInfoPage(driver);
	  storeFrontHomePage.clickSignUpnowOnbizSite();
	  storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);
	  storeFrontHomePage.clickOnWelcomeDropDown();
	  storeFrontAccountInfoPage = storeFrontHomePage.clickAccountInfoLinkPresentOnWelcomeDropDown();
	  storeFrontAccountInfoPage.updateAddressWithCityAndPostalCode(addressLine1, city, postalCode);
	     storeFrontAccountInfoPage.updateMainPhnNumber(phoneNumber);
	     storeFrontAccountInfoPage.clickSaveAccountBtn();
	     s_assert.assertTrue(storeFrontAccountInfoPage.verifyProfileUpdationMessage(),"updation message not appeared");
	     storeFrontHomePage.clickOnWelcomeDropDown();
	     storeFrontShippingInfoPage = storeFrontHomePage.clickShippingLinkPresentOnWelcomeDropDown();
	     storeFrontShippingInfoPage.clickAddNewShippingProfileLink();
	     storeFrontShippingInfoPage.enterNewShippingAddressName(firstName+" "+TestConstants.LAST_NAME+randomNum);
	     storeFrontShippingInfoPage.enterNewShippingAddressLine1(addressLine1);
	     storeFrontShippingInfoPage.enterNewShippingAddressCity(city);
	     storeFrontShippingInfoPage.selectNewShippingAddressState();
	     storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(postalCode);
	     storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(phoneNumber);
	     storeFrontShippingInfoPage.clickOnSaveShippingProfile();
	     storeFrontHomePage.clickOnWelcomeDropDown();
	     storeFrontBillingInfoPage = storeFrontHomePage.clickBillingInfoLinkPresentOnWelcomeDropDown();
	     storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
	     storeFrontBillingInfoPage.enterNewBillingNameOnCard(firstName);
	     storeFrontBillingInfoPage.selectNewBillingCardExpirationDate(TestConstants.CARD_EXP_MONTH,TestConstants.CARD_EXP_YEAR);
	     storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
	     storeFrontBillingInfoPage.selectNewBillingCardAddress();
	     storeFrontBillingInfoPage.clickOnSaveBillingProfile();
	     storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
	     storeFrontHomePage.clickAddToBagButtonWithoutFilter();
	     storeFrontHomePage.clickOnCheckoutButton();
	     storeFrontHomePage.clickOnContinueWithoutSponsorLink();
	     storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
	     storeFrontHomePage.clickOnShippingAddressNextStepBtn();
	     storeFrontHomePage.clickOnBillingNextStepBtn();
	     storeFrontHomePage.clickPlaceOrderBtn();
	     s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(), "order is not placed successfully");
	  s_assert.assertAll();
	 }
}