package com.rf.pages.website.nscore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.rf.core.driver.website.RFWebsiteDriver;

public class NSCore4ProductsTabPage extends NSCore4RFWebsiteBasePage{

	private static final Logger logger = LogManager
			.getLogger(NSCore4ProductsTabPage.class.getName());

	private static String CatalogueListCatalogNameLOC = "//tr[@class='GridColHead']//th[contains(text(),'Catalog Name')]/following::tr[%s]/td[3]";
	private static String newlyCreatedCatalogLoc = "//tr[@class='GridColHead']//th[contains(text(),'Catalog Name')]/following::tr//a[contains(text(),'%s')]";


	private static final By CREATE_A_NEW_CATALOGUE_LINK_LOC = By.linkText("Create a New Catalog");
	private static final By SAVE_CATALOGUE_BTN_LOC = By.xpath("//a[@id='btnSave']");
	private static final By CATALOGUE_SAVE_MESSAGE1_LOC = By.xpath("//div[@id='messageCenter']");
	private static final By NAME_TXT_FIELD_LOC = By.id("txtName");
	private static final By STORE_FRONT_CHECKBOX_LOC = By.xpath("//td[contains(text(),'Store')]/following-sibling::td/input[6]");
	private static final By QUICK_PRODUCT_ADD_TXT_FIELD_LOC = By.id("productQuickAdd");
	private static final By ADD_LINK_LOC = By.xpath("//a[@id='btnQuickAdd']");
	private static final By ADDED_PRODUCT_LOC = By.xpath("//table[@id='catalogItems']/tbody/tr");
	private static final By CATALOGUE_LIST_LOC = By.xpath("//table[@class='DataGrid']//tr");
	private static final By CATEGORY_TREES_LINK_LOC = By.linkText("Category Trees");
	private static final By CATALOGUE_MANAGEMENT_LINK_LOC = By.linkText("Catalog Management");
	private static final By LIST_OF_CATALOG_LOC = By.xpath("//tr[@class='GridColHead']//th[contains(text(),'Used By (Store Front/Catalog)')]//following::tr[1]/td[3]/a");
	private static final By CREATE_TREE_CONFIRMATION_MSG_BOX_LOC = By.xpath("//div[@id='categoryMessageCenter']");
	private static final By TREE_NAME_TXT_FIELD_LOC = By.id("treeName");
	private static final By CREATE_TREE_BTN_LOC = By.id("btnCreateTree");
	private static final By SAVE_CATEGORY_BTN_LOC = By.xpath("//a[@id='btnSave']");
	public static final By CREATE_NEW_CATEGORY_TREE_LINK_LOC = By.id("Create a New Category Tree");

	public NSCore4ProductsTabPage(RFWebsiteDriver driver) {
		super(driver);
	}

	public void clickCreateANewCatalogLink() {
		driver.click(CREATE_A_NEW_CATALOGUE_LINK_LOC);
		logger.info("create a new catalogue link is clicked");
	}

	public void enterCatalogInfo(String catalogueName) {
		driver.clear(NAME_TXT_FIELD_LOC);
		driver.type(NAME_TXT_FIELD_LOC, catalogueName);
		logger.info("name text field entered with: "+catalogueName);
		driver.click(STORE_FRONT_CHECKBOX_LOC);
	}

	public void clickSaveCatalogBtn() {
		driver.click(SAVE_CATALOGUE_BTN_LOC);
		logger.info("save button clicked");
	}

	public void enterSkuQuickProductAddField(String sKU) {
		driver.type(QUICK_PRODUCT_ADD_TXT_FIELD_LOC, sKU);
		driver.click(ADD_LINK_LOC);
		logger.info("product has been added and add link clicked");
		driver.waitForElementPresent(ADDED_PRODUCT_LOC);
		clickSaveCatalogBtn();
	}

	public void clickCatalogManagementLink() {
		driver.click(CATALOGUE_MANAGEMENT_LINK_LOC);
		logger.info("catalog management link is clicked");
	}

	public boolean isNewCatalogPresentInList(String catalogueName) {
		int numberOfRows = driver.findElements(CATALOGUE_LIST_LOC).size();
		boolean status = false;
		for(int i = 2;i<=numberOfRows;i++){
			String listName = driver.findElement(By.xpath(String.format(CatalogueListCatalogNameLOC,i))).getText();
			if(listName.contains(catalogueName)){
				status = true;
				break;
			}
		}
		return status;
	}

	public void clicknewlyCreatedCatalogName(String catalogName) {
		driver.click(By.xpath(String.format(newlyCreatedCatalogLoc,catalogName)));
		logger.info("catalog name is clicked to update");
	}

	public boolean isSuccessMessagePresent() {
		driver.waitForElementToBeVisible(CATALOGUE_SAVE_MESSAGE1_LOC, 2);
		if(driver.findElement(CATALOGUE_SAVE_MESSAGE1_LOC).isDisplayed()){
			return true;}
		else
			return false;

	}

	public void clickCategoryTreesLink() {
		driver.quickWaitForElementPresent(CATEGORY_TREES_LINK_LOC);
		driver.click(CATEGORY_TREES_LINK_LOC);
		logger.info("Category Trees link is clicked");
	}

	public boolean verifyCurrentPage(String pageName) {
		return driver.getCurrentUrl().contains(pageName);
	}

	public boolean isCatalogAvailableOnPage() {
		int numberOFCatalogPresent = driver.findElements(LIST_OF_CATALOG_LOC).size();
		if(numberOFCatalogPresent>0){
			return true;}
		else
			return false;
	}

	public void clickCreateANewCategoryTree() {
		driver.click(CREATE_NEW_CATEGORY_TREE_LINK_LOC);
		logger.info("create new Category Trees link is clicked");
	}

	public void enterTreeNameAndClickCreateTreeBtn(String treeName) {
		driver.type(TREE_NAME_TXT_FIELD_LOC, treeName);
		logger.info("tree name entered: "+treeName);
		driver.click(CREATE_TREE_BTN_LOC);
		logger.info("create tree btn clicked");
	}

	public boolean verifyConfirmationMessage() {
		return driver.findElement(CREATE_TREE_CONFIRMATION_MSG_BOX_LOC).isDisplayed();
	}


}