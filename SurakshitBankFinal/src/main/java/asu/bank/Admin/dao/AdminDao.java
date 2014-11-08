package asu.bank.Admin.dao;

import java.util.List;

import asu.bank.Admin.viewBeans.*;
import asu.bank.RegularEmployee.viewBeans.UserBean;
import asu.bank.hibernateFiles.Account;
import asu.bank.hibernateFiles.User;
import asu.bank.utility.SurakshitException;


public interface AdminDao {

	public void createTransaction(TransactionBean transactionToBeCreated) throws SurakshitException, Exception;
	
	public void updateTransaction(TransactionBean transactionToBeUpdated) throws SurakshitException, Exception;
	
	public TransactionBean getTransaction(Integer transactionID) throws SurakshitException, Exception;
	
	public void deleteTransaction(Integer transactionID) throws SurakshitException, Exception;
		
	public AdminBean employeeProfile() throws SurakshitException, Exception;
	
    public UserBean getUser(String userEmailID) throws SurakshitException, Exception;
    
    public UserBean getUser(String emailID, String phoneNum)throws SurakshitException, Exception;
	
	/*public void updateUser(ModifyOperationsBean userToBeUpdated, String oldEmailID) throws SurakshitException, Exception;*/
	
	public List<TransactionBean> getPendingTransactions()throws SurakshitException, Exception;

	public List<OperationBean> getPendingOperations() throws SurakshitException, Exception ;

	/*public void createUser(UserBean userToBeCreated) throws SurakshitException, Exception;*/
	
	public void updateAccount(AccountBean accountBean)throws SurakshitException, Exception;

	public AccountBean getAccountFromUserID(Integer userId)throws SurakshitException, Exception;

	public OperationBean getOperation(Integer operationID)throws SurakshitException, Exception;

	public List<UserBean> getPendingExtUsers() throws SurakshitException, Exception;

	public void updateUser(UserBean user, String emailID, byte[] publicKeyByteArray) throws SurakshitException, Exception;

	public void createAccount(Integer userId, double d) throws SurakshitException, Exception;
	
	public User createUser(InternalUserBeanCreate internalUser) throws SurakshitException, Exception;
	
	public void deleteEntryInUserAttempts(User user) throws SurakshitException, Exception;

	public void deleteEntryInUser(String emailID) throws SurakshitException, Exception;
	
	public List<User> getInternalUsersList() throws SurakshitException, Exception;
	
	public boolean modifyUser(InternalUserBeanModify internalUser) throws SurakshitException, Exception;
	
	public List<User> getExternalUsersList() throws SurakshitException, Exception;

}


