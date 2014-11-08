package asu.bank.Admin.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import asu.bank.Admin.service.AdminService;
import asu.bank.Admin.viewBeans.InternalUserBeanCreate;
import asu.bank.Admin.viewBeans.InternalUserBeanModify;
import asu.bank.Admin.viewBeans.TransactionBean;
import asu.bank.RegularEmployee.viewBeans.UserBean;
import asu.bank.utility.EmailUtilityUsingSSL;
import asu.bank.utility.MaskUtility;
import asu.bank.utility.OneTimePasswordGenerator;
import asu.bank.utility.SurakshitException;
import asu.bank.utility.UserDataUtility;

@Controller
public class AdminController {
	
	@Autowired
	AdminService adminService;
	@Autowired
	UserDataUtility userDataUtility;
	@Autowired
	EmailUtilityUsingSSL emailUtilityUsingSSL;
	@Autowired
	OneTimePasswordGenerator oneTimePass;
	@Autowired
	MaskUtility maskUtility;

	
	private static final Logger logger = Logger.getLogger(AdminController.class);
	 private static final Logger secureLogger = Logger.getLogger("secure");

	
	//private static final Logger logger = Logger.getLogger(AdminController.class);
	
	
	@RequestMapping(value="/adminGetSytemLogFiles", method = RequestMethod.POST)
	public String adminGetSytemLogFiles(HttpServletRequest request, ModelMap model) throws SurakshitException, Exception{
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		emailUtilityUsingSSL.sendLogFileToAdmin(userDetails.getUsername(), false);
		
		model.addAttribute("successMessage", "Mail sent successfully to "+ userDetails.getUsername());
		
		return "Homepage/success";
	}
	
	
	@RequestMapping(value="/adminGetSecureLogFiles", method = RequestMethod.POST)
	public String adminGetSecureLogFiles(HttpServletRequest request, ModelMap model) throws SurakshitException, Exception{
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		emailUtilityUsingSSL.sendLogFileToAdmin(userDetails.getUsername(), true);
		
		model.addAttribute("successMessage", "Mail sent successfully to "+ userDetails.getUsername());
		
		return "Homepage/success";
	}
	

	@RequestMapping(value="/adminMyProfile", method = RequestMethod.POST)
	public String loadProfile(ModelMap model) throws SurakshitException, Exception{
		model.addAttribute("EmployeeProfile",adminService.employeeProfile());
		 
		return "Admin/myProfile";
	}
	
	
		
	@RequestMapping(value="/adminGetInternalUserDetailsAdd", method = RequestMethod.POST)
	public String getInternalUserDetails(ModelMap model) throws SurakshitException, Exception{
		InternalUserBeanCreate internalUser = new InternalUserBeanCreate();
		model.addAttribute("internalUser", internalUser);
		return "Admin/getInternalUserDetailsCreateUser";			
	}
	
	@RequestMapping(value="/adminAddInternalUser", method = RequestMethod.POST)
	public String addInternalUser(@ModelAttribute("internalUser")@Valid InternalUserBeanCreate internalUser, BindingResult result, ModelMap model) throws SurakshitException, Exception{
		if(result.hasErrors())
		{
			return "Admin/getInternalUserDetailsCreateUser";
		}
		boolean success = adminService.createInternalUser(internalUser);
		if(!success){
			throw new SurakshitException("userExists");
		}else{
			model.addAttribute("successMessage", "Internal User created successfully");
			return "Homepage/success";
		}			
	}
	
	
	/*Get the external users that needs to be approved/rejected*/
	@RequestMapping(value="/adminGetPendingExtUserRequests", method = RequestMethod.POST)
	public String getPendingExtUserRequests(ModelMap model) throws SurakshitException, Exception{
		List<UserBean> pendingExtUsers =(List<UserBean>) adminService.getPendingExtUsers();
		if(pendingExtUsers.size() == 0){  
			model.addAttribute("successMessage", "There are no pending external users to be approved");
			return "Homepage/success";
		}
		model.addAttribute("pendingExtUsersList", pendingExtUsers);
		return "Admin/displayPendingExternalUsers";	
	}
	
	
	@RequestMapping(value="/adminApproveRejectExternalUser", method = RequestMethod.POST)
	public String approveRejectExtUserRequests(HttpServletRequest request, ModelMap model) throws SurakshitException, Exception{
		//Check if no attack has been performed
		String emailID = request.getParameter("emailID");
		String phoneNum = request.getParameter("phoneNum");
		boolean userExists = adminService.doesUserExists(emailID, phoneNum);
		if(!userExists){
			throw new SurakshitException("internalUserDetailsTampered");
		}
		String status = (String) request.getParameter("approveReject");
		if(status.equals("Approve")){
			adminService.approveExtUser(emailID);
			model.addAttribute("successMessage", "Successfully added external user");
			return "Homepage/success"; 
		}else if(status.equals("Reject")){
			adminService.rejectExtUser(emailID);
			model.addAttribute("successMessage", "Successfully rejected external user");
			return "Homepage/success";
		}
		model.addAttribute("successMessage", "Debugging - did not work");
		return "Homepage/success"; 
	}
	
	
	@RequestMapping(value="/adminGetPendingTransactions", method = RequestMethod.POST)
	public String getPendingTransactions(ModelMap model) throws SurakshitException, Exception{
		List<TransactionBean> pendingTransactions = adminService.getPendingTransactions();
		for(TransactionBean transactionBean: pendingTransactions)
		{
			transactionBean.setEncryptedTransactionId(maskUtility.getMaskedString(transactionBean.getTransactionId().toString()));
		}
		if(pendingTransactions.size() == 0){  
			model.addAttribute("successMessage", "There are no pending transactions to be approved");
			return "Homepage/success";
		}
		model.addAttribute("PendingTransactionsList", pendingTransactions);
		return "Admin/displayPendingTransactions";			
	}
	
	@RequestMapping(value="/adminapproveRejectTransaction", method = RequestMethod.POST)
	public String accessUserAccountTransaction(HttpServletRequest request, ModelMap model) throws SurakshitException, Exception{
		//Integer transactionID =  Integer.parseInt(request.getParameter("ID"));
		
		String tranId= maskUtility.getOriginalString(request.getParameter("CrID"));
		Integer transactionID =Integer.parseInt(tranId);
		
		String status = (String) request.getParameter("approveReject");
		if(status.equals("Approve")){
			boolean success = adminService.approveTransaction(transactionID);
			if(success == true){
				model.addAttribute("successMessage", "Transaction approved successfully");
				return "Homepage/success";
			}else{
				model.addAttribute("successMessage", "Transaction was not in a pending state to accept it");
				return "Homepage/success";
			}
		}else if(status.equals("Reject")){
			boolean success = adminService.rejectTransaction(transactionID);
			if(success == true){
				model.addAttribute("successMessage", "Transaction rejected successfully");
				return "Homepage/success";
			}else{
				model.addAttribute("successMessage", "Transaction was not in a pending state to reject it");
				return "Homepage/success";
			}
		}
		model.addAttribute("successMessage", "Something went wrong, please contact the administrator");
		return "Homepage/success";
	}
	
	
	@RequestMapping(value="/adminGetListInternalUsers", method = RequestMethod.POST)
	public String displayInternalUsers(ModelMap model) throws SurakshitException, Exception{
		List<UserBean> listOfEmployees = adminService.getListInternalUsers();
		model.addAttribute("employeeList", listOfEmployees);
		return "Admin/displayInternalUsers";
	}
	
	
	@RequestMapping(value="/adminDeleteInternalUser", method = RequestMethod.POST)
	public String deleteInternalUser(HttpServletRequest request,ModelMap model) throws SurakshitException, Exception{		
		String emailID = request.getParameter("emailID");
		String phoneNum = request.getParameter("phoneNum");
		boolean userExists = adminService.doesUserExists(emailID, phoneNum);
		if(!userExists){
			throw new SurakshitException("internalUserDetailsTampered");
		}
		adminService.deleteInternalUser(emailID);		
		model.addAttribute("successMessage", "Successfully deleted internal user");
		return "Homepage/success";
	}
	
	@RequestMapping(value="/adminRequestPII", method = RequestMethod.POST)
	public String adminRequestPII(ModelMap model) throws SurakshitException, Exception{
		return "Admin/requestPII";			
	}
	
	@RequestMapping(value="/adminGetPII", method = RequestMethod.POST)
	public String adminGetPII(HttpServletRequest request,ModelMap model) throws SurakshitException, Exception{
		boolean approval=oneTimePass.getApprovalOfGovt();
		String emailId = request.getParameter("emailId");
		if(emailId.equals(""))
			throw new SurakshitException("UserNotFound");
		
		if(!approval)
			throw new SurakshitException("GovNotApproved");
		else
		{
			UserBean userBean =adminService.getPIIDtls(emailId);
			model.addAttribute("userBean", userBean);
		}
		
		return "Admin/requestPII";			
	}
	
	/* Modify internal and external user */
	@RequestMapping(value="/adminGetInternalUserDetailsMODIFY", method = RequestMethod.POST)
	public String getInternalUserDetailsMODIFY(ModelMap model) throws SurakshitException, Exception{
		InternalUserBeanModify internalUser = new InternalUserBeanModify();
		model.addAttribute("internalUser", internalUser);
		return "Admin/getInternalUserDetailsMODIFY";
	}
	
	/* Modify internal and external user */	
	@RequestMapping(value="/adminModifyInternalUser", method = RequestMethod.POST)
	public String modifyInternalUser(@ModelAttribute("internalUser")@Valid InternalUserBeanModify internalUser ,BindingResult result,ModelMap model) throws SurakshitException, Exception{
		if(result.hasErrors()){
			return "Admin/getInternalUserDetailsMODIFY";
		}
		boolean success = adminService.modifyInternalUser(internalUser);
		if(success){
			model.addAttribute("successMessage", "Internal User modified successfully");
			return "Homepage/success";
		}else{
			throw new SurakshitException("UserNotFound");
		}
	}
	
	
	/* delete external users */
	@RequestMapping(value="/adminGetListExternalUsers", method = RequestMethod.POST)
	public String displayExternalUsers(ModelMap model) throws SurakshitException, Exception{
		List<UserBean> listOfEmployees = adminService.getListExternalUsers();
		model.addAttribute("employeeList", listOfEmployees);
		return "Admin/displayExternalUsers";
	}

	
	@RequestMapping(value="/adminDeleteExternalUser", method = RequestMethod.POST)
	public String deleteExternalUser(HttpServletRequest request,ModelMap model) throws SurakshitException, Exception{		
		String emailID = request.getParameter("emailID");
		String phoneNum = request.getParameter("phoneNum");
		boolean userExists = adminService.doesUserExists(emailID, phoneNum);
		if(!userExists){
			throw new SurakshitException("externalUserDetailsTampered");
		}
		adminService.deleteExternalUser(emailID);		
		model.addAttribute("successMessage", "Successfully deleted external user");
		return "Homepage/success";
	}
	
}
