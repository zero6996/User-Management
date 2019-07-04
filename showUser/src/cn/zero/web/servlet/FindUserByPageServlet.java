package cn.zero.web.servlet;

import cn.zero.domain.PageBean;
import cn.zero.domain.User;
import cn.zero.service.Impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/findUserByPageServlet")
public class FindUserByPageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        // 1.获取请求参数：currentPage，rows
        String currentPage = request.getParameter("currentPage"); // 当前页码
        String rows = request.getParameter("rows"); // 每页显示条数
        String search = request.getParameter("search");
        System.out.println(search);
        String[] searchs = new String[1];
        // 如果用户点击index.jsp，或者数据越界，默认显示第一页用户数据
        if (currentPage == null || "".equals(currentPage) || Integer.parseInt(currentPage) <= 0){
            currentPage = 1 + "";
        }
        if (rows == null || "".equals(rows)){
            rows = 5 + "";
        }
//        rows = "5"; // 强制设定查询显示数据为5条

        // 获取条件查询参数
        Map<String, String[]> condition = request.getParameterMap();
        // 2. 调用Service查询PageBean
        UserServiceImpl service = new UserServiceImpl();
        PageBean<User> userPage = service.findUserByPage(Integer.parseInt(currentPage), Integer.parseInt(rows),condition);
//        System.out.println(userPage);
        // 3. 将PageBean存入request
        request.setAttribute("userPage",userPage);
        HashMap<String, String[]> map = new HashMap<>(condition);
        searchs[0] = search;
        System.out.println(searchs[0]);
        map.put("search",searchs);
//        String[] test1 = map.get("search");
//        System.out.println(test1[0]);
        request.setAttribute("condition",map); // 将查询条件存入request，方便回写数据
        // 4. 转发到list.jsp展示
        request.getRequestDispatcher("/list.jsp").forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
