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
import com.rf.pages.website.storeFrontLegacy.StoreFrontLegacyConsultantPage;
import com.rf.pages.website.storeFrontLegacy.StoreFrontLegacyHomePage;
import com.rf.pages.website.storeFrontLegacy.StoreFrontLegacyPCUserPage;
import com.rf.test.website.RFLegacyStoreFrontWebsiteBaseTest;

public class BizPWSTest extends RFLegacyStoreFrontWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(BizPWSTest.class.getName());

	private StoreFrontLegacyHomePage storeFrontLegacyHomePage;
	private StoreFrontLegacyConsultantPage storeFrontLegacyConsultantPage;
	private StoreFrontLegacyPCUserPage storeFrontLegacyPCUserPage;

	private String RFL_DB = null;

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
		//verify Account status
		orderNumberList =  DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_DETAILS, orderNumber), RFL_DB);
		orderStatusID = String.valueOf(getValueFromQueryResult(orderNumberList, "OrderStatusID"));
		s_assert.assertTrue(orderStatusID.contains("4"), "Order not submitted successfully");
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

	
}