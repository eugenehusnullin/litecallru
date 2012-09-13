<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Детализированная статистика</title>
</head>
<body>
	<div>Здравствуйте, <c:out value="${username}"/>
	<a href="logout">Выход</a> </div>
	<br />
	
	<div>Детализированная статистика</div>
	<a href="commonLog?queue=${queue}&period=${period}&from=${from}&to=${to}">Перейти в общую статистику</a>
	
	<form action="detailedLog" method="get">
		<%@ include file="searchform.jsp" %>
	</form>

	<c:if test="${callsCount != null}">
		<c:choose>
		
			<c:when test="${callsCount!=0}">
				<br />
				<table>
					<thead>
						<tr>
							<td>№</td>
							<td>Время</td>
							<td>Номер звонящего</td>
							<td>Время ожидания в сек.</td>
							<td>Длительность разговора в мин.</td>
							<td>Запись разговора</td>
						</tr>
					</thead>
					<c:forEach items="${calls}" var="call">
						<tr>
							<td>${((curPage-1)*10)+call.rownum}</td>
							<td>${call.eventdate}</td>
							<td>${call.callerid}</td>
							<td>${call.waittime}</td>
							<td>${call.calltime}</td>
							<td><a href="http://31.31.23.219:11983/monitor/${call.uniqueid}.wav">Запись</a></td>
						</tr>
					</c:forEach>
				</table>
				<div>
					Найдено вызовов: <b>${callsCount}</b>
				</div>
				<div>
					Страницы:
					<c:forEach begin="1" end="${pagesCount}" var="i">
						<c:choose>
							<c:when test="${curPage eq i}">
								<c:out value="${i}"></c:out>
							</c:when>
							<c:otherwise>
								<a href="detailedLog?queue=${queue}&period=${period}&from=${from}&to=${to}&page=${i}">${i}</a>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</div>
			</c:when>
			
			<c:otherwise>
				<div>По вашему запросу ничего не найдено.</div>
			</c:otherwise>
		</c:choose>
	</c:if>
		
</body>
</html>