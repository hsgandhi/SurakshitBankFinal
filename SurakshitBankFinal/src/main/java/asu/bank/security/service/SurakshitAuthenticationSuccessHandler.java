package asu.bank.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.tanesha.recaptcha.ReCaptcha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
public class SurakshitAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	ReCaptcha reCaptcha;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication auth) throws IOException,
			ServletException {
		
			 request.getRequestDispatcher("/goToHomePage").forward(request, response);
			 
        }		
	}
