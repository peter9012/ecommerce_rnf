package com.rf.test.website.storeFront.brandRefresh;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstantsRFL;
import com.rf.core.website.constants.dbQueries.DBQueries_RFL;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.storeFrontBrandRefresh.StoreFrontBrandRefreshConsultantPage;
import com.rf.pages.website.storeFrontBrandRefresh.StoreFrontBrandRefreshHomePage;
import com.rf.pages.website.storeFrontBrandRefresh.StoreFrontBrandRefreshPCUserPage;
import com.rf.test.website.RFBrandRefreshWebsiteBaseTest;

public class BizPWSTest extends RFBrandRefreshWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(BizPWSTest.class.getName());

	private StoreFrontBrandRefreshHomePage storeFrontBrandRefreshHomePage;
	private StoreFrontBrandRefreshConsultantPage storeFrontBrandRefreshConsultantPage;
	private StoreFrontBrandRefreshPCUserPage storeFrontBrandRefreshPCUserPage;

	public BizPWSTest() {
		storeFrontBrandRefreshHomePage = new StoreFrontBrandRefreshHomePage(driver);
		storeFrontBrandRefreshConsultantPage = new StoreFrontBrandRefreshConsultantPage(driver);
		storeFrontBrandRefreshPCUserPage = new StoreFrontBrandRefreshPCUserPage(driver);
	}

	private String RFL_DB = null;
	private String RFO_DB = null;

	//PC Perks Template -  Shipping Address
	@Test 
	public void testPCPerksTemplateShippingAddressUpdate(){
		RFL_DB = driver.getDBNameRFL();
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String addressName = "Home";
		String shippingProfileFirstName = TestConstantsRFL.SHIPPING_PROFILE_FIRST_NAME+randomNumber;
		String shippingProfileLastName = TestConstantsRFL.SHIPPING_PROFILE_LAST_NAME;
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String phnNumber = TestConstantsRFL.NEW_ADDRESS_PHONE_NUMBER_US;
		List<Map<String, Object>> randomPWSList =  null;
		List<Map<String, Object>> randomPCList = null;
		String PWS = "https://rfqa"+driver.getBizPWSURL();
		driver.get(PWS);
		String pcEmailId = null;
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL, RFL_DB);
		pcEmailId = (String) getValueFromQueryResult(randomPCList, "UserName");
		storeFrontBrandRefreshHomePage.loginAsPCUser(pcEmailId,password);
		storeFrontBrandRefreshHomePage.clickHeaderLinkAfterLogin("my account");
		storeFrontBrandRefreshHomePage.clickEditOrderLink();
		storeFrontBrandRefreshHomePage.clickChangeLinkUnderShippingToOnPWS();
		storeFrontBrandRefreshHomePage.enterShippingProfileDetailsForPWS(addressName, shippingProfileFirstName, shippingProfileLastName, addressLine1, postalCode, phnNumber);
		storeFrontBrandRefreshHomePage.clickUseThisAddressShippingInformationBtn();
		storeFrontBrandRefreshHomePage.clickUseAsEnteredBtn();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getShippingAddressName().contains(shippingProfileFirstName),"Shipping address name expected is "+shippingProfileFirstName+" While on UI is "+storeFrontBrandRefreshHomePage.getShippingAddressName());
		s_assert.assertAll();
	}

	//Checkout as Consultant
	@Test 
	public void testCheckoutAsConsultant(){
		RFL_DB = driver.getDBNameRFL();
		String regimen = TestConstantsRFL.REGIMEN_NAME_REDEFINE;
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String billingName =TestConstantsRFL.BILLING_PROFILE_NAME;
		String billingProfileFirstName = TestConstantsRFL.BILLING_PROFILE_FIRST_NAME;
		String billingProfileLastName = TestConstantsRFL.BILLING_PROFILE_LAST_NAME+randomNumber;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = TestConstantsRFL.FIRST_NAME;;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String phnNumber = TestConstantsRFL.NEW_ADDRESS_PHONE_NUMBER_US;
		List<Map<String, Object>> orderNumberList =  null;
		String orderStatusID = null;
		List<Map<String, Object>> randomPWSList =  null;
		String PWS = "https://rfqa"+driver.getBizPWSURL();
		driver.get(PWS);
		while(true){
			List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
			String consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
			storeFrontBrandRefreshHomePage.loginAsConsultant(consultantEmailID,password);
			if(storeFrontBrandRefreshHomePage.isLoginFailed()){
				storeFrontBrandRefreshHomePage.refreshThePage();
				continue;
			}else{
				break;
			}
		}
		storeFrontBrandRefreshHomePage.mouseHoverShopSkinCareAndClickLinkOnPWS("CONSULTANT-ONLY PRODUCTS");
		storeFrontBrandRefreshHomePage.clickConsultantOnlyProductOnPWS(TestConstantsRFL.CONSULTANT_ONLY_BUSINESS_PROMOTION);
		storeFrontBrandRefreshHomePage.clickAddToCartButtonForEssentialsAndEnhancementsAfterLogin();
		//storeFrontBrandRefreshHomePage.mouseHoverOnMyShoppingBagLinkAndClickOnCheckoutBtn();
		storeFrontBrandRefreshHomePage.clickMyShoppingBagLink();
		storeFrontBrandRefreshHomePage.clickCheckoutBtn();
		storeFrontBrandRefreshHomePage.clickContinueBtnForPCAndRC();
		storeFrontBrandRefreshHomePage.clickChangeBillingInformationBtn();
		storeFrontBrandRefreshHomePage.enterBillingInfo(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber, CVV);
		storeFrontBrandRefreshHomePage.clickUseThisBillingInformationBtn();
		storeFrontBrandRefreshHomePage.clickUseAsEnteredBtn();
		storeFrontBrandRefreshHomePage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from biz site for Consultant user.");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getOrderConfirmationTextMsgAfterOrderPlaced().contains("You will receive an email confirmation shortly"), "Order confirmation message does not contains email confirmation");
		s_assert.assertAll();
	}

	//My account options as PC customer
	@Test 
	public void testMyAccountOptionsAsPCCustomer(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomPWSList =  null;
		List<Map<String, Object>> randomPCList = null;
		String pcEmailId = null;
		String orderHistory = "Order History";
		String editOrder = "Edit Order";
		String changeMyPCPerksStatus = "Change my PC Perks Status";
		String pCPerksFAQs = "PC Perks FAQs";
		String PWS = "https://rfqa"+driver.getBizPWSURL();
		driver.get(PWS);
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL, RFL_DB);
		pcEmailId = (String) getValueFromQueryResult(randomPCList, "UserName");
		storeFrontBrandRefreshPCUserPage = storeFrontBrandRefreshHomePage.loginAsPCUser(pcEmailId,password);
		storeFrontBrandRefreshPCUserPage.clickHeaderLinkAfterLogin("my account");
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.isOrderManagementSublinkPresent(orderHistory), "Order History link is not present in order management");
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.isOrderManagementSublinkPresent(editOrder), "Edit order link is not present in order management");
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.isOrderManagementSublinkPresent(changeMyPCPerksStatus), "Change my pc perks status link is not present in order management");
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.isOrderManagementSublinkPresent(pCPerksFAQs), "PC Perks FAQs link is not present in order management");
		storeFrontBrandRefreshPCUserPage.clickOrderManagementSublink(orderHistory);
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.isOrderNumberPresentAtOrderHistoryPage(), "Order number is not present at order history page");
		storeFrontBrandRefreshPCUserPage.navigateToBackPage();
		storeFrontBrandRefreshPCUserPage.clickOrderManagementSublink(editOrder);
		storeFrontBrandRefreshPCUserPage.clickEditOrderBtn();
		storeFrontBrandRefreshPCUserPage.clickAddToCartBtnForEditOrder();
		storeFrontBrandRefreshPCUserPage.clickUpdateOrderBtn();
		storeFrontBrandRefreshPCUserPage.clickOKBtnOfJavaScriptPopUp();
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.getOrderUpdateMessage().contains("successfully updated"), "Expected order update message is successfully updated but actual on UI is: "+storeFrontBrandRefreshPCUserPage.getOrderUpdateMessage());
		storeFrontBrandRefreshPCUserPage.clickOnRodanAndFieldsLogo();
		storeFrontBrandRefreshPCUserPage.clickHeaderLinkAfterLogin("my account");
		storeFrontBrandRefreshPCUserPage.clickOrderManagementSublink(changeMyPCPerksStatus);
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.isDelayOrCancelPCPerksLinkPresent(), "Delay or cancel PC perks link is not present");
		storeFrontBrandRefreshPCUserPage.navigateToBackPage();
		storeFrontBrandRefreshPCUserPage.clickOrderManagementSublink(pCPerksFAQs);
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.ispCPerksFAQsLinkRedirectingToAppropriatePage("PC-Perks-FAQs.pdf"), "PC Perks FAQs link is not redirecting to appropriate page");
		s_assert.assertAll();
	}

	//PC User termination 
	@Test 
	public void testPCUserTermination(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomPWSList =  null;
		List<Map<String, Object>> randomPCList = null;
		String pcEmailId = null;
		String changeMyPCPerksStatus = "Change my PC Perks Status";
		String PWS = "https://rfqa"+driver.getBizPWSURL();
		driver.get(PWS);  
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL, RFL_DB);
		pcEmailId = (String) getValueFromQueryResult(randomPCList, "UserName");
		storeFrontBrandRefreshPCUserPage = storeFrontBrandRefreshHomePage.loginAsPCUser(pcEmailId,password);
		storeFrontBrandRefreshPCUserPage.clickHeaderLinkAfterLogin("my account");
		storeFrontBrandRefreshPCUserPage.clickOrderManagementSublink(changeMyPCPerksStatus);
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.isDelayOrCancelPCPerksLinkPresent(), "Delay or cancel PC perks link is not present");
		storeFrontBrandRefreshPCUserPage.clickDelayOrCancelPCPerksLink();
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.isDelayOrCancelPCPerksPopupPresent(), "Cancel pc perks popup is not present");
		storeFrontBrandRefreshPCUserPage.clickNoThanksPleaseCancelMyPCPerksAccountBtn();
		storeFrontBrandRefreshPCUserPage.clickChangedMyMindBtn();
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.getCurrentURL().contains("Dashboard"), "User is not redirecting to dashboard after clicked on changed my mind button");
		storeFrontBrandRefreshPCUserPage.clickOrderManagementSublink(changeMyPCPerksStatus);
		storeFrontBrandRefreshPCUserPage.clickDelayOrCancelPCPerksLink();
		storeFrontBrandRefreshPCUserPage.clickNoThanksPleaseCancelMyPCPerksAccountBtn();
		storeFrontBrandRefreshPCUserPage.selectReasonForPCTermination();
		storeFrontBrandRefreshPCUserPage.enterMessageForPCTermination();
		storeFrontBrandRefreshPCUserPage.clickSendEmailToCancelBtn();
		s_assert.assertTrue(storeFrontBrandRefreshPCUserPage.getEmailConfirmationMsgAfterPCTermination().contains("you will be receiving a confirmation e-mail shortly"), "Expected email confirmation message is: you will be receiving a confirmation e-mail shortly, but actual on UI is: "+storeFrontBrandRefreshPCUserPage.getEmailConfirmationMsgAfterPCTermination());
		storeFrontBrandRefreshHomePage.loginAsPCUser(pcEmailId,password);
		s_assert.assertAll();
	}

	//Consultant enrollment-Sign up
	@Test 
	public void testConsultantEnrollmentSignUp(){
		RFL_DB = driver.getDBNameRFL();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int ssnRandomNum1 = CommonUtils.getRandomNum(100, 999);
		int ssnRandomNum2 = CommonUtils.getRandomNum(10, 99);
		int ssnRandomNum3 = CommonUtils.getRandomNum(1000, 9999);
		String firstName = TestConstantsRFL.FIRST_NAME;
		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
		String emailAddress = firstName+randomNum+"@xyz.com";
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = firstName;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String kitName = TestConstantsRFL.CONSULTANT_RFX_EXPRESS_BUSINESS_KIT;
		String regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
		String enrollemntType = "Express";
		String phnNumber1 = "415";
		String phnNumber2 = "780";
		String phnNumber3 = "9099";
		List<Map<String, Object>> randomPWSList =  null;
		String PWS = "https://rfqa"+driver.getBizPWSURL();
		List<Map<String, Object>> accountStatusIDList =  null;
		String statusID = null;
		driver.get(PWS);
		storeFrontBrandRefreshHomePage.hoverOnBeAConsultantAndClickLinkOnEnrollMe();
		storeFrontBrandRefreshHomePage.selectEnrollmentKit(kitName);
		storeFrontBrandRefreshHomePage.selectRegimenForConsultant(regimen);
		storeFrontBrandRefreshHomePage.clickNextBtnAfterSelectRegimen();
		storeFrontBrandRefreshHomePage.selectEnrollmentType(enrollemntType);
		storeFrontBrandRefreshHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontBrandRefreshHomePage.clickSetUpAccountNextBtn();
		storeFrontBrandRefreshHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear,CVV);
		storeFrontBrandRefreshHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		String bizPWS = storeFrontBrandRefreshHomePage.getBizPWSBeforeEnrollment();
		storeFrontBrandRefreshHomePage.clickCompleteAccountNextBtn();
		storeFrontBrandRefreshHomePage.clickTermsAndConditions();
		storeFrontBrandRefreshHomePage.chargeMyCardAndEnrollMe();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isCongratulationsMessageAppeared(),"Enrollment not completed successfully");
		s_assert.assertTrue(driver.getCurrentUrl().contains(bizPWS.split("\\//")[1]), "Expected biz PWS is: "+bizPWS.split("\\//")[1]+" but Actual on UI is: "+driver.getCurrentUrl());
		//verify Account status
		accountStatusIDList =  DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ACCOUNT_STATUS_ID, emailAddress), RFL_DB);
		statusID = String.valueOf(getValueFromQueryResult(accountStatusIDList, "StatusID"));
		s_assert.assertTrue(statusID.contains("1"), "Account status is not active");
		s_assert.assertAll();
	}

	//PC Perks Delay - 30 days
	@Test (enabled=true)
	public void testPCPerksDelay30Days(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomPWSList =  null;
		List<Map<String, Object>> randomPCList = null;
		String PWS = "https://rfqa"+driver.getBizPWSURL();
		String pcEmailId = null;
		driver.get(PWS);
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL, RFL_DB);
		pcEmailId = (String) getValueFromQueryResult(randomPCList, "UserName");
		storeFrontBrandRefreshHomePage.loginAsPCUser(pcEmailId, password);
		storeFrontBrandRefreshHomePage.clickHeaderLinkAfterLogin("my account");
		storeFrontBrandRefreshHomePage.clickChangeMyPcPerksStatus();
		storeFrontBrandRefreshHomePage.clickDelayCancelPcPerksLink();
		storeFrontBrandRefreshHomePage.clickPopUpYesChangeMyAutoshipDate();
		storeFrontBrandRefreshHomePage.clickChangeAutohipDateBtn();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyConfirmationMessage(),"confirmation msg not present as expected");
		storeFrontBrandRefreshHomePage.clickBackToMyAccountBtn();
		storeFrontBrandRefreshHomePage.clickEditOrderLink();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyConfirmationMessageInOrders(),"Confirmation msg not present at orders page as expected");
		s_assert.assertAll();
	}

	//PC Perks Delay - 60 days
	@Test (enabled=true)
	public void testPCPerksDelay60Days(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomPWSList =  null;
		List<Map<String, Object>> randomPCList = null;
		String PWS = "https://rfqa"+driver.getBizPWSURL();
		String pcEmailId = null;		
		driver.get(PWS);
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL, RFL_DB);
		pcEmailId = (String) getValueFromQueryResult(randomPCList, "UserName");
		storeFrontBrandRefreshHomePage.loginAsPCUser(pcEmailId, password);
		storeFrontBrandRefreshHomePage.clickHeaderLinkAfterLogin("my account");
		storeFrontBrandRefreshHomePage.clickChangeMyPcPerksStatus();
		storeFrontBrandRefreshHomePage.clickDelayCancelPcPerksLink();
		storeFrontBrandRefreshHomePage.clickPopUpYesChangeMyAutoshipDate();
		storeFrontBrandRefreshHomePage.selectSecondRadioButton();
		storeFrontBrandRefreshHomePage.clickChangeAutohipDateBtn();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyConfirmationMessage(),"confirmation msg not present as expected");
		storeFrontBrandRefreshHomePage.clickBackToMyAccountBtn();
		storeFrontBrandRefreshHomePage.clickEditOrderLink();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.verifyConfirmationMessageInOrders(),"Confirmation msg not present at orders page as expected");
		s_assert.assertAll();
	}

	//Enroll a consultant using existing Prefix  
	@Test (enabled=true)
	public void testEnrollAConsultantUsingExistingPrefix(){
		RFL_DB = driver.getDBNameRFL();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int ssnRandomNum1 = CommonUtils.getRandomNum(100, 999);
		int ssnRandomNum2 = CommonUtils.getRandomNum(00, 99);
		int ssnRandomNum3 = CommonUtils.getRandomNum(1000, 9999);
		String firstName = TestConstantsRFL.FIRST_NAME;
		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
		String emailAddress = firstName+randomNum+"@xyz.com";
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = firstName;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String kitName = "Big Business Launch Kit";
		String regimen = "Redefine";
		String enrollemntType = "Express";
		String phnNumber1 = "415";
		String phnNumber2 = "780";
		String phnNumber3 = "9099";
		List<Map<String, Object>> randomPWSList =  null;
		List<Map<String, Object>> randomConsultant = null;
		randomConsultant = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_EXISTING_CONSULTANT_SITE_URL, RFL_DB);
		String url = (String) getValueFromQueryResult(randomConsultant, "URL");
		String PWS = "https://rfqa"+driver.getBizPWSURL();
		String prefix = storeFrontBrandRefreshHomePage.getSplittedPrefixFromConsultantUrl(url);
		driver.get(PWS);
		storeFrontBrandRefreshHomePage.hoverOnBeAConsultantAndClickLinkOnEnrollMe();
		storeFrontBrandRefreshHomePage.selectEnrollmentKit(kitName);
		storeFrontBrandRefreshHomePage.selectRegimenAndClickNext(regimen);
		storeFrontBrandRefreshHomePage.selectEnrollmentType(enrollemntType);
		storeFrontBrandRefreshHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontBrandRefreshHomePage.clickSetUpAccountNextBtn();
		storeFrontBrandRefreshHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear,CVV);
		storeFrontBrandRefreshHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		storeFrontBrandRefreshHomePage.enterUserPrefixInPrefixField(prefix);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getPrefixMessageForBiz().contains("unavailable"),"Expected message is unavailable for .biz but actual on UI is: "+storeFrontBrandRefreshHomePage.getPrefixMessageForBiz());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getPrefixMessageForCom().contains("unavailable"),"Expected message is unavailable for .com but actual on UI is: "+storeFrontBrandRefreshHomePage.getPrefixMessageForCom());
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.getPrefixMessageForEmail().contains("unavailable"),"Expected message is unavailable for email but actual on UI is: "+storeFrontBrandRefreshHomePage.getPrefixMessageForEmail());
		s_assert.assertAll();
	}

	@Test (enabled=true)
	public void testPCPerksTemplateUpdate(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomPWSList =  null;
		List<Map<String, Object>> randomPCList = null;
		String PWS = "https://rfqa"+driver.getBizPWSURL();
		driver.get(PWS);
		String pcEmailId = null;
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL, RFL_DB);
		pcEmailId = (String) getValueFromQueryResult(randomPCList, "UserName");

		storeFrontBrandRefreshHomePage.loginAsPCUser(pcEmailId, password);
		storeFrontBrandRefreshHomePage.clickHeaderLinkAfterLogin("my account");
		storeFrontBrandRefreshHomePage.clickEditOrderLink();
		storeFrontBrandRefreshHomePage.clickEditOrderbtn();
		storeFrontBrandRefreshHomePage.clickRemoveLinkAboveTotal();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isStatusMessageDisplayed(),"status message is not displayed as expected");
		storeFrontBrandRefreshHomePage.clickAddToCartBtnForLowPriceItems();
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.isStatusMessageDisplayed(),"status message is not displayed as expected for add to bag order");
		storeFrontBrandRefreshHomePage.clickAddToCartBtnForHighPriceItems();
		storeFrontBrandRefreshHomePage.clickOnUpdateOrderBtn();
		storeFrontBrandRefreshHomePage.handleAlertAfterUpdateOrder();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getConfirmationMessage().contains("Replenishment Order items successfully updated!"),"No Message appearing after order update");
		String updatedOrderTotal = storeFrontBrandRefreshHomePage.getOrderTotal();
		storeFrontBrandRefreshHomePage.clickOnRodanAndFieldsLogo();
		storeFrontBrandRefreshHomePage.clickHeaderLinkAfterLogin("my account");
		storeFrontBrandRefreshHomePage.clickEditOrderLink();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getOrderTotalAtOverview().contains(updatedOrderTotal),"order total is not updated at overview page");
		s_assert.assertAll();
	}

	//PC Perks Template -  Billing Profile
	@Test 
	public void testPCPerksTemplateBillingAddressUpdate(){
		RFL_DB = driver.getDBNameRFL();
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
		List<Map<String, Object>> randomPWSList =  null;
		List<Map<String, Object>> randomPCList = null;
		String pcEmailId = null;
		String PWS = "https://rfqa"+driver.getBizPWSURL();
		driver.get(PWS);
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL, RFL_DB);
		pcEmailId = (String) getValueFromQueryResult(randomPCList, "UserName");
		storeFrontBrandRefreshHomePage.loginAsPCUser(pcEmailId,password);
		storeFrontBrandRefreshHomePage.clickHeaderLinkAfterLogin("my account");
		storeFrontBrandRefreshHomePage.clickEditOrderLink();
		//storeFrontBrandRefreshHomePage.clickSectionUnderReplenishmentOrderManagement("Billing");
		storeFrontBrandRefreshHomePage.clickChangeBillingInformationLinkUnderBillingTabOnPWS();
		storeFrontBrandRefreshHomePage.enterBillingInfoForPWS(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber,CVV);
		storeFrontBrandRefreshHomePage.clickUseThisBillingInformationBtn();
		storeFrontBrandRefreshHomePage.clickUseAsEnteredBtn();
		//s_assert.assertTrue(storeFrontBrandRefreshHomePage.getOrderBillingDetailsUpdateMessage().contains("Order billing information successfully updated!"), "Expected order Billing update message is Replenishment Order billing information successfully updated! but actual on UI is: "+storeFrontBrandRefreshHomePage.getOrderBillingDetailsUpdateMessage());
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getBillingAddressName().contains(billingProfileFirstName), "Expected billing profile name is: "+billingProfileFirstName+" Actual on UI is: "+storeFrontBrandRefreshHomePage.getBillingAddressName());
		s_assert.assertAll();
	}

	//Registering the consultant using existing consultant's email id which terminated 
	@Test (enabled=true)
	public void testRegisterUsingExistingCustomerEmailId(){
		RFL_DB = driver.getDBNameRFL();
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int ssnRandomNum1 = CommonUtils.getRandomNum(100, 999);
		int ssnRandomNum2 = CommonUtils.getRandomNum(10, 99);
		int ssnRandomNum3 = CommonUtils.getRandomNum(1000, 9999);
		String firstName = TestConstantsRFL.FIRST_NAME;
		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
		//String emailAddress = firstName+randomNum+"@xyz.com";
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = firstName;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String kitName = "Big Business Launch Kit";
		String regimen = "Redefine";
		String enrollemntType = "Express";
		String phnNumber1 = "415";
		String phnNumber2 = "780";
		String phnNumber3 = "9099";

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomPWSList =  null;
		List<Map<String, Object>> randomPCList = null;
		String consultantEmailID = null;

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_INACTIVE_CONSULTANT_EMAILID,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");

		storeFrontBrandRefreshHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isInvalidLoginPresent(),"consultant is logged in successfully with terminated user");
		String PWS = "https://rfqa"+driver.getBizPWSURL();
		driver.get(PWS);
		storeFrontBrandRefreshHomePage.hoverOnBeAConsultantAndClickLinkOnEnrollMe();
		storeFrontBrandRefreshHomePage.selectEnrollmentKit(kitName);
		storeFrontBrandRefreshHomePage.selectRegimenAndClickNext(regimen);
		storeFrontBrandRefreshHomePage.selectEnrollmentType(enrollemntType);
		storeFrontBrandRefreshHomePage.enterEmailAddress(consultantEmailID);
		storeFrontBrandRefreshHomePage.clickSetUpAccountNextButton();
		s_assert.assertTrue(driver.getCurrentUrl().contains("SetupAccount"), "Set up account page is not present");
		storeFrontBrandRefreshHomePage.enterSetUpAccountInformation(firstName, lastName, consultantEmailID, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontBrandRefreshHomePage.clickSetUpAccountNextBtn();
		storeFrontBrandRefreshHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear,CVV);
		storeFrontBrandRefreshHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		storeFrontBrandRefreshHomePage.clickBillingInfoNextBtn();
		storeFrontBrandRefreshHomePage.clickTermsAndConditions();
		storeFrontBrandRefreshHomePage.chargeMyCardAndEnrollMe();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isCongratulationsMessageAppeared(),"Enrollment is not successful using terminated consultant email id.");
		//verify Account status
		List<Map<String, Object>> accountStatusIDList =  null;
		String statusID = null;
		accountStatusIDList =  DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ACCOUNT_STATUS_ID, consultantEmailID), RFL_DB);
		statusID = String.valueOf(getValueFromQueryResult(accountStatusIDList, "StatusID"));
		s_assert.assertTrue(statusID.contains("1"), "Account status is not active");
		s_assert.assertAll();
	}

	//Enroll a consultant using existing CA Prefix (Cross Country Sponsor)   
	@Test (enabled=true)
	public void testEnrollAConsultantUsingExistingCAPrefix(){
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int ssnRandomNum1 = CommonUtils.getRandomNum(100, 999);
		int ssnRandomNum2 = CommonUtils.getRandomNum(00, 99);
		int ssnRandomNum3 = CommonUtils.getRandomNum(1000, 9999);
		String firstName = TestConstantsRFL.FIRST_NAME;
		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
		String emailAddress = firstName+randomNum+"@xyz.com";
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = firstName;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String kitName = "Big Business Launch Kit";
		String regimen = "Redefine";
		String enrollemntType = "Express";
		String phnNumber1 = "415";
		String phnNumber2 = "780";
		String phnNumber3 = "9099";
		String countryID ="40";
		String country = "ca";
		String bizPWSToAssert = null;
		String comPWSToAssert = null;
		String emailToAssert = null;
		String availableText = " is unavailable";
		String dbIP2 = driver.getDBIP2();

		List<Map<String, Object>> randomPWSList =  null;
		List<Map<String, Object>> randomConsultant = null;
		//Fetch cross country site prefix from database.
		randomConsultant = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",country,countryID), RFO_DB, dbIP2);
		String url = (String) getValueFromQueryResult(randomConsultant, "URL");
		String prefix = storeFrontBrandRefreshHomePage.getSplittedPrefixFromConsultantUrl(url);
		bizPWSToAssert = storeFrontBrandRefreshHomePage.getModifiedPWSValue(url,availableText);
		comPWSToAssert=storeFrontBrandRefreshHomePage.convertBizSiteToComSite(bizPWSToAssert);
		emailToAssert =storeFrontBrandRefreshHomePage.getModifiedEmailValue(url);
		String PWS = "https://rfqa"+driver.getBizPWSURL();
		driver.get(PWS);
		storeFrontBrandRefreshHomePage.clickHeaderLinkAfterLogin("BECOME A CONSULTANT");

		storeFrontBrandRefreshHomePage.hoverOnBeAConsultantAndClickLinkOnEnrollMe();
		storeFrontBrandRefreshHomePage.selectEnrollmentKit(kitName);
		storeFrontBrandRefreshHomePage.selectRegimenAndClickNext(regimen);
		storeFrontBrandRefreshHomePage.selectEnrollmentType(enrollemntType);
		storeFrontBrandRefreshHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontBrandRefreshHomePage.clickSetUpAccountNextBtn();
		storeFrontBrandRefreshHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear,CVV);
		storeFrontBrandRefreshHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		storeFrontBrandRefreshHomePage.enterUserPrefixInPrefixField(prefix);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getDotComPWS().contains(comPWSToAssert),"Com pws is available for cross country user site prefix");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getDotBizPWS().contains(bizPWSToAssert),"Biz pws is available for cross country user site prefix");
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.getEmailId().contains(emailToAssert),"Email is available for cross country user site prefix");
		s_assert.assertAll();
	}

	//Registering the consultant using existing consultant's email id
	@Test 
	public void RegisterConsultantUsingExistingConsultantEmailId(){
		String kitName = "Big Business Launch Kit";
		String regimen = "Redefine";
		String enrollemntType = "Express";
		String firstName = TestConstantsRFL.FIRST_NAME;
		List<Map<String, Object>> randomPWSList =  null;
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		String PWS = "https://rfqa"+driver.getBizPWSURL();
		driver.get(PWS);
		storeFrontBrandRefreshHomePage.hoverOnBeAConsultantAndClickLinkOnEnrollMe();
		storeFrontBrandRefreshHomePage.selectEnrollmentKit(kitName);
		storeFrontBrandRefreshHomePage.selectRegimenAndClickNext(regimen);
		storeFrontBrandRefreshHomePage.selectEnrollmentType(enrollemntType);
		//Enter SetUp Account Information and validate existing consultant?
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateExistingConsultantPopUp(consultantEmailID),"Existing Consultant Pop Up is not displayed!!");
		s_assert.assertAll();
	}

	//Registering the consultant using preffered customer's email id 
	@Test 
	public void RegisterConsultantUsingExistingPrefferedCustomerEmailId(){
		String kitName = "Big Business Launch Kit";
		String regimen = "Redefine";
		String enrollemntType = "Express";
		List<Map<String, Object>> randomPWSList =  null;
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomPCList =  null;
		String pcEmailID = null;
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_EMAILID,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCList, "EmailAddress");
		String PWS = "https://rfqa"+driver.getBizPWSURL();
		driver.get(PWS);

		storeFrontBrandRefreshHomePage.hoverOnBeAConsultantAndClickLinkOnEnrollMe();
		storeFrontBrandRefreshHomePage.selectEnrollmentKit(kitName);
		storeFrontBrandRefreshHomePage.selectRegimenAndClickNext(regimen);
		storeFrontBrandRefreshHomePage.selectEnrollmentType(enrollemntType);
		//Enter SetUp Account Information and validate existing PC PopUp?
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateExistingPCPopUp(pcEmailID),"Existing PC Pop Up is not displayed!!");
		s_assert.assertAll();
	}

	//Enroll a consultant using special characters in the prefix field
	@Test 
	public void testEnrollConsultantUsingSpecialCharsInPrefixField(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int ssnRandomNum1 = CommonUtils.getRandomNum(100, 999);
		int ssnRandomNum2 = CommonUtils.getRandomNum(10, 99);
		int ssnRandomNum3 = CommonUtils.getRandomNum(1000, 9999);
		String firstName = TestConstantsRFL.FIRST_NAME;
		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
		String emailAddress = firstName+randomNum+"@xyz.com";
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = firstName;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String PWSSpclChars = TestConstantsRFL.PWS_SPCLCHARS;
		String kitName = "Big Business Launch Kit";
		String regimen = "Redefine";
		String enrollemntType = "Express";
		String phnNumber1 = "415";
		String phnNumber2 = "780";
		String phnNumber3 = "9099";
		List<Map<String, Object>> randomPWSList =  null;
		RFL_DB = driver.getDBNameRFL();
		String PWS = "https://rfqa"+driver.getBizPWSURL();
		driver.get(PWS);
		storeFrontBrandRefreshHomePage.hoverOnBeAConsultantAndClickLinkOnEnrollMe();
		storeFrontBrandRefreshHomePage.selectEnrollmentKit(kitName);
		storeFrontBrandRefreshHomePage.selectRegimenAndClickNext(regimen);
		storeFrontBrandRefreshHomePage.selectEnrollmentType(enrollemntType);
		storeFrontBrandRefreshHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontBrandRefreshHomePage.clickSetUpAccountNextBtn();
		storeFrontBrandRefreshHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear,CVV);
		storeFrontBrandRefreshHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		storeFrontBrandRefreshHomePage.enterSpecialCharacterInWebSitePrefixField(PWSSpclChars);
		storeFrontBrandRefreshHomePage.clickCompleteAccountNextBtn();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isValidationMessagePresentForPrefixField(),"WebSite Prefix Field accepts Special Character");
		s_assert.assertAll();
	}

	//Registering the consultant using Retail customer's email id
	@Test 
	public void RegisterConsultantUsingExistingRetailCustomerEmailId(){
		String kitName = "Big Business Launch Kit";
		String regimen = "Redefine";
		String enrollemntType = "Express";
		List<Map<String, Object>> randomPWSList =  null;
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomRCList =  null;
		String rcEmailID = null;
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_RC_EMAILID,RFL_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "EmailAddress");
		String PWS = "https://rfqa"+driver.getBizPWSURL();
		driver.get(PWS);
		storeFrontBrandRefreshHomePage.hoverOnBeAConsultantAndClickLinkOnEnrollMe();
		storeFrontBrandRefreshHomePage.selectEnrollmentKit(kitName);
		storeFrontBrandRefreshHomePage.selectRegimenAndClickNext(regimen);
		storeFrontBrandRefreshHomePage.selectEnrollmentType(enrollemntType);
		//Enter SetUp Account Information and validate existing PC PopUp?
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateExistingRCPopUp(rcEmailID),"Existing RC Pop Up is not displayed!!");
		s_assert.assertAll();
	}

	//Register the consultant using existing consultant's SSN
	@Test 
	public void testRegisterTheConsultantUsingExistingConsultantSSN(){
		RFL_DB = driver.getDBNameRFL();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum2 = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum3 = CommonUtils.getRandomNum(10000, 1000000);
		int ssnRandomNum1 = CommonUtils.getRandomNum(100, 999);
		int ssnRandomNum2 = CommonUtils.getRandomNum(10, 99);
		int ssnRandomNum3 = CommonUtils.getRandomNum(1000, 9999);
		String firstName = TestConstantsRFL.FIRST_NAME;
		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
		String emailAddress = firstName+randomNum+"@xyz.com";
		String lastNameForReEnrollment = TestConstantsRFL.LAST_NAME+randomNum2;
		String emailAddressForReEnrollment = firstName+randomNum2+"@xyz.com";
		String lastName2 = TestConstantsRFL.LAST_NAME+randomNum3;
		String emailAddress2 = firstName+randomNum3+"@xyz.com";
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = firstName;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String kitName = TestConstantsRFL.CONSULTANT_RFX_EXPRESS_BUSINESS_KIT;
		String regimen = TestConstantsRFL.REGIMEN_NAME_REDEFINE;
		String enrollemntType = "Express";
		String phnNumber1 = "415";
		String phnNumber2 = "780";
		String phnNumber3 = "9099";
		String editMyPWS = "edit my pws";
		List<Map<String, Object>> randomPWSList =  null;
		String pwsBizBase = driver.getBizPWSURL();
		String PWS = "https://rfqa"+pwsBizBase;
		driver.get(PWS);
		storeFrontBrandRefreshHomePage.hoverOnBeAConsultantAndClickLinkOnEnrollMe();
		storeFrontBrandRefreshHomePage.selectEnrollmentKit(kitName);
		storeFrontBrandRefreshHomePage.selectRegimenForConsultant(regimen);
		storeFrontBrandRefreshHomePage.clickNextBtnAfterSelectRegimen();
		storeFrontBrandRefreshHomePage.selectEnrollmentType(enrollemntType);
		storeFrontBrandRefreshHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontBrandRefreshHomePage.clickSetUpAccountNextBtn();
		storeFrontBrandRefreshHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear,CVV);
		storeFrontBrandRefreshHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		storeFrontBrandRefreshHomePage.clickCompleteAccountNextBtn();
		storeFrontBrandRefreshHomePage.clickTermsAndConditions();
		storeFrontBrandRefreshHomePage.chargeMyCardAndEnrollMe();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isCongratulationsMessageAppeared(),"Enrollment not completed successfully");
		storeFrontBrandRefreshHomePage.clickOnRodanAndFieldsLogo();
		logout();
		driver.get(PWS);
		storeFrontBrandRefreshHomePage.hoverOnBeAConsultantAndClickLinkOnEnrollMe();
		storeFrontBrandRefreshHomePage.selectEnrollmentKit(kitName);
		storeFrontBrandRefreshHomePage.selectRegimenForConsultant(regimen);
		storeFrontBrandRefreshHomePage.clickNextBtnAfterSelectRegimen();
		storeFrontBrandRefreshHomePage.selectEnrollmentType(enrollemntType);
		storeFrontBrandRefreshHomePage.enterSetUpAccountInformation(firstName, lastNameForReEnrollment, emailAddressForReEnrollment, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontBrandRefreshHomePage.clickSetUpAccountNextBtn();
		storeFrontBrandRefreshHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear,CVV);
		storeFrontBrandRefreshHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isExistingConsultantPopupPresent(), "Existing consultant popup is not present");
		storeFrontBrandRefreshHomePage.clickCancelEnrollmentBtn();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isLoginButtonPresent(),"User is not redirected to home page after clicked on cancel enrollment button");
		driver.get(PWS);
		storeFrontBrandRefreshHomePage.hoverOnBeAConsultantAndClickLinkOnEnrollMe();
		/*storeFrontLegacyHomePage.clickEnrollNowBtnAtWhyRFPage();*/
		storeFrontBrandRefreshHomePage.selectEnrollmentKit(kitName);
		storeFrontBrandRefreshHomePage.selectRegimenForConsultant(regimen);
		storeFrontBrandRefreshHomePage.clickNextBtnAfterSelectRegimen();
		storeFrontBrandRefreshHomePage.selectEnrollmentType(enrollemntType);
		storeFrontBrandRefreshHomePage.enterSetUpAccountInformation(firstName, lastName2, emailAddress2, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontBrandRefreshHomePage.clickSetUpAccountNextBtn();
		storeFrontBrandRefreshHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear,CVV);
		storeFrontBrandRefreshHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isExistingConsultantPopupPresent(), "Existing consultant popup is not present");
		storeFrontBrandRefreshHomePage.clickSendEmailToResetMyPassword();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isEmailVerificationTextPresent(), "Email verification message is not present");
		s_assert.assertAll();
	}

	//My account- As a consultant customer
	@Test 
	public void testMyAccountAsAConsultantCustomer(){
		RFL_DB = driver.getDBNameRFL();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int ssnRandomNum1 = CommonUtils.getRandomNum(100, 999);
		int ssnRandomNum2 = CommonUtils.getRandomNum(10, 99);
		int ssnRandomNum3 = CommonUtils.getRandomNum(1000, 9999);
		String firstName = TestConstantsRFL.FIRST_NAME;
		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
		String emailAddress = firstName+randomNum+"@xyz.com";
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = firstName;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String kitName = TestConstantsRFL.CONSULTANT_RFX_EXPRESS_BUSINESS_KIT;
		String regimen = TestConstantsRFL.REGIMEN_NAME_REDEFINE;
		String enrollemntType = "Express";
		String phnNumber1 = "415";
		String phnNumber2 = "780";
		String phnNumber3 = "9099";
		String editMyPWS = "edit my pws";
		List<Map<String, Object>> randomPWSList =  null;
		String PWS = "https://rfqa"+driver.getBizPWSURL();
		driver.get(PWS);

		storeFrontBrandRefreshHomePage.hoverOnBeAConsultantAndClickLinkOnEnrollMe();
		storeFrontBrandRefreshHomePage.selectEnrollmentKit(kitName);
		storeFrontBrandRefreshHomePage.selectRegimenForConsultant(regimen);
		storeFrontBrandRefreshHomePage.clickNextBtnAfterSelectRegimen();
		storeFrontBrandRefreshHomePage.selectEnrollmentType(enrollemntType);
		storeFrontBrandRefreshHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontBrandRefreshHomePage.clickSetUpAccountNextBtn();
		storeFrontBrandRefreshHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear,CVV);
		storeFrontBrandRefreshHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		storeFrontBrandRefreshHomePage.clickCompleteAccountNextBtn();
		storeFrontBrandRefreshHomePage.clickTermsAndConditions();
		storeFrontBrandRefreshHomePage.chargeMyCardAndEnrollMe();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isCongratulationsMessageAppeared(),"Enrollment not completed successfully");
		storeFrontBrandRefreshHomePage.clickOnRodanAndFieldsLogo();
		storeFrontBrandRefreshHomePage.clickHeaderLinkAfterLogin(editMyPWS);
		storeFrontBrandRefreshHomePage.clickEditMyPhotoLink();
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.isUploadANewPhotoButtonPresent(),"Upload a new photo button is not present");
		s_assert.assertAll();
	}

	//Registering the consultant using existing CA consultant email id (Cross County Sponsor)
	@Test 
	public void RegisterConsultantUsingExistingCrossCountryConsultantEmailId(){
		RFO_DB = driver.getDBNameRFO();
		RFL_DB = driver.getDBNameRFL();
		String dbIP2 = driver.getDBIP2();
		String kitName = "Big Business Launch Kit";
		String regimen = "Redefine";
		String enrollemntType = "Express";
		String firstName = TestConstantsRFL.FIRST_NAME;
		List<Map<String, Object>> randomPWSList =  null;
		String countryID ="40";
		String country = "ca";
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomEmailIdList =  null;
		String consultantEmailID = null;
		//Fetch cross country Email address from database.
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",country,countryID), RFO_DB, dbIP2);
		String accountID= String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		//Get email id from account id
		randomEmailIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID), RFO_DB,dbIP2);
		consultantEmailID = (String) getValueFromQueryResult(randomEmailIdList, "EmailAddress");
		String PWS = "https://rfqa"+driver.getBizPWSURL();
		driver.get(PWS);
		storeFrontBrandRefreshHomePage.hoverOnBeAConsultantAndClickLinkOnEnrollMe();
		storeFrontBrandRefreshHomePage.selectEnrollmentKit(kitName);
		storeFrontBrandRefreshHomePage.selectRegimenAndClickNext(regimen);
		storeFrontBrandRefreshHomePage.selectEnrollmentType(enrollemntType);
		//Enter SetUp Account Information and validate existing consultant?
		s_assert.assertTrue(storeFrontBrandRefreshHomePage.validateExistingConsultantPopUp(consultantEmailID),"Existing Consultant Pop Up is not displayed!!");
		s_assert.assertAll();
	}
}