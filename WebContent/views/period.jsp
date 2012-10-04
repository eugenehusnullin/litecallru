<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link href="<c:url value="/css/ui-lightness/jquery-ui-1.8.24.custom.css"/>" rel="stylesheet" type="text/css" ></link>

<script src="<c:url value="/js/jquery.ui.core.js"/>" type="text/javascript" ></script>
<script src="<c:url value="/js/jquery.ui.datepicker.js"/>" type="text/javascript" ></script>
<script src="<c:url value="/js/jquery.ui.datepicker-ru.js"/>" type="text/javascript" ></script>

<script type="text/javascript">
	$(document).ready(function(){
		$('#selectDate').change(function(){ 			
			var selectedValue = $('#selectDate :selected').val(); 
			if (selectedValue == 'custom'){$(this).parent().find('#hideID').css('display', 'inline');} 
			else{$(this).parent().find('#hideID').css('display', 'none');}
			
		});
		
		$('#selectDate').change();
    });
</script>

<script>
	$(function() {
		$("#fromDatepicker").datepicker({
			maxDate: "+1d",
			defaultDate: "-1m",
			onSelect: function( selectedDate ) {
				$( "#toDatepicker" ).datepicker( "option", "minDate", selectedDate);
			}
		});
	});
	
	$(function() {
		$("#toDatepicker").datepicker({
			maxDate: "+1d",
			onSelect: function( selectedDate ) {
				$( "#fromDatepicker" ).datepicker( "option", "maxDate", selectedDate );
			}
		});
	});
</script>

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
	<input name="from" value="${from}" type="text" class="txtForm" id="fromDatepicker" />
	<span class="fieldBold">по</span>
	<input name="to" value="${to}" type="text" class="txtForm" id="toDatepicker" />
</div>

<div class="clear"></div>
<input type="submit" id="subBTN" value="Применить фильтр"  />