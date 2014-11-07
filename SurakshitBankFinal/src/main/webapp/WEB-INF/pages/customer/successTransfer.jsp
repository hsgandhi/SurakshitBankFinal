<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
 
 <sec:authorize access="isAnonymous()">
	<a href="index.jsp" >Go to home page</a>
</sec:authorize>
<br/>

${successMessage}
<tags:commonJs></tags:commonJs>

