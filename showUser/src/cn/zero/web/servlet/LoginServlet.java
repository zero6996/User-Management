package cn.zero.web.servlet;


import cn.zero.dao.Impl.UserDaoImpl;
import cn.zero.domain.AdminUser;
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

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置编码格式
//        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        // 获取数据
        Map<String, String[]> map = request.getParameterMap();
        // 创建User对象
        AdminUser adminUser = new AdminUser();
        try {
            // 使用BeanUtils封装数据
            BeanUtils.populate(adminUser,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        // 获取用户输入的验证码
        String checkCode = request.getParameter("verifycode");
        // 获取生成的验证码
        HttpSession session = request.getSession();
        String checkCode_session = (String) session.getAttribute("checkCode_session");
        if (checkCode_session == null || "".equals(checkCode_session)){
            request.setAttribute("cc_error","验证码错误！");
            // 跳转登录页面
            request.getRequestDispatcher("/login.jsp").forward(request,response);
        }
        session.removeAttribute("checkCode_session"); // 确保验证码的一次性
        // 校验验证码信息
        if (checkCode != null && checkCode_session.equalsIgnoreCase(checkCode)){
            // 验证码正确，开始登录
            UserDaoImpl dao = new UserDaoImpl();
            // 调用login方法
            AdminUser user = dao.login(adminUser);
            // 判断是否登录
            if (user != null) {
                // 登录成功，将用户存入session
                session.setAttribute("loginUser",user);
                // 响应数据，重定向到首页
                session.setAttribute("userName",user.getUser());
                response.sendRedirect(request.getContextPath()+"/index.jsp");
            } else {
                // 登录失败，返回信息，转发到login.jsp
                request.setAttribute("login_error","登录失败，用户名或密码错误");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        }else {
            // 验证码不正确
            // 提示信息
            request.setAttribute("cc_error","验证码错误！");
            // 跳转登录页面
            request.getRequestDispatcher("/login.jsp").forward(request,response);
        }

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
