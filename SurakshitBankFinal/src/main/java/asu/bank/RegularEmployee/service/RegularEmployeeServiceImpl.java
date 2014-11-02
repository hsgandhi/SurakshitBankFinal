package asu.bank.RegularEmployee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import asu.bank.RegularEmployee.dao.RegularEmployeeDao;
import asu.bank.RegularEmployee.viewBeans.RegularEmployeeBean;
import asu.bank.RegularEmployee.viewBeans.TransactionAccountUserBean;
import asu.bank.RegularEmployee.viewBeans.TransactionAccountUserBean;
import asu.bank.utility.SurakshitException;

@Service
@Transactional(propagation=Propagation.REQUIRED,rollbackFor={SurakshitException.class,Exception.class})
public class RegularEmployeeServiceImpl implements RegularEmployeeService {

	@Autowired
	RegularEmployeeDao regEmpDao;
	
	@Override
	public RegularEmployeeBean employeeProfile() throws SurakshitException,
			Exception {
		return regEmpDao.employeeProfile();
		
	}

	@Override
	public List<TransactionAccountUserBean> getPendingTransactions()
			throws SurakshitException, Exception {
		return regEmpDao.getPendingTransactions();
	}

	@Override
	public void approveTransaction(Integer transactionID)
			throws SurakshitException, Exception {
		regEmpDao.approveTransaction(transactionID);		
	}

	@Override
	public void rejectTransaction(Integer transactionID)
			throws SurakshitException, Exception {
		regEmpDao.rejectTransaction(transactionID);		
	}

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

}
