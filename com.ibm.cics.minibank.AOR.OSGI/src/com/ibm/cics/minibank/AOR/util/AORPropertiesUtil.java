package com.ibm.cics.minibank.AOR.util;

import java.util.ResourceBundle;

/**
 * Utility class to read the properties 
 */
public class AORPropertiesUtil {
	private static AORPropertiesUtil propertyUtil = null;
	private ResourceBundle propertyBundle = ResourceBundle
			.getBundle("properties/MinibankAOR");

	// If connection to DB2 available
	private boolean connectToDB2 = false;
	// DB2 table names 
	private String tableCustomer = "";
	private String tableAccount = "";
	private String tableTranHist = "";
	// Field names of table TABLE_CUSTOMER 
	private String fieldCustID = "";
	private String fieldCustName = "";
	private String fieldCustGender = "";
	private String fieldCustAge = "";
	private String fieldCustAddress = "";
	// Field names of table TABLE_ACCOUNT 
	private String fieldAcctNummber = "";
	private String fieldAcctCustID = "";
	private String fieldAcctBalance = "";
	private String fieldAcctLastChange = "";
	// Field names of table TABLE_TRANHIST 
	private String fieldHistTranName ="";
	private String fieldHistAcctNum = "";
	private String fieldHistAmount = "";
	private String fieldHistTime = "";
	
	public static AORPropertiesUtil getPropertiesUtil() {
		if ( propertyUtil == null ) {
			propertyUtil = new AORPropertiesUtil();
		}
		return propertyUtil;
	}

	public boolean isConnectToDB2() {
		return connectToDB2;
	}

	public String getTableCustomer() {
		return tableCustomer;
	}

	public String getTableAccount() {
		return tableAccount;
	}

	public String getTableTranHist() {
		return tableTranHist;
	}

	public String getFieldCustID() {
		return fieldCustID;
	}

	public String getFieldCustName() {
		return fieldCustName;
	}

	public String getFieldCustGender() {
		return fieldCustGender;
	}

	public String getFieldCustAge() {
		return fieldCustAge;
	}

	public String getFieldCustAddress() {
		return fieldCustAddress;
	}

	public String getFieldAcctNummber() {
		return fieldAcctNummber;
	}

	public String getFieldAcctCustID() {
		return fieldAcctCustID;
	}

	public String getFieldAcctBalance() {
		return fieldAcctBalance;
	}

	public String getFieldAcctLastChange() {
		return fieldAcctLastChange;
	}

	public String getFieldHistTranName() {
		return fieldHistTranName;
	}

	public String getFieldHistAcctNum() {
		return fieldHistAcctNum;
	}

	public String getFieldHistAmount() {
		return fieldHistAmount;
	}

	public String getFieldHistTime() {
		return fieldHistTime;
	}

	private AORPropertiesUtil() {
		// If connection to DB2 available
		connectToDB2 = (new Boolean(getProperty("ConnectToDB2"))).booleanValue();
		// DB2 table names 
		tableCustomer = getProperty("TABLE_CUSTOMER");
		tableAccount = getProperty("TABLE_ACCOUNT");
		tableTranHist = getProperty("TABLE_TRANHIST");
		// Field names of table TABLE_CUSTOMER 
		fieldCustID = getProperty("FIELD_CUST_ID");
		fieldCustName = getProperty("FIELD_CUST_NAME");
		fieldCustGender = getProperty("FIELD_CUST_GENDER");
		fieldCustAge = getProperty("FIELD_CUST_AGE");
		fieldCustAddress = getProperty("FIELD_CUST_ADDRESS");
		// Field names of table TABLE_ACCOUNT 
		fieldAcctNummber = getProperty("FIELD_ACCT_NUMBER");
		fieldAcctCustID = getProperty("FIELD_ACCT_CUSTID");
		fieldAcctBalance = getProperty("FIELD_ACCT_BALANCE");
		fieldAcctLastChange = getProperty("FIELD_ACCT_LASTCHANGE");
		// Field names of table TABLE_TRANHIST 
		fieldHistTranName = getProperty("FIELD_HIST_TRANNAME");
		fieldHistAcctNum = getProperty("FIELD_HIST_ACCTNUM");
		fieldHistAmount = getProperty("FIELD_HIST_AMOUNT");
		fieldHistTime = getProperty("FIELD_HIST_TIME");
	}
	
	protected String getProperty(String key) {
		return propertyBundle.getString(key);
	}
}
