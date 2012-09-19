<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form id="filtre" action="byDay" method="get">
	<div class="clear"></div>
	<div class="fieldBold" style="width:150px">Выберите период:</div> 
	<select name="period" id="selectDate" class="selForm">
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
	<div id="hideID" style="display:none;">
		<span class="fieldBold">c</span>
		<input name="from" value="${from}" type="text" class="txtForm" />
		<span class="fieldBold">по</span>
		<input name="to" value="${to}" type="text" class="txtForm" />
	</div>
	<div class="clear"></div>
	<input type="submit" id="subBTN" value="Применить фильтр"  />
</form>