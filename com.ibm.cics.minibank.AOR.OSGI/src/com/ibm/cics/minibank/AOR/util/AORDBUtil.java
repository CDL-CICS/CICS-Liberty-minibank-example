package com.ibm.cics.minibank.AOR.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.ibm.cics.minibank.common.util.IConstants;
import com.ibm.cics.server.Task;

/**
 * Utility class to do database operations
 *  - initialize the connection to DB2
 *  - close the connection to DB2
 *  - execute the SQL to query tables
 *  - execute the SQL to update tables
 */
public class AORDBUtil {
	private static AORDBUtil instance = null;
	protected Connection con = null;
	protected boolean connectToDB2 = true;

	/**
	 * Get singleton instance of DBUtil
	 */
	public static AORDBUtil getDBUtilInstance() {
		if ( instance == null ) {
			instance = new AORDBUtil();
		}
		
		return instance;
	}
	
	protected AORDBUtil() {
		super();
		// get the flag from property file, if connect to DB2
		connectToDB2 = AORPropertiesUtil.getPropertiesUtil().isConnectToDB2();
	}
	
	/**
	 * execute SQL to query tables
	 */
	public ArrayList<String> execQuerySQL(String sqlCmd) {
		System.out.println("Exec sqlCmd=" + sqlCmd);

		ArrayList<String> resultSet = new ArrayList<String>();
		String record = "";
		
		if ( connectToDB2 ) {
			initDB2Connection();

			if ( con != null ) {
				ResultSet rs;
				Task t = Task.getTask();
				Statement stmt;

				try {
					// Create the Statement
					stmt = con.createStatement();
					// Execute a query and generate a ResultSet instance	
					rs = stmt.executeQuery(sqlCmd);
					// Print all of the employee numbers to standard output device
					int columnNum = rs.getMetaData().getColumnCount();
					while ( rs.next() ) {
						record = "";
						for ( int i=1; i<=columnNum; i++) {
							record = record + rs.getString(i);
							if ( i < columnNum ) {
								record = record + IConstants.DATA_FIELD_SPLITTER;
							}
						}
						resultSet.add(record);
					}
					// Close the ResultSet
					rs.close();
					// Close the Statement
					stmt.close();
				} catch(SQLException ex) {
					System.err.println("SQLException information");
					while(ex!=null) {
						t.out.println("Error msg: " + ex.getMessage());
						t.out.println("SQLSTATE: " + ex.getSQLState());
						t.out.println("Error code: " + ex.getErrorCode());
						ex.printStackTrace();
						ex = ex.getNextException();
					}
				}
			}
			
			closeDB2Connection();

		} else {
			// if we does not have DB2 connection, simulate the database query by sending simulated result
			for ( int i=0; i<5; i++ ) {
				record = (i+1)*100 + IConstants.DATA_FIELD_SPLITTER
						+ "row" + (i+1) +"_col2" + IConstants.DATA_FIELD_SPLITTER
						+ "row" + (i+1) +"_col3" + IConstants.DATA_FIELD_SPLITTER
						+ "row" + (i+1) +"_col4" + IConstants.DATA_FIELD_SPLITTER
						+ "row" + (i+1) +"_col5";
				resultSet.add(record);
			}
		}
		
		return resultSet;
	}

	/**
	 * execute SQL to update tables
	 */
	public int execUpdateSQL(String sqlCmd) {
		System.out.println("Exec sqlCmd=" + sqlCmd);

		int numUpd = 0;
		if ( connectToDB2 ) {
			initDB2Connection();

			if ( con != null ) {
				Task t = Task.getTask();
				Statement stmt;
                System.out.println("got task");
				try {
					// Create the Statement
					System.out.println("before create statment");
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
			
			closeDB2Connection();
			
		} else {
			// if we does not have DB2 connection, simulate the database query by sending simulated result
			numUpd = 1;
		}
		
		return numUpd;
	}

	/**
	 * Internal use to initialize the connection to DB2
	 */
	protected void initDB2Connection() {
		if ( connectToDB2 ) {
			String url = "jdbc:default:connection";
			System.out.println("DB2 url=" + url);
			Task t = Task.getTask();
			try
			{
				// Create the connection
				con = DriverManager.getConnection (url);
				t.out.println(" getConnection "+url+" Finshed");
				// Commit changes when transaction finish.
				con.setAutoCommit(false);
			} catch(SQLException ex) {
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

	/**
	 * Internal use to close the connection to DB2
	 */
	protected void closeDB2Connection() {
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
