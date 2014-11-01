package asu.bank.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import asu.bank.hibernateFiles.User;
import asu.bank.hibernateFiles.UserAttempts;
import asu.bank.utility.HibernateUtility;
import asu.bank.utility.UserDataUtility;

@Repository
@Transactional(propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
public class SurakshitAuthenticationFailureHandler implements
		AuthenticationFailureHandler {
	
	private static final Integer THRESHOLD = 5;
	
	@Autowired
	HibernateUtility hibernateUtility;
	
	@Autowired
	UserDataUtility userDataUtility;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException auth)
			throws IOException, ServletException  {
		// TODO Auto-generated method stub
		
		if(!auth.getClass().isAssignableFrom(LockedException.class)) {
			try
			{
			String userName = request.getParameter("j_username");
			Integer noOfAttempt = updateFailedAttempts(userName);
			
			if(noOfAttempt!=null && noOfAttempt>=THRESHOLD)
				lockUserAccount(userName);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			request.getRequestDispatcher("/loginfailedPost").forward(request, response);
		}
		else
			request.getRequestDispatcher("/accountLockPost").forward(request, response);
	}

	private void lockUserAccount(String userName)throws Exception  {
		// TODO Auto-generated method stub
			User user = userDataUtility.getUserDtlsFromEmailId(userName);
			user.setIsAccountLocked("1");
			
			hibernateUtility.getSession().update(user);
	}

	private Integer updateFailedAttempts(String username) throws Exception  {
		// TODO Auto-generated method stub
		
		UserAttempts userAttempts =(UserAttempts)hibernateUtility.getSession()
				.createQuery("FROM UserAttempts WHERE user.emailId= :email")
				.setParameter("email", username)
				.uniqueResult();
		
		if(userAttempts==null)
			return null;
		
		Integer finalNoOfAttempts=userAttempts.getNoOfAttempts()+1;
		
		userAttempts.setNoOfAttempts(finalNoOfAttempts);
		//userAttempts.setLastUpdated(new Date());
		
		hibernateUtility.getSession().update(userAttempts);
		
		return finalNoOfAttempts;
	}

}
