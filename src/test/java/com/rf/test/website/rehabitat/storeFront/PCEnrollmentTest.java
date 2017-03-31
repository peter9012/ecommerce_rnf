package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontCartPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontShopSkinCarePage;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class PCEnrollmentTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-451 PC User Enrollment/Checkout - Incomplete Enrollment
	 * Description : This test is for incomplete enrollment of  a PC user
	 * 
	 *     
	 */
	@Test(enabled=false)//functionality not working as expected in this tc
	public void testPCIncompleteEnrollment_451(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+"pc"+timeStamp+TestConstants.EMAIL_SUFFIX;
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_PC);
		sfCheckoutPage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(), "PC has not been enrolled partially");
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.logout();
		sfHomePage.loginToStoreFront(email,  password,true);
		s_assert.assertFalse(sfHomePage.isWelcomeUserElementDisplayed(), "Incomplete enrolled PC user "+email+"  has successfuly logged in");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-480 PC User Enrollment/Checkout - T&C checkbox selected
	 * Description : This test is for successfully enrolling a PC user
	 * with T&C checkbox Selected
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testPCEnrollmentTCCheckboxSelected_480(){
		//duplicate test,same as testPCEnrollment_452
	}

	/***
	 * qTest : TC-481 PC User Enrollment/Checkout - T&C checkbox unselected
	 *  Description : This test is for successfully enrolling a PC user
	 * with T&C checkbox NOT selected
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testPCEnrollmentTCCheckboxNotSelected_481(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+"pc"+timeStamp+TestConstants.EMAIL_SUFFIX;
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_PC);
		s_assert.assertTrue(sfCartPage.isPcOneTimeFeeMsgDisplayed(),"PC one time joining fee msg has not displayed");
		sfCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT,validProductId);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		//sfCartPage.enterQuantityOfProductAtCart("1", "10");
		//sfCartPage.clickOnUpdateLinkThroughItemNumber("1");
		sfCartPage.clickCheckoutBtn();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		//sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		if(sfCheckoutPage.hasTokenizationFailed()==true){
			sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
			sfCheckoutPage.clickBillingDetailsNextbutton();
		}
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isPopUpForTermsAndConditionsCheckboxDisplayed(), "Pop up has not been displayed after NOT selecting the T&C checkboxes");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-495 PC user can select sponsor from corp
	 * Description : This test is for checking the functionality of selecting the 
	 * sponsor during PC enrollment
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testPCEnrollmentSelectSponsor_495(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+"pc"+timeStamp+TestConstants.EMAIL_SUFFIX;
		String allProduct = "ALL PRODUCTS";
		String dummyConsultant = "dummy";
		String consultantWith2Chars = "Pu";
		String existingConsultant = TestConstants.SPONSOR;
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_PC);
		sfCheckoutPage = sfCartPage.clickCheckoutBtn();
		sfCheckoutPage.searchForConsultant(dummyConsultant);
		s_assert.assertTrue(sfCheckoutPage.isNoResultFoundMsgDisplayedForSearchedConsultant(), "No results found for msg has NOT displayed for "+dummyConsultant);
		sfCheckoutPage.searchForConsultant(consultantWith2Chars);
		s_assert.assertTrue(sfCheckoutPage.isValidationMsgDisplayedForSearchedConsultant(), "Please enter at least 3 characters msg has NOT displayed for "+consultantWith2Chars);
		sfCheckoutPage.searchForConsultant(existingConsultant);
		s_assert.assertTrue(sfCheckoutPage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfCheckoutPage.selectFirstSponsorFromList();
		s_assert.assertTrue(sfCheckoutPage.isSelectedConsultantBoxDisplayed(), "consultant box of the selected sponsor has NOT displayed");
		sfHomePage.clickRemoveLink();
		s_assert.assertFalse(sfCheckoutPage.isSelectedConsultantBoxDisplayed(), "consultant box of the selected sponsor has NOT removed");
		sfCheckoutPage.searchForConsultant(existingConsultant);
		s_assert.assertTrue(sfCheckoutPage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-573 Choose a Consultant- Auto-Assign (Checkout)
	 * Description : This test validates the assigned sponsor to PC after enrollment
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testAutoAssignConsultantPcEnrollment_573(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);  
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+"pc"+timeStamp+TestConstants.EMAIL_SUFFIX;
		String sponserName = TestConstants.SPONSOR;
		String sponserNameAfterEnrollment = null;
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_PC);
		sfCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT,validProductId);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		//sfCartPage.enterQuantityOfProductAtCart("1", "2");
		//sfCartPage.clickOnUpdateLinkThroughItemNumber("1");
		sfCartPage.clickCheckoutBtn();
		//Choose sponsor and main account fields are autofilled.
		sfCheckoutPage.searchForConsultant(sponserName);
		s_assert.assertTrue(sfCheckoutPage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sponserName = sfCheckoutPage.selectAndReturnFirstSponsorFromList();
		sfCheckoutPage.clickSaveButton();
		//Fill shipping details.
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		//sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		if(sfCheckoutPage.hasTokenizationFailed()==true){
			sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
			sfCheckoutPage.clickBillingDetailsNextbutton();
		}
		sfCheckoutPage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.selectPoliciesAndProceduresChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		sfCheckoutPage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfHomePage.hasPCEnrolledSuccessfully(), "PC has not been enrolled successfully");
		//Verify the sponsor details while placing order.
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sponserNameAfterEnrollment = sfCheckoutPage.getSponsorNameFromAccountInfo();
		s_assert.assertTrue(sponserNameAfterEnrollment.trim().toLowerCase().contains(sponserName.trim().toLowerCase()),"Sponser name is not same as provided during enrollment");
		s_assert.assertAll();
	}
	/***
	 * qTest : TC-574 Choose a Consultant- Auto-Assign (Checkout) - Change sponsor
	 * Description : This test validates the re-assigned sponsor to PC after enrollment
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testUpdateSponserDuringPCEnrollment_574(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);  
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+"pc"+timeStamp+TestConstants.EMAIL_SUFFIX;
		String sponserName = TestConstants.SPONSOR;
		String secondSponser = null;
		String sponserNameAfterEnrollment = null;
		String existingConsultant = consultantWithPulseAndWithCRP();
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_PC);
		s_assert.assertTrue(sfCartPage.isPcOneTimeFeeMsgDisplayed(),"PC one time joining fee msg has not displayed");
		sfCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT,validProductId);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		//sfCartPage.enterQuantityOfProductAtCart("1", "2");
		//sfCartPage.clickOnUpdateLinkThroughItemNumber("1");
		sfCartPage.clickCheckoutBtn();
		//Choose sponser and main account fields are autofilled.
		sfCheckoutPage.searchForConsultant(existingConsultant);
		s_assert.assertTrue(sfCheckoutPage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sponserName = sfCheckoutPage.selectAndReturnFirstSponsorFromList();
		sfCheckoutPage.clickRemoveLink();
		sfCheckoutPage.searchForConsultant(sponserName);
		secondSponser = sfCheckoutPage.selectAndReturnFirstSponsorFromList();
		sfCheckoutPage.clickSaveButton();
		//Fill shipping details.
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		if(sfCheckoutPage.hasTokenizationFailed()==true){
			sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
			sfCheckoutPage.clickBillingDetailsNextbutton();
		}
		sfCheckoutPage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.selectPoliciesAndProceduresChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		sfCheckoutPage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfHomePage.hasPCEnrolledSuccessfully(), "PC has not been enrolled successfully");
		//Verify the sponser details while placing order.
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sponserNameAfterEnrollment = sfCheckoutPage.getSponsorNameFromAccountInfo();
		s_assert.assertTrue(sponserNameAfterEnrollment.toLowerCase().contains(secondSponser.toLowerCase()),"Second sponser name is not same as provided during enrollment");
		s_assert.assertAll();
	}
	/***
	 * qTest : TC-575 Choose a Consultant- Auto-Assign (Checkout) - Second time
	 * Description : This test validates the re-assigned sponsor to PC after enrollment
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testUpdateSponserAfterPCEnrollment_575(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);  
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+"pc"+timeStamp+TestConstants.EMAIL_SUFFIX;
		String sponserName = null;
		String secondSponser = null;
		String existingConsultant = TestConstants.SPONSOR;
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_PC);
		s_assert.assertTrue(sfCartPage.isPcOneTimeFeeMsgDisplayed(),"PC one time joining fee msg has not displayed");
		sfCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT,validProductId);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		//sfCartPage.enterQuantityOfProductAtCart("1", "2");
		//sfCartPage.clickOnUpdateLinkThroughItemNumber("1");
		sfCartPage.clickCheckoutBtn();
		//Choose sponser and main account fields are autofilled.
		sfCheckoutPage.searchForConsultant(existingConsultant);
		s_assert.assertTrue(sfCheckoutPage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sponserName = sfCheckoutPage.selectAndReturnFirstSponsorFromList();
		sfCheckoutPage.clickRemoveLink();
		sfCheckoutPage.searchForConsultant(existingConsultant);
		secondSponser = sfCheckoutPage.selectAndReturnSponsorFromList("2");
		sfCheckoutPage.clickSaveButton();
		//Fill shipping details.
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		if(sfCheckoutPage.hasTokenizationFailed()==true){
			sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
			sfCheckoutPage.clickBillingDetailsNextbutton();
		}
		sfCheckoutPage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.selectPoliciesAndProceduresChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		sfCheckoutPage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfHomePage.hasPCEnrolledSuccessfully(), "PC has not been enrolled successfully");
		//Try to update sponser at main account info.
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.editMainAccountInfo();
		s_assert.assertFalse(sfCheckoutPage.isChangeSponserLinkDisplayed(),"Change sponser link is present on account info page.");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-496 PC registering from PWS will have default sposnor
	 * Description : This test validates default selected sponsor while start from PWS 
	 *     
	 */
	@Test(enabled=true)
	public void testPCRegisteringFromPWSWillHaveDefaultsponsor_496(){
		//	String allProduct = "ALL PRODUCTS";
		firstName=TestConstants.PC_FIRST_NAME;
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);  
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+"pc"+timeStamp+TestConstants.EMAIL_SUFFIX;
		String homePageURL = sfHomePage.getCurrentURL();
		String prefix = pwsPrefix();
		sfHomePage.navigateToUrl(homePageURL + "/pws/" + prefix);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfCartPage.checkoutTheCart();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_PC);
		sfCheckoutPage = sfCartPage.clickCheckoutBtn();
		s_assert.assertTrue(sfCheckoutPage.isSponsorSelected(), "Sponsor is not selected by default");
		sfCheckoutPage.clickRemoveLink();
		sfCheckoutPage.searchSponsor(TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		s_assert.assertTrue(sfCheckoutPage.isSearchedSponsorSelected(), "Searched sponsor is not selected");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-452 PC User Enrollment/Checkout - Complete Enrollment
	 * Description : This test is for successfully enrolling a PC user
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testPCEnrollmentWithPWSSponsor_452(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+"pcwpwsspon"+timeStamp+TestConstants.EMAIL_SUFFIX;
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_PC);
		//		s_assert.assertTrue(sfCartPage.isPcOneTimeFeeMsgDisplayed(),"PC one time joining fee msg has not displayed");
		sfCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT,validProductId);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		//sfCartPage.enterQuantityOfProductAtCart("1", "2");
		//sfCartPage.clickOnUpdateLinkThroughItemNumber("1");
		sfCartPage.clickCheckoutBtn();
		sfCartPage.searchSponsor(TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		if(sfCheckoutPage.hasTokenizationFailed()==true){
			sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
			sfCheckoutPage.clickBillingDetailsNextbutton();
		}
		sfCheckoutPage.selectIAcknowledgePCChkBox();
		sfCheckoutPage.selectPCTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		//if(country.equalsIgnoreCase("us"))
			//s_assert.assertTrue(sfHomePage.getCurrentURL().contains("pws"), "PC has to be on pws after enrollment");
		sfCheckoutPage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfHomePage.hasPCEnrolledSuccessfully(), "PC has not been enrolled successfully");
		pcUserWithPWSSponsor=email;
		userPropertyFile.loadProps(userProps);
		setUsers("pcUserWithPWSSponsor", pcUserWithPWSSponsor);
		s_assert.assertAll();
		System.out.println(email);
	}

	@Test(enabled=true)
	public void testPCEnrollmentWithoutSponsor(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+"pcwospon"+timeStamp+TestConstants.EMAIL_SUFFIX;
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_PC);
		s_assert.assertTrue(sfCartPage.isPcOneTimeFeeMsgDisplayed(),"PC one time joining fee msg has not displayed");
		sfCartPage.clickAddMoreItemsBtn();
		//sfShopSkinCarePage=sfCartPage.clickAllProducts();// this is a temporary patch
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT,validProductId);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		//sfCartPage.enterQuantityOfProductAtCart("1", "2");
		//sfCartPage.clickOnUpdateLinkThroughItemNumber("1");
		sfCartPage.clickCheckoutBtn();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		//		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		if(sfCheckoutPage.hasTokenizationFailed()==true){
			sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
			sfCheckoutPage.clickBillingDetailsNextbutton();
		}
		sfCheckoutPage.selectIAcknowledgePCChkBox();
		sfCheckoutPage.selectPCTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		sfCheckoutPage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfHomePage.hasPCEnrolledSuccessfully(), "PC has not been enrolled successfully");
		pcUserWithoutSponsor = email;
		userPropertyFile.loadProps(userProps);
		setUsers("pcUserWithoutSponsor", pcUserWithoutSponsor);
		s_assert.assertAll();
	}

	//For PC_EMAIL_HAVING_SINGLE_BILLING_PROFILE
	@Test(enabled=true,groups="users")
	public void testPCEnrollmentHavingSingleBillingProfile(){
		navigateToStoreFrontBaseURL();
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+"WSINGLEBILLING"+timeStamp+TestConstants.EMAIL_SUFFIX;
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_PC);
		s_assert.assertTrue(sfCartPage.isPcOneTimeFeeMsgDisplayed(),"PC one time joining fee msg has not displayed");
		sfCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT,validProductId);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		//sfCartPage.enterQuantityOfProductAtCart("1", "2");
		//sfCartPage.clickOnUpdateLinkThroughItemNumber("1");
		sfCartPage.clickCheckoutBtn();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		if(sfCheckoutPage.hasTokenizationFailed()==true){
			sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
			sfCheckoutPage.clickBillingDetailsNextbutton();
		}
		sfCheckoutPage.selectIAcknowledgePCChkBox();
		sfCheckoutPage.selectPCTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		sfCheckoutPage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfHomePage.hasPCEnrolledSuccessfully(), "PC has not been enrolled successfully");
		s_assert.assertAll();
		pcUserHavingSingleBillingProfile = email;
	}
}