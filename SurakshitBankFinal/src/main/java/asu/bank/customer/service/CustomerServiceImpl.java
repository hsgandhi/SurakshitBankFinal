package asu.bank.customer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import asu.bank.customer.dao.CustomerDao;
import asu.bank.customer.viewBeans.AccountViewBean;
import asu.bank.utility.SurakshitException;

@Service
@Transactional(propagation=Propagation.REQUIRED,rollbackFor={SurakshitException.class,Exception.class})
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	CustomerDao customerDao;

	@Override
	public String getAccountNo(String username) throws SurakshitException,
			Exception {
		return customerDao.getAccountNo(username);
	}

	@Override
	public Double customerAddBalance(AccountViewBean accountViewBean)
			throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		return customerDao.customerAddBalance(accountViewBean);
	}

}
