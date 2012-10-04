<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="fieldBold" style="width:150px">Выберите номер:</div>
<select name="queue" class="selForm">
	<c:forEach items="${queues}" var="qi">
		<c:choose>
			<c:when test="${qi.name==queue}">
				<option selected="selected" value="${qi.name}">${qi.description}</option>
			</c:when>
			<c:otherwise>
				<option value="${qi.name}">${qi.description}</option>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</select>
<br />
<br />

<%@ include file="../../views/period.jsp" %>

<input type="hidden" name="page" value="1" />
