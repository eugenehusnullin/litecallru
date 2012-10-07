<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Статистика за ${date}</title>

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
					<div class="rImg">Статистика за ${date}</div>
				</div>
				<div class="gray-round">
					<div class="gray-roundRight">
						<%@ include file="searchform.jsp" %>
						<c:if test="${clients != null }">
							<c:choose>		
								<c:when test="${clients.size()!=0}">
									<table id="statInfo">
										<tr id="headTBL">
											<td style="width:70%"><b>Клиент</b></td>
											<td><b>Кол-во минут</b></td>
											<td><b>Сумма в руб. РФ</b></td>
										</tr>
										<c:forEach items="${clients}" var="client">
											<tr>
												<td>${client.clientname}</td>
												<td>${client.calltime}</td>
												<td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${client.moneysum}"></fmt:formatNumber> руб.</td>
											</tr>
										</c:forEach>
									</table>
								</c:when>
								<c:otherwise>
									<div class="fieldBold">Нет данных.</div>
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