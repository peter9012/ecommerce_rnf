package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class OrdersTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-180 Order History- Link to Order details
	 * Description : This test validates Details link under Actions drop down
	 *     
	 */
	@Test
	public void testOrderHistoryLinkToOrderDetails_178(){
		String detailsLink = "Details";
		String orderNumber = null;
		String currentURL = null;
		String orderDetailsText = "Order Details";
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		orderNumber = sfOrdersPage.getFirstOrderNumberFromOrderHistory();
		sfOrdersPage.chooselinkFromActionsDDUnderOrderHistoryForFirstOrder(detailsLink);
		currentURL = sfOrdersPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(orderNumber) && sfOrdersPage.isTextPresent(orderDetailsText),"Current url should contain "+orderNumber+"but actual on UI is "+currentURL+" and order details page is not present");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-227 Order History- Report a Problem- Field Validations
	 * Description : This test validates report problems page & field validations
	 *     
	 */
	@Test//incomplete
	public void testOrderHistoryReportAProblemFieldValidations_227(){
		String reportProblemsLink = "Report Problems";
		String name = TestConstants.FIRST_NAME;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		sfOrdersPage.chooselinkFromActionsDDUnderOrderHistoryForFirstOrder(reportProblemsLink);
		s_assert.assertFalse(sfOrdersPage.isNameFieldEditableAtReportProblemPage(name), "Name fiels is editable at report problem page");
		s_assert.assertTrue(sfOrdersPage.isEmailFieldPresentAtReportProblemPage(), "Email field is not present at report problem page");
		s_assert.assertTrue(sfOrdersPage.isProblemDDPresentAtReportProblemPage(), "Problem DD is not present at report problem page");
		s_assert.assertTrue(sfOrdersPage.isMessageBoxPresentAtReportProblemPage(), "Message box is not present at report problem page");
		s_assert.assertAll();
	}


	/***
	 * qTest : TC-228 Order History- Report a Problem- Confirmation page
	 * Description : This test validates return order request & details
	 *     
	 */
	@Test//incomplete
	public void testOrderHistoryReportAProblemConfirmationPage_228(){
		String reportProblemsLink = "Report Problems";
		String expectedConfirmationMessage = "Thank you. We have sent your problem to our customer service and they will review the issue and respond within 48 hours";
		String confirmationMessageFromUI = null;
		String message = "For Automation";
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		sfOrdersPage.chooselinkFromActionsDDUnderOrderHistoryForFirstOrder(reportProblemsLink);
		sfOrdersPage.enterTheDetailsForReportProblem(message);
		sfOrdersPage.clickSubmitBtnAtReportProblemPage();
		confirmationMessageFromUI = sfOrdersPage.getConfirmationMsgOfReportProblem();
		s_assert.assertTrue(confirmationMessageFromUI.contains(expectedConfirmationMessage), "Expected confirmation message is"+expectedConfirmationMessage+" but actual on UI is "+confirmationMessageFromUI);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-298 Checkout page edits - Edit Shipping Information
	 * Description : This test add a new product to cart and Edit Shipping Information
	 * 
	 *     
	 */
	@Test //incomplete User state can't be accessible in CA Environment
	public void testCheckoutPageEditsEditShippingInformation_298(){
		String randomWord = CommonUtils.getRandomWord(5);
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME+randomWord;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		String city = TestConstants.CITY_CA;
		String state = TestConstants.STATE_CA;
		String postalCode = TestConstants.POSTAL_CODE_CA;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickAllProducts();
		sfHomePage.selectFirstProduct();
		sfHomePage.checkoutThePopup();
		sfCheckoutPage=sfHomePage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickEditLinkOfShippingAddress();
		sfCheckoutPage.updateShippingAddressDetailsAtCheckoutPage(firstName, lastName, addressLine1, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickSaveButtonOfShippingAddress();
		s_assert.assertAll();	
	}

	/***
	 * qTest : TC-316 Consultants Cannot Ship to Quebec
	 * Description : This test validates Quebec province can not be select as shipping address
	 * for consultant
	 *     
	 */
	@Test
	public void testConsultantCanNotShipToQuebec_316(){
		if(country.equalsIgnoreCase("ca")){
			sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
			sfHomePage.clickAllProducts();
			sfHomePage.selectFirstProduct();
			sfHomePage.checkoutThePopup();
			sfCheckoutPage=sfHomePage.checkoutTheCart();
			sfCheckoutPage.clickSaveButton();
			sfCheckoutPage.clickAddNewShippingAddressButton();
			s_assert.assertTrue(sfCheckoutPage.isQuebecAddressDisabledForConsultant(), "Quebec province is enabled for consultant as shipping address");
			s_assert.assertAll();
		}
	}
}
