<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Общая статистика</title>

<link href="<c:url value="/images/favicon.png"/>" rel="shortcut icon" type="image/png" ></link>
<link href="<c:url value="/css/styles.css"/>" rel="stylesheet" type="text/css" ></link>
<script src="<c:url value="/js/jquery-1.8.2.js"/>" type="text/javascript" ></script>
 

</head>
<body>
	<div id="wrapper">
		<%@ include file="../../views/header.jsp" %>
		
		<div id="main-container" class="center">
			<div id="cont-block">
				<div class="main-title ">
					<div class="rImg">Общая статистика<span><a href="detailedLog?queue=${queue}&period=${period}&from=${from}&to=${to}&page=1"
						>Перейти в детализированную статистику</a></span></div>
				</div>
				<div class="clear"></div>
				<div class="gray-round">
					<div class="gray-roundRight">
						<form id="filtre" action="commonLog" method="get">
							<%@ include file="searchform.jsp" %>
						</form>
						<c:if test="${callsCount != null}">
							<c:choose>	
								<c:when test="${callsCount!=0}">
									<table id="statInfo">
										<tr><td>Кол-во звонков на номер</td><td>${callsCount}</td></tr>
										<tr><td>Кол-во обработанных звонков</td><td>${receivedCallsCount}</td></tr>
										<tr><td>Кол-во не обработанных звонков</td><td>${unreceivedCallsCount}</td></tr>
										<tr><td>Среднее время ожидания ответа в сек.</td><td>${averageWaitTime}</td></tr>
										<tr id="footTBL"><td><b>Общее обработанное количество минут</b></td><td><b>${sumCallTime}</b></td></tr>
									</table>
								</c:when>
								
								<c:otherwise>
									<div>По вашему запросу ничего не найдено.</div>
								</c:otherwise>
							</c:choose>
						</c:if>
					</div>
				</div>
				<div class="roundBottom"><div class="roundBottomRight"></div></div>  

				<div class="clear"></div>
			</div>
		</div>
    </div>
	
	<%@ include file="../../views/footer.html" %>
</body>
</html>