package com.ibm.cics.savvy.util;

import java.util.ResourceBundle;

/**
 * Utility class to read the properties 
 */
public class PropertiesUtil {
	private static PropertiesUtil propertyUtil = null;
	private ResourceBundle propertyBundle = ResourceBundle
			.getBundle("properties/SavvyWeb");

	private boolean linkToAOR = false;
	private String progCreateUser = "";
	private String progQueryUser = "";
	private String progCreateAcct = "";
	private String progQueryAcct = "";
	private String progDeposit = "";
	private String progWithdraw = "";
	private String progTransfer = "";	
	
	public static PropertiesUtil getPropertiesUtil() {
		if ( propertyUtil == null ) {
			propertyUtil = new PropertiesUtil();
		}
		return propertyUtil;
	}

	public boolean isLinkToAOR() {
		return linkToAOR;
	}

	public String getProgCreateUser() {
		return progCreateUser;
	}

	public String getProgQueryUser() {
		return progQueryUser;
	}

	public String getProgCreateAcct() {
		return progCreateAcct;
	}

	public String getProgQueryAcct() {
		return progQueryAcct;
	}

	public String getProgDeposit() {
		return progDeposit;
	}

	public String getProgWithdraw() {
		return progWithdraw;
	}

	public String getProgTransfer() {
		return progTransfer;
	}

	private PropertiesUtil() {
		// TODO Auto-generated constructor stub
		linkToAOR = (new Boolean(getProperty("LinkToAOR"))).booleanValue();
		progCreateUser = getProperty("PROG_CREATE_USER");
		progQueryUser = getProperty("PROG_QUERY_USER");
		progCreateAcct = getProperty("PROG_CREATE_ACCT");
		progQueryAcct = getProperty("PROG_QUERY_ACCT");
		progDeposit = getProperty("PROG_DEPOSIT");
		progWithdraw = getProperty("PROG_WITHDRAW");
		progTransfer = getProperty("PROG_TRANSFER");	
	}
	
	
	protected String getProperty(String key) {
		return propertyBundle.getString(key);
	}
}
