package cn.wchihc.jwc.filter;

import cn.wchihc.jwc.utils.TokenPool;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 解析头部token字段并取出解析
 * 获取token对应的用户
 */
@WebFilter(filterName = "访问拦截器", urlPatterns = "/*")
public class AccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        String url = request.getRequestURI();

        // 放行静态资源及登录接口

        if (url.contains("/login") || !url.contains("/api")) {
            chain.doFilter(req, resp);
            return;
        }

        HttpServletResponse response = (HttpServletResponse) resp;
        String token = request.getHeader("X-Token");

        if (token != null) {
            String role = TokenPool.validateToken(token);
            if (role != null) {
                //解析当前 session用户组 用户ID
                request.getSession().setAttribute("user_id", TokenPool.getId(token));
                request.getSession().setAttribute("role", role);
                //  具体角色细分交给 BaseServlet 判定
                // 原因 解决各接口具体权限授予问题
                chain.doFilter(req, resp);
            } else {
                //权限异常
                response.setStatus(401);
            }
            return;
        }
//        response.setStatus(401);
        request.getRequestDispatcher("login").forward(req, response);
//        chain.doFilter(req, resp);
//        response.sendRedirect("login");
    }
}
