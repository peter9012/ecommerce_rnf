package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class TerminationTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-447 PC Perks status- Cancel autoship
	 * 
	 * Description : This test validates PC Perks Autoship status after cancelling PC Perks. 
	 * 
	 *     
	 */
	@Test 
	public void testCancelPCPerksAutoship_447(){
		String currentURL = null;
		String pcPerksPageURL="pc-perks-status";
		
		sfHomePage.loginToStoreFront("", password);
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipStatusPage =sfHomePage.navigateToPCPerksStatusPage();
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(sfAutoshipStatusPage.isPCPerksAutoshipStatusPagePresent() && currentURL.contains(pcPerksPageURL),"PC Perks status page is not displayed Expected URL"+pcPerksPageURL+" while actual on UI"+currentURL);
		sfAutoshipStatusPage.delayOrCancelPCPerks();
		s_assert.assertTrue(sfAutoshipStatusPage.isDelayOrCancelPCPerksPopupPresent(),"Delay or cancel PC perks popup not present with 'delay' and 'cancel' option");
		sfAutoshipStatusPage.selectCancelPCPerksOnPopup();
		sfAutoshipStatusPage.fillTheEntriesAndClickOnSubmitDuringTermination();
		s_assert.assertTrue(sfAutoshipStatusPage.verifyAccountTerminationIsConfirmedPopup(),"PC user account termination popup is not present.");
		sfAutoshipStatusPage.clickOnConfirmTerminationPopup();
		s_assert.assertTrue(sfHomePage.isLogoutSuccessful(),"PC user is still logged in after termination");
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL, password);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains("error"),"PC user is still able to login after account termination");
		s_assert.assertAll();
	}
}
