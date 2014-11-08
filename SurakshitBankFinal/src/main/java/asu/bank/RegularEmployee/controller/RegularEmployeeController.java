package asu.bank.RegularEmployee.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import asu.bank.RegularEmployee.service.RegularEmployeeService;
import asu.bank.RegularEmployee.viewBeans.TransactionBean;
import asu.bank.RegularEmployee.viewBeans.UserBean;
import asu.bank.RegularEmployee.viewBeans.UserBeanForModify;
import asu.bank.RegularEmployee.viewBeans.UserEmailPhoneBean;
import asu.bank.utility.MaskUtility;
import asu.bank.utility.SurakshitException;

@Controller
public class RegularEmployeeController {
	
	@Autowired
	RegularEmployeeService regEmpService;
	
	@Autowired
	MaskUtility maskUtility;

	private static final Logger logger = Logger.getLogger(RegularEmployeeController.class);
	
	@RequestMapping(value="/employeeMyProfile", method = RequestMethod.POST)
	public String loadProfile(ModelMap model) throws SurakshitException, Exception{
		model.addAttribute("EmployeeProfile",regEmpService.employeeProfile());		 
		return "RegularEmployee/myProfile";
	}
	
	// encrypt
	@RequestMapping(value="/employeeGetPendingTransactions", method = RequestMethod.POST)
	public String getPendingTransactions(ModelMap model) throws SurakshitException, Exception{
		List<TransactionBean> pendingTransactions = regEmpService.getPendingTransactions();
		
		for(TransactionBean transactionBean: pendingTransactions)
		{
			transactionBean.setEncryptedTransactionId(maskUtility.getMaskedString(transactionBean.getTransactionId().toString()));
		}
		
		
		if(pendingTransactions.size() == 0){ //TODO : Did not return null although list was empty 
			model.addAttribute("successMessage", "There are no pending transactions to be approved");
			return "Homepage/success";
		}
		
		model.addAttribute("PendingTransactionsList", pendingTransactions);
		return "RegularEmployee/displayPendingTransactions";			
	}
	
	
	@RequestMapping(value="/employeeGetUserEmail", method = RequestMethod.POST)
	public String getUserDetails(ModelMap model) throws SurakshitException, Exception{
		UserEmailPhoneBean userDetails = new UserEmailPhoneBean();
		model.addAttribute("userDetails", userDetails);
		return "RegularEmployee/getUserDetailsForTransactions";
	}
	
	
	// encrypt
	@RequestMapping(value="/employeeListTransactions", method = RequestMethod.POST)
	public String employeeListTransactions(@ModelAttribute("userDetails")@Valid UserEmailPhoneBean userDetails, BindingResult result, ModelMap model) throws SurakshitException, Exception{
		if(result.hasErrors())
		{
			return "RegularEmployee/getUserDetailsForTransactions";
		}
		//regEmpService.checkPhoneNumber(userDetails.getEmailId(), userDetails.getPhoneNumber());
		 boolean exists = regEmpService.checkPhoneNumber(userDetails.getEmailId(), userDetails.getPhoneNumber());
			if(!exists){
				model.addAttribute("successMessage", "Such a user does not exist");
				return "Homepage/success";
			}

			
		List<TransactionBean> transactionList = regEmpService.getListTransactions(userDetails.getEmailId());
		for(TransactionBean transactionBean: transactionList)
		{
			transactionBean.setEncryptedTransactionId(maskUtility.getMaskedString(transactionBean.getTransactionId().toString()));
		}
		model.addAttribute("transactionList", transactionList);
		return "RegularEmployee/displayTransactionsForUser";		
		
	}
	
	// decrypt
	//encrypt
	@RequestMapping(value="/employeeModifyDeleteTransaction", method = RequestMethod.POST)
	public String modifyDeleteTransaction(HttpServletRequest request, ModelMap model) throws SurakshitException, Exception{
		//Integer transactionID =  Integer.parseInt(request.getParameter("ID"));
		
		String tranId= maskUtility.getOriginalString(request.getParameter("CrID"));
		Integer transactionID =Integer.parseInt(tranId);
		
		String status = (String) request.getParameter("modifyDelete");
		if(status.equals("Modify")){
			TransactionBean transaction = regEmpService.getTransaction(transactionID);
			
			//encrypt
			transaction.setEncryptedTransactionId(maskUtility.getMaskedString(transaction.getTransactionId().toString()));
			
			model.addAttribute("transaction", transaction);
			return "RegularEmployee/displayTransactionToBeModified";
		}else if(status.equals("Delete")){
			regEmpService.deleteTransaction(transactionID);
		}
		return "RegularEmployee/debug";
	}
	
	// decrypt
	@RequestMapping(value="/employeeModifyTransaction", method = RequestMethod.POST)
	public String modifyTransaction(HttpServletRequest request, ModelMap model) throws SurakshitException, Exception{
		String amount = request.getParameter("transactionAmount");
		boolean valid = amount.matches("\\d+(\\.\\d+)?");
		if(!valid){
			throw new SurakshitException("invalidAmount");
		}
		Double newAmount = Double.parseDouble(amount);
		//Integer transactionID =  Integer.parseInt(request.getParameter("ID"));
		//decrypt
		String tranId= maskUtility.getOriginalString(request.getParameter("CrID"));
		Integer transactionID =Integer.parseInt(tranId);
		
		TransactionBean transactionBeforeDeleting = regEmpService.getTransaction(transactionID);
		boolean toCreateNew = regEmpService.modifyTransaction(transactionID, newAmount);
		if(toCreateNew){
			TransactionBean toBeCreated = new TransactionBean();
			toBeCreated.setPrimaryUserEmail(transactionBeforeDeleting.getPrimaryUserEmail());
			toBeCreated.setSecondaryUserEmail(transactionBeforeDeleting.getSecondaryUserEmail());
			toBeCreated.setTransactionAmount(newAmount);
			toBeCreated.setTransactionType(transactionBeforeDeleting.getTransactionType());
			regEmpService.createNewTransaction(toBeCreated);
			model.addAttribute("successMessage", "Transaction modified successfully");
			return "Homepage/success";
			
		}else{
			return "RegularEmployee/debug";
		}
		
	}
	
	//decrypt
	@RequestMapping(value="/employeeApproveRejectTransaction", method = RequestMethod.POST)
	public String accessUserAccount(HttpServletRequest request, ModelMap model) throws SurakshitException, Exception{
		//Integer transactionID =  Integer.parseInt(request.getParameter("ID"));
		
		String tranId= maskUtility.getOriginalString(request.getParameter("CrID"));
		Integer transactionID =Integer.parseInt(tranId);
		
		String status = (String) request.getParameter("approveReject");
		if(status.equals("Approve")){
			boolean success = regEmpService.approveTransaction(transactionID);
			if(success){
				model.addAttribute("successMessage", "Transaction approved successfully");
				return "Homepage/success";
			}else{
				model.addAttribute("successMessage", "Transaction was not in a pending state to accept it");
				return "Homepage/success";
			}
		}else if(status.equals("Reject")){
			boolean success = regEmpService.rejectTransaction(transactionID);
			if(success){
				model.addAttribute("successMessage", "Transaction rejected successfully");
				return "Homepage/success";
			}else{
				model.addAttribute("successMessage", "Transaction was not in a pending state to reject it");
				return "Homepage/success";
			}
		}
//		model.addAttribute("message", "Something went wrong, please contact the administrator");
//		return "RegularEmployee/message";
		throw new SurakshitException("contactAdmin");
	}
	
	
	
	
	@RequestMapping(value="/employeeGetTransactionDetails", method = RequestMethod.POST)
	public String getTransactionDetails(HttpServletRequest request, ModelMap model) throws SurakshitException, Exception{
		TransactionBean newTransaction = new TransactionBean();
		model.addAttribute("newTransaction", newTransaction);
		return "RegularEmployee/getTransactionDetails";
	}
	
	@RequestMapping(value="/employeeCreateNewTransaction", method = RequestMethod.POST)
	public String createNewTransaction(@ModelAttribute("newTransaction")@Valid TransactionBean newTransaction, BindingResult result, ModelMap model) throws SurakshitException, Exception{
		if(result.hasErrors())
		{
			return "RegularEmployee/getTransactionDetails";
		}
		boolean success = regEmpService.createNewTransaction(newTransaction);
		if(!success){			
			throw new SurakshitException("newTransactionCreationFailed");
		}else{
			model.addAttribute("successMessage", "Transaction created successfully");
			return "Homepage/success";
		}
	}
	
	@RequestMapping(value="/employeeGetUserDetails", method = RequestMethod.POST)
	public String getUserDetailsAddUser(ModelMap model) throws SurakshitException, Exception{
		UserBean userDetails = new UserBean();
		model.addAttribute("userDetails", userDetails);
		return "RegularEmployee/getUserDetailsAddUser";
	}
		
	@RequestMapping(value="/employeeAddUser", method = RequestMethod.POST)
	public String addUser(@ModelAttribute("userDetails")@Valid UserBean userDetails, BindingResult result, ModelMap model) throws SurakshitException, Exception{
		if(result.hasErrors())
		{
			return "RegularEmployee/getUserDetailsAddUser";
		}
		boolean success = regEmpService.createUser(userDetails);
		if(!success){
			throw new SurakshitException("userExists");
		}else{
			model.addAttribute("successMessage", "User created successfully");
			return "Homepage/success";
		}
	}
	
	@RequestMapping(value="/employeeModifyGetUserDetails", method = RequestMethod.POST)
	public String getEmailID( ModelMap model) throws SurakshitException, Exception{
		UserEmailPhoneBean user = new UserEmailPhoneBean();
		model.addAttribute("user", user);
		return "RegularEmployee/getUserEmailModifyUser";
	}
	
	@RequestMapping(value="/employeeModifyUserEmailID", method = RequestMethod.POST)
	public String getUserDetailsToModify(@ModelAttribute("user")@Valid UserEmailPhoneBean userDetails, BindingResult result, ModelMap model) throws SurakshitException, Exception{
		if(result.hasErrors())
		{
			return "RegularEmployee/getUserEmailModifyUser";
		}
		
		 boolean exists = regEmpService.checkPhoneNumber(userDetails.getEmailId(), userDetails.getPhoneNumber());
			if(!exists){
				throw new SurakshitException("userNotExists");
			}
			
		UserBean userB = regEmpService.getUserFromEmailID(userDetails.getEmailId());		
		if(userB == null){
			throw new SurakshitException("userNotExists");
		}else{
			UserBeanForModify userDetail = new UserBeanForModify();
			model.addAttribute("userDetail", userDetail);
			model.addAttribute("oldEmailID", userB.getEmailId());
			return "RegularEmployee/getUserDetailsModifyUser";
		}
	}
	
	
	@RequestMapping(value="/employeeModifyUser", method = RequestMethod.POST)
	public String modify(@ModelAttribute("userDetail")@Valid UserBeanForModify userDetail,  BindingResult result, HttpServletRequest request, ModelMap model) throws SurakshitException, Exception{
		if(result.hasErrors())
		{
			model.addAttribute("oldEmailID", request.getParameter("oldEmailID"));
			return "RegularEmployee/getUserDetailsModifyUser";
		}
		String oldEmailID = request.getParameter("oldEmailID");
		UserBean user = regEmpService.getUserFromEmailID(oldEmailID);
		regEmpService.modifyUser(userDetail, oldEmailID);
		model.addAttribute("successMessage", "User modified successfully");
		return "Homepage/success";
	}
	
	/*
	@RequestMapping(value="/employeeViewTransaction", method = RequestMethod.POST)
	public String viewTransaction(ModelMap model) throws SurakshitException, Exception{
		return "RegularEmployee/getTransactionID";
	}
	
	@RequestMapping(value="/getTransaction", method = RequestMethod.POST)
	public String getTransaction(HttpServletRequest request, ModelMap model) throws SurakshitException, Exception{
		Integer transactionID =  Integer.parseInt(request.getParameter("transactionID"));
		model.addAttribute("transaction", regEmpService.getTransaction(transactionID));
		return "RegularEmployee/displayTransaction";
	}
		
		
	
	
		
	@RequestMapping(value="/employeeModifyTransaction", method = RequestMethod.POST)
	public String modifyTransactionGetTransactionID(ModelMap model) throws SurakshitException, Exception{
		return "RegularEmployee/editGetTransactionID";
	}	
	
	@RequestMapping(value="/editTransaction", method = RequestMethod.POST)
	public String modifyTransactionGetEditedTransaction(HttpServletRequest request, ModelMap model) throws SurakshitException, Exception{
		Integer transactionID =  Integer.parseInt(request.getParameter("transactionID"));
		model.addAttribute("transaction", regEmpService.getTransaction(transactionID));
		return "RegularEmployee/editTransaction";
	}
	
	@RequestMapping(value="/editTransaction", method = RequestMethod.POST)
	public String modifyTransactionCommitEditedTransaction(@ModelAttribute("transaction")@Valid TransactionAccountUserBean editedTransactionBean, HttpServletRequest request, ModelMap model) throws SurakshitException, Exception{
		regEmpService.modifyTransaction(editedTransactionBean);
		return "RegularEmployee/debug";
	}
	
	@RequestMapping(value="/employeeDeleteTransaction", method = RequestMethod.POST)
	public String deleteTransactionGetTransactionID(HttpServletRequest request, ModelMap model) throws SurakshitException, Exception{
		return "RegularEmployee/deleteGetTransactionID";
	}
	
	@RequestMapping(value="/deleteTransaction", method = RequestMethod.POST)
	public String deleteTransaction(HttpServletRequest request, ModelMap model) throws SurakshitException, Exception{
		Integer transactionID = Integer.parseInt(request.getParameter("transactionID"));
		regEmpService.deleteTransaction(transactionID);
		return "RegularEmployee/debug";
	}*/
	
	
}
