package com.ibm.cics.minibank.util;

import java.util.ResourceBundle;

import com.ibm.cics.minibank.util.WORPropertiesUtil;

/**
 * Utility class to read the properties 
 */
public class WORPropertiesUtil {
	private static WORPropertiesUtil propertyUtil = null;
	private ResourceBundle propertyBundle = ResourceBundle
			.getBundle("properties/MinibankWOR");

	private boolean linkToAOR = false;
	private boolean linkToLocal = false;
	private String progCreateUser = "";
	private String progQueryUser = "";
	private String progCreateAcct = "";
	private String progQueryAcct = "";
	private String progDeposit = "";
	private String progWithdraw = "";
	private String progTransfer = "";	
	
	public static WORPropertiesUtil getPropertiesUtil() {
		if ( propertyUtil == null ) {
			propertyUtil = new WORPropertiesUtil();
		}
		return propertyUtil;
	}

	public boolean isLinkToAOR() {
		return linkToAOR;
	}
	
	public boolean isLinkToLocal() {
		return linkToLocal;
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

	private WORPropertiesUtil() {
		// TODO Auto-generated constructor stub
		linkToAOR = (new Boolean(getProperty("LinkToAOR"))).booleanValue();
		linkToLocal = (new Boolean(getProperty("LinkToLocal"))).booleanValue();
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
