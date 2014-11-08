<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>	
	
        
        	<form:form action="adminAddInternalUser" commandName="internalUser">
        		<form:label path="">Please enter internal user's name</form:label>
				<form:input path="name" id="name"/> 
				<form:errors path="name"></form:errors><br />
				
				<form:label path="">Please enter internal user's address</form:label>
				<form:input path="address" id="address"/> 
				<form:errors path="address"></form:errors><br />
				
				<form:label path="">Please enter internal user's emailId</form:label>
				<form:input path="emailId" id="emailId"/> 
				<form:errors path="emailId"></form:errors><br />
				
				<form:label path="">Please enter internal user's phoneNumber</form:label>
				<form:input path="phoneNumber" id="phoneNumber"/> 
				<form:errors path="phoneNumber"></form:errors><br />
				
				<form:label path="">Please enter internal user's documentId</form:label>
				<form:input path="documentId" id="documentId"/> 
				<form:errors path="documentId"></form:errors><br />
				
				<form:label path="">Please enter internal user's password</form:label>
				<form:password path="password" id="password"/> 
				<form:errors path="password"></form:errors><br />
								
				<form:button> Submit </form:button>
			</form:form>
        
      
<tags:commonJs />