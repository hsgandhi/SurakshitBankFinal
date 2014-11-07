<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
<h1>
${successMessage}
</h1>

<form:form name="paymentDetails" action="customerMerchantPayment" commandName="customerPaymentBean">

	<table>
		<tr>
			<td> Account Number: </td>
			<td> <c:out value="${customerPaymentBean.accountId}"></c:out></td>
		</tr>
		
		<tr>
			<td> Current Account Balance: </td>
			<td><c:out value="${customerPaymentBean.balance}"></c:out></td>
		</tr>
		
		
	</table>

<table border="1">
<tr>
<td></td>
<td>Transaction Type</td>
<td>Transaction Current Status</td>
<td>Transaction Amount</td>
<td>Sender</td>
<td>Merchant</td>
</tr>
		
	<c:forEach var="transaction" items="${transaction}" >

	<tr>
			<td><input type="hidden" name="ID" value="${transaction.getTransactionId()}"></td>
			<td><c:out value="${transaction.transactionType}"></c:out></td>
			<td><c:out value="${transaction.transactionCurrentStatus}"></c:out></td>
			<td><c:out value="${transaction.transactionAmount}"></c:out></td>
			<td><c:out value="${transaction.primaryParty}"></c:out></td>
			<td><c:out value="${transaction.secondaryParty}"></c:out></td>
	</tr>

	</c:forEach>


		
</table>

</form:form>

<tags:commonJs />