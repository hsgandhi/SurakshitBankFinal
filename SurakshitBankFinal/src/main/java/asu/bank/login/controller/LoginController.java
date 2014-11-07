package asu.bank.login.controller;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import asu.bank.login.service.LoginService;
import asu.bank.login.validator.LoginValidator;
import asu.bank.login.viewBeans.MenuBean;
import asu.bank.login.viewBeans.TrialBean;
import asu.bank.utility.EmailUtilityUsingSSL;
import asu.bank.utility.KeyGenerataionUtility;
import asu.bank.utility.OneTimePasswordGenerator;
import asu.bank.utility.SurakshitException;


@Controller
public class LoginController {
	 
	 @Autowired
	LoginService loginService;
	 
	 @Autowired
	 LoginValidator loginValidator;
	 
	 @Autowired
	 ReCaptcha reCaptcha;
	 
	 @Autowired
	 EmailUtilityUsingSSL emailUtilityUsingSSL;
	 
	 @Autowired
	 OneTimePasswordGenerator oneTimePasswordGenerator;
	 
	 @Autowired
	 KeyGenerataionUtility keyGenerationUtility;
	 
	 private static final Logger logger = Logger.getLogger(LoginController.class);
	 private static final Logger secureLogger = Logger.getLogger("secure");

	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request,ModelMap model) {
		logger.info("Common Logger files");
		secureLogger.info("Secure logge file");
		
		return "login/login";
 
	}
	
	@RequestMapping(value="/testCaptcha", method = RequestMethod.POST)
	public String testCaptcha(HttpServletRequest request,HttpServletResponse response,ModelMap model)throws SurakshitException, Exception {
		/*
		String remoteAddress = request.getRemoteAddr();
		String challangeField = request.getParameter("recaptcha_challenge_field");
		String responseField = request.getParameter("recaptcha_response_field");
		
		ReCaptchaResponse reCaptchaResponse = this.reCaptcha.checkAnswer(remoteAddress, challangeField, responseField);

		 if(reCaptchaResponse.isValid()) {
			 model.addAttribute("errMsg", "ok");
		 } else {
			 model.addAttribute("errMsg", "CaptchaException");
		 }
		 */
		 model.addAttribute("errMsg", "ok");
		 return "Homepage/nilFragment";
	}
	
	//TODO implement in functionality and remove from here
	private void pkiTempFunction(HttpServletRequest request) throws SurakshitException, Exception{
		
		try
		{
			//user approval
		String filePath = request.getContextPath()+"/WEB-INF/jar/SurakshitBankUser.jar";
			
		KeyPair privatePublicPair = keyGenerationUtility.generateKeyPair();
		
		String privateKey = new String(Base64.encode(privatePublicPair.getPrivate().getEncoded()));
		emailUtilityUsingSSL.sendMailWithAttachmentForJar("gandhihardik17@gmail.com", privateKey, "Private key for critical transactions.");
		
		//send the hash string
		String originalString = UUID.randomUUID().toString();
		HttpSession sesssion = request.getSession();
		sesssion.setAttribute("originalHashString", originalString);
		
		//decrypt and compare the string
		String encryptedString= null;//get the encrypted string
		byte[] publicKeyByteArray= Base64.encode(privatePublicPair.getPublic().getEncoded());
		PublicKey pubicKey = keyGenerationUtility.genPublicKeyFromKeyByte(publicKeyByteArray);
		String decryptedString = keyGenerationUtility.decryptUsingPublicKey(pubicKey,encryptedString);
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
			throw new Exception("Problem in generation of keys");
		}
		
	}

	@RequestMapping(value="/forgotPassword", method = RequestMethod.POST)
	public String forgotPassword(HttpServletRequest request,ModelMap model)throws SurakshitException, Exception {
		
		String emailID=request.getParameter("j_username");
		
		
		if(emailID.equals(""))
			throw new SurakshitException("UserNotFound");
		
		boolean userExists=loginService.checkIfUserExists(emailID);
		
		if(!userExists)
			throw new SurakshitException("UserNotFound");
		
		String otp = oneTimePasswordGenerator.getOneTimePassword();
		HttpSession sesssion = request.getSession();
		sesssion.setAttribute("oneTimePass", otp);
		sesssion.setAttribute("emailId", emailID);
		
		emailUtilityUsingSSL.sendMail(emailID,otp);
		
		return("login/enterOTP");
	}
	
	@RequestMapping(value="/checkOTP", method = RequestMethod.POST)
	public String checkOTP(HttpServletRequest request,ModelMap model)throws SurakshitException, Exception {
		HttpSession sesssion = request.getSession();
		
		String emailID=sesssion.getAttribute("emailId").toString();
		String otp = sesssion.getAttribute("oneTimePass").toString();
		String password = request.getParameter("newPassword");
		String userOtp = request.getParameter("otp");
		String regex = "[A-Za-z0-9$@]+";
		
		if(password.equals("") || password.length()<8  || !password.matches(regex))
			throw new SurakshitException("EnterPassword");
		if(userOtp.equals("") || otp.length()<8)
			throw new SurakshitException("EnterOTP");
		
		if(userOtp.equals(otp))
			loginService.changePassword(password, emailID);
		else
			throw new SurakshitException("OTPFailed");
		
		model.addAttribute("successMessage", "Password changed successfully");
		
		return("Homepage/success");
	}
	
	
/*
	@RequestMapping(value="/loginfailed", method = RequestMethod.GET)
	public String loginFailed(ModelMap model, HttpServletRequest request) {
		System.out.println("in login failed");
		
		throw new SurakshitException("InvalidUserNameOrPassword");
	}
	*/
	
	@RequestMapping(value="/loginfailedPost", method = RequestMethod.POST)
	public String loginfailedPost(ModelMap model, HttpServletRequest request) {
		String error = "InvalidUserNameOrPassword";
		throw new SurakshitException(error);
	}
	
	@RequestMapping(value="/accountLockPost", method = RequestMethod.POST)
	public String accountLockPost(ModelMap model) {
		throw new SurakshitException("AccountLocked");
	}
	
	@RequestMapping(value="/goToHomePage", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('CUSTOMER','ADMIN','EMPLOYEE','MERCHANT')")
	public String goTOHomePage(ModelMap model) throws Exception {
		System.out.println("in goToHomePage");
		logger.debug("Finally logger is implemented");
		
		
		return "Homepage/homepage";
	}
	
	@RequestMapping(value="/getMenuDetails", method= RequestMethod.POST)
	@PreAuthorize("hasAnyRole('CUSTOMER','ADMIN','EMPLOYEE','MERCHANT')")
	public String getMenuDtls(ModelMap model)
	{
		List<MenuBean> menuBeanList = new ArrayList<MenuBean>();
		String role=null;
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println(userDetails.getUsername() + "\n" + userDetails.getPassword());
		
		 for (GrantedAuthority authority : userDetails.getAuthorities()) {
			 role =authority.getAuthority().toString();
        }  
		 
		 if("ADMIN".equalsIgnoreCase(role))
		 {
			 menuBeanList.add(getPopulatedMenuBean("MyProfile","adminMyProfile"));
			 menuBeanList.add(getPopulatedMenuBean("Add Internal User","adminGetInternalUserDetailsAdd"));
			 menuBeanList.add(getPopulatedMenuBean("PendingTransactions","adminGetPendingTransactions"));
			 menuBeanList.add(getPopulatedMenuBean("Approve/Reject External User Accounts","adminGetPendingExtUserRequests"));
			 menuBeanList.add(getPopulatedMenuBean("Get Sytem Log File","adminGetSytemLogFiles"));
			 menuBeanList.add(getPopulatedMenuBean("Get Secure Log File","adminGetSecureLogFiles"));
		 }
		 else if("EMPLOYEE".equalsIgnoreCase(role))
		 {
			 menuBeanList.add(getPopulatedMenuBean("My Profile","employeeMyProfile"));
			 menuBeanList.add(getPopulatedMenuBean("Approve Transactions","employeeGetPendingTransactions"));
			 menuBeanList.add(getPopulatedMenuBean("Create a Transaction","employeeGetTransactionDetails"));
			 menuBeanList.add(getPopulatedMenuBean("View/Modify/Delete Transactions","employeeGetUserEmail"));
			 menuBeanList.add(getPopulatedMenuBean("Add External User","employeeGetUserDetails"));
			 menuBeanList.add(getPopulatedMenuBean("View/Modify External User","employeeModifyGetUserDetails"));
		 }
		 else if("CUSTOMER".equalsIgnoreCase(role))
		 {
			 menuBeanList.add(getPopulatedMenuBean("Credit Balance","customerCreditBalance"));
			 menuBeanList.add(getPopulatedMenuBean("Debit Amount","customerDebitAmount"));
			 menuBeanList.add(getPopulatedMenuBean("View Transactions","customerViewAccountDetails"));
			 menuBeanList.add(getPopulatedMenuBean("Transfer Amount","customerTransferAmount"));
			 menuBeanList.add(getPopulatedMenuBean("Pending Payments","customerToMerchantPaymentDetails"));
			 menuBeanList.add(getPopulatedMenuBean("Update Info","customerUpdateInfo"));
		 }
		 else if("MERCHANT".equalsIgnoreCase(role))
		 {
			 menuBeanList.add(getPopulatedMenuBean("Request Customer Payment","merchantReqCustPay"));
			 menuBeanList.add(getPopulatedMenuBean("Update Personal Information","merchantUpdateInfo"));
			 menuBeanList.add(getPopulatedMenuBean("Credit Balance","merchantCreditBalance"));
			 menuBeanList.add(getPopulatedMenuBean("Debit Amount","merchantDebitAmount"));
			 menuBeanList.add(getPopulatedMenuBean("Transfer","merchantTransferAmount"));
			 menuBeanList.add(getPopulatedMenuBean("View Transactions","merchantViewAccountDetails"));
		 }
		 
		 model.addAttribute("menuList",menuBeanList);
		 
		 return "Homepage/menu";
		
	}

	private MenuBean getPopulatedMenuBean(String text, String value) {
		MenuBean menuBean = new MenuBean();
		menuBean.setViewText(text);
		menuBean.setActionValue(value);
		return menuBean;
	}

	@RequestMapping(value="/accessDenied")
	public String accessDeniedPage(ModelMap model) {
		return "login/accessDenied";
	}
	
	@RequestMapping(value="/dummyFragment", method= RequestMethod.POST)
	@PreAuthorize("hasAnyRole('CUSTOMER','ADMIN','EMPLOYEE','MERCHANT')")
	public String loadDummyFragment(){
	return "Homepage/dummyFragment";
	}
	
	
	@RequestMapping(value="/adminTrial", method=RequestMethod.POST)
	public String viewTrialPage(ModelMap model) {
	
		TrialBean trialBean = new TrialBean();
		model.addAttribute("trialBean",trialBean);
		return "Homepage/trial";
	}
	
	@RequestMapping(value="/handleAllException", method=RequestMethod.POST)
	public void handleAllException(ModelMap model) throws Exception {
		throw new Exception("Some problem occured. Please try again.");
	}
	
	@RequestMapping(value="/finalLogOut", method=RequestMethod.GET)
	public String finalLogOut(HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws Exception {
		/*
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");// HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setHeader("Expires", "Fri, 17 Mar 2010 06:00:00 GMT");
        response.setHeader("Last-Modified", new Date().toString());
        response.setHeader("Pragma", "no-cache");
        
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath(request.getContextPath());
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        */
		return "Logout/logout";
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public String logout(ModelMap model) throws Exception {
		return "Logout/finalLogout";
	}
	
}
