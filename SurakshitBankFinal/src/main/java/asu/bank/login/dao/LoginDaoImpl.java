package asu.bank.login.dao;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import asu.bank.hibernateFiles.User;
import asu.bank.utility.HibernateUtility;
import asu.bank.utility.SurakshitException;
import asu.bank.utility.UserDataUtility;

@Repository
public class LoginDaoImpl implements LoginDao {
	
	@Autowired
	HibernateUtility hibernateUtility;
	@Autowired
	UserDataUtility userDataUtility;
	
	@Override
	public void testRoom(String emailId, String age, String sex) throws SurakshitException,Exception {
		System.out.println("in dao");
		
		User user = userDataUtility.getUserDtlsFromEmailId(emailId);
		
	}

	@Override
	public void testRoomReadOnly() throws Exception {
		Session session= hibernateUtility.getSession();

		/*List<Room206> testResult= (List<Room206>)session.createCriteria(Room206.class).list();
        
        for(Room206 tst:testResult)
        {
        	System.out.println(tst.getName());
        }
        
		*/
	}

}
