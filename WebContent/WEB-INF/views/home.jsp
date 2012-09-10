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
	<div>Вы авторизованы как: <c:out value="${username}"/> <a href="/logout">Выход</a> </div>
	<br />
	
	<form action="request" method="get">
		<label>Установите параметры</label>
		<br />

		<label>Очередь:</label>
		<select name="queue">
			<c:forEach items="${queues}" var="qi">
				<c:if test="${qi.name==queue}">
					<option selected="selected" value="${qi.name}">${qi.description}</option>
				</c:if>
				<c:if test="${qi.name!=queue}">
					<option value="${qi.name}">${qi.description}</option>
				</c:if>
			</c:forEach>
		</select>		
		<br />
		
		<label>С</label>
		<input name="from" value="${from}"/>		
		<br />
		
		<label>По</label>
		<input name="to" value="${to}"/>
		<br />
		
		<input type="submit" value="Показать"/>
	</form>
	

	<c:if test="${queueLog!=null}">	
		<br />
		<table>
			<thead>
				<tr>
					<td>Соединение</td>
					<td>Дата</td>
					<td>Событие</td>
					<td>Ожидание сек.</td>
					<td>Время разговора сек.</td>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td>Соединение</td>
					<td>Дата</td>
					<td>Событие</td>
					<td>Ожидание сек.</td>
					<td>Время разговора сек.</td>
				</tr>
			</tfoot>
			<c:forEach items="${queueLog}" var="ql">
				<tr>
					<td>${ql.call}</td>
					<td>${ql.eventdate}</td>
					<td>${ql.event}</td>
					<td>${ql.waittime}</td>
					<td>${ql.calltime}</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	
	
	
</body>
</html>