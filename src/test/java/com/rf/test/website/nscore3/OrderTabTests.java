package com.rf.test.website.nscore3;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.dbQueries.DBQueries_RFL;
import com.rf.pages.website.nscore.NSCore3HomePage;
import com.rf.pages.website.nscore.NSCore3LoginPage;
import com.rf.test.website.RFNSCoreWebsiteBaseTest;

public class OrderTabTests extends RFNSCoreWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(OrderTabTests.class.getName());

	private NSCore3HomePage nscore3HomePage;
	private NSCore3LoginPage nscore3LoginPage;
	private String RFL_DB = null;


	//Orders - User is able to search for orders by customer name
	@Test
	public void testOrdersUserIsAbleToSearchForOrdersByCustomerName(){
		String orderID = "Order ID";
		String orderNumber = "Order Number";
		String customer = "Customer";
		String subTotal = "Subtotal";
		String grandTotal = "Grand Total";
		String consultant = "Consultant";
		String customerID = "Customer ID";
		String customerName = null;
		String accountNumber = null;
		String firstName = null;
		String lastName = null;
		List<Map<String, Object>> randomAccountList =  null;
		RFL_DB = driver.getDBNameRFL();
		logger.info("DB is "+RFL_DB);
		nscore3LoginPage = new NSCore3LoginPage(driver);
		nscore3HomePage = new NSCore3HomePage(driver);
		randomAccountList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACCOUNT_DETAILS,RFL_DB);
		accountNumber = (String) getValueFromQueryResult(randomAccountList, "AccountNumber");	
		firstName = (String) getValueFromQueryResult(randomAccountList, "FirstName");	
		lastName = (String) getValueFromQueryResult(randomAccountList, "LastName");	
		logger.info("Account number from DB is "+accountNumber);
		logger.info("First name from DB is "+firstName);
		logger.info("Last name from DB is "+lastName);
		customerName = firstName.trim() +" "+ lastName.trim();
		nscore3HomePage.clickOrdersLink();
		nscore3HomePage.clickSearchByDD();
		nscore3HomePage.selectValueFromSearchByDD("Customer Name");
		nscore3HomePage.enterSearchValue(customerName);
		nscore3HomePage.clickSearch();

		nscore3HomePage.clickShowColumnSelection();
		nscore3HomePage.isSelectionColumnValueChecked(orderID);
		nscore3HomePage.isSelectionColumnValueChecked(orderNumber);
		nscore3HomePage.isSelectionColumnValueChecked(customer);
		nscore3HomePage.isSelectionColumnValueChecked(subTotal);
		nscore3HomePage.isSelectionColumnValueChecked(grandTotal);
		nscore3HomePage.isSelectionColumnValueChecked(consultant);
		nscore3HomePage.isSelectionColumnValueChecked(customerID);
		nscore3HomePage.clickSaveChangesLink();

		s_assert.assertTrue(nscore3HomePage.isColumnNamePresent(customer), "customer column is not present");

		int consultantColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(customer);
		int rowNumber = nscore3HomePage.getRowNumberWithExpectedName(customerName, consultantColumnNumber);
		s_assert.assertTrue(nscore3HomePage.isExpectedValuePresentInColumn(customerName, consultantColumnNumber), customerName+" NOT found in search result");

		//get order number
		int orderNumberColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(orderNumber);
		String orderNumberValue = nscore3HomePage.getCellValue(rowNumber, orderNumberColumnNumber);

		//get customer type
		int customerColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(customer);
		String customerValue = nscore3HomePage.getCellValue(rowNumber, customerColumnNumber);

		//get customer id
		int customerIdColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(customerID);
		String customerIdValue = nscore3HomePage.getCellValue(rowNumber, customerIdColumnNumber);

		//get subtotal
		int subtotalColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(subTotal);
		String subTotalValue = nscore3HomePage.getCellValue(rowNumber, subtotalColumnNumber);

		//get grandTotal
		int grandTotalColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(grandTotal);
		String grandTotalValue = nscore3HomePage.getCellValue(rowNumber, grandTotalColumnNumber);

		nscore3HomePage.clickDetails(rowNumber);
		s_assert.assertTrue(nscore3HomePage.getOrderNumberFromOrderDetails().contains(orderNumberValue), "Expected order number at order details page is: "+orderNumber+" actual on UI is: "+nscore3HomePage.getOrderNumberFromOrderDetails());
		s_assert.assertTrue(nscore3HomePage.getCustomerTypeFromOrderDetails().contains(customerValue), "Expected customer type at order details page is: "+customerValue+" actual on UI is: "+nscore3HomePage.getCustomerTypeFromOrderDetails());
		//s_assert.assertTrue(nscore3HomePage.getAccountNumberFromOrderDetails().contains(accountNumber),"Expected Account Number at order details page is: "+accountNumber+" actual on UI is: "+nscore3HomePage.getAccountNumberFromOrderDetails());
		s_assert.assertTrue(nscore3HomePage.getSubTotalFromOrderDetails().contains(subTotalValue), "Expected subtotal at order details page is: "+subTotalValue+" actual on UI is: "+nscore3HomePage.getSubTotalFromOrderDetails());
		s_assert.assertTrue(nscore3HomePage.getgrandTotalFromOrderDetails().contains(grandTotalValue), "Expected grand total at order details page is: "+grandTotalValue+" actual on UI is: "+nscore3HomePage.getgrandTotalFromOrderDetails());
		s_assert.assertAll();
	}

	//Distributors-Search by e-mail
	@Test
	public void testSearchByEmail() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		String distributorTab = "Distributors";
		String searchByFieldOndistributorTab = "E-Mail";
		String accountNumber = null;
		String firstName = null;
		String lastName = null;
		String emailAddress = null;
		String firstNameColumnNumberFromName ="First Name";
		String LastNameColumnNumberFromName ="Last Name";
		String accountNumberColumnNumberFromName ="Account No.";
		String cellValue =null;
		nscore3LoginPage = new NSCore3LoginPage(driver);
		nscore3HomePage = new NSCore3HomePage(driver);

		List<Map<String, Object>> randomConsultantList =  null;

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
		emailAddress = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		accountNumber = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountNumber"));
		firstName = (String) getValueFromQueryResult(randomConsultantList, "FirstName");
		lastName = (String) getValueFromQueryResult(randomConsultantList, "LastName");
		//String completeName =firstName+" "+lastName;

		nscore3HomePage.clickTab(distributorTab);
		nscore3HomePage.selectValueFromNameDDOnDistributorTab(searchByFieldOndistributorTab);
		nscore3HomePage.enterSearchForFieldOnDistributorTab(emailAddress);
		nscore3HomePage.clickSearchOnDistributorTab();
		//verify the entered customer details.
		int firstNameColumnNumber=nscore3HomePage.getColumnNumberHavingExpectedColumnNameOnDistributorPage(firstNameColumnNumberFromName);
		cellValue =nscore3HomePage.getFirstRowCellValueAsPerColumnName(firstNameColumnNumber);
		s_assert.assertTrue(cellValue.equalsIgnoreCase(firstName)," Expected Customer First name"+firstName+" is not present in row and column"+firstNameColumnNumber+" While actual on UI is: "+cellValue);
		//Verify Last name
		int lastNameColumnNumber=nscore3HomePage.getColumnNumberHavingExpectedColumnNameOnDistributorPage(LastNameColumnNumberFromName);
		cellValue =nscore3HomePage.getFirstRowCellValueAsPerColumnName(lastNameColumnNumber);
		s_assert.assertTrue(cellValue.equalsIgnoreCase(lastName)," Expected Customer last name"+lastName+" is not present in row and column"+lastNameColumnNumber+" While actual on UI is: "+cellValue);
		//	  //Verify Account Number
		//	  int accountNumberColumnNumber=nscore3HomePage.getColumnNumberHavingExpectedColumnNameOnDistributorPage(accountNumberColumnNumberFromName);
		//	  cellValue =nscore3HomePage.getFirstRowCellValueAsPerColumnName(accountNumberColumnNumber);
		//	  s_assert.assertTrue(cellValue.equalsIgnoreCase(accountNumber)," Expected Customer account number"+accountNumber+" is not present in row and column"+accountNumberColumnNumber+" While actual on UI is: "+cellValue);
		s_assert.assertAll();
	}


	//Orders - User is able to search for orders by order number
	@Test
	public void testUserAbleToSearchOrdersByOrderNumber(){
		String orderID = "Order ID";
		String orderNumber = "Order Number";
		String customer = "Customer";
		String subTotal = "Subtotal";
		String grandTotal = "Grand Total";
		String consultant = "Consultant";
		String customerID = "Customer ID";
		String customerName = null;
		String orderNumberDB = null;
		String accountNumber = null;
		String accountID = null;
		String firstNameDB = null;
		String lastNameDB = null;
		List<Map<String, Object>> randomOrderList =  null;
		List<Map<String, Object>> randomOrderDetailsList =  null;
		RFL_DB = driver.getDBNameRFL();
		logger.info("DB is "+RFL_DB);
		nscore3LoginPage = new NSCore3LoginPage(driver);
		nscore3HomePage = new NSCore3HomePage(driver);
		randomOrderList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ORDER_DETAILS,RFL_DB);
		orderNumberDB = String.valueOf(getValueFromQueryResult(randomOrderList, "OrderNumber"));	
		logger.info("Order Number from DB is "+orderNumberDB);
		randomOrderDetailsList =  DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_ORDER_ACCOUNT_DETAILS_ACTIVE_USER_RFL, orderNumberDB),RFL_DB);
		firstNameDB = String.valueOf(getValueFromQueryResult(randomOrderDetailsList, "FirstName"));
		lastNameDB = String.valueOf(getValueFromQueryResult(randomOrderDetailsList, "LastName"));
		logger.info("First name from DB is "+firstNameDB);
		logger.info("Last name from DB is "+lastNameDB);
		nscore3HomePage.clickOrdersLink();
		nscore3HomePage.clickSearchByDD();
		nscore3HomePage.selectValueFromSearchByDD("Order Number");
		nscore3HomePage.enterSearchValue(orderNumberDB);
		nscore3HomePage.clickSearch();

		nscore3HomePage.clickShowColumnSelection();
		nscore3HomePage.isSelectionColumnValueChecked(orderID);
		nscore3HomePage.isSelectionColumnValueChecked(orderNumber);
		nscore3HomePage.isSelectionColumnValueChecked(customer);
		nscore3HomePage.isSelectionColumnValueChecked(subTotal);
		nscore3HomePage.isSelectionColumnValueChecked(grandTotal);
		nscore3HomePage.isSelectionColumnValueChecked(consultant);
		nscore3HomePage.isSelectionColumnValueChecked(customerID);
		nscore3HomePage.clickSaveChangesLink();
		s_assert.assertTrue(nscore3HomePage.isColumnNamePresent(orderNumber), "order Number column is not present");
		int orderNumberColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(orderNumber);
		int rowNumber = 2;
		s_assert.assertTrue(nscore3HomePage.isExpectedValuePresentInColumn(orderNumberDB, orderNumberColumnNumber), orderNumberDB+" NOT found in search result");
		customerName = firstNameDB.trim() +" "+ lastNameDB.trim();
		int consultantColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(customer);
		s_assert.assertTrue(nscore3HomePage.isExpectedValuePresentInColumn(customerName, consultantColumnNumber), customerName+" NOT found in search result");
		String orderNumberValue = nscore3HomePage.getCellValue(rowNumber, orderNumberColumnNumber);
		//get subtotal
		int subtotalColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(subTotal);
		String subTotalValue = nscore3HomePage.getCellValue(rowNumber, subtotalColumnNumber);

		//get grandTotal
		int grandTotalColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(grandTotal);
		String grandTotalValue = nscore3HomePage.getCellValue(rowNumber, grandTotalColumnNumber);
		nscore3HomePage.clickDetails(rowNumber);
		s_assert.assertTrue(nscore3HomePage.getOrderNumberFromOrderDetails().contains(orderNumberValue), "Expected order number at order details page is: "+orderNumber+" actual on UI is: "+nscore3HomePage.getOrderNumberFromOrderDetails());
		s_assert.assertTrue(nscore3HomePage.getSubTotalFromOrderDetails().contains(subTotalValue), "Expected subtotal at order details page is: "+subTotalValue+" actual on UI is: "+nscore3HomePage.getSubTotalFromOrderDetails());
		s_assert.assertTrue(nscore3HomePage.getgrandTotalFromOrderDetails().contains(grandTotalValue), "Expected grand total at order details page is: "+grandTotalValue+" actual on UI is: "+nscore3HomePage.getgrandTotalFromOrderDetails());
		s_assert.assertAll();
	}

	//Orders - User is able to search for orders by customer account
	@Test
	public void testVerifyUserIsAbleToSearchForOrderByCustomerAccount() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		String orderID = "Order ID";
		String orderNumber = "Order Number";
		String customer = "Customer";
		String subTotal = "Subtotal";
		String grandTotal = "Grand Total";
		String consultant = "Consultant";
		String accountNumber = null;
		String firstName = null;
		String lastName = null;
		nscore3LoginPage = new NSCore3LoginPage(driver);
		nscore3HomePage = new NSCore3HomePage(driver);

		List<Map<String, Object>> randomConsultantList =  null;

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
		accountNumber = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountNumber"));
		firstName = (String) getValueFromQueryResult(randomConsultantList, "FirstName");
		lastName = (String) getValueFromQueryResult(randomConsultantList, "LastName");
		String completeName =firstName.trim()+" "+lastName.trim();

		nscore3HomePage.clickOrdersLink();
		nscore3HomePage.clickSearchByDD();
		nscore3HomePage.selectValueFromSearchByDD("Customer Account");
		nscore3HomePage.enterSearchValue(accountNumber);
		nscore3HomePage.clickSearch();
		nscore3HomePage.clickShowColumnSlection();
		nscore3HomePage.isSelectionColumnValueChecked(orderID);
		nscore3HomePage.isSelectionColumnValueChecked(orderNumber);
		nscore3HomePage.isSelectionColumnValueChecked(customer);
		nscore3HomePage.isSelectionColumnValueChecked(subTotal);
		nscore3HomePage.isSelectionColumnValueChecked(grandTotal);
		nscore3HomePage.isSelectionColumnValueChecked(consultant);
		nscore3HomePage.clickSaveChangesLink();
		int customerColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(customer);
		int rowNumber = nscore3HomePage.getRowNumberWithExpectedName(completeName, customerColumnNumber);
		String cellValue =nscore3HomePage.getCellValue(rowNumber, customerColumnNumber);
		s_assert.assertTrue(cellValue.equalsIgnoreCase(completeName)," Expected Customer name"+completeName+" is not present in row "+rowNumber+"and column"+customerColumnNumber+" While actual on UI is: "+cellValue);
		//get order number
		int orderNumberColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(orderNumber);
		String orderNumberValue = nscore3HomePage.getCellValue(rowNumber, orderNumberColumnNumber);

		//get customer type
		customerColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(customer);
		String customerValue = nscore3HomePage.getCellValue(rowNumber, customerColumnNumber);

		//get subtotal
		int subtotalColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(subTotal);
		String subTotalValue = nscore3HomePage.getCellValue(rowNumber, subtotalColumnNumber);

		//get grandTotal
		int grandTotalColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(grandTotal);
		String grandTotalValue = nscore3HomePage.getCellValue(rowNumber, grandTotalColumnNumber);

		//Verify Consultant column is present.
		s_assert.assertTrue(nscore3HomePage.isColumnNamePresent(consultant),"Consultant Column is not present in UI");
		//Click details link.
		nscore3HomePage.clickDetails(rowNumber);
		s_assert.assertTrue(nscore3HomePage.getOrderNumberFromOrderDetails().contains(orderNumberValue), "Expected order number at order details page is: "+orderNumber+" actual on UI is: "+nscore3HomePage.getOrderNumberFromOrderDetails());
		s_assert.assertTrue(nscore3HomePage.getCustomerTypeFromOrderDetails().contains(customerValue), "Expected customer type at order details page is: "+customerValue+" actual on UI is: "+nscore3HomePage.getCustomerTypeFromOrderDetails());
		s_assert.assertTrue(nscore3HomePage.getSubTotalFromOrderDetails().contains(subTotalValue), "Expected subtotal at order details page is: "+subTotalValue+" actual on UI is: "+nscore3HomePage.getSubTotalFromOrderDetails());
		s_assert.assertTrue(nscore3HomePage.getgrandTotalFromOrderDetails().contains(grandTotalValue), "Expected grand total at order details page is: "+grandTotalValue+" actual on UI is: "+nscore3HomePage.getgrandTotalFromOrderDetails());
		s_assert.assertAll();
	}

	//Distributors-Search by customer Name
	@Test
	public void testSearchByCustomerName() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		String distributorTab = "Distributors";
		String searchByFieldOndistributorTab = "Name";
		String firstName = null;
		String lastName = null;
		String completeName = null;
		nscore3LoginPage = new NSCore3LoginPage(driver);
		nscore3HomePage = new NSCore3HomePage(driver);

		List<Map<String, Object>> randomConsultantList =  null;

		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
		firstName = (String) getValueFromQueryResult(randomConsultantList, "FirstName");
		lastName = (String) getValueFromQueryResult(randomConsultantList, "LastName");
		completeName = firstName+" "+lastName;

		nscore3HomePage.clickTab(distributorTab);
		nscore3HomePage.selectValueFromNameDDOnDistributorTab(searchByFieldOndistributorTab);
		nscore3HomePage.enterSearchForFieldOnDistributorTab(completeName);
		nscore3HomePage.clickSearchOnDistributorTab();
		//verify all rows contains the searched complete name.
		s_assert.assertTrue(nscore3HomePage.isAllRowsContainsTheCompleteName(completeName),"Expected complete name for all rows are: "+completeName+" but Actual on UI is not present for all rows");
		s_assert.assertAll();
	}

	//Distributors-Search by consultant
	@Test
	public void testSearchByConsultant() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		String distributorTab = "Distributors";
		String searchByFieldOndistributorTab = "Account Type";
		String userType = "Consultant";
		nscore3LoginPage = new NSCore3LoginPage(driver);
		nscore3HomePage = new NSCore3HomePage(driver);

		nscore3HomePage.clickTab(distributorTab);
		nscore3HomePage.selectValueFromNameDDOnDistributorTab(searchByFieldOndistributorTab);
		nscore3HomePage.selectUserTypeFromDDOnDistributorTab(userType);
		nscore3HomePage.clickSearchOnDistributorTab();
		//verify all rows contains the searched Account Type.
		s_assert.assertTrue(nscore3HomePage.isAllRowsContainsTheAccountType(userType),"Expected Account type for all rows are: "+userType+" but Actual on UI is not present for all rows");
		s_assert.assertAll();
	}

	//Orders - User is able to search for orders by consultant name
	//	@Test
	//	public void testOrdersUserIsAbleToSearchForOrdersByConsultant(){
	//		String orderID = "Order ID";
	//		String orderNumber = "Order Number";
	//		String customer = "Customer";
	//		String subTotal = "Subtotal";
	//		String grandTotal = "Grand Total";
	//		String consultant = "Consultant";
	//		nscore3LoginPage = new NSCore3LoginPage(driver);
	//		nscore3LoginPage.enterUsername("admin");
	//		nscore3LoginPage.enterPassword("skin123!");
	//		nscore3HomePage = nscore3LoginPage.clickLoginBtn();
	//		nscore3HomePage.clickOrdersLink();
	//		nscore3HomePage.clickSearchByDD();
	//		nscore3HomePage.selectValueFromSearchByDD("Consultant Name");
	//		nscore3HomePage.enterSearchValue("Sarah Robbins");
	//		nscore3HomePage.clickSearch();
	//		nscore3HomePage.clickShowColumnSlection();
	//		nscore3HomePage.isSelectionColumnValueChecked(orderID);
	//		nscore3HomePage.isSelectionColumnValueChecked(orderNumber);
	//		nscore3HomePage.isSelectionColumnValueChecked(customer);
	//		nscore3HomePage.isSelectionColumnValueChecked(subTotal);
	//		nscore3HomePage.isSelectionColumnValueChecked(grandTotal);
	//		nscore3HomePage.isSelectionColumnValueChecked(consultant);
	//		nscore3HomePage.clickSaveChangesLink();
	//		s_assert.assertTrue(nscore3HomePage.isColumnNamePresent(consultant), "Consultant column is not present");
	//		int consultantColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(consultant);
	//		int rowNumber = nscore3HomePage.getRowNumberWithExpectedName("Sarah Robbins", consultantColumnNumber);
	//
	//		//get order number
	//		int orderNumberColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(orderNumber);
	//		String orderNumberValue = nscore3HomePage.getCellValue(rowNumber, orderNumberColumnNumber);
	//
	//		//get customer type
	//		int customerColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(customer);
	//		String customerValue = nscore3HomePage.getCellValue(rowNumber, customerColumnNumber);
	//
	//		//get subtotal
	//		int subtotalColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(subTotal);
	//		String subTotalValue = nscore3HomePage.getCellValue(rowNumber, subtotalColumnNumber);
	//
	//		//get grandTotal
	//		int grandTotalColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(grandTotal);
	//		String grandTotalValue = nscore3HomePage.getCellValue(rowNumber, grandTotalColumnNumber);
	//
	//		nscore3HomePage.clickDetails(rowNumber);
	//		s_assert.assertTrue(nscore3HomePage.getOrderNumberFromOrderDetails().contains(orderNumberValue), "Expected order number at order details page is: "+orderNumber+" actual on UI is: "+nscore3HomePage.getOrderNumberFromOrderDetails());
	//		s_assert.assertTrue(nscore3HomePage.getCustomerTypeFromOrderDetails().contains(customerValue), "Expected customer type at order details page is: "+customerValue+" actual on UI is: "+nscore3HomePage.getCustomerTypeFromOrderDetails());
	//		s_assert.assertTrue(nscore3HomePage.getSubTotalFromOrderDetails().contains(subTotalValue), "Expected subtotal at order details page is: "+subTotalValue+" actual on UI is: "+nscore3HomePage.getSubTotalFromOrderDetails());
	//		s_assert.assertTrue(nscore3HomePage.getgrandTotalFromOrderDetails().contains(grandTotalValue), "Expected grand total at order details page is: "+grandTotalValue+" actual on UI is: "+nscore3HomePage.getgrandTotalFromOrderDetails());
	//		s_assert.assertAll();
	//	}


}