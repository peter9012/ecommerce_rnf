package com.rf.pages.website.pulselsd;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class PulseLSDLoginPage {

	public static void main(String[] args) {
		// Setting constant variables used throughout
	 	String username = "sarah@sarahrobbins.com";
		String password = "111maiden";
		String baseUrl = "https://r+f-qa:4llH41l7h3Gl0wCl0ud@laserhead.tst.randfapi.com";
		 
		// Create a new instance of the Firefoxdriver
		WebDriver driver = new FirefoxDriver();
		 	  
		// Resize window to mobile iPhone5
		Dimension d = new Dimension(375,627);
				  
		// Resize the current window to the given dimension
		driver.manage().window().setSize(d);
		  
		// Navigate to /#/login page
		driver.get(baseUrl + "/#/login");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			    
		// Test negative login scenario - wrong password
		driver.findElement(By.xpath(".//*[@id='username']")).clear();
		driver.findElement(By.xpath(".//*[@id='password']")).clear();
		driver.findElement(By.xpath(".//*[@id='username']")).sendKeys("erica.burgess@yahoo.com");
		driver.findElement(By.xpath(".//*[@id='password']")).sendKeys("wrongpassword");
		driver.findElement(By.xpath(".//*[@id='login_submit_button']")).click();
				
		// Test negative login scenario - login w/non-whitelisted user
		driver.findElement(By.xpath(".//*[@id='username']")).clear();
		driver.findElement(By.xpath(".//*[@id='password']")).clear();
		driver.findElement(By.xpath(".//*[@id='username']")).sendKeys("renee.corker@gmail.com");
		driver.findElement(By.xpath(".//*[@id='password']")).sendKeys("111maiden");
		driver.findElement(By.xpath(".//*[@id='login_submit_button']")).click();
				  
		// Test positive login scenario
		driver.findElement(By.xpath(".//*[@id='username']")).clear();
		driver.findElement(By.xpath(".//*[@id='password']")).clear();
		driver.findElement(By.xpath(".//*[@id='username']")).sendKeys(username);
		driver.findElement(By.xpath(".//*[@id='password']")).sendKeys(password);
		driver.findElement(By.xpath(".//*[@id='login_submit_button']")).click();
		
		//Close browser
		driver.close();

	}

}