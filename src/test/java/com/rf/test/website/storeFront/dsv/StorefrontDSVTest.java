package com.rf.test.website.storeFront.dsv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.DSVStoreFrontAutoshipCartPage;
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
	//-----------------------------------------------------------------------------------------------------------------


	//Hybris Project-5314:User Account login As Consultant
	@Test
	public void testUserAccountLoginAsConsultant(){
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
	public void testUserAccountLoginAsPC(){
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

}

