package cn.wchihc.jwc.servlets.admin.base;

import cn.wchihc.jwc.servlets.BaseRoleServlet;

/**
 * 本包下 限制admin访问
 */
public class AdminServlet extends BaseRoleServlet {

    public AdminServlet() {
        super("admin");
    }
}
