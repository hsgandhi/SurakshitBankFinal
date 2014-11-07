package asu.bank.utility;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;

@Component
public class KeyGenerataionUtility {
	
	public KeyPair generateKeyPair() throws  NoSuchAlgorithmException, IOException{ 
		
		KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
		SecureRandom random = new SecureRandom();
		keyGenerator.initialize(1024,random);
		KeyPair privatePublicPair = keyGenerator.generateKeyPair();
				
		return privatePublicPair;
	}
	
	public PublicKey genPublicKeyFromKeyByte(byte[] publicKeyByteArray) throws Exception{
		
		byte[] base64PublicKeyByteArray = Base64.decode(publicKeyByteArray);
		X509EncodedKeySpec x509Format = new X509EncodedKeySpec(base64PublicKeyByteArray);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(x509Format);
		
		return publicKey;
	}
	
	public String decryptUsingPublicKey(PublicKey publicKey, String hashString) throws Exception{
		
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		byte[] hashStringByteArray = hashString.getBytes("UTF-8");
		byte[] base64HashStringByteArray = Base64.decode(hashStringByteArray);
		byte[] decryptedHashStringCipher = cipher.doFinal(base64HashStringByteArray);
		//String decryptedHashString = new String(decryptedHashStringCipher, Charset.forName("UTF-8"));
		String decryptedHashString = new String(decryptedHashStringCipher);
		
		return decryptedHashString;
		
	}

}
