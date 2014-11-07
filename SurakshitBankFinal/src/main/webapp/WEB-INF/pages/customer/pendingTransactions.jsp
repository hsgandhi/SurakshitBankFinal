<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
<h1>
${successMessage}
</h1>

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
<td>Receiver</td>
<td>Date</td>
<td>Approve</td>
<td>Reject</td>
<td>Submit</td>
</tr>
		
	<c:forEach var="transaction" items="${transaction}" >
<form:form name="paymentDetails" action="customerMerchantPayment" commandName="customerPaymentBean">
	<tr>
			<td><input type="hidden" name="ID" value="${transaction.transactionId}"></td>
			<td><c:out value="${transaction.transactionType}"></c:out></td>
			<td><c:out value="${transaction.transactionCurrentStatus}"></c:out></td>
			<td><c:out value="${transaction.transactionAmount}"></c:out></td>
			<td><c:out value="${transaction.primaryParty}"></c:out></td>
			<td><c:out value="${transaction.secondaryParty}"></c:out></td>
			<td><c:out value="${transaction.transactionCreatedAt}"></c:out></td>
			<td><input type = "radio" name = "approveReject" id = "approveRejectSelection" value = "Approve" >Approve</td>
			<td><input type = "radio" name = "approveReject" id = "approveRejectSelection" value = "Reject" >Reject</td>
			<td><input type = "submit">
			
	</tr>
</form:form>
	</c:forEach>


		
</table>



<tags:commonJs />