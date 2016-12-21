package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class TopNavigationTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-72 Become a Consultant- Why R+F
	 * 
	 * Description : This test validates that Enroll now button on Why R+F page is redirecting to 
	 * sponsor search page
	 *     
	 */
	@Test //Incomplete
	public void testBecomeAConsultantAtWhyRFPage_72(){
		sfHomePage.clickWhyRF();
		sfHomePage.clickEnrollNowButton();
	}

	/***
	 * qTest : TC-73 Become a Consultant- Events
	 * 
	 * Description : This test validates that Event Calendar button is redirecting to 
	 * new tab which contain calendar
	 *     
	 */
	@Test
	public void testBecomeAConsultantEvents_73(){
		String currentWindowID = null; 
		String currentURL = null;
		sfHomePage.clickEvents();
		currentWindowID = CommonUtils.getCurrentWindowHandle();
		sfHomePage.clickFirstEventCalendar();
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains("events/calendar"), "Expected URL should contain events/Calendar but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-74 Become a Consultant- Programs and Incentives
	 * 
	 * Description : This test validates that Enroll now button is redirecting to 
	 * sponsor search page through Programs and Incentives Page
	 *     
	 */
	@Test //Incomplete
	public void testBecomeAConsultantProgramsAndIncentives_74(){
		sfHomePage.clickProgramsAndIncentives();
		sfHomePage.clickEnrollNowButton();
	}


	/***
	 * qTest : TC-134 Becoming a Consultant- Meet our Community
	 * 
	 * Description : This test validates Meet our community link and social icons	  
	 */
	@Test
	public void testBecomeAConsultantMeetOurCommunity_134(){
		String currentWindowID = null;
		String currentURL = null;
		String redefine = "redefine";
		String socialIconFacebook = "facebook";
		String socialIconTwitter = "twitter";
		String socialIconInstagram = "instagram";
		String socialIconDerm = "derm";
		String socialIconPinterest = "pinterest";
		String socialIconYoutube = "youtube";

		sfHomePage.clickMeetOurCommunityLink();
		currentWindowID = CommonUtils.getCurrentWindowHandle();
		sfHomePage.clickVisitTheBlock();
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(redefine), "Expected URL should contain "+redefine+" but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);
		//Verify Social Icon
		//Facebook
		sfHomePage.clickSocialMediaIcon(socialIconFacebook);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(socialIconFacebook), "Expected URL should contain "+socialIconFacebook+" but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);

		//Twitter
		sfHomePage.clickSocialMediaIcon(socialIconTwitter);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(socialIconTwitter), "Expected URL should contain "+socialIconTwitter+" but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);

		//Instagram
		sfHomePage.clickSocialMediaIcon(socialIconInstagram);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(socialIconInstagram), "Expected URL should contain "+socialIconInstagram+" but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);

		//Derm
		sfHomePage.clickSocialMediaIcon(socialIconDerm);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(socialIconDerm), "Expected URL should contain "+socialIconDerm+" but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);

		//Pinterest
		sfHomePage.clickSocialMediaIcon(socialIconPinterest);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(socialIconPinterest), "Expected URL should contain "+socialIconPinterest+" but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);

		//Youtube
		sfHomePage.clickSocialMediaIcon(socialIconYoutube);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(socialIconYoutube), "Expected URL should contain "+socialIconYoutube+" but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-75 About R+F- Who We Are
	 * 
	 * Description : This test validates Direct selling link & DSA code of ethics link
	 * Through Who we are page 
	 *     
	 */
	@Test
	public void testAboutRFWhoWeAre_75(){
		String currentWindowID = null;
		String currentURL = null;
		String directselling = "directselling";
		String codeOfEthics = "code-of-ethics";
		sfHomePage.clickWhoWeAreLink();
		currentWindowID = CommonUtils.getCurrentWindowHandle();
		sfHomePage.clickDirectLinkAssociationLink();
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(directselling), "Expected URL should contain "+directselling+" but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);
		sfHomePage.clickDsaCodeOfEthicsLink();
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(codeOfEthics), "Expected URL should contain "+codeOfEthics+" but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-177 About R+F- Giving Back
	 * 
	 * Description : This test validates donate now functionality
	 * Through Giving Back page 
	 *     
	 */
	@Test//Incomplete
	public void testAboutRFGivingBack_75(){
		if(country.equalsIgnoreCase("us")){
			String currentWindowID = null;
			sfHomePage.clickGivingBackLink();
			currentWindowID = CommonUtils.getCurrentWindowHandle();
			sfHomePage.clickDonateNow();
			//sfHomePage.switchToChildWindow(currentWindowID);
			s_assert.assertAll();
		}
	}

	/***
	 * qTest : TC-183 Product Search- Name
	 * 
	 * Description : This test validates search icon functionality using product name
	 *     
	 */
	@Test//Incomplete(need a product name for search)
	public void testProductSearchName_183(){
		String productName = TestConstants.PRODUCT_NAME;
		String productNameFromUI = null;
		sfHomePage.clickSearchIcon();
		sfHomePage.searchProduct(productName);
		productNameFromUI = sfHomePage.getProductName().toLowerCase();
		productName = productName.toLowerCase();
		s_assert.assertTrue(productNameFromUI.contains(productName), "Expected product name is:"+productName+"Actual On UI is:"+productNameFromUI);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-184 Product Search - Auto-complete
	 * 
	 * Description : This test validates search icon auto complete functionality 
	 * using product name
	 *     
	 */
	@Test//Incomplete(need a product name of 3 letters)
	public void testProductSearchAutoComplete_184(){
		String productName = TestConstants.PRODUCT_NAME;
		String productNameLessThanThreeLetters = "AB";
		String productNameFromUI = null;
		sfHomePage.clickSearchIcon();
		sfHomePage.searchProduct(productNameLessThanThreeLetters);
		String errorMessage = sfHomePage.getErrorMessageForSearchedProduct().toLowerCase();
		s_assert.assertTrue(errorMessage.contains("sorry, we couldn't find any results for your search"), "Expected error message is 'Sorry, we couldn't find any results for your search' Actual on UI is:"+errorMessage);
		sfHomePage.clickSearchIcon();
		sfHomePage.searchProduct(productName);
		productNameFromUI = sfHomePage.getProductName().toLowerCase();
		productName = productName.toLowerCase();
		s_assert.assertTrue(productNameFromUI.contains(productName), "Expected product name is:"+productName+"Actual On UI is:"+productNameFromUI);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-76 About R+F- Executive Team
	 * Description : This test validates team member details on executive team page.
	 *  
	 *     
	 */
	@Test
	public void testAboutRFExecutiveTeam_76(){
		String currentURL = null;
		String teamMemberName = null;
		String teamMemberNameOnPopup = null;
		String urlToAssert = "executive-team";
		sfHomePage.clickExecutiveTeam();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+ "but actual on UI is"+currentURL);
		//Click and return random team member name.
		teamMemberName = sfHomePage.clickAndReturnTeamMemberName();
		//Verify details of team member.
		teamMemberNameOnPopup = sfHomePage.getTeamMemberNameFromPopup();
		s_assert.assertTrue(teamMemberNameOnPopup.contains(teamMemberName), "Expected team member name is" +teamMemberName+ "but actual on UI is"+teamMemberNameOnPopup);
		sfHomePage.closeMemberDetailsPopup();
		s_assert.assertAll();
	}
}