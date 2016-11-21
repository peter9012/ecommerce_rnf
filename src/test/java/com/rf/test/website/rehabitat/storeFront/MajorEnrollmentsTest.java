package com.rf.test.website.rehabitat.storeFront;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontConsultantEnrollNowPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontHomePage;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class MajorEnrollmentsTest extends StoreFrontWebsiteBaseTest{
	
	public MajorEnrollmentsTest() {
		randomString = CommonUtils.getRandomWord(7);
		pcFirstName=TestConstants.PC_FIRST_NAME+"."+randomString;
		lastName = TestConstants.LAST_NAME+"."+randomString;
		email = lastName+TestConstants.EMAIL_SUFFIX;
	}
	
	private static final Logger logger = LogManager
			.getLogger(MajorEnrollmentsTest.class.getName());

	public String email=null;
	private String pcFirstName = null;
	private String lastName = null;
	private String randomString = null;


	@Test
	public void testConsultantEnrollment(){
		sfEnrollNowPage = sfHomePage.clickOnEnrollNow();
		sfEnrollNowPage.searchSponsorAndSelectFromResult(TestConstants.SPONSOR);
	}
	
	@Test
	public void testPCEnrollment(){
		sfShopSkinCarePage = sfHomePage.clickOnAllProducts();
		sfShopSkinCarePage.addFirstProductToBag();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, pcFirstName, lastName, email, password).clickOnCreateAccountButton();
	}

	

	//Test Case Hybris Phase 2-3720 :: Version : 1 :: Perform Consultant Account termination through my account
	@Test(enabled=false)//Duplicate test,covered in Enrollment validation TC-4308
	public void testAccountTerminationPageForConsultant_3720() throws InterruptedException {
		/*		RFO_DB = driver.getDBNameRFO(); 
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		 */		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
	}


}