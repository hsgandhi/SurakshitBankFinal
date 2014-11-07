<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
	
	<form action="employee_home" method="POST">
        <table border="">
        	<tr>
        		<td> NAME: </td>
        		<td> <c:out value="${EmployeeProfile.name}"></c:out> </td>        		
        	</tr>
        	
        	<tr>
        		<td> ADDRESS: </td>
        		<td> <c:out value="${EmployeeProfile.address}"></c:out> </td>
        	</tr>
        	
        	<tr>
        		<td> EMAIL ID: </td>
        		<td> <c:out value="${EmployeeProfile.emailID}"></c:out> </td>
        	</tr>
        	
        	<tr>
        		<td> PHONE NUMBER: </td>
        		<td><%--  <c:out value="${EmployeeProfile.phoneNumber}"></c:out> --%> <fmt:formatNumber type="number" groupingUsed="false" value="${EmployeeProfile.phoneNumber}" /> </td>
        	</tr>
        	
        	<tr>
        		<td> USER ID: </td>
        		<td> <c:out value="${EmployeeProfile.userID}"></c:out> </td>
        	</tr>
        	
        	<tr>
        		<td> DOCUMENT ID: </td>
        		<td> <c:out value="${EmployeeProfile.documentID}"></c:out> </td>
        	</tr>
        </table>
        
    </form>
      
<tags:commonJs />