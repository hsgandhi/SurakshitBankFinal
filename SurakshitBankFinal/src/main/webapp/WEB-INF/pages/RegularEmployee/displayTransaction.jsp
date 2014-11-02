<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
	
	<form:form action="" method="POST">
        <table>		
			<tr>
				<th>Transaction ID</th>
				<th>Transaction Type</th>
				<th>Transaction Current Status</th>
				<th>Transaction Amount</th>
				<th>Primary party Email ID</th>
				<th>Secondary party Email ID</th> 
				<th>Transaction creation timestamp</th>
			</tr>
			<tr>
				<td>${transaction.getTransactionId()}</td>
				<td>${transaction.getTransactionType()}</td>
				<td>${transaction.getTransactionCurrentStatus()}</td>
				<td>${transaction.getTransactionAmount()}</td>
				<td>${transaction.getPrimaryUserEmailID()}</td>
				<td>${transaction.getSecondaryUserEmailID()}</td> 
				<td>${transaction.getTransactionCreatedAt()}</td>
			</tr>
		</table>
        
    </form:form>
      
<tags:commonJs />