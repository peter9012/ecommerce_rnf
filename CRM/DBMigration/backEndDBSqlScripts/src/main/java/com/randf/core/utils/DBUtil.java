package com.randf.core.utils;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DBUtil {

	
	private static final String Driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static String ConnectionString = null;
	private static String databaseIP = null;
	private static String databaseUserName = null;
	private static String databasePassword = null;
	private static String databaseDomain= null;
	private static String databaseAuthentication = null;



	public static void setDBDetails(String dbIP,String dbUser,String dbPass,String dbDomain,String authentication){
		databaseIP = dbIP;
		databaseUserName = dbUser;
		databasePassword = dbPass;
		databaseDomain = dbDomain;
		databaseAuthentication = authentication;		
	}

	public static String getConnectionString(String databaseName){
		if(databaseAuthentication.equalsIgnoreCase("SQL Server Authentication")){
			ConnectionString = "jdbc:sqlserver://"+databaseIP+";databaseName="+databaseName+";instance=MSSQLSERVER;"+"domain="+databaseDomain;
		}
		else if(databaseAuthentication.equalsIgnoreCase("Windows Authentication")){
			ConnectionString = "jdbc:jtds:sqlserver://"+databaseIP+";instance=MSSQLSERVER;domain="+databaseDomain+";integratedSecurity=true;DatabaseName="+databaseName;
		}
		return ConnectionString;
	}

	public static DataSource getDataSource(String databaseName) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(Driver);
		dataSource.setUrl(getConnectionString(databaseName));
		dataSource.setUsername(databaseUserName);
		dataSource.setPassword(databasePassword);
		return dataSource;
	}
}

