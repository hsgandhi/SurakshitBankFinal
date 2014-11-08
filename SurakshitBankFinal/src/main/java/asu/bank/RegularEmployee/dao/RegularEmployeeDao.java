package asu.bank.RegularEmployee.dao;

import java.util.List;

import asu.bank.RegularEmployee.viewBeans.AccountBean;
import asu.bank.RegularEmployee.viewBeans.RegularEmployeeBean;
import asu.bank.RegularEmployee.viewBeans.TransactionBean;
import asu.bank.RegularEmployee.viewBeans.UserBean;
import asu.bank.RegularEmployee.viewBeans.UserBeanForModify;
import asu.bank.hibernateFiles.User;
import asu.bank.utility.SurakshitException;

public interface RegularEmployeeDao {

	public RegularEmployeeBean employeeProfile() throws SurakshitException,Exception;
	
	public List<TransactionBean> getPendingTransactions() throws SurakshitException, Exception;

	/*
	public void approveTransaction(Integer transactionID) throws SurakshitException, Exception;

	public void rejectTransaction(Integer transactionID) throws SurakshitException, Exception;

	public TransactionAccountUserBean getTransaction(Integer transactionID) throws SurakshitException, Exception;

	public void createTransaction(TransactionAccountUserBean newTransactionBean) throws SurakshitException, Exception;*/
	
	public void createTransaction(TransactionBean transactionToBeCreated) throws SurakshitException, Exception;
	
	public void updateTransaction(TransactionBean transactionToBeUpdated) throws SurakshitException, Exception;
	
	public TransactionBean getTransaction(Integer transactionID) throws SurakshitException, Exception;
	
	public void deleteTransaction(Integer transactionID) throws SurakshitException, Exception;
	
	public AccountBean getAccountFromAccountID(Integer accountID) throws SurakshitException, Exception;
	
	public AccountBean getAccountFromUserID(Integer userID) throws SurakshitException, Exception;
	
	public void updateAccount(AccountBean accountToBeUpdated) throws SurakshitException, Exception;
	
	public User createUser(UserBean userToBeCreated) throws SurakshitException, Exception;
	
	public UserBean getUser(String userEmailID) throws SurakshitException, Exception;
	
	public void updateUser(UserBeanForModify userToBeUpdated, String oldEmailID) throws SurakshitException, Exception;

	public List<TransactionBean> getTransactionListBasedOnUserID(Integer userId) throws SurakshitException, Exception;

	public void enterDataInUserAttempts(User user)throws SurakshitException, Exception;
	
	public boolean checkUserExists(String emailID, String phoneNum) throws SurakshitException, Exception;
}
