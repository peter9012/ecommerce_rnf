///**
// * 
// */
package com.rf.core.utils;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DBUtil {
	private static final Logger logger = LogManager
			.getLogger(DBUtil.class.getName());


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
	
	public static String getConnectionString(String databaseName, String dbIP){
		if(databaseAuthentication.equalsIgnoreCase("SQL Server Authentication")){
			ConnectionString = "jdbc:sqlserver://"+dbIP+";databaseName="+databaseName+";instance=MSSQLSERVER;"+"domain="+databaseDomain;
		}
		else if(databaseAuthentication.equalsIgnoreCase("Windows Authentication")){
			ConnectionString = "jdbc:jtds:sqlserver://"+dbIP+";instance=MSSQLSERVER;domain="+databaseDomain+";integratedSecurity=true;DatabaseName="+databaseName;
		}
		return ConnectionString;
	}

	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> performDatabaseQuery(String sQuery,String databaseName){
		JdbcTemplate jdbcTemplate;
		jdbcTemplate = new JdbcTemplate(getDataSource(databaseName));
		logger.info("QUERY TRIGGERED IS "+sQuery+"\n");
		ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();		
		List<Map<String, Object>> userDataList = jdbcTemplate.query(sQuery,rowMapper);		
		return userDataList;
	}

	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> performDatabaseQuery(String sQuery,String databaseName, String dbIP2){
		JdbcTemplate jdbcTemplate;
		if(databaseName.trim().equalsIgnoreCase("RodanFieldsLive")){
			jdbcTemplate = new JdbcTemplate(getDataSource("RodanFieldsLive", dbIP2));
		}
		else{
			jdbcTemplate = new JdbcTemplate(getDataSource("RFOperations", dbIP2));
		}
		logger.info("QUERY TRIGGERED IS "+sQuery+"\n");
		ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();  
		List<Map<String, Object>> userDataList = jdbcTemplate.query(sQuery,rowMapper);  
		return userDataList;
	}

	public static DataSource getDataSource(String databaseName) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(Driver);
		dataSource.setUrl(getConnectionString(databaseName));
		dataSource.setUsername(databaseUserName);
		dataSource.setPassword(databasePassword);
		return dataSource;
	}

	public static DataSource getDataSource(String databaseName, String dbIP) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(Driver);
		System.out.println("DB Name "+databaseName);
		dataSource.setUrl(getConnectionString(databaseName, dbIP));
		dataSource.setUsername(databaseUserName);
		dataSource.setPassword(databasePassword);
		return dataSource;
	}

	@SuppressWarnings("unchecked")
	public static void performDatabaseQueryForUpdate(String sQuery,String databaseName){
		JdbcTemplate jdbcTemplate;
		jdbcTemplate = new JdbcTemplate(getDataSource(databaseName));
		logger.info("QUERY TRIGGERED IS "+sQuery+"\n");
		jdbcTemplate.execute(sQuery);
	}	
}
