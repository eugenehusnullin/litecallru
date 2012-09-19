<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Статистика по дням</title>

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
					<div class="rImg">Статистика за ${date}</div>
				</div>
				<div class="gray-round">
					<div class="gray-roundRight">
						<%@ include file="partner_searchform.jsp" %>
						
						<table id="statInfo">
							<tr id="headTBL">
								<td style="width:70%"><b>Клиент</b></td>
								<td><b>Кол-во минут</b></td>
								<td><b>Сумма в руб. РФ</b></td>
							</tr>
							<c:forEach items="${clients}" var="client">
								<tr>
									<td>${client.clientname}</td>
									<td>${client.minutes}</td>
									<td>${client.moneysum} руб.</td>
								</tr>
							</c:forEach>
						</table>
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