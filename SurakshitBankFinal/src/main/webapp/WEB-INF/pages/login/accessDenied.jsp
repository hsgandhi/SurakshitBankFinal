<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
 
 <sec:authorize access="isAnonymous()">
	<a href="index.jsp" >Go to home page</a>
</sec:authorize>
<br/>

 You are not authorized to view these details.
 
<tags:commonJs></tags:commonJs>