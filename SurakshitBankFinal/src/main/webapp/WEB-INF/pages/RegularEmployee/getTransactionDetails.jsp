<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>	
	
        
        	<form:form action="createTransaction" method="POST" commandName="newTransactionBean">
        		<form:label path="">Transaction Type</form:label>
				<form:input path="transactionType" id="transactionType"/> <br />
				
				<form:label path="">Transaction Amount</form:label>
				<form:input path="transactionAmount" id="transactionAmount"/> <br />
				
				<form:label path="">Primary User EmailID</form:label>
				<form:input path="primaryUserEmailID" id="primaryUserEmailID"/> <br />
				
				<form:label path="">Secondary User EmailID</form:label>
				<form:input path="secondaryUserEmailID" id="secondaryUserEmailID"/> <br />
				
				<form:button> Submit </form:button>
			</form:form>
        
      
<tags:commonJs />