package asu.bank.Admin.service;

import java.util.List;

import asu.bank.Admin.viewBeans.AdminBean;
import asu.bank.Admin.viewBeans.InternalUserBeanCreate;
import asu.bank.Admin.viewBeans.TransactionBean;
import asu.bank.RegularEmployee.viewBeans.UserBean;
import asu.bank.utility.SurakshitException;

public interface AdminService {

	public AdminBean employeeProfile() throws SurakshitException,Exception;
	
	/*public List<OperationBean> getPendingOperations() throws SurakshitException, Exception;
	
	public boolean approveOperation(Integer operationID) throws SurakshitException, Exception;
	
	public boolean rejectOperation(Integer operationID) throws SurakshitException, Exception;*/
	
	public List<TransactionBean> getPendingTransactions() throws SurakshitException, Exception;

	public boolean rejectTransaction(Integer transactionID) throws SurakshitException, Exception;

	public boolean approveTransaction(Integer transactionID) throws SurakshitException, Exception;
	
	public boolean transferBetweenAccounts(String primaryPartyEmailID,String secondaryPartyEmailID, Double amount)throws SurakshitException, Exception;
	
	/*public boolean modifyTransaction(Integer transactionID, Double newAmount) throws SurakshitException, Exception;*/

	/*public void deleteTransaction(Integer transactionID) throws SurakshitException, Exception ;*/

	boolean debitAccount(String emailID, Double amount)throws SurakshitException, Exception;

	void creditAccount(String emailID, Double amount)throws SurakshitException, Exception;

	/*public boolean createNewTransaction(TransactionBean toBeCreated)throws SurakshitException, Exception;*/

	public TransactionBean getTransaction(Integer transactionID)throws SurakshitException, Exception;

	/*public boolean createUser(UserBean userDetails)throws SurakshitException, Exception;

	public UserBean getUserFromEmailID(String oldEmailID)throws SurakshitException, Exception;

	public void modifyUser(ModifyOperationsBean userDetails, String oldEmailID)throws SurakshitException, Exception;
*/
	public boolean createInternalUser(InternalUserBeanCreate internalUser)throws SurakshitException, Exception;

	public List<UserBean> getPendingExtUsers() throws SurakshitException, Exception;
	
	public boolean doesUserExists(String emailID, String phoneNum) throws SurakshitException, Exception;

	public void approveExtUser(String emailID) throws SurakshitException, Exception;

	/*public void rejectTransaction(Integer transactionID) throws SurakshitException, Exception;

	public TransactionAccountUserBean getTransaction(Integer transactionID)throws SurakshitException, Exception;

	public void modifyTransaction(TransactionAccountUserBean editedTransactionBean) throws SurakshitException, Exception;

	public void deleteTransaction(Integer transactionID) throws SurakshitException, Exception;*/
;
}
