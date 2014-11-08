package asu.bank.Admin.service;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import asu.bank.Admin.dao.AdminDao;
import asu.bank.Admin.viewBeans.AccountBean;
import asu.bank.Admin.viewBeans.AdminBean;
import asu.bank.Admin.viewBeans.InternalUserBeanCreate;
import asu.bank.Admin.viewBeans.InternalUserBeanModify;
import asu.bank.Admin.viewBeans.TransactionBean;
import asu.bank.RegularEmployee.dao.RegularEmployeeDao;
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
	
	@Autowired
	RegularEmployeeDao regEmpDao;
	
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
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(internalUser.getPassword());
		internalUser.setPassword(hashedPassword);
		User user = adminDao.createUser(internalUser);
		
		regEmpDao.enterDataInUserAttempts(user);
		
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
	
	
	@Override
	public void rejectExtUser(String emailID) throws SurakshitException,
			Exception {
		User user = userDataUtility.getUserDtlsFromEmailId(emailID);
		adminDao.deleteEntryInUserAttempts(user);
		adminDao.deleteEntryInUser(emailID);
	}	
	
	@Override
	public List<UserBean> getListInternalUsers() throws SurakshitException,
			Exception {
		List<User> employeeListReturned = adminDao.getInternalUsersList();
		//convert user to userbean
		List<UserBean> employeeList = new ArrayList<UserBean>();
		for(User u : employeeListReturned){
			UserBean local = new UserBean();
			local.setAddress(u.getAddress());
			local.setDocumentId(u.getDocumentId());
			local.setEmailId(u.getEmailId());
			local.setIsAccountEnabled(u.getIsAccountEnabled());
			local.setIsAccountLocked(u.getIsAccountLocked());
			local.setName(u.getName());
			local.setPassword(u.getPassword());
			local.setPhoneNumber(u.getPhoneNumber().toString());
			local.setRole(u.getRole());
			local.setUserId(u.getUserId());
			employeeList.add(local);
		}
		return employeeList;
	}

	@Override
	public void deleteInternalUser(String emailID)
			throws SurakshitException, Exception {
		rejectExtUser(emailID);
	}

	@Override
	public UserBean getPIIDtls(String emailId) throws SurakshitException, Exception {
		User user = userDataUtility.getUserDtlsFromEmailId(emailId);
		UserBean userBean = new UserBean();
		userBean.setAddress(user.getAddress());
		userBean.setDocumentId(user.getDocumentId());
		userBean.setEmailId(user.getEmailId());
		userBean.setName(user.getName());
		userBean.setPhoneNumber(user.getPhoneNumber().toString());
		
		return userBean;
	}

	@Override
	public boolean modifyInternalUser(InternalUserBeanModify internalUser)
			throws SurakshitException, Exception {
		boolean success = adminDao.modifyUser(internalUser);
		if(success){
			return true;
		}else{
			return false;
		}
	}	
	
	@Override
	public List<UserBean> getListExternalUsers() throws SurakshitException,
			Exception {
		List<User> employeeListReturned = adminDao.getExternalUsersList();
		
		List<UserBean> employeeList = new ArrayList<UserBean>();
		for(User u : employeeListReturned){
			UserBean local = new UserBean();
			local.setAddress(u.getAddress());
			local.setDocumentId(u.getDocumentId());
			local.setEmailId(u.getEmailId());
			local.setIsAccountEnabled(u.getIsAccountEnabled());
			local.setIsAccountLocked(u.getIsAccountLocked());
			local.setName(u.getName());
			local.setPassword(u.getPassword());
			local.setPhoneNumber(u.getPhoneNumber().toString());
			local.setRole(u.getRole());
			local.setUserId(u.getUserId());
			employeeList.add(local);
		}
		return employeeList;
	}	
	
	@Override
	public void deleteExternalUser(String emailID) throws SurakshitException,
			Exception {
		rejectExtUser(emailID);
		
	}

}//end class