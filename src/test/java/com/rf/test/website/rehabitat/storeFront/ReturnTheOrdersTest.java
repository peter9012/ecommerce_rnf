package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class ReturnTheOrdersTest extends StoreFrontWebsiteBaseTest{

	@Test(dataProvider="rfTestData")
	public void returnTheOrders(String email){
		driver.get("https://www.qa1.rodanandfields.com/US/email/createTestOrder?uid="+email);
	}
}
