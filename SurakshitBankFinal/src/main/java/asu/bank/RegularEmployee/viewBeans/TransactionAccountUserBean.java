package asu.bank.RegularEmployee.viewBeans;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import asu.bank.hibernateFiles.Transactionsstatus;
import asu.bank.hibernateFiles.User;

public class TransactionAccountUserBean {
	private Integer transactionId;
	private String transactionType;
	private String transactionCurrentStatus;
	private Double transactionAmount;	
	private Date transactionCreatedAt;
	private Integer primaryUserID;
	private Integer primaryUserAccountID;
	private String primaryUserEmailID;
	private Integer secondaryUserID;
	private Integer secondaryUserAccountID;
	private String secondaryUserEmailID;
	
	public Integer getPrimaryUserAccountID() {
		return primaryUserAccountID;
	}
	public void setPrimaryUserAccountID(Integer primaryUserAccountID) {
		this.primaryUserAccountID = primaryUserAccountID;
	}
	public String getPrimaryUserEmailID() {
		return primaryUserEmailID;
	}
	public void setPrimaryUserEmailID(String primaryUserEmailID) {
		this.primaryUserEmailID = primaryUserEmailID;
	}
	public Integer getSecondaryUserAccountID() {
		return secondaryUserAccountID;
	}
	public void setSecondaryUserAccountID(Integer secondaryUserAccountID) {
		this.secondaryUserAccountID = secondaryUserAccountID;
	}
	public String getSecondaryUserEmailID() {
		return secondaryUserEmailID;
	}
	public void setSecondaryUserEmailID(String secondaryUserEmailID) {
		this.secondaryUserEmailID = secondaryUserEmailID;
	}	
	public Integer getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
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
	public Double getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public Integer getPrimaryUserID() {
		return primaryUserID;
	}
	public void setPrimaryUserID(Integer primaryUserID) {
		this.primaryUserID = primaryUserID;
	}
	public Integer getSecondaryUserID() {
		return secondaryUserID;
	}
	public void setSecondaryUserID(Integer secondaryUserID) {
		this.secondaryUserID = secondaryUserID;
	}
	public Date getTransactionCreatedAt() {
		return transactionCreatedAt;
	}
	public void setTransactionCreatedAt(Date transactionCreatedAt) {
		this.transactionCreatedAt = transactionCreatedAt;
	}	
}
