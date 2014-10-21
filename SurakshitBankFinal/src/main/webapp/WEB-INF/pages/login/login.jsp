<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<form action="loginAction" method="POST">
        <label for="username">User Name1:</label>
        <input id="username" name="j_username" type="text"/><br/><br/>
        <label for="password">Password:</label>
        <input id="password" name="j_password" type="password"/><br/><br/>
        <input type="submit" value="Log In"/>
      </form>
</body>
</html>