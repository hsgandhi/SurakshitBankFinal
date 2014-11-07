package asu.bank.RegularEmployee.viewBeans;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class UserBean {
	
	@NotEmpty(message="Please enter a valid name without numbers in it")
	@Pattern(regexp="[A-Za-z ]+", message="Please enter a valid name without numbers in it")
	@Size(min=6, max=30, message="Name should be between 6 and 30 characters")
	private String name;
	
	@NotEmpty(message="Please enter your address")
	private String address;
	
	@NotEmpty(message="Please enter a valid email address")
	@Email(message = "Please enter a valid email address")
	@Pattern(regexp=".+@.+\\..+", message="Please enter a valid email address")
	@Size(min=6, max=30, message="EmailId should be between 6 and 30 characters")
	private String emailId;
	
	@NotEmpty(message="Please enter your phone number without hyphens")
	@Pattern(regexp="[0-9]+", message="Please enter your phone number without hyphens, only numbers allowed")
	@Size(min=9, max=10, message="Phone number should be of length 10 characters")
	private String phoneNumber;
	
	@NotEmpty(message="Please enter your document ID")
	@Pattern(regexp="[A-Za-z0-9]+", message="Please enter your document ID, only alphanumeric characters allowed")
	@Size(min=8, max=12, message="Document ID should be between 8 and 12 characters")
	private String documentId;
	
	@NotEmpty(message="Please enter a password")
	@Pattern(regexp="[A-Za-z0-9$@]+", message="Password can only contain aphanumeric characters with $ and @")
	@Size(min=6, max=20, message="Password should be between 6 and 20 characters")
	private String password;
	
	@NotEmpty(message="The role must be either CUSTOMER or MERCHANT")
	@Pattern(regexp="(CUSTOMER|MERCHANT)", message="Please enter either CUSTOMER or MERCHANT")
	private String role;
	
	private Integer userId;
	private String isAccountLocked;
	private String isAccountEnabled;
	
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getIsAccountLocked() {
		return isAccountLocked;
	}
	public void setIsAccountLocked(String isAccountLocked) {
		this.isAccountLocked = isAccountLocked;
	}
	public String getIsAccountEnabled() {
		return isAccountEnabled;
	}
	public void setIsAccountEnabled(String isAccountEnabled) {
		this.isAccountEnabled = isAccountEnabled;
	}
	
	
}
