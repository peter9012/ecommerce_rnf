package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class FooterSectionTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-83 Careers link on footer on all pages except checkout flow
	 * 
	 * Description : This test validates Careers link on all pages except checkout page. 
	 */
	@Test(enabled=true)
	public void testVerifyCareersLinkOnAllPagesExceptCheckout_83(){
		String currentURL = null;
		String reverseLinkUnderShopSkincare = "REVERSE";
		String allProductLinkUnderShopSkincare = "ALL PRODUCTS";
		String careers = "Careers";
		String careersURL = "careers";

		//Verify careers link on product category page
		sfShopSkinCarePage = sfHomePage.navigateToShopSkincareLink(reverseLinkUnderShopSkincare);
		s_assert.assertTrue(sfShopSkinCarePage.isTheFooterLinkDisplayed(careers),"Careers link is not present in footer section on reverse category page");
		//Verify careers link on all products page
		sfShopSkinCarePage = sfHomePage.navigateToShopSkincareLink(allProductLinkUnderShopSkincare);
		s_assert.assertTrue(sfShopSkinCarePage.isTheFooterLinkDisplayed(careers),"Careers link is not present in footer section on all products page");
		//verify careers link on product detail page.
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfProduct(TestConstants.PRODUCT_NUMBER);
		s_assert.assertTrue(sfProductDetailPage.isTheFooterLinkDisplayed(careers),"Careers link is not present in footer section on product detail page");
		sfProductDetailPage.addProductToCartFromProductDetailPage();
		sfCartPage = sfProductDetailPage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isTheFooterLinkDisplayed(careers),"Careers link is not present in footer section on cart page");
		sfCheckoutPage = sfCartPage.clickCheckoutBtn();
		s_assert.assertTrue(sfCheckoutPage.isTheFooterLinkDisplayed(careers),"Careers link is not present in footer section on checkout page");
		//Click careers link.
		sfCheckoutPage.clickFooterLink(careers);
		currentURL = sfCheckoutPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(careersURL), "Expected URL should contain "+careersURL+" but actual on UI is"+currentURL);
		s_assert.assertAll();

	}
	/***
	 * qTest : TC-84 Direct Selling Association link on footer on all pages except checkout flow
	 * 
	 * Description : This test validates DSA link on all pages except checkout page. 
	 */
	@Test(enabled=true)
	public void testVerifyDSALinkOnAllPagesExceptCheckout_84(){
		String currentURL = null;
		String reverseLinkUnderShopSkincare = "REVERSE";
		String allProductLinkUnderShopSkincare = "ALL PRODUCTS";
		String directSellingAssociation= "Direct Selling Association";
		String directSellingAssociationURL = "who-we-are";

		//Verify DSA  link on product category page
		sfShopSkinCarePage = sfHomePage.navigateToShopSkincareLink(reverseLinkUnderShopSkincare);
		s_assert.assertTrue(sfShopSkinCarePage.isTheFooterLinkDisplayed(directSellingAssociation),"DSA link is not present in footer section on reverse category page");
		//Verify DSA  link on all products page
		sfShopSkinCarePage = sfHomePage.navigateToShopSkincareLink(allProductLinkUnderShopSkincare);
		s_assert.assertTrue(sfShopSkinCarePage.isTheFooterLinkDisplayed(directSellingAssociation),"DSA link is not present in footer section on all products page");
		//verify DSA  link on product detail page.
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfProduct(TestConstants.PRODUCT_NUMBER);
		s_assert.assertTrue(sfProductDetailPage.isTheFooterLinkDisplayed(directSellingAssociation),"DSA link is not present in footer section on product detail page");
		sfProductDetailPage.addProductToCartFromProductDetailPage();
		sfCartPage = sfProductDetailPage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isTheFooterLinkDisplayed(directSellingAssociation),"DSA link is not present in footer section on cart page");
		sfCheckoutPage = sfCartPage.clickCheckoutBtn();
		s_assert.assertTrue(sfCheckoutPage.isTheFooterLinkDisplayed(directSellingAssociation),"DSA link is not present in footer section on checkout page");
		//Click DSA link.
		sfCheckoutPage.clickFooterLink(directSellingAssociation);
		currentURL = sfCheckoutPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(directSellingAssociationURL), "Expected URL should contain "+directSellingAssociationURL+" but actual on UI is"+currentURL);
		s_assert.assertAll();

	}


	/***
	 * qTest : TC-98 Satisfaction Guarantee link in the footer of the page
	 * 
	 * Description : This test validates satisfaction guarantee link on all pages except checkout page. 
	 */
	@Test(enabled=true)
	public void testVerifySatisfactionGuaranteeLinkOnAllPagesExceptCheckout_98(){
		String currentWindowID = null;
		String currentURL = null;
		String reverseLinkUnderShopSkincare = "REVERSE";
		String allProductLinkUnderShopSkincare = "ALL PRODUCTS";
		String satisfactionGuarantee = "Satisfaction Guarantee";
		String satisfactionGuaranteeURL = "satisfaction-guarantee";
		String returnAuthorizationFormPage = "ReturnAuthorizationForm";
		String policyAndProcedurePage = "Policies_and_Procedures";

		//Verify satisfaction guarantee link on product category page
		sfShopSkinCarePage = sfHomePage.navigateToShopSkincareLink(reverseLinkUnderShopSkincare);
		s_assert.assertTrue(sfShopSkinCarePage.isTheFooterLinkDisplayed(satisfactionGuarantee),"Satisfaction guarantee link is not present in footer section on reverse category page");
		//Verify satisfaction guarantee link on all products page
		sfShopSkinCarePage = sfHomePage.navigateToShopSkincareLink(allProductLinkUnderShopSkincare);
		s_assert.assertTrue(sfShopSkinCarePage.isTheFooterLinkDisplayed(satisfactionGuarantee),"Satisfaction guarantee link is not present in footer section on all products page");
		//verify satisfaction guarantee link on product detail page.
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfProduct(TestConstants.PRODUCT_NUMBER);
		s_assert.assertTrue(sfProductDetailPage.isTheFooterLinkDisplayed(satisfactionGuarantee),"Satisfaction guarantee link is not present in footer section on product detail page");
		sfProductDetailPage.addProductToCartFromProductDetailPage();
		sfCartPage = sfProductDetailPage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isTheFooterLinkDisplayed(satisfactionGuarantee),"Satisfaction guarantee link is not present in footer section on cart page");
		sfCheckoutPage = sfCartPage.clickCheckoutBtn();
		s_assert.assertTrue(sfCheckoutPage.isTheFooterLinkDisplayed(satisfactionGuarantee),"Satisfaction guarantee link is not present in footer section on checkout page");
		//Click satisfaction guarantee link.
		sfCheckoutPage.clickFooterLink(satisfactionGuarantee);
		currentURL = sfCheckoutPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(satisfactionGuaranteeURL), "Expected URL should contain "+satisfactionGuaranteeURL+" but actual on UI is"+currentURL);
		//Click RA link on satisfaction guarantee page.
		currentWindowID = CommonUtils.getCurrentWindowHandle();
		sfCheckoutPage.clickReturnAuthorizationFormLinkOnSatisfactionPage();
		sfCheckoutPage.switchToChildWindow(currentWindowID);
		currentURL = sfCheckoutPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(returnAuthorizationFormPage), "Expected URL should contain "+returnAuthorizationFormPage+" but actual on UI is"+currentURL);
		sfCheckoutPage.switchToParentWindow(currentWindowID);
		//Click policies and procedures link on satisfaction guarantee page.
		sfCheckoutPage.clickPoliciesAndProceduresLinkOnSatisfactionPage();
		sfCheckoutPage.switchToChildWindow(currentWindowID);
		currentURL = sfCheckoutPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(policyAndProcedurePage), "Expected URL should contain "+policyAndProcedurePage+" but actual on UI is"+currentURL);
		sfCheckoutPage.switchToParentWindow(currentWindowID);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-23 Verify "Privacy Policy" link from the footer
	 * 
	 * Description : This test validates "Privacy Policy" link in footer
	 * 
	 */ 
	@Test(enabled=true) 
	public void testVerifyPrivacyPolicyLinkFromTheFooter_23(){
		String privacyPolicy = "Privacy Policy";
		String pageHeader="PRIVACY POLICY";
		String privacyPolicyURL = "privacy-policy";

		s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(privacyPolicy),"'Privacy Policy' link is not present in footer section");
		sfHomePage.clickFooterLink(privacyPolicy);
		String currentUrl=sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(sfHomePage.isPageHeaderDisplayed(pageHeader) && currentUrl.contains(privacyPolicyURL)," Expected 'Privacy Policy' page header not displayed or page url should contains: 'privacy-policy'"+" But actual url is:"+currentUrl);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-24 Verify "Terms and Conditions" link within the the Privacy Policy page
	 * 
	 * Description : This test validates "Terms and Conditions" link within the the Privacy Policy page
	 * 
	 */ 
	@Test(enabled=true) 
	public void testVerifyTermsAndConditionsLinkWithinThePrivacyPolicyPage_24(){
		String privacyPolicy = "Privacy Policy";
		String pageHeaderPrivacyPolicy="PRIVACY POLICY";
		String pageHeaderTermsAndCondition="TERMS & CONDITIONS";
		String termsAndCondition="Terms & Condition";
		String privacyPolicyURL = "privacy-policy";
		String termsAndConditionURL = "terms-conditions";

		s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(privacyPolicy),"'Privacy Policy' link is not present in footer section");
		sfHomePage.clickFooterLink(privacyPolicy);
		String currentUrl=sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(sfHomePage.isPageHeaderDisplayed(pageHeaderPrivacyPolicy) && currentUrl.contains(privacyPolicyURL)," Expected 'Privacy Policy' page header not displayed or page url should contains: 'privacy-policy'"+" But actual url is:"+currentUrl);
		s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(termsAndCondition),"'Terms & Condition' link is not present in footer section");
		sfHomePage.clickFooterLink(termsAndCondition);
		currentUrl=sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(sfHomePage.isPageHeaderDisplayed(pageHeaderTermsAndCondition) && currentUrl.contains(termsAndConditionURL)," Expected 'TERMS & CONDITIONS' page header not displayed or page url should contains: 'terms-conditions'"+" But actual url is:"+currentUrl);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-25 Verify Disclaimer link in footer
	 * 
	 * Description : This test validates Disclaimer link in footer
	 * 
	 */ 
	@Test(enabled=true) 
	public void testVerifyDisclaimerLinkInFooter_25(){
		String disclaimer = "Disclaimer";
		String disclaimerURL = "disclaimer";
		s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(disclaimer),"Disclaimer link is not present in footer section");
		sfHomePage.clickFooterLink(disclaimer);
		String currentUrl=sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(sfHomePage.isPageHeaderDisplayed(disclaimer) && currentUrl.contains(disclaimerURL)," Expected 'disclaimer' page header not displayed or page url should contains: 'disclaimer'"+" But actual url is:"+currentUrl);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-82 Contact us link on the all the pages except for checkout flow
	 * 
	 * Description : This test validates contact us link on all pages except checkout page. 
	 */
	@Test(enabled=true)
	public void testVerifyContactUSLinkOnAllPagesExceptCheckout_82(){
		String currentURL = null;
		String reverseLinkUnderShopSkincare = "REVERSE";
		String contactUS = "Contact Us";
		String contactUsURL = "contact";

		//Verify contact us link on product category page
		sfShopSkinCarePage = sfHomePage.navigateToShopSkincareLink(reverseLinkUnderShopSkincare);
		s_assert.assertTrue(sfShopSkinCarePage.isTheFooterLinkDisplayed(contactUS),"Contact us link is not present in footer section on reverse category page");
		//Verify contact us link on all products page
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		s_assert.assertTrue(sfShopSkinCarePage.isTheFooterLinkDisplayed(contactUS),"Contact Us link is not present in footer section on all products page");
		//verify contact us link on product detail page.
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfProduct(TestConstants.PRODUCT_NUMBER);
		s_assert.assertTrue(sfProductDetailPage.isTheFooterLinkDisplayed(contactUS),"Contact Us link is not present in footer section on product detail page");
		sfProductDetailPage.addProductToCartFromProductDetailPage();
		sfCartPage = sfProductDetailPage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isTheFooterLinkDisplayed(contactUS),"Contact Us link is not present in footer section on cart page");
		sfCheckoutPage = sfCartPage.clickCheckoutBtn();
		s_assert.assertTrue(sfCheckoutPage.isTheFooterLinkDisplayed(contactUS),"Contact Us link is not present in footer section on checkout page");
		//Click contact us link.
		sfCheckoutPage.clickFooterLink(contactUS);
		currentURL = sfCheckoutPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(contactUsURL), "Expected URL should contain "+contactUsURL+" but actual on UI is"+currentURL);
		s_assert.assertAll();

	}

	/***
	 * qTest : TC-26 Verify the links in the Disclaimer page
	 * 
	 * Description : This test validates the links in the Disclaimer page
	 * 
	 */ 
	@Test(enabled=true) 
	public void testVerifyTheLinksInTheDisclaimerPage_26(){
		String policyUrl = null;
		String incomeDisclosureUrl = null;
		String disclaimer = "Disclaimer";
		String rodanAndFieldPolicies="Rodan + Fields Policies & Procedures";
		String incomeDisclosureStatement="Income Disclosure Statement";
		if(country.equalsIgnoreCase("us")){
			policyUrl="PP_11th_Edition.pdf";
			incomeDisclosureUrl="Income-Disclosure-Statement.pdf";
		}else{
			policyUrl="Policies_Procedures_CANADA.pdf";
			incomeDisclosureUrl="IncomeDisclosure.pdf";
		}
		String currentWindowID =null;
		s_assert.assertTrue(sfHomePage.isTheFooterLinkDisplayed(disclaimer),"Disclaimer link is not present in footer section");
		sfHomePage.clickFooterLink(disclaimer);
		String currentUrl=sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(sfHomePage.isPageHeaderDisplayed(disclaimer) && currentUrl.contains("disclaimer")," Expected 'disclaimer' page header not displayed or page url should contains: 'disclaimer'"+" But actual url is:"+currentUrl);
		currentWindowID = CommonUtils.getCurrentWindowHandle();
		sfHomePage.clickDisclaimerPageLinks(rodanAndFieldPolicies);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentUrl=sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentUrl.contains(policyUrl),"Expected 'Rodan + Fields Policies & Procedures' pdf url should contain: "+policyUrl+" But actual on UI is:"+currentUrl);
		sfHomePage.switchToParentWindow(currentWindowID);
		sfHomePage.clickDisclaimerPageLinks(incomeDisclosureStatement);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentUrl=sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentUrl.contains(incomeDisclosureUrl),"Expected ' Income Disclosure Statement' pdf url should contain: "+incomeDisclosureUrl+" But actual on UI is:"+currentUrl);
		sfHomePage.switchToParentWindow(currentWindowID);
		s_assert.assertAll();
	}
}
