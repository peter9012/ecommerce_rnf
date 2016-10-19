package com.rf.test.website.storeFront.legacy;

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
import com.rf.pages.website.storeFrontLegacy.StoreFrontLegacyConsultantPage;
import com.rf.pages.website.storeFrontLegacy.StoreFrontLegacyHomePage;
import com.rf.pages.website.storeFrontLegacy.StoreFrontLegacyPCUserPage;
import com.rf.test.website.RFBrandRefreshWebsiteBaseTest;
import com.rf.test.website.RFLegacyStoreFrontWebsiteBaseTest;

public class BizPWSTest extends RFBrandRefreshWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(BizPWSTest.class.getName());

	private StoreFrontLegacyHomePage storeFrontLegacyHomePage;
	private StoreFrontLegacyConsultantPage storeFrontLegacyConsultantPage;
	private StoreFrontLegacyPCUserPage storeFrontLegacyPCUserPage;

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
		String PWS = null;
		String pcEmailId = null;
		while(true){
			randomPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PWS_SITE_URL_RFL, RFL_DB);
			PWS = (String) getValueFromQueryResult(randomPWSList, "URL");
			driver.get(PWS);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL, RFL_DB);
		pcEmailId = (String) getValueFromQueryResult(randomPCList, "UserName");
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.loginAsPCUser(pcEmailId,password);
		storeFrontLegacyHomePage.clickHeaderLinkAfterLogin("my account");
		storeFrontLegacyHomePage.clickEditOrderLink();
		storeFrontLegacyHomePage.clickChangeLinkUnderShippingToOnPWS();
		storeFrontLegacyHomePage.enterShippingProfileDetailsForPWS(addressName, shippingProfileFirstName, shippingProfileLastName, addressLine1, postalCode, phnNumber);
		storeFrontLegacyHomePage.clickUseThisAddressShippingInformationBtn();
		storeFrontLegacyHomePage.clickUseAsEnteredBtn();
		s_assert.assertTrue(storeFrontLegacyHomePage.getShippingAddressName().contains(shippingProfileFirstName),"Shipping address name expected is "+shippingProfileFirstName+" While on UI is "+storeFrontLegacyHomePage.getShippingAddressName());
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
		String PWS = null;
		while(true){
			randomPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PWS_SITE_URL_RFL, RFL_DB);
			PWS = (String) getValueFromQueryResult(randomPWSList, "URL");
			driver.get(PWS);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
		String consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontLegacyHomePage.loginAsConsultant(consultantEmailID,password);
		storeFrontLegacyHomePage.clickShopSkinCareBtnOnPWS();
		storeFrontLegacyHomePage.clickRegimenOnPWS(regimen);
		storeFrontLegacyHomePage.clickAddToCartButtonAfterLogin();
		storeFrontLegacyHomePage.mouseHoverOnMyShoppingBagLinkAndClickOnCheckoutBtn();
		storeFrontLegacyHomePage.clickCheckoutBtn();
		storeFrontLegacyHomePage.clickContinueBtn();
		storeFrontLegacyHomePage.clickChangeBillingInformationBtn();
		storeFrontLegacyHomePage.enterBillingInfo(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber);
		storeFrontLegacyHomePage.clickUseThisBillingInformationBtn();
		storeFrontLegacyHomePage.clickUseAsEnteredBtn();
		storeFrontLegacyHomePage.clickCompleteOrderBtn();
		s_assert.assertTrue(storeFrontLegacyHomePage.isThankYouTextPresentAfterOrderPlaced(), "Adhoc order not placed successfully from biz site for Consultant user.");
		s_assert.assertTrue(storeFrontLegacyHomePage.getOrderConfirmationTextMsgAfterOrderPlaced().contains("You will receive an email confirmation shortly"), "Order confirmation message does not contains email confirmation");
		String orderNumber = storeFrontLegacyHomePage.getOrderNumebrAfterOrderPlaced();
		System.out.println ("OrderNumber - "+ orderNumber);
		//verify Account status
		//orderNumberList =  DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_DETAILS, orderNumber), RFL_DB);
		//orderStatusID = String.valueOf(getValueFromQueryResult(orderNumberList, "OrderStatusID"));
		//s_assert.assertTrue(orderStatusID.contains("4"), "Order not submitted successfully");
		s_assert.assertAll();
	}

	//My account options as PC customer
	@Test
	public void testMyAccountOptionsAsPCCustomer(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomPWSList =  null;
		List<Map<String, Object>> randomPCList = null;
		String PWS = null;
		String pcEmailId = null;
		String orderHistory = "Order History";
		String editOrder = "Edit Order";
		String changeMyPCPerksStatus = "Change my PC Perks Status";
		String pCPerksFAQs = "PC Perks FAQs";
		while(true){
			randomPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PWS_SITE_URL_RFL, RFL_DB);
			PWS = (String) getValueFromQueryResult(randomPWSList, "URL");
			driver.get(PWS);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL, RFL_DB);
		pcEmailId = (String) getValueFromQueryResult(randomPCList, "UserName");
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyPCUserPage = storeFrontLegacyHomePage.loginAsPCUser(pcEmailId,password);
		storeFrontLegacyPCUserPage.clickHeaderLinkAfterLogin("my account");
		s_assert.assertTrue(storeFrontLegacyPCUserPage.isOrderManagementSublinkPresent(orderHistory), "Order History link is not present in order management");
		s_assert.assertTrue(storeFrontLegacyPCUserPage.isOrderManagementSublinkPresent(editOrder), "Edit order link is not present in order management");
		s_assert.assertTrue(storeFrontLegacyPCUserPage.isOrderManagementSublinkPresent(changeMyPCPerksStatus), "Change my pc perks status link is not present in order management");
		s_assert.assertTrue(storeFrontLegacyPCUserPage.isOrderManagementSublinkPresent(pCPerksFAQs), "PC Perks FAQs link is not present in order management");
		storeFrontLegacyPCUserPage.clickOrderManagementSublink(orderHistory);
		s_assert.assertTrue(storeFrontLegacyPCUserPage.isOrderNumberPresentAtOrderHistoryPage(), "Order number is not present at order history page");
		storeFrontLegacyPCUserPage.navigateToBackPage();
		storeFrontLegacyPCUserPage.clickOrderManagementSublink(editOrder);
		storeFrontLegacyPCUserPage.clickEditOrderBtn();
		storeFrontLegacyPCUserPage.clickAddToCartBtnForEditOrder();
		storeFrontLegacyPCUserPage.clickUpdateOrderBtn();
		storeFrontLegacyPCUserPage.clickOKBtnOfJavaScriptPopUp();
		s_assert.assertTrue(storeFrontLegacyPCUserPage.getOrderUpdateMessage().contains("successfully updated"), "Expected order update message is successfully updated but actual on UI is: "+storeFrontLegacyHomePage.getOrderUpdateMessage());
		storeFrontLegacyPCUserPage.clickMyAccountLink();
		storeFrontLegacyPCUserPage.clickOrderManagementSublink(changeMyPCPerksStatus);
		s_assert.assertTrue(storeFrontLegacyPCUserPage.isDelayOrCancelPCPerksLinkPresent(), "Delay or cancel PC perks link is not present");
		storeFrontLegacyPCUserPage.navigateToBackPage();
		storeFrontLegacyPCUserPage.clickOrderManagementSublink(pCPerksFAQs);
		s_assert.assertTrue(storeFrontLegacyPCUserPage.ispCPerksFAQsLinkRedirectingToAppropriatePage("PC-Perks-FAQs.pdf"), "PC Perks FAQs link is not redirecting to appropriate page");
		s_assert.assertAll();
	}

	//PC User termination 
	@Test
	public void testPCUserTermination(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomPWSList =  null;
		List<Map<String, Object>> randomPCList = null;
		String PWS = null;
		String pcEmailId = null;
		String changeMyPCPerksStatus = "Change my PC Perks Status";
		while(true){
			randomPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PWS_SITE_URL_RFL, RFL_DB);
			PWS = (String) getValueFromQueryResult(randomPWSList, "URL");
			driver.get(PWS);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL, RFL_DB);
		pcEmailId = (String) getValueFromQueryResult(randomPCList, "UserName");
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyPCUserPage = storeFrontLegacyHomePage.loginAsPCUser(pcEmailId,password);
		storeFrontLegacyPCUserPage.clickHeaderLinkAfterLogin("my account");
		storeFrontLegacyPCUserPage.clickOrderManagementSublink(changeMyPCPerksStatus);
		s_assert.assertTrue(storeFrontLegacyPCUserPage.isDelayOrCancelPCPerksLinkPresent(), "Delay or cancel PC perks link is not present");
		storeFrontLegacyPCUserPage.clickDelayOrCancelPCPerksLink();
		s_assert.assertTrue(storeFrontLegacyPCUserPage.isDelayOrCancelPCPerksPopupPresent(), "Cancel pc perks popup is not present");
		storeFrontLegacyPCUserPage.clickNoThanksPleaseCancelMyPCPerksAccountBtn();
		storeFrontLegacyPCUserPage.clickChangedMyMindBtn();
		s_assert.assertTrue(storeFrontLegacyPCUserPage.getCurrentURL().contains("Dashboard"), "User is not redirecting to dashboard after clicked on changed my mind button");
		storeFrontLegacyPCUserPage.clickOrderManagementSublink(changeMyPCPerksStatus);
		storeFrontLegacyPCUserPage.clickDelayOrCancelPCPerksLink();
		storeFrontLegacyPCUserPage.clickNoThanksPleaseCancelMyPCPerksAccountBtn();
		storeFrontLegacyPCUserPage.selectReasonForPCTermination();
		storeFrontLegacyPCUserPage.enterMessageForPCTermination();
		storeFrontLegacyPCUserPage.clickSendEmailToCancelBtn();
		s_assert.assertTrue(storeFrontLegacyPCUserPage.getEmailConfirmationMsgAfterPCTermination().contains("you will be receiving a confirmation e-mail shortly"), "Expected email confirmation message is: you will be receiving a confirmation e-mail shortly, but actual on UI is: "+storeFrontLegacyPCUserPage.getEmailConfirmationMsgAfterPCTermination());
		storeFrontLegacyHomePage.loginAsPCUser(pcEmailId,password);
		s_assert.assertTrue(storeFrontLegacyPCUserPage.isInvalidLoginPresent(), "PC user able to login after termination");
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
		String kitName = TestConstantsRFL.CONSULTANT_RF_EXPRESS_BUSINESS_KIT;
		String regimen = TestConstantsRFL.REGIMEN_NAME_REDEFINE;
		String enrollemntType = "Express";
		String phnNumber1 = "415";
		String phnNumber2 = "780";
		String phnNumber3 = "9099";
		List<Map<String, Object>> randomPWSList =  null;
		String PWS = "https://rfqa"+driver.getBizPWSURL();
		List<Map<String, Object>> accountStatusIDList =  null;
		String statusID = null;
		driver.get(PWS);
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickEnrollNowBtnOnbizPWSPage();
//		storeFrontLegacyHomePage.clickEnrollNowBtnAtWhyRFPage();
		storeFrontLegacyHomePage.selectConsultantEnrollmentKitByPrice(kitName);
		storeFrontLegacyHomePage.selectRegimenForConsultant(regimen);
		storeFrontLegacyHomePage.clickNextBtnAfterSelectRegimen();
		storeFrontLegacyHomePage.selectEnrollmentType(enrollemntType);
		storeFrontLegacyHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontLegacyHomePage.clickSetUpAccountNextBtn();
		storeFrontLegacyHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear);
		storeFrontLegacyHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		String bizPWS = storeFrontLegacyHomePage.getBizPWSBeforeEnrollment();
		storeFrontLegacyHomePage.clickCompleteAccountNextBtn();
		storeFrontLegacyHomePage.clickTermsAndConditions();
		storeFrontLegacyHomePage.chargeMyCardAndEnrollMe();
		s_assert.assertTrue(storeFrontLegacyHomePage.isCongratulationsMessageAppeared(),"Enrollment not completed successfully");
		//		s_assert.assertTrue(driver.getCurrentUrl().contains(bizPWS.split("\\//")[1]), "Expected biz PWS is: "+bizPWS.split("\\//")[1]+" but Actual on UI is: "+driver.getCurrentUrl());
		//		//verify Account status
		//		accountStatusIDList =  DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ACCOUNT_STATUS_ID, emailAddress), RFL_DB);
		//		statusID = String.valueOf(getValueFromQueryResult(accountStatusIDList, "StatusID"));
		//		s_assert.assertTrue(statusID.contains("1"), "Account status is not active");
		s_assert.assertAll();
	}
	//--	
	//PC Perks Delay - 30 days
	@Test(enabled=true)
	public void testPCPerksDelay30Days(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomPWSList =  null;
		List<Map<String, Object>> randomPCList = null;
		String PWS = "https://rfqa"+driver.getBizPWSURL();
		String pcEmailId = null;
		
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL, RFL_DB);
		pcEmailId = (String) getValueFromQueryResult(randomPCList, "UserName");
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.loginAsPCUser(pcEmailId, password);
		storeFrontLegacyHomePage.clickHeaderLinkAfterLogin("my account");
		storeFrontLegacyHomePage.clickChangeMyPcPerksStatus();
		storeFrontLegacyHomePage.clickDelayCancelPcPerksLink();
		storeFrontLegacyHomePage.clickPopUpYesChangeMyAutoshipDate();
		storeFrontLegacyHomePage.clickChangeAutohipDateBtn();
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyConfirmationMessage(),"confirmation msg not present as expected");
		storeFrontLegacyHomePage.clickBackToMyAccountBtn();
		storeFrontLegacyHomePage.clickEditOrderLink();
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyConfirmationMessageInOrders(),"Confirmation msg not present at orders page as expected");
		s_assert.assertAll();

	}
	//PC Perks Delay - 60 days
	@Test(enabled=true)
	public void testPCPerksDelay60Days(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomPWSList =  null;
		List<Map<String, Object>> randomPCList = null;
		String PWS = "https://rfqa"+driver.getBizPWSURL();
		String pcEmailId = null;
		

		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL, RFL_DB);
		pcEmailId = (String) getValueFromQueryResult(randomPCList, "UserName");
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.loginAsPCUser(pcEmailId, password);
		storeFrontLegacyHomePage.clickHeaderLinkAfterLogin("my account");
		storeFrontLegacyHomePage.clickChangeMyPcPerksStatus();
		storeFrontLegacyHomePage.clickDelayCancelPcPerksLink();
		storeFrontLegacyHomePage.clickPopUpYesChangeMyAutoshipDate();
		storeFrontLegacyHomePage.selectSecondRadioButton();
		storeFrontLegacyHomePage.clickChangeAutohipDateBtn();
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyConfirmationMessage(),"confirmation msg not present as expected");
		storeFrontLegacyHomePage.clickBackToMyAccountBtn();
		storeFrontLegacyHomePage.clickEditOrderLink();
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyConfirmationMessageInOrders(),"Confirmation msg not present at orders page as expected");
		s_assert.assertAll();

	}

	//Enroll a consultant using existing Prefix  
	@Test(enabled=true)
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
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		List<Map<String, Object>> randomPWSList =  null;
		List<Map<String, Object>> randomConsultant = null;
		randomConsultant = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_EXISTING_CONSULTANT_SITE_URL, RFL_DB);
		String url = (String) getValueFromQueryResult(randomConsultant, "URL");
		String PWS = null;
		String prefix = storeFrontLegacyHomePage.getSplittedPrefixFromConsultantUrl(url);
		while(true){
			randomPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PWS_SITE_URL_RFL, RFL_DB);
			PWS = (String) getValueFromQueryResult(randomPWSList, "URL");
			driver.get(PWS);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		storeFrontLegacyHomePage.clickHeaderLinkAfterLogin("BECOME A CONSULTANT");
		storeFrontLegacyHomePage.clickEnrollNowBtnOnWhyRFPage();
		storeFrontLegacyHomePage.selectEnrollmentKit(kitName);
		storeFrontLegacyHomePage.selectRegimenAndClickNext(regimen);
		storeFrontLegacyHomePage.selectEnrollmentType(enrollemntType);
		storeFrontLegacyHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontLegacyHomePage.clickSetUpAccountNextBtn();
		storeFrontLegacyHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear);
		storeFrontLegacyHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		storeFrontLegacyHomePage.enterUserPrefixInPrefixField(prefix);
		s_assert.assertTrue(storeFrontLegacyHomePage.isExistingPrefixAvailable(),"Existing prefix available in suggestion list");
		s_assert.assertAll();
	}

	//PC Perks Template Update - Add/modify products
	@Test(enabled=true)
	public void testPCPerksTemplateUpdate(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomPWSList =  null;
		List<Map<String, Object>> randomPCList = null;
		String PWS = null;
		String pcEmailId = null;
		while(true){
			randomPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PWS_SITE_URL_RFL, RFL_DB);
			PWS = (String) getValueFromQueryResult(randomPWSList, "URL");
			driver.get(PWS);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL, RFL_DB);
		pcEmailId = (String) getValueFromQueryResult(randomPCList, "UserName");
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.loginAsPCUser(pcEmailId, password);
		storeFrontLegacyHomePage.clickHeaderLinkAfterLogin("my account");
		storeFrontLegacyHomePage.clickEditOrderLink();
		storeFrontLegacyHomePage.clickEditOrderbtn();
		storeFrontLegacyHomePage.clickRemoveLinkAboveTotal();
		s_assert.assertTrue(storeFrontLegacyHomePage.isStatusMessageDisplayed(),"status message is not displayed as expected");
		storeFrontLegacyHomePage.clickAddToCartBtnForLowPriceItems();
		s_assert.assertTrue(storeFrontLegacyHomePage.isStatusMessageDisplayed(),"status message is not displayed as expected");
		storeFrontLegacyHomePage.clickAddToCartBtnForHighPriceItems();
		storeFrontLegacyHomePage.clickOnUpdateOrderBtn();
		storeFrontLegacyHomePage.handleAlertAfterUpdateOrder();
		s_assert.assertTrue(storeFrontLegacyHomePage.getConfirmationMessage().contains("Replenishment Order items successfully updated!"),"No Message appearing after order update");
		String updatedOrderTotal = storeFrontLegacyHomePage.getOrderTotal();
		storeFrontLegacyHomePage.clickSectionUnderReplenishmentOrderManagement("Overview");
		s_assert.assertTrue(storeFrontLegacyHomePage.getOrderTotalAtOverview().contains(updatedOrderTotal),"order total is not updated at overview page");
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
		String PWS = null;
		String pcEmailId = null;
		while(true){
			randomPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PWS_SITE_URL_RFL, RFL_DB);
			PWS = (String) getValueFromQueryResult(randomPWSList, "URL");
			driver.get(PWS);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFL, RFL_DB);
		pcEmailId = (String) getValueFromQueryResult(randomPCList, "UserName");
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.loginAsPCUser(pcEmailId,password);
		storeFrontLegacyHomePage.clickHeaderLinkAfterLogin("my account");
		storeFrontLegacyHomePage.clickEditOrderLink();
		storeFrontLegacyHomePage.clickSectionUnderReplenishmentOrderManagement("Billing");
		storeFrontLegacyHomePage.clickChangeBillingInformationLinkUnderBillingTabOnPWS();
		storeFrontLegacyHomePage.enterBillingInfoForPWS(billingName, billingProfileFirstName, billingProfileLastName, nameOnCard, cardNumber, expMonth, expYear, addressLine1, postalCode, phnNumber);
		storeFrontLegacyHomePage.clickUseThisBillingInformationBtn();
		storeFrontLegacyHomePage.clickUseAsEnteredBtn();
		s_assert.assertTrue(storeFrontLegacyHomePage.getOrderBillingDetailsUpdateMessage().contains("Order billing information successfully updated!"), "Expected order Billing update message is Replenishment Order billing information successfully updated! but actual on UI is: "+storeFrontLegacyHomePage.getOrderBillingDetailsUpdateMessage());
		s_assert.assertAll();
	}

	//Registering the consultant using existing consultant's email id which terminated 
	@Test(enabled=true)
	public void testRegisterUsingExistingCustomerEmailId(){
		RFL_DB = driver.getDBNameRFL();
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int ssnRandomNum1 = CommonUtils.getRandomNum(100, 999);
		int ssnRandomNum2 = CommonUtils.getRandomNum(01, 99);
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
		String PWS = null;
		String consultantEmailID = null;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_INACTIVE_CONSULTANT_EMAILID,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontLegacyHomePage.isInvalidLoginPresent(),"consultant is logged in successfully with terminated user");
		while(true){
			randomPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PWS_SITE_URL_RFL, RFL_DB);
			PWS = (String) getValueFromQueryResult(randomPWSList, "URL");
			driver.get(PWS);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		storeFrontLegacyHomePage.clickHeaderLinkAfterLogin("BECOME A CONSULTANT");
		storeFrontLegacyHomePage.clickEnrollNowBtnOnWhyRFPage();
		storeFrontLegacyHomePage.selectEnrollmentKit(kitName);
		storeFrontLegacyHomePage.selectRegimenAndClickNext(regimen);
		storeFrontLegacyHomePage.selectEnrollmentType(enrollemntType);
		storeFrontLegacyHomePage.enterEmailAddress(consultantEmailID);
		storeFrontLegacyHomePage.clickSetUpAccountNextButton();
		s_assert.assertTrue(driver.getCurrentUrl().contains("SetupAccount"), "Set up account page is not present");
		storeFrontLegacyHomePage.enterSetUpAccountInformation(firstName, lastName, consultantEmailID, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontLegacyHomePage.clickSetUpAccountNextBtn();
		storeFrontLegacyHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear);
		storeFrontLegacyHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		storeFrontLegacyHomePage.clickBillingInfoNextBtn();
		storeFrontLegacyHomePage.clickTermsAndConditions();
		storeFrontLegacyHomePage.chargeMyCardAndEnrollMe();
		s_assert.assertTrue(storeFrontLegacyHomePage.isCongratulationsMessageAppeared(),"Enrollment is not successful using terminated consultant email id.");
		s_assert.assertAll();
	}

	//Enroll a consultant using existing CA Prefix (Cross Country Sponsor)   
	@Test(enabled=true)
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

		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		List<Map<String, Object>> randomPWSList =  null;
		List<Map<String, Object>> randomConsultant = null;
		//Fetch cross country site prefix from database.
		randomConsultant = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",country,countryID), RFO_DB);
		String url = (String) getValueFromQueryResult(randomConsultant, "URL");
		String PWS = null;
		String prefix = storeFrontLegacyHomePage.getSplittedPrefixFromConsultantUrl(url);
		bizPWSToAssert = storeFrontLegacyHomePage.getModifiedPWSValue(url,availableText);
		comPWSToAssert=storeFrontLegacyHomePage.convertBizSiteToComSite(bizPWSToAssert);
		emailToAssert =storeFrontLegacyHomePage.getModifiedEmailValue(url);

		while(true){
			randomPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PWS_SITE_URL_RFL, RFL_DB);
			PWS = (String) getValueFromQueryResult(randomPWSList, "URL");
			driver.get(PWS);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		storeFrontLegacyHomePage.clickHeaderLinkAfterLogin("BECOME A CONSULTANT");
		storeFrontLegacyHomePage.clickEnrollNowBtnOnWhyRFPage();
		storeFrontLegacyHomePage.selectEnrollmentKit(kitName);
		storeFrontLegacyHomePage.selectRegimenAndClickNext(regimen);
		storeFrontLegacyHomePage.selectEnrollmentType(enrollemntType);
		storeFrontLegacyHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontLegacyHomePage.clickSetUpAccountNextBtn();
		storeFrontLegacyHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear);
		storeFrontLegacyHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		storeFrontLegacyHomePage.enterUserPrefixInPrefixField(prefix);
		s_assert.assertTrue(storeFrontLegacyHomePage.getDotComPWS().contains(comPWSToAssert),"Com pws is available for cross country user site prefix");
		s_assert.assertTrue(storeFrontLegacyHomePage.getDotBizPWS().contains(bizPWSToAssert),"Biz pws is available for cross country user site prefix");
		s_assert.assertTrue(storeFrontLegacyHomePage.getEmailId().contains(emailToAssert),"Email is available for cross country user site prefix");
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
		String PWS = null;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		while(true){
			randomPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PWS_SITE_URL_RFL, RFL_DB);
			PWS = (String) getValueFromQueryResult(randomPWSList, "URL");
			driver.get(PWS);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickEnrollNowBtnOnbizPWSPage();
		/*storeFrontLegacyHomePage.clickEnrollNowBtnOnWhyRFPage();*/
		storeFrontLegacyHomePage.selectEnrollmentKit(kitName);
		storeFrontLegacyHomePage.selectRegimenAndClickNext(regimen);
		storeFrontLegacyHomePage.selectEnrollmentType(enrollemntType);
		//Enter SetUp Account Information and validate existing consultant?
		s_assert.assertTrue(storeFrontLegacyHomePage.validateExistingConsultantPopUp(consultantEmailID),"Existing Consultant Pop Up is not displayed!!");
		s_assert.assertAll();
	}

	//Registering the consultant using preffered customer's email id 
	@Test
	public void RegisterConsultantUsingExistingPrefferedCustomerEmailId(){
		String kitName = "Big Business Launch Kit";
		String regimen = "Redefine";
		String enrollemntType = "Express";
		List<Map<String, Object>> randomPWSList =  null;
		String PWS = null;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomPCList =  null;
		String pcEmailID = null;
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_EMAILID,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCList, "EmailAddress");
		while(true){
			randomPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PWS_SITE_URL_RFL, RFL_DB);
			PWS = (String) getValueFromQueryResult(randomPWSList, "URL");
			driver.get(PWS);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickEnrollNowBtnOnbizPWSPage();
		/*storeFrontLegacyHomePage.clickEnrollNowBtnOnWhyRFPage();*/
		storeFrontLegacyHomePage.selectEnrollmentKit(kitName);
		storeFrontLegacyHomePage.selectRegimenAndClickNext(regimen);
		storeFrontLegacyHomePage.selectEnrollmentType(enrollemntType);
		//Enter SetUp Account Information and validate existing PC PopUp?
		s_assert.assertTrue(storeFrontLegacyHomePage.validateExistingPCPopUp(pcEmailID),"Existing PC Pop Up is not displayed!!");
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
		String PWS = null;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		RFL_DB = driver.getDBNameRFL();
		while(true){
			randomPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PWS_SITE_URL_RFL, RFL_DB);
			PWS = (String) getValueFromQueryResult(randomPWSList, "URL");
			driver.get(PWS);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickEnrollNowBtnOnbizPWSPage();
		/*storeFrontLegacyHomePage.clickEnrollNowBtnOnWhyRFPage();*/
		storeFrontLegacyHomePage.selectEnrollmentKit(kitName);
		storeFrontLegacyHomePage.selectRegimenAndClickNext(regimen);
		storeFrontLegacyHomePage.selectEnrollmentType(enrollemntType);
		storeFrontLegacyHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontLegacyHomePage.clickSetUpAccountNextBtn();
		storeFrontLegacyHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear);
		storeFrontLegacyHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		storeFrontLegacyHomePage.enterSpecialCharacterInWebSitePrefixField(PWSSpclChars);
		storeFrontLegacyHomePage.clickCompleteAccountNextBtn();
		storeFrontLegacyHomePage.navigateToBackPage();
		s_assert.assertFalse(storeFrontLegacyHomePage.getWebSitePrefixFieldText().contains("%"),"WebSite Prefix Field accepts Special Character");
		s_assert.assertAll();
	}

	//Registering the consultant using Retail customer's email id
	@Test
	public void RegisterConsultantUsingExistingRetailCustomerEmailId(){
		String kitName = "Big Business Launch Kit";
		String regimen = "Redefine";
		String enrollemntType = "Express";
		List<Map<String, Object>> randomPWSList =  null;
		String PWS = null;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomRCList =  null;
		String rcEmailID = null;
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_RC_EMAILID,RFL_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "EmailAddress");
		while(true){
			randomPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PWS_SITE_URL_RFL, RFL_DB);
			PWS = (String) getValueFromQueryResult(randomPWSList, "URL");
			driver.get(PWS);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickEnrollNowBtnOnbizPWSPage();
		/*storeFrontLegacyHomePage.clickEnrollNowBtnOnWhyRFPage();*/
		storeFrontLegacyHomePage.selectEnrollmentKit(kitName);
		storeFrontLegacyHomePage.selectRegimenAndClickNext(regimen);
		storeFrontLegacyHomePage.selectEnrollmentType(enrollemntType);
		//Enter SetUp Account Information and validate existing PC PopUp?
		s_assert.assertTrue(storeFrontLegacyHomePage.validateExistingRCPopUp(rcEmailID),"Existing RC Pop Up is not displayed!!");
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
		String kitName = TestConstantsRFL.CONSULTANT_RF_EXPRESS_BUSINESS_KIT;
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
//		while(true){
//			randomPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PWS_SITE_URL_RFL, RFL_DB);
//			PWS = (String) getValueFromQueryResult(randomPWSList, "URL");
//			driver.get(PWS);
//			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
//			if(isSiteNotFoundPresent){
//				continue;
//			}else{
//				break;
//			}
//		}
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickEnrollNowBtnOnbizPWSPage();
		/*storeFrontLegacyHomePage.clickEnrollNowBtnAtWhyRFPage();*/
		storeFrontLegacyHomePage.selectConsultantEnrollmentKitByPrice(kitName);
		storeFrontLegacyHomePage.selectRegimenForConsultant(regimen);
		storeFrontLegacyHomePage.clickNextBtnAfterSelectRegimen();
		storeFrontLegacyHomePage.selectEnrollmentType(enrollemntType);
		storeFrontLegacyHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontLegacyHomePage.clickSetUpAccountNextBtn();
		storeFrontLegacyHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear);
		storeFrontLegacyHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		storeFrontLegacyHomePage.clickCompleteAccountNextBtn();
		storeFrontLegacyHomePage.clickTermsAndConditions();
		storeFrontLegacyHomePage.chargeMyCardAndEnrollMe();
		s_assert.assertTrue(storeFrontLegacyHomePage.isCongratulationsMessageAppeared(),"Enrollment not completed successfully");
		storeFrontLegacyHomePage.clickOnRodanAndFieldsLogo();
		logout();
		driver.get(PWS);
		storeFrontLegacyHomePage.clickEnrollNowBtnOnbizPWSPage();
		/*storeFrontLegacyHomePage.clickEnrollNowBtnAtWhyRFPage();*/
		storeFrontLegacyHomePage.selectConsultantEnrollmentKitByPrice(kitName);
		storeFrontLegacyHomePage.selectRegimenForConsultant(regimen);
		storeFrontLegacyHomePage.clickNextBtnAfterSelectRegimen();
		storeFrontLegacyHomePage.selectEnrollmentType(enrollemntType);
		storeFrontLegacyHomePage.enterSetUpAccountInformation(firstName, lastNameForReEnrollment, emailAddressForReEnrollment, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontLegacyHomePage.clickSetUpAccountNextBtn();
		storeFrontLegacyHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear);
		storeFrontLegacyHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		s_assert.assertTrue(storeFrontLegacyHomePage.isExistingConsultantPopupPresent(), "Existing consultant popup is not present");
		storeFrontLegacyHomePage.clickCancelEnrollmentBtn();
		driver.get(PWS);
		storeFrontLegacyHomePage.clickEnrollNowBtnOnbizPWSPage();
		/*storeFrontLegacyHomePage.clickEnrollNowBtnAtWhyRFPage();*/
		storeFrontLegacyHomePage.selectConsultantEnrollmentKitByPrice(kitName);
		storeFrontLegacyHomePage.selectRegimenForConsultant(regimen);
		storeFrontLegacyHomePage.clickNextBtnAfterSelectRegimen();
		storeFrontLegacyHomePage.selectEnrollmentType(enrollemntType);
		storeFrontLegacyHomePage.enterSetUpAccountInformation(firstName, lastName2, emailAddress2, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontLegacyHomePage.clickSetUpAccountNextBtn();
		storeFrontLegacyHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear);
		storeFrontLegacyHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		s_assert.assertTrue(storeFrontLegacyHomePage.isExistingConsultantPopupPresent(), "Existing consultant popup is not present");
		storeFrontLegacyHomePage.clickSendEmailToResetMyPassword();
		s_assert.assertTrue(storeFrontLegacyHomePage.isEmailVerificationTextPresent(), "Email verification message is not present");
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
		String kitName = TestConstantsRFL.CONSULTANT_RF_EXPRESS_BUSINESS_KIT;
		String regimen = TestConstantsRFL.REGIMEN_NAME_REDEFINE;
		String enrollemntType = "Express";
		String phnNumber1 = "415";
		String phnNumber2 = "780";
		String phnNumber3 = "9099";
		String editMyPWS = "edit my pws";
		List<Map<String, Object>> randomPWSList =  null;
		String PWS = null;
		while(true){
			randomPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PWS_SITE_URL_RFL, RFL_DB);
			PWS = (String) getValueFromQueryResult(randomPWSList, "URL");
			driver.get(PWS);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickEnrollNowBtnOnbizPWSPage();
		/*storeFrontLegacyHomePage.clickEnrollNowBtnAtWhyRFPage();*/
		storeFrontLegacyHomePage.selectConsultantEnrollmentKitByPrice(kitName);
		storeFrontLegacyHomePage.selectRegimenForConsultant(regimen);
		storeFrontLegacyHomePage.clickNextBtnAfterSelectRegimen();
		storeFrontLegacyHomePage.selectEnrollmentType(enrollemntType);
		storeFrontLegacyHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontLegacyHomePage.clickSetUpAccountNextBtn();
		storeFrontLegacyHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear);
		storeFrontLegacyHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		storeFrontLegacyHomePage.clickCompleteAccountNextBtn();
		storeFrontLegacyHomePage.clickTermsAndConditions();
		storeFrontLegacyHomePage.chargeMyCardAndEnrollMe();
		s_assert.assertTrue(storeFrontLegacyHomePage.isCongratulationsMessageAppeared(),"Enrollment not completed successfully");
		storeFrontLegacyHomePage.clickOnRodanAndFieldsLogo();
		storeFrontLegacyHomePage.clickHeaderLinkAfterLogin(editMyPWS);
		storeFrontLegacyHomePage.clickEditMyPhotoLink();
		s_assert.assertTrue(storeFrontLegacyHomePage.isUploadANewPhotoButtonPresent(),"Upload a new photo button is not present");
		s_assert.assertAll();
	}

	//Registering the consultant using existing CA consultant email id (Cross County Sponsor)
	@Test
	public void RegisterConsultantUsingExistingCrossCountryConsultantEmailId(){
		RFO_DB = driver.getDBNameRFO();
		RFL_DB = driver.getDBNameRFL();
		String kitName = "Big Business Launch Kit";
		String regimen = "Redefine";
		String enrollemntType = "Express";
		String firstName = TestConstantsRFL.FIRST_NAME;
		List<Map<String, Object>> randomPWSList =  null;
		String PWS = null;
		String countryID ="40";
		String country = "ca";

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomEmailIdList =  null;
		String consultantEmailID = null;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);

		//Fetch cross country Email address from database.
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",country,countryID), RFO_DB);
		String accountID= String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		//Get email id from account id
		randomEmailIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID), RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomEmailIdList, "EmailAddress");
		while(true){
			randomPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PWS_SITE_URL_RFL, RFL_DB);
			PWS = (String) getValueFromQueryResult(randomPWSList, "URL");
			driver.get(PWS);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("SiteNotFound") || driver.getCurrentUrl().contains("SiteNotActive") || driver.getCurrentUrl().contains("Error");
			if(isSiteNotFoundPresent){
				continue;
			}else{
				break;
			}
		}
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickEnrollNowBtnOnbizPWSPage();
		/*storeFrontLegacyHomePage.clickEnrollNowBtnOnWhyRFPage();*/
		storeFrontLegacyHomePage.selectEnrollmentKit(kitName);
		storeFrontLegacyHomePage.selectRegimenAndClickNext(regimen);
		storeFrontLegacyHomePage.selectEnrollmentType(enrollemntType);
		//Enter SetUp Account Information and validate existing consultant?
		s_assert.assertTrue(storeFrontLegacyHomePage.validateExistingConsultantPopUp(consultantEmailID),"Existing Consultant Pop Up is not displayed!!");
		s_assert.assertAll();
	}
}