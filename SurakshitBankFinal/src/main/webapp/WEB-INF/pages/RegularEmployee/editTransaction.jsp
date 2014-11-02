<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
	
	<form:form action="modifyTransactionCommitEditedTransaction" method="POST" commandName="transaction">
	
		<form:label path="">Transaction ID</form:label>
		<form:label path="" >${transaction.getTransactionId()}</form:label> <br />
      
        <form:label path="">Transaction Type</form:label>
		<form:input path="transactionType" id="transactionType" value="${transaction.getTransactionType()}"/> <br />
		
		<form:label path="">Transaction Amount</form:label>
		<form:input path="transactionAmount" id="transactionAmount" value="${transaction.getTransactionAmount()}"/> <br />
		
		<form:label path="">Primary Party Email ID</form:label>
		<form:label path="" >${transaction.getPrimaryUserEmailID()}</form:label> <br />
		
		<form:label path="">Secondary Party Email ID</form:label>
		<form:input path="secondaryUserEmailID" id="secondaryUserEmailID" value="${transaction.getSecondaryUserEmailID()}"/> <br />
		
		<form:button> Edit </form:button>
    </form:form>
      
<tags:commonJs />