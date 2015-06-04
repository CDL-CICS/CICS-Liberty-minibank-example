package com.ibm.cics.savvy.entity;

import java.util.HashSet;
import java.util.Set;

import com.ibm.cics.savvy.util.IConstants;

/**
 * Class for the customer info.
 * Used for data exchanging between jsp forms and actions
 */
public class User {

	private String customerID;
	private String userName;
	private UserGender userGender;
	private String age;
	private String address;
	private Set<Account> accountSet;

	public User() {
	}
	
	/**
	 * Use the passed in string to construct User object
	 */
	public User(String info) {
		HashSet<Account> acctSet = new HashSet<Account>();
		initialize(info, acctSet);
	}
	
	public User(String info, Set<Account> acctSet) {
		initialize(info, acctSet);
	}
	
	protected void initialize(String info, Set<Account> acctSet) {
		String[] parms = info.split(IConstants.DATA_FIELD_SPLITTER);
		if ( parms.length >= 5 ) {
			customerID = parms[0];
			userName = parms[1];
			if ("m".equalsIgnoreCase(parms[2])) {
				userGender = UserGender.MALE;
			} else {
				userGender = UserGender.FEMALE;
			}
			age = parms[3];
			address = parms[4];
		}
		
		setAccountSet(acctSet);
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserGender getUserGender() {
		return userGender;
	}

	public void setUserGender(UserGender userGender) {
		this.userGender = userGender;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set<Account> getAccountSet() {
		return accountSet;
	}

	public void setAccountSet(Set<Account> accountSet) {
		this.accountSet = accountSet;
	}

}
