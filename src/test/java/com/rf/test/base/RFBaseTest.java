package com.rf.test.base;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.rf.core.utils.ExcelReader;
import com.rf.core.utils.PropertyFile;
import com.rf.core.utils.SoftAssert;
import com.rf.core.website.constants.TestConstants;

/**
 * @author Shubham Mathur This class is the base for all the environments
 *         likeMobile, Desktop etc
 */

@Listeners({ com.rf.core.listeners.TestListner.class})
public class RFBaseTest{
	//public static WebDriver driver;
	public String defaultProps = "defaultenv.properties";
	public String testUsers_QA2_US_propertes = "testUsers_QA2_US.properties";
	public String testUsers_QA2_CA_propertes = "testUsers_QA2_CA.properties";
	public String testUsers_QA2_AU_propertes = "testUsers_QA2_AU.properties";
	public String testUsers_QA1_US_propertes = "testUsers_QA1_US.properties";
	public String testUsers_PROD_US_propertes = "testUsers_PROD_US.properties";
	public String testUsers_PPD_US_propertes = "testUsers_PPD_US.properties";
	public String testUsers_PPD_CA_propertes = "testUsers_PPD_CA.properties";
	public String testUsers_DEV1_US_propertes = "testUsers_DEV1_US.properties";
	public String testUsers_DEV1_CA_propertes = "testUsers_DEV1_CA.properties";
	public static String userProps = null;

	protected static PropertyFile propertyFile = new PropertyFile();
	protected static PropertyFile userPropertyFile = new PropertyFile();

	private static final Logger logger = LogManager
			.getLogger(RFBaseTest.class.getName());
	protected SoftAssert s_assert;

	/**
	 * @param envproperties
	 *            envproperties is the file name that is given to pom.xml that
	 *            contains the environment details that to be loaded
	 */
	@BeforeSuite(alwaysRun = true)
	@Parameters({"envproperties"})
	public void beforeSuite(@Optional String envproperties) {
		logger.debug("Started execution with " + " " + envproperties);
		if (!StringUtils.isEmpty(envproperties)) {
			System.out.println("Started execution with " + " " + envproperties);
			propertyFile.loadProps(envproperties);
			logger.debug("Environment properties recieved and preparing the environment for "
					+ envproperties); 
			setUserPropertyFile(envproperties);
		} else {
			System.out.println("Started execution with " + " " + defaultProps);
			propertyFile.loadProps(defaultProps);
			setUserPropertyFile(propertyFile.getProperty("environment")+propertyFile.getProperty("country"));			
			logger.info("Environment properties are not provided by the user ... loading the default properties");
			logger.info("Default Browser is  ------ "+propertyFile.getProperty("browser"));
			logger.info("Default URL is  ------ "+propertyFile.getProperty("baseUrl"));
			logger.info("Default user password is  ------ "+propertyFile.getProperty("storeFrontPassword"));
			logger.info("Default Country is  ------ "+propertyFile.getProperty("country"));
			logger.info("Default DB IP is  ------ "+propertyFile.getProperty("dbIP"));
			logger.info("Default DB Username is  ------ "+propertyFile.getProperty("dbUsername"));
			logger.info("Default DB Password is  ------ "+propertyFile.getProperty("dbPassword"));
			logger.info("Default DB Domain is  ------ "+propertyFile.getProperty("dbDomain"));
		}
		// clear screenshots folder
		try {
			File fDir = new File(System.getProperty("user.dir")
					+ "\\output\\ScreenShots");
			if (!fDir.exists()) {
				fDir.mkdirs();
			}
			FileUtils.cleanDirectory(new File(System.getProperty("user.dir")
					+ "\\output\\ScreenShots"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File fDir2 = new File(System.getProperty("user.dir")
				+ "\\test-output");
		if (!fDir2.exists()) {
			fDir2.mkdirs();
		}

		File logDir = new File(System.getProperty("user.dir")
				+ "\\logs");
		if (!logDir.exists()) {
			logDir.mkdirs();
		}

	}

	@AfterSuite(alwaysRun=true)
	public void afterSuite(){
		//create a time stamp to be added to new logs,output and test-output folders
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
		String timeStamp = String.valueOf(dateFormat.format(date));

		// set the location of the source directories of logs,output and test-output folder
		String srcLogsDirectory = System.getProperty("user.dir")+"\\logs";
		String srcOutputDirectory = System.getProperty("user.dir")+"\\output";
		String srcTestOutputDirectory = System.getProperty("user.dir")+"\\test-output";

		// set the location of the destination directories of logs,output and test-output folder under buildHistory folder
		String destinationLogsDirectory = System.getProperty("user.dir")+"\\buildHistory\\logs\\logs-"+timeStamp;
		String destinationOutputDirectory = System.getProperty("user.dir")+"\\buildHistory\\output\\output-"+timeStamp;
		String destinationTestOutputDirectory = System.getProperty("user.dir")+"\\buildHistory\\test-output\\test-output-"+timeStamp;

		// create new folders for logs,output and test-output directories
		try {
			FileUtils.forceMkdir(new File(destinationLogsDirectory));
			FileUtils.forceMkdir(new File(destinationOutputDirectory));
			FileUtils.forceMkdir(new File(destinationTestOutputDirectory));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// copy the latest data to logs,output and test-output directories
		try {
			FileUtils.copyDirectory(new File(srcLogsDirectory), new File(destinationLogsDirectory));
			FileUtils.copyDirectory(new File(srcOutputDirectory), new File(destinationOutputDirectory));
			FileUtils.copyDirectory(new File(srcTestOutputDirectory), new File(destinationTestOutputDirectory));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @throws Exception
	 */
	@DataProvider(name = "rfTestData")
	public Object[][] rfDataProvider(Method testMethod) throws Exception {
		String sheetName = testMethod.getName();
		String filePath = "src/test/resources/"
				+ testMethod
				.getDeclaringClass()
				.getName()
				.replace(TestConstants.DOT, TestConstants.FORWARD_SLASH)
				+ ".xlsx";
		System.out.println("Test data is loaded from file " + filePath
				+ " and the sheet is " + sheetName);
		Object[][] testObjArray = ExcelReader.getTableArray(filePath,
				testMethod.getName());
		return (testObjArray);

	}

	public SoftAssert getSoftAssert() {
		return s_assert;
	}

	// load env properties
	public void setUserPropertyFile(String propertyFileName){
		if(propertyFileName.toLowerCase().contains("qa2") && propertyFileName.toLowerCase().contains("us")){
			userProps=testUsers_QA2_US_propertes;		  
		}else  if(propertyFileName.toLowerCase().contains("dev1") && propertyFileName.toLowerCase().contains("us")){
			userProps=testUsers_DEV1_US_propertes;
		}
		else  if(propertyFileName.toLowerCase().contains("dev1") && propertyFileName.toLowerCase().contains("ca")){
			userProps=testUsers_DEV1_CA_propertes;
		}
		else  if(propertyFileName.toLowerCase().contains("qa1") && propertyFileName.toLowerCase().contains("us")){
			userProps=testUsers_QA1_US_propertes;
		}
		else  if(propertyFileName.toLowerCase().contains("qa2") && propertyFileName.toLowerCase().contains("ca")){
			userProps=testUsers_QA2_CA_propertes;
		}
		else  if(propertyFileName.toLowerCase().contains("prod") && propertyFileName.toLowerCase().contains("us")){
			userProps=testUsers_PROD_US_propertes;
		}
		else  if(propertyFileName.toLowerCase().contains("ppd") && propertyFileName.toLowerCase().contains("us")){
			userProps=testUsers_PPD_US_propertes;
		}
		else  if(propertyFileName.toLowerCase().contains("ppd") && propertyFileName.toLowerCase().contains("ca")){
			userProps=testUsers_PPD_CA_propertes;
		}
		else  if(propertyFileName.toLowerCase().contains("qa2") && propertyFileName.toLowerCase().contains("au")){
			userProps=testUsers_PPD_CA_propertes;
		}
		logger.info("testUsers are loaded from "+userProps);
	}
}
