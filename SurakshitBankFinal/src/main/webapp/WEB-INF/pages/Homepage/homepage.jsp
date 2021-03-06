<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
<html>
<head>
<tags:commonJs />
</head>
<body id="bodyOfApp">
<div>
<span style="float:left;">
<img src="${pageContext.request.contextPath}/resources/images/bank.jpg" height="100" width="200"/>
</span>
<span style="float: right;">
	Welcome <security:authentication property="principal.username" /> 
	<br/>
	<a href="<c:url value="j_spring_security_logout" />" align="right"> Logout</a>
</span>	
</div>

<form name="remotingForm" id="IdRemotingForm" target="menuFrame" method="post" action="getMenuDetails">
</form>
<form name="remotingForm1" target="bodyContent" method="post" action="dummyFragment">
</form>

<security:authorize access="hasAnyRole('ADMIN', 'CUSTOMER','EMPLOYEE','MERCHANT')">
	<iframe name="menuFrame" width="100%" height="20%" ></iframe>
	<iframe name="bodyContent" src="dummyFragment" width="100%" height="80%" ></iframe>
</security:authorize>
</body>
</html>

<script type="text/javascript">
document.remotingForm.submit();
document.remotingForm1.submit();
</script>





