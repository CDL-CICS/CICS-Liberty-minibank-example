package com.ibm.cics.minibank.trans;

import com.ibm.cics.server.Channel;

/**
 * interface for all transactions.
 * Different transaction must override the abstract method transactionLogic to perform its own business logic
 */
public interface ITransaction {
	
	/**
	 * Different transaction must override transactionLogic to perform its own business logic
	 */
	public void transactionLogic(Channel channel);

}
