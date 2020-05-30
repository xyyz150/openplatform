<%--
  Created by IntelliJ IDEA.
  User: xyyz150
  Date: 2016/9/9
  Time: 15:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>API在线测试工具</title>
    <script src="http://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://cdn.bootcss.com/json2/20150503/json2.min.js"></script>
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="http://cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="http://cdn.bootcss.com/select2/4.0.2/js/select2.full.min.js"></script>
    <link href="http://cdn.bootcss.com/select2/4.0.2/css/select2.min.css" rel="stylesheet">
    <link href="http://cdn.bootcss.com/select2-bootstrap-theme/0.1.0-beta.9/select2-bootstrap.min.css" rel="stylesheet">
    <script src="http://cdn.bootcss.com/jquery-validate/1.14.0/jquery.validate.min.js"></script>
    <script src="/static/js/tool.js?v26999"></script>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="page-header">
            <h2>API在线测试</h2>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-5">
            <form id="myform" class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-2 control-label select2">返回格式</label>
                    <div class="col-sm-10">
                        <select class="form-control" id="format">
                            <option selected value="json">json</option>
                            <option value="xml">xml</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">API名称</label>
                    <div class="col-sm-10">
                        <select id="apiName" class="form-control">
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">版本*</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="version" name="version" required disabled>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">appkey*</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="appkey" name="appkey" minlength="6" required>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">appsercet*</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="appsercet" name="appsercet" required>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">accessToken*</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="sessionkey" name="sessionkey" required>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">请求body*</label>
                    <div class="col-sm-10">
                        <textarea class="form-control" id="body" name="body" rows="5" required></textarea>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="button" class="btn btn-primary" id="tijiao">提交</button>
                    </div>
                </div>
            </form>
        </div>
        <div class="col-sm-6">
            <form class="form">
                <div class="form-group">
                    <label>API请求参数</label>
                    <textarea class="form-control" id="requestbody" rows="6"></textarea>
                </div>
                <div class="form-group">
                    <label>API返回结果</label>
                    <textarea class="form-control" id="responsebody" rows="10"></textarea>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
