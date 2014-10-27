<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js" type="text/javascript"></script>

<script type="text/javascript">

    $(document).unbind('keydown').bind('keydown', function (event) {
    var doPrevent = false;
    var INPUTTYPES = [
        "text", "password", "file", "date", "datetime", "datetime-local",
        "month", "week", "time", "email", "number", "range", "search", "tel",
        "url"];
    var TEXTRE = new RegExp("^" + INPUTTYPES.join("|") + "$", "i");
    if (event.keyCode === 8) {
        var d = event.srcElement || event.target;
        if ((d.tagName.toUpperCase() === 'INPUT' && d.type.match(TEXTRE)) ||
             d.tagName.toUpperCase() === 'TEXTAREA') {
            doPrevent = d.readOnly || d.disabled;
        } else {
            doPrevent = true;
        }
    }
    if (doPrevent) {
        event.preventDefault();
    }
});

</script>

</head>
<body>
	<!-- <iframe src="accessDenied" width="100%" height="20%"></iframe>
	<iframe src="accessDenied" width="100%" height="80%"></iframe> -->
	
	<a href="<c:url value="j_spring_security_logout" />" align="right"> Logout</a>
	
	<security:authorize access="hasRole('USER')">
		This text is only visible to a user
		<br/>
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
		This text is only visible to an admin
		<br/>
	</security:authorize>
	
	 <form:form method="Post" commandName="trialBean" action="adminTrial">
      	<table>
      		<tr>
      			<td>Name</td>
      			<td> <form:input path="name" id="nameInput"></form:input>
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
</body>
</html>