package asu.bank.utility;

import java.util.HashMap;
import java.util.Map;

public class SurakshitException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static Map<String,String> errorMap= new HashMap<String,String>();
	
	static{
		if(errorMap.isEmpty())
		{
			errorMap.put("LowBalance", "The balance is not sufficient");
			errorMap.put("ok", "ok");
			errorMap.put("CaptchaException", "CaptchaException");
			errorMap.put("InvalidUserNameOrPassword", "Invalid user name or password. Please try again");
			errorMap.put("UserNotFound", "No such user exists.");
			errorMap.put("EnterPassword", "Please enter proper password");
			errorMap.put("EnterOTP", "Please enter proper OTP");
			errorMap.put("NoAccountAvailable", "There is no account for this user.");
			errorMap.put("AccountLocked", "User account is locked. Contact system admin.");
		}
	}
	
	
	private String errorCode;


	public SurakshitException(String errorCode)
	{
		this.errorCode=errorCode;
	}
	public String getErrorCode() {
		return errorMap.get(errorCode);
	}


	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	

}
