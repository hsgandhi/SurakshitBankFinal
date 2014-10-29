package asu.bank.login.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
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
import asu.bank.utility.SurakshitException;


@Controller
public class LoginController {
	 
	 @Autowired
	LoginService loginService;
	 
	 @Autowired
	 LoginValidator loginValidator;
	 
	 @Autowired
		ReCaptcha reCaptcha;
	 
	 private static final Logger logger = Logger.getLogger(LoginController.class);

	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request,ModelMap model) {
		return "login/login";
 
	}
	
	@RequestMapping(value="/testCaptcha", method = RequestMethod.POST)
	public void testCaptcha(HttpServletRequest request,HttpServletResponse response,ModelMap model)throws Exception {
		
		String remoteAddress = request.getRemoteAddr();
		String challangeField = request.getParameter("recaptcha_challenge_field");
		String responseField = request.getParameter("recaptcha_response_field");

		ReCaptchaResponse reCaptchaResponse = this.reCaptcha.checkAnswer(remoteAddress, challangeField, responseField);

		 if(reCaptchaResponse.isValid()) {
			 throw new SurakshitException("ok");
		 } else {
			 throw new SurakshitException("CaptchaException"); 
		 }
	}
	
	@RequestMapping(value="/adminTrialSubmit", method = RequestMethod.POST)
	public String showHomePage(@ModelAttribute("trialBean")@Valid TrialBean trialBean, BindingResult result, ModelMap model) throws SurakshitException, Exception
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

	@RequestMapping(value="/loginfailed", method = RequestMethod.GET)
	public String loadFrames(ModelMap model, HttpServletRequest request) {
		System.out.println("in login failed");
		
		Exception exception = 
                (Exception) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");

		String error = "";
		if (exception instanceof BadCredentialsException) {
			error = "Invalid username and password!";
		}else if(exception instanceof LockedException) {
			error = exception.getMessage();
		}else{
			error = "Invalid username and password!";
		}

		model.addAttribute("errorDisplay", error);
		return "login/login";
	}
	
	@RequestMapping(value="/goToHomePage", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('CUSTOMER','ADMIN','EMPLOYEE','MERCHANT')")
	public String goTOHomePage(ModelMap model) throws Exception {
		System.out.println("in goToHomePage");
		logger.debug("Finally logger is implemented");
		
		
		return "Homepage/homepage";
	}
	
	@RequestMapping(value="getMenuDetails", method= RequestMethod.GET)
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
	
	@RequestMapping(value="/dummyFragment")
	@PreAuthorize("hasAnyRole('CUSTOMER','ADMIN','EMPLOYEE','MERCHANT')")
	public String loadDummyFragment(){
	return "Homepage/dummyFragment";
	}
	
	
	@RequestMapping(value="/adminTrial")
	public String viewTrialPage(ModelMap model) {
	
		TrialBean trialBean = new TrialBean();
		model.addAttribute("trialBean",trialBean);
		return "Homepage/trial";
	}
	
}
