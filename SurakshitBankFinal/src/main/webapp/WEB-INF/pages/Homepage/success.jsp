<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<sec:authorize access="isAuthenticated()" var="authStatus">
</sec:authorize>

<sec:authorize access="isAnonymous()" var="anonymousStatus">
</sec:authorize>

<c:if test="${not authStatus || not anonymousStatus}">
 <a href="index.jsp" >Go to home page</a>
</c:if>
<br/>

<center>
<img src="${pageContext.request.contextPath}/resources/images/success.jpg" height="100" width="100"/>
<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${successMessage}
</center>

<tags:commonJs></tags:commonJs>	

