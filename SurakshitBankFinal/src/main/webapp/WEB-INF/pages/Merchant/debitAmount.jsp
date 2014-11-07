<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form:form name="addDebitForm" action="merchantWithdrawAmount" commandName="accountDebitViewBean">

	<table>
		<tr>
			<td> Account number: </td>
			<td><c:out value="${accountDebitViewBean.accountId}"></c:out></td>
		</tr>
		
		<tr>
			<td> Account Balance: </td>
			<td><c:out value="${accountDebitViewBean.balance}"></c:out></td>
		</tr>
		
		<tr>
			<td>Please enter the amount to be withdrawn</td>
			<td><form:input path="amount" id="amountId"/>
				<form:errors path="amount"></form:errors></td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="submit" value="Withdraw Amount">
			</td>
		</tr>
	</table>

</form:form>



<tags:commonJs></tags:commonJs>