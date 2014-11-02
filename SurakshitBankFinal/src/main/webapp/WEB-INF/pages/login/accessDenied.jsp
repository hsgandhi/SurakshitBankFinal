<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <%-- 
 <sec:authorize access="isAnonymous()">
	<a href="index.jsp" >Go to home page</a>
</sec:authorize> --%>

<sec:authorize access="isAuthenticated()" var="authStatus">
</sec:authorize>
<br/>

<c:if test="${not authStatus}">
 <a href="index.jsp" >Go to home page</a>
</c:if>
<br/>
<br/>
<center>
<img src="${pageContext.request.contextPath}/resources/images/accessDenied.jpg" height="100" width="100"/>
<br/>
You are not authorized to view these details.
</center>
 
 
<tags:commonJs></tags:commonJs>