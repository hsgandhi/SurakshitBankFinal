<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
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
			Reason: ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}</font>
		</div>
	</c:if>
	<security:authorize access="hasRole('USER')">
		This text is only visible to a user
		<br/>
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
		This text is only visible to an admin
		<br/>
	</security:authorize>
	<form action="j_spring_security_check" method="POST">
        <label for="username">User Name:</label>
        <input id="username" name="j_username" type="text" value=""/><br/><br/>
        <label for="password">Password:</label>
        <input id="password" name="j_password" type="password"/><br/><br/>
        <input type="submit" value="Log In" />
      </form>
      
</body>

</html>
<%-- 
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/trial.js"></script>
<script type="text/javascript">
  	trial();
</script>      
 --%>