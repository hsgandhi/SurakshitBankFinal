package asu.bank.RegularEmployee.viewBeans;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class UserEmailPhoneBean {

	@NotEmpty(message="Please enter a valid email address")
	@Email
	@Pattern(regexp=".+@.+\\..+", message="Please enter a valid email address")
	private String emailId;
	
	@NotEmpty(message="Please enter your phone number without hyphens")
	@Pattern(regexp="[0-9]+", message="Please enter your phone number without hyphens, only numbers allowed")
	private String phoneNumber;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
}
