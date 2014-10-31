<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>

<form:form name="addCreditForm" action="customerAddBalance" commandName="accountViewBean">

	<table>
		<tr>
			<td> Account number: </td>
			<td> ${accountViewBean.accountId}</td>
		</tr>
		<tr>
			<td>Please enter the amount</td>
			<td><form:input path="currency" id="currencyId"/>
				<form:errors path="currency"></form:errors></td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="submit" value="Update Balance">
			</td>
		</tr>
	</table>

</form:form>



<tags:commonJs></tags:commonJs>