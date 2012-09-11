<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Детализированная статистика</title>
</head>
<body>
	<div>Здравствуйте, <c:out value="${username}"/>
	<a href="/logout">Выход</a> </div>
	<br />
	
	<form action="detailedLog" method="get">
		<label>Выберите номер:</label>
		<select name="queue" title="QQwww">
			<c:forEach items="${queues}" var="qi">
				<c:if test="${qi.name==queue}"><option selected="selected" value="${qi.name}">${qi.description}</option></c:if>
				<c:if test="${qi.name!=queue}"><option value="${qi.name}">${qi.description}</option></c:if>
			</c:forEach>
		</select>		
		<br />
		
		<label>Выберите период:</label>
		<select name="period">
			<c:forEach items="${periods}" var="pi">
				<c:if test="${pi.name==period}"><option selected="selected" value="${pi.name}">${pi.description}</option></c:if>
				<c:if test="${pi.name!=period}"><option value="${pi.name}">${pi.description}</option></c:if>
			</c:forEach>
		</select>
				
		<label>С</label><input name="from" value="${from}"/>		
		<label>По</label><input name="to" value="${to}"/>
		<br />		
		<input type="submit" value="Сделать отчет"/>
		
		<input type="hidden" name="page" value="1"/>
	</form>

	<c:if test="${calls!=null}">	
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
			<c:forEach items="${calls}" var="call">
				<tr>
					<td>${call.call}</td>
					<td>${call.eventdate}</td>
					<td>${call.event}</td>
					<td>${call.waittime}</td>
					<td>${call.calltime}</td>
				</tr>
			</c:forEach>
		</table>
		<div>
			<%--For displaying Previous link except for the 1st page --%>
			<c:if test="${curPage != 1}">
				<td><a href="detailedLog?queue=${queue}&period=${period}&from=${from}&to=${to}&page=${curPage - 1}">Назад</a></td>
			</c:if>

			<%--For displaying Page numbers.
    		The when condition does not display a link for the current page--%>
			<table>
				<tr>
					<c:forEach begin="1" end="${pagesCount}" var="i">
						<c:choose>
							<c:when test="${curPage eq i}">
								<td>${i}</td>
							</c:when>
							<c:otherwise>
								<td><a href="detailedLog?queue=${queue}&period=${period}&from=${from}&to=${to}&page=${i}">${i}</a></td>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</tr>
			</table>

			<%--For displaying Next link --%>
			<c:if test="${curPage lt pagesCount}">
				<td><a href="detailedLog?queue=${queue}&period=${period}&from=${from}&to=${to}&page=${curPage + 1}">Далее</a></td>
			</c:if>
		</div>
	</c:if>
</body>
</html>