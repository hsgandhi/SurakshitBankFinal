package asu.bank.login.dao;

import asu.bank.utility.SurakshitException;

public interface LoginDao {

	public boolean checkIfUserExists(String emailID)throws SurakshitException,Exception;
	public void changePassword(String password, String emailID)throws SurakshitException,Exception;
}
