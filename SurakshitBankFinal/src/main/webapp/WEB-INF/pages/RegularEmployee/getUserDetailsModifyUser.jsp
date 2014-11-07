<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>	
	
        
        	<form:form action="employeeModifyUser" commandName="userDetail" method="POST">
        	
        		<input type="hidden" name="oldEmailID" value="${oldEmailID}">
        		
        		<form:label path="">Please enter user's name</form:label>
				<form:input path="name" id="name"/> 
				<form:errors path="name"></form:errors><br />
				
				<form:label path="">Please enter user's address</form:label>
				<form:input path="address" id="address"/> 
				<form:errors path="address"></form:errors><br />
				
				<form:label path="">Please enter user's phoneNumber</form:label>
				<%-- <fmt:formatNumber type="number" groupingUsed="false" value="${EmployeeProfile.phoneNumber}" /> --%>
				<form:input path="phoneNumber" id="phoneNumber"/> 
				<form:errors path="phoneNumber"></form:errors><br />
				
				<form:label path="">Please enter user's documentId</form:label>
				<form:input path="documentId" id="documentId"/> 
				<form:errors path="documentId"></form:errors><br />
				
								
				<form:button> Modify </form:button>
			</form:form>
        
      
<tags:commonJs />