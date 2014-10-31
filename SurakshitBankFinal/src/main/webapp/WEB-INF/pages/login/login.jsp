<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>

<html>
<head>
<title>Insert title here</title>

<style>
	.linkButton {
    background:none!important;
     border:none; 
     padding:0!important;
    /*border is optional*/
     border-bottom:1px solid #444; 
     cursor: pointer;
}
</style>

</head>
<body>
<c:if test="${not empty errorDisplay}">
		<div class="error">
		<font color="red">
			Please try again.<br /> 
			Reason: ${errorDisplay}</font>
		</div>
	</c:if>
	
	
	<form method="POST" name="loginFormName" id="loginFormName">
        <label for="username">User Name:</label>
        <input id="username" name="j_username" type="text" value=""/><br/><br/>
        <label for="password">Password:</label>
        <input id="password" name="j_password" type="password"/><br/><br/>
		<tags:captcha privateKey="6Le FvfwSAAAAANnvC0Gxyq-WAIy6Sw7Sods8DACC" publicKey="6LeFvfwSAAAAAL5pSPXSuMGysfYwS8Mlqz1PLgUR"></tags:captcha>
		<br/>
		
        <input type="button" value="Log in" onclick="login()">
        <br/>
        <br/>
        <input type="button" value="Forget Password" onclick="goToForgetPassword();" class="linkButton">
      </form>
      
</body>

</html>
<tags:commonJs />
<script type="text/javascript">
	function goToForgetPassword()
	{
		if(isNullOrEmptyString(document.loginFormName.j_username.value))
			{
				alert("Please enter the username");
				return;
			}
		testCaptchaAndSubmit('forgotPassword');
	}
  	function login()
  	{
  		if(isNullOrEmptyString(document.loginFormName.j_username.value) || isNullOrEmptyString(document.loginFormName.j_password.value))
  			{
  				alert("Please enter the username and/or password");
  				return;
  			}
  		testCaptchaAndSubmit('j_spring_security_check');
  	}
  	
  	function testCaptchaAndSubmit(action)
  	{
  		$.ajax({
  	  	    url: "${pageContext.request.contextPath}/testCaptcha",
  	  	    type: "POST",
  	  	    data: $("#loginFormName").serialize(),
  	  	    success: function(result) {
  	  	        if (result == "ok") {
  	  	        	document.loginFormName.action=action;
  	  	            document.loginFormName.submit();
  	  	        }
  	  	      else if (result == "CaptchaException") {
  	  	    	Recaptcha.reload();
  	  	    	  alert('Captcha value did not match.');
  		        }
  	  	        else if (result == "error") {
  	  	            alert('error');
  	  	        }
  	  	    }
  	  	});
  	}
  	window.opener.close();
</script>      
 