package com.rf.test.website.nscore3.smoke;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.pages.website.nscore.NSCore3HomePage;
import com.rf.pages.website.nscore.NSCore3LoginPage;
import com.rf.test.website.RFNSCoreWebsiteBaseTest;

public class OrderTabTests extends RFNSCoreWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(OrderTabTests.class.getName());

	private NSCore3HomePage nscore3HomePage;
	private NSCore3LoginPage nscore3LoginPage;

	//Orders - User is able to search for orders by consultant name
	@Test
	public void testOrdersUserIsAbleToSearchForOrdersByConsultant(){
		String orderID = "Order ID";
		String orderNumber = "Order Number";
		String customer = "Customer";
		String subTotal = "Subtotal";
		String grandTotal = "Grand Total";
		String consultant = "Consultant";
		nscore3LoginPage = new NSCore3LoginPage(driver);
		nscore3LoginPage.enterUsername("admin");
		nscore3LoginPage.enterPassword("skin123!");
		nscore3HomePage = nscore3LoginPage.clickLoginBtn();
		nscore3HomePage.clickOrdersLink();
		nscore3HomePage.clickSearchByDD();
		nscore3HomePage.selectValueFromSearchByDD("Consultant Name");
		nscore3HomePage.enterSearchValue("Sarah Robbins");
		nscore3HomePage.clickSearch();
		nscore3HomePage.clickShowColumnSlection();
		nscore3HomePage.isSelectionColumnValueChecked(orderID);
		nscore3HomePage.isSelectionColumnValueChecked(orderNumber);
		nscore3HomePage.isSelectionColumnValueChecked(customer);
		nscore3HomePage.isSelectionColumnValueChecked(subTotal);
		nscore3HomePage.isSelectionColumnValueChecked(grandTotal);
		nscore3HomePage.isSelectionColumnValueChecked(consultant);
		nscore3HomePage.clickSaveChangesLink();
		s_assert.assertTrue(nscore3HomePage.isColumnNamePresent(consultant), "Consultant column is not present");
		int consultantColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(consultant);
		int rowNumber = nscore3HomePage.getRowNumberWithExpectedName("Sarah Robbins", consultantColumnNumber);

		//get order number
		int orderNumberColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(orderNumber);
		String orderNumberValue = nscore3HomePage.getCellValue(rowNumber, orderNumberColumnNumber);

		//get customer type
		int customerColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(customer);
		String customerValue = nscore3HomePage.getCellValue(rowNumber, customerColumnNumber);

		//get subtotal
		int subtotalColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(subTotal);
		String subTotalValue = nscore3HomePage.getCellValue(rowNumber, subtotalColumnNumber);

		//get grandTotal
		int grandTotalColumnNumber = nscore3HomePage.getColumnNumberHavingExpectedColumnName(grandTotal);
		String grandTotalValue = nscore3HomePage.getCellValue(rowNumber, grandTotalColumnNumber);

		nscore3HomePage.clickDetails(rowNumber);
		s_assert.assertTrue(nscore3HomePage.getOrderNumberFromOrderDetails().contains(orderNumberValue), "Expected order number at order details page is: "+orderNumber+" actual on UI is: "+nscore3HomePage.getOrderNumberFromOrderDetails());
		s_assert.assertTrue(nscore3HomePage.getCustomerTypeFromOrderDetails().contains(customerValue), "Expected customer type at order details page is: "+customerValue+" actual on UI is: "+nscore3HomePage.getCustomerTypeFromOrderDetails());
		s_assert.assertTrue(nscore3HomePage.getSubTotalFromOrderDetails().contains(subTotalValue), "Expected subtotal at order details page is: "+subTotalValue+" actual on UI is: "+nscore3HomePage.getSubTotalFromOrderDetails());
		s_assert.assertTrue(nscore3HomePage.getgrandTotalFromOrderDetails().contains(grandTotalValue), "Expected grand total at order details page is: "+grandTotalValue+" actual on UI is: "+nscore3HomePage.getgrandTotalFromOrderDetails());
		s_assert.assertAll();
	}

	//Orders - User is able to search for orders by customer account
	@Test
	public void testVerifyUserIsAbleToSearchForOrderByCustomerAccount() throws InterruptedException{
		String orderID = "Order ID";
		String orderNumber = "Order Number";
		String customer = "Customer";
		String subTotal = "Subtotal";
		String grandTotal = "Grand Total";
		String consultant = "Consultant";
		nscore3LoginPage = new NSCore3LoginPage(driver);

		//Login to application
		nscore3LoginPage.enterUsername("admin");
		nscore3LoginPage.enterPassword("skin123!");
		nscore3HomePage=nscore3LoginPage.clickLoginBtn();
		nscore3HomePage.clickOrdersLink();
		nscore3HomePage.clickSearchByDD();
		nscore3HomePage.selectValueFromSearchByDD("Customer Account");
		nscore3HomePage.enterSearchValue("010080");
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
		int rowNumber = nscore3HomePage.getRowNumberWithExpectedName("Tracy Sharpe", customerColumnNumber);
		String cellValue =nscore3HomePage.getCellValue(rowNumber, customerColumnNumber);
		s_assert.assertTrue(cellValue.equalsIgnoreCase("Tracy Sharpe"),"Tracy Sharpe customer name is not present in row "+rowNumber+"and column"+customerColumnNumber);
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

}
