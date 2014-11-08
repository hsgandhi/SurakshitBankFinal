<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>

<form name="requestPIIForm" action="adminGetPII" method="post">

<table>
<tr>
	<td>Enter email Id</td>
	<td><input type="text" name="emailId" value='<c:out value="${userBean.emailId}"></c:out>'></td>
	<td><input type="submit"></td>
</tr>
</table>

<c:if test="${not empty userBean}">

Email id :   <c:out value="${userBean.emailId}"></c:out><br/>
Name :   <c:out value="${userBean.name}"></c:out><br/>
Address:  <c:out value="${userBean.address}"></c:out><br/>
Phone Number:    <c:out value="${userBean.phoneNumber}"></c:out><br/>
Document Id:   <c:out value="${userBean.documentId}"></c:out><br/>

</c:if>



</form>


<tags:commonJs />