<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>	
	
        
        	<form:form action="employeeCreateNewTransaction" commandName="newTransaction">
        		<form:label path="">Transaction Type[DEBIT, CREDIT or PAYMENT]</form:label>
				<form:input path="transactionType" id="transactionType"/> 
				<form:errors path="transactionType"></form:errors><br />
				
				<form:label path="">Transaction Amount</form:label>
				<form:input path="transactionAmount" id="transactionAmount"/> 
				<form:errors path="transactionAmount"></form:errors><br />
				
				<form:label path="">Primary User EmailID</form:label>
				<form:input path="primaryUserEmail" id="primaryUserEmailID"/> 
				<form:errors path="primaryUserEmail"></form:errors><br />
				
				<form:label path="">Secondary User EmailID</form:label>
				<form:input path="secondaryUserEmail" id="secondaryUserEmailID"/> 
				<form:errors path="secondaryUserEmail"></form:errors><br />
				
				<form:button> Submit </form:button>
			</form:form>
        
      
<tags:commonJs />