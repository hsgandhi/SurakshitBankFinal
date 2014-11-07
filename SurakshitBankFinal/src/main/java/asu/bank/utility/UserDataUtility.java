package asu.bank.utility;

import java.util.Set;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import asu.bank.hibernateFiles.Account;
import asu.bank.hibernateFiles.User;

@Repository
public class UserDataUtility {
	
	@Autowired
	HibernateUtility hibernateUtility;
	
	public User getUserDtlsFromEmailId(String emailId) throws SurakshitException, Exception
	{
		User user = (User)hibernateUtility.getSession().createCriteria(User.class).add(Restrictions.eq("emailId", emailId)).uniqueResult();
		
		if(user!=null)
			return user ;
		else
			throw new SurakshitException("UserNotFound");
	}
	
	public String getAccountNo(String username) throws SurakshitException,
	Exception {
		String accountNo=null;
		User user = this.getUserDtlsFromEmailId(username);

		Set<Account> accounts=user.getAccounts();

		for(Account account:accounts)
		{
			accountNo=account.getAccountId().toString();
		}

		return accountNo;
}
	
	public User getUserDtlsFromUserId(Integer userId) throws SurakshitException, Exception{
		User user = (User)hibernateUtility.getSession().createCriteria(User.class).add(Restrictions.eq("userId", userId)).uniqueResult();
		if(user!=null)
			return user ;
		else
			throw new SurakshitException("UserNotFound");
	}
	

	public String getBalance(String username) throws SurakshitException, Exception{
		// TODO Auto-generated method stub
		String balance=null;
		
		User user =this.getUserDtlsFromEmailId(username);
		
		Set<Account> accounts=user.getAccounts();
		
		for(Account account:accounts)
		{
			balance=account.getBalance().toString();
		}
		return balance;
	}

}
