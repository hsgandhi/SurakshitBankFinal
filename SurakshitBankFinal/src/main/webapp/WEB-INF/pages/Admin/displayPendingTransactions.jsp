<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
	
        <table >
         <tr>
        		<th style="visibility: false"></th>
        		<th>Type</th>
        		<th>Transaction Amount</th>
        		<th>From user Email</th>
        		<th>To user Email</th>
        		<th>Transaction created time</th>
        		<th>Select operation</th>
        	</tr>
        	<c:forEach items="${PendingTransactionsList}" var="pendingTransaction">
        	<form:form action="adminapproveRejectTransaction" method="POST">
				<tr>
					<%-- <td><input type="hidden" name="ID" value="${pendingTransaction.transactionId}"></td> --%>
					<td  ><input type="hidden" name="CrID" value="${pendingTransaction.encryptedTransactionId}"></td>
					<td align="center"><c:out value="${pendingTransaction.transactionType}"></c:out> </td>
					<td align="center"><c:out value="${pendingTransaction.transactionAmount}"></c:out></td>
					<td align="center"><c:out value="${pendingTransaction.primaryUserEmail}"></c:out></td>
					<td align="center"><c:out value="${pendingTransaction.secondaryUserEmail}"></c:out></td>
					<td align="center"><c:out value="${pendingTransaction.transactionCreatedAt}"></c:out></td>
					<td><input type = "radio" name = "approveReject" id = "approveRejectSelection" value = "Approve" >Approve</td>
					<td><input type = "radio" name = "approveReject" id = "approveRejectSelection" value = "Reject" >Reject</td>
					<td><input type = "submit">
				</tr>
			</form:form>
			</c:forEach>
        </table>
      
<tags:commonJs />