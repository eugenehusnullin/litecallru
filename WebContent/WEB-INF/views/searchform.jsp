<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<label>Выберите номер:</label>
<select name="queue" title="QQwww">
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

<label>Выберите период:</label>
<select name="period">
	<c:forEach items="${periods}" var="pi">
		<c:choose>
			<c:when test="${pi.name==period}">
				<option selected="selected" value="${pi.name}">${pi.description}</option>
			</c:when>
			<c:otherwise>
				<option value="${pi.name}">${pi.description}</option>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</select>

<label>С</label>
<input name="from" value="${from}" />
<label>По</label>
<input name="to" value="${to}" />
<br />
<input type="submit" value="Сделать отчет" />

<input type="hidden" name="page" value="1" />