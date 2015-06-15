package com.ibm.cics.minibank.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.ibm.cics.minibank.util.WORDBUtil;
import com.ibm.cics.minibank.common.util.IConstants;
import com.ibm.cics.minibank.util.TransUtil;
import com.ibm.cics.minibank.util.TransferCommarea;
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
		
		InitialContext ctx = null;
		
		//UserTransaction tran = startTransaction(ctx);
		writeHistoryRecord(ctx);

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
		
		//endTransaction(tran);
		

		//without JTA, we have to commit explict
		try {
			WORDBUtil.getDBUtilInstance().commitOrRollback(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //true means commit
		
		//close DB2 connection
		WORDBUtil.getDBUtilInstance().closeDB2Connection();
		
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
	
	private void writeHistoryRecord(InitialContext ctx)
	{
		
		//open connection to DB2
		WORDBUtil.getDBUtilInstance().initDB2Connection(2); // get type 2 connection
		
		// write the transaction history record
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String txTime = formatter.format(new Date());
		// construct the SQL command
		String sqlCmd = "INSERT INTO CTUSERS.REQHISTORY(REQUEST,TRANSTIME) VALUES("
				+ "'transfer from " + sourceAcct + " to " + targetAcct +"', "
				+ "'" + txTime + "'"
				+ ")";
		// update the database table
		int numUpd = WORDBUtil.getDBUtilInstance().execUpdateSQL(sqlCmd);
		
	}
	
	private UserTransaction startTransaction(InitialContext ctx)
	{
		UserTransaction tran = null;
		try {
			ctx = new InitialContext();
			tran = 
			        (UserTransaction)ctx.lookup("java:comp/UserTransaction");
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//transaction begin
		try {
			tran.begin();
		} catch (NotSupportedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return tran;
	}
	
	private void endTransaction(UserTransaction tran)
	{
		System.out.println("get into end transacation,sourceAcct:" + sourceAcct);
		if(sourceAcct.equals("0000000004"))
		{
			System.out.println("in rollback");
			try {
				tran.rollback();
			} catch (IllegalStateException | SecurityException
					| SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("in commit");
			try {
				tran.commit();
			} catch (IllegalStateException | SecurityException
					| HeuristicMixedException | HeuristicRollbackException
					| RollbackException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
