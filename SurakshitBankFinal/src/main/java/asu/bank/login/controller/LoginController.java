package asu.bank.login.controller;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import asu.bank.hibernateFiles.Room206;
import asu.bank.utility.HibernateUtility;


@Controller
public class LoginController {
	
	
	@Autowired
	private HibernateUtility hibernateUtility;
	
	
	public HibernateUtility getHibernateUtility() {
		return hibernateUtility;
	}

	public void setHibernateUtility(HibernateUtility hibernateUtility) {
		this.hibernateUtility = hibernateUtility;
	}


	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(ModelMap model) {
		System.out.println("in login");
		
		try
		{
			
		SessionFactory sessionFac = hibernateUtility.getSessionFactory(); 
		Session session =	sessionFac.openSession();
        Transaction tx = session.beginTransaction();
        
        List<Room206> testResult= (List<Room206>)session.createCriteria(Room206.class).list();
        
        for(Room206 tst:testResult)
        {
        	System.out.println(tst.getName());
        }
        
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return "login/login";
 
	}
	
	@RequestMapping(value="/loginAction", method = RequestMethod.POST)
	public String showHomePage()
	{
		System.out.println("go to hompage");
		return "Homepage/homepage";
	}

	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String loadFrames(ModelMap model) {
		System.out.println("in login");
		return "login/login";
	}

}
