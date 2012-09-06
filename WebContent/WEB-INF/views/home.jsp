<%@ page %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Home</title>
</head>
<body>
	<div> <c:out value="${username}"/> </div>
	
	<form action="request">
		<select id="queue">
			<c:forEach items="${queues}" var="qi">
				<option value="${qi}">${qi}</option>
			</c:forEach>
		</select>
	</form>
	
</body>
</html>