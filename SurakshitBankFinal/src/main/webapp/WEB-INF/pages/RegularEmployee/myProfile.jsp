<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
	
	<form action="employee_home" method="POST">
        <table border="">
        	<tr>
        		<td> NAME: </td>
        		<td> ${EmployeeProfile.name} </td>        		
        	</tr>
        	
        	<tr>
        		<td> ADDRESS: </td>
        		<td> ${EmployeeProfile.address} </td>
        	</tr>
        	
        	<tr>
        		<td> EMAIL ID: </td>
        		<td> ${EmployeeProfile.emailID} </td>
        	</tr>
        	
        	<tr>
        		<td> PHONE NUMBER: </td>
        		<td> ${EmployeeProfile.phoneNumber} </td>
        	</tr>
        	
        	<tr>
        		<td> USER ID: </td>
        		<td> ${EmployeeProfile.userID} </td>
        	</tr>
        	
        	<tr>
        		<td> DOCUMENT ID: </td>
        		<td> ${EmployeeProfile.documentID} </td>
        	</tr>
        </table>
        
    </form>
      
<tags:commonJs />