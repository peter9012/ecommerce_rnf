package com.rf.core.driver.mobile;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.rf.core.driver.RFDriver;
import com.rf.core.utils.DBUtil;
import com.rf.core.utils.PropertyFile;
import com.rf.core.website.constants.TestConstants;


/**
 * @author Shubham Mathur RFMobileDriver extends implements Webdriver and
 *         uses RemoteWebdriver for implementation, customized reusable
 *         functions are added along with regular functions
 */
public class RFMobileDriver implements RFDriver, WebDriver {
	public static AppiumDriver driver;
	//	public static WebDriver driver;
	private PropertyFile propertyFile;
	private static int DEFAULT_TIMEOUT = 30;
	private static int MIN_DEFAULT_TIMEOUT=5;
	private static String platformUsed;

	public RFMobileDriver(PropertyFile propertyFile) {
		super();
		this.propertyFile = propertyFile;
	}

	private static final Logger logger = LogManager
			.getLogger(RFMobileDriver.class.getName());
	static DesiredCapabilities capabilities = new DesiredCapabilities();

	/**
	 * @throws MalformedURLException
	 *             Prepares the environment that tests to be run on
	 */
	public void loadApplication() throws MalformedURLException {
		//capabilities.setCapability("device", propertyFile.getProperty("device"));
		capabilities.setCapability("platformName",
				propertyFile.getProperty("platformName"));
		capabilities.setCapability("platformVersion",
				propertyFile.getProperty("platformVersion"));
		capabilities.setCapability("deviceName",
				propertyFile.getProperty("deviceName"));
		capabilities.setCapability("browserName",
				propertyFile.getProperty("browserName"));
		capabilities.setCapability("autoAcceptAlerts", "true");
		//	capabilities.setCapability("newCommandTimeout", 600);
		URL remoteUrl = new URL("http://" + propertyFile.getProperty("host")
				+ "/wd/hub");
		logger.debug("Remote URL is " + remoteUrl);
		if (propertyFile.getProperty("platformName").contains("iOS"))
			driver = new IOSDriver(remoteUrl, capabilities);
		else
			driver = new AndroidDriver(remoteUrl, capabilities);			

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get(propertyFile.getProperty("baseUrl"));
		platformUsed = propertyFile.getProperty("platformName");
	}

	public void setDBConnectionString(){
		String dbIP = null;
		String dbUsername = null;
		String dbPassword = null;
		String dbDomain = null;

		String authentication = null;
		dbIP = propertyFile.getProperty("dbIP");
		dbUsername = propertyFile.getProperty("dbUsername");
		dbPassword = propertyFile.getProperty("dbPassword");
		dbDomain = propertyFile.getProperty("dbDomain");		 
		authentication = propertyFile.getProperty("authentication");
		DBUtil.setDBDetails(dbIP, dbUsername, dbPassword, dbDomain, authentication);
		logger.info("DB connections are set");
	}

	public void selectCountry(String country){
		driver.findElement(By.xpath("//div[@class='btn-group']")).click();
		if(country.equalsIgnoreCase("ca")){
			driver.findElement(By.xpath("//div[contains(@class,'btn-group')]//a[@class='dropdownCA']")).click();
		}
		else{
			driver.findElement(By.xpath("//div[contains(@class,'btn-group')]//a[@class='dropdownUS']")).click();
		}
		waitForPageLoad();
	}

	public String getURL() {
		return propertyFile.getProperty("baseUrl");
	}

	public String getDBNameRFL(){
		return propertyFile.getProperty("databaseNameRFL");
	}

	public String getDBNameRFO(){
		return propertyFile.getProperty("databaseNameRFO");
	}

	public String getCountry(){
		return propertyFile.getProperty("country");
	}

	public String getEnvironment(){
		return propertyFile.getProperty("environment");
	}

	public String getStoreFrontPassword(){
		return propertyFile.getProperty("storeFrontPassword");
	}

	public void tap(int x,WebElement element,int y){
		driver.tap(x, element, y);
	}


	/**
	 * @param locator
	 * @return
	 */
	public boolean isElementPresent(By locator) {
		if (driver.findElements(locator).size() > 0) {
			return true;
		} else
			return false;
	}

	public void waitForElementPresent(By locator) {
		logger.info("wait started for "+locator);
		int timeout = 10;
		turnOffImplicitWaits();
		boolean isElementFound = false;
		for(int i=1;i<=timeout;i++){		
			try{
				if(driver.findElements(locator).size()==0){
					pauseExecutionFor(1000);
					logger.info("waiting...");
					continue;
				}else{
					logger.info("wait over,element found");
					isElementFound =true;
					turnOnImplicitWaits();
					pauseExecutionFor(1000);
					break;
				}			
			}catch(Exception e){
				continue;
			}
		}
		if(isElementFound ==false)
			logger.info("ELEMENT NOT FOUND");		
	}

	public void quickWaitForElementPresent(By locator){
		logger.info("quick wait started for "+locator);
		int timeout = 2;
		turnOffImplicitWaits();
		for(int i=1;i<=timeout;i++){
			try{
				if(driver.findElements(locator).size()==0){
					pauseExecutionFor(1000);
					logger.info("waiting...");
					continue;
				}else{
					logger.info("wait over,element found");
					turnOnImplicitWaits();
					break;
				}			
			}catch(Exception e){
				continue;
			}
		}
	}

	public void waitForLoadingImageToDisappear(){
		turnOffImplicitWaits();
		By locator = By.xpath("//div[@id='blockUIBody']");
		logger.info("Waiting for loading image to get disappear");
		for(int i=1;i<=DEFAULT_TIMEOUT;i++){			
			try{
				if(driver.findElements(locator).size()==1){
					pauseExecutionFor(1000);
					logger.info("waiting..");
					continue;
				}else{
					turnOnImplicitWaits();
					logger.info("loading image disappears");
					break;
				}			
			}catch(Exception e){
				continue;
			}
		}

	}

	public void waitForLoadingImageToAppear(){
		turnOffImplicitWaits();
		By locator = By.xpath("//div[@id='blockUIBody']");
		int timeout = 3;

		for(int i=1;i<=timeout;i++){			
			try{
				if(driver.findElements(locator).size()==0){
					pauseExecutionFor(1000);

					continue;
				}else{
					turnOnImplicitWaits();
					break;
				}			
			}catch(Exception e){
				continue;
			}
		}

	}

	public void waitForSpinImageToDisappear(){
		turnOffImplicitWaits();
		By locator = By.xpath("//span[@id='email-ajax-spinner'][contains(@style,'display: inline;')]");
		logger.info("Waiting for spin image to get disappear");
		for(int i=1;i<=DEFAULT_TIMEOUT;i++){
			try{
				if(driver.findElements(locator).size()==1){
					pauseExecutionFor(1000);
					logger.info("waiting..");
					continue;
				}else {
					logger.info("spin image disappears");
					turnOnImplicitWaits();
					break;
				}			
			}catch(Exception e){
				continue;
			}
		}
	}


	/**
	 * @param locator
	 *            Waits
	 */
	public void moveToELement(By locator) {
		Actions build = new Actions(driver);
		build.moveToElement(driver.findElement(locator));
	}

	public void get(String Url) {
		driver.get(Url);
	}

	public void click(By locator) {
		waitForElementToBeVisible(locator);
		findElement(locator).click();
	}

	public void type(By locator, String input) {
		findElement(locator).sendKeys(input);
	}


	public void type(By locator, Keys input) {
		findElement(locator).sendKeys(input);
	}


	public void quit() {
		driver.quit();
	}

	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	public String getTitle() {
		return driver.getTitle();
	}

	public List<WebElement> findElements(By by) {
		//		if(getPlatformUsed().equalsIgnoreCase(TestConstants.IOS))
		//		movetToElementJavascript(by);
		return driver.findElements(by);
	}

	public WebElement findElement(By by) {
		if(getPlatformUsed().equalsIgnoreCase(TestConstants.IOS))
			movetToElementJavascript(by);
		return driver.findElement(by);
	}

	public WebElement findElementWithoutMove(By by) {
		// movetToElementJavascript(by);
		return driver.findElement(by);
	}

	public String getPageSource() {
		return driver.getPageSource();
	}

	public void close() {
		driver.close();
	}

	public Set<String> getWindowHandles() {
		return driver.getWindowHandles();
	}

	public String getWindowHandle() {
		return driver.getWindowHandle();
	}

	public TargetLocator switchTo() {
		return driver.switchTo();
	}

	public Navigation navigate() {
		return driver.navigate();

	}

	public Options manage() {
		return driver.manage();
	}

	public Select getSelect(By by) {
		return new Select(findElement(by));
	}

	public void scrollToBottomJS() throws InterruptedException {
		((JavascriptExecutor) driver)
		.executeScript("window.scrollTo(0,Math.max(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight));");
	}

	public void clear(By by) {
		findElement(by).clear();
	}

	public void movetToElementJavascript(By locator) {
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].scrollIntoView(true);",
				driver.findElement(locator));
	}

	public String getAttribute(By by, String name) {
		return findElement(by).getAttribute(name);
	}

	/**
	 * 
	 * Purpose:This Method return text value of Element.
	 * 
	 * @author: Amitabh Rai
	 * @date:18-Feb-2015
	 * @returnType: String
	 * @param by
	 * @return
	 */
	public String getText(By by) {
		return findElement(by).getText().trim();
	}

	public void fnDragAndDrop(By by) throws InterruptedException {
		WebElement Slider = driver.findElement(by);
		Actions moveSlider = new Actions(driver);
		moveSlider.clickAndHold(Slider);
		moveSlider.dragAndDropBy(Slider, 150, 0).build().perform();
	}

	public Actions action() {
		return new Actions(driver);
	}

	public void pauseExecutionFor(long lTimeInMilliSeconds) {
		try {
			Thread.sleep(lTimeInMilliSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if element is visible Purpose:
	 * 
	 * @author: Sunny Sachdeva
	 * @date:16-Feb-2015
	 * @returnType: WebElement
	 */
	public boolean IsElementVisible(WebElement element) {
		return element.isDisplayed() ? true : false;
	}

	/**
	 * 
	 * Purpose:Waits for element to be visible
	 * 
	 * @author: Sunny Sachdeva
	 * @date:16-Feb-2015
	 * @returnType: WebElement
	 */
	public boolean waitForElementToBeVisible(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		WebElement element = wait.until(ExpectedConditions
				.visibilityOfElementLocated(locator));
		return IsElementVisible(element);
	}

	/**
	 * 
	 * Purpose: Waits and matches the exact title
	 * 
	 * @author: Sunny Sachdeva
	 * @date:17-Feb-2015
	 * @returnType: boolean
	 */
	public boolean IsTitleEqual(By locator, int timeOut, String title) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		return wait.until(ExpectedConditions.titleIs(title));
	}

	/**
	 * Waits and matches if title contains the text Purpose:
	 * 
	 * @author: Sunny Sachdeva
	 * @date:17-Feb-2015
	 * @returnType: boolean
	 */
	public boolean IsTitleContains(By locator, int timeOut, String title) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		return wait.until(ExpectedConditions.titleContains(title));
	}

	/**
	 * Wait for element to be clickable Purpose:
	 * 
	 * @author: Sunny Sachdeva
	 * @date:16-Feb-2015
	 * @returnType: WebElement
	 */
	public WebElement waitForElementToBeClickable(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		WebElement element = wait.until(ExpectedConditions
				.elementToBeClickable(locator));
		return element;
	}

	/**
	 * 
	 * Purpose:Wait for element to disappear
	 * 
	 * @author: Sunny Sachdeva
	 * @date:16-Feb-2015
	 * @returnType: boolean
	 */
	public boolean waitForElementToBeInVisible(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		return wait.until(ExpectedConditions
				.invisibilityOfElementLocated(locator));
	}

	public boolean waitForPageLoad() {
		boolean isLoaded = false;
		try {
			logger.debug("Waiting For Page load via JS");
			ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return ((JavascriptExecutor) driver).executeScript(
							"return document.readyState").equals("complete");
				}
			};
			WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
			wait.until(pageLoadCondition);
			isLoaded = true;
		} catch (Exception e) {
			logger.error("Error Occured waiting for Page Load "
					+ driver.getCurrentUrl());
		}
		return isLoaded;
	}

	public void selectFromList(List<WebElement> lstElementList,
			String sValueToBeSelected) throws Exception {
		logger.debug("START OF FUNCTION->selectFromList");
		boolean flag = false;
		logger.debug("Total element found --> " + lstElementList.size());
		logger.debug("Value to be selected " + sValueToBeSelected + " from list"
				+ lstElementList);
		for (WebElement e : lstElementList) {
			logger.debug(">>>>>>>>>>>>>" + e.getText() + "<<<<<<<<<<<<<<<");
			if (e.getText().equalsIgnoreCase(sValueToBeSelected)) {
				logger.debug("Value matched in list as->" + e.getText());
				e.click();

				flag = true;
				break;
			}
		}
		if (flag == false) {
			throw new Exception("No match found in the list for value->"
					+ sValueToBeSelected);
		}
		logger.debug("END OF FUNCTION->selectFromList");
	}

	public WebElement waitForElementToBeClickable(WebElement element,
			int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		return element;
	}

	public WebElement waitForElementToBeClickable(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		return element;
	}

	/**
	 * 
	 * Purpose: Helps in case of Stalement Exception
	 * 
	 * @author: Sunny Sachdeva
	 * @date:02-Mar-2015
	 * @returnType: boolean
	 */
	public boolean retryingFindClick(By by) {
		boolean result = false;
		int attempts = 0;
		while (attempts < 3) {
			try {
				driver.findElement(by).click();
				result = true;
				break;
			} catch (StaleElementReferenceException e) {
			}
			attempts++;
		}
		return result;
	}

	public boolean deleteAllCookies(WebDriver driver) {
		boolean IsDeleted = false;
		try {
			driver.manage().deleteAllCookies();
			IsDeleted = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return IsDeleted;
	}

	public static void scrollToElement(WebDriver driver, WebElement element) {
		logger.debug("Scrolling to Element");
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].scrollIntoView(true);", element);
	}

	public void turnOffImplicitWaits() {
		driver.manage().timeouts().implicitlyWait(MIN_DEFAULT_TIMEOUT, TimeUnit.SECONDS);
	}

	public void turnOnImplicitWaits() {
		driver.manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
	}

	public void clickByJS(By by) {
		WebElement element = driver.findElement(by);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
	}

	public void clickByJS(WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
	}

	public void clickByJS(WebDriver driver, WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
	}

	public void switchToSecondWindow(){
		//		Set<String> allWindows = driver.getWindowHandles();
		//		Iterator itr = allWindows.iterator();
		//		while(itr.hasNext()){
		//			driver.switchTo().window((String) itr.next());
		//			break;
		//		}

		Set<String> allWindows = driver.getWindowHandles();
		logger.info("total windows opened = "+allWindows.size());
		String parentWindow = driver.getWindowHandle();
		for(String allWin : allWindows){
			if(!allWin.equalsIgnoreCase(parentWindow)){
				driver.switchTo().window(allWin);
				break;
			}

		}
		logger.info("Switched to second window whose title is "+driver.getTitle());		
	}


	public static String takeSnapShotAndRetPath(WebDriver driver,String methodName) throws Exception {

		String FullSnapShotFilePath = "";

		try {			
			File scrFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			String sFilename = null;
			sFilename = "Screenshot-" +methodName+getDateTime() + ".png";
			FullSnapShotFilePath = System.getProperty("user.dir")
					+ "\\output\\ScreenShots\\" + sFilename;
			FileUtils.copyFile(scrFile, new File(FullSnapShotFilePath));
		} catch (Exception e) {

		}

		return FullSnapShotFilePath;
	}

	public static String takeSnapShotAndRetPath(WebDriver driver) throws Exception {

		String FullSnapShotFilePath = "";
		try {
			logger.info("Taking Screenshot");
			File scrFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			String sFilename = null;
			sFilename = "verificationFailure_Screenshot.png";
			FullSnapShotFilePath = System.getProperty("user.dir")
					+ "\\output\\ScreenShots\\" + sFilename;
			FileUtils.copyFile(scrFile, new File(FullSnapShotFilePath));
		} catch (Exception e) {

		}

		return FullSnapShotFilePath;
	}

	/**
	 * Returns current Date Time
	 * 
	 * @return
	 */
	//	public void swipe(int startx, int starty, int endx, int endy, int duration) {
	//		driver.swipe(startx, starty, endx, endy, duration);
	//	}

	/**
	 * Returns current Date Time
	 * 
	 * @return
	 */
	public static String getDateTime() {
		String sDateTime = "";
		try {
			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
			Date now = new Date();
			String strDate = sdfDate.format(now);
			String strTime = sdfTime.format(now);
			strTime = strTime.replace(":", "-");
			sDateTime = "D" + strDate + "_T" + strTime;
		} catch (Exception e) {
			System.err.println(e);
		}
		return sDateTime;
	}

	/**
	 * 
	 * Purpose: Verified if click operation is successfully by verifying after click Locator is present
	 * @author: Sunny Sachdeva 
	 * @date:10-Mar-2015
	 * @returnType: void
	 */

	public void verifyClick(By clickBy, By afterClickBy){
		int iAttempts=8;
		int iCount=0;
		do{
			click(clickBy);
			pauseExecutionFor(2000);
			iCount++;
		}
		while(iCount < iAttempts && (!isElementPresent(afterClickBy)) );
	}

	public void clickElement(WebElement element){
		waitForElementToBeClickable(element, DEFAULT_TIMEOUT).click();
		waitForPageLoad();
	}

	public boolean waitForElementToBeVisible(By locator) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
		WebElement element = wait.until(ExpectedConditions
				.visibilityOfElementLocated(locator));
		return IsElementVisible(element);
	}

	public void navigateBack(){
		driver.navigate().back();
		waitForPageLoad();
	}

	public boolean quickCheckForElementPresent(By locator) {
		turnOffImplicitWaits();
		boolean IsElementPresent=false;
		if (driver.findElements(locator).size() > 0) {
			IsElementPresent=true;
		} 
		turnOnImplicitWaits();
		return IsElementPresent;
	}

	public List<WebElement> quickFindElements(By by) {
		turnOffImplicitWaits();
		List<WebElement> list=driver.findElements(by);
		turnOnImplicitWaits();
		return list;
	}

	public boolean waitForElementToBeVisible(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
		wait.until(ExpectedConditions
				.visibilityOf(element));
		return IsElementVisible(element);
	}

	public void refreshBrowser(){
		driver.navigate().refresh();
		waitForPageLoad();
	}

	public boolean IsLocatorVisible(By by) {
		return findElement(by).isDisplayed() ? true : false;
	}

	public void scrollToTopJS() throws InterruptedException {
		((JavascriptExecutor) driver)
		.executeScript("window.scrollTo(0,0);");
	}

	//	public void takeScreenShot() throws Exception{
	//		WebDriver driver1 = new Augmenter().augment(driver);
	//		File file  = ((TakesScreenshot)driver1).getScreenshotAs(OutputType.FILE);
	//		FileUtils.copyFile(file, new File("Screenshot.jpg"));
	//	}

	public boolean isElementClickable(WebElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, MIN_DEFAULT_TIMEOUT);
			wait.until(ExpectedConditions.elementToBeClickable(element));
			return false;
		} catch(Exception e) {
			return false;
		}
	}

	public String getPlatformUsed() {
		return platformUsed;
	}

}
