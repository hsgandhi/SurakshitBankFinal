<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form:form name="merchantUpdateInfo" action="customerUpdatePersonalInfo" commandName="infoUpdateViewBean">

	<table>
	
		
		<tr>
			<td>Update your Information.</td>
		</tr>
		<tr>
			<td>Name:</td>
			<td><form:input path="name" id="name"/>
				<form:errors path="name"></form:errors></td>
		</tr>
		<tr>
			<td>Address:</td>
			<td><form:input path="address" id="address"/>
				<form:errors path="address"></form:errors></td>
		</tr>
		<tr>
			<td>Phone Number:</td>
			<td><form:input path="phoneNumber" id="phoneNumber"/>
				<form:errors path="phoneNumber"></form:errors></td>
		</tr>
		
		<tr>
			<td colspan="2">
				<input type="submit" value="Update Information">
			</td>
		</tr>
	</table>

</form:form>



<tags:commonJs></tags:commonJs>