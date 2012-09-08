<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Статистика</title>
</head>
<body>
	<div>Вы авторизованы: <c:out value="${username}"/> </div>
	<br />
	
	<form action="request" method="get">
		<label>Установите параметры</label>
		<br />

		<label>Очередь:</label>
		<select name="queue">
			<c:forEach items="${queues}" var="qi">
				<option value="${qi.name}">${qi.description}</option>
			</c:forEach>
		</select>		
		<br />
		
		<label>С</label>
		<input name="from" value=""/>		
		<br />
		
		<label>По</label>
		<input name="to"/>		
		<br />
		
		<input type="submit" value="Показать"/>
	</form>
	
	<br />
	<br />
	
	<table></table>
	
</body>
</html>