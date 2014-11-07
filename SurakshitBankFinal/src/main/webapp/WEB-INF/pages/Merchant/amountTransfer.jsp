<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tags:commonJs />
<link href="http://code.jquery.com/ui/1.10.3/themes/ui-darkness/jquery-ui.css" rel="stylesheet">
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.min.js"></script>
<link href="${pageContext.request.contextPath}/resources/css/keyboard.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/resources/js/jquery.keyboard.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery.keyboard.extension-all.js"></script>
<script>
		$(function(){
			$('#oneTimePwd').keyboard();
		});
</script>

<form:form name="accountDetails" action="merchantTransferAmountDetails" commandName="accountTransferBean">

	<table>
	
		<tr>
			<td> Your One-Time Password has been sent to your Registered Email ID. </td>
		</tr>
		<tr>
			<td> Please enter the one-time password we just sent you via email. </td>
			<td><div id="wrap"> <!-- wrapper only needed to center the input -->

		<!-- keyboard input -->
		<input id="oneTimePwd" name="oneTimePwd" type="text">

	</div> <!-- End wrapper --></td>
		</tr>
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
