<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
	
	
        
        	<form action="employeeModifyTransaction" method="POST" >
				
					<input type="hidden" name="ID" value="${transaction.transactionId}">
					<c:out value="">${transaction.transactionType}</c:out>
					<label>Please enter amount. Valid format is only numbers with/without decimal and following digits</label><br />
					<input name="transactionAmount"></input>
					<c:out value="">${transaction.primaryUserEmail}</c:out>
					<c:out value="">${transaction.secondaryUserEmail}</c:out>
					<c:out value="">${transaction.transactionCreatedAt}</c:out>
				
				<button> Submit </button>
			</form>
		
      
<tags:commonJs />