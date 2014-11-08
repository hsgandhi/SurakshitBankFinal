package asu.bank.merchant.controller;

import java.security.PublicKey;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import asu.bank.merchant.service.MerchantService;
import asu.bank.merchant.validator.MerchantAccountValidator;
import asu.bank.merchant.viewBeans.AccountDebitViewBean;
import asu.bank.merchant.viewBeans.AccountGetTransactionDetailsBean;
import asu.bank.merchant.viewBeans.AccountTransferBean;
//import asu.bank.customer.service.CustomerService;
import asu.bank.merchant.viewBeans.AccountViewBean;
import asu.bank.merchant.viewBeans.InfoUpdateViewBean;
import asu.bank.merchant.viewBeans.PayRequestViewBean;
import asu.bank.utility.EmailUtilityUsingSSL;
import asu.bank.utility.KeyGenerataionUtility;
import asu.bank.utility.OneTimePasswordGenerator;
import asu.bank.utility.SurakshitException;

@Controller
public class MerchantController {
	
	@Autowired
	MerchantService merchantService;
	
	@Autowired
	MerchantAccountValidator merchantAccountValidator;
	
	@Autowired
	KeyGenerataionUtility keyGenerationUtility;
	
	@Autowired
	OneTimePasswordGenerator merchantOneTimePasswordGenerator;
	
	@Autowired
	EmailUtilityUsingSSL emailUtilityUsingSSL;
	

	private static final Logger logger = Logger.getLogger(MerchantController.class);
	 private static final Logger secureLogger = Logger.getLogger("secure");

	
	@RequestMapping(value="/merchantReqCustPay", method = RequestMethod.POST)
	public String merchantReqCustPay(ModelMap model) throws SurakshitException, Exception {
		
		PayRequestViewBean payRequestViewBean = new PayRequestViewBean();
		model.addAttribute("payRequestViewBean", payRequestViewBean);
		
		
		return "Merchant/requestCustPayment";
	}
	
	@RequestMapping(value="/merchantRequestCustPayment", method = RequestMethod.POST)
	public String merchantRequestCustPayment(@ModelAttribute("payRequestViewBean")@Valid PayRequestViewBean payRequestViewBean, BindingResult result,ModelMap model) throws SurakshitException, Exception {
		
		logger.info("In merchant");
		if(result.hasErrors()) {
			return "Merchant/requestCustPayment";
		}
		String amt = payRequestViewBean.getPaymentAmount();
		System.out.println("The amount received is " + amt);
		String customerEmail = merchantService.checkEmailIdValidity(payRequestViewBean.getCustEmailID());
		System.out.println("In Merch Controller and the value of email is :" + customerEmail);
		if(customerEmail == "NotCustomer") {
			secureLogger.info("UserNotFound");
			throw new SurakshitException("UserNotFound");
		}
		else {
			merchantService.createPaymentRequestTransaction(customerEmail, payRequestViewBean.getPaymentAmount());
			secureLogger.info("Payment Request to Customer Created.");
			model.addAttribute("successMessage", "Payment Request to Customer Created.");
			return "Homepage/success";
		}
		
	}
	
	@RequestMapping(value="/merchantUpdateInfo", method = RequestMethod.POST)
	public String merchantUpdateInfo(ModelMap model, HttpServletRequest request) throws SurakshitException, Exception {
		
		InfoUpdateViewBean infoUpdateViewBean = new InfoUpdateViewBean();
		model.addAttribute("infoUpdateViewBean", infoUpdateViewBean);
		//infoUpdateViewBean.setOriginalMerchantHashString(originalMerchantString);
		return "Merchant/updatePersonalInfo";
	}
	
	@RequestMapping(value="/merchantUpdatePersonalInfo", method = RequestMethod.POST)
	public String merchantUpdatePersonalInfo(@ModelAttribute("infoUpdateViewBean")@Valid InfoUpdateViewBean infoUpdateViewBean, BindingResult result,ModelMap model, HttpServletRequest request) throws SurakshitException, Exception {
		if(result.hasErrors()) {
			return "Merchant/updatePersonalInfo";
		}
		merchantService.updatePersonalInfo(infoUpdateViewBean.getName(), infoUpdateViewBean.getAddress(), infoUpdateViewBean.getPhoneNumber());
		secureLogger.info("Successfully Updated Information.");
		model.addAttribute("successMessage", "Successfully Updated Information.");
		
		return "Homepage/success";
		
	}
	
	@RequestMapping(value="/merchantCreditBalance", method = RequestMethod.POST)
	public String merchantCreditBalance(HttpServletRequest request,ModelMap model) throws SurakshitException, Exception {
		
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String accountNo=merchantService.getAccountNo(userDetails.getUsername());
		
		AccountViewBean accountViewBean = new AccountViewBean();
		accountViewBean.setAccountId(accountNo);
		
		String originalMerchantString = UUID.randomUUID().toString();
		HttpSession session = request.getSession();
		session.setAttribute("originalMerchantHashString", originalMerchantString);
		
		model.addAttribute("accountViewBean", accountViewBean);
		
		return "Merchant/merchantBalance";
	}

	@RequestMapping(value="/merchantAddBalance", method = RequestMethod.POST)
	public String merchantAddBalance(@ModelAttribute("accountViewBean")@Valid AccountViewBean accountViewBean, BindingResult result,ModelMap model,HttpServletRequest request) throws SurakshitException, Exception {
		
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String encryptedString = request.getParameter("encryptedText");
		if("".equals(encryptedString))
			throw new SurakshitException("UserNotAuthenticated");
		
		
		byte[] publicKeyByteArray = merchantService.getPublicKeyByteArray();
		byte[] publicKeyBase64ByteArray= Base64.encode(publicKeyByteArray);
		PublicKey pubicKey = keyGenerationUtility.genPublicKeyFromKeyByte(publicKeyBase64ByteArray);
		String decryptedString = keyGenerationUtility.decryptUsingPublicKey(pubicKey,encryptedString);
		
		HttpSession session = request.getSession();
		if(!decryptedString.equals(session.getAttribute("originalMerchantHashString")))
			throw new SurakshitException("UserNotAuthenticated");
		
		String accountNo=merchantService.getAccountNo(userDetails.getUsername());
		
		accountViewBean.setAccountId(accountNo);
		
		if(result.hasErrors()) {
			return "Merchant/merchantBalance";
		}
		//System.out.println("Account Number is: " +  accountNo);
		/*//check other validations(custom)
		merchantAccountValidator.validate(accountViewBean, result);
		if(result.hasErrors())
		{
			return "Merchant/merchantBalance";
		}*/
		
		accountViewBean.setEmailId(userDetails.getUsername());
		
		Double finalBalance=merchantService.merchantAddBalance(accountViewBean);
		secureLogger.info("The transaction is now in " + accountViewBean.getTransactionStatus() + " state and the Balance is " + finalBalance);
		model.addAttribute("successMessage", "The transaction is now in " + accountViewBean.getTransactionStatus() + " state and the Balance is " + finalBalance);
		
		return "Homepage/success";
	}
	
	@RequestMapping(value="/merchantDebitAmount", method=RequestMethod.POST)
	public String merchantDebitAmount(HttpServletRequest request,ModelMap model) throws SurakshitException, Exception {
		
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String accountNo=merchantService.getAccountNo(userDetails.getUsername());
		
		String balance= merchantService.getBalance(userDetails.getUsername());
		
		AccountDebitViewBean accountDebitViewBean = new AccountDebitViewBean();
		
		accountDebitViewBean.setAccountId(accountNo);
		
		accountDebitViewBean.setBalance(balance);
		
		model.addAttribute("accountDebitViewBean", accountDebitViewBean);
		
		return "Merchant/debitAmount";
	
	
	}
	
	@RequestMapping(value="/merchantWithdrawAmount", method = RequestMethod.POST)
	public String merchantWithdrawAmount(@ModelAttribute("accountDebitViewBean")@Valid AccountDebitViewBean accountDebitViewBean, BindingResult result,ModelMap model) throws SurakshitException, Exception {
		
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String accountNo=merchantService.getAccountNo(userDetails.getUsername());
		System.out.println("Got account number: " + accountNo);
		accountDebitViewBean.setAccountId(accountNo);
		
		String balance= merchantService.getBalance(userDetails.getUsername());
		System.out.println("Got balance: " + balance);
		accountDebitViewBean.setBalance(balance);
		
		double balance1=Double.parseDouble(balance);
		
		//check all annotations in the view bean
		if(result.hasErrors())
		{
			return "Merchant/debitAmount";
		}
		
		//check other validations(custom)
		merchantAccountValidator.validate(accountDebitViewBean, result);
		if(result.hasErrors())
		{
			return "Merchant/debitAmount";
		}
		

		accountDebitViewBean.setEmailId(userDetails.getUsername());
		
		
		//accountDebitViewBean.setEmailId(userDetails.getUsername());
		
		Double finalBalance=merchantService.merchantDebitAmount(accountDebitViewBean);
		
		double amount = Double.parseDouble(accountDebitViewBean.getAmount());
		
		if(amount>5000)
		{
			model.addAttribute("successMessage", "The status of your debit request is " + accountDebitViewBean.getTransactionState() + "." + "The current balance is $" + finalBalance);
			secureLogger.info("The status of your debit request is " + accountDebitViewBean.getTransactionState() + "." + "The current balance is $" + finalBalance);
		}
		else
		if(amount<=balance1)
		{
			model.addAttribute("successMessage", "The status of your debit request is " + accountDebitViewBean.getTransactionState() + "." + "The current balance is $" + finalBalance);
			secureLogger.info("The status of your debit request is " + accountDebitViewBean.getTransactionState() + "." + "The current balance is $" + finalBalance);
		}
		else
		{
			model.addAttribute("successMessage", "Sorry. You dont have sufficient balance in your account");
			secureLogger.info("Sorry. You dont have sufficient balance in your account");
		}
		return "Homepage/success";
	}
	
	@RequestMapping(value="/merchantTransferAmount", method=RequestMethod.POST)
	public String merchantTransferAmount(HttpServletRequest request,ModelMap model) throws SurakshitException, Exception {
		
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String accountNoSender=merchantService.getAccountNo(userDetails.getUsername());
		
		String balanceSender= merchantService.getBalance(userDetails.getUsername());
		
		AccountTransferBean accountTransferBean = new AccountTransferBean();
		
		accountTransferBean.setAccountIdSender(accountNoSender);
		
		accountTransferBean.setBalanceSender(balanceSender);
		
		String otp = merchantOneTimePasswordGenerator.getOneTimePassword();
		HttpSession sesssion = request.getSession();
		sesssion.setAttribute("merchantOneTimePass", otp);
		emailUtilityUsingSSL.sendMail(userDetails.getUsername(),otp);
		
		model.addAttribute("accountTransferBean", accountTransferBean);
		
		return "Merchant/amountTransfer";
		
	}
	
	@RequestMapping(value="/merchantTransferAmountDetails", method = RequestMethod.POST)
	public String merchantTransferAmountDetails(@ModelAttribute("accountTransferBean")@Valid AccountTransferBean accountTransferBean, BindingResult result,ModelMap model, HttpServletRequest request) throws SurakshitException, Exception {
		HttpSession session = request.getSession();
		String otp = session.getAttribute("merchantOneTimePass").toString();
		//String userOtp = accountTransferBean.getOneTimePwd();
		String userOtp = request.getParameter("oneTimePwd");
		
		if(userOtp.equals("") || otp.length()<8)
			throw new SurakshitException("EnterOTP");
		
		System.out.println(otp + "=" + userOtp);
		
		if(otp.equals(userOtp)) {
			System.out.println("We have a match!");
			UserDetails userDetails =
					 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			String accountNo=merchantService.getAccountNo(userDetails.getUsername());
			accountTransferBean.setAccountIdSender(accountNo);
			
			String balance= merchantService.getBalance(userDetails.getUsername());
			accountTransferBean.setBalanceSender(balance);
			double balance1=Double.parseDouble(balance);
			
			//check all annotations in the view bean
			if(result.hasErrors())
			{
				return "Merchant/amountTransfer";
			}
			
			//check other validations(custom)
			merchantAccountValidator.validate(accountTransferBean, result);
			if(result.hasErrors())
			{
				return "Merchant/amountTransfer";
			}
			
			accountTransferBean.setEmailIdSender(userDetails.getUsername());
			
			Double finalBalance=merchantService.merchantTransferAmount(accountTransferBean);
			
			double amount = Double.parseDouble(accountTransferBean.getAmount());
			
			/*if(amount>5000)
			{
				model.addAttribute("successMessage", "The status of your debit request is Pending Approval." + " The current balance is $" + finalBalance);
			}
			else if(amount>0 && amount<=balance1)
			{
				model.addAttribute("successMessage", "$"+amount+ " transferred to " + accountTransferBean.getEmailIdReceiver() + "." + "Current balance is $" + finalBalance);
			}
			else
			{
				model.addAttribute("successMessage", "Sorry. You dont have sufficient balance in your account");
			}*/
			if(amount >  balance1)
			{
				model.addAttribute("successMessage", "Sorry. You dont have sufficient balance in your account");
			}
			else if(amount>5000)
			{
				model.addAttribute("successMessage", "The status of your debit request is Pending Approval." + " The current balance is $" + finalBalance);
				secureLogger.info("The status of your debit request is Pending Approval." + " The current balance is $" + finalBalance);
			}
			else if(amount < 0)
			{
				model.addAttribute("successMessage", "Can't transfer an amount lesser than zero!!");
			}
			else
			{
				model.addAttribute("successMessage", "$"+amount+ " transferred to " + accountTransferBean.getEmailIdReceiver() + "." + "Current balance is $" + finalBalance);
			}
			
			secureLogger.info("This function is transfering funds from" +accountTransferBean.getEmailIdSender()+ "to" +accountTransferBean.getEmailIdReceiver() );
			return "Homepage/success";
		}
		else
			throw new SurakshitException("OTPFailed");
		
		
	}
	
	@RequestMapping(value="/merchantViewAccountDetails", method=RequestMethod.POST)
	public String merchantViewAccountDetails(HttpServletRequest request,ModelMap model) throws SurakshitException, Exception {
		
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String accountNo=merchantService.getAccountNo(userDetails.getUsername());
		
		String balance= merchantService.getBalance(userDetails.getUsername());
		
		AccountGetTransactionDetailsBean accountGetTransactionDetailsBean = new AccountGetTransactionDetailsBean();
		
		accountGetTransactionDetailsBean.setAccountId(accountNo);
		
		accountGetTransactionDetailsBean.setBalance(balance);
		
		model.addAttribute("accountGetTransactionDetailsBean", accountGetTransactionDetailsBean);
		
		return "Merchant/viewAccountDetails";
	
	}
	
	@RequestMapping(value="/merchantGetTransactionDetails", method = RequestMethod.POST)
	public String merchantGetTransactionDetails(@ModelAttribute("accountGetTransactionDetailsBean")@Valid AccountGetTransactionDetailsBean accountGetTransactionDetailsBean, BindingResult result,ModelMap model) throws SurakshitException, Exception {
		
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<AccountGetTransactionDetailsBean> transaction= (List<AccountGetTransactionDetailsBean>) merchantService.merchantGetTransactionDetails(userDetails.getUsername());
		//accountGetTransactionDetailsBean.setTransaction(transaction);
		
		//check all annotations in the view bean
		if(result.hasErrors())
		{
			return "Merchant/viewAccountDetails";
		}
		
		//check other validations(custom)
		merchantAccountValidator.validate(accountGetTransactionDetailsBean, result);
		if(result.hasErrors())
		{
			return "Merchant/viewAccountDetails";
		}
		
		//accountDebitViewBean.setEmailId(userDetails.getUsername());
		
		//model.addAttribute("successMessage", "Your transactions are" + transaction);
		
		//model.addAttribute("successMessage", "Your transactions are");
		
		model.addAttribute("successMessage","Your transactions are");
		model.addAttribute("transaction", transaction);
		
		return "Merchant/transactions";
	}


}
