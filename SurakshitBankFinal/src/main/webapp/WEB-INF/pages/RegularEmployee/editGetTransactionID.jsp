<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix='tags' tagdir='/WEB-INF/tags' %>
	
	<form:form action="editTransaction" method="POST">
        <table>
		
		<tr>
			<td>Please enter the transaction ID</td>
			<td><input name="transactionID" /></td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="submit" value="Submit">
			</td>
		</tr>
		</table>
        
    </form:form>
      
<tags:commonJs />