<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>

	<security:authorize access="hasRole('USER')">
		This text is only visible to a user
		<br/>
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
		This text is only visible to an admin
		<br/>
	</security:authorize>
	
	 <form:form method="Post" commandName="trialBean" action="adminTrialSubmit">
      	<table>
      		<tr>
      			<td>Name</td>
      			<td> 
      			<form:input path="name" id="nameInput"></form:input>
    			<form:errors path="name" cssclass="error"></form:errors></td>
      		</tr>
      		<tr>
      			<td>Email</td>
      			<td> <form:input path="mail" id="emailInput"></form:input>
    				<form:errors path="mail" cssclass="error"></form:errors></td>
      		</tr>
      		<tr>
      			<td>DOB</td>
      			<td> <form:input path="dob" id="dobInput" placeholder="MM/dd/yyyy"></form:input>
    				<form:errors path="dob" cssclass="error"></form:errors></td>
      		</tr>
      		<tr>
      			<td>Tp Int</td>
      			<td> <form:input path="tp" id="tpInput"></form:input>
    				<form:errors path="tp" cssclass="error"></form:errors></td>
      		</tr>
      		<tr>
      			<td>Currency</td>
      			<td> <form:input path="currency" id="currencyInput"></form:input>
    				<form:errors path="currency" cssclass="error"></form:errors></td>
      		</tr>
      		<tr>
      			<td><input type="submit" value="Log In" /></td>
      		</tr>
      	</table>
      </form:form>
      
<tags:commonJs></tags:commonJs>