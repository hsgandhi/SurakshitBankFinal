package asu.bank.login.dao;

import java.math.BigDecimal;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import asu.bank.hibernateFiles.Account;
import asu.bank.utility.HibernateUtility;

@Repository
public class LoginDaoImpl implements LoginDao {
	
	/*@Autowired
	private SessionFactory sessionFactory;*/
	@Autowired
	HibernateUtility hibernateUtility;
	
	@Override
	public void testRoom(String name, String age, String sex) throws Exception {
		System.out.println("in dao");
		
		Account account = new Account();
		
		hibernateUtility.getSession().save(account);
		hibernateUtility.getSession().flush();
		System.out.println(account.getAccountId());
		throw new Exception("Abort the transacion");
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
