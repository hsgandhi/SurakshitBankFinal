<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
<html>
<head>
<tags:commonJs />
</head>
<body id="bodyOfApp">
<div align="right">
	Welcome <security:authentication property="principal.username" /> 
	<br/>
	<a href="<c:url value="j_spring_security_logout" />" align="right"> Logout</a>
</div>
<security:authorize access="hasAnyRole('ADMIN', 'CUSTOMER','EMPLOYEE','MERCHANT')">
	<iframe name="menuFrame" src="getMenuDetails" width="100%" height="20%" ></iframe>
	<iframe name="bodyContent" src="dummyFragment" width="100%" height="80%" ></iframe>
</security:authorize>
</body>
</html>





