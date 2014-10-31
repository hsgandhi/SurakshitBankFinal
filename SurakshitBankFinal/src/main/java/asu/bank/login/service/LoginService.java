package asu.bank.login.service;

import asu.bank.utility.SurakshitException;

public interface LoginService {
	
	public void testRoom(String name, String age, String sex)  throws SurakshitException,Exception;
	public boolean checkIfUserExists(String emailID) throws SurakshitException,Exception;
	public void changePassword(String password, String emailID)throws SurakshitException,Exception;

}
