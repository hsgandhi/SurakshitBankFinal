package asu.bank.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import asu.bank.login.viewBeans.TrialBean;
import asu.bank.utility.SurakshitException;


@Controller
public class LoginController {
	 
	 @Autowired
	LoginService loginService;
	 
	 @Autowired
	 LoginValidator loginValidator;
	 
	 private static final Logger logger = Logger.getLogger(LoginController.class);

	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request,ModelMap model) {
		System.out.println("in login");
		request.getParameter("j_username");
		try
		{
		//loginService.testRoom("Jaydeep", "18", "M");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return "login/login";
 
	}
	
	@RequestMapping(value="/adminTrial", method = RequestMethod.POST)
	public String showHomePage(@ModelAttribute("trialBean")@Valid TrialBean trialBean, BindingResult result, ModelMap model)
	{
		loginValidator.validate(trialBean, result);
		
		
		if(result.hasErrors())
		{
			return "Homepage/homepage";
		}
		else
		{
			System.out.println(trialBean.getName() + "/n" +
		trialBean.getMail() +
		"\n" + trialBean.getDob());
		}
		
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

		
		model.addAttribute("errorDisplay", "display");
		return "login/login";
	}
	
	@RequestMapping(value="/goToHomePage", method = RequestMethod.GET)
	//@RequestMapping(value="/goToHomePage")
	public String goTOHomePage(ModelMap model) throws Exception {
		System.out.println("in goToHomePage");
		logger.debug("Finally logger is implemented");
		
		TrialBean trialBean = new TrialBean();
		model.addAttribute("trialBean",trialBean);
		/*
		if(true)
			throw new SurakshitException("LowBalance");*/
		
		return "Homepage/homepage";
	}

	//@RequestMapping(value="/accessDenied", method = RequestMethod.GET)
	@RequestMapping(value="/accessDenied")
	public String accessDeniedPage(ModelMap model) {
		return "login/accessDenied";
	}
	
}
