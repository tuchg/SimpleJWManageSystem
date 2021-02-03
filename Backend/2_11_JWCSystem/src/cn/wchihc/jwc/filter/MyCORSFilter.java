package cn.wchihc.jwc.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用于前端调试
 * 单页应用调试必须启用
 */
@WebFilter(filterName = "跨域过滤器", urlPatterns = "/*")
public class MyCORSFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse response = (HttpServletResponse) resp;
        // CORS "pre-flight" request
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods",
                "GET, POST");
        response.addHeader("Access-Control-Allow-Headers", "x-token,Content-Type");
        response.addHeader("Access-Control-Max-Age", "1800");// 30 min

        chain.doFilter(req, resp);
    }

}
