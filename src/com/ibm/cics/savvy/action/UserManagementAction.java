package com.ibm.cics.savvy.action;

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

import com.ibm.cics.savvy.entity.User;
import com.ibm.cics.savvy.util.DBUtil;
import com.ibm.cics.savvy.util.IConstants;
import com.ibm.cics.savvy.util.PropertiesUtil;
import com.ibm.cics.savvy.util.TransUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Action class for create user and query user
 */
public class UserManagementAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private User user;
	private String tempUserGender;

	@Override
	public String execute() throws Exception {
		return super.execute();
	}

	public String doCreateUser() {
		String message = null;
		boolean inputCorrect = true;
		
		InitialContext ctx = null;
		UserTransaction tran = null;
		Connection con = null;
		try {
			ctx = new InitialContext();
			tran = 
			        (UserTransaction)ctx.lookup("java:comp/UserTransaction");
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		try {
			Integer.parseInt(user.getAge());
			inputCorrect = true;
		} catch (NumberFormatException e) {
			inputCorrect = false;
			message = "The age is not a integer number. Fail to create user.";
		}

		if ( inputCorrect ) {


			//open connection to DB2
			DataSource ds;
			try {
				ds = (DataSource)ctx.lookup("jdbc/CICSType4DataSource");
				con=ds.getConnection();
				con.setAutoCommit(false);
			} catch (NamingException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}  catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
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
			
			// write the transaction history record
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String txTime = formatter.format(new Date());
			// construct the SQL command
			String sqlCmd = "INSERT INTO YANFENG.REQHISTORY(REQUEST,TRANSTIME) VALUES("
					+ "'create USER " + user.getCustomerID() + "', "
					+ "'" + txTime + "'"
					+ ")";
			// update the database table
			Statement stat = null;
			try {
				stat = con.createStatement();
				stat.execute(sqlCmd);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
			
			// Put the create user transaction data into HashMap to construct channel/container later
			HashMap<String, String> containerData = new HashMap<String, String>();
			containerData.put(IConstants.CUST_ID, user.getCustomerID());
			containerData.put(IConstants.CUST_NAME, user.getUserName());
			containerData.put(IConstants.CUST_GENDER, tempUserGender);
			containerData.put(IConstants.CUST_AGE, user.getAge());
			containerData.put(IConstants.CUST_ADDR, user.getAddress());
			// invoke delegator method in the TransUtil object
			String[] result = TransUtil.getTranUtil().createUser(containerData);
			
			System.out.println("before add message" + result[0]);
			if ( (new Integer(result[0])).intValue() > 0 ) {
				// success
				this.addActionMessage(result[1]);
				System.out.println("call AOR success:" + result[1]);
			} else {
				// got problems
				this.addActionError(result[1]);
				System.out.println("call AOR fail:" + result[1]);
			}
			
			System.out.println("before commit and rollback");
			System.out.println("userid:"+user.getCustomerID());
			//simulate a rollback when userid is "rollback"
			
			try {
				stat.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			
			if (user.getCustomerID().equals("rollback"))
			{
				try {
					System.out.println("before rollback");
					tran.rollback();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				try {
					System.out.println("before commit");
					tran.commit();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (HeuristicMixedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (HeuristicRollbackException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RollbackException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//close DB2 connection

			DBUtil.getDBUtilInstance().closeDB2Connection();
			

		} else {
			// input incorrect parameters
			this.addActionError(message);
		}

		
		return SUCCESS;

	}

	public String toQueryUser() {
		return SUCCESS;
	}
	
	public String doQueryUser() {
		HashMap<String, String> containerData = new HashMap<String, String>();
		containerData.put(IConstants.CUST_ID, user.getCustomerID());
		//invoke method to record this request
		
		InitialContext ctx;
		UserTransaction tran = null;
		try {
			ctx = new InitialContext();
			tran = 
			        (UserTransaction)ctx.lookup("java:comp/UserTransaction");
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		//open connection to DB2
		DBUtil.getDBUtilInstance().initDB2Connection();
		
		try {
			tran.begin();
		} catch (NotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// write the transaction history record
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String txTime = formatter.format(new Date());
		
		// construct the SQL command
		String sqlCmd = "INSERT INTO YANFENG.REQHISTORY(REQUEST,TRANSTIME) VALUES("
				+ "'QUERY USER " + user.getCustomerID() + "', "
				+ "'" + txTime + "'"
				+ ")";
		// update the database table
		int numUpd = DBUtil.getDBUtilInstance().execUpdateSQL(sqlCmd);
		
		// invokde the delegator method in the TransUtil object
		user = TransUtil.getTranUtil().queryUser(containerData);
		tempUserGender = user.getUserGender().getDesc();
		
		try {
			tran.commit();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeuristicMixedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//close DB2 connection
		DBUtil.getDBUtilInstance().closeDB2Connection();
		
		return SUCCESS;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTempUserGender() {
		return tempUserGender;
	}

	public void setTempUserGender(String tempUserGender) {
		this.tempUserGender = tempUserGender;
	}
}
