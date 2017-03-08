package com.rf.core.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rf.pages.website.rehabitat.storeFront.StoreFrontHomePage;

public class PropertyFile {
	private static final Logger logger = LogManager
			.getLogger(PropertyFile.class.getName());
	
	public String path = "src/test/resources/environments/";
	private final Properties props = new Properties();
	private FileOutputStream outputStream = null;

	public void loadProps(String fileName) {
		try {
			FileInputStream inputStream = new FileInputStream(path+fileName);
			try {
				props.load(inputStream);
			} catch (IOException e) {
				e.getMessage();
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.getMessage();
			e.printStackTrace();
		}		
	}

	public String getProperty(String key) {
		return props.getProperty(key);
	}

	public void setProperty(String key, String value) {
		props.setProperty(key, value);
	}

	public void storeProperty(FileOutputStream out) {
		try {
			props.store(out, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setAndWriteProperty(String key, String value,String fileName) {
		try {
			outputStream = new FileOutputStream(path+fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setProperty(key, value);
		try {
			props.store(outputStream, null);
			logger.info("key = "+key+" with value ="+value+" stored in "+fileName);
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void clearProperty(){
		props.clear();
	}

}
