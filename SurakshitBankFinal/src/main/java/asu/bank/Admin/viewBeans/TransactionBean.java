package asu.bank.Admin.viewBeans;

import java.util.Date;

import javax.validation.constraints.*;

import org.hibernate.validator.constraints.*;

public class TransactionBean {
	
	@NotEmpty(message="Please enter a valid email address")
	@Email
	@Pattern(regexp=".+@.+\\..+", message="Please provide a valid email address")
	private String primaryUserEmail;
	
	@NotEmpty(message="Please enter a valid email address")
	@Email
	@Pattern(regexp=".+@.+\\..+", message="Please provide a valid email address")
	private String secondaryUserEmail;
	
	@NotEmpty(message="Please enter either DEBIT or CREDIT or PAYMENT")
	@Pattern(regexp="(DEBIT|CREDIT|PAYMENT)", message="Please enter either DEBIT or CREDIT or PAYMENT")
	private String transactionType;
	
	@NotNull(message="Please enter an amount ")
	@DecimalMin(value="0.01", message="Amount must be greater than zero")
	private Double transactionAmount;
	
	private Integer transactionId;
	private String transactionCurrentStatus;
	private Date transactionCreatedAt;
	
	private String encryptedTransactionId;
	
	public String getEncryptedTransactionId() {
		return encryptedTransactionId;
	}
	public void setEncryptedTransactionId(String encryptedTransactionId) {
		this.encryptedTransactionId = encryptedTransactionId;
	}
	public String getPrimaryUserEmail() {
		return primaryUserEmail;
	}
	public void setPrimaryUserEmail(String primaryUserEmail) {
		this.primaryUserEmail = primaryUserEmail;
	}
	public String getSecondaryUserEmail() {
		return secondaryUserEmail;
	}
	public void setSecondaryUserEmail(String secondaryUserEmail) {
		this.secondaryUserEmail = secondaryUserEmail;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public Double getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public Integer getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}
	public String getTransactionCurrentStatus() {
		return transactionCurrentStatus;
	}
	public void setTransactionCurrentStatus(String transactionCurrentStatus) {
		this.transactionCurrentStatus = transactionCurrentStatus;
	}
	public Date getTransactionCreatedAt() {
		return transactionCreatedAt;
	}
	public void setTransactionCreatedAt(Date transactionCreatedAt) {
		this.transactionCreatedAt = transactionCreatedAt;
	}
}
