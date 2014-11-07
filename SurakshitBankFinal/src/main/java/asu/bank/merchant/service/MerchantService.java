package asu.bank.merchant.service;

import java.util.List;

import asu.bank.merchant.viewBeans.AccountDebitViewBean;
import asu.bank.merchant.viewBeans.AccountGetTransactionDetailsBean;
import asu.bank.merchant.viewBeans.AccountTransferBean;
import asu.bank.merchant.viewBeans.AccountViewBean;
import asu.bank.hibernateFiles.User;
import asu.bank.utility.SurakshitException;

public interface MerchantService {

	String checkEmailIdValidity(String emailID) throws SurakshitException, Exception;
	void createPaymentRequestTransaction(String customerEmail, String paymentRequestAmt) throws SurakshitException, Exception;
	void updatePersonalInfo(String name, String address, String phoneNumber) throws SurakshitException, Exception;
	String getAccountNo(String username) throws SurakshitException, Exception;
	Double merchantAddBalance(AccountViewBean accountViewBean)throws SurakshitException, Exception;
	Double merchantDebitAmount(AccountDebitViewBean accountDebitViewBean)throws SurakshitException,Exception;
	String getBalance(String username) throws SurakshitException, Exception;
	Double merchantTransferAmount(AccountTransferBean accountTransferBean) throws SurakshitException,Exception;
	List<AccountGetTransactionDetailsBean> merchantGetTransactionDetails(String username)throws SurakshitException,Exception;
	byte[] getPublicKeyByteArray() throws SurakshitException, Exception;

}
