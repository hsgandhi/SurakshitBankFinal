package asu.bank.utility;

import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import asu.bank.login.controller.LoginController;

@Component
public class OneTimePasswordGenerator {
	
	private static final Logger logger = Logger.getLogger(OneTimePasswordGenerator.class);
	
	public String getOneTimePassword()
	{
		String possChar="123456789QWERTYUIOPASDFGHJKLZXCVBNM0#$";
		String otp="";
		Random random = new Random();
		for(int i=0;i<8;i++)
		{
			int nextInt = random.nextInt(possChar.length());
			otp += possChar.charAt(nextInt);
		}
		
		return otp;
	}
	
	public boolean getApprovalOfGovt()
	{
		Random random = new Random();
		int randomNo = random.nextInt(30);
		
		if((randomNo % 3) ==0)
		{
			logger.info("Allow admin to view the SSN details");
			return true;
		}
		else
			return false;
			
	}
	
	

}
