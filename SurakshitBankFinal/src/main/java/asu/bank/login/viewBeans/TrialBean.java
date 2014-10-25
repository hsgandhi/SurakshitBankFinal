package asu.bank.login.viewBeans;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class TrialBean {
	
	@Size(min=2, max=30 , message= "Name must be between 2 and 30 characters")
	private String name;
	
	@NotEmpty(message="Email cannot be empty")
	@Email(message="Email must be in proper format")
	private String mail;
	
	@NotEmpty(message="Dob cannot be empty")
	private String dob;
	 
	 @NotNull(message="Tp cannot be null")
	 private Integer tp;
	 
	 @NotNull(message="Currency cannot be null")
	 private Double currency;

	public Integer getTp() {
		return tp;
	}

	public void setTp(Integer tp) {
		this.tp = tp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public Double getCurrency() {
		return currency;
	}

	public void setCurrency(Double currency) {
		this.currency = currency;
	}

}
