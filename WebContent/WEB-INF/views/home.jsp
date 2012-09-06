<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Home</title>
</head>
<body>
	<div>Вы авторизованы: <c:out value="${username}"/> </div>
	<br />
	
	<form action="request" method="post">
		<label>Установите параметры</label>
		<br />

		<label>Очередь:</label>
		<select id="queue">
			<c:forEach items="${queues}" var="qi">
				<option value="${qi}">${qi}</option>
			</c:forEach>
		</select>
		
		<br />
		<label>С</label>
		<input type="date" id="from"/>
		
		<br />
		<label>По</label>
		<input type="date" id="to"/>
		
		<br />
		<input type="submit" value="Показать" id="submit"/>
	</form>
	
</body>
</html>