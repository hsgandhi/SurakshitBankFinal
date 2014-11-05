package asu.bank.utility;


import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service("mailService")
public class EmailUtilityUsingSSL {
	 @Autowired
	  private JavaMailSender mailSender;
	 
	 private final String PATH="D:\\lexer.jar";

	
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
		
		/*
			SimpleMailMessage message = new SimpleMailMessage();
		
		message.setFrom("surakshitbank@gmail.com");
		message.setTo(emailID);
		message.setSubject("Your one time password");
		message.setText(otp);
		mailSender.send(message);
	*/
		
		if(otp==null || otp.equals(""))
			throw new Exception("No OTP Generated");
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		 
		helper.setFrom("surakshitbank@gmail.com");
		helper.setTo(emailID);
		helper.setSubject("Your one time password");
		helper.setText(otp);
		
		//FileSystemResource file = new FileSystemResource(PATH);
		//helper.addAttachment(file.getFilename(), file);
		
		mailSender.send(message);
	}
	
	public void sendMailWithAttachment(String emailID, String msg, String subject) throws Exception
	{
		if(msg==null || msg.equals(""))
			throw new Exception("No Text entered for mail");
		
		if(subject==null || subject.equals(""))
			throw new Exception("No Subject entered for mail");
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		 
		helper.setFrom("surakshitbank@gmail.com");
		helper.setTo(emailID);
		helper.setSubject(subject);
		helper.setText(msg);
		
		FileSystemResource file = new FileSystemResource(PATH);
		helper.addAttachment(file.getFilename(), file);
		
		mailSender.send(message);
	}
	
	}