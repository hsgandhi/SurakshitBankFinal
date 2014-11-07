<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form:form name="requestCustPayForm" action="merchantRequestCustPayment" commandName="payRequestViewBean">

	<table>
		<tr>
			<td>Customer Email ID:</td>
			<td><form:input path="custEmailID" id="custEmailId"/>
				<form:errors path="custEmailID"></form:errors></td>
		</tr>
		<tr>
			<td>Amount</td>
			<td><form:input path="paymentAmount" id="payAmt"/>
				<form:errors path="paymentAmount"></form:errors></td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="submit" value="Request Payment">
			</td>
		</tr>
	</table>

</form:form>



<tags:commonJs></tags:commonJs>