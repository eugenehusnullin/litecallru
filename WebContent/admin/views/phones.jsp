<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Номера телефонов</title>

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
						<a href="defaultPartners" class="tab-select">Партнеры</a><span class="tab-select"><i>Клиенты</i></span>
						<span><a href="" class="openAddPhone">Добавить номер</a></span>
					</div>					
				</div>
				<div class="gray-round">
					<div class="gray-roundRight">
						<div class="clear"></div>
						<b>Номера клиента: ${clientName} [${clientId}]</b>
						<form name="editphones" action="deletePhones" method="post">
							<table id="statInfo">
								<tr id="headTBL">
									<td><input type="checkbox" name="master_box" onclick="javascript:ckeck_uncheck_all()" /></td>
									<td><b>Номер</b></td>
									<td><b>Тип номера</b></td>
									<td><b>Тарификация</b></td>
									<td><b>Сумма в руб. РФ за текущий месяц</b></td>
									<td><b>Удаление</b></td>
								</tr>
								<c:forEach items="${phones}" var="phone">
									<tr>
										<td><input type="checkbox" name="${phone.id}" onclick="javascript:editIds()"/></td>
										<td>${phone.description}</td>
										<td>${phone.typedescr}</td>
										<td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${phone.tariff}"></fmt:formatNumber></td>
										<td><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${phone.calltime}"></fmt:formatNumber></td>
										<td><a onclick="return confirm('Вы точно хотите удалить телефон?')" 
											href="deletePhone?id=${phone.id}&clientId=${clientId}">удалить</a></td>
									</tr>
								</c:forEach>
							</table>
							<span id="contCall">Номеров:  <b>${phonesCount}</b></span>
							<div id="pagination">
								Страницы:
								<c:forEach begin="1" end="${pagesCount}" var="i">
									<c:choose>
										<c:when test="${curPage eq i}">											
											<span><c:out value="${i}"></c:out></span>
										</c:when>
										<c:otherwise>
											<a href="phones?clientId=${clientId}&page=${i}">${i}</a>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</div>
							<div class="clear"></div>
							<input type="submit" id="btnDelete" disabled="disabled" class="greenBtn" value="Удалить"
								onclick="return confirm('Вы точно хотите удалить телефон?')" />
							<input type="hidden" name="ids"/>
							<input type="hidden" name="clientId" value="${clientId}"/>
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
	
	<div id="addPhone" style="display:none;">
		<span id="title-pop">Добавление номера</span>
    	<div id="cont-pop">
        	<form action="addPhone" method="post" onsubmit="return checkPhone(this);">
				<table class="popTable" cellspacing="0" cellpadding="0">
					<tr>
						<td><span class="field">Номер:</span>
							<span class="red">*</span><br />
							<input type="text" name="description" class="inputTXT" />
						</td>
					</tr>
					<tr>					
						<td><span class="field">Тип номера:</span>
							<span class="red">*</span><br />
							<select class="SelTarif" name="typedescr" >
								<option selected="selected">Городской</option>
								<option>Федеральный</option>
							</select>
						</td>					
					</tr>		
					<tr>					
						<td><span class="field">Тарификация:</span>
							<span class="red">*</span><br />
							<select class="SelTarif" name="tariff" >
								<option>3.99</option>
								<option>4.99</option>
								<option>5.99</option>
								<option>6.99</option>
								<option>7.99</option>
								<option>8.99</option>
								<option>9.99</option>
							</select>
						</td>
					</tr>				
				</table>
				<input type="submit" name="send"  class="greenBtn" value="Добавить" />
				<input type="hidden" name="clientId" value="${clientId}"/>
				<input type="hidden" name="page" value="${curPage}"/>
				<div class="clear"></div>
	        </form>
		</div>
	</div>
	
	<script type="text/javascript">
	    $(document).ready(function() {
		
			$('.openAddPhone').click(function(){
		        mw = new ModalWindowClass();
		        mw.show($('#addPhone').html());
				return false;
		    });
		
		});
	</script>
	
	<script>
		function checkPhone(obj){
			var return_value = true;
		    var description = obj.description.value;
		    var typedescr = obj.typedescr.value;
		    var error_msg = "Не корректно заполнины поля:  \n";
		   
		    //проверка поля отправитель
		    if(description ==""){
		        error_msg += "- Номер  \n";
		        return_value = false;
		    }
		    //проверка поля почтовый ящик
		    if(typedescr == ""){
		        error_msg += "- Тип номера \n";
		        return_value = false;
		    }
		
		    //проверка на наличие ошибок, если возникла ошибка ввыводим текст сообщения
		    if(!return_value)
		        alert(error_msg);
		       
		    return return_value;
		}
	</script>
	
	<script>
		function ckeck_uncheck_all() {
		    var frm = document.editphones;
		    
		    for (var i=0;i<frm.elements.length;i++) {
		        var elmnt = frm.elements[i];
		        if (elmnt.type=='checkbox') {
		        	elmnt.checked=frm.master_box.checked;
		        }
		    }
		    
		    document.editphones.btnDelete.disabled = !frm.master_box.checked;
		}
	</script>
	
	<script>
	    function editIds() {
				var frm = document.editphones;
		        var ids = "";
			    for (var i=0;i<frm.elements.length;i++) {
			        var elmnt = frm.elements[i];
			        if (elmnt.type=='checkbox') {
			            if(elmnt.checked == true){
			            	ids = ids + elmnt.name + ";";
			            }
			        }
			    }			    
			    frm.ids.value = ids;
			    
			    var enabled = false;
			    for (var i=0;i<frm.elements.length;i++) {
			        var elmnt = frm.elements[i];
			        if (elmnt.type=='checkbox') {
			        	enabled = enabled || elmnt.checked;
			        }
			    }
			    document.editphones.btnDelete.disabled = !enabled;
		}
	</script>
</body>
</html>