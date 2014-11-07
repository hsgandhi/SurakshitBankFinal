<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>

<form action="checkOTP" name="otpForm" method="POST">
	<table>
		<tr>
			<td colspan="2">The one time password has been sent to your emailId</td>
		</tr>
		<tr>
			<td>Enter new password:</td>
			<td><input type="password" name="newPassword" maxlength="20"/></td>
		</tr>
		<tr>
			<td>Enter the one time Password:</td>
			<td><input type="text" name="otp" maxlength="8"/></td>
		</tr>
		<tr >
			<td colspan="2"><input type="button" value="Submit" onclick="checkOTP();"/></td>
		</tr>
	</table>
</form>

<tags:commonJs></tags:commonJs>

<script>
function checkOTP()
{
	if(isNullOrEmptyString(document.otpForm.newPassword.value) || isNullOrEmptyString(document.otpForm.otp.value))
		{
			alert("Please enter both password and OTP");
			return;
		}
	if(document.otpForm.newPassword.value.length<8)
		{
			alert("Password must be atleast 8 characters long");
			return;
		}
	if(document.otpForm.otp.value.length<8)
	{
		alert("OTP must be atleast 8 characters long");
		return;
	}
	document.otpForm.submit();
}
</script>
