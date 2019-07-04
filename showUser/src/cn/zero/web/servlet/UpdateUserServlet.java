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
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/updateUserServlet")
public class UpdateUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置编码格式
        request.setCharacterEncoding("utf-8");
        // 获取所有请求参数
        Map<String, String[]> map = request.getParameterMap();
        // 创建User对象
        User updateUser = new User();
        try {
            // 使用BeanUtils封装数据
            BeanUtils.populate(updateUser,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        UserServiceImpl service = new UserServiceImpl();
        int count = service.updateUser(updateUser);
        System.out.println(count);
        if (count >0) {
            // 修改成功，响应数据，重定向到userListServlet
            request.getSession().setAttribute("update_success",updateUser.getName()+"修改成功！");
            response.sendRedirect(request.getContextPath()+"/findUserByPageServlet");
        } else {
            // 添加失败，返回信息，转发到add.jsp
            request.setAttribute("update_error","修改失败,内容不能为空");
            request.getRequestDispatcher("/findUserServlet?name="+updateUser.getName()).forward(request, response);
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
