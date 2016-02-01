package com.rf.pages.website.cscockpit;

import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class CSCockpitAutoshipTemplateTabPage extends CSCockpitRFWebsiteBasePage{
 private static final Logger logger = LogManager
   .getLogger(CSCockpitAutoshipTemplateTabPage.class.getName());
 
 private static final By RUN_NOW_BUTTON_AUTOSHIP_TEMPLATE_TAB = By.xpath("//td[text()='Run Now']");
 private static final By CONFIRM_MESSAGE_YES_LOC = By.xpath("//td[text()='Yes']");
 private static final By CONFIRM_MESSAGE_OK_LOC = By.xpath("//td[text()='OK']");
 
 protected RFWebsiteDriver driver;
 public CSCockpitAutoshipTemplateTabPage(RFWebsiteDriver driver) {
  super(driver);
  this.driver = driver;
 }

 public void clickRunNowButtonOnAutoshipTemplateTab() {
  driver.waitForElementPresent(RUN_NOW_BUTTON_AUTOSHIP_TEMPLATE_TAB);
  driver.click(RUN_NOW_BUTTON_AUTOSHIP_TEMPLATE_TAB);
  driver.waitForLoadingImageToDisappear();

  
 }
 public void clickOkForRegeneratedIdpopUp() {
  try{
    if(driver.isElementPresent(CONFIRM_MESSAGE_YES_LOC) && driver.findElement(CONFIRM_MESSAGE_YES_LOC).isDisplayed()){
     driver.click(CONFIRM_MESSAGE_YES_LOC);
      driver.waitForCSCockpitLoadingImageToDisappear(); 
        }
    }catch(NoSuchElementException nse){
   
        }
  
  }
 public boolean verifyConfirmMessagePopUpIsAppearing() {
  return driver.isElementPresent(CONFIRM_MESSAGE_OK_LOC);
 }
  public void clickOkConfirmMessagePopUp(){   
   try{
   driver.click(CONFIRM_MESSAGE_OK_LOC);
         driver.waitForCSCockpitLoadingImageToDisappear();
   }catch(NoSuchElementException e){
    
   }
  }
 
}