<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
 
 <sec:authorize access="isAnonymous()">
	<a href="index.jsp" >Go to home page</a>
</sec:authorize>
<br/>
<center>
<img src="${pageContext.request.contextPath}/resources/images/success.jpg" height="100" width="100"/>
<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${successMessage}
</center>

<tags:commonJs></tags:commonJs>	

