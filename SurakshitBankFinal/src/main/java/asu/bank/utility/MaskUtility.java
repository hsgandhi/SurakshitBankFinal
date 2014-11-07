package asu.bank.utility;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;

@Component
public class MaskUtility {
	
	public static Map<String,String> maskMap= new HashMap<String,String>();
	public static Map<String,String> unMaskMap= new HashMap<String,String>();
	
	static{
		
		maskMap.put("0", "!@");
		maskMap.put("1", "@#");
		maskMap.put("2", "#$");
		maskMap.put("3", "$%");
		maskMap.put("4", "%^");
		maskMap.put("5", "^&");
		maskMap.put("6", "&*");
		maskMap.put("7", "*(");
		maskMap.put("8", "()");
		maskMap.put("9", ")!");
		
		unMaskMap.put("!@","0");
		unMaskMap.put("@#","1");
		unMaskMap.put("#$","2");
		unMaskMap.put("$%","3");
		unMaskMap.put("%^","4");
		unMaskMap.put("^&","5");
		unMaskMap.put("&*","6");
		unMaskMap.put("*(","7");
		unMaskMap.put("()","8");
		unMaskMap.put(")!","9");
		
	}
	
	
	public String getMaskedString(String originalString)
	{
		StringBuilder maskedString=new StringBuilder();
		Character charac = null;
		for(int i =0;i<originalString.length();i++)
		{
			charac = originalString.charAt(i);
			maskedString.append(maskMap.get(charac.toString()));
			
		}
		return maskedString.toString();
	}
	
	public String getOriginalString(String maskedString)
	{
		StringBuilder originalString=new StringBuilder();
		Character charOne = null;
		Character charTwo = null;
		for(int i =0;i<maskedString.length();i++)
		{
			charOne = maskedString.charAt(i);
			i++;
			charTwo = maskedString.charAt(i);
			String key = charOne.toString() + charTwo.toString();
			originalString.append(unMaskMap.get(key));
			
		}
		return originalString.toString();
	}
	
 
}
