package asu.bank.Admin.viewBeans;

import java.util.Date;

import javax.validation.constraints.*;

import org.hibernate.validator.constraints.*;

import asu.bank.hibernateFiles.User;

public class OperationBean {
	
	@NotEmpty(message="Please enter either DEBIT or CREDIT or PAYMENT")
	@Pattern(regexp="(DEBIT|CREDIT|PAYMENT)", message="Please enter either DEBIT or CREDIT or PAYMENT")
	private String operationType;
	
	@NotNull(message="Please enter an amount ")
	@DecimalMin(value="0.01", message="Amount must be greater than zero")
	private Double operationAmount;
	
	private User user;
	
	private Integer operationId;
	private String operationCurrentStatus;
	private Date operationCreatedAt;
	
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public Double getOperationAmount() {
		return operationAmount;
	}
	public void setOperationAmount(Double operationAmount) {
		this.operationAmount = operationAmount;
	}
	public Integer getOperationId() {
		return operationId;
	}
	public void setOperationId(Integer operationId) {
		this.operationId = operationId;
	}
	public String getOperationCurrentStatus() {
		return operationCurrentStatus;
	}
	public void setOperationCurrentStatus(String operationCurrentStatus) {
		this.operationCurrentStatus = operationCurrentStatus;
	}
	public Date getOperationCreatedAt() {
		return operationCreatedAt;
	}
	public void setOperationCreatedAt(Date operationCreatedAt) {
		this.operationCreatedAt = operationCreatedAt;
	}
}
