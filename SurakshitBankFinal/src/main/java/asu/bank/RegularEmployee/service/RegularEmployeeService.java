package asu.bank.RegularEmployee.service;

import java.util.List;

import asu.bank.RegularEmployee.viewBeans.RegularEmployeeBean;
import asu.bank.RegularEmployee.viewBeans.TransactionBean;
import asu.bank.RegularEmployee.viewBeans.*;
import asu.bank.RegularEmployee.viewBeans.UserEmailPhoneBean;
import asu.bank.hibernateFiles.User;
import asu.bank.utility.SurakshitException;

public interface RegularEmployeeService {

	public RegularEmployeeBean employeeProfile() throws SurakshitException,Exception;
	
	public List<TransactionBean> getPendingTransactions() throws SurakshitException, Exception;
	
	public TransactionBean getTransaction(Integer transactionID) throws SurakshitException, Exception;

	public boolean rejectTransaction(Integer transactionID) throws SurakshitException, Exception;

	public boolean approveTransaction(Integer transactionID) throws SurakshitException, Exception;
	
	public boolean debitAccount(String emailID, Double amount) throws SurakshitException, Exception;
	
	public void creditAccount(String emailID, Double amount) throws SurakshitException, Exception;
	
	public boolean transferBetweenAccounts(String primaryPartyEmailID, String secondaryPartyEmailID, Double amount) throws SurakshitException, Exception;
	
	public boolean createNewTransaction(TransactionBean newTransactionBean) throws SurakshitException, Exception;

	public List<TransactionBean> getListTransactions(String emailId) throws SurakshitException, Exception;

	public boolean modifyTransaction(Integer transactionID, Double newAmount) throws SurakshitException, Exception;
	
	public void deleteTransaction(Integer transactionID) throws SurakshitException, Exception;

	public boolean createUser(UserBean userDetails) throws SurakshitException, Exception;

	public void modifyUser(UserBeanForModify userDetails,String oldEmailID) throws SurakshitException, Exception;
	
	public UserBean getUserFromEmailID(String emailID) throws SurakshitException, Exception;
	
	public boolean checkPhoneNumber(String emailId, String phoneNo) throws SurakshitException, Exception;

	/*public void rejectTransaction(Integer transactionID) throws SurakshitException, Exception;

	public TransactionAccountUserBean getTransaction(Integer transactionID)throws SurakshitException, Exception;

	public void modifyTransaction(TransactionAccountUserBean editedTransactionBean) throws SurakshitException, Exception;

	public void deleteTransaction(Integer transactionID) throws SurakshitException, Exception;*/
}
