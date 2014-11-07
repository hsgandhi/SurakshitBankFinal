package asu.bank.Admin.service;

import java.security.KeyPair;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import asu.bank.Admin.dao.AdminDao;
import asu.bank.Admin.dao.AdminDaoImpl;
import asu.bank.Admin.viewBeans.AccountBean;
import asu.bank.Admin.viewBeans.AdminBean;
import asu.bank.Admin.viewBeans.InternalUserBeanCreate;
import asu.bank.Admin.viewBeans.TransactionBean;
import asu.bank.RegularEmployee.viewBeans.UserBean;
import asu.bank.hibernateFiles.User;
import asu.bank.utility.EmailUtilityUsingSSL;
import asu.bank.utility.KeyGenerataionUtility;
import asu.bank.utility.SurakshitException;
import asu.bank.utility.UserDataUtility;

@Service
@Transactional(propagation=Propagation.REQUIRED,rollbackFor={SurakshitException.class,Exception.class})
public class AdminServiceImpl implements AdminService {

	@Autowired
	AdminDao adminDao;
	
	@Autowired
	UserDataUtility userDataUtility;
	
	@Autowired
	KeyGenerataionUtility keyGenerationUtility; 
	
	@Autowired
	EmailUtilityUsingSSL emailUtilityUsingSSL;
	
	private static final Logger logger = Logger.getLogger(AdminServiceImpl.class);
	 private static final Logger secureLogger = Logger.getLogger("secure");

	
	@Override
	public AdminBean employeeProfile() throws SurakshitException,
			Exception {
		return adminDao.employeeProfile();
		
	}
	
	@Override
	public TransactionBean getTransaction(Integer transactionID)
			throws SurakshitException, Exception {
		return adminDao.getTransaction(transactionID);
	}
	
				
	@Override
	public List<TransactionBean> getPendingTransactions()
			throws SurakshitException, Exception {
		return adminDao.getPendingTransactions();
	}
	
	@Override
	public boolean rejectTransaction(Integer transactionID)
			throws SurakshitException, Exception {
		TransactionBean transaction = adminDao.getTransaction(transactionID);
		if(transaction.getTransactionCurrentStatus().equals("PendingApproval")){
			transaction.setTransactionCurrentStatus("Rejected");
			adminDao.updateTransaction(transaction);
			return true;
		}
		return false;
	}
	
	
	@Override
	public boolean approveTransaction(Integer transactionID)
			throws SurakshitException, Exception {
		TransactionBean transaction = adminDao.getTransaction(transactionID);
		if(transaction.getTransactionCurrentStatus().equals("PendingApproval")){
			if(transaction.getTransactionType().equals("DEBIT")){
				if(debitAccount(transaction.getPrimaryUserEmail(), transaction.getTransactionAmount())){
					transaction.setTransactionCurrentStatus("Approved");
					adminDao.updateTransaction(transaction);
					return true;
				}else{
					transaction.setTransactionCurrentStatus("Rejected");
					adminDao.updateTransaction(transaction);
					return false;
				}
			}else if(transaction.getTransactionType().equals("CREDIT")){
				creditAccount(transaction.getPrimaryUserEmail(), transaction.getTransactionAmount());
				transaction.setTransactionCurrentStatus("Approved");
				adminDao.updateTransaction(transaction);
				return true;
			}else if(transaction.getTransactionType().equals("PAYMENT")){
				if(transferBetweenAccounts(transaction.getPrimaryUserEmail(),transaction.getSecondaryUserEmail(),transaction.getTransactionAmount())){
					transaction.setTransactionCurrentStatus("Approved");
					adminDao.updateTransaction(transaction);
					return true;
				}else{
					transaction.setTransactionCurrentStatus("Rejected");
					adminDao.updateTransaction(transaction);
					return false;
				}
			}
		}
		return false;
	}

	@Override
	public boolean transferBetweenAccounts(String primaryPartyEmailID,
			String secondaryPartyEmailID, Double amount)
			throws SurakshitException, Exception {
		boolean success = debitAccount(primaryPartyEmailID, amount);
		if(success == true){
			creditAccount(secondaryPartyEmailID, amount);
			return true;
		}
		return false;
	}
	@Override
	public boolean debitAccount(String emailID, Double amount)
			throws SurakshitException, Exception {
		User user = userDataUtility.getUserDtlsFromEmailId(emailID);
		AccountBean accountBean = adminDao.getAccountFromUserID(user.getUserId());
		if(accountBean.getBalance() > amount){
			Double temp = accountBean.getBalance();
			accountBean.setBalance(temp - amount);
			adminDao.updateAccount(accountBean);
			return true;
		}
		return false;
	}
	
	
	
	@Override
	public void creditAccount(String emailID, Double amount)throws SurakshitException, Exception {
		User user = userDataUtility.getUserDtlsFromEmailId(emailID);
		AccountBean accountBean = adminDao.getAccountFromUserID(user.getUserId());
		Double temp = accountBean.getBalance();
		accountBean.setBalance(temp + amount);
		adminDao.updateAccount(accountBean);
	}
	
	@Override
	public boolean createInternalUser(InternalUserBeanCreate internalUser)
			throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		adminDao.createUser(internalUser);
		return true;
	}

	
	/*Gets the list of external users pending approval*/
	@Override
	public List<UserBean> getPendingExtUsers() throws SurakshitException,
			Exception {
		return adminDao.getPendingExtUsers();
	}

	@Override
	public boolean doesUserExists(String emailID, String phoneNum)
			throws SurakshitException, Exception {
		UserBean user = adminDao.getUser(emailID, phoneNum);
		if(user == null){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public void approveExtUser(String emailID) throws SurakshitException,
			Exception {
		KeyPair privatePublicPair = keyGenerationUtility.generateKeyPair();
		byte[] publicKeyByteArray = privatePublicPair.getPublic().getEncoded();
		
		UserBean userBean = adminDao.getUser(emailID);
		userBean.setIsAccountEnabled(Integer.toString(1));
		adminDao.updateUser(userBean, emailID,publicKeyByteArray);
		User user = userDataUtility.getUserDtlsFromEmailId(emailID);
		adminDao.createAccount(user.getUserId(), 0.0);
		
		String privateKey = new String(Base64.encode(privatePublicPair.getPrivate().getEncoded()));
		emailUtilityUsingSSL.sendMailWithAttachmentForJar(emailID, privateKey, "Private key for critical transactions.");
		
	}	
	
}//end class
	






	/*@Override
	public List<OperationBean> getPendingOperations() throws SurakshitException, Exception
	{
		return adminDao.getPendingOperations();
	}
	
	@Override
	public boolean rejectOperation(Integer operationID)
		throws SurakshitException, Exception {
	OperationBean operation = adminDao.getOperation(operationID);
	if(operation.getOperationCurrentStatus().equals("PendingApproval")){
		operation.setOperationCurrentStatus("Rejected");
		return true;
	}
	return false;
	}
	@Override
	public boolean approveOperation(Integer operationID)
		throws SurakshitException, Exception {
	OperationBean operation = adminDao.getOperation(operationID);
	if(operation.getOperationCurrentStatus().equals("PendingApproval")){
		operation.setOperationCurrentStatus("Approved");
		return true;
	}
	return false;}*/

	/*@Override
	public void modifyUser(ModifyOperationsBean userDetails, String oldEmailID)throws SurakshitException, Exception {
		adminDao.updateUser(userDetails, oldEmailID);
	}
	
	public UserBean getUserFromEmailID(String emailID) throws SurakshitException, Exception{
		UserBean user = adminDao.getUser(emailID);
		if(user == null){
			return null;
		}else{
			return user;
		}
	}
	*/

	/*@Override
	public boolean createNewTransaction(TransactionBean newTransactionBean)
			throws SurakshitException, Exception {
		User primaryUser = userDataUtility.getUserDtlsFromEmailId(newTransactionBean.getPrimaryUserEmail());
		User secondaryUser = userDataUtility.getUserDtlsFromEmailId(newTransactionBean.getSecondaryUserEmail());
		if(primaryUser == null || secondaryUser == null){
			return false;
		}		
		if(newTransactionBean.getTransactionAmount() >= 50000.0){
			if(newTransactionBean.getTransactionType().equals("DEBIT")){
				if(!newTransactionBean.getPrimaryUserEmail().equals(newTransactionBean.getSecondaryUserEmail())){
					return false;
				}					
				if(!debitAccount(newTransactionBean.getPrimaryUserEmail(), (Double)newTransactionBean.getTransactionAmount()))
					return false;
			}else if(newTransactionBean.getTransactionType().equals("CREDIT")){
				if(!newTransactionBean.getPrimaryUserEmail().equals(newTransactionBean.getSecondaryUserEmail())){
					return false;
				}
				creditAccount(newTransactionBean.getPrimaryUserEmail(), (Double)newTransactionBean.getTransactionAmount());
			}else if(newTransactionBean.getTransactionType().equals("PAYMENT")){
				if(!transferBetweenAccounts(newTransactionBean.getPrimaryUserEmail(), newTransactionBean.getSecondaryUserEmail(), (Double)newTransactionBean.getTransactionAmount()))
					return false;
			}	
			newTransactionBean.setTransactionCurrentStatus("Approved");
			adminDao.createTransaction(newTransactionBean);
			return true;
		}else{
			newTransactionBean.setTransactionCurrentStatus("PendingApproval");
			adminDao.createTransaction(newTransactionBean);
			return true;
		}
	}*/


	/*@Override
	public boolean createUser(UserBean userDetails) throws SurakshitException,Exception {
		
		if(adminDao.getUser(userDetails.getEmailId()) != null){
			return false;
		}
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(userDetails.getPassword());
		userDetails.setPassword(hashedPassword);
		userDetails.setIsAccountEnabled(Integer.toString(0));
		userDetails.setIsAccountLocked(Integer.toString(0));
	    adminDao.createUser(userDetails);
		return true;
	}*/

	/*@Override
	public boolean modifyTransaction(Integer transactionID, Double newAmount)
			throws SurakshitException, Exception {
		TransactionBean oldTransaction = adminDao.getTransaction(transactionID);
		
		if(oldTransaction.getTransactionCurrentStatus().equals("Approved")){
			throw new SurakshitException("CantModifyApprovedTransaction");
		}else if(oldTransaction.getTransactionCurrentStatus().equals("PendingApproval")){
			deleteTransaction(oldTransaction.getTransactionId());
			return true;
		}
		return false;
	}*/

	/*
	@Override
	public void deleteTransaction(Integer transactionID)
			throws SurakshitException, Exception {
		TransactionBean transaction = adminDao.getTransaction(transactionID);
		if(transaction.getTransactionCurrentStatus().equals("Approved")){
			throw new SurakshitException("CantDeleteApprovedTransaction");
		}else if(transaction.getTransactionCurrentStatus().equals("PendingApproval")){
			adminDao.deleteTransaction(transactionID);
		}
		
	}*/
	
	
	/*
	@Override
	public TransactionAccountUserBean getTransaction(Integer transactionID)
			throws SurakshitException, Exception {
		return adminDao.getTransaction(transactionID);
	}

	@Override
	public void createTransaction(TransactionAccountUserBean newTransactionBean)
			throws SurakshitException, Exception {
		if(newTransactionBean.getTransactionAmount() >= 50000.0){
			newTransactionBean.setTransactionCurrentStatus("Approved");
		}else{
			newTransactionBean.setTransactionCurrentStatus("PendingApproval");
		}
		adminDao.createTransaction(newTransactionBean);
	}

	@Override
	public void modifyTransaction(TransactionAccountUserBean editedTransactionBean)
			throws SurakshitException, Exception {
		//TODO
		
	}

	@Override
	public void deleteTransaction(Integer transactionID) throws SurakshitException, Exception {
		TransactionAccountUserBean toBeDeletedTransaction = adminDao.getTransaction(transactionID);
		String transactionType = toBeDeletedTransaction.getTransactionType();
		if(transactionType.equals("DEBIT")){
			//Since he had withdrawn the money in the transaction, add that amount now
		}else if(transactionType.equals("CREDIT")){
			//Since he had deposited money in the transaction, reduce that amount now
		}else if(transactionType.equals("PAYMENT")){
			//The primary party should be added the amount and the secondary party should be reduced the amount
		}
		
	}
*/

