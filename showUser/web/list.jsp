<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>用户信息管理系统</title>

    <!-- 1. 导入CSS的全局样式 -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <!-- 2. jQuery导入，建议使用1.9以上的版本 -->
    <script src="js/jquery-2.1.0.min.js"></script>
    <!-- 3. 导入bootstrap的js文件 -->
    <script src="js/bootstrap.min.js"></script>
    <style type="text/css">
        td, th {
            text-align: center;
        }
    </style>
    <%--删除选中功能和全选/全不选功能JS代码--%>
    <script>
        function deleteUser(id) {
            // 用户安全提示
            if (confirm("您确定要删除吗？")){
                location.href="${pageContext.request.contextPath}/delUserServlet?id="+id;
            }
        }
        // 页面加载完毕后执行
        window.onload = function () {
            // 为删除选中绑定单击事件
            document.getElementById("delSelected").onclick = function () {
                if (confirm("确定要删除所有选中的信息么?")){
                    var flag = false;
                    // 判断是否有选中信息
                    var cbs = document.getElementsByName("uid");
                    for (var i = 0; i < cbs.length; i++) {
                        if(cbs[i].checked){ // 如果有任何一个信息被选中(checkbox.checked返回布尔值，表示是否被选中)
                            flag = true; // 允许提交
                            break;
                        }
                    }
                    if (flag)//判断是否允许提交
                        // 使用提交方法提交信息到后台
                        document.getElementById("form").submit();
                }
            }
            // 全选/全不选功能
            document.getElementById("firstCB").onclick = function () {
                // 获取列表所有的cb
                var cbs = document.getElementsByName("uid");
                // 遍历
                for (var i = 0; i < cbs.length; i++) {
                    // 设置这些cbs[i]的checked状态等于firstCB.checked
                    cbs[i].checked = this.checked;
                }
            }
        }
    </script>
</head>
<body>
<div class="container">
    <h3 style="text-align: center">用户信息列表</h3>
    <%--复杂条件分页查询功能--%>
    <div style="float: left;">
        <form class="form-inline" action="${pageContext.request.contextPath}/findUserByPageServlet" method="post">
            <div class="form-group">
                <label for="exampleInputName2">查询条件</label>
                <input type="text" class="form-control" id="exampleInputName2" name="search" value="${condition.search[0]}">
            </div>
<%--            <div class="form-group">--%>
<%--                <label for="exampleInputName3">籍贯</label>--%>
<%--                <input type="text" class="form-control" id="exampleInputName3" name="address" value="${condition.address[0]}">--%>
<%--            </div>--%>

<%--            <div class="form-group">--%>
<%--                <label for="exampleInputEmail2">邮箱</label>--%>
<%--                <input type="email" class="form-control" id="exampleInputEmail2" name="email" value="${condition.email[0]}">--%>
<%--            </div>--%>
            <button type="submit" class="btn btn-default">查询</button>
        </form>

    </div>
    <%--添加/批量删除功能--%>
    <div style="float: right;margin: 5px;">

        <a class="btn btn-primary" href="${pageContext.request.contextPath}/add.jsp">添加联系人</a>
        <a class="btn btn-primary" href="javascript:void(0);" id="delSelected">删除选中</a>

    </div>
    <%--用户数据显示区--%>
    <!--使用form表单将复选框内容提交-->
    <form action="${pageContext.request.contextPath}/delSelectedServlet" id="form" method="post">
        <table border="1" class="table table-bordered table-hover">
            <tr class="success">
                <th><input type="checkbox" id="firstCB"></th>
                <th>编号</th>
                <th>姓名</th>
                <th>性别</th>
                <th>年龄</th>
                <th>籍贯</th>
                <th>QQ</th>
                <th>邮箱</th>
                <th>操作</th>
            </tr>

            <c:forEach items="${userPage.list}" var="user" varStatus="s">
                <tr>
                    <td><input type="checkbox" name="uid" value="${user.id}"></td>
                    <td>${s.count+userPage.currentPage*5-5}</td>
                    <td>${user.name}</td>
                    <td>${user.gender}</td>
                    <td>${user.age}</td>
                    <td>${user.address}</td>
                    <td>${user.qq}</td>
                    <td>${user.email}</td>
                    <td>
                        <a class="btn btn-default btn-sm" href="${pageContext.request.contextPath}/findUserServlet?id=${user.id}">修改</a>&nbsp
                        <a class="btn btn-default btn-sm" href="javascript:deleteUser('${user.id}');">删除</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </form>
    <%--后台响应数据区--%>
    <div align="center">
        <span style="color: #2aabd2; font-size: 18px; ">
            <%--添加成功响应的数据--%>
            <%=request.getSession().getAttribute("add_success") == null ? "" : request.getSession().getAttribute("add_success")%>
            <%--显示完数据后将数据从session中删除--%>
            <%request.getSession().removeAttribute("add_success");%>
            <%--删除成功or失败显示的数据--%>
            <%=request.getSession().getAttribute("del_error") == null ? "" : request.getSession().getAttribute("del_error")%>
            <%request.getSession().removeAttribute("del_error");%>
            <%=request.getSession().getAttribute("del_success") == null ? "" : request.getSession().getAttribute("del_success")%>
            <%request.getSession().removeAttribute("del_success");%>
            <%--修改成功--%>
            <%=request.getSession().getAttribute("update_success") == null ? "" : request.getSession().getAttribute("update_success")%>
            <%request.getSession().removeAttribute("update_success");%>
        </span>
    </div>
    <%--分页查询功能--%>
    <div>
        <nav aria-label="Page navigation">
            <ul class="pagination">
                <%-- 控制前一页不超出限制--%>
                <c:if test="${userPage.currentPage == 1}">
                    <li class="disabled">
                    <a href="#" aria-label="Previous">
                </c:if>
                <c:if test="${userPage.currentPage != 1}">
                    <li>
<%--                    <a href="${pageContext.request.contextPath}/findUserByPageServlet?currentPage=${userPage.currentPage - 1}&name=${condition.name[0]}&address=${condition.address[0]}&email=${condition.email[0]}" aria-label="Previous">--%>
                    <a href="${pageContext.request.contextPath}/findUserByPageServlet?currentPage=${userPage.currentPage - 1}&search=${condition.search[0]}" aria-label="Previous">
                </c:if>
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
                <c:forEach begin="1" end="${userPage.totalPage}" var="i">
                    <c:if test="${userPage.currentPage == i}"> <%--如果服务器返回的页码等于当前页码，则将按钮激活--%>
                        <li class="active"><a href="${pageContext.request.contextPath}/findUserByPageServlet?currentPage=${i}&search=${condition.search[0]}">${i}</a></li>
                    </c:if>
                    <c:if test="${userPage.currentPage != i}"> <%--如果服务器返回的页码不等于当前页码，则正常显示--%>
                        <li><a href="${pageContext.request.contextPath}/findUserByPageServlet?currentPage=${i}&search=${condition.search[0]}">${i}</a></li>
                    </c:if>
                </c:forEach>
                <%-- 控制下一页不超出限制--%>
                <c:if test="${userPage.currentPage == userPage.totalPage}">
                    <li class="disabled">
                    <a href="#" aria-label="Next">
                </c:if>
                <c:if test="${userPage.currentPage != userPage.totalPage}">
                    <li>
                    <a href="${pageContext.request.contextPath}/findUserByPageServlet?currentPage=${userPage.currentPage + 1}&search=${condition.search[0]}" aria-label="Next">
                </c:if>
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
                <span style="font-size: 20px;margin-left: 8px;">
                    共${userPage.totalCount}条记录，共${userPage.totalPage}页
                </span>

            </ul>
        </nav>

    </div>
</div>
</body>
</html>
