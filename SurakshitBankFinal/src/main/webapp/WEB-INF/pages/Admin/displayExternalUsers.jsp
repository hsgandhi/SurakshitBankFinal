<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
	
List of external users: <br/>

        <table >
        	<tr>
        		<th>User ID</th>
        		<th>Name </th>
        		<th>Address</th>
        		<th>Email ID</th>
        		<th>Phone Number</th>
        	</tr>
        	<c:forEach items="${employeeList}" var="employee">
        	<form:form action="adminDeleteExternalUser" method="POST">
				<tr>	
					<td><input type="hidden" name="emailID" value="${employee.emailId}"></td>
					<td><input type="hidden" name="phoneNum" value="${employee.phoneNumber}"></td>					
				    <td><c:out value="${employee.userId}"></c:out></td>		
					<td><c:out value="${employee.name}"></c:out> </td>
					<td><c:out value="${employee.address}"></c:out></td>
					<td><c:out value="${employee.emailId}"></c:out></td>
					<td><c:out value="${employee.phoneNumber}"></c:out></td>	
					<td><c:out value="${employee.role}"></c:out></td>	
					<td><input type = "submit" value="Delete">
				</tr>
			</form:form>
			</c:forEach>
        </table>
      
<tags:commonJs />