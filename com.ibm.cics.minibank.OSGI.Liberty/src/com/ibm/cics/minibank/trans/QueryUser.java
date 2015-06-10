package com.ibm.cics.minibank.trans;

import java.util.ArrayList;

import com.ibm.cics.minibank.util.ContainerUtil;
import com.ibm.cics.minibank.util.DBUtil;
import com.ibm.cics.minibank.util.IConstants;
import com.ibm.cics.minibank.util.PropertiesUtil;
import com.ibm.cics.server.Channel;
import com.ibm.cics.server.CommAreaHolder;

/**
 * OSGi program to query user
 */
public class QueryUser extends Transaction implements ITransaction {

	/**
	 * @param args
	 */
	public static void main(CommAreaHolder cah) {
		System.out.println("QueryUser is being invoked...");
		QueryUser txQryUsr = new QueryUser();
		txQryUsr.doTransaction(txQryUsr);
		System.out.println("QueryUser returns...");
	}

	@Override
	public void transactionLogic(Channel channel) {
		// get transaction data from container
		String customerID = ContainerUtil.getContainerData(channel, IConstants.CUST_ID);
		
		// construct SQL command to query from customer table
		String sqlCmd = "SELECT * FROM " + PropertiesUtil.getPropertiesUtil().getTableCustomer() 
				+ " WHERE " + PropertiesUtil.getPropertiesUtil().getFieldCustID() + "='" + customerID + "'";
		// query info from the customer table
		ArrayList<String> queryList = DBUtil.getDBUtilInstance().execQuerySQL(sqlCmd);

		String userInfo = "not available";
		if ( queryList.size() > 0 ) {
			userInfo = queryList.get(0);
		}
		// put the customer info into the return container
		ContainerUtil.putContainerData(channel, IConstants.CUST_INFO, userInfo);
		
		// construct SQL command to query the customer related accounts info
		sqlCmd = "SELECT * FROM " + PropertiesUtil.getPropertiesUtil().getTableAccount()
				+ " WHERE " + PropertiesUtil.getPropertiesUtil().getFieldAcctCustID() + "='" + customerID + "'";
		// query info from the account table
		queryList = DBUtil.getDBUtilInstance().execQuerySQL(sqlCmd);
		// one user may have multiple accounts. Put all the account info into multiple return containers
		String acctRecord = null;
		for ( int i=0; i<queryList.size(); i++ ) {
			acctRecord = queryList.get(i);
			ContainerUtil.putContainerData(channel, IConstants.ACCT_LIST + i, acctRecord);
		}
		
	}

}
