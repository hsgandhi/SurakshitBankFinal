package asu.bank.customer.dao;

import asu.bank.customer.viewBeans.AccountViewBean;
import asu.bank.utility.SurakshitException;

public interface CustomerDao {

	public String getAccountNo(String username) throws SurakshitException,Exception;

	public Double customerAddBalance(AccountViewBean accountViewBean)throws SurakshitException,Exception;

}
