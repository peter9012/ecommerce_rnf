/*
 * @Author ::: Mayuri Edhara
 * 
 */
package com.randf.crm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.randf.core.utils.DBScripts;
import com.randf.core.utils.DBUtil;
import com.randf.core.utils.PropertyFile;

public class ExecuteBackUpDataScripts {
	
	/* execute this class file with the command prompt from below.
	 * Go to the path where the project is located. mine is C:\Users\medhara\CRMWS\backEndDBSqlScripts>
	 * 1. mvn clean 
	 * 2. mvn compile then 
	 * 3. mvn exec:java -Dexec.mainClass="com.randf.crm.ExecuteBackUpDataScripts" -Dexec.args="2016-02-03"
	 **/
	
	static String crmDBDefaultProps = "crmDBDefaultEnv.properties";
	
	@SuppressWarnings("resource")
	public static void main(String args[]){	

		PropertyFile propFile = new PropertyFile();
		final String Driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	    Connection connection = null;	
	    CallableStatement callableStmt = null;
	    Statement statement = null;
	    ResultSet rs = null;
	    
	    String url = null; 
		String authentication = null;
		String dbIP = null;
		String dbUsername = null;
		String dbPassword = null;
		String dbDomain = null;
		String databaseName = null;
		String result = null;
		String LastRunDate = null;
	    
	    try {	    	
	    	
	    	propFile.loadProps(crmDBDefaultProps);
	    	
			dbIP = propFile.getProperty("dbIP");
			dbUsername = propFile.getProperty("dbUsername");
			dbPassword = propFile.getProperty("dbPassword");
			dbDomain = propFile.getProperty("dbDomain");
			authentication = propFile.getProperty("authentication");
			databaseName = propFile.getProperty("databaseName");
			DBUtil.setDBDetails(dbIP, dbUsername, dbPassword, dbDomain, authentication);
			System.out.println("DB connections from file are set");	
			
			Class.forName(Driver);
	    	url = DBUtil.getConnectionString(databaseName);
	    	connection = DriverManager.getConnection(url,dbUsername,dbPassword); 
	    	System.out.println("Connection String opened");
            
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date d = formatter.parse(args[0]);
            ((SimpleDateFormat) formatter).applyPattern("yyyy-MM-dd hh:mm:ss.SSS");
            String parsedDate = formatter.format(d);
            Timestamp timestamp = Timestamp.valueOf(parsedDate);
            LastRunDate = timestamp.toString();
            
            System.out.println("Executing the Stored Procedure :: VerifyAccountMigration ::");
            callableStmt = connection.prepareCall("{call VerifyAccountMigration(?)}");
            callableStmt.setString("LastRunDate", LastRunDate);
            // callableStmt.setString("LastRunDate", "2016-01-03 08:29:52.000");
            callableStmt.execute();
            rs = callableStmt.getResultSet();

            while (rs.next()) {
            	result = rs.getString(1);
            	System.out.println("Get Result Set ::: " + result);                    
             }
            
            statement = connection.createStatement();
            DatabaseMetaData meta = connection.getMetaData();
            
            StringBuffer sb = new StringBuffer();
            
            if(result.contains(CRMConstants.AccountDiff)){
            	rs = statement.executeQuery(DBScripts.ACCOUNT_DIFFERENCE);
            	while (rs.next()) {
            		String RFO_Accounts = rs.getString("RFO_Accounts");
            		String CRM_Accounts = rs.getString("CRM_Accounts");
            		String diff = rs.getString("Difference");
	            	System.out.println("Get Result Set for "+CRMConstants.AccountDiff +" ::: "+ 
	            			"RFO_Accounts -- "  + RFO_Accounts +
	            			"CRM_Accounts -- " + CRM_Accounts +
	            			"difference in accounts " + diff ); 
            	  }
            }if (result.contains(CRMConstants.AccountMissing)){
            	rs = statement.executeQuery(DBScripts.ACCOUNTS_MISSING);
            	while (rs.next()) {
            		 
	            	/*
	            	 * Append the above rs to a string sb.append(rs)
	            	 */
            	}
            }if (result.contains(CRMConstants.AccountDups)){
            	rs = statement.executeQuery(DBScripts.ACCOUNTS_DUPLICATES);
            	while (rs.next()) {
	            	
	            	/*
	            	 * Append the above rs to a string sb.append(rs)
	            	 */
            	}
            }if (result.contains(CRMConstants.BusinessRuleFailure)){            	 
            	ResultSet res = meta.getTables(null, null, CRMConstants.BusinessRuleFailure, null); 
            	if(!res.next()){ 
            	  System.out.println(CRMConstants.BusinessRuleFailure + " table does not exists");
            	} else{
            		System.out.println(CRMConstants.BusinessRuleFailure + " table exists");
            		rs = statement.executeQuery(DBScripts.BUSINESS_RULE_FAILURE);
                	while (rs.next()) {
    	            	
    	            	/*
    	            	 * Append the above rs to a string sb.append(rs)
    	            	 */
                	}            	
            	}
            }if (result.contains(CRMConstants.ErrorLogAccts)){
            	ResultSet res = meta.getTables(null, null, CRMConstants.ErrorLogAccts, null); 
            	if(!res.next()){ 
            	  System.out.println(CRMConstants.ErrorLogAccts + " table does not exists");
            	} else{
            		System.out.println(CRMConstants.ErrorLogAccts + " table exists");
            		rs = statement.executeQuery(DBScripts.ERROR_LOG_ACCOUNTS);
                	while (rs.next()) {
    	            	
    	            	/*
    	            	 * Append the above rs to a string sb.append(rs)
    	            	 */
                	}            	
            	}
            }if (result.contains(CRMConstants.CrmMedata)){
            	rs = statement.executeQuery(DBScripts.CRM_METADATA);
            	while (rs.next()) {
	            	
	            	/*
	            	 * Append the above rs to a string sb.append(rs)
	            	 */
            	}
            }   
            
             /*
              * Get result + sb.toString(); to get the final result and send the result out to the mail. 
              * or send independent emails of each of the tables separately by calling the below sendMail method
              * in each of the above.
              */
            
            SendMail.sendMail(result);
           
            System.out.println("Execution of dbScripts complete ::: "); 
            
            
	    } catch (ClassNotFoundException e) {	            
            e.printStackTrace();
        } catch (SQLException e) {	            
            e.printStackTrace();
        } catch (Exception e) {
        	 e.printStackTrace();
        } finally {
        	try {
        		rs.close();
        		callableStmt.close();        		
        		connection.close();        		
        	} catch (Exception e){
        		e.printStackTrace();
        	}
        }	       
	}
}
