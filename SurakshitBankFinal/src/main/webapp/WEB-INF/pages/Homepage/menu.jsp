<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>

<style>
	.linkButton {
    background:none!important;
     border:none; 
     padding:0!important;
    /*border is optional*/
     border-bottom:1px solid #444; 
     cursor: pointer;
}
</style>

<security:authorize access="hasAnyRole('ADMIN', 'CUSTOMER','EMPLOYEE','MERCHANT')">
<table>
<tr>
	<c:forEach items="${menuList}" var="menu" varStatus="count">
	<form action='${menu.actionValue}' method="post" target="bodyContent">
		<input type="submit" value="${menu.viewText}" class="linkButton">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	</form>
	</c:forEach>
</tr>
</table>
</security:authorize>

<tags:commonJs />