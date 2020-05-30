<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>千胜奇门API</title>
    <script src="http://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://cdn.bootcss.com/json2/20150503/json2.min.js"></script>
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="http://cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</head>
<body>

<shiro:guest>
    <div class="container-fluid">
        <div class="row">
            <div class="page-header">
                <h2>千胜奇门API</h2>
            </div>
        </div>
        <div class="row">
            <a href="/tool">API调试工具</a>
        </div>
    </div>
</shiro:guest>
</body>
</html>