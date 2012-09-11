<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Общая статистика</title>
</head>
<body>
	<div>Здравствуйте, <c:out value="${username}"/>
	<a href="/logout">Выход</a> </div>
	<br />
	
	<div>Общая статистика</div>
	<a href="detailedLog?queue=${queue}&period=${period}&from=${from}&to=${to}&page=1">Перейти в детализированную статистику</a>
	
	<form action="commonLog" method="get">
		<%@ include file="searchform.jsp" %>
	</form>

	<table>
		<tr><td>Количество звонков на номер</td><td>${callsCount}</td></tr>
		<tr><td>Количество обработанных звонков</td><td>${receivedCallsCount}</td></tr>
		<tr><td>Количество не обработанных звонков</td><td>${unreceivedCallsCount}</td></tr>
		<tr><td>Среднее время ожидания ответа в сек.</td><td>${averageWaitTime}</td></tr>
		<tr><td><b>Общее обработанное количество минут</b></td><td><b>${sumCallTime}</b></td></tr>
	</table>
		
</body>
</html>