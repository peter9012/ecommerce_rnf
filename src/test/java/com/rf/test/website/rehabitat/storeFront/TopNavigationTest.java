package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class TopNavigationTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-73 Become a Consultant- Events
	 * 
	 * Description : This test validates that Event Calendar button is redirecting to 
	 * new tab which contain calendar
	 *     
	 */
	@Test(enabled=false)//no such functionality coming on UI
	public void testBecomeAConsultantEvents_73(){
		String currentWindowID = null; 
		String currentURL = null;
		sfHomePage.clickEvents();
		currentWindowID = CommonUtils.getCurrentWindowHandle();
		sfHomePage.clickFirstEventCalendar();
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains("events/eventrsvp"), "Expected URL should contain events/EventRsvp but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-134 Becoming a Consultant- Meet our Community
	 * 
	 * Description : This test validates Meet our community link and social icons	  
	 */
	@Test(enabled=false)
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
	@Test(enabled=false)//TODO
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
	@Test(enabled=false)//TODO Incomplete
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
	 * qTest : TC-76 About R+F- Executive Team
	 * Description : This test validates team member details on executive team page.
	 *  
	 *     
	 */
	@Test(enabled=false)
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

	/***
	 * qTest : TC-345 As a PC, Consultant, Retail/Anon User, I will be able to view a search text box
	 * Description : This test validates search text box in header navigation
	 *  
	 *     
	 */
	@Test(enabled=true)
	public void testViewSearchTextBox_345(){
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),  password,true);
		sfHomePage.clickSearchIcon();
		s_assert.assertTrue(sfHomePage.isSearchTextBoxDisplayed(),"Search text box not present after clicking search");
		sfHomePage.closeSearchTextBox();
		s_assert.assertFalse(sfHomePage.isSearchTextBoxDisplayed(),"Search text box is present after clicking close icon");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-346 As a user, I will be able to access various content pages under the About R+F
	 * Description : This test validates content pages under about RF.
	 *  
	 *     
	 */
	@Test(enabled=true)
	public void testViewContentPagesUnderAboutRF_346(){
		String currentURL = null;
		String executiveTeamURL = "executive-team";
		String whoWeAreURL = "who-we-are";
		String aboutRodanFields = "about-rodan-fields";
		String givingBackURL ="giving-back";
		//Verify meet the doctors link.
		sfHomePage.clickMeetTheDoctorsLink();
		s_assert.assertTrue(sfHomePage.isMeetTheDoctorsPagePresent(),"'Meet the Doctors' page either doesn't have the URL as 'meet-the-doctors' or meet the doctor Text is not present on page");
		//Verify giving back link.
		sfHomePage.clickGivingBackLink();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(givingBackURL)||currentURL.contains("pfc-foundation"), "Expected URL should contain "+givingBackURL+ "but actual on UI is"+currentURL);
		//Verify executive team link.
		sfHomePage.clickExecutiveTeam();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(executiveTeamURL), "Expected URL should contain "+executiveTeamURL+ "but actual on UI is"+currentURL);
		//Verify who we are link.
		sfHomePage.clickWhoWeAreLink();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(whoWeAreURL) ||currentURL.contains(aboutRodanFields), "Expected URL should contain "+whoWeAreURL+ "but actual on UI is"+currentURL);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-347 As a PC, Retail/Anon User, and Consultant I will be able to access the featured products
	 * Description : This test validates featured product section under shop skincare.
	 *  
	 *     
	 */
	@Test(enabled=false)
	public void testVerifyFeaturedProductSectionUnderShopSkinCare_347(){
		String category="FEATURED";

		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),  password,true);
		sfShopSkinCarePage = sfHomePage.navigateToShopSkincareLink(category);
		String pageTitle=sfShopSkinCarePage.getCurrentpageTitle();
		s_assert.assertTrue(sfShopSkinCarePage.isProductsDisplayedOnPage() && pageTitle.contains(category),"Expected featured products not displayed for selected category or Expected page title contains:"+category+" But actual on UI is: "+pageTitle);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-348 As a PC, Retail/Anon User, and Consultant I will be able to access the PLP
	 * Description : This test validates product list page under shop skincare.
	 *  
	 *     
	 */
	@Test(enabled=true)
	public void testVerifyPLPUnderShopSkinCare_348(){
		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(),  password,true);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		s_assert.assertTrue(sfShopSkinCarePage.isProductsDisplayedOnPage(),"All product page not present after clicking continue shopping.");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-349 As a PC, Retail/Anon User, Consultant, I will be able to access category specific Results
	 * Description : This test validates results link under categories page under shop skincare.
	 *  
	 *     
	 */
	@Test(enabled=true)
	public void testVerifyResultsLinkForCategoryUnderShopSkincare_349(){
		String currentURL = null;
		String sootheLinkUnderShopSkincare = "SOOTHE";
		String reverseLinkUnderShopSkincare = "REVERSE";
		String redefineLinkUnderShopSkincare = "REDEFINE";
		String unblemishLinkUnderShopSkincare = "UNBLEMISH";
		String sublinkNameUnderShopSkincare = "RESULTS";
		String expectedURLForSootheResults = "soothe-results";
		String expectedURLForReverseResults = "reverse-results";
		String expectedURLForRedefineResults = "redefine-results";
		String expectedURLForUnblemishResults = "unblemish-results";

		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),  password,true);
		//Verify FAQ page for soothe regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(sootheLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForSootheResults), "Expected URL should contain" +expectedURLForSootheResults+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Reverse regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(reverseLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForReverseResults), "Expected URL should contain" +expectedURLForReverseResults+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Redefine regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(redefineLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForRedefineResults), "Expected URL should contain" +expectedURLForRedefineResults+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Unblemish regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(unblemishLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForUnblemishResults), "Expected URL should contain" +expectedURLForUnblemishResults+ ". but actual on UI is"+currentURL);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-350 As a PC, Retail/Anon user, Consultant, I will be able to access category specific FAQ
	 * Description : This test validates 'FAQs' links for all category under shop skincare.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testVerifyFAQLinkForCategoryUnderShopSkincare_350(){
		String currentURL = null;
		String sootheLinkUnderShopSkincare = "SOOTHE";
		String reverseLinkUnderShopSkincare = "REVERSE";
		String redefineLinkUnderShopSkincare = "REDEFINE";
		String unblemishLinkUnderShopSkincare = "UNBLEMISH";
		String sublinkNameUnderShopSkincare = "FAQS";
		String expectedURLForSootheFAQ = "soothe-faq";
		String expectedURLForReverseFAQ = "reverse-faq";
		String expectedURLForRedefineFAQ = "redefine-faq";
		String expectedURLForUnblemishFAQ = "unblemish-faq";
		String expectedURLForSootheQuestions = "soothe-questions";
		String expectedURLForReverseQuestions = "reverse-questions";
		String expectedURLForRedefineQuestion = "redefine-questions";
		String expectedURLForUnblemishQuestions = "unblemish-questions";

		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(),  password,true);
		//Verify FAQ page for soothe regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(sootheLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForSootheFAQ) || currentURL.contains(expectedURLForSootheQuestions) , "Expected URL should contain" +expectedURLForSootheFAQ+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Reverse regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(reverseLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForReverseFAQ) || currentURL.contains(expectedURLForReverseQuestions), "Expected URL should contain" +expectedURLForReverseFAQ+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Redefine regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(redefineLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForRedefineFAQ) || currentURL.contains(expectedURLForRedefineQuestion), "Expected URL should contain" +expectedURLForRedefineFAQ+ ". but actual on UI is"+currentURL);
		//Verify FAQ page for Unblemish regimen.
		sfHomePage.navigateToShopSkinCareSubLinks(unblemishLinkUnderShopSkincare, sublinkNameUnderShopSkincare);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(expectedURLForUnblemishFAQ) || currentURL.contains(expectedURLForUnblemishQuestions), "Expected URL should contain" +expectedURLForUnblemishFAQ+ ". but actual on UI is"+currentURL);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-351 As a user, I will be able to view a header that will remain through the site experience
	 * Description : This test validates 'Header' view same on all pages except cart and checkout page.
	 * 
	 *     
	 */
	@Test(enabled=false)
	public void testVerifyHeaderViewIsConsistentThroughAllPagesExceptCartAndCheckout_351(){
		String reverseLinkUnderShopSkincare = "REVERSE";
		String allProductLinkUnderShopSkincare = "ALL PRODUCTS";

		//Login to application.
		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(),  password,true);
		sfHomePage.navigateToShopSkincareLink(reverseLinkUnderShopSkincare);
		s_assert.assertTrue(sfHomePage.isHeaderIsConsistentOnAllPages(),"Header is not present on reverse product category page.");
		sfShopSkinCarePage = sfHomePage.navigateToShopSkincareLink(allProductLinkUnderShopSkincare);
		s_assert.assertTrue(sfShopSkinCarePage.isHeaderIsConsistentOnAllPages(),"Header is not present on All product page.");
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfProduct(TestConstants.PRODUCT_NUMBER);
		s_assert.assertTrue(sfProductDetailPage.isHeaderIsConsistentOnAllPages(),"Header is not present on product detail page.");
		sfProductDetailPage.addProductToCartFromProductDetailPageAfterLogin();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertFalse(sfCartPage.isHeaderIsConsistentOnAllPages(),"Header is present on cart detail page.");
		sfCheckoutPage = sfCartPage.clickCheckoutBtn();
		s_assert.assertFalse(sfCheckoutPage.isHeaderIsConsistentOnAllPages(),"Header is present on checkout detail page.");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-542 As a user, I will be able to access the PLP from the Shop Skincare section 
	 * Description : This test validates team member details on executive team page.
	 *  
	 *     
	 */
	@Test(enabled=false)
	public void testAsAUserIWillBeAbleToAccessThePLPFromTheShopSkincareSection_542(){
		String category="ALL PRODUCTS";
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		s_assert.assertTrue(sfShopSkinCarePage.isProductsDisplayedOnPage(),"Expected user is not redirected to all products page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-362 Press Room 
	 * Description : This test validates press room details on press room details page
	 *  
	 *     
	 */
	@Test(enabled=false)
	public void testPressRoom_362(){
		String currentURL = null;
		String currentWindowID = null;
		String pressRoom = "Press Room";
		String pressMentions="PRESS MENTIONS";
		String companyPressRelease="COMPANY PRESS RELEASES";
		String productPressRelease="PRODUCT PRESS RELEASES";
		sfHomePage.clickFooterLink(pressRoom);
		//sfHomePage.clickPressRoomTabs(pressMentions);
		s_assert.assertTrue(sfHomePage.isPressMentionTabItemsDisplayed(),"Expected details related to press mention tab not displayed");
		sfHomePage.clickPressRoomTabs(companyPressRelease);
		s_assert.assertTrue(sfHomePage.isCompanyOrProductPressReleasesTabItemsDisplayed(companyPressRelease),"Expected details related to company press release tab not displayed");
		sfHomePage.clickPressRoomTabs(productPressRelease);
		s_assert.assertTrue(sfHomePage.isCompanyOrProductPressReleasesTabItemsDisplayed(productPressRelease),"Expected details related to product press release tab not displayed");
		sfHomePage.clickPressRoomTabs(pressMentions);
		s_assert.assertTrue(sfHomePage.isPressMentionTabItemsDisplayed(),"Expected various links related to press mention tab not displayed");
		currentWindowID = CommonUtils.getCurrentWindowHandle();
		sfHomePage.clickForbesPressLink();
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains("forbes"), "Expected URL should contain 'forbes' but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-72 Become a Consultant- Why R+F
	 * 
	 * Description : This test validates that Enroll now button on Why R+F page is redirecting to 
	 * sponsor search page
	 *     
	 */
	@Test(enabled=false)//functionality changed
	public void testBecomeAConsultantAtWhyRFPage_72(){
		sfHomePage.clickWhyRF();
		sfHomePage.clickEnrollNowButton();
		s_assert.assertTrue(sfHomePage.isSponserSearchPageDisplayed(),"Sponser search page is not displayed");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-74 Become a Consultant- Programs and Incentives
	 * 
	 * Description : This test validates that Enroll now button is redirecting to 
	 * sponsor search page through Programs and Incentives Page
	 *     
	 */
	@Test(enabled=true)
	public void testBecomeAConsultantProgramsAndIncentives_74(){
		sfHomePage.clickProgramsAndIncentives();
		sfHomePage.clickEnrollNowButton();
		s_assert.assertTrue(sfHomePage.isSponserSearchPageDisplayed(),"Sponser search page is not displayed");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-183 Product Search- Name
	 * 
	 * Description : This test validates search icon functionality using product name
	 *     
	 */
	@Test(enabled=false)
	public void testProductSearchName_183(){
		String productName = TestConstants.PRODUCT_NAME;
		String productCategory = TestConstants.PRODUCT_CATEGORY_REDEFINE;
		String partialProductName = "AGI";
		String partialProductCategory = "RED";
		String productNameFromUI = null;
		sfHomePage.clickSearchIcon();
		//Search product by category name.
		sfHomePage.searchProduct(productCategory);
		s_assert.assertTrue(sfHomePage.isProductSearchResultsPresent(),"Product search results for category Redefine not present.");
		//Search product by product name.
		sfHomePage.clickSearchIcon();
		sfHomePage.searchProduct(productName);
		productNameFromUI = sfHomePage.getProductName(TestConstants.PRODUCT_NUMBER).toLowerCase();
		productName = productName.toLowerCase();
		s_assert.assertTrue(productNameFromUI.contains(productName), "Expected product name is:"+productName+"Actual On UI is:"+productNameFromUI);
		//Search product by partial product name.
		sfHomePage.clickSearchIcon();
		sfHomePage.searchProduct(partialProductName);
		String errorMessage = sfHomePage.getErrorMessageForSearchedProduct().toLowerCase();
		s_assert.assertTrue(errorMessage.contains("sorry, we couldn't find any results for your search"), "Expected error message for 'Partial product name' is 'Sorry, we couldn't find any results for your search' Actual on UI is:"+errorMessage);
		//Search product by partial product category name.
		sfHomePage.clickSearchIcon();
		sfHomePage.searchProduct(partialProductCategory);
		errorMessage = sfHomePage.getErrorMessageForSearchedProduct().toLowerCase();
		s_assert.assertTrue(errorMessage.contains("sorry, we couldn't find any results for your search"), "Expected error message for 'Partial Product category name' is 'Sorry, we couldn't find any results for your search' Actual on UI is:"+errorMessage);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-184 Product Search - Auto-complete
	 * 
	 * Description : This test validates search icon auto complete functionality 
	 * using product name
	 *     
	 */
	@Test(enabled=false)
	public void testProductSearchAutoComplete_184(){
		String productNameThreeLetter ="AGI";
		String productNameLessThanThreeLetters = "AG";
		sfHomePage.clickSearchIcon();
		sfHomePage.enterProductName(productNameLessThanThreeLetters);
		s_assert.assertFalse(sfHomePage.isProductSearchAutoSuggestionPresent(),"Product search autosuggestion is not present for 2 letter.");
		sfHomePage.enterProductName(productNameThreeLetter);
		s_assert.assertTrue(sfHomePage.isProductSearchAutoSuggestionPresent(),"Product search autosuggestion is not present for 3 letter.");
		s_assert.assertAll();
	}

}