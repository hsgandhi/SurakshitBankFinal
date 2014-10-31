package asu.bank.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service("mailService")
public class EmailUtilityUsingSSL {
	 @Autowired
	  private MailSender mailSender;

	
	public void sendMail(String emailID, String otp) throws Exception
	{/*
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
 
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("surakshitbank@gmail.com","Group7bank");
				}
			});
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("surakshitbank@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("hsgandhi@asu.edu"));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler," +
					"\n\n No spam to my email, please!");
 
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		*/
		/*
		final String username = "surakshitbank@gmail.com";
		final String password = "Group7bank";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("surakshitbank@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("hsgandhi@asu.edu"));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler,"
				+ "\n\n No spam to my email, please!");
 
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		*/
			SimpleMailMessage message = new SimpleMailMessage();
	 
			message.setFrom("surakshitbank@gmail.com");
			message.setTo(emailID);
			message.setSubject("Your one time password");
			message.setText(otp);
			mailSender.send(message);	
	}
	
	}