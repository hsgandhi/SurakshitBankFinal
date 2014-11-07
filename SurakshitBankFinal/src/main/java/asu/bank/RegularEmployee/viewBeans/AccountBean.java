package asu.bank.RegularEmployee.viewBeans;

import asu.bank.hibernateFiles.User;

public class AccountBean {

	private Integer accountId;
	private Double balance;
	private String userEmailID;
	
	public String getUserEmailID() {
		return userEmailID;
	}
	public void setUserEmailID(String userEmailID) {
		this.userEmailID = userEmailID;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
	
}
