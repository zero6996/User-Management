<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- HTML5文档-->
<!DOCTYPE html>
<!-- 网页使用的语言 -->
<html lang="zh-CN">
<head>
    <!-- 指定字符集 -->
    <meta charset="utf-8">
    <!-- 使用Edge最新的浏览器的渲染方式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- viewport视口：网页可以根据设置的宽度自动进行适配，在浏览器的内部虚拟一个容器，容器的宽度与设备的宽度相同。
    width: 默认宽度与设备的宽度相同
    initial-scale: 初始的缩放比，为1:1 -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>添加用户</title>

    <script src="js/jquery-2.1.0.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <script src="js/bootstrapValidator.min.js"></script>
    <link rel="stylesheet" href="css/bootstrapValidator.min.css">
    <script>
        // 使用BootstrapValidator完成表单校验
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
                    name:{
                        message: '姓名格式不正确',
                        validators: {
                            notEmpty: {
                                message: '姓名不能为空！'
                            },
                            stringLength:{ //检测长度
                                min:1,
                                max:20,
                                message: '姓名输入不合法！'
                            },
                            regexp: { //正则验证
                                regexp: /^[\u4e00-\u9fa5]|[0-9a-zA-Z]{2,20}$/,
                                message: '姓名格式不正确'
                            }
                        }
                    },
                    age: {
                        message: '年龄信息不正确',
                        validators: {
                            notEmpty: { //检测非空,radio也可用
                                message: '年龄不能为空'
                            },
                            stringLength:{ //检测长度
                                min:1,
                                max:3,
                                message: '年龄输入不合法'
                            },
                            regexp: { //正则验证
                                regexp: /^(?:[1-9][0-9]?|1[01][0-9]|120)$/,
                                message: '年龄只能是数字且范围在1~120之间'
                            }
                        }
                    },
                    qq: {
                        validators: {
                            notEmpty: {
                                message: '内容不能为空'
                            },
                            stringLength:{
                                min:1,
                                max:20,
                                message: 'QQ号码格式错误'
                            },
                            regexp: {
                                regexp: /^\d{4,20}$/,
                                message: 'QQ只能是数字且范围在4~20位之间'
                            }
                        }
                    },
                    email:{
                        validators: {
                            notEmpty: {
                                message: '邮箱不能为空'
                            },
                            emailAddress: { //验证email地址
                                message: '邮箱地址格式有误'
                            }
                        }
                    }
                }
            });
        });
    </script>
</head>
<body>
<div class="container">
    <center><h3>添加联系人页面</h3></center>
    <form action="${pageContext.request.contextPath}/addUserServlet" method="post">
        <div class="form-group">
            <label for="name">姓名：</label>
            <input type="text" class="form-control" id="name" name="name" placeholder="请输入姓名">
        </div>

        <div class="form-group">
            <label>性别：</label>
            <input type="radio" name="gender" value="男" checked="checked"/>男
            <input type="radio" name="gender" value="女"/>女
        </div>

        <div class="form-group">
            <label for="age">年龄：</label>
            <input type="text" class="form-control" id="age" name="age" placeholder="请输入年龄">
        </div>

        <div class="form-group">
            <label for="address">籍贯：</label>
            <select name="address" class="form-control" id="address">
                <option value="广东" checked>广东</option>
                <option value="广西">广西</option>
                <option value="湖南">湖南</option>
                <option value="外国">外国</option>
            </select>
        </div>

        <div class="form-group">
            <label for="qq">QQ：</label>
            <input type="text" class="form-control" name="qq" id="qq" placeholder="请输入QQ号码"/>
        </div>

        <div class="form-group">
            <label for="email">Email：</label>
            <input type="text" class="form-control" name="email" id="email" placeholder="请输入邮箱地址"/>
        </div>

        <div class="form-group" style="text-align: center">
            <input class="btn btn-primary" type="submit" value="提交" />
            <input class="btn btn-default" type="reset" value="重置" />
            <input class="btn btn-default" type="button" value="返回" />
        </div>
        <div align="center">
            <span style="font-size: 18px; color: orangered">${add_error}</span>
        </div>
    </form>
</div>
</body>
</html>