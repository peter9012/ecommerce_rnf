package com.rf.test.base;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
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

@Listeners({ com.rf.core.listeners.TestListner.class })
public class RFBaseTest{
	public static WebDriver driver;
	// Added for local testing and will be removed later
	public String defaultProps = "defaultenv.properties";

	protected PropertyFile propertyFile = new PropertyFile();
	private static final Logger logger = LogManager
			.getLogger(RFBaseTest.class.getName());
	protected SoftAssert s_assert = new SoftAssert();

	/**
	 * @param envproperties
	 *            envproperties is the file name that is given to pom.xml that
	 *            contains the environment details that to be loaded
	 */
	@BeforeSuite(alwaysRun = true)
	@Parameters({"envproperties"})
	public void beforeSuite(@Optional String envproperties) {
		System.out.println("Started execution with " + " " + envproperties);
		logger.debug("Started execution with " + " " + envproperties);
		if (!StringUtils.isEmpty(envproperties)) {
			propertyFile.loadProps(envproperties);
			logger.debug("Environment properties recieved and preparing the environment for "
					+ envproperties); 
			logger.info("EXECUTION ENVIRONMENT ------ "+propertyFile.getProperty("browser"));

		} else {
			propertyFile.loadProps(defaultProps);
			logger.info("Environment properties are not provided by the user ... loading the default properties");
			logger.info("Default Browser is  ------ "+propertyFile.getProperty("browser"));
			logger.info("Default URL is  ------ "+propertyFile.getProperty("baseUrl"));
			logger.info("Default Country is  ------ "+propertyFile.getProperty("country"));
			logger.info("Default DB IP is  ------ "+propertyFile.getProperty("dbIP"));
			logger.info("Default DB Username is  ------ "+propertyFile.getProperty("dbUsername"));
			logger.info("Default DB Password is  ------ "+propertyFile.getProperty("dbPassword"));
			logger.info("Default DB Domain is  ------ "+propertyFile.getProperty("dbDomain"));						
		}
		// clear screenshots folder
		try {
			FileUtils.cleanDirectory(new File(System.getProperty("user.dir")
					+ "\\output\\ScreenShots"));
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
