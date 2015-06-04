package com.ibm.cics.savvy.action;

import java.util.HashMap;

import com.ibm.cics.savvy.util.IConstants;
import com.ibm.cics.savvy.util.TransUtil;
import com.ibm.cics.savvy.util.TransferCommarea;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Action class for transaction: deposit, withdraw and transfer
 */
public class TransManagementAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String sourceAcct;
	private String targetAcct;
	private String amount;
	
	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		return super.execute();
	}

	public String toDeposit() {
		return SUCCESS;
	}

	public String doDeposit() {
		String message = null;
		boolean inputCorrect = true;

		try {
			Double.parseDouble(amount);
			inputCorrect = true;
		} catch (NumberFormatException e) {
			inputCorrect = false;
			message = "The amount is not a number. Fail to deposit.";
		}
		
		if ( inputCorrect ) {
			HashMap<String, String> containerData = new HashMap<String, String>();
			containerData.put(IConstants.TRAN_ACCTNM, sourceAcct);
			containerData.put(IConstants.TRAN_AMOUNT, amount);
			// invoke the delegator method in the TransUtil object
			String[] result = TransUtil.getTranUtil().deposit(containerData);
			if ( (new Integer(result[0])).intValue() > 0 ) {
				// success
				this.addActionMessage(result[1]);
			} else {
				// got problems
				this.addActionError(result[1]);
			}
		} else {
			// input incorrect parameters
			this.addActionError(message);
		}

		return SUCCESS;
	}

	public String toWithDraw() {
		return SUCCESS;
	}

	public String doWithDraw() {
		String message = null;
		boolean inputCorrect = true;

		try {
			Double.parseDouble(amount);
			inputCorrect = true;
		} catch (NumberFormatException e) {
			inputCorrect = false;
			message = "The amount is not a number. Fail to withdraw.";
		}
		
		if ( inputCorrect ) {
			HashMap<String, String> containerData = new HashMap<String, String>();
			containerData.put(IConstants.TRAN_ACCTNM, sourceAcct);
			containerData.put(IConstants.TRAN_AMOUNT, amount);
			// invoke delegator method in the TransUtil object
			String[] result = TransUtil.getTranUtil().withdraw(containerData);
			if ( (new Integer(result[0])).intValue() > 0 ) {
				// success
				this.addActionMessage(result[1]);
			} else {
				// got problems
				this.addActionError(result[1]);
			}
		} else {
			// input incorrect parameters
			this.addActionError(message);
		}

		return SUCCESS;
	}

	public String toTransfer() {
		return SUCCESS;
	}

	public String doTransfer() {
		String message = null;
		boolean inputCorrect = true;

		try {
			Double.parseDouble(amount);
			inputCorrect = true;
		} catch (NumberFormatException e) {
			inputCorrect = false;
			message = "The amount is not a number. Fail to transfer.";
		}
		
		if ( inputCorrect ) {
			// construct commarea
			TransferCommarea commarea = new TransferCommarea();
			commarea.setSourceAccount(sourceAcct);
			commarea.setTargetAccount(targetAcct);
			commarea.setAmount(amount);
			// invokde the delegator method in the TransUtil object
			TransUtil.getTranUtil().transfer(commarea);
			// get the return info from commarea
			String tranCode = commarea.getTranResult();
			String tranMessage = commarea.getTranMessage();
			
			// parse the return info in the commarea
			if ( "0".equals(tranCode) ) {
				// success
				//String sourceAcctBalance = commarea.getSourceAcctBalance();
				//String targetAcctBalance = commarea.getTargetAcctBalance();
				//message = tranMessage + " New balance of source account is "
				//		+ sourceAcctBalance
				//		+ ". New balance of the target account is "
				//		+ targetAcctBalance;
				message = tranMessage;
				this.addActionMessage(message);
			} else {
				// got problems
				message = tranMessage;
				this.addActionError(message);
			}
		} else {
			// input incorrect parameters
			this.addActionError(message);
		}
		
		return SUCCESS;
	}

	public String getSourceAcct() {
		return sourceAcct;
	}

	public void setSourceAcct(String sourceAcct) {
		this.sourceAcct = sourceAcct;
	}

	public String getTargetAcct() {
		return targetAcct;
	}

	public void setTargetAcct(String targetAcct) {
		this.targetAcct = targetAcct;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
}
