package com.rf.test.website.storeFront.brandRefresh.radiantMigration;

import org.testng.annotations.Test;
import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstantsRFL;
import com.rf.pages.website.storeFrontBrandRefresh.StoreFrontBrandRefreshHomePage;
import com.rf.test.website.RFBrandRefreshWebsiteBaseTest;

public class MigratedPCTest extends RFBrandRefreshWebsiteBaseTest{
	
	private StoreFrontBrandRefreshHomePage storeFrontBrandRefreshHomePage;
	
	@Test
	public void testAddShippingThroughMyAccount(){
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String addressName = "Home";
		String shippingProfileFirstName = TestConstantsRFL.SHIPPING_PROFILE_FIRST_NAME+randomNumber;
		String shippingProfileLastName = TestConstantsRFL.SHIPPING_PROFILE_LAST_NAME;
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String phnNumber = TestConstantsRFL.NEW_ADDRESS_PHONE_NUMBER_US;
		storeFrontBrandRefreshHomePage = new StoreFrontBrandRefreshHomePage(driver);
		/*storeFrontBrandRefreshHomePage.loginAsPCUser(pcEmailId,password);*/
		storeFrontBrandRefreshHomePage.loginAsPCUser("brianlloyd@gmail.com",password);
		s_assert.assertFalse(storeFrontBrandRefreshHomePage.isSignInButtonPresent(), "PC user not logged in successfully");
		storeFrontBrandRefreshHomePage.clickHeaderLinkFromTopNav();
		storeFrontBrandRefreshHomePage.clickEditOrderLink();
		storeFrontBrandRefreshHomePage.clickChangeLinkUnderShippingToOnPWS();
		storeFrontBrandRefreshHomePage.enterShippingProfileDetailsForPWS(addressName, shippingProfileFirstName, shippingProfileLastName, addressLine1, postalCode, phnNumber);
		storeFrontBrandRefreshHomePage.clickUseThisAddressShippingInformationBtn();
		storeFrontBrandRefreshHomePage.clickUseAsEnteredBtn();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getShippingAddressName().contains(shippingProfileFirstName),"Shipping address name expected is "+shippingProfileFirstName+" While on UI is "+storeFrontBrandRefreshHomePage.getShippingAddressName());
		storeFrontBrandRefreshHomePage.clickChangeLinkUnderShippingToOnPWS();
		storeFrontBrandRefreshHomePage.selectDeleteForProfileAndAcceptConfirmationAlert(shippingProfileFirstName);
		s_assert.assertFalse(storeFrontBrandRefreshHomePage.isProfilePresentInSavedProfiles(shippingProfileFirstName),"Shipping Profile "+shippingProfileFirstName+" is still present after Deletion");
		s_assert.assertAll();
	}
	
	
	@Test
	public void testAddBillingThroughMyAccount(){
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String billingName =TestConstantsRFL.BILLING_PROFILE_NAME;
		String billingProfileFirstName = TestConstantsRFL.BILLING_PROFILE_FIRST_NAME+randomNumber;
		String billingProfileLastName = TestConstantsRFL.BILLING_PROFILE_LAST_NAME;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = TestConstantsRFL.FIRST_NAME;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;

		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String phnNumber = TestConstantsRFL.NEW_ADDRESS_PHONE_NUMBER_US;
		
		storeFrontBrandRefreshHomePage = new StoreFrontBrandRefreshHomePage(driver);
		storeFrontBrandRefreshHomePage.loginAsPCUser("brianlloyd@gmail.com",password);		
		/*storeFrontBrandRefreshHomePage.loginAsPCUser(pcEmailId,password);*/
		storeFrontBrandRefreshHomePage.clickHeaderLinkFromTopNav();
		storeFrontBrandRefreshHomePage.clickEditOrderLink();
		storeFrontBrandRefreshHomePage.clickChangeBillingInformationLinkUnderBillingTabOnPWS();
		storeFrontBrandRefreshHomePage.enterBillingInfoForPWS(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber,CVV);
		storeFrontBrandRefreshHomePage.clickUseThisBillingInformationBtn();
		storeFrontBrandRefreshHomePage.clickUseAsEnteredBtn();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getBillingAddressName().contains(billingProfileFirstName), "Expected billing profile name is: "+billingProfileFirstName+" Actual on UI is: "+storeFrontBrandRefreshHomePage.getBillingAddressName());
		storeFrontBrandRefreshHomePage.clickChangeBillingInformationLinkUnderBillingTabOnPWS();
		storeFrontBrandRefreshHomePage.selectDeleteForProfileAndAcceptConfirmationAlert(billingProfileFirstName);
		s_assert.assertFalse(storeFrontBrandRefreshHomePage.isProfilePresentInSavedProfiles(billingProfileFirstName),"Billing Profile "+billingProfileFirstName+" is still present after Deletion");
		s_assert.assertAll();
	}
	
	
	@Test
	public void testAddShippingBillingWhileAdhocCheckoutForPC(){
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String firstName = TestConstantsRFL.FIRST_NAME;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = firstName;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String phnNumber = TestConstantsRFL.NEW_ADDRESS_PHONE_NUMBER_US;
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String billingName =TestConstantsRFL.BILLING_PROFILE_NAME;
		String billingProfileFirstName = TestConstantsRFL.SHIPPING_PROFILE_FIRST_NAME;
		String billingProfileLastName = TestConstantsRFL.SHIPPING_PROFILE_LAST_NAME+randomNumber;
		String regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
		String addressName = "Home";
		String shippingProfileFirstName = TestConstantsRFL.SHIPPING_PROFILE_FIRST_NAME+randomNumber;
		String shippingProfileLastName = TestConstantsRFL.SHIPPING_PROFILE_LAST_NAME;
		
		storeFrontBrandRefreshHomePage = new StoreFrontBrandRefreshHomePage(driver);
		storeFrontBrandRefreshHomePage.loginAsPCUser("brianlloyd@gmail.com", password);
		s_assert.assertFalse(storeFrontBrandRefreshHomePage.isSignInButtonPresent(), "RC user not logged in successfully");
		storeFrontBrandRefreshHomePage.clickShopSkinCareHeader();
		storeFrontBrandRefreshHomePage.selectRegimen(regimen);
		storeFrontBrandRefreshHomePage.clickAddToCartBtn();
		storeFrontBrandRefreshHomePage.clickCheckoutBtn();
		storeFrontBrandRefreshHomePage.clickChangeShippingAddressBtn();
		storeFrontBrandRefreshHomePage.enterShippingProfileDetailsForPWS(addressName, shippingProfileFirstName, shippingProfileLastName, addressLine1, postalCode, phnNumber);
		storeFrontBrandRefreshHomePage.clickUseThisAddressShippingInformationBtn();
		storeFrontBrandRefreshHomePage.clickUseAsEnteredBtn();
		storeFrontBrandRefreshHomePage.clickChangeBillingInformationBtn();
		storeFrontBrandRefreshHomePage.enterBillingInfo(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber,CVV);
		storeFrontBrandRefreshHomePage.clickUseThisBillingInformationBtn();
		storeFrontBrandRefreshHomePage.clickUseAsEnteredBtn();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getShippingAddressName().contains(shippingProfileFirstName),"Shipping address name expected is "+shippingProfileFirstName+" While on UI is "+storeFrontBrandRefreshHomePage.getShippingAddressName());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getBillingAddressName().contains(billingProfileFirstName), "Expected billing profile name is: "+billingProfileFirstName+" Actual on UI is: "+storeFrontBrandRefreshHomePage.getBillingAddressName());
		
		storeFrontBrandRefreshHomePage.clickCompleteOrderBtn();
		storeFrontBrandRefreshHomePage.clickOKBtnOnPopup();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from corp site.");
		s_assert.assertAll();
	}
	
	
	

}
