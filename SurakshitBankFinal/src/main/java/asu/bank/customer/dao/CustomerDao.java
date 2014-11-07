package asu.bank.customer.dao;

import asu.bank.customer.viewBeans.AccountDebitViewBean;
import asu.bank.customer.viewBeans.AccountGetTransactionDetailsBean;
import asu.bank.customer.viewBeans.AccountViewBean;
import asu.bank.customer.viewBeans.AccountTransferBean;
import asu.bank.customer.viewBeans.CustomerPaymentBean;
import asu.bank.utility.SurakshitException;

import java.util.List;

public interface CustomerDao {

	public String getAccountNo(String username) throws SurakshitException,Exception;
	
	public String getBalance(String username) throws SurakshitException,Exception;

	public Double customerAddBalance(AccountViewBean accountViewBean)throws SurakshitException,Exception;
	
	public Double customerDebitAmount(AccountDebitViewBean accountDebitViewBean)throws SurakshitException,Exception;
	
	public List<AccountGetTransactionDetailsBean> customerGetTransactionDetails(String username)throws SurakshitException,Exception;

	public Double customerTransferAmount(AccountTransferBean accountTransferBean)throws SurakshitException, Exception;
	
	public List<CustomerPaymentBean> custToMerchantPayment(String username)
			throws SurakshitException, Exception;

	Double customerMakePaymentApproved(String transactionId)
			throws SurakshitException, Exception;

	Double customerMakePaymentRejected(String transactionId)
			throws SurakshitException, Exception;

	void updatePersonalInfo(String name, String address, String phoneNumber)
			throws SurakshitException, Exception;

////	void approveTransaction(Integer transactionID) throws SurakshitException,
////			Exception;
//
//	void rejectTransaction(Integer transactionID) throws SurakshitException,
//			Exception; 
//	
}
