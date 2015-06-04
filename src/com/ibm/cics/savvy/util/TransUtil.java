package com.ibm.cics.savvy.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.ibm.cics.savvy.entity.Account;
import com.ibm.cics.savvy.entity.TransHistory;
import com.ibm.cics.savvy.entity.User;
import com.ibm.cics.server.CCSIDErrorException;
import com.ibm.cics.server.Channel;
import com.ibm.cics.server.ChannelErrorException;
import com.ibm.cics.server.CodePageErrorException;
import com.ibm.cics.server.Container;
import com.ibm.cics.server.ContainerErrorException;
import com.ibm.cics.server.InvalidProgramIdException;
import com.ibm.cics.server.InvalidRequestException;
import com.ibm.cics.server.InvalidSystemIdException;
import com.ibm.cics.server.LengthErrorException;
import com.ibm.cics.server.NotAuthorisedException;
import com.ibm.cics.server.Program;
import com.ibm.cics.server.RolledBackException;
import com.ibm.cics.server.Task;
import com.ibm.cics.server.TerminalException;

/**
 * Utility class to help DPL calls to JOR programs
 */
public class TransUtil {
	private static TransUtil tranUtil = null;
	private boolean linkToAOR = true;

	/**
	 *  Get singleton instance for the TransUtil
	 */
	public static TransUtil getTranUtil() {
		if ( tranUtil == null ) {
			tranUtil = new TransUtil();
		}
		return tranUtil;
	}

	private TransUtil() {
		// TODO Auto-generated constructor stub
		linkToAOR = PropertiesUtil.getPropertiesUtil().isLinkToAOR();
	}
	
	/**
	 *  Delegator method for create user. It links to OSGi programs on JOR
	 *  It returns string array to indicate the success/fail status and detail message
	 */
	public String[] createUser(HashMap<String, String> containerData) {
		String[] info = new String[2];
		
		if ( linkToAOR ) {
			Channel channel = callTransaction(PropertiesUtil.getPropertiesUtil().getProgCreateUser(), containerData);
			// get the transaction status. 1: success; 0: fail
			info[0] = getContainerData(channel, IConstants.TRAN_CODE);
			// get the transaction detail message
			info[1] = getContainerData(channel, IConstants.TRAN_MSG);
			
		} else {
			// do simulated return if the OSGi programs on JOR are not ready
			info[0] = "1";
			info[1] = "Create user " + containerData.get(IConstants.CUST_ID) + " is successful";
		}
		
		return info;
	}
	
	/**
	 *  Delegator method for query user. It links to OSGi programs on JOR
	 *  It returns a User object which includes the customer info and related accounts info.
	 *  One user may have multiple accounts
	 */
	public User queryUser(HashMap<String, String> containerData) {
		String userInfo = "";
		Set<Account> accountSet = new HashSet<Account>();

		if ( linkToAOR ) {
			Channel channel = callTransaction(PropertiesUtil.getPropertiesUtil().getProgQueryUser(), containerData);
			Container container = null;
			String acctInfo = null;
			
			// iterate all the return containers to find out the customer info and related accounts info
			Iterator<Container> it = channel.containerIterator();
			while ( it.hasNext() ) {
				container = it.next();
				if ( container != null ) {
					if ( container.getName().startsWith(IConstants.CUST_INFO) ) {
						// this is customer info
						userInfo = getContainerData(channel, container.getName());

					} else if ( container.getName().startsWith(IConstants.ACCT_LIST) ) {
						// this is a container for an account
						acctInfo = getContainerData(channel, container.getName());
						// create an Account object and put it into set
						Account account = new Account(acctInfo);
						accountSet.add(account);
					}
				}
			}
			
		} else {
			// do simulated return if the OSGi programs on JOR are not ready
			userInfo = "kcai_zsc;caikai;m;100;Beijing Haidian";
			String acctInfo = null;
			for ( int i=0; i<5; i++) {
				acctInfo = "000000000"+i+";kcai_zsc;"+ ((i+1)*100) +";2013-03-11 15:30:00";
				Account acct = new Account(acctInfo);
				accountSet.add(acct);
			}

		}
		
		// use the customer info and account set to create User object, and return
		User user = new User(userInfo, accountSet);
		return user;
	}

	/**
	 *  Delegator method for create account. It links to OSGi programs on JOR
	 *  It returns string array to indicate the success/fail status and detail message
	 */
	public String[] createAccount(HashMap<String, String> containerData) {
		String[] info = new String[2];
		
		if ( linkToAOR ) {
			Channel channel = callTransaction(PropertiesUtil.getPropertiesUtil().getProgCreateAcct(), containerData);
			// get the transaction status. 1: success; 0: fail
			info[0] = getContainerData(channel, IConstants.TRAN_CODE);
			// get the transaction detail message
			info[1] = getContainerData(channel, IConstants.TRAN_MSG);
		} else {
			// do simulated return if the OSGi programs on JOR are not ready
			info[0] = "1";
			info[1] = "Create account " + containerData.get(IConstants.ACCT_NUMBER) + " is successful";
			
		}

		return info;
	}
	
	/**
	 *  Delegator method for query account. It links to OSGi programs on JOR
	 *  It returns an Account object which includes the account info and related transaction history records.
	 *  One account may have multiple transaction history records.
	 */
	public Account queryAccount(HashMap<String, String> containerData) {
		String acctInfo = "";
		Set<TransHistory> transHistories = new HashSet<TransHistory>();

		if ( linkToAOR ) {
			Channel channel = callTransaction(PropertiesUtil.getPropertiesUtil().getProgQueryAcct(), containerData);
			Container container = null;
			String tranInfo = ""; 

			// iterate all the return containers to find out the account info and related transaction records
			Iterator<Container> it = channel.containerIterator();
			while ( it.hasNext() ) {
				container = it.next();
				if ( container != null ) {
					if ( container.getName().startsWith(IConstants.ACCT_INFO) ) {
						// this is account info
						acctInfo = getContainerData(channel, container.getName());
						
					} else if ( container.getName().startsWith(IConstants.HIST_LIST) ) {
						// this is a container for a transaction history record
						tranInfo = getContainerData(channel, container.getName());
						// create a transaction history record object and put it into set
						TransHistory tranRecord = new TransHistory(tranInfo);
						transHistories.add(tranRecord);
					}
				}
			}
		} else {
			// do simulated return if the OSGi programs on JOR are not ready
			acctInfo = "0000000001;kcai_zsc;100;2013-03-11 15:30:00";
			String tranInfo = null;
			for ( int i=0; i<5; i++) {
				tranInfo = "DEPOSIT;0000000001;"+ (i+1) +"25;2013-03-08 19:30:00";
				TransHistory tranRecord = new TransHistory(tranInfo);
				transHistories.add(tranRecord);
			}
			
		}
		
		// use the account info and transaction history record set to create Account object, and return
		Account account = new Account(acctInfo, transHistories);
		return account;
	}
	
	/**
	 *  Delegator method for deposit. It links to OSGi programs on JOR
	 *  It returns string array to indicate the success/fail status and detail message
	 */
	public String[] deposit(HashMap<String, String> containerData) {
		String[] info = new String[2];

		if ( linkToAOR ) {
			Channel channel = callTransaction(PropertiesUtil.getPropertiesUtil().getProgDeposit(), containerData);
			// get the transaction status. 1: success; 0: fail
			info[0] = getContainerData(channel, IConstants.TRAN_CODE);
			// get the transaction detail message
			info[1] = getContainerData(channel, IConstants.TRAN_MSG);
		} else {
			// do simulated return if the OSGi programs on JOR are not ready
			info[0] = "1";
			info[1] = "Successfully deposit "
					+ containerData.get(IConstants.TRAN_AMOUNT)
					+ " to account "
					+ containerData.get(IConstants.TRAN_ACCTNM);
			
		}

		return info;
	}
	
	/**
	 *  Delegator method for deposit. It links to OSGi programs on JOR
	 *  It returns string array to indicate the success/fail status and detail message
	 */
	public String[] withdraw(HashMap<String, String> containerData) {
		String[] info = new String[2];

		if ( linkToAOR ) {
			Channel channel = callTransaction(PropertiesUtil.getPropertiesUtil().getProgWithdraw(), containerData);
			// get the transaction status. 1: success; 0: fail
			info[0] = getContainerData(channel, IConstants.TRAN_CODE);
			// get the transaction detail message
			info[1] = getContainerData(channel, IConstants.TRAN_MSG);
		} else {
			// do simulated return if the OSGi programs on JOR are not ready
			info[0] = "1";
			info[1] = "Successfully withdraw "
					+ containerData.get(IConstants.TRAN_AMOUNT)
					+ " from account "
					+ containerData.get(IConstants.TRAN_ACCTNM);
			
		}

		return info;
	}
	
	/**
	 *  Delegator method for deposit. It links to OSGi programs on JOR
	 *  It returns status and detail message in the commarea
	 */
	public void transfer(TransferCommarea commarea) {
		
		if ( linkToAOR ) {
			Program bkTran = new Program();
			bkTran.setName(PropertiesUtil.getPropertiesUtil().getProgTransfer());
			try {
				byte[] data = commarea.createCommarea();
				// link to the program on AOR, 
				// the return data is put in the commarea buffer
				bkTran.setSyncOnReturn(true);
				bkTran.link(data);
				
			} catch (InvalidRequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LengthErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidSystemIdException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotAuthorisedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidProgramIdException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RolledBackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TerminalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// do simulated return if the OSGi programs on JOR are not ready
			commarea.setTranResult("0");
			commarea.setTranMessage("Successfully transferred.");
			commarea.setSourceAcctBalance("500");
			commarea.setTargetAcctBalance("365");
		}
		
	}
	
	/**
	 * Called by delegator methods to perform the real behavior.
	 * It creates the channel/container with transaction data, and links to the OSGi programs on JOR 
	 */
	protected Channel callTransaction(String txProgName, HashMap<String, String> containerData) {
		Task task = Task.getTask();
		Channel channel = null;
		
		// create program object and set the program name to be executed
		// the program is a remote resource which pointing to a remote program on JOR
		Program bkTran = new Program();
		bkTran.setName(txProgName);
		
		try {
			// create channel
			channel = task.createChannel("BANKTRAN");
			
			// create containers and put the transaction data into containers
			Iterator<String> it = containerData.keySet().iterator();
			Container container = null;
			String containerName = null;
			String data = null;
			while ( it.hasNext() ) {
				containerName = it.next();
				data = containerData.get(containerName);
				container = channel.createContainer(containerName);
				container.put(data.getBytes());
			}
			
			System.out.println("\nProgram.link(channel) to program " + bkTran.getName());
			// sync on return
			bkTran.setSyncOnReturn(false);
			// links to OSGi programs on JOR
			bkTran.link(channel);

		} catch (InvalidRequestException e) {
			System.out.println("InvalidRequestException");
			releaseChannel(channel);
			e.printStackTrace();
		} catch (LengthErrorException e) {
			System.out.println("LengthErrorException");
			releaseChannel(channel);
			e.printStackTrace();
		} catch (InvalidSystemIdException e) {
			System.out.println("InvalidSystemIdException");
			releaseChannel(channel);
			e.printStackTrace();
		} catch (NotAuthorisedException e) {
			System.out.println("NotAuthorisedException");
			releaseChannel(channel);
			e.printStackTrace();
		} catch (InvalidProgramIdException e) {
			System.out.println("InvalidProgramIdException");
			releaseChannel(channel);
			e.printStackTrace();
		} catch (RolledBackException e) {
			System.out.println("RolledBackException");
			releaseChannel(channel);
			e.printStackTrace();
		} catch (TerminalException e) {
			System.out.println("TerminalException");
			releaseChannel(channel);
			e.printStackTrace();
		} catch (ChannelErrorException e) {
			System.out.println("ChannelErrorException");
			releaseChannel(channel);
			e.printStackTrace();
		} catch (ContainerErrorException e) {
			System.out.println("ContainerErrorException");
			releaseChannel(channel);
			e.printStackTrace();
		} catch (CCSIDErrorException e) {
			System.out.println("CCSIDErrorException");
			releaseChannel(channel);
			e.printStackTrace();
		} catch (CodePageErrorException e) {
			System.out.println("CodePageErrorException");
			releaseChannel(channel);
			e.printStackTrace();
		}
		
		return channel;
	}
	
	/**
	 * Called to release channel/containers when the link to JOR got exceptions
	 */
	protected void releaseChannel(Channel channel) {
		if ( channel != null ) {
			System.out.println("release channel...");
			Iterator<Container> it = channel.containerIterator();
			while ( it.hasNext() ) {
				String containerName = it.next().getName();
				try {
					channel.deleteContainer(containerName);
				} catch (InvalidRequestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ContainerErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ChannelErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CCSIDErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CodePageErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			channel = null;
		}
		
	}

	/**
	 * Called to get data from specified container
	 */
	protected String getContainerData(Channel channel, String containerName) {
		String data = "";
		Container container;
		try {
			if ( channel != null ) {
				container = channel.getContainer(containerName);
				if ( container != null ) {
					data = new String(container.get());
					System.out.println("container " + containerName + " data="+data);
				} else {
					System.out.println("container " + containerName + " is not found");
				}
			} else {
				System.out.println("channel is null");
			}
		} catch (ContainerErrorException e) {
			// TODO Auto-generated catch block
			System.out.println("ContainerErrorException");
			e.printStackTrace();
		} catch (ChannelErrorException e) {
			// TODO Auto-generated catch block
			System.out.println("ChannelErrorException");
			e.printStackTrace();
		} catch (CCSIDErrorException e) {
			// TODO Auto-generated catch block
			System.out.println("CCSIDErrorException");
			e.printStackTrace();
		} catch (CodePageErrorException e) {
			// TODO Auto-generated catch block
			System.out.println("CodePageErrorException");
			e.printStackTrace();
		}
		
		return data;
	}
	
}
