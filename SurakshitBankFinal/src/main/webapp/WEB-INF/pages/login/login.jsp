<%@ page contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>

<html>
<head>
<tags:commonJs />
<link href="http://code.jquery.com/ui/1.10.3/themes/ui-darkness/jquery-ui.css" rel="stylesheet">
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.min.js"></script>
<link href="${pageContext.request.contextPath}/resources/css/keyboard.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/resources/js/jquery.keyboard.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery.keyboard.extension-all.js"></script>
	
	
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
<center>
<c:if test="${not empty errorDisplay}">
		<div class="error">
		<font color="red">
			Please try again.<br /> 
			Reason: ${errorDisplay}</font>
		</div>
	</c:if>
	
	
<img src="${pageContext.request.contextPath}/resources/images/bank.jpg" height="100" width="200"/>
<br/>
<br/>
	
	<form method="POST" name="loginFormName" id="loginFormName">
        <label for="username">User Name:</label>
        <input id="username" name="j_username" type="text" value=""/><br/><br/>
        <div class="block">
        <label for="password">Password:</label>
        <input id="password" name="j_password" type="password" class="qwerty"/>&nbsp;&nbsp;&nbsp;
        <img id="passwd" class="tooltip" title="Click to open the virtual keyboard" src="${pageContext.request.contextPath}/resources/images/keyboard.png">
        <div class="code ui-corner-all"></div>
        </div>
        <br/>
        <br/>
		<tags:captcha privateKey="6Le FvfwSAAAAANnvC0Gxyq-WAIy6Sw7Sods8DACC" publicKey="6LeFvfwSAAAAAL5pSPXSuMGysfYwS8Mlqz1PLgUR"></tags:captcha>
		<br/>
		
        <input type="button" value="Log in" onclick="login()">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" value="Forget Password" onclick="goToForgetPassword();" class="linkButton">
      </form>
      
      			<h2>Important instruction:</h2>
      			<br/>
      			1) Do not use back button or refresh button in the application.
      			<br/>
      			2) The account will be locked if the 5 continuous login attempts fails.
      			<br/>
      			3) If your account gets locked, contact the system admin by emailing surakshitbank@gmail.com
      			
 </center>
</body>

</html>
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
  	$('.qwerty') 
  	 .keyboard({  
  	  openOn : null, 
  	  stayOpen : true, 
  	  layout : 'qwerty' 
  	 }).addTyping(); 
  	$('#passwd').click(function(){ 
  	 $('.qwerty').getkeyboard().reveal(); 
  	});
  	window.opener.close();
</script>      
 