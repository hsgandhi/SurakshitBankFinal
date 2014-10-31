package asu.bank.customer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import asu.bank.customer.viewBeans.AccountViewBean;
import asu.bank.hibernateFiles.Account;
import asu.bank.utility.HibernateUtility;
import asu.bank.utility.SurakshitException;
import asu.bank.utility.UserDataUtility;

@Repository
public class CustomerDaoImpl implements CustomerDao {

	@Autowired
	HibernateUtility hibernateUtility;
	@Autowired
	UserDataUtility userDataUtility;
	
	@Override
	public String getAccountNo(String username) throws SurakshitException,
			Exception {
		String accountNo=userDataUtility.getAccountNo(username);
		
		if(accountNo==null)
			throw new SurakshitException("NoAccountAvailable");
		
		return accountNo;
	}

	@Override
	public Double customerAddBalance(AccountViewBean accountViewBean)
			throws SurakshitException, Exception {
		
		Account account = (Account)hibernateUtility.getSession().get(Account.class, Integer.parseInt(accountViewBean.getAccountId()));
		
		Double finalBalance=account.getBalance()+ Double.parseDouble(accountViewBean.getCurrency());
		
		account.setBalance(finalBalance);
		
		hibernateUtility.getSession().update(account);
		
		return finalBalance;
	}
}
