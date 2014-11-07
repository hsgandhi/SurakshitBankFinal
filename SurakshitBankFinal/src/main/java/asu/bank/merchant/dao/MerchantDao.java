package asu.bank.merchant.dao;

import java.util.List;

import asu.bank.merchant.viewBeans.AccountDebitViewBean;
import asu.bank.merchant.viewBeans.AccountGetTransactionDetailsBean;
import asu.bank.merchant.viewBeans.AccountTransferBean;
import asu.bank.merchant.viewBeans.AccountViewBean;
import asu.bank.hibernateFiles.User;
import asu.bank.utility.SurakshitException;

public interface MerchantDao {
	void createPaymentRequestTransaction(User customer, String paymentRequestAmt) throws SurakshitException, Exception;
	void updatePersonalInfo(String name, String address, String phoneNumber) throws SurakshitException, Exception;
	public String getAccountNo(String username) throws SurakshitException,Exception;
	public Double merchantAddBalance(AccountViewBean accountViewBean) throws SurakshitException,Exception;
	public String getBalance(String username) throws SurakshitException,Exception;
	public Double merchantDebitAmount(AccountDebitViewBean accountDebitViewBean)throws SurakshitException,Exception;
	public Double merchantTransferAmount(AccountTransferBean accountTransferBean)throws SurakshitException, Exception;
	public List<AccountGetTransactionDetailsBean> merchantGetTransactionDetails(String username)throws SurakshitException,Exception;
}
