package asu.bank.customer.viewBeans;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class AccountDebitViewBean {

	private String balance;
	private String accountId;
	@Email
	@Pattern(regexp=".+@.+\\..+", message="Please enter a valid email address")
	private String emailId;
	
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	

	@NotEmpty(message="Please enter an amount to  be debited.")
	@DecimalMin(value="0.01", message="Amount must be greater than zero")
	@Size(min=1, max=15) 
	private String amount;
	
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
