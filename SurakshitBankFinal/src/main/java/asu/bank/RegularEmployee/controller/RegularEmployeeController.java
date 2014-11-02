package asu.bank.RegularEmployee.controller;

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
import asu.bank.utility.SurakshitException;
import asu.bank.utility.UserDataUtility;
import asu.bank.RegularEmployee.viewBeans.TransactionAccountUserBean;
import asu.bank.customer.viewBeans.AccountViewBean;
import asu.bank.hibernateFiles.User;

@Controller
public class RegularEmployeeController {
	
	@Autowired
	RegularEmployeeService regEmpService;
	
	@Autowired
	UserDataUtility userDataUtility;

	private static final Logger logger = Logger.getLogger(RegularEmployeeController.class);
	
	@RequestMapping(value="/employeeMyProfile", method = RequestMethod.POST)
	public String loadProfile(ModelMap model) throws SurakshitException, Exception{
		model.addAttribute("EmployeeProfile",regEmpService.employeeProfile());
		 
		return "RegularEmployee/myProfile";
	}
	
	@RequestMapping(value="/employeeGetPendingTransactions", method = RequestMethod.POST)
	public String getPendingTransactions(ModelMap model) throws SurakshitException, Exception{
		model.addAttribute("PendingTransactionsList", regEmpService.getPendingTransactions());
		
		return "RegularEmployee/getPendingTransactions";
	}
	
	@RequestMapping(value="/approveRejectTransaction", method = RequestMethod.POST)
	public String accessUserAccount(HttpServletRequest request, ModelMap model) throws SurakshitException, Exception{
		Integer transactionID =  Integer.parseInt(request.getParameter("ID"));
		String status = (String) request.getParameter("approveReject");
		if(status.equals("Approve")){
			regEmpService.approveTransaction(transactionID);
		}else if(status.equals("Reject")){
			regEmpService.rejectTransaction(transactionID);
		}
		return "RegularEmployee/debug";
	}
	
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
		
	@RequestMapping(value="/employeeGetTransactionDetails", method = RequestMethod.POST)
	public String getTransactionDetails(ModelMap model) throws SurakshitException, Exception{
		TransactionAccountUserBean newTransactionBean = new TransactionAccountUserBean();
		model.addAttribute("newTransactionBean",newTransactionBean);
		return "RegularEmployee/getTransactionDetails";
	}	
	
	@RequestMapping(value="/createTransaction", method = RequestMethod.POST)
	public String createTransaction(@ModelAttribute("newTransactionBean")@Valid TransactionAccountUserBean newTransactionBean, HttpServletRequest request, ModelMap model) throws SurakshitException, Exception{
		regEmpService.createTransaction(newTransactionBean);
		return "RegularEmployee/debug";
	}
		
	@RequestMapping(value="/employeeModifyTransaction", method = RequestMethod.POST)
	public String modifyTransactionGetTransactionID(ModelMap model) throws SurakshitException, Exception{
		return "RegularEmployee/editGetTransactionID";
	}	
	
	/*@RequestMapping(value="/editTransaction", method = RequestMethod.POST)
	public String modifyTransactionGetEditedTransaction(HttpServletRequest request, ModelMap model) throws SurakshitException, Exception{
		Integer transactionID =  Integer.parseInt(request.getParameter("transactionID"));
		model.addAttribute("transaction", regEmpService.getTransaction(transactionID));
		return "RegularEmployee/editTransaction";
	}*/
	
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
	}
	
	
}
