package com.ibm.cics.savvy.action;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Action class to show manu page
 */
public class MenuPageAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	public String toCatalog() {
		return SUCCESS;
	}
}
