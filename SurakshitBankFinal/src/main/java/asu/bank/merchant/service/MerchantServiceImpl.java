package asu.bank.merchant.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import asu.bank.customer.dao.CustomerDao;
import asu.bank.login.service.LoginServiceImpl;
import asu.bank.merchant.viewBeans.AccountDebitViewBean;
import asu.bank.merchant.viewBeans.AccountGetTransactionDetailsBean;
import asu.bank.merchant.viewBeans.AccountTransferBean;
import asu.bank.merchant.viewBeans.AccountViewBean;
import asu.bank.hibernateFiles.User;
import asu.bank.merchant.dao.MerchantDao;
import asu.bank.utility.SurakshitException;
import asu.bank.utility.UserDataUtility;

@Service
@Transactional(propagation=Propagation.REQUIRED,rollbackFor={SurakshitException.class,Exception.class})
public class MerchantServiceImpl implements MerchantService {
	@Autowired
	MerchantDao merchantDao;
	
	@Autowired
	UserDataUtility userDataUtility;


	private static final Logger logger = Logger.getLogger(MerchantServiceImpl.class);
	 private static final Logger secureLogger = Logger.getLogger("secure");

	
	@Override
	public String checkEmailIdValidity(String emailID) throws SurakshitException, Exception {
		System.out.println("Reached MerchantServiceImpl.java" + emailID);
		User customer = userDataUtility.getUserDtlsFromEmailId(emailID);
		if(customer.getRole().equals("CUSTOMER")) {
			String customerEmailID = customer.getEmailId();
			System.out.println("Create transaction!");
			return customerEmailID;
		}
		else
			return "NotCustomer";
	}
	
	@Override
	public void createPaymentRequestTransaction(String customerEmail, String paymentRequestAmt) throws SurakshitException, Exception {
		merchantDao.createPaymentRequestTransaction(userDataUtility.getUserDtlsFromEmailId(customerEmail), paymentRequestAmt);
		
	}
	
	@Override
	public void updatePersonalInfo(String name, String address, String phoneNumber) throws SurakshitException, Exception {
		merchantDao.updatePersonalInfo(name, address, phoneNumber);
	}
	
	@Override
	public String getAccountNo(String username) throws SurakshitException, Exception {
		return merchantDao.getAccountNo(username);
	}

	@Override
	public Double merchantAddBalance(AccountViewBean accountViewBean) throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		System.out.println("In the merchant service add balance function");
		return merchantDao.merchantAddBalance(accountViewBean);
	}
	
	@Override
	public Double merchantDebitAmount(AccountDebitViewBean accountDebitViewBean)
			throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		return merchantDao.merchantDebitAmount(accountDebitViewBean);
	}

	@Override
	public String getBalance(String username) throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		return merchantDao.getBalance(username);
	}
	
	@Override
	public Double merchantTransferAmount(AccountTransferBean accountTransferBean)
			throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		secureLogger.info("This function is transfering funds from" +accountTransferBean.getEmailIdSender()+ "to" +accountTransferBean.getEmailIdReceiver() );
		return merchantDao.merchantTransferAmount(accountTransferBean);
	}
	
	@Override
	public List<AccountGetTransactionDetailsBean> merchantGetTransactionDetails(String username) throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		return merchantDao.merchantGetTransactionDetails(username);
	}
	
	@Override
	public byte[] getPublicKeyByteArray() throws SurakshitException, Exception {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userObj = userDataUtility.getUserDtlsFromEmailId(userDetails.getUsername()); 
		byte[] publicKeyByteArray= userObj.getPublicKey();
		return publicKeyByteArray;
	}
	
}
