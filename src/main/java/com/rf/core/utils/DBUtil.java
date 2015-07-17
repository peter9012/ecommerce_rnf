///**
// * 
// */
package com.rf.core.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DBUtil {


	//	private Config config;
	// To do - Put in config file
	private static Connection con;
	private static final String Driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static String ConnectionString = null;

	static {
		try {
			Class.forName(Driver);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * Purpose: To Make connection to DB
	 * 
	 */
	public static Connection initDatabaseProvider() throws SQLException
	{
		if(null==con || con.isClosed())
		{
			con= DriverManager.getConnection(getConnectionString());
		}
		return con;
	}


	/**
	 * 
	 * Purpose: Checks the state of Connection
	 */
	public static boolean isConnectedToDB() throws SQLException
	{
		boolean IsConncted=false;
		if(!con.isClosed()){
			IsConncted=true;
		}
		return IsConncted;
	}

	/**
	 * 
	 * Purpose: Get ResultSet from DB 
	 *
	 */
	public static ResultSet performSelectQuery(String sQuery) throws SQLException
	{
		Connection con = initDatabaseProvider();
		ResultSet rs;
		PreparedStatement st = con.prepareStatement(sQuery);
		rs = st.executeQuery();		
		return rs;
	}

	/**
	 * 
	 * Purpose: Perform Update context in DB
	 * 
	public static void performUpdateQuery(String query) throws SQLException {
		Connection con = initDatabaseProvider();
		//ResultSet rs;
		PreparedStatement st = con.prepareStatement(query);
		st.executeUpdate();
	}

	/**
	 * 
	 * Purpose: Close the Connection
	 *
	 */
	public static void closeConnection() 
	{
		if(con!=null)
		{
			try {
				con.close();
			} catch (SQLException e) {

			}
		}
	}
	
	public static void setConnectionString(String dbIP,String dbUsername,String dbPassword,String dbDomain,String databaseName){
		ConnectionString = "jdbc:jtds:sqlserver://"+dbIP+";instance=MSSQLSERVER;user="+dbUsername+";password="+dbPassword+";domain="+dbDomain+";integratedSecurity=true;DatabaseName="+databaseName;	
	}
	
	public static String getConnectionString(){
		return ConnectionString;
	}

}
