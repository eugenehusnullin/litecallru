<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Детализированная статистика</title>

<link href="images/favicon.png" rel="shortcut icon" type="image/png" />
<link rel="stylesheet" type="text/css" href="css/styles.css" />   
<script type="text/javascript" src="js/jquery.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
		$('#selectDate').change(function(){ 			
			var myform = $('#selectDate :selected').val(); 
			if (myform == 'custom')
				{ $(this).parent().find('#hideID').css('display', 'inline');} 
			else 
				{$(this).parent().find('#hideID').css('display', 'none');}
			
		});
    });
</script>

</head>
<body>
	<div id="wrapper">
	<%@ include file="header.jsp" %>
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
				
								<table id="statInfo">
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
											<td>
												<c:if test="${call.call==1}">
													<script type="text/javascript" src="http://szenprogs.ru/scripts/swfobject.js"></script>
													<div id="music_box1" style="height:16px;"><embed type="application/x-shockwave-flash" src="http://szenprogs.ru/flash/audio.swf?r=7909186" width="150" height="16" id="music_code" name="music_code" quality="high" wmode="transparent" allowscriptaccess="always" flashvars="song_url=http://31.31.23.219:11983/monitor/${call.uniqueid}.wav&amp;show_copyright=0&amp;background_color=#7da62e&amp;autoplay=0&amp;textoff=1&amp;loop=0"></div>
													<script type="text/javascript">  var rnumber = Math.floor(Math.random()*9999999);  var so = new SWFObject("http://szenprogs.ru/flash/audio.swf?r="+rnumber, "music_code", "150", "16", "9");  so.addParam("wmode", "transparent");  so.addParam("allowScriptAccess", "always");  so.addVariable("song_url", "http://31.31.23.219:11983/monitor/${call.uniqueid}.wav");  so.addVariable("show_copyright", "0");  so.addVariable("background_color", "#7da62e");  so.addVariable("autoplay", "0");  so.addVariable("textoff", "1");  so.addVariable("loop", "0");  so.write("music_box1");</script>																										
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
	<%@ include file="footer.html" %>
</body>
</html>