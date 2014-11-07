package asu.bank.customer.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import asu.bank.customer.dao.CustomerDao;
import asu.bank.customer.dao.CustomerDaoImpl;
import asu.bank.customer.viewBeans.AccountDebitViewBean;
import asu.bank.customer.viewBeans.AccountGetTransactionDetailsBean;
import asu.bank.customer.viewBeans.AccountTransferBean;
import asu.bank.customer.viewBeans.AccountViewBean;
import asu.bank.customer.viewBeans.CustomerPaymentBean;
import asu.bank.utility.SurakshitException;

@Service
@Transactional(propagation=Propagation.REQUIRED,rollbackFor={SurakshitException.class,Exception.class})
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	CustomerDao customerDao;
	
	private static final Logger logger = Logger.getLogger(CustomerServiceImpl.class);
	 private static final Logger secureLogger = Logger.getLogger("secure");


	@Override
	public String getAccountNo(String username) throws SurakshitException,
			Exception {
		return customerDao.getAccountNo(username);
	}

	@Override
	public Double customerAddBalance(AccountViewBean accountViewBean)
			throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		return customerDao.customerAddBalance(accountViewBean);
	}

	@Override
	public Double customerDebitAmount(
			AccountDebitViewBean accountDebitViewBean)
			throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		return customerDao.customerDebitAmount(accountDebitViewBean);
	}

	@Override
	public String getBalance(String username) throws SurakshitException,
			Exception {
		// TODO Auto-generated method stub
		return customerDao.getBalance(username);
	}

	@Override
	public List<AccountGetTransactionDetailsBean> customerGetTransactionDetails(String username)
			throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		return customerDao.customerGetTransactionDetails(username);
	}

	@Override
	public Double customerTransferAmount(AccountTransferBean accountTransferBean)
			throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		secureLogger.info("This function is transfering funds from" +accountTransferBean.getEmailIdSender()+ "to" +accountTransferBean.getEmailIdReceiver() );
		return customerDao.customerTransferAmount(accountTransferBean);
		
	}

	@Override
	public List<CustomerPaymentBean> custToMerchantPayment(
			String username) throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		return customerDao.custToMerchantPayment(username);
	}

	@Override
	public Double customerMakePaymentApproved(String transactionId)
			throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		return customerDao.customerMakePaymentApproved(transactionId);
	}
	
	@Override
	public Double customerMakePaymentRejected(String transactionId)
			throws SurakshitException, Exception{
		return customerDao.customerMakePaymentApproved(transactionId);
	}

	@Override
	public void updatePersonalInfo(String name, String address,
			String phoneNumber) throws SurakshitException, Exception {
		// TODO Auto-generated method stub
		customerDao.updatePersonalInfo(name, address, phoneNumber);
	}

	
	
//
//	@Override
//	public void approveTransaction(Integer transactionID)
//			throws SurakshitException, Exception {
//		// TODO Auto-generated method stub
//		customerDao.approveTransaction(transactionID);
//	}

	
}
