package asu.bank.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import asu.bank.login.dao.LoginDao;
import asu.bank.utility.SurakshitException;

@Service
@Transactional(propagation=Propagation.REQUIRED,rollbackFor={SurakshitException.class,Exception.class})
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	LoginDao loginDao;

	@Override
	public void testRoom(String name, String age, String sex)  throws SurakshitException,Exception{
		System.out.println("in service");
		loginDao.testRoom(name, age, sex);
		//loginDao.testRoom("Hardikkjahsdfhaksdfhashfshffhashfkhfashfklhfahdfh", "17", "M");
		//loginDao.testRoomReadOnly();
	}

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
