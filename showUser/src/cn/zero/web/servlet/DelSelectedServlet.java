package cn.zero.web.servlet;

import cn.zero.service.Impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/delSelectedServlet")
public class DelSelectedServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 获取id数组
        String[] uids = request.getParameterValues("uid");
        // 2. 调用service删除
        UserServiceImpl service = new UserServiceImpl();
        service.delUser(uids);
        // 3. 重定向到查询所有的Servlet
        request.getSession().setAttribute("del_success","删除成功");
        response.sendRedirect(request.getContextPath()+"/findUserByPageServlet");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
