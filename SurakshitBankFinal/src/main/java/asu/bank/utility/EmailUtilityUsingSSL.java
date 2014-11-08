package asu.bank.utility;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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
	 
	 private final String PATH="C:\\Surakshitbank\\SurakshitBankUser.jar";
	 private final String PATHDOWN="Downloads:\\SurakshitBankUser.jar";

	
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
	
	public void sendMailWithAttachmentForJar(String emailID, String msg, String subject) throws Exception
	{
		String hiddenmessage ="";
		String link ="http://www.public.asu.edu/~jmvaze/SurakshitBankUser.jar";
		//hiddenmessage = "Please download file for generation of encrtypted key using the link +" link;
		
		if(msg==null || msg.equals(""))
			throw new Exception("No Text entered for mail");
		
		if(subject==null || subject.equals(""))
			throw new Exception("No Subject entered for mail");
		
		File file = null;
		String fileName= emailID.substring(0, emailID.indexOf("@"));
		try
		{
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		 
		helper.setFrom("surakshitbank@gmail.com");
		helper.setTo(emailID);
		helper.setSubject(subject);
		helper.setText("Your account has been enabled. \n Please find attached text file "+ fileName +" containing the private key. \n Please find the attached jar file to be used in critical transactions \n" + hiddenmessage);
		
		file = new File(fileName+ ".txt");
		try (FileOutputStream fop = new FileOutputStream(file)) {
			if (!file.exists()) {
				file.createNewFile();
			}
			byte[] contentInBytes = msg.getBytes();
			 
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
		}
		catch(IOException e)
		{
			throw new Exception("File not created for privatekey");
		}
		
		FileSystemResource fileSysResForJar = new FileSystemResource(PATH);
		helper.addAttachment(fileSysResForJar.getFilename(), fileSysResForJar);
		
		FileSystemResource fileSysResForTextFile = new FileSystemResource(file);
		helper.addAttachment(fileSysResForTextFile.getFilename(), fileSysResForTextFile);
		
		mailSender.send(message);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception("Mail not sent.");
		}
		finally
		{ 
			if (file.exists()) {
				file.delete();
			}
		}
	}
	
	
	public void sendLogFileToAdmin(String emailID, boolean isSecureLogFile) throws Exception
	{
		String filePath=null;
		String msg=null;
		String subject=null;
		
		if(isSecureLogFile)
		{
			filePath="C:\\Surakshitbank\\secureLogger.html";
			msg = "Please find attached secure logging log file. Open it in a browser.";
			subject ="Secure logging file";
		}
		else 
		{
			filePath="C:\\Surakshitbank\\logger.txt";
			msg = "Please find attached system logging log file.";
			subject ="System log file";
		}
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		 
		helper.setFrom("surakshitbank@gmail.com");
		helper.setTo(emailID);
		helper.setSubject(subject);
		helper.setText(msg);
		
		FileSystemResource fileSysResForTextFile = new FileSystemResource(filePath);
		helper.addAttachment(fileSysResForTextFile.getFilename(), fileSysResForTextFile);
		
		mailSender.send(message);
	}
	
	}