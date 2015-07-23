package com.rf.test.website.storeFront;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.test.website.RFWebsiteBaseTest;

public class RFPWSLoginTest extends RFWebsiteBaseTest {

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsulatantPage;

	// Tests are disabled as the users for EC-3720 are unavailable right now for tst4
	
	//Consultant with PWS - Her own PWS - Her own PWS
	@Test(enabled=false)
	public void testConsultantWithPWSLoginFromOwnPWS(){
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(TestConstants.CONSULTANT1_PWS_URL);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT1_WITH_PWS_EMAIL_ID, TestConstants.CONSULTANT1_WITH_PWS_PASSWORD);
		assertTrue("Consultant is not on her own PWS", storeFrontConsulatantPage.getCurrentURL().contains(TestConstants.CONSULTANT1_PWS_URL));		
	}

	//Consultant with PWS -Someone else’s PWS - Her own PW
	@Test(enabled=false)
	public void testConsultantWithPWSLoginFromOthersPWS(){
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(TestConstants.CONSULTANT2_PWS_URL);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT1_WITH_PWS_EMAIL_ID, TestConstants.CONSULTANT1_WITH_PWS_PASSWORD);		
		assertTrue("Consultant is not on her own PWS", storeFrontConsulatantPage.getCurrentURL().contains(TestConstants.CONSULTANT1_PWS_URL));
	}


	//Consultant with PWS - Corporate site - Her own .com PWS
	@Test(enabled=false)
	public void testConsultantWithPWSLoginFromCorp(){
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT1_WITH_PWS_EMAIL_ID, TestConstants.CONSULTANT1_WITH_PWS_PASSWORD);
		assertTrue("Consultant is not on her own .com PWS", storeFrontConsulatantPage.getCurrentURL().contains(TestConstants.CONSULTANT1_PWS_COM_URL));
	}


	//Consultant W/O PWS - Someone else’s PWS - Corporate site
	@Test(enabled=false)
	public void testConsultantWithoutPWSLoginFromOthersPWS(){
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(TestConstants.CONSULTANT2_PWS_URL);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT1_WITHOUT_PWS_EMAIL_ID, TestConstants.CONSULTANT1_WITHOUT_PWS_PASSWORD);		
		assertTrue("Consultant is not on corporate site", storeFrontConsulatantPage.getCurrentURL().contains(TestConstants.CORPORATE_SITE));
	}


	//Consultant W/O PWS - Corporate site - Corporate site
	@Test(enabled=false)
	public void testConsultantWithoutPWSLoginFromCorp(){
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT1_WITHOUT_PWS_EMAIL_ID, TestConstants.CONSULTANT1_WITHOUT_PWS_PASSWORD);
		assertTrue("Consultant is not on corporate site", storeFrontConsulatantPage.getCurrentURL().contains(TestConstants.CORPORATE_SITE));
	}


	//PC, whose Sponsor has PWS - Sponsor’s PWS - Sponsor’s PWS
	@Test(enabled=false)
	public void testPreferredCustomerWithPWSSponsorLoginFromSponsorsPWS(){
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(TestConstants.CONSULTANT1_PWS_URL);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.PCUSER_SPONSOR_PWS_EMAIL_ID, TestConstants.PCUSER_SPONSOR_PWS_PASSWORD);
		assertTrue("PC is not on Sponsor's PWS", storeFrontConsulatantPage.getCurrentURL().contains(TestConstants.CONSULTANT1_PWS_URL));
	}


	//PC, whose Sponsor has PWS - Not Sponsor’s PWS - Sponsor’s PWS
	@Test(enabled=false)
	public void testPreferredCustomerWithPWSSponsorLoginFromOtherSponsorsPWS(){
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(TestConstants.CONSULTANT2_PWS_URL);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.PCUSER_SPONSOR_PWS_EMAIL_ID, TestConstants.PCUSER_SPONSOR_PWS_PASSWORD);
		assertTrue("PC is not on Sponsor's PWS", storeFrontConsulatantPage.getCurrentURL().contains(TestConstants.CONSULTANT1_PWS_URL));
	}


	//PC, whose Sponsor has PWS - Corporate Site - Sponsor’s PWS
	@Test(enabled=false)
	public void testPreferredCustomerWithPWSSponsorLoginFromCorp(){
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(TestConstants.CORPORATE_SITE);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.PCUSER_SPONSOR_PWS_EMAIL_ID, TestConstants.PCUSER_SPONSOR_PWS_PASSWORD);
		assertTrue("PC is not on Sponsor's PWS", storeFrontConsulatantPage.getCurrentURL().contains(TestConstants.CONSULTANT1_PWS_URL));
	}



	//PC, whose Sponsor has No PWS - someone's PWS - Corporate Site
	@Test(enabled=false)
	public void testPreferredCustomerWithNoPWSSponsorLoginFromOtherPWS(){
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(TestConstants.CONSULTANT2_PWS_URL);	
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.PCUSER_SPONSOR_NO_PWS_EMAIL_ID, TestConstants.PCUSER_SPONSOR_NO_PWS_PASSWORD);			
		assertTrue("PC is not on corporate site", storeFrontConsulatantPage.getCurrentURL().contains(TestConstants.CORPORATE_SITE));
	}


	//PC, whose Sponsor has No PWS - Corporate Site - Corporate Site
	@Test(enabled=false)
	public void testPreferredCustomerWithNoPWSSponsorLoginFromCorp(){
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(TestConstants.CORPORATE_SITE);	
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.PCUSER_SPONSOR_NO_PWS_EMAIL_ID, TestConstants.PCUSER_SPONSOR_NO_PWS_PASSWORD);			
		assertTrue("PC is not on corporate site", storeFrontConsulatantPage.getCurrentURL().contains(TestConstants.CORPORATE_SITE));
	}


	//PC, with no sponsor - someone's site - Corporate Site 
	@Test(enabled=false)
	public void testPreferredCustomerNoSponsorLoginFromOthersPWS(){
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(TestConstants.CONSULTANT2_PWS_URL);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.PCUSER_NO_SPONSOR_EMAIL_ID, TestConstants.PCUSER_NO_SPONSOR_PASSWORD);		
		assertTrue("PC is not on corporate site", storeFrontConsulatantPage.getCurrentURL().contains(TestConstants.CORPORATE_SITE));
	}



	//PC, with no sponsor - corporate site - Corporate Site
	@Test(enabled=false)
	public void testPreferredCustomerNoSponsorLoginFromCorp(){
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.PCUSER_NO_SPONSOR_EMAIL_ID, TestConstants.PCUSER_NO_SPONSOR_PASSWORD);		
		assertTrue("PC is not on corporate site", storeFrontConsulatantPage.getCurrentURL().contains(TestConstants.CORPORATE_SITE));
	}


	//RC - someone's PWS - someone's PWS
	@Test(enabled=false)
	public void testRetailCustomerLoginFromOthersPWS(){
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(TestConstants.CONSULTANT2_PWS_URL);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.RCUSER_EMAIL_ID, TestConstants.RCUSER_PASSWORD);
		assertTrue("RC is not on someone's PWS", storeFrontConsulatantPage.getCurrentURL().contains(TestConstants.CONSULTANT2_PWS_URL));
	}


	//RC - corporate site - Corporate Site
	@Test(enabled=false)
	public void testRetailCustomerLoginFromCorp(){
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.RCUSER_EMAIL_ID, TestConstants.RCUSER_PASSWORD);
		assertTrue("RC is not on corporate site", storeFrontConsulatantPage.getCurrentURL().contains(TestConstants.CORPORATE_SITE));
	}

}
