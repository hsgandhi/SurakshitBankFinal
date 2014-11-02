package asu.bank.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import asu.bank.hibernateFiles.UserAttempts;
import asu.bank.utility.HibernateUtility;

@Repository
@Transactional(propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
public class SurakshitAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	HibernateUtility hibernateUtility;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication auth) throws IOException,
			ServletException {
		try
		{
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			
			UserAttempts userAttempts =(UserAttempts)hibernateUtility.getSession()
					.createQuery("FROM UserAttempts WHERE user.emailId= :email")
					.setParameter("email", userDetails.getUsername())
					.uniqueResult();
			
			userAttempts.setNoOfAttempts(0);
			
			hibernateUtility.getSession().update(userAttempts);
			
			request.getRequestDispatcher("/goToHomePage").forward(request, response);
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
			request.getRequestDispatcher("/handleAllException").forward(request, response);
		}
			 
        }		
	}
