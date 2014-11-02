<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
	
	
        <table >
        	<c:forEach items="${PendingTransactionsList}" var="pendingTransaction">
        	<form:form action="approveRejectTransaction" method="POST">
				<tr>
					<td><input type="hidden" name="ID" value="${pendingTransaction.getTransactionId()}"></td>
					<td>${pendingTransaction.getTransactionType()}</td>
					<td>${pendingTransaction.getTransactionAmount()}</td>
					<td>${pendingTransaction.getUserByPrimaryParty().getUserId()}</td>
					<td>${pendingTransaction.getUserBySecondaryParty().getUserId()}</td>
					<td>${pendingTransaction.getTransactionCreatedAt()}</td>
					<td><input type = "radio" name = "approveReject" id = "approveRejectSelection" value = "Approve" >Approve</td>
					<td><input type = "radio" name = "approveReject" id = "approveRejectSelection" value = "Reject" >Reject</td>
					<td><input type = "submit">
				</tr>
			</form:form>
			</c:forEach>
        </table>
      
<tags:commonJs />