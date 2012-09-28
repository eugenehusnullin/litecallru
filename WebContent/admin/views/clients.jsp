<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Клиенты</title>

<link href="<c:url value="images/favicon.png"/>" rel="shortcut icon" type="image/png" ></link>
<link href="<c:url value="css/styles.css"/>" rel="stylesheet" type="text/css" ></link>
<script src="<c:url value="js/jquery.js"/>" type="text/javascript" ></script>
</head>
<body>
		<div id="wrapper">
		<%@ include file="header.jsp" %>
		<div id="main-container" class="center">
			<div id="cont-block">
				<div class="main-title ">
					<div class="rImg">
						<a href="" class="tab-select">Партнеры</a><span class="tab-select"><i>Клиенты</i></span>
						<span><a href="" class="openAddClient">Добавить клиента</a></span>
					</div>					
				</div>
				<div class="gray-round">
					<div class="gray-roundRight">
						<form id="filtre" action="changeSortClient" method="get">
							<span class="fieldBold" style="width:200px">Сортировать по сумме:</span>
							<select id="selectDate" name="selectedSort" class="selForm">
								<c:choose>
									<c:when test="${sortOrder==1}">
										<option selected="selected" value="1">по возрастанию</option>
										<option value="-1">по убыванию</option>
									</c:when>
									<c:otherwise>
										<option value="1">по возрастанию</option>
										<option selected="selected" value="-1">по убыванию</option>
									</c:otherwise>
								</c:choose>
							</select>
							<div class="clear"></div>
							<input type="submit" id="subBTN" value="Сортировать" />
						</form>
						<form id="search">
							<input type="text" placeholder="поиск по ID" />							
							<div class="clear"></div>
							<input type="submit" id="subBTN" value="Искать" />
						</form>						
						
						<div class="clear"></div>
						<form name="editusers">
							<table id="statInfo">
								<tr id="headTBL">
									<td>ID</td>
									<td>Клиенты</td>
									<td>ID партнера</td>
									<td>Сумма в руб. РФ</td>
									<td>Изменение</td>
									<td>Удаление</td>
								</tr>
								<c:forEach items="${clients}" var="client">
									<tr>
										<td>${client.id}</td>
										<td>${client.name}</td>
										<td>${client.partnerid}</td>
										<td>${client.calltime}</td>
										<td>Изменить</td>
										<td><a href="deleteClient?id=${client.id}">Удалить</a></td>
									</tr>
								</c:forEach>
							</table>
							<span id="contCall">Клиентов:  <b>${clientsCount}</b></span>
							<div id="pagination">
								Страницы:
								<c:forEach begin="1" end="${pagesCount}" var="i">
									<c:choose>
										<c:when test="${curPage eq i}">											
											<span><c:out value="${i}"></c:out></span>
										</c:when>
										<c:otherwise>
											<a href="clients?page=${i}&sortOrder=${sortOrder}">${i}</a>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</div>
							<div class="clear"></div>
						</form>
					</div>
				</div>
				<div class="roundBottom"><div class="roundBottomRight"></div></div>  
				<div class="clear"></div>
			</div>
		</div>
	</div>
	<%@ include file="../../views/footer.html" %>
	
	<script type="text/javascript" src="js/modal-window.js"></script>
	
	<div id="addClient" style="display:none;">
		<span id="title-pop">Добавление клиента</span>
    	<div id="cont-pop">
        	<form action="addClient" method="post" onsubmit="return checkForm(this);">
				<table class="popTable" cellspacing="0" cellpadding="0">
					<tr>
						<td><span class="field">Наименование клиента:</span>
							<span class="red">*</span><br />
							<input type="text" name="name" class="inputTXT" /></td>
					</tr>
					<tr>					
						<td><span class="field">e-mail клиента:</span>
							<span class="red">*</span><br />
							<input type="text" name="email" class="inputTXT" /></td>					
					</tr>		
					<tr>					
						<td><span class="field">ID партнера:</span><br />
							<input type="text" name="partnerid" class="inputTXT" /></td>					
					</tr>				
				</table>
				<input type="submit" name="send"  class="greenBtn" value="Добавить" />
				<div class="clear"></div>
	        </form>
		</div>
	</div>
	
	
	<script type="text/javascript">
	    $(document).ready(function() {
		
			$('.openAddClient').click(function(){
		        mw = new ModalWindowClass();
		        mw.show($('#addClient').html());
				return false;
		    });
		
		});
	</script>
	
	<script>
		function checkForm(obj){
		    var return_value = true;
		    // регулярное вырожение, для проверки почтового ящика           
		    var reg_mail = /[0-9a-z_]+@[0-9a-z_^.]+.[a-z]{2,3}/i;
		    // регулярное вырожение, для проверки отправителя
		    var reg_sender = /[a-z]+/i;
		    // заносим значение поля почтовый ящик в переменную mail
		    var mail = obj.YMail.value;
		    // заносим значение поля отправитель в переменную sender
		    var sender = obj.YName.value;
		    // заносим значение поля сообщение в переменную msg
		    var error_msg = "Не корректно заполнины поля:  \n";
		   
		    //проверка поля отправитель
		    if(reg_sender.exec(sender) == null && sender ==""){
		        error_msg += "- ФИО партнера  \n";
		        return_value = false;
		    }
		    //проверка поля почтовый ящик
		    if(reg_mail.exec(mail) == null){
		        error_msg += "- e-mail партнера \n";
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