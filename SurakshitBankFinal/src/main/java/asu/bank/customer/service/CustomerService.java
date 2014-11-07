package asu.bank.customer.service;

import java.util.List;

import asu.bank.customer.viewBeans.AccountDebitViewBean;
import asu.bank.customer.viewBeans.AccountGetTransactionDetailsBean;
import asu.bank.customer.viewBeans.AccountTransferBean;
import asu.bank.customer.viewBeans.AccountViewBean;
import asu.bank.customer.viewBeans.CustomerPaymentBean;
import asu.bank.utility.SurakshitException;

public interface CustomerService {

	String getAccountNo(String username) throws SurakshitException, Exception;
	
	String getBalance(String username) throws SurakshitException, Exception;

	Double customerAddBalance(AccountViewBean accountViewBean)throws SurakshitException, Exception;

	Double customerDebitAmount(AccountDebitViewBean accountDebitViewBean)throws SurakshitException,Exception;

	List<AccountGetTransactionDetailsBean> customerGetTransactionDetails(String username)throws SurakshitException,Exception;
	
	Double customerTransferAmount(AccountTransferBean accountTransferBean) throws SurakshitException,Exception;
	
	List<CustomerPaymentBean> custToMerchantPayment(String username)throws SurakshitException,Exception;
	
	
	Double customerMakePaymentApproved(String transactionId)
			throws SurakshitException, Exception;

	Double customerMakePaymentRejected(String transactionId)
			throws SurakshitException, Exception;
	

	void updatePersonalInfo(String name, String address, String phoneNumber)
			throws SurakshitException, Exception;
	
//	void approveTransaction(Integer transactionID)
//			throws SurakshitException, Exception;
//	
}
