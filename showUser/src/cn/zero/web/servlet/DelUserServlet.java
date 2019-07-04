package cn.zero.web.servlet;

import cn.zero.dao.Impl.UserDaoImpl;
import cn.zero.service.Impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/delUserServlet")
public class DelUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        UserServiceImpl service = new UserServiceImpl();
        int result = service.delUser(id);
        System.out.println(result);
        HttpSession session = request.getSession();
        if (result>0){
            // 删除成功,响应数据，重定向到list.jsp页面，显示成功
            session.setAttribute("del_success","删除成功");
            response.sendRedirect(request.getContextPath()+"/findUserByPageServlet");
        } else {
            // 删除失败，返回错误信息，
            session.setAttribute("del_error","删除失败,请重试");
            response.sendRedirect(request.getContextPath()+"/findUserByPageServlet");
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
