package cn.zero.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 登录过滤器,访问除了登录资源以外的所有资源，都必须先登录
 */
@WebFilter("/*")
public class LoginFilter implements Filter {
    public void destroy() {
    }
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        // 0. 强制转换
        HttpServletRequest request = (HttpServletRequest) req;
        // 1. 获取资源请求路径
        String uri = request.getRequestURI();
        // 2. 判断是否是登录相关的资源,要注意排除掉css/js/图片/验证码等资源
        if (uri.contains("/login.jsp") || uri.contains("loginServlet") || uri.contains("checkCodeServlet") || uri.contains("/css/") || uri.contains("/js/") || uri.contains("/fonts/")){
            // 是， 说明用户就是想登录，放行
            chain.doFilter(req,resp);
        }else {
            // 不是登录相关资源，需验证用户是否登录才能放行
            // 3. 从session中获取loginUser
            Object loginUser = request.getSession().getAttribute("loginUser");
            if(loginUser != null){
                // 登录过了，放行
                chain.doFilter(req,resp);
            }else {
                // 没有登录，跳转到登录页面
                request.setAttribute("login_msg","你尚未登录，请登录");
                request.getRequestDispatcher("/login.jsp").forward(request,resp);
            }
        }
        // 2. 判断当前用户是否登录，通过session值是否有user来判断
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
