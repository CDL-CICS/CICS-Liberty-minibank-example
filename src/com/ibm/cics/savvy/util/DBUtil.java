package com.ibm.cics.savvy.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.*;
import javax.naming.*;
import javax.sql.*;

import com.ibm.cics.server.Task;

/**
 * Utility class to do database operations
 *  - initialize the connection to DB2
 *  - close the connection to DB2
 *  - execute the SQL to query tables
 *  - execute the SQL to update tables
 */
public class DBUtil {
	private static DBUtil instance = null;
	protected Connection con = null;
	protected boolean connectToDB2 = true;

	/**
	 * Get singleton instance of DBUtil
	 */
	public static DBUtil getDBUtilInstance() {
		if ( instance == null ) {
			instance = new DBUtil();
		}
		
		return instance;
	}
	
	protected DBUtil() {
		super();
	}
	

	/**
	 * execute SQL to update tables
	 */
	public int execUpdateSQL(String sqlCmd) {
		System.out.println("Exec sqlCmd=" + sqlCmd);

		int numUpd = 1; // set default to ok


            
			if ( con != null ) {
				System.out.println("before get task");
				Task t = Task.getTask();
				Statement stmt;
				System.out.println("after get task");

				try {
					// Create the Statement
					System.out.println("before get statement");
					stmt = con.createStatement();
					// Execute a query and generate a ResultSet instance	
					System.out.println("before execute SQL");
					numUpd = stmt.executeUpdate(sqlCmd);
					System.out.println("after execute SQL");
					// Close the Statement
					stmt.close();
				} catch(SQLException ex) {
					numUpd = 0;
					System.err.println("SQLException information");
					while(ex!=null) {
						t.err.println("Error msg: " + ex.getMessage());
						t.err.println("SQLSTATE: " + ex.getSQLState());
						t.err.println("Error code: " + ex.getErrorCode());
						ex.printStackTrace();
						ex = ex.getNextException();
					}
				}
			
			

		}
		
		return numUpd;
	}

	/**
	 * initialize the connection to DB2
	 */
	public void initDB2Connection() {
		if ( connectToDB2 ) {
			Task t = Task.getTask();
			try
			{
				// Create the connection
				
				Context ctx=new InitialContext();
				DataSource ds=(DataSource)ctx.lookup("jdbc/CICSType4DataSource"); 
				con=ds.getConnection(); 
				// Commit changes automatically
				con.setAutoCommit(false);
				
				System.out.println("got JDBC connect");
			} catch(SQLException ex) {
				System.err.println("SQLException information");
				while(ex!=null) {
					t.err.println("Error msg: " + ex.getMessage());
					t.err.println("SQLSTATE: " + ex.getSQLState());
					t.err.println("Error code: " + ex.getErrorCode());
					ex.printStackTrace();
					ex = ex.getNextException();
				}
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
	}

	/**
	 * close the connection to DB2
	 */
	public void closeDB2Connection() {
		Task t = Task.getTask();
		try {
			con.close();
			t.out.println("**** JDBC Connection CLOSED *****");
		}
		catch(SQLException ex)
		{
			System.err.println("SQLException information");
			while(ex!=null) {
				t.err.println("Error msg: " + ex.getMessage());
				t.err.println("SQLSTATE: " + ex.getSQLState());
				t.err.println("Error code: " + ex.getErrorCode());
				ex.printStackTrace();
				ex = ex.getNextException();
			}
		}		
	}

}
