package asu.bank.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import asu.bank.security.viewBeans.UserDtls;

@Repository
@Transactional(propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
public class SurakshitAuthenticationFailureHandler implements
		AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException auth)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		String userName = request.getParameter("j_username");
		Integer noOfAttempt = getNoOfFailedAttempts(userName);
		
			response.sendRedirect("loginfailed");
	}

	private Integer getNoOfFailedAttempts(String username) {
		// TODO Auto-generated method stub
		return null;
	}

}
