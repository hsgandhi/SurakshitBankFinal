package asu.bank.login.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import asu.bank.login.dao.LoginDao;
import asu.bank.login.dao.LoginDaoImpl;
import asu.bank.utility.SurakshitException;

@Service
@Transactional(propagation=Propagation.REQUIRED,rollbackFor={SurakshitException.class,Exception.class})
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	LoginDao loginDao;

	private static final Logger logger = Logger.getLogger(LoginServiceImpl.class);
	 private static final Logger secureLogger = Logger.getLogger("secure");


	@Override
	public boolean checkIfUserExists(String emailID) throws SurakshitException,
			Exception {
		
		return loginDao.checkIfUserExists(emailID);
	}

	@Override
	public void changePassword(String password,String emailID) throws SurakshitException,
			Exception {
		// TODO Auto-generated method stub
		loginDao.changePassword(password,emailID);
	}
	
	

}
