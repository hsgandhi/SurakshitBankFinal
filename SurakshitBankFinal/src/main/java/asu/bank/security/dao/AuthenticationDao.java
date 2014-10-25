package asu.bank.security.dao;

import org.springframework.security.authentication.BadCredentialsException;

import asu.bank.security.viewBeans.UserDtls;

public interface AuthenticationDao {

	UserDtls getUserDtlsFromUserName(String userName) throws BadCredentialsException, Exception;
}
