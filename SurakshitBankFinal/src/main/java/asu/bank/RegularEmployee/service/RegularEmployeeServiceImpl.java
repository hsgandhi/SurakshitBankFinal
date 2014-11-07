package asu.bank.RegularEmployee.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import asu.bank.RegularEmployee.controller.RegularEmployeeController;
import asu.bank.RegularEmployee.dao.RegularEmployeeDao;
import asu.bank.RegularEmployee.viewBeans.AccountBean;
import asu.bank.RegularEmployee.viewBeans.RegularEmployeeBean;
import asu.bank.RegularEmployee.viewBeans.TransactionBean;
import asu.bank.RegularEmployee.viewBeans.*;
import asu.bank.RegularEmployee.viewBeans.UserBeanForModify;
import asu.bank.RegularEmployee.viewBeans.UserEmailPhoneBean;
import asu.bank.hibernateFiles.User;
import asu.bank.utility.SurakshitException;
import asu.bank.utility.UserDataUtility;

@Service
@Transactional(propagation=Propagation.REQUIRED,rollbackFor={SurakshitException.class,Exception.class})
public class RegularEmployeeServiceImpl implements RegularEmployeeService {

	@Autowired
	RegularEmployeeDao regEmpDao;
	
	@Autowired
	UserDataUtility userDataUtility;
	
	@Override
	public RegularEmployeeBean employeeProfile() throws SurakshitException,
			Exception {
		return regEmpDao.employeeProfile();
		
	}

	@Override
	public List<TransactionBean> getPendingTransactions()
			throws SurakshitException, Exception {
		return regEmpDao.getPendingTransactions();
	}
	
	@Override
	public boolean rejectTransaction(Integer transactionID)
			throws SurakshitException, Exception {
		TransactionBean transaction = regEmpDao.getTransaction(transactionID);
		if(transaction.getTransactionCurrentStatus().equals("PendingApproval")){
			transaction.setTransactionCurrentStatus("Rejected");
			regEmpDao.updateTransaction(transaction);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean approveTransaction(Integer transactionID)
			throws SurakshitException, Exception {
		TransactionBean transaction = regEmpDao.getTransaction(transactionID);
		if(transaction.getTransactionCurrentStatus().equals("PendingApproval")){
			if(transaction.getTransactionType().equals("DEBIT")){
				if(debitAccount(transaction.getPrimaryUserEmail(), transaction.getTransactionAmount())){
					transaction.setTransactionCurrentStatus("Approved");
					regEmpDao.updateTransaction(transaction);
					return true;
				}else{
					transaction.setTransactionCurrentStatus("Rejected");
					regEmpDao.updateTransaction(transaction);
					return false;
				}
			}else if(transaction.getTransactionType().equals("CREDIT")){
				creditAccount(transaction.getPrimaryUserEmail(), transaction.getTransactionAmount());
				transaction.setTransactionCurrentStatus("Approved");
				regEmpDao.updateTransaction(transaction);
				return true;
			}else if(transaction.getTransactionType().equals("PAYMENT")){
				if(transferBetweenAccounts(transaction.getPrimaryUserEmail(),transaction.getSecondaryUserEmail(),transaction.getTransactionAmount())){
					transaction.setTransactionCurrentStatus("Approved");
					regEmpDao.updateTransaction(transaction);
					return true;
				}else{
					transaction.setTransactionCurrentStatus("Rejected");
					regEmpDao.updateTransaction(transaction);
					return false;
				}
			}
		}
		return false;
	}

	@Override
	public boolean debitAccount(String emailID, Double amount)
			throws SurakshitException, Exception {
		User user = userDataUtility.getUserDtlsFromEmailId(emailID);
		AccountBean accountBean = regEmpDao.getAccountFromUserID(user.getUserId());
		if(accountBean.getBalance() > amount){
			Double temp = accountBean.getBalance();
			accountBean.setBalance(temp - amount);
			regEmpDao.updateAccount(accountBean);
			return true;
		}
		return false;
	}

	@Override
	public void creditAccount(String emailID, Double amount)
			throws SurakshitException, Exception {
		User user = userDataUtility.getUserDtlsFromEmailId(emailID);
		AccountBean accountBean = regEmpDao.getAccountFromUserID(user.getUserId());
		Double temp = accountBean.getBalance();
		accountBean.setBalance(temp + amount);
		regEmpDao.updateAccount(accountBean);
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
	public boolean createNewTransaction(TransactionBean newTransactionBean)
			throws SurakshitException, Exception {
		User primaryUser = userDataUtility.getUserDtlsFromEmailId(newTransactionBean.getPrimaryUserEmail());
		User secondaryUser = userDataUtility.getUserDtlsFromEmailId(newTransactionBean.getSecondaryUserEmail());
		if(primaryUser == null || secondaryUser == null){
			return false;
		}		
		if(newTransactionBean.getTransactionAmount() < 50000.0){
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
			regEmpDao.createTransaction(newTransactionBean);
			return true;
		}else{
			newTransactionBean.setTransactionCurrentStatus("PendingApproval");
			regEmpDao.createTransaction(newTransactionBean);
			return true;
		}
	}

	@Override
	public List<TransactionBean> getListTransactions(String emailId) throws SurakshitException,
			Exception {
		
		User user = userDataUtility.getUserDtlsFromEmailId(emailId);
		return regEmpDao.getTransactionListBasedOnUserID(user.getUserId());
		
	}

	@Override
	public void checkPhoneNumber(String emailId, String phoneNo) throws SurakshitException,
			Exception {
		
		User user = userDataUtility.getUserDtlsFromEmailId(emailId);
		Double phoneNum = Double.parseDouble(phoneNo);
		
		if(!phoneNo.equals(user.getPhoneNumber()))
			throw new SurakshitException("phoneNotFound");
	}

	@Override
	public TransactionBean getTransaction(Integer transactionID)
			throws SurakshitException, Exception {
		return regEmpDao.getTransaction(transactionID);
	}

	@Override
	public boolean modifyTransaction(Integer transactionID, Double newAmount)
			throws SurakshitException, Exception {
		TransactionBean oldTransaction = regEmpDao.getTransaction(transactionID);
		
		if(oldTransaction.getTransactionCurrentStatus().equals("Approved")){
			throw new SurakshitException("CantModifyApprovedTransaction");
		}else if(oldTransaction.getTransactionCurrentStatus().equals("PendingApproval")){
			deleteTransaction(oldTransaction.getTransactionId());
			return true;
		}
		return false;
	}

	@Override
	public void deleteTransaction(Integer transactionID)
			throws SurakshitException, Exception {
		TransactionBean transaction = regEmpDao.getTransaction(transactionID);
		if(transaction.getTransactionCurrentStatus().equals("Approved")){
			throw new SurakshitException("CantDeleteApprovedTransaction");
		}else if(transaction.getTransactionCurrentStatus().equals("PendingApproval") && transaction.getTransactionAmount() < 50000){
			regEmpDao.deleteTransaction(transactionID);
		}else{
			throw new SurakshitException("phoneNotFound");
		}
		
	}

	@Override
	public boolean createUser(UserBean userDetails) throws SurakshitException,Exception {
		
		if(regEmpDao.getUser(userDetails.getEmailId()) != null){
			return false;
		}
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(userDetails.getPassword());
		userDetails.setPassword(hashedPassword);
		userDetails.setIsAccountEnabled(Integer.toString(0));
		userDetails.setIsAccountLocked(Integer.toString(0));
		User user = regEmpDao.createUser(userDetails);
		
		 regEmpDao.enterDataInUserAttempts(user);
		return true;
	}

	@Override
	public void modifyUser(UserBeanForModify userDetails, String oldEmailID)
			throws SurakshitException, Exception {
		regEmpDao.updateUser(userDetails, oldEmailID);
	}
	
	public UserBean getUserFromEmailID(String emailID) throws SurakshitException, Exception{
		UserBean user = regEmpDao.getUser(emailID);
		if(user == null){
			return null;
		}else{
			return user;
		}
	}
	
	
	/*
	@Override
	public TransactionAccountUserBean getTransaction(Integer transactionID)
			throws SurakshitException, Exception {
		return regEmpDao.getTransaction(transactionID);
	}

	@Override
	public void createTransaction(TransactionAccountUserBean newTransactionBean)
			throws SurakshitException, Exception {
		if(newTransactionBean.getTransactionAmount() < 50000.0){
			newTransactionBean.setTransactionCurrentStatus("Approved");
		}else{
			newTransactionBean.setTransactionCurrentStatus("PendingApproval");
		}
		regEmpDao.createTransaction(newTransactionBean);
	}

	@Override
	public void modifyTransaction(TransactionAccountUserBean editedTransactionBean)
			throws SurakshitException, Exception {
		//TODO
		
	}

	@Override
	public void deleteTransaction(Integer transactionID) throws SurakshitException, Exception {
		TransactionAccountUserBean toBeDeletedTransaction = regEmpDao.getTransaction(transactionID);
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
}
