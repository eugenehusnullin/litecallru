<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Детализированная статистика</title>

<link href="<c:url value="/images/favicon.png"/>" rel="shortcut icon" type="image/png" ></link>
<link href="<c:url value="/css/styles.css"/>" rel="stylesheet" type="text/css" ></link>
<script src="<c:url value="/js/jquery-1.8.2.js"/>" type="text/javascript" ></script>
<script src="<c:url value="/js/open-win.js"/>" type="text/javascript" ></script>
</head>
<body>
	<div id="wrapper">
	<%@ include file="../../views/header.jsp" %>
	<div id="main-container" class="center">
		<div id="cont-block">
			<div class="main-title ">
				<div class="rImg">Детализированная статистика<span><a href="commonLog?queue=${queue}&period=${period}&from=${from}&to=${to}"
					>Перейти в общую статистику</a></span></div>
			</div>
			<div class="clear"></div>
			<div class="gray-round">
				<div class="gray-roundRight">
					<form id="filtre" action="detailedLog" method="get">
						<%@ include file="searchform.jsp" %>
					</form>

					<c:if test="${callsCount != null}">
						<c:choose>		
							<c:when test="${callsCount!=0}">
								<script src="http://szenprogs.ru/scripts/swfobject.js" type="text/javascript"></script>
								<table id="statInfo">
									<thead>
										<tr>
											<td>№</td>
											<td>Время</td>
											<td>Номер</td>
											<td>Время ожидания в сек.</td>
											<td>Длительность разговора в мин.</td>
											<td>Запись разговора</td>
										</tr>
									</thead>
									<c:forEach items="${calls}" var="call">
										<tr>
											<td>${call.rownum}</td>
											<td>${call.eventdate}</td>
											<td>${call.callerid}</td>
											<td>${call.waittime}</td>
											<td>${call.calltime}</td>
											<td>
<c:if test="${call.call==1}">
<div id="mon_${call.uniqueid}" style="height:16px;width:150px;">
	<c:choose>
		<c:when test="${call.iscurrentdate==1}">
			<a href="compare" target="_blank" onclick=
				"ShowWin('http://${monitorhost}/monitor/${call.uniqueid}.wav', 350, 150); return false;">прослушать</a>
		</c:when>
		<c:otherwise>		
		  	<script type="text/javascript">
		  		var rnumber = Math.floor(Math.random()*9999999);
		  		var so = new SWFObject("http://szenprogs.ru/flash/audio.swf?r="+rnumber, "music_code", "150", "16", "9");
		  		so.addParam("wmode", "transparent");
		  		so.addParam("allowScriptAccess", "always");
		  		so.addVariable("song_url", "http://${monitorhost}/monitor/${call.uniqueid}.mp3");
		  		so.addVariable("show_copyright", "0");
		  		so.addVariable("background_color", "#7da62e");
		  		so.addVariable("autoplay", "0");
		  		so.addVariable("textoff", "1");
		  		so.addVariable("loop", "0");
		  		so.write("mon_${call.uniqueid}");
		  	</script>
		</c:otherwise>
	</c:choose>
</div>
</c:if>
											</td>
										</tr>
									</c:forEach>
								</table>
								<span id="contCall">Найдено вызовов:  <b>${callsCount}</b></span>
								<div id="pagination">
									Страницы:
									<c:forEach begin="1" end="${pagesCount}" var="i">
										<c:choose>
											<c:when test="${curPage eq i}">											
												<span><c:out value="${i}"></c:out></span>
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