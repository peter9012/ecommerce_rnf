package com.rf.test.website.storeFront.regression;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontCartAutoShipPage;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.pages.website.storeFront.StoreFrontShippingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class CartAndCheckoutValidationTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(CartAndCheckoutValidationTest.class.getName());
	public String emailID=null;
	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontShippingInfoPage storeFrontShippingInfoPage;
	private String kitName = null;
	private String regimenName = null;
	private String enrollmentType = null;
	private String addressLine1 = null;
	private String city = null;
	private String postalCode = null;
	private String phoneNumber = null;
	private String country = null;
	private String RFO_DB = null;
	private String env = null;

	//Hybris Project-2327 :: Version : 1 :: check Mini Cart - Not Logged In user
	@Test
	public void testCheckMiniCartForNotLoggedInUser_2327() throws InterruptedException {
		//Navigate to the website
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//validate no mini cart is shown for not logged in user
		s_assert.assertFalse(storeFrontHomePage.validateMiniCart(), "mini cart is displayed for not registered user");
		//Add a item to the cart and validate the mini cart in the header section
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		s_assert.assertTrue(storeFrontHomePage.validateMiniCart(), "mini cart is not being displayed");
		s_assert.assertAll();
	}

	//Hybris Project-2329 :: Version : 1 :: check Mini Cart - Consultant 
	@Test
	public void testCheckMiniCartForConsultant_2329() throws InterruptedException  {
		//Login as consultant and validate the 'Next CRP' mini cart in the header section
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		s_assert.assertTrue(storeFrontConsultantPage.validateNextCRPMiniCart(), "next CRP Mini cart is not displayed");
		//Add a item to the cart and validate the mini cart in the header section
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();  

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		s_assert.assertTrue(storeFrontHomePage.validateMiniCart(), "mini cart is not being displayed");
		s_assert.assertAll();
	}

	//Hybris Project-2328 :: Version : 1 :: check Mini Cart - PC 
	@Test
	public void testCheckMiniCartForPC_2328() throws InterruptedException {
		//Login as consultant and validate the 'Next PC PERKS' mini cart in the header section
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);  
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		logger.info("login is successful");

		s_assert.assertTrue(storeFrontPCUserPage.validateNextPCPerksMiniCart(), "next PC Perks  Mini cart is not displayed");
		//Add a item to the cart and validate the mini cart in the header section
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		s_assert.assertTrue(storeFrontHomePage.validateMiniCart(), "mini cart is not being displayed");
		s_assert.assertAll();
	}

	// Hybris Project-143:CRP Template - Check Threshold by Add/remove products and increase/decrease qty
	@Test(enabled=true) 
	public void testCheckThresholdByAddRemoveProductsAndIncreaseDecreaseQty_143() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 

		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String qtyOfProducts="10";
		String newQtyOfProducts="5";

		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		storeFrontHomePage.clickOnAutoshipCart();
		storeFrontUpdateCartPage= new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnContinueShoppingLink();
		storeFrontUpdateCartPage.selectDifferentProductAndAddItToCRP();   
		int noOfProduct = storeFrontUpdateCartPage.getNoOfProductInCart();   
		for(int i=noOfProduct; i>=1; i--){
			boolean flag = storeFrontUpdateCartPage.getValueOfFlag(i);
			if(flag==true){
				double SVValue = Double.parseDouble(storeFrontUpdateCartPage.getSVValueFromCart().trim());
				String SVValueOfRemovedProduct = storeFrontUpdateCartPage.removeProductSFromCart(i);

				double remainingSVValue = storeFrontUpdateCartPage.compareSVValue(SVValueOfRemovedProduct, SVValue);
				if(driver.getCountry().equalsIgnoreCase("us")){
					if(remainingSVValue>=80.00){
						s_assert.assertTrue(storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG),"Error message for product removal from UI is "+i+storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG);

					}else{
						s_assert.assertTrue(storeFrontHomePage.getThresholdMessageIsDisplayed().contains(TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG_FOR_CONSULTANT),"Error message for threshold condition for zero quantity from UI is  "+i+storeFrontHomePage.getThresholdMessageIsDisplayed()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG_FOR_CONSULTANT);
						break;
					}
				}else 
					if(remainingSVValue>=100.00){
						s_assert.assertTrue(storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG),"Error message for product removal from UI is 3 "+i+storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG);

					}else{
						s_assert.assertTrue(storeFrontHomePage.getThresholdMessageIsDisplayed().contains(TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG_CA),"Error message for threshold condition for zero quantity from UI is 4 "+i+storeFrontHomePage.getThresholdMessageIsDisplayed()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG);
						break;
					}

			}else{
				logger.info("SV value is null");
			}
		}

		storeFrontHomePage.addQuantityOfProduct(qtyOfProducts);
		s_assert.assertTrue(storeFrontHomePage.getAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_UPDATE_CART_MSG),"auto ship update cart message from UI is "+storeFrontHomePage.getAutoshipTemplateUpdatedMsg()+" while expected msg is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED);
		storeFrontHomePage.addQuantityOfProduct(newQtyOfProducts);
		s_assert.assertTrue(storeFrontHomePage.getAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_UPDATE_CART_MSG),"auto ship update cart message from UI is "+storeFrontHomePage.getAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED);
		s_assert.assertAll(); 
	}

	// Hybris Project-142 Autoship template - manage products in cart - PC perk 
	@Test
	public void testAutoshipTemplateManagePoductsInCartPCPerk_142() throws InterruptedException {
		String qtyOfProducts="10";
		String newQtyOfProducts="5";
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);  
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		logger.info("login is successful"); 
		//click on auto ship cart

		storeFrontHomePage.clickOnAutoshipCart();
		storeFrontUpdateCartPage= new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnContinueShoppingLink();
		storeFrontUpdateCartPage.selectProductAndProceedToBuyForPC();   
		int noOfProduct = storeFrontUpdateCartPage.getNoOfProductInCart();   
		for(int i=noOfProduct; i>=1; i--){
			boolean flag = storeFrontUpdateCartPage.getValueOfFlagForPC(i);
			if(flag==true){
				double SVValue = Double.parseDouble(storeFrontUpdateCartPage.getSubtotalFromCart().split("\\$")[1].trim());
				String SVValueOfRemovedProduct = storeFrontUpdateCartPage.removeProductsFromCartForPC(i);

				double remainingSVValue = storeFrontUpdateCartPage.compareSubtotalValue(SVValueOfRemovedProduct, SVValue);
				if(driver.getCountry().equalsIgnoreCase("us")){
					if(remainingSVValue>=80.00){
						s_assert.assertTrue(storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG),"Error message for product removal from UI is "+i+storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG);

					}else{
						s_assert.assertTrue(storeFrontHomePage.getThresholdMessageIsDisplayed().contains(TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG),"Error message for threshold condition for zero quantity from UI is  "+i+storeFrontHomePage.getThresholdMessageIsDisplayed()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG);
						break;
					}
				}else 
					if(remainingSVValue>=90.00){
						s_assert.assertTrue(storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG),"Error message for product removal from UI is 3 "+i+storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG);

					}else{
						s_assert.assertTrue(storeFrontHomePage.getThresholdMessageIsDisplayed().contains(TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG_CA_FOR_PC),"Error message for threshold condition for zero quantity from UI is 4 "+i+storeFrontHomePage.getThresholdMessageIsDisplayed()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG);
						break;
					}

			}else{
				logger.info("SV value is null");
			}
		}

		storeFrontHomePage.addQuantityOfProduct(qtyOfProducts);
		s_assert.assertTrue(storeFrontHomePage.getAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED),"auto ship update cart message from UI is "+storeFrontHomePage.getAutoshipTemplateUpdatedMsg()+" while expected msg is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED);
		storeFrontHomePage.addQuantityOfProduct(newQtyOfProducts);
		s_assert.assertTrue(storeFrontHomePage.getAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED),"auto ship update cart message from UI is "+storeFrontHomePage.getAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED);
		s_assert.assertAll(); 
	}

	//Hybris Project-2279:Add Multiple Billing profiles and during checkout
	@Test
	public void testAddMultipeBillingProfileDuringCheckout_2279() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int i=0;
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		for(i=0;i<2;i++){
			storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
			storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
			storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+i+" "+lastName);
			storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
			storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontUpdateCartPage.selectNewBillingCardAddress();
			storeFrontUpdateCartPage.clickOnSaveBillingProfile();
			s_assert.assertTrue(storeFrontUpdateCartPage.isNewlyCreatedBillingProfileIsSelectedByDefault(newBillingProfileName+i),"New Billing Profile is not selected by default on CRP cart page");
		}
		i=1;
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
		storeFrontUpdateCartPage.clickBillingEditAfterSave();
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewlyCreatedBillingProfileIsSelectedByDefault(newBillingProfileName+i),"New Billing Profile is not selected by default on CRP cart page");
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(),"Order is not placed successfully");
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();

		//------------------ Verify that adhoc orders template doesn't contains the newly created billing profile by verifying by name------------------------------------------------------------

		s_assert.assertTrue(storeFrontOrdersPage.isPaymentMethodContainsName(newBillingProfileName+i),"AdHoc Orders Template Payment Method contains new billing profile when future autoship checkbox not selected");
		//------------------Verify that billing info page contains the newly created billing profile
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName+i),"Newly added Billing profile is NOT listed on the billing page");
		s_assert.assertAll();
	}

	//Hybris Project-2297:Remove one of the product from Multiple Orderline Cart--> Subtotal is recalculated
	@Test
	public void testModifyQtyInCartAndValidateSubTotal_2297() throws InterruptedException	 {
		RFO_DB = driver.getDBNameRFO(); 
		String country = driver.getCountry();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;

		if(country.equalsIgnoreCase("us")){
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
		}
		else if(country.equalsIgnoreCase("ca")){
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
		} 
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product with the price less than $80 and proceed to buy it
		storeFrontHomePage.applyPriceFilterLowToHigh();
		storeFrontHomePage.selectProductAndProceedToBuyWithoutFilter();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
		//update qty to 2 of the first product
		storeFrontHomePage.addQuantityOfProduct("2"); 
		//add another product in the cart
		storeFrontHomePage.addAnotherProduct();

		logger.info("2 products are successfully added to the cart");
		//update qty to 2 of the second product
		storeFrontHomePage.updateQuantityOfProductToTheSecondProduct("2"); 

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//click on edit shopping cart link
		storeFrontHomePage.clickEditShoppingCartLink();
		//Increase the qty of product 1 to 3
		storeFrontHomePage.addQuantityOfProduct("3"); 
		//Decrease the qty of product2 to 1
		storeFrontHomePage.updateQuantityOfProductToTheSecondProduct("1");

		//get the sub-total of the first product
		double subtotal1=storeFrontHomePage.getSubTotalOfFirstProduct();
		//get the sub-total of the second product
		double subtotal2=storeFrontHomePage.getSubTotalOfSecondProduct();
		//validate sub-total is recalculated accordingly to the updated qty of product(s)
		s_assert.assertTrue(storeFrontHomePage.validateSubTotal(subtotal1, subtotal2), "sub-total is not recalculated accordingly to the updated qty of product(s)");
		//Remove product 2 from the cart
		storeFrontHomePage.removefirstProductFromTheCart();
		//validate product has been removed from the cart
		s_assert.assertTrue(storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG),"Error message for product removal from UI is "+storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG);
		subtotal2=0;
		//validate sub-total is recalculated accordingly after removing product 2
		s_assert.assertTrue(storeFrontHomePage.validateSubTotal(subtotal1, subtotal2), "sub-total is not recalculated accordingly to the updated qty of product(s)");
		//click on check-out button
		storeFrontHomePage.clickOnCheckoutButton();
		s_assert.assertAll(); 
	}

	//Hybris Project-2302:REmove Product from CArt
	@Test
	public void testValidateRemoveProductsFromCart_2302() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		String country = driver.getCountry();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		if(country.equalsIgnoreCase("us")){
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
		}
		else if(country.equalsIgnoreCase("ca")){
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
		} 
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		//Add multiple quantities of multiple product in the cart
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product with the price less than $80 and proceed to buy it
		storeFrontHomePage.applyPriceFilterLowToHigh();
		storeFrontHomePage.selectProductAndProceedToBuyWithoutFilter();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
		//update qty to 2 of the first product
		storeFrontHomePage.addQuantityOfProduct("3"); 
		//add another product in the cart
		storeFrontHomePage.addAnotherProduct();

		//update qty to 2 of the second product
		storeFrontHomePage.updateQuantityOfProductToTheSecondProduct("3"); 
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//click on edit shopping cart link
		storeFrontHomePage.clickEditShoppingCartLink();
		//get the sub-total of the first product
		double subtotal1=storeFrontHomePage.getSubTotalOfFirstProduct();
		//Remove product 2 from the cart and validate the subtotal
		storeFrontHomePage.removefirstProductFromTheCart();
		//validate product has been removed from the cart
		s_assert.assertTrue(storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG),"Error message for product removal from UI is "+storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG);
		double subtotal2=0;
		//validate sub-total is recalculated accordingly after removing product 2
		s_assert.assertTrue(storeFrontHomePage.validateSubTotal(subtotal1, subtotal2), "sub-total is not recalculated accordingly to the updated qty of product(s)");
		//Remove all the products from the cart
		storeFrontHomePage.removeFirstProductFromTheCart();
		//validate empty shopping cart page is displayed
		storeFrontHomePage.validateEmptyShoppingCartPageIsDisplayed();
		//click on continue shopping link
		storeFrontHomePage.clickOnContinueShoppingLinkOnEmptyShoppingCartPage();
		//Select a product  and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
		//update qty to 2 of the first product
		storeFrontHomePage.addQuantityOfProduct("2"); 
		//add another product in the cart
		storeFrontHomePage.addSecondProduct();

		logger.info("2 products are successfully added to the cart");
		//update qty to 2 of the second product
		storeFrontHomePage.updateQuantityOfProductToTheSecondProduct("2"); 
		//Decrease quantities of both the product added to 1
		storeFrontHomePage.addQuantityOfProduct("1"); 
		storeFrontHomePage.updateQuantityOfProductToTheSecondProduct("1"); 
		//validate the sub-total according to the updated quantities
		//get the sub-total of the first product
		subtotal1=storeFrontHomePage.getSubTotalOfFirstProduct();
		//get the sub-total of the second product
		subtotal2=storeFrontHomePage.getSubTotalOfSecondProduct();
		//validate sub-total is recalculated accordingly to the updated qty of product(s)
		s_assert.assertTrue(storeFrontHomePage.validateSubTotal(subtotal1, subtotal2), "sub-total is not recalculated accordingly to the updated qty of product(s)");
		storeFrontHomePage.removefirstProductFromTheCart();
		//add one more product to the shopping cart
		storeFrontHomePage.addSecondProduct();
		//re-validate the sub-total
		//get the sub-total of the first product
		subtotal1=storeFrontHomePage.getSubTotalOfFirstProduct();
		//get the sub-total of the second product
		subtotal2=storeFrontHomePage.getSubTotalOfSecondProduct();
		//validate sub-total is recalculated accordingly to the updated qty of product(s)
		s_assert.assertTrue(storeFrontHomePage.validateSubTotal(subtotal1, subtotal2), "sub-total is not recalculated accordingly to the updated qty of product(s)");
		//click on check-out button
		storeFrontHomePage.clickOnCheckoutButton();
		s_assert.assertAll(); 
	}

	//Hybris Project-2142:Check Shipping and Handling Fee for UPS Ground for Order total 0-999999
	@Test
	public void testCheckShippingAndHandlingFeeForUPSGround_2142() throws InterruptedException {  
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;   
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPSGroundInOrderSummary();
		double subtotal = storeFrontUpdateCartPage.getSubtotalValue();
		logger.info("subtotal ="+subtotal);
		String deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		String handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
		logger.info("handlingCharges ="+handlingCharges);
		if(subtotal<=999999){
			if(driver.getCountry().equalsIgnoreCase("ca")){
				//Assert  shipping cost from UI
				s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("CAD$ 15.00"),"Shipping charges on UI is not As per shipping method selected");
				//Handling charges
				s_assert.assertTrue(handlingCharges.equalsIgnoreCase("CAD$ 2.50"),"Handling charges on UI is not As per shipping method selected");
			}else if(driver.getCountry().equalsIgnoreCase("us")){
				//Assert  shipping cost from UI
				s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("$17.00"),"Shipping charges on UI is not As per shipping method selected");
				//Handling charges
				s_assert.assertTrue(handlingCharges.equalsIgnoreCase("$2.50"),"Handling charges on UI is not As per shipping method selected");
			}
		}else{
			logger.info(" Order total is not in required range");
		}
		s_assert.assertAll();
	}

	//Hybris Project-2144:Check Shipping and Handling Fee for UPS 2Day for Order total 0- 0-999999
	@Test
	public void testCheckShippingAndHandlingFee_2144() throws SQLException, InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontConsultantPage.clickAddToBagButton(driver.getCountry());
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPS2DayInOrderSummary();
		double orderTotal = storeFrontUpdateCartPage.getOrderTotal();
		logger.info("subtotal ="+orderTotal);
		String deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		String handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
		logger.info("handlingCharges ="+handlingCharges);
		if(orderTotal<=999999){
			if(driver.getCountry().equalsIgnoreCase("CA")){
				//Assert of shipping cost from UI
				s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("CAD$ 20.00"),"Shipping charges on UI is not As per shipping method selected");
				s_assert.assertTrue(handlingCharges.equalsIgnoreCase("CAD$ 2.50"),"Handling charges on UI is not As per shipping method selected");
			}else if(driver.getCountry().equalsIgnoreCase("US")){
				s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("$23.00"),"Shipping charges on UI is not As per shipping method selected");
				s_assert.assertTrue(handlingCharges.equalsIgnoreCase("$2.50"),"Handling charges on UI is not As per shipping method selected");
			}

		}else{
			logger.info("Order total is not in required range");
		}
		s_assert.assertAll();
	}

	//Hybris Project-2145:Check Shipping and Handling Fee for UPS 2Day for Order total 0-999999-CRP Autoship
	@Test
	public void testCheckShippingAndHandlingFeeAsPerOrderTotal_2145() throws SQLException, InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickOnAutoShipButton();
		storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();
		storeFrontUpdateCartPage.clickOnEditShipping();
		storeFrontUpdateCartPage.selectShippingMethod2Day();
		storeFrontUpdateCartPage.clickOnNextStepBtnShippingAddress();
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		String orderNumber = storeFrontOrdersPage.getAutoshipOrderNumber();
		storeFrontOrdersPage.clickOrderNumber(orderNumber);
		double orderGrandTotal =storeFrontOrdersPage.getOrderGrandTotal();
		if(orderGrandTotal<=999999){
			if(driver.getCountry().equalsIgnoreCase("CA")){
				s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains("CAD$ 2.50"),"Handling charges on UI is not As per shipping method selected");
				s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains("22"),"Shipping charges on UI is not As per shipping method selected");
			}
			else if(driver.getCountry().equalsIgnoreCase("US")){
				s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains("$2.50"),"Handling charges on UI is not As per shipping method selected");
				s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(TestConstants.SHIPPING_CHARGES_ON_UI_FOR_US),"Shipping charges on UI is not As per shipping method selected");
			}
		}else{
			logger.info(" Order total is not in required range");
		}
		s_assert.assertAll();
	}

	// Hybris Project-2130:To verify Change date functionality for PC shouldnt be present on the storefront
	@Test
	public void testVerifyChangeDateFunctionalityForPCUser_2130() throws InterruptedException	  {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);  
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontCartAutoShipPage=storeFrontPCUserPage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage=storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditShipping();
		s_assert.assertFalse(storeFrontUpdateCartPage.checkDateFunctionality(), "check date functionality is present");
		s_assert.assertAll();
	}

	//Hybris Project-2132:Verify The Mini Functionality
	@Test
	public void testMiniFunctionality_2132() throws InterruptedException{
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		//storeFrontHomePage.applyPriceFilterLowToHigh();
		//		String selectedProductName=storeFrontHomePage.getProductName();
		storeFrontHomePage.selectProductAndProceedToBuyWithoutFilter();


		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");

		//click on continue shopping link
		storeFrontHomePage.clickOnContinueShoppingLink();
		//verify the number of products in mini cart
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInMiniCart("1"),"number of products in the mini cart is not 1");

		//click on product link for product detail page
		storeFrontHomePage.clickProductLinkForProductDetail();

		//verify product details
		s_assert.assertTrue(storeFrontHomePage.isProductImageExist(),"product image not present");
		//s_assert.assertTrue(storeFrontHomePage.verifyProductName(selectedProductName),"Product name is not as expected");

		//navigate back to quick shop page
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		storeFrontHomePage.updateProductQuantityOnModalWindowAndProceedToBuy("3");
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("4"), "number of products in the cart is NOT 4");
		//click on continue shopping link
		storeFrontHomePage.clickOnContinueShoppingLink();

		//click on mini cart icon
		storeFrontHomePage.clickMiniCart();
		//remove all products from mini cart
		storeFrontHomePage.deleteAllItemsFromCart();		
		s_assert.assertTrue(storeFrontHomePage.isCartHasZeroItems(), "number of products in the cart is NOT 0");
		s_assert.assertAll();	
	}

	//Hybris Project-2113:Reduce the Quantity
	@Test
	public void testReduceTheQuantity_2113() throws InterruptedException{
		String qtyIncrease = "2";
		String qtyReduce = "1";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontHomePage.applyPriceFilterLowToHigh();
		storeFrontHomePage.clickAddToBagButtonWithoutFilter();
		double subTotalOfAddedProduct = storeFrontHomePage.getSubTotalOnShoppingCartPage();
		storeFrontHomePage.addQuantityOfProduct(qtyIncrease);
		s_assert.assertTrue(storeFrontHomePage.validateAutoshipTemplateUpdatedMsgAfterIncreasingQtyOfProducts(),"update message not coming as expected");
		double subTotalOfAfterUpdate = storeFrontHomePage.getSubTotalOnShoppingCartPage();
		s_assert.assertTrue(storeFrontHomePage.verifySubTotalAccordingToQuantity(qtyIncrease,subTotalOfAddedProduct,subTotalOfAfterUpdate),"subTotal is not updated with increased quantity");
		storeFrontHomePage.addQuantityOfProduct(qtyReduce);
		s_assert.assertTrue(storeFrontHomePage.validateAutoshipTemplateUpdatedMsgAfterIncreasingQtyOfProducts(),"update message not coming as expected");
		double subTotalAfterReduce = storeFrontHomePage.getSubTotalOnShoppingCartPage();
		s_assert.assertTrue(storeFrontHomePage.verifySubTotalAccordingToQuantity(qtyReduce,subTotalOfAddedProduct,subTotalAfterReduce),"subTotal is not updated with reduced quantity");
		s_assert.assertAll();	  
	}

	//Hybris Project-2120:Increase the Quantity
	@Test
	public void testIncreseTheQuantity_2120() throws InterruptedException{
		String qtyIncrease = "2";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontHomePage.applyPriceFilterLowToHigh();
		storeFrontHomePage.clickAddToBagButtonWithoutFilter();
		double subTotalOfAddedProduct = storeFrontHomePage.getSubTotalOnShoppingCartPage();
		storeFrontHomePage.addQuantityOfProduct(qtyIncrease);
		s_assert.assertTrue(storeFrontHomePage.validateAutoshipTemplateUpdatedMsgAfterIncreasingQtyOfProducts(),"update message not coming as expected");
		s_assert.assertTrue(storeFrontHomePage.getAutoshipTemplateUpdatedMsg().contains("Product quantity has been updated."),"update message not coming as expected");
		double subTotalOfAfterUpdate = storeFrontHomePage.getSubTotalOnShoppingCartPage();
		s_assert.assertTrue(storeFrontHomePage.verifySubTotalAccordingToQuantity(qtyIncrease,subTotalOfAddedProduct,subTotalOfAfterUpdate),"subTotal is not updated with increased quantity");
		s_assert.assertAll();
	}

	//Hybris Project-2121:Remove Product from the cart
	@Test
	public void testRemoveProductFromTheCart_2121() throws InterruptedException{
		String qtyIncrease = "2";
		String qtyReduce = "1";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontHomePage.applyPriceFilterLowToHigh();
		storeFrontHomePage.clickAddToBagButtonWithoutFilter();
		double subTotalOfAddedProduct = storeFrontHomePage.getSubTotalOnShoppingCartPage();
		storeFrontHomePage.addQuantityOfProduct(qtyIncrease);
		s_assert.assertTrue(storeFrontHomePage.validateAutoshipTemplateUpdatedMsgAfterIncreasingQtyOfProducts(),"update message not coming as expected");
		double subTotalOfAfterUpdate = storeFrontHomePage.getSubTotalOnShoppingCartPage();
		s_assert.assertTrue(storeFrontHomePage.verifySubTotalAccordingToQuantity(qtyIncrease,subTotalOfAddedProduct,subTotalOfAfterUpdate),"subTotal is not updated with increased quantity");
		storeFrontHomePage.addQuantityOfProduct(qtyReduce);
		s_assert.assertTrue(storeFrontHomePage.validateAutoshipTemplateUpdatedMsgAfterIncreasingQtyOfProducts(),"update message not coming as expected");
		double subTotalAfterReduce = storeFrontHomePage.getSubTotalOnShoppingCartPage();
		s_assert.assertTrue(storeFrontHomePage.verifySubTotalAccordingToQuantity(qtyReduce,subTotalOfAddedProduct,subTotalAfterReduce),"subTotal is not updated with reduced quantity");
		s_assert.assertAll();
	}

	//Hybris Project-2293 withoutloggin in add product to cart--> click ContinueShopping
	@Test
	public void testWithOutLoginQuickShopScreen_2293() throws InterruptedException	{
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//Click on Continue shopping link
		storeFrontHomePage.clickOnContinueShoppingLink();

		//validate quick-shop screen
		s_assert.assertTrue(storeFrontHomePage.validateQuickShopScreen(),"QuickShop page is not displayed");
		s_assert.assertAll();
	}

	// Hybris Project-2292:Continue Shopping - logged in
	@Test
	public void testQuickShopScreenWithRegisteredUser_2292() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserEmailID = null;
		String accountIdForRCUser = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//For Consultant
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//Click on Continue shopping link
		storeFrontHomePage.clickOnContinueShoppingLink();
		//validate quick-shop screen
		s_assert.assertTrue(storeFrontHomePage.validateQuickShopScreen(),"QuickShop page is not displayed");
		logout();

		//For PC User
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//Click on Continue shopping link
		storeFrontHomePage.clickOnContinueShoppingLink();
		//validate quick-shop screen
		s_assert.assertTrue(storeFrontHomePage.validateQuickShopScreen(),"QuickShop page is not displayed");
		logout();

		//For RC User
		while(true){
			randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");  
			accountIdForRCUser = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForRCUser);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//Click on Continue shopping link
		storeFrontHomePage.clickOnContinueShoppingLink();
		//validate quick-shop screen
		s_assert.assertTrue(storeFrontHomePage.validateQuickShopScreen(),"QuickShop page is not displayed");

		s_assert.assertAll();
	}

	//Hybris Project-2307:Product Listing Scren should have Buy Now button --> click Buy Now--> product is added to the cart
	@Test 
	public void testProductListHaveBuyNowButtonAndProductAddedToCart_2307() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//login As PC User and verify Buy Now Button.
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//verify product list page has buy now button After login.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(),"Add to Bag Button is not present");
		//Select a product  and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());
		//login as RC User And Verify Buy Now Button.
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserEmailID = null;
		String accountIdForRCUser = null;
		while(true){
			randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");		
			accountIdForRCUser = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForRCUser);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//verify product list page has buy now button After login.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(),"Add to Bag Button is not present");
		//Select a product  and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());
		//login as consultant and verify Buy Now Button.
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountIdForConsultant = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountIdForConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForConsultant);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("loginError for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//verify product list page has buy now button After login.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(),"Add to Bag Button is not present");
		//Select a product  and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());

		//verify Buy now button for all users without login.
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//verify product list page has buy now button without login.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(),"Add to Bag Button is not present");
		//Select a product  and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
	}

	//Hybris Project-2309:Add Product froom Quick Info Screen
	@Test
	public void testAddProductFromQuickInfoScreen_2309() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//login As PC User and Add Product From Quick Info Screen
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement
					(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		storeFrontHomePage.clickAddToBagButtonOnQuickInfoPopup();
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());

		//login as RC User And Verify Buy Now Button.
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserEmailID = null;
		String accountIdForRCUser = null;
		while(true){
			randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement
					(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");		
			accountIdForRCUser = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForRCUser);
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		storeFrontHomePage.clickAddToBagButtonOnQuickInfoPopup();
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());

		//login as consultant and verify Buy Now Button.
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountIdForConsultant = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement
					(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountIdForConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForConsultant);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		storeFrontHomePage.clickAddToBagButtonOnQuickInfoPopup();
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
	}

	//Hybris Project-2323:Check Buy Now Link
	@Test
	public void testCheckBuyNowLink_2323() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//login As PC User and verify Buy Now Button.
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement
					(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//verify product list page has buy now button After login.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(),"Add to Bag Button is not present");
		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup");
		storeFrontHomePage.clickViewProductDetailLink();
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());

		//login as RC User And Verify Buy Now Button.
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserEmailID = null;
		String accountIdForRCUser = null;
		while(true){
			randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement
					(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");		
			accountIdForRCUser = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForRCUser);
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//verify product list page has buy now button After login.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(),"Add to Bag Button is not present");
		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup");
		storeFrontHomePage.clickViewProductDetailLink();
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());

		//login as consultant and verify Buy Now Button.
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountIdForConsultant = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement
					(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountIdForConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForConsultant);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//verify product list page has buy now button After login.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(),"Add to Bag Button is not present");
		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		//verify add to bag button on quick info popup 
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup");
		storeFrontHomePage.clickViewProductDetailLink();
		//verify add to bag button on product detail page
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page");
	}

	//Hybris Project-2319:Check Quick Info Screen layout and informations
	@Test
	public void testVerifyQuickInfoScreenLayoutAndProductDetails_2319() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//login As PC User and verify Buy Now Button.
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		String selectedProductName=storeFrontHomePage.getProductName();
		String selectedProductPrice=storeFrontHomePage.getProductPrice();

		//verify product list page has buy now button After login.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(),"Add to Bag Button is not present");
		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup");
		storeFrontHomePage.clickViewProductDetailLink();
		//verify product details
		s_assert.assertTrue(storeFrontHomePage.isProductImageExist(),"product image not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductName(selectedProductName),"Product name is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page");
		s_assert.assertTrue(storeFrontHomePage.verifyProductPrice(selectedProductPrice),"Product price is not as expected");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());

		//login as RC User And Verify Buy Now Button.
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserEmailID = null;
		String accountIdForRCUser = null;
		while(true){
			randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement
					(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");		
			accountIdForRCUser = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForRCUser);
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		String selectedProduct=storeFrontHomePage.getProductName();
		String priceOfProduct=storeFrontHomePage.getProductPrice();
		//verify product list page has buy now button After login.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(),"Add to Bag Button is not present");
		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup");
		storeFrontHomePage.clickViewProductDetailLink();
		//verify product details
		s_assert.assertTrue(storeFrontHomePage.isProductImageExist(),"product image not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductName(selectedProduct),"Product name is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page");
		s_assert.assertTrue(storeFrontHomePage.verifyProductPrice(priceOfProduct),"Product price is not as expected");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());

		//login as consultant and verify Buy Now Button.
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountIdForConsultant = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement
					(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountIdForConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForConsultant);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		String ProductName=storeFrontHomePage.getProductName();
		String productPrice=storeFrontHomePage.getProductPrice();
		//verify product list page has buy now button After login.
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButton(),"Add to Bag Button is not present");
		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		//verify add to bag button on quick info popup 
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup");
		storeFrontHomePage.clickViewProductDetailLink();
		//verify product details
		s_assert.assertTrue(storeFrontHomePage.isProductImageExist(),"product image not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductName(ProductName),"Product name is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page");
		s_assert.assertTrue(storeFrontHomePage.verifyProductPrice(productPrice),"Product price is not as expected");
	}

	//Hybris Project-2324:Category Product List page
	@Test
	public void testVerifyCategoryProductListPage_2324() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//login As PC User User and verify Product Details and category.
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//verify the different categories and verify the products in present in each category.
		s_assert.assertTrue(storeFrontHomePage.verifyProductInfoPresentOnQuikShopProducts(),"Product Info not present in quikshop product page");
		s_assert.assertTrue(storeFrontHomePage.verifyRetailPricePresentInProductInfo(),"Retail Price not present in product info");
		s_assert.assertTrue(storeFrontHomePage.verifyPCPricePresentInProductInfo(),"PC Price not present in product info");
		s_assert.assertTrue(storeFrontHomePage.verifyBuyNowButtonPresentInProductInfo(),"Buy now button not present in poduct info");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToPCPerksButtonInProductInfo(),"Add to Pc Perks Button not present below product info");
		s_assert.assertAll();
	}

	//Hybris Project-2325:CAtegory Product List for Consultant
	@Test
	public void testVerifyCategoryProductListPage_2325() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//login As Consultant User and verify Product Details and category.
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountIdForConsultant = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement
					(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountIdForConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForConsultant);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//verify the different categories and verify the products in present in each category.
		s_assert.assertTrue(storeFrontHomePage.verifyProductInfoPresentOnQuikShopProducts(),"Product Info not present in quikshop product page");
		s_assert.assertTrue(storeFrontHomePage.verifyRetailPricePresentInProductInfo(),"Retail Price not present in product info");
		s_assert.assertTrue(storeFrontHomePage.verifyPCPricePresentInProductInfo(),"PC Price not present in product info");
		s_assert.assertTrue(storeFrontHomePage.verifyBuyNowButtonPresentInProductInfo(),"Buy now button not present in poduct info");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToCRPButtonPresent(),"Add to crp Button not present in product info");
		s_assert.assertAll();
	}

	// Hybris Project-2326:Category Product List for Retail Customers & PC
	@Test
	public void testVerifyCategoryProductListPage_2326() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//login As RC User User and verify Product Details and category.
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserEmailID = null;
		String accountIdForRCUser = null;
		while(true){
			randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement
					(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");  
			accountIdForRCUser = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForRCUser);
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//verify the different categories and verify the products in present in each category.
		s_assert.assertTrue(storeFrontHomePage.verifyProductInfoPresentOnQuikShopProducts(),"Product Info not present in quikshop product page");
		s_assert.assertTrue(storeFrontHomePage.verifyRetailPricePresentInProductInfo(),"Retail Price not present in product info");
		s_assert.assertTrue(storeFrontHomePage.verifyBuyNowButtonPresentInProductInfo(),"Buy now button not present in poduct info");
		s_assert.assertAll();
	}

	//Hybris Project-2317:heck View Product Details button from Quick Info pop-up
	@Test
	public void testViewProductDetailsFromQuickInfoPopup_2317() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(1,4);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//login As PC User and Verify product Details.
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		String selectedProduct=storeFrontHomePage.getProductName(randomNum);
		String priceOfProduct=storeFrontHomePage.getProductPrice(randomNum);

		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo(randomNum);

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToPCPerksButtonOnQuickInfoPopup(),"Add to PC Perks Button is not present on quick info popup");
		storeFrontHomePage.clickViewProductDetailLink();
		//verify product details
		s_assert.assertTrue(storeFrontHomePage.isProductImageExist(),"product image not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductName(selectedProduct),"Product name is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToPCPerksButtonOnProductDetailPage(),"Add to PC Perks Button is not present on product detail page");
		s_assert.assertTrue(storeFrontHomePage.verifyProductPrice(priceOfProduct),"Product price is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyProductLongDescription(),"Product Description is not present on product detail page");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());

		//login as RC User And Verify Product Details.
		List<Map<String, Object>> randomRCUserList =  null;
		String rcUserEmailID = null;
		String accountIdForRCUser = null;
		while(true){
			randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCUserList, "UserName");		
			accountIdForRCUser = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForRCUser);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		String selectedProductName=storeFrontHomePage.getProductName(randomNum);
		String selectedProductPrice=storeFrontHomePage.getProductPrice(randomNum);

		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo(randomNum);

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup");
		storeFrontHomePage.clickViewProductDetailLink();
		//verify product details
		s_assert.assertTrue(storeFrontHomePage.isProductImageExist(),"product image not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductName(selectedProductName),"Product name is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page");
		s_assert.assertTrue(storeFrontHomePage.verifyProductPrice(selectedProductPrice),"Product price is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyProductLongDescription(),"Product Description is not present on product detail page");
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());

		//login as consultant and verify Product Details.
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountIdForConsultant = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountIdForConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForConsultant);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		String ProductName=storeFrontHomePage.getProductName(randomNum);
		String productPrice=storeFrontHomePage.getProductPrice(randomNum);

		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo(randomNum);

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToCRPButtonOnQuickInfoPopup(),"Add to CRP Button is not present on quick info popup");
		storeFrontHomePage.clickViewProductDetailLink();
		//verify product details
		s_assert.assertTrue(storeFrontHomePage.isProductImageExist(),"product image not present");
		s_assert.assertTrue(storeFrontHomePage.verifyProductName(ProductName),"Product name is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToCRPButtonOnProductDetailPage(),"Add to CRP Button is not present on product detail page");
		s_assert.assertTrue(storeFrontHomePage.verifyProductPrice(productPrice),"Product price is not as expected");
		s_assert.assertTrue(storeFrontHomePage.verifyProductLongDescription(),"Product Description is not present on product detail page");
		s_assert.assertAll(); 
	}

	//Hybris Project-2316:check Product Details APge Layout and information
	@Test
	public void testVerifyProductDetailsFromQuickInfoPopup_2316() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(1,6);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//login As PC User and Verify product Details.
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed for PC");
		logger.info("Quick shop products are displayed for PC");
		String selectedProduct=storeFrontHomePage.getProductName(randomNum);
		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo(randomNum);

		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists for PC");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnQuickInfoPopup(),"Add to Bag Button is not present on quick info popup for PC");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToPCPerksButtonOnQuickInfoPopup(),"Add to PC Perks Button is not present on quick info popup for PC");
		storeFrontHomePage.clickViewProductDetailLink();
		//verify product details
		s_assert.assertTrue(storeFrontHomePage.isProductImageExist(),"product image not present for PC");
		s_assert.assertTrue(storeFrontHomePage.verifyProductName(selectedProduct),"Product name is not as expected for PC");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToBagButtonOnProductDetailPage(),"Add to Bag Button is not present on product detail page for PC");
		s_assert.assertTrue(storeFrontHomePage.verifyAddToPCPerksButtonOnProductDetailPage(),"Add to PC Perks Button is not present on product detail page for PC");
		s_assert.assertTrue(storeFrontHomePage.verifyProductLongDescription(),"Product Description is not present on product detail page for PC");
		s_assert.assertTrue(storeFrontHomePage.verifyProductUsageNotesBox(),"Product Usage Notes box is not present on product detail page for PC");
		s_assert.assertTrue(storeFrontHomePage.verifyProductIngredientsBox(),"Product Ingredients box is not present on product detail page for PC");
		logout();

		s_assert.assertAll(); 
	}

	//Hybris Project-2262:View QV/SV value in the cart
	@Test
	public void testCreateAdhocOrderConsultantAndVerifySVValue_2262() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);

		//login as consultant and verify Product Details.
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountIdForConsultant = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountIdForConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForConsultant);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		//Place order.
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		s_assert.assertTrue(storeFrontUpdateCartPage.verifySVValueOnCartPage(),"SV Value is not present on cart page");
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifySVValueOnOrderSummaryPage(),"SV Value is not present on order summary page");
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifySVValueOnOrderConfirmationPage(), "SV Value is not present on order confirmation page");
		//String orderNumber = storeFrontUpdateCartPage.getOrderNumberAfterPlaceOrder();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");

		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();

		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);
		s_assert.assertTrue(storeFrontOrdersPage.verifySVValueOnOrderPage(),"Product SV Value is not present on order page.");
		s_assert.assertAll();
	}

	//Hybris Project-3765:RC2PC User - Place Adhoc Order from spsonsor's PWS site check Sposnor of Order
	@Test
	public void testPlaceAdhocOrderFromSponsorPWSSiteCheckSponsorOfOrder_3765() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		// create RC User
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Click on our product link that is located at the top of the page and then click in on quick shop

		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		String emailAddress = firstName+randomNum+"@xyz.com";
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNum, emailAddress, password);

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();

		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(), "Order Not placed successfully");
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");

		storeFrontHomePage.clickOnRodanAndFieldsLogo();

		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();  

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		storeFrontHomePage.clickYesIWantToJoinPCPerksCB();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksCheckBoxIsSelected(), "Yes I want to join pc perks checkboz is not selected");

		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);

		String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		// sponser search by Account Number
		List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		String accountnumber = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));

		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(accountnumber);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.checkIAcknowledgePCAccountCheckBox();
		storeFrontHomePage.checkPCPerksTermsAndConditionsCheckBox();
		storeFrontHomePage.clickPlaceOrderBtn();
		//Validate welcome PC Perks Message
		s_assert.assertTrue(storeFrontHomePage.isWelcomePCPerksMessageDisplayed(), "welcome PC perks message should be displayed");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		String pws = driver.getCurrentUrl();
		logout();
		storeFrontHomePage.openPWS(pws);
		//storeFrontHomePage.openPWSSite(driver.getCountry(), driver.getEnvironment());
		storeFrontHomePage.loginAsPCUser(emailAddress, password);
		// again placed adhoc order
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();  

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		String pwsAfterOrderPlaced = driver.getCurrentUrl();
		System.out.println("After "+pwsAfterOrderPlaced);
		s_assert.assertTrue(storeFrontHomePage.verifyUrlAfterplacedAnAdhocOrder(pws, pwsAfterOrderPlaced), "pws before and are not equal");
		s_assert.assertAll(); 
	}

	// Hybris Project-2305:Filter Product based on Price
	@Test
	public void testFilterProductBasedOnPrice_2305() throws InterruptedException{
		storeFrontHomePage = new StoreFrontHomePage(driver);

		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		String priceBeforeApplyFilter = storeFrontHomePage.getProductPriceBeforeApplyFilter();

		// apply filter low to high
		storeFrontHomePage.applyPriceFilterLowToHigh();
		s_assert.assertTrue(storeFrontHomePage.verifyPriceFromLowToHigh(), "Prices are not in format from low to high");

		// apply filter high to low
		storeFrontHomePage.applyPriceFilterHighToLow();
		s_assert.assertTrue(storeFrontHomePage.verifyPriceFromHighTolow(), "Prices are not in format from high to low");

		//deselect the price filter
		storeFrontHomePage.deselectPriceFilter();
		s_assert.assertTrue(storeFrontHomePage.verifyPriceAfterDeselectThefilter(priceBeforeApplyFilter), "Price is not as before after deselect the filter");

		s_assert.assertAll(); 

	}

	//Hybris Project-2306:Filter Product on Category
	@Test
	public void testFilterProductOnCategory_2306(){
		storeFrontHomePage = new StoreFrontHomePage(driver);

		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		int sizeOfProductFilter = storeFrontHomePage.getSizeOfProductFilter();
		for(int i=1; i<=sizeOfProductFilter; i++){
			s_assert.assertTrue(storeFrontHomePage.verifyProductFilterIsApply(i), "Product name is not similar as product filter"+" "+i );
		}

		s_assert.assertAll();
	}

	// Hybris Project-3766:RC2PC User - Place Adhoc Order from Different PWS site check Sposnor of Order
	@Test
	public void testPlaceAdhocOrderFromDifferentPWSCheckSponsorOfOrder_3766() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		// create RC User
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Click on our product link that is located at the top of the page and then click in on quick shop

		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		String emailAddress = firstName+randomNum+"@xyz.com";
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNum, emailAddress, password);

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();

		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(), "Order Not placed successfully");
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");

		storeFrontHomePage.clickOnRodanAndFieldsLogo();

		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();  

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		storeFrontHomePage.clickYesIWantToJoinPCPerksCB();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksCheckBoxIsSelected(), "Yes I want to join pc perks checkboz is not selected");


		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);

		String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		// sponser search by Account Number
		List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		String accountnumber = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));

		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(accountnumber);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.checkIAcknowledgePCAccountCheckBox();
		storeFrontHomePage.checkPCPerksTermsAndConditionsCheckBox();
		storeFrontHomePage.clickPlaceOrderBtn();
		//Validate welcome PC Perks Message
		s_assert.assertTrue(storeFrontHomePage.isWelcomePCPerksMessageDisplayed(), "welcome PC perks message should be displayed");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		String pws = driver.getCurrentUrl();
		System.out.println("before  "+pws);

		logout();
		storeFrontHomePage.openPWSSite(driver.getCountry(), driver.getEnvironment());
		storeFrontHomePage.loginAsPCUser(emailAddress, password);
		// again placed adhoc order
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();  

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		String pwsAfterOrderPlaced = driver.getCurrentUrl();
		System.out.println("After "+pwsAfterOrderPlaced);
		s_assert.assertTrue(storeFrontHomePage.verifyUrlAfterplacedAnAdhocOrder(pws, pwsAfterOrderPlaced), "pws before and are not equal");
		s_assert.assertAll(); 
	}

	//Hybris Project-3768:Update PCPerk Template from Different PWS site(Other Than Sponsor's PWS)
	@Test
	public void testUpdatePCPerkTemplateFromDifferentPWS_3768() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		// create RC User
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Click on our product link that is located at the top of the page and then click in on quick shop

		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		String emailAddress = firstName+randomNum+"@xyz.com";
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNum, emailAddress, password);

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();

		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(), "Order Not placed successfully");
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");

		storeFrontHomePage.clickOnRodanAndFieldsLogo();

		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();  

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		storeFrontHomePage.clickYesIWantToJoinPCPerksCB();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksCheckBoxIsSelected(), "Yes I want to join pc perks checkboz is not selected");


		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);

		String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		// sponser search by Account Number
		List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		String accountnumber = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));

		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(accountnumber);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.checkIAcknowledgePCAccountCheckBox();
		storeFrontHomePage.checkPCPerksTermsAndConditionsCheckBox();
		storeFrontHomePage.clickPlaceOrderBtn();
		//Validate welcome PC Perks Message
		s_assert.assertTrue(storeFrontHomePage.isWelcomePCPerksMessageDisplayed(), "welcome PC perks message should be displayed");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		String pws = driver.getCurrentUrl();
		logout();
		storeFrontHomePage.openPWS(pws);
		//storeFrontHomePage.openPWSSite(driver.getCountry(), driver.getEnvironment());
		storeFrontHomePage.loginAsPCUser(emailAddress, password);
		storeFrontHomePage.clickOnAutoshipCart();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnContinueShoppingLink();
		storeFrontUpdateCartPage.selectAProductAndAddItToPCPerks();
		storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();
		storeFrontUpdateCartPage.clickUpdateCartBtn();

		//validate cart has been updated?
		s_assert.assertTrue(storeFrontUpdateCartPage.validateCartUpdated(), "cart is not updated!! ");
		storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		String pwsAfterUpdateCart = driver.getCurrentUrl();
		System.out.println("After "+pwsAfterUpdateCart);
		s_assert.assertTrue(storeFrontHomePage.verifyUrlAfterplacedAnAdhocOrder(pws, pwsAfterUpdateCart), "pws before and are not equal");

		s_assert.assertAll(); 
	}

	//Hybris Project-2267:Login as Existing PC and Place an Adhoc Order, Check for Alert message
	@Test
	public void testExistingPCPlaceAnAdhocOrderAndCheckForAlertMessage_2267() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);

		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}  

		logger.info("login is successful");
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontPCUserPage.clickAddToBagButton();
		storeFrontPCUserPage.clickOnPlaceOrderButton();
		s_assert.assertTrue(storeFrontPCUserPage.verifyCheckoutConfirmationPOPupPresent(),"Checkout Confirmation pop up not present");
		s_assert.assertTrue(storeFrontPCUserPage.verifyCheckoutConfirmationPopUpMessagePC(),"Checkout Confirmation pop up message is not present as expected");
		storeFrontPCUserPage.clickOnOkButtonOnCheckoutConfirmationPopUp();
		s_assert.assertTrue(storeFrontPCUserPage.verifyAccountInfoPageHeaderPresent(),"Account info page header is not present");
		s_assert.assertAll();
	}

	//Hybris Project-2264:Check Savings Per Product as a PC user
	@Test
	public void testSavingsPerProductAsAPCUser_2264() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		//s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"PC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();

		//assert Retail price on products page
		s_assert.assertTrue(storeFrontPCUserPage.verifyRetailPriceIsAvailableOnProductsPage(),"Retail Price is not available");

		//assert your price on products page
		s_assert.assertTrue(storeFrontPCUserPage.verifyYourPriceIsAvailableOnProductsPage(),"Your Price is not available");
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnBuyNowButton();

		//assert Retail price on Adhoc cart page
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyRetailPriceIsAvailableOnAdhocCart(),"Retail Price is not available on Adhoc cart");

		//assert your price on Adhoc cart page
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyYourPriceIsAvailableOnAdhocCart(),"Your Price is not available on Adhoc cart");
		storeFrontUpdateCartPage.clickOnCheckoutButton();

		//assert total saving on Adhoc cart page
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyTotalSavingsIsAvailableOnAdhocCart(),"Total Savings is not available on Adhoc cart");

		storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		storeFrontUpdateCartPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickOnAddToPcPerksButton();

		//assert Retail price on Autoship cart page
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyRetailPriceIsAvailableOnAutoshipCart(),"Retail Price is not available on Autoship cart");

		//assert your price on Autoship cart page
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyYourPriceIsAvailableOnAutoshipCart(),"Your Price is not available on Autoship cart");

		storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();

		//assert total saving on Autoship cart page
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyTotalSavingsIsAvailableOnAutoshipCart(),"Total Savings is not available on Autoship cart");

		s_assert.assertAll();
	}

	//Hybris Project-2170:Login as Existing Consultant and Place an Adhoc Order - check Alert Message
	@Test
	public void testExistingConsultantPlaceAnAdhocOrderAndCheckForAlertMessage_2170() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);

		//login as consultant and verify Product Details.
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountIdForConsultant = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountIdForConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForConsultant);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontConsultantPage.clickAddToBagButton();
		storeFrontConsultantPage.clickOnPlaceOrderButton();
		s_assert.assertTrue(storeFrontConsultantPage.verifyCheckoutConfirmationPOPupPresent(),"Checkout Confirmation pop up not present");
		s_assert.assertTrue(storeFrontConsultantPage.verifyCheckoutConfirmationPopUpMessageConsultant(),"Checkout Confirmation pop up message is not present as expected");
		storeFrontConsultantPage.clickOnOkButtonOnCheckoutConfirmationPopUp();
		s_assert.assertTrue(storeFrontConsultantPage.verifyAccountInfoPageHeaderPresent(),"Account info page header is not present");
		s_assert.assertAll();
	}

	//Hybris Project-2318:PC Perks Message
	@Test
	public void testVerifyPCPerksMessageOnModalPopup_2318() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		storeFrontHomePage = new StoreFrontHomePage(driver);

		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();
		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		//verify pc perks message on modal popup
		s_assert.assertTrue(storeFrontHomePage.getPCPerksMessageFromModalPopup().contains(TestConstants.PC_PERKS_MESSAGE_ON_MODAL_POPUP),"PC Perks message is not comming on modal popup");
		s_assert.assertAll();
	}

	// Hybris Project-139:PC Perks - Delay
	@Test
	public void testDelayPCPerks_139() throws InterruptedException{

		RFO_DB = driver.getDBNameRFO(); 
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//login As PC User and Verify product Details.
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountIdForPCUser = null;
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		//Verify Order Page
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage=storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Order Page Is not Displayed");
		//verify autoship order section on order page.
		s_assert.assertTrue(storeFrontOrdersPage.verifyAutoshipOrderSectionOnOrderPage(),"Autoship order section is not on order page for pc user");
		//verify Order History section on order page.
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrderHistorySectionOnOrderPage(),"Order history section is not on order page for pc user");
		//verify Return order section on order page.
		s_assert.assertTrue(storeFrontOrdersPage.verifyReturnOrderSectionOnOrderPage(),"Return order section is not on order page for pc user ");

		//Verify Delay And Cancel PC Perks Message
		storeFrontPCUserPage.clickOnYourAccountDropdown();
		storeFrontPCUserPage.clickOnPCPerksStatus();
		s_assert.assertTrue(storeFrontPCUserPage.verifyPCPerksStatus(),"PC perks status page is not present");
		storeFrontPCUserPage.clickDelayOrCancelPCPerks();
		storeFrontPCUserPage.clickChangeMyAutoshipDateButton();
		s_assert.assertTrue(storeFrontPCUserPage.verifyNextAutoshipDateRadioButtons(),"Next AutoShip Date radio button are not present");
		String nextBillDate=storeFrontPCUserPage.getNextBillAndShipDate();
		String dateAfterOneMonth=storeFrontPCUserPage.getOneMonthOutDate(nextBillDate);
		String dateAfterTwoMonth=storeFrontPCUserPage.getOneMonthOutDate(dateAfterOneMonth);
		String dateAfterThreeMonth=storeFrontPCUserPage.getOneMonthOutDate(dateAfterTwoMonth);

		//assert for date after one month and after two month
		s_assert.assertTrue(storeFrontPCUserPage.getShipAndBillDateAfterOneMonthFromUI().contains(dateAfterOneMonth),"Date After one month is not as expected");
		s_assert.assertTrue(storeFrontPCUserPage.getShipAndBillDateAfterTwoMonthFromUI().contains(dateAfterTwoMonth),"Date after two month is not as expected");

		//select autoship date one month Later.
		storeFrontPCUserPage.selectFirstAutoshipDateAndClickSave();
		//String autoshipDateFromOrderPage=storeFrontOrdersPage.getAutoshipOrderDate();

		//verify One month later autoship date on order page
		s_assert.assertTrue(storeFrontOrdersPage.verifyAutoShipOrderDate(dateAfterOneMonth),"Next Selected autoship order date is not updated on order page");
		//verify one month later autoship date on PC Perks Status Page
		storeFrontOrdersPage.clickOnYourAccountDropdown();
		storeFrontPCUserPage.clickOnPCPerksStatus();
		s_assert.assertTrue(storeFrontPCUserPage.getNextBillAndShipDate().equalsIgnoreCase(dateAfterOneMonth),"Next Bill and ship date one month later is not the updated one");

		//select autoship date Two month Later.
		storeFrontPCUserPage.clickDelayOrCancelPCPerks();
		storeFrontPCUserPage.clickChangeMyAutoshipDateButton();
		storeFrontPCUserPage.selectSecondAutoshipDateAndClickSave();
		//verify Two month later autoship date on order page
		s_assert.assertTrue(storeFrontOrdersPage.verifyAutoShipOrderDate(dateAfterThreeMonth),"Next Selected autoship order date is not updated on order page");
		//verify two month later autoship date on PC Perks Status Page
		storeFrontOrdersPage.clickOnYourAccountDropdown();
		storeFrontPCUserPage.clickOnPCPerksStatus();
		s_assert.assertTrue(storeFrontPCUserPage.getNextBillAndShipDate().equalsIgnoreCase(dateAfterThreeMonth),"Next Bill and ship date is two month later not the updated one");
		s_assert.assertAll();
	}


	//Hybris Project-2090:10% off, free shipping for terms and conditions for the PC Perks Program
	@Test
	public void testFreeShippingTermsAndConditionsForPCPerks_2090() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String emailAddress = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
		// Click on our product link that is located at the top of the page and then click in on quick shop
		/*storeFrontHomePage.clickOnShopLink();
		       storeFrontHomePage.clickOnAllProductsLink();*/
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product with the price less than $80 and proceed to buy it
		//  storeFrontHomePage.applyPriceFilterLowToHigh();
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		//  s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password,emailAddress);

		//validate 10% discount for PC User account in order summary section
		s_assert.assertTrue(storeFrontHomePage.validateDiscountForEnrollingAsPCUser(TestConstants.DISCOUNT_TEXT_FOR_PC_USER),"Discount text Checkbox is not checked for pc User");

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();

		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		//validate bill to this card radio button selected under billing profile section
		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Bill to this card radio button is not selected under billing profile");
		s_assert.assertTrue(storeFrontHomePage.isBillingProfileIsSelectedByDefault(newBillingProfileName),"Bill to this card radio button is not selected under billing profile");
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksTermsAndConditionsPopupPresent(),"PC Perks terms and condition popup is not present");
		s_assert.assertTrue(storeFrontHomePage.getPCPerksTermsAndConditionsPopupText().toLowerCase().contains(TestConstants.PC_PERKS_TERMS_CONDITION_POPUP_TEXT.toLowerCase()),"PC perks terms and candition popup text is not as expected");
		storeFrontHomePage.clickPCPerksTermsAndConditionPopupOkay();
		//verify for popup saying select terms and candition to avail 10 percent discount
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(),"Congrats message is not present");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();
	}

	//Hybris Project-145:Update PC Template -EDIT Cart , Shipping info, billing info and Save
	@Test
	public void testUpdatePCTemplate_145() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountID = null;
		String lastName = "ln";
		String newShipingAddressName = TestConstants.ADDRESS_NAME+randomNum;
		String newBillingAddressName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}  
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnAutoshipCart();
		storeFrontPCUserPage.clickOnContinueShoppingLink();
		storeFrontPCUserPage.clickOnAddtoPCPerksButton();
		String updatedMsg = storeFrontPCUserPage.getAutoshipTemplateUpdatedMsg();
		s_assert.assertTrue(storeFrontPCUserPage.verifyUpdateCartMessage(updatedMsg),"Autoship Cart has not been Updated");
		storeFrontCartAutoShipPage = new StoreFrontCartAutoShipPage(driver);
		storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);

		storeFrontUpdateCartPage.clickOnEditShipping();
		storeFrontUpdateCartPage.clickOnEditForDefaultShippingAddress();
		storeFrontUpdateCartPage.enterNewShippingAddressName(newShipingAddressName+" "+lastName);
		storeFrontUpdateCartPage.clickOnSaveShippingProfileAfterEditDuringEnrollment();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifySelectedShippingAddressIsDefault(newShipingAddressName),"selected shipping address is not default");
		String selectedMethodName = storeFrontUpdateCartPage.selectAndGetShippingMethodName();
		storeFrontUpdateCartPage.clickOnNextStepBtnShippingAddress();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifySelectedShippingMethodNameOnUI(selectedMethodName),"Selected Shipping method name is not present on UI");

		//		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
		storeFrontUpdateCartPage.clickOnEditDefaultBillingProfile();
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate(TestConstants.CARD_EXP_MONTH_OPTION,TestConstants.CARD_EXP_YEAR_OPTION);
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyErrorMessageForCreditCardSecurityCode(),"Error message for credit card security code is not present");
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingAddressName);
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		//s_assert.assertTrue(storeFrontUpdateCartPage.verifyBillingProfileIsUpdatedSuccessfully(),"Billing profile is not been updated successfully");
		//verify selected billing profile is default
		s_assert.assertTrue(storeFrontUpdateCartPage.verifySelectedbillingProfileIsDefault(newBillingAddressName),"selected billing profile is not default");
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.validateCartUpdated(),"Your Next cart has been updated message not present on UI");
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyUpdatedAddressPresentUpdateCartPg(newShipingAddressName+" "+lastName),"updated address not present on Updated cart page");
		s_assert.assertAll();
	}


	//Hybris Project-4781:Update PC Template -ADD Cart , Shipping info, billing info and Save
	@Test
	public void testUpdatePCTemplateAddCart_ShippingInfo_billingInfo_Save_4781() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountID = null;
		String lastName = "lN";
		String newShipingAddressName = TestConstants.ADDRESS_NAME+randomNum;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String secondNewBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNumber;
		String newBillingAddressName = TestConstants.BILLING_ADDRESS_NAME+randomNumber;
		country = driver.getCountry();
		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}  

		logger.info("login is successful");
		storeFrontPCUserPage.clickOnAutoshipCart();
		storeFrontPCUserPage.clickOnContinueShoppingLink();
		storeFrontPCUserPage.clickOnAddtoPCPerksButton();
		String updatedMsg = storeFrontPCUserPage.getAutoshipTemplateUpdatedMsg();
		s_assert.assertTrue(storeFrontPCUserPage.verifyUpdateCartMessage(updatedMsg),"Autoship Cart has not been Updated");
		storeFrontCartAutoShipPage = new StoreFrontCartAutoShipPage(driver);
		storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
		String defaultBillingProfile=storeFrontUpdateCartPage.getDefaultSelectedBillingAddressName();
		logger.info("default billing profile is "+defaultBillingProfile);
		//storeFrontUpdateCartPage.clickOnEditDefaultBillingProfile();
		//Add new billing profile
		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		s_assert.assertTrue(storeFrontUpdateCartPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly created billing address is not present on page");
		//Add new billing address.
		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(secondNewBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickAddANewAddressLink();
		storeFrontUpdateCartPage.enterNewBillingAddressName(newBillingAddressName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingAddressLine1(addressLine1);
		storeFrontUpdateCartPage.enterNewBillingAddressCity(city);
		storeFrontUpdateCartPage.selectNewBillingAddressState();
		storeFrontUpdateCartPage.enterNewBillingAddressPostalCode(postalCode);
		storeFrontUpdateCartPage.enterNewBillingAddressPhoneNumber(phoneNumber);
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		s_assert.assertTrue(storeFrontUpdateCartPage.isTheBillingAddressPresentOnPage(secondNewBillingProfileName),"Newly created billing address is not present on page");
		s_assert.assertTrue(storeFrontUpdateCartPage.isBillingProfileIsSelectedByDefault(defaultBillingProfile),"Default billing profile is not as expected");

		//Add new shipping profile
		storeFrontUpdateCartPage.clickOnEditShipping();
		storeFrontUpdateCartPage.clickOnAddANewShippingAddress();
		storeFrontShippingInfoPage = new StoreFrontShippingInfoPage(driver);
		storeFrontShippingInfoPage.enterNewShippingAddressName(newShipingAddressName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(city);
		storeFrontShippingInfoPage.selectNewShippingAddressState();
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(phoneNumber);
		storeFrontUpdateCartPage.clickOnSaveCRPShippingInfo();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyNewlyCreatedShippingAddressIsSelectedByDefault(newShipingAddressName));
		s_assert.assertTrue(storeFrontUpdateCartPage.verifySelectedShippingAddressIsDefault(newShipingAddressName),"selected shipping address is not default");
		String selectedMethodName = storeFrontUpdateCartPage.selectAndGetShippingMethodName();
		storeFrontUpdateCartPage.clickOnNextStepBtnShippingAddress();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifySelectedShippingMethodNameOnUI(selectedMethodName),"Selected Shipping method name is not present on UI");
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.validateCartUpdated(),"Your Next cart has been updated message not present on UI");
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyUpdatedAddressPresentUpdateCartPg(newShipingAddressName+" "+lastName),"updated address not present on Updated cart page");
		s_assert.assertAll();
	}

	//  Hybris Project-141:PC Perks Total Savings Calculation & disclaimer
	@Test
	public void testPCPerksTotalSavingsCalculation_141() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		//s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"PC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontPCUserPage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();

		//assert total saving on Autoship cart page
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyTotalSavingsIsAvailableOnAutoshipCart(),"Total Savings is not available on Autoship cart");

		s_assert.assertAll();

	}

	//Hybris Project-2141:Check Shipping and Handling Fee for UPS Ground for Order total 1000-999999
	@Test
	public void testShippingAndHandlingFeeForUPSGroundForOrderTotal_1000_999999_2141() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontConsultantPage.applyPriceFilterHighToLow();
		storeFrontConsultantPage.clickAddToBagButton(driver.getCountry());
		String quantity = "10";

		while(true){
			storeFrontConsultantPage.addQuantityOfProduct(quantity);
			String total = storeFrontUpdateCartPage.getTotalPriceOfProduct();
			String orderTotal = total.split("\\$")[1].trim();
			String[] getTotal = orderTotal.split("\\,");
			String finalTotal = getTotal[0]+getTotal[1];
			System.out.println("Order total for consultant"+finalTotal);
			double totalFromUI = Double.parseDouble(finalTotal);
			if(totalFromUI<1000){
				continue;
			}else{
				break;
			}
		}
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPSGroundInOrderSummary();

		String deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		String handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
		logger.info("handlingCharges ="+handlingCharges);
		if(driver.getCountry().equalsIgnoreCase("CA")){
			//Assert of shipping cost from UI
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("CAD$ 15.00"),"Shipping charges on UI is not As per shipping method selected");
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("CAD$ 2.50"),"Handling charges on UI is not As per shipping method selected");
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("$25.00"),"Shipping charges on UI is not As per shipping method selected");
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("$2.50"),"Handling charges on UI is not As per shipping method selected");
		}
		storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		logout();

		driver.get(driver.getURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		//s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"PC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontPCUserPage.applyPriceFilterHighToLow();
		storeFrontPCUserPage.clickAddToBagButton(driver.getCountry());

		while(true){
			storeFrontPCUserPage.addQuantityOfProduct(quantity);
			String total = storeFrontUpdateCartPage.getTotalPriceOfProductForPC();
			String orderTotal = total.split("\\$")[1].trim();
			String[] getTotal = orderTotal.split("\\,");
			String finalTotal = getTotal[0]+getTotal[1];
			System.out.println("Order total for consultant"+finalTotal);
			double totalFromUI = Double.parseDouble(finalTotal);
			if(totalFromUI<1000){
				continue;
			}else{
				break;
			}
		}
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPSGroundInOrderSummary();

		deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
		logger.info("handlingCharges ="+handlingCharges);
		if(driver.getCountry().equalsIgnoreCase("CA")){
			//Assert of shipping cost from UI
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("CAD$ 15.00"),"Shipping charges on UI is not As per shipping method selected");
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("CAD$ 2.50"),"Handling charges on UI is not As per shipping method selected");
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("$25.00"),"Shipping charges on UI is not As per shipping method selected");
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("$2.50"),"Handling charges on UI is not As per shipping method selected");
		}
		s_assert.assertAll();

	}

	//Hybris Project-2148:Check Shipping and Handling Fee for UPS 1Day for Order total 0-999999
	@Test
	public void testCheckShippingAndHandlingFeeForUPS1DayForOrderTotal_0_999999_2148() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontConsultantPage.applyPriceFilterHighToLow();
		storeFrontConsultantPage.clickAddToBagButton(driver.getCountry());

		while(true){
			String total = storeFrontUpdateCartPage.getTotalPriceOfProduct();
			String orderTotal = total.split("\\$")[1].trim();
			System.out.println("Order total for consultant"+orderTotal);
			double totalFromUI = Double.parseDouble(orderTotal);
			if(totalFromUI<0){
				continue;
			}else{
				break;
			}
		}
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPS2DayInOrderSummary();

		String deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		String handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
		logger.info("handlingCharges ="+handlingCharges);
		if(driver.getCountry().equalsIgnoreCase("CA")){
			//Assert of shipping cost from UI
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("CAD$ 20.00"),"Shipping charges on UI is not As per shipping method selected");
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("CAD$ 2.50"),"Handling charges on UI is not As per shipping method selected");
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("$23.00"),"Shipping charges on UI is not As per shipping method selected");
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("$2.50"),"Handling charges on UI is not As per shipping method selected");
		}
		storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		logout();

		driver.get(driver.getURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		//s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"PC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontPCUserPage.applyPriceFilterHighToLow();
		storeFrontPCUserPage.clickAddToBagButton(driver.getCountry());

		while(true){
			String total = storeFrontUpdateCartPage.getTotalPriceOfProductForPC();
			String orderTotal = total.split("\\$")[1].trim();
			System.out.println("Order total for consultant"+orderTotal);
			double totalFromUI = Double.parseDouble(orderTotal);
			if(totalFromUI<0){
				continue;
			}else{
				break;
			}
		}
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPS2DayInOrderSummary();

		deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
		logger.info("handlingCharges ="+handlingCharges);
		if(driver.getCountry().equalsIgnoreCase("CA")){
			//Assert of shipping cost from UI
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("CAD$ 20.00"),"Shipping charges on UI is not As per shipping method selected");
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("CAD$ 2.50"),"Handling charges on UI is not As per shipping method selected");
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("$23.00"),"Shipping charges on UI is not As per shipping method selected");
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("$2.50"),"Handling charges on UI is not As per shipping method selected");
		}
		storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		s_assert.assertAll();
	}

	//Hybris Project-2295:Without Loggin in, Add Multiple Order line to the cart with Multiple Quantity
	@Test
	public void testWithoutLogin_AddMultipleOrderLineTotheCartWithMultipleQuantity_2295() throws InterruptedException{
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String quantity = "2";
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontHomePage.applyPriceFilterLowToHigh();
		storeFrontHomePage.clickAddToBagButtonWithoutFilter();
		String subtotalOfAddedProduct = storeFrontHomePage.getSubTotalOfAddedProduct();
		storeFrontHomePage.addQuantityOfProduct(quantity);
		s_assert.assertTrue(storeFrontHomePage.validateSubTotalAfterQuantityIncreased(subtotalOfAddedProduct,quantity),"subtotal not present on UI as expected");
		s_assert.assertAll();
	}

	//Hybris Project-3874:COM: Join PCPerk in the shipment section - US Sponsor WITHOUT Pulse
	@Test
	public void testJoinPCPerksInShipmentSection_3874() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		List<Map<String, Object>> sponserWithoutPWSList =  null;
		String sponserId=null;
		String rcEmailID = null;
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcEmailID = (String) getValueFromQueryResult(randomRCList, "Username");
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcEmailID,password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+rcEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");

		//Click on place order
		storeFrontHomePage.clickOnCheckoutButton();
		while(true){
			sponserWithoutPWSList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_ACTIVE_US_CONSULTANT_WITHOUT_PWS_AND_PULSE,RFO_DB);
			sponserId=String.valueOf(getValueFromQueryResult(sponserWithoutPWSList, "AccountNumber"));
			storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(sponserId);
			if(storeFrontHomePage.verifySponserSearchResult(sponserId)){
				storeFrontHomePage.clickSearchAgain();
				continue;
			}else
				break;
		}
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinuePCAndRC();

		s_assert.assertTrue(storeFrontHomePage.validatePCPerksCheckBoxIsDisplayed(), "pc perks checkbox is displayed");
		s_assert.assertFalse(storeFrontHomePage.verifyPCPerksCheckBoxIsSelected(),"pc perks checbox is selected for rc user");
		storeFrontHomePage.clickYesIWantToJoinPCPerksCB();

		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.validateCorpCurrentUrlPresent(),"current url is not a corp url");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();
	}

	//Hybris Project-3880:BIZ:Join PCPerk in the Order Summary section -CA Sposnor WITHOUT Pulse
	@Test
	public void testJoinPCPerksInOrderSummarySection_3880() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String accountID= null;
		String accountNumber = null;
		//Get .Biz PWS from database to start enrolling rc user and upgrading it to pc user
		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
		//String emailAddressOfSponser= (String) getValueFromQueryResult(randomConsultantList, "Username"); 
		String bizPWSOfSponser=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));
		if(driver.getCountry().equalsIgnoreCase("ca")){
			List<Map<String, Object>> randomCrossCountryConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_CONSULTANT_NO_PWS_WITH_COUNTRY_RFO,"236"),RFO_DB);
			accountID = String.valueOf(getValueFromQueryResult(randomCrossCountryConsultantList, "AccountID"));
			List<Map<String, Object>> randomCrossCountryConsultantAccountNumberList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
			accountNumber = String.valueOf(getValueFromQueryResult(randomCrossCountryConsultantAccountNumberList, "AccountNumber"));
		}
		else{
			List<Map<String, Object>> randomCrossCountryConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_CONSULTANT_NO_PWS_WITH_COUNTRY_RFO,"40"),RFO_DB);
			accountID = String.valueOf(getValueFromQueryResult(randomCrossCountryConsultantList, "AccountID"));
			List<Map<String, Object>> randomCrossCountryConsultantAccountNumberList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
			accountNumber = String.valueOf(getValueFromQueryResult(randomCrossCountryConsultantAccountNumberList, "AccountNumber"));
		}
		//Open biz pws of Sponser
		storeFrontHomePage.openConsultantPWS(bizPWSOfSponser);
		while(true){
			if(driver.getCurrentUrl().contains("sitenotfound")){
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
				//String emailAddressOfSponser= (String) getValueFromQueryResult(randomConsultantList, "Username"); 
				bizPWSOfSponser=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));
				storeFrontHomePage.openConsultantPWS(bizPWSOfSponser); 
				continue;
			}else
				break;
		} 
		logger.info("biz pws for login is "+bizPWSOfSponser);
		//Login as RC user
		while(true){
			List<Map<String, Object>> randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			String rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
			String accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 

		//s_assert.assertTrue(storeFrontRCUserPage.verifyRCUserPage(rcUserEmailID),"RC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		// Click on Shop link and select All product link  
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Assert continue without sponser link is not present
		s_assert.assertFalse(storeFrontHomePage.verifyContinueWithoutSponserLinkPresent(), "Continue without Sponser link is present on pws enrollment");
		s_assert.assertTrue(storeFrontHomePage.verifyNotYourSponsorLinkIsPresent(),"Not your Sponser link is not present.");

		//		//Click not your sponser link and verify continue without sponser link is present.
		//		storeFrontHomePage.clickOnNotYourSponsorLink();
		//		s_assert.assertTrue(storeFrontHomePage.verifySponserSearchFieldIsPresent(),"Sponser search field is not present");
		//		String url=driver.getCurrentUrl();
		//		//Search for sponser and ids.
		//		storeFrontHomePage.enterSponserNameAndClickSearchAndContinue(accountNumber);

		//check pc perks checkbox at checkout page in order summary Section.
		storeFrontHomePage.checkPCPerksCheckBox();
		//storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		s_assert.assertTrue(storeFrontHomePage.isShippingAddressNextStepBtnIsPresent(),"Shipping Address Next Step Button Is not Present");
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		//		String currentURL=driver.getCurrentUrl();
		//		logger.info("Url After successful enroll is "+currentURL);
		//		s_assert.assertTrue(storeFrontHomePage.validateCorpCurrentUrlPresent(),"current url is not a corp url");
		s_assert.assertAll();
	}

	// Hybris Project-3883:BIZ:Join PCPerk in the Order Summary - US Sponsor WITHOUT Pulse
	@Test
	public void testJoinPCPerksInOrderSummarySection_3883() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNumber;
		String lastName = "lN";
		country = driver.getCountry();
		String URL=null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.FIRST_NAME+randomNumber;
		String emailAddress=firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
		//Enroll Cross Country Consultant without pulse And PWS.
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;

		if(driver.getCountry().equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.selectDifferentCountry();

		//storeFrontHomePage.openConsultantPWS(URL);
		String  newSponserEmailAddress=TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME, newSponserEmailAddress, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.uncheckPulseAndCRPEnrollment();
		s_assert.assertTrue(storeFrontHomePage.verifySubsribeToPulseCheckBoxIsNotSelected(), "Subscribe to pulse checkbox selected after uncheck");
		s_assert.assertTrue(storeFrontHomePage.verifyEnrollToCRPCheckBoxIsNotSelected(), "Enroll to CRP checkbox selected after uncheck");
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		URL=driver.getCurrentUrl();
		String comURL=storeFrontHomePage.convertBizSiteToComSite(URL);
		String urlToAssert=storeFrontHomePage.convertCountryInPWS(comURL);
		logout();

		//Get enrolled Consultant Account Number As Sponser Id.
		List<Map<String, Object>> consultantDetails = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FROM_EMAIL_ADDRESS,newSponserEmailAddress),RFO_DB);
		String sponserId=(String) getValueFromQueryResult(consultantDetails, "AccountNumber"); 

		//Get .biz PWS from database to start enrolling rc user and upgrading it to pc user
		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
		String bizPWSOfSponser=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));

		//Open com pws of Sponser
		storeFrontHomePage.openConsultantPWS(bizPWSOfSponser);
		//Hover shop now and click all products link.
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		//storeFrontHomePage.selectProductAndProceedToBuy();
		storeFrontHomePage.clickAddToBagButton();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNumber, emailAddress, password);

		//Assert continue without sponser link is not present
		s_assert.assertFalse(storeFrontHomePage.verifyContinueWithoutSponserLinkPresent(), "Continue without Sponser link is present on pws enrollment");
		s_assert.assertTrue(storeFrontHomePage.verifyNotYourSponsorLinkIsPresent(),"Not your Sponser link is not present.");

		//check pc perks checkbox at checkout page in order summary section.
		storeFrontHomePage.checkPCPerksCheckBox();
		//Click not your sponser link and verify continue without sponser link is present.
		storeFrontHomePage.clickOnNotYourSponsorLink();
		s_assert.assertTrue(storeFrontHomePage.verifySponserSearchFieldIsPresent(),"Sponser search field is not present");

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		//Search for sponser and ids.
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(sponserId);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		//verify the cross country sponser is selected.
		s_assert.assertTrue(storeFrontHomePage.getSponserNameFromUIWhileEnrollingPCUser().contains(newSponserEmailAddress),"Cross Country Sponser is not selected");
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		s_assert.assertTrue(storeFrontHomePage.isShippingAddressNextStepBtnIsPresent(),"Shipping Address Next Step Button Is not Present");
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();

		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		String currentURL=driver.getCurrentUrl();
		s_assert.assertTrue(urlToAssert.contains(currentURL),"After pc Enrollment the site does not navigated to expected url");
		s_assert.assertAll();
	}

	//Hybris Project-3882:COM:Join PCPerk in the Order Summary section -US Sponsor WITH Pulse
	@Test
	public void testJoinPCPerksInOrderSummarySection_3882() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNumber;
		String lastName = "lN";
		country = driver.getCountry();
		List<Map<String, Object>> randomConsultantList=null;
		List<Map<String, Object>> sponsorIdList =null;
		String requiredCountry=null;
		String requiredCountryId=null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.FIRST_NAME+randomNumber;
		String emailAddress=firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			requiredCountry="us";
			requiredCountryId="236";
		}else{
			requiredCountry="ca";
			requiredCountryId="40";
		}
		//Get Cross Country Sponser With Pulse And PWS.
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",requiredCountry,requiredCountryId),RFO_DB);
		String emailAddressOfSponser= (String) getValueFromQueryResult(randomConsultantList, "Username"); 
		String bizPWSOfSponser=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));
		String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		// sponser search by Account Number
		sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		String sponserId = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));

		String comURL=storeFrontHomePage.convertBizSiteToComSite(bizPWSOfSponser);
		String urlToAssert=storeFrontHomePage.convertCountryInPWS(comURL);
		logger.info(" pws to assert is "+urlToAssert);

		//Get .biz PWS from database to start enrolling rc user and upgrading it to pc user
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
		String bizPWSForEnrollment=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));
		String accountIdOfConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		//Open biz pws of Sponser
		storeFrontHomePage.openConsultantPWS(bizPWSForEnrollment);
		while(true){
			if(driver.getCurrentUrl().contains("sitenotfound")){
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
				bizPWSForEnrollment=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));
				accountIdOfConsultant = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
				storeFrontHomePage.openConsultantPWS(bizPWSForEnrollment);	
				continue;
			}else
				break;
		}	
		logger.info("biz pws to start enroll is "+bizPWSForEnrollment);
		// sponser search by Account Number
		sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountIdOfConsultant),RFO_DB);

		//Hover shop now and click all products link.
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		//storeFrontHomePage.selectProductAndProceedToBuy();
		storeFrontHomePage.clickAddToBagButton();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNumber, emailAddress, password);

		//Assert continue without sponser link is not present
		s_assert.assertFalse(storeFrontHomePage.verifyContinueWithoutSponserLinkPresent(), "Continue without Sponser link is present on pws enrollment");
		s_assert.assertTrue(storeFrontHomePage.verifyNotYourSponsorLinkIsPresent(),"Not your Sponser link is not present.");

		//check pc perks checkbox at checkout page in order summary section.
		storeFrontHomePage.checkPCPerksCheckBox();
		//Click not your sponser link and verify continue without sponser link is present.
		storeFrontHomePage.clickOnNotYourSponsorLink();
		s_assert.assertTrue(storeFrontHomePage.verifySponserSearchFieldIsPresent(),"Sponser search field is not present");

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		//Search for sponser and ids.
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(sponserId);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		//verify the cross country sponser is selected.
		s_assert.assertTrue(storeFrontHomePage.getSponserNameFromUIWhileEnrollingPCUser().contains(emailAddressOfSponser),"Cross Country Sponser is not selected");
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		s_assert.assertTrue(storeFrontHomePage.isShippingAddressNextStepBtnIsPresent(),"Shipping Address Next Step Button Is not Present");
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();

		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		String currentURL=driver.getCurrentUrl();
		logger.info(" pws after successful  enroll is "+currentURL);
		s_assert.assertTrue(currentURL.contains(urlToAssert),"After pc Enrollment the site does not navigated to expected url");
		s_assert.assertAll();
	}

	//Hybris Project-2151:Check the shipping method disclaimers for " UPS Ground (HD)/FedEX"
	@Test
	public void testCheckShippingMethodForUPSGroundHD_FedEX_2151() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");

		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontConsultantPage.clickAddToBagButton(driver.getCountry());
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPSGroundInOrderSummary();

		String selectedShippingMethodName = storeFrontUpdateCartPage.getSelectedShippingMethodName();

		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyShippingAddressContainsShippingMethodName(selectedShippingMethodName),"Checkout page doesn't contain shipping method name FedEx Ground (HD)");
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyShippingAddressContainsShippingMethodNameAfterPlaceOrder(selectedShippingMethodName),"Thanku for your order page doesn't contain shipping method name FedEx Ground (HD)");

		storeFrontUpdateCartPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontUpdateCartPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();
		s_assert.assertTrue(storeFrontOrdersPage.verifyShippingMethodOnTemplateAfterAdhocOrderPlaced(selectedShippingMethodName),"Adhoc order page doesn't contain shipping method name FedEx Ground (HD)");
		s_assert.assertAll();
	}

	//Hybris Project-2152:Check the shipping method disclaimers for " UPS 2Day/FedEx 2Day"
	@Test
	public void testCheckShippingMethodForUPS2Day_FedEx2Day_2152() throws InterruptedException{

		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");

		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontConsultantPage.clickAddToBagButton(driver.getCountry());
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPS2DayInOrderSummary();
		String selectedShippingMethodName = storeFrontUpdateCartPage.getSelectedShippingMethodName();

		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyShippingAddressContainsShippingMethodName(selectedShippingMethodName),"Checkout page doesn't contain shipping method name FedEx Ground (HD)");
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyShippingAddressContainsShippingMethodNameAfterPlaceOrder(selectedShippingMethodName),"Thanku for your order page doesn't contain shipping method name FedEx Ground (HD)");

		storeFrontUpdateCartPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontUpdateCartPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();
		s_assert.assertTrue(storeFrontOrdersPage.verifyShippingMethodOnTemplateAfterAdhocOrderPlaced(selectedShippingMethodName),"Adhoc order page doesn't contain shipping method name FedEx Ground (HD)");
		s_assert.assertAll();
	}

	//Hybris Project-2157:Place An order as logged in PC User and check for PC Perk Promo
	@Test
	public void testPlacedAnAdhocOrderAsPCAndChekcForPCPerkPromo_2157() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";

		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}	
		logger.info("login is successful");
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		s_assert.assertFalse(storeFrontUpdateCartPage.verifyPCPerksPromoDuringPlaceAdhocOrder(), "Pc perk promo is available on Main Account Info Page");
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		s_assert.assertFalse(storeFrontUpdateCartPage.verifyPCPerksPromoDuringPlaceAdhocOrder(), "Pc perk promo is available on Billing Info Page");
		storeFrontUpdateCartPage.clickOnDefaultBillingProfileEdit();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();

		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		s_assert.assertFalse(storeFrontUpdateCartPage.verifyPCPerksPromoDuringPlaceAdhocOrder(), "Pc perk promo is available on order confirmation Page");
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		//String orderNumber = storeFrontUpdateCartPage.getOrderNumberAfterPlaceOrder();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");
		s_assert.assertAll();
	}

	//Hybris Project-2312:Check Prices for PC
	@Test
	public void testPricesForPC_2312() throws InterruptedException	{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID =null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		country = driver.getCountry();
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		logger.info("login is successful");
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();  
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//validate user is able to see various product prices attached to the product. on quick shop page
		s_assert.assertTrue(storeFrontHomePage.validateRetailProductProcesAttachedToProduct(), "retail product prices attached to product is not displayed");
		//Add product to cart & checkout..
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//validate prices related to attched product on summary page..
		s_assert.assertTrue(storeFrontHomePage.validateProductPricingDetailOnSumaaryPage(), "Pricing information related to product is not present on summary page");
		s_assert.assertAll();
	}

	//Hybris Project-2313:Check Prices as Consultant & PC log in
	@Test
	public void testProductPricesAsConsultantAndPC_2313() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		//Login as Consultant..
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID =null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		country = driver.getCountry();
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();  

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//validate user is able to see various product prices attached to the product. on quick shop page
		s_assert.assertTrue(storeFrontHomePage.validateRetailProductProcesAttachedToProduct(), "retail product prices attached to product is not displayed");
		//Add product to cart & checkout..
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//validate prices related to attched product on summary page..
		s_assert.assertTrue(storeFrontHomePage.validateProductPricingDetailOnSumaaryPage(), "Pricing information related to product is not present on summary page");
		storeFrontHomePage.hitBrowserBackBtn();
		logout();
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		logger.info("login is successful");
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();  

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//validate user is able to see various product prices attached to the product. on quick shop page
		s_assert.assertTrue(storeFrontHomePage.validateRetailProductProcesAttachedToProduct(), "retail product prices attached to product is not displayed");
		s_assert.assertAll();
	}

	//Hybris Project-2314:Check Product prices without logging in
	@Test
	public void testProductPricesWithOutLoggingIn_2314(){
		storeFrontHomePage = new StoreFrontHomePage(driver);
		//Click All Products link
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//validate user is able to see various product prices attached to the product..
		s_assert.assertTrue(storeFrontHomePage.validateRetailProductProcesAttachedToProduct(), "retail product prices attached to product is not displayed");
		s_assert.assertAll();
	}

	// Hybris Project-2271:check with Browser back button from checkout to cart
	@Test
	public void testBrowserBackButtonFromCheckoutToCart_2271() throws InterruptedException		{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		country = driver.getCountry();
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		//click All Products link 
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();  

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//hit browser back button..
		storeFrontHomePage.hitBrowserBackBtn();
		//validate user is navigated to shopping cart page..
		storeFrontHomePage.validateMiniCart();
		s_assert.assertAll();
	}

	//Hybris Project-2286:Place an Order with existing customer - AUTOMATION ONLY
	@Test
	public void testPlaceOrderWithExistingCustomer_2286() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> accountNameDetailsList = null;
		List<Map<String, Object>> randomConsultantList =  null;
		String firstNameDB = null;
		String lastNameDB = null;
		String consultantEmailID = null;
		String accountID = null;
		country = driver.getCountry();
		if(country.equalsIgnoreCase("CA")){
			city = TestConstants.CONSULTANT_CITY_FOR_ACCOUNT_INFORMATION_CA;
			postalCode = TestConstants.CONSULTANT_POSTAL_CODE_FOR_ACCOUNT_INFORMATION_CA;
			phoneNumber = TestConstants.CONSULTANT_MAIN_PHONE_NUMBER_FOR_ACCOUNT_INFORMATION_CA;
		}else{
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontUpdateCartPage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
		storeFrontUpdateCartPage.clickOnCheckoutButton();

		//assert First Name with RFO
		accountNameDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_DETAILS_QUERY,consultantEmailID), RFO_DB);
		firstNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "FirstName");
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyFirstNameAndLastNameAndEmailAddressFromUIOnAccountInfoPage(firstNameDB), "First Name on UI is different from DB");

		// assert Last Name with RFO
		lastNameDB = (String) getValueFromQueryResult(accountNameDetailsList, "LastName");
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyFirstNameAndLastNameAndEmailAddressFromUIOnAccountInfoPage(lastNameDB), "Last Name on UI is different from DB");
		storeFrontUpdateCartPage.clickEditMainAccountInfoOnCartPage();

		//assert for email address
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyFirstNameAndLastNameAndEmailAddressFromUIOnAccountInfoPage(consultantEmailID), "Email Address on UI is different from DB");

		//verify that user can edit main account info.
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyUserCanEditMainAccountInfoOnCartPage(),"User cannot edit main account info");
		s_assert.assertAll();
	}

	// Hybris Project-2291:Adhoc shopping cart is persistent when user is navigating among PWS sites ad corp site
	@Test
	public void testAdhocShoppingCartIsPersistentForBizAndCom_2291() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		country = driver.getCountry();
		env = driver.getEnvironment();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		//verify Adhoc Shopping cart for corp site.
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		logout();

		//verify Adhoc shopping cart for biz site.
		String bizPWS=storeFrontHomePage.openPWSSite(country, env);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		logout();

		//verify Adhoc shopping cart for com site.
		String comPWS=storeFrontHomePage.convertBizSiteToComSite(bizPWS);
		storeFrontHomePage.openConsultantPWS(comPWS);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		s_assert.assertAll();
	}


	//Hybris Project-3762:Update the PC Perks Template from US sponsor's BIZ PWS who has Pulse/ PWS
	@Test
	public void testUpdatePCPerksTemplateFromDifferentUSSponsorBizPWS_3762() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontHomePage.openPWSSite(driver.getCountry(), driver.getEnvironment());

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		//s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"PC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		String PWSAfterLogin = driver.getCurrentUrl();
		logger.info("after login "+PWSAfterLogin);
		storeFrontPCUserPage.clickOnAutoshipCart();
		storeFrontPCUserPage.clickOnContinueShoppingLink();
		storeFrontPCUserPage.clickOnAddToPcPerksButton();
		String updatedMsg = TestConstants.PC_PERKS_TEMPLATE_PRODUCT_ADDED;
		s_assert.assertTrue(storeFrontPCUserPage.verifyUpdateCartMessage(updatedMsg),"Autoship Cart has not been Updated");
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyCartUpdateMessage(),"Autoship Cart has not been Updated after click on update cart");
		storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		logout();

		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontHomePage.clickAddToBagButton(driver.getCountry());

		//assert sign up link
		// s_assert.assertFalse(storeFrontHomePage.verifySignUpLinkIsPresent(), "Sign up link is present on checkout page");
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		storeFrontHomePage.clickOnNotYourSponsorLink();
		//assert continue without sponser link is not present and request your sponser button
		s_assert.assertFalse(storeFrontHomePage.verifyContinueWithoutSponserLinkPresent(),"continue without sponser link present");
		s_assert.assertFalse(storeFrontHomePage.verifyRequestASponsorBtn(),"Request a sponser button is present");

		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();

		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		//s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		//s_assert.assertTrue(storeFrontHomePage.getUserNameAForVerifyLogin(firstName).contains(firstName),"Profile Name After Login"+firstName+" and on UI is "+storeFrontHomePage.getUserNameAForVerifyLogin(firstName));
		String currentPWSUrl=driver.getCurrentUrl();
		logger.info("current url After "+currentPWSUrl);
		s_assert.assertTrue(storeFrontHomePage.verifyPWSAfterSuccessfulEnrollment(currentPWSUrl,PWSAfterLogin.split("\\:")[1]),"PWS of the RC after enrollment is not same as the one it started enrollment");  
		s_assert.assertAll();
	}

	//Hybris Project-2167:Login as Existing PC and Place an Adhoc Order, Check for Alert message
	@Test
	public void testLoginAsPCAndPlaceAdhocOrder_CheckAlrtMessage_2167() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnPlaceOrderButton();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyCheckoutConfirmationPOPupPresent(),"pop up not present");
		storeFrontUpdateCartPage.clickOnOkButtonOnCheckoutConfirmationPopUp();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");
		s_assert.assertAll();
	}

	//Hybris Project-2263:Check PC Perk 1st Order confirmation Screen
	@Test
	public void testCheckPcPerkIstOrderConfirmationScreen_2263() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.FIRST_NAME+randomNum;
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();

		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksTermsAndConditionsPopup(),"PC Perks terms and conditions popup not visible when checkboxes for t&c not selected and place order button clicked");
		logger.info("PC Perks terms and conditions popup is visible when checkboxes for t&c not selected and place order button clicked");
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();
	}

	//Hybris Project-3740:Update PC Perks template from sponsor's PWS site
	@Test
	public void testUpdatePCPerksTemplateFromSponserBizPWS_3740() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String emailAddress=firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;

		//Hover shop now and click all products link.
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password,emailAddress);

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		//Get Sponser from database.
		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
		String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		// sponser search by Account Number
		List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		String accountnumber = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));

		//Search for sponser and ids.
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(accountnumber);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();

		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksTermsAndConditionsPopup(),"PC Perks terms and conditions popup not visible when checkboxes for t&c not selected and place order button clicked");
		logger.info("PC Perks terms and conditions popup is visible when checkboxes for t&c not selected and place order button clicked");
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		String comUrlOfSponser=driver.getCurrentUrl();
		String bizUrlOfSponser=storeFrontHomePage.convertComSiteToBizSite(comUrlOfSponser);
		logout();
		storeFrontHomePage.openConsultantPWS(bizUrlOfSponser);
		storeFrontPCUserPage=storeFrontHomePage.loginAsPCUser(emailAddress, password);
		storeFrontPCUserPage.clickOnAutoshipCart();
		storeFrontUpdateCartPage=new StoreFrontUpdateCartPage(driver);
		int getProductCountOnCartPage=Integer.parseInt(storeFrontUpdateCartPage.getProductCountOnAutoShipCartPage());
		int expectedProductCount=getProductCountOnCartPage+1;
		storeFrontUpdateCartPage.clickOnContinueShoppingLink();
		storeFrontUpdateCartPage.selectDifferentProductAndAddItToPCPerks();
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInAutoshipCart(Integer.toString(expectedProductCount)), "Product in Autoship cart is not as expected");
		//update quantity of existing product.
		int getNewProductCountOnCartPage=storeFrontUpdateCartPage.getDifferentProductCountOnAutoShipCartPage();
		if(getNewProductCountOnCartPage<=2){
			storeFrontUpdateCartPage.addQuantityOfProduct("5");
		}else{
			storeFrontUpdateCartPage.updateQuantityOfProductToTheSecondProduct("5");
		}
		s_assert.assertTrue(storeFrontUpdateCartPage.getAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED),"auto ship update cart message from UI is "+storeFrontHomePage.getAutoshipTemplateUpdatedMsg());
		s_assert.assertAll(); 
	}

	//	// Hybris Project-3866:Update PC Perks template from sponsor's PWS site
	//	@Test
	//	public void testUpdatePCPerksTemplateFromSponserComPWS_3866() throws InterruptedException{
	//		RFO_DB = driver.getDBNameRFO();
	//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
	//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
	//		String lastName = "lN";
	//		country = driver.getCountry();
	//		storeFrontHomePage = new StoreFrontHomePage(driver);
	//		String firstName=TestConstants.FIRST_NAME+randomNum;
	//		String emailAddress=firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
	//
	//		//Hover shop now and click all products link.
	//		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
	//
	//		// Products are displayed?
	//		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
	//		logger.info("Quick shop products are displayed");
	//
	//		//Select a product and proceed to buy it
	//		storeFrontHomePage.selectProductAndProceedToBuy();
	//
	//		//Cart page is displayed?
	//		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
	//		logger.info("Cart page is displayed");
	//
	//		//1 product is in the Shopping Cart?
	//		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
	//		logger.info("1 product is successfully added to the cart");
	//
	//		//Click on Check out
	//		storeFrontHomePage.clickOnCheckoutButton();
	//
	//		//Log in or create an account page is displayed?
	//		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
	//		logger.info("Login or Create Account page is displayed");
	//
	//		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
	//		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password,emailAddress);
	//
	//		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
	//		storeFrontHomePage.enterMainAccountInfo();
	//		logger.info("Main account details entered");
	//
	//		//Get Sponser from database.
	//		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
	//		String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
	//		// sponser search by Account Number
	//		List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
	//		String accountnumber = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));
	//
	//		//Search for sponser and ids.
	//		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(accountnumber);
	//		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
	//		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
	//		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
	//
	//		//Enter Billing Profile
	//		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
	//		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
	//		storeFrontHomePage.selectNewBillingCardExpirationDate();
	//		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
	//		storeFrontHomePage.selectNewBillingCardAddress();
	//		storeFrontHomePage.clickOnSaveBillingProfile();
	//		storeFrontHomePage.clickOnBillingNextStepBtn();
	//		storeFrontHomePage.clickPlaceOrderBtn();
	//		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksTermsAndConditionsPopup(),"PC Perks terms and conditions popup not visible when checkboxes for t&c not selected and place order button clicked");
	//		logger.info("PC Perks terms and conditions popup is visible when checkboxes for t&c not selected and place order button clicked");
	//		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
	//		storeFrontHomePage.clickPlaceOrderBtn();
	//		storeFrontHomePage.clickOnRodanAndFieldsLogo();
	//		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
	//		//Add new product and verify products of autoship cart
	//		storeFrontHomePage.clickOnAutoshipCart();
	//		storeFrontUpdateCartPage=new StoreFrontUpdateCartPage(driver);
	//		int getProductCountOnCartPage=Integer.parseInt(storeFrontUpdateCartPage.getProductCountOnAutoShipCartPage());
	//		int expectedProductCount=getProductCountOnCartPage+1;
	//		storeFrontUpdateCartPage.clickOnContinueShoppingLink();
	//		storeFrontUpdateCartPage.selectDifferentProductAndAddItToPCPerks();
	//		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInAutoshipCart(Integer.toString(expectedProductCount)), "Product in Autoship cart is not as expected");
	//		//update quantity of existing product.
	//		int getNewProductCountOnCartPage=storeFrontUpdateCartPage.getDifferentProductCountOnAutoShipCartPage();
	//		if(getNewProductCountOnCartPage<=2){
	//			storeFrontUpdateCartPage.addQuantityOfProduct("5");
	//		}else{
	//			storeFrontUpdateCartPage.updateQuantityOfProductToTheSecondProduct("5");
	//		}
	//		s_assert.assertTrue(storeFrontUpdateCartPage.getAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED),"auto ship update cart message from UI is "+storeFrontHomePage.getAutoshipTemplateUpdatedMsg());
	//		s_assert.assertAll(); 
	//
	//	}

	//Hybris Project-3860:Update the PC Perks Template from US sponsor's COMPWS who has Pulse/ PWS
	@Test
	public void testUpdateThePCPerksTemplateFromUSSponsorCOMPWSWithPulseAndPWS_3860() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
		RFO_DB = driver.getDBNameRFO();
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.FIRST_NAME+randomNum;
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		//Select a product with the price less than $80 and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		String emailAddress = firstName+randomNum+"@xyz.com";
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password, emailAddress);

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		String countryName = null;
		//get cross country sponsor
		if(driver.getCountry().equalsIgnoreCase("us")){
			countryId = "40";
			countryName = "ca";
		}else{
			countryId = "236";
			countryName = "us";
		}
		List<Map<String, Object>> sponserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",countryName,countryId),RFO_DB);
		String sponserHavingPulse = String.valueOf(getValueFromQueryResult(sponserList, "AccountID"));
		String sponsorPWS = String.valueOf(getValueFromQueryResult(sponserList, "URL"));
		// sponser search by Account Number
		List<Map<String, Object>> sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,sponserHavingPulse),RFO_DB);
		String idForConsultant = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));
		storeFrontHomePage.searchCID(idForConsultant);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();

		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();

		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		storeFrontHomePage.clickOnWelcomeDropDown();
		storeFrontCartAutoShipPage = storeFrontHomePage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontCartAutoShipPage.clickOnContinueShoppingLink();
		storeFrontCartAutoShipPage.clickOnAddToPcPerksButton();
		storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontCartAutoShipPage.clickUpdateCartBtn();
		storeFrontCartAutoShipPage.clickOnRodanAndFieldsLogo();
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());
		logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(emailAddress, password);
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage= new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickEditMainAccountInfoOnCartPage();
		s_assert.assertFalse(storeFrontHomePage.verifyContinueWithoutSponserLinkPresent(),"continue without sponser link present");
		s_assert.assertFalse(storeFrontHomePage.verifyRequestASponsorBtn(),"Request a sponser button is present");
		s_assert.assertFalse(storeFrontHomePage.verifyNotYourSponsorLinkIsPresent(),"Not your Sponser link is not present.");

		storeFrontUpdateCartPage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		//String orderNumber = storeFrontUpdateCartPage.getOrderNumberAfterPlaceOrder();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");

		String PWSAfterSelectSponsor = driver.getCurrentUrl();
		String convertedPWSAfterSelectSponsor = storeFrontHomePage.convertCountryInPWS(PWSAfterSelectSponsor);
		String sponsorComPWS = storeFrontHomePage.convertBizSiteToComSite(sponsorPWS);
		s_assert.assertTrue(convertedPWSAfterSelectSponsor.contains(sponsorComPWS.split("\\:")[1].toLowerCase()), "CA sponsor PWS for ca corp"+sponsorComPWS.toLowerCase()+" and on UI after select ca sponsor from ca corp site is "+convertedPWSAfterSelectSponsor);
		s_assert.assertAll();  
	}

	// Hybris Project-4657:Place an adhoc order for RC user enrolled without creating an order
	@Test
	public void testPlaceAdhocOrderForRcUserEnrolledWithoutCreatingAnOrder_4657() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		country = driver.getCountry();
		env = driver.getEnvironment();
		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		String firstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = TestConstants.LAST_NAME;
		String emailAddress = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage = new StoreFrontHomePage(driver);

		storeFrontHomePage.openPWSSite(country, env);
		storeFrontHomePage.clickSignUpnowOnbizSite();
		storeFrontHomePage.enterNewRCDetails(firstName, lastName, emailAddress, password);
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		storeFrontRCUserPage = new StoreFrontRCUserPage(driver);
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontRCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.updateAccountInformation(firstName, lastName, addressLine1, city, postalCode, phoneNumber);
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyConfirmationMessagePresentOnUI(),"Your profile has been updated message not present");
		storeFrontAccountInfoPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontHomePage.clickShippingLinkPresentOnWelcomeDropDown();
		storeFrontShippingInfoPage.clickAddNewShippingProfileLink();
		storeFrontShippingInfoPage.enterNewShippingAddressName(firstName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(city);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(phoneNumber);
		storeFrontShippingInfoPage.selectNewShippingAddressState();
		storeFrontShippingInfoPage.clickOnSaveShippingProfile();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyConfirmationMessagePresentOnUI(),"Your profile has been updated message not present");
		storeFrontShippingInfoPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontShippingInfoPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontBillingInfoPage.enterNewBillingNameOnCard(firstName);
		storeFrontBillingInfoPage.selectNewBillingCardExpirationDate();
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.selectNewBillingCardAddress();
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontHomePage.clickAddToBagButton();
		storeFrontHomePage.clickOnPlaceOrderButton();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(),"Order Not placed successfully");
		s_assert.assertAll();
	}

	// Hybris Project-3857:Place Adhoc Order as RC - AUTOMATION ONLY
	@Test
	public void testPlaceAdhocOrderAsRC_3857() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openPWSSite(driver.getCountry(), driver.getEnvironment());
		// Click on our product link that is located at the top of the page and then click in on quick shop

		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		String emailAddress = firstName+randomNum+"@xyz.com";
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNum, emailAddress, password);

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		logger.info("Account Id of the user is "+accountID);

		// Get Account Number
		List<Map<String, Object>>sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		String sponsorID = (String) getValueFromQueryResult(sponsorIdList, "AccountNumber");

		storeFrontHomePage.clickOnNotYourSponsorLink();
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(sponsorID);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();

		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(), "Order Not placed successfully");
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");

		// Placed Adhoc order with different sponsor
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		logger.info("Account Id of the user is "+accountID);

		// Get Account Number
		sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		sponsorID = (String) getValueFromQueryResult(sponsorIdList, "AccountNumber");

		storeFrontHomePage.clickOnNotYourSponsorLink();
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(sponsorID);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(), "Order Not placed successfully");
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");

		s_assert.assertAll();	

	}

	//Hybris Project-2308:Add 20 Products to the Cart and Update Quantity from Product Details screen
	@Test
	public void testAdd_20_ProductsToTheCartAndUpdateQuantityFromProductDetailsScreen_2308() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		String qty = "20";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontHomePage.clickAddToBagButton();
		storeFrontHomePage.addQuantityOfProduct(qty);
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart(qty),"number of products in cart has not been updated");
		s_assert.assertTrue(storeFrontHomePage.verifyConfirmationMessagePresentOnUI(),"confirmation message is not present on UI");
		s_assert.assertAll();
	}

	//Hybris Project-3879:COM: Join PCPerk in the shipment section - CA Spsonor with Pulse
	@Test
	public void testJoinPCPerksInShipmentSection_3879() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomConsultantList =null;
		List<Map<String, Object>> sponsorIdList=null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNumber;
		String lastName = "lN";
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.FIRST_NAME+randomNumber;
		String emailAddress=firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;

		//Get a Sponser for pc user.
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".com",driver.getCountry(),countryId),RFO_DB);
		String emailAddressOfSponser= (String) getValueFromQueryResult(randomConsultantList, "Username"); 
		String comPWSOfSponser=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));
		String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		// sponser search by Account Number
		sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		String sponserId = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));
		//Get .biz PWS from database to start enrolling rc user and upgrading it to pc user
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
		String emailAddressOfConsultant= (String) getValueFromQueryResult(randomConsultantList, "Username"); 
		String bizPWSOfConsultant=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));
		// sponser search by Account Number
		sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		//Open com pws of Sponser
		storeFrontHomePage.openConsultantPWS(bizPWSOfConsultant);
		//Hover shop now and click all products link.
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//Select a product and proceed to buy it
		storeFrontHomePage.clickAddToBagButton();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNumber, emailAddress, password);

		//Assert the default consultant.
		s_assert.assertTrue(storeFrontHomePage.getSponserNameFromUIWhileEnrollingPCUser().contains(emailAddressOfConsultant),"Default consultant is not the one whose pws is used");
		//Assert continue without sponser link is not present
		s_assert.assertFalse(storeFrontHomePage.verifyContinueWithoutSponserLinkPresent(), "Continue without Sponser link is present on pws enrollment");
		s_assert.assertTrue(storeFrontHomePage.verifyNotYourSponsorLinkIsPresent(),"Not your Sponser link is not present.");

		//Click not your sponser link and verify continue without sponser link is present.
		storeFrontHomePage.clickOnNotYourSponsorLink();
		s_assert.assertTrue(storeFrontHomePage.verifySponserSearchFieldIsPresent(),"Sponser search field is not present");

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		//Search for sponser and ids.
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(sponserId);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		//verify the  sponser is selected.
		s_assert.assertTrue(storeFrontHomePage.getSponserNameFromUIWhileEnrollingPCUser().contains(emailAddressOfSponser),"Cross Country Sponser is not selected");
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		s_assert.assertTrue(storeFrontHomePage.isShippingAddressNextStepBtnIsPresent(),"Shipping Address Next Step Button Is not Present");

		//check pc perks checkbox at checkout page in Shipment section.
		storeFrontHomePage.checkPCPerksCheckBox();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();

		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		String currentURL=driver.getCurrentUrl();
		s_assert.assertTrue(currentURL.contains(comPWSOfSponser),"After pc Enrollment the site does not navigated to expected url");
		s_assert.assertAll();
	}

	//Hybris Project-4773:Kit products are not displayed during adhoc order
	@Test
	public void testVerifyKitProductAreNotDisplayedDuringAdhocOrder_4773() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		storeFrontHomePage = new StoreFrontHomePage(driver);

		//PC-Enrollment
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Verify enrollment kit option are not present for pc User.
		s_assert.assertFalse(storeFrontHomePage.isKitPresentDuringPCEnrollment(), "Kit Options are present during pc enrollment");

		//verify all prices are in respective currency.
		s_assert.assertTrue(storeFrontHomePage.verifyProductPriceAsPerCountry(driver.getCountry()), "Product Prices are not as per country selected");
		s_assert.assertAll();
	}

	//Hybris Project-2282:Try selecting 2 Shipping address; This is not allowed
	@Test
	public void testTrySelecting2ShippingAddress_2282() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		int randomNum = CommonUtils.getRandomNum(1,6);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontShippingInfoPage=new StoreFrontShippingInfoPage(driver);
		storeFrontUpdateCartPage=new StoreFrontUpdateCartPage(driver);
		country = driver.getCountry();
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		//click All Products link 
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();  

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//add a shipping address(if<=1)
		if(country.equalsIgnoreCase("us")){
			storeFrontUpdateCartPage.addAshippingProfile(TestConstants.CITY_US, TestConstants.ADDRESS_LINE_1_US, TestConstants.NEW_SHIPPING_PROFILE_FIRST_NAME_US+randomNum+" last", TestConstants.PHONE_NUMBER_US, TestConstants.POSTAL_CODE_US);
		}
		else{
			storeFrontUpdateCartPage.addAshippingProfile(TestConstants.CITY_CA, TestConstants.ADDRESS_LINE_1_CA, TestConstants.NEW_SHIPPING_PROFILE_FIRST_NAME_CA+randomNum+" last", TestConstants.PHONE_NUMBER_CA, TestConstants.POSTAL_CODE_CA);
		}
		//In shipment section,validate selecting 2 shipping address(only one radio button shld be selected at a time)
		//Select first radio button and validate 2nd is un selected..
		s_assert.assertFalse(storeFrontUpdateCartPage.selectFirstAddressAndValidateSecondIsUnSelected(), "second radio button is also selected!!");
		//Select Second radio button and validate first is un selected..
		s_assert.assertFalse(storeFrontUpdateCartPage.selectSecondAddressAndValidateFirstIsUnSelected(), "first radio button is also selected!!");
		s_assert.assertAll(); 
	}

	//Hybris Project-2368:Check for Error Messages throughout Checkout Flow
	@Test
	public void testValidateErrorMessagesThroughOutChkOutFlow_2368() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		country = driver.getCountry();

		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.FIRST_NAME+randomNum;
		//continue with PC Enrollment Flow..
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product with the price less than $80 and proceed to buy it
		storeFrontHomePage.applyPriceFilterLowToHigh();
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");
		//click 'Create Act' button without entering the new Customers details
		storeFrontHomePage.clickCreateActBtnOnChkOutPage();
		//validate error message for various field(s)..
		s_assert.assertTrue(storeFrontHomePage.validateErrorMessagesForNewCustomerFields(), "Error message(s) for 'New Customer' respective fields are not populated!!");
		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);
		//without entering 'Main Account Info' click save button and validate Error messages..
		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		s_assert.assertTrue(storeFrontHomePage.validateErrorMessagesForMainActInfoFields(), "Error message(s) for respective fields are not populated!!");
		//Enter main act Info and click next without selecting sponsor
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//without entering billing profile information click on save billing profile..
		storeFrontHomePage.clickOnSaveBillingProfile();
		//validate error message on various 'Add new billing Info' field(s)..
		s_assert.assertTrue(storeFrontHomePage.validateErrorMessagesForAddNewBillingInfoFields(), "Error message(s) for'Add new Billing Info' respective fields are not populated!!");
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertAll(); 
	}

	//Hybris Project-2280:Add Multiple cards and try to select Multiple cards for payment on checkout screen
	@Test
	public void testAddMultipleCardsAndSelectMultipleCardForPaymentOnCheckoutScreen_2280() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		String defaultBillingProfile =storeFrontUpdateCartPage.getDefaultSelectedBillingAddressName();
		storeFrontUpdateCartPage.selectAndGetBillingMethodName(driver.getCountry());
		s_assert.assertFalse(storeFrontUpdateCartPage.isNewlyCreatedBillingProfileIsSelectedByDefault(defaultBillingProfile),"Bill to this card radio button is selected old one");
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyIsOnlyOneRadioButtonSelected(driver.getCountry()),"Bill to this card radio button is more than one selected");
		s_assert.assertAll();
	}

	//Hybris Project-2284:Select Shipping Methods
	@Test
	public void testSelectShippingMethod_2284() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String consultantEmailID=null;
		String lN=TestConstants.LAST_NAME;
		String shippingProfileName=TestConstants.NEW_SHIPPING_PROFILE_FIRST_NAME+randomNum;
		if(driver.getCountry().equalsIgnoreCase("us")){
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}else{
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			List<Map<String, Object>>  randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		s_assert.assertTrue(storeFrontUpdateCartPage.isShippingAddressNextStepBtnIsPresent(),"Shipping Address Next Step Button Is not Present");
		storeFrontUpdateCartPage.clickAddNewShippingProfileLink();
		//Enter new shipping address details
		storeFrontUpdateCartPage.enterNewShippingAddressName(shippingProfileName+" "+lN);
		storeFrontUpdateCartPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontUpdateCartPage.enterNewShippingAddressCity(city);
		storeFrontUpdateCartPage.selectNewShippingAddressStateOnCartPage();
		storeFrontUpdateCartPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontUpdateCartPage.enterNewShippingAddressPhoneNumber(phoneNumber);
		//change the shipping method and proceed to next stp
		storeFrontUpdateCartPage.clickOnSaveShippingProfile();
		String defaultSelectedShippingMethod=storeFrontUpdateCartPage.getDefaultSelectedShippingMethodName();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyNewlyCreatedShippingAddressIsSelectedByDefault(shippingProfileName),"Newly created shipping address is not selected by default");
		//Assert the selected shipping method in order summary
		s_assert.assertTrue(storeFrontUpdateCartPage.getSelectedShippingMethodName().toLowerCase().contains(defaultSelectedShippingMethod.toLowerCase()),"The default selected shipping method is not present in order summary shipping dropdown");
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");

		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);
		s_assert.assertTrue(defaultSelectedShippingMethod.toLowerCase().contains(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate().toLowerCase()),"The default selected shipping method is not at order detail page");
		s_assert.assertAll();
	}

	//Hybris Project-2277:Add New Billing Address during checkout and Edit existing one
	@Test
	public void testAddNewBillingAddressDuringCheckoutAndEditExistingOne_2277() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		country = driver.getCountry();
		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();  
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();

		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickAddANewAddressLink();
		storeFrontUpdateCartPage.enterNewBillingAddressName(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingAddressLine1(addressLine1);
		storeFrontUpdateCartPage.enterNewBillingAddressCity(city);
		storeFrontUpdateCartPage.selectNewBillingAddressState();
		storeFrontUpdateCartPage.enterNewBillingAddressPostalCode(postalCode);
		storeFrontUpdateCartPage.enterNewBillingAddressPhoneNumber(phoneNumber);
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		storeFrontUpdateCartPage.clickOnEditDefaultBillingProfile();
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate(TestConstants.CARD_EXP_MONTH,TestConstants.CARD_EXP_YEAR);
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.clickAddANewAddressLink();
		storeFrontUpdateCartPage.selectNewlyAddedBillingAddressName(newBillingProfileName);
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefaultAfterClickOnEdit(newBillingProfileName),"New Billing Profile is not selected by default on CRP cart page");
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		storeFrontUpdateCartPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontUpdateCartPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();
		s_assert.assertTrue(storeFrontOrdersPage.validateBillingAddressOnOrderPage(newBillingProfileName),"billing address is not present on orders page");
		s_assert.assertAll();
	}

	//Hybris Project-2140:Check Shipping and Handling Fee for Order total 0-99.99
	@Test
	public void testCheckShippingAndHandlingFeeForOrderTotal0To99_2140() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontConsultantPage.applyPriceFilterLowToHigh();
		while(true){
			storeFrontConsultantPage.selectProductAndProceedToBuyWithoutFilter();
			String total = storeFrontUpdateCartPage.getTotalPriceOfProduct();
			String orderTotal = total.split("\\$")[1].trim();
			System.out.println("Order total for consultant"+orderTotal);
			double totalFromUI = Double.parseDouble(orderTotal);
			if(totalFromUI<99 && totalFromUI>0){
				break;
			}else{
				storeFrontConsultantPage.navigateToBackPage();
				continue;
			}
		}
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPS2DayInOrderSummary();
		String deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		String handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
		logger.info("handlingCharges ="+handlingCharges);
		if(driver.getCountry().equalsIgnoreCase("CA")){
			//Assert of shipping cost from UI
			s_assert.assertTrue(deliveryCharges.contains(TestConstants.SHIPPING_CHARGES_FOR_UPS2DAY_CA),"Shipping charges on UI is not As per shipping method selected at order history page");
			s_assert.assertTrue(handlingCharges.contains(TestConstants.HANDLING_CHARGES_FOR_UPS2DAY_CA),"Handling charges on UI is not As per shipping method selected at order history page");
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			s_assert.assertTrue(deliveryCharges.contains(TestConstants.SHIPPING_CHARGES_FOR_UPS2DAY_US),"Shipping charges on UI is not As per shipping method selected at order history page");
			s_assert.assertTrue(handlingCharges.contains(TestConstants.HANDLING_CHARGES_FOR_UPS2DAY_US),"Handling charges on UI is not As per shipping method selected at order history page");
		}
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");
		//assert shipping and handling fee
		deliveryCharges = storeFrontUpdateCartPage.getShippingChargesAtOrderConfirmationPage();
		handlingCharges = storeFrontUpdateCartPage.getHandlingChargesAtOrderConfirmationPage();
		logger.info("deliveryCharges 1 ="+deliveryCharges);
		logger.info("handlingCharges 1 ="+handlingCharges);
		if(driver.getCountry().equalsIgnoreCase("CA")){
			//Assert of shipping cost from UI
			s_assert.assertTrue(deliveryCharges.contains(TestConstants.SHIPPING_CHARGES_FOR_UPS2DAY_CA),"Shipping charges on UI is not As per shipping method selected at order history page");
			s_assert.assertTrue(handlingCharges.contains(TestConstants.HANDLING_CHARGES_FOR_UPS2DAY_CA),"Handling charges on UI is not As per shipping method selected at order history page");
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			s_assert.assertTrue(deliveryCharges.contains(TestConstants.SHIPPING_CHARGES_FOR_UPS2DAY_US),"Shipping charges on UI is not As per shipping method selected at order history page");
			s_assert.assertTrue(handlingCharges.contains(TestConstants.HANDLING_CHARGES_FOR_UPS2DAY_US),"Handling charges on UI is not As per shipping method selected at order history page");
		}
		//storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();

		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);
		handlingCharges = storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate();
		deliveryCharges = storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate();
		logger.info("deliveryCharges 2 ="+deliveryCharges);
		logger.info("handlingCharges 2 ="+handlingCharges);
		if(driver.getCountry().equalsIgnoreCase("CA")){
			//Assert of shipping cost from UI
			s_assert.assertTrue(deliveryCharges.contains(TestConstants.SHIPPING_CHARGES_FOR_UPS2DAY_CA),"Shipping charges on UI is not As per shipping method selected at order history page");
			s_assert.assertTrue(handlingCharges.contains(TestConstants.HANDLING_CHARGES_FOR_UPS2DAY_CA),"Handling charges on UI is not As per shipping method selected at order history page");
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			s_assert.assertTrue(deliveryCharges.contains(TestConstants.SHIPPING_CHARGES_FOR_UPS2DAY_US),"Shipping charges on UI is not As per shipping method selected at order history page");
			s_assert.assertTrue(handlingCharges.contains(TestConstants.HANDLING_CHARGES_FOR_UPS2DAY_US),"Handling charges on UI is not As per shipping method selected at order history page");
		}

		// assert for order total > 100
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();		
		while(true){
			storeFrontConsultantPage.clickAddToBagButton(driver.getCountry());
			String total = storeFrontUpdateCartPage.getTotalPriceOfProduct();
			String orderTotal = total.split("\\$")[1].trim();
			System.out.println("Order total for consultant"+orderTotal);
			double totalFromUI = Double.parseDouble(orderTotal);
			if(totalFromUI>99){
				break;
			}else{
				storeFrontConsultantPage.navigateToBackPage();
				continue;
			}
		}
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPS2DayInOrderSummary();

		deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
		logger.info("handlingCharges ="+handlingCharges);
		System.out.println("shipping charges at shipping page "+deliveryCharges);
		System.out.println("handlin charges at shipping page "+handlingCharges);
		logger.info("deliveryCharges 3 ="+deliveryCharges);
		logger.info("handlingCharges 3 ="+handlingCharges);
		if(driver.getCountry().equalsIgnoreCase("CA")){
			//Assert of shipping cost from UI
			s_assert.assertTrue(deliveryCharges.contains(TestConstants.SHIPPING_CHARGES_FOR_UPS2DAY_CA),"When order total>100 Shipping charges on UI is not As per shipping method selected at order history page ");
			s_assert.assertTrue(handlingCharges.contains(TestConstants.HANDLING_CHARGES_FOR_UPS2DAY_CA),"When order total>100 Handling charges on UI is not As per shipping method selected at order history page");
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			s_assert.assertTrue(deliveryCharges.contains(TestConstants.SHIPPING_CHARGES_FOR_UPS2DAY_AND_TOTAL_GREATER_THAN_99),"When order total>100 Shipping charges on UI is not As per shipping method selected at order history page");
			s_assert.assertTrue(handlingCharges.contains(TestConstants.HANDLING_CHARGES_FOR_UPS2DAY_US),"When order total>100 Handling charges on UI is not As per shipping method selected at order history page");
		}
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");
		//assert shipping and handling fee
		deliveryCharges = storeFrontUpdateCartPage.getShippingChargesAtOrderConfirmationPage();
		handlingCharges = storeFrontUpdateCartPage.getHandlingChargesAtOrderConfirmationPage();
		logger.info("deliveryCharges 4 ="+deliveryCharges);
		logger.info("handlingCharges 4 ="+handlingCharges);
		if(driver.getCountry().equalsIgnoreCase("CA")){
			//Assert of shipping cost from UI
			s_assert.assertTrue(deliveryCharges.contains(TestConstants.SHIPPING_CHARGES_FOR_UPS2DAY_CA),"When order total>100 Shipping charges on UI is not As per shipping method selected at order history page");
			s_assert.assertTrue(handlingCharges.contains(TestConstants.HANDLING_CHARGES_FOR_UPS2DAY_CA),"When order total>100 Handling charges on UI is not As per shipping method selected at order history page");
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			s_assert.assertTrue(deliveryCharges.contains(TestConstants.SHIPPING_CHARGES_FOR_UPS2DAY_AND_TOTAL_GREATER_THAN_99),"When order total>100 Shipping charges on UI is not As per shipping method selected at order history page");
			s_assert.assertTrue(handlingCharges.contains(TestConstants.HANDLING_CHARGES_FOR_UPS2DAY_US),"When order total>100 Handling charges on UI is not As per shipping method selected at order history page");
		}
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		// Get Order Number
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);
		handlingCharges = storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate();
		deliveryCharges = storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate();
		logger.info("deliveryCharges 5 ="+deliveryCharges);
		logger.info("handlingCharges 5 ="+handlingCharges);
		if(driver.getCountry().equalsIgnoreCase("CA")){
			//Assert of shipping cost from UI
			s_assert.assertTrue(deliveryCharges.contains(TestConstants.SHIPPING_CHARGES_FOR_UPS2DAY_CA),"When order total>100 Shipping charges on UI is not As per shipping method selected at order history page");
			s_assert.assertTrue(handlingCharges.contains(TestConstants.HANDLING_CHARGES_FOR_UPS2DAY_CA),"When order total>100 Handling charges on UI is not As per shipping method selected at order history page");
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			s_assert.assertTrue(deliveryCharges.contains(TestConstants.SHIPPING_CHARGES_FOR_UPS2DAY_AND_TOTAL_GREATER_THAN_99),"When order total>100 Shipping charges on UI is not As per shipping method selected at order history page");
			s_assert.assertTrue(handlingCharges.contains(TestConstants.HANDLING_CHARGES_FOR_UPS2DAY_US),"When order total>100 Handling charges on UI is not As per shipping method selected at order history page");
		}
		s_assert.assertAll();
	}

	// Hybris Project-2268:Check for PC Perk Promo as PC User and Consultant
	@Test
	public void testCheckForPcPerkPromoAsPCAndConsultant_2268() throws InterruptedException{
		country = driver.getCountry();
		RFO_DB = driver.getDBNameRFO();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();
		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksInfoOnModalWindow(),"PC Perks promo message is not present at modal window as a customer");
		storeFrontHomePage.clickOnModalWindowCloseIcon();
		//login as consultant
		while(true){
			List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
			String consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username"); 
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();
		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		s_assert.assertFalse(storeFrontHomePage.verifyPCPerksInfoOnModalWindow(),"PC Perks promo message is not present at modal window as a customer");
		storeFrontHomePage.clickOnModalWindowCloseIcon();
		//logout();
		driver.get(driver.getURL()+"/"+driver.getCountry());
		while(true){
			List<Map<String, Object>> randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			String pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		s_assert.assertAll();	
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//Mouse hover product and click quick info
		storeFrontHomePage.mouseHoverProductAndClickQuickInfo();
		//Assert for modal window
		s_assert.assertTrue(storeFrontHomePage.isModalWindowExists(),"modal window not exists");
		s_assert.assertFalse(storeFrontHomePage.verifyPCPerksInfoOnModalWindow(),"PC Perks promo message is not present at modal window as a customer");
		storeFrontHomePage.clickOnModalWindowCloseIcon();
	}

	//Hybris Project-1885:Create Adhoc Order with American Express Card
	@Test
	public void testCreateAdhocOrderWithAmericanExpressCard_1885() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID =null;
		String accountId = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontRCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickOnContinueWithoutSponsorLink();
		storeFrontUpdateCartPage.clickOnNextButtonAfterSelectingSponsor();
		String subtotal = storeFrontUpdateCartPage.getSubtotal();
		logger.info("Subtotal while creating order is "+subtotal);
		String deliveryCharges = storeFrontUpdateCartPage.getDeliveryCharges();
		logger.info("Delivery charges while creating order is "+deliveryCharges);
		String handlingCharges = storeFrontUpdateCartPage.getHandlingCharges();
		logger.info("Handling charges while creating order is "+handlingCharges);
		String tax = storeFrontUpdateCartPage.getTax();
		logger.info("Tax while creating order is "+tax);
		String total = storeFrontUpdateCartPage.getTotal();
		logger.info("Total while creating order is "+total);
		String shippingMethod = storeFrontUpdateCartPage.getShippingMethod();
		logger.info("shippingMethod ="+shippingMethod);
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.AMERICAN_EXPRESS_CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE_FOR_SPECIAL_CARDS);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");
		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subtotal),"Adhoc Order template subtotal "+subtotal+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(tax),"Adhoc Order template tax "+tax+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(total),"Adhoc Order template grand total "+total+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingCharges),"Adhoc Order template handling amount "+handlingCharges+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
		s_assert.assertTrue(shippingMethod.contains(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate()),"Adhoc Order template shipping method "+shippingMethod+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getCreditCardNumber().contains(TestConstants.AMERICAN_EXPRESS_CARD_NUMBER.substring(11)),"Adhoc Order template credit card number "+TestConstants.AMERICAN_EXPRESS_CARD_NUMBER.substring(11)+" and on UI is "+storeFrontOrdersPage.getCreditCardNumber());
		s_assert.assertAll();
	}

	//Hybris Project-1888:Create Adhoc Order with Visa Card
	@Test
	public void testCreateAdhocOrderWithVisaCard_1888() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID =null;
		String accountId = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontRCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickOnContinueWithoutSponsorLink();
		storeFrontUpdateCartPage.clickOnNextButtonAfterSelectingSponsor();
		String subtotal = storeFrontUpdateCartPage.getSubtotal();
		logger.info("Subtotal while creating order is "+subtotal);
		String deliveryCharges = storeFrontUpdateCartPage.getDeliveryCharges();
		logger.info("Delivery charges while creating order is "+deliveryCharges);
		String handlingCharges = storeFrontUpdateCartPage.getHandlingCharges();
		logger.info("Handling charges while creating order is "+handlingCharges);
		String tax = storeFrontUpdateCartPage.getTax();
		logger.info("Tax while creating order is "+tax);
		String total = storeFrontUpdateCartPage.getTotal();
		logger.info("Total while creating order is "+total);
		String shippingMethod = storeFrontUpdateCartPage.getShippingMethod();
		logger.info("shippingMethod ="+shippingMethod);
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");
		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subtotal),"Adhoc Order template subtotal "+subtotal+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(tax),"Adhoc Order template tax "+tax+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(total),"Adhoc Order template grand total "+total+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingCharges),"Adhoc Order template handling amount "+handlingCharges+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
		s_assert.assertTrue(shippingMethod.contains(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate()),"Adhoc Order template shipping method "+shippingMethod+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getCreditCardNumber().contains(TestConstants.CARD_NUMBER.substring(12)),"Adhoc Order template credit card number "+TestConstants.CARD_NUMBER.substring(11)+" and on UI is "+storeFrontOrdersPage.getCreditCardNumber());
		s_assert.assertAll();
	}

	//Hybris Project-1891:Create Order With Shipping Method UPS Standard Overnight-CAD$30.00
	@Test
	public void testCreateOrderWithShippingMethodUPStandardOvernight_1891() throws InterruptedException	{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontOrdersPage=new StoreFrontOrdersPage(driver);
		storeFrontPCUserPage=new StoreFrontPCUserPage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		logger.info("login is successful");
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		storeFrontHomePage.selectProductAndProceedToBuy();
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//select UPS Standard Overnight shipping method
		String selectedShippingMethod=storeFrontHomePage.selectShippingMethodUPSStandardOvernightUnderShippingSectionAndGetName();
		//click next on shipping section
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		//click place order
		storeFrontHomePage.clickPlaceOrderBtn();
		//Navigate to Orders Page
		storeFrontHomePage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		//click on first adhoc order placed
		storeFrontOrdersPage.clickOnFirstAdHocOrder();
		//validate the shipping Method as selected on Orders detail page
		s_assert.assertTrue(storeFrontOrdersPage.verifyShippingMethodOnTemplateAfterAdhocOrderPlaced(selectedShippingMethod), "Selected Shipping Method didn't match with the method on Orders page!!");
		s_assert.assertAll(); 
	}

	//Hybris Project-1890:Create Order With Shipping Method UPS 2Day-CAD$20.00
	@Test
	public void testCreateOrderWithShippingMethodUPS2Day_1890() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID =null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontOrdersPage=new StoreFrontOrdersPage(driver);
		storeFrontRCUserPage=new StoreFrontRCUserPage(driver);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		logger.info("login is successful");
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		storeFrontHomePage.selectProductAndProceedToBuy();
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//click edit next to shipment Section 
		storeFrontHomePage.clickEditShippingInShipmentOnCheckoutPage();
		//select UPS 2 Day shipping method
		String selectedShippingMethod=storeFrontHomePage.selectShippingMethodUPS2DayUnderShippingSectionAndGetName();
		//click next on shipping section
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		//click place order
		storeFrontHomePage.clickPlaceOrderBtn();
		//Navigate to Orders Page
		storeFrontHomePage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		//click on first adhoc order placed
		storeFrontOrdersPage.clickOnFirstAdHocOrder();
		//validate the shipping Method as selected on Orders detail page
		s_assert.assertTrue(storeFrontOrdersPage.verifyShippingMethodOnTemplateAfterAdhocOrderPlaced(selectedShippingMethod), "Selected Shipping Method didn't match with the method on Orders page!!");
		s_assert.assertAll(); 
	}


	//Hybris Project-2143:Check Shipping and Handling Fee for UPS Ground for Order total 0-999999-PC Perk Autoship
	@Test
	public void testCheckShippingAndHandlingFeeForUPSGround_2143() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("Login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		logger.info("login is successful"); 
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPSGroundInOrderSummary();
		String deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		String handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
		logger.info("handlingCharges ="+handlingCharges);
		if(driver.getCountry().equalsIgnoreCase("ca")){
			//Assert  shipping cost from UI
			s_assert.assertTrue(deliveryCharges.contains("15.00"),"Shipping charges on UI is not As per shipping method selected");
			//Handling charges
			s_assert.assertTrue(handlingCharges.contains("2.50"),"Handling charges on UI is not As per shipping method selected");
		}else if(driver.getCountry().equalsIgnoreCase("us")){
			//Assert  shipping cost from UI
			s_assert.assertTrue(deliveryCharges.contains("17.00"),"Shipping charges on UI is not As per shipping method selected");
			//Handling charges
			s_assert.assertTrue(handlingCharges.contains("2.50"),"Handling charges on UI is not As per shipping method selected");
		}
		s_assert.assertAll();
	}

	// Hybris Project-2146:Check Shipping and Handling Fee for UPS 2Day for Order total 0-999999-PCPerk Autoship
	@Test
	public void testCheckShippingAndHandlingFeeForUPS2Day_2146() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		logger.info("login is successful"); 
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPS2DayInOrderSummary();
		double subtotal = storeFrontUpdateCartPage.getSubtotalValue();
		logger.info("subtotal ="+subtotal);
		String deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		String handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
		logger.info("handlingCharges ="+handlingCharges);
		if(subtotal<=999999){
			if(driver.getCountry().equalsIgnoreCase("ca")){ 
				//Assert  shipping cost from UI
				s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("CAD$ 20.00"),"Shipping charges on UI is not As per shipping method selected");
				//Handling charges
				s_assert.assertTrue(handlingCharges.equalsIgnoreCase("CAD$ 2.50"),"Handling charges on UI is not As per shipping method selected");
			}else if(driver.getCountry().equalsIgnoreCase("us")){
				//Assert  shipping cost from UI
				s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("$23.00"),"Shipping charges on UI is not As per shipping method selected");
				//Handling charges
				s_assert.assertTrue(handlingCharges.equalsIgnoreCase("$2.50"),"Handling charges on UI is not As per shipping method selected");
			}
		}else{
			logger.info(" Order total is not in required range");
		}
		s_assert.assertAll();
	}

	// Hybris Project-2150:Check Shipping and Handling Fee for UPS 1Day for Order total 0-999999-PCPerk
	@Test
	public void testCheckShippingAndHandlingFeeForUPS1Day_2150() throws InterruptedException  {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("Login error for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		logger.info("login is successful"); 
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPStandardOvernightInOrderSummary();
		double subtotal = storeFrontUpdateCartPage.getSubtotalValue();
		logger.info("subtotal ="+subtotal);
		String deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		String handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
		logger.info("handlingCharges ="+handlingCharges);
		if(subtotal<=999999){
			if(driver.getCountry().equalsIgnoreCase("ca")){ 
				//Assert  shipping cost from UI
				s_assert.assertTrue(deliveryCharges.contains("30.00"),"Shipping charges on UI is not As per shipping method selected");
				//Handling charges
				s_assert.assertTrue(handlingCharges.contains("2.50"),"Handling charges on UI is not As per shipping method selected");
			}else if(driver.getCountry().equalsIgnoreCase("us")){
				s_assert.assertTrue(deliveryCharges.contains("17.00"),"Shipping charges on UI is not As per shipping method selected");
				//Handling charges
				s_assert.assertTrue(handlingCharges.contains("2.50"),"Handling charges on UI is not As per shipping method selected");
			}
		}else{
			logger.info(" Order total is not in required range");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1880:Create Adhoc Order with Multiple line items
	@Test
	public void testCreateAdhocOrderWithMultipleLineItems_1880() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		} 
		logger.info("login is successful");
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		String firstProductName = storeFrontUpdateCartPage.clickAddToBagAndGetProductName("1");
		storeFrontUpdateCartPage.clickOnContinueShoppingLink();
		String secondProductName = storeFrontUpdateCartPage.clickAddToBagAndGetProductName("2");
		storeFrontUpdateCartPage.clickOnContinueShoppingLink();
		String thirdProductName = storeFrontPCUserPage.clickAddToBagAndGetProductName("3");
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		// get details of adhoc order
		String subtotal = storeFrontUpdateCartPage.getSubtotal();
		logger.info("subtotal ="+subtotal);
		String deliveryCharges = storeFrontUpdateCartPage.getDeliveryCharges();
		logger.info("deliveryCharges ="+deliveryCharges);
		String handlingCharges = storeFrontUpdateCartPage.getHandlingCharges();
		logger.info("handlingCharges ="+handlingCharges);
		String tax = storeFrontUpdateCartPage.getTax();
		logger.info("tax ="+tax);
		String total = storeFrontUpdateCartPage.getTotal();
		logger.info("total ="+total);
		String shippingMethod = storeFrontUpdateCartPage.getShippingMethod();
		logger.info("shippingMethod ="+shippingMethod);
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		String BillingAddress = storeFrontUpdateCartPage.getSelectedBillingAddress();
		logger.info("BillingAddress ="+BillingAddress);
		storeFrontUpdateCartPage.clickOnDefaultBillingProfileEdit();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");
		//assert product names
		s_assert.assertTrue(storeFrontUpdateCartPage.getProductNameAtOrderConfirmationPage("2").contains(firstProductName),"When select first product, Product name "+firstProductName+" and at order confirmation page "+storeFrontUpdateCartPage.getProductNameAtOrderConfirmationPage("2"));
		s_assert.assertTrue(storeFrontUpdateCartPage.getProductNameAtOrderConfirmationPage("3").contains(secondProductName),"When select second product, Product name "+secondProductName+" and at order confirmation page "+storeFrontUpdateCartPage.getProductNameAtOrderConfirmationPage("3"));
		s_assert.assertTrue(storeFrontUpdateCartPage.getProductNameAtOrderConfirmationPage("4").contains(thirdProductName),"When select third product, Product name "+thirdProductName+" and at order confirmation page "+storeFrontUpdateCartPage.getProductNameAtOrderConfirmationPage("4"));
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subtotal),"Adhoc Order template subtotal "+subtotal+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(tax),"Adhoc Order template tax "+tax+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(total),"Adhoc Order template grand total "+total+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingCharges),"Adhoc Order template handling amount "+handlingCharges+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
		s_assert.assertTrue(shippingMethod.contains(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate()),"Adhoc Order template shipping method "+shippingMethod+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());
		//assert product names at adhoc template
		s_assert.assertTrue(storeFrontOrdersPage.getProductNameFromAdhocTemplate("2").contains(firstProductName),"When select first product, Product name "+firstProductName+" and at Adhoc template "+storeFrontOrdersPage.getProductNameFromAdhocTemplate("2"));
		s_assert.assertTrue(storeFrontOrdersPage.getProductNameFromAdhocTemplate("3").contains(secondProductName),"When select second product, Product name "+secondProductName+" and at Adhoc template "+storeFrontOrdersPage.getProductNameFromAdhocTemplate("3"));
		s_assert.assertTrue(storeFrontOrdersPage.getProductNameFromAdhocTemplate("4").contains(thirdProductName),"When select third product, Product name "+thirdProductName+" and at Adhoc template "+storeFrontOrdersPage.getProductNameFromAdhocTemplate("4"));
		s_assert.assertAll();
	}

	// Hybris Project-2108:Do not update billing profile for autoship on changing default selection
	@Test
	public void testDoNotUpdateBillingProfileForAutoShipOnChangingDefaultSelection_2108() throws InterruptedException	 {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		String country = driver.getCountry();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String accountID = null;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage=new StoreFrontUpdateCartPage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage=storeFrontHomePage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		//Add a new Billing Profile
		storeFrontBillingInfoPage.clickAddNewBillingProfileLink();
		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontBillingInfoPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontBillingInfoPage.selectNewBillingCardExpirationDate(TestConstants.CARD_EXP_MONTH, TestConstants.CARD_EXP_YEAR);
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.selectNewBillingCardAddress();
		//storeFrontBillingInfoPage.selectUseThisBillingProfileFutureAutoshipChkbox();
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();
		//Make the newly created billing profile as default Profile
		storeFrontBillingInfoPage.makeBillingProfileDefault(newBillingProfileName);
		//validate billing profile updated
		s_assert.assertTrue(storeFrontBillingInfoPage.validateBillingProfileUpdated(),"Billing Profile is not updated!!");
		//Go to Autoship cart
		storeFrontHomePage.clickOnAutoshipCart();
		storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();
		//validate newly selected billing profile is NOT reflected under billing profile Autoship Order
		s_assert.assertFalse(storeFrontUpdateCartPage.validateNewlySelectedDefaultBillingProfileIsNotUpdatedInAutoshipBillingProfileSection(newBillingProfileName),"Newly selected default biiling profile is updated under Autoship Billing Section");
		s_assert.assertAll();
	}

	//Hybris Project-2390:Shipping address selected in autoship cart.
	@Test
	public void testShippingAddressSelectedInAutoShipCart_2390() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		String country = driver.getCountry();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage=new StoreFrontUpdateCartPage(driver);
		if(country.equalsIgnoreCase("us")){
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
		}
		else if(country.equalsIgnoreCase("ca")){
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
		} 
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage=storeFrontHomePage.clickShippingLinkPresentOnWelcomeDropDown();

		storeFrontShippingInfoPage.clickAddNewShippingProfileLink();
		String newShippingAddressName = TestConstants.ADDRESS_NAME+randomNum;
		storeFrontShippingInfoPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(city);
		storeFrontShippingInfoPage.selectNewShippingAddressState();
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER);
		storeFrontShippingInfoPage.selectUseThisShippingProfileFutureAutoshipChkbox();
		storeFrontShippingInfoPage.clickOnSaveShippingProfile();
		//change default shipping profile selection and validate Update AutoShip PopUp
		storeFrontShippingInfoPage.makeShippingProfileAsDefault(newShippingAddressName);
		s_assert.assertTrue(storeFrontShippingInfoPage.validateUpdateAutoShipPopUpPresent(), "Update AutoShip PopUp is not present!!");
		//Go to Autoship cart
		storeFrontHomePage.clickOnAutoshipCart();
		storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();
		//validate newly selected Shipping profile is reflected under shipment section under Autoship Order
		s_assert.assertTrue(storeFrontUpdateCartPage.validateNewlySelectedDefaultShippingProfileIsUpdatedInAutoshipShippingSection(newShippingAddressName+" "+lastName),"Newly selected default Shipping profile is NOT updated under Autoship Shipping Section");
		s_assert.assertAll();
	}

	//Hybris Project-2149:Check Shipping and Handling Fee for UPS 1Day for Order total 0-999999-CRPAutoship
	@Test
	public void testCheckShippingAndHandlingFeeForUPS1DayCRPAutoship_2149() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO();
		String country = driver.getCountry();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage=new StoreFrontUpdateCartPage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful"); 
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontHomePage.selectProductAndProceedToAddToCRPfterLogin();
		//click Update More Info button on Autoship Page
		storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();
		storeFrontUpdateCartPage.clickOnEditShipping();
		storeFrontUpdateCartPage.selectShippingMethodUPStandardOvernightUnderShippingMethodOnAutoShipOrderPage();
		storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtn();
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		//validate Delivery/Shipping Charges On Order Summary
		String handlingCharges=storeFrontUpdateCartPage.getHandlingCharges();
		logger.info("handling charges"+handlingCharges);
		String deliveryCharges=storeFrontUpdateCartPage.getDeliveryCharges();
		logger.info("Delivery charges"+deliveryCharges);
		if(driver.getCountry().equalsIgnoreCase("us")){
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("$2.50"),"Handling charges on UI is not As per shipping method selected for CRP Autoship");
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("$35.00"),"Shipping/Delivery charges on UI is not As per shipping method selected for CRP Autoship");
		}else if(driver.getCountry().equalsIgnoreCase("ca")){
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("CAD$ 2.50"),"Handling charges on UI is not As per shipping method selected for CRP Autoship");
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("CAD$ 30.00"),"Shipping/Delivery charges on UI is not As per shipping method selected for CRP Autoship"); 
		}
		s_assert.assertAll();
	}

	//Hybris Project-2147:Check Shipping and Handling Fee for UPS 2Day for Order total 0-999999-EnrollmentKit
	@Test
	public void testCheckShippingAndHandlingFeeForUPS2DayConsultantEnrollmentKit_2147() throws InterruptedException {
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
		String firstName=TestConstants.FIRST_NAME+randomNum;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.applyPriceFilterHighToLow();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.addQuantityOfProduct("5");
		storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
		//double subTotal=storeFrontHomePage.getSubTotalValueOnReviewOrderPage();
		String handlingCharges=storeFrontHomePage.getHandlingChargesOnReviewOrderPage();
		String deliveryCharges=storeFrontHomePage.getShippingChargesOnReviewOrderPage();
		//Validate shipping cost from UI
		if(driver.getCountry().equalsIgnoreCase("ca")){
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("CAD$ 0.00"),"Shipping charges on UI is not As per shipping method selected");
			//Validate Handling charges cost from UI
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("CAD$ 0.00"),"Handling charges on UI is not As per shipping method selected");
		}else if(driver.getCountry().equalsIgnoreCase("us")){
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("$0.00"),"Shipping charges on UI is not As per shipping method selected");
			//Validate Handling charges cost from UI
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("$0.00"),"Handling charges on UI is not As per shipping method selected");

		}
		s_assert.assertAll();
	}

	//Hybris Project-1889:Create Adhoc Order with Master Card
	@Test
	public void testCreateAdhocOrderWithMasterCard_1889() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID =null;
		String accountId = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontRCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickOnContinueWithoutSponsorLink();
		storeFrontUpdateCartPage.clickOnNextButtonAfterSelectingSponsor();
		String subtotal = storeFrontUpdateCartPage.getSubtotal();
		logger.info("Subtotal while creating order is "+subtotal);
		String deliveryCharges = storeFrontUpdateCartPage.getDeliveryCharges();
		logger.info("Delivery charges while creating order is "+deliveryCharges);
		String handlingCharges = storeFrontUpdateCartPage.getHandlingCharges();
		logger.info("Handling charges while creating order is "+handlingCharges);
		String tax = storeFrontUpdateCartPage.getTax();
		logger.info("Tax while creating order is "+tax);
		String total = storeFrontUpdateCartPage.getTotal();
		logger.info("Total while creating order is "+total);
		String shippingMethod = storeFrontUpdateCartPage.getShippingMethod();
		logger.info("shippingMethod ="+shippingMethod);
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.MASTER_CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");
		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subtotal),"Adhoc Order template subtotal "+subtotal+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(tax),"Adhoc Order template tax "+tax+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(total),"Adhoc Order template grand total "+total+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingCharges),"Adhoc Order template handling amount "+handlingCharges+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
		s_assert.assertTrue(shippingMethod.contains(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate()),"Adhoc Order template shipping method "+shippingMethod+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getCreditCardNumber().contains(TestConstants.MASTER_CARD_NUMBER.substring(12)),"Adhoc Order template credit card number "+TestConstants.MASTER_CARD_NUMBER.substring(11)+" and on UI is "+storeFrontOrdersPage.getCreditCardNumber());
		s_assert.assertAll();
	}

	//Hybris Project-1886:Shouldn't allow Discover Card on the storefront
	@Test
	public void testShouldNotAllowDiscoverCardOnTHeStoreFront_1886() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID =null;
		String accountId = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomRCList = 
					DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontRCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickOnContinueWithoutSponsorLink();
		storeFrontUpdateCartPage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.DISCOVER_CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		s_assert.assertTrue(storeFrontHomePage.validateInvalidCreditCardMessage(), "Please enter a valid credit card message is displayed");
		s_assert.assertAll();
	}

	// Hybris Project-2092:"Edit Shopping Cart" while checking out
	@Test
	public void testEditShoppingCartWhileCheckingOut_2092() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		String country = driver.getCountry();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;

		if(country.equalsIgnoreCase("us")){
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
		}
		else if(country.equalsIgnoreCase("ca")){
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
		} 
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product with the price less than $80 and proceed to buy it
		storeFrontHomePage.applyPriceFilterLowToHigh();
		storeFrontHomePage.selectProductAndProceedToBuyWithoutFilter();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");
		//update qty to 2 of the first product
		storeFrontHomePage.addQuantityOfProduct("2"); 
		//add another product in the cart
		storeFrontHomePage.addAnotherProduct();

		logger.info("2 products are successfully added to the cart");
		//update qty to 2 of the second product
		storeFrontHomePage.updateQuantityOfProductToTheSecondProduct("2"); 

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//click on edit shopping cart link
		storeFrontHomePage.clickEditShoppingCartLink();
		//Increase the qty of product 1 to 3
		storeFrontHomePage.addQuantityOfProduct("3"); 
		//Decrease the qty of product2 to 1
		storeFrontHomePage.updateQuantityOfProductToTheSecondProduct("1");

		//get the sub-total of the first product
		double subtotal1=storeFrontHomePage.getSubTotalOfFirstProduct();
		//get the sub-total of the second product
		double subtotal2=storeFrontHomePage.getSubTotalOfSecondProduct();
		//validate sub-total is recalculated accordingly to the updated qty of product(s)
		s_assert.assertTrue(storeFrontHomePage.validateSubTotal(subtotal1, subtotal2), "sub-total is not recalculated accordingly to the updated qty of product(s)");
		//click on check-out button
		storeFrontHomePage.clickOnCheckoutButton();
		//verify product p1 and p2 are displayed in the order?
		s_assert.assertTrue(storeFrontHomePage.validate2ProductsAreDisplayedInReviewOrderSection().trim().equalsIgnoreCase("2"),"Two products are not displayed in review order section");
		s_assert.assertAll(); 
	}

	// Hybris Project-2303:Navigate to existing Cart from Mini cart in the header
	@Test
	public void testNavigateToExistingCartFromMiniCartInTheHeader_2303() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		s_assert.assertTrue(storeFrontConsultantPage.validateNextCRPMiniCart(), "next CRP Mini cart is not displayed");
		//Add a item to the cart and validate the mini cart in the header section
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();  

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		storeFrontHomePage.selectProductAndProceedToBuy();
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		//verify Mini cart?
		s_assert.assertTrue(storeFrontHomePage.validateMiniCart(), "mini cart is not being displayed");
		//click on mini cart icon and verify products on cart page
		s_assert.assertTrue(storeFrontHomePage.clickMiniCartAndValidatePreaddedProductsOnCartPage(),"detailed view of cart is not displayed");
		s_assert.assertAll();
	}	

	//Hybris Project-3748:Click On "Buy Now" for a logged in Consultant.
	@Test
	public void testClickOnBuyNowForALoggedInConsultant_3748() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontConsultantPage.clickAddToBagButtonWithoutFilter();
		s_assert.assertTrue(storeFrontConsultantPage.isCartPageDisplayed(),"cart page is not displayed");
		s_assert.assertAll();
	}

}