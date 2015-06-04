package com.ibm.cics.savvy.entity;

import java.util.HashSet;
import java.util.Set;

import com.ibm.cics.savvy.util.IConstants;

/**
 * Class for the account info.
 * Used for data exchanging between jsp forms and actions.
 */
public class Account {
	private String accountNumber;
	private String customerID;
	private String balance;
	private String lastChangeTime;
	private Set<TransHistory> transHistories;
	
	public Account() {
	}
	
	/**
	 * Use the passed in string to construct Account object
	 */
	public Account(String acctInfo) {
		HashSet<TransHistory> tranHist = new HashSet<TransHistory>();
		initialize(acctInfo, tranHist);
	}
	
	/**
	 * Use the passed in string and transaction history records to construct Account object
	 */
	public Account(String acctInfo, Set<TransHistory> tranHist) {
		initialize(acctInfo, tranHist);
	}
	
	protected void initialize(String acctInfo, Set<TransHistory> tranHist) {
		String[] parms = acctInfo.split(IConstants.DATA_FIELD_SPLITTER);
		if ( parms.length >= 4 ) {
			accountNumber = parms[0];
			customerID = parms[1];
			balance = parms[2];
			lastChangeTime = parms[3];
		}
		
		setTransHistories(tranHist);
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getLastChangeTime() {
		return lastChangeTime;
	}

	public void setLastChangeTime(String lastChangeTime) {
		this.lastChangeTime = lastChangeTime;
	}

	public Set<TransHistory> getTransHistories() {
		return transHistories;
	}

	public void setTransHistories(Set<TransHistory> transHistories) {
		this.transHistories = transHistories;
	}

}
