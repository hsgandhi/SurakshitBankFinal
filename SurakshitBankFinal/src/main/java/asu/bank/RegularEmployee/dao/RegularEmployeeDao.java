package asu.bank.RegularEmployee.dao;

import java.util.List;

import asu.bank.RegularEmployee.viewBeans.RegularEmployeeBean;
import asu.bank.RegularEmployee.viewBeans.TransactionAccountUserBean;
import asu.bank.utility.SurakshitException;

public interface RegularEmployeeDao {

	public RegularEmployeeBean employeeProfile() throws SurakshitException,Exception;
	
	public List<TransactionAccountUserBean> getPendingTransactions() throws SurakshitException, Exception;

	public void approveTransaction(Integer transactionID) throws SurakshitException, Exception;

	public void rejectTransaction(Integer transactionID) throws SurakshitException, Exception;

	public TransactionAccountUserBean getTransaction(Integer transactionID) throws SurakshitException, Exception;

	public void createTransaction(TransactionAccountUserBean newTransactionBean) throws SurakshitException, Exception;
}
