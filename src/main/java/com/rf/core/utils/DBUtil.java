///**
// * 
// */
package com.rf.core.utils;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


public class DBUtil {

	private static final String Driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static String ConnectionString = null;
	private static String dbUsername = null;
	private static String dbPassword = null;


	public static void setDBDetails(String dbIP,String dbUser,String dbPass,String dbDomain,String databaseName,String authentication){
		if(authentication.equalsIgnoreCase("SQL Server Authentication")){
			ConnectionString = "jdbc:sqlserver://"+dbIP+";databaseName="+databaseName+";instance=MSSQLSERVER;"+"domain="+dbDomain;
		}
		else if(authentication.equalsIgnoreCase("Windows Authentication")){
			ConnectionString = "jdbc:jtds:sqlserver://"+dbIP+";instance=MSSQLSERVER;domain="+dbDomain+";integratedSecurity=true;DatabaseName="+databaseName;
		}
		dbUsername = dbUser;
		dbPassword = dbPass;
	}

	public static String getConnectionString(){
		return ConnectionString;
	}

	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> performDatabaseQuery(String sQuery){
		JdbcTemplate jdbcTemplate;
		jdbcTemplate = new JdbcTemplate(getDataSource());
		System.out.println("query is "+sQuery);
		ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();		
		List<Map<String, Object>> userDataList = jdbcTemplate.query(sQuery,rowMapper);		
		return userDataList;
	}

	public static DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(Driver);
		dataSource.setUrl(ConnectionString);
		dataSource.setUsername(dbUsername);
		dataSource.setPassword(dbPassword);
		return dataSource;
	}
	
//	public static void main(String[] args){
//		List<Map<String, Object>> mapList =  null;
//		setDBDetails("10.223.176.9", "smathur", "111Reverse", "RF", "RodanFieldsLive", "Windows Authentication");
//		mapList = performDatabaseQuery("select top 1 * from dbo.orders");
//		System.out.println(mapList.size());
//	}
}
