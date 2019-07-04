package cn.zero.web.servlet;

import cn.zero.domain.User;
import cn.zero.service.Impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/findUserServlet")
public class FindUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取用户id
        String id = request.getParameter("id");
        // 根据id查询用户信息User
        UserServiceImpl service = new UserServiceImpl();
        User user = service.findUser(id);
        // 将user对象存到request中
        request.setAttribute("user",user);
        // 转发到update.jsp
        request.getRequestDispatcher("/update.jsp").forward(request,response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
