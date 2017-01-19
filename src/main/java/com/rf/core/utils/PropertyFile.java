package com.rf.core.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyFile {

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
		System.out.println("key="+key);
		System.out.println("value="+value);
		System.out.println("file="+fileName);
		 try {
				outputStream = new FileOutputStream(path+fileName);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		setProperty(key, value);
		try {
			props.store(outputStream, null);
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
