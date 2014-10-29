<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>

<security:authorize access="hasAnyRole('ADMIN', 'CUSTOMER','EMPLOYEE','MERCHANT')">
<form name="menuForm">
<table>
<tr>
	<c:forEach items="${menuList}" var="menu">
		<a href="#" onclick="parent.window.frames['bodyContent'].location = '${menu.actionValue}'">${menu.viewText}</a>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	</c:forEach>
</tr>
</table>
</form>
</security:authorize>

<tags:commonJs />
