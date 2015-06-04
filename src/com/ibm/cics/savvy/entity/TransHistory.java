package com.ibm.cics.savvy.entity;

import com.ibm.cics.savvy.util.IConstants;

/**
 * Class for the transaction history records.
 * Used for data exchanging between jsp forms and actions
 */
public class TransHistory {
	private String transName;
	private String accountNum;
	private String transAmount;
	private String transTime;
	
	public TransHistory() {
	}
	
	/**
	 * Use the passed in string to construct TransHistory object
	 */
	public TransHistory(String histRecord) {
		String[] parms = histRecord.split(IConstants.DATA_FIELD_SPLITTER);
		if ( parms.length >= 4 ) {
			transName = parms[0];
			accountNum = parms[1];
			transAmount = parms[2];
			transTime = parms[3];
		}
	}

	public String getTransName() {
		return transName;
	}

	public void setTransName(String transName) {
		this.transName = transName;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

}
