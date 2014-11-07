package asu.bank.RegularEmployee.viewBeans;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class UserBeanForModify {
	@NotEmpty(message="Please enter a valid name without numbers in it")
	@Pattern(regexp="[A-Za-z ]+", message="Please enter a valid name without numbers in it")
	private String name;
	
	@NotEmpty(message="Please enter your address")
	private String address;
	
//	@NotEmpty(message="Please enter a valid email address")
//	@Email
//	@Pattern(regexp=".+@.+\\..+", message="Please enter a valid email address")
	private String emailId;
	
	@NotEmpty(message="Please enter your phone number without hyphens")
	@Pattern(regexp="[0-9]+", message="Please enter your phone number without hyphens, only numbers allowed")
	private String phoneNumber;
	
	@NotEmpty(message="Please enter your document ID")
	@Pattern(regexp="[A-Za-z0-9]+", message="Please enter your document ID, only alphanumeric characters allowed")
	@Size(min=8, max=12, message="Document ID should be between 8 and 12 characters")
	private String documentId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

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

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	
	
	
}
