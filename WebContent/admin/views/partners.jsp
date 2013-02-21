<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Партнеры</title>

<link href="<c:url value="/images/favicon.png"/>" rel="shortcut icon" type="image/png" ></link>
<link href="<c:url value="/css/styles.css"/>" rel="stylesheet" type="text/css" ></link>
<script src="<c:url value="/js/jquery-1.8.2.js"/>" type="text/javascript" ></script>
<script src="<c:url value="/js/modal-window.js"/>" type="text/javascript" ></script>
</head>
<body>
		<div id="wrapper">
		<%@ include file="header.jsp" %>
		<div id="main-container" class="center">
			<div id="cont-block">
				<div class="main-title ">
					<div class="rImg">
						<span class="tab-select"><i>Партнеры</i></span><a href="defaultClients" class="tab-select">Клиенты</a>
						<span><a href="" class="openAddPartner">Добавить партнера</a></span>
					</div>					
				</div>
				<div class="gray-round">
					<div class="gray-roundRight">
						<form id="filtre" action="changeSortPartners" method="get">
							<span class="fieldBold" style="width:200px">Сортировать по:</span>
							<select id="selectDate" name="sortType" class="selForm">
								<option <c:if test="${sortType==1}">selected="selected"</c:if> value="1">кол-во клиентов</option>
								<option <c:if test="${sortType==2}">selected="selected"</c:if> value="2">сумма</option>
							</select>
							<select id="selectDate" name="sortOrder" class="selForm">
								<option <c:if test="${sortOrder==1}">selected="selected"</c:if> value="1">по возрастанию</option>
								<option <c:if test="${sortOrder==-1}">selected="selected"</c:if> value="-1">по убыванию</option>
							</select>
							<div class="clear"></div>
							<input type="submit" id="subBTN" value="Сортировать" />
						</form>
						<form id="search" action="searchPartner" method="get">
							<input type="text" placeholder="поиск по ID" name="id" />							
							<div class="clear"></div>
							<input type="submit" id="subBTN" value="Искать" />
							<input type="hidden" name="sortType" value="${sortType}" />
							<input type="hidden" name="sortOrder" value="${sortOrder}" />
						</form>						
						
						<div class="clear"></div>
						<table id="statInfo">
							<tr id="headTBL">
								<td><b>ID</b></td>
								<td style="width:40%"><b>Партнер</b></td>
								<td><b>Кол-во клиентов</b></td>
								<td><b>Сумма в руб. РФ</b></td>
								<td><b>Удаление</b></td>
							</tr>
							<c:forEach items="${partners}" var="partner">
								<tr>
									<td>${partner.id}</td>
									<td><a href="clients?partnerId=${partner.id}&page=1&sortOrder=1">${partner.name}</a></td>
									<td>${partner.clientsCount}</td>
									<td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${partner.calltime}"></fmt:formatNumber></td>
									<td><a href="deletePartner?id=${partner.id}&sortType=${sortType}&sortOrder=${sortOrder}"
											onclick="return confirm('Вы точно хотите удалить партнера?')" >удалить</a></td>
								</tr>
							</c:forEach>
						</table>
						<span id="contCall">Партнеров:  <b>${partnersCount}</b></span>
						<div id="pagination">
							Страницы:
							<c:forEach begin="1" end="${pagesCount}" var="i">
								<c:choose>
									<c:when test="${curPage eq i}">											
										<span><c:out value="${i}"></c:out></span>
									</c:when>
									<c:otherwise>
										<a href="partners?page=${i}&sortType=${sortType}&sortOrder=${sortOrder}">${i}</a>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</div>
					</div>
				</div>
				<div class="roundBottom"><div class="roundBottomRight"></div></div>
				  
				<div class="clear"></div>
			</div>
		</div>
	</div>
	<%@ include file="../../views/footer.html" %>
	
	<div id="addPartner" style="display:none;">
		<span id="title-pop">Добавление партнера</span>
    	<div id="cont-pop">
        	<form action="addPartner" method="post" onsubmit="return checkPartnerForm(this);">
				<table class="popTable" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<span class="field">Наименование (ФИО):</span>
							<span class="red">*</span><br />
							<input type="text" name="name" class="inputTXT" />
						</td>
					</tr>
					<tr>
						<td>
							<span class="field">e-mail партнера:</span>
							<span class="red">*</span><br />
							<input type="text" name="email" class="inputTXT" />
						</td>					
					</tr>		
				</table>
				<input type="submit" name="send"  class="greenBtn" value="Добавить" />
				<input type="hidden" name="sortType" value="${sortType}" />
				<input type="hidden" name="sortOrder" value="${sortOrder}" />
				<div class="clear"></div>
	        </form>
		</div>
	</div>
	
	
	<script type="text/javascript">
	    $(document).ready(function() {
		
			$('.openAddPartner').click(function(){
		        mw = new ModalWindowClass();
		        mw.show($('#addPartner').html());
				return false;
		    });
		
		});
	</script>
	
	<script>
		function checkPartnerForm(obj){
		    var return_value = true;
		    // регулярное вырожение, для проверки почтового ящика           
		    var reg_mail = /[0-9a-z_]+@[0-9a-z_^.]+.[a-z]{2,3}/i;
		    // регулярное вырожение, для проверки отправителя
		    var reg_sender = /[a-z]+/i;
		    // заносим значение поля почтовый ящик в переменную mail
		    var mail = obj.email.value;
		    // заносим значение поля отправитель в переменную sender
		    var sender = obj.name.value;
		    // заносим значение поля сообщение в переменную msg
		    var error_msg = "Не корректно заполнины поля:  \n";
		   
		    //проверка поля отправитель
		    if(reg_sender.exec(sender) == null && sender ==""){
		        error_msg += "- Наименование \n";
		        return_value = false;
		    }
		    //проверка поля почтовый ящик
		    if(reg_mail.exec(mail) == null){
		        error_msg += "- e-mail \n";
		        return_value = false;
		    }
		
		    //проверка на наличие ошибок, если возникла ошибка ввыводим текст сообщения
		    if(!return_value)
		        alert(error_msg);
		       
		    return return_value;
		}
	</script>
</body>
</html>