package com.rf.core.driver.website;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;

import org.openqa.selenium.Dimension;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.rf.core.driver.RFDriver;
import com.rf.core.utils.DBUtil;
import com.rf.core.utils.PropertyFile;

/**
 * @author ShubhamMathur RFWebsiteDriver extends implements Webdriver and
 *         uses Firefox/Internet/Chrome for implementation, customized reusable
 *         functions are added along with regular functions
 */
public class RFWebsiteDriver implements RFDriver,WebDriver {
	public static WebDriver driver; // added static and changed visibility from public to private
	private PropertyFile propertyFile;

	private static int DEFAULT_TIMEOUT = 15;

	private static int DEFAULT_TIMEOUT_CSCOCKPIT = 70;
	String browser = null;
	String dbIP = null;
	String baseUrl  =null;
	String country = null;
	String environment = null;
	String storeFrontUserPassword = null;

	public RFWebsiteDriver(PropertyFile propertyFile) {
		//super();
		this.propertyFile = propertyFile;			
	}

	private static final Logger logger = LogManager
			.getLogger(RFWebsiteDriver.class.getName());

	/**
	 * @throws MalformedURLException
	 *             Prepares the environment that tests to be run on
	 */
	public void loadApplication() throws MalformedURLException {
		browser=System.getProperty("browser");
		if(StringUtils.isEmpty(browser)){
			browser = propertyFile.getProperty("browser");
		}
		FirefoxProfile prof = new FirefoxProfile();
		prof.setPreference("brower.startup.homepage", "about:blank");
		prof.setPreference("startup.homepage_welcome_url", "about:blank");
		prof.setPreference("startup.homepage_welcome_url.additional",  "about:blank");
		if (browser.equalsIgnoreCase("firefox"))
			driver = new FirefoxDriver(prof);
		else if (browser.equalsIgnoreCase("chrome")){
			System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("no-sandbox");
			options.addArguments("chrome.switches","--disable-extensions");
			options.addArguments("disable-popup-blocking");
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			// for clearing cache
			capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
			driver = new ChromeDriver(capabilities);
		}
		else if(browser.equalsIgnoreCase("headless")){
			driver = new HtmlUnitDriver();		

		}
		else if(browser.equalsIgnoreCase("ie")){
			System.setProperty("webdriver.ie.driver", "src/test/resources/IEDriverServer.exe");
			DesiredCapabilities capabilities = new DesiredCapabilities();
			// for clearing cache
			capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
			driver = new InternetExplorerDriver(capabilities);
		}
		else if(propertyFile.getProperty("browser").equalsIgnoreCase("safari"))
		{ 
			System.setProperty("webdriver.safari.noinstall", "false");
			DesiredCapabilities capabilities = new DesiredCapabilities();
			// for clearing cache
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
			capabilities.setCapability(CapabilityType.HAS_NATIVE_EVENTS, true);
			capabilities.setCapability(CapabilityType.ENABLE_PERSISTENT_HOVERING, true);
			capabilities.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
			capabilities.setCapability(CapabilityType.SUPPORTS_FINDING_BY_CSS, true);
			capabilities.setCapability(CapabilityType.SUPPORTS_ALERTS, true);
			driver = new SafariDriver(capabilities);
			pauseExecutionFor(2000);
		}
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		if (browser.equalsIgnoreCase("firefox")){
			System.out.println(driver.manage().window().getSize());
			Dimension d = new Dimension(1936, 1056);
			driver.manage().window().setSize(d);
			System.out.println("Dimension reset to larger");
			System.out.println(driver.manage().window().getSize());	
		}		
		logger.info("Window is maximized");
		// for clearing cookies
		driver.manage().deleteAllCookies();
		logger.info("All cookies deleted");
		//driver.get(propertyFile.getProperty("baseUrl"));
	}

	public void setDBConnectionString(){
		String dbUsername = null;
		String dbPassword = null;
		String dbDomain = null;

		dbIP=System.getProperty("dbIP");
		if(StringUtils.isEmpty(dbIP)){
			dbIP = propertyFile.getProperty("dbIP");
		}
		String authentication = null;
		dbUsername = propertyFile.getProperty("dbUsername");
		dbPassword = propertyFile.getProperty("dbPassword");
		dbDomain = propertyFile.getProperty("dbDomain");		 
		authentication = propertyFile.getProperty("authentication");
		DBUtil.setDBDetails(dbIP, dbUsername, dbPassword, dbDomain, authentication);
		logger.info("DB connections are set");
	}

	public String getURL() {
		baseUrl=System.getProperty("baseUrl");
		if(StringUtils.isEmpty(baseUrl)){
			baseUrl = propertyFile.getProperty("baseUrl");
		}
		return baseUrl;
	}

	public String getBrowser(){
		return browser;
	}

	public String getStoreFrontUserPassword(){
		storeFrontUserPassword=propertyFile.getProperty("storeFrontPassword");
		return storeFrontUserPassword;
	}

	public String getCountry(){
		country=System.getProperty("country");
		if(StringUtils.isEmpty(country)){
			country = propertyFile.getProperty("country");
		}
		return country;
	}

	public String getEnvironment(){
		environment=System.getProperty("env");
		if(StringUtils.isEmpty(environment)){
			environment = propertyFile.getProperty("environment");			
		}
		return environment;
	}

	/**
	 * @param locator
	 * @return
	 */
	public boolean isElementPresent(By locator) {
		turnOffImplicitWaits(2);
		try{
			if (driver.findElements(locator).size() > 0) {
				return true;
			} else{
				turnOnImplicitWaits();
				return false;

			}
		}
		catch(Exception e){
			turnOnImplicitWaits();
			return false;
		}
	}


	public void waitForElementPresent(By locator) {
		logger.info("wait started for "+locator);
		int timeout = 10;
		turnOffImplicitWaits(1);
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
					//pauseExecutionFor(1000);
					break;
				}			
			}catch(Exception e){
				continue;
			}
		}
		turnOnImplicitWaits();
		if(isElementFound ==false)
			logger.info("ELEMENT NOT FOUND");		
	}

	public void waitForElementPresent(By locator, int timeout) {
		logger.info("wait started for "+locator);
		turnOffImplicitWaits(1);
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
					pauseExecutionFor(1000);
					break;
				}			
			}catch(Exception e){
				continue;
			}finally{
				turnOnImplicitWaits();
			}
		}
		if(isElementFound ==false)
			logger.info("ELEMENT NOT FOUND");		
	}
	
	public void waitForElementIsPresent(By locator, int timeout) {
		logger.info("wait started for "+locator);
		turnOffImplicitWaits(1);
		boolean isElementFound = false;
		for(int i=1;i<=timeout;i++){	
			Dimension d =driver.findElement(locator).getSize();
			if(d.getWidth()>0 && d.getHeight()>0){
				isElementFound =true;
				logger.info("wait over,element found");
				break;
			}
			else{
				logger.info("waiting...");
				pauseExecutionFor(1000);				
			}			
		}
		turnOnImplicitWaits();
		if(isElementFound ==false)
			logger.info("ELEMENT NOT FOUND");		
	}

	public void quickWaitForElementPresent(By locator){
		logger.info("quick wait started for "+locator);
		int timeout = 2;
		turnOffImplicitWaits(3);
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

	public void quickWaitForElementPresent(By locator,int timeout){
		logger.info("quick wait started for "+locator);
		turnOffImplicitWaits(1);
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

	public void waitForElementNotPresent(By locator) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
			logger.info("waiting for locator " + locator);
			wait.until(ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(locator)));
			logger.info("Element found");
		} catch (Exception e) {
			e.getStackTrace();
		}		
	}

	public void waitForElementNotPresent(By locator, int timeout) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			logger.info("waiting for locator " + locator);
			wait.until(ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(locator)));
			logger.info("Element found");
		} catch (Exception e) {
			e.getStackTrace();
		}		
	}

	public void waitForURLNotHaving(String keyWord, int timeout) {
		logger.info("wait started for not having " + keyWord + " in Current url");
		boolean iskeyWordPresent = true;
		for(int i=1;i<=timeout;i++){  
			try{
				if(driver.getCurrentUrl().contains(keyWord)){
					pauseExecutionFor(1000);
					logger.info("waiting...");
					continue;
				}else{
					logger.info("wait over," + keyWord + " is not present in Current Url");
					iskeyWordPresent = false;
					pauseExecutionFor(1000);
					break;
				}   
			}catch(Exception e){
				continue;
			}
		}
		if(iskeyWordPresent == false)
			logger.info(keyWord + " is not present in current URL");  
	}

	public void waitForLoadingImageToDisappear(){
		//int DEFAULT_TIMEOUT = 10;
		turnOffImplicitWaits(1);
		//		By locator = By.xpath("//div[@id='blockUIBody']");
		By locator = By.xpath("//html[@class='loader']");
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

	public void waitForCSCockpitLoadingImageToDisappear(){
		turnOffImplicitWaits(1);
		By locator = By.xpath("//div[@class='z-loading-indicator']");
		logger.info("Waiting for cscockpit loading image to get disappear");
		for(int i=1;i<=DEFAULT_TIMEOUT_CSCOCKPIT;i++){			
			try{
				if(driver.findElements(locator).size()==1){
					pauseExecutionFor(1000);
					logger.info("waiting..");
					continue;
				}else{
					turnOnImplicitWaits();
					logger.info("cscockpit loading image disappears");
					break;
				}			
			}catch(Exception e){
				continue;
			}
		}

	}

	public void waitForCSCockpitLoadingImageToDisappear(int time){
		turnOffImplicitWaits(1);
		By locator = By.xpath("//div[@class='z-loading-indicator']");
		logger.info("Waiting for cscockpit loading image to get disappear");
		for(int i=1;i<=time;i++){			
			try{
				if(driver.findElements(locator).size()==1){
					pauseExecutionFor(1000);
					logger.info("waiting..");
					continue;
				}else{
					turnOnImplicitWaits();
					logger.info("cscockpit loading image disappears");
					break;
				}			
			}catch(Exception e){
				continue;
			}
		}

	}

	public void waitForCRMLoadingImageToDisappear(){
		turnOffImplicitWaits(2);
		By locator = By.xpath("//span[contains(text(),'Loading')]");
		logger.info("Waiting for crm loading image to get disappear");
		for(int i=1;i<=DEFAULT_TIMEOUT;i++){			
			try{
				if(driver.findElements(locator).size()==1){
					pauseExecutionFor(1000);
					logger.info("waiting..");
					continue;
				}else{
					turnOnImplicitWaits();
					logger.info(" crm loading image disappears");
					break;
				}			
			}catch(Exception e){
				continue;
			}
		}

	}	

	public void waitForLoadingImageToAppear(){
		turnOffImplicitWaits(2);
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
		turnOffImplicitWaits(1);
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

	public void waitForTokenizing() {
		int timeout = 2;
		turnOffImplicitWaits(1);
		boolean isElementFound = false;
		for(int i=1;i<=timeout;i++){  
			try{
				if(driver.findElements(By.xpath("//*[@id='card_accountNumber'][@style='']")).size()==0){
					pauseExecutionFor(1000);
					logger.info("waiting...");
					//continue;
				}else{
					logger.info("wait over,element found");
					isElementFound =true;
					pauseExecutionFor(1000);
					break;
				}   
			}catch(Exception e){
				continue;
			}
			
		}
			turnOnImplicitWaits();
		if(isElementFound ==false)
			logger.info("ELEMENT NOT FOUND");  
	}

	public void waitForStorfrontLegacyLoadingImageToDisappear(){
		turnOffImplicitWaits(2);
		By locator = By.xpath("//div[contains(@id,'UpdateProgress')][contains(@style,'display: block;')]");
		logger.info("Waiting for storefront legacy loading image to get disappear");
		for(int i=1;i<=DEFAULT_TIMEOUT;i++){   
			try{
				if(driver.findElements(locator).size()==1){
					pauseExecutionFor(1000);
					logger.info("waiting..");
					continue;
				}else{
					turnOnImplicitWaits();
					logger.info("Storefront legacy loading image disappears");
					break;
				}   
			}catch(Exception e){
				continue;
			}
		}

	}

	public void waitForNSCore4LoadingImageToDisappear(){
		turnOffImplicitWaits(1);
		By locator = By.xpath("//div[@style='display: block;']/img");
		logger.info("Waiting for NSCore4 loading image to get disappear");
		for(int i=1;i<=DEFAULT_TIMEOUT;i++){   
			try{
				if(driver.findElements(locator).size()==1){
					pauseExecutionFor(1000);
					logger.info("waiting..");
					continue;
				}else{
					turnOnImplicitWaits();
					logger.info("NSCore4 loading image disappears");
					break;
				}   
			}catch(Exception e){
				continue;
			}
		}

	}

	public void waitForNSCore4ProcessImageToDisappear(){
		turnOffImplicitWaits(1);
		By locator = By.xpath("//div[@class='HModalOverlay']");
		logger.info("Waiting for NSCore4 process loading image to get disappear");
		for(int i=1;i<=DEFAULT_TIMEOUT;i++){   
			try{
				if(driver.findElements(locator).size()==1){
					pauseExecutionFor(1000);
					logger.info("waiting..");
					continue;
				}else{
					turnOnImplicitWaits();
					logger.info("NSCore4 process loading image disappears");
					break;
				}   
			}catch(Exception e){
				continue;
			}
		}
	}

	public void waitForLSDJustAMomentImageToDisappear(){
		turnOffImplicitWaits(1);
		By locator = By.xpath("//div[@id='preload'][not(@style='display:none')]");
		logger.info("Waiting for LSD loading image to get disappear");
		for(int i=1;i<=DEFAULT_TIMEOUT;i++){   
			try{
				if(driver.findElements(locator).size()==1){
					pauseExecutionFor(1000);
					logger.info("waiting..");
					continue;
				}else{
					turnOnImplicitWaits();
					logger.info("LSD loading image disappears");
					break;
				}   
			}catch(Exception e){
				continue;
			}
		}
	}

	public void waitForLSDLoaderAnimationImageToDisappear(){
		turnOffImplicitWaits(1);
		By locator = By.xpath("//div[contains(@class,'loader-animation')][(@style='')]");
		logger.info("Waiting for LSD loading image to get disappear");
		for(int i=1;i<=DEFAULT_TIMEOUT;i++){   
			try{
				if(driver.findElements(locator).size()==1){
					pauseExecutionFor(1000);
					logger.info("waiting..");
					continue;
				}else{
					turnOnImplicitWaits();
					logger.info("LSD Animation image disappears");
					break;
				}   
			}catch(Exception e){
				continue;
			}
		}
	}

	public void waitForElementTobeEnabled(By locator){
		for(int time=1;time<=30;time++){
			if(driver.findElement(locator).isEnabled()==true){
				break;
			}		
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public void moveToElement(By locator) {
		Actions build = new Actions(driver);
		build.moveToElement(driver.findElement(locator)).pause(1000).click(driver.findElement(locator)).build().perform();
		pauseExecutionFor(1000);
	}

	public void moveToElementByJS(By locator) {
		waitForElementPresent(locator, 5);
		String strJavaScript = "var element = arguments[0];"
				+ "var mouseEventObj = document.createEvent('MouseEvents');"
				+ "mouseEventObj.initEvent( 'mouseover', true, true );"
				+ "element.dispatchEvent(mouseEventObj);";
		JavascriptExecutor js =  (JavascriptExecutor)RFWebsiteDriver.driver;
		js.executeScript(strJavaScript, driver.findElement(locator));
		pauseExecutionFor(1000);
	}
	
	public void dblClickByJS(By locator) {  
		  String strJavaScript = "var targLink  = arguments[0];"
		    +" var clickEvent  = document.createEvent ('MouseEvents');"
		    +"clickEvent.initEvent ('dblclick', true, true);"
		    +"targLink.dispatchEvent (clickEvent);";
		  JavascriptExecutor js =  (JavascriptExecutor)RFWebsiteDriver.driver;
		  js.executeScript(strJavaScript, driver.findElement(locator));
		  pauseExecutionFor(1000);
		 }
	
	public void doubleClickByJS(By locator) {  
		  String strJavaScript = "var targLink  = arguments[0];"
		    +" var event = new MouseEvent('dblclick', {"
		    +"'view': window,"
		    +"'bubbles': true,"
		    +"'cancelable': true});"
		    +"targLink.dispatchEvent (event);";
		  JavascriptExecutor js =  (JavascriptExecutor)RFWebsiteDriver.driver;
		  js.executeScript(strJavaScript, driver.findElement(locator));
		  pauseExecutionFor(1000);
		 }
	
	public void clickByJS(By locator) {  
		  String strJavaScript = "var targLink  = arguments[0];"
		    +" var clickEvent  = document.createEvent ('MouseEvents');"
		    +"clickEvent.initEvent ('click', true, true,window);"
		    +"targLink.dispatchEvent (clickEvent);";
		  JavascriptExecutor js =  (JavascriptExecutor)RFWebsiteDriver.driver;
		  js.executeScript(strJavaScript, driver.findElement(locator));
		  pauseExecutionFor(1000);
		 }

	public void get(String Url) {
		logger.info("URL opened is "+Url);
		driver.get(Url);
		waitForPageLoad();
	}

	public void click(By locator) {		
		waitForElementToBeClickable(locator, DEFAULT_TIMEOUT);
		//movetToElementJavascript(locator);
		turnOffImplicitWaits(1);
		try{
			findElement(locator).click();			
		}catch(Exception e){
			retryingFindClick(locator);
		}
		turnOnImplicitWaits();
	}
	
	public void click(By locator,int timeout) {		
		waitForElementToBeClickable(locator, timeout);
		//movetToElementJavascript(locator);
		turnOffImplicitWaits(1);
		try{
			findElement(locator).click();			
		}catch(Exception e){
			retryingFindClick(locator);
		}
		turnOnImplicitWaits();
	}

	public void clickByAction(By locator) {
		Actions build = new Actions(driver);
		build.doubleClick(driver.findElement(locator)).build().perform();
		logger.info("double clicked on "+locator);
		pauseExecutionFor(1000);
	}

	public void type(By locator, String input) {
		try{
			//clickByJS(driver, findElement(locator));
			findElement(locator).clear();
		}catch(Exception e){
			pauseExecutionFor(2000);
			//			clickByJS(driver, findElement(locator));
			findElement(locator).clear();	
		}
		findElement(locator).sendKeys(input);
	}

	public void typeByJSLocId(String id, String input) {
		String javascript = "document.getElementById('"+id+"').value="+input;
		((JavascriptExecutor) RFWebsiteDriver.driver).executeScript(javascript);
	}

	public void quit() {
		driver.quit();
	}

	public String getCurrentUrl() {
		String currentURL = driver.getCurrentUrl();
		logger.info("current URL is "+currentURL);
		return currentURL;
	}

	public String getTitle() {
		return driver.getTitle();
	}

	public List<WebElement> findElements(By by) {
		// movetToElementJavascript(by);
		return driver.findElements(by);
	}

	public WebElement findElement(By by) {
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
		//		quickWaitForElementPresent(by);
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
	 * @author: 
	 * @date:
	 * @returnType: String
	 * @param by
	 * @return
	 */
	public String getText(By by) {
		return findElement(by).getText();
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
	 * @author: 
	 * @date:
	 * @returnType: WebElement
	 */
	public boolean isElementVisible(WebElement element) {
		try{
			return element.isDisplayed() ? true : false;
		}catch(Exception ex){
			return false;
		}
	}

	/**
	 * Checks if element is visible Purpose:
	 * 
	 * @author: 
	 * @date:
	 * @returnType: WebElement
	 */
	public boolean isElementVisible(By by) {
		try{
			turnOffImplicitWaits(3);
			return driver.findElement(by).isDisplayed() ? true : false;
		}catch(Exception ex){
			return false;
		}finally{
			turnOnImplicitWaits();
		}
	}

	/***
	 * This method press Escape key through action class
	 * 
	 * @param 
	 * @return
	 * 
	 */
	public void pressEscapeKey(){
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		robot.keyPress(KeyEvent.VK_ESCAPE);
		robot.keyRelease(KeyEvent.VK_ESCAPE);
	}

	/**
	 * 
	 * Purpose:Waits for element to be visible
	 * 
	 * @author: 
	 * @date:
	 * @returnType: WebElement
	 */
	public boolean waitForElementToBeVisible(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		WebElement element = wait.until(ExpectedConditions
				.visibilityOfElementLocated(locator));
		return isElementVisible(locator);
	}

	/**
	 * 
	 * Purpose:Waits for element to be visible with no timeout exception
	 * 
	 * @author: 
	 * @date:
	 * @returnType: void
	 */
	public void waitForElementVisibleNoTimeOut(By locator, int timeInterval){
		for(int time =1;time<=timeInterval;time++){
			if(isElementVisible(locator)==true){
				break;
			}
			else{
				pauseExecutionFor(1000);
				continue;
			}
		}
	}

	/**
	 * 
	 * Purpose: Waits and matches the exact title
	 * 
	 * @author: 
	 * @date:
	 * @returnType: boolean
	 */
	public boolean IsTitleEqual(By locator, int timeOut, String title) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		return wait.until(ExpectedConditions.titleIs(title));
	}

	/**
	 * Waits and matches if title contains the text Purpose:
	 * 
	 * @author: 
	 * @date:
	 * @returnType: boolean
	 */
	public boolean IsTitleContains(By locator, int timeOut, String title) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		return wait.until(ExpectedConditions.titleContains(title));
	}

	/**
	 * Wait for element to be clickable Purpose:
	 * 
	 * @author: 
	 * @date:
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
	 * @author: 
	 * @date:
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
			logger.info("Waiting For Page load via JS");
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
		logger.info("page load complete..");
		return isLoaded;
	}

	public void selectFromList(List<WebElement> lstElementList,
			String sValueToBeSelected) throws Exception {
		logger.info("START OF FUNCTION->selectFromList");
		boolean flag = false;
		logger.info("Total element found --> " + lstElementList.size());
		logger.info("Value to be selected " + sValueToBeSelected + " from list"
				+ lstElementList);
		for (WebElement e : lstElementList) {
			logger.info(">>>>>>>>>>>>>" + e.getText() + "<<<<<<<<<<<<<<<");
			if (e.getText().equalsIgnoreCase(sValueToBeSelected)) {
				logger.info("Value matched in list as->" + e.getText());
				e.click();
				flag = true;
				break;
			}
		}
		if (flag == false) {
			throw new Exception("No match found in the list for value->"
					+ sValueToBeSelected);
		}
		logger.info("END OF FUNCTION->selectFromList");
	}

	public WebElement waitForElementToBeClickable(WebElement element,
			int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		return element;
	}

	/**
	 * 
	 * Purpose: Helps in case of Stalement Exception
	 * 
	 * @author: 
	 * @date:
	 * @returnType: boolean
	 */
	public boolean retryingFindClick(By by) {
		boolean result = false;
		int attempts = 0;
		while (attempts < 5) {
			try {
				driver.findElement(by).click();
				result = true;
				break;
			} catch (StaleElementReferenceException e) {
			}
			attempts++;
			pauseExecutionFor(1000);
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
		logger.info("Scrolling to Element");
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].scrollIntoView(true);", element);
	}

	//	public void turnOffImplicitWaits() {
	//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	//	}

	public void turnOnImplicitWaits() {
		driver.manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
	}

	public void turnOffImplicitWaits(int time) {
		driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
	}

	//	public void turnOnImplicitWaits(int time) {
	//		driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
	//	}

	public void clickByJS(WebDriver driver, By by) {
		quickWaitForElementPresent(by);
		WebElement element = driver.findElement(by);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
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

	public String switchToSecondWindow(){
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
		return parentWindow;
	}

	public void switchToChildWindow(String parentWinHandle){
		Set<String> allWindows = driver.getWindowHandles();
		logger.info("total windows opened = "+allWindows.size());
		for(String allWin : allWindows){
			if(!allWin.equalsIgnoreCase(parentWinHandle)){
				driver.switchTo().window(allWin);
				break;
			}

		}
		logger.info("Switched to second window whose title is "+driver.getTitle());	

	}

	public String getCrmURL(){
		return propertyFile.getProperty("crmUrl");
	}

	public void switchToFrame(By by){
		WebElement element= driver.findElement(by);
		driver.switchTo().frame(element);
	}
	
	public void typeByJS(By by, String input) {
        WebElement element = driver.findElement(by);
        JavascriptExecutor executor = (JavascriptExecutor)RFWebsiteDriver.driver;
        executor.executeScript("arguments[0].value="+input+";", element);
	}
}

