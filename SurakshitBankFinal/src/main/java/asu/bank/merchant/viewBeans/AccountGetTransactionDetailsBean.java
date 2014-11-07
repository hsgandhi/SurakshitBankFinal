package asu.bank.merchant.viewBeans;

import java.util.List;


public class AccountGetTransactionDetailsBean {
	
	
	private String accountId;
	private String balance;
	private String transactionType;
	private String transactionCurrentStatus;
	private String transactionAmount;
	private String primaryParty;
	private String secondaryParty;
	private String timeStamp;
	
	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getTransactionCurrentStatus() {
		return transactionCurrentStatus;
	}

	public void setTransactionCurrentStatus(String transactionCurrentStatus) {
		this.transactionCurrentStatus = transactionCurrentStatus;
	}

	public String getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getPrimaryParty() {
		return primaryParty;
	}

	public void setPrimaryParty(String primaryParty) {
		this.primaryParty = primaryParty;
	}

	public String getSecondaryParty() {
		return secondaryParty;
	}

	public void setSecondaryParty(String secondaryParty) {
		this.secondaryParty = secondaryParty;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

//	public List<AccountGetTransactionDetailsBean> transaction;
//	public List<AccountGetTransactionDetailsBean> getTransaction() {
//		return transaction;
//	}
//
//	public void setTransaction(List<AccountGetTransactionDetailsBean> transaction) {
//		this.transaction = transaction;
//	}
	


}
