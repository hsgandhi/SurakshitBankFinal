package asu.bank.customer.viewBeans;

import java.sql.Date;
import java.util.List;


public class AccountGetTransactionDetailsBean {
	
	private String accountId;
	private String balance;
	private String transactionType;
	private String transactionCurrentStatus;
	private String transactionAmount;
	private String primaryParty;
	private String secondaryParty;
	private String amount;
	private String emailIdReceiver;
	private String transactionCreatedAt;
	
	
	public String getTransactionCreatedAt() {
		return transactionCreatedAt;
	}

	public void setTransactionCreatedAt(String transactionCreatedAt) {
		this.transactionCreatedAt = transactionCreatedAt;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getEmailIdReceiver() {
		return emailIdReceiver;
	}

	public void setEmailIdReceiver(String emailIdReceiver) {
		this.emailIdReceiver = emailIdReceiver;
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
