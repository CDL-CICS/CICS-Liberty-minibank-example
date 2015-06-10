package com.ibm.cics.minibank.trans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.ibm.cics.minibank.entity.Account;
import com.ibm.cics.minibank.entity.TransHistory;
import com.ibm.cics.minibank.util.ContainerUtil;
import com.ibm.cics.minibank.util.DBUtil;
import com.ibm.cics.minibank.util.IConstants;
import com.ibm.cics.minibank.util.PropertiesUtil;
import com.ibm.cics.server.Channel;
import com.ibm.cics.server.CommAreaHolder;

/**
 * OSGi program to query account
 */
public class QueryAccount extends Transaction {


	public Account transactionLogic(String acctNum) {
		// get transaction data from containers
		
		Account returnAccount = new Account();
		Set<TransHistory> transHistories = new HashSet<TransHistory>();

		// construct the SQL command to query account info
		String sqlCmd = "SELECT * FROM " + PropertiesUtil.getPropertiesUtil().getTableAccount() 
				+ " WHERE " + PropertiesUtil.getPropertiesUtil().getFieldAcctNummber() + "='" + acctNum + "'";
		// query from the database table
		ArrayList<String> queryList = DBUtil.getDBUtilInstance().execQuerySQL(sqlCmd);
		String acctInfo = "not available";
		if ( queryList.size() > 0 ) {
			acctInfo = queryList.get(0);
		}
		
		// construct the SQL command to query transaction history records
		sqlCmd = "SELECT * FROM " + PropertiesUtil.getPropertiesUtil().getTableTranHist()
				+ " WHERE " + PropertiesUtil.getPropertiesUtil().getFieldHistAcctNum() + "='" + acctNum + "'";
		// query from the database table
		queryList = DBUtil.getDBUtilInstance().execQuerySQL(sqlCmd);
		// put all the transaction history records into multiple return containers
		String histRecord = null;
		for ( int i=0; i<queryList.size(); i++ ) {
			histRecord = queryList.get(i);
			
			// create a transaction history record object and put it into set
			TransHistory tranRecord = new TransHistory(histRecord);
			transHistories.add(tranRecord);
		}
		
		return returnAccount;
	}

}
