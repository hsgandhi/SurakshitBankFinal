package asu.bank.merchant.viewBeans;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class AccountTransferBean {
	
	private String emailIdSender;
	
	@NotEmpty(message="Please enter Receiver's email id.") 
	@Email
	@Pattern(regexp=".+@.+\\..+", message="Please enter a valid email address")
	private String emailIdReceiver;
	
	private String accountIdSender;
	private String accountIdReceiver;
	private String balanceSender;
	private String balanceReceiver;
	
	@NotEmpty(message="Please enter the amount to be transferred")
	@DecimalMin(value="0.01", message="Amount must be greater than zero")
	private String amount;
	
	private String oneTimePwd;
	
	
	public String getOneTimePwd() {
		return oneTimePwd;
	}


	public void setOneTimePwd(String oneTimePwd) {
		this.oneTimePwd = oneTimePwd;
	}


	public String getBalanceSender() {
		return balanceSender;
	}


	public void setBalanceSender(String balanceSender) {
		this.balanceSender = balanceSender;
	}


	public String getBalanceReceiver() {
		return balanceReceiver;
	}


	public void setBalanceReceiver(String balanceReceiver) {
		this.balanceReceiver = balanceReceiver;
	}

	public String getEmailIdSender() {
		return emailIdSender;
	}


	public void setEmailIdSender(String emailIdSender) {
		this.emailIdSender = emailIdSender;
	}


	public String getEmailIdReceiver() {
		return emailIdReceiver;
	}


	public void setEmailIdReceiver(String emailIdReceiver) {
		this.emailIdReceiver = emailIdReceiver;
	}


	public String getAccountIdSender() {
		return accountIdSender;
	}


	public void setAccountIdSender(String accountIdSender) {
		this.accountIdSender = accountIdSender;
	}


	public String getAccountIdReceiver() {
		return accountIdReceiver;
	}


	public void setAccountIdReceiver(String accountIdReceiver) {
		this.accountIdReceiver = accountIdReceiver;
	}


	public String getAmount() {
		return amount;
	}


	public void setAmount(String amount) {
		this.amount = amount;
	}
	
}
