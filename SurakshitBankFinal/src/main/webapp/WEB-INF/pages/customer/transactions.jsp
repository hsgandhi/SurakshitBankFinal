<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
<h1>
${successMessage}
</h1>
<form:form name="transactionDetails" >

<table border="1">
<tr>
<td><b>Transaction Type</b></td>
<td><b>Transaction Current Status</b></td>
<td><b>Transaction Amount</b></td>
<td><b>Sender</b></td>
<td><b>Receiver</b></td>
<td><b>Date</b></td>
</tr>
		
	<c:forEach var="transaction" items="${transaction}" >

	<tr>
			<td><c:out value="${transaction.transactionType}"></c:out></td>
			<td><c:out value="${transaction.transactionCurrentStatus}"></c:out></td>
			<td><c:out value="${transaction.transactionAmount}"></c:out></td>
			<td><c:out value="${transaction.primaryParty}"></c:out></td>
			<td><c:out value="${transaction.secondaryParty}"></c:out></td>
			<td><c:out value="${transaction.transactionCreatedAt}"></c:out></td>
	
	</tr>

	</c:forEach>
	
</table>

</form:form>

<tags:commonJs />