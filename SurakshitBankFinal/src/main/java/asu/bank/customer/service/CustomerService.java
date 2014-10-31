package asu.bank.customer.service;

import asu.bank.customer.viewBeans.AccountViewBean;
import asu.bank.utility.SurakshitException;

public interface CustomerService {

	String getAccountNo(String username) throws SurakshitException, Exception;

	Double customerAddBalance(AccountViewBean accountViewBean)throws SurakshitException, Exception;


}
