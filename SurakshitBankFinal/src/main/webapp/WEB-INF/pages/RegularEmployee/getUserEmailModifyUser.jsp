<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>	
	
        
        	<form:form action="employeeModifyUserEmailID" commandName="user">
        		<form:label path="">Please enter user's email ID</form:label>
				<form:input path="emailId" id="emailId"/> 
				<form:errors path="emailId"></form:errors><br />
				
				<form:label path="">Please enter user's phone number</form:label>
				<form:input path="phoneNumber" id="phoneNumber"/> 
				<form:errors path="phoneNumber"></form:errors><br />
								
				<form:button> Submit </form:button>
			</form:form>
        
      
<tags:commonJs />