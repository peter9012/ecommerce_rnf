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
	public static WebDriver driver;
	// Added for local testing and will be removed later
	public String defaultProps = "defaultenv.properties";

	protected PropertyFile propertyFile = new PropertyFile();
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
		System.out.println("Started execution with " + " " + envproperties);
		String pathOfAtuReports = System.getProperty("user.dir")+"\\ATU Reports";
		logger.debug("Started execution with " + " " + envproperties);
		if (!StringUtils.isEmpty(envproperties)) {
			propertyFile.loadProps(envproperties);
			logger.debug("Environment properties recieved and preparing the environment for "
					+ envproperties); 
		} else {
			propertyFile.loadProps(defaultProps);
			logger.info("Environment properties are not provided by the user ... loading the default properties");
			logger.info("Default Browser is  ------ "+propertyFile.getProperty("browser"));
			logger.info("Default URL is  ------ "+propertyFile.getProperty("baseUrl"));
			logger.info("Default user password is  ------ "+propertyFile.getProperty("password"));
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
		
		propertyFile.setProperty("atu.reports.dir",pathOfAtuReports);
		propertyFile.setProperty("atu.proj.header.logo","src/test/resources/staticdata/RodanAndFields.png");
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


}
