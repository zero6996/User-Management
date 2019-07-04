<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>管理员登录</title>

	  <script src="js/jquery-2.1.0.min.js"></script>
	  <script src="js/bootstrap.min.js"></script>
	  <link href="css/bootstrap.min.css" rel="stylesheet">
	  <script src="js/bootstrapValidator.min.js"></script>
	  <link rel="stylesheet" href="css/bootstrapValidator.min.css">
    <script type="text/javascript">
		function refreshCode() {
			// 1. 获取验证码对象
			var img = document.getElementById("img");
			// 2.设置其src属性，加时间戳
			img.src = "${pageContext.request.contextPath}/checkCodeServlet?time=" + new Date().getTime();
		}
		$(function () {
			$('form').bootstrapValidator({
				live: 'enabled', //验证时机，enabled是内容有变化就验证（默认），disabled和submitted是提交再验证
				message: 'This value is not valid',
				feedbackIcons: { //根据验证结果显示的各种图标
					valid: 'glyphicon glyphicon-ok',
					invalid: 'glyphicon glyphicon-remove',
					validating: 'glyphicon glyphicon-refresh'
				},
				fields: {
					user:{
						message: '用户名格式不正确',
						validators: {
							notEmpty: {
								message: '用户名不能为空！'
							},
							stringLength:{ //检测长度
								min:1,
								max:30,
								message: '用户名输入不合法！'
							},
							regexp: { //正则验证
								regexp: /^[\u4e00-\u9fa5]|[0-9a-zA-Z]{2,20}$/,
								message: '用户名格式不正确'
							}
						}
					},
					password:{
						validators: {
							notEmpty: {
								message: '密码不能为空！'
							},
							stringLength:{ //检测长度
								min:1,
								max:30,
								message: '密码输入不合法！'
							},
							regexp: { //正则验证
								regexp: /^[\w_-]{6,20}$/, // \w等价于[a-zA-Z0-9]
								message: '密码格式不正确'
							}
						}
					}
				},
			});
		});
	</script>
  </head>
  <body>
  	<div class="container" style="width: 400px;">
  		<h3 style="text-align: center;">管理员登录</h3>
        <form action="${pageContext.request.contextPath}/loginServlet" method="post">
	      <div class="form-group">
	        <label for="user">用户名：</label>
	        <input type="text" name="user" class="form-control" id="user" placeholder="请输入用户名"/>
	      </div>
	      
	      <div class="form-group">
	        <label for="password">密码：</label>
	        <input type="password" name="password" class="form-control" id="password" placeholder="请输入密码"/>
	      </div>
	      
	      <div class="form-inline">
	        <label for="img">验证码：</label>
	        <input type="text" name="verifycode" class="form-control" id="verifycode" placeholder="请输入验证码" style="width: 120px;"/>
              <a href="javascript:refreshCode();">
                  <img src="${pageContext.request.contextPath}/checkCodeServlet" title="看不清点击刷新" id="img"/>
              </a>
	      </div>
	      <hr/>
	      <div class="form-group" style="text-align: center;">
	        <input class="btn btn btn-primary" type="submit" value="登录">
	       </div>
	  	</form>

		<!-- 出错显示的信息框 -->
	  	<div class="alert alert-warning alert-dismissible" role="alert">
		  <button type="button" class="close" data-dismiss="alert" >
		  	<span>&times;</span></button>
		   <strong>
			   <%=request.getAttribute("login_error") == null ? "" : request.getAttribute("login_error")%>
			   <%=request.getAttribute("cc_error") == null ? "" : request.getAttribute("cc_error")%>
			   <%=request.getAttribute("login_msg") == null ? "" : request.getAttribute("login_msg")%>
		   </strong>
		</div>
  	</div>
  </body>
</html>