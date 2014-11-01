<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
 <sec:authorize access="isAuthenticated()" var="authStatus">
</sec:authorize>

<c:if test="${not authStatus}">
 <a href="index.jsp" >Go to home page</a>
</c:if>

<br/>
<br/>

${errMsg}
<tags:commonJs></tags:commonJs>	

