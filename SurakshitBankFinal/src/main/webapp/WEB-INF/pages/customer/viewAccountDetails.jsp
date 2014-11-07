<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form:form name="accountDetails" action="customerGetTransactionDetails" commandName="accountGetTransactionDetailsBean">

	<table>
		<tr>
			<td> Account Number: </td>
			<td> <c:out value="${accountGetTransactionDetailsBean.accountId}"></c:out></td>
		</tr>
		
		<tr>
			<td> Current Account Balance: </td>
			<td><c:out value="${accountGetTransactionDetailsBean.balance}"></c:out></td>
		</tr>
		
		<tr>
			<td colspan="2">
				<input type="submit" value="Get Transaction Details">
			</td>
		</tr>
	</table>

</form:form>



<tags:commonJs></tags:commonJs>