package com.rf.test.website.storeFront.brandRefresh;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstantsRFL;
import com.rf.core.website.constants.dbQueries.DBQueries_RFL;
import com.rf.pages.website.storeFrontBrandRefresh.StoreFrontBrandRefreshConsultantPage;
import com.rf.pages.website.storeFrontBrandRefresh.StoreFrontBrandRefreshHomePage;
import com.rf.pages.website.storeFrontBrandRefresh.StoreFrontBrandRefreshPCUserPage;
import com.rf.test.website.RFBrandRefreshWebsiteBaseTest;

public class UINavigationTest extends RFBrandRefreshWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(UINavigationTest.class.getName());

	private StoreFrontBrandRefreshHomePage storeFrontBrandRefreshHomePage;
	private String RFL_DB = null;

	public UINavigationTest() {
		storeFrontBrandRefreshHomePage = new StoreFrontBrandRefreshHomePage(driver);		
	}

	//Products-Redefine-Regimen-Links should be redirecting to the appropriate page
	@Test
	public void testProductsRedefineRegimenLinksRedirectingToTheAppropriatePage(){
		String subLinkRegimen = TestConstantsRFL.REGIMEN_NAME_REDEFINE;
		String subLinkProducts = "Products";
		String subLinkResults = "Results";
		String subLinkFAQ = "FAQs";
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickLink(subLinkRegimen);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains(subLinkRegimen.toLowerCase()), "Expected regimen name is "+subLinkRegimen.toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains(subLinkProducts.toLowerCase()), "Expected sublink in url is "+subLinkProducts.toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickSubLink(subLinkRegimen, subLinkResults);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains(subLinkResults.toLowerCase()), "Expected sublink in url is "+subLinkResults.toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserIsRedirectedToResultsPage(subLinkRegimen),"user is not on results page");
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickSubLink(subLinkRegimen,subLinkFAQ);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("CommonQuestions".toLowerCase()), "Expected sublink in url is "+"CommonQuestions".toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserIsRedirectedToFAQsPage(subLinkRegimen),"user is not on FAQs page");
		s_assert.assertAll();
	}

	//Reverse Products-links should be working properly (products, testimonials, in the news, FAQ's, advice, glossary)
	@Test
	public void testReverseProductsLinksWorkingProperly(){
		String subLinkRegimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
		String subLinkProducts = "Products";
		String subLinkFAQ = "FAQs";
		String subLinkResults = "Results";
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickLink(subLinkRegimen);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains(subLinkRegimen.toLowerCase()), "Expected regimen name is "+subLinkRegimen.toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains(subLinkProducts.toLowerCase()), "Expected sublink in url is "+subLinkProducts.toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickSubLink(subLinkRegimen,subLinkFAQ);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("CommonQuestions".toLowerCase()), "Expected sublink in url is "+"CommonQuestions".toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserIsRedirectedToFAQsPage(subLinkRegimen),"user is not on FAQs page");
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickSubLink(subLinkRegimen, subLinkResults);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains(subLinkResults.toLowerCase()), "Expected sublink in url is "+subLinkResults.toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserIsRedirectedToResultsPage(subLinkRegimen),"user is not on results page");
		s_assert.assertAll();
	}

	//Unblemish Products-links should be working properly (products, testimonials, in the news, FAQ's, advice, glossary)
	@Test
	public void testUnblemishProductsLinksWorkingProperly(){
		String subLinkRegimen = TestConstantsRFL.REGIMEN_NAME_UNBLEMISH;
		String subLinkProducts = "Products";
		String subLinkFAQ = "FAQs";
		String subLinkResults = "Results";
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickLink(subLinkRegimen);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains(subLinkRegimen.toLowerCase()), "Expected regimen name is "+subLinkRegimen.toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains(subLinkProducts.toLowerCase()), "Expected sublink in url is "+subLinkProducts.toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickSubLink(subLinkRegimen,subLinkFAQ);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("CommonQuestions".toLowerCase()), "Expected sublink in url is "+"CommonQuestions".toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserIsRedirectedToFAQsPage(subLinkRegimen),"user is not on FAQs page");
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickSubLink(subLinkRegimen, subLinkResults);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains(subLinkResults.toLowerCase()), "Expected sublink in url is "+subLinkResults.toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserIsRedirectedToResultsPage(subLinkRegimen),"user is not on results page");
		s_assert.assertAll();
	}

	//Soothe Products-links should be working properly (products, testimonials, in the news, FAQ's, advice, glossary)
	@Test
	public void testSootheProductsLinksWorkingProperly(){
		String subLinkRegimen = TestConstantsRFL.REGIMEN_NAME_SOOTHE;
		String subLinkProducts = "Products";
		String subLinkFAQ = "FAQs";
		String subLinkResults = "Results";
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickLink(subLinkRegimen);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains(subLinkRegimen.toLowerCase()), "Expected regimen name is "+subLinkRegimen.toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains(subLinkProducts.toLowerCase()), "Expected sublink in url is "+subLinkProducts.toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickSubLink(subLinkRegimen,subLinkFAQ);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("CommonQuestions".toLowerCase()), "Expected sublink in url is "+"CommonQuestions".toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserIsRedirectedToFAQsPage(subLinkRegimen),"user is not on FAQs page");
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickSubLink(subLinkRegimen, subLinkResults);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains(subLinkResults.toLowerCase()), "Expected sublink in url is "+subLinkResults.toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserIsRedirectedToResultsPage(subLinkRegimen),"user is not on results page");
		s_assert.assertAll();
	}

	//product philosophy link should be working
	@Test(enabled=false)//needs updation
	public void testProductsPhilosophyLinkShouldWorkingProper(){
		storeFrontBrandRefreshHomePage.clickShopSkinCareBtn();
		//verify Product Philosophy link working?
		storeFrontBrandRefreshHomePage.clickProductPhilosophyLink();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateProductPhilosohyPageDisplayed(),"Product Philosophy page is not displayed!!");
		s_assert.assertAll();
	}

	//Digital Product Catalog- Links should be displayed the information properly
	@Test(enabled=false)//needs updation
	public void testDigitalProductCatalogLinkShouldDisplayInformationProperly(){
		storeFrontBrandRefreshHomePage.clickShopSkinCareBtn();
		//verify Digital Product Catalog- Link should be displayed the information properly?
		storeFrontBrandRefreshHomePage.clickDigitalProductCatalogLink();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateRealResultsLink(),"Real Results link didn't work");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateSolutionToolLink(),"Solution Tool link didn't work");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validatePCPerksLink(),"PC Perks link didn't work");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateDigitalProductCatalogLink(),"Digital Product link didn't work");
		s_assert.assertAll();
	}

	//Company Links Should be Present
	@Test(enabled=false)//needs updation
	public void testCompanyLinksShouldBePresent(){
		storeFrontBrandRefreshHomePage.clickAboutRFBtn();
		//verify company links is present?
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateExecutiveTeamLinkPresent(),"Executive Team Link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateContactUsLinkPresent(),"Contact Us Link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateWhoWeAreLinkPresent(),"WhoWeAre/Our History Link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validatePressRoomLinkPresent(),"Press Room Link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateCareersLinkPresent(),"Careers Link is not present");
		s_assert.assertAll();
	}

	//Footer- Privacy Policy link should be redirecting to the appropriate page
	@Test
	public void testFooterPrivacyPolicyLinkShouldRedirectionToAppropriatePage(){
		storeFrontBrandRefreshHomePage.clickPrivacyPolicyLink();
		/*s_assert.assertTrue(storeFrontBrandRefreshHomePage.isPrivacyPolicyPagePresent(), "Privacy policy page is not present after clicked on privacy policy link");*/
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("privacy"), "Expected url having privacy but actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertAll();
	}

	//Footer-Terms & Conditions link should redirecting to the appropriate page
	@Test
	public void testFooterTermsAndConditionLinkShouldRedirectionToAppropriatePage(){
		storeFrontBrandRefreshHomePage.clickTermsAndConditionsLink();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("terms"), "Expected url having terms but actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertAll();
	}

	//Satisfaction Guarantee-link should be redirecting properly 
	@Test
	public void testSatisfactionGuaranteeLinkShouldBeRedirectionProperly(){
		storeFrontBrandRefreshHomePage.clickSatisfactionGuaranteeLink();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isSatisfactionGuaranteePagePresent(), "Satisfaction guarantee page is not present after clicked on privacy policy link");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("guarantee"), "Expected url having guarantee but actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertAll();
	}

	//Real results products- links should be redirecting to the appropriate page
	@Test
	public void testProductsLinkShouldBeRedirectionToAppropriatePage(){
		//For REDEFINE
		String regimen = TestConstantsRFL.REGIMEN_NAME_REDEFINE;
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickLink(regimen);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.isRegimenNamePresentAfterClickedOnRegimen(regimen), "regimen name i.e.: "+regimen+" not present");
		//For REVERSE
		regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickLink(regimen);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.isRegimenNamePresentAfterClickedOnRegimen(regimen), "regimen name i.e.: "+regimen+" not present");
		//For UNBLEMISH

		regimen = TestConstantsRFL.REGIMEN_NAME_UNBLEMISH;
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickLink(regimen);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.isRegimenNamePresentAfterClickedOnRegimen(regimen), "regimen name i.e.: "+regimen+" not present");
		//For SOOTHE

		regimen = TestConstantsRFL.REGIMEN_NAME_SOOTHE;
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickLink(regimen);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.isRegimenNamePresentAfterClickedOnRegimen(regimen), "regimen name i.e.: "+regimen+" not present");
		//For PROMOTIONS

		regimen = TestConstantsRFL.REGIMEN_NAME_PROMOTIONS;
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickLink(TestConstantsRFL.REGIMEN_NAME_FEATURED);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.isRegimenNamePresentAfterClickedOnRegimen(regimen), "regimen name i.e.: "+regimen+" not present");
		//For ENHANCEMENTS

		regimen = TestConstantsRFL.REGIMEN_NAME_ENHANCEMENTS;
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickLink(regimen);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.isRegimenNamePresentAfterClickedOnRegimen(regimen), "regimen name i.e.: "+regimen+" not present");
		//For ESSENTIALS

		regimen = TestConstantsRFL.REGIMEN_NAME_ESSENTIALS;
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickLink(regimen);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.isRegimenNamePresentAfterClickedOnRegimen(regimen), "regimen name i.e.: "+regimen+" not present");
		s_assert.assertAll();
	}

	//Log in as an existen consultant
	@Test
	public void testLoginAsExistingConsultant(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontBrandRefreshHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserSuccessfullyLoggedIn(),"consultant is not logged in successfully");
		s_assert.assertAll();
	}

	//Log in as valid PC customer
	@Test
	public void testLoginAsExistingPC(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomPCList =  null;
		String pcEmailID = null;

		while(true){
			randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_EMAILID,RFL_DB);
			pcEmailID = (String) getValueFromQueryResult(randomPCList, "EmailAddress");
			storeFrontBrandRefreshHomePage.loginAsPCUser(pcEmailID,password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}

		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserSuccessfullyLoggedIn(),"PC user is not logged in successfully");
		s_assert.assertAll();
	}

	//Log in with a valid RC customer
	@Test
	public void testLoginAsExistingRC(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomRCList =  null;
		String rcEmailID = null;
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_RC_EMAILID,RFL_DB);
			rcEmailID = (String) getValueFromQueryResult(randomRCList, "EmailAddress");
			storeFrontBrandRefreshHomePage.loginAsPCUser(rcEmailID,password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+rcEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserSuccessfullyLoggedIn(),"RC user is not logged in successfully");
		s_assert.assertAll();
	}

	//Solution Tool-Find a Rodan  + Fields consultant should be working properly
	@Test(enabled=false)
	public void testVerifyUserIsRedirectedToPwsAfterSelectingSponser(){
		String sponsorID = TestConstantsRFL.CID_CONSULTANT;
		String fetchedPWS = null;

		storeFrontBrandRefreshHomePage.clickHomeTabBtn();
		storeFrontBrandRefreshHomePage.clickSolutionToolImageLink();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySolutionToolPage(),"Solution tool page is displayed");
		storeFrontBrandRefreshHomePage.clickFindRodanFieldConsultantLink();
		storeFrontBrandRefreshHomePage.enterIDNumberAsSponsorForPCAndRC(sponsorID);
		storeFrontBrandRefreshHomePage.clickBeginSearchBtn();
		fetchedPWS = storeFrontBrandRefreshHomePage.getPWSFromFindConsultantPage();
		storeFrontBrandRefreshHomePage.selectSponsorRadioBtnOnFindConsultantPage();
		storeFrontBrandRefreshHomePage.clickSelectAndContinueBtnForPCAndRC();
		s_assert.assertFalse(storeFrontBrandRefreshHomePage.getCurrentURL().contains(fetchedPWS),"Expected pws is: "+fetchedPWS +"While actual on UI: "+storeFrontBrandRefreshHomePage.getCurrentURL());
		s_assert.assertAll(); 
	}

	//Redefine-Sub links should be displayed properly(regimen, products, results, testimonials, in the news, FAQs, Advice, Glossary)
	@Test
	public void testVerifyRedefineRegimenLinksDisplayedProperly(){
		String regimen = TestConstantsRFL.REGIMEN_NAME_REDEFINE;
		String subSectionRegimen = "Regimen";
		String subSectionProducts = "Products";
		String subSectionResults = "Results";
		//String subSectionTestimonials = "Testimonials";
		//String subSectionInTheNews = "In the News";
		String subSectionFAQ = "FAQs";
		//String subSectionAdvice = "Advice";
		//String subSectionGlossary = "Glossary";


		//storeFrontBrandRefreshHomePage.clickShopSkinCareBtn();
		//storeFrontBrandRefreshHomePage.selectRegimen(regimen);
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCare();
		//Verify visibility of redefine regimen Sections.
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyRedefineRegimenSections(subSectionRegimen),"Redefine regimen section regimen is not displayed");
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyRedefineRegimenSections(subSectionProducts),"Redefine regimen section Products is not displayed");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyRedefineRegimenSections(subSectionResults),"Redefine regimen section Results is not displayed");
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyRedefineRegimenSections(subSectionTestimonials),"Redefine regimen section testimonials is not displayed");
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyRedefineRegimenSections(subSectionInTheNews),"Redefine regimen section In the news is not displayed");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyRedefineRegimenSections(subSectionFAQ),"Redefine regimen section FAQ is not displayed");
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyRedefineRegimenSections(subSectionAdvice),"Redefine regimen section Advice is not displayed");
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyRedefineRegimenSections(subSectionGlossary),"Redefine regimen section Glossary is not displayed");

		s_assert.assertAll();
	}

	//log out with a valid user
	@Test
	public void testLogoutWithAValidUser(){
		RFL_DB = driver.getDBNameRFL();
		String whyRF = "Why R+F";
		//  String programsAndIncentives = "Programs and Incentives";
		//  String incomeIllustrator = "Income Illustrator";
		//  String events = "Events";
		//  String meetOurCommunity = "Meet Our Community";
		//  String enrollNow = "Enroll Now";
		//  String gettingStarted = "Getting Started";
		//  String businessKits = "Business Kits";
		//  String redefineYourFuture = "Redefine Your Future";
		String consultantEmailID = null;

		List<Map<String, Object>> randomConsultantList =  null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
			storeFrontBrandRefreshHomePage.loginAsConsultant(consultantEmailID,password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserSuccessfullyLoggedIn(),"consultant is not logged in successfully");
		logout();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isForgotPasswordLinkPresent(),"User is not logout successfully");
		//  driver.getURL();
		//  storeFrontBrandRefreshHomePage.clickBeAConsultantBtn();
		//  s_assert.assertTrue(storeFrontBrandRefreshHomePage.isSublinkOfBusinessSystemPresent(whyRF), "Why R+F link is not present under business system");
		//  s_assert.assertTrue(storeFrontBrandRefreshHomePage.isSublinkOfBusinessSystemPresent(programsAndIncentives), "Programs And Incentives link is not present under business system");
		//  s_assert.assertTrue(storeFrontBrandRefreshHomePage.isSublinkOfBusinessSystemPresent(incomeIllustrator), "Income Illustrator link is not present under business system");
		//  s_assert.assertTrue(storeFrontBrandRefreshHomePage.isSublinkOfBusinessSystemPresent(events), "Events link is not present under business system");
		//  s_assert.assertTrue(storeFrontBrandRefreshHomePage.isSublinkOfBusinessSystemPresent(meetOurCommunity), "Meet Our Community link is not present under business system");
		//  s_assert.assertTrue(storeFrontBrandRefreshHomePage.isSublinkOfBusinessSystemPresent(enrollNow), "enroll Now link is not present under business system");
		//  storeFrontBrandRefreshHomePage.clickSublinkOfBusinessSystem(whyRF);
		//  s_assert.assertTrue(storeFrontBrandRefreshHomePage.isSublinkOfBusinessSystemPresent(gettingStarted), "Getting Started link is not present under Why R+F for business system");
		//  s_assert.assertTrue(storeFrontBrandRefreshHomePage.isSublinkOfBusinessSystemPresent(businessKits), "Business Kits link is not present under Why R+F for business system");
		//  s_assert.assertTrue(storeFrontBrandRefreshHomePage.isSublinkOfBusinessSystemPresent(redefineYourFuture), "Redefine Your Future link is not present under Why R+F for business system");
		//  s_assert.assertTrue(storeFrontBrandRefreshHomePage.isSublinkOfBusinessSystemPresent(enrollNow), "Enroll Now link is not present under Why R+F for business system");
		//  s_assert.assertTrue(storeFrontBrandRefreshHomePage.getSelectedHighlightLinkName().contains(gettingStarted), "Expected selected and highlight link name is: "+gettingStarted+" Actual on UI: "+storeFrontBrandRefreshHomePage.getSelectedHighlightLinkName());
		//  storeFrontBrandRefreshHomePage.clickSublinkOfBusinessSystem(businessKits);
		//  s_assert.assertTrue(storeFrontBrandRefreshHomePage.getSelectedHighlightLinkName().contains(businessKits), "Expected selected and highlight link name is: "+businessKits+" Actual on UI: "+storeFrontBrandRefreshHomePage.getSelectedHighlightLinkName());
		//  s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("business/kits"), "Expected url contains is: business/kits but Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		//  storeFrontBrandRefreshHomePage.clickSublinkOfBusinessSystem(gettingStarted);
		//  storeFrontBrandRefreshHomePage.clickClickhereLink();
		//  s_assert.assertTrue(storeFrontBrandRefreshHomePage.isClickHereLinkRedirectinToAppropriatePage("PP_11th_Edition.pdf"), "Click here link of business system is not redirecting to PP_11th_Edition.pdf page");
		s_assert.assertAll();
	}

	//Corporate_BUsinessSystem_ Direct Selling
	@Test
	public void testCorporateBusinessSystemDirectSelling(){
		storeFrontBrandRefreshHomePage.clickAboutRFBtn();
		/*storeFrontBrandRefreshHomePage.clickClickhereLink();*/
		storeFrontBrandRefreshHomePage.mouseHoverAboutRFAndClickLink("Who We Are");
		storeFrontBrandRefreshHomePage.clickClickhereLinkToLearnDirectSelling();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isClickHereLinkRedirectinToAppropriatePage("directselling.org"), "Click here link of business system is not redirecting to http://directselling.org/ page");
		s_assert.assertAll();
	}

	//Corporate_BUsinessSystem_GettingStarted 
	@Test(enabled=false)//needs updation
	public void testCorporateBusinessSystemGettingStarted(){
		String gettingStarted = "Getting Started";
		String whyRF = "Why R+F";
		String redefineYourFuture = "Redefine Your Future";
		String enrollNow = "Enroll Now";
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int ssnRandomNum1 = CommonUtils.getRandomNum(100, 999);
		int ssnRandomNum2 = CommonUtils.getRandomNum(00, 99);
		int ssnRandomNum3 = CommonUtils.getRandomNum(1000, 9999);
		String firstName = TestConstantsRFL.FIRST_NAME;
		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
		String emailAddress = firstName+randomNum+"@xyz.com";
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = firstName;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String CID = TestConstantsRFL.CID_CONSULTANT;
		String kitName = "Big Business Launch Kit";
		String regimen = "Redefine";
		String enrollemntType = "Express";
		String phnNumber1 = "415";
		String phnNumber2 = "780";
		String phnNumber3 = "9099";

		storeFrontBrandRefreshHomePage.clickBeAConsultantBtn();
		storeFrontBrandRefreshHomePage.clickSublinkOfBusinessSystem(whyRF);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isSublinkOfBusinessSystemPresent(gettingStarted), "Getting Started link is not present under Why R+F for business system");
		storeFrontBrandRefreshHomePage.clickSublinkOfBusinessSystem(gettingStarted);
		storeFrontBrandRefreshHomePage.clickClickhereLink();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isClickHereLinkRedirectinToAppropriatePage("PP_11th_Edition.pdf"), "Click here link of business system is not redirecting to PP_11th_Edition.pdf page");
		storeFrontBrandRefreshHomePage.clickSublinkOfBusinessSystem(redefineYourFuture);
		storeFrontBrandRefreshHomePage.clickDetailsLink();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isClickHereLinkRedirectinToAppropriatePage("REDEFINE-Your-Future-with-BBL-020813.pdf"), "Details link of redefine your future is not redirecting to REDEFINE-Your-Future-with-BBL-020813.pdf");
		storeFrontBrandRefreshHomePage.clickSublinkOfBusinessSystem(enrollNow);
		storeFrontBrandRefreshHomePage.enterCID(CID);
		storeFrontBrandRefreshHomePage.clickSearchResults();
		storeFrontBrandRefreshHomePage.selectEnrollmentKit(kitName);
		storeFrontBrandRefreshHomePage.selectRegimenAndClickNext(regimen);
		storeFrontBrandRefreshHomePage.selectEnrollmentType(enrollemntType);
		storeFrontBrandRefreshHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontBrandRefreshHomePage.clickSetUpAccountNextBtn();
		storeFrontBrandRefreshHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear);
		storeFrontBrandRefreshHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		storeFrontBrandRefreshHomePage.clickCompleteAccountNextBtn();
		storeFrontBrandRefreshHomePage.clickTermsAndConditions();
		storeFrontBrandRefreshHomePage.chargeMyCardAndEnrollMe();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isCongratulationsMessageAppeared(),"Congratulations Message not appeared");
		s_assert.assertAll();
	}

	//Corporate_BUsinessSystem_ SuccessStories
	@Test(enabled=false)//No more visible on UI
	public void testCorporateBusinessSystemSuccessStories(){
		String meetOurCommunity = "Meet Our Community";
		String rfxCircleAG = "RFXcircleAG";
		String rfxCircleHZ = "RFXcircleHZ";
		String eliteVAL = "EliteVAL";
		String eliteVMZ = "EliteVMZ";
		String carAchieversAC= "CarAchieversAC";
		String carAchieversDE = "CarAchieversDE";
		String carAchieversFG = "CarAchieversFG";
		String carAchieversHL = "CarAchieversHL";
		String carAchieversMR = "CarAchieversMR";
		String carAchieversSZ = "CarAchieversSZ";

		//storeFrontBrandRefreshHomePage.clickBeAConsultantBtn();
		//storeFrontBrandRefreshHomePage.clickSublinkOfBusinessSystem(meetOurCommunity);
		storeFrontBrandRefreshHomePage.mouseHoverBeAConsultantAndClickLink(meetOurCommunity);
		storeFrontBrandRefreshHomePage.clickSublinkOfBusinessSystem(rfxCircleAG);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().contains("RFXcircleAG"), "Expected url contains is: RFxcircleAG but Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL());
		storeFrontBrandRefreshHomePage.navigateToBackPage();
		storeFrontBrandRefreshHomePage.clickSublinkOfBusinessSystem(rfxCircleHZ);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().contains("RFXcircleHZ"), "Expected url contains is: RFxcircleHZ but Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL());
		storeFrontBrandRefreshHomePage.navigateToBackPage();
		storeFrontBrandRefreshHomePage.clickSublinkOfBusinessSystem(eliteVAL);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().contains("EliteVAL"), "Expected url contains is: EliteVAL but Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL());
		storeFrontBrandRefreshHomePage.navigateToBackPage();
		storeFrontBrandRefreshHomePage.clickSublinkOfBusinessSystem(eliteVMZ);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().contains("EliteVMZ"), "Expected url contains is: EliteVMZ but Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL());
		storeFrontBrandRefreshHomePage.navigateToBackPage();
		storeFrontBrandRefreshHomePage.clickSublinkOfBusinessSystem(carAchieversAC);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().contains("CarAchieversAC"), "Expected url contains is: CarAchieversAC but Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL());
		storeFrontBrandRefreshHomePage.navigateToBackPage();
		storeFrontBrandRefreshHomePage.clickSublinkOfBusinessSystem(carAchieversDE);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().contains("CarAchieversDE"), "Expected url contains is: CarAchieversDE but Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL());
		storeFrontBrandRefreshHomePage.navigateToBackPage();
		storeFrontBrandRefreshHomePage.clickSublinkOfBusinessSystem(carAchieversFG);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().contains("CarAchieversFG"), "Expected url contains is: CarAchieversFG but Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL());
		storeFrontBrandRefreshHomePage.navigateToBackPage();
		storeFrontBrandRefreshHomePage.clickSublinkOfBusinessSystem(carAchieversHL);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().contains("CarAchieversHL"), "Expected url contains is: CarAchieversHL but Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL());
		storeFrontBrandRefreshHomePage.navigateToBackPage();
		storeFrontBrandRefreshHomePage.clickSublinkOfBusinessSystem(carAchieversMR);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().contains("CarAchieversMR"), "Expected url contains is: CarAchieversMR but Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL());
		storeFrontBrandRefreshHomePage.navigateToBackPage();
		storeFrontBrandRefreshHomePage.clickSublinkOfBusinessSystem(carAchieversSZ);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().contains("CarAchieversSZ"), "Expected url contains is: CarAchieversSZ but Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL());
		s_assert.assertAll();
	}

	//The Compensation Plan section Compensation Plan is displayed
	@Test//(enabled=false)//needs updation
	public void testCompensationPlanSectionIsDisplayed(){
		String firstSubSectionUnderBusinessSystem = "Why R+F";
		String secondSubSectionUnderBusinessSystem = "Programs and Incentives";
		//String thirdSubSectionUnderBusinessSystem = "Income Illustrator";
		String fourthSubSectionUnderBusinessSystem = "Events";
		String fifthSubSectionUnderBusinessSystem = "Meet Our Community";
		String sixthSubSectionUnderBusinessSystem = "Enroll Now";

		String firstSubSectionUnderProgramsAndIncentives = "Compensation Plan";
		String secondSubSectionUnderProgramsAndIncentives = "Programs & Incentives";
		String thirdSubSectionUnderProgramsAndIncentives = "Enroll Now";

		storeFrontBrandRefreshHomePage.mouseHoverBeAConsultant();
		//storeFrontBrandRefreshHomePage.clickBeAConsultantBtn();
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("Business".toLowerCase()), "URL does not contain Business Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtBusinessSystemPage(firstSubSectionUnderBusinessSystem),""+firstSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtBusinessSystemPage(secondSubSectionUnderBusinessSystem),""+secondSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtBusinessSystemPage(thirdSubSectionUnderBusinessSystem),""+thirdSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtBusinessSystemPage(fourthSubSectionUnderBusinessSystem),""+fourthSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtBusinessSystemPage(fifthSubSectionUnderBusinessSystem),""+fifthSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtBusinessSystemPage(sixthSubSectionUnderBusinessSystem),""+sixthSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		storeFrontBrandRefreshHomePage.clickSubSectionUnderBusinessSystem(secondSubSectionUnderBusinessSystem);
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.getSelectedHighlightLinkName().contains("Programs and Incentives"), "Expected selected and highlight link name is: "+"Programs and Incentives but Actual on UI: "+storeFrontBrandRefreshHomePage.getSelectedHighlightLinkName());
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtProgramsAndIncentives(firstSubSectionUnderProgramsAndIncentives),""+firstSubSectionUnderProgramsAndIncentives+" subTitle not present under Programs and Incentives page");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtProgramsAndIncentives(secondSubSectionUnderProgramsAndIncentives),""+secondSubSectionUnderProgramsAndIncentives+" subTitle not present under Programs and Incentives page");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtProgramsAndIncentives(thirdSubSectionUnderProgramsAndIncentives),""+thirdSubSectionUnderProgramsAndIncentives+" subTitle not present under Programs and Incentives page");
		storeFrontBrandRefreshHomePage.clickToReadIncomeDisclosure();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentUrlOpenedWindow().contains("RF-Income-Disclosure-Statement.pdf"),"current url is not a valid and Expected url");
		s_assert.assertAll();
	}

	//The Compensation Plan section Programs and Incentives is displayed
	@Test//(enabled=false)//needs updation
	public void testCompensationPlanProgramsAndIncentivesIsDisplayed(){
		String firstSubSectionUnderBusinessSystem = "Why R+F";
		String secondSubSectionUnderBusinessSystem = "Programs and Incentives";
		//String thirdSubSectionUnderBusinessSystem = "Income Illustrator";
		String fourthSubSectionUnderBusinessSystem = "Events";
		String fifthSubSectionUnderBusinessSystem = "Meet Our Community";
		String sixthSubSectionUnderBusinessSystem = "Enroll Now";

		//String firstSubSectionUnderProgramsAndIncentives = "Compensation Plan";
		String secondSubSectionUnderProgramsAndIncentives = "Programs & Incentives";
		String thirdSubSectionUnderProgramsAndIncentives = "Enroll Now";

		//storeFrontBrandRefreshHomePage.clickBeAConsultantBtn();
		storeFrontBrandRefreshHomePage.mouseHoverBeAConsultant();
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("Business".toLowerCase()), "URL does not contain Business Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtBusinessSystemPage(firstSubSectionUnderBusinessSystem),""+firstSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtBusinessSystemPage(secondSubSectionUnderBusinessSystem),""+secondSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtBusinessSystemPage(thirdSubSectionUnderBusinessSystem),""+thirdSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtBusinessSystemPage(fourthSubSectionUnderBusinessSystem),""+fourthSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtBusinessSystemPage(fifthSubSectionUnderBusinessSystem),""+fifthSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtBusinessSystemPage(sixthSubSectionUnderBusinessSystem),""+sixthSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		storeFrontBrandRefreshHomePage.clickSubSectionUnderBusinessSystem(secondSubSectionUnderBusinessSystem);
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtProgramsAndIncentives(firstSubSectionUnderProgramsAndIncentives),""+firstSubSectionUnderProgramsAndIncentives+" subTitle not present under Programs and Incentives page");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtProgramsAndIncentives(secondSubSectionUnderProgramsAndIncentives),""+secondSubSectionUnderProgramsAndIncentives+" subTitle not present under Programs and Incentives page");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtProgramsAndIncentives(thirdSubSectionUnderProgramsAndIncentives),""+thirdSubSectionUnderProgramsAndIncentives+" subTitle not present under Programs and Incentives page");
		s_assert.assertAll();
	}

	//The Compensation Plan section Enroll Now is displayed
	@Test(enabled=false)//needs updation
	public void testCompensationPlanSectionEnrollNowIsDisplayed(){
		String firstSubSectionUnderBusinessSystem = "Why R+F";
		String secondSubSectionUnderBusinessSystem = "Programs and Incentives";
		String thirdSubSectionUnderBusinessSystem = "Income Illustrator";
		String fourthSubSectionUnderBusinessSystem = "Events";
		String fifthSubSectionUnderBusinessSystem = "Meet Our Community";
		String sixthSubSectionUnderBusinessSystem = "Enroll Now";
		String firstSubSectionUnderProgramsAndIncentives = "Compensation Plan";
		String secondSubSectionUnderProgramsAndIncentives = "Programs and Incentives";
		String thirdSubSectionUnderProgramsAndIncentives = "Enroll Now";
		storeFrontBrandRefreshHomePage.clickBeAConsultantBtn();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("Business".toLowerCase()), "URL does not contain Business Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtBusinessSystemPage(firstSubSectionUnderBusinessSystem),""+firstSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtBusinessSystemPage(secondSubSectionUnderBusinessSystem),""+secondSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtBusinessSystemPage(thirdSubSectionUnderBusinessSystem),""+thirdSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtBusinessSystemPage(fourthSubSectionUnderBusinessSystem),""+thirdSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtBusinessSystemPage(fifthSubSectionUnderBusinessSystem),""+fifthSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtBusinessSystemPage(sixthSubSectionUnderBusinessSystem),""+sixthSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		storeFrontBrandRefreshHomePage.clickSubSectionUnderBusinessSystem(secondSubSectionUnderBusinessSystem);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtProgramsAndIncentives(firstSubSectionUnderProgramsAndIncentives),""+firstSubSectionUnderProgramsAndIncentives+" subTitle not present under Programs and Incentives page");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtProgramsAndIncentives(secondSubSectionUnderProgramsAndIncentives),""+secondSubSectionUnderProgramsAndIncentives+" subTitle not present under Programs and Incentives page");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifySubSectionPresentAtProgramsAndIncentives(thirdSubSectionUnderProgramsAndIncentives),""+thirdSubSectionUnderProgramsAndIncentives+" subTitle not present under Programs and Incentives page");
		storeFrontBrandRefreshHomePage.clickSubSectionUnderBusinessSystem(thirdSubSectionUnderProgramsAndIncentives);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().contains("NewEnrollment/SearchSponsor"),"current url is not a valid and Expected url");
		s_assert.assertAll();
	}

	//Contact us-link should be redirecting properly
	@Test
	public void testContactUsLinkShouldBeRedirectingProperly(){
		storeFrontBrandRefreshHomePage.clickContactUsAtFooter();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("Contact".toLowerCase()), "URL does not contain Contact Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifylinkIsRedirectedToContactUsPage(),"link is not redirected to contact us page");
		s_assert.assertAll();
	}

	//Essentials Products-links should be working properly (Real results, PC perks, solution tool, digital product catalog)
	@Test(enabled=false)//needs updation
	public void testVerifyEssentialRegimenProductLinksWorkingProperly(){
		String regimen = TestConstantsRFL.REGIMEN_NAME_ESSENTIALS;
		String subSectionLinkRealResults = "Real Results";
		String subSectionLinkPCPerks = "PC PERKS";
		String subSectionSolutionTool = "Solution Tool";
		String subSectionDigitalProductCatalogue = "Digital Product Catalog";
		storeFrontBrandRefreshHomePage.clickShopSkinCareBtn();
		storeFrontBrandRefreshHomePage.selectRegimen(regimen);

		//Verify visibility of Essentials regimen Sections.
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyEssentialsRegimenSections(subSectionLinkRealResults),"Essential regimen section real results is not displayed");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyEssentialsRegimenSections(subSectionLinkPCPerks),"Essential regimen section PC Perks is not displayed");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyEssentialsRegimenSections(subSectionSolutionTool),"Essential regimen section Solution tool is not displayed");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyEssentialsRegimenSections(subSectionDigitalProductCatalogue),"Essential regimen section Digital product catalogue is not displayed");

		//Verify Visibility of Essentials Regimen Real Results subSections.	
		storeFrontBrandRefreshHomePage.clickSubSectionUnderRegimen(subSectionLinkRealResults);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("Results".toLowerCase()), "Expected regimen name is "+"Results".toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserIsRedirectedToRealResultsPage(),"user is not on Real results page");
		storeFrontBrandRefreshHomePage.navigateToBackPage();

		//Verify Visibility of Essentials Regimen PC Perks subSections.		
		storeFrontBrandRefreshHomePage.clickSubSectionUnderRegimen(subSectionLinkPCPerks);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("PC".toLowerCase()), "Expected regimen name is "+"PCPerks".toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());		
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserIsRedirectedToPCPerksPage(),"user is not on PC Perks page");
		storeFrontBrandRefreshHomePage.navigateToBackPage();

		//Verify Visibility of Essentials Regimen Solution tool subSections.	
		storeFrontBrandRefreshHomePage.clickSubSectionUnderRegimen(subSectionSolutionTool);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("Solution".toLowerCase()), "Expected regimen name is "+"SolutionTool".toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserIsRedirectedToSolutionToolPage(),"user is not on Solution tool page");
		storeFrontBrandRefreshHomePage.navigateToBackPage();

		//Verify Visibility of Essentials Regimen Digital product catalogue subSections.	
		storeFrontBrandRefreshHomePage.clickSubSectionUnderRegimen(subSectionDigitalProductCatalogue);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("Digimag".toLowerCase()), "Expected regimen name is "+"Digimag".toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserIsRedirectedToDigitalProductCataloguePage(),"user is not on Digital product catalogue page");
		storeFrontBrandRefreshHomePage.navigateToBackPage();
		s_assert.assertAll();
	}

	//Enhancements Products-links should be working properly (Real results, PC perks, solution tool, digital product catalog)
	@Test(enabled=false)//needs updation
	public void testVerifyEnhancementsRegimenProductLinksWorkingProperly(){
		String regimen = TestConstantsRFL.REGIMEN_NAME_ENHANCEMENTS;
		String subSectionLinkRealResults = "Real Results";
		String subSectionLinkPCPerks = "PC PERKS";
		String subSectionSolutionTool = "Solution Tool";
		String subSectionDigitalProductCatalogue = "Digital Product Catalog";


		storeFrontBrandRefreshHomePage.clickShopSkinCareBtn();
		storeFrontBrandRefreshHomePage.selectRegimen(regimen);

		//Verify visibility of Essentials regimen Sections.
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyEnhancementsRegimenSections(subSectionLinkRealResults),"Enhancements regimen section real results is not displayed");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyEnhancementsRegimenSections(subSectionLinkPCPerks),"Enhancements regimen section PC Perks is not displayed");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyEnhancementsRegimenSections(subSectionSolutionTool),"Enhancements regimen section Solution tool is not displayed");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyEnhancementsRegimenSections(subSectionDigitalProductCatalogue),"Enhancements regimen section Digital product catalogue is not displayed");

		//Verify Visibility of Essentials Regimen Real Results subSections.	
		storeFrontBrandRefreshHomePage.clickSubSectionUnderRegimen(subSectionLinkRealResults);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("Results".toLowerCase()), "Expected regimen name is "+"Results".toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserIsRedirectedToRealResultsPage(),"user is not on Real results page");
		storeFrontBrandRefreshHomePage.navigateToBackPage();

		//Verify Visibility of Essentials Regimen PC Perks subSections.		
		storeFrontBrandRefreshHomePage.clickSubSectionUnderRegimen(subSectionLinkPCPerks);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("PC".toLowerCase()), "Expected regimen name is "+"PCPerks".toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());		
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserIsRedirectedToPCPerksPage(),"user is not on PC Perks page");
		storeFrontBrandRefreshHomePage.navigateToBackPage();

		//Verify Visibility of Essentials Regimen Solution tool subSections.	
		storeFrontBrandRefreshHomePage.clickSubSectionUnderRegimen(subSectionSolutionTool);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("Solution".toLowerCase()), "Expected regimen name is "+"SolutionTool".toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserIsRedirectedToSolutionToolPage(),"user is not on Solution tool page");
		storeFrontBrandRefreshHomePage.navigateToBackPage();

		//Verify Visibility of Essentials Regimen Digital product catalogue subSections.	
		storeFrontBrandRefreshHomePage.clickSubSectionUnderRegimen(subSectionDigitalProductCatalogue);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("Digimag".toLowerCase()), "Expected regimen name is "+"Digimag".toLowerCase()+" Actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyUserIsRedirectedToDigitalProductCataloguePage(),"user is not on Digital product catalogue page");
		storeFrontBrandRefreshHomePage.navigateToBackPage();
		s_assert.assertAll();
	}

	//Corporate_ FindAConsultant
	@Test(enabled=true)
	public void testCorporateFindAConsultant() throws InterruptedException{
		String expectedURL = "LocatePWS.aspx?fromHome=1";
		storeFrontBrandRefreshHomePage = new StoreFrontBrandRefreshHomePage(driver);
		storeFrontBrandRefreshHomePage.clickConnectWithAConsultant();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentUrlOpenedWindow().contains(expectedURL), "Current url expected is: "+expectedURL+" while actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL());
		s_assert.assertAll();
	}

	//Corporate_BUsinessSystem_CompensationPlan
	@Test(enabled=false)//needs updation
	public void testCorporateBusinessCompensationPlan(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int ssnRandomNum1 = CommonUtils.getRandomNum(100, 999);
		int ssnRandomNum2 = CommonUtils.getRandomNum(00, 99);
		int ssnRandomNum3 = CommonUtils.getRandomNum(1000, 9999);
		String firstName = TestConstantsRFL.FIRST_NAME;
		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
		String emailAddress = firstName+randomNum+"@xyz.com";
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = firstName;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String CID = TestConstantsRFL.CID_CONSULTANT;
		String kitName = "Big Business Launch Kit";
		String regimen = "Redefine";
		String enrollemntType = "Express";
		String phnNumber1 = "415";
		String phnNumber2 = "780";
		String phnNumber3 = "9099";
		String secondSubSectionUnderBusinessSystem = "Programs and Incentives";
		String firstSubSectionUnderProgramsAndIncentives = "Compensation Plan";
		String secondSubSectionUnderProgramsAndIncentives = "Programs and Incentives";

		storeFrontBrandRefreshHomePage.clickBeAConsultantBtn();
		storeFrontBrandRefreshHomePage.clickSubSectionUnderBusinessSystem(secondSubSectionUnderBusinessSystem);
		storeFrontBrandRefreshHomePage.clickSubSectionUnderBusinessSystem(firstSubSectionUnderProgramsAndIncentives);
		//Verify Compensation page
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getSelectedHighlightLinkName().contains("Compensation Plan"), "Expected selected and highlight link name is: "+"Compensation Plan"+" Actual on UI: "+storeFrontBrandRefreshHomePage.getSelectedHighlightLinkName());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("Compensation".toLowerCase()), "Expected url should contain: "+"Compensation"+" Actual on UI: "+storeFrontBrandRefreshHomePage.getCurrentURL());
		storeFrontBrandRefreshHomePage.clickToReadIncomeDisclosure();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentUrlOpenedWindow().contains("RF-Income-Disclosure-Statement.pdf"),"current url is not a valid and Expected url");
		//Click program and incentives link
		storeFrontBrandRefreshHomePage.clickSubSectionUnderBusinessSystem(secondSubSectionUnderProgramsAndIncentives);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().toLowerCase().contains("ProgramsIncentives".toLowerCase()), "Expected url should contain: "+"ProgramsIncentives"+" Actual on UI: "+storeFrontBrandRefreshHomePage.getCurrentURL());
		//click details link under fast start program section
		storeFrontBrandRefreshHomePage.clickDetailsLinkUnderProgramsIncentivePage("Fast Start Program");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentUrlOpenedWindow().contains("Fast_Start_Flyer_2013_Secured.pdf"),"current url is not as expected under detail page of fast start program section page");
		//click details link under Road to RFx car incentive program section
		storeFrontBrandRefreshHomePage.clickDetailsLinkUnderProgramsIncentivePage("Road to RF");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentUrlOpenedWindow().contains("Road-to-RFx-Car-Incentive-Program-Flyer-09.24.11.pdf"),"current url is not as expected under detail page of road to rfx car incentive program section page");
		//click details link under Elite V  program section
		storeFrontBrandRefreshHomePage.clickDetailsLinkUnderProgramsIncentivePage("Elite V Program");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentUrlOpenedWindow().contains("Elite_V_Flyer_Hawaii_2016.pdf"),"current url is not as expected under detail page of Elite V  program section page");
		//click details link under RFx Circle program section
		storeFrontBrandRefreshHomePage.clickDetailsLinkUnderProgramsIncentivePage("RF");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentUrlOpenedWindow().contains("2016_RFx_Circle_TCs.pdf"),"current url is not as expected under detail page of RFx Circle  program section page");
		storeFrontBrandRefreshHomePage.navigateToBackPage();
		storeFrontBrandRefreshHomePage.navigateToBackPage();
		//complete enroll flow.
		storeFrontBrandRefreshHomePage.clickEnrollNowBtnOnBusinessPage();
		storeFrontBrandRefreshHomePage.enterCID(CID);
		storeFrontBrandRefreshHomePage.clickSearchResults();
		storeFrontBrandRefreshHomePage.selectEnrollmentKit(kitName);
		storeFrontBrandRefreshHomePage.selectRegimenAndClickNext(regimen);
		storeFrontBrandRefreshHomePage.selectEnrollmentType(enrollemntType);
		storeFrontBrandRefreshHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontBrandRefreshHomePage.clickSetUpAccountNextBtn();
		storeFrontBrandRefreshHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear);
		storeFrontBrandRefreshHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		storeFrontBrandRefreshHomePage.clickCompleteAccountNextBtn();
		storeFrontBrandRefreshHomePage.clickTermsAndConditions();
		storeFrontBrandRefreshHomePage.chargeMyCardAndEnrollMe();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isCongratulationsMessageAppeared(),"");
		s_assert.assertAll();
	}

	//Corporate_BUsinessSystem_ IncomeIllustrator  
	@Test(enabled=false)//needs updation
	public void corporateBusinessSystemIncomeIllustrator(){
		String incomeIllustrator = "Income Illustrator";

		storeFrontBrandRefreshHomePage.clickBeAConsultantBtn();
		storeFrontBrandRefreshHomePage.clickSublinkOfBusinessSystem(incomeIllustrator);
		storeFrontBrandRefreshHomePage.clickStartNowBtn();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isStartNowBtnRedirectinToAppropriatePage("IncomeIllustrator/index.html"), "start now btn of income illustrator is not redirecting to IncomeIllustrator/index.html");
		s_assert.assertAll();

	}

	//The Getting Started section Redefine Your Future is displayed
	@Test(enabled=false)
	public void testGettingStartedSectionRedefineYourFutureIsDisplayed(){

		storeFrontBrandRefreshHomePage.clickBeAConsultantBtn();
		//verify the sub-menu title under the title business system?
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateEnrollNowLinkPresent(),"Enroll Now Link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateMeetOurCommunityLinkPresent(),"Meet Our Community is not present");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateEventsLinkPresent(),"Events Link is not present");
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateIncomeIllustratorLinkPresent(),"Income Illustrator Link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateProgramsAndIncentivesLinkPresent(),"ProgramIncentives Link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateWhyRFLinkPresent(),"Why RF Link is not present");
		//Navigate to Redefine Your Future section-
		storeFrontBrandRefreshHomePage.clickWhyRFLinkUnderBusinessSystem();
		storeFrontBrandRefreshHomePage.clickRedefineYourFutureLinkUnderWhyRF();
		//verify url for 'Redefine Your Future'
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateRedefineYourFuturePageDisplayed(),"'Redefine Your Future' Page Is not displayed");
		s_assert.assertAll();
	}

	//The Getting Started Section Business Kit is displayed
	@Test(enabled=false)
	public void testGettingStartedSectionBusinessKitDisplayed(){

		storeFrontBrandRefreshHomePage.clickBeAConsultantBtn();
		//verify the sub-menu title under the title business system?
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateEnrollNowLinkPresent(),"Enroll Now Link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateMeetOurCommunityLinkPresent(),"Meet Our Community is not present");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateEventsLinkPresent(),"Events Link is not present");
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateIncomeIllustratorLinkPresent(),"Income Illustrator Link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateProgramsAndIncentivesLinkPresent(),"ProgramIncentives Link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateWhyRFLinkPresent(),"Why RF Link is not present");
		//Navigate to business kit section-
		storeFrontBrandRefreshHomePage.clickWhyRFLinkUnderBusinessSystem();
		storeFrontBrandRefreshHomePage.clickBusinessKitsUnderWhyRF();
		//verify that the 'Business Kits' Section displays the information?
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateBusinessKitSectionIsDisplayed(),"Business Kit Section is not displayed with the Information");
		s_assert.assertAll();
	}

	//Company Contact us Link should be displayed properly
	@Test(enabled=false)
	public void testCompanyContactUsLink(){

		storeFrontBrandRefreshHomePage.clickAboutRFBtn();
		//verify company Contact Us Link?
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateCompanyContactUsLink(),"Company Contact Us link didn't work");
		s_assert.assertAll();
	}

	//Company Press Room Link should be displayed properly
	@Test(enabled=false)
	public void testCompanyPressRoomLink(){

		storeFrontBrandRefreshHomePage.clickAboutRFBtn();
		//verify company Press Room Link?
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateCompanyPressRoomLink(),"Company Press Room link didn't work");
		s_assert.assertAll();
	}

	//Company Careers Link should be displayed properly
	@Test(enabled=false)
	public void testCompanyCareersLink(){

		storeFrontBrandRefreshHomePage.clickAboutRFBtn();
		//verify company careers Link?
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateCompanyCareersLink(),"Company careers link didn't work");
		s_assert.assertAll();
	}

	//Company Our Story Link should be displayed properly
	@Test(enabled=false)
	public void testCompanyOurStoryLink(){

		storeFrontBrandRefreshHomePage.clickAboutRFBtn();
		//verify Our Story Link?
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateWhoWeAreLink(),"Who We Are link didn't work");
		s_assert.assertAll();
	}

	//Company Execute Link should be displayed properly
	@Test(enabled=false)
	public void testCompanyExecuteTeamLink(){

		storeFrontBrandRefreshHomePage.clickAboutRFBtn();
		//verify Execute Team Link?
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateExecuteTeamLink(),"Execute Team link didn't work");
		s_assert.assertAll();
	}

	//The Getting Started section Enroll Now is displayed
	@Test(enabled=false)//needs updation
	public void testGettingStartedSectionEnrollNowIsDisplayed(){

		storeFrontBrandRefreshHomePage.clickBeAConsultantBtn();
		//verify the sub-menu title under the title business system?
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateEnrollNowLinkPresent(),"Enroll Now Link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateMeetOurCommunityLinkPresent(),"Meet Our Link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateEventsLinkPresent(),"Events Link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateIncomeIllustratorLinkPresent(),"Income Illustrator Link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateProgramsAndIncentivesLinkPresent(),"ProgramIncentives Link is not present");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateWhyRFLinkPresent(),"Why RF Link is not present");
		//Navigate to Redefine Your Future section-
		storeFrontBrandRefreshHomePage.clickWhyRFLinkUnderBusinessSystem();
		storeFrontBrandRefreshHomePage.clickEnrollNowLinkUnderWhyRF();
		//verify url for 'Enroll Now'
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateSearchSponsorPageDisplayed(),"'Search Sponsor' Page Is not displayed");
		s_assert.assertAll();
	}

	//Disclaimer-link should be redirecting properly
	@Test
	public void testDisclaimerLinkShouldBeRedirectedProperly(){

		//verify Disclaimer in footer should redirect properly?
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateDisclaimerLinkInFooter(),"'Disclaimer Link' doesn't redirect to disclaimer page");
		s_assert.assertAll();
	}

	//Company-PFC Foundation link should be redirecting properly 
	@Test(enabled=false)//needs updation
	public void testCompanyPFCFoundationLinkShouldRedirectProperly(){
		storeFrontBrandRefreshHomePage.clickAboutRFBtn();
		//verify company PFC Foundation link?
		storeFrontBrandRefreshHomePage.clickGivingBackLinkUnderAboutRF();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateCompanyPFCFoundationPageDisplayed(),"'Company PFC Foundation' page is not displayed");
		s_assert.assertAll();
	}

	//Corporate_ R+FInTheNews
	@Test(enabled=true)
	public void testCorporateRFInTheNews(){
		String expectedURL = "Company/PR";
		//storeFrontBrandRefreshHomePage.mouseHoverAboutRFAndClickLink("Press Room");
		storeFrontBrandRefreshHomePage.clickCompanyPressRoomLink();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getCurrentURL().contains(expectedURL), "Current url expected is: "+expectedURL+" while actual on UI is "+storeFrontBrandRefreshHomePage.getCurrentURL());
		s_assert.assertAll();
	}



}