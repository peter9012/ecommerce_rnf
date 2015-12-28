package com.rf.test.website.storeFront.dsv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.DSVStoreFrontHomePage;
import com.rf.test.website.RFWebsiteBaseTest;


public class StorefrontDSVTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(StorefrontDSVTest.class.getName());
	private DSVStoreFrontHomePage dsvHomePage;

	//Hybris Project-5314:User Account login As Consultant
	@Test
	public void testUserAccountLoginAsConsultant(){
		dsvHomePage = new DSVStoreFrontHomePage(driver);
		dsvHomePage.clickLoginLink();
		dsvHomePage.enterUsername(TestConstants.DSV_CONSULTANT_USERNAME);
		dsvHomePage.enterPassword(TestConstants.DSV_CONSULTANT_PASSWORD);
		dsvHomePage.clickLoginBtn();
		s_assert.assertTrue(dsvHomePage.getWebdriver().getCurrentUrl().contains(TestConstants.DSV_PWS_SUFFIX), "Consultant is not on PWS after login,the url coming is "+dsvHomePage.getWebdriver().getCurrentUrl());
		s_assert.assertTrue(dsvHomePage.getWelcomeText().contains("Welcome"), "Home page doesn't have the 'Welcome' link");
		s_assert.assertTrue(dsvHomePage.isCRPCartImagePresent(), "CRP Cart image is not present on home page");
		s_assert.assertTrue(dsvHomePage.getNextCRPText().contains(TestConstants.DSV_NEXT_CRP_TEXT), "Expected Next CRP text is "+TestConstants.DSV_NEXT_CRP_TEXT+" But actually the text coming is "+dsvHomePage.getNextCRPText());
		s_assert.assertAll();		
	}

	//Hybris Project-5321:User Account login As PC
	@Test
	public void testUserAccountLoginAsPC(){
		dsvHomePage = new DSVStoreFrontHomePage(driver);
		String baseURL = dsvHomePage.getBaseURL();	
		dsvHomePage.clickLoginLink();
		dsvHomePage.enterUsername(TestConstants.DSV_PC_USERNAME);
		dsvHomePage.enterPassword(TestConstants.DSV_PC_PASSWORD);
		dsvHomePage.clickLoginBtn();
		s_assert.assertTrue(dsvHomePage.getWebdriver().getCurrentUrl().contains(baseURL), "PC is not corp site after login,the url coming is "+dsvHomePage.getWebdriver().getCurrentUrl());
		s_assert.assertTrue(dsvHomePage.getWelcomeText().contains("Welcome"), "Home page doesn't have the 'Welcome' link");		
		s_assert.assertAll();		
	}

	//Hybris Project-5327:User Account login As RC
	@Test
	public void testUserAccountLoginAsRC(){
		dsvHomePage = new DSVStoreFrontHomePage(driver);
		String baseURL = dsvHomePage.getBaseURL();		
		dsvHomePage.clickLoginLink();
		dsvHomePage.enterUsername(TestConstants.DSV_RC_USERNAME);
		dsvHomePage.enterPassword(TestConstants.DSV_RC_PASSWORD);
		dsvHomePage.clickLoginBtn();
		s_assert.assertFalse(dsvHomePage.getWebdriver().getCurrentUrl().contains(TestConstants.DSV_PWS_SUFFIX), "RC is on PWS after login,the url coming is "+dsvHomePage.getWebdriver().getCurrentUrl());
		s_assert.assertTrue(dsvHomePage.getWebdriver().getCurrentUrl().contains(baseURL), "RC is not corp site after login,the url coming is "+dsvHomePage.getWebdriver().getCurrentUrl());
		s_assert.assertTrue(dsvHomePage.getWelcomeText().contains("Welcome"), "Home page doesn't have the 'Welcome' link");		
		s_assert.assertAll();		
	}
}

