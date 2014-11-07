package asu.bank.customer.viewBeans;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class InfoUpdateViewBean {
	@NotEmpty(message="Please enter the updated Name.")
	@Pattern(regexp="[A-Za-z ]+", message="Please enter a valid name without numbers in it")
	@Size(max=50)
	private String name;

	@Email
	@Pattern(regexp=".+@.+\\..+", message="Please enter a valid email address")
	@Size(max=50)
	private String emailID;
	
	@NotEmpty(message="Please enter your phone number without hyphens")
	@Pattern(regexp="[0-9]+", message="Please enter your phone number without hyphens, only numbers allowed")
	@Size(max=10)
	private String phoneNumber;
	
	@NotEmpty(message="Please enter the updated Address.")
	@Pattern(regexp="[a-zA-Z0-9#,\\- ]+", message="Please enter only alphabets, numbers and - or , symbols.")
	@Size(max=75)
	private String address;
	
	/*private String originalMerchantHashString;
	
	public String getOriginalMerchantHashString() {
		return originalMerchantHashString;
	}
	public void setOriginalMerchantHashString(String originalMerchantHashString) {
		this.originalMerchantHashString = originalMerchantHashString;
	}*/
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

}
