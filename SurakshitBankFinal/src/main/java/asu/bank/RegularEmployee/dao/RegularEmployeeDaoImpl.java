package asu.bank.RegularEmployee.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import asu.bank.RegularEmployee.viewBeans.RegularEmployeeBean;
import asu.bank.RegularEmployee.viewBeans.TransactionAccountUserBean;
import asu.bank.hibernateFiles.Account;
import asu.bank.hibernateFiles.User;
import asu.bank.utility.HibernateUtility;
import asu.bank.utility.SurakshitException;
import asu.bank.utility.UserDataUtility;
import asu.bank.hibernateFiles.Transaction;;

@Repository
public class RegularEmployeeDaoImpl implements RegularEmployeeDao{
	
	@Autowired
	HibernateUtility hibernateUtility;
	@Autowired
	UserDataUtility userDataUtility;

	@Override
	public RegularEmployeeBean employeeProfile() throws SurakshitException,
			Exception {
		
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userDataUtility.getUserDtlsFromEmailId(userDetails.getUsername()); //Username contains the email ID
		
		RegularEmployeeBean employee = new RegularEmployeeBean();
		employee.setName(user.getName());
		employee.setEmailID(user.getEmailId());
		employee.setAddress(user.getAddress());
		employee.setPhoneNumber(user.getPhoneNumber());
		employee.setUserID(user.getUserId());
		employee.setDocumentID(user.getDocumentId());
		
		return employee;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TransactionAccountUserBean> getPendingTransactions() throws SurakshitException,
			Exception {
		
		Session session = hibernateUtility.getSession();
		List<TransactionAccountUserBean> pendingTransactions = (List<TransactionAccountUserBean>) session.createQuery("From Transaction where transactionCurrentStatus = :Status and transactionAmount < 50000")
				.setParameter("Status","PendingApproval").list();
										
		return pendingTransactions;
	}

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
	}

}
