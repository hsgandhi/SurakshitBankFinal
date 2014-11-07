<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form:form name="accountDetails" action="customerTransferAmountDetails" commandName="accountTransferBean">

	<table>
		<tr>
			<td> Account Number: </td>
			<td> <c:out value="${accountTransferBean.accountIdSender}"></c:out></td>
		</tr>
		
		<tr>
			<td> Current Account Balance: </td>
			<td> <c:out value="${accountTransferBean.balanceSender}"></c:out></td>
		</tr>
		
		<tr>
			<td>Please enter Amount to be transfered: </td>
			<td><form:input path="amount" id="amountId"/>
				<form:errors path="amount"></form:errors></td>
		</tr>
		
		<tr>
			<td>Please email id of the receiver</td>
			<td><form:input path="emailIdReceiver" id="emailId"/>
				<form:errors path="emailIdReceiver"></form:errors></td>
		</tr>
		
		
		<tr>
			<td colspan="2">
				<input type="submit" value="Transfer Amount">
			</td>
		</tr>
	</table>

</form:form>



<tags:commonJs></tags:commonJs>