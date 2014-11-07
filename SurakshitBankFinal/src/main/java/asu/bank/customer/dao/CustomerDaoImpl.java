package asu.bank.customer.dao;


import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import asu.bank.Admin.service.AdminServiceImpl;
import asu.bank.customer.viewBeans.AccountDebitViewBean;
import asu.bank.customer.viewBeans.AccountGetTransactionDetailsBean;
import asu.bank.customer.viewBeans.AccountTransferBean;
import asu.bank.customer.viewBeans.AccountViewBean;
import asu.bank.customer.viewBeans.CustomerPaymentBean;
import asu.bank.hibernateFiles.Account;
import asu.bank.hibernateFiles.Transaction;
import asu.bank.hibernateFiles.User;
import asu.bank.utility.HibernateUtility;
import asu.bank.utility.SurakshitException;
import asu.bank.utility.UserDataUtility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class CustomerDaoImpl implements CustomerDao {

	@Autowired
	HibernateUtility hibernateUtility;
	@Autowired
	UserDataUtility userDataUtility;
	
	private static final Logger logger = Logger.getLogger(CustomerDaoImpl.class);
	 private static final Logger secureLogger = Logger.getLogger("secure");

	
	@Override
	public String getAccountNo(String username) throws SurakshitException,
			Exception {
		String accountNo=userDataUtility.getAccountNo(username);
		
		if(accountNo==null)
			throw new SurakshitException("NoAccountAvailable");
		
		return accountNo;
	}

	
	
	@Override
	public Double customerAddBalance(AccountViewBean accountViewBean)
			throws SurakshitException, Exception {
		User user=userDataUtility.getUserDtlsFromEmailId(accountViewBean.getEmailId());
		
		Account account = (Account)hibernateUtility.getSession().get(Account.class, Integer.parseInt(accountViewBean.getAccountId()));
		
		Double amount=Double.parseDouble(accountViewBean.getCurrency());
		
		Double finalBalance=account.getBalance();
		
		Transaction transaction=new Transaction();
		transaction.setTransactionAmount(amount);
		
		if(amount>0 && amount<=5000)
		{
			
			finalBalance=finalBalance+ amount;
			
			account.setBalance(finalBalance);
			
			hibernateUtility.getSession().update(account);

			transaction.setTransactionCurrentStatus("Approved");
		}
		
		else
		{
			transaction.setTransactionCurrentStatus("PendingApproval");
		}	
		
		transaction.setTransactionType("CREDIT");
		
		transaction.setUserByPrimaryParty(user);
		transaction.setUserBySecondaryParty(user);
		
		hibernateUtility.getSession().save(transaction);
		
		return finalBalance;
	}

	@Override
	public Double customerDebitAmount(AccountDebitViewBean accountDebitViewBean)
			throws SurakshitException, Exception {
		
	User user=userDataUtility.getUserDtlsFromEmailId(accountDebitViewBean.getEmailId());	
	
	Account account = (Account)hibernateUtility.getSession().get(Account.class,  Integer.parseInt(accountDebitViewBean.getAccountId()));
	
	double amount = Double.parseDouble(accountDebitViewBean.getAmount());
	double currentBalance= account.getBalance(); 
	double finalBalance;
	
	Transaction transaction=new Transaction();
	
	transaction.setTransactionAmount(amount);
	
	if(amount>5000 && amount <= currentBalance)
	{
		finalBalance=currentBalance;
		transaction.setTransactionCurrentStatus("PendingApproval");
	}
	else
	if(amount <= currentBalance && amount>0)
	{
		finalBalance= currentBalance - amount ;
		account.setBalance(finalBalance);
		hibernateUtility.getSession().update(account);
		transaction.setTransactionCurrentStatus("Approved");
	}
	else
	{
		finalBalance=currentBalance;
		transaction.setTransactionCurrentStatus("Rejected");
	}
	
	
		
	
	transaction.setTransactionType("DEBIT");
	
	transaction.setUserByPrimaryParty(user);
	transaction.setUserBySecondaryParty(user);
	
	hibernateUtility.getSession().save(transaction);

	
	return finalBalance;
	}

	@Override
	public String getBalance(String username) throws SurakshitException,
			Exception {
		// TODO Auto-generated method stub
		String balance=userDataUtility.getBalance(username);
		
		return balance;
	}



	@Override
	public List<AccountGetTransactionDetailsBean> customerGetTransactionDetails(String username)
			throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		User user = userDataUtility.getUserDtlsFromEmailId(username);

		
		Set<Transaction> transactionFinal=new HashSet<Transaction>();
		Set<Transaction> transaction=new HashSet<Transaction>();
		Set<Transaction> transaction1=new HashSet<Transaction>();

		transaction=user.getTransactionsForPrimaryParty();
		transaction1=user.getTransactionsForSecondaryParty();
			
		transactionFinal.addAll(transaction);
		transactionFinal.addAll(transaction1);
		
		List<Transaction> transactionFinalList = new ArrayList<Transaction>(transactionFinal);
		
		List<AccountGetTransactionDetailsBean> transactionFinalBean=new ArrayList<AccountGetTransactionDetailsBean>();

		for(Transaction t : transactionFinalList){
			AccountGetTransactionDetailsBean accountgetTransactionDetailsBean = new AccountGetTransactionDetailsBean();
			accountgetTransactionDetailsBean.setTransactionType(t.getTransactionType());
			accountgetTransactionDetailsBean.setTransactionCurrentStatus(t.getTransactionCurrentStatus());
			accountgetTransactionDetailsBean.setTransactionAmount(t.getTransactionAmount().toString());
			accountgetTransactionDetailsBean.setPrimaryParty(t.getUserByPrimaryParty().getEmailId());
			accountgetTransactionDetailsBean.setSecondaryParty(t.getUserBySecondaryParty().getEmailId());
			accountgetTransactionDetailsBean.setTransactionCreatedAt(t.getTransactionCreatedAt().toString());
			transactionFinalBean.add(accountgetTransactionDetailsBean);
		}
		
		return transactionFinalBean;
		
	}



	@Override
	public Double customerTransferAmount(AccountTransferBean accountTransferBean)
			throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		
		User userSender=userDataUtility.getUserDtlsFromEmailId(accountTransferBean.getEmailIdSender());
		User userReceiver=userDataUtility.getUserDtlsFromEmailId(accountTransferBean.getEmailIdReceiver());
		
		String accountIdReceiver=userDataUtility.getAccountNo(accountTransferBean.getEmailIdReceiver());
		accountTransferBean.setAccountIdReceiver(accountIdReceiver);
		
		Account accountSender = (Account)hibernateUtility.getSession().get(Account.class,  Integer.parseInt(accountTransferBean.getAccountIdSender()));
		Account accountReceiver = (Account)hibernateUtility.getSession().get(Account.class,  Integer.parseInt(accountTransferBean.getAccountIdReceiver()));
		
		
		
		double amount = Double.parseDouble(accountTransferBean.getAmount());
		
		double currentBalanceSender=accountSender.getBalance();
		double currentBalanceReceiver=accountReceiver.getBalance();
		
		double finalBalanceSender=0;
		double finalBalanceReceiver=0;
		
		Transaction transaction=new Transaction();
		
		transaction.setTransactionAmount(amount);
		
		
		if(amount>5000 && amount<=currentBalanceSender)
		{
			finalBalanceSender= currentBalanceSender;
			transaction.setTransactionCurrentStatus("PendingApproval");
		}	
		
		else
		
		if(amount <= currentBalanceSender)
		{
			finalBalanceSender= currentBalanceSender - amount ;
			finalBalanceReceiver=currentBalanceReceiver + amount;
			
			accountSender.setBalance(finalBalanceSender);
			accountReceiver.setBalance(finalBalanceReceiver);
			
			hibernateUtility.getSession().update(accountSender);
			hibernateUtility.getSession().update(accountReceiver);
			

			transaction.setTransactionCurrentStatus("Approved");
		}
		
		else
		{
			finalBalanceSender=currentBalanceSender;
			finalBalanceReceiver=currentBalanceReceiver;

			transaction.setTransactionCurrentStatus("Rejected");
		}

		
			
		
		transaction.setTransactionType("PAYMENT");
		
		transaction.setUserByPrimaryParty(userSender);
		transaction.setUserBySecondaryParty(userReceiver);
		
		hibernateUtility.getSession().save(transaction);

		secureLogger.info("This function is transfering funds from" +accountTransferBean.getEmailIdSender()+ "to" +accountTransferBean.getEmailIdReceiver() );
		
		return finalBalanceSender;
	}



	@Override
	public List<CustomerPaymentBean> custToMerchantPayment(
			String username) throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		
		List<Transaction> transactionFinalList = (List<Transaction>)hibernateUtility.getSession().createQuery("FROM Transaction WHERE userByPrimaryParty.emailId= :EMAILID and transactionType='PAYMENTREQUEST' and transactionCurrentStatus='PendingApproval'")
				.setParameter("EMAILID", username)								
				.list();
		
		List<CustomerPaymentBean> transactionFinalBean=new ArrayList<CustomerPaymentBean>();

		for(Transaction t : transactionFinalList){
			CustomerPaymentBean customerPaymentBean = new CustomerPaymentBean();
			customerPaymentBean.setTransactionId(t.getTransactionId().toString());
			customerPaymentBean.setTransactionType(t.getTransactionType());
			customerPaymentBean.setTransactionCurrentStatus(t.getTransactionCurrentStatus());
			customerPaymentBean.setTransactionAmount(t.getTransactionAmount().toString());
			customerPaymentBean.setPrimaryParty(t.getUserByPrimaryParty().getEmailId());
			customerPaymentBean.setSecondaryParty(t.getUserBySecondaryParty().getEmailId());
			customerPaymentBean.setTransactionCreatedAt(t.getTransactionCreatedAt().toString());
			transactionFinalBean.add(customerPaymentBean);
		}
		
		return transactionFinalBean;
	}

	
	
	@Override
	public Double customerMakePaymentRejected(String transactionId)
			throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		Transaction transaction = (Transaction)hibernateUtility.getSession().get(Transaction.class, Integer.parseInt(transactionId));
		
		User userSender=userDataUtility.getUserDtlsFromEmailId(transaction.getUserByPrimaryParty().getEmailId());
		
		Set<Account> accountSender=(Set<Account>)userSender.getAccounts();
		Account accountSender1=new Account();
		
		
		for(Account account:accountSender )
			accountSender1=account;
		
		double currentBalanceSender=accountSender1.getBalance();
	
			transaction.setTransactionCurrentStatus("Rejected");
		
		hibernateUtility.getSession().save(transaction);

		return currentBalanceSender;
	}
	
	
	@Override
	public Double customerMakePaymentApproved(String transactionId)
			throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		Transaction transaction = (Transaction)hibernateUtility.getSession().get(Transaction.class, Integer.parseInt(transactionId));
		
		User userSender=userDataUtility.getUserDtlsFromEmailId(transaction.getUserByPrimaryParty().getEmailId());
		User userReceiver=userDataUtility.getUserDtlsFromEmailId(transaction.getUserBySecondaryParty().getEmailId());
		
		//String accountIdReceiver=userDataUtility.getAccountNo(transaction.getUserBySecondaryParty().getEmailId());
		//customerPaymentBean.setAccountIdReceiver(accountIdReceiver);
		
		
		Set<Account> accountSender=(Set<Account>)userSender.getAccounts();
		Account accountSender1=new Account();
		
		
		for(Account account:accountSender )
			accountSender1=account;
		
		Set<Account> accountReceiver=(Set<Account>)userReceiver.getAccounts();
		Account accountReceiver1=new Account();
		
		for(Account account:accountReceiver )
			accountReceiver1=account;
		
		
		double amount =(transaction.getTransactionAmount());
		
		double currentBalanceSender=accountSender1.getBalance();
		double currentBalanceReceiver=accountReceiver1.getBalance();
		
		double finalBalanceSender;
		double finalBalanceReceiver;
		
		if(amount <= currentBalanceSender)
		{
			finalBalanceSender= currentBalanceSender - amount ;
			finalBalanceReceiver=currentBalanceReceiver + amount;
			
			accountSender1.setBalance(finalBalanceSender);
			accountReceiver1.setBalance(finalBalanceReceiver);
			
			hibernateUtility.getSession().update(accountSender1);
			hibernateUtility.getSession().update(accountReceiver1);
		}
		
		else
		{
			finalBalanceSender=currentBalanceSender;
			finalBalanceReceiver=currentBalanceReceiver;
		}

		//Transaction transaction1=new Transaction();
		
		transaction.setTransactionAmount(amount);
		
		if(amount>5000 && amount<=currentBalanceSender)
		{
			transaction.setTransactionCurrentStatus("PendingApproval");
			transaction.setTransactionType("PAYMENT");
		}	
		
		else
		
		if(amount<=currentBalanceSender && amount>0)
		{
			transaction.setTransactionCurrentStatus("Approved");

			transaction.setTransactionType("PAYMENT");
		}
		
		else
		{
			transaction.setTransactionCurrentStatus("Rejected");
		}	
		
		
		transaction.setUserByPrimaryParty(userSender);
		transaction.setUserBySecondaryParty(userReceiver);
		
		hibernateUtility.getSession().save(transaction);

		return finalBalanceSender;
	}
	
	
	
	@Override
	public void updatePersonalInfo(String name, String address, String phoneNumber) throws SurakshitException, Exception {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userForUpdate = userDataUtility.getUserDtlsFromEmailId(userDetails.getUsername());
		System.out.println("Customer Dao: " + userForUpdate.getEmailId() + ", " + name + ", " + phoneNumber + ", " + address);
		userForUpdate.setName(name);
		userForUpdate.setAddress(address);
		userForUpdate.setPhoneNumber(new Double(phoneNumber));
		hibernateUtility.getSession().update(userForUpdate);
	}
	
	
	
}
