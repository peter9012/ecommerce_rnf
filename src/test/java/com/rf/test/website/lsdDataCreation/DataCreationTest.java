package com.rf.test.website.lsdDataCreation;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.test.website.RFWebsiteBaseTest;

public class DataCreationTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(DataCreationTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private String RFO_DB = null;
	private String kitName = null;
	private String regimenName = null;
	private String enrollmentType = null;
	private String addressLine1 = null;
	private String city = null;
	private String postalCode = null;
	private String phoneNumber = null;
	private String country = null;
	private String env = null;


	@Test
	public void testLSDDataCreation() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		storeFrontHomePage = new StoreFrontHomePage(driver);
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
		env = driver.getEnvironment();		
		kitName = TestConstants.KIT_PRICE_EXPRESS_CA;			 
		addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		city = TestConstants.CITY_CA;
		postalCode = TestConstants.POSTAL_CODE_CA;
		phoneNumber = TestConstants.PHONE_NUMBER_CA;
		String cardNumber = TestConstants.CARD_NUMBER;
		String nameOnCard = TestConstants.FIRST_NAME+randomNum;
		String productQuantity = "5";
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		List<Map<String, Object>> sponsorList =  null;

		/**
		 * Mary McQueen enrollment(TOP LEVEL)
		 */
		String topLevelUserEmailID = TestConstants.FIRST_NAME_FOR_TOP_LEVEL_CONSULTANT+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		String firstName = TestConstants.FIRST_NAME_FOR_TOP_LEVEL_CONSULTANT;
		String lastName = TestConstants.LAST_NAME_FOR_TOP_LEVEL_CONSULTANT;
		String consultantEmailID = 	storeFrontHomePage.createConsultant("test", kitName, regimenName, enrollmentType, firstName, lastName, topLevelUserEmailID, password, addressLine1, city, postalCode, phoneNumber, cardNumber,nameOnCard, socialInsuranceNumber, productQuantity );
		Assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible for top level consultant");
		logger.info("Top level consultant email : "+consultantEmailID);
		System.out.println("Top level consultant email : "+consultantEmailID);
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		logout();

		//get Account number of top level consultant
		String sponsorIDOfTopLevelConsultant = null;
		sponsorList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FROM_EMAIL_ADDRESS,consultantEmailID),RFO_DB);
		sponsorIDOfTopLevelConsultant = String.valueOf(getValueFromQueryResult(sponsorList, "AccountNumber"));

		/**
		 * LEVEL 1 Ramone Marine consultant enrollment under Mary McQueen 
		 */
		driver.get(driver.getURL()+"/"+country);
		String SINForFirstConsultantOfL1 = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstNameForFirstConsultantOfL1 = TestConstants.FIRST_NAME_FOR_FIRST_CONSULTANT_OF_L1;
		String lastNameForFirstConsultantOfL1 = TestConstants.LAST_NAME_FOR_FIRST_CONSULTANT_OF_L1;
		String emailIdForFirstConsultantOfL1 = TestConstants.FIRST_NAME_FOR_FIRST_CONSULTANT_OF_L1+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		String firstConsultantEmailIDOfL1 = storeFrontHomePage.createConsultant(sponsorIDOfTopLevelConsultant, kitName, regimenName, enrollmentType, firstNameForFirstConsultantOfL1, lastNameForFirstConsultantOfL1, emailIdForFirstConsultantOfL1, password, addressLine1, city, postalCode, phoneNumber, cardNumber,nameOnCard, SINForFirstConsultantOfL1, productQuantity );
		logger.info("First consultant email of L1: "+firstConsultantEmailIDOfL1);
		System.out.println("First consultant email of L1: "+firstConsultantEmailIDOfL1);
		Assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible for first consultant of L1");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		logout();

		/**
		 * LEVEL 1 Luigi Shalhoub consultant enrollment under Mary McQueen 
		 */
		driver.get(driver.getURL()+"/"+country);
		String SINForSecondConsultantOfL1 = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstNameForSecondConsultantOfL1 = TestConstants.FIRST_NAME_FOR_SECOND_CONSULTANT_OF_L1;
		String lastNameForSecondConsultantOfL1 = TestConstants.LAST_NAME_FOR_SECOND_CONSULTANT_OF_L1;
		String emailIdForSecondConsultantOfL1 = TestConstants.FIRST_NAME_FOR_SECOND_CONSULTANT_OF_L1+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		String secondConsultantEmailIDOfL1 = storeFrontHomePage.createConsultant(sponsorIDOfTopLevelConsultant, kitName, regimenName, enrollmentType, firstNameForSecondConsultantOfL1, lastNameForSecondConsultantOfL1, emailIdForSecondConsultantOfL1, password, addressLine1, city, postalCode, phoneNumber, cardNumber,nameOnCard, SINForSecondConsultantOfL1, productQuantity );
		logger.info("Second consultant email of L1: "+secondConsultantEmailIDOfL1);
		System.out.println("Second consultant email of L1: "+secondConsultantEmailIDOfL1);
		Assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible for second consultant of L1");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		logout();

		/**
		 * LEVEL 1 Guido Quaroni consultant enrollment under Mary McQueen 
		 */
		driver.get(driver.getURL()+"/"+country);
		String SINForThirdConsultantOfL1 = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstNameForThirdConsultantOfL1 = TestConstants.FIRST_NAME_FOR_THIRD_CONSULTANT_OF_L1;
		String lastNameForThirdConsultantOfL1 = TestConstants.LAST_NAME_FOR_THIRD_CONSULTANT_OF_L1;
		String emailIdForThirdConsultantOfL1 = TestConstants.FIRST_NAME_FOR_THIRD_CONSULTANT_OF_L1+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		String thirdConsultantEmailIDOfL1 = storeFrontHomePage.createConsultant(sponsorIDOfTopLevelConsultant, kitName, regimenName, enrollmentType, firstNameForThirdConsultantOfL1, lastNameForThirdConsultantOfL1, emailIdForThirdConsultantOfL1, password, addressLine1, city, postalCode, phoneNumber, cardNumber,nameOnCard, SINForThirdConsultantOfL1, productQuantity );
		logger.info("Third consultant email of L1: "+thirdConsultantEmailIDOfL1);
		System.out.println("Third consultant email of L1: "+thirdConsultantEmailIDOfL1);
		Assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible for third consultant of L1");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		logout();

		/**
		 * LEVEL 1 Harv Clarkson consultant enrollment under Mary McQueen 
		 */
		driver.get(driver.getURL()+"/"+country);
		String SINForFourthConsultantOfL1 = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstNameForFourthConsultantOfL1 = TestConstants.FIRST_NAME_FOR_FOURTH_CONSULTANT_OF_L1;
		String lastNameForFourthConsultantOfL1 = TestConstants.LAST_NAME_FOR_FOURTH_CONSULTANT_OF_L1;
		String emailIdForFourthConsultantOfL1 = TestConstants.FIRST_NAME_FOR_FOURTH_CONSULTANT_OF_L1+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		String fourthConsultantEmailIDOfL1 = storeFrontHomePage.createConsultant(sponsorIDOfTopLevelConsultant, kitName, regimenName, enrollmentType, firstNameForFourthConsultantOfL1, lastNameForFourthConsultantOfL1, emailIdForFourthConsultantOfL1, password, addressLine1, city, postalCode, phoneNumber, cardNumber,nameOnCard, SINForFourthConsultantOfL1, productQuantity );
		logger.info("Fourth consultant email of L1: "+fourthConsultantEmailIDOfL1);
		System.out.println("Fourth consultant email of L1: "+fourthConsultantEmailIDOfL1);
		Assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible for fourth consultant of L1");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		logout();

		/**
		 * LEVEL 1 Sally Carrera consultant enrollment under Mary McQueen 
		 */
		driver.get(driver.getURL()+"/"+country);
		String SINForFifthConsultantOfL1 = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstNameForFifthConsultantOfL1 = TestConstants.FIRST_NAME_FOR_FIFTH_CONSULTANT_OF_L1;
		String lastNameForFifthConsultantOfL1 = TestConstants.LAST_NAME_FOR_FIFTH_CONSULTANT_OF_L1;
		String emailIdForFifthConsultantOfL1 = TestConstants.FIRST_NAME_FOR_FIFTH_CONSULTANT_OF_L1+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		String fifthConsultantEmailIDOfL1 = storeFrontHomePage.createConsultant(sponsorIDOfTopLevelConsultant, kitName, regimenName, enrollmentType, firstNameForFifthConsultantOfL1, lastNameForFifthConsultantOfL1, emailIdForFifthConsultantOfL1, password, addressLine1, city, postalCode, phoneNumber, cardNumber,nameOnCard, SINForFifthConsultantOfL1, productQuantity );
		logger.info("Fifth consultant email of L1: "+fifthConsultantEmailIDOfL1);
		System.out.println("Fifth consultant email of L1: "+fifthConsultantEmailIDOfL1);
		Assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible for fifth consultant of L1");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		logout();

		/**
		 * LEVEL 1 Flo Lewis consultant enrollment under Mary McQueen 
		 */
		driver.get(driver.getURL()+"/"+country);
		String SINForSixthConsultantOfL1 = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstNameForSixthConsultantOfL1 = TestConstants.FIRST_NAME_FOR_SIXTH_CONSULTANT_OF_L1;
		String lastNameForSixthConsultantOfL1 = TestConstants.LAST_NAME_FOR_SIXTH_CONSULTANT_OF_L1;
		String emailIdForSixthConsultantOfL1 = TestConstants.FIRST_NAME_FOR_SIXTH_CONSULTANT_OF_L1+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		String sixthConsultantEmailIDOfL1 = storeFrontHomePage.createConsultant(sponsorIDOfTopLevelConsultant, kitName, regimenName, enrollmentType, firstNameForSixthConsultantOfL1, lastNameForSixthConsultantOfL1, emailIdForSixthConsultantOfL1, password, addressLine1, city, postalCode, phoneNumber, cardNumber,nameOnCard, SINForSixthConsultantOfL1, productQuantity );
		logger.info("Sixth consultant email of L1: "+sixthConsultantEmailIDOfL1);
		System.out.println("Sixth consultant email of L1: "+sixthConsultantEmailIDOfL1);
		Assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible for sixth consultant of L1");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		logout();

		/**
		 * LEVEL 1 Fillmore Carlin consultant enrollment under Mary McQueen 
		 */
		driver.get(driver.getURL()+"/"+country);
		String SINForSeventhConsultantOfL1 = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstNameForSeventhConsultantOfL1 = TestConstants.FIRST_NAME_FOR_SEVENTH_CONSULTANT_OF_L1;
		String lastNameForSeventhConsultantOfL1 = TestConstants.LAST_NAME_FOR_SEVENTH_CONSULTANT_OF_L1;
		String emailIdForSeventhConsultantOfL1 = TestConstants.FIRST_NAME_FOR_SEVENTH_CONSULTANT_OF_L1+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		String seventhConsultantEmailIDOfL1 = storeFrontHomePage.createConsultant(sponsorIDOfTopLevelConsultant, kitName, regimenName, enrollmentType, firstNameForSeventhConsultantOfL1, lastNameForSeventhConsultantOfL1, emailIdForSeventhConsultantOfL1, password, addressLine1, city, postalCode, phoneNumber, cardNumber,nameOnCard, SINForSeventhConsultantOfL1, productQuantity );
		logger.info("Seventh consultant email of L1: "+seventhConsultantEmailIDOfL1);
		System.out.println("Seventh consultant email of L1: "+seventhConsultantEmailIDOfL1);
		Assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible for seventh consultant of L1");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		logout();

		/**
		 * LEVEL 1 Mack Crystal consultant enrollment under Mary McQueen 
		 */
		driver.get(driver.getURL()+"/"+country);
		String SINForEighthConsultantOfL1 = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstNameForEighthConsultantOfL1 = TestConstants.FIRST_NAME_FOR_EIGHTH_CONSULTANT_OF_L1;
		String lastNameForEighthConsultantOfL1 = TestConstants.LAST_NAME_FOR_EIGHTH_CONSULTANT_OF_L1;
		String emailIdForEighthConsultantOfL1 = TestConstants.FIRST_NAME_FOR_EIGHTH_CONSULTANT_OF_L1+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		String eighthConsultantEmailIDOfL1 = storeFrontHomePage.createConsultant(sponsorIDOfTopLevelConsultant, kitName, regimenName, enrollmentType, firstNameForEighthConsultantOfL1, lastNameForEighthConsultantOfL1, emailIdForEighthConsultantOfL1, password, addressLine1, city, postalCode, phoneNumber, cardNumber,nameOnCard, SINForEighthConsultantOfL1, productQuantity );
		logger.info("Eighth consultant email of L1: "+eighthConsultantEmailIDOfL1);
		System.out.println("Eighth consultant email of L1: "+eighthConsultantEmailIDOfL1);
		Assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible for eighth consultant of L1");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		logout();

		/**
		 * LEVEL 1 Woody Car PC enrollment under Mary McQueen 
		 */
		driver.get(driver.getURL()+"/"+country);
		String firstNameForFirstPCOfL1 = TestConstants.FIRST_NAME_FOR_FIRST_PC_OF_L1;
		String lastNameForFirstPCOfL1 = TestConstants.LAST_NAME_FOR_FIRST_PC_OF_L1;
		String emailIdForFirstPCOfL1 = TestConstants.FIRST_NAME_FOR_FIRST_PC_OF_L1+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		String firstPCEmailIDOfL1 = storeFrontHomePage.createPC(firstNameForFirstPCOfL1, lastNameForFirstPCOfL1, password, emailIdForFirstPCOfL1, sponsorIDOfTopLevelConsultant, newBillingProfileName);
		logger.info("First pc email of L1: "+firstPCEmailIDOfL1);
		System.out.println("First pc email of L1: "+firstPCEmailIDOfL1);
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		Assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownPresentForCheckUserRegistered(), "First PC of L1 is not registered successfully");
		driver.get(driver.getURL()+"/"+country);
		if(storeFrontHomePage.verifyWelcomeDropdownPresentForCheckUserRegistered() == true){
			logout();
		}

		/**
		 * LEVEL 1 PT Flea PC enrollment under Mary McQueen 
		 */
		driver.get(driver.getURL()+"/"+country);
		String firstNameForSecondPCOfL1 = TestConstants.FIRST_NAME_FOR_SECOND_PC_OF_L1;
		String lastNameForSecondPCOfL1 = TestConstants.LAST_NAME_FOR_SECOND_PC_OF_L1;
		String emailIdForSecondPCOfL1 = TestConstants.FIRST_NAME_FOR_SECOND_PC_OF_L1+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		String secondPCEmailIDOfL1 = storeFrontHomePage.createPC(firstNameForSecondPCOfL1, lastNameForSecondPCOfL1, password, emailIdForSecondPCOfL1, sponsorIDOfTopLevelConsultant, newBillingProfileName);
		logger.info("Second pc email of L1: "+secondPCEmailIDOfL1);
		System.out.println("Second pc email of L1: "+secondPCEmailIDOfL1);
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		Assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownPresentForCheckUserRegistered(), "User NOT registered successfully");
		driver.get(driver.getURL()+"/"+country);
		if(storeFrontHomePage.verifyWelcomeDropdownPresentForCheckUserRegistered() == true){
			logout();
		}

		//-------------------------------------------------------------------LEVEL 1 COMPLETED---------------------------------------------------------------------------------------------------------------------------------------------------------------

		/**
		 * LEVEL 2 Snotrod Romano consultant enrollment under Ramone Marine 
		 */

		//get Account number of first consultant of L1
		String sponsorIDOfFirstConsultantOfL1 = null;
		sponsorList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FROM_EMAIL_ADDRESS,firstConsultantEmailIDOfL1),RFO_DB);
		sponsorIDOfFirstConsultantOfL1 = String.valueOf(getValueFromQueryResult(sponsorList, "AccountNumber"));

		driver.get(driver.getURL()+"/"+country);
		String SINForFirstConsultantOfL2 = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstNameForFirstConsultantOfL2 = TestConstants.FIRST_NAME_FOR_FIRST_CONSULTANT_OF_L2;
		String lastNameForFirstConsultantOfL2 = TestConstants.LAST_NAME_FOR_FIRST_CONSULTANT_OF_L2;
		String emailIdForFirstConsultantOfL2 = TestConstants.FIRST_NAME_FOR_FIRST_CONSULTANT_OF_L2+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		String firstConsultantEmailIDOfL2 = storeFrontHomePage.createConsultant(sponsorIDOfFirstConsultantOfL1, kitName, regimenName, enrollmentType, firstNameForFirstConsultantOfL2, lastNameForFirstConsultantOfL2, emailIdForFirstConsultantOfL2, password, addressLine1, city, postalCode, phoneNumber, cardNumber,nameOnCard, SINForFirstConsultantOfL2, productQuantity );
		logger.info("First consultant email of L2: "+firstConsultantEmailIDOfL2);
		System.out.println("First consultant email of L2: "+firstConsultantEmailIDOfL2);
		Assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible for first consultant of L1");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		logout();

		/**
		 * LEVEL 2 Darell Waltrip consultant enrollment under Luigi Shalhoub 
		 */

		//get Account number of second consultant of L1
		String sponsorIDOfSecondConsultantOfL1 = null;
		sponsorList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FROM_EMAIL_ADDRESS,secondConsultantEmailIDOfL1),RFO_DB);
		sponsorIDOfSecondConsultantOfL1 = String.valueOf(getValueFromQueryResult(sponsorList, "AccountNumber"));

		driver.get(driver.getURL()+"/"+country);
		String SINForSecondConsultantOfL2 = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstNameForSecondConsultantOfL2 = TestConstants.FIRST_NAME_FOR_SECOND_CONSULTANT_OF_L2;
		String lastNameForSecondConsultantOfL2 = TestConstants.LAST_NAME_FOR_SECOND_CONSULTANT_OF_L2;
		String emailIdForSecondConsultantOfL2 = TestConstants.FIRST_NAME_FOR_SECOND_CONSULTANT_OF_L2+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		String secondConsultantEmailIDOfL2 = storeFrontHomePage.createConsultant(sponsorIDOfSecondConsultantOfL1, kitName, regimenName, enrollmentType, firstNameForSecondConsultantOfL2, lastNameForSecondConsultantOfL2, emailIdForSecondConsultantOfL2, password, addressLine1, city, postalCode, phoneNumber, cardNumber,nameOnCard, SINForSecondConsultantOfL2, productQuantity );
		logger.info("Second consultant email of L2: "+secondConsultantEmailIDOfL2);
		System.out.println("Second consultant email of L2: "+secondConsultantEmailIDOfL2);
		Assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible for second consultant of L1");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		logout();

		/**
		 * LEVEL 2 Jerry Ranft consultant enrollment under Guido Quaroni 
		 */

		//get Account number of third consultant of L1
		String sponsorIDOfThirdConsultantOfL1 = null;
		sponsorList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FROM_EMAIL_ADDRESS,thirdConsultantEmailIDOfL1),RFO_DB);
		sponsorIDOfThirdConsultantOfL1 = String.valueOf(getValueFromQueryResult(sponsorList, "AccountNumber"));

		driver.get(driver.getURL()+"/"+country);
		String SINForThirdConsultantOfL2 = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstNameForThirdConsultantOfL2 = TestConstants.FIRST_NAME_FOR_THIRD_CONSULTANT_OF_L2;
		String lastNameForThirdConsultantOfL2 = TestConstants.LAST_NAME_FOR_THIRD_CONSULTANT_OF_L2;
		String emailIdForThirdConsultantOfL2 = TestConstants.FIRST_NAME_FOR_THIRD_CONSULTANT_OF_L2+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		String thirdConsultantEmailIDOfL2 = storeFrontHomePage.createConsultant(sponsorIDOfThirdConsultantOfL1, kitName, regimenName, enrollmentType, firstNameForThirdConsultantOfL2, lastNameForThirdConsultantOfL2, emailIdForThirdConsultantOfL2, password, addressLine1, city, postalCode, phoneNumber, cardNumber,nameOnCard, SINForThirdConsultantOfL2, productQuantity );
		logger.info("Third consultant email of L2: "+thirdConsultantEmailIDOfL2);
		System.out.println("Third consultant email of L2: "+thirdConsultantEmailIDOfL2);
		Assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible for third consultant of L1");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		logout();

		/**
		 * LEVEL 2 Jay Limo consultant enrollment under Harv Clarkson 
		 */

		//get Account number of fourth consultant of L1
		String sponsorIDOfFourthConsultantOfL1 = null;
		sponsorList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FROM_EMAIL_ADDRESS,fourthConsultantEmailIDOfL1),RFO_DB);
		sponsorIDOfFourthConsultantOfL1 = String.valueOf(getValueFromQueryResult(sponsorList, "AccountNumber"));

		driver.get(driver.getURL()+"/"+country);
		String SINForFourthConsultantOfL2 = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstNameForFourthConsultantOfL2 = TestConstants.FIRST_NAME_FOR_FOURTH_CONSULTANT_OF_L2;
		String lastNameForFourthConsultantOfL2 = TestConstants.LAST_NAME_FOR_FOURTH_CONSULTANT_OF_L2;
		String emailIdForFourthConsultantOfL2 = TestConstants.FIRST_NAME_FOR_FOURTH_CONSULTANT_OF_L2+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		String fourthConsultantEmailIDOfL2 = storeFrontHomePage.createConsultant(sponsorIDOfFourthConsultantOfL1, kitName, regimenName, enrollmentType, firstNameForFourthConsultantOfL2, lastNameForFourthConsultantOfL2, emailIdForFourthConsultantOfL2, password, addressLine1, city, postalCode, phoneNumber, cardNumber,nameOnCard, SINForFourthConsultantOfL2, productQuantity );
		logger.info("Fourth consultant email of L2: "+fourthConsultantEmailIDOfL2);
		System.out.println("Fourth consultant email of L2: "+fourthConsultantEmailIDOfL2);
		Assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible for fourth consultant of L1");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		logout();

		/**
		 * LEVEL 2 Hamm Truck consultant enrollment under Sally Carrera 
		 */

		//get Account number of fifth consultant of L1
		String sponsorIDOfFifthConsultantOfL1 = null;
		sponsorList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FROM_EMAIL_ADDRESS,fifthConsultantEmailIDOfL1),RFO_DB);
		sponsorIDOfFifthConsultantOfL1 = String.valueOf(getValueFromQueryResult(sponsorList, "AccountNumber"));

		driver.get(driver.getURL()+"/"+country);
		String SINForFifthConsultantOfL2 = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstNameForFifthConsultantOfL2 = TestConstants.FIRST_NAME_FOR_FIFTH_CONSULTANT_OF_L2;
		String lastNameForFifthConsultantOfL2 = TestConstants.LAST_NAME_FOR_FIFTH_CONSULTANT_OF_L2;
		String emailIdForFifthConsultantOfL2 = TestConstants.FIRST_NAME_FOR_FIFTH_CONSULTANT_OF_L2+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		String fifthConsultantEmailIDOfL2 = storeFrontHomePage.createConsultant(sponsorIDOfFifthConsultantOfL1, kitName, regimenName, enrollmentType, firstNameForFifthConsultantOfL2, lastNameForFifthConsultantOfL2, emailIdForFifthConsultantOfL2, password, addressLine1, city, postalCode, phoneNumber, cardNumber,nameOnCard, SINForFifthConsultantOfL2, productQuantity );
		logger.info("Fifth consultant email of L2: "+fifthConsultantEmailIDOfL2);
		System.out.println("Fifth consultant email of L2: "+fifthConsultantEmailIDOfL2);
		Assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible for fifth consultant of L1");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		logout();

		/**
		 * LEVEL 2 Albert Hinkey consultant enrollment under Flo Lewis 
		 */

		//get Account number of sixth consultant of L1
		String sponsorIDOfSixthConsultantOfL1 = null;
		sponsorList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FROM_EMAIL_ADDRESS,sixthConsultantEmailIDOfL1),RFO_DB);
		sponsorIDOfSixthConsultantOfL1 = String.valueOf(getValueFromQueryResult(sponsorList, "AccountNumber"));

		driver.get(driver.getURL()+"/"+country);
		String SINForSixthConsultantOfL2 = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstNameForSixthConsultantOfL2 = TestConstants.FIRST_NAME_FOR_SIXTH_CONSULTANT_OF_L2;
		String lastNameForSixthConsultantOfL2 = TestConstants.LAST_NAME_FOR_SIXTH_CONSULTANT_OF_L2;
		String emailIdForSixthConsultantOfL2 = TestConstants.FIRST_NAME_FOR_SIXTH_CONSULTANT_OF_L2+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		String sixthConsultantEmailIDOfL2 = storeFrontHomePage.createConsultant(sponsorIDOfSixthConsultantOfL1, kitName, regimenName, enrollmentType, firstNameForSixthConsultantOfL2, lastNameForSixthConsultantOfL2, emailIdForSixthConsultantOfL2, password, addressLine1, city, postalCode, phoneNumber, cardNumber,nameOnCard, SINForSixthConsultantOfL2, productQuantity );
		logger.info("Sixth consultant email of L2: "+sixthConsultantEmailIDOfL2);
		System.out.println("Sixth consultant email of L2: "+sixthConsultantEmailIDOfL2);
		Assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible for sixth consultant of L1");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		logout();

		/**
		 * LEVEL 2 Mia Collins consultant enrollment under Fillmore Carlin 
		 */

		//get Account number of Seventh consultant of L1
		String sponsorIDOfSeventhConsultantOfL1 = null;
		sponsorList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FROM_EMAIL_ADDRESS,seventhConsultantEmailIDOfL1),RFO_DB);
		sponsorIDOfSeventhConsultantOfL1 = String.valueOf(getValueFromQueryResult(sponsorList, "AccountNumber"));

		driver.get(driver.getURL()+"/"+country);
		String SINForSeventhConsultantOfL2 = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstNameForSeventhConsultantOfL2 = TestConstants.FIRST_NAME_FOR_SEVENTH_CONSULTANT_OF_L2;
		String lastNameForSeventhConsultantOfL2 = TestConstants.LAST_NAME_FOR_SEVENTH_CONSULTANT_OF_L2;
		String emailIdForSeventhConsultantOfL2 = TestConstants.FIRST_NAME_FOR_SEVENTH_CONSULTANT_OF_L2+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		String seventhConsultantEmailIDOfL2 = storeFrontHomePage.createConsultant(sponsorIDOfSeventhConsultantOfL1, kitName, regimenName, enrollmentType, firstNameForSeventhConsultantOfL2, lastNameForSeventhConsultantOfL2, emailIdForSeventhConsultantOfL2, password, addressLine1, city, postalCode, phoneNumber, cardNumber,nameOnCard, SINForSeventhConsultantOfL2, productQuantity );
		logger.info("Seventh consultant email of L2: "+seventhConsultantEmailIDOfL2);
		System.out.println("Seventh consultant email of L2: "+seventhConsultantEmailIDOfL2);
		Assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible for seventh consultant of L1");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		logout();

		/**
		 * LEVEL 2 Elvis Crow consultant enrollment under Mack Crystal 
		 */
		//get Account number of Eighth consultant of L1
		String sponsorIDOfEighthConsultantOfL1 = null;
		sponsorList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FROM_EMAIL_ADDRESS,eighthConsultantEmailIDOfL1),RFO_DB);
		sponsorIDOfEighthConsultantOfL1 = String.valueOf(getValueFromQueryResult(sponsorList, "AccountNumber"));

		driver.get(driver.getURL()+"/"+country);
		String SINForEighthConsultantOfL2 = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstNameForEighthConsultantOfL2 = TestConstants.FIRST_NAME_FOR_EIGHTH_CONSULTANT_OF_L2;
		String lastNameForEighthConsultantOfL2 = TestConstants.LAST_NAME_FOR_EIGHTH_CONSULTANT_OF_L2;
		String emailIdForEighthConsultantOfL2 = TestConstants.FIRST_NAME_FOR_EIGHTH_CONSULTANT_OF_L2+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		String eighthConsultantEmailIDOfL2 = storeFrontHomePage.createConsultant(sponsorIDOfEighthConsultantOfL1, kitName, regimenName, enrollmentType, firstNameForEighthConsultantOfL2, lastNameForEighthConsultantOfL2, emailIdForEighthConsultantOfL2, password, addressLine1, city, postalCode, phoneNumber, cardNumber,nameOnCard, SINForEighthConsultantOfL2, productQuantity );
		logger.info("Eighth consultant email of L2: "+eighthConsultantEmailIDOfL2);
		System.out.println("Eighth consultant email of L2: "+eighthConsultantEmailIDOfL2);
		Assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible for eighth consultant of L1");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		logout();

		//-------------------------------------------------------------------LEVEL 2 COMPLETED---------------------------------------------------------------------------------------------------------------------------------------------------------------

		/**
		 * LEVEL 2 Tia Knight PC enrollment under Snotrod Romano 
		 */
		driver.get(driver.getURL()+"/"+country);
		String firstNameForFirstPCOfL3 = TestConstants.FIRST_NAME_FOR_FIRST_PC_OF_L3;
		String lastNameForFirstPCOfL3 = TestConstants.LAST_NAME_FOR_FIRST_PC_OF_L3;
		String emailIdForFirstPCOfL3 = TestConstants.FIRST_NAME_FOR_FIRST_PC_OF_L3+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		String firstPCEmailIDOfL3 = storeFrontHomePage.createPC(firstNameForFirstPCOfL3, lastNameForFirstPCOfL3, password, emailIdForFirstPCOfL3, sponsorIDOfFirstConsultantOfL1, newBillingProfileName);
		logger.info("First pc email of L1: "+firstPCEmailIDOfL3);
		System.out.println("First pc email of L1: "+firstPCEmailIDOfL3);
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		Assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownPresentForCheckUserRegistered(), "First PC of L1 is not registered successfully");
		driver.get(driver.getURL()+"/"+country);
		if(storeFrontHomePage.verifyWelcomeDropdownPresentForCheckUserRegistered() == true){
			logout();
		}


		/**
		 * LEVEL 2 Van Kind PC enrollment under Darell Waltrip 
		 */
		driver.get(driver.getURL()+"/"+country);
		String firstNameForSecondPCOfL3 = TestConstants.FIRST_NAME_FOR_SECOND_PC_OF_L3;
		String lastNameForSecondPCOfL3 = TestConstants.LAST_NAME_FOR_SECOND_PC_OF_L3;
		String emailIdForSecondPCOfL3 = TestConstants.FIRST_NAME_FOR_SECOND_PC_OF_L3+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		String secondPCEmailIDOfL3 = storeFrontHomePage.createPC(firstNameForSecondPCOfL3, lastNameForSecondPCOfL3, password, emailIdForSecondPCOfL3, sponsorIDOfSecondConsultantOfL1, newBillingProfileName);
		logger.info("Second pc email of L3: "+secondPCEmailIDOfL3);
		System.out.println("Second pc email of L3: "+secondPCEmailIDOfL3);
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		Assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownPresentForCheckUserRegistered(), "User NOT registered successfully");
		driver.get(driver.getURL()+"/"+country);
		if(storeFrontHomePage.verifyWelcomeDropdownPresentForCheckUserRegistered() == true){
			logout();
		}

		/**
		 * LEVEL 2 Fred Stanton PC enrollment under Jerry Ranft 
		 */
		driver.get(driver.getURL()+"/"+country);
		String firstNameForThirdPCOfL3 = TestConstants.FIRST_NAME_FOR_THIRD_PC_OF_L3;
		String lastNameForThirdPCOfL3 = TestConstants.LAST_NAME_FOR_THIRD_PC_OF_L3;
		String emailIdForThirdPCOfL3 = TestConstants.FIRST_NAME_FOR_THIRD_PC_OF_L3+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		String thirdPCEmailIDOfL3 = storeFrontHomePage.createPC(firstNameForThirdPCOfL3, lastNameForThirdPCOfL3, password, emailIdForThirdPCOfL3, sponsorIDOfThirdConsultantOfL1, newBillingProfileName);
		logger.info("Third pc email of L3: "+thirdPCEmailIDOfL3);
		System.out.println("Third pc email of L3: "+thirdPCEmailIDOfL3);
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		Assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownPresentForCheckUserRegistered(), "User NOT registered successfully");
		driver.get(driver.getURL()+"/"+country);
		if(storeFrontHomePage.verifyWelcomeDropdownPresentForCheckUserRegistered() == true){
			logout();
		}

		/**
		 * LEVEL 2 DJ Holowicki PC enrollment under Jay Limo 
		 */
		driver.get(driver.getURL()+"/"+country);
		String firstNameForFourthPCOfL3 = TestConstants.FIRST_NAME_FOR_FOURTH_PC_OF_L3;
		String lastNameForFourthPCOfL3 = TestConstants.LAST_NAME_FOR_FOURTH_PC_OF_L3;
		String emailIdForFourthPCOfL3 = TestConstants.FIRST_NAME_FOR_FOURTH_PC_OF_L3+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		String fourthPCEmailIDOfL3 = storeFrontHomePage.createPC(firstNameForFourthPCOfL3, lastNameForFourthPCOfL3, password, emailIdForFourthPCOfL3, sponsorIDOfFourthConsultantOfL1, newBillingProfileName);
		logger.info("Fourth pc email of L3: "+fourthPCEmailIDOfL3);
		System.out.println("Fourth pc email of L3: "+fourthPCEmailIDOfL3);
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		Assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownPresentForCheckUserRegistered(), "User NOT registered successfully");
		driver.get(driver.getURL()+"/"+country);
		if(storeFrontHomePage.verifyWelcomeDropdownPresentForCheckUserRegistered() == true){
			logout();
		}


		/**
		 * LEVEL 2 Master Guy PC enrollment under Hamm Truck 
		 */
		driver.get(driver.getURL()+"/"+country);
		String firstNameForFifthPCOfL3 = TestConstants.FIRST_NAME_FOR_FIFTH_PC_OF_L3;
		String lastNameForFifthPCOfL3 = TestConstants.LAST_NAME_FOR_FIFTH_PC_OF_L3;
		String emailIdForFifthPCOfL3 = TestConstants.FIRST_NAME_FOR_FIFTH_PC_OF_L3+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		String fifthPCEmailIDOfL3 = storeFrontHomePage.createPC(firstNameForFifthPCOfL3, lastNameForFifthPCOfL3, password, emailIdForFifthPCOfL3, sponsorIDOfFifthConsultantOfL1, newBillingProfileName);
		logger.info("Fifth pc email of L3: "+fifthPCEmailIDOfL3);
		System.out.println("Fifth pc email of L3: "+fifthPCEmailIDOfL3);
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		Assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownPresentForCheckUserRegistered(), "User NOT registered successfully");
		driver.get(driver.getURL()+"/"+country);
		if(storeFrontHomePage.verifyWelcomeDropdownPresentForCheckUserRegistered() == true){
			logout();
		}

		/**
		 * LEVEL 2 Doc Hudson PC enrollment under Albert Hinkey 
		 */
		driver.get(driver.getURL()+"/"+country);
		String firstNameForSixthPCOfL3 = TestConstants.FIRST_NAME_FOR_SIXTH_PC_OF_L3;
		String lastNameForSixthPCOfL3 = TestConstants.LAST_NAME_FOR_SIXTH_PC_OF_L3;
		String emailIdForSixthPCOfL3 = TestConstants.FIRST_NAME_FOR_SIXTH_PC_OF_L3+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		String sixthPCEmailIDOfL3 = storeFrontHomePage.createPC(firstNameForSixthPCOfL3, lastNameForSixthPCOfL3, password, emailIdForSixthPCOfL3, sponsorIDOfSixthConsultantOfL1, newBillingProfileName);
		logger.info("Sixth pc email of L3: "+sixthPCEmailIDOfL3);
		System.out.println("Sixth pc email of L3: "+sixthPCEmailIDOfL3);
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		Assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownPresentForCheckUserRegistered(), "User NOT registered successfully");
		driver.get(driver.getURL()+"/"+country);
		if(storeFrontHomePage.verifyWelcomeDropdownPresentForCheckUserRegistered() == true){
			logout();
		}

		/**
		 * LEVEL 2 James Goodwin PC enrollment under Mia Collins 
		 */
		driver.get(driver.getURL()+"/"+country);
		String firstNameForSeventhPCOfL3 = TestConstants.FIRST_NAME_FOR_SEVENTH_PC_OF_L3;
		String lastNameForSeventhPCOfL3 = TestConstants.LAST_NAME_FOR_SEVENTH_PC_OF_L3;
		String emailIdForSeventhPCOfL3 = TestConstants.FIRST_NAME_FOR_SEVENTH_PC_OF_L3+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		String seventhPCEmailIDOfL3 = storeFrontHomePage.createPC(firstNameForSeventhPCOfL3, lastNameForSeventhPCOfL3, password, emailIdForSeventhPCOfL3, sponsorIDOfSeventhConsultantOfL1, newBillingProfileName);
		logger.info("Seventh pc email of L3: "+seventhPCEmailIDOfL3);
		System.out.println("Seventh pc email of L3: "+seventhPCEmailIDOfL3);
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		Assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownPresentForCheckUserRegistered(), "User NOT registered successfully");
		driver.get(driver.getURL()+"/"+country);
		if(storeFrontHomePage.verifyWelcomeDropdownPresentForCheckUserRegistered() == true){
			logout();
		}

		/**
		 * LEVEL 2 Boost Rivera PC enrollment under Elvis Crow 
		 */
		driver.get(driver.getURL()+"/"+country);
		String firstNameForEighthPCOfL3 = TestConstants.FIRST_NAME_FOR_EIGHTH_PC_OF_L3;
		String lastNameForEighthPCOfL3 = TestConstants.LAST_NAME_FOR_EIGHTH_PC_OF_L3;
		String emailIdForEighthPCOfL3 = TestConstants.FIRST_NAME_FOR_EIGHTH_PC_OF_L3+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		String eighthPCEmailIDOfL3 = storeFrontHomePage.createPC(firstNameForEighthPCOfL3, lastNameForEighthPCOfL3, password, emailIdForEighthPCOfL3, sponsorIDOfEighthConsultantOfL1, newBillingProfileName);
		logger.info("Eighth pc email of L3: "+eighthPCEmailIDOfL3);
		System.out.println("Eighth pc email of L3: "+eighthPCEmailIDOfL3);
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		Assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownPresentForCheckUserRegistered(), "User NOT registered successfully");
		driver.get(driver.getURL()+"/"+country);
		if(storeFrontHomePage.verifyWelcomeDropdownPresentForCheckUserRegistered() == true){
			logout();
		}

		//-------------------------------------------------------------------LEVEL 3 COMPLETED---------------------------------------------------------------------------------------------------------------------------------------------------------------

	}


}
