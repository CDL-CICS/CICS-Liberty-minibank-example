/**
 * 
 */
package com.ibm.cics.minibank.trans;

import java.util.ArrayList;

import com.ibm.cics.minibank.util.DBUtil;
import com.ibm.cics.minibank.util.IConstants;
import com.ibm.cics.minibank.util.PropertiesUtil;
import com.ibm.cics.server.Channel;
import com.ibm.cics.server.Task;

/**
 * Bank transaction base class
 * It implements basic methods common to all transaction types
 */
public abstract class Transaction implements ITransaction {

	public void doTransaction(ITransaction tran) {
		Task task = Task.getTask();
		Channel channel = task.getCurrentChannel();
		if (channel != null) {
			tran.transactionLogic(channel);
		} else {
			System.out.println("There is no Current Channel");
		}
	}

	/**
	 * Get balance of an account
	 */
	protected double getAccountBalance(String acctNum) {
		String balance = "0";
		
		String sqlCmd = "SELECT "
				+ PropertiesUtil.getPropertiesUtil().getFieldAcctBalance()
				+ " FROM "
				+ PropertiesUtil.getPropertiesUtil().getTableAccount()
				+ " WHERE "
				+ PropertiesUtil.getPropertiesUtil().getFieldAcctNummber()
				+ "='" + acctNum + "'";
		ArrayList<String> queryList = DBUtil.getDBUtilInstance().execQuerySQL(sqlCmd);
		if ( queryList.size() > 0 ) {
			String record = queryList.get(0);
			if ( record.contains(IConstants.DATA_FIELD_SPLITTER) ) {
				String[] items = record.split(IConstants.DATA_FIELD_SPLITTER);
				balance = items[0];
			} else {
				balance = record;
			}
		}
		
		double value = (new Double(balance)).doubleValue(); 
		return value;
	}

	/**
	 * Update an account balance
	 */
	protected int setAccountBalance(String acctNum, double newBalance) {

		String sqlCmd = "UPDATE "
				+ PropertiesUtil.getPropertiesUtil().getTableAccount()
				+ " SET "
				+ PropertiesUtil.getPropertiesUtil().getFieldAcctBalance()
				+ "='" + newBalance + "' WHERE "
				+ PropertiesUtil.getPropertiesUtil().getFieldAcctNummber()
				+ "='" + acctNum + "'";
		int numUpd = DBUtil.getDBUtilInstance().execUpdateSQL(sqlCmd);
		return numUpd;
	}

	/**
	 * Add a transaction record into transaction history table
	 */
	protected int addTranHistRecord(String tranName, String acctNum, float amount, String txTime) {
		int numUpd = 0;
		String sqlCmd = "INSERT INTO " + PropertiesUtil.getPropertiesUtil().getTableTranHist() + "("
				+ PropertiesUtil.getPropertiesUtil().getFieldHistTranName() + ", "
				+ PropertiesUtil.getPropertiesUtil().getFieldHistAcctNum() + ", "
				+ PropertiesUtil.getPropertiesUtil().getFieldHistAmount() + ", "
				+ PropertiesUtil.getPropertiesUtil().getFieldHistTime()
				+ ") VALUES("
				+ "'" + tranName + "', "
				+ "'" + acctNum + "', "
				+ amount + ", "
				+ "'" + txTime + "'"
				+ ")";
		numUpd = DBUtil.getDBUtilInstance().execUpdateSQL(sqlCmd);
		return numUpd;
	}

}
