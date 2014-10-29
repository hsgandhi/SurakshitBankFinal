<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>

<html>
<head>
<title>Insert title here</title>

<style>
.error {
    color: #ff0000;
    font-style: italic;
    font-weight: bold;
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
	
	
	<form action="j_spring_security_check" method="POST" name="loginFormName" id="loginFormName">
        <label for="username">User Name:</label>
        <input id="username" name="j_username" type="text" value=""/><br/><br/>
        <label for="password">Password:</label>
        <input id="password" name="j_password" type="password"/><br/><br/>
		<tags:captcha privateKey="6LeFvfwSAAAAANnvC0Gxyq-WAIy6Sw7Sods8DACC" publicKey="6LeFvfwSAAAAAL5pSPXSuMGysfYwS8Mlqz1PLgUR"></tags:captcha>
		<br/>
        <input type="button" value="Log in" onclick="testCaptcha()">
      </form>
      
</body>

</html>
<script src="${pageContext.request.contextPath}/resources/js/common/jquery-2.1.1.min.js" type="text/javascript"></script>
<script type="text/javascript">
  	window.opener.close();
  	function testCaptcha()
  	{
  	$.ajax({
  	    url: "${pageContext.request.contextPath}/testCaptcha",
  	    type: "POST",
  	    data: $("#loginFormName").serialize(),
  	    beforeSend: function (xhr) {
  	        xhr.setRequestHeader("X-Ajax-call", "true");
  	    },
  	    success: function(result) {
  	        if (result == "ok") {
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
</script>      
 