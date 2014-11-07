package asu.bank.Admin.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import asu.bank.Admin.viewBeans.AccountBean;
import asu.bank.Admin.viewBeans.AdminBean;
import asu.bank.Admin.viewBeans.InternalUserBeanCreate;
import asu.bank.Admin.viewBeans.OperationBean;
import asu.bank.Admin.viewBeans.TransactionBean;
import asu.bank.RegularEmployee.viewBeans.UserBean;
import asu.bank.hibernateFiles.Account;
import asu.bank.hibernateFiles.Operation;
import asu.bank.hibernateFiles.Transaction;
import asu.bank.hibernateFiles.User;
import asu.bank.utility.HibernateUtility;
import asu.bank.utility.SurakshitException;
import asu.bank.utility.UserDataUtility;
@Repository
public class AdminDaoImpl implements AdminDao{
	
	@Autowired
	HibernateUtility hibernateUtility;
	@Autowired
	UserDataUtility userDataUtility;
	
	private static final Logger logger = Logger.getLogger(AdminDaoImpl.class);
	 private static final Logger secureLogger = Logger.getLogger("secure");

	
	@Override
	public void createTransaction(TransactionBean transactionToBeCreated)
			throws SurakshitException, Exception {
		Session session = hibernateUtility.getSession();
		Transaction transaction = new Transaction();
		transaction.setTransactionAmount(transactionToBeCreated.getTransactionAmount());
		transaction.setTransactionCurrentStatus(transactionToBeCreated.getTransactionCurrentStatus());
		transaction.setTransactionType(transactionToBeCreated.getTransactionType());
		transaction.setUserByPrimaryParty(userDataUtility.getUserDtlsFromEmailId(transactionToBeCreated.getPrimaryUserEmail()));
		transaction.setUserBySecondaryParty(userDataUtility.getUserDtlsFromEmailId(transactionToBeCreated.getSecondaryUserEmail()));
		session.save(transaction);		
	}
	
	@Override
	public void updateTransaction(TransactionBean transactionToBeUpdated)
			throws SurakshitException, Exception {
		Session session = hibernateUtility.getSession();
		Transaction transaction = (Transaction) session.createQuery("From Transaction where transactionId = :tID").setParameter("tID", transactionToBeUpdated.getTransactionId()).uniqueResult();
		if(transactionToBeUpdated.getTransactionAmount() != null )
			transaction.setTransactionAmount(transactionToBeUpdated.getTransactionAmount());
		if(transactionToBeUpdated.getTransactionCurrentStatus() != null)
			transaction.setTransactionCurrentStatus(transactionToBeUpdated.getTransactionCurrentStatus());		
		if(transactionToBeUpdated.getTransactionType() != null )
			transaction.setTransactionType(transactionToBeUpdated.getTransactionType());
		/* Since only transactionAmount can be updated, the following not needed.*/
		/*
		if(transactionToBeUpdated.getPrimaryUserEmail() != null)
			transaction.setUserByPrimaryParty(userDataUtility.getUserDtlsFromEmailId(transactionToBeUpdated.getPrimaryUserEmail()));
		if(transactionToBeUpdated.getSecondaryUserEmail() != null)
			transaction.setUserBySecondaryParty(userDataUtility.getUserDtlsFromEmailId(transactionToBeUpdated.getSecondaryUserEmail()));*/
		session.save(transaction);
	}
	@Override
	public OperationBean getOperation(Integer operationID)
			throws SurakshitException, Exception {
		Session session = hibernateUtility.getSession();
		Operation operation = (Operation) session.createQuery("From Operation where operationId = :tID").setParameter("tID", operationID).uniqueResult();
		OperationBean operationBean = new OperationBean();
		//operationBean.setOperationCreatedAt(operation.getOperationCreatedAt());
		operationBean.setOperationCurrentStatus(operation.getOperationCurrentStatus());
		operationBean.setOperationId(operation.getOperationId());
		operationBean.setOperationType(operation.getOperationType());
		return operationBean;
	}
	@Override
	public AccountBean getAccountFromUserID(Integer userID)	throws SurakshitException, Exception {
		Session session = hibernateUtility.getSession();
		User user = userDataUtility.getUserDtlsFromUserId(userID);
		Account account = (Account) session.createQuery("From Account where userId = :uID").setParameter("uID", user).uniqueResult();
		AccountBean accountToBeReturned = new AccountBean();
		accountToBeReturned.setAccountId(account.getAccountId());
		accountToBeReturned.setBalance(account.getBalance());
		accountToBeReturned.setUserEmailID(account.getUser().getEmailId());
		return accountToBeReturned;
	}
	
	@Override
	public TransactionBean getTransaction(Integer transactionID)
			throws SurakshitException, Exception {
		Session session = hibernateUtility.getSession();
		Transaction transaction = (Transaction) session.createQuery("From Transaction where transactionId = :tID").setParameter("tID", transactionID).uniqueResult();
		TransactionBean transactionBean = new TransactionBean();
		transactionBean.setPrimaryUserEmail(transaction.getUserByPrimaryParty().getEmailId());
		transactionBean.setSecondaryUserEmail(transaction.getUserBySecondaryParty().getEmailId());
		transactionBean.setTransactionAmount(transaction.getTransactionAmount());
		transactionBean.setTransactionCreatedAt(transaction.getTransactionCreatedAt());
		transactionBean.setTransactionCurrentStatus(transaction.getTransactionCurrentStatus());
		transactionBean.setTransactionId(transaction.getTransactionId());
		transactionBean.setTransactionType(transaction.getTransactionType());
		return transactionBean;
	}
	
	
	@Override
	public void deleteTransaction(Integer transactionID)
			throws SurakshitException, Exception {
		Session session = hibernateUtility.getSession();
		session.createQuery("Delete From Transaction where transactionId = :tID").setParameter("tID", transactionID);		
	}

	@Override
	public AdminBean employeeProfile() throws SurakshitException,Exception {
		
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userDataUtility.getUserDtlsFromEmailId(userDetails.getUsername()); //Username contains the email ID
		
		AdminBean employee = new AdminBean();
		employee.setName(user.getName());
		employee.setEmailID(user.getEmailId());
		employee.setAddress(user.getAddress());
		employee.setPhoneNumber(user.getPhoneNumber().toString());
		employee.setUserID(user.getUserId());
		employee.setDocumentID(user.getDocumentId());
		
		return employee;
	}


	


	@Override
	public void updateUser(UserBean userToBeUpdated, String oldEmailID, byte[] publicKeyByteArray) throws SurakshitException,
			Exception {
		Session session = hibernateUtility.getSession();
		User user = (User) session.createQuery("From User where emailId = :emailID").setParameter("emailID", oldEmailID).uniqueResult();
		if(userToBeUpdated.getAddress() != null)
			user.setAddress(userToBeUpdated.getAddress());
		if(userToBeUpdated.getDocumentId() != null)
			user.setDocumentId(userToBeUpdated.getDocumentId());
		if(userToBeUpdated.getEmailId() != null)
			user.setEmailId(userToBeUpdated.getEmailId());
		if(userToBeUpdated.getName() != null)
			user.setName(userToBeUpdated.getName());
		//TODO - What condition for the setPhoneNumber variable
		if(userToBeUpdated.getPhoneNumber() != null)
			user.setPhoneNumber(new Double(userToBeUpdated.getPhoneNumber()));
		user.setIsAccountEnabled(Integer.toString(1));
		user.setPublicKey(publicKeyByteArray);
		
		session.save(user);
	}

	
	
	
	@SuppressWarnings({ "unchecked", "null" })
	@Override
	public List<TransactionBean> getPendingTransactions() throws SurakshitException,
			Exception {
		
		Session session = hibernateUtility.getSession();
		String transactionType = "PAYMENTREQUEST";
		List<Transaction> pendingTransactions = (List<Transaction>) session.createQuery("From Transaction where transactionCurrentStatus = :Status and transactionType != :transType and transactionAmount >= 50000")
				.setParameter("Status","PendingApproval").setParameter("transType", transactionType).list();
		List<TransactionBean> pendingTransactionsBean = new ArrayList<TransactionBean>() ;
		
		for(Transaction t : pendingTransactions){
			TransactionBean local = new TransactionBean();
			local.setPrimaryUserEmail(t.getUserByPrimaryParty().getEmailId());
			local.setSecondaryUserEmail(t.getUserBySecondaryParty().getEmailId());
			local.setTransactionAmount(t.getTransactionAmount());
			local.setTransactionCreatedAt(t.getTransactionCreatedAt());
			local.setTransactionCurrentStatus(t.getTransactionCurrentStatus());
			local.setTransactionId(t.getTransactionId());
			local.setTransactionType(t.getTransactionType());
			pendingTransactionsBean.add(local);
		}
		return pendingTransactionsBean;
	}

	@SuppressWarnings({ "unchecked", "null" })
	@Override
	public List<OperationBean> getPendingOperations() throws SurakshitException,
			Exception {
		
		Session session = hibernateUtility.getSession();
		List<Operation> pendingOperations = (List<Operation>) session.createQuery("From Operation where transactionCurrentStatus = :Status and transactionAmount >= 50000")
				.setParameter("Status","PendingApproval").list();
		List<OperationBean> pendingOperationsBean = new ArrayList<OperationBean>() ;
		
		for(Operation t : pendingOperations){
			OperationBean local = new OperationBean();
			//local.setOperationCreatedAt(t.getOperationCreatedAt());
			local.setOperationCurrentStatus(t.getOperationCurrentStatus());
			local.setOperationId(t.getOperationId());
			local.setOperationType(t.getOperationType());
			pendingOperationsBean.add(local);
		}
		return pendingOperationsBean;
	}	

	@Override
	public void updateAccount(AccountBean accountToBeUpdated)
			throws SurakshitException, Exception {
		Session session = hibernateUtility.getSession();
		User userFromEmailID = userDataUtility.getUserDtlsFromEmailId(accountToBeUpdated.getUserEmailID());
		Account account = (Account) session.createQuery("From Account where user = :EMPLOYEE").setParameter("EMPLOYEE", userFromEmailID).uniqueResult();
		account.setBalance(accountToBeUpdated.getBalance());		
	}

	@Override
	public asu.bank.RegularEmployee.viewBeans.UserBean getUser(
			String userEmailID) throws SurakshitException, Exception {
		Session session = hibernateUtility.getSession();
		User user = (User) session.createQuery("From User where emailId = :emailID").setParameter("emailID", userEmailID).uniqueResult();
		if(user == null){
			return null;
		}
		UserBean userToBeReturned = new UserBean();
		userToBeReturned.setAddress(user.getAddress());
		userToBeReturned.setDocumentId(user.getDocumentId());
		userToBeReturned.setEmailId(user.getEmailId());
		userToBeReturned.setIsAccountEnabled(user.getIsAccountEnabled());
		userToBeReturned.setIsAccountLocked(user.getIsAccountLocked());
		userToBeReturned.setName(user.getName());
		userToBeReturned.setPassword(user.getPassword());
		userToBeReturned.setPhoneNumber(user.getPhoneNumber().toString());
		userToBeReturned.setRole(user.getRole());
		userToBeReturned.setUserId(user.getUserId());
		return userToBeReturned;
	}


	@Override
	public List<asu.bank.RegularEmployee.viewBeans.UserBean> getPendingExtUsers()
			throws SurakshitException, Exception {
		Session session = hibernateUtility.getSession();
		String accountStatus = Integer.toString(0);
		List<User> pendingExtUsers = (List<User>) session.createQuery("From User where isAccountEnabled = :accountStatus").setParameter("accountStatus", accountStatus).list();
		
		List<UserBean> pendingExtUserBeans = new ArrayList<UserBean>();
		for(User u : pendingExtUsers){
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
			pendingExtUserBeans.add(local);
		}
		return pendingExtUserBeans;
		
	}

	@Override
	public UserBean getUser(String emailID, String phoneNum)
			throws SurakshitException, Exception {
		Session session = hibernateUtility.getSession();
//		try{
			User user = (User) session.createQuery("From User where emailId = :emailID and phoneNumber = :phoneNumber").setParameter("emailID", emailID).setParameter("phoneNumber", new Double(phoneNum)).uniqueResult();
//		}catch(NullPointerException e){
//			return null;
//		}
		if(user == null){
			return null;
		}
		
		UserBean userToBeReturned = new UserBean();
		userToBeReturned.setAddress(user.getAddress());
		userToBeReturned.setDocumentId(user.getDocumentId());
		userToBeReturned.setEmailId(user.getEmailId());
		userToBeReturned.setIsAccountEnabled(user.getIsAccountEnabled());
		userToBeReturned.setIsAccountLocked(user.getIsAccountLocked());
		userToBeReturned.setName(user.getName());
		userToBeReturned.setPassword(user.getPassword());
		userToBeReturned.setPhoneNumber(user.getPhoneNumber().toString());
		userToBeReturned.setRole(user.getRole());
		userToBeReturned.setUserId(user.getUserId());
		return userToBeReturned;
	}

	@Override
	public void createAccount(Integer userId, double d)
			throws SurakshitException, Exception {
		Session session = hibernateUtility.getSession();
		Account account = new Account();
		User user = userDataUtility.getUserDtlsFromUserId(userId);
		account.setBalance(d);
		account.setUser(user);
		session.save(account);
	}

	@Override
	public void createUser(InternalUserBeanCreate internalUser)
			throws SurakshitException, Exception {
		Session session = hibernateUtility.getSession();
		User existingUser = (User) session.createQuery("From User where emailId = :emailID").setParameter("emailID", internalUser.getEmailId()).uniqueResult();
		if(existingUser != null){
			throw new SurakshitException("userExists");
		}
		User user = new User();
		user.setAddress(internalUser.getAddress());
		user.setDocumentId(internalUser.getDocumentId());
		user.setEmailId(internalUser.getEmailId());
		user.setIsAccountEnabled(Integer.toString(1));
		user.setIsAccountLocked(Integer.toString(0));
		user.setRole("EMPLOYEE");
		user.setName(internalUser.getName());
		user.setPassword(internalUser.getPassword());
		user.setPhoneNumber(Double.parseDouble(internalUser.getPhoneNumber()));
		session.save(user);
	}

	
	
	/*@Override
	public void createUser(
			asu.bank.RegularEmployee.viewBeans.UserBean userToBeCreated)
			throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		
	}*/

	/*
	@Override
	public void approveTransaction(Integer transactionID)
			throws SurakshitException, Exception {
		Session session = hibernateUtility.getSession();
		Transaction transaction = (Transaction) session.createQuery("From Transaction where transactionId = :ID").setParameter("ID", transactionID).uniqueResult();
		transaction.setTransactionCurrentStatus("Approved");
		session.save(transaction);
		double transactionAmount = transaction.getTransactionAmount();
		Integer userIDFromTransaction = transaction.getUserByPrimaryParty().getUserId();
		Account account = (Account) session.createQuery("From Account where user.userId = :userIDTransaction").setParameter("userIDTransaction", userIDFromTransaction).uniqueResult();
		double balanceBeforeTransaction = account.getBalance();
		if(transaction.getTransactionType().equals("DEBIT")){
			account.setBalance(balanceBeforeTransaction - transactionAmount);
			session.save(account);
		}else if(transaction.getTransactionType().equals("CREDIT")){
			account.setBalance(balanceBeforeTransaction + transactionAmount);
			session.save(account);
		}else if(transaction.getTransactionType().equals("PAYMENT")){
			//Retrieve the second user account
			Integer userIDFromTransactionSecondUser = transaction.getUserBySecondaryParty().getUserId();
			Account accountSecondaryParty = (Account)session.createQuery("From Account where user.userId = :userIDTransactionSecondUser").setParameter("userIDTransactionSecondUser", userIDFromTransactionSecondUser).uniqueResult();
			account.setBalance(balanceBeforeTransaction - transactionAmount);
			session.save(account);
			double balanceSecondUserBeforeTransaction = accountSecondaryParty.getBalance();
			accountSecondaryParty.setBalance(balanceSecondUserBeforeTransaction + transactionAmount);
			session.save(accountSecondaryParty);
		}
		
	}

	@Override
	public void rejectTransaction(Integer transactionID)
			throws SurakshitException, Exception {
		Session session = hibernateUtility.getSession();
		Transaction transaction = (Transaction) session.createQuery("From Transaction where transactionId = :ID").setParameter("ID", transactionID).uniqueResult();
		transaction.setTransactionCurrentStatus("Rejected");
		session.save(transaction);
		
	}

	@Override
	public TransactionAccountUserBean getTransaction(Integer transactionID)
			throws SurakshitException, Exception {
		Session session = hibernateUtility.getSession();
		Transaction transaction = (Transaction) session.createQuery("From Transaction where transactionId = :ID")
				.setParameter("ID",transactionID).uniqueResult();
		TransactionAccountUserBean transactionBean = new TransactionAccountUserBean();
		transactionBean.setTransactionAmount(transaction.getTransactionAmount());
		transactionBean.setTransactionCreatedAt(transaction.getTransactionCreatedAt());
		transactionBean.setTransactionCurrentStatus(transaction.getTransactionCurrentStatus());
		transactionBean.setTransactionId(transaction.getTransactionId());
		transactionBean.setTransactionType(transaction.getTransactionType());
		transactionBean.setPrimaryUserEmailID(transaction.getUserByPrimaryParty().getEmailId());
		transactionBean.setPrimaryUserID(transaction.getUserByPrimaryParty().getUserId());
		transactionBean.setSecondaryUserEmailID(transaction.getUserBySecondaryParty().getEmailId());
		transactionBean.setSecondaryUserID(transaction.getUserBySecondaryParty().getUserId());
		return transactionBean;
	}

	@Override
	public void createTransaction(TransactionAccountUserBean newTransactionBean)
			throws SurakshitException, Exception {
		Session session = hibernateUtility.getSession();
		User primaryUser = userDataUtility.getUserDtlsFromEmailId(newTransactionBean.getPrimaryUserEmailID());
		User secondaryUser = userDataUtility.getUserDtlsFromEmailId(newTransactionBean.getSecondaryUserEmailID());
		Transaction transactionToBeCreated = new Transaction();
		transactionToBeCreated.setTransactionAmount(newTransactionBean.getTransactionAmount());
		transactionToBeCreated.setTransactionCurrentStatus(newTransactionBean.getTransactionCurrentStatus());
		transactionToBeCreated.setTransactionType(newTransactionBean.getTransactionType());
		transactionToBeCreated.setTransactionType(newTransactionBean.getTransactionType());
		transactionToBeCreated.setUserByPrimaryParty(primaryUser);
		transactionToBeCreated.setUserBySecondaryParty(secondaryUser);
		session.save(transactionToBeCreated);
	}*/
	
	/*@Override
	public AccountBean getAccountFromAccountID(Integer accountID) throws SurakshitException,
			Exception {
		Session session = hibernateUtility.getSession();
		Account account = (Account) session.createQuery("From Account where accountId = :aID").setParameter("aID", accountID).uniqueResult();
		AccountBean accountToBeReturned = new AccountBean();
		accountToBeReturned.setAccountId(account.getAccountId());
		accountToBeReturned.setBalance(account.getBalance());
		accountToBeReturned.setUserEmailID(account.getUser().getEmailId());
		return accountToBeReturned;
	}
	
	@Override
	public AccountBean getAccountFromUserID(Integer userID)
			throws SurakshitException, Exception {
		Session session = hibernateUtility.getSession();
		User user = userDataUtility.getUserDtlsFromUserId(userID);
		Account account = (Account) session.createQuery("From Account where userId = :uID").setParameter("uID", user).uniqueResult();
		AccountBean accountToBeReturned = new AccountBean();
		accountToBeReturned.setAccountId(account.getAccountId());
		accountToBeReturned.setBalance(account.getBalance());
		accountToBeReturned.setUserEmailID(account.getUser().getEmailId());
		return accountToBeReturned;
	}

	@Override
	public void updateAccount(AccountBean accountToBeUpdated)
			throws SurakshitException, Exception {
		Session session = hibernateUtility.getSession();
		User userFromEmailID = userDataUtility.getUserDtlsFromEmailId(accountToBeUpdated.getUserEmailID());
		Account account = (Account) session.createQuery("From Account where user = :USER").setParameter("USER", userFromEmailID).uniqueResult();
		account.setBalance(accountToBeUpdated.getBalance());		
	}*/


	/* Duplicate method 
	 * @Override
	public UserBean getUser(String userEmailID) throws SurakshitException,
			Exception {
		Session session = hibernateUtility.getSession();
		User user = (User) session.createQuery("From User where emailId = :emailID").setParameter("emailID", userEmailID).uniqueResult();
		if(user == null){
			return null;
		}
		UserBean userToBeReturned = new UserBean();
		userToBeReturned.setAddress(user.getAddress());
		userToBeReturned.setDocumentId(user.getDocumentId());
		userToBeReturned.setEmailId(user.getEmailId());
		userToBeReturned.setIsAccountEnabled(user.getIsAccountEnabled());
		userToBeReturned.setIsAccountLocked(user.getIsAccountLocked());
		userToBeReturned.setName(user.getName());
		userToBeReturned.setPassword(user.getPassword());
		userToBeReturned.setPhoneNumber(user.getPhoneNumber().toString());
		userToBeReturned.setRole(user.getRole());
		userToBeReturned.setUserId(user.getUserId());
		return userToBeReturned;
	}*/
	
	/*@Override
	public void createUser(UserBean userToBeCreated) throws SurakshitException,
			Exception {
		Session session = hibernateUtility.getSession();
		User user = new User();
		user.setAddress(userToBeCreated.getAddress());
		user.setDocumentId(userToBeCreated.getDocumentId());
		user.setEmailId(userToBeCreated.getEmailId());
		user.setIsAccountEnabled(userToBeCreated.getIsAccountEnabled());
		user.setIsAccountLocked(userToBeCreated.getIsAccountLocked());
		user.setName(userToBeCreated.getName());
		user.setPassword(userToBeCreated.getPassword());
		user.setPhoneNumber(new String(userToBeCreated.getPhoneNumber()));
		user.setRole(userToBeCreated.getRole());
		session.save(user);
	}*/

}
