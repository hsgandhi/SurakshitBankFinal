package asu.bank.login.dao;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import asu.bank.hibernateFiles.User;
import asu.bank.utility.HibernateUtility;
import asu.bank.utility.SurakshitException;
import asu.bank.utility.UserDataUtility;

@Repository
public class LoginDaoImpl implements LoginDao {
	
	@Autowired
	HibernateUtility hibernateUtility;
	@Autowired
	UserDataUtility userDataUtility;
	
	@Override
	public void testRoom(String emailId, String age, String sex) throws SurakshitException,Exception {
		System.out.println("in dao");
		
		User user = userDataUtility.getUserDtlsFromEmailId(emailId);
		
	}


	@Override
	public boolean checkIfUserExists(String emailID) throws SurakshitException,
			Exception {
		User user = userDataUtility.getUserDtlsFromEmailId(emailID);
		
		return user==null?false:true;
	}


	@Override
	public void changePassword(String password, String emailID) throws SurakshitException,
			Exception {
		// TODO Auto-generated method stub
		User user = userDataUtility.getUserDtlsFromEmailId(emailID);
		
		if(user==null)
			throw new SurakshitException("UserNotFound");
		
		/*
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(user.getPassword());
		*/
		//user.setPassword(hashedPassword);
		user.setPassword(password);
		
		hibernateUtility.getSession().update(user);
	}

}
