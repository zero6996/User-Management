package cn.zero.web.servlet;

import cn.zero.dao.Impl.UserDaoImpl;
import cn.zero.domain.User;
import cn.zero.service.Impl.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/addUserServlet")
public class AddUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置编码格式
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        // 获取所有请求参数
        Map<String, String[]> map = request.getParameterMap();
        // 创建User对象
        User addUser = new User();
        try {
            // 使用BeanUtils封装数据
            BeanUtils.populate(addUser,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        // 为什么BeanUtils封装完数据我的一列数据变成了null
        /*
        注意为什么会存储到数据库的内容为NULL？
        只有key与实体属性的名字一样才可以，这里的实体属性名字就是Html表单中对应的“name”与数据库表格里的对应列名 都要与key一致才行。
        因为原来我的性别实体属性是sex，而User表中是Gender，封装数据时找不到sex对应名称，故会有默认值null
         */
//        String gender = addUser.getGender();
//        System.out.println(gender);
        // 调用Service的添加方法保存用户
        UserServiceImpl service = new UserServiceImpl();
        int count = service.addUser(addUser);
        System.out.println(count);
        HttpSession session = request.getSession();
        if (count >0) {
            // 添加成功响应数据，重定向到userListServlet
            session.setAttribute("add_success",addUser.getName()+"添加成功");
            response.sendRedirect(request.getContextPath()+"/findUserByPageServlet");
        } else {
            // 添加失败，返回信息，转发到add.jsp
            request.setAttribute("add_error","添加失败,内容不能为空");
            request.getRequestDispatcher("/add.jsp").forward(request, response);
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
