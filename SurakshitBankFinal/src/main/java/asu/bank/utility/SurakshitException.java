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
			errorMap.put("GovNotApproved", "Government authorities did not authorize to view this PII");
			
			//added by kartik
			errorMap.put("phoneNotFound", "Either the email ID or the phone number entered is incorrect");
			errorMap.put("insufficientFunds", "You do not have sufficient funds to perform this operation");
			errorMap.put("CantModifyApprovedTransaction", "Sorry, the transaction is already approved and thus cannot be modified");
			errorMap.put("CantDeleteApprovedTransaction", "Sorry, the transaction is already approved and thus cannot be deleted");
			errorMap.put("newTransactionCreationFailed", "Either the primary or the secondary email ID's or both does not exist. Please try again with valid email IDs");
			errorMap.put("invalidAmount", "Amount entered is invalid. Please go back and try again");
			errorMap.put("userExists", "This email has already been registered");
			errorMap.put("userNotExists", "The user does not exist");
			errorMap.put("contactAdmin", "Something went wrong. Please test the administrator.");
			errorMap.put("DuplicateCredentials", "Either document Id, email Id or phone number is already used for account creation.");
			
			//by jaydeep
			errorMap.put("OTPFailed", "The password you entered didn't match the one we sent you.");
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
