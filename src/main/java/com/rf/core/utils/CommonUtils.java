package com.rf.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import com.rf.core.driver.website.RFWebsiteDriver;


/**
 * Utility class for common functions.
 * 
 * @author GSPANN
 * 
 */
public class CommonUtils {


	public static FileOutputStream getFileOutputStream(final String fileName) throws IOException {
		FileOutputStream fos = new FileOutputStream(fileName);
		return fos;
	}

	public static InputStream loadInputStream(final String classpathLocation, final String fileSystemLocation)
			throws IOException {
		InputStream in = null;

		in = CommonUtils.class.getResourceAsStream(classpathLocation);
		if (in == null) {
			in = new FileInputStream(fileSystemLocation);
		}
		return in;
	}

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns test dat file path attached at test class
	 * 
	 * @return the path string
	 */
	public static String getTestDataFilePath(final String className) {
		String fileSep = File.separator;
		return System.getProperty("user.dir") + fileSep + "test"+fileSep+"resources"+fileSep+"testdata"+fileSep + className + ".xlsx";
	}


	/**
	 * Returns Integer value of Amount
	 * 
	 * @param value
	 * @return - Integer Value
	 */
	public static int getIntVal(String value) {
		String stringVal = value;
		if (value.contains(".")) {
			String[] valArray = stringVal.split("[.]");
			stringVal = valArray[0];
		}
		if (value.contains(",")) {
			stringVal = stringVal.replace(",", "");
		}
		return Integer.parseInt(stringVal);
	}

	/**
	 * Generates random integer from a range
	 * 
	 * @param min
	 * @param max
	 * @return - random integer
	 */
	public static int getRandomNum(int min, int max) {
		return new Random().nextInt((max - min) + 1) + min;
	} 

	public static String getCurrentTimeStamp(){
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		timeStamp = timeStamp.replace('.', '_').replaceAll("_", "");
		return timeStamp;
	}

	public static String getRandomWord(int length) {
		String r = "";
		for(int i = 0; i < length; i++) {
			r += (char)(Math.random() * 26 + 97);
		}
		return r;
	}

	public static String getCurrentDate(String dayFormat, TimeZone timeZone) {
		Date date = new Date();
		/* Specifying the format */
		DateFormat requiredFormat = new SimpleDateFormat(dayFormat);
		/* Setting the Timezone */
		requiredFormat.setTimeZone(timeZone);
		/* Picking the day value in the required Format */
		String strCurrentDay = requiredFormat.format(date).toUpperCase();
		return strCurrentDay;
	}

	public static int convertMonthInToDigitFormat(String monthFromUI){
		monthFromUI = monthFromUI.toLowerCase();
		int month = 0;
		switch (monthFromUI) {  
		case "jan":
			month=1;
			break;
		case "feb":
			month=2;
			break;
		case "mar":
			month=3;
			break;
		case "apr":
			month=4;
			break;
		case "may":
			month=5;
			break;
		case "jun":
			month=6;
			break;
		case "jul":
			month=7;
			break;
		case "aug":
			month=8;
			break;
		case "sep":
			month=9;
			break;
		case "oct":
			month=10;
			break;
		case "nov":
			month=11;
			break;
		case "dec":
			month=12;
			break;
		}
		return month;
	}

	public static String convertDBDateFormatToUIFormat(String DBDate){
		String UIMonth=null;
		String[] splittedDate = DBDate.split(" ");
		String date = (splittedDate[0].split("-")[2].charAt(0))=='0'?splittedDate[0].split("-")[2].split("0")[1]:splittedDate[0].split("-")[2];
		String month = (splittedDate[0].split("-")[1].charAt(0))=='0'?splittedDate[0].split("-")[1].split("0")[1]:splittedDate[0].split("-")[1];		
		String year = splittedDate[0].split("-")[0];		
		switch (Integer.parseInt(month)) {		
		case 1:
			UIMonth="January";
			break;
		case 2:
			UIMonth="February";
			break;
		case 3:
			UIMonth="March";
			break;
		case 4:
			UIMonth="April";
			break;
		case 5:
			UIMonth="May";
			break;
		case 6:
			UIMonth="June";
			break;
		case 7:
			UIMonth="July";
			break;
		case 8:
			UIMonth="August";
			break;
		case 9:
			UIMonth="September";
			break;
		case 10:
			UIMonth="October";
			break;
		case 11:
			UIMonth="November";
			break;
		case 12:
			UIMonth="December";
			break;		
		}

		return UIMonth+" "+date+", "+year;
	}

	/***
	 * This method take the current window handle
	 * 
	 * @param 
	 * @return current window handle
	 * 
	 */
	public static String getCurrentWindowHandle(){
		String currentWindowHandle = RFWebsiteDriver.driver.getWindowHandle();
		return currentWindowHandle;
	}

}
