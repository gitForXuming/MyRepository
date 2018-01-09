<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>抢红包</title>
<script type="text/javascript" language="javascript" src="E:\WebDevelopment\xingyetuoguan\springMvc\WebContent\js\jquery-1.5.2.js"></script>
<script type="text/javascript" language="javascript" src="E:\WebDevelopment\xingyetuoguan\springMvc\WebContent\js\test.js"></script>
<script type="text/javascript">
	$().ready(function(){
		$("#btnPut").click(function(){
			var totalMoney = $("#totalMoney").val();
			var totalCount = $("#totalCount").val();
			if(null==totalMoney||parseFloat(totalMoney)<parseFloat("0.00")){
				alert("红包金额不能为空");
				return false;
			}
			if(null==totalCount||0==totalCount){
				alert("红包个数不能为空");
				return false;
			}
				
			$.post("hongbao/ajaxPut" ,{'totalMoney':totalMoney,'totalCount':totalCount} ,function(data){
				alert(1);
				if(null!=data.errorMessage&&""!=data.errorMessage){
					alert(data.errorMessage);
					return false;
				}
				$("#putSuccess").html("已塞入钱包");
				
			})
		});
	});
	
	
	function submit(){
		var totalGrobPeople = $("#totalGrobPeople").val();
		if(null==totalGrobPeople||""==totalGrobPeople){
			alert("抢红包人数不能为空");
			return false;
		}
		
		$("#totalPeople").attr("value",totalGrobPeople);
		document.grabForm.submit();
	}
</script>
</head>
<body>
	红包总额：<input type="text" id="totalMoney" name="totalMoney"/><br>
	钱包个数：<input type="text" id="totalCount" name="totalCount"/>&nbsp;&nbsp;
	<input type="button" id="btnPut"  value ="塞入钱包"><br>
	<label id="putSuccess" style="color: red"></label>
	
	<br>
	<br>
	模拟抢红包人数：<input type="text" id="totalGrobPeople" name="totalGrobPeople"/>&nbsp;&nbsp;
	<input type="button" id="btnPut"  value ="开始抢" onclick="javascript:submit();"><br>
	<form name="grabForm" id="grabForm" action="grabController" method="post">
			<input type="hidden" name="totalPeople" id="totalPeople" />
	</form>
	
	
</body>
</html>