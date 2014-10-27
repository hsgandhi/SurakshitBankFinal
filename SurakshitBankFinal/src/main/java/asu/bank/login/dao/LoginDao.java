package asu.bank.login.dao;

import asu.bank.utility.SurakshitException;

public interface LoginDao {

	public void testRoom(String name, String age, String sex)  throws SurakshitException,Exception;
	public void testRoomReadOnly() throws Exception;
}
