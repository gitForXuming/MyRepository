<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>

    <script type="text/javascript" language="javascript" src="${contentPath}/js/jquery-1.5.2.js"></script>
    <script type="text/javascript" language="javascript" src="${contentPath}/js/test.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            $("#btn").click(function () {
                var number = $("#num").val();
                if (number.length == 0) {
                    alert(1);
                } else {

                    $.ajax({
                        type: "post",
                        url: "ajaxQuery",
                        data: {"number": number},
                        success: function (data) {
                            if (null != data.errorMessage && "" != data.errorMessage) {
                                alert(data.errorMessage);
                                return false;
                            }
                            $("#num").attr("value", data.number);
                            $("#username").attr("value", data.username);
                            $("#password").attr("value", data.password);
                        }
                    });
                }
            });
        });
    </script>
</head>
<body>
进入到添加页面${contentPath}
<form action="addsubmit" method="post">
    编号：<input type="text" id="num" name="number"/><input type="button" id="btn" value="检查编号"><br>
    <spring:message code="username"/><input type="text" id="username" name="username"/><br>
    密码：<input type="text" id="password" name="password"/><br>
    <input type="submit" value="提交">
</form>
</body>
</html>