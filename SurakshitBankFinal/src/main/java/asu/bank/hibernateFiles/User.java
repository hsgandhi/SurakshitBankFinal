package asu.bank.hibernateFiles;

// Generated Oct 24, 2014 12:58:34 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * User generated by hbm2java
 */
@Entity
@Table(name = "user", catalog = "surakshit_bank", uniqueConstraints = {
		@UniqueConstraint(columnNames = "DocumentID"),
		@UniqueConstraint(columnNames = "PhoneNumber"),
		@UniqueConstraint(columnNames = "EmailID") })
public class User implements java.io.Serializable {

	private Integer userId;
	private String name;
	private String address;
	private String emailId;
	private long phoneNumber;
	private String documentId;
	private String password;
	private String role;
	//private Merchant merchant;
	private Set<Operation> operations = new HashSet(0);
	private Set<Account> accounts = new HashSet(0);
	private Customer customer;
	private Set<Transaction> transactionsForPrimaryParty = new HashSet(0);
	private Set<Transaction> transactionsForSecondaryParty = new HashSet(0);

	public User() {
	}

	public User(String name, String address, String emailId, long phoneNumber,
			String documentId, String password) {
		this.name = name;
		this.address = address;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
		this.documentId = documentId;
		this.password = password;
	}

	public User(String name, String address, String emailId, long phoneNumber,
			String documentId, String password, String role,
			//Merchant merchant,
			Set<Account> accounts ,
			Set<Operation> operations, Customer customer, Set<Transaction> transactionsForPrimaryParty,
			Set<Transaction> transactionsForSecondaryParty) {
		this.name = name;
		this.address = address;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
		this.documentId = documentId;
		this.password = password;
		this.role = role;
		//this.merchant = merchant;
		this.accounts=accounts;
		this.operations = operations;
		this.customer = customer;
		this.transactionsForPrimaryParty = transactionsForPrimaryParty;
		this.transactionsForSecondaryParty = transactionsForSecondaryParty;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "UserID", unique = true, nullable = false)
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "Name", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "Address", nullable = false, length = 75)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "EmailID", unique = true, nullable = false, length = 50)
	public String getEmailId() {
		return this.emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	@Column(name = "PhoneNumber", unique = true, nullable = false, precision = 10, scale = 0)
	public long getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Column(name = "DocumentID", unique = true, nullable = false, length = 10)
	public String getDocumentId() {
		return this.documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	@Column(name = "Password", nullable = false)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "Role", length = 20)
	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	/*@OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
	public Merchant getMerchant() {
		return this.merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}
*/
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set<Account> getAccounts() {
		return this.accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set<Operation> getOperations() {
		return this.operations;
	}

	public void setOperations(Set<Operation> operations) {
		this.operations = operations;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userByPrimaryParty")
	public Set<Transaction> getTransactionsForPrimaryParty() {
		return this.transactionsForPrimaryParty;
	}

	public void setTransactionsForPrimaryParty(Set<Transaction> transactionsForPrimaryParty) {
		this.transactionsForPrimaryParty = transactionsForPrimaryParty;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userBySecondaryParty")
	public Set<Transaction> getTransactionsForSecondaryParty() {
		return this.transactionsForSecondaryParty;
	}

	public void setTransactionsForSecondaryParty(
			Set<Transaction> transactionsForSecondaryParty) {
		this.transactionsForSecondaryParty = transactionsForSecondaryParty;
	}

/*	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set<Transaction> getLogins() {
		return this.logins;
	}

	public void setLogins(Set<Transaction> logins) {
		this.logins = logins;
	}*/

}