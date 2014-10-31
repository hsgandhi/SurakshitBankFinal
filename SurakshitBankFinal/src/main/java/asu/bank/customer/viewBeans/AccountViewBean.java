package asu.bank.customer.viewBeans;

import javax.validation.constraints.DecimalMin;

import org.hibernate.validator.constraints.NotEmpty;

public class AccountViewBean {
	
	private String emailId;
	private String accountId;
	
	@NotEmpty(message="Please enter the amount")
	@DecimalMin(value="0.01", message="Amount must be greater than zero")
	//@Pattern
	private String currency;
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	/*
	public BigDecimal getCurrency() {
		return currency;
	}
	public void setCurrency(BigDecimal currency) {
		this.currency = currency;
	}
	*/
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCurrency() {
		return currency;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

}
