package asu.bank.security.service;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import asu.bank.security.dao.AuthenticationDao;
import asu.bank.security.viewBeans.UserDtls;

@Service("userDetailsService")
public class AuthenticationService implements UserDetailsService {
	
	@Autowired
	private AuthenticationDao authenticationDao;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		UserDtls userDtls ;
		try
		{
			userDtls = authenticationDao.getUserDtlsFromUserName(username);
		}
		catch(BadCredentialsException badExp)
		{
			throw badExp;
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
			throw new BadCredentialsException("Some error occured. Please try again");
		}
		return userDtls;
	}
	
}
