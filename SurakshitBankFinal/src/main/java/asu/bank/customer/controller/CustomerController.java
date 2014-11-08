package asu.bank.customer.controller;

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

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.List;
import java.util.UUID;

import asu.bank.RegularEmployee.viewBeans.TransactionBean;
import asu.bank.customer.dao.CustomerDaoImpl;
import asu.bank.customer.service.CustomerService;
import asu.bank.customer.validator.CustomerAccountValidator;
import asu.bank.customer.viewBeans.AccountDebitViewBean;
import asu.bank.customer.viewBeans.AccountGetTransactionDetailsBean;
import asu.bank.customer.viewBeans.AccountTransferBean;
import asu.bank.customer.viewBeans.AccountViewBean;
import asu.bank.customer.viewBeans.CustomerPaymentBean;
import asu.bank.customer.viewBeans.InfoUpdateViewBean;
import asu.bank.login.controller.LoginController;
import asu.bank.merchant.service.MerchantService;
import asu.bank.utility.KeyGenerataionUtility;
import asu.bank.utility.MaskUtility;
import asu.bank.utility.SurakshitException;

@Controller
public class CustomerController {
	
	//private static final Logger logger = Logger.getLogger(LoginController.class);
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	CustomerAccountValidator customerValidator;
	
	@Autowired
	MerchantService merchantService;
	
	@Autowired
	KeyGenerataionUtility keyGenerationUtility;
	
	@Autowired
	MaskUtility maskUtility;
	
	private static final Logger logger = Logger.getLogger(CustomerController.class);
	 private static final Logger secureLogger = Logger.getLogger("secure");

	
	@RequestMapping(value="/customerCreditBalance", method = RequestMethod.POST)
	public String customerCreditBalance(HttpServletRequest request,ModelMap model) throws SurakshitException, Exception {
		
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String accountNo=customerService.getAccountNo(userDetails.getUsername());
		
		AccountViewBean accountViewBean = new AccountViewBean();
		accountViewBean.setAccountId(accountNo);
		
		String originalCustomerString = UUID.randomUUID().toString();
		HttpSession session = request.getSession();
		session.setAttribute("originalCustomerHashString", originalCustomerString);
		
		model.addAttribute("accountViewBean", accountViewBean);
		
		return "customer/creditBalance";
	}

	@RequestMapping(value="/customerAddBalance", method = RequestMethod.POST)
	public String customerAddBalance(@ModelAttribute("accountViewBean")@Valid AccountViewBean accountViewBean, BindingResult result,ModelMap model,HttpServletRequest request) throws SurakshitException, Exception {
		
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String encryptedString = request.getParameter("encryptedText");
		if("".equals(encryptedString))
			throw new SurakshitException("UserNotAuthenticated");
		
		
		byte[] publicKeyByteArray = merchantService.getPublicKeyByteArray();
		byte[] publicKeyBase64ByteArray= Base64.encode(publicKeyByteArray);
		PublicKey pubicKey = keyGenerationUtility.genPublicKeyFromKeyByte(publicKeyBase64ByteArray);
		String decryptedString = keyGenerationUtility.decryptUsingPublicKey(pubicKey,encryptedString);
		
		HttpSession session = request.getSession();
		if(!decryptedString.equals(session.getAttribute("originalCustomerHashString")))
			throw new SurakshitException("UserNotAuthenticated");
		
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
		
		double amount=Double.parseDouble(accountViewBean.getCurrency());
		
		Double finalBalance=customerService.customerAddBalance(accountViewBean);
		
		if(amount>0 && amount<=5000)
		{
			model.addAttribute("successMessage", "Balance has been updated. The new balance is " +finalBalance);
		}
		else 
		{
			model.addAttribute("successMessage", "Thank you. The transaction is pending with the approver.");
		}
		
		logger.info("Credit is done by customer");
		
		return "Homepage/success";
	}
	
	@RequestMapping(value="/customerDebitAmount", method=RequestMethod.POST)
	public String customerDebitAmount(HttpServletRequest request,ModelMap model) throws SurakshitException, Exception {
		
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String accountNo=customerService.getAccountNo(userDetails.getUsername());
		
		String balance= customerService.getBalance(userDetails.getUsername());
		
		AccountDebitViewBean accountDebitViewBean = new AccountDebitViewBean();
		
		accountDebitViewBean.setAccountId(accountNo);
		
		accountDebitViewBean.setBalance(balance);
		
		model.addAttribute("accountDebitViewBean", accountDebitViewBean);
		
		return "customer/debitAmount";
	
	
	}
	
	@RequestMapping(value="/customerWithdrawAmount", method = RequestMethod.POST)
	public String customerWithdrawAmount(@ModelAttribute("accountDebitViewBean")@Valid AccountDebitViewBean accountDebitViewBean, BindingResult result,ModelMap model) throws SurakshitException, Exception {
		
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String accountNo=customerService.getAccountNo(userDetails.getUsername());
		accountDebitViewBean.setAccountId(accountNo);
		
		String balance= customerService.getBalance(userDetails.getUsername());
		accountDebitViewBean.setBalance(balance);
		
		double balance1=Double.parseDouble(balance);
		
		//check all annotations in the view bean
		if(result.hasErrors())
		{
			return "customer/debitAmount";
		}
		
		//check other validations(custom)
		customerValidator.validate(accountDebitViewBean, result);
		if(result.hasErrors())
		{
			return "customer/debitAmount";
		}
		

		accountDebitViewBean.setEmailId(userDetails.getUsername());
		
		
		//accountDebitViewBean.setEmailId(userDetails.getUsername());
		
		Double finalBalance=customerService.customerDebitAmount(accountDebitViewBean);
		
		double amount = Double.parseDouble(accountDebitViewBean.getAmount());
		
		if(amount>5000 && amount<=balance1)
		{
			model.addAttribute("successMessage", "Your request is pending with the bank and current balance is" + finalBalance);
		}	
		else
		if(amount<=balance1)
		{
			model.addAttribute("successMessage", "Your account is debited with "+amount+ "and current balance is" + finalBalance);
		}
		else
		{
			model.addAttribute("successMessage", "Sorry. You dont have sufficient balance in your account");
		}
		
		logger.info("Debit will be done by customer");
		
		return "Homepage/success";
	}

	
	@RequestMapping(value="/customerViewAccountDetails", method=RequestMethod.POST)
	public String customerViewAccountDetails(HttpServletRequest request,ModelMap model) throws SurakshitException, Exception {
		
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String accountNo=customerService.getAccountNo(userDetails.getUsername());
		
		String balance= customerService.getBalance(userDetails.getUsername());
		
		AccountGetTransactionDetailsBean accountGetTransactionDetailsBean = new AccountGetTransactionDetailsBean();
		
		accountGetTransactionDetailsBean.setAccountId(accountNo);
		
		accountGetTransactionDetailsBean.setBalance(balance);
		
		model.addAttribute("accountGetTransactionDetailsBean", accountGetTransactionDetailsBean);
		
		return "customer/viewAccountDetails";
	
	}
	
	@RequestMapping(value="/customerGetTransactionDetails", method = RequestMethod.POST)
	public String customerGetTransactionDetails(@ModelAttribute("accountGetTransactionDetailsBean")@Valid AccountGetTransactionDetailsBean accountGetTransactionDetailsBean, BindingResult result,ModelMap model) throws SurakshitException, Exception {
		
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<AccountGetTransactionDetailsBean> transaction= (List<AccountGetTransactionDetailsBean>) customerService.customerGetTransactionDetails(userDetails.getUsername());
		//accountGetTransactionDetailsBean.setTransaction(transaction);
		
		//check all annotations in the view bean
		if(result.hasErrors())
		{
			return "customer/viewAccountDetails";
		}
		
		//check other validations(custom)
		customerValidator.validate(accountGetTransactionDetailsBean, result);
		if(result.hasErrors())
		{
			return "customer/viewAccountDetails";
		}
		
		//accountDebitViewBean.setEmailId(userDetails.getUsername());
		
		//model.addAttribute("successMessage", "Your transactions are" + transaction);
		
		//model.addAttribute("successMessage", "Your transactions are");
		
		model.addAttribute("successMessage","Your transactions are");
		model.addAttribute("transaction", transaction);
		
		logger.info("Transactions related to customer are shown");
		
		return "customer/transactions";
	}
	
	
	@RequestMapping(value="/customerTransferAmount", method=RequestMethod.POST)
	public String customerTransferAmount(HttpServletRequest request,ModelMap model) throws SurakshitException, Exception {
		
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String accountNoSender=customerService.getAccountNo(userDetails.getUsername());
		
		String balanceSender= customerService.getBalance(userDetails.getUsername());
		
		AccountTransferBean accountTransferBean = new AccountTransferBean();
		
		accountTransferBean.setAccountIdSender(accountNoSender);
		
		accountTransferBean.setBalanceSender(balanceSender);
		
		model.addAttribute("accountTransferBean", accountTransferBean);
		
		return "customer/amountTransfer";
		
	}
	
	@RequestMapping(value="/customerTransferAmountDetails", method = RequestMethod.POST)
	public String customerTransferAmountDetails(@ModelAttribute("accountTransferBean")@Valid AccountTransferBean accountTransferBean, BindingResult result,ModelMap model) throws SurakshitException, Exception {
		
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String accountNo=customerService.getAccountNo(userDetails.getUsername());
		accountTransferBean.setAccountIdSender(accountNo);
		
		String balance= customerService.getBalance(userDetails.getUsername());
		accountTransferBean.setBalanceSender(balance);
		double balance1=Double.parseDouble(balance);
		
		//check all annotations in the view bean
		if(result.hasErrors())
		{
			return "customer/amountTransfer";
		}
		
		//check other validations(custom)
		customerValidator.validate(accountTransferBean, result);
		if(result.hasErrors())
		{
			return "customer/amountTransfer";
		}
		
		accountTransferBean.setEmailIdSender(userDetails.getUsername());
		
		Double finalBalance=customerService.customerTransferAmount(accountTransferBean);
		
		double amount = Double.parseDouble(accountTransferBean.getAmount());
		
		if(amount>5000 && amount<=balance1)
		{
			model.addAttribute("successMessage", "Your request is pending with the bank and your current balance is" + balance1);
		}
		else
		if(amount>0 && amount<=balance1)
		{
			model.addAttribute("successMessage", "Your account is debited with "+amount+ "and current balance is" + finalBalance);
		}
		else
		{
			model.addAttribute("successMessage", "Sorry. You dont have sufficient balance in your account");
		}
		
		secureLogger.info("This function is transfering funds from" +accountTransferBean.getEmailIdSender()+ "to" +accountTransferBean.getEmailIdReceiver() );
		
		return "customer/successTransfer";
	}
		

	//encrypt
	@RequestMapping(value="/customerToMerchantPaymentDetails", method = RequestMethod.POST)
	public String customerToMerchantPaymentDetails(@ModelAttribute("customerPaymentBean")@Valid CustomerPaymentBean customerPaymentBean, BindingResult result,ModelMap model) throws SurakshitException, Exception {
		

		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String accountNo=customerService.getAccountNo(userDetails.getUsername());
		
		String balance= customerService.getBalance(userDetails.getUsername());
		
		customerPaymentBean.setAccountId(accountNo);
		
		customerPaymentBean.setBalance(balance);
		
		model.addAttribute("CustomerPaymentBean", customerPaymentBean);
		
		
		List<CustomerPaymentBean> transaction= (List<CustomerPaymentBean>) customerService.custToMerchantPayment(userDetails.getUsername());
		
		if(result.hasErrors())
		{
			return "customer/pendingTransactions";
		}
		
		//check other validations(custom)
		customerValidator.validate(customerPaymentBean, result);
		if(result.hasErrors())
		{
			return "customer/pendingTransactions";
		}
		if(transaction!=null)
		{
			for(CustomerPaymentBean transactionBean: transaction)
			{
				transactionBean.setEncryptedTransactionId(maskUtility.getMaskedString(transactionBean.getTransactionId().toString()));
			}
			
			model.addAttribute("successMessage","Your payments to be made to the Merchant");
			model.addAttribute("transaction", transaction);
		}
		else
		{
			model.addAttribute("successMessage","There are nomore payments to be made to the merchant ");
		}
		
		return "customer/pendingTransactions";
	}
	
	//decrypt
	@RequestMapping(value="/customerMerchantPayment", method = RequestMethod.POST)
	public String customerMerchantPayment(HttpServletRequest request, ModelMap model) throws SurakshitException, Exception{
		
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String accountNo=customerService.getAccountNo(userDetails.getUsername());
		
		String balance= customerService.getBalance(userDetails.getUsername());
		
		CustomerPaymentBean customerPaymentBean= new CustomerPaymentBean();
		
		customerPaymentBean.setAccountId(accountNo);
		
		customerPaymentBean.setBalance(balance);
		
		model.addAttribute("customerPaymentBean", customerPaymentBean);
		
		
		
		//Integer transactionID =  Integer.parseInt(request.getParameter("ID"));
		
		String tranId= maskUtility.getOriginalString(request.getParameter("CrID"));
		Integer transactionID =Integer.parseInt(tranId);
		
		String status = (String) request.getParameter("approveReject");
		Double finalBal=0.0;
		if(status.equals("Approve")){
			finalBal=customerService.customerMakePaymentApproved(transactionID.toString());
		}else if(status.equals("Reject")){
			finalBal=customerService.customerMakePaymentRejected(transactionID.toString());
			}
		else
		if(status.equals("")){return "customer/pendingTransactions";}
		
		List<CustomerPaymentBean> transaction= (List<CustomerPaymentBean>) customerService.custToMerchantPayment(userDetails.getUsername());
		//encrypt here
		for(CustomerPaymentBean transactionBean: transaction)
		{
			transactionBean.setEncryptedTransactionId(maskUtility.getMaskedString(transactionBean.getTransactionId().toString()));
		}
	
		if(finalBal!=Double.parseDouble(balance))
		{
			model.addAttribute("successMessage","Your payments to be made to the Merchant and your current balance is" +finalBal);
			model.addAttribute("transaction", transaction);
			
		}
		else
		{
			model.addAttribute("successMessage","Sorry payment not made to the Merchant and your current balance is" +finalBal);
			model.addAttribute("transaction", transaction);
			
			
		}
		customerPaymentBean.setBalance(finalBal.toString());
		logger.info("Transferring amount between the customers");
		
		return "customer/pendingTransactions";
	}
	
	
	
	
	
	@RequestMapping(value="/customerUpdateInfo", method = RequestMethod.POST)
	public String customerUpdateInfo(ModelMap model, HttpServletRequest request) throws SurakshitException, Exception {
		
		InfoUpdateViewBean infoUpdateViewBean = new InfoUpdateViewBean();
		model.addAttribute("infoUpdateViewBean", infoUpdateViewBean);
		String originalMerchantString = UUID.randomUUID().toString();
		System.out.println("Original String: " +  originalMerchantString);
		HttpSession session = request.getSession();
		session.setAttribute("originalMerchantHashString", originalMerchantString);
		System.out.println("Set Session Attribute: " + session.getAttribute("originalMerchantHashString"));
		//infoUpdateViewBean.setOriginalMerchantHashString(originalMerchantString);
		return "customer/updatePersonalInfo";
	}
	
	@RequestMapping(value="/customerUpdatePersonalInfo", method = RequestMethod.POST)
	public String customerUpdatePersonalInfo(@ModelAttribute("infoUpdateViewBean")@Valid InfoUpdateViewBean infoUpdateViewBean, BindingResult result,ModelMap model) throws SurakshitException, Exception {
		if(result.hasErrors()) {
			return "customer/updatePersonalInfo";
		}
		
//		KeyPair privatePublicPair = keyGenerationUtility.generateKeyPair();
//		byte[] publicKeyByteArray = customerService.getPublicKeyByteArray();
//		PublicKey pubicKey = keyGenerationUtility.genPublicKeyFromKeyByte(publicKeyByteArray);
//		
		customerService.updatePersonalInfo(infoUpdateViewBean.getName(), infoUpdateViewBean.getAddress(), infoUpdateViewBean.getPhoneNumber());
		model.addAttribute("successMessage", "Successfully Updated Information.");
		
		return "Homepage/success";
		
	}
	
}
