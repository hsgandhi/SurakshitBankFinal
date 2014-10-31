package asu.bank.customer.controller;

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

import asu.bank.customer.service.CustomerService;
import asu.bank.customer.validator.CustomerAccountValidator;
import asu.bank.customer.viewBeans.AccountViewBean;
import asu.bank.login.controller.LoginController;
import asu.bank.utility.SurakshitException;

@Controller
public class CustomerController {
	
	private static final Logger logger = Logger.getLogger(LoginController.class);
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	CustomerAccountValidator customerValidator;
	
	@RequestMapping(value="/customerCreditBalance", method = RequestMethod.POST)
	public String customerCreditBalance(HttpServletRequest request,ModelMap model) throws SurakshitException, Exception {
		
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String accountNo=customerService.getAccountNo(userDetails.getUsername());
		
		AccountViewBean accountViewBean = new AccountViewBean();
		accountViewBean.setAccountId(accountNo);
		
		model.addAttribute("accountViewBean", accountViewBean);
		
		return "customer/creditBalance";
	}

	@RequestMapping(value="/customerAddBalance", method = RequestMethod.POST)
	public String customerAddBalance(@ModelAttribute("accountViewBean")@Valid AccountViewBean accountViewBean, BindingResult result,ModelMap model) throws SurakshitException, Exception {
		
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String accountNo=customerService.getAccountNo(userDetails.getUsername());
		accountViewBean.setAccountId(accountNo);
		
		//check all annotations in the view bean
		if(result.hasErrors())
		{
			return "customer/creditBalance";
		}
		
		//check other validations(custom)
		customerValidator.validate(accountViewBean, result);
		if(result.hasErrors())
		{
			return "customer/creditBalance";
		}
		
		accountViewBean.setEmailId(userDetails.getUsername());
		
		Double finalBalance=customerService.customerAddBalance(accountViewBean);
		
		model.addAttribute("successMessage", "Balance has been updated. The new balance is " + finalBalance);
		
		return "Homepage/success";
	}
}
