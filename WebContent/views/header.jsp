<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="header" class="center">
	<div id="welcome">Здравствуйте, <c:out value="${humanname}"/>. Ваш логин <c:out value="${username}"/></div>
	<a href="<c:url value="/logout"/>" id="out-for-client">Выход</a>
	<div id="logo"><a href="."><img src="images/logotype.png" alt=""></a></div>
	<div id="head-pic"></div>
</div>