package asu.bank.login.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import asu.bank.login.service.LoginService;
import asu.bank.login.validator.LoginValidator;
import asu.bank.login.viewBeans.MenuBean;
import asu.bank.login.viewBeans.TrialBean;
import asu.bank.utility.EmailUtilityUsingSSL;
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
	 
	 private static final Logger logger = Logger.getLogger(LoginController.class);

	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request,ModelMap model) {
		System.out.println("I am here");
		return "login/login";
 
	}
	
	@RequestMapping(value="/testCaptcha", method = RequestMethod.POST)
	public String testCaptcha(HttpServletRequest request,HttpServletResponse response,ModelMap model)throws SurakshitException, Exception {
		
		String remoteAddress = request.getRemoteAddr();
		String challangeField = request.getParameter("recaptcha_challenge_field");
		String responseField = request.getParameter("recaptcha_response_field");

		ReCaptchaResponse reCaptchaResponse = this.reCaptcha.checkAnswer(remoteAddress, challangeField, responseField);

		 if(reCaptchaResponse.isValid()) {
			 model.addAttribute("errMsg", "ok");
		 } else {
			 model.addAttribute("errMsg", "CaptchaException");
		 }
		 return "Homepage/nilFragment";
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
		String regex = "";
		
		if(password.equals("") || password.length()<8 )//|| !password.matches(regex))
			throw new SurakshitException("EnterPassword");
		if(userOtp.equals("") || otp.length()<8)
			throw new SurakshitException("EnterOTP");
		
		if(userOtp.equals(otp))
			loginService.changePassword(password, emailID);
		
		return("index.jsp");
	}
	
	
	@RequestMapping(value="/adminTrialSubmit", method = RequestMethod.POST)
	public String showHomePage(@ModelAttribute("trialBean")@Valid TrialBean trialBean, BindingResult result, ModelMap model, HttpServletRequest request) throws SurakshitException, Exception
	{
		
		//loginService.testRoom("hsgandhi@asu.edu", "hi", "hi");
		loginValidator.validate(trialBean, result);
		
		
		if(result.hasErrors())
		{
			return "Homepage/trial";
		}
		else
		{
			System.out.println(trialBean.getName() + "/n" +
		trialBean.getMail() +
		"\n" + trialBean.getDob());
		}
		
		//how to get roles from security context
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println(userDetails.getUsername() + "\n" + userDetails.getPassword());
		
		 for (GrantedAuthority authority : userDetails.getAuthorities()) {
			 System.out.println(authority.getAuthority().toString());
         }  
		 
		return "login/login";
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
			 menuBeanList.add(getPopulatedMenuBean("Working Menu","adminTrial"));
			 menuBeanList.add(getPopulatedMenuBean("text1","actionValue1"));
			 menuBeanList.add(getPopulatedMenuBean("text2","actionValue2"));
			 menuBeanList.add(getPopulatedMenuBean("text3","actionValue3"));
			 menuBeanList.add(getPopulatedMenuBean("text4","actionValue4"));
		 }
		 else if("EMPLOYEE".equalsIgnoreCase(role))
		 {
			 
		 }
		 else if("CUSTOMER".equalsIgnoreCase(role))
		 {
			 menuBeanList.add(getPopulatedMenuBean("Credit Balance","customerCreditBalance"));
		 }
		 else if("MERCHANT".equalsIgnoreCase(role))
		 {
			 
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
