<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="header" class="center">
	<div id="welcome">Административная часть. Ваш логин <c:out value="${username}"/></div>
	<a href="<c:url value="/logout"/>" id="out-for-client">Выход</a>
	<div id="logo"><a href="."><img src="<c:url value="/images/logotype.png"/>" alt="Логотип LiteCall"></a></div>
	<div id="admin-head-pic"></div>
</div>