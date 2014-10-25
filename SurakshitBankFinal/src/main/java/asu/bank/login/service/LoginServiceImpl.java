package asu.bank.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import asu.bank.login.dao.LoginDao;

@Service
@Transactional(propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	LoginDao loginDao;

	@Override
	public void testRoom(String name, String age, String sex)  throws Exception{
		System.out.println("in service");
		//loginDao.testRoom(name, age, sex);
		//loginDao.testRoom("Hardikkjahsdfhaksdfhashfshffhashfkhfashfklhfahdfh", "17", "M");
		//loginDao.testRoomReadOnly();
	}
	
	

}
