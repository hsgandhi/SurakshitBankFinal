package asu.bank.merchant.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import asu.bank.merchant.viewBeans.AccountDebitViewBean;
import asu.bank.merchant.viewBeans.AccountGetTransactionDetailsBean;
import asu.bank.merchant.viewBeans.AccountTransferBean;
import asu.bank.merchant.viewBeans.AccountViewBean;
import asu.bank.hibernateFiles.Account;
import asu.bank.hibernateFiles.Transaction;
import asu.bank.hibernateFiles.User;
import asu.bank.merchant.viewBeans.PayRequestViewBean;
import asu.bank.utility.HibernateUtility;
import asu.bank.utility.SurakshitException;
import asu.bank.utility.UserDataUtility;

@Repository
public class MerchantDaoImpl implements MerchantDao {
	@Autowired
	HibernateUtility hibernateUtility;
	@Autowired
	UserDataUtility userDataUtility;
	
	@Override
	public void createPaymentRequestTransaction(User customer, String paymentRequestAmt) throws SurakshitException, Exception {
		Transaction transactionTable = new Transaction();
		transactionTable.setUserByPrimaryParty(customer);
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User merchant = userDataUtility.getUserDtlsFromEmailId(userDetails.getUsername());
		transactionTable.setUserBySecondaryParty(merchant);
		transactionTable.setTransactionType("PAYMENTREQUEST");
		transactionTable.setTransactionAmount(new Double(paymentRequestAmt));
		transactionTable.setTransactionCurrentStatus("PendingApproval");
		hibernateUtility.getSession().save(transactionTable);
	}
	
	@Override
	public void updatePersonalInfo(String name, String address, String phoneNumber) throws SurakshitException, Exception {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userForUpdate = userDataUtility.getUserDtlsFromEmailId(userDetails.getUsername());
		System.out.println("Merch Dao: " + userForUpdate.getEmailId() + ", " + name + ", " + phoneNumber + ", " + address);
		userForUpdate.setName(name);
		userForUpdate.setAddress(address);
		userForUpdate.setPhoneNumber(new Double(phoneNumber));
		hibernateUtility.getSession().update(userForUpdate);
	}
	
	@Override
	public String getAccountNo(String username) throws SurakshitException, Exception {
		String accountNo=userDataUtility.getAccountNo(username);
		
		if(accountNo==null)
			throw new SurakshitException("NoAccountAvailable");
		
		return accountNo;
	}

	@Override
	public Double merchantAddBalance(AccountViewBean accountViewBean) throws SurakshitException, Exception {
		
		User user=userDataUtility.getUserDtlsFromEmailId(accountViewBean.getEmailId());
		
		Double amount=Double.parseDouble(accountViewBean.getCurrency());
		
		Account account = (Account)hibernateUtility.getSession().get(Account.class, Integer.parseInt(accountViewBean.getAccountId()));
		
		Double finalBalance=account.getBalance();
		
		Transaction transaction=new Transaction();
		transaction.setTransactionAmount(amount);
		
		if(amount>0 && amount<=5000)
		{
			
			finalBalance=finalBalance+ amount;
			
			account.setBalance(finalBalance);
			
			hibernateUtility.getSession().update(account);
			accountViewBean.setTransactionStatus("Approved");
			transaction.setTransactionCurrentStatus("Approved");
		}
		
		else
		{
			transaction.setTransactionCurrentStatus("PendingApproval");
			accountViewBean.setTransactionStatus("PendingApproval");
		}	
		
		transaction.setTransactionType("CREDIT");
		
		transaction.setUserByPrimaryParty(user);
		transaction.setUserBySecondaryParty(user);
		
		hibernateUtility.getSession().save(transaction);
		
		return finalBalance;
	}
	
	@Override
	public Double merchantDebitAmount(AccountDebitViewBean accountDebitViewBean) throws SurakshitException, Exception {
		
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
		accountDebitViewBean.setTransactionState("PendingApproval");
	}
	else
	if(amount <= currentBalance && amount>0)
	{
		finalBalance= currentBalance - amount ;
		account.setBalance(finalBalance);
		hibernateUtility.getSession().update(account);
		transaction.setTransactionCurrentStatus("Approved");
		accountDebitViewBean.setTransactionState("Approved");
	}
	else
	{
		finalBalance=currentBalance;
		transaction.setTransactionCurrentStatus("Rejected");
		accountDebitViewBean.setTransactionState("Rejected");
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
	public Double merchantTransferAmount(AccountTransferBean accountTransferBean) throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		
		User userSender=userDataUtility.getUserDtlsFromEmailId(accountTransferBean.getEmailIdSender());
		User userReceiver=userDataUtility.getUserDtlsFromEmailId(accountTransferBean.getEmailIdReceiver());
		
		String accountIdReceiver=userDataUtility.getAccountNo(accountTransferBean.getEmailIdReceiver());
		accountTransferBean.setAccountIdReceiver(accountIdReceiver);
		
		Account accountSender = (Account)hibernateUtility.getSession().get(Account.class,  Integer.parseInt(accountTransferBean.getAccountIdSender()));
		Account accountReceiver = (Account)hibernateUtility.getSession().get(Account.class,  Integer.parseInt(accountTransferBean.getAccountIdReceiver()));
		//System.out.println("Sender: " + accountSender.getAccountId() + " and Receiver: " + accountReceiver.getAccountId() );
		double amount = Double.parseDouble(accountTransferBean.getAmount());
		//System.out.println("Amount to be transferred: " + amount);
		
		double currentBalanceSender=accountSender.getBalance();
		double currentBalanceReceiver=accountReceiver.getBalance();
		
		double finalBalanceSender;
		double finalBalanceReceiver;
		
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

		return finalBalanceSender;
		
	}
	
	@Override
	public List<AccountGetTransactionDetailsBean> merchantGetTransactionDetails(String username) throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		User user = userDataUtility.getUserDtlsFromEmailId(username);

		
		Set<Transaction> transactionFinal=new HashSet<Transaction>();
		Set<Transaction> transaction=new HashSet<Transaction>();
		Set<Transaction> transaction1=new HashSet<Transaction>();

		transaction=user.getTransactionsForPrimaryParty();
		transaction1=user.getTransactionsForSecondaryParty();
			
		transactionFinal.addAll(transaction);
		transactionFinal.addAll(transaction1);
		
		//List<String> listMain = new ArrayList<String>();
		
		List<Transaction> transactionFinalList = new ArrayList<Transaction>(transactionFinal);
		
		List<AccountGetTransactionDetailsBean> transactionFinalBean=new ArrayList<AccountGetTransactionDetailsBean>();

		for(Transaction t : transactionFinalList){
			AccountGetTransactionDetailsBean accountgetTransactionDetailsBean = new AccountGetTransactionDetailsBean();
			accountgetTransactionDetailsBean.setTransactionType(t.getTransactionType());
			accountgetTransactionDetailsBean.setTransactionCurrentStatus(t.getTransactionCurrentStatus());
			accountgetTransactionDetailsBean.setTransactionAmount(t.getTransactionAmount().toString());
			accountgetTransactionDetailsBean.setPrimaryParty(t.getUserByPrimaryParty().getEmailId());
			accountgetTransactionDetailsBean.setSecondaryParty(t.getUserBySecondaryParty().getEmailId());
			accountgetTransactionDetailsBean.setTimeStamp(t.getTransactionCreatedAt().toString());
			transactionFinalBean.add(accountgetTransactionDetailsBean);
		}
		
		return transactionFinalBean;
		
	}

}
