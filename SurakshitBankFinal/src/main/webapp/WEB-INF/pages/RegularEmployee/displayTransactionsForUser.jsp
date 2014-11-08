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
        		<th></th>
        	</tr>
        	
        	<c:forEach items="${transactionList}" var="transaction">
        	<form:form action="employeeModifyDeleteTransaction" method="POST">
				<tr>
					<%-- <td><input type="hidden" name="ID" value="${transaction.transactionId}"></td> --%>
					<td><input type="hidden" name="CrID" value="${pendingTransaction.encryptedTransactionId}"></td>
					<td align="center"><c:out value="${transaction.transactionType}"></c:out> </td>
					<td align="center"><c:out value="${transaction.transactionAmount}"></c:out></td>
					<td align="center"><c:out value="${transaction.primaryUserEmail}"></c:out></td>
					<td align="center"><c:out value="${transaction.secondaryUserEmail}"></c:out></td>
					<td align="center"><c:out value="${transaction.transactionCreatedAt}"></c:out></td>
					<td align="center"><c:out value="${transaction.transactionCurrentStatus}"></c:out></td>
					<td><input type = "radio" name = "modifyDelete" id = "modifyDeleteSelection" value = "Modify" >Modify</td>
					<td><input type = "radio" name = "modifyDelete" id = "modifyDeleteSelection" value = "Delete" >Delete</td>
					<td><input type = "submit">
				</tr>
			</form:form>
			</c:forEach>
        </table>
      
<tags:commonJs />