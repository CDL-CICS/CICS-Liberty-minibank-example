package com.ibm.cics.minibank.trans;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ibm.cics.minibank.common.util.ContainerUtil;
import com.ibm.cics.minibank.common.util.IConstants;
import com.ibm.cics.server.Channel;
import com.ibm.cics.server.CommAreaHolder;

/**
 * OSGi program to do withdraw
 */
public class Withdraw extends Transaction implements ITransaction {

	/**
	 * @param args
	 */
	public static void main(CommAreaHolder cah) {
		System.out.println("Withdraw is being invoked...");
		Withdraw withdraw = new Withdraw();
		withdraw.doTransaction(withdraw);
		System.out.println("Withdraw returns...");
	}

	@Override
	public void transactionLogic(Channel channel) {
		// get transaction data from containers
		String acctNum = ContainerUtil.getContainerData(channel, IConstants.TRAN_ACCTNM);
		String amount = ContainerUtil.getContainerData(channel, IConstants.TRAN_AMOUNT);

		// get the original balance
		int numUpd = 0;
		String message = null;
		double value = (new Double(amount)).doubleValue();
		double balance = getAccountBalance(acctNum);
		// check if the original balance is enough for the withdraw amount
		if ( value > balance ) {
			// no enough balance
			numUpd = 0;
		} else {
			// do the calculation, and update the account with new balance
			numUpd = this.setAccountBalance(acctNum, balance-value);
		}
		// put the transaction status into the return container
		ContainerUtil.putContainerData(channel, IConstants.TRAN_CODE, (new Integer(numUpd)).toString());
		// put the detail transaction message into the return container
		if ( numUpd > 0 ) {
			message = "Account balance for " + acctNum + " is updated. New balance is " + (balance-value);
		} else {
			if ( value > balance ) {
				message = "No enough balance. Current balance is " + balance + ". Failed to withdraw for account " + acctNum;
			} else {
				message = "Failed to update balance for account " + acctNum;
			}
		}
		ContainerUtil.putContainerData(channel, IConstants.TRAN_MSG, message);
		
		// write the transaction history record
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String txTime = formatter.format(new Date());
		addTranHistRecord(IConstants.TRAN_WITHDRAW, acctNum, (new Float(amount)).floatValue(), txTime);
	}

}
