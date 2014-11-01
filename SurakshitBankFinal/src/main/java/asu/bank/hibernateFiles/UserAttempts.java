package asu.bank.hibernateFiles;

// Generated Nov 1, 2014 1:20:02 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * UserAttempts generated by hbm2java
 */
@Entity
@Table(name = "user_attempts", catalog = "surakshit_bank", uniqueConstraints = @UniqueConstraint(columnNames = "UserID"))
public class UserAttempts implements java.io.Serializable {

	private Integer id;
	private User user;
	private Integer noOfAttempts;
	private Date lastUpdated;

	public UserAttempts() {
	}

	public UserAttempts(User user, Date lastUpdated) {
		this.user = user;
		this.lastUpdated = lastUpdated;
	}

	public UserAttempts(User user, Integer noOfAttempts, Date lastUpdated) {
		this.user = user;
		this.noOfAttempts = noOfAttempts;
		this.lastUpdated = lastUpdated;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UserID", unique = true, nullable = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "NoOfAttempts")
	public Integer getNoOfAttempts() {
		return this.noOfAttempts;
	}

	public void setNoOfAttempts(Integer noOfAttempts) {
		this.noOfAttempts = noOfAttempts;
	}

	@Column(name = "LastUpdated", nullable = false, length = 0)
	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}
