package asu.bank.merchant.viewBeans;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class PayRequestViewBean {
	
	@NotEmpty(message="Please enter the Email ID of the Customer.")
	@Email
	@Pattern(regexp=".+@.+\\..+", message="Please enter a valid email address")
	private String custEmailID;
	
	@NotEmpty(message="Please enter the Amount.")
	@DecimalMin(value="0.01", message="Please enter an Amount greater than zero.")
	private String paymentAmount;
	
	public String getCustEmailID() {
		return custEmailID;
	}
	public void setCustEmailID(String custEmailID) {
		this.custEmailID = custEmailID;
	}
	public String getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

}
