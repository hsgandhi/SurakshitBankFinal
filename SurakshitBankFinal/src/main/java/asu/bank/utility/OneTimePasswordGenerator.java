package asu.bank.utility;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class OneTimePasswordGenerator {
	
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

}
