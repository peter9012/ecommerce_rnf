package com.rf.test.website.storeFront.dsv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.DSVStoreFrontAutoshipCartPage;
import com.rf.pages.website.DSVStoreFrontBillingInfoPage;
import com.rf.pages.website.DSVStoreFrontHomePage;
import com.rf.pages.website.DSVStoreFrontQuickShopPage;
import com.rf.pages.website.DSVStoreFrontShippingInfoPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class StorefrontDSVTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(StorefrontDSVTest.class.getName());

	//-------------------------------------------------Pages---------------------------------------------------------
	private DSVStoreFrontHomePage dsvStoreFrontHomePage;
	private DSVStoreFrontAutoshipCartPage dsvStoreFrontAutoshipCartPage;
	private DSVStoreFrontQuickShopPage dsvStoreFrontQuickShopPage;
	private DSVStoreFrontShippingInfoPage dsvStoreFrontShippingInfoPage;
	private DSVStoreFrontBillingInfoPage dsvStoreFrontBillingInfoPage;
	//-----------------------------------------------------------------------------------------------------------------


	//Hybris Project-5314:User Account login As Consultant
	@Test
	public void testUserAccountLoginAsConsultant_5314(){
		dsvStoreFrontHomePage = new DSVStoreFrontHomePage(driver);
		dsvStoreFrontHomePage.clickLoginLink();
		dsvStoreFrontHomePage.enterUsername(TestConstants.DSV_CONSULTANT_USERNAME);
		dsvStoreFrontHomePage.enterPassword(TestConstants.DSV_CONSULTANT_PASSWORD);
		dsvStoreFrontHomePage.clickLoginBtn();
		s_assert.assertTrue(dsvStoreFrontHomePage.getWebdriver().getCurrentUrl().contains(TestConstants.DSV_PWS_SUFFIX), "Consultant is not on PWS after login,the url coming is "+dsvStoreFrontHomePage.getWebdriver().getCurrentUrl());
		s_assert.assertTrue(dsvStoreFrontHomePage.getWelcomeText().contains("Welcome"), "Home page doesn't have the 'Welcome' link");
		s_assert.assertTrue(dsvStoreFrontHomePage.isCRPCartImagePresent(), "CRP Cart image is not present on home page");
		s_assert.assertTrue(dsvStoreFrontHomePage.getNextCRPText().contains(TestConstants.DSV_NEXT_CRP_TEXT), "Expected Next CRP text is "+TestConstants.DSV_NEXT_CRP_TEXT+" But actually the text coming is "+dsvStoreFrontHomePage.getNextCRPText());
		s_assert.assertAll();		
	}

	//Hybris Project-5321:User Account login As PC
	@Test
	public void testUserAccountLoginAsPC_5321(){
		dsvStoreFrontHomePage = new DSVStoreFrontHomePage(driver);
		String baseURL = dsvStoreFrontHomePage.getBaseURL();	
		dsvStoreFrontHomePage.clickLoginLink();
		dsvStoreFrontHomePage.enterUsername(TestConstants.DSV_PC_USERNAME);
		dsvStoreFrontHomePage.enterPassword(TestConstants.DSV_PC_PASSWORD);
		dsvStoreFrontHomePage.clickLoginBtn();
		s_assert.assertTrue(dsvStoreFrontHomePage.getWebdriver().getCurrentUrl().contains(baseURL), "PC is not corp site after login,the url coming is "+dsvStoreFrontHomePage.getWebdriver().getCurrentUrl());
		s_assert.assertTrue(dsvStoreFrontHomePage.getWelcomeText().contains("Welcome"), "Home page doesn't have the 'Welcome' link");		
		s_assert.assertAll();		
	}

	//Hybris Project-5327:User Account login As RC
	@Test
	public void testUserAccountLoginAsRC(){
		dsvStoreFrontHomePage = new DSVStoreFrontHomePage(driver);
		String baseURL = dsvStoreFrontHomePage.getBaseURL();		
		dsvStoreFrontHomePage.clickLoginLink();
		dsvStoreFrontHomePage.enterUsername(TestConstants.DSV_RC_USERNAME);
		dsvStoreFrontHomePage.enterPassword(TestConstants.DSV_RC_PASSWORD);
		dsvStoreFrontHomePage.clickLoginBtn();
		s_assert.assertFalse(dsvStoreFrontHomePage.getWebdriver().getCurrentUrl().contains(TestConstants.DSV_PWS_SUFFIX), "RC is on PWS after login,the url coming is "+dsvStoreFrontHomePage.getWebdriver().getCurrentUrl());
		s_assert.assertTrue(dsvStoreFrontHomePage.getWebdriver().getCurrentUrl().contains(baseURL), "RC is not corp site after login,the url coming is "+dsvStoreFrontHomePage.getWebdriver().getCurrentUrl());
		s_assert.assertTrue(dsvStoreFrontHomePage.getWelcomeText().contains("Welcome"), "Home page doesn't have the 'Welcome' link");		
		s_assert.assertAll();		
	}

	//Hybris Project-5318:Adding new and Editing existing Shipping Profile AS Consultant
	@Test
	public void testAddAndEditShippingProfileAsConsultant() throws Exception{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String fName = "New";
		String lName1 = "SP"+randomNum;
		String lName2 = "SP"+randomNum;
		String name1 = fName+" "+lName1;
		String name2 = fName+" "+lName2;
		dsvStoreFrontHomePage = new DSVStoreFrontHomePage(driver);
		dsvStoreFrontHomePage.clickLoginLink();
		dsvStoreFrontHomePage.enterUsername(TestConstants.DSV_CONSULTANT_USERNAME);
		dsvStoreFrontHomePage.enterPassword(TestConstants.DSV_CONSULTANT_PASSWORD);
		dsvStoreFrontHomePage.clickLoginBtn();
		s_assert.assertTrue(dsvStoreFrontHomePage.getWebdriver().getCurrentUrl().contains(TestConstants.DSV_PWS_SUFFIX), "Consultant is not on PWS after login,the url coming is "+dsvStoreFrontHomePage.getWebdriver().getCurrentUrl());
		s_assert.assertTrue(dsvStoreFrontHomePage.getWelcomeText().contains("Welcome"), "Home page doesn't have the 'Welcome' link");
		dsvStoreFrontHomePage.clickWelcomeDropDown();
		dsvStoreFrontShippingInfoPage = dsvStoreFrontHomePage.clickShippingInfoLinkFromWelcomeDropDown();
		dsvStoreFrontShippingInfoPage.clickAddANewShippingAddressLink();
		dsvStoreFrontShippingInfoPage.enterNewShippingAddressDetails(name1, TestConstants.DSV_ADDRESS_LINE_1_CA, TestConstants.DSV_CITY_CA, TestConstants.DSV_POSTAL_CODE_CA, TestConstants.DSV_PHONE_NUMBER,TestConstants.DSV_SECURITY_CODE);
		dsvStoreFrontShippingInfoPage.clickSaveAddressBtn();
		s_assert.assertTrue(dsvStoreFrontShippingInfoPage.isShippingProfilePresentonPage(lName1), name1+" shipping profile is not added on the page");
		dsvStoreFrontShippingInfoPage.clickEditShippingProfileLink(lName1);
		dsvStoreFrontShippingInfoPage.enterNameWithCardNumberAndSecurityCode(name2,TestConstants.DSV_SECURITY_CODE);
		dsvStoreFrontShippingInfoPage.clickSaveAddressBtn();
		s_assert.assertTrue(dsvStoreFrontShippingInfoPage.isShippingProfilePresentonPage(lName2), name1+" shipping profile is not edited on the page");
		s_assert.assertAll();
	}

	//Hybris Project-5319:Adding new and Editing existing Billing Profile AS Consultant
	@Test
	public void testAddAndEditBillingProfileAsConsultant() throws Exception{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String fName = "New";
		String lName1 = "BP"+randomNum;
		String lName2 = "BP"+randomNum;
		String name1 = fName+" "+lName1;
		String name2 = fName+" "+lName2;
		dsvStoreFrontHomePage = new DSVStoreFrontHomePage(driver);
		dsvStoreFrontHomePage.clickLoginLink();
		dsvStoreFrontHomePage.enterUsername(TestConstants.DSV_CONSULTANT_USERNAME);
		dsvStoreFrontHomePage.enterPassword(TestConstants.DSV_CONSULTANT_PASSWORD);
		dsvStoreFrontHomePage.clickLoginBtn();
		s_assert.assertTrue(dsvStoreFrontHomePage.getWebdriver().getCurrentUrl().contains(TestConstants.DSV_PWS_SUFFIX), "Consultant is not on PWS after login,the url coming is "+dsvStoreFrontHomePage.getWebdriver().getCurrentUrl());
		s_assert.assertTrue(dsvStoreFrontHomePage.getWelcomeText().contains("Welcome"), "Home page doesn't have the 'Welcome' link");
		dsvStoreFrontHomePage.clickWelcomeDropDown();
		dsvStoreFrontBillingInfoPage = dsvStoreFrontHomePage.clickBillingInfoLinkFromWelcomeDropDown();
		dsvStoreFrontBillingInfoPage.clickAddANewBillingProfileLink();
		dsvStoreFrontBillingInfoPage.enterNewBillingProfileDetails(TestConstants.DSV_CARD_NUMBER, name1, TestConstants.DSV_EXPIRY_MONTH, TestConstants.DSV_EXPIRY_YEAR, TestConstants.DSV_SECURITY_CODE);
		dsvStoreFrontBillingInfoPage.clickSaveBillingProfileBtn();
		s_assert.assertTrue(dsvStoreFrontBillingInfoPage.isBillingProfilePresentonPage(lName1), name1+" billing profile is not added on the page");
		dsvStoreFrontBillingInfoPage.clickEditBillingProfileLink(lName1);
		dsvStoreFrontBillingInfoPage.enterNameAndSecurityCode(name2,TestConstants.DSV_SECURITY_CODE);
		dsvStoreFrontBillingInfoPage.clickSaveBillingProfileBtn();
		s_assert.assertTrue(dsvStoreFrontBillingInfoPage.isBillingProfilePresentonPage(lName2), name1+" billing profile is not edited on the page");
		s_assert.assertAll();
	}



	// Hybris Project-5324:Adding new and Editing existing Shipping Profile As PC
	@Test
	public void testAddAndEditShippingProfileAsPC() throws Exception{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String fName = "New";
		String lName1 = "SP"+randomNum;
		String lName2 = "SP"+randomNum;
		String name1 = fName+" "+lName1;
		String name2 = fName+" "+lName2;			
		dsvStoreFrontHomePage = new DSVStoreFrontHomePage(driver);
		String baseURL = dsvStoreFrontHomePage.getBaseURL();
		dsvStoreFrontHomePage.clickLoginLink();
		dsvStoreFrontHomePage.enterUsername(TestConstants.DSV_PC_USERNAME);
		dsvStoreFrontHomePage.enterPassword(TestConstants.DSV_PC_PASSWORD);
		dsvStoreFrontHomePage.clickLoginBtn();
		s_assert.assertTrue(dsvStoreFrontHomePage.getWebdriver().getCurrentUrl().contains(baseURL), "PC is not corp site after login,the url coming is "+dsvStoreFrontHomePage.getWebdriver().getCurrentUrl());
		s_assert.assertTrue(dsvStoreFrontHomePage.getWelcomeText().contains("Welcome"), "Home page doesn't have the 'Welcome' link");		
		dsvStoreFrontHomePage.clickWelcomeDropDown();
		dsvStoreFrontShippingInfoPage = dsvStoreFrontHomePage.clickShippingInfoLinkFromWelcomeDropDown();
		dsvStoreFrontShippingInfoPage.clickAddANewShippingAddressLink();
		dsvStoreFrontShippingInfoPage.enterNewShippingAddressDetails(name1, TestConstants.DSV_ADDRESS_LINE_1_CA, TestConstants.DSV_CITY_CA, TestConstants.DSV_POSTAL_CODE_CA, TestConstants.DSV_PHONE_NUMBER,TestConstants.DSV_SECURITY_CODE);
		dsvStoreFrontShippingInfoPage.clickSaveAddressBtn();
		s_assert.assertTrue(dsvStoreFrontShippingInfoPage.isShippingProfilePresentonPage(lName1), name1+" shipping profile is not added on the page");
		dsvStoreFrontShippingInfoPage.clickEditShippingProfileLink(lName1);
		dsvStoreFrontShippingInfoPage.enterNameWithCardNumberAndSecurityCode(name2,TestConstants.DSV_SECURITY_CODE);
		dsvStoreFrontShippingInfoPage.clickSaveAddressBtn();
		s_assert.assertTrue(dsvStoreFrontShippingInfoPage.isShippingProfilePresentonPage(lName2), name1+" shipping profile is not edited on the page");
		s_assert.assertAll();
	}

	//Hybris Project-5325:Adding new and Editing existing Billing Profile As PC
	@Test
	public void testAddAndEditBillingProfileAsPC() throws Exception{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String fName = "New";
		String lName1 = "BP"+randomNum;
		String lName2 = "BP"+randomNum;
		String name1 = fName+" "+lName1;
		String name2 = fName+" "+lName2;
		dsvStoreFrontHomePage = new DSVStoreFrontHomePage(driver);
		String baseURL = dsvStoreFrontHomePage.getBaseURL();
		dsvStoreFrontHomePage.clickLoginLink();
		dsvStoreFrontHomePage.enterUsername(TestConstants.DSV_PC_USERNAME);
		dsvStoreFrontHomePage.enterPassword(TestConstants.DSV_PC_PASSWORD);
		dsvStoreFrontHomePage.clickLoginBtn();
		s_assert.assertTrue(dsvStoreFrontHomePage.getWebdriver().getCurrentUrl().contains(baseURL), "PC is not corp site after login,the url coming is "+dsvStoreFrontHomePage.getWebdriver().getCurrentUrl());
		s_assert.assertTrue(dsvStoreFrontHomePage.getWelcomeText().contains("Welcome"), "Home page doesn't have the 'Welcome' link");		
		dsvStoreFrontHomePage.clickWelcomeDropDown();
		dsvStoreFrontBillingInfoPage = dsvStoreFrontHomePage.clickBillingInfoLinkFromWelcomeDropDown();
		dsvStoreFrontBillingInfoPage.clickAddANewBillingProfileLink();
		dsvStoreFrontBillingInfoPage.enterNewBillingProfileDetails(TestConstants.DSV_CARD_NUMBER, name1, TestConstants.DSV_EXPIRY_MONTH, TestConstants.DSV_EXPIRY_YEAR, TestConstants.DSV_SECURITY_CODE);
		dsvStoreFrontBillingInfoPage.clickSaveBillingProfileBtn();
		s_assert.assertTrue(dsvStoreFrontBillingInfoPage.isBillingProfilePresentonPage(lName1), name1+" billing profile is not added on the page");
		dsvStoreFrontBillingInfoPage.clickEditBillingProfileLink(lName1);
		dsvStoreFrontBillingInfoPage.enterNameAndSecurityCode(name2,TestConstants.DSV_SECURITY_CODE);
		dsvStoreFrontBillingInfoPage.clickSaveBillingProfileBtn();
		s_assert.assertTrue(dsvStoreFrontBillingInfoPage.isBillingProfilePresentonPage(lName2), name1+" billing profile is not edited on the page");
		s_assert.assertAll();
	}
}

