package com.ibm.cics.minibank.trans;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ibm.cics.minibank.common.util.ContainerUtil;
import com.ibm.cics.minibank.common.util.IConstants;
import com.ibm.cics.server.Channel;
import com.ibm.cics.server.CommAreaHolder;

/**
 * OSGi program to do deposit
 */
public class Deposit extends Transaction implements ITransaction {

	/**
	 * @param args
	 */
	public static void main(CommAreaHolder cah) {
		System.out.println("Deposit is being invoked...");
		Deposit deposit = new Deposit();
		deposit.doTransaction(deposit);
		System.out.println("Deposit returns...");
	}

	@Override
	public void transactionLogic(Channel channel) {
		// get transaction data from containers
		String acctNum = ContainerUtil.getContainerData(channel, IConstants.TRAN_ACCTNM);
		String amount = ContainerUtil.getContainerData(channel, IConstants.TRAN_AMOUNT);

		// get the original balance, add up the amount and update the account with new balance  
		double value = (new Double(amount)).doubleValue();
		double balance = getAccountBalance(acctNum);
		int numUpd = this.setAccountBalance(acctNum, balance+value);
		// put the transaction status to return container
		ContainerUtil.putContainerData(channel, IConstants.TRAN_CODE, (new Integer(numUpd)).toString());
		// put the detail transaction message to return container
		String message = null;
		if ( numUpd > 0 ) {
			message = "Account balance for " + acctNum + " is updated. New balance is " + (balance+value);
		} else {
			message = "Failed to update balance for account " + acctNum;
		}
		ContainerUtil.putContainerData(channel, IConstants.TRAN_MSG, message);
		
		// write the transaction history record
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String txTime = formatter.format(new Date());
		addTranHistRecord(IConstants.TRAN_DEPOSIT, acctNum, (new Float(amount)).floatValue(), txTime);
	}

}
