<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Вход</title>
</head>
<body onload='document.f.username.focus();'>
	<div>Вход в партнерку (пользователь 700001 пароль partner1)</div>
	<div>Вход клиента (пользователь 100001 пароль 12345)</div>
	<h3>Для входа введите имя пользователя (логин) и пароль</h3>	
	<form name='f' action="login" method="post">
		<table>
    		<tr><td>Имя пользователя:</td><td><input type='text' name='username' value=''/></td></tr>
    		<tr><td>Пароль:</td><td><input type='password' name='password'/></td></tr>
    		<tr><td colspan='2'><input type="submit" name="submit" value="Войти"/></td></tr>
		</table>
	</form>
	<% if (request.getParameter("auth") != null && request.getParameter("auth").equals("0")) { %>
	<div style="color: red; padding-left: 10px;">Имя пользователя или пароль заданы неверно.</div>	
	<% } %>
</body>
</html>