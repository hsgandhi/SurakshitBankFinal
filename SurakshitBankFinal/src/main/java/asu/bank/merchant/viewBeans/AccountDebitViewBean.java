package asu.bank.merchant.viewBeans;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class AccountDebitViewBean {

	private String balance;
	private String accountId;
	
	private String transactionState;
	
	public String getTransactionState() {
		return transactionState;
	}
	public void setTransactionState(String transactionState) {
		this.transactionState = transactionState;
	}
	/*@NotEmpty(message="Please enter the Email ID of the Customer.")
	@Email
	@Pattern(regexp=".+@.+\\..+", message="Please enter a valid email address")*/
	private String emailId;
	
	@NotEmpty(message="Please enter an amount to  be debited.")
	@DecimalMin(value="0.01", message="Amount must be greater than zero")
	private String amount;
	
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
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
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	
	
}
