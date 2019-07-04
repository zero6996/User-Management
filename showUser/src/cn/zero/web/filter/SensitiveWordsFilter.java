package cn.zero.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * 敏感词汇过滤器
 */
@WebFilter("/*")
public class SensitiveWordsFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        // 1. 创建代理对象，增强getParameter方法
        ServletRequest proxy_req = (ServletRequest) Proxy.newProxyInstance(req.getClass().getClassLoader(), req.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 增强getParameter方法

                // 判断是否是getParameter方法
                if (method.getName().equals("getParameter")){
                    // 增强返回值
                    // 1. 获取返回值
                    String value = (String) method.invoke(req, args);
                    // 2. 判断不为空
                    if (value != null){
                        // 3. 遍历敏感词汇数组,判断返回值是否有敏感词
                        for (String str: list){
                            if (value.contains(str)){
                                // 有，则替换为***
                                value = value.replaceAll(str, "***");
                            }
                        }
                    }

                    return value;
                }
                // 判断方法名是否是 getParameterMap
                if (method.getName().equals("getParameterMap")){
                    // 增强其返回值
                    // 获取返回值
                    /*
                        request.getParameterMap()返回值map是一个不可更改的map,故可以将其转换为map集合在做修改
                        原因是：因为request.getParameterMap()返回的map继承了linkedhashmap，他有个boolean变量lock，
                              重写了put方法，当判断到lock为true时，就抛出了这个异常

                         其他解决方法：https://blog.csdn.net/yirentianran/article/details/2199852

                     */
                    Map<String, String[]> map = new HashMap<String, String[]>((Map<String,String[]>)method.invoke(req,args));
                    // map集合不为空
                    if (map != null){
                        for (String key:map.keySet()){
                            String[] values = map.get(key);
                            for (String str : list){
                                if (values[0].contains(str)){
                                    values[0] = values[0].replaceAll(str, "***");
                                }
                            }
                            map.put(key,values);
                        }
                    }
                    return map;
                }
                // 判断方法名是否是 getParameterValue
                return method.invoke(req,args);
            }
        });
        // 2. 放行
        chain.doFilter(proxy_req, resp);
    }
    private List<String> list = new ArrayList<>(); // 敏感词汇集合
    public void init(FilterConfig config) throws ServletException {
        try{
            // 1. 获取文件真实路径
            ServletContext servletContext = config.getServletContext();
            String realPath = servletContext.getRealPath("/WEB-INF/classes/SensitiveWords.txt");
            // 2. 读取文件
            BufferedReader br = new BufferedReader(new FileReader(realPath));
            // 3. 将文件的每一行数据添加到list中
            String line = null;
            while((line = br.readLine()) != null){
                list.add(line);
            }
            br.close();
            System.out.println(list);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void destroy() {

    }

}
