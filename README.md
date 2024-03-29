# :memo:A simple project for User-Management



## :speech_balloon:项目概述

这是一个简单的`JavaWeb`小项目，主要实现管理员:guardsman:登录，对用户:family:信息进行增删改查​​。后端使用MVC设计模型思想分离业务逻辑、数据操作、界面显示；前端使用`JSP+EL+JSTL`进行展示操作。

:ballot_box_with_check:：[项目链接](http://111.230.219.30/showUser/login.jsp)

- 测试账号：`admin`；密码：`admin123`



## :page_with_curl:简单功能

### :one:管理员登录

登录功能输入用户名密验证码，表单信息会提交到`loginServlet`进行登录校验，成功则跳转到首页，失败则返回失败信息。

### :two:首页列表查询

点击查询，会跳转到`findUserByPageServlet`，这个`Servlet`主要功能是查询总记录数据`findTotalCount()`和分页信息数据`findByPage()`，然后转发到前端做显示。

### :three:添加用户

点击添加联系人按钮，跳转到`add.jsp`页面，填写信息后点击提交，会将form表单填写的所有信息传递给`AddUserServlet`进行处理。

`AddUserServlet`主要操作：

1. 设置编码`setCharacterEncoding("utf-8")`
2. 获取所有请求参数`request.getParameterMap()`
3. 使用`BeanUtils.populate`将请求参数封装成User对象
4. 调用`service`的`addUser(User user)`方法完成保存
5. 跳转回查询界面

`UserService`业务层主要进行保存操作。

`UserDao`数据访问层，封装了对数据库的所有操作。

![title](https://raw.githubusercontent.com/zero6996/GitNote-images/master/GitNote/2019/06/23/3.%E6%B7%BB%E5%8A%A0%E5%8A%9F%E8%83%BD-1561276529181.bmp)

### :four:删除用户

点击删除按钮，会将这条数据的ID获取，封装在请求头里面传递给后台

`DelUserServlet`主要操作：

1. 获取要删除的用户ID，`getParameter`
2. 调用`service`的`delUser(String id)`方法完成删除
3. 跳转回查询页面

![title](https://raw.githubusercontent.com/zero6996/GitNote-images/master/GitNote/2019/06/23/4.%E5%88%A0%E9%99%A4%E5%8A%9F%E8%83%BD-1561276588764.bmp)



### :five:修改用户信息

修改用到了两个`Servlet`进行处理。首先点击修改按钮，会将用户ID传递随请求头传递给`findUserServlet`。

`findUserServlet`主要操作：

1. 获取用户ID
2. 根据ID查询用户信息`findUser(id)`，返回一个User对象
3. 将User对象存入request
4. 转发到`update.jsp`

`update.jsp`页面主要操作：

1. 从request域中获取User对象的数据
2. 使用EL表达式将所有数据回写到页面里。`${user.xxx}`
3. 将获取的ID值填入隐藏域值，以确定修改用户。`<input type="hidden" name="id" value="${user.id}">`
4. 点击提交，提交表单信息到`UpdateUserServlet`进行处理。

`UpdateUserServlet`主要操作：

1. 设置编码
2. 获取表单数据，并封装成User对象
3. 调用Service方法`updateUser(User user)`，将User对象传入，完成修改
4. 跳转回查询页面

![title](https://raw.githubusercontent.com/zero6996/GitNote-images/master/GitNote/2019/06/23/5.%E4%BF%AE%E6%94%B9%E5%8A%9F%E8%83%BD-1561276609416.bmp)



## :bar_chart:复杂功能



### :one:删除选中功能

点击删除选中按钮，主要问题就是要先获取选中的用户条目，在将这些选中条目的ID提交到后台进行批量删除操作。

#### 1.1 获取选中条目的ID，通过form表单提交到后台

```javascript
/*
为删除选中按钮绑定单击事件，提交每个条目复选框的值(该值就是条目的ID)
1. 给每一个条目添加checkbox,值是条目对应的ID
2. 获取当前页面的所有复选框标签，然后遍历，判断其是否有checked属性，有则允许提交表单
3. 提交form表单
*/
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
}
/*
    全选/全不选功能JS代码
    1. 在表头单元格添加一个复选框
    2. 获取该复选框标签对象
    3. 为该对象绑定单击事件，点击就获取所有的复选框对象，然后遍历，设置这些复选框的		checked=this.checked。(this:就是当前复选框对象,即'firstCB')
*/
document.getElementById("firstCB").onclick = function () {
    // 获取列表所有的cb
    var cbs = document.getElementsByName("uid");
    // 遍历
    for (var i = 0; i < cbs.length; i++) {
        // 设置这些cbs[i]的checked状态等于firstCB.checked
        cbs[i].checked = this.checked;
    }
}
```



####  1.2 后台`DelSelectedServlet`主要操作

1. 获取请求参数的ID数组。`getParameterValues()`
2. 调用`service`的`delUser(String[] ids)`进行批量删除。该方法会对id数组进行遍历，然后调用`dao`的`delUser(Int id)`方法操作数据库进行删除。
3. 全部删除完毕，跳转回查询页面。



![title](https://raw.githubusercontent.com/zero6996/GitNote-images/master/GitNote/2019/06/23/6.%E5%88%A0%E9%99%A4%E9%80%89%E4%B8%AD%E5%8A%9F%E8%83%BD-1561276629972.bmp)

### :two:分页查询

好处是可以减轻服务器内存的开销，并且提升用户体验



前端需要传递给后台的信息主要有两个，当前页码`currentPage`和每页显示条数`rows`

1. 当前页码`currentPage`：需要查询数据库得到总记录数`totalCount=select count(*) from user;`，然后通过三元运算符`totalCount % rows == 0 ? totalCount / rows:totalCount / rows + 1`计算出`totalPage`总页码。将总页码数返回给前端页面做进一步处理
2. 每页显示条数`rows`：自定义的条数。

当第一次访问`findUserByPageServlet`时，后台会做两次SQL查询，查询总记录数和当前分页数据，然后全部封装进`PageBean`对象，存入request返回。前端接受到这个对象，会在分页查询功能区遍历该对象，进行当前页码计算，以确定有多少分页。当用户点击不同分页时，会将当前页码返回给`findUserByPageServlet`做进一步的查询操作。

```jsp
<%--分页查询部分代码,遍历生成li,有多少页就会生成多少li--%>
<c:forEach begin="1" end="${userPage.totalPage}" var="i">
    <c:if test="${userPage.currentPage == i}"> <%--如果服务器返回的页码等于当前页码，则将按钮激活--%>
        <li class="active"><a href="${pageContext.request.contextPath}/findUserByPageServlet?currentPage=${i}&name=${condition.name[0]}&address=${condition.address[0]}&email=${condition.email[0]}">${i}</a></li>
    </c:if>
    <c:if test="${userPage.currentPage != i}"> <%--如果服务器返回的页码不等于当前页码，则正常显示--%>
        <li><a href="${pageContext.request.contextPath}/findUserByPageServlet?currentPage=${i}&name=${condition.name[0]}&address=${condition.address[0]}&email=${condition.email[0]}">${i}</a></li>
    </c:if>
</c:forEach>
```

![title](https://raw.githubusercontent.com/zero6996/GitNote-images/master/GitNote/2019/06/23/7.%E5%88%86%E9%A1%B5%E6%9F%A5%E8%AF%A2%E5%8A%9F%E8%83%BD-1561276688555.bmp)
`findUserByPageServlet`主要操作：

1. 接受请求参数，获取当前页码和每页显示条目数
2. 调用`service`的`findUserByPage(currentPage,rows,condition)`方法查询数据，返回`PageBean`对象，里面封装了当前分页数据对象。
3. 将`PageBean`对象存入request域中
4. 转发到`list.jsp`进行显示

`findUserByPage()`方法主要功能：

1. 创建一个空的`PageBean`对象
2. 设置当前页码属性和显示条目属性
3. 调用`dao`的`findTotalCount()`方法查询总记录数，并设置进`PageBean`
4. 计算开始索引`start = (currentPage - 1) * rows`，然后调用`dao`的`findByPage(start,rows)`方法查询当前分页数据集合。
5. 计算总页码数，并设置。
6. 返回`PageBean`对象



`findTotalCount`方法功能：

1. 定义模板初始化sql。`select count(*) from user where 1 = 1 `
2. 遍历条件map，获取其值
3. 如果值不为空或null，则拼接sql字符串，同时将对应值传入list集合。
4. 使用`queryForObject()`方法查询出总记录数，并返回

`findByPage`方法功能：

1. 定义初始化sql。
2. 遍历条件map，获取其值
3. 如果值不为空或null，则拼接sql字符串，同时将对应值传入list集合。
4. 将分页查询添加进sql字符串。`limit ?,?`
5. 将分页查询参数值添加入参数集合中。`params.add(start); params.add(rows);`
6. 使用`query`查询数据，返回分页数据对象集合。

![title](https://raw.githubusercontent.com/zero6996/GitNote-images/master/GitNote/2019/06/23/7_2.%E5%88%86%E9%A1%B5%E6%9F%A5%E8%AF%A2%E5%8A%9F%E8%83%BD2-1561276716927.bmp)



### :three:复杂条件查询

前端主要就是要将复杂条件查询的数据通过表单参数提交到后台，后台获取参数`map`后，调用方法查询数据，返回当前分页数据。将`PageBean`和条件参数map存入request，交给前端做进一步处理。

![title](https://raw.githubusercontent.com/zero6996/GitNote-images/master/GitNote/2019/06/23/%E5%A4%8D%E6%9D%82%E6%9D%A1%E4%BB%B6%E5%88%86%E9%A1%B5%E6%9F%A5%E8%AF%A2-1561276734854.bmp)

### :calendar:更新日志

#### 6月26号

完成了项目主体内容，前后端数据交互，可以进行复杂条件查询、分页查询、增删用户、批量删除等功能。



#### 6月27号

1. 学习了`Filter`过滤器，编写了过滤器实现了访问权限控制。即访问除了登录相关资源以外的页面或者资源，会先进行判断用户是否登录，登录则放行，反之则转发到登录页面。
2. 将分页查询到的用户数据，在前端显示的用户编号进行唯一操作。即查询到10个用户，第一页会显示编号`1~5`的用户，第二页显示`5~10`的用户。



#### 7月1号

将复杂条件查询搜索框整合为了一个，实现输入查询条件，显示符合条件的用户信息。编写了数据访问层DAO，增加了模糊查询数据，返回JavaBean对象的方法。