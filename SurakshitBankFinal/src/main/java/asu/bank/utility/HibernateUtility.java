package asu.bank.utility;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateUtility {
private SessionFactory sessionFactory;
private Session session;

public Session getSession() {
	return session;
}

public void setSession(Session session) {
	this.session = sessionFactory.openSession();
}

public SessionFactory getSessionFactory() {
	return sessionFactory;
}

public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
}

}
