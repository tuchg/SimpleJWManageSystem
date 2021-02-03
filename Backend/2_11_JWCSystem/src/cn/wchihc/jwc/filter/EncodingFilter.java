package cn.wchihc.jwc.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "字符编码过滤器", urlPatterns = "/*")
public class EncodingFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        HttpServletRequest request = (HttpServletRequest) req;
        //将 api-url响应数据声明为json数据格式
        String url = request.getRequestURI();
        if (!url.contains(".") && !url.equals(request.getContextPath() + "/"))
            resp.setContentType("application/json;charset=utf-8");

        chain.doFilter(req, resp);
    }

}
