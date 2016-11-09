package com.rf.test.website.storeFront.hybris;

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

public class PC_CartAndCheckoutValidationTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(PC_CartAndCheckoutValidationTest.class.getName());
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
	private String state = null;

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
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);  
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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

	// Hybris Project-142 Autoship template - manage products in cart - PC perk 
	@Test //**** THIS TEST FAILS IF ONE ITEM IN CART IS ABOVE THRESHOLD ****
	public void testAutoshipTemplateManagePoductsInCartPCPerk_142() throws InterruptedException {
		String qtyOfProducts="4";
		String newQtyOfProducts="3";
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountID = null;
		double remainingSVValue= 0;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);  
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
		for(int j=2;j<=4;j++){
			if(storeFrontUpdateCartPage.getNoOfProductInCart()<3){
				storeFrontUpdateCartPage.clickOnContinueShoppingLink();
				storeFrontUpdateCartPage.selectProductAndProceedToBuyForPC(j); 
			}else{
				break;
			}
			noOfProduct = storeFrontUpdateCartPage.getNoOfProductInCart();  
		}
		for(int i=noOfProduct; i>=1; i--){
			boolean flag = storeFrontUpdateCartPage.getValueOfFlagForPC(i);
			String subTot = null;
			if(flag==true){
				subTot = storeFrontUpdateCartPage.getSubtotalFromCart().split("\\$")[1].trim();
				if(subTot.contains(",")){
					String[] subTotStringArray = subTot.split(",");
					subTot= subTotStringArray[0]+subTotStringArray[1];
				}
				double SVValue = Double.parseDouble(subTot);
				String SVValueOfRemovedProduct = storeFrontUpdateCartPage.removeProductsFromCartForPC(i);
				remainingSVValue = storeFrontUpdateCartPage.compareSubtotalValue(SVValueOfRemovedProduct, SVValue);
				noOfProduct = storeFrontUpdateCartPage.getNoOfProductInCart(); 
				if(driver.getCountry().equalsIgnoreCase("us")){
					if(remainingSVValue>=80.00 && noOfProduct>1){
						s_assert.assertTrue(storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG),"Error message for product removal from UI is "+storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG);
					}
				}
				else {
					if(remainingSVValue>=90.00 && noOfProduct>1){
						s_assert.assertTrue(storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG),"Error message for product removal from UI is 3 "+storeFrontHomePage.getProductRemovedAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_REMOVED_MSG);
					}
				}}
			else{
				logger.info("SV value is null");
			}
		}
		if(driver.getCountry().equalsIgnoreCase("us")){
			s_assert.assertTrue(storeFrontHomePage.getThresholdMessageIsDisplayed().contains(TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG),"Error message for threshold condition for zero quantity from UI is  "+storeFrontHomePage.getThresholdMessageIsDisplayed()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG);
		}else {
			s_assert.assertTrue(storeFrontHomePage.getThresholdMessageIsDisplayed().contains(TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG_CA_FOR_PC),"Error message for threshold condition for zero quantity from UI is 4 "+storeFrontHomePage.getThresholdMessageIsDisplayed()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_THRESHOLD_MSG);
		}
		storeFrontHomePage.addQuantityOfProduct(qtyOfProducts);
		s_assert.assertTrue(storeFrontHomePage.getAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED),"auto ship update cart message from UI is "+storeFrontHomePage.getAutoshipTemplateUpdatedMsg()+" while expected msg is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED);
		storeFrontHomePage.addQuantityOfProduct(newQtyOfProducts);
		s_assert.assertTrue(storeFrontHomePage.getAutoshipTemplateUpdatedMsg().contains(TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED),"auto ship update cart message from UI is "+storeFrontHomePage.getAutoshipTemplateUpdatedMsg()+" while expected is "+TestConstants.AUTOSHIP_TEMPLATE_PRODUCT_ADDED);
		s_assert.assertAll(); 
	}

	// Hybris Project-2130:To verify Change date functionality for PC shouldnt be present on the storefront
	@Test 
	public void testVerifyChangeDateFunctionalityForPCUser_2130() throws InterruptedException   {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);  
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontPCUserPage.clickEditPCPerksLinkPresentOnWelcomeDropDown();
		storeFrontCartAutoShipPage = new StoreFrontCartAutoShipPage(driver);
		storeFrontUpdateCartPage=storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditShipping();
		s_assert.assertFalse(storeFrontUpdateCartPage.checkDateFunctionality(), "check date functionality is present");
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
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
					(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
					(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
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
		logger.info("PWS after enrollment is:"+pws);
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
		System.out.println("After order placed is:"+pwsAfterOrderPlaced);
		logger.info("After "+pwsAfterOrderPlaced);
		s_assert.assertTrue(storeFrontHomePage.verifyUrlAfterplacedAnAdhocOrder(pws, pwsAfterOrderPlaced), "pws before and are not equal");
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

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

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
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(), "Order Not placed successfully");

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
		logger.info("PWS after enrollment is:"+pws);
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
		logger.info("PWS after order placed is:"+pwsAfterOrderPlaced);
		//PWS has nothing to do with sponsor receiving commission
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
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
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
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}  

		logger.info("login is successful");
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontPCUserPage.clickAddToBagButton();
		storeFrontPCUserPage.clickOnCheckoutButtonAfterAddProduct();
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
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountIdForPCUser = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountIdForPCUser);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
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
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
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
			state = TestConstants.PROVINCE_CA;
		}else{
			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
		//		//Add new billing profile
		//		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
		//		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		//		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		//		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
		//		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		//		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		//		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		//		s_assert.assertTrue(storeFrontUpdateCartPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly created billing address is not present on page");
		//Add new billing address.
		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(secondNewBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
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
		storeFrontShippingInfoPage.selectNewShippingAddressState(state);
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
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		} 
		//s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"PC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontPCUserPage.clickEditPCPerksLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();

		//assert total saving on Autoship cart page
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyTotalSavingsIsAvailableOnAutoshipCart(),"Total Savings is not available on Autoship cart");
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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

		//For Cross country sponsor
		if(driver.getCountry().equalsIgnoreCase("ca")){
			countryId="236";
		}else{
			countryId="40";
		}
		storeFrontHomePage.clickOnCheckoutButton();
		while(true){
			sponserWithoutPWSList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITHOUT_PULSE_RFO, countryId),RFO_DB);
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
	@Test(enabled=false)//test related to switching on US site
	public void testJoinPCPerksInOrderSummarySection_3883() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNumber;
		String lastName = "lN";
		country = driver.getCountry();
		String URL=null;
		String state = null;
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
			state = TestConstants.PROVINCE_US;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.selectDifferentCountry();

		//storeFrontHomePage.openConsultantPWS(URL);
		String  newSponserEmailAddress=TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME, newSponserEmailAddress, password, addressLine1, city, state,postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.uncheckPulseAndCRPEnrollment();
		s_assert.assertTrue(storeFrontHomePage.verifySubsribeToPulseCheckBoxIsNotSelected(), "Subscribe to pulse checkbox selected after uncheck");
		s_assert.assertTrue(storeFrontHomePage.verifyEnrollToCRPCheckBoxIsNotSelected(), "Enroll to CRP checkbox selected after uncheck");
		storeFrontHomePage.clickNextButton();
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

		//Select a product and proceed to buy it
		//storeFrontHomePage.selectProductAndProceedToBuy();
		storeFrontHomePage.clickAddToBagButton();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNumber, emailAddress, password);


		//check pc perks checkbox at checkout page in order summary section.
		storeFrontHomePage.checkPCPerksCheckBox();
		//Click not your sponser link and verify continue without sponser link is present.
		storeFrontHomePage.clickOnNotYourSponsorLink();

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		//Search for sponser and ids.
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(sponserId);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();

		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		String currentURL=driver.getCurrentUrl();
		//s_assert.assertTrue(urlToAssert.contains(currentURL),"After pc Enrollment the site does not navigated to expected url");
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
		List<Map<String, Object>> sponsorIdList = null;
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

		storeFrontHomePage.clickAddToBagButton();

		//Cart page is displayed?
		//1 product is in the Shopping Cart?
		// *** ^^^ ABOVE CHECKS ASSERTED IN NEXT STEP ***
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME+randomNumber, emailAddress, password);

		//Assert continue without sponser link is not present

		//check pc perks checkbox at checkout page in order summary section.
		storeFrontHomePage.checkPCPerksCheckBox();
		//Click not your sponser link and verify continue without sponser link is present.
		storeFrontHomePage.clickOnNotYourSponsorLink();

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		//Search for sponser and ids.
		storeFrontHomePage.enterSponsorNameAndClickOnSearchForPCAndRC(sponserId);
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPCAndRC();
		//*** NOT REQUIRED CCS validation removed
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
		//*** VERIFY NEXT BUTTON BY CLICKING IT ***
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();

		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
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
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
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
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("error");
			if(isSiteNotFoundPresent){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
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
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButtonAfterAddProduct();
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
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
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
		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();
		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
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
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
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
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		storeFrontHomePage.clickOnWelcomeDropDown();
		storeFrontHomePage.clickEditPCPerksLinkPresentOnWelcomeDropDown();
		storeFrontCartAutoShipPage = new StoreFrontCartAutoShipPage(driver);
		storeFrontCartAutoShipPage.clickOnContinueShoppingLink();
		storeFrontCartAutoShipPage.clickOnAddToPcPerksButton();
		storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontCartAutoShipPage.clickUpdateCartBtn();
		storeFrontCartAutoShipPage.clickOnRodanAndFieldsLogo();
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
		s_assert.assertTrue(driver.getCurrentUrl().contains("corprfo")||driver.getCurrentUrl().contains("qa.rodanandfields"), "PC is not redirecting to corporate site after placed order");
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
		String emailAddressOfConsultant = null;
		String bizPWSOfConsultant=null;
		//Get a Sponser for pc user.
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".com",driver.getCountry(),countryId),RFO_DB);
		String emailAddressOfSponser= (String) getValueFromQueryResult(randomConsultantList, "Username"); 
		String comPWSOfSponser=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));
		String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		// sponser search by Account Number
		sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		String sponserId = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));
		while(true){		
			//Get .biz PWS from database to start enrolling rc user and upgrading it to pc user
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
			emailAddressOfConsultant= (String) getValueFromQueryResult(randomConsultantList, "Username"); 
			bizPWSOfConsultant=String.valueOf(getValueFromQueryResult(randomConsultantList, "URL"));
			// sponser search by Account Number
			sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
			//Open com pws of Sponser
			storeFrontHomePage.openConsultantPWS(bizPWSOfConsultant);
			boolean isError = driver.getCurrentUrl().toLowerCase().contains("sitenotfound");
			if(isError){
				continue;
			}
			else
				break;
		}	
		//Hover shop now and click all products link.
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		//Select a product and proceed to buy it
		storeFrontHomePage.clickAddToBagButton();
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
		storeFrontHomePage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		String currentURL=driver.getCurrentUrl();
		logger.info("pws of sponsor after split is "+comPWSOfSponser.split(":")[1]);
		s_assert.assertTrue(currentURL.toLowerCase().contains(comPWSOfSponser.split(":")[1].toLowerCase()),"After pc Enrollment the site does not navigated to expected url");
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
			List<Map<String, Object>> randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			String pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("Login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		} 
		logger.info("login is successful"); 
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPSGroundForAdhocOrder();
		String deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		if(driver.getCountry().equalsIgnoreCase("ca")){
			//Assert  shipping cost from UI
			s_assert.assertTrue(storeFrontUpdateCartPage.isDeliveryChargesPresent(),"Shipping/Delivery charges on UI is not present");
		}else if(driver.getCountry().equalsIgnoreCase("us")){
			//Assert  shipping cost from UI
			s_assert.assertTrue(storeFrontUpdateCartPage.isDeliveryChargesPresent(),"Shipping/Delivery charges on UI is not present");
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
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		} 
		logger.info("login is successful"); 
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethod2DayForAdhocOrder();
		// storeFrontUpdateCartPage.selectShippingMethodUPS2DayInOrderSummary();
		double subtotal = storeFrontUpdateCartPage.getSubtotalValue();
		logger.info("subtotal ="+subtotal);
		String deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		if(subtotal<=999999){
			if(driver.getCountry().equalsIgnoreCase("ca")){ 
				//Assert  shipping cost from UI
				s_assert.assertTrue(storeFrontUpdateCartPage.isDeliveryChargesPresent(),"Shipping charges is not present on UI");
			}else if(driver.getCountry().equalsIgnoreCase("us")){
				//Assert  shipping cost from UI
				s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("$23.00"),"Shipping charges on UI is not As per shipping method selected");
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
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("Login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		} 
		logger.info("login is successful"); 
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		//storeFrontUpdateCartPage.selectShippingMethodUPStandardOvernightInOrderSummary();
		storeFrontUpdateCartPage.selectShippingMethodUPSStandardOverNightForAdhocOrder();
		double subtotal = storeFrontUpdateCartPage.getSubtotalValue();
		logger.info("subtotal ="+subtotal);
		String deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		if(subtotal<=999999){
			if(driver.getCountry().equalsIgnoreCase("ca")){ 
				//Assert  shipping cost from UI
				s_assert.assertTrue(storeFrontUpdateCartPage.isDeliveryChargesPresent(),"Shipping charges is not present on UI");
			}else if(driver.getCountry().equalsIgnoreCase("us")){
				s_assert.assertTrue(deliveryCharges.contains("17.00"),"Shipping charges on UI is not As per shipping method selected");
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
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
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
		/*		String handlingCharges = storeFrontUpdateCartPage.getHandlingCharges();
		logger.info("handlingCharges ="+handlingCharges);*/
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
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
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
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(subtotal),"Adhoc Order template subtotal "+subtotal+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getTaxAmountFromAutoshipTemplate().contains(tax),"Adhoc Order template tax "+tax+" and on UI is "+storeFrontOrdersPage.getTaxAmountFromAdhocOrderTemplate());
		s_assert.assertTrue(storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate().contains(total),"Adhoc Order template grand total "+total+" and on UI is "+storeFrontOrdersPage.getGrandTotalFromAutoshipTemplate());
		/*		s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingCharges),"Adhoc Order template handling amount "+handlingCharges+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
		 */		s_assert.assertTrue(shippingMethod.contains(storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate()),"Adhoc Order template shipping method "+shippingMethod+" and on UI is "+storeFrontOrdersPage.getShippingMethodFromAutoshipTemplate());
		 //assert product names at adhoc template
		 s_assert.assertTrue(storeFrontOrdersPage.getProductNameFromAdhocTemplate("2").contains(firstProductName),"When select first product, Product name "+firstProductName+" and at Adhoc template "+storeFrontOrdersPage.getProductNameFromAdhocTemplate("2"));
		 s_assert.assertTrue(storeFrontOrdersPage.getProductNameFromAdhocTemplate("3").contains(secondProductName),"When select second product, Product name "+secondProductName+" and at Adhoc template "+storeFrontOrdersPage.getProductNameFromAdhocTemplate("3"));
		 s_assert.assertTrue(storeFrontOrdersPage.getProductNameFromAdhocTemplate("4").contains(thirdProductName),"When select third product, Product name "+thirdProductName+" and at Adhoc template "+storeFrontOrdersPage.getProductNameFromAdhocTemplate("4"));
		 s_assert.assertAll();
	}


	//Hybris Project-4013:Logged in RC/PC/ Consultant to Corp site and click on "Buy Now"
	@Test 
	public void testLoggedInPCtoCorpSiteAndClickOnBuyNow_4013() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		} 
		//s_assert.assertTrue(storeFrontPCUserPage.verifyPCUserPage(),"PC User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontPCUserPage.clickAddToBagButtonWithoutFilter();
		s_assert.assertTrue(storeFrontPCUserPage.isCartPageDisplayed(),"card page is not displayed after clicking buy now button");
		s_assert.assertAll();
	}

	//Hybris Project-4840:Product Prices and SV/QV Values display
	@Test 
	public void testProductPricesAndSVQVValuesDisplay_4840() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		//verify 'Price' & 'SV' Value is displayed for each product
		s_assert.assertTrue(storeFrontHomePage.getPriceInformationOfProductsOnProductsPage().trim().contains("Your Price"),"Price is not shown for the products");
		s_assert.assertTrue(storeFrontHomePage.getPriceInformationOfProductsOnProductsPage().trim().contains("SV"),"SV Value is not shown for the products");
		logout();
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		//verify when user is not logged IN only 'Retail' Price is displayed and no 'SV' Value is shown
		s_assert.assertTrue(storeFrontHomePage.getPriceInformationOfProductsOnProductsPage().trim().contains("Retail"),"Retail Price is not shown for the products");
		s_assert.assertFalse(storeFrontHomePage.getPriceInformationOfProductsOnProductsPage().trim().contains("SV"),"SV Value is shown for the products");
		//For PC User
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		} 
		logger.info("login is successful");
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		//verify 'Price' displayed & 'SV' Value is not displayed for each product
		s_assert.assertTrue(storeFrontHomePage.getPriceInformationOfProductsOnProductsPage().trim().contains("Your Price"),"Price is not shown for the products");
		s_assert.assertFalse(storeFrontHomePage.getPriceInformationOfProductsOnProductsPage().trim().contains("SV"),"SV Value is shown for the products");
		logout();
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		//verify when user is not logged IN only 'Retail' Price is displayed and no 'SV' Value is shown
		s_assert.assertTrue(storeFrontHomePage.getPriceInformationOfProductsOnProductsPage().trim().contains("Retail"),"Retail Price is not shown for the products");
		s_assert.assertFalse(storeFrontHomePage.getPriceInformationOfProductsOnProductsPage().trim().contains("SV"),"SV Value is shown for the products");
		//For RC USER
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID =null;
		accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			System.out.println("Account ID IS"+accountId);
			logger.info("Account Id of the user is "+accountId);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		} 
		logger.info("login is successful");
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		//verify 'Retail Price' displayed & 'SV' Value is not displayed for each product
		s_assert.assertTrue(storeFrontHomePage.getPriceInformationOfProductsOnProductsPage().trim().contains("Retail"),"Retail Price is not shown for the products");
		s_assert.assertFalse(storeFrontHomePage.getPriceInformationOfProductsOnProductsPage().trim().contains("SV"),"SV Value is shown for the products");
		logout();
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		//verify when user is not logged IN only 'Retail' Price is displayed and no 'SV' Value is shown
		s_assert.assertTrue(storeFrontHomePage.getPriceInformationOfProductsOnProductsPage().trim().contains("Retail"),"Retail Price is not shown for the products");
		s_assert.assertFalse(storeFrontHomePage.getPriceInformationOfProductsOnProductsPage().trim().contains("SV"),"SV Value is shown for the products");
		s_assert.assertAll();
	}

}