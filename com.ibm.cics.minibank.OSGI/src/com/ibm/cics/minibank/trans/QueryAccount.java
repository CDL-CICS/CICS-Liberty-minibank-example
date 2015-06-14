package com.ibm.cics.minibank.trans;

import java.util.ArrayList;

import com.ibm.cics.minibank.common.util.ContainerUtil;
import com.ibm.cics.minibank.AOR.util.AORDBUtil;
import com.ibm.cics.minibank.common.util.IConstants;
import com.ibm.cics.minibank.AOR.util.AORPropertiesUtil;
import com.ibm.cics.server.Channel;
import com.ibm.cics.server.CommAreaHolder;

/**
 * OSGi program to query account
 */
public class QueryAccount extends Transaction implements ITransaction {

	/**
	 * @param args
	 */
	public static void main(CommAreaHolder cah ) {
		System.out.println("QueryAccount is being invoked...");
		QueryAccount txQryAcct = new QueryAccount();
		txQryAcct.doTransaction(txQryAcct);
		System.out.println("QueryAccount returns...");
	}

	@Override
	public void transactionLogic(Channel channel) {
		// get transaction data from containers
		String acctNum = ContainerUtil.getContainerData(channel, IConstants.ACCT_NUMBER);

		// construct the SQL command to query account info
		String sqlCmd = "SELECT * FROM " + AORPropertiesUtil.getPropertiesUtil().getTableAccount() 
				+ " WHERE " + AORPropertiesUtil.getPropertiesUtil().getFieldAcctNummber() + "='" + acctNum + "'";
		// query from the database table
		ArrayList<String> queryList = AORDBUtil.getDBUtilInstance().execQuerySQL(sqlCmd);
		String acctInfo = "not available";
		if ( queryList.size() > 0 ) {
			acctInfo = queryList.get(0);
		}
		// put the account info into return containers
		ContainerUtil.putContainerData(channel, IConstants.ACCT_INFO, acctInfo);
		
		// construct the SQL command to query transaction history records
		sqlCmd = "SELECT * FROM " + AORPropertiesUtil.getPropertiesUtil().getTableTranHist()
				+ " WHERE " + AORPropertiesUtil.getPropertiesUtil().getFieldHistAcctNum() + "='" + acctNum + "'";
		// query from the database table
		queryList = AORDBUtil.getDBUtilInstance().execQuerySQL(sqlCmd);
		// put all the transaction history records into multiple return containers
		String histRecord = null;
		for ( int i=0; i<queryList.size(); i++ ) {
			histRecord = queryList.get(i);
			ContainerUtil.putContainerData(channel, IConstants.HIST_LIST + i, histRecord);
		}
	}

}
