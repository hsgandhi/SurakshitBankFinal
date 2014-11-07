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
<td>Transaction Type</td>
<td>Transaction Current Status</td>
<td>Transaction Amount</td>
<td>Sender</td>
<td>Receiver</td>
<td>Date</td>
</tr>
		
	<c:forEach var="transaction" items="${transaction}" >

	<tr>
			<td><c:out value="${transaction.transactionType}"></c:out></td>
			<td><c:out value="${transaction.transactionCurrentStatus}"></c:out></td>
			<td><c:out value="${transaction.transactionAmount}"></c:out></td>
			<td><c:out value="${transaction.primaryParty}"></c:out></td>
			<td><c:out value="${transaction.secondaryParty}"></c:out></td>
			<td><c:out value="${transaction.timeStamp}"></c:out></td>
	
	</tr>

	</c:forEach>
	
</table>

</form:form>

<tags:commonJs />