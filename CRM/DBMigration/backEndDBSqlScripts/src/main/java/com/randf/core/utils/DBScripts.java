package com.randf.core.utils;

public class DBScripts {
	
	public static String ACCOUNT_DIFFERENCE = "SELECT * FROM CRM.SFDC.AccountDifference";
	public static String ACCOUNTS_MISSING = "SELECT * FROM CRM.SFDC.AccountsMissing";
	public static String ACCOUNTS_DUPLICATES = "SELECT * FROM CRM.SFDC.Accounts_Dups";
	public static String BUSINESS_RULE_FAILURE = "SELECT * FROM CRM.SFDC.BusinessRuleFailure";
	public static String ERROR_LOG_ACCOUNTS = "SELECT * FROM CRM.SFDC.ErrorLog_Accounts";
	public static String CRM_METADATA = "SELECT * FROM CRM.SFDC.CRM_METADATA";
	
}
