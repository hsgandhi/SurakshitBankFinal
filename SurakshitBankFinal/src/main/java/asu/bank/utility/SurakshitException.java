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
